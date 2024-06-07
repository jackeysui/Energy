package com.linyang.energy.controller.yunNanEnergy.CollectConfigManage;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.RoleBean;
import com.linyang.energy.service.CollectConfigManageService;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 20-5-12.
 */
@Controller
@RequestMapping("/productProcessController")
public class ProductProcessController extends BaseController {

    @Autowired
    private CollectConfigManageService collectConfigManageService;

    /**
             * 进入“生产工序管理页面”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/productProcessManage")
    public ModelAndView productProcessManage(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        return new ModelAndView("energy/yunNan/CollectConfigManage/productProcessManage", params);
    }
    
    /**
	 * 
	 * 函数功能说明  :查询角色的列表
	 * @param pageInfo 前台参数集合
	 * @return  Map<String,Object>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getProdProcPageList")
	public @ResponseBody Map<String, Object> getProdProcPageList(HttpServletRequest request) throws IOException {
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
		List<Map<String,Object>> list = collectConfigManageService.getProdProcPageList(page,mapQuery);
		
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
	@RequestMapping("/gotoProcAddUpdate")
	public ModelAndView gotoProcAddUpdate(HttpServletRequest request,String procId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> proc=new HashMap<String,Object>();
		long pId=Long.valueOf(procId);
		long entId=0l;
		if(pId>0){
			proc = collectConfigManageService.getProdProcById(pId);
			entId=Long.valueOf(proc.get("ENT_ID").toString());
		}else{
			proc.put("PROCESS_ID", "0");
			proc.put("PROCESS_CODE", "");
			proc.put("PROCESS_NAME", "");
			Object entIdo=request.getSession().getAttribute("SELECTED_ENT_ID");
			if(entIdo!=null) {
				entId=Long.valueOf(request.getSession().getAttribute("SELECTED_ENT_ID").toString());
				proc.put("ENT_ID",entIdo);
			}
			else {
				proc.put("ENT_ID","");
			}
			//proc.put("ENT_ID", request.getSession().getAttribute("SELECTED_ENT_ID")==null?"":request.getSession().getAttribute("SELECTED_ENT_ID")); //获取选择企业信息
			proc.put("ENT_NAME", request.getSession().getAttribute("SELECTED_ENT_NAME")==null?"":request.getSession().getAttribute("SELECTED_ENT_NAME"));
		}
		
		map.put("proc",proc);
		map.put("proccodes",collectConfigManageService.getProdPCode(entId)); 
		return new ModelAndView("energy/yunNan/CollectConfigManage/prod_proc_add_update",map);
	}
	
	/**
	 * 获取工序编码
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getProdPcode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getProdPcode(HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<String, Object>();
		long entId=Long.valueOf(request.getParameter("entId"));	
		resultmap.put("proccodes", collectConfigManageService.getProdPCode(entId));
		return resultmap;
	}
	
	/**
	 * 合并分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mergeProdProcInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> mergeProdProcInfo(HttpServletRequest request){
		Map<String, String> resultmap = new HashMap<String, String>();
		int flag=0;
		Map<String, Object> procMap=null;
		try {
			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(procMap!=null) {
			if(procMap.get("procId").toString().equals("0"))
			{
				procMap.put("procId", SequenceUtils.getDBSequence()+"");
				flag=collectConfigManageService.addProdProc(procMap);
			}
			else {
				flag=collectConfigManageService.updateProdProc(procMap);
			}
			request.getSession().setAttribute("SELECTED_ENT_ID", procMap.get("entId"));
			request.getSession().setAttribute("SELECTED_ENT_NAME", procMap.get("entName"));
		}
		
		resultmap.put("flag", flag+"");
		return resultmap;
	}
	
	/**
	 * 删除工序信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteProdProc", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> deleteProdProc(HttpServletRequest request,String procId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		int flag = 0;
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		flag=collectConfigManageService.deleteProdProc(Long.parseLong(procId));
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
	@RequestMapping(value = "/uploadProdProc", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> uploadProdProc(HttpServletRequest request,String procId,String typeId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		result=collectConfigManageService.uploadProdProc(procId, typeId);
		if(result.get("flag").toString().equals("true")){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("delete a procId:").append(procId).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		return result;
	}
	
	/**
	 * 获取工序
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getProcList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getProcList(HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<String, Object>();
		Map<String, Object> procMap=null;
		List<Map<String,Object>> devices=new ArrayList<Map<String,Object>>();
		try {
			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			//e.printStackTrace();
		}
		if(procMap!=null) {
			devices=collectConfigManageService.getAllProdProcList(procMap);
		}
		resultmap.put("procs", devices);
		return resultmap;
	}
	
	/**
	 * 检查是否有工序依赖
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkProcHasDepends", method = RequestMethod.POST)
	public @ResponseBody boolean checkProcHasDepends(HttpServletRequest request,String procId){
		return collectConfigManageService.getProcSonCounts(Long.valueOf(procId))>0;
	}
}
