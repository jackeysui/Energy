package com.linyang.energy.controller.energydataquery;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import com.leegern.util.StringUtil;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.DayHarService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DateUtils;

/**
 * @Description 日谐波数据查询Controller 
 * @author 周礼
 * @date 2014.08.28 11:43:23
 */
@Controller
@RequestMapping("/DayHar")
public class DayHarController extends BaseController{
	@Autowired
	private DayHarService DayHarService;
	@Autowired
	private UserAnalysisService userAnalysisService;
	public static final String DEFAULT_SHORT_PATTERN = "yyyy-MM-dd";

	/**
	 * 日谐波电流电压最大值查询页面
	 * @return
	 */
	@RequestMapping("/showHarPage")
	public ModelAndView showVoltageCurrentPage(HttpServletRequest request){
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		request.setAttribute("time", DateUtils.getBeforeAfterDate(currentTime, -1));
		return new ModelAndView("/energy/dataquery/query_harmonic");
	}
	/**
	 * 得到日谐波柱状图数据
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/harChart", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getDayHarChartData(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String, Object>();
		try
		{
			JSONObject json = JSONObject.fromObject(request.getParameter("param"));
			Map<String, Object> param = this.populateParam(json);
			List<Map<String,Object>> har = DayHarService.getDayHarChartDatas(param);
			List<Map<String,Object>> dis = DayHarService.getDayDisChartDatas(param);
			map.put("Dis", dis);
			map.put("Har", har);
	
	        //记录用户足迹
			Long curveType = (Long)param.get("curveType");
			Long operItemId = OperItemConstant.OPER_ITEM_18;//谐波电流日最大值
			if(curveType == 2){//谐波电压含有率日最大值
				operItemId = OperItemConstant.OPER_ITEM_20;
			}
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,40L,1);
		}
		catch(ParseException e){
			Log.info("getDayHarChartData error ParseException");
		}
		return map;
	}
	/**
	 * 得到日谐波报表数据
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/harReport", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getDayHarReportData(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		try
		{
			JSONObject json = JSONObject.fromObject(request.getParameter("param"));
			Map<String, Object> param = this.populateParam(json);
			List<Map<String,Object>> har = DayHarService.getDayHarReportDatas(param);
			List<Map<String,Object>> dis = DayHarService.getDayDisReportDatas(param);
			map.put("Har", har);
			map.put("Dis", dis);
		}
		catch(ParseException ex){
			Log.info("getDayHarReportData error ParseException");
		}
		
		return map;
	}
	/**
	 * 导出到Excel
	 * @author 周礼
	 * @param 参数 request(页面请求),response(页面响应,返回excel)
	 */

	@RequestMapping(value = "/harExcel")
	public @ResponseBody void getEleExcel(HttpServletRequest request,HttpServletResponse response) {
		try{
			Map<String, Object> params = null;
			JSONObject json = JSONObject.fromObject(request.getParameter("param"));
			params = this.populateParam(json);
			int curveType = ((Long)params.get("curveType")).intValue();
			String title = "";
			switch( curveType ) {
			case 1:
				title = "谐波电流报表";
				break;
			case 2:
				title = "谐波电压报表";
				break;
	
			}
			byte[] bs = title.getBytes();
			response.setHeader("Content-Disposition", "attachment;filename="+new String(bs, "ISO-8859-1")+".xls");	//指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			//得到数据：数据库查询的结果集map，调用上面getDayHarReportData方法
			Map<String,Object> map = getDayHarReportData(request);
			//得到页面请求信息pageInfo，作为参数传进getHarExcel方法
			Map<String,Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("param"));
			DayHarService.getHarExcel("Cache.xls", response.getOutputStream(),map,queryMap);
			
			Long operItemId = OperItemConstant.OPER_ITEM_19;//谐波电流日最大值
			if(curveType == 2){//谐波电压含有率日最大值
				operItemId = OperItemConstant.OPER_ITEM_21;
			}
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,40L,1);
		}
		catch(ParseException e ) 
		{
			Log.info("getEleExcel error ParseException");
		}
		catch(UnsupportedEncodingException e ) 
		{
			Log.info("getEleExcel error UnsupportedEncodingException");
		}
		catch(IOException e ) 
		{
			Log.info("getEleExcel error IOException");
		}
	}
	private Map<String, Object> populateParam(JSONObject json) throws ParseException{
		Map<String, Object> params = new HashMap<String, Object>();
		if (! StringUtil.isEmpty(json.get("Time"))) {
			params.put("Time",convertStrToDate(json.getString("Time")));
			Date endTime = ( Date )params.get( "Time" ) ;
			Calendar cal_ = new GregorianCalendar( ) ;
			cal_.setTime( endTime ) ;
			cal_.add( Calendar.DAY_OF_MONTH , 1 ) ;	
			cal_.add( Calendar.SECOND , -1 ) ;	
			endTime =  cal_.getTime( ) ;
			params.put( "endTime" , endTime ) ;
			
		}
		if (json.has("curveType")) {
			params.put("curveType", json.getLong("curveType"));
		}
		if (json.has("meterId")) {
			params.put("meterId", json.getLong("meterId"));
		}
		if(json.has("meterName")){
			params.put("meterName",json.getString("meterName"));
		}
		if(json.has("curTimes")){
			params.put("curTimes",json.getInt("curTimes"));
		}
		return params;
	}
	
	public static Date convertStrToDate(String dateStr){
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		try{
			if(dateStr != null && dateStr.trim().length()>0) date = sdf.parse(dateStr);
		}
		catch (ParseException e) {
			Log.info("convertStrToDate error ParseException");
		}
		return date;
	}
	
	/**
	 * 得到曲线有功功率数据
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getCurChartData", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getCurChartData(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("param"));
			Map<String, Object> param = this.populateParam(json);
			result =  DayHarService.getCurChartData(param);

            //记录用户足迹
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),OperItemConstant.OPER_ITEM_100,40L,1);
		} catch (ParseException e) {
			Log.info("getCurChartData error ParseException");
		}
		return result;
	}
	
	/**
	 * 导出曲线谐波有功功率，曲线基波有功功率
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportExcel")
	public @ResponseBody void exportExcel(HttpServletRequest request,HttpServletResponse response) {
		try{
			JSONObject json = JSONObject.fromObject(request.getParameter("param"));
			Map<String, Object> param = this.populateParam(json);
			String title = "曲线谐波，曲线基波有功功率";
			byte[] bs = title.getBytes();
			response.setHeader("Content-Disposition", "attachment;filename="+new String(bs, "ISO-8859-1")+".xls");	//指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			Map<String,Object> map = DayHarService.getCurChartData(param);
			DayHarService.exportExcel("Cache.xls", response.getOutputStream(),map,param, title);
			
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),OperItemConstant.OPER_ITEM_101,40L,1);
		}
		catch(ParseException e ) 
		{
			Log.info("exportExcel error ParseException");
		}
		catch(UnsupportedEncodingException e ) 
		{
			Log.info("exportExcel error UnsupportedEncodingException");
		}
		catch(IOException e ) 
		{
			Log.info("exportExcel error IOException");
		}
	}
	
	/**
	 * 日谐电流电压曲线查询页面
	 * @return
	 */
	@RequestMapping("/showHarIVPage")
	public ModelAndView showHarIVPage(HttpServletRequest request){
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		request.setAttribute("time", DateUtils.getBeforeAfterDate(currentTime, -1));
		return new ModelAndView("/energy/dataquery/query_hariv");
	}
	
	
	/**
	 * 查询日谐电流电压曲线
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getHarIVData", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getHarIVData(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("param"));
			Map<String, Object> param = this.populateParam(json);
			result =  DayHarService.getHarIVData(param);

            //记录用户足迹
            int curveType = Integer.valueOf(param.get("curveType").toString());
            Long operItemId = OperItemConstant.OPER_ITEM_102;
            if(curveType == 2){
                operItemId = OperItemConstant.OPER_ITEM_103;
            }
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,227L,1);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".getCurChartData()--无法查询日谐电流电压曲线");
		}
		return result;
	}
}
