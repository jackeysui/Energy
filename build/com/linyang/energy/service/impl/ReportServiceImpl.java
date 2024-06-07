package com.linyang.energy.service.impl;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;

import com.leegern.util.StringUtil;
import com.linyang.energy.mapping.reportanalysis.ReportAnalysis4LostMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.energy.common.CommonResource;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.reportanalysis.ReportAnalysisMapper;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.ReportService;import com.linyang.energy.utils.DataUtil;
import com.linyang.util.DoubleUtils;

@Service
public class ReportServiceImpl implements ReportService{
	
	@Autowired
	private ReportAnalysisMapper reportAnalysisMapper;
	
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	
	@Autowired
	private ReportAnalysis4LostMapper reportAnalysis4LostMapper;

	@Override
	public Map<String, Object> search(Map<String, Object> param) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		int dataType = (Integer) param.get("dataType");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		//得到能管单元数据,能管单元不计算电压电流
		if(param.get("ledgerIdAry") != null && dataType != 5 && dataType != 6){
			//得到能源单元数据
			list = reportAnalysisMapper.getLedgerReportData(param);
			//日报-功率因素
			if (dataType == 4) {
				if(list != null && list.size() > 0){
					for (Map<String, Object> map : list) {
						double pf = DataUtil.getPF(((BigDecimal)map.get("A")).doubleValue(), ((BigDecimal)map.get("B")).doubleValue(), 3);
						map.put("A", pf);
					}
				}
			}else if(dataType == 11 || dataType == 16){
				//月报-综合能源
				return assembledMonGeneralData(dataType, param, list);
			}
		}
		//得到测量点数据--综合能源没有测量点
		if(param.get("meterIdAry") != null && dataType != 11 && dataType != 16){
			List<Map<String, Object>> meterList = reportAnalysisMapper.getMeterReportData(param);
			if(dataType == 7){
				assembledMonEleData(param, dataType, meterList);
			}else if(dataType == 8 || dataType == 9 || dataType == 10){
				assembledMonOtherData(param, dataType, meterList);
			}
			if(list != null) list.addAll(meterList);
		}
		
		if(dataType != 11){
			//补全数据
			result = completeData(param, list, dataType);
		}else {
			result.put("list",list);
		}
		
		return result;
	}
	
	
	/**
	 * 拼装月综合能源数据-仅限能管单元
	 * @param param
	 * @param ledgerList
	 * @return
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> assembledMonGeneralData(int dataType, Map<String, Object> param, List<Map<String, Object>> ledgerList) throws ParseException{
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		//月报-综合能源
		Date beginDate = (Date) param.get("beginDate");
		Date endDate = (Date) param.get("endDate");
		
		String[] ledgerIdAry = (String[]) param.get("ledgerIdAry");
		List<Long> ids = new ArrayList<Long>();
		for (String ledgerId : ledgerIdAry) {
			ids.add(Long.parseLong(ledgerId));
		}
		List<LedgerBean> ledgers = ledgerManagerMapper.getLedgerByIds(ids);
		
		TimeEnum dateType = TimeEnum.DAY;
		String partten = DateUtil.DEFAULT_SHORT_PATTERN;
		//年报，综合能源
		if(dataType == 16){
			dateType = TimeEnum.MONTH;
			partten = DateUtil.DEFAULT_SIMPLE_PATTERN;
		}
		
		ChartItemWithTime chartItem = new ChartItemWithTime(dateType, "", beginDate.getTime()/1000, endDate.getTime()/1000);
		SortedMap<String, Object> chartMap = chartItem.getMap();
		Iterator<Entry<String, Object>> itChart = chartMap.entrySet().iterator();
		while (itChart.hasNext()) {
			Entry<String, Object> entry = itChart.next();
			//建立一个空的EMOMAP
			Map<String, Object> defaultMap = new HashMap<String, Object>();
			for (LedgerBean ledger : ledgers) {
				Map<String, Object> unit = new HashMap<String, Object>();
				unit.put("OBJECTNAME", ledger.getLedgerName());
				defaultMap.put(ledger.getLedgerId().toString(), unit);
			}
			entry.setValue(defaultMap);			
		}
		
		//得到电水气热转换单位
		CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
		double elecUnit = new BigDecimal(resource.getElecUnit()).divide(new BigDecimal(1000), 7, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		double waterUnit = new BigDecimal(resource.getWaterUnit()).divide(new BigDecimal(1000), 7, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		double gasUnit = new BigDecimal(resource.getGasUnit()).divide(new BigDecimal(1000), 7, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		double hotUnit = new BigDecimal(resource.getHotUnit()).divide(new BigDecimal(1000), 7, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		for (Map<String, Object> map : ledgerList) {
			calcMonGeneralData(elecUnit, waterUnit, gasUnit, hotUnit, map);
			Date freezeTime = (Date)map.get("FREEZETIME");
			String timeStr = DateUtil.convertDateToStr(freezeTime, partten);
			Map<String,Object> objMap = (Map<String, Object>) chartMap.get(timeStr);
			//把从数据库查出的值放入map中
			objMap.put(((BigDecimal) map.get("OBJECTID")).toString(), map);
			chartMap.put(timeStr, objMap);
		}
		
		List<String> dates = new ArrayList<String>();
		Iterator<Entry<String, Object>> it = chartMap.entrySet().iterator();
		//第一次循环，KEY为时间
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			dates.add((String) entry.getKey());
			Map<String, Object> emoMap = (Map<String, Object>) entry.getValue();
			Iterator<Entry<String, Object>> ite = emoMap.entrySet().iterator();
			//日期
			String freezeTime = (String) entry.getKey();
			//第二次循环，KEY为能管单元，VALUE为具体的MAP
			while(ite.hasNext()){
				Entry<String, Object> entr = ite.next();
				Map<String, Object> map = (Map<String, Object>) entr.getValue();
				map.put("FREEZETIME", freezeTime);
				resultList.add(map);
			}
		}
		result.put("dates", dates);
		result.put("list",resultList);
		return result;
	}

	/**
	 * 计算电水气热转换折标煤的数据
	 * @param elecUnit
	 * @param waterUnit
	 * @param gasUnit
	 * @param hotUnit
	 * @param map
	 */
	private void calcMonGeneralData(double elecUnit, double waterUnit,
			double gasUnit, double hotUnit, Map<String, Object> map) {
		double a = 0f;
		//电量折标煤
		double aa = 0f;
		if(map.get("A") != null ){
			a = ((BigDecimal)map.get("A")).doubleValue();
			aa = DataUtil.retained(DataUtil.doubleMultiply(a, elecUnit), 2);
			map.put("AA", aa);
		}
		double c = 0f;
		//水量折标煤
		double cc =0f;
		if(map.get("C") != null ){
			c = ((BigDecimal)map.get("C")).doubleValue();
			cc = DataUtil.retained(DataUtil.doubleMultiply(c, waterUnit), 2);
			map.put("CC", cc);
		}
		double d = 0f;
		//气量折标煤
		double dd = 0f;
		if(map.get("D") != null ){
			d = ((BigDecimal)map.get("D")).doubleValue();
			dd = DataUtil.retained(DataUtil.doubleMultiply(d, gasUnit), 2);
			map.put("DD",dd);
		}
		double e = 0f;
		//热量折标煤
		double ee = 0f;
		if(map.get("E") != null ){
			e = ((BigDecimal)map.get("E")).doubleValue();
			ee = DataUtil.retained(DataUtil.doubleMultiply(e, hotUnit), 2);
			map.put("EE", ee);
		}
		map.put("TOTAL", DataUtil.retained(new BigDecimal(aa).add(new BigDecimal(cc)).add(new BigDecimal(dd)).add(new BigDecimal(ee)).doubleValue(), 2));//合计标准折标煤
	}

	/**
	 * 补全数据
	 * @param param
	 * @param list
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> completeData(Map<String, Object> param,
			List<Map<String, Object>> list, int dataType) throws ParseException {
		
		List<String> ledgerIdAry = null;
		List<String> meterIdAry = null;
		
		if(param.get("meterIdAry") != null)
			meterIdAry = (List<String>)param.get( "meterIdAry" );
//			meterIdAry = getList((String[])param.get( "meterIdAry" ));
		if(param.get("ledgerIdAry") != null)
			ledgerIdAry = getList((String[])param.get( "ledgerIdAry" ));
//			ledgerIdAry = (List<String>)param.get( "ledgerIdAry" );
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		TimeEnum dateType = null;
		String datePartten = "";
		if(dataType == 1){
			//电量
			dateType = TimeEnum.HOUR;
			datePartten = DateUtil.DEFAULT_FULL_PATTERN;
		}else if(dataType == 2 || dataType == 3 || dataType == 4 || dataType == 5 || dataType == 6){
			dateType = TimeEnum.MINUTE15;
			datePartten = DateUtil.DEFAULT_FULL_PATTERN;
		}else if(dataType == 7 || dataType == 8 || dataType == 9 || dataType == 10 ){
			dateType = TimeEnum.DAY;
			datePartten = DateUtil.DEFAULT_SHORT_PATTERN;
		}else if(dataType == 12 || dataType == 13 || dataType == 14 || dataType == 15 ){
			dateType = TimeEnum.MONTH;
			datePartten = DateUtil.DEFAULT_SIMPLE_PATTERN;
		}
		
		//存放每个对象的数据
		Map<Long, ChartItemWithTime> objMap = new LinkedHashMap<Long, ChartItemWithTime>();
		
		for (Map<String, Object> map : list) {
			Long objectId = ((BigDecimal) map.get("OBJECTID")).longValue();
			if(!objMap.containsKey(objectId)){
				//如果objMap没有存放该对象，新建一个对象
				Date beginDate = (Date) param.get("beginDate");
				Date endDate = (Date) param.get("endDate");
				if(dataType == 1){//电量，减一个小时
					Calendar cal = Calendar.getInstance();
					cal.setTime(endDate);
					cal.add(Calendar.HOUR_OF_DAY, -1);
					endDate = cal.getTime();
				}
				String objectName = (String) map.get("OBJECTNAME");
				String PTCT = "-";
				if(map.get("PTCT") != null ){
					PTCT = ((Integer) map.get("PTCT")).toString();
				}
				String ADDRESS = "-";
				if( map.get("ADDRESS") != null ){
					ADDRESS = (String) map.get("ADDRESS");
				}
				ChartItemWithTime chartItem = new ChartItemWithTime(dateType, objectName + "," + PTCT + "," + ADDRESS, beginDate.getTime()/1000, endDate.getTime()/1000);
				objMap.put(objectId, chartItem);
			}
			
			Date freezeTime = (Date)map.get("FREEZETIME");
			String timeStr = DateUtil.convertDateToStr(freezeTime, datePartten);
			//把生成的数据放入ChartItemWithTime中
			((ChartItemWithTime)objMap.get(objectId)).getMap().put(timeStr, map);
		}
		//存放最后的结果集合
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		//存放最后的日期列表集合
		List<String> dates = new ArrayList<String>();
		Iterator<Entry<Long, ChartItemWithTime>> objIt = objMap.entrySet().iterator();
		int i = 0;
		while(objIt.hasNext()){
			Entry<Long, ChartItemWithTime> entry = objIt.next();
			ChartItemWithTime item = entry.getValue();
			Long objId = entry.getKey();
			Iterator<Entry<String, Object>> it = item.getMap().entrySet().iterator();
			// add or update method by catkins.
			// date 2019/9/11
			// Modify the content: 
			if(meterIdAry != null && meterIdAry.contains( objId.toString() ))
				meterIdAry.remove( objId.toString() );
			if(ledgerIdAry != null && ledgerIdAry.contains( objId.toString() ))
				ledgerIdAry.remove( objId.toString() );
			//end
			while (it.hasNext()) {
				Entry<String, Object> entr = it.next();
				if(i == 0){
					if(dataType == 7 || dataType == 8 || dataType == 9 || dataType == 10){//月报，不显示年份
						dates.add(((String)entr.getKey()).substring(5,10));
					}else {
						dates.add((String)entr.getKey());
					}
					
				}
				Map<String, Object> map = null;
				try {
					map = (Map<String, Object>) entr.getValue();
				} catch (ClassCastException e) {
					String objInfo = item.getName();
					String[] objInfoAry = objInfo.split(",");
					map = new HashMap<String, Object>();
					map.put("OBJECTID", objId);
					map.put("OBJECTNAME", objInfoAry[0]);//OBJECTnAME
					map.put("PTCT", objInfoAry[1]);//存入PTCT
					map.put("ADDRESS", objInfoAry[2]);//存入ADDRESS
					map.put("FREEZETIME", entr.getKey());
					map.put("A", "-");
					map.put("B", "-");
					map.put("C", "-");
				}
				resultList.add(map);
			}
			i++;
		}
		
		// add or update method by catkins.
		// date 2019/9/11
		// Modify the content:
		
		if ( dates.size() > 0 ) {
//			if (ledgerIdAry != null) {
//				Date beginDate = (Date) param.get("beginDate");
//				Date endDate = (Date) param.get("endDate");
//				param.put( "ledgerIdAry" , ledgerIdAry );
//			}
			if (meterIdAry != null) {
				Date beginDate = (Date) param.get("beginDate");
				Date endDate = (Date) param.get("endDate");
				param.put( "meterIdAry" , meterIdAry );
				resultList.addAll( this.getMeterReportData4Lost( param,beginDate,endDate,dataType,meterIdAry ) );
			}
		}
		
		//end
		resultMap.put("dates",dates);
		resultMap.put("list",resultList);
		return resultMap;
	}
 
	/**
	 * 拼装月报-水气热数据
	 * @param param
	 * @param dataType
	 * @param list2
	 */
	private void assembledMonOtherData(Map<String, Object> param, int dataType,
			List<Map<String, Object>> list2) {
		//月报-水气热
		List<Long> ids = new ArrayList<Long>();
		
		for (Map<String, Object> map : list2) {
			Long objectId = ((BigDecimal) map.get("OBJECTID")).longValue();
			if(!ids.contains(objectId)){
				ids.add(objectId);
				Date beginDate = (Date) param.get("beginDate");
				Map<String, Object> beginMap = reportAnalysisMapper.getMeterReading(dataType, objectId, DateUtil.getSomeDateInYear(beginDate, -1));
				Double readA = null;
				if(beginMap != null && beginMap.get("A") != null){
					readA = ((BigDecimal)beginMap.get("A")).doubleValue();
				}
				map.put("READA", readA);
				
				Date endDate = (Date) param.get("endDate");
				Map<String, Object> endMap = reportAnalysisMapper.getMeterReading(dataType, objectId, DateUtil.getSomeDateInYear(endDate, -1));
				Double readC = null;
				if(endMap != null && endMap.get("A") != null ){
					readC = ((BigDecimal)endMap.get("A")).doubleValue();
				}
				map.put("READC", readC);
				
				if(readA != null && readC != null){
					double readE = DataUtil.retained(new BigDecimal(readC).subtract(new BigDecimal(readA)).doubleValue(), 5);
					map.put("READE", readE);
					map.put("TOTAL1", readE);
				}
			}
		}
	}
	

	/**
	 * 拼装月报-电量数据（测量点）
	 * @param param
	 * @param dataType
	 * @param list2
	 */
	private void assembledMonEleData(Map<String, Object> param, int dataType,
			List<Map<String, Object>> list) {
		//月报-电量
		List<Long> ids = new ArrayList<Long>();
		
		for (Map<String, Object> map : list) {
			Long objectId = ((BigDecimal) map.get("OBJECTID")).longValue();
			if(!ids.contains(objectId)){
				ids.add(objectId);
				Date beginDate = (Date) param.get("beginDate");
				double readA = 0f;
				double readB = 0f;
				
				int pt = ((BigDecimal)map.get("PT")).intValue();
				int ct = ((BigDecimal)map.get("CT")).intValue();
				map.put("PTCT", pt * ct);
				
				//得到初始表的读数
				Map<String, Object> beginMap = reportAnalysisMapper.getMeterReading(dataType, objectId, DateUtil.getSomeDateInYear(beginDate, -1));
				if(beginMap != null ){
					if(beginMap.get("A") != null ){
						readA = ((BigDecimal)beginMap.get("A")).doubleValue();
					}
					map.put("READA", readA);
					if(beginMap.get("B") != null ){
						readB = ((BigDecimal)beginMap.get("B")).doubleValue();
					}
					map.put("READB", readB);
				}
				
				Date endDate = (Date) param.get("endDate");
				//得到结束表的读数
				Map<String, Object> endMap = reportAnalysisMapper.getMeterReading(dataType, objectId, DateUtil.getSomeDateInYear(endDate, -1));
				if(endMap != null){
					double readC = 0f;
					if(endMap.get("A") != null ){
						readC = ((BigDecimal)endMap.get("A")).doubleValue();
					}
					map.put("READC", readC);
					
					double readD = 0f;
					if(endMap.get("B") != null ){
						readD = ((BigDecimal)endMap.get("B")).doubleValue();
					}
					map.put("READD", readD);
					
					double readE = DataUtil.retained(new BigDecimal(readC).subtract(new BigDecimal(readA)).doubleValue(), 5);
					double readF = DataUtil.retained(new BigDecimal(readD).subtract(new BigDecimal(readB)).doubleValue(), 5);
					map.put("READE", readE);
					map.put("READF", readF);
					
					map.put("TOTAL1", DataUtil.retained(new BigDecimal(readE).multiply(new BigDecimal(pt*ct)).doubleValue(), 2));
					map.put("TOTAL2", DataUtil.retained(new BigDecimal(readF).multiply(new BigDecimal(pt*ct)).doubleValue(), 2));	
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public void reportExcel(String str, ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param, String title) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
		List<String> dates = (List<String>) result.get("dates");
        int dataType = (Integer) param.get("dataType");
        int display = (Integer)param.get("display");
        HSSFWorkbook wb = null;
		switch (dataType) {
			case 1:
				//日报-正向有功电量，正向无功电量
				wb = createDayEleReport(list, title);
				break;
			case 2:
				//日报-有功功率
				wb = createDayPFReport(list, title);
				break;
			case 3:
				//日报-无功功率
				wb = createDayPFReport(list, title);
				break;
			case 4:
				//日报-功率因数
				wb = createDayPFReport(list, title);
				break;
			case 5:
				//日报-电流
				wb = createDayCurIVReport(list, title, dataType);
				break;
			case 6:
				//日报-电压
				wb = createDayCurIVReport(list, title, dataType);
				break;
			case 7:
				//月报-电
				wb = createMonEleReport(list, title, dates,display);
				break;
			case 8:
				//月报-水
				wb = createMonOtherReport(list, title, dates, dataType,display);
				break;
			case 9:
				//月报-气
				wb = createMonOtherReport(list, title, dates, dataType,display);
				break;
			case 10:
				//月报-热
				wb = createMonOtherReport(list, title, dates, dataType,display);
				break;
			case 11:
				//月报-综合能源
				wb = createMonGeneralReport(list, title, dates, dataType);
				break;
			case 12:
				//年报-电
				wb = createYearReport(list, title, dates, dataType);
				break;
			case 13:
				//年报-水
				wb = createYearReport(list, title, dates, dataType);
				break;
			case 14:
				//年报-气
				wb = createYearReport(list, title, dates, dataType);
				break;
			case 15:
				//年报-热
				wb = createYearReport(list, title, dates, dataType);
				break;
			case 16:
				//年报-综合能源
				wb = createMonGeneralReport(list, title, dates, dataType);
				break;
			default:
				break;
				
				
		}
		try {
			outputStream.flush();
            if(wb != null){wb.write(outputStream);}
			outputStream.close();
		} catch (IOException e) {
			Log.error("report excel - out put stream error!");
		}
	
	}
	
	/**
	 * 生成年报-电水气热
	 * @param list
	 * @param title
	 * @param dates
	 * @param dataType
	 * @return
	 */
	private HSSFWorkbook createYearReport(List<Map<String, Object>> list,
			String title, List<String> dates, int dataType) {
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = null;
		try{
			sheet = wb.createSheet(URLDecoder.decode(title, "UTF-8"));}
		catch(UnsupportedEncodingException ex){Log.error("EncodingException"+ex.getMessage());}
		//设置列的个数
		int columnLength = 3 + dates.size();
		for (int i = 0; i < columnLength; i++) {
			int width = 300;
			if(i == 1){
				width = 2* 300;
			}
			sheet.setColumnWidth(i,12*width);
		}
		
		//数据总数量
		int dataSize = list.size();
		//总行数，头部一行，一个对象一行
		int dataRow = dataType == 12?2 + list.size()/dates.size():1+list.size();
		
		HSSFRow[] rows = new HSSFRow[dataRow];
		
		//定义表头风格
		HSSFCellStyle thStyle = setThStyle(wb); 
		
		//第一行
		rows[0] = sheet.createRow(0);
		createCell(0,columnLength-1,rows[0],thStyle);
		//序号
		rows[0].getCell(0).setCellValue("序号");
		//采集点/能管单元
		rows[0].getCell(1).setCellValue("采集点/能管单元");
		
		//日期
		for (int i = 2; i < 2+dates.size(); i++) {
			rows[0].getCell(i).setCellValue(dates.get(i-2));
			//合计
			if( i+1 == 2+dates.size()){
				rows[0].getCell(i+1).setCellValue("合计");
			}
		}
		
		//定义数据样式
		HSSFCellStyle tdStyle = setTdStyle(wb);			
		
		//合计
		Map<String,Double> totalMap = new HashMap<String, Double>();
		
		Map<String, Double> dayMap = new LinkedHashMap<String, Double>();
		int j = 1;
		//从第二行开始
		for (int i = 0; i < dataSize; i++) {
			Map<String, Object> map = list.get(i);
			String objId = (String) map.get("OBJECTNAME");
			Double val = changeToDouble(map.get("A"));
			if(totalMap.containsKey(objId)){
				Double total = totalMap.get(objId);					
				totalMap.put(objId, DoubleUtils.getDoubleValue(new BigDecimal(total).add(new BigDecimal(val)).doubleValue(), 5));
			}else {
				totalMap.put(objId, DoubleUtils.getDoubleValue(val, 5));
			}
			
			Date freezeTime = null;
			String time = "";
			try {
				freezeTime = (Date) map.get("FREEZETIME");
				time = DateUtil.convertDateToStr(freezeTime, DateUtil.DEFAULT_SHORT_PATTERN);
			} catch (ParseException e) {
				time = (String)map.get("FREEZETIME");
			} catch (ClassCastException e) {
				time = (String)map.get("FREEZETIME");
			}
			
			time = time.substring(0, 7);
			
			//合计每天的数据
			if(dayMap.get(time) != null){
				double total = dayMap.get(time);
				dayMap.put(time, DoubleUtils.getDoubleValue(new BigDecimal(total).add(new BigDecimal(val)).doubleValue(), 5) );
			}else {
				dayMap.put(time, DoubleUtils.getDoubleValue(val, 5));
			}
			
			//每个对象的第一天
			if(i%dates.size() == 0){
				rows[j] = sheet.createRow(j);
				createCell(0,columnLength-1,rows[j],tdStyle);
				//序号
				//以字符串格式输出避免小数问题
				Integer num = j;
				rows[j].getCell(0).setCellValue(num.toString());
				//采集点/能管单元
				rows[j].getCell(1).setCellValue(checkIsNull(map.get("OBJECTNAME")));
			}
			rows[j].getCell(i%dates.size() + 2).setCellValue(checkIsNull2(map.get("A")));
			
			if(i%dates.size() == dates.size()-1 ){
				rows[j].getCell(i%dates.size() + 3).setCellValue(totalMap.get(objId));
				j++;
			}
		}
		if(dataType == 12){
			//合计行
			rows[j] = sheet.createRow(j);
			createCell(0, columnLength-1, rows[j], tdStyle);
			for (int k = 0; k < 2; k++) {
				if(k == 1){
					rows[j].getCell(k).setCellValue("合计");
				}else {
					rows[j].getCell(k).setCellValue("-");
				}
			}
			
			int n = 2;
			for ( Map.Entry<String, Double> entry : dayMap.entrySet()) {
				rows[j].getCell(n).setCellValue(entry.getValue());
				n++;
			}
			rows[j].getCell(n).setCellValue("-");
		}
		return wb;
	}
	
	/**
	 * 转化为数字
	 * @param obj
	 * @return
	 */
	private Double changeToDouble(Object obj){
		if(obj == null) {
			return 0D;
		}else if(obj.toString().equals("-")){
			return 0D;
		}
		return Double.parseDouble(obj.toString());
	}

	/**
	 * 生成月报-综合能源
	 * @param list
	 * @param title
	 * @param dates
	 * @param dataType
	 * @return
	 */
	private HSSFWorkbook createMonGeneralReport(List<Map<String, Object>> list,
			String title, List<String> dates, int dataType) {
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			HSSFSheet sheet = wb.createSheet(URLDecoder.decode(title, "UTF-8"));

			//设置列宽
			for (int i = 0; i < 11; i++) {
				int width = 256;
				if(i == 1){
					width = 256 * 2;
				}
				sheet.setColumnWidth(i,12*width);
			}
			
			//总行数
			int dataRow = list.size() + 2;
			
			HSSFRow[] rows = new HSSFRow[dataRow];
			
			//定义表头风格
			HSSFCellStyle thStyle = setThStyle(wb);
			
			//第一行
			rows[0] = sheet.createRow(0);
			createCell(0, 10, rows[0], thStyle);
			//合并单元格-----时间
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			rows[0].getCell(0).setCellValue("时间");
			
			//能管单元
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
			rows[0].getCell(1).setCellValue("能管单元");
			
			//电
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
			rows[0].getCell(2).setCellValue("电");
			
			//水
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
			rows[0].getCell(4).setCellValue("水");
			
			//气
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 7));
			rows[0].getCell(6).setCellValue("气");
			
			//热
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 9));
			rows[0].getCell(8).setCellValue("热");
			
			//热
			rows[0].getCell(10).setCellValue("合计");
			
			//第二行
			rows[1] = sheet.createRow(1);
			createCell(0, 10, rows[1], thStyle);
			rows[1].getCell(2).setCellValue("电量(kWh)");
			rows[1].getCell(3).setCellValue("折标煤");
			rows[1].getCell(4).setCellValue("水量(m³)");
			rows[1].getCell(5).setCellValue("折标煤");
			rows[1].getCell(6).setCellValue("气量(m³)");
			rows[1].getCell(7).setCellValue("折标煤");
			rows[1].getCell(8).setCellValue("热量(kWh)");
			rows[1].getCell(9).setCellValue("折标煤");
			rows[1].getCell(10).setCellValue("折标煤");
			
			//EMO总数
			int emoNum = list.size() / dates.size();
			
			//定义数据样式
			HSSFCellStyle tdStyle = setTdStyle(wb);
			
			int j = 2;
			//从第四行开始
			for (int i = 0; i < list.size() ; i++) {
				Map<String, Object> map = list.get(i);
				rows[j] = sheet.createRow(j);
				createCell(0, 10, rows[j], tdStyle);
				//每天的第一个对象
				if(i%emoNum == 0){
					sheet.addMergedRegion(new CellRangeAddress(j, j+emoNum-1, 0, 0));
					rows[j].getCell(0).setCellValue((String)map.get("FREEZETIME"));
				}
				//采集点/能管单元
				rows[j].getCell(1).setCellValue(checkIsNull(map.get("OBJECTNAME")));
				rows[j].getCell(2).setCellValue(checkIsNull2(map.get("A")));
				rows[j].getCell(3).setCellValue(checkIsNull2(map.get("AA")));
				rows[j].getCell(4).setCellValue(checkIsNull2(map.get("C")));
				rows[j].getCell(5).setCellValue(checkIsNull2(map.get("CC")));
				rows[j].getCell(6).setCellValue(checkIsNull2(map.get("D")));
				rows[j].getCell(7).setCellValue(checkIsNull2(map.get("DD")));
				rows[j].getCell(8).setCellValue(checkIsNull2(map.get("E")));
				rows[j].getCell(9).setCellValue(checkIsNull2(map.get("EE")));
				rows[j].getCell(10).setCellValue(checkIsNull2(map.get("TOTAL")));
				j++;
			}
		} catch (UnsupportedEncodingException e) {
			Log.info("createMonGeneralReport error UnsupportedEncodingException");
		}
		return wb;
	}

	/**
	 * 生成日报-正向有功电量，正向无功电量报表
	 * @param list
	 */
	private HSSFWorkbook createDayEleReport(List<Map<String, Object>> list, String title) {
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
            HSSFSheet sheet = wb.createSheet(URLDecoder.decode(title, "UTF-8"));

			//设置列宽
			for (int i = 0; i < 16; i++) {
				int width = 256;
				if(i == 0){
					width = 256 * 2;
				}
				sheet.setColumnWidth(i,12*width);
			}
			
			//数据总数量
			int dataSize = list.size();
			//总行数，12个数据为一行
			int dataRow = 2 +dataSize/6;
			
			HSSFRow[] rows = new HSSFRow[dataRow];
			
			//定义表头风格
			HSSFCellStyle thStyle = setThStyle(wb);
			//第一行
			rows[0] = sheet.createRow(0);
			createCell(0, 15, rows[0], thStyle);
			//合并单元格-----采集点/能管单元
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
			rows[0].getCell(0).setCellValue("采集点/能管单元");
			//时间 从1:00到12:00
			for (int i = 4; i < 16; i++) {
				rows[0].getCell(i).setCellValue( i-3 + ":00");
			}
			
			//定义数据样式
			HSSFCellStyle tdStyle = setTdStyle(wb);
			
			//从第三行开始
			int j = 1;
			for (int i = 0; i < dataSize; i++) {
				Map<String, Object> map = list.get(i);
				//一天里的第一个数据
				if(i%24 == 0 && i%12 ==0){
					rows[j] = sheet.createRow(j);
					createCell(0, 15, rows[j], tdStyle);
					rows[j+2] = sheet.createRow(j+2);
					createCell(0, 15, rows[j+2], tdStyle);
					sheet.addMergedRegion(new CellRangeAddress(j, j+3, 0, 0));//合并单元格---具体的对象
					sheet.addMergedRegion(new CellRangeAddress(j, j+3, 1, 1));//合并单元格---表地址
			        sheet.addMergedRegion(new CellRangeAddress(j, j+1, 2, 2));//合并单元格---有功
			        sheet.addMergedRegion(new CellRangeAddress(j+2, j+3, 2, 2));//合并单元格---无功
			        
			        rows[j].getCell(0).setCellValue(checkIsNull(map.get("OBJECTNAME")));
			        rows[j].getCell(1).setCellValue(checkIsNull(map.get("ADDRESS")));
			        rows[j].getCell(2).setCellValue("有功");
			        rows[j].getCell(3).setCellValue("AM");
			        rows[j+2].getCell(2).setCellValue("无功");
			        rows[j+2].getCell(3).setCellValue("AM");
			        
				}else if(i%24 == 12){
					//一天里下午的第一个数据；
					rows[j+1] = sheet.createRow(j+1);
					createCell(0, 15, rows[j+1], tdStyle);
					rows[j+3] = sheet.createRow(j+3);
					createCell(0, 15, rows[j+3], tdStyle);
					
					rows[j+1].getCell(3).setCellValue("PM");
					rows[j+3].getCell(3).setCellValue("PM");
				};
				
				if(i % 24 < 12){
					//AM数据
					rows[j].getCell(i%12+4).setCellValue(checkIsNull(map.get("A"))); 
					rows[j+2].getCell(i%12+4).setCellValue(checkIsNull(map.get("B"))); 
				}else if(i % 24 > 12 || i % 24 == 12){
					//PM数据
					rows[j+1].getCell(i%12+4).setCellValue(checkIsNull(map.get("A"))); 
					rows[j+3].getCell(i%12+4).setCellValue(checkIsNull(map.get("B")));
				}
				//一天里最后一个数据
				if(i % 24 == 23){
					j = j+4;//一天的数据生成4行
				}
			}
		} catch (UnsupportedEncodingException e) {Log.error(this.getClass() + "decode error!");}
		return wb;
	}
	

	/**
	 * 生成日报-功率因数-有功功率，无功功率excel
	 * @param list
	 * @return
	 */
	private HSSFWorkbook createDayPFReport(List<Map<String, Object>> list, String title) {
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			HSSFSheet sheet = wb.createSheet(title);

			int colCount = 96;
			
			//设置列宽
			for (int i = 0; i < colCount; i++) {
				int width = 12;
				if(i == 0){
					width = 24;
				}
				sheet.setColumnWidth(i,width*256);
			}
			
			//数据总数量
			int dataSize = list.size();
			//总行数，96个数据为一行
			int dataRow = 1 +dataSize/96;
			
			HSSFRow[] rows = new HSSFRow[dataRow];
			
			//定义表头风格
			HSSFCellStyle thStyle = setThStyle(wb);
			
			//第一行
			rows[0] = sheet.createRow(0);
			createCell(0, colCount, rows[0], thStyle);
			rows[0].getCell(0).setCellValue("采集点/能管单元");
			
			//时间 从0:00到23:45分
			for (int i = 0; i < colCount; i++) {
				String timeStr = "";
				if( i % 4 == 0){
					timeStr = ":00";
				}else if(i % 4 == 1){
					timeStr = ":15";
				}else if(i % 4 == 2){
					timeStr = ":30";
				}else if(i % 4 == 3){
					timeStr = ":45";
				}
				rows[0].getCell(i+1).setCellValue( i/4 +""+ timeStr );
			}
			
			//定义数据样式
			HSSFCellStyle tdStyle = setTdStyle(wb);
			
			//从第二行开始
			int j = 1;
			for (int i = 0; i < dataSize; i++) {
				Map<String, Object> map = list.get(i);
				//一天里的第一个数据
				if(i%96 == 0){
					rows[j] = sheet.createRow(j);
					createCell(0, colCount, rows[j], tdStyle);
					rows[j].getCell(0).setCellValue(checkIsNull(map.get("OBJECTNAME")));
				}
				rows[j].getCell(i%96 + 1).setCellValue(checkIsNull(map.get("A"))); 
				//一天里最后一个数据
				if(i % 96 == 95){
					j++;
				}
			}
		} catch (RuntimeException e) {
			Log.info("createDayPFReport error UnsupportedEncodingException");
		}
		return wb;
	}
	
	/**
	 * 生成日报-电流,电压
	 * @param list
	 * @param title
	 * @return
	 */
	private HSSFWorkbook createDayCurIVReport(List<Map<String, Object>> list, String title, int dataType) {
		// 声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = null;
		try {
			sheet = wb.createSheet(URLDecoder.decode(title, "UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			Log.error("EncodingException" + ex.getMessage());
		}

		int colCount = 97;

		// 设置列宽
		for (int i = 0; i < 97; i++) {
			int width = 256;
			if (i == 0) {
				width = 256 * 2;
			}
			sheet.setColumnWidth(i, 12 * width);
		}

		// 数据总数量
		int dataSize = list.size();
		// 总行数，96个数据为3行
		int dataRow = 1 + dataSize * 3 / 96;

		HSSFRow[] rows = new HSSFRow[dataRow];
		// 定义表头风格
		HSSFCellStyle thStyle = setThStyle(wb);

		// 第1行
		rows[0] = sheet.createRow(0);
		createCell(0, colCount, rows[0], thStyle);
		// 合并单元格-----采集点/能管单元
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
		rows[0].getCell(0).setCellValue("采集点/能管单元");

		// 时间 从0:00到23:45分
		for (int i = 0; i < 96; i++) {
			String timeStr = "";
			if (i % 4 == 0) {
				timeStr = ":00";
			} else if (i % 4 == 1) {
				timeStr = ":15";
			} else if (i % 4 == 2) {
				timeStr = ":30";
			} else if (i % 4 == 3) {
				timeStr = ":45";
			}
			rows[0].getCell(i + 2).setCellValue(i / 4 + "" + timeStr);
		}

		// 定义数据样式
		HSSFCellStyle tdStyle = setTdStyle(wb);

		// 从第2行开始
		int j = 1;
		for (int i = 0; i < dataSize; i++) {
			Map<String, Object> map = list.get(i);
			// 一天里的第一个数据
			if (i % 96 == 0) {
				rows[j] = sheet.createRow(j);
				createCell(0, colCount, rows[j], tdStyle);
				rows[j + 1] = sheet.createRow(j + 1);
				createCell(0, colCount, rows[j + 1], tdStyle);
				rows[j + 2] = sheet.createRow(j + 2);
				createCell(0, colCount, rows[j + 2], tdStyle);

				sheet.addMergedRegion(new CellRangeAddress(j, j + 2, 0, 0));// 合并单元格---具体的对象

				rows[j].getCell(0).setCellValue(checkIsNull(map.get("OBJECTNAME")));
				rows[j].getCell(1).setCellValue("A相");
				rows[j + 1].getCell(1).setCellValue("B相");
				rows[j + 2].getCell(1).setCellValue("C相");
			}

			rows[j].getCell(i % 96 + 2).setCellValue(checkVolData(map.get("A"), map.get("COMMMODE"), dataType));
			rows[j + 1].getCell(i % 96 + 2).setCellValue(checkVolData(map.get("B"), map.get("COMMMODE"), dataType));
			rows[j + 2].getCell(i % 96 + 2).setCellValue(checkVolData(map.get("C"), map.get("COMMMODE"), dataType));

			// 一天里最后一个数据
			if (i % 96 == 95) {
				j = j + 3;// 一天的数据生成3行
			}
		}

		return wb;
	}
	
	/**  
     * 生成单元格
     * @param sheet  
     * @param region  
     * @param cs  
     */    
    private void createCell(int start, int end, HSSFRow row,    HSSFCellStyle style) {    
        for(int i=start;i<=end;i++){       
            HSSFCell cell = row.createCell(i);       
            cell.setCellValue("");       
            cell.setCellStyle(style);       
        }    
    }   
	
	/**
	 * 月报-电量
	 * @param list
	 * @param title
	 * @param dates
	 * @return
	 */
	private HSSFWorkbook createMonEleReport(List<Map<String, Object>> list,
			String title, List<String> dates,int display) {

		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet=null;
		try {
			try{
				sheet = wb.createSheet(URLDecoder.decode(title, "UTF-8"));}
			catch(UnsupportedEncodingException ex){Log.error("EncodingException"+ex.getMessage());}
			//是否显示基本信息
			int displayWidth = display ==1 ? 13:3;
			
			//设置列宽
			int columnLength = displayWidth + dates.size();
			for (int i = 0; i < columnLength; i++) {
				int width = 256;
				if(i == 1){
					//对象列
					width = 2 * 256;
				}
				sheet.setColumnWidth(i,12*width);
			}
			
			//数据总数量
			int dataSize = list.size();
			//总行数，头部三行，一个对象一行
			int dataRow = 3 + list.size()/dates.size();			
			HSSFRow[] rows = new HSSFRow[dataRow];			
			//定义表头风格
			HSSFCellStyle thStyle = setThStyle(wb);			
			//第一行
			rows[0] = sheet.createRow(0);
			//生成单元格
			createCell(0, columnLength-1, rows[0], thStyle);			
			//合并单元格-----序号
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			rows[0].getCell(0).setCellValue("序号");
			//采集点/能管单元
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
			rows[0].getCell(1).setCellValue("采集点/能管单元");
			
			if(display == 1){
				//表地址
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
				rows[0].getCell(2).setCellValue("表地址");
				//倍率
				rows[0].getCell(3).setCellValue("倍率");
				
				//有功功率
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 7));
				rows[0].getCell(4).setCellValue("正向有功电量");
				
				//无功功率
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 11));
				rows[0].getCell(8).setCellValue("正向无功电量");
			}
			
			int dateDisplay = display == 1? 12:2;
			//日期
			for (int i = dateDisplay; i < dateDisplay+dates.size(); i++) {
				sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
				rows[0].getCell(i).setCellValue(dates.get(i-dateDisplay));
			}
			//合计
			sheet.addMergedRegion(new CellRangeAddress(0, 1, dateDisplay+dates.size(), dateDisplay+dates.size()));
			rows[0].getCell(dateDisplay+dates.size()).setCellValue("合计");
			
			//第二行
			rows[1] = sheet.createRow(1);
			createCell(0, columnLength-1, rows[1], thStyle);
			
			if(display == 1){
				//PT*CT
				rows[1].getCell(3).setCellValue("(PT*CT)");
				rows[1].getCell(4).setCellValue("初始读数");
				rows[1].getCell(5).setCellValue("结束读数");
				rows[1].getCell(6).setCellValue("读数差");
				rows[1].getCell(7).setCellValue("电量(度)");
				rows[1].getCell(8).setCellValue("初始读数");
				rows[1].getCell(9).setCellValue("结束读数");
				rows[1].getCell(10).setCellValue("读数差");
				rows[1].getCell(11).setCellValue("电量(度)");
			}
			
			//定义数据样式
			HSSFCellStyle tdStyle = setTdStyle(wb);
			
			int j = 2;
			Map<Long,Double> monMap = new HashMap<Long, Double>();
			Map<String, Double> dayMap = new LinkedHashMap<String, Double>();
			//从第四行开始
			for (int i = 0; i < dataSize; i++) {
				Map<String, Object> map = list.get(i);
				Long objectId = 0L;
				try {
					objectId = ((BigDecimal) map.get("OBJECTID")).longValue();
				} catch (ClassCastException e) {
					objectId = (Long) map.get("OBJECTID");
				}
				double val = 0;
				try {
					val = ((BigDecimal) map.get("A")).doubleValue();
				} catch (Exception e) {
					val = 0;
				}
				String time = "";
				try {
					Date freezeTime = (Date) map.get("FREEZETIME");
					try {
						time = DateUtil.convertDateToStr(freezeTime, DateUtil.DEFAULT_SHORT_PATTERN);
					} catch (ParseException e) {
						Log.error("createMonEleReport error ParseException"+e.getMessage());
					}
				} catch (ClassCastException e) {
					time = (String) map.get("FREEZETIME");
				}
				
				
				//合计每天的数据
				if(dayMap.get(time) != null){
					double total = dayMap.get(time);
					dayMap.put(time, new BigDecimal(total).add(new BigDecimal(val)).doubleValue());
				}else {
					dayMap.put(time, val);
				}
				
				//每个对象的第一天
				if(i%dates.size() == 0){
					rows[j] = sheet.createRow(j);
					createCell(0, columnLength-1, rows[j], tdStyle);
					//以字符串格式输出避免小数问题
					Integer num = j - 1;
					rows[j].getCell(0).setCellValue(num.toString());
					rows[j].getCell(1).setCellValue(checkIsNull(map.get("OBJECTNAME")));
					if(display == 1){
						rows[j].getCell(2).setCellValue(checkIsNull2(map.get("ADDRESS")));
						rows[j].getCell(3).setCellValue(checkIsNull2(map.get("PTCT")));
						rows[j].getCell(4).setCellValue(checkIsNull2(map.get("READA")));
						rows[j].getCell(5).setCellValue(checkIsNull2(map.get("READC")));
						rows[j].getCell(6).setCellValue(checkIsNull2(map.get("READE")));
						rows[j].getCell(7).setCellValue(checkIsNull2(map.get("TOTAL1")));
						rows[j].getCell(8).setCellValue(checkIsNull2(map.get("READB")));
						rows[j].getCell(9).setCellValue(checkIsNull2(map.get("READD")));
						rows[j].getCell(10).setCellValue(checkIsNull2(map.get("READF")));
						rows[j].getCell(11).setCellValue(checkIsNull2(map.get("TOTAL2")));
					}
					monMap.put(objectId, val);
				}else {
					double total = monMap.get(objectId);
					monMap.put(objectId, new BigDecimal(total).add(new BigDecimal(val)).doubleValue());
				}
                String aValue = "-";
                if(map.keySet().contains("A")){
                    aValue = checkIsNull(map.get("A"));
                }
				rows[j].getCell(i%dates.size() + dateDisplay).setCellValue(aValue);
				if(i%dates.size() == dates.size()-1 ){
					rows[j].getCell(columnLength-1).setCellValue(monMap.get(objectId));
					j++;
				}
			}
			//合计行
			rows[j] = sheet.createRow(j);
			createCell(0, columnLength-1, rows[j], tdStyle);
			for (int k = 0; k < displayWidth; k++) {
				if(k == 1){
					rows[j].getCell(k).setCellValue("合计");
				}else {
					rows[j].getCell(k).setCellValue("-");
				}
			}
			
			int n = dateDisplay;
			for ( Map.Entry<String, Double> entry : dayMap.entrySet()) {
				rows[j].getCell(n).setCellValue(entry.getValue());
				n++;
			}
			rows[j].getCell(n).setCellValue("-");
			
		} catch (Exception e) {
			Log.error("createMonEleReport error Exception");
		}
		return wb;
	}
	
	/**
	 * 月报-水气热
	 * @param list
	 * @param title
	 * @param dates
	 * @param dataType 
	 * @return
	 */
	private HSSFWorkbook createMonOtherReport(List<Map<String, Object>> list, String title, List<String> dates,
			int dataType, int display) {
		String typeName = "";
		String typeUnit = "";
		switch (dataType) {
		case 8:
			typeName = "水";
			typeUnit = "水量（m3）";
			break;
		case 9:
			typeName = "气";
			typeUnit = "气量（m3）";
			break;
		case 10:
			typeName = "热";
			typeUnit = "热量（kWh）";
			break;
		default:
			break;
		}

		// 声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = null;
		try {
			sheet = wb.createSheet(URLDecoder.decode(title, "UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			Log.error("EncodingException" + ex.getMessage());
		}

		// 设置列宽
		int columnLength = 6 + dates.size();
		for (int i = 0; i < columnLength; i++) {
			int width = 256;
			if (i == 1) {
				width = 2 * 256;
			}
			sheet.setColumnWidth(i, 12 * width);
		}

		// 数据总数量
		int dataSize = list.size();
		// 总行数，头部三行，一个对象一行
		int dataRow = 3 + list.size() / dates.size();

		HSSFRow[] rows = new HSSFRow[dataRow];

		// 定义表头风格
		HSSFCellStyle thStyle = setThStyle(wb);

		// 第一行
		rows[0] = sheet.createRow(0);
		createCell(0, columnLength - 1, rows[0], thStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		rows[0].getCell(0).setCellValue("序号");
		// 采集点/能管单元
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		rows[0].getCell(1).setCellValue("采集点/能管单元");
		// 有功功率
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));
		rows[0].getCell(2).setCellValue(typeName + "量");

		// 日期
		for (int i = 6; i < 6 + dates.size(); i++) {
			sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
			rows[0].getCell(i).setCellValue(dates.get(i - 6));
		}

		// 第二行
		rows[1] = sheet.createRow(1);
		createCell(0, columnLength - 1, rows[1], thStyle);
		rows[1].getCell(2).setCellValue("初始读数");
		rows[1].getCell(3).setCellValue("结束读数");
		rows[1].getCell(4).setCellValue("读数差");
		rows[1].getCell(5).setCellValue(typeUnit);

		// 定义数据样式
		HSSFCellStyle tdStyle = setTdStyle(wb);

		int j = 2;
		// 从第四行开始
		for (int i = 0; i < dataSize; i++) {
			Map<String, Object> map = list.get(i);
			// 每个对象的第一天
			if (i % dates.size() == 0) {
				rows[j] = sheet.createRow(j);
				createCell(0, columnLength - 1, rows[j], tdStyle);
				// 序号
				// 以字符串格式输出避免小数问题
				Integer num = j - 1;
				rows[j].getCell(0).setCellValue(num.toString());
				// 采集点/能管单元
				rows[j].getCell(1).setCellValue(checkIsNull(map.get("OBJECTNAME")));
				rows[j].getCell(2).setCellValue(checkIsNull2(map.get("READA")));
				// 有功功率结束读数
				rows[j].getCell(3).setCellValue(checkIsNull2(map.get("READC")));
				// 有功功率读数差
				rows[j].getCell(4).setCellValue(checkIsNull2(map.get("READE")));
				// 有功功率电量
				rows[j].getCell(5).setCellValue(checkIsNull2(map.get("TOTAL1")));
			}
			rows[j].getCell(i % dates.size() + 6).setCellValue(checkIsNull2(map.get("A")));

			if (i % dates.size() == dates.size() - 1) {
				j++;
			}
		}
		return wb;
	}

	/**
	 * 定义表头风格
	 * @param wb
	 */
	private HSSFCellStyle setThStyle(HSSFWorkbook wb) {
		HSSFCellStyle titlestyle = wb.createCellStyle();
        titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);     
        titlestyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        titlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
        titlestyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
        titlestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
        titlestyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
        titlestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.WHITE.index);
        titlestyle.setFont(font);
        return titlestyle;
	}
	
	/**
	 * 定义数据样式
	 * @param wb
	 * @return
	 */
	private HSSFCellStyle setTdStyle(HSSFWorkbook wb){
		//定义具体表格内容样式
	    HSSFCellStyle style_ =wb.createCellStyle();
	    HSSFDataFormat df = wb.createDataFormat();
	    style_.setDataFormat(df.getFormat("0.00###"));//支持5位
	    style_.setRightBorderColor(HSSFColor.BLACK.index);
	    style_.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    style_.setLeftBorderColor(HSSFColor.BLACK.index);
	    style_.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    style_.setTopBorderColor(HSSFColor.BLACK.index);
	    style_.setBorderTop(HSSFCellStyle.BORDER_THIN);
	    style_.setBottomBorderColor(HSSFColor.BLACK.index);
	    style_.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    style_.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	    style_.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	    return style_;
	}
	
	/**
	 * 判断是否为空
	 * @param obj
	 * @return
	 */
	private String checkIsNull(Object obj) {
		if(obj == null) {
			return "";
		}
		return obj.toString();
	}
	
	/**
	 * 判断是否为空
	 * @param obj
	 * @return
	 */
	private String checkVolData(Object obj, Object commMode, int dataType) {
		if(obj == null) {
			return "";
		}else if(commMode == null){
			return obj.toString();
		}else if(dataType == 6 && ( ((BigDecimal)commMode).intValue() == 2 ||  ((BigDecimal)commMode).intValue() == 3) ){
			return Math.round(((BigDecimal)obj).multiply(new BigDecimal(1.732)).floatValue()) + "";
		}
		return obj.toString();
	}
	
	/**
	 * 判断是否为空
	 * @param obj
	 * @return
	 */
	private String checkIsNull2(Object obj) {
		if(obj == null) {
			return "-";
		}
		return obj.toString();
	}
	
	/**
	 * 判断是否为空
	 * @param obj
	 * @return
	 */
	private String checkIsNull3(Object obj) {
		if(obj == null) {
			return "";
		}
		return Math.round(Double.parseDouble(obj.toString()))+"";
	}


	@Override
	public Map<String, Object> getMeterTypeMap(Long ledgerId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean hasEle = false;
		boolean hasWater = false;
		boolean hasGas = false;
		boolean hasHot = false;
		Integer eleCount = reportAnalysisMapper.countMeterByType(ledgerId, 1);
		if(eleCount != null && eleCount > 0 ){
			hasEle = true;
		}
		Integer waterCount = reportAnalysisMapper.countMeterByType(ledgerId, 2);
		if(waterCount != null && waterCount > 0 ){
			hasWater = true;
		}
		Integer gasCount = reportAnalysisMapper.countMeterByType(ledgerId, 3);
		if(gasCount != null && gasCount > 0 ){
			hasGas = true;
		}
		Integer hotCount = reportAnalysisMapper.countMeterByType(ledgerId, 4);
		if(hotCount != null && hotCount > 0 ){
			hasHot = true;
		}
		resultMap.put("hasEle", hasEle);
		resultMap.put("hasWater", hasWater);
		resultMap.put("hasGas", hasGas);
		resultMap.put("hasHot", hasHot);
		return resultMap;
	}


	/**
	 * 查询商户月电费数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> searchMonthEle(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = this.reportAnalysisMapper.getMonthEle(param);
		int display = (Integer) param.get("display");
		//总电量
		double eleTotal = 0d;
		//总电费
		double feeTotal = 0d;
		//总的测量点个数
		int meterSize = 0;
		if(list != null && list.size() > 0){
			Map<Long, Map<String, Object>> data = new LinkedHashMap<Long, Map<String, Object>>();
			for (Map<String, Object> map : list) {
				Long ledgerId = ((BigDecimal) map.get("LEDGER_ID")).longValue();
				String ledgerName= (String)map.get("LEDGER_NAME");
				Long meterId = ((BigDecimal) map.get("METER_ID")).longValue();
//				Long rateNumber = (Long) map.get("RATE_NUMBER");
				String meterName = (String) map.get("METER_NAME");
				Double val = ((BigDecimal)map.get("FAQ")).doubleValue();
				Double rateValue = ((BigDecimal)map.get("RATE_VALUE")).doubleValue();
				String sectorName = (String) map.get("SECTOR_NAME");
				//商户电费
				double fee = DataUtil.doubleMultiply(val, rateValue);
				
				//分户总
				Map<String, Object> detail = data.get(ledgerId);
				//不为空，有数据
				if(detail != null){
					double singleVal = (Double) detail.get("ele");
					detail.put("ele", DataUtil.doubleAdd(val, singleVal));
					double singlefee = (Double) detail.get("fee");
					detail.put("fee", DataUtil.doubleAdd(fee, singlefee));
					Map<String, Double> priceMap = (Map<String, Double>) detail.get("price");
					priceMap.put(sectorName, rateValue);
					//商户下属测量点
					Map<Long, Map<String, Object>> meterList = (Map<Long, Map<String, Object>>) detail.get("meterList");
					Map<String, Object> meterMap = meterList.get(meterId);
					if(meterMap != null){
						double meterVal = (Double) meterMap.get("ele");
						meterMap.put("ele", DataUtil.doubleAdd(val, meterVal));
						double meterfee = (Double) meterMap.get("fee");
						meterMap.put("fee", DataUtil.doubleAdd(fee, meterfee));
						priceMap.put(sectorName, rateValue);
						meterMap.put("price", priceMap);
					}else {
						meterSize++;
						meterMap = new HashMap<String, Object>();
						meterMap.put("meterName", meterName);
						meterMap.put("ele", val);
						meterMap.put("fee", fee);
						meterMap.put("price", priceMap);
						meterList.put(meterId, meterMap);
					}
					
					detail.put("meterList", meterList);
					
				}
				//为空，没有数据
				else {
					detail = new HashMap<String, Object>();
					//商户名称
					detail.put("name", ledgerName);
					//商户电量
					detail.put("ele", val);
					//商户电费
					detail.put("fee", fee);
					//商户电价
					Map<String, Double> priceMap = new LinkedHashMap<String, Double>();
					priceMap.put(sectorName, rateValue);
					detail.put("price", priceMap);
					//商户下属测量点
					meterSize++;
					
					Map<Long, Map<String, Object>> meterList = new HashMap<Long, Map<String, Object>>();
					Map<String, Object> meterMap = new HashMap<String, Object>();
					meterMap.put("meterName", meterName);
					meterMap.put("ele", val);
					meterMap.put("fee", fee);
					meterMap.put("price", priceMap);
					meterList.put(meterId, meterMap);
					detail.put("meterList", meterList);
				}
				data.put(ledgerId, detail);
				eleTotal = DataUtil.doubleAdd(eleTotal, val);
				feeTotal = DataUtil.doubleAdd(feeTotal, fee);
			}
			int rowNum = data.size();
			//显示测量点
			if(display == 1){
				rowNum = rowNum + meterSize;
			}
			result.put("data", data);
			
			//数据保留两位小数
			Iterator<Entry<Long, Map<String, Object>>> it = data.entrySet().iterator();
			while(it.hasNext()){
				Entry<Long, Map<String, Object>> entry = it.next();
				Map<String, Object> map = entry.getValue();
				double ele = (Double)map.get("ele");
				map.put("ele", DataUtil.retained(ele,5));
				double fee = (Double)map.get("fee");
				map.put("fee", DataUtil.retained(fee,2));
				Map<Long, Map<String, Object>> meterList = (Map<Long, Map<String, Object>>) map.get("meterList");
				Iterator<Entry<Long, Map<String, Object>>> ite = meterList.entrySet().iterator();
				while(ite.hasNext()){
					Entry<Long, Map<String, Object>> entr = ite.next();
					Map<String, Object> meter= entr.getValue();
					double meterEle = (Double)meter.get("ele");
					meter.put("ele", DataUtil.retained(meterEle,5));
					double meteFee = (Double)meter.get("fee");
					meter.put("fee", DataUtil.retained(meteFee,2));
				}
			}
			
			
			result.put("eleTotal", DataUtil.retained(eleTotal,5) );
			result.put("feeTotal", DataUtil.retained(feeTotal,2) );
			result.put("rowNum", rowNum);
		}
		return result;
	}

	/**
	 * 导出商户月电费数据
	 */
	@Override
	public void reportMonthExcel(String string,
			ServletOutputStream outputStream, Map<String, Object> result,
			Map<String, Object> param, String title) {
        HSSFWorkbook wb = createMonFeeReport(result, title, param);
		try {
			outputStream.flush();
			wb.write(outputStream);
			outputStream.close();
		} catch (IOException e) {
			Log.error(this.getClass() + ".reportMonthExcel()--无法导出商户月电费数据");
		}
	}

	/**
	 * 生成商户月电费数据
	 * @param result
	 * @param title
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private HSSFWorkbook createMonFeeReport(Map<String, Object> result,
			String title, Map<String, Object> param) {


		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
            HSSFSheet sheet = wb.createSheet(URLDecoder.decode(title, "UTF-8"));
			int display = (Integer)param.get("display");
			//设置多少列
			int columnLength = display ==1 ? 5:4;
			for (int i = 0; i < columnLength; i++) {
				int width = 3 * 256;
				sheet.setColumnWidth(i,12*width);
			}
			
			//总行数，头部1行，合计1行
			int dataRow = 2 + (Integer)result.get("rowNum");
			
			HSSFRow[] rows = new HSSFRow[dataRow];
			
			//定义表头风格
			HSSFCellStyle thStyle = setThStyle(wb);
			
			//第一行
			rows[0] = sheet.createRow(0);
			//生成单元格
			createCell(0, columnLength-1, rows[0], thStyle);
			
			//商户
			rows[0].getCell(0).setCellValue("商户");
			
			//包含测量点
			if(display == 1){
				//表计
				rows[0].getCell(1).setCellValue("表计");
				//电量
				rows[0].getCell(2).setCellValue("电量(kWh)");
				//电费
				rows[0].getCell(3).setCellValue("电费(元)");
				//电价
				rows[0].getCell(4).setCellValue("电价(元/kWh)");
			}
			//不包含测量点
			else {
				//电量
				rows[0].getCell(1).setCellValue("电量(kWh)");
				//电费
				rows[0].getCell(2).setCellValue("电费(元)");
				//电价
				rows[0].getCell(3).setCellValue("电价(元/kWh)");
			}
			
			//定义数据样式
			HSSFCellStyle tdStyle = setTdStyle(wb);
			
			int j = 0;
			
			Map<Long, Map<String, Object>> data = (Map<Long, Map<String, Object>>) result.get("data");
			Iterator<Entry<Long, Map<String, Object>>> it = data.entrySet().iterator();
			while(it.hasNext()){
				Entry<Long, Map<String, Object>> entry = it.next();
				Map<String, Object> ledger = entry.getValue();
				String name = (String) ledger.get("name");
				Double ele = (Double) ledger.get("ele");
				Double fee = (Double) ledger.get("fee");
				List<String> priceList = new ArrayList<String>();
				Map<String, Double> priceMap = (Map<String, Double>) ledger.get("price");
				Iterator<Entry<String, Double>> priceIt = priceMap.entrySet().iterator();
				while(priceIt.hasNext()){
					Entry<String, Double> priceEntry = priceIt.next();
					priceList.add(priceEntry.getKey() + ":" + priceEntry.getValue());
				}
				String price = StringUtils.join(priceList.toArray(), ",");
				
				//显示测量点
				if(display == 1){
					j++;
					rows[j] = sheet.createRow(j);
					createCell(0, columnLength-1, rows[j], tdStyle);
					
					Map<Long, Map<String, Object>> meterList = (Map<Long, Map<String, Object>>) ledger.get("meterList");
					sheet.addMergedRegion(new CellRangeAddress(j, j+meterList.size(), 0, 0));
					rows[j].getCell(0).setCellValue(checkIsNull2(name));
					rows[j].getCell(1).setCellValue("总");
					rows[j].getCell(2).setCellValue(checkIsNull2(ele));
					rows[j].getCell(3).setCellValue(checkIsNull2(fee));
					rows[j].getCell(4).setCellValue(checkIsNull2(price));
					
					//得到测量点信息
					Iterator<Entry<Long, Map<String, Object>>>  ite = meterList.entrySet().iterator();
					while(ite.hasNext()){
						Entry<Long, Map<String, Object>> en = ite.next();
						Map<String, Object> meter = en.getValue();
						String meterName = (String) meter.get("meterName");
						Double meterEle = (Double) meter.get("ele");
						Double meterFee = (Double) meter.get("fee");
						
						j++;
						rows[j] = sheet.createRow(j);
						createCell(0, columnLength-1, rows[j], tdStyle);
						
						rows[j].getCell(1).setCellValue(checkIsNull2(meterName));
						rows[j].getCell(2).setCellValue(checkIsNull2(meterEle));
						rows[j].getCell(3).setCellValue(checkIsNull2(meterFee));
						rows[j].getCell(4).setCellValue(checkIsNull2(price));
					}
				}
				//不显示测量点
				else {
					j++;
					rows[j] = sheet.createRow(j);
					createCell(0, columnLength-1, rows[j], tdStyle);
					
					rows[j].getCell(0).setCellValue(checkIsNull2(name));
					rows[j].getCell(1).setCellValue(checkIsNull2(ele));
					rows[j].getCell(2).setCellValue(checkIsNull2(fee));
					rows[j].getCell(3).setCellValue(checkIsNull2(price));
				}
				
			}
			
			//合计
			//显示测量点
			j++;
			rows[j] = sheet.createRow(j);
			createCell(0, columnLength-1, rows[j], tdStyle);
			if(display == 1){
				rows[j].getCell(0).setCellValue("合计");
				rows[j].getCell(1).setCellValue("-");
				rows[j].getCell(2).setCellValue(checkIsNull2(result.get("eleTotal")));
				rows[j].getCell(3).setCellValue(checkIsNull2(result.get("feeTotal")));
				rows[j].getCell(4).setCellValue("-");
			}
			//不显示测量点
			else {
				rows[j].getCell(0).setCellValue("合计");
				rows[j].getCell(1).setCellValue(checkIsNull2(result.get("eleTotal")));
				rows[j].getCell(2).setCellValue(checkIsNull2(result.get("feeTotal")));
				rows[j].getCell(3).setCellValue("-");
			}
		} catch (UnsupportedEncodingException e) {Log.error(this.getClass() + "decode error!");}
		return wb;
	}


	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ReportService#getChildLedger(java.util.Map)
	 */
	@Override
	public List<String> getChildLedger(Map<String, Object> param) {
		return reportAnalysisMapper.getChildLedger(param);
	}


	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ReportService#getChildMeter(java.util.Map)
	 */
	@Override
	public List<String> getChildMeter(Map<String, Object> param) {
		return reportAnalysisMapper.getChildMeter(param);
	}


	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ReportService#getEleChildMeterByM(java.util.Map)
	 */
	@Override
	public List<String> getEleChildMeterByM(Map<String, Object> param) {
		return reportAnalysisMapper.getEleChildMeterByM(param);
	}


	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ReportService#getEleChildMeterByL(java.util.Map)
	 */
	@Override
	public List<String> getEleChildMeterByL(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return reportAnalysisMapper.getEleChildMeterByL(param);
	}
	
	@Override
	public List<String> getEleChildMeterNew(Map<String, Object> param) {
		return reportAnalysisMapper.getEleChildMeterNew( param );
	}
	
	
	
	/**
	 * 补全数据(针对丢失的测量点)
	 * @param param
	 * @param list
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> completeData_new(Map<String, Object> param,
											 List<Map<String, Object>> list, int dataType) throws ParseException {
		
		TimeEnum dateType = null;
		String datePartten = "";
		if(dataType == 1){
			//电量
			dateType = TimeEnum.HOUR;
			datePartten = DateUtil.DEFAULT_FULL_PATTERN;
		}else if(dataType == 2 || dataType == 3 || dataType == 4 || dataType == 5 || dataType == 6){
			dateType = TimeEnum.MINUTE15;
			datePartten = DateUtil.DEFAULT_FULL_PATTERN;
		}else if(dataType == 7 || dataType == 8 || dataType == 9 || dataType == 10 ){
			dateType = TimeEnum.DAY;
			datePartten = DateUtil.DEFAULT_SHORT_PATTERN;
		}else if(dataType == 12 || dataType == 13 || dataType == 14 || dataType == 15 ){
			dateType = TimeEnum.MONTH;
			datePartten = DateUtil.DEFAULT_SIMPLE_PATTERN;
		}
		
		//存放每个对象的数据
		Map<Long, ChartItemWithTime> objMap = new LinkedHashMap<Long, ChartItemWithTime>();
		
		for (Map<String, Object> map : list) {
			Long objectId = ((BigDecimal) map.get("OBJECTID")).longValue();
			if(!objMap.containsKey(objectId)){
				//如果objMap没有存放该对象，新建一个对象
				Date beginDate = (Date) param.get("beginDate");
				Date endDate = (Date) param.get("endDate");
				if(dataType == 1){//电量，减一个小时
					Calendar cal = Calendar.getInstance();
					cal.setTime(endDate);
					cal.add(Calendar.HOUR_OF_DAY, -1);
					endDate = cal.getTime();
				}
				String objectName = (String) map.get("OBJECTNAME");
				String PTCT = "-";
				if(map.get("PTCT") != null ){
					PTCT = ((Integer) map.get("PTCT")).toString();
				}
				String ADDRESS = "-";
				if( map.get("ADDRESS") != null ){
					ADDRESS = (String) map.get("ADDRESS");
				}
				ChartItemWithTime chartItem = new ChartItemWithTime(dateType, objectName + "," + PTCT + "," + ADDRESS, beginDate.getTime()/1000, endDate.getTime()/1000);
				objMap.put(objectId, chartItem);
			}
			
			Date freezeTime = (Date)map.get("FREEZETIME");
			String timeStr = DateUtil.convertDateToStr(freezeTime, datePartten);
			//把生成的数据放入ChartItemWithTime中
			((ChartItemWithTime)objMap.get(objectId)).getMap().put(timeStr, map);
		}
		//存放最后的结果集合
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		//存放最后的日期列表集合
		List<String> dates = new ArrayList<String>();
		Iterator<Entry<Long, ChartItemWithTime>> objIt = objMap.entrySet().iterator();
		int i = 0;
		while(objIt.hasNext()){
			Entry<Long, ChartItemWithTime> entry = objIt.next();
			ChartItemWithTime item = entry.getValue();
			Long objId = entry.getKey();
			Iterator<Entry<String, Object>> it = item.getMap().entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entr = it.next();
				if(i == 0){
					if(dataType == 7 || dataType == 8 || dataType == 9 || dataType == 10){//月报，不显示年份
						dates.add(((String)entr.getKey()).substring(5,10));
					}else {
						dates.add((String)entr.getKey());
					}
					
				}
				Map<String, Object> map = null;
				try {
					map = (Map<String, Object>) entr.getValue();
				} catch (ClassCastException e) {
					String objInfo = item.getName();
					String[] objInfoAry = objInfo.split(",");
					map = new HashMap<String, Object>();
					map.put("OBJECTID", objId);
					map.put("OBJECTNAME", objInfoAry[0]);//OBJECTnAME
					map.put("PTCT", objInfoAry[1]);//存入PTCT
					map.put("ADDRESS", objInfoAry[2]);//存入ADDRESS
					map.put("FREEZETIME", entr.getKey());
					map.put("A", "-");
					map.put("B", "-");
					map.put("C", "-");
				}
				resultList.add(map);
			}
			i++;
		}
		return resultList;
	}
	
	/**
	 * 把字符串数组转成可删除元素的字符串集合
	 * @author catkins.
	 * @param arr
	 * @return java.util.List<java.lang.String>
	 * @exception
	 * @date 2019/9/11 15:15
	 */
	private List<String> getList(String[] arr){
		List<String> strings = Arrays.asList( arr );
		
		List<String> list = new ArrayList<>( strings );
		
		return list;
	}
	
	/**
	 * 查询缺失测量点的数据
	 * @author catkins.
	 * @param param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/11 16:07
	 */
	@Override
	public List<Map<String, Object>> getMeterReportData4Lost(Map<String,Object> param,Date beginDate,Date endDate,int dataType,List<String> objectIds) {
		List<Map<String,Object>> list = new ArrayList<>( 0 );
		//得到测量点数据--综合能源没有测量点
		if(dataType != 11 && dataType != 16){
			for (String objectId :objectIds) {
				List<Map<String, Object>> meterList = reportAnalysis4LostMapper.getMeterReportData4Lost( beginDate,endDate,dataType,Long.parseLong( objectId ) );
				if(dataType == 7){
					assembledMonEleData(param, dataType, meterList);
				}else if(dataType == 8 || dataType == 9 || dataType == 10){
					assembledMonOtherData(param, dataType, meterList);
				}
				if(list != null)
					list.addAll(meterList);
			}
		}
		try {
			if(dataType != 11){
				//补全数据
				list = completeData_new( param, list, dataType );
			} else {
				return list;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

    @Override
    public Map<String, Object> fhAnalysisDataPage(Map<String, Object> paramInfo){
        Map<String, Object> result = new HashMap<String, Object>();

        String ledgerIds = paramInfo.get("ledgerIds").toString();
        List<Long> ledgerList = null;
        if(StringUtil.isNotEmpty(ledgerIds)){
            String[] t1 = ledgerIds.split(",");
            ledgerList = new ArrayList<>();
            for (int i = 0; i < t1.length; i++) {
                ledgerList.add(Long.parseLong(t1[i]));
            }
        }

        String meterIds = paramInfo.get("meterIds").toString();
        List<Long> meterList = null;
        if(StringUtil.isNotEmpty(meterIds)){
            String[] t2 = meterIds.split(",");
            meterList = new ArrayList<>();
            for (int i = 0; i < t2.length; i++) {
                meterList.add(Long.parseLong(t2[i]));
            }
        }

        Integer objType = Integer.valueOf(paramInfo.get("objType").toString());   //对象类型, 0: 所有,1: 设备;
        Integer frequence = Integer.valueOf(paramInfo.get("frequence").toString()); //数据频率, 1: 60分钟， 2: 30分钟， 3: 15分钟;

        String beginTime = paramInfo.get("beginTime").toString() + " 00:00";
        Date beginDate = com.linyang.energy.utils.DateUtil.convertStrToDate(beginTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);
        String endTime = paramInfo.get("endTime").toString() + " 23:59";
        Date endDate = com.linyang.energy.utils.DateUtil.convertStrToDate(endTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);

//        List<Long> meters = this.reportAnalysisMapper.getFhAnalysisMeters(ledgerList, meterList, objType);

        String message = "";
        List<Map<String, Object>> list = null;
        Map<String, Object> param = new HashMap<>();
        param.put("ledgerList", ledgerList);
        param.put("meterList", meterList);
        param.put("objType", objType);
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        param.put("frequence", frequence);
        param.put("page", paramInfo.get("page"));
        list = this.reportAnalysisMapper.getFhIDataPageList(param);

        result.put("list", list);
        result.put("message", message);

        return result;
    }

    @Override
    public void fhAnalysisExcel(ServletOutputStream outputStream, Map<String, Object> paramInfo, String title){
        ///先查询全部数据
        String ledgerIds = paramInfo.get("ledgerIds").toString();
        List<Long> ledgerList = null;
        if(StringUtil.isNotEmpty(ledgerIds)){
            String[] t1 = ledgerIds.split(",");
            ledgerList = new ArrayList<>();
            for (int i = 0; i < t1.length; i++) {
                ledgerList.add(Long.parseLong(t1[i]));
            }
        }

        String meterIds = paramInfo.get("meterIds").toString();
        List<Long> meterList = null;
        if(StringUtil.isNotEmpty(meterIds)){
            String[] t2 = meterIds.split(",");
            meterList = new ArrayList<>();
            for (int i = 0; i < t2.length; i++) {
                meterList.add(Long.parseLong(t2[i]));
            }
        }

        Integer objType = Integer.valueOf(paramInfo.get("objType").toString());   //对象类型, 0: 所有,1: 设备;
        Integer frequence = Integer.valueOf(paramInfo.get("frequence").toString()); //数据频率, 1: 60分钟， 2: 30分钟， 3: 15分钟;

        String beginTime = paramInfo.get("beginTime").toString() + " 00:00";
        Date beginDate = com.linyang.energy.utils.DateUtil.convertStrToDate(beginTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);
        String endTime = paramInfo.get("endTime").toString() + " 23:59";
        Date endDate = com.linyang.energy.utils.DateUtil.convertStrToDate(endTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);

//        List<Long> meters = this.reportAnalysisMapper.getFhAnalysisMeters(ledgerList, meterList, objType);

        Map<String, Object> param = new HashMap<>();
        param.put("ledgerList", ledgerList);
        param.put("meterList", meterList);
        param.put("objType", objType);
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        param.put("frequence", frequence);
        List<Map<String, Object>> list = this.reportAnalysisMapper.getFhIDataList(param);

        ///再导出表格
        //声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        try{
            //定义一个表格
            HSSFSheet sheet = null;
            sheet = wb.createSheet(title);
            sheet.setColumnWidth((short)0,(short)24*256);
            sheet.setColumnWidth((short)1,(short)24*256);
            sheet.setColumnWidth((short)2,(short)24*128);
            sheet.setColumnWidth((short)3,(short)24*128);
            sheet.setColumnWidth((short)4,(short)24*128);
            sheet.setColumnWidth((short)5,(short)24*128);
            sheet.setColumnWidth((short)6,(short)24*128);
            sheet.setColumnWidth((short)7,(short)24*128);
            sheet.setColumnWidth((short)8,(short)24*128);
            sheet.setColumnWidth((short)9,(short)24*128);
            sheet.setColumnWidth((short)10,(short)24*128);
            sheet.setColumnWidth((short)11,(short)24*128);
            sheet.setColumnWidth((short)12,(short)24*128);
            sheet.setColumnWidth((short)13,(short)24*128);
            sheet.setColumnWidth((short)14,(short)24*128);
            sheet.setColumnWidth((short)15,(short)24*128);
            sheet.setColumnWidth((short)16,(short)24*128);
            sheet.setColumnWidth((short)17,(short)24*128);
            sheet.setColumnWidth((short)18,(short)24*128);
            sheet.setColumnWidth((short)19,(short)24*128);
            sheet.setColumnWidth((short)20,(short)24*128);
            sheet.setColumnWidth((short)21,(short)24*128);

            //定义标题风格
            HSSFCellStyle titlestyle = wb.createCellStyle();
            titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            titlestyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
            titlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            HSSFFont font = wb.createFont();
            font.setColor(HSSFColor.WHITE.index);
            titlestyle.setFont(font);

            //定义具体表格内容样式
            HSSFCellStyle style_ =wb.createCellStyle();
            style_.setRightBorderColor(HSSFColor.BLACK.index);
            style_.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style_.setLeftBorderColor(HSSFColor.BLACK.index);
            style_.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style_.setTopBorderColor(HSSFColor.BLACK.index);
            style_.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style_.setBottomBorderColor(HSSFColor.BLACK.index);
            style_.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style_.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style_.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            //行号
            int rowIndex = 0;
            //表格表头
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 4, 6));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 7, 9));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 10, 13));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 14, 17));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 18, 21));

            HSSFRow headRow = sheet.createRow(rowIndex++);
            HSSFCell headCellA = headRow.createCell(0);
            HSSFCell headCellB = headRow.createCell(1);
            HSSFCell headCellC = headRow.createCell(2);
            HSSFCell headCellD = headRow.createCell(3);
            HSSFCell headCellE = headRow.createCell(4);
            HSSFCell headCellF = headRow.createCell(7);
            HSSFCell headCellG = headRow.createCell(10);
            HSSFCell headCellH = headRow.createCell(14);
            HSSFCell headCellI = headRow.createCell(18);
            headCellA.setCellStyle(titlestyle);
            headCellB.setCellStyle(titlestyle);
            headCellC.setCellStyle(titlestyle);
            headCellD.setCellStyle(titlestyle);
            headCellE.setCellStyle(titlestyle);
            headCellF.setCellStyle(titlestyle);
            headCellG.setCellStyle(titlestyle);
            headCellH.setCellStyle(titlestyle);
            headCellI.setCellStyle(titlestyle);
            headCellA.setCellValue("企业名称");
            headCellB.setCellValue("采集点名称");
            headCellC.setCellValue("日期");
            headCellD.setCellValue("时间");
            headCellE.setCellValue("电流(A)");
            headCellF.setCellValue("电压(kV)");
            headCellG.setCellValue("功率因数(%)");
            headCellH.setCellValue("有功功率(kW)");
            headCellI.setCellValue("无功功率(kVar)");
            //表格表头
            sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex, 3, 3));

            HSSFRow trRow = sheet.createRow(rowIndex++);

            HSSFCell cellA = trRow.createCell(0);
            HSSFCell cellB = trRow.createCell(1);
            HSSFCell cellD = trRow.createCell(2);
            HSSFCell cellE = trRow.createCell(3);
            HSSFCell cellF = trRow.createCell(4);
            HSSFCell cellG = trRow.createCell(5);
            HSSFCell cellH = trRow.createCell(6);
            HSSFCell cellI = trRow.createCell(7);
            HSSFCell cellJ = trRow.createCell(8);
            HSSFCell cellK = trRow.createCell(9);
            HSSFCell cellL = trRow.createCell(10);
            HSSFCell cellM = trRow.createCell(11);
            HSSFCell cellN = trRow.createCell(12);
            HSSFCell cellO = trRow.createCell(13);
            HSSFCell cellP = trRow.createCell(14);
            HSSFCell cellQ = trRow.createCell(15);
            HSSFCell cellR = trRow.createCell(16);
            HSSFCell cellS = trRow.createCell(17);
            HSSFCell cellT = trRow.createCell(18);
            HSSFCell cellU = trRow.createCell(19);
            HSSFCell cellV = trRow.createCell(20);
            HSSFCell cellW = trRow.createCell(21);

            cellA.setCellStyle(titlestyle);
            cellB.setCellStyle(titlestyle);
            cellD.setCellStyle(titlestyle);
            cellE.setCellStyle(titlestyle);
            cellF.setCellStyle(titlestyle);
            cellG.setCellStyle(titlestyle);
            cellH.setCellStyle(titlestyle);
            cellI.setCellStyle(titlestyle);
            cellJ.setCellStyle(titlestyle);
            cellK.setCellStyle(titlestyle);
            cellL.setCellStyle(titlestyle);
            cellM.setCellStyle(titlestyle);
            cellN.setCellStyle(titlestyle);
            cellO.setCellStyle(titlestyle);
            cellP.setCellStyle(titlestyle);
            cellQ.setCellStyle(titlestyle);
            cellR.setCellStyle(titlestyle);
            cellS.setCellStyle(titlestyle);
            cellT.setCellStyle(titlestyle);
            cellU.setCellStyle(titlestyle);
            cellV.setCellStyle(titlestyle);
            cellW.setCellStyle(titlestyle);

//            cellA.setCellValue("企业名称");
//            cellB.setCellValue("采集点名称");
//            cellD.setCellValue("日期");
//            cellE.setCellValue("时间");
            cellF.setCellValue("A相");
            cellG.setCellValue("B相");
            cellH.setCellValue("C相");
            cellI.setCellValue("A相");
            cellJ.setCellValue("B相");
            cellK.setCellValue("C相");
            cellL.setCellValue("总");
            cellM.setCellValue("A相");
            cellN.setCellValue("B相");
            cellO.setCellValue("C相");
            cellP.setCellValue("总");
            cellQ.setCellValue("A相");
            cellR.setCellValue("B相");
            cellS.setCellValue("C相");
            cellT.setCellValue("总");
            cellU.setCellValue("A相");
            cellV.setCellValue("B相");
            cellW.setCellValue("C相");

            //表格数据
            if(list != null && list.size() > 0){
                for( int i = 0 ; i < list.size( ) ; i ++){
                    Map<String, Object> map = list.get(i);
                    HSSFRow row1 = sheet.createRow(rowIndex++);
                    HSSFCell cell1A = row1.createCell(0);
                    HSSFCell cell1B = row1.createCell(1);
                    HSSFCell cell1D = row1.createCell(2);
                    HSSFCell cell1E = row1.createCell(3);
                    HSSFCell cell1F = row1.createCell(4);
                    HSSFCell cell1G = row1.createCell(5);
                    HSSFCell cell1H = row1.createCell(6);
                    HSSFCell cell1I = row1.createCell(7);
                    HSSFCell cell1J = row1.createCell(8);
                    HSSFCell cell1K = row1.createCell(9);
                    HSSFCell cell1L = row1.createCell(10);
                    HSSFCell cell1M = row1.createCell(11);
                    HSSFCell cell1N = row1.createCell(12);
                    HSSFCell cell1O = row1.createCell(13);
                    HSSFCell cell1P = row1.createCell(14);
                    HSSFCell cell1Q = row1.createCell(15);
                    HSSFCell cell1R = row1.createCell(16);
                    HSSFCell cell1S = row1.createCell(17);
                    HSSFCell cell1T = row1.createCell(18);
                    HSSFCell cell1U = row1.createCell(19);
                    HSSFCell cell1V = row1.createCell(20);
                    HSSFCell cell1W = row1.createCell(21);

                    cell1A.setCellStyle(style_);
                    cell1B.setCellStyle(style_);
                    cell1D.setCellStyle(style_);
                    cell1E.setCellStyle(style_);
                    cell1F.setCellStyle(style_);
                    cell1G.setCellStyle(style_);
                    cell1H.setCellStyle(style_);
                    cell1I.setCellStyle(style_);
                    cell1J.setCellStyle(style_);
                    cell1K.setCellStyle(style_);
                    cell1L.setCellStyle(style_);
                    cell1M.setCellStyle(style_);
                    cell1N.setCellStyle(style_);
                    cell1O.setCellStyle(style_);
                    cell1P.setCellStyle(style_);
                    cell1Q.setCellStyle(style_);
                    cell1R.setCellStyle(style_);
                    cell1S.setCellStyle(style_);
                    cell1T.setCellStyle(style_);
                    cell1U.setCellStyle(style_);
                    cell1V.setCellStyle(style_);
                    cell1W.setCellStyle(style_);

                    cell1A.setCellValue(checkIsNull(map.get("ledgerName")));
                    cell1B.setCellValue(checkIsNull(map.get("meterName")));
                    cell1D.setCellValue(checkIsNull(map.get("dateVal")));
                    cell1E.setCellValue(checkIsNull(map.get("timeVal")));
                    cell1F.setCellValue(checkIsNull(map.get("Ia")));
                    cell1G.setCellValue(checkIsNull(map.get("Ib")));
                    cell1H.setCellValue(checkIsNull(map.get("Ic")));
                    cell1I.setCellValue(checkIsNull(map.get("Ua")));
                    cell1J.setCellValue(checkIsNull(map.get("Ub")));
                    cell1K.setCellValue(checkIsNull(map.get("Uc")));
                    cell1L.setCellValue(checkIsNull3(map.get("PF")));
                    cell1M.setCellValue(checkIsNull3(map.get("PFa")));
                    cell1N.setCellValue(checkIsNull3(map.get("PFb")));
                    cell1O.setCellValue(checkIsNull3(map.get("PFc")));
                    cell1P.setCellValue(checkIsNull(map.get("P")));
                    cell1Q.setCellValue(checkIsNull(map.get("Pa")));
                    cell1R.setCellValue(checkIsNull(map.get("Pb")));
                    cell1S.setCellValue(checkIsNull(map.get("Pc")));
                    cell1T.setCellValue(checkIsNull(map.get("Q")));
                    cell1U.setCellValue(checkIsNull(map.get("Qa")));
                    cell1V.setCellValue(checkIsNull(map.get("Qb")));
                    cell1W.setCellValue(checkIsNull(map.get("Qc")));
                }
            }

            outputStream.flush();
            wb.write(outputStream);
            outputStream.close();
        }
        catch( IOException e ){
            Log.info("fhAnalysisExcel error IOException!");
        }
    }

    @Override
    public List<Map<String, Object>> energyAnalysisData(Map<String, Object> paramInfo){
        String ledgerIds = paramInfo.get("ledgerIds").toString();
        List<Long> ledgerList = null;
        if(StringUtil.isNotEmpty(ledgerIds)){
            String[] t1 = ledgerIds.split(",");
            ledgerList = new ArrayList<>();
            for (int i = 0; i < t1.length; i++) {
                ledgerList.add(Long.parseLong(t1[i]));
            }
        }

        String meterIds = paramInfo.get("meterIds").toString();
        List<Long> meterList = null;
        if(StringUtil.isNotEmpty(meterIds)){
            String[] t2 = meterIds.split(",");
            meterList = new ArrayList<>();
            for (int i = 0; i < t2.length; i++) {
                meterList.add(Long.parseLong(t2[i]));
            }
        }

        Integer eleOrder = Integer.valueOf(paramInfo.get("eleOrder").toString());   //电耗排名, 1：Top 10, 2：Top 20, 3：全部;
        Integer orderType = Integer.valueOf(paramInfo.get("orderType").toString()); //排序方式, 1：电耗小, 2：电耗大;

        String beginTime = paramInfo.get("beginTime").toString() + " 00:00";
        Date beginDate = com.linyang.energy.utils.DateUtil.convertStrToDate(beginTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);
        String endTime = paramInfo.get("endTime").toString() + " 23:59";
        Date endDate = com.linyang.energy.utils.DateUtil.convertStrToDate(endTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);

        List<Map<String, Object>> list = this.reportAnalysisMapper.getEleAnalysisData(ledgerList, meterList, orderType, beginDate, endDate);
        if(eleOrder == 1){
            if(list != null && list.size() > 10){
                list = list.subList(0, 10);
            }
        }
        else if(eleOrder == 2){
            if(list != null && list.size() > 20){
                list = list.subList(0, 20);
            }
        }

        return list;
    }

    @Override
    public void energyAnalysisExcel(ServletOutputStream outputStream, Map<String, Object> paramInfo, String title){
        ///先查询全部数据
        List<Map<String, Object>> list = energyAnalysisData(paramInfo);

        ///再导出表格
        //声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        try{
            //定义一个表格
            HSSFSheet sheet = null;
            sheet = wb.createSheet(title);
            sheet.setColumnWidth((short)0,(short)24*256);
            sheet.setColumnWidth((short)1,(short)24*256);
            sheet.setColumnWidth((short)2,(short)24*256);
            sheet.setColumnWidth((short)3,(short)24*256);
            sheet.setColumnWidth((short)4,(short)24*256);

            //定义标题样式
            HSSFCellStyle titlestyle = wb.createCellStyle();
            titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            titlestyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
            titlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            HSSFFont font = wb.createFont();
            font.setColor(HSSFColor.WHITE.index);
            titlestyle.setFont(font);

            //定义具体表格内容样式
            HSSFCellStyle style_ =wb.createCellStyle();
            style_.setRightBorderColor(HSSFColor.BLACK.index);
            style_.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style_.setLeftBorderColor(HSSFColor.BLACK.index);
            style_.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style_.setTopBorderColor(HSSFColor.BLACK.index);
            style_.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style_.setBottomBorderColor(HSSFColor.BLACK.index);
            style_.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style_.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style_.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            //行号
            int rowIndex = 0;
            //表格表头
            HSSFRow headRow = sheet.createRow(rowIndex++);
            HSSFCell headCellA = headRow.createCell(0);
            HSSFCell headCellB = headRow.createCell(1);
            HSSFCell headCellC = headRow.createCell(2);
            HSSFCell headCellD = headRow.createCell(3);
            HSSFCell headCellE = headRow.createCell(4);
            headCellA.setCellStyle(titlestyle);
            headCellB.setCellStyle(titlestyle);
            headCellC.setCellStyle(titlestyle);
            headCellD.setCellStyle(titlestyle);
            headCellE.setCellStyle(titlestyle);
            headCellA.setCellValue("企业名称");
            headCellB.setCellValue("采集点名称");
            headCellC.setCellValue("设备容积（立方）");
            headCellD.setCellValue("产品重量");
            headCellE.setCellValue("电耗（重量）");

            //表格数据
            if(list != null && list.size() > 0){
                for( int i = 0 ; i < list.size( ) ; i ++){
                    Map<String, Object> map = list.get(i);
                    HSSFRow row1 = sheet.createRow(rowIndex++);
                    HSSFCell cell1A = row1.createCell(0);
                    HSSFCell cell1B = row1.createCell(1);
                    HSSFCell cell1D = row1.createCell(2);
                    HSSFCell cell1E = row1.createCell(3);
                    HSSFCell cell1F = row1.createCell(4);

                    cell1A.setCellStyle(style_);
                    cell1B.setCellStyle(style_);
                    cell1D.setCellStyle(style_);
                    cell1E.setCellStyle(style_);
                    cell1F.setCellStyle(style_);

                    cell1A.setCellValue(checkIsNull(map.get("ledgerName")));
                    cell1B.setCellValue(checkIsNull(map.get("meterName")));
                    cell1D.setCellValue(checkIsNull(map.get("fSize")));
                    cell1E.setCellValue(checkIsNull(map.get("fWeig")));
                    cell1F.setCellValue(checkIsNull(map.get("elePw")));
                }
            }

            outputStream.flush();
            wb.write(outputStream);
            outputStream.close();
        }
        catch( IOException e ){
            Log.info("energyAnalysisExcel error IOException!");
        }
    }

    @Override
    public Map<String, Object> energyDetailDataPage(Map<String, Object> paramInfo){
        Map<String, Object> result = new HashMap<String, Object>();

        String ledgerIds = paramInfo.get("ledgerIds").toString();
        List<Long> ledgerList = null;
        if(StringUtil.isNotEmpty(ledgerIds)){
            String[] t1 = ledgerIds.split(",");
            ledgerList = new ArrayList<>();
            for (int i = 0; i < t1.length; i++) {
                ledgerList.add(Long.parseLong(t1[i]));
            }
        }

        String meterIds = paramInfo.get("meterIds").toString();
        List<Long> meterList = null;
        if(StringUtil.isNotEmpty(meterIds)){
            String[] t2 = meterIds.split(",");
            meterList = new ArrayList<>();
            for (int i = 0; i < t2.length; i++) {
                meterList.add(Long.parseLong(t2[i]));
            }
        }

        String beginTime = paramInfo.get("beginTime").toString() + " 00:00";
        Date beginDate = com.linyang.energy.utils.DateUtil.convertStrToDate(beginTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);
        String endTime = paramInfo.get("endTime").toString() + " 23:59";
        Date endDate = com.linyang.energy.utils.DateUtil.convertStrToDate(endTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);

        Map<String, Object> param = new HashMap<>();
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        param.put("ledgerList", ledgerList);
        param.put("meterList", meterList);
        param.put("page", paramInfo.get("page"));
        List<Map<String, Object>> list = this.reportAnalysisMapper.energyDetailDataPageList(param);
        result.put("list", list);

        return result;
    }

    @Override
    public void energyDetailExcel(ServletOutputStream outputStream, Map<String, Object> paramInfo, String title){
        ///先查询全部数据
        String ledgerIds = paramInfo.get("ledgerIds").toString();
        List<Long> ledgerList = null;
        if(StringUtil.isNotEmpty(ledgerIds)){
            String[] t1 = ledgerIds.split(",");
            ledgerList = new ArrayList<>();
            for (int i = 0; i < t1.length; i++) {
                ledgerList.add(Long.parseLong(t1[i]));
            }
        }

        String meterIds = paramInfo.get("meterIds").toString();
        List<Long> meterList = null;
        if(StringUtil.isNotEmpty(meterIds)){
            String[] t2 = meterIds.split(",");
            meterList = new ArrayList<>();
            for (int i = 0; i < t2.length; i++) {
                meterList.add(Long.parseLong(t2[i]));
            }
        }

        String beginTime = paramInfo.get("beginTime").toString() + " 00:00";
        Date beginDate = com.linyang.energy.utils.DateUtil.convertStrToDate(beginTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);
        String endTime = paramInfo.get("endTime").toString() + " 23:59";
        Date endDate = com.linyang.energy.utils.DateUtil.convertStrToDate(endTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);

        Map<String, Object> param = new HashMap<>();
        param.put("beginDate", beginDate);
        param.put("endDate", endDate);
        param.put("ledgerList", ledgerList);
        param.put("meterList", meterList);
        List<Map<String, Object>> list = this.reportAnalysisMapper.energyDetailDataList(param);

        ///再导出表格
        //声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        try{
            //定义一个表格
            HSSFSheet sheet = null;
            sheet = wb.createSheet(title);
            sheet.setColumnWidth((short)0,(short)24*256);
            sheet.setColumnWidth((short)1,(short)24*256);
            sheet.setColumnWidth((short)2,(short)24*256);
            sheet.setColumnWidth((short)3,(short)24*256);
            sheet.setColumnWidth((short)4,(short)24*256);
            sheet.setColumnWidth((short)5,(short)24*256);
            sheet.setColumnWidth((short)6,(short)24*256);

            //定义标题样式
            HSSFCellStyle titlestyle = wb.createCellStyle();
            titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            titlestyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
            titlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
            titlestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            HSSFFont font = wb.createFont();
            font.setColor(HSSFColor.WHITE.index);
            titlestyle.setFont(font);

            //定义具体表格内容样式
            HSSFCellStyle style_ =wb.createCellStyle();
            style_.setRightBorderColor(HSSFColor.BLACK.index);
            style_.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style_.setLeftBorderColor(HSSFColor.BLACK.index);
            style_.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            style_.setTopBorderColor(HSSFColor.BLACK.index);
            style_.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style_.setBottomBorderColor(HSSFColor.BLACK.index);
            style_.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style_.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            style_.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            //行号
            int rowIndex = 0;
            //表格表头
            HSSFRow headRow = sheet.createRow(rowIndex++);
            HSSFCell headCellA = headRow.createCell(0);
            HSSFCell headCellB = headRow.createCell(1);
            HSSFCell headCellC = headRow.createCell(2);
            HSSFCell headCellD = headRow.createCell(3);
            HSSFCell headCellE = headRow.createCell(4);
            HSSFCell headCellF = headRow.createCell(5);
            HSSFCell headCellG = headRow.createCell(6);
            headCellA.setCellStyle(titlestyle);
            headCellB.setCellStyle(titlestyle);
            headCellC.setCellStyle(titlestyle);
            headCellD.setCellStyle(titlestyle);
            headCellE.setCellStyle(titlestyle);
            headCellF.setCellStyle(titlestyle);
            headCellG.setCellStyle(titlestyle);
            headCellA.setCellValue("企业名称");
            headCellB.setCellValue("采集点名称");
            headCellC.setCellValue("设备容积（立方）");
            headCellD.setCellValue("产品重量");
            headCellE.setCellValue("日期");
            headCellF.setCellValue("电量");
            headCellG.setCellValue("电耗（重量）");

            //表格数据
            if(list != null && list.size() > 0){
                for( int i = 0 ; i < list.size( ) ; i ++){
                    Map<String, Object> map = list.get(i);
                    HSSFRow row1 = sheet.createRow(rowIndex++);
                    HSSFCell cell1A = row1.createCell(0);
                    HSSFCell cell1B = row1.createCell(1);
                    HSSFCell cell1D = row1.createCell(2);
                    HSSFCell cell1E = row1.createCell(3);
                    HSSFCell cell1F = row1.createCell(4);
                    HSSFCell cell1G = row1.createCell(5);
                    HSSFCell cell1H = row1.createCell(6);

                    cell1A.setCellStyle(style_);
                    cell1B.setCellStyle(style_);
                    cell1D.setCellStyle(style_);
                    cell1E.setCellStyle(style_);
                    cell1F.setCellStyle(style_);
                    cell1G.setCellStyle(style_);
                    cell1H.setCellStyle(style_);

                    cell1A.setCellValue(checkIsNull(map.get("ledgerName")));
                    cell1B.setCellValue(checkIsNull(map.get("meterName")));
                    cell1D.setCellValue(checkIsNull(map.get("fSize")));
                    cell1E.setCellValue(checkIsNull(map.get("fWeig")));
                    cell1F.setCellValue(checkIsNull(map.get("timeVal")));
                    cell1G.setCellValue(checkIsNull(map.get("apQ")));
                    cell1H.setCellValue(checkIsNull(map.get("ecPw")));
                }
            }

            outputStream.flush();
            wb.write(outputStream);
            outputStream.close();
        }
        catch( IOException e ){
            Log.info("energyDetailExcel error IOException!");
        }
    }
	
}
