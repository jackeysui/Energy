package com.linyang.energy.controller;

import com.leegern.util.DateUtil;
import com.leegern.util.StringUtil;
import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.service.DemandDeclareService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.MeterManagerService;
import com.linyang.energy.utils.CommonOperaDefine;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * 需量申报
 * @author guosen
 * @date 2014-12-22
 * @Version 6273
 */
@Controller
@RequestMapping("/demandDeclare")
public class DemandDeclareController extends BaseController{

	@Autowired
	private DemandDeclareService demandDeclareService;
	@Autowired
	private MeterManagerService meterManagerService;
	@Autowired
	private LedgerManagerService ledgerManagerService;

	public static final Integer ANALYTYPE_102 = 102;

	public static final Integer ANALYTYPE_105 = 105;

	public static final Integer TREETYPE = 1;

	/**
	 * 跳转到需量申报页面
	 * @param request
	 * @return
	 * 分户分析类型:101-建筑楼宇,102-企事业单位,103-商户,104-区域,105-平台运营商,106-设备,107-部门,108-云终端
	 */
	@RequestMapping("/index")
	public ModelAndView gotoDemandDeclarePage(HttpServletRequest request) {
		boolean flag = true;
		LedgerBean ledgerBean = null;
		Map<String, Object> map = new HashMap<String, Object>(1);
		String meterIdstr = request.getParameter("meterId");
		String treeTypestr = request.getParameter("treeType");
		if(StringUtils.isNotEmpty(meterIdstr)){
			Long meterId = Long.parseLong(meterIdstr);
			int treeType = 0;
			if(treeTypestr!=null && !treeTypestr.equals(""))
				treeType = Integer.parseInt(treeTypestr);
			if (treeType == TREETYPE) {
				ledgerBean = ledgerManagerService.getLedgerDataById(meterId);
					do {
						if (ledgerBean != null && ledgerBean.getAnalyType() != ANALYTYPE_102)
							ledgerBean = ledgerManagerService.getLedgerDataById(ledgerBean.getParentLedgerId());
						if (ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_102)
							flag = false;
						if (ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_105)
							flag = false;
					} while (flag);
					if(ledgerBean != null) map.put("ledgerBean", ledgerBean);
					else map.put("ledgerBean", null);
			}else {
				MeterBean meterBean = this.meterManagerService.getMeterDataById(meterId);
				if(meterBean != null)
					ledgerBean = ledgerManagerService.getLedgerDataById(meterBean.getLedgerId());
				do{
				if(ledgerBean != null && ledgerBean.getAnalyType() != ANALYTYPE_102 && ledgerBean.getAnalyType() != ANALYTYPE_105 )
					ledgerBean = ledgerManagerService.getLedgerDataById(ledgerBean.getParentLedgerId());
				if(ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_102 || ledgerBean.getAnalyType() == ANALYTYPE_105 )
					flag=false;
				}while(flag);
					map.put("ledgerBean", null);
				if(ledgerBean != null)
					map.put("ledgerBean", ledgerBean);
			}
		}
		Log.info("map:"+map.toString());
		return new ModelAndView(URLConstant.URL_DEMAND_DECLARE_EMO, map);
	}

	/**
	 * 函数功能说明  :得到申报记录列表
	 * @param request
	 * @return
	 * @return  Map<String,Object>
	 *
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryDeclareList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryDeclareList(HttpServletRequest request)  {
		Map<String, Object> result = new HashMap<String, Object>(3);
		Page page = null;
		// 得到当前分页
		Map<String, Object> queryMap = null;
		try {
			queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		} catch (IOException e) {
			Log.info("Query error");
		}
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
		// 分页查询费率信息

		List<Map<String,Object>> dataList = null;
		if (queryMap.containsKey("queryMap"))
			dataList = demandDeclareService.getDecalrePageData(page, (Map<String,Object>)queryMap.get("queryMap"));
		result.put("page", page);
		result.put("list",dataList);
		if (queryMap.containsKey("queryMap"))
			result.put("queryMap",queryMap.get("queryMap"));
		Log.info("result:"+result.toString());
		return result;
	}
	/**
	 *
	 * 函数功能说明  :获取emo申报列表数据
	 * @param request
	 * @return
	 * @return  Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryEmoDeclareList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryEmoDeclareList(HttpServletRequest request){
		Map<String,Object> dataMap = null;
		try {
			String ledgerIdStr = "";
			if(request.getParameter("ledgerId")!=null) {
                ledgerIdStr = request.getParameter("ledgerId");
                if(ledgerIdStr != null && !ledgerIdStr.equals(""))
                dataMap = demandDeclareService.getEmoDecalreData(Long.parseLong(ledgerIdStr));
            }
		} catch (NumberFormatException e) {
			Log.info("Query error");
		}
		Log.info("dataMap:"+dataMap);
		return dataMap;
	}

	/**
	 * 保存申报信息
	 * @param request
	 * @return Map<String, Object>
	 */
	@RequestMapping("/insert")
	public @ResponseBody Map<String, Object> insert(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> param = this.populateParam(json);
			result = this.demandDeclareService.insert(param);
			isSuccess = true;
		} catch (ParseException e) {
			Log.info("saveSubscriptionInfo error ParseException");

		}
        //记录日志
        StringBuilder sb = new StringBuilder();
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        sb.append("add declare for:").append(request.getParameter("ledgerName"))
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT, CommonOperaDefine.MODULE_ID_DEMAND_ANALYZE, CommonOperaDefine.MODULE_NAME_DEMAND_ANALYZE, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);

        return result;

	}

	/**
	 * 保存申报信息
	 * @param request
	 * @return Map<String, Object>
	 */
	@RequestMapping("/insertDemandDeclare")
	public @ResponseBody Map<String, Object> insertDemandDeclare(HttpServletRequest request){
		Map<String, Object> result = null;
        boolean isSuccess = false;
		try {
			List<Map<String, Object>> paramList = jacksonUtils.readJSON2ListMap(request.getParameter("paramInfo"));
			result = this.demandDeclareService.insertDemandDeclare(paramList);
			isSuccess = true;
		} catch (IOException e) {
			Log.info("insertDemandDeclare error IOException");
		}

        //记录日志
        StringBuilder sb = new StringBuilder();
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        sb.append("add declare for:").append(request.getParameter("ledgerName"))
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT, CommonOperaDefine.MODULE_ID_DEMAND_ANALYZE, CommonOperaDefine.MODULE_NAME_DEMAND_ANALYZE, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);

		return result;
	}

	/**
	 * 删除申报记录
	 * (暂时去除删除功能)
	 * @param	request
	 * @param	declareTime
	 * @param	ledgerName
	 * @return	boolean:isSuccess
	 */
	@RequestMapping("/delete")
	public @ResponseBody boolean delete(HttpServletRequest request, Date declareTime, String ledgerName) {
        boolean isSuccess = false;
        if(declareTime != null) {
			isSuccess = this.demandDeclareService.delete(declareTime);
		}

        //记录日志
        StringBuilder sb = new StringBuilder();
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if (isSuccess) {
            rst = CommonOperaDefine.OPRATOR_SUCCESS;
        }
        if(ledgerName!=null && !ledgerName.equals("")) {
            sb.append("delete declare for:").append(ledgerName)
                    .append(" by ").
                    append(super.getSessionUserInfo(request).getLoginName()).
                    append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_ID_DEMAND_ANALYZE, CommonOperaDefine.MODULE_NAME_DEMAND_ANALYZE, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);

        return isSuccess;
	}

	/**
	 * 	删除申报记录
	 * 	(暂时去除删除功能)
	 * @param request
	 * @return	boolean:isSuccess
	 */
	@RequestMapping("/deleteDemandDeclare")
	public @ResponseBody boolean deleteDemandDeclare(HttpServletRequest request) {
		boolean isSuccess = false;
		List<Date> errorItems = new ArrayList<Date>(0);
		List<Date> declareTime= null;
		try {
			declareTime = jacksonUtils.readJSON2Genric(request.getParameter("declareTime"),new TypeReference<ArrayList<Date>>(){});
		} catch (IOException e) {
			Log.info("Deletion error");
		}

		for (Date time : declareTime) {
				boolean returnFlag = this.demandDeclareService.delete(time);
				if (!returnFlag) {
					errorItems.add(time);
				}
			}
		isSuccess = errorItems.isEmpty()? true:false;

        //记录日志
        StringBuilder sb = new StringBuilder();
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        sb.append("delete declare for:").append(request.getParameter("ledgerName"))
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_ID_DEMAND_ANALYZE, CommonOperaDefine.MODULE_NAME_DEMAND_ANALYZE, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);

		return isSuccess;
	}



	/**
	 *	更新申报记录
	 * @param request
	 * @param recId
	 * @param declareValue
	 * @param ledgerName
	 * @return	boolean:isSuccess
	 */
	@RequestMapping("/update")
	public @ResponseBody boolean update(HttpServletRequest request, Long recId, Double declareValue, String ledgerName){
		boolean isSuccess = false;

		if (recId!=null && recId != 0L && declareValue!=null && declareValue != 0d) {
			isSuccess = this.demandDeclareService.update(recId, declareValue);
		}

		//记录日志
        StringBuilder sb = new StringBuilder();
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess == true){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }

		if (ledgerName!=null && !ledgerName.equals("")) {
			sb.append("update declare for:").append(ledgerName)
					.append(" by ").
					append(super.getSessionUserInfo(request).getLoginName()).
					append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
		}

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_ID_DEMAND_ANALYZE, CommonOperaDefine.MODULE_NAME_DEMAND_ANALYZE, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, String.valueOf(sb)), request);

        return isSuccess;
	}

	/**
	 * 封装参数
	 * @param json
	 * @return	Map<String, Object>
	 * @throws ParseException
	 */
	private Map<String, Object> populateParam(JSONObject json) throws ParseException {
		Map<String, Object> params = new HashMap<String, Object>(6);
		if(json != null) {
			if (json.has("meterId")) {
				params.put("meterId", json.getLong("meterId"));
			}
			if (json.has("declareType")) {
				params.put("declareType", json.getInt("declareType"));
			}
			if (json.has("declareValue")) {
				params.put("declareValue", json.getDouble("declareValue"));
			}
			if (!StringUtil.isEmpty(json.get("beginTime")) && !StringUtil.isEmpty(json.get("endTime"))) {
				params.put("beginTime", DateUtil.convertStrToDate(json.getString("beginTime"), DateUtil.DEFAULT_SHORT_PATTERN));
				params.put("endTime", DateUtil.convertStrToDate(json.getString("endTime"), DateUtil.DEFAULT_SHORT_PATTERN));
			}
		}
		return params;
	}

	/**
	 * 得到emo申报类型
	 * @param request
	 * @return	Integer:declareType
	 */
    @RequestMapping(value = "/getEmoDecalreType")
	public @ResponseBody Integer getEmoDecalreType(HttpServletRequest request){
		String ledgerIdStr = request.getParameter("ledgerId");
        if(ledgerIdStr == null || ledgerIdStr.length()==0){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("ledgerId", ledgerIdStr);
        map.put("date", DateUtil.getCurrMonthFirstDay());
		return demandDeclareService.getEmoDecalreType(map);
	}

}
