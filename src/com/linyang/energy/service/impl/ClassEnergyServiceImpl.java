package com.linyang.energy.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.NumberUtil;
import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.energy.common.CommonResource;
import com.linyang.energy.mapping.classAnalysis.ClassMapper;
import com.linyang.energy.model.ClassConfigBean;
import com.linyang.energy.model.ProductConfigBean;
import com.linyang.energy.model.ProductDetailBean;
import com.linyang.energy.model.TeamConfigBean;
import com.linyang.energy.model.WorkingHourBean;
import com.linyang.energy.model.WorkshopMeterBean;
import com.linyang.energy.service.ClassEnergyService;
import com.linyang.energy.service.ClassService;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.MapUtil;

/**
 * 班组能耗service的实现类
 * @author guosen
 * @date 2016-8-23
 *
 */
@Service
public class ClassEnergyServiceImpl implements ClassEnergyService {
	
	@Autowired
	private ClassMapper classMapper;
	
	@Autowired
	private ClassService classService;
	
	/**
	 * 转换单位的临界值，当最小的值大于10000，进行单位转换
	 */
	private static final int CHANGE_UNIT_VALUE = 10000;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> search(Map<String, Object> param) {
		//得到能耗标准
		CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
		//根据分析类型区分查询
		Integer analysisType = (Integer) param.get("analysisType");
		//能源类型
		int energyType = (Integer)param.get("energyType");
		//得到班制信息
		Long classId = (Long) param.get("classId");
		//得到车间,单位信息
		List<Long> workshopIds = (List<Long>) param.get("workshopIds");
		//时间类型
		Integer dateType = (Integer) param.get("dateType");
		//开始时间
		Date beginTime = (Date) param.get("beginTime");
		//结束时间
		Date endTime = (Date) param.get("endTime");
		//产品ID，0代表所有
		Long productId = (Long) param.get("productId");
		//展示类型
//		int etype = (Integer) param.get("etype");
		
		if(analysisType == null){
			return null;
		}
		//班组能耗统计
		else if(analysisType == 0){
			return this.classAllEnergyCount(energyType, classId, workshopIds, dateType, beginTime, endTime, resource,analysisType);
		}
		//班组能耗排名
		else if(analysisType == 1){
			return this.classAllEnergyRank(energyType, workshopIds, dateType, beginTime, endTime, resource,analysisType);
		}
		//班组单耗统计
		else if(analysisType == 2){
			return this.classSingleEnergyCount(classId, workshopIds, dateType, beginTime, endTime, productId, resource,analysisType,energyType);
		}
		//班组单耗排名
		else if(analysisType == 3){
			return this.classSingleEnergyRank(workshopIds, dateType, beginTime, endTime, productId, resource,analysisType,energyType);
		}
		return null;
	}
	
	/**
	 * 班组单耗排名
	 * @param workshopIds
	 * @param dateType
	 * @param beginTime
	 * @param endTime
	 * @param productId
	 * @param resource
	 * @return
	 */
	private Map<String, Object> classSingleEnergyRank(List<Long> workshopIds,
			Integer dateType, Date beginTime, Date endTime, Long productId,
			CommonResource resource,Integer analysisType,int energyType) {

		Map<String, Object> result = new HashMap<String, Object>();
			//result:数据信息
		Map<String, Double> series = new HashMap<String, Double>();
		
		Map<Long,ClassConfigBean> classMap = new HashMap<Long, ClassConfigBean>();
		
		//参考单耗值
		double referenceValue = getProductReferenceValue(productId, workshopIds);
		
		//循环车间/单位
		for (Long workshopId : workshopIds) {
			Map<String, Object> workshopMap = this.classMapper.getWorkShopById(workshopId);
			//得到这个车间选择的班制Id
			Long classId = ((BigDecimal)workshopMap.get("CLASS_ID")).longValue();
			ClassConfigBean classConfigBean = null;
			
			if(classMap.containsKey(classId)){
				classConfigBean = classMap.get(classId);
			}else {
				classConfigBean = this.buildClassConfig(dateType, classId, beginTime, endTime);
				classMap.put(classId, classConfigBean);
			}
			
			String workshopName = (String) workshopMap.get("WORKSHOP_NAME");
			
			//得到车间所属的测量点信息
			List<WorkshopMeterBean> meterList = this.classMapper.getWorkshopMetersByWorkshopId(workshopId);
			
			List<TeamConfigBean> teams = classConfigBean.getTeams();
			
			//循环班制
			for (int i=0; i< teams.size(); i ++) {
				TeamConfigBean teamConfigBean = teams.get(i);

				String teamName = teamConfigBean.getTeamName();
				//拼接组成x轴名称
				String xAxisName = workshopName + "-" + teamName;
				
				double val = caculateClassEnergyValue(productId, resource,
						workshopId, meterList, teamConfigBean,analysisType,energyType);
				series.put(xAxisName, NumberUtil.formatDouble(val, NumberUtil.PATTERN_DOUBLE));
			}
		}
		
		result.put("referenceValue", referenceValue);
		result.put("dateSeries", MapUtil.sortByValue(series) );
		
		return result;
	}

	/**
	 * 班组单耗统计
	 * @param energyType
	 * @param classId
	 * @param workshopIds
	 * @param dateType
	 * @param beginTime
	 * @param endTime
	 * @param productId
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> classSingleEnergyCount(Long classId, 
			List<Long> workshopIds, Integer dateType,
			Date beginTime, Date endTime, Long productId,
			CommonResource resource,Integer analysisType,int energyType) {

		Map<String, Object> result = new HashMap<String, Object>();
		
		ClassConfigBean classBean = buildClassConfig(dateType, classId, beginTime, endTime);
		
		//result:车间，单元列表
		List<String> workshopXAxis = new ArrayList<String>();
		//result:班组列表
		List<String> classLedend = new ArrayList<String>();
		//result:数据信息
		Map<String, Object> series = new HashMap<String, Object>();
		
		//得到这个车间的班组数据
		List<TeamConfigBean> teams = classBean.getTeams();
		for (TeamConfigBean teamConfigBean : teams) {
			String teamName = teamConfigBean.getTeamName();
			if(!classLedend.contains(teamName)){
				classLedend.add(teamName);
			}
			List<Double> list = new ArrayList<Double>();
			series.put(teamName, list);
		}

		//参考单耗值
		double referenceValue = getProductReferenceValue(productId, workshopIds);
		
		//循环车间/单位
		for (Long workshopId : workshopIds) {
			Map<String, Object> workshopMap = this.classMapper.getWorkShopById(workshopId);
			String workshopName = (String) workshopMap.get("WORKSHOP_NAME");
			workshopXAxis.add(workshopName);
			
			//得到车间所属的测量点信息
			List<WorkshopMeterBean> meterList = this.classMapper.getWorkshopMetersByWorkshopId(workshopId);
			
			//循环班制
			for (int i=0; i< teams.size(); i ++) {
				TeamConfigBean teamConfigBean = teams.get(i);
				
				double val = caculateClassEnergyValue(productId, resource,
						workshopId, meterList, teamConfigBean,analysisType,energyType);
				
				List<Double> list = (List<Double>) series.get(teamConfigBean.getTeamName());
				list.add(val);
				series.put(teamConfigBean.getTeamName(), list);
			}
		}
		
		result.put("referenceValue", referenceValue);
		result.put("classLedend", classLedend);
		result.put("dateSeries", series);
		result.put("workshopXAxis", workshopXAxis);
		
		return result;
	
	}

	/**
	 * 计算某个班组的单耗
	 * @param productId
	 * @param resource
	 * @param workshopId
	 * @param meterList
	 * @param teamConfigBean
	 * @return
	 */
	private double caculateClassEnergyValue(Long productId,
			CommonResource resource, Long workshopId,
			List<WorkshopMeterBean> meterList, TeamConfigBean teamConfigBean,Integer analysisType,int energyType) {
		Long teamId = teamConfigBean.getTeamId();
		//该班组的能耗
		double energyVal = 0;
		//该班组的产量
		double outputAll = 0;
		int totalTimes = teamConfigBean.getBeginList().size();
		
		List<Date> beginDates = teamConfigBean.getBeginList();
		List<Date> endDates = teamConfigBean.getEndList();
		
		//根据时间段取数据
		for (int j = 0; j < totalTimes; j++) {
			Date beginDate = beginDates.get(j);
			Date endDate = endDates.get(j);
			
			//计算产量
			ProductDetailBean detail = caculateOutput(productId, workshopId, teamId, beginDate, endDate);
			//如果产量为空，不计算他的能耗
			if(detail != null){
				outputAll = DataUtil.doubleAdd(outputAll, detail.getOutput());
				Date outputStartTime = detail.getStartTime();
				Date outputEndTime = detail.getEndTime();
				
				//整天数，去除时分秒
				Date beginDate2 = this.getFormatDate(outputStartTime, 1);
				Date endDate2 = this.getFormatDate(outputEndTime, 2);
				//得到选择的时间段所在的天内的比例,按分钟算
				double ratio = this.getDateRatio(outputStartTime,outputEndTime,beginDate2,endDate2);
				
				//循环对象
				for (WorkshopMeterBean workshopMeter : meterList) {
					//查询条件
					Map<String, Object> queryParam = buildQueryParam(0, outputStartTime, outputEndTime, beginDate2,endDate2, workshopMeter);
					
					int meterType = workshopMeter.getMeterType();
					//电
					if(meterType == 1 ){
						Double val = this.classMapper.queryClassEnergyData(queryParam);
						val = val != null ? val : 0;
						//折算成标准煤
						if ((analysisType == 0 || analysisType == 1) && energyType == 0) {
							val = DataUtil.doubleMultiply(val, resource.getElecUnit());//换算成标准煤,单位:吨
						}
						energyVal = DataUtil.doubleAdd(energyVal, val);
					}
					//水
					else if(meterType == 2){
						Double val = this.classMapper.queryClassEnergyData(queryParam);
						val = val != null ? DataUtil.doubleMultiply(val, ratio) : 0;
						//折算成标准煤
						if ((analysisType == 0 || analysisType == 1) && energyType == 0) {
							val = DataUtil.doubleMultiply(val, resource.getWaterUnit());//换算成标准煤,单位:吨
						}
						energyVal = DataUtil.doubleAdd(energyVal, val);
					}
					//气
					else if(meterType == 3){
						Double val = this.classMapper.queryClassEnergyData(queryParam);
						val = val != null ? DataUtil.doubleMultiply(val, ratio) : 0;
						//折算成标准煤
						if ((analysisType == 0 || analysisType == 1) && energyType == 0) {
							val = DataUtil.doubleMultiply(val, resource.getGasUnit());//换算成标准煤,单位:吨
						}
						energyVal = DataUtil.doubleAdd(energyVal, val);
					}
					//热
					else if(meterType == 4){
						Double val = this.classMapper.queryClassEnergyData(queryParam);
						val = val != null ? DataUtil.doubleMultiply(val, ratio) : 0;
						//折算成标准煤
						if ((analysisType == 0 || analysisType == 1) && energyType == 0) {
							val = DataUtil.doubleMultiply(val, resource.getHotUnit());//换算成标准煤,单位:吨
						}
						energyVal = DataUtil.doubleAdd(energyVal, val);
					}
					
				}
			}
		}
		
		double val = 0;
		if(outputAll > 0){
			val = DataUtil.doubleDivide(energyVal, outputAll, 2);
		}
		return val;
	}

	/**
	 * 班组能耗排名
	 * @param energyType
	 * @param workshopIds
	 * @param dateType
	 * @param beginTime
	 * @param endTime
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> classAllEnergyRank(int energyType,
			List<Long> workshopIds, Integer dateType, Date beginTime,
			Date endTime, CommonResource resource,Integer analysisType) {
		Map<String, Object> result = new HashMap<String, Object>();
		//result:数据信息
		Map<String, Double> series = new HashMap<String, Double>();
		
		
			//存放班制信息
			Map<Long,ClassConfigBean> classMap = new HashMap<Long, ClassConfigBean>();

			//是否需要换算单位
			boolean changeUnit = false;
			//最小值，用来换算单位
			double minValue = CHANGE_UNIT_VALUE;
			
			//循环车间/单位
			for (Long workshopId : workshopIds) {
				Map<String, Object> workshopMap = this.classMapper.getWorkShopById(workshopId);
				//得到这个车间选择的班制Id
				Long classId = ((BigDecimal)workshopMap.get("CLASS_ID")).longValue();
				
				ClassConfigBean classConfigBean = null;
				
				if(classMap.containsKey(classId)){
					classConfigBean = classMap.get(classId);
				}else {
					classConfigBean = this.buildClassConfig(dateType, classId, beginTime, endTime);
					classMap.put(classId, classConfigBean);
				}
				
				String workshopName = (String) workshopMap.get("WORKSHOP_NAME");
				
				//得到车间所属的测量点信息
				List<WorkshopMeterBean> meterList = this.classMapper.getWorkshopMetersByWorkshopId(workshopId);
				
				List<TeamConfigBean> teams = classConfigBean.getTeams();
				//循环班制
				for (int i=0; i< teams.size(); i ++) {
					TeamConfigBean teamConfigBean = teams.get(i);
					//计算班组能耗
					double energyVal = caculateEnergyValue(energyType, resource, meterList, teamConfigBean,analysisType);
					
					String teamName = teamConfigBean.getTeamName();
					//拼接组成x轴名称
					String xAxisName = workshopName + "-" + teamName;
					
					//值为0的被排除
					if(minValue > energyVal && energyVal != 0){
						minValue = energyVal;
					}
					
					series.put(xAxisName, energyVal);
				}
			}

			//大于10000
			if(minValue >= CHANGE_UNIT_VALUE){
				changeUnit = true;
				Iterator it =  series.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Double> entry =  (Entry<String, Double>) it.next();
					double value = entry.getValue();
					value = DataUtil.doubleDivide(value, 1000);
					series.put(entry.getKey(),  NumberUtil.formatDouble(value, NumberUtil.PATTERN_DOUBLE));
				}
			}
			
			result.put("dateSeries", MapUtil.sortByValue(series) );
			result.put("changeUnit", changeUnit);
		return result;
	
	}

	/**
	 * 计算班组能耗
	 * @param energyType
	 * @param resource
	 * @param meterList
	 * @param teamConfigBean
	 * @return
	 */
	private double caculateEnergyValue(int energyType, CommonResource resource,
			List<WorkshopMeterBean> meterList, TeamConfigBean teamConfigBean,Integer analysisType) {
		//该班组的能耗
		double energyVal = 0;
		int totalTimes = teamConfigBean.getBeginList().size();
		
		List<Date> beginDates = teamConfigBean.getBeginList();
		List<Date> endDates = teamConfigBean.getEndList();
		
		//根据时间段取数据
		for (int j = 0; j < totalTimes; j++) {
			Date beginDate = beginDates.get(j);
			Date endDate = endDates.get(j);
			//整天数，去除时分秒
			Date beginDate2 = this.getFormatDate(beginDate,1);
			Date endDate2 = this.getFormatDate(endDate,2);
			//得到选择的时间段所在的天内的比例,按分钟算
			double ratio = this.getDateRatio(beginDate,endDate,beginDate2,endDate2);
			
			//循环对象
			for (WorkshopMeterBean workshopMeter : meterList) {
				
				//查询条件
				Map<String, Object> queryParam = buildQueryParam(energyType, beginDate, endDate, beginDate2,endDate2, workshopMeter);
				
				//根据能源类型读数数据
				int meterType = workshopMeter.getMeterType();
				
				Double val = this.classMapper.queryClassEnergyData(queryParam);
				
				//电
				if(meterType == 1 ){
					if(energyType == 0 || energyType == 1){
						val = val != null ? val : 0;
						//折算成标准煤
						if ((analysisType == 0 || analysisType == 1) && energyType == 0) {
							val = val * resource.getElecUnit();//换算成标准煤,单位:kgce
						}
						energyVal = DataUtil.doubleAdd(energyVal, val);
					}
				}
				//水
				else if(meterType == 2){
					if(energyType == 0 || energyType == 2){
						val = val != null ? DataUtil.doubleMultiply(val, ratio) : 0;
						//折算成标准煤
						if ((analysisType == 0 || analysisType == 1) && energyType == 0) {
							val = DataUtil.doubleMultiply(val, resource.getWaterUnit());//换算成标准煤,单位:kgce
						}
						energyVal = DataUtil.doubleAdd(energyVal, val);
					}
				}
				//气
				else if(meterType == 3){
					if(energyType == 0 || energyType == 3){
						val = val != null ? DataUtil.doubleMultiply(val, ratio) : 0;
						//折算成标准煤
						if ((analysisType == 0 || analysisType == 1) && energyType == 0) {
							val = DataUtil.doubleMultiply(val, resource.getGasUnit());//换算成标准煤,单位:kgce
						}
						energyVal =  DataUtil.doubleAdd(energyVal, val);
					}
				}
				//热
				else if(meterType == 4){
					if (energyType == 0 || energyType == 4){
						val = val != null ? DataUtil.doubleMultiply(val, ratio) : 0;
						//折算成标准煤
						if ((analysisType == 0 || analysisType == 1) && energyType == 0) {
							val = DataUtil.doubleMultiply(val, resource.getHotUnit());//换算成标准煤,单位:kgce
						}
						energyVal = DataUtil.doubleAdd(energyVal, val);
					}
				}
			}
		}
		return NumberUtil.formatDouble(energyVal, NumberUtil.PATTERN_DOUBLE);
	}


	/**
	 * 班组能耗统计
	 * @param energyType
	 * @param classId
	 * @param workshopIds
	 * @param dateType
	 * @param beginTime
	 * @param endTime
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> classAllEnergyCount(int energyType,
			Long classId, List<Long> workshopIds, Integer dateType,
			Date beginTime, Date endTime, CommonResource resource,Integer analysisType) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		//result:	车间/单元列表，X轴数据
		List<String> workshopXAxis = new ArrayList<String>();
		//result:	班组列表，图列数据
		List<String> classLedend = new ArrayList<String>();
		//result:	数据信息，具体内容数据
		Map<String, Object> dataSeries = new HashMap<String, Object>();
		
		
			//得到班制信息
			ClassConfigBean classBean = buildClassConfig(dateType, classId, beginTime, endTime);
			//得到这个车间的班组数据
			List<TeamConfigBean> teams = classBean.getTeams();
			
			for (TeamConfigBean teamConfigBean : teams) {
				String teamName = teamConfigBean.getTeamName();
				//添加班制数据
				if(!classLedend.contains(teamName)){
					classLedend.add(teamName);
				}
				List<Double> list = new ArrayList<Double>();
				dataSeries.put(teamName, list);
			}
			
			//是否需要换算单位
			boolean changeUnit = false;
			//最小值，用来换算单位
			double minValue = CHANGE_UNIT_VALUE;
			
			//循环车间/单位
			for (Long workshopId : workshopIds) {
				Map<String, Object> workshopMap = this.classMapper.getWorkShopById(workshopId);
				
				String workshopName = (String) workshopMap.get("WORKSHOP_NAME");
				workshopXAxis.add(workshopName);
				
				//得到车间所属的测量点信息
				List<WorkshopMeterBean> meterList = this.classMapper.getWorkshopMetersByWorkshopId(workshopId);
				
				//循环班制
				for (int i=0; i< teams.size(); i ++) {
					TeamConfigBean teamConfigBean = teams.get(i);
					//计算班组能耗
					double energyVal = caculateEnergyValue(energyType, resource, meterList, teamConfigBean,analysisType);
					
					//值为0的被排除
					if(minValue > energyVal && energyVal != 0){
						minValue = energyVal;
					}
					
					String teamName = teamConfigBean.getTeamName();
					
					List<Double> list = (List<Double>) dataSeries.get(teamName);
					list.add(energyVal);
					
					dataSeries.put(teamName, list);
				}
			}
			
			//大于等于10000,转换代为kgce->tce
			if(minValue >= CHANGE_UNIT_VALUE){
				changeUnit = true;
				Iterator it =  dataSeries.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, List<Double>> entry= (Entry<String, List<Double>>) it.next();
					List<Double> list = entry.getValue();
					for (int i = 0; i < list.size(); i++ ) {
						double value = list.get(i);
						value = DataUtil.doubleDivide(value, 1000);
						list.set(i, NumberUtil.formatDouble(value, NumberUtil.PATTERN_DOUBLE));
					}
				}
			}
			
			result.put("changeUnit", changeUnit);
			
			result.put("classLedend", classLedend);
			result.put("dateSeries", dataSeries);
			result.put("workshopXAxis", workshopXAxis);
//			result.put( "etype" , etype );
			
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.error(this.getClass() + ".classAllEnergyCount()", e);
//			result = null;
//		}
		return result;
	}

	/**
	 * 拼装查询参数
	 * @param energyType	能源类型
	 * @param beginDate		电类型的开始时间
	 * @param endDate		电类型的结束时间
	 * @param beginDate2	水气热的开始时间
	 * @param endDate2		水气热的结束时间
	 * @param workshopMeter	车间-测量点关系对象
	 * @return
	 */
	private Map<String, Object> buildQueryParam(int energyType, Date beginDate,
			Date endDate, Date beginDate2, Date endDate2,
			WorkshopMeterBean workshopMeter) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("energyType", energyType);
		queryParam.put("beginDate", beginDate);
		queryParam.put("endDate", endDate);
		queryParam.put("beginDate2", beginDate2);
		queryParam.put("endDate2", endDate2);
		queryParam.put("meterId", workshopMeter.getMeterId());
		queryParam.put("meterType", workshopMeter.getMeterType());
		queryParam.put("addAttr", workshopMeter.getAddAttr());
		queryParam.put("pct", workshopMeter.getPct());
		return queryParam;
	}

	/**
	 * 得到选择的时间段所在的天内的比例
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private double getDateRatio(Date beginDate, Date endDate, Date beginDate2, Date endDate2) {
		//开始时间大于结束时间，有问题
		if(beginDate.getTime() > endDate.getTime()){
			return 0;
		}
		int betWeenDays = DateUtil.daysBetweenDates(endDate2, beginDate2) + 1;
		int betWeenMinutes = DateUtil.getMinBetweenTime(beginDate, endDate);
		double ratio = DataUtil.doubleDivide(betWeenMinutes, betWeenDays * 1440, 2);
		return NumberUtil.formatDouble(ratio, "#.0000");
	}


	/**
	 * 天数取整，去掉时分秒
	 * @param beginDate
	 * @param type 1-开始时间；2-结束时间;
	 * @return
	 */
	private Date getFormatDate(Date date, int type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(type == 2 && cal.get(Calendar.HOUR_OF_DAY) ==0 && cal.get(Calendar.MINUTE) ==0&& cal.get(Calendar.SECOND) ==0){
			cal.add(Calendar.DATE, -1);
		}
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}


	/**
	 * 根据班制ID得到班制信息
	 * @param dateType
	 * @param classId
	 * @return
	 */
	private ClassConfigBean buildClassConfig(Integer dateType, Long classId, Date beginTime, Date endTime) {
		//根据主键得到班制信息
		ClassConfigBean classBean = this.classMapper.getClassConfigById(classId);
		//循环周期
		int cycle = classBean.getCycle();
		
		//根据班制ID获取下面的班组工作时间列表
		List<WorkingHourBean> teamList = this.classMapper.getWorkingHoursByClassId(classId);
		//一共有多少个时间段
		int teamSize = teamList.size();
		//拼装班组集合
		List<TeamConfigBean> teamConfigList = buildTeamConfigList(teamList);
		classBean.setTeams(teamConfigList);
		
		//时间类型-最近一轮班次
		if(dateType == 0){
			if(cycle == ClassConfigBean.CYCLE_DAY || cycle == ClassConfigBean.CYCLE_WEEK){
				buildLastDateList(cycle, teamList, teamSize, teamConfigList);
			}else {
				buildLastDateList2(classId, teamList, teamSize, teamConfigList);
			}
			
		}
		//时间类型-上月和自定义时间类型
		else if(dateType == 1 || dateType == 2){
			//日，周
			if(cycle == ClassConfigBean.CYCLE_DAY || cycle == ClassConfigBean.CYCLE_WEEK){
				buildCustomDateList(beginTime, endTime, cycle, teamList, teamSize, teamConfigList);
			}
			//自定义
			else {
				//得到自定义类型的班制时间列表
				buildCustomDateList2(beginTime, endTime,teamList, teamSize, teamConfigList);
			}
			
		}
		return classBean;
	}

	/**
	 * 最近一轮班次,拼装开始时间和结束时间
	 * @param cycle
	 * @param teamList
	 * @param teamSize
	 * @param teamConfigList
	 */
	private void buildLastDateList2(long classId, List<WorkingHourBean> teamList,
			int teamSize, List<TeamConfigBean> teamConfigList) {
		List<WorkingHourBean> list = this.classService.getRecentClassWorkHours(classId);
		for (WorkingHourBean workingHourBean : list) {
			long teamId = workingHourBean.getTeamId();
			for (TeamConfigBean teamConfig : teamConfigList) {
				if(teamConfig.getTeamId() == teamId){
					teamConfig.getBeginList().add(workingHourBean.getOnDuty());
					teamConfig.getEndList().add(workingHourBean.getOffDuty());
				}
			}
		}
		
		
	}

	/**
	 * 得到自定义类型的班制时间列表
	 * @param beginTime
	 * @param endTime
	 * @param teamList
	 * @param teamSize
	 * @param teamConfigList
	 */
	@SuppressWarnings("unchecked")
	private void buildCustomDateList2(Date beginTime, Date endTime,
			List<WorkingHourBean> teamList, int teamSize,
			List<TeamConfigBean> teamConfigList) {
		for (TeamConfigBean teamConfig : teamConfigList) {
			long teamId = teamConfig.getTeamId();
			Map<String, Object> map = classService.getProductSplitTime(teamId, beginTime, endTime);
			List<Map<String,Date>> list = (List<Map<String, Date>>) map.get("list");
			for (Map<String, Date> timeMap : list) {
				Date beginDate = (Date) timeMap.get("begin");
				Date endDate = (Date) timeMap.get("end");
				teamConfig.getBeginList().add(beginDate);
				teamConfig.getEndList().add(endDate);
			}
		}
		
	}

	/**
	 * 时间类型-上月和自定义时间类型,拼装开始时间和结束时间
	 * @param beginTime
	 * @param endTime
	 * @param cycle
	 * @param teamList
	 * @param teamSize
	 * @param teamConfigList
	 */
	private void buildCustomDateList(Date beginTime, Date endTime, int cycle,
			List<WorkingHourBean> teamList, int teamSize,
			List<TeamConfigBean> teamConfigList) {
		int weekNum = 0;
		
		if(cycle == ClassConfigBean.CYCLE_DAY){
			weekNum = 0;
		}else if(cycle == ClassConfigBean.CYCLE_WEEK){
			weekNum = this.getDateWeekNum(beginTime);
		}
		
		int beginMinute = this.getTimeMinute(beginTime, weekNum);
		//查看当前时间属于哪个区间
		int beginIndex = 0;
		boolean hasTime = false;
		
		for (int i = 0; i < teamSize; i++) {
			WorkingHourBean workingHourBean = teamList.get(i);
			//得到下班时间
			Date offDuty = workingHourBean.getOffDuty();
			int offDutyWeek = workingHourBean.getOffDutyWeek();
			Integer offMinute = this.getTimeMinute(offDuty, offDutyWeek);
			//得到上班时间
			Date onDuty = workingHourBean.getOnDuty();
			int onDutyWeek = workingHourBean.getOnDutyWeek();
			Integer onMinute = this.getTimeMinute(onDuty, onDutyWeek);
			//如果在这个区间，则从这个区间开始,不跨天（循环周期为周没有影响）
			if(offMinute >= onMinute && offMinute >= beginMinute && onMinute <= beginMinute){
				beginIndex = i;
				hasTime = true;
				break;
			}
			//跨天（循环周期为周没有影响）
			else if(offMinute < onMinute && offMinute >= beginMinute && offMinute > beginMinute){
				beginIndex = i;
				hasTime = true;
				break;
			}
		}
		
		boolean first = true;
		
		Date lastDate = null;
		int lastMinute = 0;
		int count = 0;
		//循环存入对象中
		while (true) {
			if(beginIndex == teamSize){
				beginIndex = 0;
			}
			WorkingHourBean workingHourBean = teamList.get(beginIndex);
			
			long teamId = workingHourBean.getTeamId();
			
			Date onDuty = workingHourBean.getOnDuty();
			int onDutyWeek = workingHourBean.getOnDutyWeek();
			
			Date offDuty = workingHourBean.getOffDuty();
			int offDutyWeek = workingHourBean.getOffDutyWeek();
			
			Date beginDate = null;
			Date endDate = null;
			
			if(first){
				int beginWeekNum = 0;
				if(cycle == ClassConfigBean.CYCLE_WEEK){
					beginWeekNum = this.getDateWeekNum(beginTime);
				}
				//开始时间分钟数
				int firstBeginMinute = this.getTimeMinute(beginTime, beginWeekNum);
				
				if(hasTime){
					beginDate = beginTime;
				}else {
					//上班时间分钟数
					int onMinutie = this.getTimeMinute(onDuty, onDutyWeek);
					int betweenMinute = getBetweenMinute(firstBeginMinute, onDutyWeek, onMinutie);
					beginDate = this.dateAddMinute(beginTime, betweenMinute);
					//重新获得开始的分钟数
					firstBeginMinute = this.getTimeMinute(beginDate, onDutyWeek);
				}
				//下班时间分钟数
				int offMinute = this.getTimeMinute(offDuty, offDutyWeek);
				//得到开始时间和结束时间差的分钟数
				int betweenMinutes = this.getBetweenMinute(firstBeginMinute, offDutyWeek, offMinute);
				endDate = dateAddMinute(beginDate, betweenMinutes);
				
				first = false;
			}else {
				int onMinutie = this.getTimeMinute(onDuty, onDutyWeek);
				int betweenMinute = getBetweenMinute(lastMinute, onDutyWeek, onMinutie);
				beginDate = this.dateAddMinute(lastDate, betweenMinute);

				//得到开始时间和结束时间差的分钟数
				int betweenMinutes = this.getBetweenMinute(onDuty, onDutyWeek, offDuty, offDutyWeek);
				endDate = dateAddMinute(beginDate, betweenMinutes);
				
			}
			
			//设置上一个时间段的结束时间：
			lastDate = endDate;
			//得到分钟数
			lastMinute = this.getTimeMinute(lastDate, offDutyWeek);

			//如果上个时间段的结束时间大于选择的结束时间，跳出循环
			if(lastDate.getTime() >= endTime.getTime()){
				//最后的结束时间大于开始时间，还有半个周期
				if(endTime.getTime() > beginDate.getTime()){
					endDate = endTime;
					for (TeamConfigBean teamConfig : teamConfigList) {
						//判断是否存在该班组，如果存在，取出来
						if(teamConfig.getTeamId() == teamId){
							teamConfig.getBeginList().add(beginDate);
							teamConfig.getEndList().add(endDate);
						}
					}
				}
				break;
			}
			
			beginIndex++;
			
			for (TeamConfigBean teamConfig : teamConfigList) {
				//判断是否存在该班组，如果存在，取出来
				if(teamConfig.getTeamId() == teamId){
					teamConfig.getBeginList().add(beginDate);
					teamConfig.getEndList().add(endDate);
				}
			}
			
			//TODO:万一死循环，执行300次退出
			count++;
			if(count == 300){
				break;
			}
		}
	}

	/**
	 * 得到两个时间相差的分钟数
	 * @param lastMinute
	 * @param type =0位天；!=0位周
	 * @param onMinutie
	 * @return
	 */
	private int getBetweenMinute(int lastMinute, int type, int onMinutie) {
		//得到开始时间和上一次结束时间差的分钟数
		int betweenMinute = onMinutie - lastMinute;
		int ratio = 1;
		//周期为周
		if(type != 0){
			ratio = 7;
		}
		if(betweenMinute < 0){
			betweenMinute = onMinutie + 1440*ratio - lastMinute;
		}
		return betweenMinute;
	}

	/**
	 * 增加分钟数，得到一个新的时间
	 * @param beginDate
	 * @param betweenMinutes
	 * @return
	 */
	private Date dateAddMinute(Date beginDate, int betweenMinutes) {
		Date endDate;
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		cal.add(Calendar.MINUTE, betweenMinutes);
		endDate = cal.getTime();
		return endDate;
	}

	/**
	 * 查看两个时间相差多少分钟
	 * @param onDuty
	 * @param onDutyWeek
	 * @param offDuty
	 * @param offDutyWeek
	 * @return
	 */
	private int getBetweenMinute(Date onDuty, int onDutyWeek, Date offDuty,
			int offDutyWeek) {
		int betweenMinute = 0;
		
		int onMinute = this.getTimeMinute(onDuty, onDutyWeek);
		int offMinute = this.getTimeMinute(offDuty, offDutyWeek);
		
		int ratio = 1;
		//周期为周
		if(onDutyWeek != 0){
			ratio = 7;
		}
		
		//上班时间大于下班时间，跨天了。
		if(onMinute >= offMinute){
			betweenMinute = offMinute + 1440*ratio - onMinute;
		}else {
			betweenMinute = offMinute - onMinute;
		}
		return betweenMinute;
	}

	/**
	 * 得到两个日期相差几天，安星期算
	 * @param lastNum
	 * @param beginNum
	 * @return
	 */
	private int getBetweenDayByWeekNum(int lastNum, int beginNum) {
		int num = beginNum - lastNum;
		if(lastNum > beginNum){
			num = beginNum + 7 -lastNum;
		}
		return num;
	}

	/**
	 * 最近一轮班次,拼装开始时间和结束时间
	 * @param cycle
	 * @param teamList
	 * @param teamSize
	 * @param teamConfigList
	 */
	private void buildLastDateList(int cycle, List<WorkingHourBean> teamList,
			int teamSize, List<TeamConfigBean> teamConfigList) {
		//设置基准时间
		Date baseDate = null;
		//设置当前时间
		Date currentDate = DateUtil.clearDate(new Date());
		
		for (int i = teamSize-1 ; i >= 0; i--) {
			WorkingHourBean workingHourBean = teamList.get(i);
			long teamId = workingHourBean.getTeamId();
			
			//得到下班时间
			Date offDuty = workingHourBean.getOffDuty();
			Integer offMinute = this.getTimeMinute(offDuty);
			Integer offDutyWeek = workingHourBean.getOffDutyWeek();
			//得到上班时间
			Date onDuty = workingHourBean.getOnDuty();
			Integer onMinute = this.getTimeMinute(onDuty);
			Integer onDutyWeek = workingHourBean.getOnDutyWeek();
			
			//如果是最后一个，判断起始位置
			if(i == teamSize -1){
				//循环周期为天 或者自定义
				if(cycle == ClassConfigBean.CYCLE_DAY){
					Integer nowMinute = this.getTimeMinute(currentDate);
					//如果当前时间 >下班时间
					if(nowMinute >= offMinute){
						baseDate = currentDate;
					}
					//如果当前时间 <下班时间,向前推一天
					else {
						baseDate = DateUtil.addDateDay(currentDate, -1);
					}
				}
				//循环周期为周
				else if(cycle == ClassConfigBean.CYCLE_WEEK){
					
					Integer offMinuteWeek = this.getTimeMinute(offDuty, offDutyWeek);
					//得到当前时间是星期几
					int weekNum = getDateWeekNum(currentDate);
					Integer nowMinute = this.getTimeMinute(currentDate, weekNum);
					
					int between = 0;
					//当前时间大于结束时间，回退到结束那天
					if(nowMinute >= offMinuteWeek){
						between = (nowMinute-offMinuteWeek)/(60*24);
					}
					//当前时间小于结束时间，回退到上个星期的结束那天。
					else {
						between = nowMinute/(60*24) + 7 - offMinuteWeek/(60*24);
					}
					baseDate = DateUtil.addDateDay(currentDate, between *-1);
				}
				
			}
			//得到结束时间
			Date endDate = buildDate(baseDate, offMinute);
			
			//上班时间大于下班时间，说明跨天，要减一天处理
			if(cycle == ClassConfigBean.CYCLE_DAY){
				if(onMinute >= offMinute){
					baseDate = DateUtil.addDateDay(baseDate, -1);
				}
			}else {
				int beweenDay = this.getBetweenDayByWeekNum(onDutyWeek, offDutyWeek);
				baseDate = DateUtil.addDateDay(baseDate, -1*beweenDay);
			}
			
			//得到开始时间
			Date beginDate = buildDate(baseDate, onMinute);
			
			for (TeamConfigBean teamConfig : teamConfigList) {
				//判断是否存在该班组，如果存在，取出来
				if(teamConfig.getTeamId() == teamId){
					teamConfig.getBeginList().add(beginDate);
					teamConfig.getEndList().add(endDate);
				}
			}
			
		}
	}

	/**
	 * 拼装班组集合
	 * @param teamList
	 * @return
	 */
	private List<TeamConfigBean> buildTeamConfigList(
			List<WorkingHourBean> teamList) {
		//新建一个空的班组对象列表
		List<TeamConfigBean> teamConfigList = new ArrayList<TeamConfigBean>();
		
		//把工作时间列表循环插入到班组中
		for (WorkingHourBean workingHourBean : teamList) {
			
			long teamId = workingHourBean.getTeamId();
			String teamName = workingHourBean.getTeamName();
			
			//得到上班时间
			Date onDuty = workingHourBean.getOnDuty();
			//得到上班周期
			int onDutyWeek = workingHourBean.getOnDutyWeek();
			//得到下班时间
			Date offDuty = workingHourBean.getOffDuty();
			//得到下班星期
			int offDutyWeek = workingHourBean.getOffDutyWeek();
			
			boolean isExist = false;
			
			//判断班组集合中是否存在该班组
			for (TeamConfigBean teamConfig : teamConfigList) {
				//如果存在，取出来
				if(teamConfig.getTeamId() == teamId){
					isExist = true;
					//如果已经存在这个班组，把时间数据加入该班组对象中
					buildTeamConfig(onDuty, onDutyWeek, offDuty, offDutyWeek, teamConfig);
				}
			}
			
			//不存在，新建
			if(!isExist){
				TeamConfigBean teamConfig = new TeamConfigBean();
				teamConfig.setTeamId(teamId);
				teamConfig.setTeamName(teamName);
				buildTeamConfig(onDuty, onDutyWeek, offDuty, offDutyWeek, teamConfig);
				teamConfigList.add(teamConfig);
			}
		}
		return teamConfigList;
	}

	/**
	 * 得到当前时间是星期几
	 * @return
	 */
	private int getDateWeekNum(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int weekNum = cal.get(Calendar.DAY_OF_WEEK);
		if(weekNum == 1){
			weekNum = 7;
		}else {
			weekNum = weekNum - 1;
		}
		return weekNum;
	}

	/**
	 * 组装生成时间，baseDate的年月日==yyyy-MM-dd和minute的分钟，小时===HH:mm
	 * @param baseDate
	 * @param minute
	 * @return
	 */
	private Date buildDate(Date baseDate, Integer minute) {
		String minuteStr = Integer.toString(minute%60);
		if(minute%60 < 10){
			minuteStr = "0" + minute%60;
		}
		String timeStr = DateUtil.convertDateToStr(baseDate, DateUtil.SHORT_PATTERN) + " " + minute/60 + ":" + minuteStr;
		Date date = DateUtil.convertStrToDate(timeStr, DateUtil.MOUDLE_PATTERN);
		return date;
	}

	/**
	 * 得到某一天的分钟数， 星期几*24*60 + 小时*60 + 分钟
	 * @param date	时间	
	 * @param weekNum 星期
	 * @return
	 */
	private Integer getTimeMinute(Date date, int weekNum) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);//小时；
		int minute = cal.get(Calendar.MINUTE);//分钟；
		return weekNum*24*60 + hour*60 + minute;
	}
	
	/**
	 * 得到某一天的分钟数， 小时*60 + 分钟
	 * @param beginDate
	 * @return
	 */
	private Integer getTimeMinute(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);//小时；
		int minute = cal.get(Calendar.MINUTE);//分钟；
		return hour*60 + minute;
	}
	
	/**
	 * 组装班组对象
	 * @param onDuty
	 * @param onDutyWeek
	 * @param offDuty
	 * @param offDutyWeek
	 * @param teamConfig
	 */
	private void buildTeamConfig(Date onDuty, int onDutyWeek, Date offDuty,
			int offDutyWeek, TeamConfigBean teamConfig) {
		teamConfig.getOnDutyList().add(onDuty);
		teamConfig.getOnDutyWeekList().add(onDutyWeek);
		teamConfig.getOffDutyList().add(offDuty);
		teamConfig.getOffDutyWeekList().add(offDutyWeek);
	}

	/**
	 * 得到产品的参考单耗
	 * @param productId
	 * @param workshopIds
	 * @return
	 */
	private double getProductReferenceValue(Long productId, List<Long> workshopIds){
		//参考单耗值
		double referenceValue = 0;
		//所有产品
		if(productId == 0){
			//所有产品的参考单号值： 各个产品的参考单耗/折算系数相加除以产品的个数
			//查看这些车间下的所有产品
			List<ProductConfigBean> productList = this.classMapper.queryProductConfigByWorkshopIds(workshopIds);
			if(productList != null && productList.size() > 0){
				double total = 0;
				for (ProductConfigBean productConfigBean : productList) {
					double unitConsumption = productConfigBean.getUnitConsumption();
					int convertRatio = productConfigBean.getConvertRatio();
					total = new BigDecimal(unitConsumption).multiply(new BigDecimal(100)).divide(new BigDecimal(convertRatio), 2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(total)).doubleValue();
				}
				referenceValue = DataUtil.doubleDivide(total, productList.size(), 2);
			}
		}
		//单个产品
		else {
			ProductConfigBean product = this.classMapper.queryProductConfigById(productId);
			referenceValue = DataUtil.doubleDivide(DataUtil.doubleMultiply(product.getUnitConsumption(), 100), product.getConvertRatio(), 2);
		}
		return referenceValue;
	}

	/**
	 * 查看某一时间段具体的产量情况
	 * @param productId		产品Id
	 * @param workshopId	车间ID
	 * @param teamId		班组Id
	 * @param beginDate		产量开始时间
	 * @param endDate		产量结束时间
	 * @return
	 */
	private ProductDetailBean caculateOutput(Long productId, Long workshopId, Long teamId,
			Date beginDate, Date endDate) {
		ProductDetailBean detail = null;
		Map<String, Object> productParam = new HashMap<String, Object>();
		productParam.put("workshopId", workshopId);
		productParam.put("productId", productId);
		productParam.put("teamId", teamId);
		productParam.put("beginDate", beginDate);
		productParam.put("endDate", endDate);
		
		Map<String, Object> map = this.classMapper.getProductDetail(productParam);
		if(map != null ){
			Long pId = ((BigDecimal)map.get("PRODUCT_ID")).longValue();
			Double out = ((BigDecimal) map.get("OUTPUT")).doubleValue();
			Date startTime = (Date) map.get("START_TIME");
			Date endTime = (Date) map.get("END_TIME");
			//根据产品这算系数，折算成基准产品产量
			ProductConfigBean product = this.classMapper.queryProductConfigById(pId);
			if(product != null && out != null){
				int ratio = product.getConvertRatio();
				detail = new ProductDetailBean();
				detail.setOutput(new BigDecimal(out).multiply(new BigDecimal(ratio)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN).doubleValue());
				detail.setStartTime(startTime);
				detail.setEndTime(endTime);
			}
		}
		
		return detail;
	}


	@Override
	public Map<String, Object> getWorkshopListByClassId(Long classId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("list", this.classMapper.getWorkshopListByClassId(classId));
		return result;
	}

}