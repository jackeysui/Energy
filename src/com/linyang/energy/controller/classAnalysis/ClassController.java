package com.linyang.energy.controller.classAnalysis;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.ClassService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班组控制器
 * Created by Administrator on 16-8-1.
 *
 */
@Controller
@RequestMapping("/class")
public class ClassController extends BaseController {

    @Autowired
    private ClassService classService;

    @Autowired
    private LedgerManagerService ledgerManagerService;
    
    @Autowired
    private UserAnalysisService userAnalysisService;


    /**
     * 进入班制配置页面   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoClassConfig")
    public ModelAndView gotoClassConfig(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
        params.put("loginLedger", bean);
        return new ModelAndView(URLConstant.URL_CLASS_CONFIG, params);
    }

    /**
     * 得到下拉de企业列表
     * @param request
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
            dataList = this.classService.getCompLedgerList(ledgerId);
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
     * 根据ledgerId查询班制信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getLedgerClasses")
    public @ResponseBody Map<String,Object> getLedgerClasses(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        if(ledgerId > 0){
            result = this.classService.getLedgerClassMessage(ledgerId);
        }
        return result;
    }

    /**
     * 根据 班制ID 查询班组
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTeamsByClassId")
    public @ResponseBody Map<String,Object> getTeamsByClassId(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long classId = super.getLongParam(request, "classId", -1);
        if(classId > 0){
            result = this.classService.getTeamsByClassId(classId);
        }
        return result;
    }

    /**
     * 新增、修改班制
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertOrUpdateClass")
    public @ResponseBody Map<String,Object> insertOrUpdateClass(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> classInfo = jacksonUtils.readJSON2Map(request.getParameter("classInfo"));
            Map<String, Object> result = this.classService.insertOrUpdateClass(classInfo);
            String resultMess = result.get("message").toString();
            if(resultMess.length()>0){  //保存失败
                isSuccess = false;
                message = resultMess;
            }
            else{  //保存成功
                resultMap.put("classId", result.get("classId"));
            }
    
            //记录用户足迹
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_132, 124l, 1);
            
        } catch (IOException e) {
            //保存失败
        	Log.info("insertOrUpdateClass error IOException");
            isSuccess = false;
            message = "保存错误";
        }
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 删除班制
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteClass")
    public @ResponseBody Map<String,Object> deleteClass(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = false;
        
            Long classId = super.getLongParam(request, "classId", -1);
            this.classService.deleteClass(classId);

            isSuccess = true;
       

        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }


    /**
     * 进入生产单元配置页面   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoWorkShopConfig")
    public ModelAndView gotoWorkShopConfig(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        LedgerBean loginLedger = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
        params.put("loginLedger", loginLedger);
        return new ModelAndView(URLConstant.URL_WORKSHOP_CONFIG, params);
    }

    @RequestMapping("/getAjaxClassData")
    public @ResponseBody Map<String, Object> getAjaxClassData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Integer pageIndex = super.getIntParams(request, "pageIndex", 1);
        Integer pageSize = super.getIntParams(request, "pageSize", 10);
        Page page = new Page(pageIndex, pageSize);
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        List<Map<String, Object>> list = this.classService.getWorkShopConfigs(page, ledgerId);
        map.put("list",list);
        map.put("page", page);
        //记录用户足迹
        this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_154, 125l, 1);
        return map;
    }

    /**
     * 根据ID查询车间详细信息
     * @param request
     * @return
     */
    @RequestMapping("/gotoWorkShopDetail")
    public ModelAndView gotoWorkShopDetail(HttpServletRequest request){
        long ledgerId = super.getLongParam(request, "ledgerId", -1);
        long workShopId = super.getLongParam(request, "workShopId", -1);
        Map<String, Object> map = this.classService.getWorkShopDetail(ledgerId, workShopId);
        map.put("LEDGER_ID", ledgerId);
        map.putAll(this.classService.getLedgerCanChoose(ledgerId));
        return new ModelAndView("energy/classmanager/add_update_workshop",map);
    }

    /**
     * 新增、修改 生产单元配置
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertUpdateWorkshop")
    public @ResponseBody Map<String,Object> insertUpdateWorkshop(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> workInfo = jacksonUtils.readJSON2Map(request.getParameter("workInfo"));
            Map<String, Object> map = this.classService.insertUpdateWorkshop(workInfo);
            String resultMess = map.get("message").toString();
            if(resultMess.length()>0){  //保存失败
                isSuccess = false;
                message = resultMess;
            }
        } catch (IOException e) {
            //保存失败
        	Log.info("insertUpdateWorkshop error IOException");
            isSuccess = false;
            message = "保存错误";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 删除 生产单元配置
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteWorkshopConfig")
    public @ResponseBody Map<String,Object> deleteWorkshopConfig(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = false;
        try {
            String workShopStr = super.getStrParam(request, "workShopStr", "");
            if(workShopStr!=null){
            String[] items = workShopStr.split(",");
            for(int i = 0; i < items.length; i++){
                String item = items[i];
                Long workShopId = Long.valueOf(item);
                this.classService.deleteWorkshopConfig(workShopId);
            }

            isSuccess = true;}
        } catch (NumberFormatException e) {
        	Log.info("deleteWorkshopConfig error NumberFormatException");
            isSuccess = false;
        }

        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }

    /*************************************************************************************
     * 自定义排班计划---初始化页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/getDefineClassDetail")
    public @ResponseBody Map<String,Object> getDefineClassDetail(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long classId = super.getLongParam(request, "classId", -1);
        if(classId > 0){
            result = this.classService.getDefineClassDetail(classId);
        }
        return result;
    }

    /**
     * 自定义排班计划---保存
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertUpdateDefineClass")
    public @ResponseBody Map<String,Object> insertUpdateDefineClass(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> workingInfo = jacksonUtils.readJSON2Map(request.getParameter("workingInfo"));
            Map<String, Object> result = this.classService.insertUpdateDefineClass(workingInfo);
            String resultMess = result.get("message").toString();
            if(resultMess.length()>0){  //保存失败
                isSuccess = false;
                message = resultMess;
            }
        } catch (IOException e) {
            //保存失败
        	Log.info("insertUpdateDefineClass error IOException");
            isSuccess = false;
            message = "保存错误";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

}
