package com.linyang.energy.controller.energydataquery;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.Else;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleIfStatement.ElseIf;
import com.leegern.util.DateUtil;
import com.leegern.util.StringUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.CurveBean;
import com.linyang.energy.service.MeterManagerService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.service.VoltCurrPowerService;
import com.linyang.energy.utils.OperItemConstant;

/**
 * @Description 曲线查询Controller 
 * @author Leegern
 * @date Dec 4, 2013 5:12:24 PM
 */
@Controller
@RequestMapping("/voltcurrpower")
public class VoltCurrPowerController extends BaseController {
	@Autowired
	private VoltCurrPowerService voltCurrPowerService;
	@Autowired
	private MeterManagerService meterManagerService;
	@Autowired
	private UserAnalysisService userAnalysisService;

	/**
	 * 查询图表数据
	 * @return  
	 */
	@RequestMapping(value = "/queryVoltCurrPowerInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryVoltCurrPowerInfo(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		List<CurveBean> chartData = null;	//曲线数据
		boolean hasData = false;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
//			System.out.println("json >> " + json.toString());
			Map<String, Object> params = this.populateParam(json);
			int curveType = (Integer)params.get("curveType");
			int objectType = (Integer)params.get("objectType");
			chartData = voltCurrPowerService.queryVoltCurrPowerInfo(params);
			if(chartData!= null && chartData.size() > 0){
				hasData = true;
			}
            if(curveType == 8 || curveType == 9){
                CurveBean noblance = this.voltCurrPowerService.getLastNoblanceData(params);
                if(noblance != null){
                    result.put("lastAngle", noblance);
                }
            }
			if(json.has("volMeterId")){
				result.put("volMeterId", json.getString("volMeterId"));
			}
			result.putAll(handleData(chartData, params));
			if(curveType == 1 && objectType ==1){//是EMO且是电压曲线
				List<Map<String, Object>> volMeters = voltCurrPowerService.getVolMeterIds(params);
				if(volMeters != null){
					result.put("volMeters", volMeters);
				}
			}
			//记录用户足迹
            if(json.getInt("recordUse") == 1){
                Long operItemId = getOperItemId(curveType);
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,13L, 1);
            }
		} catch (ParseException e) {
			Log.info("queryVoltCurrPowerInfo error ParseException");
		}
		result.put("hasData", hasData);
		return result;
	}
	
	/**
	 * 根据曲线类型的到操作ID
	 * @param curveType 曲线类型
	 * @return
	 */
	private Long getOperItemId(Integer curveType) {
		Long operItemId = OperItemConstant.OPER_ITEM_5;
		if(curveType==1){//电压
			operItemId = OperItemConstant.OPER_ITEM_15;
		}else if(curveType==2){//电流
			operItemId = OperItemConstant.OPER_ITEM_13;
		}else if(curveType==3){//有功功率
			operItemId = OperItemConstant.OPER_ITEM_9;
		}else if(curveType==4){//无功功率
			operItemId = OperItemConstant.OPER_ITEM_11;
		}else if(curveType==5){//功率因数
			operItemId = OperItemConstant.OPER_ITEM_7;
		}else if(curveType==6){//电量
			operItemId = OperItemConstant.OPER_ITEM_5;
		}else if (curveType==7) {//需量
			operItemId = OperItemConstant.OPER_ITEM_91;
		}else if (curveType==8) {//电压相位角
            operItemId = OperItemConstant.OPER_ITEM_93;
        }else if (curveType==9) {//电流相位角
            operItemId = OperItemConstant.OPER_ITEM_95;
        }else if (curveType==10) {//电网频率
        	operItemId = OperItemConstant.OPER_ITEM_98;
        }else if (curveType==11) {//水量
            operItemId = OperItemConstant.OPER_ITEM_107;
        }else if (curveType==12) {//气量
			operItemId = OperItemConstant.OPER_ITEM_109;
		}
		return operItemId;
	}

	/**
	 * 处理返回的数据
	 * @param chartData
	 * @param lineData
	 * @param params
	 * @return
	 */
	private Map<String, Object> handleData(List<CurveBean> chartData, Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int curveType = (Integer)params.get("curveType");
		//电压电流情况下要判断接线方式
		if (curveType == CurveBean.CURVE_TYPE_V || curveType==CurveBean.CURVE_TYPE_I ) {
			Integer oType = (Integer)params.get("objectType");
			Integer commMode = 0;
			if(oType != null && oType == 1){//EMO
				commMode = this.meterManagerService.getCommModeByLedgerId((Long)params.get("objectId"));
			}else {//DCP
				commMode = this.meterManagerService.getCommModeByMeterId((Long)params.get("objectId"));
			}
			result.put("commMode", commMode);//接线方式
		}
		//查询电压、电流、有功功率、无功功率、功率因数水平线
		if (curveType != CurveBean.CURVE_TYPE_ELE && curveType != CurveBean.CURVE_TYPE_RP && curveType != CurveBean.CURVE_TYPE_D
                && curveType != CurveBean.CURVE_TYPE_FREQ && curveType != CurveBean.CURVE_TYPE_WATER) {//排除

			Map<String, Object> lineData = voltCurrPowerService.queryStraightLine(params);
			result.put("lineData",  lineData);
		}
		result.put("chartData", chartData);
		return result;
	}
	
	/**
	 * 查询报表数据
	 * @return  
	 */
	@RequestMapping(value = "/queryVoltCurrReportInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryVoltCurrReportInfo(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		List<CurveBean> chartData = null;	//曲线数据
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> params = this.populateParam(json);
			chartData = voltCurrPowerService.queryVoltCurrPowerReportInfo(params);
			result.putAll(handleData(chartData, params));

		} catch (ParseException e) {
			Log.info("queryVoltCurrReportInfo error ParseException");
		}
		return result;
	}
	
	/**
	 * 设置用户y轴最小值
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setUserYMin", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> setUserYMin(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "成功";
        Long accountId = super.getSessionUserInfo(request).getAccountId();
        Integer yMin = super.getIntParams(request, "yMin", 0);
        
            this.voltCurrPowerService.setUserYMin(accountId, yMin);
       

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
	}
	
	/**
	 * 导出EXCEL
	 * @return
	 */
	@RequestMapping(value = "/toExcel")
	public @ResponseBody void toExcel(HttpServletRequest request , HttpServletResponse response )  {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = null;
		List<CurveBean> chartData = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			params = this.populateParam(json);
			//标准功率因数：
			result.put("standardPF", (String)params.get("standardPF"));
			int curveType = (Integer)params.get("curveType");
			Long operItemId = OperItemConstant.OPER_ITEM_6;
			if(curveType==1){//电压
				operItemId = OperItemConstant.OPER_ITEM_16;
			}else if(curveType==2){//电流
				operItemId = OperItemConstant.OPER_ITEM_14;
			}else if(curveType==3){//有功功率
				operItemId = OperItemConstant.OPER_ITEM_10;
			}else if(curveType==4){//无功功率
				operItemId = OperItemConstant.OPER_ITEM_12;
			}else if(curveType==5){//功率因数
				operItemId = OperItemConstant.OPER_ITEM_8;
			}else if(curveType==6){//电量
				operItemId = OperItemConstant.OPER_ITEM_6;
			}else if (curveType==7) {//需量
				operItemId = OperItemConstant.OPER_ITEM_92;
			}else if (curveType==8) {//电压相位角
                operItemId = OperItemConstant.OPER_ITEM_94;
            }else if (curveType==9) {//电流相位角
                operItemId = OperItemConstant.OPER_ITEM_96;
            }else if (curveType==10) {//电网频率
				operItemId = OperItemConstant.OPER_ITEM_99;
			}
            else if (curveType==11) {//水量
                operItemId = OperItemConstant.OPER_ITEM_108;
            }
            else if (curveType==12) {//气量
				operItemId = OperItemConstant.OPER_ITEM_110;
			}
			result.put("objectId", (Long)params.get("objectId"));
			result.put("oType", (Integer)params.get("objectType"));
			result.put( "checkType" , (Integer)params.get("curveType") ) ;
            result.put( "showEleCD" , (Integer)params.get("showEleCD") ) ;
			// 查询电压电流功率曲线数据
			chartData = voltCurrPowerService.queryVoltCurrPowerReportInfo(params);
			
			result.putAll(handleData(chartData, params));
			String title = this.voltCurrPowerService.getTitleByCurveType(curveType);
			byte[] bs = title.getBytes();
			title = new String(bs, "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+title+".xls");	//指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			voltCurrPowerService.getEleExcel("Cache.xls", response.getOutputStream() , result );
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,13L,1);

		} 
		catch (ParseException e) {
			Log.info("toExcel error ParseException");
		}
		catch (UnsupportedEncodingException e) {
			Log.info("toExcel error UnsupportedEncodingException");
		}
		catch (IOException e) {
			Log.info("toExcel error IOException");
		}
	}
	
	/**
	 * 查询电压列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryVoltCurrPowerList", method = RequestMethod.POST)
	public @ResponseBody List<CurveBean> queryVoltCurrPowerList(HttpServletRequest request){
		Map<String, Object> params = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			params = this.populateParam(json);
		} catch (ParseException e) {
			Log.info("queryVoltCurrPowerList error ParseException");
		}
		return voltCurrPowerService.queryVoltCurrPowerList(params);
	}


    /**
     * 指标关联 -- 记录用户使用记录
     */
    @RequestMapping(value = "/insertUserRecord")
    public @ResponseBody void insertUserRecord(HttpServletRequest request,HttpServletResponse response) {
        //记录用户足迹
        this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_54, 215L, 1);
    }

	
	/**
	 * 封装查询参数
	 * @param json
	 * @return
	 * @throws ParseException 
	 */
	private Map<String, Object> populateParam(JSONObject json) throws ParseException {
		Map<String, Object> params = new HashMap<String, Object>();
		//对象类型：1-EMO;2-DCP
		if(json.has("objectType")){
			params.put("objectType", json.getInt("objectType"));
		}
		// 对象ID
		if (json.has("objectId")) {
			params.put("objectId", json.getLong("objectId"));
		}
		// 起止时间
		if (! StringUtil.isEmpty(json.get("beginTime")) && ! StringUtil.isEmpty(json.get("endTime"))) {
			params.put("beginTime", DateUtil.convertStrToDate(json.getString("beginTime")));
			params.put("endTime",   DateUtil.convertStrToDate(json.getString("endTime")));
		}
		// 曲线类型
		if (json.has("curveType")) {
			params.put("curveType", json.getInt("curveType"));
		}
		if (json.has("isLedger")) {
			params.put("isLedger", json.getBoolean("isLedger"));
		}
		// 间隔天数,转换成曲线数据类型
		if (json.has("beweenDays")) {
			int beweenDays = json.getInt("beweenDays");
			int dataType = 0;
			//一周之内的曲线，默认取15分钟一个数据，最大672个数据。
			if(beweenDays < 7) {
				dataType = 0;
			}
			 //2、一个月之内(31天)的曲线，默认取60分钟数据，最大744个数据。
			if (beweenDays > 6 && beweenDays <= 31) {
				dataType = 1;
			}
			//3、大于一个月小于一年的数据，默认每天一个数据。（只做电量）
			if (beweenDays > 31) {
				dataType = 2;
			}
			params.put("dataType", dataType);
		}
		if (json.has("ledgerName")) {
			params.put("ledgerName", json.getString("ledgerName"));
		}
		if (json.has("standardPF")) {
			params.put("standardPF", json.getString("standardPF"));
		}
		if (json.has("volMeterId") && !json.getString("volMeterId").equals("null")){
			params.put("objectId", json.getLong("volMeterId"));
			params.put("objectType", 2);
		}
        if (json.has("showEleCD")) {
			params.put("showEleCD", json.getInt("showEleCD"));
		}
		if (json.has("frequency")) {
			params.put( "frequency" , json.getInt("frequency") );
		}
		if (json.has("Vtype")) {
			params.put( "Vtype" , json.getInt("Vtype") );
		}
		return params;
	}
	
	
	
	/**
	 * 查询电压列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryComm", method = RequestMethod.POST)
	public @ResponseBody Integer queryComm(HttpServletRequest request){
		Long meterId = Long.parseLong( request.getParameter( "meterId" ) );
		Integer commMode = voltCurrPowerService.queryCommMode(meterId);
		return commMode;
	}
	
	
	
	
}
