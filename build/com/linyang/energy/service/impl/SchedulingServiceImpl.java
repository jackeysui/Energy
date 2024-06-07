package com.linyang.energy.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.leegern.util.NumberUtil;
import com.linyang.energy.model.*;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.ChartItem;
import com.linyang.energy.dto.ChartItemWithDatas;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.CurSataBean;
import com.linyang.energy.dto.FinanceBean;
import com.linyang.energy.dto.GradeBean;
import com.linyang.energy.dto.Item;
import com.linyang.energy.dto.ProductGradeBean;
import com.linyang.energy.dto.SeriesBean;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.dto.TimeFieldBean;
import com.linyang.energy.mapping.energyanalysis.SchedulingMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.service.SchedulingService;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.energy.utils.ChartConditionUtils;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;
import com.linyang.util.DoubleUtils;
/**
 * 排班分析业务逻辑层接口实现类
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 18, 2014
 */
@Service
public class SchedulingServiceImpl implements SchedulingService {
	
	/**
	 * 正在计算标识
	 */
	private transient static AtomicBoolean isCalculate = new AtomicBoolean(false);
	/**
	 * 计算时锁对象
	 */
	private transient final Object computeLock = new Object(); 
	
	@Autowired
	private SchedulingMapper schedulingMapper;
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	@Autowired
	private MeterManagerMapper meterManagerMapper;

	@Override
	public List<AssembleaBean> getAssembleInfo() {
		return schedulingMapper.getAssembleInfo();
	}

	@Override
	public List<Map<String, Object>> getSchedulerDetail(long scheduleId) {
		return schedulingMapper.getSchedulerDetail(scheduleId);
	}
	
	@Override
	public List<Map<String, Object>> getRatePrice(long assembleId) {
		List<Map<String, Object>> rateInfo = new ArrayList<Map<String,Object>>();
		Long assembleMeterId = schedulingMapper.getAssembleMeterId(assembleId);
		if(assembleMeterId != null){
			List<Map<String, Object>> list = schedulingMapper.getRateInfo(assembleMeterId);
			if(list != null)
				rateInfo.addAll(list);
		}
		return rateInfo;
	}
	@Override
	public Map<String, Object> getEnergyUseDatas(HttpServletRequest request,Map<String, Object> queryMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> energyUseDatas = schedulingMapper.getEnergyUseDatas(buildCondition(queryMap));
		if(CommonMethod.isCollectionNotEmpty(energyUseDatas)){
			Map<String, String> map2 = DictionaryDataFactory.getDictionaryData(request).get("device_type");
			//按照能源类型分类
			Map<Long,Map<Long,SeriesBean>> llMap = new HashMap<Long,Map<Long,SeriesBean>>();
			Map<Long,Item> pieMapping = new HashMap<Long, Item>();
			Set<String> legend = new LinkedHashSet<String>();
			for (Map<String, Object> map : energyUseDatas) {
				Long typeId = Long.valueOf(map.get("TYPE_ID").toString());
				Long gradeId = Long.valueOf(map.get("GRADE_ID").toString());
				String gradeName = map.get("GRADE_NAME").toString();
				legend.add(gradeName);
				double analyData = DoubleUtils.getDoubleValue(Double.valueOf(map.get("ANALY_DATA")==null?"0":map.get("ANALY_DATA").toString()),2);
				map.put("ANALY_DATA",analyData);
				if(llMap.containsKey(typeId)){
					Map<Long, SeriesBean> mm = llMap.get(typeId);
					if(mm.containsKey(gradeId)){
						mm.get(gradeId).getData().add(analyData);
					}else{
						SeriesBean seriesBean = new SeriesBean(gradeId,gradeName);
						seriesBean.getData().add(analyData);
						mm.put(gradeId, seriesBean);
					}
				}else{
					SeriesBean seriesBean = new SeriesBean(gradeId,gradeName);
					seriesBean.getData().add(analyData);
					Map<Long,SeriesBean> mm = new HashMap<Long, SeriesBean>();
					mm.put(gradeId, seriesBean);
					llMap.put(typeId,mm);
				}
				if(pieMapping.containsKey(gradeId)){
					pieMapping.get(gradeId).addValue(analyData);
				}else{
					pieMapping.put(gradeId, new Item(gradeName,analyData));
				}
			}
			List<String> xAxisDatas = new  ArrayList<String>();
			Map<Long,SeriesBean> mapList = new HashMap<Long,SeriesBean>();
			for (Map.Entry<Long,Map<Long,SeriesBean>> entry : llMap.entrySet()) {
				xAxisDatas.add(map2.get(String.valueOf(entry.getKey())));
				Map<Long, SeriesBean> value = entry.getValue();
				for (SeriesBean bean : value.values()) {
					if(mapList.containsKey(bean.getSeriesId())){
						mapList.get(bean.getSeriesId()).getData().addAll(bean.getData());
					}else{
						mapList.put(bean.getSeriesId(), bean);
					}
				}
			}
			result.put("legend",legend);
			result.put("xAxisDatas",xAxisDatas);
			result.put("list",mapList.values());
			//饼图
			result.put("pieDatas",pieMapping.values());
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> getProductsDatas(Map<String, Object> queryMap,long scheduleId) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> productsDatas = schedulingMapper.getProductsDatas(buildCondition(queryMap));
		if(CommonMethod.isCollectionNotEmpty(productsDatas)){
			List<Long> gradeIdList = getGroupIds(scheduleId);
			//按照产品分类
			Map<Long,Map<Long,SeriesBean>> llMap = new HashMap<Long,Map<Long,SeriesBean>>();
			Map<Long,Item> pieMapping = new HashMap<Long, Item>();
			Set<String> legend = new LinkedHashSet<String>();
			Set<String> xAxisDatas = new LinkedHashSet<String>();
			Map<Long,ProductGradeBean> mapping = new HashMap<Long,ProductGradeBean>();
			for (Map<String, Object> map : productsDatas) {
				Long productId = Long.valueOf(map.get("PRODUCT_ID").toString());
				String productName = map.get("PRODUCT_NAME").toString();
				Long gradeId = Long.valueOf(map.get("GRADE_ID").toString());
				String gradeName = map.get("GRADE_NAME").toString();
				legend.add(gradeName);
				xAxisDatas.add(productName);
				double analyData = DoubleUtils.getDoubleValue(Double.valueOf(map.get("ANALY_DATA")==null?"0":map.get("ANALY_DATA").toString()),2);
				map.put("ANALY_DATA",analyData);
				if(llMap.containsKey(productId)){
					Map<Long, SeriesBean> mm = llMap.get(productId);
					if(mm.containsKey(gradeId)){
						mm.get(gradeId).getData().add(analyData);
					}else{
						SeriesBean seriesBean = new SeriesBean(gradeId,gradeName);
						seriesBean.getData().add(analyData);
						mm.put(gradeId, seriesBean);
					}
				}else{
					SeriesBean seriesBean = new SeriesBean(gradeId,gradeName);
					seriesBean.getData().add(analyData);
					Map<Long,SeriesBean> mm = new HashMap<Long, SeriesBean>();
					mm.put(gradeId, seriesBean);
					llMap.put(productId,mm);
				}
				if(pieMapping.containsKey(productId)){
					pieMapping.get(productId).addValue(analyData);
				}else{
					pieMapping.put(productId, new Item(productName,analyData));
				}
				
				GradeBean gradeBean = new GradeBean(gradeId,gradeName,analyData);
				if(mapping.containsKey(productId)){
					mapping.get(productId).addBean(gradeBean);
				}else{
					ProductGradeBean productGradeBean = new ProductGradeBean(productId,productName,gradeIdList);
					productGradeBean.addBean(gradeBean);
					mapping.put(productId,productGradeBean);
				}
			}
			Map<Long,SeriesBean> mapList = new HashMap<Long,SeriesBean>();
			for (Map.Entry<Long,Map<Long,SeriesBean>> entry : llMap.entrySet()) {
				Map<Long, SeriesBean> value = entry.getValue();
				for (SeriesBean bean : value.values()) {
					if(mapList.containsKey(bean.getSeriesId())){
						mapList.get(bean.getSeriesId()).getData().addAll(bean.getData());
					}else{
						mapList.put(bean.getSeriesId(), bean);
					}
				}
			}
			result.put("legend",legend);
			result.put("xAxisDatas",xAxisDatas);
			result.put("list",mapList.values());
			result.put("products",buildList(mapping.values()));
			//饼图
			result.put("pieDatas",pieMapping.values());
		}
		return result;
	}

	
	public List<Long> getGroupIds(long scheduleId){
		List<Map<String, Object>> schedulerDetail = schedulingMapper.getSchedulerDetail(scheduleId);
		List<Long> list = new ArrayList<Long>();
		if(CommonMethod.isCollectionNotEmpty(schedulerDetail)){
			for (Map<String, Object> map : schedulerDetail) {
				list.add(Long.valueOf(map.get("GRADE_ID").toString()));
			}
		}
		return list;
	}
	
	@Override
	public Map<String, Object> getQStat(Map<String, Object> queryMap) {
		Map<String,Object> result = new HashMap<String, Object>();
		String time = queryMap.get("time").toString();
		queryMap.put("beginTime",DateUtils.convertShortDateStr2Date(time));
		queryMap.put("endTime",DateUtils.convertLongDateStr2Date(time+" 23:59:59"));
		long assembleId = Long.valueOf(queryMap.get("assembleId").toString());
		long scheduleId = Long.valueOf(queryMap.get("scheduleId").toString());
		List<Map<String, Object>> list = schedulingMapper.getQStat(queryMap);
		int flag = 1;
		if(CommonMethod.isCollectionNotEmpty(list)){
			CurSataBean curSataBean = new CurSataBean(time,WebConstant.density);
			Map<Long, Double> timeMap = curSataBean.getTimeMap();
			List<Map<String, Object>> ratePrice = getRatePrice(assembleId); 
			if(!CommonMethod.isCollectionNotEmpty(ratePrice)){
				flag = 2;
				result.put("flag", flag);
				return result;
			}
				
			for (Map<String, Object> map : list) {
				long timeField = ((Date)map.get("TIME_FIELD")).getTime()/1000;
				Double valueField = Double.valueOf(map.get("VALUE_FIELD")==null?"0":map.get("VALUE_FIELD").toString());
				if(map.get("VALUE_FIELD")!= null )
					valueField = DataUtil.doubleMultiply(valueField, caclulate(ratePrice,timeField,time));
				timeMap.put(timeField,valueField);
			}
			List<Map<String, Object>> schedulerDetail = getSchedulerDetail(scheduleId);
			if(!CommonMethod.isCollectionNotEmpty(schedulerDetail)){
				flag = 3;
				result.put("flag", flag);
				return result;
			}
			List<ChartItemWithDatas> listCharts = groupScheduler(schedulerDetail,curSataBean,time);
			double totalMoney = processCharts(listCharts,curSataBean.buildResult().getMap());
			result.put("list", listCharts);
			result.put("xAxisDatas", curSataBean.getMap().keySet());
			result.put("totalMoney",DoubleUtils.getDoubleValue(totalMoney, 2) );
		}
		result.put("flag", flag);
		return result;
	}
	
	@Override
	public Map<String, Object> getFutureQStat(FinanceBean bean) {
		bean.buildTime();
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		String time = bean.getTime();
		Long assembleId = bean.getAssembleId();
		Long scheduleId = bean.getScheduleId();
		queryMap.put("assembleId",assembleId.toString());
		queryMap.put("scheduleId",scheduleId.toString());
		queryMap.put("beginTime",DateUtils.convertShortDateStr2Date(time));
		Date endTime = DateUtils.convertLongDateStr2Date(time+" 23:59:59");
		queryMap.put("endTime",endTime);
		List<Map<String, Object>> list = schedulingMapper.getQStat(queryMap);
		int flag = 1;
		if(CommonMethod.isCollectionNotEmpty(list)){
			CurSataBean curSataBean = new CurSataBean(time,WebConstant.density);
			Map<Long, Double> timeMap = curSataBean.getTimeMap();
			List<Map<String, Object>> ratePrice = getRatePrice(assembleId); 
			if(!CommonMethod.isCollectionNotEmpty(ratePrice)){
				flag = 2;
				result.put("flag", flag);
				return result;
			}
			for (Map<String, Object> map : list) {
				long timeField = ((Date)map.get("TIME_FIELD")).getTime()/1000;
				Double valueField = Double.valueOf(map.get("VALUE_FIELD")==null?"0":map.get("VALUE_FIELD").toString());
				timeMap.put(timeField,valueField);
			}
			List<Map<String, Object>> schedulerDetail = getSchedulerDetail(scheduleId);
			if(!CommonMethod.isCollectionNotEmpty(schedulerDetail)){
				flag = 3;
				result.put("flag", flag);
				return result;
			}
			
			List<TimeFieldBean> oldTimeList = convertSchedulerBeanList(time,schedulerDetail);
			List<Long>  differenceList = new ArrayList<Long>();
			//差值，如果大于0表示后移了(后面超出)，如果小于0表示前移(前面超出了)
			long difference = 0;
			for (int i = 0; i < oldTimeList.size(); i++) {
				TimeFieldBean oldTimeFieldBean = oldTimeList.get(i);
				TimeFieldBean fieldBean = bean.getTimeList().get(i);
				if(oldTimeFieldBean.equals(fieldBean)){
					differenceList.add(0L);
					continue;
				}
				
				if(fieldBean.getLongTime2() >= fieldBean.getLongTime1()){
					 difference = fieldBean.getLongTime1()-oldTimeFieldBean.getLongTime1();
				}else{
					 difference = fieldBean.getLongTime2()-oldTimeFieldBean.getLongTime2();
				}
				differenceList.add(difference);
			}
			List<ChartItemWithDatas> listChartDatas = new ArrayList<ChartItemWithDatas>();
			ChartItemWithDatas chart = null;
			double totalMoney = 0;
			int size = bean.getTimeList().size();
			for (int i = 0; i < size; i++) {
				TimeFieldBean timeFieldBean = bean.getTimeList().get(i);
				chart = new ChartItemWithDatas(timeFieldBean.getGradeName());
				long longTime1 = timeFieldBean.getLongTime1();
				long longTime2 = timeFieldBean.getLongTime2();
				if(longTime1 <= longTime2){//直接不需要跨天
					//一共的点数
					int num =(int) (longTime2- longTime1)/(WebConstant.density*60);
					for (int j = 0; j < num; j++) {
						long key = longTime1+j*WebConstant.density*60;
						Double temp = timeMap.get(key-differenceList.get(i));
						if(temp != null){
							double money = DataUtil.doubleMultiply(temp, caclulate(ratePrice, key, time));
							totalMoney = DataUtil.doubleAdd(totalMoney, money);
							chart.getMap().put(key+"",money);
						}
					}
				}else{//需要跨天(difference <0表示是前移，>0表示是后移)
					//一共的点数
					int num =(int) ((longTime2+86400)- longTime1)/(WebConstant.density*60);
					for (int j = 0; j < num; j++) {
						long key = longTime1+j*WebConstant.density*60;
						long m = key-differenceList.get(i);
						if(m > endTime.getTime()/1000)
							m = m-86400;
						Double temp = timeMap.get(m);
						if(temp != null){
							if(key > endTime.getTime()/1000)
								key = key-86400;
							double money = DataUtil.doubleMultiply(temp, caclulate(ratePrice, key, time));
							totalMoney = DataUtil.doubleAdd(totalMoney, money);
							chart.getMap().put(key+"",money);
						}
					}
				}
				TreeMap<String,Object> treeMap = new TreeMap<String,Object>();
				for (Long key : timeMap.keySet()) {
					SortedMap<String, Object> map = chart.getMap();
					if(!map.containsKey(key))
						map.put(key+"", "-");
				}
				for (Object key : chart.getMap().keySet()) {
					treeMap.put(DateUtils.convertTimeToString(Long.valueOf(key.toString()), "HH:mm"), chart.getMap().get(key));
				}
				listChartDatas.add(chart.setDatas(treeMap));
			}
			
			
			result.put("list", listChartDatas);
			result.put("xAxisDatas", listChartDatas.get(0).getMap().keySet());
			result.put("totalMoney",DoubleUtils.getDoubleValue(totalMoney, 2));
		}
		result.put("flag", flag);
		return result;
	}
	
	private List<TimeFieldBean> convertSchedulerBeanList(String day,List<Map<String, Object>> schedulerDetail) {
		List<TimeFieldBean> list = new ArrayList<TimeFieldBean>();
		for (Map<String, Object> map : schedulerDetail) {
			TimeFieldBean bean = new TimeFieldBean();
			map.get("START_TIME").toString();
			bean.setGradeId( Long.valueOf(map.get("GRADE_ID").toString()));
			bean.setStartTime(map.get("START_TIME").toString());
			bean.setEndTime(map.get("END_TIME").toString());
			bean.buildTime(day);
			
			list.add(bean);
		}
		return list;
	}

	/**
	 * 是否正在计算
	 * @return
	 */
	@Override
	public boolean isComputing(){
		boolean b = isCalculate.get();
		Log.info("isCalculate:"+b);
		return b;
	}

	@Override
	public boolean computingOptimalSolution(Map<String, Object> queryMap) {
		boolean isSuccess = false;
		Log.info("开始执行计算最优解方法");
		if(!isCalculate.get()){
			Log.info("进入方法...");
			//重入锁
			synchronized (computeLock) {
				isCalculate.set(true);
//				try{
//					
//				}catch (Exception e) {
//					
//				}finally{
//					isCalculate.set(false);
//				}
				
				
				
				
				isCalculate.set(false);


				
				
				
			}
			isSuccess = true;
		}
		Log.info("计算最优解方法执行结束");
		return isSuccess;
	}

	@Override
	public Map<String, Object> getOptimalSolution() {
		
		return null;
	}
	
	
	@Override
	public Map<String, Object> getPowerFactorDatas(Map<String, Object> queryMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean isHasData = false;
		List<ChartItem> list = new ArrayList<ChartItem>();
		queryMap = ChartConditionUtils.processDate(queryMap);
		TimeEnum timeType =  TimeEnum.formInt2TimeEnum(Integer.valueOf(queryMap.get("timeType").toString()));
		Integer tType = Integer.valueOf(queryMap.get("timeType").toString());//时间类型
		//对象类型：1-EMO;2-DCP;
		Integer oType = Integer.valueOf(queryMap.get("oType").toString());
        Integer isVirtual = 0;
        long objectId = Long.valueOf(queryMap.get("objectId").toString());
		long ledgerId = objectId;
		if(oType == 2){
            MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(objectId);
			ledgerId = meterBean.getLedgerId();
            isVirtual = meterBean.getIsVirtual();
		}
		queryMap.put("ledgerId", ledgerId);
        queryMap.put("isVirtual", isVirtual);
		//得到一个分户的标准功率因数
		Double factor = schedulingMapper.getThresholdValue(ledgerId);
		//标准功率因数曲线
		if(factor != null){
			isHasData = true;
			ChartItemWithTime chart2 = new ChartItemWithTime(
				timeType,
				queryMap.get("cosName").toString(),
				((Date)queryMap.get("beginTime")).getTime()/1000,
				((Date)queryMap.get("endTime")).getTime()/1000,
				factor
			);
			if(timeType == TimeEnum.MINUTE)
				chart2.convertMinuteMap();
			list.add(chart2);
			result.put("legend", chart2.getMap().keySet());
		}
		
		FactorBean factorQ = schedulingMapper.getFactorQ(queryMap);
		if(factorQ == null){
			factorQ = new FactorBean();
		}
		
		//标准cosφ
		factorQ.setFactor(factor);
		//功率因数
		factorQ.setPowerFactor(DataUtil.getPF(factorQ.getTotalFaq(), factorQ.getTotalFrq()) );
		if(factorQ.getPowerFactor() != null) {
			factorQ.setRate(schedulingMapper.getFactor(factorQ.getPowerFactor(), factor));
		}
		factorQ.setQcost(schedulingMapper.getLastMonthCost(DateUtils.getPreMonthFirstDayDate(), ledgerId));
		result.put("bean", CommonMethod.convertBean2Map(factorQ));
		
		//获取功率因数曲线
		List<Map<String, Object>> chartDatas = schedulingMapper.getChartDatas(queryMap);
		ChartItemWithTime factorChart = new ChartItemWithTime(timeType,queryMap.get("name").toString(),((Date)queryMap.get("beginTime")).getTime()/1000,((Date)queryMap.get("endTime")).getTime()/1000);
		if(CommonMethod.isCollectionNotEmpty(chartDatas)){
			isHasData = true;
//			ChartItemWithTime factorChart = new ChartItemWithTime(timeType,queryMap.get("name").toString(),((Date)queryMap.get("beginTime")).getTime()/1000,((Date)queryMap.get("endTime")).getTime()/1000);
			for (Map<String, Object> map : chartDatas) {
				double value = 0D;
				if(oType == 2 && (tType == 1 || tType ==2 )) {
					double faq = Double.valueOf(map.get("FAQ")==null?"0":map.get("FAQ").toString());
					double frq = Double.valueOf(map.get("FRQ")==null?"0":map.get("FRQ").toString());
					value = DataUtil.getPF(faq, frq);
				}else {
					value = Double.valueOf(map.get("FACTOR")==null?"0":map.get("FACTOR").toString());
				}
				Date time = (Date)map.get("TIME");
				if(time != null ){
					String timeString = DateUtil.convertDateToStr(time,timeType.getDateFormat());
					Map<String, Object> map2 = factorChart.getMap();
					if(map2.containsKey(timeString)){
						map2.put(timeString, DataUtil.doubleAdd(Double.valueOf(map2.get(timeString).toString()), value));
					}
				}
			}
			if(timeType == TimeEnum.MINUTE)
				factorChart.convertMinuteMap();
			factorChart.dataScale();			
			result.put("legend", factorChart.getMap().keySet());
		}
		list.add(factorChart);
		
		//获取有功功率曲线,只有类型为分的才有
		if(tType == 6){
			List<Map<String, Object>> aPDatas = schedulingMapper.getAPChartDatas(queryMap);
			if(CommonMethod.isCollectionNotEmpty(aPDatas)){
				ChartItemWithTime apChart = new ChartItemWithTime(
						timeType,
						"有功功率",
						((Date)queryMap.get("beginTime")).getTime()/1000,
						((Date)queryMap.get("endTime")).getTime()/1000);
				for (Map<String, Object> map : aPDatas) {
					double value = Double.valueOf(map.get("AP")==null?"0":map.get("AP").toString());
					Date time = (Date)map.get("TIME");
					if(time != null ){
						String timeString = DateUtil.convertDateToStr(time,timeType.getDateFormat());
						Map<String, Object> map2 = apChart.getMap();
						if(map2.containsKey(timeString)){
							map2.put(timeString, DataUtil.doubleAdd(Double.valueOf(map2.get(timeString).toString()), value));
						}
					}
				}
				if(timeType == TimeEnum.MINUTE)
					apChart.convertMinuteMap();
				apChart.dataScale();
				list.add(apChart);
				result.put("legend", apChart.getMap().keySet());
			}
		}
		result.put("list", list);
		result.put("isHasData", isHasData);
		return result;
	}

	@Override
	public Map<String, Object> getPowerFactorStat(Map<String, Object> queryMap) {
        Date baseDate = WebConstant.getChartBaseDate();
		Map<String, Object> result = new HashMap<String, Object>();
		long timeType = Long.valueOf(queryMap.get("timeType").toString());
		if(timeType == 1){//昨日
			Date yeatday = DateUtils.convertShortDateStr2Date(DateUtils.getBeforeAfterDate(DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT), -1));
			queryMap.put("beginTime", yeatday);
			queryMap.put("endTime", yeatday);
			Date yyeatday = DateUtils.convertShortDateStr2Date(DateUtils.getBeforeAfterDate(DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT), -2));
			queryMap.put("beginTime2", yyeatday);
			queryMap.put("endTime2", yyeatday);
		}else if(timeType == 2){//上月
			queryMap.put("beginTime", DateUtils.getPreMonthFirstDayDate());
			queryMap.put("endTime", DateUtils.getPreMonthLastDayDate());
			queryMap.put("beginTime2", DateUtil.getPrePreMonthFirstDay(baseDate));
			queryMap.put("endTime2", DateUtil.getPrePreMonthLastDay(baseDate));
		}
		List<PowerFactorStatBean> powerFactorStat = schedulingMapper.getPowerFactorStat(queryMap);
		if(CommonMethod.isCollectionNotEmpty(powerFactorStat)){
			List<PowerFactorStatBean> temp1 = new ArrayList<PowerFactorStatBean>();
			List<PowerFactorStatBean> temp2 = new ArrayList<PowerFactorStatBean>();
			for (PowerFactorStatBean powerFactorStatBean : powerFactorStat) {
				if(powerFactorStatBean.getType()==1)
					temp1.add(powerFactorStatBean);
				else
					temp2.add(powerFactorStatBean);
			}
			result.put("list1", temp1);
			result.put("list2", temp2);
		}
		List<PowerFactorStatBean> powerFactorStat2 = schedulingMapper.getPowerFactorStat2(queryMap);
		if(CommonMethod.isCollectionNotEmpty(powerFactorStat2)){
			List<PowerFactorStatBean> temp1 = new ArrayList<PowerFactorStatBean>();
			List<PowerFactorStatBean> temp2 = new ArrayList<PowerFactorStatBean>();
			for (PowerFactorStatBean powerFactorStatBean : powerFactorStat2) {
				if(powerFactorStatBean.getType()==1)
					temp1.add(powerFactorStatBean);
				else
					temp2.add(powerFactorStatBean);
			}
			result.put("list3", temp1);
			result.put("list4", temp2);
		}
		return result;
	}
	
	
	@Override
	public Double getFactor(double pf, double rate) {
		return schedulingMapper.getFactor(pf, rate);
	}

	private Map<String,Object> buildCondition(Map<String,Object> queryMap){
		long beginDate = Long.valueOf(queryMap.get("beginDate").toString());
		long endDate =  Long.valueOf(queryMap.get("endDate").toString());
		queryMap.put("beginTime", new Date(beginDate*1000));
		queryMap.put("endTime", new Date(endDate*1000));
		return queryMap;
	}

	
	private Collection<ProductGradeBean> buildList(Collection<ProductGradeBean> collection){
		if(CommonMethod.isCollectionNotEmpty(collection)){
			for (ProductGradeBean productGradeBean : collection) {
				productGradeBean.add2List();
			}
		}
		
		return collection;
	}

	private double caclulate(List<Map<String, Object>> ratePrice,long time,String day){
		double value = 0;
		if(CommonMethod.isCollectionNotEmpty(ratePrice)){
			for (Map<String, Object> map : ratePrice) {
				long beginTime =DateUtils.convertTimeToLong(day +" "+map.get("BEGIN_TIME").toString(), "yyyy-MM-dd HH:mm");
				long endTime =DateUtils.convertTimeToLong(day +" "+map.get("END_TIME").toString(), "yyyy-MM-dd HH:mm");
				if(beginTime > endTime){
					endTime = 86400+endTime;
					if(time + 86400 < endTime)
						time += 86400;
				}
				if(time >= beginTime && time <= endTime){
					value = Double.valueOf(map.get("RATE_VALUE").toString()); 
					break;
				}else{
					continue;
				}
			}
		}
		return value;
	}
	
	private List<ChartItemWithDatas> groupScheduler(List<Map<String, Object>> schedulerDetail,CurSataBean curSataBean,String day){
		Map<Long,ChartItemWithDatas> mapping = new HashMap<Long, ChartItemWithDatas>();
		if(CommonMethod.isCollectionNotEmpty(schedulerDetail)&& !curSataBean.getTimeMap().isEmpty()){
			for (Map<String, Object> map : schedulerDetail) {
				mapping.put(Long.valueOf(map.get("GRADE_ID").toString()), new ChartItemWithDatas(map.get("GRADE_NAME").toString()));
			}
			for (Map.Entry<Long, Double> entry : curSataBean.getTimeMap().entrySet()) {
				Long key = entry.getKey();
				Double value = entry.getValue();
				for (Map<String, Object> map : schedulerDetail) {
					Long gradeId = Long.valueOf(map.get("GRADE_ID").toString());
					long beginTime =DateUtils.convertTimeToLong(day +" "+map.get("START_TIME").toString(), "yyyy-MM-dd HH:mm");
					long endTime =DateUtils.convertTimeToLong(day +" "+map.get("END_TIME").toString(), "yyyy-MM-dd HH:mm");
					if(key >= beginTime && key <endTime){
						mapping.get(gradeId).getMap().put(DateUtils.convertTimeToString(key, "HH:mm"), value);
					}
				}
			}
		}
		return new ArrayList<ChartItemWithDatas>(mapping.values());
	}
	

	private double processCharts(List<ChartItemWithDatas> listCharts,TreeMap<String, Double> map) {
		BigDecimal totalMoney = BigDecimal.ZERO;
		for (String key : map.keySet()) {
			for (ChartItemWithDatas chartItemWithDatas : listCharts) {
				chartItemWithDatas.dataScale();
				if(!chartItemWithDatas.getMap().containsKey(key)){
					chartItemWithDatas.getMap().put(key, "-");
				}else{
					totalMoney = totalMoney.add(new BigDecimal(Double.valueOf(chartItemWithDatas.getMap().get(key).toString())));
				}
			}
		}
		return totalMoney.doubleValue();
	}
	
	@Override
	public Map<String, Object> getLoadDataStat(Map<String, Object> queryMap) {
		long meterId = Long.valueOf(queryMap.get("meterId").toString());
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		int treeType = Integer.parseInt(queryMap.get("treeType").toString());

		Map<String, Object> data = new HashMap<String, Object>();

		Double epwr = null;
		if (treeType == 1) {
			epwr = schedulingMapper.getLedgerEPwr(meterId);
		}else {
			epwr = schedulingMapper.getPointEPwr(meterId);
		}
		if (epwr != null)
			data.put("power", epwr);

		Date nDate = com.linyang.energy.utils.DateUtil.clearDate(WebConstant.getChartBaseDate());
		Date preYearDate = DateUtil.getLastYearDate(nDate);
		Date nDate30 = DateUtil.addDateDay(nDate, -30);

		Map<String, Object> yearData = null;
		if (treeType == 1) {
			yearData = schedulingMapper.getLedgerMaxMinLoadData(meterId, preYearDate, nDate);
		}else {
			yearData = schedulingMapper.getMaxMinLoadData(meterId, preYearDate, nDate);
		}
		if (yearData != null && yearData.size() > 0) {
			if (yearData.get("MAXLOAD") != null) {
				data.put("yearMaxLoad", Double.valueOf(yearData.get("MAXLOAD").toString()));
			}
			if (yearData.get("MINLOAD") != null) {
				data.put("yearMinLoad", Double.valueOf(yearData.get("MINLOAD").toString()));
			}
		}
		Double avgYear = null;
		if (treeType == 1) {
			avgYear = schedulingMapper.getLedgerAvgLoadMax(meterId, preYearDate, nDate);
		}else {
			avgYear = schedulingMapper.getAvgLoadMax(meterId, preYearDate, nDate);
		}
		if (avgYear != null)
			data.put("yearAvgMaxLoad", avgYear);
		
		Map<String, Object> currData = null;
		if (treeType == 1) {
			currData = schedulingMapper.getLedgerMaxMinLoadData(meterId, startDate, endDate);
		} else {
			currData = schedulingMapper.getMaxMinLoadData(meterId, startDate, endDate);
		}
			
		Double load;
		Date occTime;
		if (currData != null && currData.size() > 0) {
			if (currData.get("MAXLOAD") != null) {
				load = Double.valueOf(currData.get("MAXLOAD").toString());
				data.put("currMaxLoad", load);
				if (treeType == 1) {
					occTime = schedulingMapper.getLedgerMaxLoadTime(meterId, startDate, endDate);
				} else {
					occTime = schedulingMapper.getMaxLoadTime(load, meterId, startDate, endDate);
				}
				if (occTime != null)
					data.put("currMaxLoadTime", DateUtil.convertDateToStr(occTime, DateUtil.DEFAULT_PATTERN));
			}
			if (currData.get("MINLOAD") != null) {
				load = Double.valueOf(currData.get("MINLOAD").toString());
				data.put("currMinLoad", load);
				if (treeType == 1) {
					occTime = schedulingMapper.getLedgerMinLoadTime(meterId, startDate, endDate);
				} else {
					occTime = schedulingMapper.getMinLoadTime(load, meterId, startDate, endDate);
				}
				if (occTime != null)
					data.put("currMinLoadTime", DateUtil.convertDateToStr(occTime, DateUtil.DEFAULT_PATTERN));
			}
		}
		Map<String, Object> preData = null;
		if (treeType == 1) {
			preData = schedulingMapper.getLedgerMaxMinLoadData(meterId, nDate30, nDate);
		} else {
			preData = schedulingMapper.getMaxMinLoadData(meterId, nDate30, nDate);
		}
		if (preData != null && preData.size() > 0) {
			if (preData.get("MAXLOAD") != null) {
				data.put("maxLoad30", Double.valueOf(preData.get("MAXLOAD").toString()));
			}
		}
		Double avgMonth = null;
		if (treeType == 1) {
			avgMonth = schedulingMapper.getLedgerAvgLoadMax(meterId, nDate30, nDate);
		} else {
			avgMonth = schedulingMapper.getAvgLoadMax(meterId, nDate30, nDate);
		}
		if (avgMonth != null)
			data.put("monthAvgMaxLoad", avgMonth);

		Double avgCurr = null;
		if (treeType == 1) {
			avgCurr = schedulingMapper.getLedgerAvgLoadMax(meterId, startDate, endDate);
		} else {
			avgCurr = schedulingMapper.getAvgLoadMax(meterId, startDate, endDate);
		}
		if (avgCurr != null)
			data.put("currAvgMaxLoad", avgCurr);

		return data;
	}

	@Override
	public List<Map<String, Object>> getCurveLoadData(Map<String, Object> queryMap) {
		long meterId = Long.valueOf(queryMap.get("meterId").toString());
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		int treeType = Integer.parseInt(queryMap.get("treeType").toString());

        int dataSource = Integer.parseInt(queryMap.get("dataSource").toString());
        String noIncludes = queryMap.get("noIncludes").toString();
        List<String> dayList = null;
        if(noIncludes.length()>0){
            dayList = new ArrayList<String>();
            String[] days = noIncludes.split(",");
            for(int i = 0; i < days.length; i++){
                dayList.add(days[i]);
            }
        }

		List<Map<String, Object>> loadData = null;
		if (treeType == 1) {//分户
			loadData = schedulingMapper.getLedgerLoadData(meterId, startDate, endDate);
		}
        else {
			loadData = schedulingMapper.getLoadData(meterId, startDate, endDate, dataSource, dayList);
		}
		return loadData;
	}

	@Override
	public List<Map<String, Object>> getCurveLoadDataTime(Map<String, Object> queryMap) {
		long meterId = Long.valueOf(queryMap.get("meterId").toString());
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		int treeType = Integer.parseInt(queryMap.get("treeType").toString());

        int dataSource = Integer.parseInt(queryMap.get("dataSource").toString());
        String noIncludes = queryMap.get("noIncludes").toString();
        List<String> dayList = null;
        if(noIncludes.length()>0){
            dayList = new ArrayList<String>();
            String[] days = noIncludes.split(",");
            for(int i = 0; i < days.length; i++){
                dayList.add(days[i]);
            }
        }

		List<Map<String, Object>> loadTimeData = null;
		if (treeType == 1) {//分户
			loadTimeData = schedulingMapper.getLedgerLoadTimeData(meterId, startDate, endDate);
		}
        else {
			loadTimeData = schedulingMapper.getLoadTimeData(meterId, startDate, endDate, dataSource, dayList);
		}
		return loadTimeData;
	}

    @Override
    public List<Map<String, Object>> getCurveDistribution(Map<String, Object> queryMap) {
        long meterId = Long.valueOf(queryMap.get("meterId").toString());
        Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
        Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
        int treeType = Integer.parseInt(queryMap.get("treeType").toString());

        String noIncludes = queryMap.get("noIncludes").toString();
        List<String> dayList = null;
        if(noIncludes.length()>0){
            dayList = new ArrayList<String>();
            String[] days = noIncludes.split(",");
            for(int i = 0; i < days.length; i++){
                dayList.add(days[i]);
            }
        }

        List<Map<String, Object>> loadData = null;
        if (treeType == 2) {
            loadData = schedulingMapper.getDistribution(meterId, startDate, endDate, dayList);
        }

        return loadData;
    }

	// edit by chengq 20151207，增加EMO电流日不平衡度计算  1分户  2计量点
	@Override
	public Map<String, Object> getIUDataStat(Map<String, Object> queryMap) {
		
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		Map<String, Object> data = new HashMap<String, Object>();
		// 获取匹配数据的计量点ID
		long pointId = this.getTargetPointId4Iu(queryMap);
		if(pointId == -1)
			return data;
		// 根据计量点ID获取数据
		Double ecur = schedulingMapper.getPointECur(pointId);
		if (ecur != null)
			data.put("cur", ecur);
		Double maxi = schedulingMapper.getMaxUnI(pointId, startDate, endDate);
		if (maxi != null) {
			data.put("maxUI", maxi);
			Date maxT = schedulingMapper.getMaxUnTimeI(maxi, pointId, startDate, endDate);
			if (maxT != null)
				data.put("maxUITime", DateUtil.convertDateToStr(maxT, DateUtil.DEFAULT_PATTERN));
		}
		Long ul = schedulingMapper.getSumUnLimitI(pointId, startDate, endDate);
		if (ul != null)
			data.put("iLimit", ul);
		return data;
	}

	@Override
	public List<Map<String, Object>> getIULimitData(Map<String, Object> queryMap) {
		
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		long pointId = this.getTargetPointId4Iu(queryMap);
		if(pointId == -1)
			return new ArrayList<Map<String, Object>>();
		return schedulingMapper.getIULimitData(pointId, startDate, endDate);
	}

	@Override
	public List<Map<String, Object>> getIUMaxData(Map<String, Object> queryMap) {
		
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		long pointId = this.getTargetPointId4Iu(queryMap);
		if(pointId == -1)
			return new ArrayList<Map<String, Object>>();
		return schedulingMapper.getIUMaxData(pointId, startDate, endDate);
	}

	@Override
	public List<Map<String, Object>> getIUMaxTimeData(Map<String, Object> queryMap) {
		
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		long pointId = this.getTargetPointId4Iu(queryMap);
		if(pointId == -1)
			return new ArrayList<Map<String, Object>>();
		return schedulingMapper.getIUMaxTimeData(pointId, startDate, endDate);
	}

	// edit by chengq 20151207，增加EMO电压日不平衡度计算  1分户  2计量点
	@Override
	public Map<String, Object> getVUDataStat(Map<String, Object> queryMap) {
		
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		Map<String,Object> data = new HashMap<String, Object>();
		// 获取匹配数据的计量点ID
		long pointId = this.getTargetPointId4Vu(queryMap);
		if(pointId == -1)
			return data;
		// 根据计量点ID获取数据
		Double evol = schedulingMapper.getPointEVol(pointId);
		if (evol != null)
			data.put("vol", evol);
		Double maxv = schedulingMapper.getMaxUnV(pointId, startDate, endDate);
		if (maxv != null) {
			data.put("maxUV", maxv);
			Date maxT = schedulingMapper.getMaxUnTimeV(maxv, pointId, startDate, endDate);
			if (maxT != null)
				data.put("maxUVTime", DateUtil.convertDateToStr(maxT, DateUtil.DEFAULT_PATTERN));
		}
		Long ul = schedulingMapper.getSumUnLimitV(pointId, startDate, endDate);
		if (ul != null)
			data.put("iLimit", ul);
		return data;
	}
	
	@Override
	public List<Map<String, Object>> getVULimitData(Map<String, Object> queryMap) {
		//long pointId = Long.valueOf(queryMap.get("pointId").toString());
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		long pointId = this.getTargetPointId4Vu(queryMap);
		if(pointId == -1)
			return new ArrayList<Map<String, Object>>();
		return schedulingMapper.getVULimitData(pointId, startDate, endDate);
	}

	@Override
	public List<Map<String, Object>> getVUMaxData(Map<String, Object> queryMap) {
		
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		long pointId = this.getTargetPointId4Vu(queryMap);
		if(pointId == -1)
			return new ArrayList<Map<String, Object>>();
		return schedulingMapper.getVUMaxData(pointId, startDate, endDate);
	}

	@Override
	public List<Map<String, Object>> getVUMaxTimeData(Map<String, Object> queryMap) {
		
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		long pointId = this.getTargetPointId4Vu(queryMap);
		if(pointId == -1)
			return new ArrayList<Map<String, Object>>();
		return schedulingMapper.getVUMaxTimeData(pointId, startDate, endDate);
	}

	@Override
	public List<LineLossBean> getDayLineLossData(Map<String, Object> queryMap) {
		long ledgerId = Long.valueOf(queryMap.get("ledgerId").toString());
		int level1 = Integer.valueOf(queryMap.get("level1").toString());
		int level2 = Integer.valueOf(queryMap.get("level2").toString());
		int lossType = Integer.valueOf( queryMap.get("lossType").toString() );
		Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);

		List<LineLossBean> lineData = new ArrayList<LineLossBean>();
		List<Long> meterLevel1 = schedulingMapper.getLineLossByLevel_2(ledgerId, level1,lossType);
		if (meterLevel1 != null && meterLevel1.size() > 0) {
			String meterIds = processIds(meterLevel1); /*后面为修改*/
			List<Long> mids = new ArrayList<Long>();
			if(meterIds.split(",").length > 0) {
				String[] ms =meterIds.split(",");
				for (int i = 0; i <ms.length; i++) {
					mids.add(Long.parseLong(ms[i]));
				}
			}
//			lineData = schedulingMapper.getDayLineLossInfo_2(mids, startDate, endDate,lossType);
			lineData = schedulingMapper.getDayLineLossInfo_2new(mids, startDate, endDate,lossType);
			for (LineLossBean lb : lineData) {
                MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(lb.getMeterId());
				  List<Long> meters = new ArrayList<Long>();
                if(meterBean.getIsVirtual() == 1){
                    meters = schedulingMapper.getVirtualLineLossMeters(lb.getMeterId(), level2);
                }
                else{
                    meters = schedulingMapper.getLineLossMeters(lb.getMeterId(), level2);
                }
                //List<Long> meters = schedulingMapper.getLineLossMeters(lb.getMeterId(), level2);
				if (meters == null || meters.size() == 0)
					continue;
				meterIds = processIds(meters); /*后面为修改*/
				List<Long> mids2 = new ArrayList<Long>();
				if(meterIds.split(",").length > 0) {
					String[] ms =meterIds.split(",");
					for (int i = 0; i <ms.length; i++) {
						mids2.add(Long.parseLong(ms[i]));
					}
				}
//				List<LineLossBean> childLineData = schedulingMapper.getDayLineLossInfo_2(mids2, startDate, endDate,lossType);
				List<LineLossBean> childLineData = schedulingMapper.getDayLineLossInfo_2new(mids2, startDate, endDate,lossType);
				lb.setChildMeters(childLineData);
			}
		}

		return lineData;
	}

	private String processIds(List<Long> meterLevel1) {
		StringBuffer meterIds = new StringBuffer("");
		for (int i = 0; i < meterLevel1.size(); i++) {
			if (i == (meterLevel1.size() - 1))
				meterIds.append(meterLevel1.get(i));
			else
				meterIds.append(meterLevel1.get(i)).append(",");
		}
		return meterIds.toString();
	}

	@Override
	public Integer getLineMaxLevel(long ledgerId,int lossType) {
		return schedulingMapper.getLineMaxLevel(ledgerId,lossType);
	}

	/**
	 * 电流不平衡分析：获取符合条件计量点ID
	 * @param queryMap
	 * @return
	 */
	private long getTargetPointId4Iu(Map<String,Object> queryMap){
		int type = Integer.parseInt(queryMap.get("type")!= null?queryMap.get("type").toString():"0");
		long dataId = Long.valueOf(queryMap.get("pointId").toString());
		
		
		long pointId = dataId;
		if (type == 1) {
			List<MeterBean> pointList = ledgerManagerMapper.querySummaryMeterByLedgerId(dataId);
			int pointIdSize = pointList.size();
			if (pointIdSize == 1) { // 分户下只有一块总表,取该表数据
				pointId = pointList.get(0).getMeterId();
			} else if (pointIdSize > 1) { // 分户下有多块总表,取条件内“电流日不平衡度最大值”所属总表的数据

				
				pointId = -1;
			} else if (pointIdSize == 0) { // 分户下无总表,取条件内“电流日不平衡度最大值”所属电表或分户的数据

				
				pointId = -1;
			}
		}
		return pointId;
	}
	
	/**
	 * 电压不平衡分析：获取符合条件计量点ID
	 * @param queryMap
	 * @return
	 */
	private long getTargetPointId4Vu(Map<String,Object> queryMap){
		int type = Integer.parseInt(queryMap.get("type")!= null?queryMap.get("type").toString():"0");
		long dataId = Long.valueOf(queryMap.get("pointId").toString());
		//Date startDate = DateUtil.convertStrToDate(queryMap.get("startDate").toString(), DateUtil.SHORT_PATTERN);
		//Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		long pointId = dataId;
		if (type == 1) {
			List<MeterBean> pointList = ledgerManagerMapper.querySummaryMeterByLedgerId(dataId);
			int pointIdSize = pointList.size();
			if (pointIdSize == 1) { // 分户下只有一块总表,取该表数据
				pointId = pointList.get(0).getMeterId();
			} else if (pointIdSize > 1) { // 分户下有多块总表,取条件内“电压日不平衡度最大值”所属总表的数据
//				List<Long> maxIuMaxList = schedulingMapper.getMaxVuMaxListOfSP(dataId, startDate, endDate);
//				pointId = maxIuMaxList.get(0);
				pointId = -1;
			} else if (pointIdSize == 0) { // 分户下无总表,取条件内“电压日不平衡度最大值”所属电表或分户的数据
//				List<Long> maxIuMaxList = schedulingMapper.getMaxVuMaxListOfAP(dataId, startDate, endDate);
//				pointId = maxIuMaxList.get(0);
				pointId = -1;
			}
		}
		return pointId;
	}
	
	@Override
	public List<MeterBean> querySummaryMeterByLedgerId(long ledgerId){
		return ledgerManagerMapper.querySummaryMeterByLedgerId(ledgerId);
	}

    @Override
    public Map<String, Object> getSaveEnergyData(Long ledgerId, Integer selectType, String beginTime, String endTime){
        Map<String, Object> result = new HashMap<String, Object>();

        try{
            String message = "";
            LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
            if(ledger == null){
                message = "不存在该能管对象！";
                result.put("message", message);
                return result;
            }
            else if(ledger.getBaseQ() == null){
                message = "该能管对象未配置基准能耗！";
                result.put("message", message);
                return result;
            }
            else {
                System.out.println("1: " + new Date());
                //启动功率
                double startPower = ledger.getStartPower();
                //基准能耗
                Double baseQ = ledger.getBaseQ();
                result.put("baseQ", baseQ);

                Date beginDate = DateUtil.convertStrToDate(beginTime, DateUtil.MOUDLE_PATTERN);
                Date endDate = DateUtil.convertStrToDate(endTime, DateUtil.MOUDLE_PATTERN);
                //得到所选时间段内，达到基准功率的所有时间点
                List<Date> dateList = this.schedulingMapper.getOverStartTimesBy(ledgerId, beginDate, endDate, startPower);
                System.out.println("2: " + new Date());
                //开机时长
                double startTime = ((double) dateList.size())/4;
                result.put("startTime", startTime);
                //改造前能耗
                Double q1 = NumberUtil.formatDouble(baseQ * startTime, "0.##");
                result.put("q1", q1);
                //实际能耗
                double q2 = 0;
                if(CollectionUtils.isNotEmpty(dateList)){
                    List<Map<String, Date>> timeCouple = new ArrayList<>();   //用于存放时间对结果
                    while(dateList.size() > 0){
                        Date start = dateList.get(0);  //某个时间段的开始
                        Date end = dateList.get(0);    //某个时间段的结束
                        int clearNum = 0; //表示需要清除dateList中的前clearNum个数据
                        if(dateList.size() > 1){
                            for(int i = 1; i < dateList.size(); i++){
                                Date temp = dateList.get(i);
                                Date temp_before = dateList.get(i-1);
                                if(DateUtil.getMinBetweenTime(temp_before, temp) <= 15){
                                    end = dateList.get(i);
                                    if(i == dateList.size() - 1){
                                        clearNum = dateList.size();
                                    }
                                }
                                else {
                                    clearNum = i;
                                    break;
                                }
                            }
                        }
                        else {
                            clearNum = 1;
                        }

                        //将start、end存到timeCouple中
                        Map<String, Date> map = new HashMap<>();
                        map.put("start", start);
                        map.put("end", DateUtil.getNextMinuteDate(end, 15));
                        timeCouple.add(map);
                        //清除dateList中的前clearNum个数据
                        int totalNum = dateList.size();
                        dateList = dateList.subList(clearNum, totalNum);
                    }
                    System.out.println("3: " + new Date());
                    //用得到的结果timeCouple去计算
                    if(CollectionUtils.isNotEmpty(timeCouple)){
                        for(int i = 0; i < timeCouple.size(); i++){
                            Date time1 = timeCouple.get(i).get("start");
                            Date time2 = timeCouple.get(i).get("end");
                            //用电能示值曲线计算电量
                            double tempQ = this.schedulingMapper.getLedgerQDataByE(ledgerId, time1, time2);
                            q2 = q2 + tempQ;
                        }
                    }
                    System.out.println("4: " + new Date());
                }
                q2 = NumberUtil.formatDouble(q2, "0.##");
                result.put("q2", q2);
                //节约电量
                Double saveQ = q2 - q1;
                result.put("saveQ", NumberUtil.formatDouble(saveQ, "0.##"));
            }
        }
        catch (Exception e){
            Log.error(this.getClass() + ".getSaveEnergyData()");
        }

        return result;
    }

    @Override
    public void exportSaveEnergyExcel(ServletOutputStream outputStream, JSONObject json){
        String deviceName = json.getString("deviceName");
        String time1 = json.getString("time1");
        String time2 = json.getString("time2");
        String baseQ = json.getString("baseQ");
        String startTime = json.getString("startTime");
        String q2 = json.getString("q2");
        String q1 = json.getString("q1");
        String saveQ = json.getString("saveQ");

        //声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();

        //定义一个表格
        HSSFSheet sheet = wb.createSheet("节能量统计");
        sheet.setColumnWidth((short)0,(short)24*256);
        sheet.setColumnWidth((short)1,(short)24*256);
        sheet.setColumnWidth((short)2,(short)24*256);
        sheet.setColumnWidth((short)3,(short)24*256);
        sheet.setColumnWidth((short)4,(short)24*256);
        sheet.setColumnWidth((short)5,(short)24*256);
        sheet.setColumnWidth((short)6,(short)24*256);
        sheet.setColumnWidth((short)7,(short)24*256);

        //定义整体风格
        HSSFCellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

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

        try{
            HSSFRow trRow = sheet.createRow(0);
            HSSFCell cellA = trRow.createCell(0);
            HSSFCell cellB = trRow.createCell(1);
            HSSFCell cellC = trRow.createCell(2);
            HSSFCell cellD = trRow.createCell(3);
            HSSFCell cellE = trRow.createCell(4);
            HSSFCell cellF = trRow.createCell(5);
            HSSFCell cellG = trRow.createCell(6);
            HSSFCell cellH = trRow.createCell(7);
            cellA.setCellStyle(titlestyle);
            cellB.setCellStyle(titlestyle);
            cellC.setCellStyle(titlestyle);
            cellD.setCellStyle(titlestyle);
            cellE.setCellStyle(titlestyle);
            cellF.setCellStyle(titlestyle);
            cellG.setCellStyle(titlestyle);
            cellH.setCellStyle(titlestyle);
            cellA.setCellValue("被改造设施");
            cellB.setCellValue("起始结算时间");
            cellC.setCellValue("结束结算时间");
            cellD.setCellValue("基准电耗(kWh/h)");
            cellE.setCellValue("开机时长(h)");
            cellF.setCellValue("实际电耗(kWh)");
            cellG.setCellValue("改造前电耗(kWh)");
            cellH.setCellValue("节约电量(kWh)");

            HSSFRow tdrow = sheet.createRow(1);
            HSSFCell cell1A = tdrow.createCell(0);
            HSSFCell cell1B = tdrow.createCell(1);
            HSSFCell cell1C = tdrow.createCell(2);
            HSSFCell cell1D = tdrow.createCell(3);
            HSSFCell cell1E = tdrow.createCell(4);
            HSSFCell cell1F = tdrow.createCell(5);
            HSSFCell cell1G = tdrow.createCell(6);
            HSSFCell cell1H = tdrow.createCell(7);
            cell1A.setCellStyle(style_);
            cell1B.setCellStyle(style_);
            cell1C.setCellStyle(style_);
            cell1D.setCellStyle(style_);
            cell1E.setCellStyle(style_);
            cell1F.setCellStyle(style_);
            cell1G.setCellStyle(style_);
            cell1H.setCellStyle(style_);
            cell1A.setCellValue(deviceName);
            cell1B.setCellValue(time1);
            cell1C.setCellValue(time2);
            cell1D.setCellValue(baseQ);
            cell1E.setCellValue(startTime);
            cell1F.setCellValue(q2);
            cell1G.setCellValue(q1);
            cell1H.setCellValue(saveQ);

            outputStream.flush();
            wb.write(outputStream);
            outputStream.close();
        }
        catch(IOException e ){
            Log.info("exportSaveEnergyExcel error IOException");
        }
    }
}
