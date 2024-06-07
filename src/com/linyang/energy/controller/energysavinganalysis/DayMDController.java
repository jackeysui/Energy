package com.linyang.energy.controller.energysavinganalysis;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.MeterManagerService;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.DateUtil;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.CurveBean;
import com.linyang.energy.service.DayMDService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.WebConstant;

/**
 * 日需量分析
 * @author guosen
 * @date 2014-12-23
 */
@Controller
@RequestMapping("/dayMD")
public class DayMDController extends BaseController{
	
	@Autowired
	private DayMDService dayMDService;
	@Autowired
	private UserAnalysisService userAnalysisService;
    @Autowired
    private MeterManagerService meterManagerService;
	
	/**
	 * 日需量分析页面
	 * @param request
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView gotoDayMDPage(HttpServletRequest request){
		try {
			Date endTime = com.linyang.energy.utils.DateUtil.addDateDay(WebConstant.getChartBaseDate(),-1);
			Date beginTime = DateUtil.getMonthFirstDay(endTime);
			request.setAttribute("beginTime", DateUtil.convertDateToStr(beginTime, DateUtil.DEFAULT_SHORT_PATTERN));
			request.setAttribute("endTime", DateUtil.convertDateToStr(endTime, DateUtil.DEFAULT_SHORT_PATTERN));
		} catch (ParseException e) {
			Log.error(this.getClass() + ".gotoDayMDPage()--无法生成日需量分析");
		}
		return new ModelAndView(URLConstant.URL_DAY_MD);
		
	}
	
	/**
	 * 得到数据
	 * @param request
	 * @return
	 */
	@RequestMapping("getDayMDData")
	public @ResponseBody Map<String, Object> getDayMDData(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> params = this.populateParam(json);
			result.put("chartData", this.dayMDService.getDayMDData(params));
			
			Long operItemId = OperItemConstant.OPER_ITEM_30;
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,43L, 1);
			
		} catch (ParseException e) {
			Log.info("getDayMDData error ParseException");
		}
		return result;
	}
	
	/**
	 * 导出excel
	 * @param params 查询参数
	 * @return
	 */
	@RequestMapping(value = "/exportExcel")
	public @ResponseBody void exportExcel(HttpServletRequest request , HttpServletResponse response )  {
		Map<String, Object> params = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			params = this.populateParam(json);
			List<CurveBean> list = this.dayMDService.getDayMDData(params);
			int treeType = (Integer) params.get("treeType");
			response.setHeader("Content-Disposition", "attachment;filename=dayMD.xls");	
			response.setContentType("application/vnd.ms-excel");
			dayMDService.exportExcel(response.getOutputStream(), list, treeType);
			
			Long operItemId = OperItemConstant.OPER_ITEM_31;
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,43L, 1);
		} catch (ParseException e) {
			Log.info("exportExcel error ParseException");
		}
		catch (IOException e) {
			Log.info("exportExcel error IOException");
		}
	}

    @RequestMapping(value = "/beforeClickMmd", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> beforeClickMmd(HttpServletRequest request) {
        Map<String,Object> map = new HashMap<String, Object>();
        Long pointId = getLongParam(request, "pointId", -1);
        int treeType = super.getIntParams(request, "treeType", -1);
        if(pointId != -1){
        	if(treeType == 1){
        		Long volume= this.dayMDService.getLedgerVolumeByLedgerId(pointId);
        		if(volume != null){
        			map.put("volume", volume);
        		}
        	}else{
        		MeterBean meterBean = this.meterManagerService.getMeterDataById(pointId);
        		if(meterBean != null){
        			map.put("volume", meterBean.getVolume());
        		}
        	}
        }
        return map;
    }
	
	/**
	 * 封装查询参数
	 * @param json
	 * @return
	 * @throws ParseException 
	 */
	private Map<String, Object> populateParam(JSONObject json) throws ParseException {
		Map<String, Object> params = new HashMap<String, Object>();
		if(json.has("meterId")) {
			params.put("meterId", json.getLong("meterId"));
		}
		if(json.has("meterName")){
			params.put("meterName", json.getString("meterName"));
		}
		if(json.has("treeType")){
			params.put("treeType", json.getInt("treeType"));
		}
		if(json.has("dateType")){//日期类型
			int dateType = json.getInt("dateType");
			Date beginTime = null; 
			Date endTime = null;
			Date convertDate = null;
			String convertStr = null;
			switch (dateType) {
				case 1://上月
					beginTime = DateUtil.clearDate(DateUtil.getLastMonthFirstDay());
					convertDate = DateUtil.getLastDayOfMonth(beginTime);
					convertStr = DateUtil.convertDateToStr(convertDate, DateUtil.DEFAULT_SHORT_PATTERN);
					endTime = DateUtil.convertStrToDate(convertStr+" 23:59:59");
					break;
				case 2://本月
					beginTime = DateUtil.clearDate(DateUtil.getMonthFirstDay(new Date()));
					convertDate = DateUtil.getLastDayOfMonth(beginTime);
					convertStr = DateUtil.convertDateToStr(convertDate, DateUtil.DEFAULT_SHORT_PATTERN);
					endTime = DateUtil.convertStrToDate(convertStr+" 23:59:59");
					break;
				case 4://自定义
					beginTime = DateUtil.convertStrToDate(json.getString("beginTime")+" 00:00:00");
					endTime = DateUtil.convertStrToDate(json.getString("endTime")+" 23:59:59");
					break;
			}
			params.put("beginTime", beginTime);
			params.put("endTime",   endTime);
		}
		
		return params;
	}
}
