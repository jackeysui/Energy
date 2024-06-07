package com.linyang.energy.controller;


import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.energy.common.URLConstant;
import com.linyang.energy.model.DesignBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.PowerInputBean;
import com.linyang.energy.service.DesignService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;

/**
 * 
 * @类功能说明：  生产计划controller
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2014-2-21 下午01:32:49  
 * @版本：V1.0
 */
@Controller
@RequestMapping("/designManager")
public class DesignController extends BaseController{
	/**
	 * 注入生产计划Service
	 */
	@Autowired
	private DesignService designService;
	
	/**
	 * 进入生产计划页面
	 * @return
	 */
	@RequestMapping(value = "/gotoDesignMain")
	public ModelAndView gotoDesignMain(){	
		Map<String, Object> map = new HashMap<String, Object>();
		String beginDate = DateUtil.getCurrentDateStr("yyyy-MM-dd HH:mm");
		String endDate = DateUtil.getTomorrowDateStr("yyyy-MM-dd HH:mm");
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		return new ModelAndView(URLConstant.URL_DESIGN_LIST,map);
	}
	
	/**
	 * 
	 * 函数功能说明  :初始化页面的能源类型和单位
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/setSelectData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> setSelectData(HttpServletRequest request) {
		Map<String, Object> quaryMap = new HashMap<String,Object> ();
		long productId=Long.valueOf(request.getParameter("productId"));
		List<Map<Long, String>> powerDatas = designService.getPowerData(productId);
		List<Map<Long, String>> unitDatas = designService.getUnitData();
		quaryMap.put("powerDatas", powerDatas);
		quaryMap.put("unitDatas", unitDatas);
		return quaryMap;
	}
	
	/**
	 * 
	 * 函数功能说明  :初始化页面的产品和流水线
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/setInitData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> setInitData(HttpServletRequest request) {
		Map<String, Object> quaryMap = new HashMap<String,Object> (); 
		List<Map<Long, String>> powerDatas = designService.getProductInfo();
		List<Map<Long, String>> unitDatas = designService.getAssembleData();
		quaryMap.put("products", powerDatas);
		quaryMap.put("assembles", unitDatas);
		return quaryMap;
	}
	
	/**
	 * 
	 * 函数功能说明  :得到生产计划列表
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/queryPlanList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryPlanList(HttpServletRequest request) {
		Map<String, Object> result = designService.getPlanListData();
		return result;
	}
	
	/**
	 * 
	 * 函数功能说明  :根据条件查询列表
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/searchDesign", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> searchDesign(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String,Object> ();
		String currentTime=DateUtil.getCurrentDateStr("yyyy-MM-dd");//当前时间
		String agoTime=DateUtil.getAWeekAgoDateStr("yyyy-MM-dd HH:mm");//当前时间一周前的时间
		String nextTime=DateUtil.addDay(currentTime, 7);//当前时间的下周时间
		String begin = " 00:00";
		String end=" 23:59";
		if(!"0".equals(request.getParameter("assembleId"))) {
			map.put("assembleId", Long.valueOf(request.getParameter("assembleId")));
		}
		if(!"0".equals(request.getParameter("productId"))) {
			map.put("productId", Long.valueOf(request.getParameter("productId")));
		}
		int value = Integer.valueOf(request.getParameter("checkDate"));
		if(value==1) {
			map.put("beginTime", DateUtil.convertStrToTimestamp(DateUtil.getMonday(agoTime)+begin,"yyyy-MM-dd HH:mm"));
			map.put("endTime", DateUtil.convertStrToTimestamp(DateUtil.getSunday(agoTime)+end,"yyyy-MM-dd HH:mm"));
		} 
		if(value==2) {
			map.put("beginTime", DateUtil.convertStrToTimestamp(DateUtil.getYesterdayDateStr("yyyy-MM-dd")+begin,"yyyy-MM-dd HH:mm"));
			map.put("endTime", DateUtil.convertStrToTimestamp(DateUtil.getYesterdayDateStr("yyyy-MM-dd")+end,"yyyy-MM-dd HH:mm"));
		}
		if(value==3) {
			map.put("beginTime", DateUtil.convertStrToTimestamp(currentTime+begin,"yyyy-MM-dd HH:mm"));
			map.put("endTime", DateUtil.convertStrToTimestamp(DateUtil.addDay(currentTime, 1)+begin,"yyyy-MM-dd HH:mm"));
		}
		if(value==4) {
			map.put("beginTime", DateUtil.convertStrToTimestamp(DateUtil.addDay(currentTime, 1)+begin,"yyyy-MM-dd HH:mm"));
			map.put("endTime", DateUtil.convertStrToTimestamp(DateUtil.addDay(currentTime, 2)+begin,"yyyy-MM-dd HH:mm"));
		}
		if(value==5) {
			map.put("beginTime", DateUtil.convertStrToTimestamp(DateUtil.getMonday(nextTime)+begin,"yyyy-MM-dd HH:mm"));
			map.put("endTime", DateUtil.convertStrToTimestamp(DateUtil.getSunday(nextTime)+end,"yyyy-MM-dd HH:mm"));
		}
		if(value==6) {
			map.put("beginTime", DateUtil.convertStrToTimestamp(request.getParameter("beginTime"),"yyyy-MM-dd HH:mm"));
			map.put("endTime", DateUtil.convertStrToTimestamp(request.getParameter("endTime"),"yyyy-MM-dd HH:mm"));
		}
		Map<String, Object> result = designService.searchDesign(map);
		return result;
	}
	
	/**
	 * 
	 * 函数功能说明  :根据Id删除生产计划
	 * @param scheduleId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/deletePlanData", method = RequestMethod.POST)
	public @ResponseBody boolean deletePlanData(HttpServletRequest request,Long planId) {
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("delete a plan,id is ")
	    .append(planId.toString());
		isSuccess = designService.deletePlanData(planId);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_DESIGN_ID,CommonOperaDefine.MODULE_NAME_DESIGN,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	/**
	 * @throws ParseException 
	 * 
	 * 函数功能说明  :根据ID得到计划相关信息
	 * @param request
	 * @param productId
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getPlanData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getProductData(HttpServletRequest request,long planId) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		DesignBean plan = designService.getPlanInfo(planId);
		List<PowerInputBean> dataList = designService.getPowerDataById(planId);
		List<Map<Long, String>> powerDatas = designService.getPowerData(plan.getProductId());
		List<Map<Long, String>> unitDatas = designService.getUnitData();
		result.put("list",dataList);
		result.put("plan", plan);
		result.put("powerData", powerDatas);
		result.put("unitData", unitDatas);
		result.put("beginTime", DateUtil.convertDateToStr(plan.getBeginTime(),"yyyy-MM-dd HH:mm"));
		result.put("endTime", DateUtil.convertDateToStr(plan.getEndTime(),"yyyy-MM-dd HH:mm"));
		return result;
	}
	
	/**
	 * @throws IOException 
	 * @throws ParseException 
	 * 
	 * 函数功能说明  :保存信息
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/savePlanInfo")
	public @ResponseBody boolean saveProductInfo (HttpServletRequest request) throws ParseException, IOException {
		String s=request.getParameter("powerData");
		DesignBean design = new DesignBean();
		design.setAssembleId(Long.valueOf(request.getParameter("assembleId")));
		design.setBeginTime(DateUtil.convertStrToDate(request.getParameter("beginTime"),"yyyy-MM-dd HH:mm"));	
		design.setEndTime(DateUtil.convertStrToDate(request.getParameter("endTime"),"yyyy-MM-dd HH:mm"));
		String typev=request.getParameter("typeValue");if(null!=typev&&typev.length()<=20&&typev.length()>0){
		design.setOutput(Double.valueOf(typev));}
		design.setProductId(Long.valueOf(request.getParameter("productId")));
		String optType = request.getParameter("optType");
		List<Map<String, Object>> powerList = jacksonUtils.readJSON2ListMap(s);
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		if("0".equals(optType)) { //新增
			//默认的费率序列
			design.setPlanId(SequenceUtils.getDBSequence());
			desc.append("add a plan, ")
		    .append(design.getPlanId());
			isSuccess = designService.addPlanData(design,powerList);
		} else {
			design.setPlanId(Long.valueOf(request.getParameter("planId")));
			desc.append("update a plan, ")
		    .append(design.getPlanId());
			isSuccess = designService.updatePlanData(design,powerList);
		}
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		if("0".equals(optType)) {
			super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_DESIGN_ID,CommonOperaDefine.MODULE_NAME_DESIGN,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		} else {
			super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_DESIGN_ID,CommonOperaDefine.MODULE_NAME_DESIGN,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		}
		return isSuccess;		
	}
}
