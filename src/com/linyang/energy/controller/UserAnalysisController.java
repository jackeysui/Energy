package com.linyang.energy.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.minlog.Log;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DateUtils;

@Controller
@RequestMapping("/userAnalysis")
public class UserAnalysisController extends BaseController {
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 功能点击率分析页面
	 * @return
	 */
	@RequestMapping("/showClickRatePage")
	public ModelAndView showClickRatePage(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(com.linyang.energy.utils.DateUtil.addDateDay(
				WebConstant.getChartBaseDate(), -1), com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		String beginTime = DateUtils.getBeforeAfterDate(currentTime, -30);

		map.put("beginTime", beginTime);
		map.put("endTime", currentTime);
		map.put("oledgerId", super.getSessionUserInfo(request).getLedgerId());
		return new ModelAndView("energy/userAnalysis/click_rate_analysis",map);
	}
	
	/**
	 * 功能点击率分析
	 * @return
	 */
	@RequestMapping("/clickRateAnalysis")
	public @ResponseBody Map<String, Object> clickRateAnalysis(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
			map.put("list", userAnalysisService.clickRateAnalysis(queryMap));
			map.put("oledgerId", super.getSessionUserInfo(request).getLedgerId());
		} catch (IOException e) {
			Log.info("clickRateAnalysis error IOException");
		}
		return map;
	}
	
	/**
	 * 用户活跃度分析页面
	 * @return
	 */
	@RequestMapping("/showActivityPage")
	public ModelAndView showActivityPage() {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(com.linyang.energy.utils.DateUtil.addDateDay(
				WebConstant.getChartBaseDate(), -1), com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		String beginTime = DateUtils.getBeforeAfterDate(currentTime, -30);

		map.put("beginTime", beginTime);
		map.put("endTime", currentTime);
		return new ModelAndView("energy/userAnalysis/user_activity_analysis",map);
	}
	
	
	/**
	 * 用户活跃度分析
	 * @return
	 */
	@RequestMapping("/clickActivity")
	public @ResponseBody Map<String, Object> clickActivity(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
			map.put("list", userAnalysisService.clickActivity(queryMap));
		} catch (IOException e) {
			Log.info("clickActivity error IOException");
		}
		return map;
	}

}
