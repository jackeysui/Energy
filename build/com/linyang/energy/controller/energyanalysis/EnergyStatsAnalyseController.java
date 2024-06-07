package com.linyang.energy.controller.energyanalysis;


import java.text.ParseException;
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

import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.RealCurveBean;
import com.linyang.energy.service.EnergyStatsAnalyseService;

/**
 * @Description 能耗分项统计分析Controller
 * @author Leegern
 * @date Dec 11, 2013 10:48:43 AM
 */
@Controller
@RequestMapping("/energystats")
public class EnergyStatsAnalyseController extends BaseController {
	/**
	 * 注入能耗分项service
	 */
	@Autowired
	private EnergyStatsAnalyseService energyStatsAnalyseService;
	
	
	/**
	 * 进入能耗分析统计页面
	 * @return
	 */
	@RequestMapping(value = "/gotoEnergyStatsMain")
	public ModelAndView gotoEnergyStatsMain(){
		Map<String, Object> params = new HashMap<String, Object>();
		String date = DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
		params.put("beginTime", date);
		params.put("endTime",   date);
		return new ModelAndView(URLConstant.URL_ENERGY_STATS, "params", params);
	}
	
	/**
	 * 查询能耗分项统计数据 
	 * @param request
	 * @return lineData 折线数据, pieData 饼图数据
	 */
	@RequestMapping(value = "/queryStatData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryStatData(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// 处理查询参数
			this.populateParam(json, param);
			// 图表数据
			result = energyStatsAnalyseService.queryStatData(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryStatData()--无法查询能耗分项统计数据 ");
		}
		return result;
	}
	
	/**
	 * 查询耗电、水、气量(昨日、去年同期、平均消耗) 
	 * @param request
	 * @return yesterdayStat 今日统计, eveRate 前日对比, averageVal 平均消耗量
	 */
	@RequestMapping(value = "/getUseElecInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getUseElecInfo(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// ------- 查询日消耗量 --------
			// 处理查询参数
			this.populateParam(json, param);
			// 查询耗电、水、气量(昨日、前日对比) 
			Map<String, Object> dataStat = energyStatsAnalyseService.getUseElecInfo(param);
			result.putAll(dataStat);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".getUseElecInfo()--无法查询耗电、水、气量(昨日、去年同期、平均消耗)");
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
		// 分项Id
		if (json.has("typeId")) {
			param.put("typeId", json.getInt("typeId"));
		}
		// 分户Id
		if (json.has("ledgerId")) {
			param.put("ledgerId", json.getInt("ledgerId"));
		}
		return energyStatsAnalyseService.getElecWaterGasPeak(param);
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
			// 分户Id
			if (json.has("ledgerId")) {
				param.put("ledgerId", json.getInt("ledgerId"));
			}
			// 起止时间
			param.put("beginTime", DateUtil.getCurrYearFirstDay());
			param.put("endTime",   DateUtil.getCurrentDate(null));
			result = energyStatsAnalyseService.getCurrentYearTop(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".getCurrentYearTop()--无法查询今年TopN日数据(电水气)");
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
		// 统计类型
		if (json.has("statType")) {
			param.put("statType", json.getInt("statType"));
		}
		// 分项Id
		if (json.has("typeId")) {
			param.put("typeId", json.getLong("typeId"));
		}
		// 分户Id
		if (json.has("ledgerId")) {
			param.put("ledgerId", json.getLong("ledgerId"));
		}
		// 计量点类型
		if (json.has("meterType")) {
			param.put("meterType", json.getInt("meterType"));
		}
		// 时间类型
		if (json.has("dateType")) {
			int dateType = json.getInt("dateType");
			Date beginTime = null;
			Date endTime = null;
			switch (dateType) {
			// 昨日
			case 1:
				beginTime = DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN + " 00:00:00"));
				endTime   = DateUtil.convertStrToDate(DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN + " 23:59:59"));
				break;
			// 上周
			case 2:
				String tmpDate = DateUtil.getCurrentDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
				beginTime = DateUtil.getSomeDateInYear(DateUtil.getMondayDateInWeek(DateUtil.convertStrToDate(tmpDate + " 00:00:00")), -7);
				Date eDate = DateUtil.getSomeDateInYear(beginTime, 6);
				endTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(eDate, DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59");
				break;
			// 上月
			case 3:
				beginTime = DateUtil.getLastMonthFirstDay();
				String dateStr = DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SHORT_PATTERN);
				endTime = DateUtil.getLastDayOfMonth(DateUtil.convertStrToDate(dateStr + " 23:59:59"));
				break;
			// 上年
			case 4:
				beginTime = DateUtil.getLastYearFirstDay();
				endTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(DateUtil.getLastYearLastDay(), DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59");
				break;
			// 自定义
			case 5:
				beginTime = DateUtil.convertStrToDate(json.getString("beginTime") + " 00:00:00");
				endTime = DateUtil.convertStrToDate(json.getString("endTime") + " 23:59:59");
				break;
			}
			param.put("beginTime", beginTime);
			param.put("endTime",   endTime);
			
		}
		return param;
	}
}
