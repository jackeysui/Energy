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
import org.springframework.util.CollectionUtils;
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
@RequestMapping("/consumeDeviceController")
public class ConsumeDeviceController extends BaseController {

    @Autowired
    private CollectConfigManageService collectConfigManageService;

    /**
             * 进入“生产工序管理页面”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/consumeDeviceManage")
    public ModelAndView productProcessManage(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        return new ModelAndView("energy/yunNan/CollectConfigManage/consumeDeviceManage", params);
    }
    
    /**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getConDeivcePageList")
	public @ResponseBody Map<String, Object> getConDeivcePageList(HttpServletRequest request) throws IOException {
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
		List<Map<String,Object>> list = collectConfigManageService.getConDevicePageList(page,mapQuery);
		
		    map.put("queryMap",mapQuery);
			map.put("page", page);
			map.put("list", list);
		return map;
	}
	
	/**
	 */
	@RequestMapping("/gotoConDeviceAddUpdate")
	public ModelAndView gotoConDeviceAddUpdate(HttpServletRequest request,String procId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> device=new HashMap<String,Object>();
		long dId=Long.valueOf(procId);
		Map<String,Object> querymap=new HashMap<String,Object>();
		if(dId>0){
			device = collectConfigManageService.getConDeviceById(dId);
			querymap.put("entId", device.get("ENT_ID")); //更新时获取本工序单元的企业id
		}else{
			device.put("DEVICE_ID", "0");
			device.put("DEVICE_NO", "");
			device.put("DEVICE_NAME", "");
			device.put("DEVICE_TYPE", "");
			device.put("MODEL", "");
			device.put("LOCATION", "");
			device.put("DEPT", "");
			device.put("CURRENT_STATUS", "");
			device.put("MANUFACTURER", "");
			device.put("USING_DATE", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
			device.put("ENERGY_CODE", "");
			device.put("ENERGY_VALUE", "");
			device.put("PROCESS_ID", "");
			device.put("UNIT_ID", "");
			device.put("ENT_ID", request.getSession().getAttribute("SELECTED_ENT_ID")==null?"":request.getSession().getAttribute("SELECTED_ENT_ID")); //获取选择企业信息
			device.put("ENT_NAME", request.getSession().getAttribute("SELECTED_ENT_NAME")==null?"":request.getSession().getAttribute("SELECTED_ENT_NAME"));
			querymap.put("entId", ""); //新建的时候获取列表页面传过来的企业id
		}
		map.put("proc",device);
		//静态数据
		//设备分类devicetypes
		//map.put("devicetypes",collectConfigManageService.getEqpType()); 
		//能源种类energycods
		map.put("energycods",collectConfigManageService.getEnType()); 
		
		return new ModelAndView("energy/yunNan/CollectConfigManage/consume_device_add_update",map);
	}
	
	/**
	 * 根据权限获得企业列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getDeviceTypes")
	public @ResponseBody String getDeviceTypes(HttpServletRequest request) {
		String str = "";
		List<Map<String, Object>> dataList =collectConfigManageService.getEqpType();
		//按照插件所需要的格式拼接字符串
		if(!CollectionUtils.isEmpty(dataList)) {
			StringBuffer sb = new StringBuffer("[");
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("EQP_TYPE_ID"))
						.append("',label:'").append(data.get("EQP_TYPE_NAME"))
						.append("',value:'").append(data.get("EQP_TYPE_ID"))
						.append("',num:'").append(data.get("EQP_TYPE_ID"))
						.append("',count:'0'},");
			}
			str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		}
		else{
			str = "[]";
		}
		return str;
	}
	
	
	/**
	 * 合并分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mergeConDeviceInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> mergeConDeviceInfo(HttpServletRequest request){
		Map<String, String> resultmap = new HashMap<String, String>();
		int flag=0;
		Map<String, Object> procMap=null;
		try {
			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(procMap!=null) {
			if(procMap.get("deviceId").toString().equals("0"))
			{
				procMap.put("deviceId", SequenceUtils.getDBSequence()+"");
				flag=collectConfigManageService.addConDevice(procMap);
			}
			else {
				flag=collectConfigManageService.updateConDevice(procMap);
			}
			request.getSession().setAttribute("SELECTED_ENT_ID", procMap.get("entId"));
			request.getSession().setAttribute("SELECTED_ENT_NAME", procMap.get("entName"));
		}
		
		resultmap.put("flag", flag+"");
		return resultmap;
	}
	
	/**
	 * 获取工序单元
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getProcUnitList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getProcUnitList(HttpServletRequest request){
		Map<String, Object> resultmap = new HashMap<String, Object>();
		Map<String, Object> procMap=null;
		List<Map<String,Object>> units=new ArrayList<Map<String,Object>>();
		try {
			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(procMap!=null) {
			units=collectConfigManageService.getAllProcUnitList(procMap);
		}
		resultmap.put("units", units);
		return resultmap;
	}
	
	/**
	 * 修改分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteConDevice", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> deleteConDevice(HttpServletRequest request,String procId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		int flag = 0;
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		flag=collectConfigManageService.deleteConDevice(Long.parseLong(procId));
		if(flag>0){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("delete a consume device:").append(procId).append(" by ").
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
	@RequestMapping(value = "/uploadConsumeDevice", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> uploadConsumeDevice(HttpServletRequest request,String procId,String typeId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		result=collectConfigManageService.uploadConsumeDevice(procId, typeId);
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
	 * 检查是否有工序单元依赖
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkDeviceHasDepends", method = RequestMethod.POST)
	public @ResponseBody boolean checkDeviceHasDepends(HttpServletRequest request,String deviceId){
		return collectConfigManageService.getDeviceSonCounts(Long.valueOf(deviceId))>0;
	}
}
