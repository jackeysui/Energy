package com.linyang.energy.controller.yunNanEnergy.EnterpriseBaseFile;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.controller.yunNanEnergy.CollectConfigManage.CollConfigStaticData;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.service.EntBaseFileService;
import com.linyang.energy.service.YunNanService;
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
@RequestMapping("/runYearInfoController")
public class RunYearInfoController extends BaseController {

	 @Autowired
	 private EntBaseFileService entBaseFileService;

    /**
             * 进入“年度经营信息”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/runYearInfo")
    public ModelAndView runYearInfo(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        return new ModelAndView("energy/yunNan/EntBaseFileManage/runYearInfo", params);
    }
    

    /**
	 * 
	 * 函数功能说明  :查询角色的列表
	 * @param pageInfo 前台参数集合
	 * @return  Map<String,Object>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getRunYearPageList")
	public @ResponseBody Map<String, Object> getRunYearPageList(HttpServletRequest request) throws IOException {
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
		List<Map<String,Object>> list = entBaseFileService.getRunYearPageList(page,mapQuery);
		
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
	@RequestMapping("/gotoRunYearAddUpdate")
	public ModelAndView gotoRunYearAddUpdate(HttpServletRequest request,String entId,String cntYear) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> proc=new HashMap<String,Object>();
		int cYear=Integer.parseInt(cntYear);
		if(cYear>0){
			Map<String,Object> query=new HashMap<String,Object>();
			query.put("entId", entId);
			query.put("cntYear", cntYear);
			proc = entBaseFileService.getRunYearInfo(query);
		}else{
			proc.put("CNT_YEAR", DateUtil.getCurrentDateStr("yyyy"));
			proc.put("OUTPUT_VALUE", "");
			proc.put("ADDED_VALUE", "");
			proc.put("INCOME", "");
			proc.put("ENT_ID", "");
			proc.put("ENT_NAME", "");
		}
		map.put("proc",proc);
		map.put("staticYears",CollConfigStaticData.getStaticYears()); 
		return new ModelAndView("energy/yunNan/EntBaseFileManage/run_year_add_update",map);
	}
	
	/**
	 * 合并分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mergeRunYearcInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> mergeRunYearcInfo(HttpServletRequest request){
		Map<String, String> resultmap = new HashMap<String, String>();
		int flag=0;
		Map<String, Object> procMap=null;
		try {
			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(procMap!=null) {
			flag=entBaseFileService.saveRunYearInfo(procMap);
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
	public @ResponseBody Map<String, String> deleteProdProc(HttpServletRequest request,String entId,String cntYear){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		int flag = 0;
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		Map<String,Object> queryCondition=new HashMap<String,Object>();
		queryCondition.put("entId", entId);
		queryCondition.put("cntYear", cntYear);
		flag=entBaseFileService.deleteRunYearInfo(queryCondition);
		if(flag>0){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("delete a run year info entId:").append(entId).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		result.put("flag", flag+"");
		return result;
	}
	
	/**
	 * 上传年度经营信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadRunYear", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> uploadRunYear(HttpServletRequest request,String entId,String cntYear,String typeId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		result=entBaseFileService.uploadRunYearInfo(entId, cntYear, typeId);
		if(result.get("flag").toString().equals("true")){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("upload a run year info entId:").append(entId).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		return result;
	}
	
	/**
	 * 删除工序信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getRunYearInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getRunYearInfo(HttpServletRequest request,String entId,String cntYear){
		Map<String,Object> queryCondition=new HashMap<String,Object>();
		queryCondition.put("entId", entId);
		queryCondition.put("cntYear", cntYear);
		Map<String, Object> result=entBaseFileService.getRunYearInfo(queryCondition);
		return result;
	}
}
