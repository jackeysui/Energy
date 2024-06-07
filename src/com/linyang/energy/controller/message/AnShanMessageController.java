package com.linyang.energy.controller.message;/*
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  - /// |    |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.'  >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 *
 *
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *            佛祖保佑     永不宕机     永无BUG
 */

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.ServiceReportBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.AnShanMessageService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.PhoneService;
import com.linyang.energy.utils.*;
import com.linyang.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import com.linyang.energy.utils.ImgUtils;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:20 2020/11/27
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/anShanMessage")
public class AnShanMessageController extends BaseController {
	
	@Autowired
	private AnShanMessageService anShanMessageService;
	
	@Autowired
	private LedgerManagerService ledgerManagerService;
	
	@Autowired
	private PhoneService phoneService;
	
	/**
	 * 数据保留位数
	 */
	private final String KEEP_FEW = "0.00";
	
	/**
	 * 冶炼行业服务报告列表
	 * @return
	 */
	@RequestMapping("/showServiceReportListPage")
	public ModelAndView showServiceReportListPage(HttpServletRequest request){
		//TODO:鞍山冶炼行业服务报告列表页面
		Calendar cal = Calendar.getInstance();
		cal.setTime(WebConstant.getChartBaseDate());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		String endDate = year + "-" + month;
		cal.add(Calendar.MONTH, -1);
		int year2 = cal.get(Calendar.YEAR);
		int month2 = cal.get(Calendar.MONTH) + 1;
		String beginDate = year2 + "-" + month2;
		
		// 测试用时间
//		endDate = "2016-05";
//		beginDate = "2016-05";
		
		//开始时间
		request.setAttribute("beginDate", beginDate);
		//结束时间
		request.setAttribute("endDate", endDate);
		
		int isAdmin = 0;
		UserBean userBean = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (userBean.getLedgerId() == 0L) {
			//权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(userBean.getAccountId());
		} else {
			ledgerId = userBean.getLedgerId();
		}
		LedgerBean ledger = ledgerManagerService.selectByLedgerId(ledgerId);
		if(ledger.getAnalyType()==null || ledger.getAnalyType()!=102){
			isAdmin = 1;
		}
		
		request.setAttribute("isAdmin", isAdmin);
		return new ModelAndView("/energy/message/lm_service_report_list");
	}
	
	/**
	 * 得到服务报告列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchSerivceReport")
	public @ResponseBody
	Map<String, Object> searchSerivceReport(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Long selectLedgerId = getLongParam(request, "selectLedgerId", 0);
		Integer ledgerType = getIntParams(request, "ledgerType", 0);
		String beginDateStr = getStrParam(request, "beginDate", "") + "-01";
		String endDateStr = getStrParam(request, "endDate", "") + "-01";
		//得到该日期的第一天
		Date beginDate = DateUtil.getNextMonthFirstDay(DateUtils.convertDateStr2Date(beginDateStr, DateUtils.FORMAT_SHORT));
		//得到这个月的最后一天
		Date endDate = DateUtil.getMonthLastDay(DateUtil.getNextMonthFirstDay(DateUtils.convertDateStr2Date(endDateStr, DateUtils.FORMAT_SHORT)));
		
		Page page = super.getCurrentPage(request.getParameter("pageIndex"), request.getParameter("pageSize"));
		result.put("page", page);
		
		//企业用户只能查看自己的服务报告，并且是已经推送状态的
		Integer status = getIntParams(request, "status", 0);
		Integer isAdmin = getIntParams(request, "isAdmin", 1);
		if(isAdmin != 1){ //企业用户
			status = ServiceReportBean.STATUS_PUSH;
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userLedgerId", super.getSessionUserInfo(request).getLedgerId());
		param.put("userAccountId", super.getSessionUserInfo(request).getAccountId());
		param.put("selectLedgerId", selectLedgerId);
		param.put("ledgerType", ledgerType);
		param.put("beginDate", beginDate);
		param.put("endDate", endDate);
		param.put("status", status);
		param.put(Dialect.pageNameField, page);
		List<ServiceReportBean> list = anShanMessageService.searchSerivceReport(param);
		result.put("list", list);
		//记录用户足迹
//		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_144, 122l, 1);
		return result;
	}
	
	/**
	 * 冶炼行业服务报告页面
	 * @return
	 */
	@RequestMapping("/pushServiceReportPage")
	public ModelAndView pushServiceReportPage(HttpServletRequest request){
		long reportId = getLongParam(request, "reportId", 0);
		request.setAttribute("reportId", reportId);
		return new ModelAndView("/energy/message/lm_service_report_push");
	}
	
	/**
	 * 获取服务报告需要的数据
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryReportInfoData2")
	public @ResponseBody Map<String, Object> queryReportInfoData2(HttpServletRequest request, long reportId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Long accountId = super.getSessionUserInfo(request).getLedgerId();
		map = anShanMessageService.queryReportInfoData(reportId,accountId);
		String tips = anShanMessageService.getReportTips(reportId, accountId);
		map.put("tips", tips);
		return map;
		
	}
	
	
	/**
	 * 获取服务报告需要的数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryReportInfoData3")
	public @ResponseBody Map<String, Object> queryReportInfoData3(HttpServletRequest request, long reportId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Long accountId = super.getSessionUserInfo(request).getLedgerId();
		List<Map<String, Object>> lm = this.phoneService.queryTradeBenData( "LM" );
		map.put( "datas",anShanMessageService.queryReportInfoData2(reportId,accountId) );
		map.put( "tradeBen",lm );
		return map;
	}
	
	
	/**
	 * 服务报告导出word
	 * 谐波电流图片编号从 1500开始 谐波电压图片编号从 2000开始   企业设备能效图片可以默认是55
	 * 负载率曲线 300开始 负载率时间曲线 400开始 电流不平衡曲线 500开始 电流不平衡度最大值发生时间 600开始 电流不平衡度越限日累计时间 700开始
	 * 电压不平衡度曲线 800开始 电压不平衡最大值发生时间 900开始  电压不平衡度越限日累计时间 1000开始
	 * 图片编号需要修改 report4.tfl文件里的 <pkg:part pkg:name="/word/media/image5.png" pkg:contentType="image/png"></pkg:part> 的pkg:name="/word/media/image5.png"
	 * 以及 <Relationship Id="rId18" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/image15.png"/> 的Id="rId18"和Target="media/image15.png"
	 * 这个功能还需要修改拦截器,以及tomcat上传文件的最大长度
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toWord")
	public @ResponseBody Boolean toWord(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		Map<String, Object> params = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
		String type = "report";
		if(params.containsKey("reportId")){
			Map<String, Object> map = new HashMap<String, Object>();
			Long accountId = super.getSessionUserInfo(request).getLedgerId();
			
			map = anShanMessageService.queryReportInfoData(Long.parseLong(params.get("reportId").toString()),accountId);
			map.putAll( anShanMessageService.queryReportInfoData2(Long.parseLong(params.get("reportId").toString()),accountId) );
			
//			List<Map<String, Object>> childList = (List<Map<String, Object>>) map.get("childList");
//			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//			double qTotal = 0;
//			for (int i = 0; i < childList.size(); i++) {
//				qTotal = DataUtil.doubleAdd(qTotal, Double.parseDouble(childList.get(i).get("Q").toString()));
//			}
//			for (int i = 0; i < childList.size(); i++) {
//				Map<String, Object> childMap = new HashMap<String, Object>();
//				childMap.put("name", childList.get(i).get("LEDGER_NAME"));
//				double q = Double.parseDouble(childList.get(i).get("Q").toString());
//				childMap.put("Q", Math.round(q));
//				childMap.put("Qpercent", new BigDecimal(q).multiply(new BigDecimal(100)).divide(new BigDecimal(qTotal), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
//				list.add(childMap);
//			}
			
			List<Map<String, Object>> powerData = (List<Map<String, Object>>) map.get("powerData");
			List<Map<String, Object>> powerList = new ArrayList<Map<String,Object>>();
			Map<String, Object> powerMap = new HashMap<String, Object>();
			for (int i = 0; i < ((Math.floor((powerData.size() - 1) / 24) + 1) * 24); i++) {
				if (i >= powerData.size() || powerData.get( i ) == null ) {
					powerMap.put( "kWD"+i , "-");
					powerMap.put( "kWA"+i , "-");
					powerMap.put( "kWB"+i , "-");
					powerMap.put( "kWC"+i , "-");
				} else {
					if ( powerData.get( i ).containsKey( "D" ) ) {
						powerMap.put( "kWD"+i , keepDigits(powerData.get( i ).get( "D" ).toString(),KEEP_FEW));
					} else {
						powerMap.put( "kWD"+i , "-");
					}
					if ( powerData.get( i ).containsKey( "A" ) ) {
						powerMap.put( "kWA"+i , keepDigits(powerData.get( i ).get( "A" ).toString(),KEEP_FEW));
					} else {
						powerMap.put( "kWA"+i , "-");
					}
					if ( powerData.get( i ).containsKey( "B" ) ) {
						powerMap.put( "kWB"+i , keepDigits(powerData.get( i ).get( "B" ).toString(),KEEP_FEW));
					} else {
						powerMap.put( "kWB"+i , "-");
					}
					if ( powerData.get( i ).containsKey( "C" ) ) {
						powerMap.put( "kWC"+i , keepDigits(powerData.get( i ).get( "C" ).toString(),KEEP_FEW));
					} else {
						powerMap.put( "kWC"+i , "-");
					}
				}
			}
			powerList.add(powerMap);
			
			// 处理一下页面拿到的谐波电流数据
			if( params.containsKey( "harIParams" ) ){
				Map<String, Object> harMap1 = (Map<String, Object>) map.get("harMap1");
				//页面上拿到的数据
				List<Map<String,Object>> harIParams = (List<Map<String,Object>>)params.get( "harIParams" );
				this.assessmentWordHar(harMap1,harIParams);
				params.put( "table3", harIParams);
			}
			// 处理一下页面拿到的谐波电压数据
			if( params.containsKey( "harVParams" ) ){
				Map<String, Object> harMap1 = (Map<String, Object>) map.get("harMap2");
				//页面上拿到的数据
				List<Map<String,Object>> harVParams = (List<Map<String,Object>>)params.get( "harVParams" );
				this.assessmentWordHar(harMap1,harVParams);
				params.put( "table4", harVParams);
			}
			// 行业能效情况
			Map<String, Object> industryEData = (Map<String, Object>) map.get("industryEData");
			this.assessmentWordEdata(industryEData);
			
			// 企业设备能效情况
			List<Map<String, Object>> meterEDatas = (List<Map<String, Object>>) map.get("meterEDatas");
			
			
			params.put( "startDate",DateUtil.convertDateToStr((Date)map.get( "startDate" ),DateUtil.SHORT_PATTERN) );
			params.put( "endDate",DateUtil.convertDateToStr((Date)map.get( "endDate" ),DateUtil.SHORT_PATTERN) );
			params.put( "meterEDatas",meterEDatas );
			params.put( "industryEData",industryEData );
			params.put( "table2", powerList);
//			params.put("table1", list);
			type = "report3";
		}
		
		String month = params.get("month").toString();
		String fileName = "月服务报告";
		
		File file = null;
		InputStream fin = null;
		OutputStream out = null;
		try {  // 调用工具类WordGenerator的createDoc方法生成Word文档
			file = WordGenerator.createDoc(params, type);
			fin = new FileInputStream(file);
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/msword");
			// 设置浏览器以下载的方式处理该文件默认名
			response.setHeader("Content-Disposition", "attachment;filename="+month+new String(fileName.getBytes(), "ISO-8859-1")+".doc");
			
			out = response.getOutputStream();
			byte[] buffer = new byte[512];  // 缓冲区
			int bytesToRead = -1;
			// 通过循环将读入的Word文件的内容输出到浏览器中
			while((bytesToRead = fin.read(buffer)) != -1) {
				out.write(buffer, 0, bytesToRead);
			}
			return true;
		} finally {
			CloseHander(file,fin,out);
		}
	}
	
	private void CloseHander(File file,InputStream fin,OutputStream out){
		try{
			if(fin != null) {fin.close(); fin = null;}
			if(out != null) {out.close();}
			if(file != null){
				if(!file.delete()){
					Log.error("delete file fail");
				}
			}}catch(IOException ex){Log.error("Close Hander"+ex.getMessage());}
	}
	
	/**
	 * 保留两位小数
	 * @author catkins
	 * @param dataNumber
	 * @return java.lang.String
	 * @exception
	 * @date 2019/7/29 13:33
	 */
	private String keepDigits(String dataNumber,String pattern) {
		double num = Double.parseDouble( dataNumber );
		DecimalFormat df = null;
		if(pattern == null || pattern.equals( "" ))
			df = new DecimalFormat("#0.0000");
		else
			df = new DecimalFormat(pattern);
		return df.format( num );
	}
	
	/**
	 * 处理word需要的谐波数据
	 * @param harMap		查询得到的谐波数据
	 * @param harParams		页面返回的谐波数据
	 */
	private void assessmentWordHar(Map<String,Object> harMap,List<Map<String,Object>> harParams){
		for (int i = 0; i < harParams.size(); i++) {
			String meterName =  harParams.get( i ).get( "harName" ).toString();
			List<Map<String, Object>> harList = (List<Map<String, Object>>)harMap.get( meterName );
			for (Map<String,Object> har:harList) {
				if( Integer.parseInt( har.get( "num" ).toString() ) == 0 ){
					har.put( "num","总畸变" );
					if( !har.containsKey( "a_time" ) )
						har.put( "a_time","" );
					else
						har.put( "a_time",DateUtil.convertDateToStr( (Date)har.get( "a_time" ),DateUtil.DEFAULT_PATTERN ) );
					if( !har.containsKey( "b_time" ) )
						har.put( "b_time","" );
					else
						har.put( "b_time",DateUtil.convertDateToStr( (Date)har.get( "b_time" ),DateUtil.DEFAULT_PATTERN ) );
					if( !har.containsKey( "c_time" ) )
						har.put( "c_time","" );
					else
						har.put( "c_time",DateUtil.convertDateToStr( (Date)har.get( "c_time" ),DateUtil.DEFAULT_PATTERN ) );
					
					if( !har.containsKey( "a_max" ) )
						har.put( "a_max","" );
					else if( har.get( "a_max" ).toString().indexOf( "_" ) > 0 )
						har.put( "a_max",har.get( "a_max" ).toString().substring( 0,har.get( "a_max" ).toString().lastIndexOf( "_" ) ) );
					if( !har.containsKey( "b_max" ) )
						har.put( "b_max","" );
					else if( har.get( "b_max" ).toString().indexOf( "_" ) > 0 )
						har.put( "b_max",har.get( "b_max" ).toString().substring( 0,har.get( "b_max" ).toString().lastIndexOf( "_" ) ) );
					if( !har.containsKey( "c_max" ) )
						har.put( "c_max","" );
					else if( har.get( "c_max" ).toString().indexOf( "_" ) > 0 )
						har.put( "c_max",har.get( "c_max" ).toString().substring( 0,har.get( "c_max" ).toString().lastIndexOf( "_" ) ) );
					continue;
				}
				if( Integer.parseInt( har.get( "num" ).toString() ) != 0 ){
					if( !har.containsKey( "a_time" ) )
						har.put( "a_time","" );
					else
						har.put( "a_time",DateUtil.convertDateToStr( (Date)har.get( "a_time" ),DateUtil.DEFAULT_PATTERN ) );
					if( !har.containsKey( "b_time" ) )
						har.put( "b_time","" );
					else
						har.put( "b_time",DateUtil.convertDateToStr( (Date)har.get( "b_time" ),DateUtil.DEFAULT_PATTERN ) );
					if( !har.containsKey( "c_time" ) )
						har.put( "c_time","" );
					else
						har.put( "c_time",DateUtil.convertDateToStr( (Date)har.get( "c_time" ),DateUtil.DEFAULT_PATTERN ) );
					
					if( !har.containsKey( "a_max" ) )
						har.put( "a_max","" );
					else if( har.get( "a_max" ).toString().indexOf( "_" ) > 0 )
						har.put( "a_max",har.get( "a_max" ).toString().substring( 0,har.get( "a_max" ).toString().lastIndexOf( "_" ) ) );
					if( !har.containsKey( "b_max" ) )
						har.put( "b_max","" );
					else if( har.get( "b_max" ).toString().indexOf( "_" ) > 0 )
						har.put( "b_max",har.get( "b_max" ).toString().substring( 0,har.get( "b_max" ).toString().lastIndexOf( "_" ) ) );
					if( !har.containsKey( "c_max" ) )
						har.put( "c_max","" );
					else if( har.get( "c_max" ).toString().indexOf( "_" ) > 0 )
						har.put( "c_max",har.get( "c_max" ).toString().substring( 0,har.get( "c_max" ).toString().lastIndexOf( "_" ) ) );
				}
			}
			harParams.get( i ).put("harList",harList);
			harParams.get( i ).put( "harName",harParams.get( i ).get( "harName" ).toString().substring( 0,harParams.get( i ).get( "harName" ).toString().indexOf( "," ) ) );
		}
	}
	
	/**
	 * 处理行业能效情况数据
	 * @param industryEData
	 */
	private void assessmentWordEdata(Map<String, Object> industryEData){
		String pwText = "";		//吨电耗文字说明
		String feeText = "";	//吨电费文字说明
		if( industryEData.containsKey( "AVGPW" ))
			industryEData.put( "AVGPW" , keepDigits(industryEData.get( "AVGPW" ).toString() , KEEP_FEW ) );
		if( industryEData.containsKey( "AVGFEE" ) )
			industryEData.put( "AVGFEE" , keepDigits( industryEData.get( "AVGFEE" ).toString() , KEEP_FEW ) );
		if( industryEData.containsKey( "MINPW" ) )
			industryEData.put( "MINPW" , keepDigits( industryEData.get( "MINPW" ).toString() , KEEP_FEW ) );
		if( industryEData.containsKey( "MINFEE" ) )
			industryEData.put( "MINFEE" , keepDigits( industryEData.get( "MINFEE" ).toString() , KEEP_FEW ) );
		if( industryEData.containsKey( "MAXPW" ) )
			industryEData.put( "MAXPW" , keepDigits( industryEData.get( "MAXPW" ).toString() , KEEP_FEW ) );
		if( industryEData.containsKey( "MAXFEE" ) )
			industryEData.put( "MAXFEE" , keepDigits( industryEData.get( "MAXFEE" ).toString() , KEEP_FEW ) );
		if( industryEData.containsKey( "MAVGPW" ) )
			industryEData.put( "MAVGPW" , keepDigits( industryEData.get( "MAVGPW" ).toString() , KEEP_FEW ) );
		if( industryEData.containsKey( "MAVGFEE" ) )
			industryEData.put( "MAVGFEE" , keepDigits( industryEData.get( "MAVGFEE" ).toString(),KEEP_FEW ) );
		if( industryEData.containsKey( "MMINPW" ) )
			industryEData.put( "MMINPW",keepDigits( industryEData.get( "MMINPW" ).toString(),KEEP_FEW ) );
		if( industryEData.containsKey( "MMINFEE" ) )
			industryEData.put( "MMINFEE",keepDigits( industryEData.get( "MMINFEE" ).toString(),KEEP_FEW ) );
		if( industryEData.containsKey( "MMAXPW" ) )
			industryEData.put( "MMAXPW",keepDigits( industryEData.get( "MMAXPW" ).toString(),KEEP_FEW ) );
		if( industryEData.containsKey( "MMAXFEE" ) )
			industryEData.put( "MMAXFEE",keepDigits( industryEData.get( "MMAXFEE" ).toString(),KEEP_FEW ) );
		if( industryEData.containsKey( "meterMAXPW" ) )
			industryEData.put( "meterMAXPW",keepDigits( industryEData.get( "meterMAXPW" ).toString(),KEEP_FEW ) );
		if( industryEData.containsKey( "meterMINPW" ) )
			industryEData.put( "meterMINPW",keepDigits( industryEData.get( "meterMINPW" ).toString(),KEEP_FEW ) );
		
		if ( industryEData.containsKey( "AVGPW" ) && industryEData.containsKey( "ledgerPW" ) ) {
			if( Double.parseDouble( industryEData.get( "AVGPW" ).toString() ) > Double.parseDouble( industryEData.get( "ledgerPW" ).toString() ) )
				pwText = "吨电耗："+keepDigits( industryEData.get( "ledgerPW" ).toString(),KEEP_FEW )+"千瓦时/吨，处于略优于行业平均水平。";
			else if( Double.parseDouble( industryEData.get( "AVGPW" ).toString() ) < Double.parseDouble( industryEData.get( "ledgerPW" ).toString() ) )
				pwText = "吨电耗："+keepDigits( industryEData.get( "ledgerPW" ).toString(),KEEP_FEW )+"千瓦时/吨，处于略差于行业平均水平。";
			else
				pwText = "吨电耗："+keepDigits( industryEData.get( "ledgerPW" ).toString(),KEEP_FEW )+"千瓦时/吨，处于行业平均水平。";
		} else {
			pwText = "吨电耗：-- 千瓦时/吨，处于 -- ";
		}
		
		if ( industryEData.containsKey( "AVGFEE" ) && industryEData.containsKey( "ledgerFEE" ) ) {
			if( Double.parseDouble( industryEData.get( "AVGFEE" ).toString() ) > Double.parseDouble( industryEData.get( "ledgerFEE" ).toString() ) )
				feeText = "吨电费："+keepDigits( industryEData.get( "ledgerFEE" ).toString(),KEEP_FEW )+"千瓦时/吨，处于略优于行业平均水平。";
			else if( Double.parseDouble( industryEData.get( "AVGFEE" ).toString() ) < Double.parseDouble( industryEData.get( "ledgerPW" ).toString() ) )
				feeText = "吨电费："+keepDigits( industryEData.get( "ledgerFEE" ).toString(),KEEP_FEW )+"千瓦时/吨，处于略差于行业平均水平。";
			else
				feeText = "吨电费："+keepDigits( industryEData.get( "ledgerFEE" ).toString(),KEEP_FEW )+"千瓦时/吨，处于行业平均水平。";
		} else {
			feeText = "吨电费：-- 千瓦时/吨，处于 -- 。";
		}
		industryEData.put( "pwText",pwText );
		industryEData.put( "feeText",feeText );
	}
	
	@RequestMapping( value = "/compressionImg" )
	public @ResponseBody String compressionImg(HttpServletRequest request){
		String base64string = request.getParameter( "base64string" );
		String s = compressionImage( base64string );
		return s;
	}
	
	
	private String compressionImage(String base64string){
		String imgBase64 = "";
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] fileByte = decoder.decodeBuffer(base64string);
			byte[] imgBytes = ImgUtils.compressPicForScale( fileByte ,40);
			imgBase64 = Base64.encodeBase64String(imgBytes);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return imgBase64;
	}
	
	
	
	

	
	
	
	
	
}
