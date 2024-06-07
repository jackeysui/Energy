package com.linyang.energy.controller;

import com.leegern.util.CollectionUtil;
import com.leegern.util.StringUtil;
import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.model.EnviManageBean;
import com.linyang.energy.model.IndustryBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.EnviManageService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 18-12-10.
 */
@Controller
@RequestMapping("/enviManage")
public class EnviManageController extends BaseController{

    @Autowired
    private EnviManageService enviManageService;

    @Autowired
    private LedgerManagerService ledgerManagerService;

    /**
     * 产污/治污设施关联配置  列表页   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoEnviManageList")
    public ModelAndView gotoProductOutput(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
        String ledgerName = "";
        Integer analyType = 0;
        if(null != ledgerId && ledgerId > 0){  //非群组
            LedgerBean bean = this.ledgerManagerService.getLedgerDataById(ledgerId);
            ledgerName = bean.getLedgerName();
            analyType = bean.getAnalyType();
        }
        params.put("userLedgerId", ledgerId);
        params.put("userLedgerName", ledgerName);
        params.put("userLedgerType", analyType);

        return new ModelAndView("energy/enviManage/enviManage_list", params);
    }


    /**
     * 查询产污/治污树
     */
    @RequestMapping(value = "/queryPolluteControlTree")
    public @ResponseBody List<IndustryBean> queryPolluteControlTree(HttpServletRequest request){
        Integer type = super.getIntParams(request, "type", -1);
        return enviManageService.getPolluteControlTree(type, null);
    }


    /**
     * 产污/治污设施关联配置 ajax分页搜索
     */
    @RequestMapping(value = "/ajaxEnviManageList")
    public @ResponseBody Map<String, Object> ajaxEnviManageList(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object>  paramInfo = new HashMap<String, Object>();
        Integer pageIndex = super.getIntParams(request, "pageIndex", 1);
        Integer pageSize = super.getIntParams(request, "pageSize", 10);
        Page page = new Page(pageIndex, pageSize);

        //条件查询
        Long accountId = super.getSessionUserInfo(request).getAccountId();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        String deviceName = super.getStrParam(request, "deviceName", null);
        String polluteId = super.getStrParam(request, "polluteId", null);
        String controlId = super.getStrParam(request, "controlId", null);
        paramInfo.put("accountId", accountId);
        paramInfo.put("ledgerId", ledgerId);
        paramInfo.put("deviceName", deviceName);
        paramInfo.put("polluteId", polluteId);
        paramInfo.put("controlId", controlId);
        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
            userLedgerId = 0L;
        }
        paramInfo.put("isGroup", isGroup);
        paramInfo.put("userLedgerId", userLedgerId);
        paramInfo.put("page", page);
        List<EnviManageBean> list = this.enviManageService.ajaxEnviManagePageList(paramInfo);
        if (list != null && list.size() > 0) {
            resultMap.put("list", list);
        }
        resultMap.put("page", page);
        return resultMap;
    }


    /**
     * 产污/治污设施关联配置  删除
     */
    @RequestMapping(value = "/deletePolluteControl")
    public @ResponseBody Map<String,Object> deletePolluteControl(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = false;
        String message = "";
        try {
            String outputStr = super.getStrParam(request, "outputStr", "");
            if(outputStr!=null){
                String[] items = outputStr.split(",");
                for (int i = 0; i < items.length; i++) {
                    String item = items[i];
                    Long relateId = Long.valueOf(item);
                    //查询是否配置了"产污"事件
                    int count = this.enviManageService.getEventSetNumBy(relateId);
                    if(count > 0){
                        message = "已配置产污事件的关联关系无法删除!";
                    }
                    else {
                        //删除操作
                        this.enviManageService.deletePolluteControlRelation(relateId);
                    }
                }
                isSuccess = true;
            }
        }
        catch (RuntimeException e) {
            Log.info(this.getClass() + ".deletePolluteControl()", e);
            isSuccess = false;
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }


    /**
     * 产污/治污设施关联配置  添加、修改页
     */
    @RequestMapping(value = "/gotoEditPolluteControl")
    public ModelAndView gotoEditPolluteControl(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        long relateId = getLongParam(request, "relateId", -1);
        resultMap.put("relateId", relateId);
        long ledgerId = -1;
        String ledgerName = "";
        EnviManageBean enviManageBean = this.enviManageService.getEnviManageByRelateId(relateId);
        if(null != enviManageBean){
            ledgerId = enviManageBean.getLedgerId();
            ledgerName = enviManageBean.getLedgerName();
        }
        resultMap.put("ledgerId", ledgerId);
        resultMap.put("ledgerName", ledgerName);

        return new ModelAndView("energy/enviManage/add_update_envi", resultMap);
    }


    /**
     * 根据 ledgerId、产污/治污ID、type获取产污/治污信息
     */
    @RequestMapping(value = "/getPolluteControlMessage")
    public @ResponseBody Map<String, Object> getPolluteControlMessage(HttpServletRequest request){
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        Long id = super.getLongParam(request, "id", -1);
        Integer type = super.getIntParams(request, "type", -1);     //1表示产污、2表示治污
        Map<String, Object> resultMap = this.enviManageService.getPolluteControlMessage(ledgerId, id, type);
        return resultMap;
    }


    /**
     * 根据 ledgerId、relateId 获取未关联的产污/治污列表
     */
    @RequestMapping(value = "/getLedgerNotRelated")
    public @ResponseBody Map<String, Object> getLedgerNotRelated(HttpServletRequest request){
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        Long relateId = super.getLongParam(request, "relateId", -1);
        Map<String, Object> resultMap = this.enviManageService.getLedgerNotRelated(ledgerId, relateId);
        return resultMap;
    }


    /**
     *  产污/治污关联 保存
     */
    @RequestMapping(value = "/insertUpdateEnviManage")
    public @ResponseBody Map<String,Object> insertUpdateEnviManage(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Long relateId = super.getLongParam(request, "relateId", -1);
            Long ledgerId = super.getLongParam(request, "ledgerId", -1);
            Long polluteId = super.getLongParam(request, "polluteId", -1);
            Long controlId = super.getLongParam(request, "controlId", -1);
            String polluteValStr = super.getStrParam(request, "polluteVal", "");
            Double polluteVal = Double.valueOf(polluteValStr);
            String controlValStr = super.getStrParam(request, "controlVal", "");
            Double controlVal = Double.valueOf(controlValStr);
            EnviManageBean enviManageBean = new EnviManageBean();
            enviManageBean.setRelateId(relateId);
            enviManageBean.setLedgerId(ledgerId);
            enviManageBean.setPolluteId(polluteId);
            enviManageBean.setControlId(controlId);
            enviManageBean.setPolluteVal(polluteVal);
            enviManageBean.setControlVal(controlVal);
            this.enviManageService.insertUpdateEnviManage(enviManageBean);
        }
        catch (Exception e) {
            Log.info("insertUpdateEnviManage error Exception!");
            isSuccess = false;
            message = "保存错误";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }


    /**
     * 污染源治理效果分析  列表页   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoEnviContolResultList")
    public ModelAndView gotoEnviContolResultList(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
        }
        params.put("isGroup", isGroup);

        params.put("beginTime", DateUtil.getAWeekAgoDateStr(DateUtil.SHORT_PATTERN));
        params.put("endTime",  DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));

        return new ModelAndView("energy/enviManage/controlResult_list", params);
    }

    /**
     * 得到"非群组"用户的 下一级能管对象（机构）列表
     * @return
     */
    @RequestMapping("/getChildLedgerList")
    public @ResponseBody String getChildLedgerList(HttpServletRequest request){
        Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if(null!=ledgerId && ledgerId>0){  //非群组
            dataList = this.enviManageService.getChildLedgerList(ledgerId);
        }

        //按照插件所需要的格式拼接字符串
        String str = "";
        if(CollectionUtil.isNotEmpty(dataList)) {
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
     * 污染源治理效果分析 搜索
     */
    @RequestMapping(value = "/ajaxEnviContolResult")
    public @ResponseBody Map<String, Object> ajaxEnviContolResult(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Long partId = super.getLongParam(request, "partId", -1);
        String ledgerName = super.getStrParam(request, "ledgerName", null);
        String baseTime = super.getStrParam(request, "baseTime", null);
        String beginTime = super.getStrParam(request, "beginTime", null);
        String endTime = super.getStrParam(request, "endTime", null);

        Long accountId = super.getSessionUserInfo(request).getAccountId();
        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
        }

        resultMap.putAll(this.enviManageService.ajaxEnviContolResult(partId, ledgerName, baseTime, beginTime, endTime, accountId, isGroup, userLedgerId));
        return resultMap;
    }

    @RequestMapping(value = "/getExcel")
    public @ResponseBody void getExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pageInfo = request.getParameter("pageInfo");
        Map<String, Object> pageMap = jacksonUtils.readJSON2Map(pageInfo);

        String partIdStr = pageMap.get("partId").toString();
        long partId = -1;
        if(StringUtil.isNotEmpty(partIdStr)){
            partId = Long.valueOf(partIdStr);
        }
        String ledgerName = String.valueOf(pageMap.get("ledgerName").toString());
        String baseTime = String.valueOf(pageMap.get("baseTime").toString());
        String beginTime = String.valueOf(pageMap.get("beginTime").toString());
        String endTime = String.valueOf(pageMap.get("endTime").toString());

        Long accountId = super.getSessionUserInfo(request).getAccountId();
        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
        }

        Map<String, Object> queryMap = this.enviManageService.ajaxEnviContolResult(partId, ledgerName, baseTime, beginTime, endTime, accountId, isGroup, userLedgerId);

        String filename = "治理效果分析";
        response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(), "ISO-8859-1")+".xls");	//指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        request.setAttribute("exportExcel", 1);

        enviManageService.getEleExcel(filename, response.getOutputStream(), queryMap);
    }

    /**
     * 污染源"开机分析"  列表页   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoEnviContolOpenList")
    public ModelAndView gotoEnviContolOpenList(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
        }
        params.put("isGroup", isGroup);

        Object o=request.getSession().getAttribute("j23648");
    	if(o!=null){
    		Map<String,Object> param=(Map<String,Object>)o;
    		params.put("beginTime", param.get("beginTime").toString());
    		params.put("endTime", param.get("endTime").toString());
    		//params.put("regionId", param.get("regionId").toString());
    		//params.put("regionName", param.get("regionName").toString());
    		params.put("ledgerName", param.get("ledgerName").toString());
    	}
    	else{
    		params.put("beginTime", DateUtil.getAWeekAgoDateStr(DateUtil.SHORT_PATTERN));
    	    params.put("endTime",  DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
    	}
        
        return new ModelAndView("energy/enviManage/controlOpen_list", params);
    }

    /**
     * 污染源"开机分析" 搜索
     */
    @RequestMapping(value = "/ajaxEnviPolluteOpen")
    public @ResponseBody Map<String, Object> ajaxEnviPolluteOpen(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        request.getSession().setAttribute("j23648",null);
        Long partId = super.getLongParam(request, "partId", -1);
        String ledgerName = super.getStrParam(request, "ledgerName", null);
        Integer hasControl = super.getIntParams(request, "hasControl", -1);
        String beginTime = super.getStrParam(request, "beginTime", null);
        String endTime = super.getStrParam(request, "endTime", null);

        Long accountId = super.getSessionUserInfo(request).getAccountId();
        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
        }

        resultMap.putAll(this.enviManageService.ajaxEnviPolluteOpen(partId, ledgerName, hasControl, beginTime, endTime, accountId, isGroup, userLedgerId));
        return resultMap;
    }

    @RequestMapping(value = "/getOpenExcel")
    public @ResponseBody void getOpenExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pageInfo = request.getParameter("pageInfo");
        Map<String, Object> pageMap = jacksonUtils.readJSON2Map(pageInfo);

        String partIdStr = pageMap.get("partId").toString();
        long partId = -1;
        if(StringUtil.isNotEmpty(partIdStr)){
            partId = Long.valueOf(partIdStr);
        }
        String ledgerName = String.valueOf(pageMap.get("ledgerName").toString());
        String beginTime = String.valueOf(pageMap.get("beginTime").toString());
        String endTime = String.valueOf(pageMap.get("endTime").toString());
        Integer hasControl = Integer.valueOf(pageMap.get("hasControl").toString());

        Long accountId = super.getSessionUserInfo(request).getAccountId();
        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
        }

        Map<String, Object> queryMap = this.enviManageService.ajaxEnviPolluteOpen(partId, ledgerName, hasControl, beginTime, endTime, accountId, isGroup, userLedgerId);

        String filename = "设施开机分析";
        response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(), "ISO-8859-1")+".xls");	//指定下载的文件名
        response.setContentType("application/vnd.ms-excel");
        request.setAttribute("exportExcel", 1);

        enviManageService.getOpenExcel(filename, response.getOutputStream(), queryMap);
    }


    /**
     * 减排企业计划  列表页   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoReducePlan")
    public ModelAndView gotoReducePlan(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("beginTime", DateUtil.getAWeekAgoDateStr(DateUtil.SHORT_PATTERN));
        params.put("endTime",  DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));

        return new ModelAndView("energy/reduce/reduce_plan", params);
    }

    /**
     * 减排企业计划 搜索
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryReducePlan")
    public @ResponseBody Map<String, Object> queryReducePlan(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Integer pageIndex = super.getIntParams(request, "pageIndex", 1);
        Integer pageSize = super.getIntParams(request, "pageSize", 10);
        Page page = new Page(pageIndex, pageSize);

        Integer planType = super.getIntParams(request, "planType", 0);
        String beginTime = super.getStrParam(request, "beginTime", "");
        String endTime = super.getStrParam(request, "endTime", "");

        Map<String, Object>  paramInfo = new HashMap<String, Object>();
        paramInfo.put("planType", planType);
        paramInfo.put("beginTime", beginTime);
        paramInfo.put("endTime", endTime);
        paramInfo.put("page", page);

        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
            userLedgerId = 0L;
        }
        paramInfo.put("isGroup", isGroup);
        paramInfo.put("userLedgerId", userLedgerId);
        paramInfo.put("accountId", super.getSessionUserInfo(request).getAccountId());

        List<Map<String, Object>> list = this.enviManageService.queryReducePlanList(paramInfo);
        resultMap.put("list", list);
        resultMap.put("page", page);

        return resultMap;
    }

    /**
     * 减排企业列表  列表页   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoReduceList")
    public ModelAndView gotoReduceList(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("beginTime", DateUtil.getAWeekAgoDateStr(DateUtil.SHORT_PATTERN));
        params.put("endTime",  DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));

        return new ModelAndView("energy/reduce/reduce_list", params);
    }

    /**
     * 减排企业列表 搜索
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryReduceList")
    public @ResponseBody Map<String, Object> queryReduceList(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Integer pageIndex = super.getIntParams(request, "pageIndex", 1);
        Integer pageSize = super.getIntParams(request, "pageSize", 10);
        Page page = new Page(pageIndex, pageSize);

        String regionId = super.getStrParam(request, "regionId", "");         //区域
        String ledgerName = super.getStrParam(request, "ledgerName", "");
        String beginTime = super.getStrParam(request, "beginTime", "");
        String endTime = super.getStrParam(request, "endTime", "");
        Integer planType = super.getIntParams(request, "planType", 0);        //管控措施
        Integer limitResult = super.getIntParams(request, "limitResult", 0);  //审核情况
        Map<String, Object>  paramInfo = new HashMap<String, Object>();
        paramInfo.put("regionId", regionId);
        paramInfo.put("ledgerName", ledgerName);
        paramInfo.put("beginTime", beginTime);
        paramInfo.put("endTime", endTime);
        paramInfo.put("planType", planType);
        paramInfo.put("limitResult", limitResult);
        paramInfo.put("page", page);

        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
            userLedgerId = 0L;
        }
        paramInfo.put("isGroup", isGroup);
        paramInfo.put("userLedgerId", userLedgerId);
        paramInfo.put("accountId", super.getSessionUserInfo(request).getAccountId());

        List<Map<String, Object>> list = this.enviManageService.queryReduceList(paramInfo);
        resultMap.put("list", list);
        resultMap.put("page", page);

        return resultMap;
    }

    /**
     * 修改审核状态
     */
    @RequestMapping(value = "/updateLimitResult")
    public @ResponseBody Map<String,Object> updateLimitResult(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Long ledgerId = super.getLongParam(request, "ledgerId", -1);
            Integer planType = super.getIntParams(request, "planType", 0);        //管控措施
            Integer limitResult = super.getIntParams(request, "limitResult", 0);  //审核情况
            String beginTime = super.getStrParam(request, "beginTime", "");
            String endTime = super.getStrParam(request, "endTime", "");
            Integer newPlanType = super.getIntParams(request, "newPlanType", 1);        //管控措施
            Map<String, Object>  paramInfo = new HashMap<String, Object>();
            paramInfo.put("ledgerId", ledgerId);
            paramInfo.put("planType", planType);
            paramInfo.put("limitResult", limitResult);
            paramInfo.put("beginTime", beginTime);
            paramInfo.put("endTime", endTime);
            paramInfo.put("newPlanType", newPlanType);
            this.enviManageService.updateLimitResult(paramInfo);
        }
        catch (Exception e) {
            Log.info("updateLimitResult error IOException");
            isSuccess = false;
            message = "修改失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 减排效果排名  列表页   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoReduceRanking")
    public ModelAndView gotoReduceRanking(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("beginTime", DateUtil.getAWeekAgoDateStr(DateUtil.SHORT_PATTERN));
        params.put("endTime",  DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));

        return new ModelAndView("energy/reduce/reduce_rank", params);
    }

    /**
     * 减排效果排名 搜索
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryReduceRanking")
    public @ResponseBody Map<String, Object> queryReduceRanking(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String regionId = super.getStrParam(request, "regionId", "");      //区域
        String ledgerName = super.getStrParam(request, "ledgerName", "");
        String industryId = super.getStrParam(request, "industryId", "");  //行业
        Integer coStatus = super.getIntParams(request, "coStatus", 0);     //企业状态
        Integer planType = super.getIntParams(request, "planType", 0);     //管控措施
        String beginTime = super.getStrParam(request, "beginTime", "");
        String endTime = super.getStrParam(request, "endTime", "");

        Map<String, Object>  paramInfo = new HashMap<String, Object>();
        paramInfo.put("regionId", regionId);
        paramInfo.put("ledgerName", ledgerName);
        paramInfo.put("industryId", industryId);
        paramInfo.put("coStatus", coStatus);
        paramInfo.put("planType", planType);
        paramInfo.put("beginTime", beginTime);
        paramInfo.put("endTime", endTime);

        int isGroup = 0;
        Long userLedgerId = super.getSessionUserInfo(request).getLedgerId();
        if(null == userLedgerId || userLedgerId == 0){  //群组
            isGroup = 1;
            userLedgerId = 0L;
        }
        paramInfo.put("isGroup", isGroup);
        paramInfo.put("userLedgerId", userLedgerId);
        paramInfo.put("accountId", super.getSessionUserInfo(request).getAccountId());

        List<Map<String, Object>> list = this.enviManageService.queryReduceRanking(paramInfo);
        resultMap.put("list", list);

        return resultMap;
    }

}
