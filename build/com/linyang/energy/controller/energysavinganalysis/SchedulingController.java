package com.linyang.energy.controller.energysavinganalysis;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.DateUtil;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.dto.FinanceBean;
import com.linyang.energy.model.LineLossBean;
import com.linyang.energy.model.MeterBean;
//import com.linyang.energy.service.CostService;
import com.linyang.energy.service.SchedulingService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DateUtils;

/**
 *排班分析（班次能耗分析、班次财务分析）、功率因数分析
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 3, 2014
 */
@Controller
@RequestMapping("/scheduleController")
public class SchedulingController extends BaseController{
	@Autowired
	private SchedulingService schedulingService;


	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 班次能耗分析页面
	 * @return
	 */
	@RequestMapping(value = "/showEnergyPage", method = RequestMethod.GET)
	public ModelAndView showEnergyPage(){
		Map<String,Object> map = new HashMap<String, Object>();
		String currentTime = DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
		String yeastday = DateUtils.getBeforeAfterDate(currentTime, -1);
		map.put("yeastday", yeastday);
		map.put("list", schedulingService.getAssembleInfo());
		return new ModelAndView("/energy/scheduleanalysis/energy_schedule_analysis",map);
	} 
	
	/**
	 * 
	 * @param scheduleId
	 * @return
	 */
	@RequestMapping(value = "/getSchedulerDetail", method = RequestMethod.POST)
	public  @ResponseBody List<Map<String,Object>> getSchedulerDetail(long scheduleId){
		return schedulingService.getSchedulerDetail(scheduleId);
	}
	
	/**
	 *、班次财务分析页面
	 * @return
	 */
	@RequestMapping(value = "/showFinancePage", method = RequestMethod.GET)
	public ModelAndView showFinancePage(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		String currentTime = DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
		String yeastday = DateUtils.getBeforeAfterDate(currentTime, -1);
		map.put("yeastday", yeastday);
		map.put("list", schedulingService.getAssembleInfo());
		map.put("density", WebConstant.density);
		return new ModelAndView("/energy/scheduleanalysis/energy_finance_analysis",map);
	} 
	
	/**
	 * 得到流水线下分时电价信息
	 * @param assembleId
	 * @return
	 */
	@RequestMapping(value = "/getRatePrice", method = RequestMethod.POST)
	public @ResponseBody  List<Map<String,Object>> getRatePrice(long assembleId){
		return schedulingService.getRatePrice(assembleId);
	}
	
	@RequestMapping(value = "/getQStat", method = RequestMethod.POST)
	public  @ResponseBody  Map<String,Object> getQStat(HttpServletRequest request) throws IOException{
		return schedulingService.getQStat(jacksonUtils.readJSON2Map(request.getParameter("pageInfo"))); 
	}
	
	@RequestMapping(value = "/getFutureQStat", method = RequestMethod.POST)
	public  @ResponseBody  Map<String,Object> getFutureQStat(HttpServletRequest request) throws IOException{
		return schedulingService.getFutureQStat(jacksonUtils.readJSON2Bean(FinanceBean.class, request.getParameter("pageInfo"))); 
	}
	
	/**
	 *、功率因数分析页面
	 * @return
	 */
	@RequestMapping(value = "/showPowerFactorPage", method = RequestMethod.GET)
	public ModelAndView showPowerFactorPage(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(com.linyang.energy.utils.DateUtil.addDateDay(
				WebConstant.getChartBaseDate(), -1), com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		String beginTime = DateUtils.getBeforeAfterDate(currentTime, -30);

		map.put("beginTime", beginTime);
		map.put("endTime", currentTime);
		return new ModelAndView("/energy/scheduleanalysis/power_factor_analysis", "params", map);
	} 
	
	/**
	 * 班次用能图表
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/energyUseChart", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getEnergyUseDatas(HttpServletRequest request) throws IOException{
		return schedulingService.getEnergyUseDatas(request,jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
	}
	
	/**
	 * 班次产品产量图表
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/productsChart", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getProductsDatas(HttpServletRequest request,long scheduleId) throws IOException{
		return schedulingService.getProductsDatas(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")),scheduleId);
	}
	/**
	 * 功率因数曲线相关信息
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/factorChart", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getPowerFactorDatas(HttpServletRequest request) throws IOException{
		Long operItemId = OperItemConstant.OPER_ITEM_26;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,29L,1);
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(queryMap);
		result.putAll(schedulingService.getPowerFactorDatas(queryMap));
//		result.putAll(costService.getPFEval(params));//功率因数评价
		return result;
	}
	
	
	/**
	 * 功率因数统计
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/factorStat", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPowerFactorStat(HttpServletRequest request) throws IOException{
		Long operItemId = OperItemConstant.OPER_ITEM_26;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,29L,1);
		return schedulingService.getPowerFactorStat(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
	}
	
	/**
	 * 电费改善预期
	 * @param pf
	 * @param rate
	 * @return
	 */
	@RequestMapping(value = "/powerFactor", method = RequestMethod.POST)
	public @ResponseBody Double getPowerFactor(double pf, double rate){
		return schedulingService.getFactor(pf, rate);
	}
	
	/**
	 * 负载率统计信息
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getLoadDataStat", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getLoadDataStat(HttpServletRequest request) throws IOException {
		Long operItemId = OperItemConstant.OPER_ITEM_35;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,102L,1);
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
		return schedulingService.getLoadDataStat(queryMap);
	}
	
	/**
	 * 取负载率曲线
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getCurveLoadData", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getCurveLoadData(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
		
		return schedulingService.getCurveLoadData(queryMap);
	}
	
	/**
	 * 取负载率发生时间曲线
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getCurveLoadDataTime", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getCurveLoadDataTime(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
		
		return schedulingService.getCurveLoadDataTime(queryMap);
	}

    /**
     * 取负载率分布曲线
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getCurveDistribution", method = RequestMethod.POST)
    public @ResponseBody List<Map<String, Object>> getCurveDistribution(HttpServletRequest request) throws IOException {
        Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

        return schedulingService.getCurveDistribution(queryMap);
    }

	
	/**
	 * 日负载率页面
	 * @param request
	 * @return
	 */
	@RequestMapping("showLoadRatio")
	public ModelAndView gotoLoadRatioPage(HttpServletRequest request){
		try {
			Date beginTime = com.linyang.energy.utils.DateUtil.addDateDay(WebConstant.getChartBaseDate(),-30);
			Date endTime = com.linyang.energy.utils.DateUtil.addDateDay(WebConstant.getChartBaseDate(),-1);
			request.setAttribute("beginTime", DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SHORT_PATTERN));
			request.setAttribute("endTime", DateUtil.convertDateToStr(endTime, DateUtil.DEFAULT_SHORT_PATTERN));
		} catch (ParseException e) {
			Log.error(this.getClass() + ".gotoLoadRatioPage()--无法查询日负载率");
		}
		return new ModelAndView(URLConstant.URL_DAY_LOAD_RATIO);
	}

	/**
	 * 取电流不平衡统计信息
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getIUDataStat", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getIUDataStat(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
		Long operItemId = OperItemConstant.OPER_ITEM_39;//电流不平衡度分析
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,103L,1);
		return schedulingService.getIUDataStat(queryMap);
	}

	/**
	 * 取电压不平衡统计信息
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getVUDataStat", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getVUDataStat(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
		Long operItemId = OperItemConstant.OPER_ITEM_37;//电压不平衡度分析
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,103L,1);
		return schedulingService.getVUDataStat(queryMap);
	}

	/**
	 * 取电流不平衡曲线
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getIUMaxData", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getIUMaxData(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

		return schedulingService.getIUMaxData(queryMap);
	}

	/**
	 * 取电压不平衡曲线
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getVUMaxData", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getVUMaxData(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

		return schedulingService.getVUMaxData(queryMap);
	}

	/**
	 * 取电流不平衡最大值发生时间曲线
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getIUMaxTimeData", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getIUMaxTimeData(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

		return schedulingService.getIUMaxTimeData(queryMap);
	}

	/**
	 * 取电压不平衡最大值发生时间曲线
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getVUMaxTimeData", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getVUMaxTimeData(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

		return schedulingService.getVUMaxTimeData(queryMap);
	}

	/**
	 * 取电流不平衡度越限日累计时间曲线
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getIULimitData", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getIULimitData(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

		return schedulingService.getIULimitData(queryMap);
	}

	/**
	 * 取电压不平衡度越限日累计时间曲线
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getVULimitData", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getVULimitData(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

		return schedulingService.getVULimitData(queryMap);
	}
	
	
	/**
	 * 日不平衡分析页面
	 * @param request
	 * @return
	 */
	@RequestMapping("showUnbalance")
	public ModelAndView gotoUnbalancePage(HttpServletRequest request){
		try {
			Date beginTime = com.linyang.energy.utils.DateUtil.addDateDay(WebConstant.getChartBaseDate(),-30);
			Date endTime = com.linyang.energy.utils.DateUtil.addDateDay(WebConstant.getChartBaseDate(),-1);
			request.setAttribute("beginTime", DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SHORT_PATTERN));
			request.setAttribute("endTime", DateUtil.convertDateToStr(endTime, DateUtil.DEFAULT_SHORT_PATTERN));
		} catch (ParseException e) {
			Log.error(this.getClass() + ".showUnbalance()--无法生成日不平衡分析");
		}
		return new ModelAndView(URLConstant.URL_DAY_UNBALANCE);
	}
	
	/**
	 * 线损分析页面
	 * @param request
	 * @return
	 */
	@RequestMapping("showLineLoss")
	public ModelAndView gotoLineLossPage(HttpServletRequest request){
		try {
			Date beginTime = com.linyang.energy.utils.DateUtil.addDateDay(WebConstant.getChartBaseDate(),-30);
			Date endTime = com.linyang.energy.utils.DateUtil.addDateDay(WebConstant.getChartBaseDate(),-1);
			request.setAttribute("beginTime", DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SHORT_PATTERN));
			request.setAttribute("endTime", DateUtil.convertDateToStr(endTime, DateUtil.DEFAULT_SHORT_PATTERN));
		} catch (ParseException e) {
			Log.error(this.getClass() + ".showLineLoss()--无法查询线损分析");
		}
		return new ModelAndView(URLConstant.URL_LINE_LOSS);
	}

	/**
	 * 根据电能示值计算每条线损的详细电量
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/getDayLineLossData", method = RequestMethod.POST)
	public @ResponseBody List<LineLossBean> getDayLineLossData(HttpServletRequest request) throws IOException {
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
		Long operItemId = OperItemConstant.OPER_ITEM_41;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,104L,1);
		return schedulingService.getDayLineLossData(queryMap);
	}
	
	/**
	 * 取一个线损的最大级数
	 * 
	 * @param request
	 * @param ledgerId
	 * @return
	 */
	@RequestMapping(value = "/getLineMaxLevel", method = RequestMethod.POST)
	public @ResponseBody Integer getLineMaxLevel(long ledgerId,int lossType) {
		return schedulingService.getLineMaxLevel(ledgerId,lossType);
	}

    /**
     * 节能量统计
     * @param request
     * @return
     */
    @RequestMapping("/showSaveEnergy")
    public ModelAndView showSaveEnergy(HttpServletRequest request){
        try {
            Date endTime = com.linyang.energy.utils.DateUtil.clearDate(WebConstant.getChartBaseDate());
            Date beginTime = com.linyang.energy.utils.DateUtil.addDateDay(endTime, -7);
            request.setAttribute("beginTime", DateUtil.convertDateToStr(beginTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
            request.setAttribute("endTime", DateUtil.convertDateToStr(endTime, com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
        }
        catch (ParseException e) {
            Log.error(this.getClass() + ".showSaveEnergy()--无法生成节能量统计");
        }
        return new ModelAndView(URLConstant.URL_SAVE_ENERGY);
    }

    /**
     * 节能量统计 -- 数据查询
     */
    @RequestMapping(value = "/getSaveEnergyData", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getSaveEnergyData(HttpServletRequest request){
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        Integer selectType = super.getIntParams(request, "selectType", 1);
        String beginTime = super.getStrParam(request, "beginTime", "");
        String endTime = super.getStrParam(request, "endTime", "");
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_123, 101l, 1);
        return this.schedulingService.getSaveEnergyData(ledgerId, selectType, beginTime, endTime);
    }

    /**
     * 节能量统计 -- 导出
     */
    @RequestMapping(value = "/exportSaveEnergyExcel")
    public @ResponseBody void exportSaveEnergyExcel(HttpServletRequest request , HttpServletResponse response ){
        try {
            JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));

            String filename = "节能量统计";
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1") + ".xls");
            response.setContentType("application/vnd.ms-excel");
            this.schedulingService.exportSaveEnergyExcel(response.getOutputStream(), json);
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_124, 101l, 1);
        }
        catch (IOException e) {
            Log.info("exportSaveEnergyExcel error IOException");
        }
    }

	/**
	 * 功率因数分析
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/pfAnalysis",  method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> pfAnalysis(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Long operItemId = OperItemConstant.OPER_ITEM_27;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,29L,1);
		return map;
	}
	
	/**
	 * 日负载率深度分析
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/analysis", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> analysis(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Long operItemId = OperItemConstant.OPER_ITEM_36;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,102L,1);
		return map;
	}
	
	/**
	 * 日负载率深度分析
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/UnbalanceAnalysis",  method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> UnbalanceAnalysis(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Long operItemId = OperItemConstant.OPER_ITEM_38;
		String selectType = request.getParameter("selectType");
		if(selectType==null){return map;}
		if(selectType.equals("2")){//电流不平衡度分析
			operItemId = OperItemConstant.OPER_ITEM_40;
		}
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,103L,1);
		return map;
	}
	
	@RequestMapping(value = "/querySummaryMeterByLedgerId",  method = RequestMethod.POST)
	public @ResponseBody List<MeterBean> querySummaryMeterByLedgerId(HttpServletRequest request){
		String ledgerId = request.getParameter("ledgerId");
		return schedulingService.querySummaryMeterByLedgerId(Long.parseLong(ledgerId));
	}
	
}
