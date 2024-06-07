package com.linyang.energy.controller.energyanalysis;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.OrderCondition;
import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.dto.LedgerCost;
import com.linyang.energy.service.ClfcHouseStatisticStatService;
import com.linyang.util.DateUtils;

/**
 * 能耗分类统计、能耗分户统计
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 3, 2014
 */
@Controller
@RequestMapping("/chssController")
public class ClfcHouseStatisticStatController extends BaseController{
	
	@Autowired
	private ClfcHouseStatisticStatService clfcHouseStatisticStatService;
	
	/**
	 * 能耗分类统计页面
	 * @return
	 */
	@RequestMapping("/showClfcPage")
	public ModelAndView showClfcPage(HttpServletRequest request){
		String currentTime = DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
		String beforeAfterDate = DateUtils.getBeforeAfterDate(currentTime, -7);
		//开始时间
		request.setAttribute("beginDate", beforeAfterDate);
		//结束时间
		request.setAttribute("endDate", currentTime);
		return new ModelAndView("/energy/energyanalysis/classification_ statistic");
	}
	
	
	
	/**
	 * 能耗分户统计页面
	 * @return
	 */
	@RequestMapping("/showHousePage")
	public ModelAndView showHousePage(HttpServletRequest request){
		String today = DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
		String yestday = DateUtils.getBeforeAfterDate(today, -1);
		//开始时间
		request.setAttribute("yestday", yestday);
		request.setAttribute("today", today);
		//结束时间
		return new ModelAndView("/energy/energyanalysis/household_statistics");
	}
	
	/**
	 * 能耗分类统计
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/clfcChart", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getClfcChartDatas(HttpServletRequest request) throws IOException{
		return clfcHouseStatisticStatService.getClfcChartDatas(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
	}
	/**
	 * 得到一个分户自己及下级分户（下一级）的能耗统计数据
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/clfcChildStat", method = RequestMethod.POST)
	public @ResponseBody List<LedgerCost> getLegderChildStat(HttpServletRequest request)throws IOException{
		return clfcHouseStatisticStatService.getLegderChildStat(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
	}
	/**
	 * 能耗分户统计
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getHouseStat", method = RequestMethod.POST)
	public  @ResponseBody Map<String,Object> getHouseStat(HttpServletRequest request) throws IOException{
		String pageInfo = request.getParameter("pageInfo");
		//Log.debug("pageInfo:"+pageInfo);
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(pageInfo);
		Page page = null;
		if(queryMap.containsKey("pageIndex") && queryMap.containsKey("pageSize")){
			page = new Page(Integer.parseInt(queryMap.get("pageIndex").toString()),Integer.parseInt(queryMap.get("pageSize").toString()),new OrderCondition(queryMap.get("order").toString(),queryMap.get("cloumName").toString()));
		}else
			page = new Page(1,2,new OrderCondition(queryMap.get("order").toString(),queryMap.get("cloumName").toString()));
		return clfcHouseStatisticStatService.getHouseStat(page,queryMap);
	}
	/**
	 * 得到分户的图片信息
	 * @param legerdIds
	 * @return
	 */
	@RequestMapping(value = "/getLegerdsPics", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getLegerdsPics(Long[] legerdIds){
		return clfcHouseStatisticStatService.getLegerdsPics(legerdIds);
	}
	

}
