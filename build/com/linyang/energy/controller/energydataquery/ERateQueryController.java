package com.linyang.energy.controller.energydataquery;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.DateUtil;
import com.leegern.util.StringUtil;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.ERateQueryService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;

/**
 * 
 * @author guosen
 * @date 2014-11-24
 */
@Controller
@RequestMapping("/eRate")
public class ERateQueryController extends BaseController{
	
	@Autowired
	private ERateQueryService eRateQueryService;
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 查询电能示值
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryIndicationInfo")
	public @ResponseBody Map<String, Object> queryIndicationInfo(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
            Map<String, Object> chartData = new HashMap<String, Object>();

			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> param = this.populateParam(json);
            int type = (Integer) param.get("type");
            if(type == 1){ //EMO
                chartData = this.eRateQueryService.getChildQData(param);
            }
            result.put("tbData", this.eRateQueryService.queryIndicationInfo(param));
            result.put("chartData", chartData);

			//记录用户足迹
            int sourceType = (Integer) param.get("sourceType");
            if(sourceType == 1){
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),OperItemConstant.OPER_ITEM_22,41L,1);
            }
            else if(sourceType == 2){
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),OperItemConstant.OPER_ITEM_66,206L,1);
            }
		} catch (ParseException e) {
			Log.info("queryIndicationInfo error IOException");
		}
		catch (UnsupportedEncodingException e) {
			Log.info("queryIndicationInfo error UnsupportedEncodingException");
		}
		return result;
	}
	
	/**
	 * 封装查询参数
	 * @param json
	 * @return
	 * @throws ParseException 
	 */
	private Map<String, Object> populateParam(JSONObject json) throws ParseException, UnsupportedEncodingException {
		Map<String, Object> params = new HashMap<String, Object>();
		// Id
		if (json.has("id")) {
			params.put("id", json.getLong("id"));
		}
		if (json.has("type")) {
			params.put("type", json.getInt("type"));
		}
		if (json.has("name")) {
			try{params.put("name", URLDecoder.decode(json.getString("name"),"UTF-8"));}catch(UnsupportedEncodingException ex){Log.error("Decode"+ex.getMessage());}//URLDecoder.decode(json.getString("name"), "UTF-8"));
		}
        Integer sourceType = null;
		if (json.has("sourceType")) {
            sourceType = json.getInt("sourceType");
			params.put("sourceType", sourceType);
		}
        
        if(json.has("dataTypes")){
            params.put("dataTypes",(List) json.get("dataTypes"));
        }

        if(sourceType != null){
            if (! StringUtil.isEmpty(json.get("beginTime")) && ! StringUtil.isEmpty(json.get("endTime"))) {
                // 电能示值-起止时间
                Date beginTime = DateUtil.convertStrToDate(json.getString("beginTime"));
                Date endTime = DateUtil.convertStrToDate(json.getString("endTime"));
                params.put("excelBegin", beginTime);
                params.put("excelEnd",   endTime);
                if(sourceType == 1){
                    beginTime = com.linyang.energy.utils.DateUtil.getDateBetween(beginTime, -1);
                    endTime = com.linyang.energy.utils.DateUtil.getDateBetween(endTime, -1);
                }
                params.put("beginTime", beginTime);
                params.put("endTime",   endTime);
            }
        }
		return params;
	}
	
	/**
	 * 导出到Excel
	 */
	@RequestMapping(value = "/optExcel")
	public @ResponseBody void optExcel(HttpServletRequest request,HttpServletResponse response) {
		try {
            Map<String, Object> result = new HashMap<String, Object>();
            String paramInfo = request.getParameter("paramInfo");
			JSONObject json = JSONObject.fromObject(paramInfo);
			Map<String, Object> param = this.populateParam(json);

            result = this.eRateQueryService.queryIndicationInfo(param);
			
			String title = "能耗示值查询";
			byte[] bs = title.getBytes();
			title = new String(bs, "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+title+".xls");	//指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			eRateQueryService.optExcel(response.getOutputStream(),result, param);

			//记录用户足迹
            int sourceType = (Integer) param.get("sourceType");
            if(sourceType == 1){
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),OperItemConstant.OPER_ITEM_23,41L,1);
            }
            else if(sourceType == 2){
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),OperItemConstant.OPER_ITEM_67,206L,1);
            }			
		} 
		catch (UnsupportedEncodingException e) {
			Log.info("optExcel error UnsupportedEncodingException");

		}
		catch (ParseException e) {
			Log.info("optExcel error ParseException");
		}
		catch (IOException e) {
			Log.info("optExcel error IOException");
		}
	}
}
