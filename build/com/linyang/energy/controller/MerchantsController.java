package com.linyang.energy.controller;

import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.model.MerchantBean;
import com.linyang.energy.service.MerchantsService;
import com.linyang.energy.service.TreeSetService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.util.CommonMethod;
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
import java.text.ParseException;
import java.util.*;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 10:38 2019/7/15
 * @ Description：商户报表
 * @ Modified By：:dingy
 * @Version: V4.6
 */
@Controller
@RequestMapping("/merchants")
public class MerchantsController extends BaseController{
	
	@Autowired
	private TreeSetService treeSetService;
	
	@Autowired
	private MerchantsService merchantsService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	@RequestMapping(value="/merchantPage")
	public ModelAndView gotoMerchantPage(HttpServletRequest request) {
		//拿一个月的时间
		Date baseTime = DateUtil.getSomeDateInYear(new Date(), -1);
		try {
			request.setAttribute("endTime", DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SIMPLE_PATTERN));
			Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
			List<Map<String, Object>> dataList = treeSetService.getCompanyList(ledgerId, 103);//商户
			request.setAttribute("dataList", dataList);
		} catch (ParseException e) {
			Log.error("MerchantsController.gotoMerchantPage()-----日期转换出错");
		}
		return new ModelAndView("/energy/merchants/merchant_report");
	}
	
	/**
	 * 得到商户报表的表格数据
	 * @author catkins
	 * @param request
	 * @return java.util.List<com.linyang.energy.model.MerchantBean>
	 * @exception
	 * @date 2019/7/18 10:45
	 */
	@RequestMapping(value = "/queryTabList" , method = RequestMethod.POST )
	public @ResponseBody Map<String,Object> queryTabList(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>( 0 );
		try {
			long parLedgerId = super.getSessionUserInfo(request).getLedgerId();
			JSONObject json = JSONObject.fromObject(request.getParameter( "paramInfo" ));
			Map<String, Object> map = this.populateParam( json );
//			List<Long> ledgerIds = (List<Long>) map.get( "ledgerIds" );
//			for (long ledgerId:ledgerIds) {
			result = merchantsService.getMerchants( DateUtil.getCalculateMonth(DateUtil.convertStrToDate(map.get( "beginTime" ).toString(), DateUtil.DEFAULT_SHORT_PATTERN),-1),
						DateUtil.convertStrToDate(map.get( "endTime" ).toString(), DateUtil.DEFAULT_SHORT_PATTERN), Long.parseLong( map.get( "ledgerId" ).toString()  ),
						Integer.parseInt( map.get( "dataType" ).toString() ),Integer.parseInt( map.get( "instead" ).toString() ) ,
						DateUtil.convertStrToDate(map.get( "dateTime" ).toString(), DateUtil.DEFAULT_SIMPLE_PATTERN),parLedgerId ,
						map.get( "userNo" ).toString() ,Integer.parseInt( map.get( "payStatus" ).toString() ),map.get( "ledgerName" ).toString()) ;
//			}
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_111, 237l, 1);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 查询上级能管对象等级
	 * @author catkins
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/7/18 15:08
	 */
	@RequestMapping(value = "/queryLevel" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryLevel(HttpServletRequest request){
		Map<String,Object> levelMap = new HashMap<>( 0 );
		String ledgerId = request.getParameter( "ledgerId" );
		long parLedgerId = super.getSessionUserInfo(request).getLedgerId();
		Integer level = merchantsService.queryLevel( Long.parseLong( ledgerId ) ,parLedgerId );
		levelMap.put( "level" , level );
		return levelMap;
	}
	
	/**
	 * 确认缴费,修改缴费状态(实则为新增一条上月数据)
	 * @author catkins
	 * @param request
	 * @return boolean
	 * @exception
	 * @date 2019/7/22 16:02
	 */
	@RequestMapping(value = "/payCost" ,method = RequestMethod.POST)
	public @ResponseBody boolean payCost(HttpServletRequest request){
		int i = 0;
		try {
			String ledgerId = request.getParameter( "ledgerId" );
			String dateTime = request.getParameter( "dateTime" );
			i = merchantsService.doPayCost( Long.parseLong( ledgerId ),
					com.linyang.energy.utils.DateUtil.getNextMonthFirstDay(DateUtil.convertStrToDate(dateTime, DateUtil.DEFAULT_SIMPLE_PATTERN)) );
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(i >= 1){
			return true;
		}else {
			return false;
		}
	}
	
	
	
	/**
	 * 处理得到的查询参数
	 * @author catkins
	 * @param json
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/7/18 10:57
	 */
	private Map<String,Object> populateParam(JSONObject json)throws Exception{
		Map<String,Object> params =new HashMap<>( 0 );
		Map<String, Object> calcData  = new HashMap<>( 0 );
		//获取选中的能管对象id
		if (json.has("ledgerId")) {
			String ledgerId = json.getString("ledgerId");
			params.put( "ledgerId",ledgerId);
//			String[] ledgerIdAry = ledgerId.split(",");
//			params.put( "ledgerIds", convertString2Long(ledgerIdAry) );
			calcData = merchantsService.queryCalcData( Long.parseLong( params.get( "ledgerId" ).toString() ) ,0);
		}
		
		//开始时间
		String beginTimeStr = "";
		if (json.has("beginTime")) {
			beginTimeStr = json.getString("beginTime");
			if(calcData.get( "QCALCDATE" ).toString().length() == 1)
				beginTimeStr += "-0"+calcData.get( "QCALCDATE" ).toString();
			else
				beginTimeStr += "-"+calcData.get( "QCALCDATE" ).toString();
			params.put("beginTime", beginTimeStr);
		}
		//结束时间
		String endTimeStr = "";
		if (json.has("endTime")) {
			endTimeStr = json.getString("endTime");
			params.put("endTime", endTimeStr);
		} else {
			endTimeStr = json.getString("beginTime");
			if(calcData.get( "QCALCDATE" ).toString().length() == 1)
				endTimeStr += "-0"+calcData.get( "QCALCDATE" ).toString();
			else
				endTimeStr += "-"+calcData.get( "QCALCDATE" ).toString();
			params.put("endTime", endTimeStr);
		}
		//查询类型(1.电,2.水,3.气)
		if(json.has( "dataType" )){
			String dataType = json.getString( "dataType" );
			params.put( "dataType" , dataType );
		}
		//展示几级能管对象父节点
		if(json.has( "level" )){
			String level = json.getString( "level" );
			params.put( "level" , level );
		}
		//用之前最近的数据还是之后数据进行替代缺失数据的标识
		if(json.has( "instead" )){
			String instead = json.getString( "instead" );
			params.put( "instead" , instead );
		}
		String dateTime = "";
		if(json.has( "dateTime" )){
			dateTime = json.getString("dateTime");
			params.put("dateTime", dateTime);
		}
		//用户号
		if(json.has( "userNo" )){
			String userNo = json.getString( "userNo" );
			params.put( "userNo" , userNo.trim() );
		}
		//缴费状态	0全部,1已缴费,2未缴费
		if(json.has( "payStatus" )){
			String payStatus = json.getString( "payStatus" );
			params.put( "payStatus" , payStatus );
		}
		//最下级能管对象名称
		if(json.has( "ledgerName" )){
			String ledgerName = json.getString( "ledgerName" );
			params.put( "ledgerName" , ledgerName.trim() );
		}
		return params;
	}
	
	/**
	 *转换成List<Long>
	 * @param strArray
	 * @return
	 */
	private List<Long> convertString2Long(String[] strArray){
		List<Long> StringList = new ArrayList<Long>();
		if(CommonMethod.isArrayNotEmpty(strArray)){
			for (String string : strArray) {
				StringList.add(Long.valueOf(string));
			}
		}
		return StringList;
	}
	
	/**
	 * 导出EXCEL
	 * @return
	 */
	@RequestMapping(value = "/reportMerExcel")
	public @ResponseBody void reportMerExcel(HttpServletRequest request , HttpServletResponse response ) {
		try {
			
			long parLedgerId = super.getSessionUserInfo(request).getLedgerId();
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> map=null;
			if(json!=null && json.toString().length()>0){
				map = this.populateParam(json);
			}
//			System.out.println(map.toString());
			Map<String, Object> result = null;
			if(map!=null && map.toString().length()>0){
				result = merchantsService.getMerchants( DateUtil.convertStrToDate(map.get( "beginTime" ).toString(), DateUtil.DEFAULT_SHORT_PATTERN),
						DateUtil.convertStrToDate(map.get( "endTime" ).toString(), DateUtil.DEFAULT_SHORT_PATTERN), Long.parseLong( map.get( "ledgerId" ).toString()  ),
						Integer.parseInt( map.get( "dataType" ).toString() ),Integer.parseInt( map.get( "instead" ).toString() ) ,
						DateUtil.convertStrToDate(map.get( "dateTime" ).toString(), DateUtil.DEFAULT_SIMPLE_PATTERN),parLedgerId,
						map.get( "userNo" ).toString() ,Integer.parseInt( map.get( "payStatus" ).toString() ),map.get( "ledgerName" ).toString()) ;
			}
			String title = "商户报表";
			byte[] bs = title.getBytes();
			title = new String(bs, "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+title+".xls");	//指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			merchantsService.getExcel("Cache.xls", response.getOutputStream(), result ,  Integer.parseInt( map.get( "dataType" ).toString() ),
					map.get( "beginTime" ).toString(),map.get( "endTime" ).toString());
			
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_112, 237l, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
