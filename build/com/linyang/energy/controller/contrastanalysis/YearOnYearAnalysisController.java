package com.linyang.energy.controller.contrastanalysis;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.leegern.util.ArrayUtil;
import com.leegern.util.DateUtil;
import com.leegern.util.StringUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.RealCurveBean;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.service.YearOnYearAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DateUtils;

/**
 * @Description 对比分析Controller
 * @author Leegern
 * @date Jan 14, 2014 3:53:49 PM
 */
@Controller
@RequestMapping("/yearanalysis")
public class YearOnYearAnalysisController extends BaseController {
	@Autowired
	private YearOnYearAnalysisService yearOnYearAnalysisService;
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	// --------------------------------- 同比占比 ------------------------------------- //
	/**
	 * 进入同比分析页面
	 * @return
	 */
	@RequestMapping(value = "/gotoYearAnalysisMain")
	public ModelAndView gotoYearAnalysisMain(){
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			String date = DateUtil.convertDateToStr(DateUtil.getSomeDateInYear(WebConstant.getChartBaseDate(), -1),
					DateUtil.DEFAULT_SHORT_PATTERN);
			String destTime = DateUtil.convertDateToStr(DateUtil.getSomeDateInYear(WebConstant.getChartBaseDate(), -2),
					DateUtil.DEFAULT_SHORT_PATTERN);
			params.put("destTime", destTime);
			params.put("baseTime", date);
			params.put("demo", WebConstant.demo);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".gotoYearAnalysisMain()--同比分析失败");
		}
		
		return new ModelAndView(URLConstant.URL_ENERGY_ANNULUS, "params", params);
	}
	
	/**
	 * 查询同比环比统计数据 
	 * @param request
	 * @return bigChartData 大图数据, smallChartData 小图数据
	 */
	@RequestMapping(value = "/queryEnergyStatData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryEnergyStatData(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// 处理查询参数
			this.populateParam(json, param);
            if (json.has("baseId") && json.has("baseType")) {
                param.put("baseType", json.getInt("baseType"));
                param.put("baseId", json.getLong("baseId"));
            }
			// 图表数据
			result = yearOnYearAnalysisService.queryEnergyStatData(param);
			
			Long operItemId = OperItemConstant.OPER_ITEM_24;
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,21L,1);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryEnergyStatData()--查询同比环比统计数据失败");
		}
		return result;
	}
	
	/**
	 * 查询耗电、水、气量(昨日统计、上周同期、上月同期、去年同期、平均消耗) 
	 * @param request
	 * @return yesterdayStat 昨日统计,       
	 * 		   lastWeekRate 上周同期比, lastMonthRate 上月同期比,
	 * 		   lastYearRate 去年同期比, averageVal 平均消耗量
	 */
	@RequestMapping(value = "/getScaleInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getScaleInfo(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// ------- 查询日消耗量 --------
			// 处理查询参数
			this.populateParam(json, param);
            if (json.has("baseId") && json.has("baseType")) {
                param.put("baseType", json.getInt("baseType"));
                param.put("baseId", json.getLong("baseId"));
            }
			// 查询耗电、水、气量(今日、去年同期) 
			result = yearOnYearAnalysisService.getScaleInfo(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".getScaleInfo()--无法查询耗电、水、气量(昨日统计、上周同期、上月同期、去年同期、平均消耗)");
		}
		return result;
	}
	
	/**
	 * 查询电水气峰值
	 * @param request
	 * @return todayTotal 今日, monthTotal 月峰值, yearTotal 年峰值 
	 */
	@RequestMapping(value = "/getElecWaterGasPeak", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getElecWaterGasPeak(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		// 计量点类型
		if (json.has("meterType")) {
			param.put("meterType", json.getInt("meterType"));
		}
        if (json.has("isVirtual")) {
            param.put("isVirtual", json.getInt("isVirtual"));
        }
		// EMO Id、DCP ID
		if (json.has("baseId") && json.has("baseType")) {
			param.put("baseId", json.getLong("baseId"));
			param.put("baseType", json.getInt("baseType"));
		}
		return yearOnYearAnalysisService.getElecWaterGasPeak(param);
	}
	
	/**
	 * 查询今年TopN日数据(电水气)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCurrentYearTop", method = RequestMethod.POST)
	public @ResponseBody List<RealCurveBean> getCurrentYearTop(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<RealCurveBean> result = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// 计量点类型
			if (json.has("meterType")) {
				param.put("meterType", json.getInt("meterType"));
			}
            if (json.has("isVirtual")) {
                param.put("isVirtual", json.getInt("isVirtual"));
            }
			// 起止时间
			param.put("beginTime", DateUtil.getCurrYearFirstDay());
			param.put("endTime",   DateUtil.getCurrentDate(null));
            // EMO Id 和 DCP ID
            if(json.has("baseType") && json.has("baseId")){
                Integer baseType = json.getInt("baseType");
                Long baseId = json.getLong("baseId");
                if(baseType == 1){
                    param.put("ledgerId", baseId);
                }
                else {
                    param.put("meterID", baseId);
                }
            }
			result = yearOnYearAnalysisService.getCurrentYearTop(param);
		} catch (ParseException e) {
            if(e != null){ Log.error(this.getClass() + ".getCurrentYearTop()--无法查询今年TopN日数据(电水气)");}
		}
		return result;
	}
	
	/**
	 * 处理查询参数
	 * @param json  
	 * @param param
	 * @return
	 * @throws ParseException 
	 */
	private Map<String, Object> populateParam(JSONObject json, Map<String, Object> param) throws ParseException {
		List<Date> destDates = new ArrayList<Date>();
		Date baseTime = null;
		int periodType = 0;
		// 统计类型
		if (json.has("statType")) {
			param.put("statType", json.getInt("statType"));
		}
		// 分户Id
		if (json.has("ledgerId")) {
			param.put("ledgerId", json.getLong("ledgerId"));
		}
		// 基准日期
		if (json.has("baseTime")) {
			baseTime = DateUtil.convertStrToDate(json.getString("baseTime"), DateUtil.DEFAULT_SHORT_PATTERN);
			param.put("baseTime", baseTime);
		}
		// 计量点类型
		if (json.has("meterType")) {
			param.put("meterType", json.getInt("meterType"));
		}
        // 是否虚拟计量点
        if (json.has("isVirtual")) {
            param.put("isVirtual", json.getInt("isVirtual"));
        }
		// 统计周期
		if (json.has("periodType")) {
			periodType = json.getInt("periodType");
			param.put("periodType", periodType);
		}
		// 时间类型
		if (json.has("dateType")) {
			int dateType = json.getInt("dateType");
			switch (dateType) {
			// 同比
			case 1:
				if (null != baseTime) {
					destDates.add(DateUtil.getLastYearSameDate(baseTime));
				}
				break;
			// 环比
			case 2:
				if (null != baseTime) {
					// 年统计周期
					if (periodType == 4) {
						destDates.add(DateUtil.getLastYearSameDate(baseTime));
					}
					else {
						destDates.add(DateUtil.getLastMonthDate(baseTime));
					}
				}
				break;
			// 自定义
			case 3:
				if (json.has("destTimes")) {
					String destTimes = json.getString("destTimes");
					if (StringUtil.isNotEmpty(destTimes)) {
						String[] array = destTimes.split(",");
						for (int i = 0; i < array.length; i++) {
							destDates.add(DateUtil.convertStrToDate(array[i], DateUtil.DEFAULT_SHORT_PATTERN));
						}
					}
				}
				break;
			}
			param.put("destDates",  destDates);
		}
		return param;
	}
	
	// --------------------------------- 分项占比 ------------------------------------- //
	/**
	 * 进入分项占比页面
	 * @return
	 */
	@RequestMapping(value = "/gotoPartialScaleMain")
	public ModelAndView gotoPartialScaleMain(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("baseTime",   DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN).substring(0, 10));
		return new ModelAndView(URLConstant.URL_PARTIAL_SCALE, "params", params);
	}
	
	/**
	 * 查询分项占比数据
	 * @param  request
	 * @return chartData 占比数据
	 */
	@RequestMapping(value = "/queryPartialScaleData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryPartialScaleData(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			this.populateScaleParam(json, param);
            if (json.has("baseId") && json.has("baseType")) {
                param.put("baseType", json.getInt("baseType"));
                param.put("baseId", json.getLong("baseId"));
            }
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryPartialScaleData()--无法查询分项占比数据");
		}
		return yearOnYearAnalysisService.queryPartialScaleData(param);
	}
	
	/**
	 * 处理对比分析查询参数
	 * @param json  
	 * @param param
	 * @return
	 * @throws ParseException 
	 */
	private void populateScaleParam(JSONObject json, Map<String, Object> param) throws ParseException {
        // 数据类型
        if (json.has("dataType")) {
			param.put("dataType", json.getInt("dataType"));
		}
        // 排序方式
        if (json.has("sortType")) {
            param.put("sortType", json.getInt("sortType"));
        }
		// 统计类型
		if (json.has("statType")) {
			param.put("statType", json.getInt("statType"));
		}
		// 分项Id
		if (json.has("typeId")) {
			param.put("typeId", json.getInt("typeId"));
		}
		// 分户Id
		if (json.has("ledgerId")) {
			param.put("ledgerId", json.getLong("ledgerId"));
		}
		// 排名类别
		if (json.has("rankingType")) {
			param.put("rankingType", json.getInt("rankingType"));
		}
		// 测量点类型
		if (json.has("meterType")) {
			param.put("meterType", json.getInt("meterType"));
		}
		// 快速选择时间类型
		if (json.has("dateType")) {
			int dateType = json.getInt("dateType");
            param.put("dateType", dateType);
			Date beginTime = null;
			Date endTime = null;
			Date incBeginTime = null;
			Date incEndTime = null;
			String start = " 00:00:00";
			String end = " 23:59:59";
			switch (dateType) {
			// 昨日
			case 1:
				beginTime = DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN + start));
				endTime   = DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN + end));
				
				incBeginTime = DateUtil.getSomeDateInYear(beginTime, -1);
				incEndTime = DateUtil.getSomeDateInYear(endTime, -1);
				break;
			// 上周
			case 2:
				String tmpDate = DateUtil.getAWeekAgoDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
				beginTime = DateUtil.getMondayDateInWeek(DateUtil.convertStrToDate(tmpDate + start));
				Date eDate = DateUtil.getSomeDateInYear(beginTime, 6);
				endTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(eDate, DateUtil.DEFAULT_SHORT_PATTERN) + end);
				
				incBeginTime = DateUtil.getSomeDateInYear(beginTime, -7);
				incEndTime = DateUtil.getSomeDateInYear(endTime, -7);
				break;
			// 上月
			case 3:
				beginTime = DateUtil.getLastMonthFirstDay();
				String dateStr = DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SHORT_PATTERN);
				endTime = DateUtil.getLastDayOfMonth(DateUtil.convertStrToDate(dateStr + end));
				
				incBeginTime = DateUtil.getCalculateMonth(beginTime, -1);
				incEndTime = DateUtil.getCalculateMonth(endTime, -1);
				break;
			// 上季度
			case 6:
				// 当前季度
				int currSeason = com.linyang.energy.utils.DateUtil.getQuartar(new Date());
				Date currDate = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
				int year = DateUtil.getYear(currDate);
				switch (currSeason) {
				case 1:
					beginTime = getDate(year - 1, 9, 1, 0, 0, 0);
					endTime = getDate(year - 1, 11, 31, 23, 59, 59);
					break;
				case 2:
					beginTime = getDate(year, 0, 1, 0, 0, 0);
					endTime = getDate(year, 2, 31, 23, 59, 59);
					break;
				case 3:
					beginTime = getDate(year, 3, 1, 0, 0, 0); 
					endTime = getDate(year, 5, 30, 23, 59, 59);
					break;
				case 4:
					beginTime = getDate(year, 6, 1, 0, 0, 0); 
					endTime = getDate(year, 8, 30, 23, 59, 59);
					break;
				}
				incBeginTime = DateUtil.getCalculateMonth(beginTime, -3);
				incEndTime = DateUtil.getCalculateMonth(endTime, -3);
				break;
			// 上年
			case 4:
				beginTime = DateUtil.getLastYearFirstDay();
				endTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(DateUtil.getLastYearLastDay(), DateUtil.DEFAULT_SHORT_PATTERN) + end);
				
				incBeginTime = DateUtil.getCalculateMonth(beginTime, -12);
				incEndTime = DateUtil.getCalculateMonth(endTime, -12);
				break;
			// 自定义
			case 5:
				// 统计周期
				int periodType = json.getInt("periodType");
				param.put("periodType", periodType);
				// 基准时间
				String baseTime = json.getString("baseTime");
				Date baseDateStart = DateUtil.convertStrToDate(baseTime + start);
				Date baseDateEnd = DateUtil.convertStrToDate(baseTime + end);
				// 日
				if (periodType == 2) {
					beginTime = baseDateStart;
					endTime = baseDateEnd;
					
					incBeginTime = DateUtil.getSomeDateInYear(beginTime, -1);
					incEndTime = DateUtil.getSomeDateInYear(endTime, -1);
				}
				// 周
				else if (periodType == 3) {
					beginTime = DateUtil.getMondayDateInWeek(baseDateStart);
					endTime = DateUtil.getSundayDateInWeek(baseDateEnd);
//					// 判断是否在当前周
//					if (baseDateStart.getTime() >= DateUtil.getMondayDateInWeek(new Date()).getTime()) {
//						endTime = DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN + end));
//					}
					
					incBeginTime = DateUtil.getSomeDateInYear(beginTime, -7);
					incEndTime = DateUtil.getSomeDateInYear(endTime, -7);
				}
				// 月
				else if (periodType == 4) {
					beginTime = DateUtil.getMonthFirstDay(baseDateStart);
					endTime = DateUtil.getLastDayOfMonth(baseDateEnd);
//					// 判断是否在当前月
//					if (baseDateStart.getTime() >= DateUtil.getMonthFirstDay(new Date()).getTime()) {
//						endTime = DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN + end));
//					}
					
					incBeginTime = DateUtil.getCalculateMonth(beginTime, -1);
					incEndTime = DateUtil.getCalculateMonth(endTime, -1);
				}
				// 年
				else if (periodType == 5) {
					beginTime = DateUtil.getYearFirstDay(baseDateStart);
					endTime = DateUtil.getYearLastDay(baseDateEnd);
//					// 判断是否在当前年
//					if (baseDateStart.getTime() >= DateUtil.getYearFirstDay(new Date()).getTime()) {
//						endTime = DateUtil.getLastDayOfMonth(DateUtil.getLastMonthDate(baseDateEnd));
//					}
					
					incBeginTime = DateUtil.getCalculateMonth(beginTime, -12);
					incEndTime = DateUtil.getCalculateMonth(endTime, -12);
				}
				// 季度
				else if (periodType == 6) {
					// 给定日期所在季度
					int season = com.linyang.energy.utils.DateUtil.getQuartar(new Date());
					Date giveDate = DateUtil.convertStrToDate(baseTime,DateUtil.DEFAULT_SHORT_PATTERN);
					int years = DateUtil.getYear(giveDate);
					switch (season) {
						case 1:
							beginTime = getDate(years, 0, 1, 0, 0, 0);
							endTime = getDate(years, 2, 31, 23, 59, 59);
							break;
						case 2:
							beginTime = getDate(years, 3, 1, 0, 0, 0); 
							endTime = getDate(years, 5, 30, 23, 59, 59);
							break;
						case 3:
							beginTime = getDate(years, 6, 1, 0, 0, 0); 
							endTime = getDate(years, 8, 30, 23, 59, 59);
							break;
						case 4:
							beginTime = getDate(years, 9, 1, 0, 0, 0);
							endTime = getDate(years, 11, 31, 23, 59, 59);
							break;
					}
					incBeginTime = DateUtil.getCalculateMonth(beginTime, -3);
					incEndTime = DateUtil.getCalculateMonth(endTime, - 3);
				}
				break;
			}
			// 起止日期
			param.put("beginTime", beginTime);
			param.put("endTime",   endTime);
			// 上一周期起止日期
			param.put("incBeginTime", incBeginTime);
			param.put("incEndTime",   incEndTime);
		}
	}
	
	// --------------------------------- 能耗排名 ------------------------------------- //
	/**
	 * 进入能耗排名页面
	 * @return
	 */
	@RequestMapping(value = "/gotoEnergyRankingMain")
	public ModelAndView gotoEnergyRankingMain(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("baseTime",   DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN).substring(0, 10));
		return new ModelAndView(URLConstant.URL_ENERGY_RANKING, "params", params);
	}
    
    @RequestMapping(value = "/gotoEnergyRankingMainNew")
	public ModelAndView gotoEnergyRankingMainNew(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("baseTime",   DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN).substring(0, 10));
		return new ModelAndView(URLConstant.URL_ENERGY_RANKING_NEW, "params", params);
	}
	
	/**
	 * 获取能耗排名数据
	 * @param request
	 * @return chartData 图表数据
	 */
	@RequestMapping(value = "/queryEngergyRankingData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryEngergyRankingData(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			this.populateScaleParam(json, param);
			result = yearOnYearAnalysisService.queryEngergyRankingData(param);
            //记录用户足迹
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_53, 211L, 1);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryEngergyRankingData()--无法获取能耗排名数据");
		}
		return result;
	} 
	
    @RequestMapping(value = "/queryEngergyRankingDataNew", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryEngergyRankingDataNew(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			this.populateScaleParam(json, param);
			result = yearOnYearAnalysisService.queryEngergyRankingDataNew(param);
            //记录用户足迹
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_53, 211L, 1);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryEngergyRankingDataNew()--无法查询电量排名数据");
		}
		return result;
	}
    
	/**
	 * 查询能耗排名耗电、水、气量(昨日统计、前日、平均消耗)
	 * @param request
	 * @return yesterdayStat 昨日统计, eveRate 前日同期比, averageVal 平均消耗值
	 */
	@RequestMapping(value = "/getRankingScaleInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getRankingScaleInfo(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// ------- 查询日消耗量 --------
			// 处理查询参数
			this.populateScaleParam(json, param);
			// 查询耗电、水、气量(今日、去年同期) 
			Map<String, Object> dataStat = yearOnYearAnalysisService.getRankingScaleInfo(param);
			result.putAll(dataStat);
			
//			// ------- 查询平均消耗值 --------
//			// 统计类型(statType=2、5、8, 取日数据)
//			if (json.has("meterType")) {
//				int meterType = json.getInt("meterType");
//				switch (meterType) {
//				// 电
//				case 1:
//					param.put("statType", 2);
//					break;
//				// 水
//				case 2:
//					param.put("statType", 5);
//					break;
//				// 气
//				case 3:
//					param.put("statType", 8);
//					break;
//				}
//				// 起止时间
//				param.put("beginDate", DateUtil.getCurrYearFirstDay());
//				param.put("endDate",   DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59"));
//				// 查询平均值(电水气) 
//				Map<String, Object> dataAvg = yearOnYearAnalysisService.getAverageValue(param);
//				result.putAll(dataAvg);
//			}
		} catch (ParseException e) {
			Log.error(this.getClass() + ".getRankingScaleInfo()--无法查询能耗排名耗电、水、气量(昨日统计、前日、平均消耗)");
		}
		return result;
	} 
	
	/**
	 * 查询能耗排名增幅、降幅TopN日数据(电水气)
	 * @param request
	 * @return maxTop 最大值, minTop 最小值, incrementTop 增幅前三, decrementTop 降幅前三 
	 */
	@RequestMapping(value = "/queryEnergyTop", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryEnergyTop(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = null;
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		try {
			this.populateScaleParam(json, param);

		} catch (ParseException e) {
			Log.info("queryEnergyTop error ParseException");
		} 
		try {
			result = yearOnYearAnalysisService.queryEnergyTop(param);
		} catch (IllegalAccessException e) {
			Log.info("queryEnergyTop error IllegalAccessException");
		} catch (InvocationTargetException e) {
			Log.info("queryEnergyTop error InvocationTargetException");
		} 
		
		return result;
	}

    // --------------------------------- 需求响应 ------------------------------------- //
    /**
     * 进入 需求响应 页面
     * @return
     */
    @RequestMapping(value = "/gotoEnergyDemand")
    public ModelAndView gotoEnergyDemand(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
                com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
        String baseTime = DateUtils.getBeforeAfterDate(currentTime, -1);
        params.put("baseTime",   baseTime);
        params.put("demo",   WebConstant.demo);
        params.put("ledgerId",   super.getSessionUserInfo(request).getLedgerId());
        return new ModelAndView(URLConstant.URL_ENERGY_DEMAND, "params", params);
    }

    // --------------------------------- 耗能类比 ------------------------------------- //
	/**
	 * 进入 耗能类比 页面
	 * @return
	 */
	@RequestMapping(value = "/gotoEnergyHouseholdMain")
	public ModelAndView gotoEnergyHouseholdMain(HttpServletRequest request){
		Map<String, Object> params = new HashMap<String, Object>();
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		String baseTime = DateUtils.getBeforeAfterDate(currentTime, -1);
		params.put("baseTime",   baseTime);
		params.put("demo",   WebConstant.demo);
		params.put("ledgerId",   super.getSessionUserInfo(request).getLedgerId());
		return new ModelAndView(URLConstant.URL_ENERGY_HOUSEHOLD, "params", params);
	} 
	
	/**
	 * 查询耗能类比数据
	 * @return lineChartData 折线数据, columnChartData 柱状图数据
	 */
	@RequestMapping(value = "/queryHouseholdData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryHouseholdData(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = null;
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		try {
			this.populateHouseholdParam(json, param);
			result = yearOnYearAnalysisService.queryHouseholdData(param); 
			
			Long operItemId = OperItemConstant.OPER_ITEM_25;
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,25L,1);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryHouseholdData()--无法查询耗能类比数据");
		}
		return result;
	}
	
	/**
	 * 查询耗能类比耗电、水、气量(昨日统计、前日、平均消耗)
	 * @param request
	 * @return yesterdayStat 昨日统计, eveRate 前日同期比, averageVal 平均消耗值
	 */
	@RequestMapping(value = "/getHouseholdScaleInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getHouseholdScaleInfo(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// ------- 查询日消耗量 -------- //
			// 处理查询参数
			this.populateParam(json, param);
            if (json.has("baseId") && json.has("baseType")) {
                param.put("baseType", json.getInt("baseType"));
                param.put("baseId", json.getLong("baseId"));
            }
			// 查询耗电、水、气量(今日、去年同期) 
			result = yearOnYearAnalysisService.getHouseholdScaleInfo(param);
		} catch (ParseException e) {
			Log.info("getHouseholdScaleInfo error ParseException");
		} 
		return result;
	}
	
	/**
	 * 查询分项、分户消耗(电水汽)前N名
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getEnergyTop", method = RequestMethod.POST)
	public @ResponseBody List<RealCurveBean> getEnergyTop(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<RealCurveBean> result = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// 处理分户对比参数
			this.populateHouseholdParam(json, param);
            param.put("accountId",super.getSessionUserInfo(request).getAccountId());
			result = yearOnYearAnalysisService.getEnergyTop(param);
		} catch (ParseException e) {
			Log.info("getEnergyTop error ParseException");
		}
		return result;
	}
	
	/**
	 * 处理分户对比参数
	 * @param json
	 * @param param
	 * @throws ParseException 
	 */
	private void populateHouseholdParam(JSONObject json, Map<String, Object> param) throws ParseException {
		List<Long> ledgerIds = new ArrayList<Long>();
		List<Long> meterIds = new ArrayList<Long>();
        param.put("ledgerIds", ledgerIds);
        param.put("meterIds", meterIds);
		// 处理特殊参数
		if (json.has("topType")) {
			param.put("topType", json.getInt("topType"));
		}
		// 比对EMO、DCP
		if (json.has("ledgerDest")) {
			String ledgerDest = json.getString("ledgerDest");
			if(ledgerDest.length() > 0){
				List<String> ids = ArrayUtil.arrayToList(ledgerDest.split(","));
				for (String id : ids) {
					ledgerIds.add(Long.parseLong(id));
				}
			}
			
		}
        if (json.has("meterDest")) {
        	String meterDest = json.getString("meterDest");
        	if(meterDest.length() > 0){
        		List<String> ids = ArrayUtil.arrayToList(meterDest.split(","));
            	for (String id : ids) {
                    meterIds.add(Long.parseLong(id));
                }
        	}
        }
		// 基准EMO、DCP
        if (json.has("baseId") && json.has("baseType")) {
            param.put("baseType", json.getInt("baseType"));
            param.put("baseId", json.getLong("baseId"));
        }
        // 统计类型
		if (json.has("statType")) {
			param.put("statType", json.getInt("statType"));
		}
		// 分项Id
		if (json.has("typeId")) {
			List<Long> list = new ArrayList<Long>();
			list.add(json.getLong("typeId"));
			param.put("typeId", list);
		}
		// 测量点类型
		if (json.has("meterType")) {
			param.put("meterType", json.getInt("meterType"));
		}
		// 快速选择时间类型
		if (json.has("dateType")) {
			int dateType = json.getInt("dateType");
			Date beginTime = null;
			Date endTime = null;
			String start = " 00:00:00";
			String end = " 23:59:59";
			switch (dateType) {
			// 昨日
			case 1:
				beginTime = DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN + start));
				endTime   = DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN + end));
				break;
			// 上周
			case 2:
				String tmpDate = DateUtil.getAWeekAgoDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
				beginTime = DateUtil.getMondayDateInWeek(DateUtil.convertStrToDate(tmpDate + start));
				Date eDate = DateUtil.getSomeDateInYear(beginTime, 6);
				endTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(eDate, DateUtil.DEFAULT_SHORT_PATTERN) + end);
				break;
			// 上月
			case 3:
				beginTime = DateUtil.getLastMonthFirstDay();
				String dateStr = DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SHORT_PATTERN);
				endTime = DateUtil.getLastDayOfMonth(DateUtil.convertStrToDate(dateStr + end));
				break;
			// 上季度
			case 6:
				// 当前季度
			    int quarter = com.linyang.energy.utils.DateUtil.getQuartar(new Date());
				int year = DateUtil.getYear(new Date());
				switch (quarter) {
				case 1:
					beginTime = getDate(year - 1, 9, 1, 0, 0, 0);
					endTime = getDate(year - 1, 11, 31, 23, 59, 59);
					break;
				case 2:
					beginTime = getDate(year, 0, 1, 0, 0, 0);
					endTime = getDate(year, 2, 31, 23, 59, 59);
					break;
				case 3:
					beginTime = getDate(year, 3, 1, 0, 0, 0); 
					endTime = getDate(year, 5, 30, 23, 59, 59);
					break;
				case 4:
					beginTime = getDate(year, 6, 1, 0, 0, 0); 
					endTime = getDate(year, 8, 30, 23, 59, 59);
					break;
				}
				break;
			// 上年
			case 4:
				beginTime = DateUtil.getLastYearFirstDay();
				endTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(DateUtil.getLastYearLastDay(), DateUtil.DEFAULT_SHORT_PATTERN) + end);
				break;
			// 自定义
			case 5:
				// 统计周期
				int periodType = json.getInt("periodType");
				param.put("periodType", periodType);
				// 基准时间
				String baseTime = json.getString("baseTime");
				Date baseDateStart = DateUtil.convertStrToDate(baseTime + start);
				Date baseDateEnd = DateUtil.convertStrToDate(baseTime + end);
				// 日
				if (periodType == 2) {
					beginTime = baseDateStart;
					endTime = baseDateEnd;
				}
				// 周
				else if (periodType == 3) {
					beginTime = DateUtil.getMondayDateInWeek(baseDateStart);
					endTime = DateUtil.getSundayDateInWeek(baseDateEnd);
				}
				// 月
				else if (periodType == 4) {
					beginTime = DateUtil.getMonthFirstDay(baseDateStart);
					endTime = DateUtil.getLastDayOfMonth(baseDateEnd);
				}
				// 年
				else if (periodType == 5) {
					beginTime = DateUtil.getYearFirstDay(baseDateStart);
					endTime = DateUtil.getYearLastDay(baseDateEnd);
				}
				break;
			}
			// 起止日期
			param.put("beginTime", beginTime);
			param.put("endTime",   endTime);
		}
	}
	
	private Date getDate(int year, int month, int day, int hour, int min, int sec) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, sec);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}