package com.linyang.energy.controller.heatpump;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import com.linyang.energy.service.LedgerManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.HeatPumpDataService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;
import net.sf.json.JSONObject;

/**
 * @Description 数据同步管理Controller
 * @author GaoP
 * @date Sep 1, 2014 10:32:16 AM
 */

@Controller
@RequestMapping("/heatpumpdataquery")
public class HeatPumpDataQueryControll extends BaseController {	
	@Autowired
	private HeatPumpDataService heatPumpDataService;

    @Autowired
    private LedgerManagerService ledgerManagerService;
    
    @Autowired
    private UserAnalysisService userAnalysisService;

	/**
	 * 
	 * @return
	 */
	@RequestMapping("/gotoHeatPumpPage")
	public @ResponseBody ModelAndView gotoHeatPumpPage(){
		Map<String, String> param = new HashMap<String, String>();
		Date baseDate=WebConstant.getChartBaseDate();
		String currentTime = DateUtil.convertDateToStr(baseDate,"yyyy-MM-dd HH:00");
		Calendar c=Calendar.getInstance();
		c.setTime(baseDate);
		c.add(Calendar.HOUR_OF_DAY, -6);
		String beginTime =  DateUtil.convertDateToStr(c.getTime(),"yyyy-MM-dd HH:00");
		param.put("beginTime", beginTime);
		param.put("endTime",   currentTime);
		return new ModelAndView(URLConstant.URL_HEATPUMP_DATAQUERY, "params", param);
	}
	
	
	/**
	 * 查询图表数据
	 * @return  
	 */
	@RequestMapping(value = "/queryHeatPumpDataInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryVoltCurrPowerInfo(HttpServletRequest request){
		Map<String, Object> result = null;
		Map<String, Object> param = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		this.populateParam(json, param);
		result=heatPumpDataService.getHeatPumpData(param);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_118, 231l, 1);
		return result;
	}
	
	/**
	 * 处理数据
	 * @param json
	 * @param param
	 */
	@SuppressWarnings({ "unchecked"})
	private void populateParam(JSONObject json, Map<String, Object> param) {
		if(json.has("beginTime")){
			param.put("beginTime", json.getString("beginTime")+":00");
			param.put("beginDate", json.getString("beginTime").substring(0, 10));
		}
		if(json.has("endTime")){
			param.put("endTime", json.getString("endTime")+":00");
			param.put("endDate", json.getString("endTime").substring(0, 10));
		}
		if(json.has("elecData")){
			param.put("elecData", json.getString("elecData"));
		}
		if(json.has("heatData")){
			param.put("heatData", json.getString("heatData"));
		}
		if(json.has("statusData")){
			param.put("statusData", json.getString("statusData"));
		}
		if(json.has("tempData")){
			param.put("tempData", json.getString("tempData"));
		}	
		if(json.has("objectId")){
			param.put("meterId", json.getLong("objectId"));
		}
	}
	
	@RequestMapping(value = "/eleExcel")
	public @ResponseBody void getEleExcel(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String filename = "热泵数据查询";
		Map<String, Object> param = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		this.populateParam(json, param);
       
		response.setHeader("Content-Disposition",
		"attachment;filename="+new String(filename.getBytes(), "ISO-8859-1")+".xls");	//指定下载的文件名
		response.setContentType("application/vnd.ms-excel");
		//得到数据：数据库查询的结果集map，调用上面getElectricityChartData方法
        request.setAttribute("exportExcel", 1);
		//得到页面请求信息pageInfo，作为参数传进getEleExcel方法
        heatPumpDataService.getEleExcel(filename, response.getOutputStream(),param);//"Cache.xls"
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_119, 231l, 1);
	}

    /**
     * 进入热泵工艺图页面   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoHeatMap")
    public ModelAndView gotoHeatMap(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        return new ModelAndView(URLConstant.URL_HEATPUMP_MAP, params);
    }

    /**
     * 查询热泵类型和数据
     * @return
     */
    @RequestMapping("/getLedgerHeatType")
    public @ResponseBody Map<String, Object> getLedgerHeatType(HttpServletRequest request){
        long ledgerId = getLongParam(request, "ledgerId", 0);
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_120, 231l, 1);
        return ledgerManagerService.getLedgerHeatType(ledgerId);
    }
}
