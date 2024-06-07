package com.linyang.energy.controller.yunNanEnergy.CollectConfigManage;

import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.service.CollectConfigManageService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 20-5-12.
 */
@Controller
@RequestMapping("/processUnitController")
public class ProcessUnitController extends BaseController {

	 @Autowired
	 private CollectConfigManageService collectConfigManageService;

    /**
             * 进入“生产工序管理页面”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/processUnitManage")
    public ModelAndView processUnitManage(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        return new ModelAndView("energy/yunNan/CollectConfigManage/processUnitManage", params);
    }

    /**
	 * 
	 * 函数功能说明  :查询工序单元的列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getProcUnitPageList")
	public @ResponseBody Map<String, Object> getprocUnitPageList(HttpServletRequest request) throws IOException {
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
		mapQuery.put("ledgerId",this.getSessionUserInfo(request).getLedgerId());
		List<Map<String,Object>> list = collectConfigManageService.getProcUnitPageList(page,mapQuery);
		
	    map.put("queryMap",mapQuery);
		map.put("page", page);
		map.put("list", list);
		return map;
	}
	
	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param roleId 角色ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoProcUnitAddUpdate")
	public ModelAndView gotoProcUnitAddUpdate(HttpServletRequest request,String procId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> proc=new HashMap<String,Object>();
		long pId=Long.valueOf(procId);
		Map<String,Object> querymap=new HashMap<String,Object>();
		long entId=0l;
		if(pId>0){
			proc = collectConfigManageService.getProcUnitById(pId);
			querymap.put("entId", proc.get("ENT_ID")); //更新时获取本工序单元的企业id
			entId=Long.valueOf(proc.get("ENT_ID").toString());
		}else{
			proc.put("UNIT_ID", "0");
			proc.put("UNIT_CODE", "");
			proc.put("UNIT_NAME", "");
			proc.put("COMM_DATE", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
			proc.put("DESIGNED_CAPC", "");
			proc.put("PROCESS_ID", "0");
			proc.put("UPLOAD_STATUS", "");
			//proc.put("ENT_ID", request.getSession().getAttribute("SELECTED_ENT_ID")==null?"":request.getSession().getAttribute("SELECTED_ENT_ID")); //获取选择企业信息
			proc.put("ENT_NAME", request.getSession().getAttribute("SELECTED_ENT_NAME")==null?"":request.getSession().getAttribute("SELECTED_ENT_NAME"));
			Object entIdo=request.getSession().getAttribute("SELECTED_ENT_ID");
			if(entIdo!=null) {
				entId=Long.parseLong(request.getSession().getAttribute("SELECTED_ENT_ID").toString());
				proc.put("ENT_ID",entIdo);
			}
			else {
				proc.put("ENT_ID","");
			}
			querymap.put("entId", ""); 
		}
		
		map.put("proc",proc);
		map.put("unitcodes", collectConfigManageService.getProdPUnitCode(entId));
		//map.put("allprocs", collectConfigManageService.getAllProdProcList(querymap));
		
		return new ModelAndView("energy/yunNan/CollectConfigManage/proc_unit_add_update",map);
	}
	
	/**
	 * 获取工序单元编码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getProdUnitCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getProdUnitCode(HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<String, Object>();
		long entId=Long.parseLong(request.getParameter("entId"));	
		resultmap.put("unitcodes", collectConfigManageService.getProdPUnitCode(entId));
		return resultmap;
	}
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/mergeProcessUnitInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> mergeProcessUnitInfo(HttpServletRequest request){
		Map<String, String> resultmap = new HashMap<String, String>();
		int flag=0;
		Map<String, Object> unitMap=null;
		try {
			System.out.println(request.getParameter("procInfo"));
			unitMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(unitMap!=null) {
			if(unitMap.get("unitId").toString().equals("0"))
			{
				unitMap.put("unitId", SequenceUtils.getDBSequence()+"");
				flag=collectConfigManageService.addProcUnit(unitMap);
			}
			else {
				flag=collectConfigManageService.updateProcUnit(unitMap);
			}
			request.getSession().setAttribute("SELECTED_ENT_ID", unitMap.get("entId"));
			request.getSession().setAttribute("SELECTED_ENT_NAME", unitMap.get("entName"));
		}
		
		resultmap.put("flag", flag+"");
		return resultmap;
	}
	
	/**
	 * 修改分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteProcessUnit", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> deleteProcessUnit(HttpServletRequest request,String procId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		int flag = 0;
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		flag=collectConfigManageService.deleteProcUnit(Long.parseLong(procId));
		if(flag>0){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("delete a procId:").append(procId).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		result.put("flag", flag+"");
		return result;
	}
	
	/**
	 * 上传工序信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadProcUnit", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> uploadProcUnit(HttpServletRequest request,String procId,String typeId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		result=collectConfigManageService.uploadProcUnit(procId, typeId);
		if(result.get("flag").toString().equals("true")){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("upload a proc unit:").append(procId).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		return result;
	}
	
	/**
	 * 检查是否有工序单元依赖
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkUnitHasDepends", method = RequestMethod.POST)
	public @ResponseBody boolean checkUnitHasDepends(HttpServletRequest request,String unitId){
		return collectConfigManageService.getPUnitSonCounts(Long.valueOf(unitId))>0;
	}
}
