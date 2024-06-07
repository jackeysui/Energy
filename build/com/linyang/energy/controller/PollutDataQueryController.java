package com.linyang.energy.controller;

import com.leegern.util.DateUtil;
import com.leegern.util.StringUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.model.CurveBean;
import com.linyang.energy.service.MeterManagerService;
import com.linyang.energy.service.PollutVoltCurrPowerService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.service.VoltCurrPowerService;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.PropertyUtil;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DateUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 10:23 2018/12/11
 * @ Description：产污设施管理
 * @ Modified By：:dingy
 * @Version: $version$
 */
@Controller
@RequestMapping("pollutQuery")
public class PollutDataQueryController extends BaseController {
	
	@Autowired
	private VoltCurrPowerService voltCurrPowerService;
	@Autowired
	private MeterManagerService meterManagerService;
	@Autowired
	private PollutVoltCurrPowerService pollutVoltCurrPowerService;
	@Autowired
	private UserAnalysisService userAnalysisService;
	/**
	 * 电量查询
	 * @return
	 */
	@RequestMapping("/showPollutVcPage")
	public ModelAndView showVoltageCurrentPage(HttpServletRequest request){
		Map<String, String> param = new HashMap<String, String>();
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		String beforeAfterDate = DateUtils.getBeforeAfterDate(currentTime, -1);
		Long accountId = super.getSessionUserInfo(request).getAccountId();
		//获取用户y轴最小值设置
		param.put("yMinValue", voltCurrPowerService.getUserYMin(accountId).toString());
		param.put("beginTime", beforeAfterDate);
		param.put("endTime",   beforeAfterDate);
		
		param.put("menuType", "1");//查询选项类型
		return new ModelAndView("/energy/dataquery/query_pollutVol_current", "params", param);
	}
	
	
	
	/**
	 * 查询报表数据
	 * @return
	 */
	@RequestMapping(value = "/queryPollutVoltCurrReportInfo", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> queryPollutVoltCurrReportInfo(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		boolean hasData = false;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> params = this.populateParam(json);
			
			Map<String, Object> idMap = null;
			if (null != params.get( "objectId" )) {
				idMap = pollutVoltCurrPowerService.queryFacilRelation( Long.parseLong( params.get( "objectId" ).toString() ) );
			}
			
			if (null != idMap) {
				params.putAll( idMap );
				
				List<CurveBean> curveBeans = pollutVoltCurrPowerService.queryPollutCur( params );
				
				result.putAll(handleData(curveBeans, params));
				if(null != curveBeans && curveBeans.size() > 0)
					hasData = true;
			} else {
				result.put("isNotPollut", true);
			}
			result.put("hasData", hasData);
		} catch (ParseException e) {
			Log.info("queryVoltCurrReportInfo error ParseException");
		}
		return result;
	}
	
	/**
	 * 处理返回的数据
	 * @param chartData
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
		ListSort(chartData);
		result.put("chartData", chartData);
		return result;
	}
	
	
	/**
	 * 对传进来的bean的产污数量进行排序(从大到小)
	 * @author dingy
	 * @param list
	 * @return void
	 * @exception
	 * @date 2018/10/31 15:27
	 */
	private static void ListSort(List<CurveBean> list) {
		Collections.sort( list, new Comparator<CurveBean>() {
			@Override
			public int compare(CurveBean o1, CurveBean o2) {
				try {
					if (o1.getFreezeTime().after( o2.getFreezeTime() )) {
						return 1;
					} else if (o1.getFreezeTime().before( o2.getFreezeTime() )) {
						return -1;
					} else {
						return 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		} );
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
			if (beweenDays > 6 && beweenDays < 31) {
				dataType = 1;
			}
			//3、大于一个月小于一年的数据，默认每天一个数据。（只做电量）
			if (beweenDays > 30) {
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
		return params;
	}
	
	
	
	/**
	 * 导出EXCEL
	 * @return
	 */
	@RequestMapping(value = "/toExcel")
	public @ResponseBody
	void toExcel(HttpServletRequest request , HttpServletResponse response )  {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> params = null;
		List<CurveBean> chartData = null;
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			params = this.populateParam(json);
			//标准功率因数：
			result.put("standardPF", (String)params.get("standardPF"));
			
			Long operItemId = OperItemConstant.OPER_ITEM_6;
			
			result.put("objectId", (Long)params.get("objectId"));
			result.put("oType", (Integer)params.get("objectType"));
			result.put( "checkType" , (Integer)params.get("curveType") ) ;
			result.put( "showEleCD" , (Integer)params.get("showEleCD") ) ;
			// 查询电量曲线数据
			Map<String, Object> idMap = null;
			if (null != params.get( "objectId" )) {
				idMap = pollutVoltCurrPowerService.queryFacilRelation( Long.parseLong( params.get( "objectId" ).toString() ) );
			}
			
			if (null != idMap) {
				params.putAll( idMap );
				
				List<CurveBean> curveBeans = pollutVoltCurrPowerService.queryPollutCur( params );
				
				result.putAll(handleData(curveBeans, params));
			}
			
			String title = "设施用电分析";
			byte[] bs = title.getBytes();
			title = new String(bs, "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+title+".xls");	//指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			pollutVoltCurrPowerService.getEleExcel("Cache.xls", response.getOutputStream() , result );
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
	 * 查询某个id下事件的相应数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryEventDeclar", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> queryEventDeclar(HttpServletRequest request) {
		List<Map<String, Object>> maps = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "paramInfo" ) );
			maps = pollutVoltCurrPowerService.queryEventDeclar( map );
			if (null != maps) {
				processResult(maps);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maps;
	}
	
	/**
	 * 处理查询结果,计算是否逾期
	 * @return
	 */
	private void processResult(List<Map<String, Object>> maps){
		for (Map<String,Object> map:maps) {
			//再判断一下是否逾期(这里判断是已经申报了的部分)
			if (map.containsKey( "EVENT_START_TIME" ) && map.containsKey( "DECLARE_TIME" )) {
				Date eventStartTime = com.linyang.energy.utils.DateUtil.parseDate( map.get( "EVENT_START_TIME" ).toString(), com.linyang.energy.utils.DateUtil.DEFAULT_PATTERN );
				Date declareTime = com.linyang.energy.utils.DateUtil.parseDate( map.get( "DECLARE_TIME" ).toString(), com.linyang.energy.utils.DateUtil.DEFAULT_PATTERN );
				if(com.linyang.energy.utils.DateUtil.daysBetweenDates(declareTime,eventStartTime) > Integer.parseInt( PropertyUtil.getProperty( "timeOut", "3").trim() )){
					map.put( "OVER_DUE" , 1 ); // 替换掉map里面的逾期标识符
				} else {
					map.put( "OVER_DUE" , 0 );
				}
			}
			//这里需要判断一下没有申报的
			if(!map.containsKey( "DECLARE_TIME" )){
				if (map.containsKey( "EVENT_START_TIME" )) {
					Date eventStartTime = com.linyang.energy.utils.DateUtil.parseDate( map.get( "EVENT_START_TIME" ).toString(), com.linyang.energy.utils.DateUtil.DEFAULT_PATTERN );
					if(com.linyang.energy.utils.DateUtil.daysBetweenDates(new Date(),eventStartTime) > Integer.parseInt( PropertyUtil.getProperty( "timeOut", "3").trim() )){
						map.put( "OVER_DUE" , 1 ); // 替换掉map里面的逾期标识符
					} else {
						map.put( "OVER_DUE" , 0 );
					}
				} else {
					map.put( "OVER_DUE" , 0 );
				}
			}
		}
	}
	
	/**
	 * 字符串日期转换成需要的时间类型
	 * @return
	 */
	private static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		ParsePosition pos = new ParsePosition( 0 );
		Date strtodate = formatter.parse( strDate, pos );
		return strtodate;
	}
}
