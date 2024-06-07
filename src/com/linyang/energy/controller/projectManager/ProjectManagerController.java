
package com.linyang.energy.controller.projectManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.ProjectBean;
import com.linyang.energy.service.ProjectManagerService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;

/** 
 * @类功能说明： 
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-30 上午08:57:50  
 * @版本：V1.0  */
@Controller
@RequestMapping("/projectManagerController")
public class ProjectManagerController extends BaseController{
	@Autowired
	private ProjectManagerService projectManagerService;
	/**
	 * 
	 * 函数功能说明 :跳转到项目改造管理页面
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoProjectManager")
	public ModelAndView gotoProjectManager() {
		return new ModelAndView("energy/projectManager/projectManager");
	}
	/**
	 * 
	 * 函数功能说明 :跳转到图形页面
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoShowCharts")
	public ModelAndView gotoShowCharts(String projectId ,String objectId){
		if (objectId == null || objectId.trim().length()==0) objectId = "0"; Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		ProjectBean project =  projectManagerService.getProjectByProjectId(Long.valueOf(projectId));
		map.put("objectId",objectId);
		map.put("projectName",project.getProjectName());
		map.put("endTime", df.format(project.getEndTime()));
		return new ModelAndView("energy/projectManager/showCharts",map);
	}
	/**
	 * 函数功能说明  :展示图形
	 * @return  Map<String,Object>
	 * @throws IOException 
	 */
	@RequestMapping("/showCharts")
	public @ResponseBody Map<String, Object> showCharts(HttpServletRequest request) throws IOException{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		queryMap.put("objectId", queryMap.get("objectId"));
		long endDay = Long.valueOf(queryMap.get("endDate").toString())*1000;
		String stdDay = DateUtil.convertDateToStr(new Date(endDay), "yyyy-MM-dd");
		//按月对比
		if(queryMap.get("endDate") != null&& queryMap.get("operType").toString().equals("month")){
			queryMap.put("BeginDate", new Date(endDay));
			queryMap.put("BeginDate1", new Date(endDay+24*60*60));
			queryMap.put("lastEndDate", DateUtil.getLastMonthDate(new Date(endDay)));
			queryMap.put("nextEndDate", DateUtil.getNextMonthDate(new Date(endDay+24*60*60)));
			map.put("listM", projectManagerService.getMonthChartData(queryMap));
			map.put("listM1", projectManagerService.getMonthChartDataN(queryMap));	
			List<String> dateList = new ArrayList<String> ();
			for(int i=1;i<31;i++) {
				dateList.add(DateUtil.addDay(stdDay, i));
			}
			map.put("dayArray", dateList);
		} else if(queryMap.get("endDate") != null&& queryMap.get("operType").toString().equals("year")){
			//按年对比
			queryMap.put("BeginDate", DateUtil.getNextMonthDate(DateUtil.getCurrMonthFirstDay(new Date(endDay))));
			queryMap.put("BeginDate1", DateUtil.getCurrMonthFirstDay(new Date(endDay)));
			queryMap.put("lastEndDate", DateUtil.getLastYearDate(DateUtil.getCurrMonthFirstDay(new Date(endDay))));
			queryMap.put("nextEndDate", DateUtil.getNextYearDate(DateUtil.getCurrMonthFirstDay(new Date(endDay))));		
			List<Map<String,Object>> listY =projectManagerService.getYearChartData(queryMap);
			List<Map<String,Object>> listY1 =projectManagerService.getYearChartDataN(queryMap);
			map.put("listY",  listY);
			map.put("listY1", listY1);
			List<String> dateList = new ArrayList<String> ();
			//增长后的月份
			int mon = Integer.valueOf(stdDay.substring(5,7));
			int year = Integer.valueOf(stdDay.substring(0,4));
			for(int i=1;i<13;i++) {
				mon++;
				if(mon>12) {
					mon = mon-12;
					year += 1;
				}
				String str = (mon<10)?"0"+mon:mon+"";
				dateList.add(year+"-"+str+"-"+"01");
			}
			map.put("monArray", dateList);
		} else if(queryMap.get("endDate") != null&& queryMap.get("operType").toString().equals("quarter")){
			//按季度对比
			queryMap.put("BeginDate", new Date(endDay));
			queryMap.put("BeginDate1", new Date(endDay+24*60*60));
			queryMap.put("lastEndDate", DateUtil.getMonthDate(new Date(endDay), -3));
			queryMap.put("nextEndDate", DateUtil.getMonthDate(new Date(endDay+24*60*60),3));
			map.put("listQ", projectManagerService.getQuarterChartData(queryMap));
			map.put("listQ1", projectManagerService.getQuarterChartDataN(queryMap));	
			List<String> dateList = new ArrayList<String> ();
			for(int i=1;i<91;i++) {
				dateList.add(DateUtil.addDay(stdDay, i));
			}
			map.put("quarterArray", dateList);
		}
		return map;
	}

	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param accountId 用户ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoProjectAddUpdate")
	public ModelAndView gotoProjectAddUpdate(String projectId) {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if(CommonMethod.isNotEmpty(projectId)){
			ProjectBean project =  projectManagerService.getProjectByProjectId(Long.valueOf(projectId));
			map.put("beginTime", df.format(project.getBeginTime()));
			map.put("endTime", df.format(project.getEndTime()));
			map.put("project",project );
			map.put("type",1);//修改			
		}else{
			//map.put("user", new UserBean());
			map.put("type",0);//添加
			String currentTime = DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
			map.put("beginTime", currentTime);
			map.put("endTime", currentTime);
		}
		return new ModelAndView("energy/projectManager/project_add_update",map);
	}
	/**
	 * 
	 * 函数功能说明  :查询用户的列表
	 * @param pageInfo 前台参数集合
	 * @return  Map<String,Object>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getAjaxPageData")
	public @ResponseBody Map<String, Object> getAjaxPageData(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
			Page page = null;
			Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
			if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
				page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
			else
				page = new Page();
			if(queryMap.containsKey("queryMap"))
				mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
			List<ProjectBean> list = projectManagerService.getprojectPagedata(page,mapQuery);
			map.put("queryMap",mapQuery);	
			map.put("list",list);
			map.put("page", page);
		return map;
	}
	   /**
     * 
     * 函数功能说明  :新增项目
     * @param project 项目参数信息
     * @param investment 投资参数信息
     * @return  boolean     
     */
	@RequestMapping("/addProjectInfo")
	public @ResponseBody boolean addProjectInfo (HttpServletRequest request,ProjectBean project){
		//默认的用户序列
		project.setProjectId(SequenceUtils.getDBSequence());
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("add a project:" + project.getProjectName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = projectManagerService.addProjectInfo(project);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_PROJECT_ID,CommonOperaDefine.MODULE_NAME_PROJECT,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
		
	}
	/**
     * 
     * 函数功能说明  :修改项目信息
     * @param project  项目参数信息
     * @param investment 投资参数信息
     * @return  boolean     
     */ 
	@RequestMapping("/updateProjectInfo")
	public @ResponseBody boolean updateProjectInfo (HttpServletRequest request,ProjectBean project){
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("update a project:" + project.getProjectName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = projectManagerService.updateProjectInfo(project);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_PROJECT_ID,CommonOperaDefine.MODULE_NAME_PROJECT,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		  return isSuccess;	
			
	}
	/**
	 * 
	 * 函数功能说明  :删除项目
	 * @param projectId 项目参数信息
	 * @return  boolean     
	 */
	@RequestMapping("/deleteProject")
	public @ResponseBody boolean deleteProject (HttpServletRequest request,String projectId){
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("delete a project:" + this.projectManagerService.getProjectByProjectId(Long.valueOf(projectId)).getProjectName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = projectManagerService.deleteProject(projectId);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_PROJECT_ID,CommonOperaDefine.MODULE_NAME_PROJECT,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	/**
	 * 函数功能说明  :检查项目名称是否重复
	 * @param projectName 项目名称
	 * @param operType 操作类型
	 * @return  boolean
	 */
	@RequestMapping("/checkProjectName")
	public @ResponseBody boolean checkProjectName(String projectName,int operType ){
		return projectManagerService.checkProjectName(projectName,operType);
	}

}
