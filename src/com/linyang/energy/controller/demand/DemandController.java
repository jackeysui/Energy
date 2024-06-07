package com.linyang.energy.controller.demand;

import com.leegern.util.StringUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.ClassService;
import com.linyang.energy.service.DemandService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.linyang.energy.utils.DateUtil;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;

/**
 * 需求响应控制器
 * Created by Administrator on 16-9-7.
 */
@Controller
@RequestMapping("/demand")
public class DemandController extends BaseController {

    @Autowired
    private DemandService demandService;

    @Autowired
    private ClassService classService;

    @Autowired
    private LedgerManagerService ledgerManagerService;

    @Autowired
    private UserAnalysisService userAnalysisService;

    /**
     * 进入“模拟响应预览”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/gotoDemandSimulate")
    public ModelAndView gotoDemandSimulate(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
        params.put("loginLedger", bean);
        return new ModelAndView(URLConstant.URL_DEMAND_SIMULATE, params);
    }

    /**
     * 得到下拉de企业列表
     * @return
     */
    @RequestMapping("/getCompLedgerList")
    public @ResponseBody String getCompLedgerList(HttpServletRequest request){
        Long accountId = super.getSessionUserInfo(request).getAccountId();
        Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if(ledgerId == 0){  //群组
            dataList = ledgerManagerService.getLedgersForGroup2(accountId);
        }
        else {  //非群组
            dataList = this.demandService.getCompLedgerList(ledgerId);
        }

        //按照插件所需要的格式拼接字符串
        String str = "";
        if(dataList != null && dataList.size()>0) {
            StringBuffer sb = new StringBuffer("[");
            for(Map<String,Object> data : dataList) {
                sb.append("{id:'").append(data.get("LEDGER_ID"))
                        .append("',label:'").append(data.get("LEDGER_NAME"))
                        .append("',value:'").append(data.get("LEDGER_NAME"))
                        .append("',num:'").append(data.get("LEDGER_ID"))
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
     * 根据ledgerId查询响应方案列表
     * @return
     */
    @RequestMapping(value = "/getLedgerDemandMethod")
    public @ResponseBody Map<String,Object> getLedgerDemandMethod(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        if(ledgerId > 0){
            result = this.demandService.getLedgerDemandMethod(ledgerId);
        }
        result.putAll(this.classService.getLedgerCanChoose(ledgerId));
        result.put("time", this.demandService.getLastWorkDayDefaultTime(ledgerId));
        return result;
    }

    /**
     * 根据响应方案ID查询详细信息
     * @return
     */
    @RequestMapping("/getDetailsByDemand")
    public @ResponseBody Map<String,Object> getDetailsByDemand(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long planId = super.getLongParam(request, "planId", -1);
        if(planId > 0){
            result = this.demandService.getDetailsByDemand(planId);
        }
        return result;
    }

    /**
     * 新增、修改 响应方案
     * @return
     */
    @RequestMapping(value = "/insertUpdateDemand")
    public @ResponseBody Map<String,Object> insertUpdateDemand(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> demandInfo = jacksonUtils.readJSON2Map(request.getParameter("demandInfo"));
            Map<String, Object> map = this.demandService.insertUpdateDemand(demandInfo);
            resultMap.put("planId", map.get("planId"));

            String resultMess = map.get("message").toString();
            if(resultMess.length()>0){  //保存失败
                isSuccess = false;
                message = resultMess;
            }
        } catch (IOException e) {
            //保存失败
        	Log.info("insertUpdateDemand error IOException");
            isSuccess = false;
            message = "保存错误";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 删除 响应方案
     * @return
     */
    @RequestMapping(value = "/deleteDemand")
    public @ResponseBody Map<String,Object> deleteDemand(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = false;
        
            Long planId = super.getLongParam(request, "planId", -1);
            this.demandService.deleteDemand(planId);

            isSuccess = true;
        

        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }

    /**
     * 点击响应效果预览
     * @return
     */
    @RequestMapping("/getDemandSimulate")
    public @ResponseBody Map<String,Object> getDemandSimulate(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        Long planId = super.getLongParam(request, "planId", -1);
        String dayStr = super.getStrParam(request, "dayStr", "");
        String beginTime = super.getStrParam(request, "beginTime", "");
        String endTime = super.getStrParam(request, "endTime", "");

        if(ledgerId > 0 && planId > 0 &&beginTime!=null&&endTime!=null&&dayStr!=null&& dayStr.length()>0 && beginTime.length()>0 && endTime.length()>0){
            result = this.demandService.getDemandResponse(ledgerId, planId, dayStr, beginTime, endTime);
        }

        //记录用户足迹
        this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_57, 131L, 1);
        return result;
    }


    /**
     * 进入“实际响应生成”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/gotoDemandActual")
    public ModelAndView gotoDemandActual(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
        params.put("loginLedger", bean);
        return new ModelAndView(URLConstant.URL_DEMAND_ACTUAL, params);
    }

    /**
     * 获取默认时间段
     * @return
     */
    @RequestMapping(value = "/getDefaultTime")
    public @ResponseBody Map<String,Object> getDefaultTime(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        if(ledgerId > 0){
            result.putAll(this.demandService.getLastWorkDayDefaultTime(ledgerId));
        }
        return result;
    }

    /**
     * 点击响应效果评价
     * @return
     */
    @RequestMapping("/getDemandActual")
    public @ResponseBody Map<String,Object> getDemandActual(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        String dayStr = super.getStrParam(request, "dayStr", "");
        String beginTime = super.getStrParam(request, "beginTime", "");
        String endTime = super.getStrParam(request, "endTime", "");

        if(ledgerId > 0 &&dayStr!=null&& dayStr.length()>0 &&beginTime!=null&&endTime!=null&& beginTime.length()>0 && endTime.length()>0){
            result = this.demandService.getDemandResponse(ledgerId, null, dayStr, beginTime, endTime);
        }
        //记录用户足迹
        this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_58, 132L, 1);
        return result;
    }

    /**
     * 保存响应事件
     * @return
     */
    @RequestMapping(value = "/insertResponse")
    public @ResponseBody Map<String,Object> insertResponse(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Long ledgerId = super.getLongParam(request, "ledgerId", -1);
            String beginStr = super.getStrParam(request, "beginStr", "");
            String endStr = super.getStrParam(request, "endStr", "");
            Date beginDate = DateUtil.convertStrToDate(beginStr, DateUtil.MOUDLE_PATTERN);
            Date endDate = DateUtil.convertStrToDate(endStr, DateUtil.MOUDLE_PATTERN);
            String t1=super.getStrParam(request, "baseValBefore", "");
            String t2=super.getStrParam(request, "baseValAfter", "");
            String t3=super.getStrParam(request, "adjust", "");
            String t4=super.getStrParam(request, "average", "");
            String t5=super.getStrParam(request, "cutDownVal", "");
            Double baseValBefore = 0d,baseValAfter =0d,adjust =0d,average =0d,cutDownVal =0d;
            if(null!=t1&&t1.length()<=20&&t1.length()>0){baseValBefore=Double.valueOf(t1);}
            if(null!=t2&&t2.length()<=20&&t2.length()>0){baseValAfter = Double.valueOf(t2);}
            if(null!=t3&&t3.length()<=20&&t3.length()>0){adjust = Double.valueOf(t3);}
            if(null!=t4&&t4.length()<=20&&t4.length()>0){average = Double.valueOf(t4);}
            if(null!=t5&&t5.length()<=20&&t5.length()>0){cutDownVal = Double.valueOf(t5);}          

            this.demandService.insertResponse(ledgerId,beginDate,endDate,baseValBefore,baseValAfter,adjust,average,cutDownVal);
        }
        catch (NumberFormatException e) {
            //保存失败
        	Log.info("saveSubscriptionInfo error NumberFormatException");
            isSuccess = false;
            message = "保存错误";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }


    /**
     * 进入“历史响应查询”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/gotoDemandHistory")
    public ModelAndView gotoDemandHistory(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
        params.put("loginLedger", bean);
        return new ModelAndView(URLConstant.URL_DEMAND_HISTORY, params);
    }

    /**
     * 根据ledgerId查询响应历史列表
     * @return
     */
    @RequestMapping(value = "/getLedgerDemandHistory")
    public @ResponseBody Map<String,Object> getLedgerDemandHistory(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        if(ledgerId > 0){
            result = this.demandService.getLedgerDemandHistory(ledgerId);
        }
        return result;
    }

    /**
     * 根据响应历史ID查询信息
     * @return
     */
    @RequestMapping("/getHistoryDetailById")
    public @ResponseBody Map<String,Object> getHistoryDetailById(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long incidentId  = super.getLongParam(request, "incidentId", -1);
        if(incidentId > 0){
            result = this.demandService.getHistoryDetailById(incidentId);

            //记录用户足迹
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_59, 133L, 1);
        }
        return result;
    }

    /**
     * 删除历史响应
     * @return
     */
    @RequestMapping(value = "/deleteHistoryDetail")
    public @ResponseBody Map<String,Object> deleteHistoryDetail(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        Long incidentId = super.getLongParam(request, "incidentId", -1);
        this.demandService.deleteHistoryDetail(incidentId);
        boolean isSuccess = true;

        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }

    /**
     * 电网需求响应： “企业负荷数据”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/gotoEleNetDemand")
    public ModelAndView gotoEleNetDemand(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
        params.put("loginLedger", bean);
        return new ModelAndView(URLConstant.URL_ELE_DEMAND_DATA, params);
    }

    @RequestMapping("/getEleNetDemand")
    public @ResponseBody Map<String,Object> getEleNetDemand(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        Long planId = super.getLongParam(request, "planId", -1);
        String planBegin = super.getStrParam(request, "planBegin", "");
        String planEnd = super.getStrParam(request, "planEnd", "");
        String beginTime = super.getStrParam(request, "beginTime", "");
        String endTime = super.getStrParam(request, "endTime", "");

        if( ledgerId > 0 && planId > 0){
            result = this.demandService.getEleDemandResult(ledgerId, planId, planBegin, planEnd, beginTime, endTime);
        }
        return result;
    }

    @RequestMapping(value = "/getEleDemandPlan")
    public @ResponseBody Map<String,Object> getEleDemandPlan(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        if(ledgerId > 0){
            result = this.demandService.getEleDemandPlan(ledgerId);
        }
        return result;
    }

    @RequestMapping(value = "/getElePlanDetail")
    public @ResponseBody Map<String,Object> getElePlanDetail(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long planId = super.getLongParam(request, "planId", -1);
        if(planId > 0){
            result = this.demandService.getElePlanDetail(planId);
        }
        //记录用户足迹
        this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_116, 138l, 1);
        return result;
    }

}
