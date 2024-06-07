package com.linyang.energy.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.FlowLineBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.service.FlowLineService;
import com.linyang.energy.utils.CommonOperaDefine;

@Controller
@RequestMapping("/flowLine")
public class FlowLineManagerController extends BaseController{
	
	@Autowired
	private FlowLineService flowLineService;
	
	
	/**
	 * 跳转到页面
	 * @return
	 */
	@RequestMapping("/showPage")
	public ModelAndView showPage() {
		 return new ModelAndView("/energy/system/flowLine_manager");
	}
	
	/**
	 * 跳转到页面
	 * @return
	 */
	@RequestMapping("/showAddPage")
	public ModelAndView showAddPage() {
		 return new ModelAndView("/energy/system/flowline_add_simple","list",flowLineService.getScheduleInfos());
		 
	}
	
	
	/**
	 * 跳转到页面
	 * @return
	 */
	@RequestMapping("/showUpdatePage")
	public ModelAndView showUpdatePage(long flowlineId) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("list", flowLineService.getScheduleInfos());
		map.put("infoBean", flowLineService.getInfo(flowlineId));
		return new ModelAndView("/energy/system/flowline_update_simple",map);
	}
	
	@RequestMapping(value = "/pageList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> pageList(HttpServletRequest request) throws IOException{
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		Page page = null;
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", flowLineService.pageList(page, queryMap));
		map.put("page", page);
		return map;
	}
	
	
	@RequestMapping(value = "/addFlowLine", method = RequestMethod.POST)
	public @ResponseBody boolean addFlowLine(HttpServletRequest request) throws IOException{
		int result = CommonOperaDefine.OPRATOR_FAIL;
		boolean isSuccess = flowLineService.addFlowLine(jacksonUtils.readJSON2Bean(FlowLineBean.class, request.getParameter("pageInfo")));
		StringBuffer desc = new StringBuffer();
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		desc.append("add a flowLine, assemblename is ").append(jacksonUtils.readJSON2Bean(FlowLineBean.class, request.getParameter("pageInfo")).getAssembleName());
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_LINE_ID,CommonOperaDefine.MODULE_NAME_LINE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	
	@RequestMapping(value = "/updateFlowLine", method = RequestMethod.POST)
	public @ResponseBody boolean updateFlowLine(HttpServletRequest request)throws IOException{
		boolean isSuccess = flowLineService.updateFlowLine(jacksonUtils.readJSON2Bean(FlowLineBean.class, request.getParameter("pageInfo")));
		StringBuffer desc = new StringBuffer();
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		desc.append("update a flowLine, assembleId is ").append(jacksonUtils.readJSON2Bean(FlowLineBean.class, request.getParameter("pageInfo")).getAssembleId());
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_LINE_ID,CommonOperaDefine.MODULE_NAME_LINE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	
	@RequestMapping(value = "/deleteFlowLine", method = RequestMethod.POST)
	public @ResponseBody boolean deleteFlowLine(HttpServletRequest request,Long[] flowLineIds){
		boolean isSuccess = flowLineService.deleteFlowLine(Arrays.asList(flowLineIds));
		StringBuffer desc = new StringBuffer();
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		desc.append("delete flowLine, assembleId is ").append(flowLineIds[0]);
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_LINE_ID,CommonOperaDefine.MODULE_NAME_LINE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	@RequestMapping(value = "/getPlanLink", method = RequestMethod.POST)
	public @ResponseBody boolean getPlanLink(Long assembleId) {
		return flowLineService.getPlanLink(assembleId);
	}
	
	@RequestMapping(value = "/checkMeterLink", method = RequestMethod.POST)
	public @ResponseBody boolean checkMeterLink(Long meterId) {
		return flowLineService.getMeterLink(meterId);
	}
	
	@RequestMapping(value = "/checkFlowLineName", method = RequestMethod.POST)
	public @ResponseBody boolean checkFlowLineName(HttpServletRequest request) {
		String assembleName = request.getParameter("assembleName");
		String existName = request.getParameter("existName");
		String type = request.getParameter("type");
		return flowLineService.checkFlowLineName(assembleName,existName,type);
	}
}
