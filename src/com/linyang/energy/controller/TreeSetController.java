package com.linyang.energy.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.MeterManagerService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.minlog.Log;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.model.LedgerRelationBean;
import com.linyang.energy.model.LineLossTreeBean;
import com.linyang.energy.service.TreeSetService;

/**
 * 
 * @author guosen
 *
 */
@Controller
@RequestMapping("/treeSet")
public class TreeSetController extends BaseController{
	
	@Autowired
	private TreeSetService treeSetService;
    @Autowired
    private LedgerManagerService ledgerManagerService;
    @Autowired
    private MeterManagerService meterManagerService;
    @Autowired
    private UserAnalysisService userAnalysisService;

	/**
	 * 显示EMO模型配置
	 * @param request
	 * @return
	 */
	@RequestMapping("/showEMOTreePage")
	public ModelAndView showEMOTreePage(HttpServletRequest request){
		return new ModelAndView(URLConstant.EMO_TREE_SET);
	}
	
	/**
	 * 显示电力拓扑模型配置
	 * @param request
	 * @return
	 */
	@RequestMapping("/showEleTreePage")
	public ModelAndView showEleTreePage(HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("listStr", this.meterManagerService.getVirtualContains(0L, 1));
        map.put("listStr", this.meterManagerService.getVirtualContainss(0L, 1));
		return new ModelAndView(URLConstant.ELE_TREE_SET,map);
	}
	
	
	/**
	 * 得到该用户下的企业列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getCompanyList")
	public @ResponseBody String getCompanyList(HttpServletRequest request){
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		Integer analyType = getIntParams(request, "analyType", 102);

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        if(ledgerId == 0){  //群组
            dataList = ledgerManagerService.getLedgersForGroup2(super.getSessionUserInfo(request).getAccountId());
        }
        else {  //非群组
            dataList = treeSetService.getCompanyList(ledgerId, analyType);
        }

		String str = listToString(dataList);
		return str;
	}
	
	/**
	 * 得到该用户下的企业列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/getEMOList")
	public @ResponseBody String getEMOList(HttpServletRequest request){
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		List<Map<String, Object>> dataList = treeSetService.getEMOList(ledgerId);
		String str = listToString(dataList);
		return str;
	}

	/**
	 * 把list转换为string
	 * @param dataList
	 * @return
	 */
	private String listToString(List<Map<String, Object>> dataList) {
		StringBuffer sb = new StringBuffer("[");
		String str = "";
		//按照插件所需要的格式拼接字符串
		if(dataList.size()>0) {
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("LEDGER_ID"))
					.append("',label:'").append(data.get("LEDGER_NAME"))
					.append("',value:'").append(data.get("LEDGER_NAME"))
					.append("',num:'").append(data.get("LEDGER_ID"))
					.append("',count:'0'},");
			}	
			str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		} else
			str = "[]";
		return str;
	}

    /**
     * 得到管辖区域内未设置经纬度的企业列表
     * @param request
     * @return
     */
    @RequestMapping("/getNoPositionLedgerList")
    public @ResponseBody String getNoPositionLedgerList(HttpServletRequest request){
        Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
        List<Map<String, Object>> dataList = treeSetService.getNoPositionLedgerList(ledgerId);
        StringBuffer sb = new StringBuffer("[");
        String str = "";
        //按照插件所需要的格式拼接字符串
        if(dataList != null && dataList.size()>0) {
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
     * 得到首页地图 某种搜索模式 下的自动完成结果
     * @param request
     * @return
     */
    @RequestMapping("/getSearchModelDataList")
    public @ResponseBody String getSearchModelDataList(HttpServletRequest request){
        Integer searchModel = getIntParams(request, "searchModel", -1);
        Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
        List<Map<String, Object>> dataList = treeSetService.getSearchModelDataList(ledgerId, searchModel);
        StringBuffer sb = new StringBuffer("[");
        String str = "";
        //按照插件所需要的格式拼接字符串
        if(dataList != null && dataList.size()>0) {
            for(Map<String,Object> data : dataList) {
                sb.append("{id:'").append(data.get("ID"))
                        .append("',label:'").append(data.get("NAME"))
                        .append("',value:'").append(data.get("NAME"))
                        .append("',num:'").append(data.get("ID"))
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
	 * 得到未配置的DCP
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUnSetDCP")
	public @ResponseBody List<LineLossTreeBean> getUnSetDCP(HttpServletRequest request){
		long ledgerId = getLongParam(request, "ledgerId", 0);
		return treeSetService.getUnSetDCP(ledgerId);
	}
	
	/**
	 * 得到已配置的DCP
	 * @param request
	 * @return
	 */
	@RequestMapping("/getSetDCP")
	public @ResponseBody List<LineLossTreeBean> getSetDCP(HttpServletRequest request){
		long ledgerId = getLongParam(request, "ledgerId", 0);
		long type = getLongParam(request, "type", 0);
		String meterName = getStrParam( request,"meterName",null );
		if(meterName != null && !meterName.equals( "" ))
			meterName = meterName.replaceAll("\\s*", "");
		return treeSetService.getSetDCP(ledgerId, type,meterName);
	}
	
	/**
	 * 得到EMO管理树
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getEMOTree")
	public @ResponseBody List<Map<String, Object>> getEMOTree(HttpServletRequest request){
		long ledgerId = getLongParam(request, "ledgerId", 0);
		String meterName = getStrParam( request,"meterName",null );
		if(meterName != null && !meterName.equals( "" ))
			meterName = meterName.replaceAll("\\s*", "");
		return treeSetService.getEMOTree(ledgerId,meterName);
	}
	
	/**
	 * 得到EMO总加组模型配置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getEMOModel1")
	public @ResponseBody List<LedgerRelationBean> getEMOModel1(HttpServletRequest request){
		long ledgerId = getLongParam(request, "ledgerId", 0);
		return treeSetService.getEMOModel1(ledgerId);
	}
	
	/**
	 * 得到EMO表计模型配置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getEMOModel2")
	public @ResponseBody List<Map<String, Object>> getEMOModel2(HttpServletRequest request){
		long ledgerId = getLongParam(request, "ledgerId", 0);
		return treeSetService.getEMOModel2(ledgerId);
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(value="/save")
	public @ResponseBody boolean save(HttpServletRequest request){
        boolean result = true;
        Long ledgerId = getLongParam(request, "ledgerId", 0);
		try {
			List<Map<String, Object>> ledgerRelationBeans = jacksonUtils.readJSON2ListMap(request.getParameter("ledgerRelationBeans"));
			List<Map<String, Object>> ledgerShowBeans = jacksonUtils.readJSON2ListMap(request.getParameter("ledgerShowBeans"));
            result = treeSetService.save(ledgerId, ledgerRelationBeans,ledgerShowBeans);
		} catch (IOException e) {
			Log.info("save error IOException");
            result = false;
		}

        //记录日志
        StringBuilder sb = new StringBuilder();
        sb.append("update a ledger object model:").append(this.ledgerManagerService.getLedgerDataById(ledgerId).getLedgerName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(result == true){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_ID_LEDGER_MODEL, CommonOperaDefine.MODULE_NAME_LEDGER_MODEL, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_148, 117l, 1);
		return result;
	}
	
	/**
	 * 根据Id得到未配置的DCP
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUnSetDCPByName")
	public @ResponseBody List<LineLossTreeBean> getUnSetDCPByName(HttpServletRequest request){
		String meterName = getStrParam(request, "meterName", "");
		long ledgerId = getLongParam( request,"treeName" ,0 );
		return treeSetService.getUnSetDCPByName("%"+meterName+"%",ledgerId);
	}
}
