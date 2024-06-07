package com.linyang.energy.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.RateBean;
import com.linyang.energy.model.RateSectorBean;
import com.linyang.energy.model.RateSectorContentBean;
import com.linyang.energy.service.RateService;
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
@RequestMapping("/rateManager")
public class RateController extends BaseController{
	/**
	 * 注入费率service
	 */
	@Autowired
	private RateService rateService;
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 进入费率管理页面
	 * @return
	 */
	@RequestMapping(value = "/gotoRateList")
	public String gotoRateList(){	
		return URLConstant.URL_RATE_LIST;
	}
	
	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param rateId 费率ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoRateAddUpdate")
	public ModelAndView gotoRateAddUpdate(HttpServletRequest request,String rateId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(CommonMethod.isNotEmpty(rateId)){
			RateBean rate = rateService.getRateData(Long.valueOf(rateId));
			map.put("rate",rate);
			map.put("type",1);//修改
		}else{
			map.put("type",0);//添加
		}
		map.put("density", WebConstant.density);
		return new ModelAndView("energy/system/rate_add_update",map);
	}
	
	@RequestMapping(value = "/getSectorData", method = RequestMethod.POST)
	public @ResponseBody List<RateSectorBean> getRateData(HttpServletRequest request,String rateId) {
		List<RateSectorBean> sector = rateService.getSectorData(Long.valueOf(rateId));
		return sector;
	}
	
	/**
	 * 
	 * 函数功能说明  :得到费率列表
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryRateList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryRateList(HttpServletRequest request) throws IOException {
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
		// 分页查询费率信息
		List<Map<String,Object>> dataList = rateService.getRatePageData(page, mapQuery);
		result.put("page", page);
		result.put("list",dataList);
		result.put("queryMap",mapQuery);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_138, 37l, 1);
		return result;
	}
	
	/**
	 * 
	 * 函数功能说明  :根据费率Id删除费率
	 * @param scheduleId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/deleteRateData", method = RequestMethod.POST)
	public @ResponseBody boolean deleteScheduleData(HttpServletRequest request,Long rateId) {
		boolean isSuccess = false;
        RateBean rateBean = this.rateService.getRateData(rateId);
		StringBuffer desc = new StringBuffer();
		desc.append("delete a rate:" + rateBean.getRateName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = rateService.deleteRateData(rateId);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_RATE_ID,CommonOperaDefine.MODULE_NAME_RATE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
	}
	
	/**
	 * 
	 * 函数功能说明  :检查是否有分户和费率关联
	 * @param scheduleId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/getRateLedgerLink", method = RequestMethod.POST)
	public @ResponseBody boolean getRateLedgerLink(Long rateId) {
		return rateService.getRateLedgerLink(rateId);
	}
	
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的费率名称 
	 * @param scheduleName
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/checkRateName", method = RequestMethod.POST)
	public @ResponseBody boolean checkRateName(String rateName, Long rateId) {
		return rateService.checkRateName(rateName, rateId);
	}
	
	/**
	 * 
	 * 函数功能说明  :新增费率信息
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/addRateInfo")
	public @ResponseBody boolean addRateInfo (HttpServletRequest request) throws IOException{
		String s=request.getParameter("rateData");
		RateBean rate = new RateBean();
		rate.setRateRemark(request.getParameter("rateRemark"));
		rate.setRateName(request.getParameter("rateName"));
		rate.setBeginEffectDate(new Date());
		rate.setEndEffectDate(new Date());
		//默认的费率序列
		rate.setRateId(SequenceUtils.getDBSequence());
		List<Map<String, Object>> gradeList = jacksonUtils.readJSON2ListMap(s);
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("add a rate:" + rate.getRateName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = rateService.addRateData(rate,gradeList,null);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_RATE_ID,CommonOperaDefine.MODULE_NAME_RATE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;		
	}
	
	/**
	 * 
	 * 函数功能说明  :修改费率信息
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/updateRateInfo")
	public @ResponseBody boolean updateRateInfo (HttpServletRequest request) throws IOException{
		String s=request.getParameter("rateData");
		RateBean rate = new RateBean();
		rate.setRateRemark(request.getParameter("rateRemark"));
		rate.setRateName(request.getParameter("rateName"));
		rate.setBeginEffectDate(new Date());
		rate.setEndEffectDate(new Date());
		rate.setRateId(Long.valueOf(request.getParameter("rateId")));
		List<Map<String, Object>> sectorList = jacksonUtils.readJSON2ListMap(s);
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("update a rate:" + rate.getRateName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = rateService.updateRateData(rate,sectorList,null);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_RATE_ID,CommonOperaDefine.MODULE_NAME_RATE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
	}
	
	/**
	 * 进入电价费率时段管理页面
	 * @author guosen
	 * @date 2014-12-17
	 * @return
	 */
	@RequestMapping(value = "/gotoRateManagementPage")
	public String gotoRateManagementPage(){	
		return URLConstant.URL_RATE_MANAGEMENT;
	}
	
	/**
	 * 函数功能说明  :新增，修改 费率页面
	 * @author guosen
	 * @date 2014-12-17
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoRateAddUpdatePage")
	public ModelAndView gotoRateAddUpdatePage(HttpServletRequest request,String rateId) {
		Map<String, Object> map = new HashMap<String, Object>(); String str_rateId = rateId;
		if (null != str_rateId && str_rateId.trim().length()>0) map.put("rateId", str_rateId);
		map.put("density", WebConstant.density);
		return new ModelAndView(URLConstant.URL_PRICE_MANAGEMENT,map);
	}

    @RequestMapping("/gotoOtherRateAddUpdate")
    public ModelAndView gotoOtherRateAddUpdate(HttpServletRequest request, String rateType, Long rateId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String str_rateType = rateType; if (str_rateType != null && str_rateType.length()>0) map.put("rateType", str_rateType); else map.put("rateType", "0");
        long l_rateId = -1; if (rateId != null) l_rateId = rateId;
        if(l_rateId > 0){
        	map.put("rateId", l_rateId);
            List<Map<String,Object>> list = this.rateService.getWaterGasHotByRateId(l_rateId);
            if(list != null && list.size() > 0){
                map.putAll(list.get(0));
            }
        }
        return new ModelAndView(URLConstant.OTHER_PRICE_MANAGEMENT, map);
    }
	
	/**
	 * 
	 * 函数功能说明  :新增费率信息
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/insertRateInfo")
	public @ResponseBody boolean insertRateInfo (HttpServletRequest request) throws IOException{
		String ratestr = request.getParameter("rateId");
		boolean isUpdate =false;
		if(StringUtils.isNotEmpty(ratestr)){
			isUpdate = true;
		}
		RateBean rate = new RateBean();
		rate.setRateRemark(request.getParameter("rateRemark"));
		rate.setRateName(request.getParameter("rateName"));
		rate.setRateNumber(Integer.parseInt(request.getParameter("rateNumber")));
		String t1=request.getParameter("volumeRate");
		String t2=request.getParameter("demandRate");
		String t3=request.getParameter("demandThres");
		if(Boolean.parseBoolean(request.getParameter("hasBaseRate"))){//基本电价
            double d1=0d,d2=0d,d3=0d;
            if(null!=t1&&t1.length()>0&&t1.length()<=20){d1=Double.parseDouble(t1);}
            if(null!=t2&&t2.length()>0&&t2.length()<=20){d2=Double.parseDouble(t2);}
            if(null!=t3&&t3.length()>0&&t3.length()<=20){d3=Double.parseDouble(t3);}
			rate.setVolumeRate(d1);
			rate.setDemandRate(d2);
			rate.setDemandThres(d3);
		}else{
			rate.setVolumeRate(null);
			rate.setDemandRate(null);
			rate.setDemandThres(null);
		}
		List<Map<String, Object>> sectorList = null;//价格管理
		if(Boolean.parseBoolean(request.getParameter("priceManagement"))){
			String sectorAry=request.getParameter("sectorAry");
			sectorList = jacksonUtils.readJSON2ListMap(sectorAry);
		}
		List<Map<String, Object>> sectorContentList = null;//价格管理
		if(Boolean.parseBoolean(request.getParameter("sectorSet"))){
			String sectorContentAry=request.getParameter("sectorContentAry");
			sectorContentList = jacksonUtils.readJSON2ListMap(sectorContentAry);
		}
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		
		int result = CommonOperaDefine.OPRATOR_FAIL;
        int logType = CommonOperaDefine.LOG_TYPE_INSERT;
		if(isUpdate){
			rate.setRateId(Long.parseLong(ratestr));
			isSuccess = rateService.updateRateData(rate, sectorList,sectorContentList);
            logType = CommonOperaDefine.LOG_TYPE_UPDATE;
            desc.append("update a rate:" + rate.getRateName());
		}else {
			//默认的费率序列
			rate.setRateId(SequenceUtils.getDBSequence());
			isSuccess = rateService.addRateData(rate,sectorList,sectorContentList);
			desc.append("add a rate:" + rate.getRateName());
		}
		desc.append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(logType,CommonOperaDefine.MODULE_NAME_RATE_ID,CommonOperaDefine.MODULE_NAME_RATE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;		
	}

    @RequestMapping("/insertOtherRateInfo")
    public @ResponseBody boolean insertOtherRateInfo(HttpServletRequest request) throws IOException{
        int rateType = super.getIntParams(request, "rateType", 0);
        Long rateId = super.getLongParam(request, "rateId", 0);
        String rateName = super.getStrParam(request, "rateName", "");
        String rateRemark = super.getStrParam(request, "rateRemark", "");
        String rateValue_str = super.getStrParam(request, "rateValue", "");
        Double rateValue=0d;
        if(null!=rateValue_str&&rateValue_str.length()<=20&&rateValue_str.length()>0){
        	rateValue = Double.valueOf(rateValue_str);}

        StringBuffer desc = new StringBuffer();
        desc.append("add a rate:" + rateName)
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

        int result = CommonOperaDefine.OPRATOR_FAIL;
        int logType = CommonOperaDefine.LOG_TYPE_INSERT;
        if(rateId > 0){
            logType = CommonOperaDefine.LOG_TYPE_UPDATE;
        }
        rateService.updateOtherRateData(rateType, rateId,rateName,rateRemark,rateValue);

        result =  CommonOperaDefine.OPRATOR_SUCCESS;
        super.writeLog(new OptLogBean(logType,CommonOperaDefine.MODULE_NAME_RATE_ID,CommonOperaDefine.MODULE_NAME_RATE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
        return true;
    }
	
	
	/**
	 * 函数功能说明  :得到价格管理数据
	 * @author guosen
	 * @date 2014-12-17
	 * @return  ModelAndView
	 */
	@RequestMapping("/getPriceData")
	public @ResponseBody Map<String, Object> getPriceData(HttpServletRequest request,String rateId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long rateIdL = Long.parseLong(rateId);
		RateBean rate = this.rateService.getRateData(rateIdL);
		result.put("rate", rate);
		List<RateSectorBean> sectorList = this.rateService.getSectorData(rateIdL);
		result.put("sectors", sectorList);
		List<RateSectorContentBean> sectorContentList = this.rateService.getSectorContentData(rateIdL);
		result.put("sectorContents", sectorContentList);
		result.put("density", WebConstant.density);
		return result;
	}
}
