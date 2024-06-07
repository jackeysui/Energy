package com.linyang.energy.service.impl;

import java.math.BigDecimal;import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leegern.util.CollectionUtil;
import com.leegern.util.DateUtil;
import com.leegern.util.NumberUtil;
import com.linyang.energy.mapping.energyanalysis.EnergyStatsAnalyseMapper;
import com.linyang.energy.model.RealCurveBean;
import com.linyang.energy.service.EnergyStatsAnalyseService;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.WebConstant;

/**
 * @Description 能耗分项统计分析Service实现
 * @author Leegern
 * @date Dec 11, 2013 10:51:23 AM
 */
@Service
public class EnergyStatsAnalyseServiceImpl implements EnergyStatsAnalyseService {
	@Autowired
	private EnergyStatsAnalyseMapper energyStatsAnalyseMapper;
	
	/**
	 * 每天多少毫秒数
	 */
	private static long DAY_SECOND = 86400 * 1000;
	
	/**
	 * 开始时间
	 */
	private static String TIME_START = " 00:00:00";
	
	/**
	 * 截止时间
	 */
	private static String TIME_END = " 23:59:59";
	
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EnergyStatsAnalyseService#queryStatData(java.util.Map)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> queryStatData(Map<String, Object> param) throws ParseException {
		// 查询能耗分项统计数据 
		List<RealCurveBean> list = energyStatsAnalyseMapper.queryStatData(param);
		Map<String, RealCurveBean> pieMap = new TreeMap<String, RealCurveBean>();
		// date - > typeName -> item
		Map<String, Map<String, RealCurveBean>> lineMap = new TreeMap<String, Map<String,RealCurveBean>>();
		if (CollectionUtil.isNotEmpty(list)) {
			
			int statType = (Integer)param.get("statType");
			Date beginTime = (Date)param.get("beginTime");
			Date endTime = (Date)param.get("endTime");
			int days = DateUtil.getBetweenDays(beginTime, endTime) + 1;
			long typeId = (Long)param.get("typeId");
			Date tmpDate = null;
			StringBuffer dateFormat = new StringBuffer();
			int type = 0;
			int minute = WebConstant.density;
			// 曲线数据精确到分钟
			if (statType == 1 || statType == 4 || statType == 7) {
				/*
				 * 点数          曲线间隔
				 * 49～96       15分钟
				 * 25～48       30分钟
				 * 1～24        60分钟
				 * 97～288      5分钟
				 * 289～1440    1分钟
				 */
				/*int count = energyStatsAnalyseMapper.queryCurveCount(param);
				if (count >= 49 && count <= 96) {
					minute = 15;
				}
				else if (count >= 25 && count <= 48) {
					minute = 30;
				}
				else if (count >= 1 && count <= 14) {
					minute = 60;
				}
				else if (count >= 97 && count <= 288) {
					minute = 5;
				}
				else if (count >= 289 && count <= 1440) {
					minute = 1;
				}*/
				int period = this.getPageCount(1440 * days, minute);
				Calendar cal = Calendar.getInstance();
				cal.setTime(beginTime);
				long time = cal.getTimeInMillis();
				for (int i = 0; i < period; i++) {
					if (i > 0) {
						cal.setTimeInMillis(time + minute * 60000 * (i + 1));
						tmpDate = cal.getTime();
					}
					else {
						tmpDate = beginTime;
					}
					dateFormat.append(DateUtil.convertDateToStr(tmpDate, DateUtil.DEFAULT_FULL_PATTERN).replace(" ", "_"));
					lineMap.put(dateFormat.toString(), new TreeMap<String, RealCurveBean>());
				}
				type = 1;
			}
			// 按日统计
			else if (statType == 2 || statType == 5 || statType == 8) {
				for (int i = 0; i < days; i++) {
					if (i > 0) {
						beginTime.setTime((beginTime.getTime() + DAY_SECOND));
					}
					tmpDate = beginTime;
					dateFormat.append(DateUtil.convertDateToStr(tmpDate, DateUtil.DEFAULT_SHORT_PATTERN));
					lineMap.put(dateFormat.toString(), new TreeMap<String, RealCurveBean>());
				}
				type = 2;
			}
			// 按周统计
			else if (statType == 10 || statType == 12 || statType == 14) {
				int period = this.getPageCount(days, 7);
				Date mondayDate = DateUtil.getMondayDateInWeek(beginTime);
				for (int i = 0; i < period; i++) {
					if (i > 0) {
						mondayDate = DateUtil.getSomeDateInYear(mondayDate, 7);
					}
					dateFormat.append(DateUtil.convertDateToStr(mondayDate, DateUtil.DEFAULT_SHORT_PATTERN));
					lineMap.put(dateFormat.toString(), new TreeMap<String, RealCurveBean>());
				}
				type = 3;
			}
			// 按月统计
			else if (statType == 3 || statType == 6 || statType == 9) {
				// 格式化日期(yyyy-MM)
				tmpDate = DateUtil.convertStrToDate(DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SIMPLE_PATTERN), DateUtil.DEFAULT_SIMPLE_PATTERN);
				Date tmpEnd = DateUtil.convertStrToDate(DateUtil.convertDateToStr(endTime, DateUtil.DEFAULT_SIMPLE_PATTERN), DateUtil.DEFAULT_SIMPLE_PATTERN);
				int amount = DateUtil.getBetweenMonthes(tmpDate, tmpEnd);
				Date date = tmpDate;
				for (int i = 0; i <= amount; i++) {
					if (i > 0) {
						date = DateUtil.getCalculateMonth(tmpDate, i);
					}
					dateFormat.append(DateUtil.convertDateToStr(date, DateUtil.DEFAULT_SIMPLE_PATTERN));
					lineMap.put(dateFormat.toString(), new TreeMap<String, RealCurveBean>());
				}
				type = 4;
			}
			// 按年统计
			else if (statType == 11 || statType == 13 || statType == 15) {
				int startYear = this.getDateYear(beginTime);
				int endYear = this.getDateYear(endTime);
				while (endYear >= startYear) {
					dateFormat.append(startYear);
					lineMap.put(dateFormat.toString(), new TreeMap<String, RealCurveBean>());
					startYear ++;
				}
				type = 5;
			}
			
			// ------- 根据分项Id查询子分项, 并且填充分项
			List<RealCurveBean> types = energyStatsAnalyseMapper.getSubDeviceTypes(typeId);
			int count = 0;
			for (Map.Entry<String, Map<String, RealCurveBean>> entry : lineMap.entrySet()) {
				Map<String, RealCurveBean> value = entry.getValue();
				for (RealCurveBean bean : types) {
					value.put(bean.getTypeName(), null);
					if (count == 0) {
						pieMap.put(bean.getTypeId() + "", bean);
					}
				}
				count ++;
			}
			
			// ------- 拼装数据
			for (RealCurveBean bean : list) {
				String keyTime = "";
				switch (type)
				{
				// 曲线
				case 1:
					keyTime = DateUtil.convertDateToStr(bean.getFreezeTime(), DateUtil.DEFAULT_FULL_PATTERN).replace(" ", "_");
					break;
				// 日
				case 2:
					keyTime = bean.getFreezeTimeStr();
					break;
				// 周
				case 3:
					keyTime = DateUtil.convertDateToStr(DateUtil.getMondayDateInWeek(bean.getFreezeTime()), DateUtil.DEFAULT_SHORT_PATTERN);
					break;
				// 月
				case 4:
					keyTime = DateUtil.convertDateToStr(bean.getFreezeTime(), DateUtil.DEFAULT_SIMPLE_PATTERN);
					break;
				// 年
				case 5:
					keyTime = this.getDateYear(bean.getFreezeTime()) + "";
					break;
				}
				if (lineMap.containsKey(keyTime)) {
					Map<String, RealCurveBean> currMap = lineMap.get(keyTime);
					String keyType = bean.getTypeName() + "";
					if (null == currMap.get(keyType)) {
						currMap.put(keyType, bean);
					}
					else {
						RealCurveBean currBean = currMap.get(keyType);
						currBean.setDataValue(DataUtil.doubleAdd(currBean.getDataValue(), bean.getDataValue()));
					}
				}
				// ------ 处理饼图数据(按照分项Id统计) ------ //
				String typeIdKey = bean.getTypeId() + "";
				if ( pieMap.containsKey(typeIdKey) ) {
					RealCurveBean rcb = pieMap.get(typeIdKey);
					rcb.setDataValue(DataUtil.doubleAdd(rcb.getDataValue(), bean.getDataValue()));
				}
			}
		}
		Map<String, Object> resultData = new HashMap<String, Object>();
		// 折线图数据
		resultData.put("lineData", lineMap);
		// 饼图数据
		resultData.put("pieData",  pieMap);
		return resultData;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EnergyStatsAnalyseService#getUseElecInfo(java.util.Map)
	 */
	@Override
	public Map<String, Object> getUseElecInfo(Map<String, Object> param) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		int meterType = (Integer)param.get("meterType"); // 计量点类型
		int statType = 0;                                // 统计类型
		double yesterdayStat = 0;
		double eveStat = 0;
		double eveRate = 0;
		double averageVal = 0; 
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
		// -------- 算昨日消耗量
		// 昨日日期
		String yesterday = DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
		param.put("beginTime", DateUtil.convertStrToDate(yesterday + TIME_START));
		param.put("endTime",   DateUtil.convertStrToDate(yesterday + TIME_END));
		// 昨日数据
		List<RealCurveBean> todayList = energyStatsAnalyseMapper.queryStatData(param);
		for (RealCurveBean realCurveBean : todayList) {
			yesterdayStat = DataUtil.doubleAdd(yesterdayStat, realCurveBean.getDataValue());
		}
		result.put("yesterdayStat", yesterdayStat); // 昨日统计
		if (yesterdayStat != 0) {
			averageVal = DataUtil.doubleDivide(yesterdayStat, 24, 2);
		}
		result.put("averageVal", averageVal);      // 昨日平均消耗
		// -------- 算前日消耗量
		// 前日日期
		param.put("beginTime", DateUtil.getSomeDateInYear(DateUtil.convertStrToDate(yesterday + TIME_START), -1));
		param.put("endTime",   DateUtil.getSomeDateInYear(DateUtil.convertStrToDate(yesterday + TIME_END),   -1));
		// 查询前日数据
		List<RealCurveBean> lastList = energyStatsAnalyseMapper.queryStatData(param);
		for (RealCurveBean realCurveBean : lastList) {
			eveStat = DataUtil.doubleAdd(eveStat, realCurveBean.getDataValue());
		}
		if (eveStat != 0) {
			eveRate = new BigDecimal(yesterdayStat).subtract(new BigDecimal(eveStat).divide(new BigDecimal(eveStat), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100))).doubleValue();
		}
		result.put("eveRate", eveRate); // 前日比对
		return result;
	}
	
//	/*
//	 * (non-Javadoc)
//	 * @see com.linyang.energy.service.EnergyStatsAnalyseService#getAverageValue(java.util.Map)
//	 */
//	@Override
//	public Map<String, Object> getAverageValue(Map<String, Object> param) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		Date beginDate = (Date)param.get("beginDate");   // 起始时间
//		Date endDate = (Date)param.get("endDate");       // 截止时间
//		int meterType = (Integer)param.get("meterType"); // 计量点类型
//		double total = 0;
//		double averageVal = 0;
//		// -------- 算平均消耗值
//		List<RealCurveBean> list = energyStatsAnalyseMapper.queryStatData(param);
//		for (RealCurveBean realCurveBean : list) {
//			total += realCurveBean.getDataValue();
//		}
//		if (total > 0) {
//			// 起止日期相差天数
//			int days = DateUtil.getBetweenDays(beginDate, endDate);
//			// 计量点是电的话, 需要换算成kw/h
//			if (meterType == 1) {
//				averageVal = total/((days + 1)/24) * 100;
//			}
//			else {
//				averageVal = total/(days + 1) * 100;
//			}
//		}
//		result.put("averageVal", NumberUtil.formatDouble(averageVal, NumberUtil.PATTERN_DOUBLE));
//		return result;
//	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EnergyStatsAnalyseService#getElecWaterGasPeak(java.util.Map)
	 */
	@Override
	public Map<String, Object> getElecWaterGasPeak(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		double todayPeak = 0;
		double monthPeak = 0;
		double yearPeak = 0;
		// 今日
		param.put("beginTime", DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN));
		param.put("endTime",   DateUtil.getCurrentDate(DateUtil.DEFAULT_FULL_PATTERN));
		Map<String, Object> today = energyStatsAnalyseMapper.getElecWaterGasPeak(param);
		if (CollectionUtil.isNotEmpty(today)) {
			todayPeak = Double.valueOf(today.get("DATA_VALUE").toString());
		}
		result.put("todayTotal", todayPeak);
		
		// 本月
		param.put("beginTime", DateUtil.getCurrMonthFirstDay());
		Map<String, Object> month = energyStatsAnalyseMapper.getElecWaterGasPeak(param);
		if (CollectionUtil.isNotEmpty(month)) {
			monthPeak = Double.valueOf(month.get("DATA_VALUE").toString());
		}
		result.put("monthTotal", monthPeak);
		
		// 本年
		param.put("beginTime", DateUtil.getCurrYearFirstDay());
		Map<String, Object> year = energyStatsAnalyseMapper.getElecWaterGasPeak(param);
		if (CollectionUtil.isNotEmpty(year)) {
			yearPeak = Double.parseDouble(year.get("DATA_VALUE").toString());
		}
		result.put("yearTotal", yearPeak);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EnergyStatsAnalyseService#getCurrentYearTop(java.util.Map)
	 */
	@Override
	public List<RealCurveBean> getCurrentYearTop(Map<String, Object> param) throws ParseException {
		return energyStatsAnalyseMapper.getElecWaterGasTopNData(param);
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
	 * 获取给定时间的年份
	 * @param date
	 * @return
	 */
	private int getDateYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}
}
