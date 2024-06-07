package com.linyang.energy.controller.energyanalysis;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.linyang.energy.service.ProductsEnergyService;

/**
 * @Description 单位产品能耗Controller
 * @author Leegern
 * @date Dec 11, 2013 10:58:24 AM
 */
@Controller
@RequestMapping("/productsenergy")
public class ProductsEnergyController extends BaseController {
	@Autowired
	private ProductsEnergyService productsEnergyService;
	
	/**
	 * 进入单位产品能耗页面
	 * @return
	 */
	@RequestMapping(value = "/gotoProductsEnergyMain")
	public ModelAndView gotoProductsEnergyMain() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("baseTime",   DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN));
		return new ModelAndView(URLConstant.URL_PRODUCTS_ENERGY, "params", params);
	}
	
	/**
	 * 查询产品能耗信息
	 * @param request
	 * @return products 产量, energy 能耗
	 */
	@RequestMapping(value = "/queryProductsEnergyInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryProductsEnergyInfo(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = null; 
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		// 处理请求参数
		try {
			this.populateParams(json, param);
			resultMap = productsEnergyService.queryProductsEnergyInfo(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryProductsEnergyInfo()--无法查询产品能耗信息");
		}
		return resultMap;
	}
	
	/**
	 * 处理请求参数
	 * @param json
	 * @param param
	 * @throws ParseException 
	 */
	private void populateParams(JSONObject json, Map<String, Object> param) throws ParseException {
		Date baseTime = null;                // 基准时间
		Date beginTime = null;               // 本周期开始时间
		Date endTime = null;                 // 本周期结束时间
		Date samePeriodBeginTime = null;     // 同期开始时间
		Date samePeriodEndTime = null;       // 同期结束时间
		Date thisPeriodTotalBeginTime = null;// 本期累计开始时间(所选日期所在的年累计)
		Date thisPeriodTotalEndTime = null;    // 本期累计结束时间 
		Date samePeriodTotalBeginTime = null;// 同期累计开始时间
		Date samePeriodTotalEndTime = null;    // 同期累计结束时间
		int season = 0;
		int year = 0;
		// 查询类型
		if (json.has("queryType")) {
			param.put("queryType", json.getInt("queryType"));
		}
		// 统计类型
		if (json.has("statType")) {
			switch (json.getInt("statType")) {
			// 上月
			case 1:
				beginTime = DateUtil.getLastMonthFirstDay();
				endTime = DateUtil.getLastDayOfMonth(beginTime);
				samePeriodBeginTime = DateUtil.getLastYearSameDate(beginTime);
				samePeriodEndTime = DateUtil.getLastYearSameDate(endTime);
				thisPeriodTotalBeginTime = DateUtil.getCurrYearFirstDay();
				thisPeriodTotalEndTime = endTime;
				samePeriodTotalBeginTime = DateUtil.getLastYearFirstDay();
				samePeriodTotalEndTime = samePeriodEndTime;
				break;
			// 上季度
			case 2:
				season = com.linyang.energy.utils.DateUtil.getQuartar(new Date());
				year = Calendar.getInstance().get(Calendar.YEAR);
				if (season == 1) {
					beginTime = getDate(year - 1, 9, 1, 0, 0, 0);
					endTime = getDate(year - 1, 11, 31, 23, 59, 59);
					thisPeriodTotalBeginTime = DateUtil.getLastYearFirstDay();
					thisPeriodTotalEndTime = DateUtil.getLastYearLastDay();
				}
				else {
					if (season == 2) {
						beginTime = getDate(year, 0, 1, 0, 0, 0);
						endTime = getDate(year, 2, 31, 23, 59, 59);
					}
					else if (season == 3) {
						beginTime = getDate(year, 3, 1, 0, 0, 0);;
						endTime = getDate(year, 5, 30, 23, 59, 59);
					}
					else {
						beginTime = getDate(year, 6, 1, 0, 0, 0);
						endTime = getDate(year, 8, 30, 23, 59, 59);
					}
					thisPeriodTotalBeginTime = DateUtil.getCurrYearFirstDay();
					thisPeriodTotalEndTime = endTime;
				}
				samePeriodBeginTime = DateUtil.getLastYearSameDate(beginTime);
				samePeriodEndTime = DateUtil.getLastYearSameDate(endTime);
				samePeriodTotalBeginTime = DateUtil.getLastYearSameDate(thisPeriodTotalBeginTime);
				samePeriodTotalEndTime = DateUtil.getLastYearSameDate(thisPeriodTotalEndTime);
				break;
			// 上年
			case 3:
				beginTime = DateUtil.getLastYearFirstDay();
				endTime = DateUtil.getLastYearLastDay();
				samePeriodBeginTime = DateUtil.getLastYearSameDate(beginTime);
				samePeriodEndTime = DateUtil.getLastYearSameDate(endTime);
				thisPeriodTotalBeginTime = beginTime;
				thisPeriodTotalEndTime = endTime;
				samePeriodTotalBeginTime = samePeriodBeginTime;
				samePeriodTotalEndTime = samePeriodEndTime;
				break;
			// 自定义
			default:
				int periodType = json.getInt("periodType");
				baseTime = DateUtil.convertStrToDate(json.getString("baseTime").toString(), DateUtil.DEFAULT_SIMPLE_PATTERN);
				// 月
				if (periodType == 1) {
					beginTime = DateUtil.getMonthFirstDay(baseTime);
					endTime = DateUtil.getLastDayOfMonth(baseTime);
					samePeriodBeginTime = DateUtil.getLastYearSameDate(beginTime);
					samePeriodEndTime = DateUtil.getLastYearSameDate(endTime);
					thisPeriodTotalBeginTime = DateUtil.getYearFirstDay(beginTime);
					thisPeriodTotalEndTime = endTime;
					samePeriodTotalBeginTime = DateUtil.getLastYearSameDate(thisPeriodTotalBeginTime);
					samePeriodTotalEndTime = DateUtil.getLastYearSameDate(endTime);
				}
				// 季度
				else if (periodType == 2) {
					season = com.linyang.energy.utils.DateUtil.getQuartar(new Date());
					Calendar cal = Calendar.getInstance();
					cal.setTime(baseTime);
					year = cal.get(Calendar.YEAR);
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
					samePeriodBeginTime = DateUtil.getLastYearSameDate(beginTime);
					samePeriodEndTime = DateUtil.getLastYearSameDate(endTime);
					thisPeriodTotalBeginTime = DateUtil.getYearFirstDay(beginTime);
					thisPeriodTotalEndTime = endTime;
					samePeriodTotalBeginTime = DateUtil.getLastYearSameDate(thisPeriodTotalBeginTime);
					samePeriodTotalEndTime = DateUtil.getLastYearSameDate(thisPeriodTotalEndTime);
				}
				break;
			}
			param.put("baseTime", baseTime);
			param.put("beginTime", beginTime);
			param.put("endTime", endTime);
			param.put("samePeriodBeginTime", samePeriodBeginTime);
			param.put("samePeriodEndTime", samePeriodEndTime);
			param.put("thisPeriodTotalBeginTime", thisPeriodTotalBeginTime);
			param.put("thisPeriodTotalEndTime", thisPeriodTotalEndTime);
			param.put("samePeriodTotalBeginTime", samePeriodTotalBeginTime);
			param.put("samePeriodTotalEndTime", samePeriodTotalEndTime);
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
