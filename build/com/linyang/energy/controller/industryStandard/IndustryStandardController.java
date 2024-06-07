package com.linyang.energy.controller.industryStandard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.DataQueryService;
import com.linyang.energy.service.IndustryStandardService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.util.DoubleUtils;

/** 
* @Description
* @author Jijialu
* @date 2017年4月19日 下午1:40:26 
*/
@Controller
@RequestMapping("industryStandard")
public class IndustryStandardController extends BaseController{
	
	@Autowired IndustryStandardService industryStandardService;
	
	@Autowired LedgerManagerService ledgerManagerService;

    @Autowired
    private UserAnalysisService userAnalysisService;
	
	/**
	 * 进入行业对标发布页面
	 * @return
	 */
	@RequestMapping("gotoIndustryStandard")
	public ModelAndView gotoIndustryStandard(){
		return new ModelAndView("energy/industryStandard/industry_standard");
	}
	
	/**
	 * 获得行业指标列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getIndustryStandardIdList")
	public @ResponseBody String getIndustryStandardIdList(HttpServletRequest request){
        List<Map<String, Object>> dataList = this.industryStandardService.getIndustryStandardIdList();
        StringBuffer sb = new StringBuffer("[");
        String str = "";
        //按照插件所需要的格式拼接字符串
        if(dataList != null && dataList.size()>0) {
            for(Map<String, Object> data : dataList) {
                sb.append("{id:'").append(data.get("STANDARD_ID"))
                        .append("',label:'").append(data.get("STANDARD_NAME"))
                        .append("',value:'").append(data.get("STANDARD_NAME"))
                        .append("',num:'").append(data.get("STANDARD_ID"))
                        .append("',count:'0'},");
            }
            str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
        }
        else {
            str = "[]";
        }
        return str;
	}
	
	/**
     * 根据id得到行业指标列表
     * @param request
     * @return
     */
	@RequestMapping(value = "/getIndustryStandardList")
	public @ResponseBody Map<String, Object> getIndustryStandardList(HttpServletRequest request){
		Map<String, Object>  paramInfo = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		//指标id
		Long standardId = super.getLongParam(request, "standardId", 0);
		Integer pageIndex = super.getIntParams(request, "pageIndex", 1);
        Integer pageSize = super.getIntParams(request, "pageSize", 10);
        Page page = new Page(pageIndex, pageSize);
		paramInfo.put("standardId", standardId);
		paramInfo.put("page", page);
		list = this.industryStandardService.getIndustryStandardPageList(paramInfo);
		
		resultMap.put("page", page);
		if (list != null && list.size() > 0) {
			resultMap.put("list", list);
		}
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_104,226L,1);
		return resultMap;
	}
	
	/**
	 * 根据standardId获得产品配置信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gotoStandardDetail")
	public ModelAndView gotoProductConfigDetail(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> unitList = new ArrayList<String>();
		Map<String, Object> standardInfo = null;
		Long standardId = super.getLongParam(request, "standardId", -1);
		if (standardId > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("standardId", standardId);
			standardInfo = this.industryStandardService.getIndustryStandardById(standardId);
		}
		if(standardInfo != null){
			resultMap.putAll(standardInfo);
		}
		
		//获取静态数据库中的单位
		Map<String, String> map = DictionaryDataFactory.getDictionaryData(request).get("unit_type");
		for (String value : map.values()) {  
			unitList.add(value);
		}
		
		resultMap.put("unitList", unitList);
		return new ModelAndView("energy/industryStandard/add_update_standard", resultMap);
	}
	
	 /**
     * 新增 产品配置
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertStandard")
    public @ResponseBody Map<String,Object> insertStandard(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> standardInfo = jacksonUtils.readJSON2Map(request.getParameter("standardInfo"));
            Map<String, Object> result = this.industryStandardService.insertStandard(standardInfo);
            String resultMessage = result.get("message").toString();
            if(resultMessage.length()>0){  //保存失败
                isSuccess = false;
                message = resultMessage;
            }
        } catch (IOException e) {
        	Log.info("insertStandard error IOException");
            isSuccess = false;
            message = "保存失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 修改 产品配置
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateStandard")
    public @ResponseBody Map<String,Object> updateStandard(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> standardInfo = jacksonUtils.readJSON2Map(request.getParameter("standardInfo"));
            this.industryStandardService.updateStandard(standardInfo);
        } catch (IOException e) {
        	Log.info("saveSubscriptionInfo error IOException");
            isSuccess = false;
            message = "保存失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }
    
    /**
     * 删除产品配置
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteStandard")
    public @ResponseBody Map<String,Object> deleteProductConfig(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String message = "删除成功";
        boolean isSuccess = true;
        
        try {
            String standardStr = super.getStrParam(request, "standardStr", "");
            if(standardStr!=null){
            String[] items = standardStr.split(",");
            for(int i = 0; i < items.length; i++){
                String item = items[i];
                Long standardId = Long.valueOf(item);
                this.industryStandardService.deleteStandard(standardId);;
            }}

        } catch (NumberFormatException e) {
        	Log.info("deleteProductConfig error NumberFormatException");
            isSuccess = false;
            message = "删除失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }
    
    /**
	 * 进入行业指标分析页面
	 * @param request
	 * @return
	 */
	@RequestMapping("gotoLedgerStandard")
	public ModelAndView gotoLedgerStandard(HttpServletRequest request){
		Map<String, Object> params = new HashMap<String, Object>();
		LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
		//获取能管单元的类型
		Integer analyType = bean.getAnalyType();
		params.put("analyType", analyType);
		return new ModelAndView("energy/industryStandard/ledger_standard", params);
	}
	
	/**
	 * 获得行业指标列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getLedgerStandardIdList")
	public @ResponseBody String getLedgerStandardIdList(HttpServletRequest request){
		Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        List<Map<String, Object>> dataList = this.industryStandardService.getLedgerStandardIdList(ledgerId);
        StringBuffer sb = new StringBuffer("[");
        String str = "";
        //按照插件所需要的格式拼接字符串
        if(dataList != null && dataList.size()>0) {
            for(Map<String, Object> data : dataList) {
                sb.append("{id:'").append(data.get("STANDARD_ID"))
                        .append("',label:'").append(data.get("STANDARD_NAME"))
                        .append("',value:'").append(data.get("STANDARD_NAME"))
                        .append("',num:'").append(data.get("STANDARD_ID"))
                        .append("',count:'0'},");
            }
            str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
        }
        else {
            str = "[]";
        }
        return str;
	}
	
	/**
	 * 进入企业指标录入页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gotoLedgerStandardAdd")
	public ModelAndView gotoLedgerStandardAdd(HttpServletRequest request){
		String currentTime = com.linyang.energy.utils.DateUtil
				.getCurrentDateStr(com.linyang.energy.utils.DateUtil.MONTH_PATTERN);
		String beforeAfterDate = com.linyang.energy.utils.DateUtil.convertDateToStr(
				com.linyang.energy.utils.DateUtil.getMonthDate(new Date(), -1),
				com.linyang.energy.utils.DateUtil.MONTH_PATTERN);
		//开始时间
		request.setAttribute("beginDate", beforeAfterDate);
		//结束时间
		request.setAttribute("endDate", currentTime);
		return new ModelAndView("energy/industryStandard/add_ledger_standard");
	}
	
	/**
	 * 根据id获得指标信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getStandardDetailById")
    public @ResponseBody Map<String,Object> getStandardDetailById(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Long standardId = super.getLongParam(request, "standardId", -1);
		resultMap = this.industryStandardService.getIndustryStandardById(standardId);
		return resultMap;
    }
	
	/**
	 * 获取企业单耗及国标差值
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/getLedgerDiff")
    public @ResponseBody Map<String,Object> getLedgerDiff(HttpServletRequest request) throws IOException{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
		boolean hasQ = false;
		Double ledgerData = Double.valueOf(queryMap.get("ledgerData").toString());
		Double standardData = Double.valueOf(queryMap.get("standardData").toString());
		Date beginDate = DateUtil.convertStrToDate(queryMap.get("beginDate").toString(), DateUtil.SHORT_PATTERN);
		Date endDate = DateUtil.convertStrToDate(queryMap.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
		Long ledgerId = bean.getLedgerId();
		
		//总能耗
		Double Q = this.industryStandardService.queryLedgerQSum(ledgerId, beginDate, endDate);
		if (Q != null) {
			hasQ = true;
			//单耗(用总能耗除以用户输入数值)
			Double ledgerUnitConsumption = DataUtil.doubleDivide(Q, ledgerData, 2);
			Double ledgerDiff = Math.abs(DoubleUtils.getDoubleValue(DataUtil.doubleSubtract(ledgerUnitConsumption, standardData), 2));
			
			resultMap.put("unitConsumption", String.format("%.2f", ledgerUnitConsumption));
			resultMap.put("ledgerDiff", ledgerDiff);
		}
		
		resultMap.put("hasQ", hasQ);
		
		return resultMap;
    }
	
	/**
	 * 根据id获得指标信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/insertOrUpdateLedgerStandard")
    public @ResponseBody Map<String,Object> insertOrUpdateLedgerStandard (HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
		Long ledgerId = bean.getLedgerId();
        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> standardInfo = jacksonUtils.readJSON2Map(request.getParameter("standardInfo"));
            standardInfo.put("ledgerId", ledgerId);
            this.industryStandardService.insertOrUpdateLedgerStandard(standardInfo);
        } catch (IOException e) {
        	Log.error("insertOrUpdateLedgerStandard error IOException"+e.getMessage());
            isSuccess = false;
            message = "保存失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }
	
	/**
	 * 获取企业指标信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLedgerStandardInfo")
    public @ResponseBody Map<String,Object> getLedgerStandardInfo(HttpServletRequest request){
		Map<String,Object> result = new HashMap<String, Object>();
		boolean hasData = false;
		Long standardId = super.getLongParam(request, "standardId", 0);
		Long ledgerId = super.getLongParam(request, "ledgerId", -1);
		Map<String,Object> standardInfo = this.industryStandardService.getLedgerStandardInfo(ledgerId, standardId);	
		if (standardInfo != null) {
			hasData = true;
			result.putAll(standardInfo);
		}
		result.put("hasData", hasData);

        //记录用户足迹
        this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_104,226L,1);
		return result;
    }
	
}
