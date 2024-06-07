/**
 * 
 */
package com.linyang.energy.controller.demand;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.model.*;
import com.linyang.energy.service.*;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;

/** 
 * @公司名称：江苏林洋电子有限公司
 * @作者：bowen  
 * @创建时间：2019-09-07 下午02:58:50  
 * @版本：V1.0  */
@Controller
@RequestMapping("/gridDemand")
public class GridDemandController extends BaseController{
	@Autowired
	private GridDemandPlanService gridDemandPlanService;

	 @Autowired
	 private ClassService classService;
	 
	 @Autowired
	 private LedgerManagerService ledgerManagerService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 
	 * 函数功能说明 :跳转到电网需求响应方案管理页面
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoGridDemandPlan")
	public ModelAndView gotoGridDemandPlan() {
		Map<String, Object> map = new HashMap<String, Object>();
		String endDate = DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
		map.put("endDate", endDate);
		return new ModelAndView("energy/demand/grid_demand_plan",map);
	}
	
	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param roleId 角色ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoGridDemandPlanAddUpdate")
	public ModelAndView gotoGridDemandPlanAddUpdate(HttpServletRequest request,String planId) {
		Map<String, Object> map = new HashMap<String, Object>();
		int canedit=1;//1能编辑，0不能编辑
		if(CommonMethod.isNotEmpty(planId)){
			map.put("type",1);//修改
			GridDemandPlanBean gridDemandPlanBean = gridDemandPlanService.getGridDemandPlan(Long.valueOf(planId));
			map.put("plan",gridDemandPlanBean);
			
			if(gridDemandPlanBean.getPlanStatus()==2) {
				canedit=0; //已生效
			}
			
			Date current=com.leegern.util.DateUtil.getCurrentDate(com.leegern.util.DateUtil.DEFAULT_SHORT_PATTERN);
			try {
				Date start=DateUtil.convertStrToDate(gridDemandPlanBean.getStartDate(),com.leegern.util.DateUtil.DEFAULT_SHORT_PATTERN);
				if(current.getTime()>start.getTime()) {
					canedit=0; //当前日期大于开始日期不能删除
				}
			}
			catch(Exception ex) {
				System.out.println(ex.getMessage());
			}
			
		}else{
			GridDemandPlanBean temp=new GridDemandPlanBean();
			temp.setStartDate(DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
			temp.setEndDate(DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
			temp.setStartTime("01:00:00");
			temp.setEndTime("10:00:00");
			map.put("plan",temp);
			map.put("type",0);//添加
		}
		
		map.put("canEdit",canedit);
		return new ModelAndView("energy/demand/grid_demand_add_update",map);
	}
	
	/**
	 * 
	 * 函数功能说明 :跳转到企业需求响应方案管理页面
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoEntDemandPlan")
	public ModelAndView gotoEntDemandPlan(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String endDate = DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);	
		map.put("endDate", endDate);
		UserBean user=super.getSessionUserInfo(request);
		LedgerBean ledgerBean=this.ledgerManagerService.getLedgerDataById(user.getLedgerId());
		if(ledgerBean!=null) {
			map.put("ledgerName", ledgerBean.getLedgerName());
			map.put("analyType", ledgerBean.getAnalyType());
			map.put("ledgerId", ledgerBean.getLedgerId());
		}
		
		return new ModelAndView("energy/demand/ent_demand_plan",map);
	}
	
	/**
	 * 
	 * 企业调峰能力
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoEntPeakAbility")
	public ModelAndView gotoEntPeakAbility(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentTime = DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
		map.put("defaultDate", currentTime);
		UserBean user=super.getSessionUserInfo(request);
		LedgerBean ledgerBean=this.ledgerManagerService.getLedgerDataById(user.getLedgerId());
		if(ledgerBean!=null) {
			map.put("ledgerName", ledgerBean.getLedgerName());
			map.put("analyType", ledgerBean.getAnalyType());
			map.put("ledgerId", ledgerBean.getLedgerId());
		}
		
		return new ModelAndView("energy/demand/ledger_ability",map);
	}
	
	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param roleId 角色ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoEntDemandPlanAddUpdate")
	public ModelAndView gotoEntDemandPlanAddUpdate(HttpServletRequest request,String planId,String ledgerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		long ledgerIdl=Long.valueOf(ledgerId);
		GridDemandPlanBean gridDemandPlanBean = gridDemandPlanService.getPlanLedgerConfig(Long.valueOf(planId),ledgerIdl);
		int canedit=1;//1能编辑，0不能编辑
		if(gridDemandPlanBean.getPlanStatus()==2) {
			canedit=0; //已生效
		}
		
		Date current=com.leegern.util.DateUtil.getCurrentDate(com.leegern.util.DateUtil.DEFAULT_SHORT_PATTERN);
		try {
			Date start=DateUtil.convertStrToDate(gridDemandPlanBean.getStartDate(),com.leegern.util.DateUtil.DEFAULT_SHORT_PATTERN);
			if(current.getTime()>start.getTime()) {
				canedit=0; //当前日期大于开始日期不能
			}
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		if(CommonMethod.isNotEmpty(gridDemandPlanBean.getLedgerId())){
			map.put("type",1);//修改			
		}else{
			gridDemandPlanBean.setLedgerId(ledgerIdl);
			map.put("type",0);//添加
		}
		
		//System.out.println("F:"+gridDemandPlanBean.getPlanLoads().size());
		map.putAll(this.classService.getLedgerCanChoose(ledgerIdl));
		map.put("plan",gridDemandPlanBean);
		map.put("canEdit",canedit);
		return new ModelAndView("energy/demand/ent_demand_add_update",map);
	}
	
	/**
	 * 
	 * 函数功能说明  :查询角色的列表
	 * @param pageInfo 前台参数集合
	 * @return  Map<String,Object>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getGridPlanPageList")
	public @ResponseBody Map<String, Object> getGridPlanPageList(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		Page page = null;
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
		mapQuery.put("page", page);
		if(queryMap.containsKey("queryMap")) {
			Map<String,Object> temp=(Map<String,Object>)queryMap.get("queryMap");
			temp.remove("page");
			mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
		}
		
		List<GridDemandPlanBean> list = gridDemandPlanService.getGridDemandPlanListPageData(mapQuery);
		
		long operItemId = OperItemConstant.OPER_ITEM_113;
		long moduleId = 136l;
		if( queryMap.get( "pageType" ) != null && queryMap.get( "pageType" ).toString().equals( "2" ) ){
			operItemId = OperItemConstant.OPER_ITEM_114;
			moduleId = 135l;
		}
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), operItemId, moduleId, 1);
		
		map.put("queryMap",mapQuery);
		map.put("page", page);
		map.put("list", list);
		return map;
	}
	/**
     * 
             * 函数功能说明  :新增方案
     * @return  boolean     
     */
	@RequestMapping("/addUpdateGridPlanInfo")
	public @ResponseBody boolean addUpdateGridPlanInfo (HttpServletRequest request,Long[] moduleIds){
		GridDemandPlanBean plan = new GridDemandPlanBean();
		plan.setPlanName(request.getParameter("planName"));
		plan.setPlanStatus(1);
		plan.setStartDate(request.getParameter("startDate"));
		plan.setEndDate(request.getParameter("endDate"));
		plan.setStartTime(request.getParameter("startTime"));
		plan.setEndTime(request.getParameter("endTime"));
		String pitchs=request.getParameter("pitch");
		if(pitchs!=null&&pitchs.length()>0) {
			plan.setPitchPeak(Integer.parseInt(pitchs));
		}
		
		plan.setRemarks(request.getParameter("remarks"));
		String type=request.getParameter("type");
		int isSuccess = 0;
		if(type.equals("0")) {
			plan.setPlanId(SequenceUtils.getDBSequence());
			isSuccess = gridDemandPlanService.insertGridDemand(plan);
		}
		else {
			plan.setPlanId(Long.parseLong(request.getParameter("planId")));
			isSuccess = gridDemandPlanService.updateGridDemand(plan);
		}
		
		StringBuffer desc = new StringBuffer();
		desc.append("update a plan:" + plan.getPlanName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess>0){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_ROLE_ID,CommonOperaDefine.MODULE_NAME_ROLE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess>0;	
		
	}
	
	
	/**
     * 
             * 函数功能说明  :新增方案
     * @return  boolean     
     */
	@RequestMapping("/addUpdateGridPlanLedgerInfo")
	public @ResponseBody boolean addUpdateGridPlanLedgerInfo (HttpServletRequest request,Long[] moduleIds){
		int isSuccess = 0;
		try {
            Map<String, Object> demandInfo = jacksonUtils.readJSON2Map(request.getParameter("demandInfo"));
            GridDemandPlanBean plan = new GridDemandPlanBean();
            plan.setPlanId(Long.parseLong(demandInfo.get("planId").toString()));
    		plan.setLedgerId(Long.parseLong(demandInfo.get("ledgerId").toString()));
    		plan.setLedgerPitchPeak(Integer.parseInt(demandInfo.get("ledgerPitchPeak").toString()));
    		plan.setSubRemarks(demandInfo.get("subRemarks").toString());
    		List<Map<String, Object>> relationList = (List<Map<String, Object>>) demandInfo.get("loadList");
    		plan.setPlanLoads(relationList);
    		int type=Integer.valueOf(demandInfo.get("type").toString());
    		isSuccess = gridDemandPlanService.mergePlanLedgerConfig(plan, type);
    		//日志
    		StringBuffer desc = new StringBuffer();
    		desc.append("update a plan ledger config:" + plan.getPlanName())
            .append(" by ").
            append(super.getSessionUserInfo(request).getLoginName()).
            append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
    		int result = CommonOperaDefine.OPRATOR_FAIL;
    		if(isSuccess>0){
    			result =  CommonOperaDefine.OPRATOR_SUCCESS;
    		}
    		
    		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_ROLE_ID,CommonOperaDefine.MODULE_NAME_ROLE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
        } catch (IOException e) {
        	Log.info("insertUpdateDemand error IOException");
            isSuccess = 0;
        }
		
		return isSuccess>0;	
	}
	
	/**
     * 
             * 函数功能说明  :新增方案
     * @return  boolean     
     */
	@RequestMapping("/estimatePeakValue")
	public @ResponseBody double estimatePeakValue (HttpServletRequest request,Long[] moduleIds){
		double result = 0;
		try {
            Map<String, Object> demandInfo = jacksonUtils.readJSON2Map(request.getParameter("demandInfo"));
            GridDemandPlanBean plan = new GridDemandPlanBean();
            plan.setPlanId(Long.parseLong(demandInfo.get("planId").toString()));
            plan.setStartTime(demandInfo.get("startTime").toString());
            plan.setEndTime(demandInfo.get("endTime").toString());
    		plan.setLedgerId(Long.parseLong(demandInfo.get("ledgerId").toString()));
    		List<Map<String, Object>> relationList = (List<Map<String, Object>>) demandInfo.get("loadList");
    		plan.setPlanLoads(relationList);
    		result=gridDemandPlanService.estimagePitchPeak(plan);
        } catch (IOException e) {
        	Log.info("estimatePeakValue error IOException");
        	e.printStackTrace();
        	result = 0;
        }
		
		return result;	
	}
	
	/**
	 * 
	 * 函数功能说明  :删除角色
	 * @param role 角色参数信息
	 * @return  boolean     
	 */
	@RequestMapping("/deleteGridPlan")
	public @ResponseBody int deleteGridPlan (HttpServletRequest request){
		String str = request.getParameter("planId");
		if(str==null){return 0;}
		long planId = Long.valueOf(str);
		GridDemandPlanBean roleBean = this.gridDemandPlanService.getGridDemandPlan(planId);
		int isSuccess = 0;
		StringBuffer desc = new StringBuffer();
		desc.append("delete a gridplan:" + roleBean.getPlanName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		isSuccess = gridDemandPlanService.deleteGridDemand(planId);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess==1){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_ROLE_ID,CommonOperaDefine.MODULE_NAME_ROLE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	/**
	 * 
	 * @return  boolean     
	 */
	@RequestMapping("/verrifyGridPlan")
	public @ResponseBody int verrifyGridPlan (HttpServletRequest request){
		String str = request.getParameter("planId");
		String type = request.getParameter("planStatus");
		if(str==null||type==null){return 0;}
		long planId = Long.valueOf(str);
		int planStatus = Integer.valueOf(type);
		GridDemandPlanBean roleBean = this.gridDemandPlanService.getGridDemandPlan(planId);
		int isSuccess = 0;
		StringBuffer desc = new StringBuffer();
		desc.append("update a gridplan status to "+planStatus+":" + roleBean.getPlanName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		isSuccess = gridDemandPlanService.updatePlanStatus(planId, planStatus);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess==1){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_ROLE_ID,CommonOperaDefine.MODULE_NAME_ROLE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	/**
	 * 
	 * @return  boolean     
	 */
	@RequestMapping("/canJump")
	public @ResponseBody boolean canJump (HttpServletRequest request){
		String str = request.getParameter("planId");
		if(str==null){return false;}
		long planId = Long.valueOf(str);
		int count= this.gridDemandPlanService.getPlanLedgerCount(planId);
		return count>0;
	}
	
	/**
	 * 函数功能说明  :检查方案名称是否重复
	 * @param role 角色参数信息
	 * @return  boolean
	 */
	@RequestMapping("/checkPlanName")
	public @ResponseBody int checkPlanName (HttpServletRequest request){
		 Map<String, Object> queryMap;
		 int nums=0;
		try {
			 queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
			 long planId=0;
			 String planidstr=queryMap.get("planId").toString();
			 if(planidstr!=null&&planidstr.length()>0) {
				 planId=Long.valueOf(planidstr);
			 }
			 
			 String planName=queryMap.get("planName").toString();
		     nums= gridDemandPlanService.getDemandNumByName(planId,planName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    return nums;		
	}
	
	/**
	 * 保存调峰能力方案
	 * @author catkins.
	 * @param request
	 * @return java.lang.Boolean
	 * @exception
	 * @date 2019/9/19 13:54
	 */
	@RequestMapping(value = "savePeak" , method = RequestMethod.POST)
	public @ResponseBody Boolean saveLedgerPeakloadConfig(HttpServletRequest request){
		boolean flag = false;
		try {
			EntPeakAbility entBean = jacksonUtils.readJSON2Bean( EntPeakAbility.class,request.getParameter( "pageInfo" ) );
			int i = gridDemandPlanService.saveLedgerPeakloadConfig( entBean );
			if(i>0)
				flag = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
	/**
	 * 获取方案下企业列表
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/8 14:37
	 */
	@RequestMapping(value = "/ledgerGridList",method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryGridListPages(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>( 0 );
		List<EntPeakAbility> entDatas = null;
		Page page = null;
		// 得到当前分页
		try {
			Long ledgerId = this.getSessionUserInfo(request).getLedgerId();
			Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
			if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
				page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
			else
				page = new Page();
			if ( !queryMap.containsKey( "ledgerId" ) ) {
				queryMap.put("ledgerId", ledgerId  );
			}
			if(queryMap.containsKey( "ledgerId" ) && queryMap.get( "ledgerId" ).toString().equals( "-1" )){
				queryMap.put("ledgerId", ledgerId  );
			}
			
			entDatas = gridDemandPlanService.getLedgerPeakConfigPageData( page, queryMap );
			
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_117, 139l, 1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put( "page", page);
		result.put( "list",entDatas );
		return result;
	}
	
	/**
	 * 新增或者修改的时候查询用
	 * @author catkins.
	 * @param request
	 * @return com.linyang.energy.model.EntPeakAbility
	 * @exception
	 * @date 2019/9/19 15:25
	 */
	@RequestMapping(value = "/ledgerPeak" , method = RequestMethod.POST)
	public @ResponseBody EntPeakAbility queryLedgerPeakConfig(HttpServletRequest request){
		long ledgerId = Long.parseLong( request.getParameter( "ledgerId" ) );
		EntPeakAbility ledgerPeakConfig = null;
		if (ledgerId != -1) {
			ledgerPeakConfig = gridDemandPlanService.getLedgerPeakConfig( ledgerId );
		}
		return ledgerPeakConfig;
	}
	
	@RequestMapping(value = "/deleteLedgerPeak" , method = RequestMethod.POST)
	public @ResponseBody Boolean deleteLedgerPeak(HttpServletRequest request){
		boolean flag = false;
		
		long ledgerId = Long.parseLong( request.getParameter( "ledgerId" ) );
		
		int i = gridDemandPlanService.deleteLedgerPeakConfig( ledgerId );
		
		if(i > 0)
			flag = true;
		
		return flag;
	}
	
	@RequestMapping(value = "/ledgerList" , method = RequestMethod.POST)
	public @ResponseBody String getLedgerBeanList(HttpServletRequest request){
		Long ledgerId = this.getSessionUserInfo(request).getLedgerId();
		List<LedgerBean> ledgerList = gridDemandPlanService.getLedgerList( ledgerId );
		String str = "";
		StringBuilder sb = new StringBuilder( "[" );
		for(LedgerBean user : ledgerList) {
			sb.append("{id:'").append(user.getLedgerId())
					.append("',label:'").append(user.getLedgerName())
					.append("',value:'").append(user.getLedgerId()).append("',num:'")
					.append(user.getLedgerId()).append("',count:'0'},");
		}
		str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		return str;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
