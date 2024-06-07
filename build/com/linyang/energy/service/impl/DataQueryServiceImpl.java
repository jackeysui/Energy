package com.linyang.energy.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.math.BigDecimal;import java.math.RoundingMode;import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Callable;

import com.leegern.util.NumberUtil;
import com.leegern.util.StringUtil;
import com.linyang.energy.mapping.eleAssessment.EleAssessmentMapper;
import com.linyang.energy.service.DayHarService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.DataQueryResultBean;
import com.linyang.energy.dto.DataQueryXmlBean;
import com.linyang.energy.dto.MeterTypeEnum;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.energydataquery.DataQueryMapper;
import com.linyang.energy.service.DataQueryService;
import com.linyang.energy.thread.DataQueryThread;
import com.linyang.energy.utils.ChartConditionUtils;
import com.linyang.energy.utils.ChartNameUtils;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.QueryMapping;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;
import com.linyang.util.DoubleUtils;
import com.linyang.util.threadPool.ThreadExector;
import com.linyang.util.threadPool.callable.CallableThreadExector;

/**
 * 数据查询业务逻辑层接口实现类
 * 
 * @description:
 * @version:0.1
 * @author:Cherry
 * @date:Dec 12, 2013
 */
@Service
public class DataQueryServiceImpl implements DataQueryService {

	@Autowired
	private DataQueryMapper dataQueryMapper;
	@Autowired
	private DayHarService DayHarService;
	@Autowired
	private EleAssessmentMapper eleAssessmentMapper;

	private String defaultChartName = ChartNameUtils.getChartNameBean()
			.getEleChartName();
	private String defaultRateNameSuffer = ChartNameUtils.getChartNameBean()
			.getRateNameSuffer();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.linyang.energy.service.DataQueryService#getThermalConsumptionChartData
	 * (java.util.Map)
	 */
	@Override
	public List<ChartItemWithTime> getThermalConsumptionChartData(
			Map<String, Object> queryMap) {
		List<ChartItemWithTime> list = new ArrayList<ChartItemWithTime>();
		if (queryMap.containsKey("beginDate")
				&& queryMap.containsKey("endDate")
				&& queryMap.containsKey("meterId")) {
			long beginDate = Long.valueOf(queryMap.get("beginDate").toString());
			long endDate = Long.valueOf(queryMap.get("endDate").toString());
			queryMap.put("beginTime", new Date(beginDate * 1000));
			queryMap.put("endTime", new Date(endDate * 1000));
			List<DataQueryXmlBean> queryMapping = QueryMapping
					.getQueryMapping(MeterTypeEnum.HOT);
			if (CommonMethod.isCollectionNotEmpty(queryMapping)) {
				ThreadExector exector = new CallableThreadExector();
				List<Callable<DataQueryResultBean>> threadList = new ArrayList<Callable<DataQueryResultBean>>(
						queryMapping.size());
				Map<String, Object> hashMap = null;
				for (DataQueryXmlBean dataQueryXmlBean : queryMapping) {
					hashMap = new HashMap<String, Object>(queryMap);
					hashMap.putAll(CommonMethod
							.convertBean2Map(dataQueryXmlBean));
					threadList.add(new DataQueryThread(dataQueryMapper,
							hashMap, dataQueryXmlBean));
				}
				List<DataQueryResultBean> excuteTasksAndWaitForResult = exector
						.excuteTasksAndWaitForResult(threadList);
				if (CommonMethod
						.isCollectionNotEmpty(excuteTasksAndWaitForResult)) {
					for (DataQueryResultBean dataQueryResultBean : excuteTasksAndWaitForResult) {
						DataQueryXmlBean dataQueryXmlBean = dataQueryResultBean
								.getDataQueryXmlBean();
						String valueField = dataQueryXmlBean.getValueField();
						List<Map<String, Object>> datas = dataQueryResultBean
								.getDatas();
						if (CommonMethod.isCollectionNotEmpty(datas)) {
							ChartItemWithTime item = new ChartItemWithTime(
									TimeEnum.MINUTE,
									dataQueryXmlBean.getName(), beginDate,
									endDate);
							;
							for (Map<String, Object> map : datas) {
								double value = Double.valueOf(map
										.get(valueField) == null ? "0" : map
										.get(valueField).toString());
								Date time = (Date) map.get(dataQueryXmlBean
										.getTimeField());
								if (time != null) {
									String timeString = DateUtil
											.convertDateToStr(time,
													"yyyy-MM-dd HH:mm");
									Map<String, Object> map2 = item.getMap();
									if (map2.containsKey(timeString)) {
										map2.put(timeString, value);
									}
								}
							}
							list.add(item);
						}
					}
				}
			}
		}
		return ChartConditionUtils.itemDataScale(list);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.linyang.energy.service.DataQueryService#getElectricityChartData(java
	 * .util.Map)
	 */
	@Override
	public List<ChartItemWithTime> getElectricityChartData(Map<String, Object> queryMap) {
        List<ChartItemWithTime> list = new ArrayList<ChartItemWithTime>();

		if (queryMap.containsKey("beginDate") && queryMap.containsKey("endDate")
				&& queryMap.containsKey("type") && queryMap.containsKey("timeType")) {

			TimeEnum timeType = TimeEnum.formInt2TimeEnum(Integer.valueOf(queryMap.get("timeType").toString()));
			long beginDate = Long.valueOf(queryMap.get("beginDate").toString());
			long endDate = Long.valueOf(queryMap.get("endDate").toString());
			int dataType = Integer.valueOf(queryMap.get("dataType").toString());
			List<Map<String, Object>> eleList = new ArrayList<Map<String, Object>>();
			// "电量" ,选择"日",当日数据
			if (timeType == TimeEnum.DAY && dataType == 0
                    && DateUtils.convertTimeToLong(DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT) + " 00:00:00") <= endDate){

				queryMap.put("beginDate", endDate);
				eleList = dataQueryMapper.getEleValue(ChartConditionUtils.processDate(queryMap));
			}
			// "电量" ,选择"月",当日数据
			if (timeType == TimeEnum.MONTH && dataType == 0
					&& DateUtils.convertTimeToLong(DateUtil.getCurrentDateStr(DateUtil.MONTH_PATTERN) + "-01 00:00:00") <= endDate){

				queryMap.put("beginDate", DateUtils.convertTimeToLong(DateUtil.getCurrentDateStr(DateUtil.MONTH_PATTERN) + "-01 00:00:00"));
				eleList = dataQueryMapper.getEleValue(ChartConditionUtils.processDate(queryMap));
			}
			// 历史数据,电量数据由尖峰平谷计算
			if (dataType == 0){
				queryMap.put("dataType", "1");
				queryMap.put("beginDate", beginDate);
				if (timeType == TimeEnum.DAY
                        && DateUtils.convertTimeToLong(DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT) + " 00:00:00") <= endDate){

					endDate = DateUtils.convertTimeToLong(DateUtils.addDay(DateUtils.convertTimeToString(endDate,DateUtils.FORMAT_SHORT), -1),DateUtils.FORMAT_SHORT);
					queryMap.put("endDate", endDate);
				}
                else{
					queryMap.put("endDate", endDate);
				}
				List<Map<String, Object>> eleList2 = dataQueryMapper.getEleValue(ChartConditionUtils.processDate(queryMap));// 取到的数据为复费率
				List<Map<String, Object>> eleListTmp = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> map : eleList2){
					double value = DoubleUtils.getDoubleValue(Double.valueOf(map.get("ELE_VALUE") == null ? "0" : map.get("ELE_VALUE").toString()),5);
					Date time = (Date) map.get("TIME_FIELD");
					if (map.get("SECTOR_ID") != null && map.get("TIME_FIELD") != null && map.get("ELE_VALUE") != null){
						boolean notExist = true;
						for (Map<String, Object> mapTmp : eleListTmp){
							Date timeTmp = (Date) mapTmp.get("TIME_FIELD");
							if (time.compareTo(timeTmp) == 0){
								notExist = false;
								double valueTmp = DoubleUtils.getDoubleValue(Double.valueOf(mapTmp.get("ELE_VALUE") == null ? "0": mapTmp.get("ELE_VALUE").toString()),5);
								valueTmp = DataUtil.doubleAdd(valueTmp, value);
								mapTmp.put("ELE_VALUE", DoubleUtils.getDoubleValue(valueTmp,5));
							}
						}
						if (notExist) {
							Map<String, Object> mapTmp = new HashMap<String, Object>();
							mapTmp.put("TIME_FIELD", time);
							mapTmp.put("ELE_VALUE", value);
							eleListTmp.add(mapTmp);
						}
					}
				}
				eleList.addAll(eleListTmp);
			} else {
				eleList = dataQueryMapper.getEleValue(ChartConditionUtils
						.processDate(queryMap));
			}

			if (CommonMethod.isCollectionNotEmpty(eleList)) {
				// 防止线上第一个数据出现ELE_VALUE为空
				Map<String, Object> eleFirst = eleList.get(0);
				if (!eleFirst.keySet().contains("ELE_VALUE")) {
					eleFirst.put("ELE_VALUE", 0);
				}
				if (dataType != 1) {// 不需要查询电量表的，直接查统计表
					buildSingleDatas(list, beginDate, endDate, timeType,
							dataType, eleList);
				} else {
					builderRateDatas(list, beginDate, endDate, timeType,
							eleList);
				}
			}
		}
		return ChartConditionUtils.itemDataScale2(list);
	}

	/**
	 * @param list
	 * @param beginDate
	 * @param endDate
	 * @param timeType
	 * @param eleList
	 */
	private void builderRateDatas(List<ChartItemWithTime> list, long beginDate,
			long endDate, TimeEnum timeType, List<Map<String, Object>> eleList) {
		ChartItemWithTime item = null;
		Map<Long, ChartItemWithTime> mapping = new HashMap<Long, ChartItemWithTime>();
		for (Map<String, Object> map : eleList) {
			double value = DoubleUtils.getDoubleValue(Double
					.valueOf(map.get("ELE_VALUE") == null ? "0" : map.get(
							"ELE_VALUE").toString()),5);
			Date time = (Date) map.get("TIME_FIELD");
			Long sectorId = Long.valueOf(map.get("SECTOR_ID") == null ? "0"
					: map.get("SECTOR_ID").toString());
			if (map.get("SECTOR_ID") != null && map.get("TIME_FIELD") != null
					&& map.get("ELE_VALUE") != null
					&& !mapping.containsKey(sectorId)) {
				String sectorName = map.get("SECTOR_NAME") == null ? (defaultRateNameSuffer + sectorId)
						: map.get("SECTOR_NAME").toString();
				item = new ChartItemWithTime(timeType, sectorName, beginDate,
						endDate);
				mapping.put(sectorId, item);
			}
			if (time != null) {
				String timeString = DateUtil.convertDateToStr(time, timeType
						.getDateFormat());
				if (timeType == TimeEnum.WEEK)
					timeString = DateUtil.getMonday(timeString);
				Map<String, Object> map2 = mapping.get(sectorId).getMap();
				if (map2.containsKey(timeString)) {
					map2.put(timeString, DataUtil.doubleAdd(Double.valueOf(map2.get(timeString)
							.toString()),
							value));
				} else
					map2.put(timeString, value);
			}
		}
		list.addAll(mapping.values());
	}

	/**
	 * @param list
	 * @param beginDate
	 * @param endDate
	 * @param timeType
	 * @param eleList
	 */
	private void buildSingleDatas(List<ChartItemWithTime> list, long beginDate,
			long endDate, TimeEnum timeType, int dataType,
			List<Map<String, Object>> eleList) {
		ChartItemWithTime item = null;
		for (Map<String, Object> map : eleList) {
			// 取整
			double value = Double.valueOf(map.get("ELE_VALUE") == null ? "0"
					: map.get("ELE_VALUE").toString());
			value = DoubleUtils.getDoubleValue(value, 5);
			if (item == null && map.get("ELE_VALUE") != null) {
				if (dataType == 2) {
					item = new ChartItemWithTime(timeType, ChartNameUtils
							.getChartNameBean().getWaterName(), beginDate,
							endDate);
				} else if (dataType == 3) {
					item = new ChartItemWithTime(timeType, ChartNameUtils
							.getChartNameBean().getGasName(), beginDate,
							endDate);
				} else if (dataType == 4) {
					item = new ChartItemWithTime(timeType, ChartNameUtils
							.getChartNameBean().getHeatName(), beginDate,
							endDate);
				} else {
					item = new ChartItemWithTime(timeType, defaultChartName,
							beginDate, endDate);
				}
			}
			Date time = (Date) map.get("TIME_FIELD");
			if (time != null) {
				String timeString = DateUtil.convertDateToStr(time, timeType
						.getDateFormat());
				if (timeType == TimeEnum.WEEK)
					timeString = DateUtil.getMonday(timeString);
				Map<String, Object> map2 = new HashMap<String, Object>();    if(item != null){map2 = item.getMap();}
				if (map2.containsKey(timeString)) {
					map2.put(timeString, DataUtil.doubleAdd(Double.valueOf(map2.get(timeString)
							.toString()),
							value));
				} else
					map2.put(timeString, value);
			}
		}
		if (item != null)
			list.add(item);
	}

	/**
	 * 得到Excel，数据填充
	 * 
	 * @author 周礼
	 * @param 参数
	 *            table名字sheetName，输出流output，结果集map，页面请求的信息queryMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getEleExcel(String sheetName, OutputStream output,
			Map<String, Object> map, Map<String, Object> queryMap)
			throws UnsupportedEncodingException {
		List<ChartItemWithTime> temp = (List<ChartItemWithTime>) map
				.get("list");
		// SortedMap<Object, Object> result1 = temp.get(0).getMap();//尖
		// SortedMap<Object, Object> result2 = temp.get(1).getMap();//峰
		// SortedMap<Object, Object> result3 = temp.get(2).getMap();//平
		// SortedMap<Object, Object> result4 = temp.get(3).getMap();//谷
		SortedMap<String, Object> result1 = new TreeMap<String, Object>();
		SortedMap<String, Object> result2 = new TreeMap<String, Object>();
		SortedMap<String, Object> result3 = new TreeMap<String, Object>();
		SortedMap<String, Object> result4 = new TreeMap<String, Object>();
		if (temp.size() > 0) {
			result1 = temp.get(0).getMap();// 尖
		}
		if (temp.size() > 1) {
			result2 = temp.get(1).getMap();// 峰
		}
		if (temp.size() > 2) {
			result3 = temp.get(2).getMap();// 峰
		}
		if (temp.size() > 3) {
			result4 = temp.get(3).getMap();// 谷
		}
		// 电量单位名
		String q_unitname = "";
		int timeType = Integer.valueOf(queryMap.get("timeType").toString());
		int dataType = 0;
		if (queryMap.get("dataType") != null) {
			dataType = Integer.valueOf(queryMap.get("dataType").toString());
		}

		String name = "";
		// type类型 1表示分户2表示计量点，如果是分户name="总",计量点的话是计量点的名字.
		int type = Integer.valueOf(queryMap.get("type").toString());
		if (type == 1)
			name = "总";
		else
			name = dataQueryMapper.getMeterName(ChartConditionUtils
					.processDate(queryMap));
		// 计量点名称或分户名称
        int unitFlag = Integer.valueOf(queryMap.get("unitFlag").toString());
		if (timeType == 1) {
			if (dataType == 2) {
                if(unitFlag == 0){
                    q_unitname = "日水量(m³)";
                }
                else if(unitFlag == 1){
                    q_unitname = "日水量(km³)";
                }
                else if(unitFlag == 2){
                    q_unitname = "日水量(Mm³)";
                }
			}
            else if (dataType == 3) {
                if(unitFlag == 0){
                    q_unitname = "日气量(m³)";
                }
                else if(unitFlag == 1){
                    q_unitname = "日气量(km³)";
                }
                else if(unitFlag == 2){
                    q_unitname = "日气量(Mm³)";
                }
			}
            else if (dataType == 4) {
                if(unitFlag == 0){
                    q_unitname = "日热量(kWh)";
                }
                else if(unitFlag == 1){
                    q_unitname = "日热量(MWh)";
                }
                else if(unitFlag == 2){
                    q_unitname = "日热量(GWh)";
                }
			}
            else {
                if(unitFlag == 0){
                    q_unitname = "日电量(kWh)";
                }
                else if(unitFlag == 1){
                    q_unitname = "日电量(MWh)";
                }
                else if(unitFlag == 2){
                    q_unitname = "日电量(GWh)";
                }
			}
		}
		if (timeType == 4) {
			if (dataType == 2) {
                if(unitFlag == 0){
                    q_unitname = "周水量(m³)";
                }
                else if(unitFlag == 1){
                    q_unitname = "周水量(km³)";
                }
                else if(unitFlag == 2){
                    q_unitname = "周水量(Mm³)";
                }
			}
            else if (dataType == 3) {
                if(unitFlag == 0){
                    q_unitname = "周气量(m³)";
                }
                else if(unitFlag == 1){
                    q_unitname = "周气量(km³)";
                }
                else if(unitFlag == 2){
                    q_unitname = "周气量(Mm³)";
                }
			}
            else if (dataType == 4) {
                if(unitFlag == 0){
                    q_unitname = "周热量(kWh)";
                }
                else if(unitFlag == 1){
                    q_unitname = "周热量(MWh)";
                }
                else if(unitFlag == 2){
                    q_unitname = "周热量(GWh)";
                }
			}
            else {
                if(unitFlag == 0){
                    q_unitname = "周电量(kWh)";
                }
                else if(unitFlag == 1){
                    q_unitname = "周电量(MWh)";
                }
                else if(unitFlag == 2){
                    q_unitname = "周电量(GWh)";
                }
			}
		}
		if (timeType == 2) {
			if (dataType == 2) {
                if(unitFlag == 0){
                    q_unitname = "月水量(m³)";
                }
                else if(unitFlag == 1){
                    q_unitname = "月水量(km³)";
                }
                else if(unitFlag == 2){
                    q_unitname = "月水量(Mm³)";
                }
			}
            else if (dataType == 3) {
                if(unitFlag == 0){
                    q_unitname = "月气量(m³)";
                }
                else if(unitFlag == 1){
                    q_unitname = "月气量(km³)";
                }
                else if(unitFlag == 2){
                    q_unitname = "月气量(Mm³)";
                }
			}
            else if (dataType == 4) {
                if(unitFlag == 0){
                    q_unitname = "月热量(kWh)";
                }
                else if(unitFlag == 1){
                    q_unitname = "月热量(MWh)";
                }
                else if(unitFlag == 2){
                    q_unitname = "月热量(GWh)";
                }
			}
            else {
                if(unitFlag == 0){
                    q_unitname = "月电量(kWh)";
                }
                else if(unitFlag == 1){
                    q_unitname = "月电量(MWh)";
                }
                else if(unitFlag == 2){
                    q_unitname = "月电量(GWh)";
                }
			}
		}
		if (timeType == 3) {
			if (dataType == 2) {
                if(unitFlag == 0){
                    q_unitname = "年水量(m³)";
                }
                else if(unitFlag == 1){
                    q_unitname = "年水量(km³)";
                }
                else if(unitFlag == 2){
                    q_unitname = "年水量(Mm³)";
                }
			}
            else if (dataType == 3) {
                if(unitFlag == 0){
                    q_unitname = "年气量(m³)";
                }
                else if(unitFlag == 1){
                    q_unitname = "年气量(km³)";
                }
                else if(unitFlag == 2){
                    q_unitname = "年气量(Mm³)";
                }
			}
            else if (dataType == 4) {
                if(unitFlag == 0){
                    q_unitname = "年热量(kWh)";
                }
                else if(unitFlag == 1){
                    q_unitname = "年热量(MWh)";
                }
                else if(unitFlag == 2){
                    q_unitname = "年热量(GWh)";
                }
			}
            else {
                if(unitFlag == 0){
                    q_unitname = "年电量(kWh)";
                }
                else if(unitFlag == 1){
                    q_unitname = "年电量(MWh)";
                }
                else if(unitFlag == 2){
                    q_unitname = "年电量(GWh)";
                }
			}
		}

		// 声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 生成一个表格"分户电量"。
		HSSFSheet sheet = null;
		if (sheetName != null) {
			sheet = wb.createSheet(sheetName);
		} else {
			sheet = wb.createSheet("能管对象电量");
		}
		// 设置默认宽度为15字节
		sheet.setDefaultColumnWidth(15);

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

		// 生成一个表头样式
		// HSSFCellStyle style = wb.createCellStyle();
		// style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// //生成一个表头字体
		// HSSFFont font =wb.createFont();
		// font.setFontHeightInPoints((short) 12);
		// font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		//
		// //把字体应用到当前的样式
		// style.setFont(font);

		// 生成并设置另一个表格内容样式
		HSSFCellStyle style_ = wb.createCellStyle();
		style_.setRightBorderColor(HSSFColor.BLACK.index);
		style_.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style_.setLeftBorderColor(HSSFColor.BLACK.index);
		style_.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style_.setTopBorderColor(HSSFColor.BLACK.index);
		style_.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style_.setBottomBorderColor(HSSFColor.BLACK.index);
		style_.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style_.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 生成另一个字体
		HSSFFont font_ = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style_.setFont(font_);

		HSSFRow nameRow = sheet.createRow(0);
		String userName = queryMap.get("userName").toString();
		nameRow.createCell(0)
				.setCellValue(URLDecoder.decode(userName, "UTF-8"));// 用户名
		String typeName = queryMap.get("typeName").toString();
		nameRow.createCell(2)
				.setCellValue(URLDecoder.decode(typeName, "UTF-8"));// 类型

		// 生成表头，也就是第一行
		HSSFRow row = sheet.createRow(1);
		HSSFCell cellA = row.createCell(0);
		HSSFCell cellB = row.createCell(1);
		HSSFCell cellC = row.createCell(2);
		cellA.setCellStyle(titlestyle);
		cellB.setCellStyle(titlestyle);
		cellC.setCellStyle(titlestyle);
		cellA.setCellValue("名称");
		cellB.setCellValue("日期");
		cellC.setCellValue(q_unitname);

		// excel表格内容填充
		int i = 2;
		Set set = result1.keySet();
		Iterator<String> its = set.iterator();
		while (its.hasNext()) {
			// key日期
			String key = its.next();
			// value电量值
			Double val = new BigDecimal(Double.valueOf(result1.get(key) == null ? "0"
					: result1.get(key).toString()))
					.add(new BigDecimal(Double.valueOf(result2.get(key) == null ? "0" : result2
							.get(key).toString())))
					.add(new BigDecimal(Double.valueOf(result3.get(key) == null ? "0" : result3
							.get(key).toString())))
					.add(new BigDecimal(Double.valueOf(result4.get(key) == null ? "0" : result4
							.get(key).toString()))).doubleValue();
            if(unitFlag == 1){
                val = DataUtil.doubleDivide(val, 1000, 1);
            }
            else if(unitFlag == 2){
                val = DataUtil.doubleDivide(val, 1000000, 1);
            }
			HSSFRow row1 = sheet.createRow(i);
			HSSFCell cell1A = row1.createCell(0);
			HSSFCell cell1B = row1.createCell(1);
			HSSFCell cell1C = row1.createCell(2);
			cell1A.setCellStyle(style_);
			cell1B.setCellStyle(style_);
			cell1C.setCellStyle(style_);
			cell1A.setCellValue(name);
			cell1B.setCellValue(key);
			cell1C.setCellValue(val);
			i++;
		}
		try {
			output.flush();
			wb.write(output);
			output.close();
		} catch (IOException e) {
			Log.info("getEleExcel error IOException");
		}
	}

	@Override
	public List<Map<String, Object>> getPowData(Long ledgerId, Date baseDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ledgerId", ledgerId);
		Date startDate = DateUtil.clearDate(baseDate);
		Date endDate = baseDate;
		param.put("startDate", startDate);
		param.put("endDate", endDate);
		return this.dataQueryMapper.getLedger96ApData(param);
	}

	@Override
	public Map<String, Object> getHarData(Long ledgerId) {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Long> meterIds = this.eleAssessmentMapper
				.getTmeterByLedgerId(ledgerId);
		if (meterIds != null && meterIds.size() > 0) {
			Long meterId = meterIds.get(0);
			Map<String, Object> queryMap = new HashMap<String, Object>();
			Date nowDate = new Date();
			Date time = DateUtil
					.clearDate(DateUtil.getDateBetween(nowDate, -1));
			queryMap.put("Time", time);
			queryMap.put("curveType", 2);
			queryMap.put("meterId", meterId);
			
			Date endTime = time;
			Calendar cal_ = new GregorianCalendar( ) ;
			cal_.setTime( endTime ) ;
			cal_.add( Calendar.DAY_OF_MONTH , 1 ) ;	
			cal_.add( Calendar.SECOND , -1 ) ;	
			endTime =  cal_.getTime( ) ;
			queryMap.put( "endTime" , endTime );
			
			List<Map<String, Object>> har = this.DayHarService
					.getDayHarChartDatas(queryMap);
			List<Map<String, Object>> dis = this.DayHarService
					.getDayDisChartDatas(queryMap);
			result.put("Dis", dis);
			result.put("Har", har);
		}
		return result;
	}

	@Override
	public Double getCurrentTotalQ(Long ledgerId, Date beginTime, Date endTime) {
		double result = this.dataQueryMapper.getCurrentTotalQ(ledgerId,
				beginTime, endTime);
		return NumberUtil.formatDouble(result, NumberUtil.PATTERN_FLOAT);
	}

}
