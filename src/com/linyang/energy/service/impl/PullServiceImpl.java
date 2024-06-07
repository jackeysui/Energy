package com.linyang.energy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.linyang.energy.mapping.authmanager.OptLogBeanMapper;
import com.linyang.energy.mapping.pullSwitch.PullSwitchMapper;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.PullResultBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.PullService;
import com.linyang.energy.utils.CipherAES;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.ws.pullwebService.DataCollectionServiceServiceLocator;
import com.linyang.ws.pullwebService.DataCollectionWebService;
import com.linyang.ws.pullwebService.ResultBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.*;


/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:27 2019/11/27
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class PullServiceImpl implements PullService {
	
	@Autowired
	private PullSwitchMapper pullSwitchMapper;
	
	@Autowired
	private OptLogBeanMapper optLogBeanMapper;
	
	protected static final String loginSessionKey = "sessionInfo";
	
	/**
	 * 拉合闸发送
	 * @author catkins.
	 * @param param
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/12/6 14:17
	 */
	@Override
	public Map<String,Object> pullTheSwicth(Map<String,Object> param,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<>( 0 );
		Integer code = 200;
		
		List<PullResultBean> resultList = new ArrayList<>( 0 );
		DataCollectionServiceServiceLocator locator = new DataCollectionServiceServiceLocator();
		StringBuilder logStr = null;
		PullResultBean pullBean = null;
		try {
			DataCollectionWebService dataCollectionServicePort = locator.getDataCollectionServicePort();
			
			List<String> array= Arrays.asList(param.get( "meterIds" ).toString().split( "," ));
			for ( String meterId : array ) {
				logStr = new StringBuilder(  );
				pullBean = new PullResultBean();
				Map<String,Object> newParam = new HashMap<>( 0 );
				newParam.put( "switchType" , param.get( "switchType" ) );
				newParam.put( "delayMins" , param.get( "delayMins" ) );
				newParam.put( "expirdTimes" , System.currentTimeMillis()+Long.parseLong( param.get( "expirdTimes" ).toString() )*1000*60 );
				newParam.put( "password", param.get( "password" ) );
				newParam.put( "controlPassword", param.get( "controlPassword" ) );
				newParam.put( "meterType", param.get( "meterType" ) );
				Map<String, Object> map = pullSwitchMapper.queryMeterById( Long.parseLong( meterId ) );
				Map<String, Object> map2 = new HashMap<>( 0 );
				ResultBean resultBean = null;
				if (map != null) {
					newParam.put( "teraddr", map.get( "TERMINAL_ADDRESS" ) );
					newParam.put( "mpointindex", map.get( "MPOINT_INDEX" ) );
				
					String jsonStr = JSONObject.toJSONString( newParam );
					byte[] encrypt = CipherAES.encrypt( jsonStr, param.get( "password" ).toString().substring( 0,16 ) );
					jsonStr = Arrays.toString( encrypt );
					
					logStr.append( "采集点名称:" ).append( map.get( "METER_NAME" ) );
					
					resultBean =  dataCollectionServicePort.remoteSwitch( param.get( "userName" ).toString(), jsonStr.substring( 1,jsonStr.length()-1 ) ) ;
					map2 = pullSwitchMapper.queryLedger( Long.parseLong( map.get( "LEDGER_ID" ).toString() ) );
					
					pullBean.setMsg( resultBean.getMsg() );
					pullBean.setStatusCode( resultBean.getStatusCode() );
					pullBean.setLedgerName(map2.get( "LEDGER_NAME" ).toString());
					pullBean.setMeterId( Long.parseLong( map.get( "METER_ID" ).toString() ) );
					pullBean.setMeterName( map.get( "METER_NAME" ).toString() );
					pullBean.setTerminalId( Long.parseLong( map.get( "TERMINAL_ID" ).toString() ) );
					pullBean.setTerminalName( map.get( "TERMINAL_NAME" ).toString() );
					pullBean.setMpointId( Long.parseLong( map.get( "MPOINT_ID" ).toString() ) );
					pullBean.setMpointName( map.get( "MPOINT_NAME" ).toString() );
					resultList.add( pullBean );
					
					if( Integer.parseInt( param.get( "switchType" ).toString() ) == 1 ){
						logStr.append( " 拉闸->" ).append( resultBean.getMsg() ).append( ";" );
					} else {
						logStr.append( " 合闸->" ).append( resultBean.getMsg() ).append( ";" );
					}
				} else {
					Map<String, Object> meterData = pullSwitchMapper.queryMeterDataById( Long.parseLong( meterId ) );
					
					pullBean.setMsg( "终端不存在" );
					pullBean.setLedgerName( meterData.get( "LEDGER_NAME" ).toString());
					pullBean.setMeterId( Long.parseLong( meterData.get( "METER_ID" ).toString() ) );
					pullBean.setMeterName( meterData.get( "METER_NAME" ).toString() );
					pullBean.setTerminalName( "-" );
					pullBean.setMpointName( "-" );
					resultList.add( pullBean );
					
					if( Integer.parseInt( param.get( "switchType" ).toString() ) == 1 ){
						logStr.append( " 拉闸->" ).append( meterData.get( "LEDGER_NAME" ) ).append( ":" ).append( meterData.get( "METER_NAME" ) ).append( "  采集终端不存在;" );
					} else {
						logStr.append( " 合闸->" ).append( meterData.get( "LEDGER_NAME" ) ).append( ":" ).append( meterData.get( "METER_NAME" ) ).append( "  采集终端不存在;" );
					}
				}
				this.log(request,logStr.toString());
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			logStr = new StringBuilder(  );
			if( Integer.parseInt( param.get( "switchType" ).toString() ) == 1 ){
				logStr.append( " 拉闸->" ).append( "操作失败" ).append( ";" );
			} else {
				logStr.append( " 合闸->" ).append( "操作失败" ).append( ";" );
			}
			this.log(request,logStr.toString());
			code = 400;
		} catch (Exception e1){
			e1.printStackTrace();
		}
		result.put( "resultList", resultList );
		result.put( "code",code );
		return result;
	}
	
	
	/**
	 * 对参数进行第二次处理
	 * @author catkins.
	 * @param param
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/12/6 14:19
	 */
	private List<String> processParam(Map<String,Object> param) throws Exception{
		List<String> list = new ArrayList<>( 0 );
		List<String> array= Arrays.asList(param.get( "meterIds" ).toString().split( "," ));
		for ( String meterId : array ) {
			Map<String,Object> newParam = new HashMap<>( 0 );
			newParam.put( "switchType" , param.get( "switchType" ) );
			newParam.put( "delayMins" , param.get( "delayMins" ) );
			newParam.put( "expirdTimes" , System.currentTimeMillis()+Long.parseLong( param.get( "expirdTimes" ).toString() )*1000*60 );
			newParam.put( "password", param.get( "password" ) );
			newParam.put( "controlPassword", param.get( "controlPassword" ) );
			newParam.put( "meterType", param.get( "meterType" ) );
			Map<String, Object> map = pullSwitchMapper.queryMeterById( Long.parseLong( meterId ) );
			newParam.put( "meteraddr", map.get( "AMMETER_ADDRESS" ) );
			String jsonStr = JSONObject.toJSONString( newParam );
			byte[] encrypt = CipherAES.encrypt( jsonStr, param.get( "password" ).toString().substring( 0,16 ) );
			list.add( Arrays.toString( encrypt ) );
		}
		return list;
	}
	
	
	/**
	 * 函数功能说明  :记录日志
	 * @return  boolean
	 */
	public void writeLog(OptLogBean optLogBean, HttpServletRequest request) {
		if(optLogBean != null){
			optLogBean.setOptlogId(SequenceUtils.getDBSequence());
			optLogBean.setOptIp(getIpAddr(request));
			UserBean sessionUserInfo = this.getSessionUserInfo(request);
			optLogBean.setOptId(sessionUserInfo.getAccountId());
			optLogBean.setOptName(sessionUserInfo.getLoginName());
			if(optLogBean.getOptRemark().length()>170)
				optLogBean.setOptRemark(StringUtils.substring(optLogBean.getOptRemark(),0,170));
			optLogBeanMapper.writeLog(optLogBean);
		}
	}
	
	
	/**
	 * 获取ip地址
	 * @return
	 */
	public  String getIpAddr(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.trim().length()==0 || "unknown".equalsIgnoreCase(ip.trim())) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.trim().length()==0 || "unknown".equalsIgnoreCase(ip.trim())) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.trim().length()==0 || "unknown".equalsIgnoreCase(ip.trim())) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.trim().length()==0) return ""; else return ip;		//为了代码检查, 没办法
	}
	
	/**
	 * 获取session中的用户信息,如果session中不存在，则返回null
	 * @return
	 */
	public UserBean getSessionUserInfo(HttpServletRequest request){
		UserBean user = null;
		if(request.getSession() != null){
			user = ((UserBean) request.getSession().getAttribute(loginSessionKey));
		}
		return user;
	}
	
	
	public void log(HttpServletRequest request,String logStr){
		//记录日志
		StringBuilder sb = new StringBuilder();
		sb.append("pullSwitch:")
				.append(" by ").
				append(this.getSessionUserInfo(request).getLoginName()).
				append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		this.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, 144, "拉合闸", CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, 1 ,sb.toString()+";"+logStr), request);
	}
	
}
