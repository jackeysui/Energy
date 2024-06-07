package com.linyang.energy.controller.energysavinganalysis;

import java.text.ParseException;
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

import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.DeviceTypeBean;
import com.linyang.energy.model.LedgerStatBean;
import com.linyang.energy.model.ProductStatBean;
import com.linyang.energy.service.EnergySavingRankingService;

/**
 * @Description 节能潜力排名Controller
 * @author Leegern
 * @date Feb 11, 2014 5:24:40 PM
 */
@Controller
@RequestMapping("/savingranking")
public class EnergySavingRankingController extends BaseController {
	@Autowired
	private EnergySavingRankingService energySavingRankingService;
	
	/**
	 * 进入节能潜力排名页面
	 * @return
	 */
	@RequestMapping(value = "/gotoSavingRankingMain")
	public ModelAndView gotoSavingRankingMain() {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params.put("baseTime", DateUtil.convertDateToStr(DateUtil.getLastMonthDate(new Date()), DateUtil.DEFAULT_SHORT_PATTERN));
		} catch (ParseException e) {
			Log.error(this.getClass() + ".gotoSavingRankingMain()--无法生成节能潜力排名");
		}
		return new ModelAndView(URLConstant.URL_SAVING_RANKING, "params", params);
	}
	
	/**
	 * 查询节能潜力统计Top数据
	 * @return products 产品, ledgers 建筑
	 */
	@RequestMapping(value = "/queryStatTopData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryStatTopData(HttpServletRequest request) {
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		Map<String, Object> result = null;
		try {
			// 收集查询参数
			Map<String, Object> param = this.populateParams(json);
			// 查询节能潜力统计Top数据 
			result = energySavingRankingService.queryStatTopData(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryStatTopData()--无法查询节能潜力统计Top数据");
		}
		return result;
	}
	
	/**
	 * 查询能耗类型
	 * @return
	 */
	@RequestMapping(value = "/getEnergyTypes", method = RequestMethod.POST)
	public @ResponseBody List<ProductStatBean> getEnergyTypes(HttpServletRequest request) {
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		List<ProductStatBean> energyTypes = null;
		try {
			// 收集查询参数
			Map<String, Object> param = this.populateParams(json);
			// 查询能耗类型
			energyTypes = energySavingRankingService.getEnergyTypes(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".getEnergyTypes()--无法查询能耗类型");
		}
		return energyTypes;
	}
	
	/**
	 * 查询分项根节点数据
	 * @return
	 */
	@RequestMapping(value = "/getDeviceTypes", method = RequestMethod.POST)
	public @ResponseBody List<DeviceTypeBean> getDeviceTypes() {
		return energySavingRankingService.getDeviceTypes();
	}
	
	/**
	 * 查询分项用电、水统计数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryParticalStatData", method = RequestMethod.POST)
	public @ResponseBody List<LedgerStatBean> queryParticalStatData(HttpServletRequest request) {
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		List<LedgerStatBean> result = null;
		try {
			Map<String, Object> param = this.populateParams(json);
			// 查询分项用电、水统计数据
			result = energySavingRankingService.queryParticalStatData(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryParticalStatData()--无法查询分项用电、水统计数据");
		}
		return result;
	}
	
	/**
	 * 处理查询参数
	 * @param json
	 * @return
	 * @throws ParseException 
	 */
	private Map<String, Object> populateParams(JSONObject json) throws ParseException {
		Map<String, Object> param = new HashMap<String, Object>();
		// 分析类型
		if (json.has("analyseType")) {
			param.put("analyseType", json.getInt("analyseType"));
		}
		// 查询类型
		if (json.has("queryType")) {
			param.put("queryType", json.getInt("queryType"));
		}
		// 分项Id
		if (json.has("typeId")) {
			param.put("typeId", json.getLong("typeId"));
		}
		// 时间类型
		if (json.has("dateType")) {
			int dateType = json.getInt("dateType");
			Date baseTime = null;
			Date beginTime = null;
			Date endTime = null;
			int season = 0;
			int year = 0;
			switch (dateType) {
			// 上月
			case 1:
				beginTime = DateUtil.getLastMonthFirstDay();
				String dateStr = DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SHORT_PATTERN);
				endTime = DateUtil.getLastDayOfMonth(DateUtil.convertStrToDate(dateStr + " 23:59:59"));
				break;
			// 上季度
			case 2:
				season = com.linyang.energy.utils.DateUtil.getQuartar(new Date());
				year = this.getDateYear(new Date());
				if (season == 1) {
//					beginTime = DateUtil.convertStrToDate((year - 1) + "-09-01 00:00:00");
//					endTime = DateUtil.getLastYearLastDay();
					beginTime = getDate(year - 1, 9, 1, 0, 0, 0);
					endTime = getDate(year - 1, 11, 31, 23, 59, 59);
				}
				else if (season == 2) {
//					beginTime = DateUtil.convertStrToDate(year + "-01-01 00:00:00");
//					endTime = DateUtil.convertStrToDate(year + "-03-31 23:59:59");
					beginTime = getDate(year, 0, 1, 0, 0, 0);
					endTime = getDate(year, 2, 31, 23, 59, 59);
				}
				else if (season == 3) {
//					beginTime = DateUtil.convertStrToDate(year + "-04-01 00:00:00");
//					endTime = DateUtil.convertStrToDate(year + "-06-30 23:59:59");
					beginTime = getDate(year, 3, 1, 0, 0, 0);;
					endTime = getDate(year, 5, 30, 23, 59, 59);
				}
				else {
//					beginTime = DateUtil.convertStrToDate(year + "-07-01 00:00:00");
//					endTime = DateUtil.convertStrToDate(year + "-09-30 23:59:59");
					beginTime = getDate(year, 6, 1, 0, 0, 0);
					endTime = getDate(year, 8, 30, 23, 59, 59);
				}
				break;
			// 上年
			case 3:
				beginTime = DateUtil.getLastYearFirstDay();
				endTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(DateUtil.getLastYearLastDay(), DateUtil.DEFAULT_SHORT_PATTERN) + " 23:59:59");
				break;
			// 自定义
			case 4:
				// 统计周期
				int periodType = json.getInt("periodType");
				baseTime = DateUtil.convertStrToDate(json.getString("baseTime"), DateUtil.DEFAULT_SHORT_PATTERN);
				// 月
				if (periodType == 1) {
					beginTime = DateUtil.getMonthFirstDay(baseTime);
					endTime = DateUtil.getLastDayOfMonth(baseTime);
				}
				// 季度
				else if (periodType == 2) {
					season = com.linyang.energy.utils.DateUtil.getQuartar(new Date());
					year = this.getDateYear(baseTime);
					if (season == 1) {
						beginTime = getDate(year, 0, 1, 0, 0, 0);
						endTime = getDate(year, 2, 31, 23, 59, 59);
					}
					else if (season == 2) {
						beginTime = getDate(year, 3, 1, 0, 0, 0);
						endTime = getDate(year, 5, 30, 23, 59, 59);
					}
					else if (season == 3) {
						beginTime = getDate(year, 6, 1, 0, 0, 0);
						endTime = getDate(year, 8, 30, 23, 59, 59);
					}
					else {
						beginTime = getDate(year, 9, 1, 0, 0, 0);
						endTime = getDate(year, 11, 31, 23, 59, 59);
					}
				}
				// 年
				else {
					beginTime = DateUtil.getYearFirstDay(baseTime);
					endTime = DateUtil.getYearLastDay(baseTime);
				}
				break;
			}
			param.put("beginTime", beginTime);
			param.put("endTime",   endTime);
			
		}
		return param;
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
	
	/**
	 * 获取日期时间
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param min
	 * @param sec
	 * @return
	 */
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
