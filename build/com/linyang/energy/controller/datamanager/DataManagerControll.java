package com.linyang.energy.controller.datamanager;


import java.text.ParseException;
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
import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.TaskJobGroup;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.TaskGroupQueryService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.util.DateUtils;

/**
 * @Description 数据同步管理Controller
 * @author GaoP
 * @date Sep 1, 2014 10:32:16 AM
 */

@Controller
@RequestMapping("/datamanager")
public class DataManagerControll extends BaseController {
	
	@Autowired
	private TaskGroupQueryService taskgroupqueryservice;
	

	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/gotoDataManagerMain")
	public @ResponseBody ModelAndView gotoDataManagerMain(){
		return new ModelAndView(URLConstant.DATA_SYNC_MANAGER);
	}
	
	

	
	/**
	 * 查询任务列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryDataTask", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryDataTask(HttpServletRequest request){

		Map<String, Object> resultMap = new HashMap<String, Object>();
		//JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		// 分页信息
		//Page page = super.getCurrentPage(json.getString("pageNo"), json.getString("pageSize"));
		List<TaskJobGroup> result = null;
		
		
			result=taskgroupqueryservice.queryTaskGroupList();
		
		//resultMap.put("page", page);
		resultMap.put("dataInfo", result);
		return resultMap; 
	}
	
	/**
     * 
     * 函数功能说明  :修改任务群组信息
     * @param taskJobGroup  群组信息
     * @return  boolean     
     */ 
	@RequestMapping(value = "/UpdateJobTaskGroup", method = RequestMethod.POST)
	public @ResponseBody boolean UpdateJobTaskGroup(HttpServletRequest request,TaskJobGroup taskJobGroup){
		int isSuccess = 0;	
		taskJobGroup.setStartTime(DateUtils.convertDateStr2Date(taskJobGroup.getStartTimeStr(),"yyyy-MM-dd HH:mm:ss"));
		taskJobGroup.setEndTime(DateUtils.convertDateStr2Date(taskJobGroup.getEndTimeStr(),"yyyy-MM-dd HH:mm:ss"));		
		StringBuffer desc = new StringBuffer();
		desc.append("update a TaskJobGroup:" + taskJobGroup.getGroupName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));

		isSuccess = taskgroupqueryservice.updateTaskGroup(taskJobGroup);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess>0){
			taskgroupqueryservice.updateJobStatus(taskJobGroup.getGroupID());
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_USER,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		  return isSuccess>0;	
			
	}
}
