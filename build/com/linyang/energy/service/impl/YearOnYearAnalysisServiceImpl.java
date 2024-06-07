package com.linyang.energy.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;import java.text.ParseException;
import java.util.*;

import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.MeterBean;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leegern.util.CollectionUtil;
import com.leegern.util.DateUtil;
import com.leegern.util.NumberUtil;
import com.linyang.common.web.common.Log;
import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.energy.common.CommonResource;
import com.linyang.energy.mapping.contrastanalysis.YearOnYearAnalysisMapper;
import com.linyang.energy.mapping.energyanalysis.EnergyStatsAnalyseMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.RealCurveBean;
import com.linyang.energy.service.YearOnYearAnalysisService;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DoubleUtils;

/**
 * @Description 对比分析Service实现类
 * @author Leegern
 * @date Jan 14, 2014 3:56:44 PM
 */
@Service
public class YearOnYearAnalysisServiceImpl implements YearOnYearAnalysisService {
	/**
	 * 注入对比mapper
	 */
	@Autowired
	private YearOnYearAnalysisMapper yearOnYearAnalysisMapper;
	
	/**
	 * 注入能耗分项统计分析mapper
	 */
	@Autowired
	private EnergyStatsAnalyseMapper energyStatsAnalyseMapper;
	
	/**
	 * 注入分户管理mapper
	 */
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;


    @Autowired
    private MeterManagerMapper meterManagerMapper;
	
	/**
	 * 处理类型: 同比环比
	 */
	private static final int OPERATION_TYPE_FST = 1;
	
	/**
	 * 处理类型: 分户对比
	 */
//	private static final int OPERATION_TYPE_SEC = 2;
	
	/**
	 * 标识是否有数据
	 */
	private static boolean hasData = false;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#queryEnergyStatData(java.util.Map)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, Object> queryEnergyStatData(Map<String, Object> param) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		//  拼装后的数据结构 chooseDate - > periodDate - > item
		Map<String, Map<String, RealCurveBean>> tmpMap = new HashMap<String, Map<String, RealCurveBean>>();
		Map<String, RealCurveBean> periodMap = new TreeMap<String, RealCurveBean>();
		Map<String, RealCurveBean> totalMap = new HashMap<String, RealCurveBean>();  // 存放统计数
		Map<String, RealCurveBean> dataMap = null;
		Date baseTime = (Date)param.get("baseTime");      // 基准时间
		long baseLongTime = baseTime.getTime();           // 基准时间
		int statType = (Integer)param.get("statType");    // 统计类型
		int periodType = (Integer)param.get("periodType");// 统计周期
		List<Date> destDates = (List<Date>)param.get("destDates"); // 目标比对时间 
		int minute = WebConstant.density;                                  // 曲线密度
		int totalMinute = 1440;                           // 一天的分钟数
		int xSize = 0;                                    // X轴的点数
		double dataValue = 0;
		RealCurveBean tmpBean = null;
		Date tmpDate = null;
		String baseTimeStr = DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN); // 基准时间
		String timeStr = "";
		// --------  to do something ...
		hasData = false;
		
		try {
			switch (periodType) {
			// 日(日数据先插曲线数据, 为空再查日数据)
			case 1:
				// 日曲线数据
				param.put("beginTime", baseTime);
				param.put("endTime",   DateUtil.convertStrToDate(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59"));
				List<RealCurveBean> todayList = getEmoDcpEnergyStatData(param);
				param.put("beginTime", destDates.get(0));	//zzy 问过, 应该只有一个对比日期
				param.put("endTime",   DateUtil.convertStrToDate(DateUtil.convertDateToStr(destDates.get(0), DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59"));
				List<RealCurveBean> lastList = getEmoDcpEnergyStatData(param);
				if (CollectionUtil.isNotEmpty(todayList) || CollectionUtil.isNotEmpty(lastList)) {
					hasData = true;
					// 得到x轴的点数
					xSize = this.getPageCount(totalMinute, minute);
					for (int i = 0; i < xSize; i++) {
//						if (i > 0) {
							baseLongTime += 60000 * minute;
//						}
						periodMap.put(DateUtil.convertDateToStr(baseLongTime, DateUtil.DEFAULT_FULL_PATTERN).replace(" ", "_"), new RealCurveBean());
					}
					
					// 填充数据
					if (CollectionUtil.isNotEmpty(todayList)) {
						totalMap.put(baseTimeStr, new RealCurveBean());
						for (RealCurveBean realCurveBean : todayList) {
							String keyTime = DateUtil.convertDateToStr(realCurveBean.getFreezeTime(), DateUtil.DEFAULT_FULL_PATTERN).replace(" ", "_");
							if (periodMap.containsKey(keyTime)) {
								// 拷贝数据
								BeanUtils.copyProperties(periodMap.get(keyTime), realCurveBean);
							}
							if (new BigDecimal(totalMap.get(baseTimeStr).getDataValue()).compareTo(BigDecimal.ZERO) == 0) {
								totalMap.put(baseTimeStr, realCurveBean);
							}
							else {
								// 算总数
								totalMap.get(baseTimeStr).setDataValue(DataUtil.doubleAdd(totalMap.get(baseTimeStr).getDataValue(), realCurveBean.getDataValue()));
							}
						}
						// 数据存入map中
						tmpMap.put(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN), periodMap);
					} else {
						totalMap.put(baseTimeStr, null);
						tmpMap.put(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN), null);
					}
					
					// 比对时间填充数据
					//for (int i = 0; i < destDates.size(); i++) {
						baseLongTime = destDates.get(0).getTime();
						baseTimeStr = DateUtil.convertDateToStr(destDates.get(0), DateUtil.DEFAULT_FULL_PATTERN);
						timeStr = DateUtil.convertDateToStr(destDates.get(0), DateUtil.DEFAULT_SHORT_PATTERN);
						dataMap = new TreeMap<String, RealCurveBean>();
						for (int j = 0; j < xSize; j++) {
//							if (j > 0) {
								baseLongTime += 60000 * minute;
//							}
							dataMap.put(DateUtil.convertDateToStr(baseLongTime, DateUtil.DEFAULT_FULL_PATTERN).replace(" ", "_"), new RealCurveBean());
						}
//						param.put("beginTime", destDates.get(i));
//						param.put("endTime",   DateUtil.convertStrToDate(DateUtil.convertDateToStr(destDates.get(i), DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59"));
						// 查询比对时间数据
//						todayList = getEmoDcpEnergyStatData(param);
						if (CollectionUtil.isNotEmpty(lastList)) {
							totalMap.put(timeStr, new RealCurveBean());
							for (RealCurveBean bean : lastList) {
								String keyTime = DateUtil.convertDateToStr(bean.getFreezeTime(), DateUtil.DEFAULT_FULL_PATTERN).replace(" ", "_");
								if (dataMap.containsKey(keyTime)) {
									// 拷贝数据
									BeanUtils.copyProperties(dataMap.get(keyTime), bean);
								}
								if (new BigDecimal(totalMap.get(timeStr).getDataValue()).compareTo(BigDecimal.ZERO) == 0) {
									totalMap.put(timeStr, bean);
								}
								else {
									// 算总数
									totalMap.get(timeStr).setDataValue(DataUtil.doubleAdd(totalMap.get(timeStr).getDataValue(), bean.getDataValue()));
								}
							}
							// 数据存入map中
							tmpMap.put(timeStr, dataMap);
						} else {
							totalMap.put(baseTimeStr, null);
							tmpMap.put(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN), null);
						}
					//}
				} else {	// 日数据
					// 基准数据
					param.put("statType", (statType + 1));
					todayList = getEmoDcpEnergyStatData(param);
					if (CollectionUtil.isNotEmpty(todayList)) {
						hasData = true;
					}
					for (RealCurveBean bean : todayList) {
						dataValue = DataUtil.doubleAdd(dataValue, bean.getDataValue());
						tmpBean = bean;
					}
                    if(tmpBean != null){
                        tmpBean.setDataValue(dataValue);
                    }
					totalMap.put(baseTimeStr, tmpBean);
					periodMap.put(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN), tmpBean);
					// 数据存入map中
					tmpMap.put(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN), periodMap);
					
					// 比对时间填充数据
					for (int i = 0; i < destDates.size(); i++) {
						baseTimeStr = DateUtil.convertDateToStr(destDates.get(i), DateUtil.DEFAULT_SHORT_PATTERN);
						dataMap = new TreeMap<String, RealCurveBean>();
						dataMap.put(baseTimeStr, new RealCurveBean());
						param.put("beginTime", destDates.get(i));
						param.put("endTime",   DateUtil.convertStrToDate(DateUtil.convertDateToStr(destDates.get(i), DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59"));
						// 查询比对时间数据
						todayList = getEmoDcpEnergyStatData(param);
						if (CollectionUtil.isNotEmpty(todayList)) {
							hasData = true;
						}
						totalMap.put(baseTimeStr, new RealCurveBean());
						for (RealCurveBean bean : todayList) {
							String keyTime = bean.getFreezeTimeStr();
							if (dataMap.containsKey(keyTime)) {
								// 拷贝数据
								BeanUtils.copyProperties(dataMap.get(keyTime), bean);
							}
							if (new BigDecimal(totalMap.get(baseTimeStr).getDataValue()).compareTo(BigDecimal.ZERO) == 0) {
								totalMap.put(baseTimeStr, bean);
							}
							else {
								// 算总数
								totalMap.get(baseTimeStr).setDataValue(DataUtil.doubleAdd(totalMap.get(baseTimeStr).getDataValue(), bean.getDataValue()));
							}
						}
						// 数据存入map中
						tmpMap.put(baseTimeStr, dataMap);
					}
				}
				break;
			// 周
			case 2:
				Date thisDateStart = DateUtil.getMondayDateInWeek(baseTime);
				Date thisDateEnd = DateUtil.getSundayDateInWeek(baseTime);
//				// 判断是否是本周
//				if (baseTime.getTime() >= DateUtil.getMondayDateInWeek(new Date()).getTime() && baseTime.getTime() <= DateUtil.getSundayDateInWeek(new Date()).getTime() ) {
//					thisDateEnd = DateUtil.getSomeDateInYear(DateUtil.getCurrentDate(DateUtil.DEFAULT_FULL_PATTERN), -1);
//				}
				int amount = DateUtil.getBetweenDays(thisDateStart, thisDateEnd);
				Date thisDateStart1 = new Date(thisDateStart.getTime());
				for (int i = 0; i <= amount; i++) {
					if (i > 0) {
						thisDateStart1 = DateUtil.getSomeDateInYear(thisDateStart1, 1);
					}
					periodMap.put(DateUtil.convertDateToStr(thisDateStart1, DateUtil.DEFAULT_SHORT_PATTERN),
							new RealCurveBean());
				}
				param.put("beginTime", thisDateStart);
				param.put("endTime",   thisDateEnd);
				// 处理统计数据
				this.populateStatData(param, periodMap, baseTimeStr, totalMap, DateUtil.DEFAULT_SHORT_PATTERN, OPERATION_TYPE_FST);
				// 数据存入map中
				tmpMap.put(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN), periodMap);
				
				// 比对时间填充数据
				for (int i = 0; i < destDates.size(); i++) {
					baseTimeStr = DateUtil.convertDateToStr(destDates.get(i), DateUtil.DEFAULT_SHORT_PATTERN);
					// 数据Map
					dataMap = new TreeMap<String, RealCurveBean>();
					Date lastDateStart = DateUtil.getMondayDateInWeek(destDates.get(i));
					Date lastDateEnd = DateUtil.convertStrToDate(DateUtil.convertDateToStr(DateUtil.getSomeDateInYear(lastDateStart, amount), DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59", DateUtil.DEFAULT_FULL_PATTERN);
					param.put("beginTime", lastDateStart);
					param.put("endTime",   lastDateEnd);
					// 生成周期
					for (int j = 0; j <= amount; j++) {
						dataMap.put(DateUtil.convertDateToStr(DateUtil.getSomeDateInYear(lastDateStart, j), DateUtil.DEFAULT_SHORT_PATTERN), new RealCurveBean());
					}
					// 处理统计数据
					this.populateStatData(param, dataMap, baseTimeStr, totalMap, DateUtil.DEFAULT_SHORT_PATTERN, OPERATION_TYPE_FST);
					// 数据存入map中
					tmpMap.put(baseTimeStr, dataMap);
				}
				break;
			// 月
			case 3:
				Date thisDateStart3 = DateUtil.getMonthFirstDay(baseTime);
				Date thisDateEnd3 = DateUtil.convertStrToDate((DateUtil.convertDateToStr(DateUtil.getLastDayOfMonth(baseTime), DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59"));
//				String baseTimeStr3 = DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SIMPLE_PATTERN);
//				// 判断基准时间是否是当前月
//				if (baseTimeStr3.equals(DateUtil.getCurrentDate(DateUtil.DEFAULT_SIMPLE_PATTERN))) {
//					thisDateEnd3 = DateUtil.getSomeoneDate(-1);
//				}
				int amount3 = DateUtil.getBetweenDays(thisDateStart3, thisDateEnd3);
				tmpDate = new Date(thisDateStart3.getTime());
				for (int i = 0; i <= amount3; i++) {
					if (i > 0) {
						tmpDate = DateUtil.getSomeDateInYear(tmpDate, 1);
					}
					periodMap.put(DateUtil.convertDateToStr(tmpDate, DateUtil.DEFAULT_SHORT_PATTERN), new RealCurveBean());
				}
				param.put("beginTime", thisDateStart3);
				param.put("endTime",   thisDateEnd3);
				// 处理统计数据
				this.populateStatData(param, periodMap, baseTimeStr, totalMap, DateUtil.DEFAULT_SHORT_PATTERN, OPERATION_TYPE_FST);
				// 数据存入map中
				tmpMap.put(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN), periodMap);
				
				// 比对时间填充数据
				for (int i = 0; i < destDates.size(); i++) {
					baseTimeStr = DateUtil.convertDateToStr(destDates.get(i), DateUtil.DEFAULT_SHORT_PATTERN);
					// 数据Map
					dataMap = new TreeMap<String, RealCurveBean>();
					Date lastDateStart3 = DateUtil.getMonthFirstDay(destDates.get(i));
					Date lastDateEnd3 = DateUtil.convertStrToDate(com.linyang.energy.utils.DateUtil
							.getMonthLastDay(DateUtil.convertDateToStr(lastDateStart3, DateUtil.DEFAULT_SHORT_PATTERN))
							+ " 23:59:59");
					param.put("beginTime", lastDateStart3);
					param.put("endTime",   lastDateEnd3);
					int amount4 = DateUtil.getBetweenDays(lastDateStart3, lastDateEnd3);
					if (amount4 > amount3)
						amount4 = amount3;
					// 生成周期
					for (int j = 0; j <= amount4; j++) {
						dataMap.put(DateUtil.convertDateToStr(DateUtil.getSomeDateInYear(lastDateStart3, j), DateUtil.DEFAULT_SHORT_PATTERN), new RealCurveBean());
					}
					// 处理统计数据
					this.populateStatData(param, dataMap, baseTimeStr, totalMap, DateUtil.DEFAULT_SHORT_PATTERN, OPERATION_TYPE_FST);
					// 数据存入map中
					tmpMap.put(baseTimeStr, dataMap);
				}
				break;
			// 年
			case 4:
				Date thisDateStart4 = DateUtil.getYearFirstDay(baseTime);
				Date thisDateEnd4 = DateUtil.getYearLastDay(baseTime);
				baseTimeStr = baseTimeStr.substring(0, 7);
				int amount4 = DateUtil.getBetweenMonthes(thisDateStart4, thisDateEnd4);
				tmpDate = new Date(thisDateStart4.getTime());
				for (int i = 0; i <= amount4; i++) {
					if (i > 0) {
						tmpDate = DateUtil.getCalculateMonth(tmpDate, 1);
					}
					periodMap.put(DateUtil.convertDateToStr(tmpDate, DateUtil.DEFAULT_SIMPLE_PATTERN), new RealCurveBean());
				}
				param.put("beginTime", thisDateStart4);
				param.put("endTime",   thisDateEnd4);
				// 处理统计数据
				this.populateStatData(param, periodMap, baseTimeStr, totalMap, DateUtil.DEFAULT_SIMPLE_PATTERN, OPERATION_TYPE_FST);
				// 数据存入map中
				tmpMap.put(DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SIMPLE_PATTERN), periodMap);
				
				// 比对时间填充数据
				for (int i = 0; i < destDates.size(); i++) {
					baseTimeStr = DateUtil.convertDateToStr(destDates.get(i), DateUtil.DEFAULT_SIMPLE_PATTERN);
					// 数据Map
					dataMap = new TreeMap<String, RealCurveBean>();
					Date lastDateStart4 = DateUtil.getYearFirstDay(destDates.get(i));
					Date lastDateEnd4 = DateUtil.getCalculateMonth(DateUtil.getLastDayOfMonth(lastDateStart4), amount4);
					param.put("beginTime", lastDateStart4);
					param.put("endTime",   lastDateEnd4);
					// 生成周期
					for (int j = 0; j <= amount4; j++) {
						dataMap.put(DateUtil.convertDateToStr(DateUtil.getCalculateMonth(lastDateStart4, j), DateUtil.DEFAULT_SIMPLE_PATTERN), new RealCurveBean());
					}
					// 处理统计数据
					this.populateStatData(param, dataMap, baseTimeStr, totalMap, DateUtil.DEFAULT_SIMPLE_PATTERN, OPERATION_TYPE_FST);
					// 数据存入map中
					tmpMap.put(baseTimeStr, dataMap);
				}
				break;
			}
		} catch (ParseException e) {
			Log.info("queryEnergyStatData error ParseException");
		}
		catch (IllegalAccessException e) {
			Log.info("queryEnergyStatData error IllegalAccessException");
		}
		catch (InvocationTargetException e) {
			Log.info("queryEnergyStatData error InvocationTargetException");
		}
		if (! hasData) {
			tmpMap = null;
			totalMap = null;
		}
		// 大图数据
 		result.put("bigChartData", tmpMap);
		// 小图数据
		result.put("smallChartData", totalMap);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#getScaleInfo(java.util.Map)
	 */
	@Override
	public Map<String, Object> getScaleInfo(Map<String, Object> param) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		int meterType = (Integer)param.get("meterType"); // 计量点类型
		int statType = 0;                                // 统计类型
		double yesterdayStat = 0;                        // 昨日统计
		double lastWeekStat = 0;                         // 上周同期统计
		double lastMonthStat = 0;                        // 上月同期统计
		double lastYearStat = 0;                         // 去年同期统计        
		
		double lastWeekRate = 0;                         // 上周同期比
		double lastMonthRate = 0;                        // 上月同期比
		double lastYearRate = 0;                         // 去年同期比
		double averageVal = 0;                           // 昨日平均值
		
		// 电日数据
		if (1 == meterType) {
			statType = 1;
		}
		// 水日数据
		else if (2 == meterType) {
			statType = 5;
		}
		// 气日数据
		else if (3 == meterType){
			statType = 8;
		}
		//热日数据
		else if (4 == meterType){
			statType = 20;
		}
		param.put("statType", statType);
		String yesterday = DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
		Date startYesterday = DateUtil.convertStrToDate(yesterday + " 00:00:00");
		Date endYesterday = DateUtil.convertStrToDate(yesterday + " 23:59:59");
		
		// -------- 算昨日消耗量 -------- //
		// 昨日日期
		param.put("beginTime", startYesterday);
		param.put("endTime",   endYesterday);
		// 查询昨日数据
		List<RealCurveBean> todayList = getEmoDcpEnergyStatData(param);
		for (RealCurveBean realCurveBean : todayList) {
			yesterdayStat = DataUtil.doubleAdd(yesterdayStat, realCurveBean.getDataValue());
		}
		// 昨日统计
		result.put("yesterdayStat", yesterdayStat); 
		if (yesterdayStat != 0) {
			averageVal = DataUtil.doubleDivide(yesterdayStat, 24);
		}
		// 平均值
		result.put("averageVal", DoubleUtils.getDoubleValue(averageVal, 5));
		
		// -------- 算上周同期 -------- //
		// 上周同期日期
		param.put("beginTime", DateUtil.getSomeDateInYear(startYesterday, -7));
		param.put("endTime",   DateUtil.getSomeDateInYear(endYesterday, -7));
		// 查询上周同期数据
		List<RealCurveBean> lastWeekList = getEmoDcpEnergyStatData(param);
		
		for (RealCurveBean realCurveBean : lastWeekList) {
			lastWeekStat = DataUtil.doubleAdd(lastWeekStat, realCurveBean.getDataValue());
		}
		if (lastWeekStat != 0) {
			lastWeekRate = new BigDecimal(yesterdayStat).subtract(new BigDecimal(lastWeekStat)).divide(new BigDecimal(lastWeekStat), 4, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).doubleValue();
		}
		// 上周同期占比
		result.put("lastWeekRate", lastWeekRate); 
		
		
		// -------- 算上月同期 -------- //
		// 上月同期日期
		param.put("beginTime", DateUtil.getLastMonthDate(startYesterday));
		param.put("endTime",   DateUtil.getLastMonthDate(endYesterday));
		// 查询上月数据
		List<RealCurveBean> lastMonthList = getEmoDcpEnergyStatData(param);
		for (RealCurveBean realCurveBean : lastMonthList) {
			lastMonthStat = DataUtil.doubleAdd(lastMonthStat, realCurveBean.getDataValue());
		}
		if (lastMonthStat != 0) {
			lastMonthRate = new BigDecimal(yesterdayStat).subtract(new BigDecimal(lastMonthStat)).divide(new BigDecimal(lastMonthStat), 4, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).doubleValue();
		}
		// 上月同期占比
		result.put("lastMonthRate", lastMonthRate); 
		
		
		// -------- 算去年同期消耗量 -------- //
		// 去年同期日期
		param.put("beginTime", DateUtil.getLastYearSameDate(startYesterday));
		param.put("endTime",   DateUtil.getLastYearSameDate(endYesterday));
		// 查询去年同期数据
		List<RealCurveBean> lastYearList = getEmoDcpEnergyStatData(param);
		for (RealCurveBean realCurveBean : lastYearList) {
			lastYearStat = DataUtil.doubleAdd(lastYearStat, realCurveBean.getDataValue());
		}
		if (lastYearStat != 0) {
			lastYearRate = new BigDecimal(yesterdayStat).subtract(new BigDecimal(lastYearStat)).divide(new BigDecimal(lastYearStat), 4, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).doubleValue();
		}
		// 去年同期占比
		result.put("lastYearRate", lastYearRate); 
		
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#getAverageValue(java.util.Map)
	 */
	@Override
	public Map<String, Object> getAverageValue(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		Date beginDate = (Date)param.get("beginDate");   // 起始时间
		Date endDate = (Date)param.get("endDate");       // 截止时间
		int meterType = (Integer)param.get("meterType"); // 计量点类型
		double total = 0;
		double averageVal = 0;
		// -------- 算平均消耗值
		List<RealCurveBean> list = yearOnYearAnalysisMapper.queryEnergyStatData(param);
		for (RealCurveBean realCurveBean : list) {
			total = DataUtil.doubleAdd(total, realCurveBean.getDataValue());
		}
		if (total > 0) {
			// 起止日期相差天数
			int days = DateUtil.getBetweenDays(beginDate, endDate);
			// 计量点是电的话, 需要换算成kw/h
			if (meterType == 1) {
				averageVal = new BigDecimal(total).divide(new BigDecimal(days + 1).divide(new BigDecimal(24), 4, BigDecimal.ROUND_DOWN), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue();
			}
			else {
				averageVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(total, 100), days + 1, 2);
			}
		}
		result.put("averageVal", NumberUtil.formatDouble(averageVal, NumberUtil.PATTERN_DOUBLE));
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#getElecWaterGasPeak(java.util.Map)
	 */
	@Override
	public Map<String, Object> getElecWaterGasPeak(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		double todayTotal = 0;
		double monthTotal = 0;
		double yearTotal = 0;
		int meterType = (Integer) param.get("meterType");
		if(meterType == 1){
			//电
			// 今日
			param.put("beginTime", DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN));
			param.put("endTime",   DateUtil.getCurrentDate(DateUtil.DEFAULT_FULL_PATTERN));
		}else {
			//水气热
			// 昨日
			Date yesterday = DateUtil.getSomeDateInYear(DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN), -1);
			param.put("beginTime", yesterday);
			Calendar cal = Calendar.getInstance();
			cal.setTime(yesterday);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			param.put("endTime", cal.getTime());
		}
		
		Map<String, Object> today = yearOnYearAnalysisMapper.getElecWaterGasPeak(param);
		if(meterType == 1){
			//电
			if (CollectionUtil.isNotEmpty(today) && null != today.get("DATA_VALUE")) {
				todayTotal = DoubleUtils.getDoubleValue(Double.valueOf(today.get("DATA_VALUE").toString()), 2);
			}
		}else {
			//水气热
			if (CollectionUtil.isNotEmpty(today) && null != today.get("DATA_VALUE")) {
				todayTotal = DataUtil.doubleDivide(Double.valueOf(today.get("DATA_VALUE").toString()),  24);
			}
		}
		
		result.put("todayTotal", todayTotal);
		param.put("endTime",   DateUtil.getCurrentDate(DateUtil.DEFAULT_FULL_PATTERN));
		// 本月
		param.put("beginTime", DateUtil.getCurrMonthFirstDay());
		Map<String, Object> month = yearOnYearAnalysisMapper.getElecWaterGasPeak(param);
		if(meterType == 1){
			//电
			if (CollectionUtil.isNotEmpty(month) && null != month.get("DATA_VALUE")) {
				monthTotal = DoubleUtils.getDoubleValue(Double.valueOf(month.get("DATA_VALUE").toString()), 2);
			}
		}else {
			if (CollectionUtil.isNotEmpty(month) && null != month.get("DATA_VALUE")) {
				monthTotal = DataUtil.doubleDivide(Double.valueOf(month.get("DATA_VALUE").toString()), 24);
			}
		}
		
		result.put("monthTotal", monthTotal);
		
		// 本年
		param.put("beginTime", DateUtil.getCurrYearFirstDay());
		Map<String, Object> year = yearOnYearAnalysisMapper.getElecWaterGasPeak(param);
		if(meterType == 1){
			if (CollectionUtil.isNotEmpty(year) && null != year.get("DATA_VALUE")) {
				yearTotal = DoubleUtils.getDoubleValue(Double.valueOf(year.get("DATA_VALUE").toString()), 2);
			}
		}else {
			if (CollectionUtil.isNotEmpty(year) && null != year.get("DATA_VALUE")) {
				yearTotal = DataUtil.doubleDivide(Double.valueOf(year.get("DATA_VALUE").toString()), 24);
			}
		}
		result.put("yearTotal",  yearTotal);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#getCurrentYearTop(java.util.Map)
	 */
	@Override
	public List<RealCurveBean> getCurrentYearTop(Map<String, Object> param) throws ParseException {
        List<RealCurveBean> result = new ArrayList<RealCurveBean>();
        // EMO Id 和 DCP ID
        if(param.keySet().contains("meterID")){
            result = energyStatsAnalyseMapper.getMeterElecWaterGasTopN(param);
        }
        else{
            result = energyStatsAnalyseMapper.getElecWaterGasTopNData(param);
        }
		return result;
	}
	
	/**
	 * 获取周期数
	 * @param rowCount
	 * @param pageSize
	 * @return
	 */
	private int getPageCount(int rowCount, int pageSize) {
		int pageCount = 1;
		if(rowCount>0){
			if(rowCount/pageSize==0)
				pageCount=1;
			else{
				if(rowCount%pageSize==0)
					pageCount=rowCount/pageSize;
				else
					pageCount=rowCount/pageSize+1;
			}
		}
		return pageCount;
	}
	
	/**
	 * 组装数据
	 * @param param       查询参数
	 * @param periodMap   周期数据存储结构
	 * @param baseTimeStr 统计日期或分户Id
	 * @param totalMap    存储统计数
	 * @param pattern     日期格式
	 * @param type        操作类型
	 * @throws ParseException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private void populateStatData(Map<String, Object> param, Map<String, RealCurveBean> periodMap, String baseTimeStr, Map<String, RealCurveBean> totalMap, String pattern, int type) throws ParseException, IllegalAccessException, InvocationTargetException {
		// 查询统计数据
		List<RealCurveBean> dataList = null;
		// 同比环比
		switch(type)
		{
		case OPERATION_TYPE_FST:
			dataList = getEmoDcpEnergyStatData(param);break;
		default:// 分户对比
			getEmoDcpHouseholdData(param);
		}
		if (CollectionUtil.isNotEmpty(dataList)) {
			hasData = true;
		}
		totalMap.put(baseTimeStr, new RealCurveBean());
        if(dataList != null && dataList.size() > 0){
            for (RealCurveBean realCurveBean : dataList) {
                String keyTime = DateUtil.convertDateToStr(realCurveBean.getFreezeTime(), pattern);
                if (periodMap.containsKey(keyTime)) {
                    BeanUtils.copyProperties(periodMap.get(keyTime), realCurveBean);  // 拷贝数据
                }
                if (((Double) totalMap.get(baseTimeStr).getDataValue()).equals(0d)) {
                    totalMap.put(baseTimeStr, realCurveBean);
                }
                else {
                    totalMap.get(baseTimeStr).setDataValue(DataUtil.doubleAdd(totalMap.get(baseTimeStr).getDataValue(), realCurveBean.getDataValue())); // 算总数
                }
            }
        }
	}

	
	
	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#queryPartialScaleData(java.util.Map)
	 */
	@Override
	public Map<String, Object> queryPartialScaleData(Map<String, Object> param) {
        processLedgerId(param);
		Map<String, Object> result = new HashMap<String, Object>();
		List<RealCurveBean> list = yearOnYearAnalysisMapper.queryPartialScaleData(param);
		result.put("chartData", list);
		return result;
	}

	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#queryEngergyRankingData(java.util.Map)
	 */
	@Override
	public Map<String, Object> queryEngergyRankingData(Map<String, Object> param) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		// 排名类型
		int rankingType = (Integer) param.get("rankingType");
		// 前一周期开始时间
		Date beginTime = (Date)param.get("incBeginTime");
		// 前一周期结束时间
		Date endTime = (Date)param.get("incEndTime");
		List<RealCurveBean> currData = null;
		List<RealCurveBean> lastData = null;
		List<RealCurveBean> resultData = new ArrayList<RealCurveBean>();
		Map<String, RealCurveBean> currMap = new HashMap<String, RealCurveBean>();
		Map<String, RealCurveBean> lastMap = new HashMap<String, RealCurveBean>();
		
		// 分户或增幅排名
		if (rankingType == 1 || rankingType == 3) {
			currData = yearOnYearAnalysisMapper.queryEnergyRankingData(param);
			// 增幅排名需要查询上一周期数据
			if (rankingType == 3) {
				param.put("beginTime", beginTime);
				param.put("endTime",   endTime);
				lastData = yearOnYearAnalysisMapper.queryEnergyRankingData(param);
				if (CollectionUtil.isNotEmpty(currData) && CollectionUtil.isNotEmpty(lastData)) {
					// 转换数据
					for (RealCurveBean bean : currData) {
						currMap.put(bean.getTypeId() + "", bean);
					}
					for (RealCurveBean bean : lastData) {
						lastMap.put(bean.getTypeId() + "", bean);
					}
				}
				RealCurveBean bean = null;
				double lastValue = 0;
				double diff = 0;
				for (Map.Entry<String, RealCurveBean> entry : currMap.entrySet()) {
					String key = entry.getKey();
					RealCurveBean value = entry.getValue();
					if (lastMap.containsKey(key) && null != lastMap.get(key)) {
						bean = new RealCurveBean();
						lastValue = lastMap.get(key).getDataValue();
						diff = new BigDecimal(value.getDataValue()).subtract(new BigDecimal(lastValue)).doubleValue();
						bean.setDataValue(diff);
						bean.setTypeName(value.getTypeName());
						resultData.add(bean);
					}
				}
			}
			else {
				resultData.addAll(currData);
			}
		}
		// 分项排名
		else if (rankingType == 2) {
			currData = yearOnYearAnalysisMapper.queryPartialScaleData(param);
			resultData.addAll(currData);
		}
		if (CollectionUtil.isNotEmpty(resultData)) {
			// 降序
			this.sortListDesc(resultData);
		}
		result.put("chartData", resultData);
		return result;
	}
	
    @Override
	public Map<String, Object> queryEngergyRankingDataNew(Map<String, Object> param) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
        //修正起始时间
        Date beginTime = (Date)param.get("beginTime");
        DateUtil.getCurrMonthFirstDay();
        Calendar cal = Calendar.getInstance();
		cal.setTime(beginTime);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        param.put("beginTime", cal.getTime());

        CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
        param.put("elecUnit", resource.getElecUnit());
        param.put("waterUnit", resource.getWaterUnit());
        param.put("gasUnit", resource.getGasUnit());
        param.put("hotUnit", resource.getHotUnit());

        List<Map<String, Object>> chartData = yearOnYearAnalysisMapper.queryEnergyRankingDataNew(param);
        if(chartData != null && chartData.size() > 10){
            List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
            subList.addAll(chartData.subList(0, 10));
            result.put("chartData", subList);
        }
        else{
            result.put("chartData", chartData);
        }
		return result;
	}
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#getRankingScaleInfo(java.util.Map)
	 */
	@Override
	public Map<String, Object> getRankingScaleInfo(Map<String, Object> param) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		int meterType = (Integer)param.get("meterType"); // 计量点类型
		int rankingType = (Integer)param.get("rankingType"); // 排名类型
		int statType = 0;                                // 统计类型
		double yesterdayStat = 0;                        // 昨日统计
		double eveStat = 0;                              // 前日统计
		double eveRate = 0;                              // 前日同期比
		double averageVal = 0;                           // 平均值
		// 电日数据
		if (1 == meterType) {
			statType = 2;
		}
		// 水日数据
		else if (2 == meterType) {
			statType = 5;
		}
		// 气日数据
		else {
			statType = 8;
		}
		param.put("statType", statType);
		String yesterday = DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
		Date startYesterday = DateUtil.convertStrToDate(yesterday + " 00:00:00");
		Date endYesterday = DateUtil.convertStrToDate(yesterday + " 23:59:59");
		
		// -------- 昨日消耗量 -------- //
		// 昨日日期
		param.put("beginTime", startYesterday);
		param.put("endTime",   endYesterday);
		// 查询昨日数据
		List<RealCurveBean> todayList = null;
		if (rankingType == 2) {
			// 分项
			todayList = yearOnYearAnalysisMapper.queryPartialScaleData(param);
		}
		else {
			// 分户、增幅
			todayList = yearOnYearAnalysisMapper.queryEnergyRankingData(param);
		}
		for (RealCurveBean realCurveBean : todayList) {
			yesterdayStat = DataUtil.doubleAdd(yesterdayStat, realCurveBean.getDataValue());
		}
		// 昨日统计
		result.put("yesterdayStat", yesterdayStat); 
		if (yesterdayStat != 0) {
			averageVal = DataUtil.doubleDivide(yesterdayStat, 24, 2);
		}
		// 平均值
		result.put("averageVal", NumberUtil.formatDouble(averageVal, NumberUtil.PATTERN_DOUBLE));
		
		// -------- 前日消耗量 -------- //
		// 前日同期日期
		param.put("beginTime", DateUtil.getSomeDateInYear(startYesterday, -1));
		param.put("endTime",   DateUtil.getSomeDateInYear(endYesterday, -1));
		// 查询前日同期数据
		List<RealCurveBean> lastWeekList = null;
		if (rankingType == 2) {
			// 分项
			lastWeekList = yearOnYearAnalysisMapper.queryPartialScaleData(param);
		}
		else {
			// 分户、增幅
			lastWeekList = yearOnYearAnalysisMapper.queryEnergyRankingData(param);
		}
		for (RealCurveBean realCurveBean : lastWeekList) {
			eveStat = DataUtil.doubleAdd(eveStat, realCurveBean.getDataValue());
		}
		if (eveStat != 0) {
			eveRate = new BigDecimal(yesterdayStat).subtract(new BigDecimal(eveStat)).divide(new BigDecimal(eveStat), 4, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).doubleValue();
		}
		// 前日同期占比
		result.put("eveRate", eveRate); 
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#queryEnergyTop(java.util.Map)
	 */
	@Override
	public Map<String, Object> queryEnergyTop(Map<String, Object> param) throws IllegalAccessException, InvocationTargetException {
		// 排名类型
		int rankingType = (Integer)param.get("rankingType");
//		// 统计周期
//		int periodType = (Integer)param.get("periodType");
		// 前一周期开始时间
		Date beginTime = (Date)param.get("incBeginTime");
		// 前一周期结束时间
		Date endTime = (Date)param.get("incEndTime");
		List<RealCurveBean> currData = null;
		List<RealCurveBean> lastData = null;
		Map<String, RealCurveBean> currMap = new HashMap<String, RealCurveBean>();
		Map<String, RealCurveBean> lastMap = new HashMap<String, RealCurveBean>();
		Map<String, Object> result = new HashMap<String, Object>();
		List<RealCurveBean> maxTop = null;
		List<RealCurveBean> minTop = null;
		List<RealCurveBean> inrTop = null;
		List<RealCurveBean> incrementTop = null;
		List<RealCurveBean> decrementTop = null;
		int size = 0;
		
		// 分户或增幅排名
		if (rankingType == 1 || rankingType == 3) {
			currData = yearOnYearAnalysisMapper.queryEnergyRankingData(param);
			param.put("beginTime", beginTime);
			param.put("endTime",   endTime);
			lastData = yearOnYearAnalysisMapper.queryEnergyRankingData(param);
		}
		// 分项排名
		else if (rankingType == 2) {
			currData = yearOnYearAnalysisMapper.queryPartialScaleData(param);
			param.put("beginTime", beginTime);
			param.put("endTime",   endTime);
			lastData = yearOnYearAnalysisMapper.queryPartialScaleData(param);
		}
		if (currData!=null && currData.size()>0 && lastData!=null && lastData.size()>0) {
			// 转换数据
            for(int i = 0; i < currData.size(); i++){
                currMap.put(currData.get(i).getTypeId() + "", currData.get(i));
            }
            for(int i = 0; i < lastData.size(); i++){
                lastMap.put(lastData.get(i).getTypeId() + "", lastData.get(i));
            }
			inrTop = new ArrayList<RealCurveBean>();
			RealCurveBean bean = null;
			double lastValue = 0;
			double rate = 0;
			for (Map.Entry<String, RealCurveBean> entry : currMap.entrySet()) {
				String key = entry.getKey();
				RealCurveBean value = entry.getValue();
				if (lastMap.containsKey(key) && null != lastMap.get(key)) {
					bean = new RealCurveBean();
					lastValue = lastMap.get(key).getDataValue();
					rate = new BigDecimal(value.getDataValue()).subtract(new BigDecimal(lastValue)).divide(new BigDecimal(lastValue), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue();// 保留两位小数
					bean.setDataValue(rate);
					bean.setTypeName(value.getTypeName());
					inrTop.add(bean);
				}
				else {
					value.setDataValue(100); // 上一周期无数据，就设置为100
					inrTop.add(value);
				}
			}
		}
		if (currData != null && currData.size() > 0) {
            size = currData.size();
			RealCurveBean bean = null;
			// 降序
			this.sortListDesc(currData);
			minTop = new ArrayList<RealCurveBean>();
			maxTop = new ArrayList<RealCurveBean>(currData.subList(0, (size >= 3) ? 3 : size));
			for (int i = 0; i < size; i++) {
				if (i == 3) {
					break;
				}
				bean = new RealCurveBean();
				BeanUtils.copyProperties(bean, currData.get(size - i - 1));
				minTop.add(bean);
				// 升序
				this.sortListAsc(minTop);
			}
		}
		if (inrTop != null && inrTop.size() > 0) {
            size = inrTop.size();
			RealCurveBean bean = null;
			// 降序
			this.sortListDesc(inrTop);
			decrementTop = new ArrayList<RealCurveBean>();
			incrementTop = new ArrayList<RealCurveBean>(inrTop.subList(0, (size >= 3 ? 3 : size)));
			for (int i = 0; i < size; i++) {
				if (i == 3) {
					break;
				}
				bean = new RealCurveBean();
				BeanUtils.copyProperties(bean, inrTop.get(size - i - 1));
				decrementTop.add(bean);
				// 升序
				this.sortListAsc(decrementTop);
			}
		}
		// ------------- 最大、最小、增幅、降幅 ------------ //
		// 最大值
		result.put("maxTop", maxTop);
		// 最小值
		result.put("minTop", minTop);
		// 增幅数据
		result.put("incrementTop", incrementTop);
		// 降幅数据
		result.put("decrementTop", decrementTop);
		return result;
	}
	
	/**
	 * 按照统计值升序排序
	 * @param list
	 * @param sorter
	 */
	private void sortListAsc(List<RealCurveBean> list){
		Collections.sort(list, new Comparator<RealCurveBean>(){
			@Override
			public int compare(RealCurveBean o1, RealCurveBean o2) {
				return DataUtil.doubleSubtract(o1.getDataValue(), o2.getDataValue()) <= 0 ? -1 : 1;
			}
		});
	}
	
	/**
	 * 按照统计值降序排序
	 * @param list
	 * @param sorter
	 */
	private void sortListDesc(List<RealCurveBean> list){
		Collections.sort(list, new Comparator<RealCurveBean>(){
			@Override
			public int compare(RealCurveBean o1, RealCurveBean o2) {
				return (new BigDecimal(o1.getDataValue())).compareTo(new BigDecimal(o2.getDataValue())) <= 0 ? 1 : -1;
			}
		});
	}
	
	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#queryHouseholdData(java.util.Map)
	 */
	@SuppressWarnings({ "unchecked"})
	@Override
	public Map<String, Object> queryHouseholdData(Map<String, Object> param)  {
		Map<String, Object> result = new HashMap<String, Object>();
		// 返回的数据 ledgerId -> periodDate -> item
		Map<String, Map<String, RealCurveBean>> resultData = new HashMap<String, Map<String, RealCurveBean>>();
		// X轴时间数据
		List<RealCurveBean> xData = new ArrayList<RealCurveBean>();  
		// 存放统计数(ledgerId -> item)
		Map<String, RealCurveBean> totalMap = new HashMap<String, RealCurveBean>();  
		Map<String, RealCurveBean> periodData = null;     // 周期数据
		RealCurveBean realBean = null;                    // 临时变量
		int minute = WebConstant.density;                // 曲线密度
		int totalMinute = 1440;                           // 一天的分钟数
		Date tmpDate = null;                              // 日期变量
		int amount = 0;                                   // 天数变量
		Date beginTime = (Date)param.get("beginTime");    // 开始时间
		Date endTime = (Date)param.get("endTime");        // 截止时间
		Date baseTime = new Date(beginTime.getTime());    // 基准开始时间
		
		int statType = (Integer)param.get("statType");    // 统计类型
		List<Long> ledgerIds = (List<Long>)param.get("ledgerIds"); // 目标比对分户Id
		Map<Long, String> ledgerNames = this.getLedgerName(ledgerIds); // 分户名称
        List<Long> meterIds = (List<Long>)param.get("meterIds"); // 目标比对DCP的Id
        Map<Long, String> meterNames = this.getMeterName(meterIds); // DCP名称
		try {
			// 曲线数据
			if (statType == 1) {
				// 得到x轴的点数
				amount = this.getPageCount(totalMinute, minute);
				for (int i = 0; i < ledgerIds.size() + meterIds.size(); i++) {  //EMO或DCP
					periodData = new TreeMap<String, RealCurveBean>();
					long baseLongTime = beginTime.getTime();   // 基准开始时间
					for (int j = 0; j < amount; j++) {
//						if (j > 0) {
							baseLongTime += 60000 * minute;
//						}
						// X轴时间数据
						if (i == 0) {
							realBean = new RealCurveBean();
							realBean.setStatDate(DateUtil.convertDateToStr(baseLongTime, DateUtil.DEFAULT_FULL_PATTERN));
							xData.add(realBean);
						}
						// 构造周期结构
						periodData.put(DateUtil.convertDateToStr(baseLongTime, DateUtil.DEFAULT_FULL_PATTERN), new RealCurveBean());
					}

                    if(i < ledgerIds.size()){
                        totalMap.put(ledgerNames.get(ledgerIds.get(i)), new RealCurveBean());
                        param.put("ledgerId", ledgerIds.get(i)); // 设置EMO Id参数
                    }
                    else{
                        totalMap.put(meterNames.get(meterIds.get(i - ledgerIds.size())), new RealCurveBean());
                        param.put("meterID", meterIds.get(i - ledgerIds.size())); // 设置DCP Id参数
                    }
					// 处理统计数据
					this.populateHouseholdData(periodData, DateUtil.DEFAULT_FULL_PATTERN, totalMap, param);
					// 存处理后的数据
                    if(i < ledgerIds.size()){
                        param.remove("ledgerId");
                        resultData.put(ledgerNames.get(ledgerIds.get(i)), periodData);
                    }
                    else {
                        param.remove("meterID");
                        resultData.put(meterNames.get(meterIds.get(i - ledgerIds.size())), periodData);
                    }
				}
			}
			// 日数据
			else if (statType == 2 || statType == 5 || statType == 8 || statType == 10 || statType == 12 || statType == 14 || statType == 20) {
				amount = DateUtil.getBetweenDays(beginTime, endTime);
				for (int i = 0; i < ledgerIds.size() + meterIds.size(); i++) {
					periodData = new TreeMap<String, RealCurveBean>();
					for (int j = 0; j <= amount; j++) {
						tmpDate = DateUtil.getSomeDateInYear(baseTime, j);
						// X轴时间数据
						if (i == 0) {
							realBean = new RealCurveBean();
							realBean.setStatDate(DateUtil.convertDateToStr(tmpDate, DateUtil.DEFAULT_SHORT_PATTERN));
							xData.add(realBean);
						}
						// 构造周期结构
						periodData.put(DateUtil.convertDateToStr(tmpDate, DateUtil.DEFAULT_SHORT_PATTERN), new RealCurveBean());
					}

                    if(i < ledgerIds.size()){
                        totalMap.put(ledgerNames.get(ledgerIds.get(i)), new RealCurveBean());
                        param.put("ledgerId", ledgerIds.get(i)); // 设置EMO Id参数
                    }
                    else{
                        totalMap.put(meterNames.get(meterIds.get(i - ledgerIds.size())), new RealCurveBean());
                        param.put("meterID", meterIds.get(i - ledgerIds.size())); // 设置DCP Id参数
                    }
					// 处理统计数据
					this.populateHouseholdData(periodData, DateUtil.DEFAULT_SHORT_PATTERN, totalMap, param);
					// 存处理后的数据
                    if(i < ledgerIds.size()){
                        param.remove("ledgerId");
                        resultData.put(ledgerNames.get(ledgerIds.get(i)), periodData);
                    }
                    else {
                        param.remove("meterID");
                        resultData.put(meterNames.get(meterIds.get(i - ledgerIds.size())), periodData);
                    }
				}
			}
			// 月数据
			else if (statType == 3 || statType == 6 || statType == 9 || statType == 11 || statType == 13 || statType == 15  || statType == 21) {
				amount = DateUtil.getBetweenMonthes(beginTime, endTime);
				for (int i = 0; i < ledgerIds.size() + meterIds.size(); i++) {
					periodData = new TreeMap<String, RealCurveBean>();
					for (int j = 0; j <= amount; j++) {
						tmpDate = DateUtil.getCalculateMonth(baseTime, j);
						// X轴时间数据
						if (i == 0) {
							realBean = new RealCurveBean();
							realBean.setStatDate(DateUtil.convertDateToStr(tmpDate, DateUtil.DEFAULT_SIMPLE_PATTERN));
							xData.add(realBean);
						}
						// 构造周期结构
						periodData.put(DateUtil.convertDateToStr(tmpDate, DateUtil.DEFAULT_SIMPLE_PATTERN), new RealCurveBean());
					}

                    if(i < ledgerIds.size()){
                        totalMap.put(ledgerNames.get(ledgerIds.get(i)), new RealCurveBean());
                        param.put("ledgerId", ledgerIds.get(i)); // 设置EMO Id参数
                    }
                    else{
                        totalMap.put(meterNames.get(meterIds.get(i - ledgerIds.size())), new RealCurveBean());
                        param.put("meterID", meterIds.get(i - ledgerIds.size())); // 设置DCP Id参数
                    }
					// 处理统计数据
					this.populateHouseholdData(periodData, DateUtil.DEFAULT_SIMPLE_PATTERN, totalMap, param);
					// 存处理后的数据
                    if(i < ledgerIds.size()){
                        param.remove("ledgerId");
                        resultData.put(ledgerNames.get(ledgerIds.get(i)), periodData);
                    }
                    else {
                        param.remove("meterID");
                        resultData.put(meterNames.get(meterIds.get(i - ledgerIds.size())), periodData);
                    }
				}
			}
		} catch (ParseException e) {
			Log.info("queryHouseholdData error ParseException");
		}
		catch (InvocationTargetException e) {
			Log.info("queryHouseholdData error InvocationTargetException");
		}
		catch (IllegalAccessException e) {
			Log.info("queryHouseholdData error IllegalAccessException");
		}
		// 折线图数据
		result.put("lineChartData", resultData);
		// X轴时间周期数据
		result.put("xData", xData);
		// 柱状图数据
		result.put("columnChartData", totalMap);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#getHouseholdScaleInfo(java.util.Map)
	 */
	@Override
	public Map<String, Object> getHouseholdScaleInfo(Map<String, Object> param) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		int meterType = (Integer)param.get("meterType"); // 计量点类型
		int statType = 0;                                // 统计类型
		double yesterdayStat = 0;                        // 昨日统计
		double eveStat = 0;                              // 前日统计
		double eveRate = 0;                              // 前日同期比
		double averageVal = 0;                           // 昨日平均值
		// 电日数据
		if (1 == meterType) {
			statType = 2;
		}
		// 水日数据
		else if (2 == meterType) {
			statType = 5;
		}
		// 气日数据
		else {
			statType = 8;
		}
		param.put("statType", statType);
		String yesterday = DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
		Date startYesterday = DateUtil.convertStrToDate(yesterday + " 00:00:00");
		Date endYesterday = DateUtil.convertStrToDate(yesterday + " 23:59:59");
		
		// -------- 算昨日消耗量 -------- //
		// 昨日日期
		param.put("beginTime", startYesterday);
		param.put("endTime",   endYesterday);
		// 查询昨日数据
		List<RealCurveBean> todayList = getEmoDcpEnergyStatData(param);
		for (RealCurveBean realCurveBean : todayList) {
			yesterdayStat = DataUtil.doubleAdd(yesterdayStat, realCurveBean.getDataValue());
		}
		// 昨日统计
		result.put("yesterdayStat", yesterdayStat); 
		if (yesterdayStat != 0) {
			averageVal = DataUtil.doubleDivide(yesterdayStat, 24, 2);
		}
		// 平均值
		result.put("averageVal", NumberUtil.formatDouble(averageVal, NumberUtil.PATTERN_DOUBLE));
		
		// -------- 算前日同期 -------- //
		// 上周同期日期
		param.put("beginTime", DateUtil.getSomeDateInYear(startYesterday, -1));
		param.put("endTime",   DateUtil.getSomeDateInYear(endYesterday, -1));
		// 查询前日数据
		List<RealCurveBean> lastWeekList = getEmoDcpEnergyStatData(param);
		for (RealCurveBean realCurveBean : lastWeekList) {
			eveStat = DataUtil.doubleAdd(eveStat, realCurveBean.getDataValue());
		}
		if (eveStat != 0) {
			eveRate = DataUtil.doubleMultiply(DataUtil.doubleDivide(DataUtil.doubleSubtract(yesterdayStat, eveStat), eveStat, 4), 100);
		}
		// 前日同期比
		result.put("eveRate", eveRate); 
		return result;
	}

    @Override
    public List<RealCurveBean> getEmoDcpEnergyStatData(Map<String, Object> param){
        List<RealCurveBean> list = new ArrayList<RealCurveBean>();
        if(param.keySet().contains("baseType") && param.keySet().contains("baseId")){
            Integer baseType = Integer.valueOf(param.get("baseType").toString());
            Long baseId = Long.valueOf(param.get("baseId").toString());
            if(baseType == 1){
                param.put("ledgerId", baseId);
                list = yearOnYearAnalysisMapper.queryEnergyStatData(param);
            }
            else if(baseType == 2){
                param.put("meterID", baseId);
                list = yearOnYearAnalysisMapper.queryMeterEnergyStatData(param);
            }
        }
        return list;
    }

    @Override
    public List<RealCurveBean> getEmoDcpHouseholdData(Map<String, Object> param){
        List<RealCurveBean> list = new ArrayList<RealCurveBean>();
        if(param.keySet().contains("baseType") && param.keySet().contains("baseId")){
            Integer baseType = Integer.valueOf(param.get("baseType").toString());
            Long baseId = Long.valueOf(param.get("baseId").toString());
            if(baseType == 1){
                param.put("ledgerId", baseId);
                list = yearOnYearAnalysisMapper.queryHouseholdData(param);
            }
            else if(baseType == 2){
                param.put("meterID", baseId);
                list = yearOnYearAnalysisMapper.queryHouseholdMeterData(param);
            }
        }
        return list;
    }
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.YearOnYearAnalysisService#getEnergyTop(java.util.Map)
	 */
	@Override
	public List<RealCurveBean> getEnergyTop(Map<String, Object> param) {
        processLedgerId(param);
		int topType = (Integer)param.get("topType");
		int statType = (Integer)param.get("statType");
		List<RealCurveBean> list = new ArrayList<RealCurveBean>();
		// 按分项统计
		if (topType == 1) {
			// 日数据不查曲线数据，查日数据
			if (statType == 1) {
				statType = 2;
			}
			else if (statType == 4) {
				statType = 5;
			}
			else if (statType == 7) {
				statType = 8;
			}
			param.put("statType", statType);
			list = yearOnYearAnalysisMapper.queryPartialScaleData(param);
		}
		// 按分户统计
		else {
            list = yearOnYearAnalysisMapper.getEnergyTop(param);
		}
		// 判断是否为空
		if (CollectionUtil.isNotEmpty(list)) {
			// 降序
			this.sortListDesc(list);
			// 取前三条
			if (list.size() >= 3) {
				list = list.subList(0, 3);
			}
		}
		return list;
	}

    private void processLedgerId(Map<String, Object> param){
        Long ledgerId = null;
        if(param.keySet().contains("baseType") && param.keySet().contains("baseId")){
            Integer baseType = Integer.valueOf(param.get("baseType").toString());
            Long baseId = Long.valueOf(param.get("baseId").toString());
            if(baseType == 1){
                LedgerBean ledgerBean= ledgerManagerMapper.selectByLedgerId(baseId);
                Long parentId = ledgerBean.getParentLedgerId();
                if(parentId != null && parentId > 0){
                    ledgerId = parentId;
                }
                else{
                    ledgerId = baseId;
                }
            }
            else if(baseType == 2){
                MeterBean meterBean = meterManagerMapper.getMeterDataByPrimaryKey(baseId);
                ledgerId = meterBean.getLedgerId();
            }
        }
        if(ledgerId != null){
            param.put("ledgerId", ledgerId);
        }
    }

    @Override
    public List<Long> getChildDevice(Long typeId){
        List<Long> list = this.yearOnYearAnalysisMapper.getChildDevice(typeId);
        if(list == null){
            return new ArrayList<Long>();
        }
        return list;
    }

    /**
	 * 处理分户对比数据
	 * @param periodData 周期数据
	 * @param pattern    时间格式
	 * @param totalMap   总数数据
	 * @param param      参数
	 * @throws ParseException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void populateHouseholdData(Map<String, RealCurveBean> periodData, String pattern, Map<String, RealCurveBean> totalMap, Map<String, Object> param) throws ParseException, IllegalAccessException, InvocationTargetException {
		double total = 0;
		String ledgerName = "";
		long ledgerId = 0;
		// 查询分户统计数据
        List<RealCurveBean> dataList = new ArrayList<RealCurveBean>();
        if(param.keySet().contains("meterID")){
            MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(Long.valueOf(param.get("meterID").toString()));
            param.put("isVirtual", meterBean.getIsVirtual());
            dataList = yearOnYearAnalysisMapper.queryHouseholdMeterData(param);
        }
        else{
            dataList = yearOnYearAnalysisMapper.queryHouseholdData(param);
        }
		for (RealCurveBean bean : dataList) {
			String keyTime = DateUtil.convertDateToStr(bean.getFreezeTime(), pattern);
			ledgerName = bean.getTypeName();
			ledgerId = bean.getTypeId();
			if (periodData.containsKey(keyTime)) {

                // bean格式化数据
                bean.setDataValue(DoubleUtils.getDoubleValue(bean.getDataValue(), 5));

				// 拷贝数据
				BeanUtils.copyProperties(periodData.get(keyTime), bean);
			}
			total = DataUtil.doubleAdd(total, bean.getDataValue());
		}
        RealCurveBean bean = totalMap.get(ledgerName);
        if(bean != null){
        	 // 算总数
            bean.setDataValue(DoubleUtils.getDoubleValue(total, 5));
            bean.setTypeName(ledgerName);
            bean.setTypeId(ledgerId);
        }
	}
	
	/**
	 * 根据分户Ids查询分户信息
	 * @param ledgerIds
	 * @return
	 */
	private Map<Long, String> getLedgerName(List<Long> ledgerIds) {
		Map<Long, String> result = new HashMap<Long, String>();
		// 查询分户信息
        if(ledgerIds != null && ledgerIds.size() > 0){
            List<LedgerBean> list = ledgerManagerMapper.getLedgerByIds(ledgerIds);
            if (CollectionUtil.isNotEmpty(list)) {
                for (LedgerBean ledgerBean : list) {
                    result.put(ledgerBean.getLedgerId(), ledgerBean.getLedgerName());
                }
            }
        }
		return result;
	}
    private Map<Long, String> getMeterName(List<Long> meterIds) {
        Map<Long, String> result = new HashMap<Long, String>();
        if (meterIds != null && meterIds.size() > 0) {
            for(int i =0; i < meterIds.size(); i++){
                Long meterId = meterIds.get(i);
                MeterBean meterBean = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                result.put(meterId, meterBean.getMeterName());
            }
        }
        return result;
    }

}
