package com.linyang.energy.controller;

import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.PullResultBean;
import com.linyang.energy.service.PullService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.service.impl.PullServiceImpl;
import com.linyang.energy.utils.CipherUtil;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.ws.pullwebService.ResultBean;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:42 2019/11/27
 * @ Description：远程拉合闸
 * @ Modified By：:catkins.
 * @Version: V4.8
 */
@Controller
@RequestMapping("/pullSwitch")
public class PullController extends BaseController {
	
	@Autowired
	private PullService pullService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 跳转至拉合闸页面
	 * @author catkins.
	 * @param
	 * @return org.springframework.web.servlet.ModelAndView
	 * @exception
	 * @date 2019/11/27 13:44
	 */
	@RequestMapping(value="/pullPage")
	public ModelAndView gotoPullPage() {
		return new ModelAndView("/energy/pullSwitch/pull_switch");
	}
	
	/**
	 * 拉合闸发送
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/11/27 13:46
	 */
	@RequestMapping(value = "/pullTheSwitch" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> pullTheSwitch(HttpServletRequest request){
		JSONObject json = JSONObject.fromObject(request.getParameter( "paramInfo" ));
		Map<String, Object> params = this.processParam( json );
		Map<String,Object> resultMap = pullService.pullTheSwicth( params,request );
//		List<PullResultBean> resultList = (List<PullResultBean>)resultMap.get( "resultList" );
//		String logStr = resultMap.get( "logStr" ).toString();
//
//		//记录日志
//		StringBuilder sb = new StringBuilder();
//		sb.append("pullSwitch:")
//				.append(" by ").
//				append(super.getSessionUserInfo(request).getLoginName()).
//				append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
//		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, 144, "拉合闸", CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, 1 ,sb.toString()+";"+logStr), request);
		
		long operItemId = OperItemConstant.OPER_ITEM_125;
		if( params.get( "switchType" ).toString().equals( "2" ) ){
			operItemId = OperItemConstant.OPER_ITEM_126;
		}
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), operItemId, 144l, 1);
		return resultMap;
	}
	
	
	/**
	 * 对页面收到的参数进行组装
	 * @author catkins.
	 * @param json
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/12/6 13:58
	 */
	private Map<String,Object> processParam(JSONObject json){
		Map<String,Object> params = new HashMap<>( 0 );
		// 页面选择的测量点
		if(json.has( "meterIds" )){
			String meterIds = json.getString( "meterIds" );
			params.put( "meterIds" , meterIds );
		}
		// 拉合闸类型
		if(json.has( "switchType" )){
			String switchType = json.getString( "switchType" );
			params.put( "switchType" , switchType );
		}
		// 拉合闸延时时间
		if(json.has( "delayMins" )){
			String delayMins = json.getString( "delayMins" );
			params.put( "delayMins" , delayMins );
		}
		// 端口有效时间(现在时间+页面发送时间的毫秒)
		if(json.has( "expirdTimes" )){
			String expirdTimes = json.getString( "expirdTimes" );
			params.put( "expirdTimes" , expirdTimes );
		}
		// 拉合闸权限的用户名
		if(json.has( "userName" )){
			String userName = json.getString( "userName" );
			params.put( "userName" , userName );
		}
		// 拉合闸权限的密码
		if(json.has( "userpwd" )){
			String userpwd = json.getString( "userpwd" );
			params.put( "password" , CipherUtil.generatePasswordSha256( userpwd ) );
		}
		// 表控制密码
		if(json.has( "controlPassword" )){
			String controlPassword = json.getString( "controlPassword" );
			params.put( "controlPassword" , controlPassword );
		}
		// 表计类型(1.单相表  2.G表)
		if(json.has( "meterType" )){
			String meterType = json.getString( "meterType" );
			params.put( "meterType" , meterType );
		}
		return params;
	}
}
