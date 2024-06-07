package com.linyang.energy.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.common.ResponseCommon;
import com.linyang.common.web.common.StringEscapeEditor;
import com.linyang.common.web.page.Page;
import com.linyang.energy.mapping.authmanager.OptLogBeanMapper;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.JacksonUtils;

/**
 * 基础控制器
 * 其他控制器继承此控制器获得日期字段类型转换和防止XSS攻击的功能
 * 基础控制器
 *
 */
@Controller
@RequestMapping("/baseController")
public class BaseController{
	@Autowired
	private OptLogBeanMapper optLogBeanMapper;
	
	protected static final String loginSessionKey = "sessionInfo";
	/**
	 * json装换工具类
	 */
	protected JacksonUtils jacksonUtils = JacksonUtils.getInstance();
	
	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
		//com.linyang.util.CommonMethod.getAbsoluteFilePath("");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df,true));
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
	/**
	 * 获取session中的角色类型,如果session中不存在，则返回null
	 * @return
	 */
	public Integer getSessionRoleType(HttpServletRequest request){
		Object obj = request.getSession().getAttribute("roleType");
		if (obj == null || !(obj instanceof String) || ((String) obj).trim().length()==0) return null; Integer roleType = Integer.parseInt((String) obj);
		return roleType;
	}
	/**
	 * 函数功能说明  :记录日志
	 * @return  boolean
	 */
	public void writeLog(OptLogBean optLogBean,HttpServletRequest request) {
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
	 * 函数功能说明  :记录手机端日志
	 * @return  boolean
	 */
	public void writePhoneLog(OptLogBean optLogBean,HttpServletRequest request) {
		if(optLogBean != null){
			optLogBean.setOptlogId(SequenceUtils.getDBSequence());
			optLogBean.setOptIp(getIpAddr(request));
			if(optLogBean.getOptRemark().length()>170)
				optLogBean.setOptRemark(StringUtils.substring(optLogBean.getOptRemark(),0,170));
			optLogBeanMapper.writeLog(optLogBean);
		}
	}
	
	/**
	 * 从request中获得类型为字符串的参数值
	 * @param param_name 参数名
	 * @param default_value 参数值为NULL时的默认值
	 * @return 用法如: String endTime = getStrParam("endTime","");
	 */
	public String getStrParam(HttpServletRequest request,String param_name,String default_value){
		String paramName = request.getParameter(param_name); if (paramName != null && paramName.trim().length()>0) return paramName; else return default_value;
	}
	
	/**
	 * 从context.request中获得类型为长整型的参数值
	 * @param param_name 参数名
	 * @param default_value 参数值为NULL时的默认值
	 * @return 
	 */
	public long getLongParam(HttpServletRequest request,String param_name,long default_value){
		String paramName = request.getParameter(param_name); if (paramName != null && paramName.trim().length()>0) return Long.parseLong(paramName); else return default_value;
	}
	
	/**
	 * 从context.request中获得类型为整型的参数值
	 * @param param_name 参数名
	 * @param default_value 参数值为NULL时的默认值
	 * @return
	 */
	public int getIntParams(HttpServletRequest request,String param_name,int default_value){
		String paramName = request.getParameter(param_name); if (paramName != null && paramName.trim().length()>0) return Integer.parseInt(paramName); else return default_value;
	}
	
	/**
	 * 判断客户端语言是否是中文
	 * @return true 是, false 否
	 */
	public boolean isChineseLocal(HttpServletRequest request){		
		return ("zh".equalsIgnoreCase(request.getLocale().getLanguage())|| "zh_cn".equalsIgnoreCase(request.getLocale().getLanguage())) ? true : false;
	}
	
	/**
	 * 导出中文文件名需要转成ISO8859-1
	 * @param str  需要转码的字符串
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String convertEncoding(String str) throws UnsupportedEncodingException{
		String encodedStr = null; if (str != null && str.trim().length()>0) encodedStr = new String(str.getBytes("GB2312"), "ISO8859-1"); return encodedStr; 
	}
	
	/**
	 * 设置response contentType and 字符编码 输出JSON
	 * @param response
	 * @param json
	 * @throws IOException
	 */
	public  void printJSON(HttpServletResponse response,JSON json)throws IOException {
		ResponseCommon.printObject(response,json);
	}

	/**
	 * 输出JSON数据，Object不需要转换，方法内部自己转换
	 * @param response
	 * @param object
	 * @throws Exception
	 */
	public void printJSON(HttpServletResponse response,Object object) {
		try {ResponseCommon.printObject(response,JacksonUtils.getInstance().writerJavaObject2JSON(object));} catch (Exception e) {Log.info("saveSubscriptionInfo error IOException");}
	}

	/**
	 * 不负责转换object的类容，如果需要转换则自行转换
	 * @param response
	 * @param object
	 * @throws IOException
	 */
	public  void printObject(HttpServletResponse response,Object object)throws IOException {
		ResponseCommon.printObject(response, object);
	}

	/**
	 * 调用这个之前，请DataExportUtils.exportDatas2Excel(dataBean),这样页面就可以了
	 * 媒体返回类型为Excel
	 * @param response
	 * @param fileName 要导出的文件名称
	 * @throws IOException
	 */
	public  void responseExcel(HttpServletResponse response,String fileName) throws IOException {
		ResponseCommon.responseContentDisposition(response, fileName, "application/msexcel");
	}

	/**
	 * 返回文件
	 * @param response
	 * @param fileName 文件路径
	 * @throws IOException
	 */
	public  void responseFile(HttpServletResponse response,String fileName) throws IOException {
		ResponseCommon.responseContentDisposition(response, fileName,"application/octet-stream");
	}

	/**
	 * 调用这个之前，请DataExportUtils.exportDatas2CSV(dataBean),这样页面就可以了
	 * 媒体返回类型为csv
	 * @param response
	 * @param fileName 要导出的文件名称
	 * @throws IOException
	 */
	public  void responseCsv(HttpServletResponse response,String fileName)throws IOException {
		ResponseCommon.responseContentDisposition(response, fileName, "application/csv");
	}

	/**
	 * response ContentDisposition 以附件形式返回文件
	 * @param response
	 * @param fileName 文件名
	 * @param ContentType response ContentType HTTP内容类型
	 * @throws IOException
	 */
	public  void responseContentDisposition(HttpServletResponse response,String fileName, String ContentType) throws IOException {
		ResponseCommon.responseContentDisposition(response, fileName, ContentType);
	}

	/**
	 * 发送文本。使用UTF-8编码。
	 * @param response   HttpServletResponse
	 * @param text 发送的字符串
	 */
	public  void renderText(HttpServletResponse response,String text) {
		ResponseCommon.renderText(response, text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 * @param response  HttpServletResponse
	 * @param text  发送的字符串
	 */
	public  void renderJson(HttpServletResponse response,String text) {
		ResponseCommon.renderJson(response, text);
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 * @param response  HttpServletResponse
	 * @param text   发送的字符串
	 */
	public  void renderXml(HttpServletResponse response,String text) {
		ResponseCommon.renderXml(response, text);
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 * 
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public  void render(HttpServletResponse response,String contentType,String text) {
		ResponseCommon.render(response,contentType, text);
	}
	
	/**
	 * 转换得到excle导出的文件名，防止乱码
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public  String getExportExcelName(String fileName) throws IOException{
		return ResponseCommon.getExportExcelName(fileName);
	}
	
	/**
	 * 获取当前分页信息
	 * @param pageNo   当前页码
	 * @param pageSize 每页条数
	 * @return
	 */
	protected Page getCurrentPage(String pageNo, String pageSize){
		Page page = new Page();
		if (! StringUtils.isEmpty(pageNo) && ! StringUtils.isEmpty(pageSize)) {
			page.setPageIndex(Integer.parseInt(pageNo));
			page.setPageSize(Integer.parseInt(pageSize));
		}
		return page;
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
	
	public boolean validateData(Object object) {
		if (object == null)
			return false;
		if (object instanceof String && ((String) object).length()==0)
			return false;
		else if (object instanceof Integer  && ((Integer) object) == 0)
			return false;
		else if (object instanceof Long && ((Long) object) == 0)
			return false;
		return true;
	}
}
