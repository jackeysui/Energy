package com.linyang.energy.controller.yunNanEnergy.CollectConfigManage;

import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 20-5-12.
 */
@Controller
@RequestMapping("/dataConfigController")
public class DataConfigController extends BaseController {

    @Autowired
    private CollectConfigManageService collectConfigManageService;
    @Autowired
    private LedgerManagerMapper ledgerManagerMapper;
    

    /**
             * 进入“采集配置管理页面”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/dataConfigManage")
    public ModelAndView collDataConfigManage(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        //频率
        params.put("frequces",CollConfigStaticData.getStaticFrequce());
      	//数据来源
        params.put("inputtypes",collectConfigManageService.getDataSrc());
        return new ModelAndView("energy/yunNan/CollectConfigManage/dataConfigManage", params);
    }
    
    /**
	 * 
	 * 函数功能说明  :查询角色的列表
	 * @param pageInfo 前台参数集合
	 * @return  Map<String,Object>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getCollDataConfigPageList")
	public @ResponseBody Map<String, Object> getCollDataConfigPageList(HttpServletRequest request) throws IOException {
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
		List<Map<String,Object>> list = collectConfigManageService.getDataConfigPageList(page,mapQuery);
		convertFrequce(list);
	    map.put("queryMap",mapQuery);
		map.put("page", page);
		map.put("list", list);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getCollDataConfigList")
	public @ResponseBody Map<String, Object> getDataConfigList(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();	
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("querymap"));		
		List<Map<String,Object>> list = collectConfigManageService.getDataConfigList(queryMap);
		map.put("datas", list);
		return map;
	}
	
	private void convertFrequce(List<Map<String,Object>> list)
	{
		for(Map<String,Object> map:list)
		{
			if(map.get("FREQUCE")!=null) {
				map.put("FREQUCE_STR", CollConfigStaticData.convertFrequenceStr(map.get("FREQUCE").toString()));
			}
			else {
				map.put("FREQUCE_STR","");
			}
		}
	}
	
	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param roleId 角色ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoDataConfigAddUpdate")
	public ModelAndView gotoDataConfigAddUpdate(HttpServletRequest request,String dataId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> data=new HashMap<String,Object>();
		long dId=Long.valueOf(dataId);
		Map<String,Object> querymap=new HashMap<String,Object>();
		querymap.put("entId",  ""); 
		querymap.put("procId", "");
		querymap.put("unitId", "");
		String dTypeId="01";
		if(dId>0){
			data = collectConfigManageService.getDataConfigById(dId);
			querymap.put("procId", data.get("PROCESS_ID"));
			querymap.put("unitId", data.get("UNIT_ID"));
			dTypeId=data.get("DTYPE_ID").toString();
		}else{
			data.put("DATA_ID", "0");
			data.put("DATA_CODE", "");
			data.put("DATA_NAME", "");
			data.put("DEVICE_ID", "0");
			data.put("DEVICE_TYPE", "");
			data.put("PROCESS_ID", "");
			data.put("UNIT_ID", "");
			data.put("COLLPLAT_ID", "");
			data.put("DATAUSAGE_ID", "");
			data.put("DTYPE_ID", dTypeId);
			data.put("EN_TYPE_ID", "");
			data.put("INPUT_TYPE", "");
			data.put("DATA_MAX", "");
			data.put("DATA_MIN", "");
			data.put("FREQUCE", "");
			data.put("SCOPE", "");
			data.put("EMO_ID", "");
			data.put("EMO_TYPE", "1");
			data.put("ENT_ID", request.getSession().getAttribute("SELECTED_ENT_ID")==null?"":request.getSession().getAttribute("SELECTED_ENT_ID")); //获取选择企业信息
			data.put("ENT_NAME", request.getSession().getAttribute("SELECTED_ENT_NAME")==null?"":request.getSession().getAttribute("SELECTED_ENT_NAME")); 
		}
		map.put("proc",data);
		//范围
		map.put("scopes",CollConfigStaticData.getStaticScopes()); 
		//频率
		map.put("frequces",CollConfigStaticData.getStaticFrequce());
		//数据来源
		map.put("inputtypes",collectConfigManageService.getDataSrc());
		//重点耗能设备类型
		map.put("devicetyps",collectConfigManageService.getEqpType());//所有测量点列表
		
		//采集数据类型energyclass
		map.put("energyclass",collectConfigManageService.getDataType());
		//能源分类分项energytypes
		map.put("energytypes",collectConfigManageService.getEnTypeList(dTypeId));
		//用途datausages
		map.put("datausages",collectConfigManageService.getDataUsage());
		//采集系统名称collplats 
		map.put("collplats",CollConfigStaticData.getStaticCollplateSystems());
		
		return new ModelAndView("energy/yunNan/CollectConfigManage/data_config_add_update",map);
	}
	
	
	
	/**
	 * 合并分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mergeCollDataConfigInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> mergeCollDataConfigInfo(HttpServletRequest request){
		Map<String, String> resultmap = new HashMap<String, String>();
		int flag=0;
		Map<String, Object> procMap=null;
		try {
			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(procMap!=null) {
			if(procMap.get("dataId").toString().equals("0"))
			{
				procMap.put("dataId", SequenceUtils.getDBSequence()+"");
				flag=collectConfigManageService.addDataConfig(procMap);
			}
			else {
				flag=collectConfigManageService.updateDataConfig(procMap);
			}
			request.getSession().setAttribute("SELECTED_ENT_ID", procMap.get("entId"));
			request.getSession().setAttribute("SELECTED_ENT_NAME", procMap.get("entName"));
		}
		 
		
		resultmap.put("flag", flag+"");
		return resultmap;
	}
	
	/**
	 * 获取耗能设备
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getDeviceList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getDeviceList(HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<String, Object>();
		Map<String, Object> procMap=null;
		List<Map<String,Object>> devices=new ArrayList<Map<String,Object>>();
		try {
			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(procMap!=null) {
			devices=collectConfigManageService.getAllDeviceList(procMap);
		}
		resultmap.put("devices", devices);
		return resultmap;
	}
	
	/**
	 * 获取耗能设备
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getEnTypeList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEnTypeList(HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<String, Object>();
		String dTypeId = request.getParameter("dTypeId");
		List<Map<String,Object>> devices=collectConfigManageService.getEnTypeList(dTypeId);
		resultmap.put("devices", devices);
		return resultmap;
	}
	
	/**
	 * 获取耗能设备
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getProcAndLedgerList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getProcAndLedgerList(HttpServletRequest request){
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
			resultmap.put("mpeds", collectConfigManageService.getMpedListByLedgerID(Long.valueOf(procMap.get("entId").toString())));
			//所有能管对象列表
			resultmap.put("ledgers", ledgerManagerMapper.getAllSubLedgersByLedgerId(Long.valueOf(procMap.get("entId").toString())));
		}
		resultmap.put("procs", devices);
		return resultmap;
	}
	
	/**
	 * 修改分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteCollDataConfig", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> deleteCollDataConfig(HttpServletRequest request,String dataId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		int flag = 0;
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		flag=collectConfigManageService.deleteDataConfig(Long.valueOf(dataId));
		if(flag>0){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("delete a procId:").append(dataId).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		result.put("flag", flag+"");
		return result;
	}
	
	/**
	 * 上传采集数据项配置信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadDataConfig", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> uploadDataConfig(HttpServletRequest request,String procId,String typeId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		result=collectConfigManageService.uploadDataConfig(procId, typeId);
		if(result.get("flag").toString().equals("true")){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("delete a data config:").append(procId).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		return result;
	}
}
