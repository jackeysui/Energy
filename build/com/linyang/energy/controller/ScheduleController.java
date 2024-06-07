package com.linyang.energy.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.UserAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.ScheduleBean;
import com.linyang.energy.model.ScheduleDetailBean;
import com.linyang.energy.service.ScheduleService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.CommonMethod;

/**
 * 
 * @类功能说明： 排班管理
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2014-1-21 上午11:11:10  
 * @版本：V1.0
 */
@Controller
@RequestMapping("/scheduleManager")
public class ScheduleController extends BaseController{
	/**
	 * 注入排班service
	 */
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 进入排班管理页面
	 * @return
	 */
	@RequestMapping(value = "/gotoScheduleList")
	public String gotoScheduleList(){	
		return URLConstant.URL_SCHEDULE_LIST;
	}
	
	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param scheduleId 班次ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoScheduleAddUpdate")
	public ModelAndView gotoScheduleAddUpdate(HttpServletRequest request,String scheduleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(CommonMethod.isNotEmpty(scheduleId)){
			ScheduleBean schedule = scheduleService.getScheduleData(Long.valueOf(scheduleId));
			map.put("schedule",schedule);
			map.put("type",1);//修改
		}else{
			map.put("type",0);//添加
		}
		map.put("density", WebConstant.density);
		return new ModelAndView("energy/system/schedule_add_update",map);
	}
	
	@RequestMapping(value = "/getGradeData", method = RequestMethod.POST)
	public @ResponseBody List<ScheduleDetailBean> getGradeData(HttpServletRequest request,String scheduleId) {
		List<ScheduleDetailBean> grade = scheduleService.getGradeData(Long.valueOf(scheduleId));
		return grade;
	}
	
	/**
	 * 
	 * 函数功能说明  :得到排班列表
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryScheduleList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryScheduleList(HttpServletRequest request) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		Page page = null;
		// 得到当前分页
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
		if(queryMap.containsKey("queryMap"))
			mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
		// 分页查询排班信息
		List<Map<String,Object>> dataList = scheduleService.getSchedulePageData(page, mapQuery);
		result.put("page", page);
		result.put("list",dataList);
		result.put("queryMap",mapQuery);
		return result;
	}
	
	/**
	 * 
	 * 函数功能说明  :根据计划Id删除排班计划
	 * @param scheduleId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/deleteScheduleData", method = RequestMethod.POST)
	public @ResponseBody boolean deleteScheduleData(HttpServletRequest request,Long scheduleId) {
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("delete a schedule,id is ")
	    .append(scheduleId.toString());
		isSuccess = scheduleService.deleteScheduleData(scheduleId);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_SCHEDULE_ID,CommonOperaDefine.MODULE_NAME_SCHEDULE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	/**
	 * 
	 * 函数功能说明  :检查是否有流水线和排班关联
	 * @param scheduleId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/getScheduleAssembleLink", method = RequestMethod.POST)
	public @ResponseBody boolean getScheduleAssembleLink(Long scheduleId) {
		return scheduleService.getScheduleAssembleLink(scheduleId);
	}
	
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的排班名称 
	 * @param scheduleName
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/checkScheduleName", method = RequestMethod.POST)
	public @ResponseBody boolean checkScheduleName(String scheduleName) {
		return scheduleService.checkScheduleName(scheduleName);
	}
	
	/**
	 * 
	 * 函数功能说明  :新增排班信息
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/addScheduleInfo")
	public @ResponseBody boolean addScheduleInfo (HttpServletRequest request) throws IOException{
		String s=request.getParameter("scheduleData");
		ScheduleBean schedule = new ScheduleBean();
		schedule.setRemark(request.getParameter("remark"));
		schedule.setScheduleName(request.getParameter("scheduleName"));
		//默认的排班序列
		schedule.setScheduleId(SequenceUtils.getDBSequence());
		List<Map<String, Object>> gradeList = jacksonUtils.readJSON2ListMap(s);
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("add a schedule, ")
	    .append(schedule.getScheduleName().toString());
		isSuccess = scheduleService.addScheduleData(schedule,gradeList);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_SCHEDULE_ID,CommonOperaDefine.MODULE_NAME_SCHEDULE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;		
	}
	
	/**
	 * 
	 * 函数功能说明  :修改排班信息
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/updateScheduleInfo")
	public @ResponseBody boolean updateScheduleInfo (HttpServletRequest request) throws IOException{
		String s=request.getParameter("scheduleData");
		ScheduleBean schedule = new ScheduleBean();
		schedule.setRemark(request.getParameter("remark"));
		schedule.setScheduleName(request.getParameter("scheduleName"));
		schedule.setScheduleId(Long.valueOf(request.getParameter("scheduleId")));
		List<Map<String, Object>> gradeList = jacksonUtils.readJSON2ListMap(s);
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("update a schedule, ")
	    .append(schedule.getScheduleName().toString());
		isSuccess = scheduleService.updateScheduleData(schedule,gradeList);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_SCHEDULE_ID,CommonOperaDefine.MODULE_NAME_SCHEDULE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
	}
}
