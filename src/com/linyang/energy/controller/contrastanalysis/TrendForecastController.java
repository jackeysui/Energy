package com.linyang.energy.controller.contrastanalysis;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.utils.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.TrendForecastService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.util.DateUtils;

/**
 * 趋势预测
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 15, 2014
 */
@Controller
@RequestMapping("/trendForecast")
public class TrendForecastController extends BaseController{
	
	@Autowired
	private TrendForecastService trendForecastService;

	/**
	 * 跳转到趋势预测页面
	 * @return
	 */
	@RequestMapping("/showPage")
	public ModelAndView showPage(){
		Map<String,Object> map = new HashMap<String, Object>();
		String currentTime = DateUtil.getTomorrowDateStr(DateUtil.SHORT_PATTERN);//DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
		map.put("beginDate",currentTime);
		map.put("endDate",DateUtils.getBeforeAfterDate(currentTime,7));
		map.put("maxDate",DateUtil.convertDateToStr(DateUtil.getNextYearDate(new Date()),DateUtils.FORMAT_SHORT));
        map.put("demo", WebConstant.demo);
		return new ModelAndView("energy/contrastanalysis/trend_forecast",map);
	}
	/**
	 * 获取趋势预测数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/tfChart", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getTrendForecastChartDatas(HttpServletRequest request) throws IOException{
		return trendForecastService.getTrendForecastChartDatas(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
	}
	
}
