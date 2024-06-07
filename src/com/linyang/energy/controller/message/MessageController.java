package com.linyang.energy.controller.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linyang.energy.model.*;

import com.linyang.energy.service.*;
import com.linyang.energy.utils.*;
import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.dto.EventRelatedNodeTreeBean;
import com.linyang.energy.model.EventSettingRecBean;
import com.linyang.energy.model.GroupBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.SubscriberBean;
import com.linyang.energy.model.SuggestBean;
import com.linyang.energy.model.UserBean;
import com.linyang.util.DateUtils;
//import com.sun.org.apache.bcel.internal.generic.RETURN;

/**
 * 消息订阅
 * @author guosen
 *
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private LedgerManagerService ledgerManagerService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 批量订阅页面
	 * @return
	 */
	@RequestMapping("/showBatchPage")
	public ModelAndView showBatchPage(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("eventTypeList", messageService.getAllEventTypeList());
		map.put("csrf", CSRFTokenManager.getTokenForSession(request.getSession()));
		return new ModelAndView(URLConstant.BATCH_ORDER,map);
	}
	/**
	 * 用户反馈页面
	 * @return
	 */
	@RequestMapping("/userContact")
	public ModelAndView userContact(HttpServletRequest request){
		return new ModelAndView(URLConstant.CUSTOMER_CONTACT);
	}
	/**
	 * 客户订阅页面
	 * @return
	 */
	@RequestMapping("/showCustomerPage")
	public ModelAndView showCustomerPage(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("eventTypeList", messageService.getAllEventTypeList());
		map.put("csrf", CSRFTokenManager.getTokenForSession(request.getSession()));
		return new ModelAndView(URLConstant.CUSTOMER_ORDER,map);
	}
	
	/**
	 * 自定义信息推送页面
	 * @return
	 */
	@RequestMapping("/showCustomPushPage")
	public ModelAndView showCustomPushPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.CUSTOM_PUSH);
	}
	
	/**
	 * 新闻政策发布页面
	 * @return
	 */
	@RequestMapping("/showPressPolicyPage")
	public ModelAndView showPressPolicyPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.PRESS_POLICY);
	}
	
	/**
	 * 服务报告信息页面
	 * @return
	 */
	@RequestMapping("/showServiceReportPage")
	public ModelAndView showServiceReportPage(HttpServletRequest request){
		request.setAttribute("baseDate", DateUtils.getCurrentTime("yyyy-MM-dd"));
		request.setAttribute("monthStr", DateUtils.getCurrentTime("yyyy-MM-dd").split("-")[1]);
		return new ModelAndView(URLConstant.SERVICE_REPORT);
	}
	
	/**
	 * 服务报告列表页面
	 * @return
	 */
	@RequestMapping("/showServiceReportListPage")
	public ModelAndView showServiceReportListPage(HttpServletRequest request){
		//TODO:服务报告列表页面
		Calendar cal = Calendar.getInstance();
		cal.setTime(WebConstant.getChartBaseDate());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		String endDate = year + "-" + month;
		cal.add(Calendar.MONTH, -1);
		int year2 = cal.get(Calendar.YEAR);
		int month2 = cal.get(Calendar.MONTH) + 1;
		String beginDate = year2 + "-" + month2;
//		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(), com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
//		Date beforeAfterDate = DateUtil.getCurrMonthFirstDay(WebConstant.getChartBaseDate());
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
		return new ModelAndView(URLConstant.SERVICE_REPORT_LIST);
	}
	
	
	/**
	 * 推送服务报告页面
	 * @return
	 */
	@RequestMapping("/pushServiceReportPage")
	public ModelAndView pushServiceReportPage(HttpServletRequest request){
		long reportId = getLongParam(request, "reportId", 0);
		request.setAttribute("reportId", reportId);
		return new ModelAndView(URLConstant.SERVICE_REPORT_PUSH);
	}
	
	/**
	 * 添加订阅者页面
	 * @return
	 */
	@RequestMapping("/showAddSubscriberPage")
	public ModelAndView showAddSubscriberPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.ADD_SUBSCRIBER);
	}
	
	/**
	 * 根据类型得到用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/getUserList")
	public @ResponseBody Map<String, Object> getUserList(HttpServletRequest request, String type){
		Map<String, Object> map = new HashMap<String, Object>();
		// 得到当前分页
		Page page = super.getCurrentPage(request.getParameter("pageIndex"), request.getParameter("pageSize"));
		map.put("page", page);
		String keyword = null; keyword =  request.getParameter("keyword");

		if(type.equals("1")){//群组
			long loginAccountId = this.getSessionUserInfo(request).getAccountId();
			String groupName = null;
			if(null != keyword && keyword.length() > 0){
				groupName = keyword;
			}
			List<GroupBean> groupList = groupService.getGroupPageList(page, loginAccountId, GroupBean.GROUP_LEDGER, groupName);	
			map.put("list", groupList);
		}else if(type.equals("2")){//企业
			LedgerBean ledger = new LedgerBean();
			ledger.setLedgerId(1L);
			ledger.setLedgerName(keyword);
			// 分页查询分户信息
			List<LedgerBean> dataList = ledgerManagerService.getLedgerList(page, ledger,"");
			map.put("list", dataList);
		}else if(type.equals("3")){//账户
			Map<String, Object> mapQuery = new HashMap<String, Object>();
			mapQuery.put("ledgerId",  super.getSessionUserInfo(request).getLedgerId());
			mapQuery.put("keyWord",  keyword);
			//加入查询参数设置
			mapQuery.put("message", 1);			
			List<Map<String,Object>> list = userService.getUserPageList(page,mapQuery);
			map.put("list", list);
		}
		return map;
	}
	
	
	
	/**
	 * 保存批量订阅
	 * 
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/saveBatchSubscriptionInfo")
	public @ResponseBody boolean saveBatchSubscriptionInfo(HttpServletRequest request,Integer type) throws IOException {
        boolean isSuccess = true;
        
        //校验CSRF Token
        String tokenIn = request.getHeader("RequestVerificationToken");
        String tokenSession = CSRFTokenManager.getTokenFromSession(request.getSession());
        if (!validateData(tokenIn) || !validateData(tokenSession) || !tokenIn.equals(tokenSession)) {
        	isSuccess = false;
        	return isSuccess;
        }
        
        List<SubscriberBean> subscriber = jacksonUtils.readJSON2Genric(request.getParameter("subscriber"),
                new TypeReference<List<SubscriberBean>>() {});
        List<Integer> eventId = jacksonUtils.readJSON2Genric(request.getParameter("eventId"),
                new TypeReference<List<Integer>>() {});
        List<Integer> infoId = jacksonUtils.readJSON2Genric(request.getParameter("infoId"),
                new TypeReference<List<Integer>>() {});

		
			messageService.saveBatchSubscriptionInfo(subscriber,eventId,infoId,type);


        //记录日志
        List<Long> userIds = messageService.getSubscriberUsers(subscriber);
        StringBuilder sb = new StringBuilder();
        int rst = CommonOperaDefine.OPRATOR_FAIL;
//        if(isSuccess == true){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
//        }
        sb.append("add batch book for:");
        if(userIds != null && userIds.size() > 0){
            for(int i = 0; i < userIds.size(); i++){
                UserBean userBean = userService.getUserByAccountId(userIds.get(i));
                if(i == 0){
                    sb.append(userBean.getLoginName());
                }
                else {
                    sb.append("，" + userBean.getLoginName());
                }
            }
        }
        sb.append(" by ").
           append(super.getSessionUserInfo(request).getLoginName()).
           append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT, CommonOperaDefine.MODULE_ID_BATCH_BOOK, CommonOperaDefine.MODULE_NAME_BATCH_BOOK, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_129, 201l, 1);

        return isSuccess;
	}
	
	/**
	 * 获取用户订阅的所有信息
	 * 
	 * @param request
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/getUserBookInfo", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> getUserBookInfo(HttpServletRequest request, long accountId) {
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_130, 202l, 1);
		return messageService.getUserBookInfo(accountId);
	}
	
	/**
	 * 得到账户数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAccountData", method = RequestMethod.POST)
	public @ResponseBody String getAccountData(HttpServletRequest request) {
		String str = "";
		StringBuffer sb = new StringBuffer("[");
		List<UserBean> userList = userService.getCanBookUsers(super.getSessionUserInfo(request).getLedgerId(), super.getSessionUserInfo(request).getAccountId());
		//按照插件所需要的格式拼接字符串
		if(userList.size()>0) {
			for(UserBean user : userList) {
				sb.append("{id:'").append(user.getAccountId())
					.append("',label:'").append(user.getLoginName())
					.append("',value:'").append(user.getAccountId()).append("',num:'")
					.append(user.getAccountId()).append("',count:'0'},");
			}	
			str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		} else
			str = "[]";
		return str;
	}
	
	/**
	 * 得到分户数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLedgerData", method = RequestMethod.POST)
	public @ResponseBody String getLedgerData(HttpServletRequest request) {
		String str = "";
		StringBuffer sb = new StringBuffer("[");
		List<UserBean> userList = userService.getLedgerData();
		//按照插件所需要的格式拼接字符串
		if(userList.size()>0) {
			for(UserBean user : userList) {
				sb.append("{id:'").append(user.getLedgerId())
					.append("',label:'").append(user.getLedgerName())
					.append("',value:'").append(user.getLedgerId()).append("',num:'")
					.append(user.getLedgerId()).append("',count:'0'},");
			}	
			str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		} else
			str = "[]";
		return str;
	}
	
	/**
	 * 保存订阅
	 * 
	 * @param request
	 * @param accountId
	 * @return
	 */
	@RequestMapping(value = "/saveSubscriptionInfo")
	public @ResponseBody boolean saveSubscriptionInfo(HttpServletRequest request, long accountId) {
        boolean isSuccess = true;
        
        //校验CSRF Token	
        String tokenIn = request.getHeader("RequestVerificationToken");
        String tokenSession = CSRFTokenManager.getTokenFromSession(request.getSession());
        if (!validateData(tokenIn) || !validateData(tokenSession) || !tokenIn.equals(tokenSession)) {
        	isSuccess = false;
        	return isSuccess;
        }
        
		try {
			List<Integer> eventId = jacksonUtils.readJSON2Genric(request.getParameter("eventId"),
					new TypeReference<List<Integer>>() {});
			List<Integer> infoId = jacksonUtils.readJSON2Genric(request.getParameter("infoId"),
					new TypeReference<List<Integer>>() {});
			messageService.saveSubscriptionInfo(accountId,eventId,infoId);

		} catch (IOException e) {
			Log.info("saveSubscriptionInfo error IOException");
            isSuccess = false;
		}

        //记录日志
        StringBuilder sb = new StringBuilder();
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess == true){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        sb.append("update user book for:").append(userService.getUserByAccountId(accountId).getLoginName())
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_ID_USER_BOOK, CommonOperaDefine.MODULE_NAME_USER_BOOK, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);


        return isSuccess;
	}
	
	/**
	 * 
	 * 函数功能说明  :上传图片
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException      
	 * @return  Map<String,String>     
	 * @throws
	 */
	@RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> addImage(HttpServletRequest request) throws IllegalStateException, IOException {
		String msg = "获取文件失败";
		boolean isSuccess = false;
		Map<String,Object> result = new HashMap<String,Object> ();
		result.put("msg", msg);	result.put("isSuccess", isSuccess);
		    //转型为MultipartHttpRequest(重点的所在)   
			MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;   
		    //  获得图片（根据前台的name名称得到上传的文件）   
			MultipartFile imgFile = multipartRequest.getFile("filePath"); if (imgFile == null) return result;
			String fileName = imgFile.getOriginalFilename(); if (null == fileName || fileName.trim().length()==0) return result;
			if(imgFile.getSize()>10*1024*1024) {
				msg = "图片太大";
				result.put("fileName", "");
			} else if(!(imgFile.getOriginalFilename() ==null || imgFile.getOriginalFilename().length()==0)) {
				String saveName =  System.currentTimeMillis()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
				File file = null;
                if(!DataUtil.checkFilePath(saveName)){ file = new File(request.getSession().getServletContext().getRealPath("/upload") +"/"+saveName); }
				// 文件路径是否存在
                if (file != null && null != file.getParentFile() && !file.exists()) {
                    if(!file.getParentFile().mkdirs()){
                        msg = "文件创建失败";
                    }
                }
			imgFile.transferTo(file); //保存文件
			msg = "上传成功";
			isSuccess = true;
			result.put("fileName", saveName);
		} 
		
		result.put("msg", msg);
		result.put("isSuccess", isSuccess);
		return result;
	} 
	
	/**
	 * 发送自定义消息
	 * 
	 * @param request
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/saveDefinedMsg")
	public @ResponseBody boolean saveDefinedMsg(HttpServletRequest request,String type) {
        boolean result = true;
        List<SubscriberBean> subscriber = null;
        String title =request.getParameter("title");
        String content = request.getParameter("content");
		try {
			if(type.equals("2")){//按收信人发送
				subscriber = jacksonUtils.readJSON2Genric(request.getParameter("subscriber"),
						new TypeReference<List<SubscriberBean>>() {});
			}
			
			String fileName = "definedMsg"+SequenceUtils.getDBSequence()+".html";
			String filePath = request.getSession().getServletContext().getRealPath("/") + WebConstant.uploadFile + fileName;
			OutputHtmlUtil.createHtmlFile(filePath, title, content);
			messageService.saveDefinedMsg(subscriber, title, WebConstant.uploadFile + fileName);

		} catch (IOException e) {
			Log.info("saveDefinedMsg error IOException");
            result = false;
		}

        //记录日志
        StringBuilder sb = new StringBuilder();
        sb.append("add a user-defined message:").append(title)
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(result == true){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT, CommonOperaDefine.MODULE_ID_USER_DEFINED_MESSAGE, CommonOperaDefine.MODULE_NAME_USER_DEFINED_MESSAGE, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_139, 108l, 1);
        return result;
	}
	
	/**
	 * 发送新闻政策消息
	 * 
	 * @param request
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/saveNewsInfo")
	public @ResponseBody boolean saveNewsInfo(HttpServletRequest request, int type) {
        boolean result = true;
        String title =request.getParameter("title");
        String content = request.getParameter("content");
        String picUrl = WebConstant.uploadFile + request.getParameter("picName");
		try {
			String fileName = "news"+SequenceUtils.getDBSequence()+".html";
			String filePath = request.getSession().getServletContext().getRealPath("/") + WebConstant.uploadFile + fileName;
			OutputHtmlUtil.createHtmlFile(filePath, title, content);
			messageService.saveNewsInfo(title, type, picUrl, WebConstant.uploadFile + fileName, content);

		} catch (Exception e) {
			Log.error("saveNewsInfo fail!");
            result = false;
		}


        //记录日志
        StringBuilder sb = new StringBuilder();
        sb.append("add a news-policy:").append(title)
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(result){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT, CommonOperaDefine.MODULE_ID_NEWS_POLICY_PUBLISH, CommonOperaDefine.MODULE_NAME_NEWS_POLICY_PUBLISH, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_140, 109l, 1);
		return result;
	}
	
	/**
	 * 得到服务报告列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchSerivceReport")
	public @ResponseBody Map<String, Object> searchSerivceReport(HttpServletRequest request) {
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
        List<ServiceReportBean> list = messageService.searchSerivceReport(param);
        result.put("list", list);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_144, 122l, 1);
		return result;
	}
	
	/**
	 * 发送服务报告
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveReportInfo")
	public @ResponseBody boolean saveReportInfo(HttpServletRequest request, long ledgerId, String baseDate) {
		
			String content = request.getParameter("content");
			messageService.saveReportInfo(ledgerId, DateUtils.convertDateStr2Date(baseDate, "yyyy-MM-dd"), content);
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_141, 110l, 1);
		return true;
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
			map = messageService.queryReportInfoData2(reportId,accountId);
			String tips = messageService.getReportTips(reportId, accountId);
			map.put("tips", tips);
			return map;

	}
	
	/**
	 * 获取服务报告需要的数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryReportInfoData")
	public @ResponseBody Map<String, Object> queryReportInfoData(HttpServletRequest request, long ledgerId, String baseDate) {
		
		
			return messageService.queryReportInfoData(ledgerId, DateUtils.convertDateStr2Date(baseDate, "yyyy-MM-dd"));

	}
	
	/**
	 * 用户自动提醒参数配置页面
	 * @return
	 */
	@RequestMapping("/showAutoReminderSetPage")
	public ModelAndView showAutoReminderSetPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.AUTO_REMINDER_SET);
	}
	
	/**
	 * 得到用户自动提醒参数配置
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAutoReminderSet")
	public @ResponseBody Map<String,Object> getAutoReminderSet(HttpServletRequest request) {	
			return messageService.getAutoReminderSet();	
	}
	
	/**
	 * 保存用户自动提醒参数配置
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveAutoReminderSet")
	public @ResponseBody boolean saveAutoReminderSet(HttpServletRequest request) {
        boolean result = true;
//		try {
			String nologin = request.getParameter("nologin");
			String news = request.getParameter("news");
			String unRead = request.getParameter("unRead");
			messageService.saveAutoReminderSet(nologin, news, unRead);
//		} catch (Exception e) {
//			Log.error("saveAutoReminderSet", e);
//            result = false;
//		}

        //记录日志
        StringBuilder sb = new StringBuilder();
        sb.append("update auto remind parameter set:")
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        int rst = CommonOperaDefine.OPRATOR_FAIL;
//        if(result == true){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
//        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_ID_AUTO_REMIND_SET, CommonOperaDefine.MODULE_NAME_AUTO_REMIND_SET, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_142, 114l, 1);
        return result;
	}
	
	/**
	 * 获取用户反馈列表
	 */
	@RequestMapping(value = "/querySuggestList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> querySuggestList(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(getStrParam(request, "paramInfo", ""));
		// 分页信息
		Page page = super.getCurrentPage(json.getString("pageNo"), json.getString("pageSize"));
		List<SuggestBean> result = null;
		param.put("page", page);
		
		result = messageService.querySuggestPageList(param);
		
		resultMap.put("page", page);
		if(result != null && result.size() > 0){
			resultMap.put("dataInfo", result);
		}
		
		return resultMap; 
	}
	
	/**
	 * 提交回复
	 */
	@RequestMapping(value = "/updateReply" , method=RequestMethod.POST)
	public @ResponseBody boolean updateReply(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String,Object>();
		Long accountId = getLongParam(request, "accountId",-1);
		String sugMsg = getStrParam(request,"sugMsg","");
		String sugReply = getStrParam(request, "sugReply", "");
		if(sugReply==null){return false;}
		String content = "您的反馈："+sugMsg+"；您得到的回复："+sugReply;
		
		param.put("sugId",getStrParam(request, "sugId", ""));
		param.put("sugReply",getStrParam(request, "sugReply", ""));
		
		boolean isSuccess = false;
		isSuccess = messageService.updateReply(param);
		
		if(isSuccess){
			String fileName = "definedMsg"+SequenceUtils.getDBSequence()+".html";
			String filePath = request.getSession().getServletContext().getRealPath("/") + WebConstant.uploadFile + fileName;
			OutputHtmlUtil.createHtmlFile(filePath, "反馈信息回复", content);
			
			messageService.saveDefinedMsg(accountId, "反馈信息回复", WebConstant.uploadFile + fileName);
		}

        //记录日志
        StringBuilder sb = new StringBuilder();
        if(sugReply.length() > 7){
            sugReply = sugReply.substring(6) + "...";
        }
        sb.append("update a user feedback reply:").append(sugReply)
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess == true){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT, CommonOperaDefine.MODULE_ID_USER_FEEDBACK, CommonOperaDefine.MODULE_NAME_USER_FEEDBACK, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);

        return isSuccess;
	}
	
	/**
	 * 导出到Excel
	 * @param: request(页面请求),response(页面响应,返回excel)
	 */
	@RequestMapping(value = "/sugExcel")
	public @ResponseBody void getSugExcel(HttpServletRequest request,HttpServletResponse response) {
		String filename = "用户反馈";
		
		try {
			response.setHeader("Content-Disposition",
			"attachment;filename="+new String(filename.getBytes(), "ISO-8859-1")+".xls");
		} catch (UnsupportedEncodingException e) {
			Log.info("getSugExcel error UnsupportedEncodingException");
		}	//指定下载的文件名
		response.setContentType("application/vnd.ms-excel");
		//得到数据
		List<SuggestBean> list = messageService.querySuggestList();
		//得到页面请求信息pageInfo，作为参数传进getEleExcel方法
		try {
			messageService.getSugExcel("Cache.xls", response.getOutputStream(),list);
		} catch (IOException e) {
			Log.info("getSugExcel error IOException");
		} 

	}
	
	/**
	 * 越限参数设置页面
	 * @return
	 */
	@RequestMapping("/eventExceedSet")
	public ModelAndView showEventExceedSetPage(HttpServletRequest request){
		String ledgerId = super.getStrParam(request, "ledgerId", "");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("csrf", CSRFTokenManager.getTokenForSession(request.getSession()));		
		if (ledgerId != null && ledgerId.length()>0) {
			map.put("ledgerId", ledgerId);
			String ledgerName = messageService.getLedgerName(Long.parseLong(ledgerId));
			if(ledgerName != null) {
				map.put("ledgerName", ledgerName);
			}else {
				map.put("ledgerName", "");
			}
		} else {
			Long accountId = super.getSessionUserInfo(request).getAccountId();
			List<UserBean> userList = userService.getCompanyLedgerDataByAccountId(accountId);
			if(userList.size()>0) {
				map.put("ledgerId", userList.get(0).getLedgerId());
				map.put("ledgerName", userList.get(0).getLedgerName());
			}else {
				map.put("ledgerId", "ledgerId");
				map.put("ledgerName", "");
			}
		}
        Integer roleType = super.getSessionRoleType(request);
		map.put("eventTypeList", messageService.getEventTypeListByRoleType(roleType));
		return new ModelAndView(URLConstant.Event_Exceed_Set,map);
	}
	
	/**
	 * 得到企业分户数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCompanyLedgerData")
	public @ResponseBody String getCompanyLedgerData(HttpServletRequest request) {
		String str = "";

        Long accountId = super.getSessionUserInfo(request).getAccountId();
        Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
        if(ledgerId == 0){  //群组

            List<Map<String, Object>> dataList = ledgerManagerService.getLedgersForGroup2(accountId);
            //按照插件所需要的格式拼接字符串
            if(!CollectionUtils.isEmpty(dataList)) {
                StringBuffer sb = new StringBuffer("[");
                for(Map<String,Object> data : dataList) {
                    sb.append("{id:'").append(data.get("LEDGER_ID"))
                            .append("',label:'").append(data.get("LEDGER_NAME"))
                            .append("',value:'").append(data.get("LEDGER_ID"))
                            .append("',num:'").append(data.get("LEDGER_ID"))
                            .append("',count:'0'},");
                }
                str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
            }
            else{
                str = "[]";
            }

        }
        else {  //非群组

            List<UserBean> userList = userService.getCompanyLedgerDataByAccountId(accountId);
            //按照插件所需要的格式拼接字符串
            if(!CollectionUtils.isEmpty(userList)) {
                StringBuffer sb = new StringBuffer("[");
                for(UserBean user : userList) {
                    sb.append("{id:'").append(user.getLedgerId())
                            .append("',label:'").append(user.getLedgerName())
                            .append("',value:'").append(user.getLedgerId()).append("',num:'")
                            .append(user.getLedgerId()).append("',count:'0'},");
                }
                str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
            }
            else{
                str = "[]";
            }

        }

		return str;
	}
	
	@RequestMapping(value = "/saveEventSettingData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEventSettingData(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//校验CSRF Token
        String tokenIn = request.getHeader("RequestVerificationToken");
        String tokenSession = CSRFTokenManager.getTokenFromSession(request.getSession());
        if (!validateData(tokenIn) || !validateData(tokenSession) || !tokenIn.equals(tokenSession)) {
        	resultMap.put("isSuccess", CommonOperaDefine.OPRATOR_FAIL);
        	return resultMap;
        }
		
		Map<String, Object> settingInfo = new HashMap<String, Object>();
		try {
			settingInfo = jacksonUtils.readJSON2Map(request.getParameter("settingInfo"));
		} catch (IOException e) {
			Log.error(this.getClass() + ".saveEventSettingData() -- 无法保存事件设置数据");
		}
		
		StringBuffer desc = new StringBuffer();
		desc.append("save event setting data--event type:" + settingInfo.get("eventTypeName").toString())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		resultMap = messageService.saveEventSettingData(settingInfo);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if((Boolean) resultMap.get("isSuccess")){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_ID_EVENT_EXCEED_SET_MODEL,CommonOperaDefine.MODULE_NAME_EVENT_EXCEED_SET_MODEL,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return resultMap;
	}
	
	@RequestMapping(value = "/getEventSettingRecPageList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEventSettingRecPageList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		Page page = null;
		// 得到当前分页
		Map<String, Object> pageInfo = new HashMap<String, Object>();
		try {
			pageInfo = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		} catch (IOException e) {
			Log.error(this.getClass() + ".getEventSettingRecPageList()--无法获取参数");
		}
		if(pageInfo.get("pageIndex")!= null && pageInfo.get("pageSize") != null)
			page = new Page(Integer.valueOf(pageInfo.get("pageIndex").toString()),Integer.valueOf(pageInfo.get("pageSize").toString()));
		else
			page = new Page();
		if(pageInfo.containsKey("queryMap"))
			queryMap.putAll((Map<String,Object>)pageInfo.get("queryMap"));
		// 分页查询费率信息
		List<EventSettingRecBean> dataList = messageService.getEventSettingRecPageData(page, queryMap);
		result.put("page", page);
		result.put("list",dataList);
		result.put("queryMap",queryMap);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_128, 204l, 1);
		return result;
	}
	
	@RequestMapping(value = "/getEventRelatedNodes", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEventRelatedNodes(HttpServletRequest request) {
		String legerId = request.getParameter("ledgerId");
		Map<String, Object> result = new HashMap<String, Object>();
		boolean isCompany = true;
		if (legerId == null) {
			String accountId = request.getParameter("accountId");
			if (accountId != null) {
				UserBean userBean = userService.getUserByAccountId(Long.parseLong(accountId));
				LedgerBean ledgerBean = ledgerManagerService.selectByLedgerId(userBean.getLedgerId());
				if (ledgerBean.getAnalyType() != 102) {
					isCompany = false;
					result.put("isCompany", isCompany);
					return result;
				}
				legerId = userBean.getLedgerId().toString();
			}
		}
		String treeType = request.getParameter("treeType");
		String eventTypeIdStr = request.getParameter("eventTypeId");
		long eventTypeId = 0l;
		if (eventTypeIdStr != null) {
			eventTypeId = Long.parseLong(eventTypeIdStr);
		}
		List<EventRelatedNodeTreeBean> eList = messageService.getEventRelatedNodes(Long.parseLong(legerId),Integer.parseInt(treeType),eventTypeId, request);
		result.put("isCompany", isCompany);
		result.put("list", eList);
		return result;
	}
	
	@RequestMapping(value = "/updateEventSettingData", method = RequestMethod.POST)
	public @ResponseBody Boolean updateEventSettingData(HttpServletRequest request) {
		boolean isSuccess = false;
		
		//校验CSRF Token
        String tokenIn = request.getHeader("RequestVerificationToken");
        String tokenSession = CSRFTokenManager.getTokenFromSession(request.getSession());
        if (!validateData(tokenIn) || !validateData(tokenSession) || !tokenIn.equals(tokenSession)) {
        	return isSuccess;
        }
		
		List<Map<String, Object>> itemMapList = new ArrayList<Map<String,Object>>();
		try {
			itemMapList = jacksonUtils.readJSON2ListMap(request.getParameter("items"));
		} catch (IOException e) {
			Log.error("MessageController -- updateEventSettingData getParameter error!");
		}
		
		StringBuffer desc = new StringBuffer();
		desc.append("update event setting data")
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		isSuccess = messageService.updateEventSettingData(itemMapList);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_ID_EVENT_EXCEED_SET_MODEL,CommonOperaDefine.MODULE_NAME_EVENT_EXCEED_SET_MODEL,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	@RequestMapping(value = "/deleteEventSettingData", method = RequestMethod.POST)
	public @ResponseBody Boolean deleteEventSettingData(HttpServletRequest request) {
		boolean isSuccess = false;
		
		//校验CSRF Token
        String tokenIn = request.getHeader("RequestVerificationToken");
        String tokenSession = CSRFTokenManager.getTokenFromSession(request.getSession());
        if (!validateData(tokenIn) || !validateData(tokenSession) || !tokenIn.equals(tokenSession)) {
        	return isSuccess;
        }
		
		List<Long> recIdList = new ArrayList<Long>();
		try {
			recIdList = jacksonUtils.readJSON2Genric(request.getParameter("recIds"), new TypeReference<List<Long>>(){});
		} catch (IOException e) {
			Log.error("read parameter error, can not delete event setting data");
		}
		
		StringBuffer desc = new StringBuffer();
		desc.append("delete event setting data -- recId:"+recIdList.toString())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		isSuccess = messageService.deleteEventSettingData(recIdList);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_ID_EVENT_EXCEED_SET_MODEL,CommonOperaDefine.MODULE_NAME_EVENT_EXCEED_SET_MODEL,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	/**
	 * 推送服务报告
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pushServiceReport", method = RequestMethod.POST)
	public @ResponseBody Boolean pushServiceReport(HttpServletRequest request) {
		boolean isSuccess = false;
		Long reportId = getLongParam(request, "reportId", 0);
		String spAdvise = getStrParam(request, "spAdvise", "");
		String spName = getStrParam(request, "spName", "");
		String spPhone = getStrParam(request, "spPhone", "");
		
			this.messageService.pushServiceReport(reportId,spAdvise,spName,spPhone);
			isSuccess = true;
		
		return isSuccess;
	}
	
	/**
	 * 服务报告导出word
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/toWord")
	public @ResponseBody Boolean toWord(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");      
        Map<String, Object> params = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
        String type = "report";
        if(params.containsKey("reportId")){
        	Map<String, Object> map = new HashMap<String, Object>();
        	Long accountId = super.getSessionUserInfo(request).getLedgerId();
        	map = messageService.queryReportInfoData2(Long.parseLong(params.get("reportId").toString()),accountId);
        	List<Map<String, Object>> childList = (List<Map<String, Object>>) map.get("childList");
        	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        	
        	double qTotal = 0;
        	for (int i = 0; i < childList.size(); i++) {
        		qTotal = DataUtil.doubleAdd(qTotal, Double.parseDouble(childList.get(i).get("Q").toString()));
			}
        	for (int i = 0; i < childList.size(); i++) {
        		Map<String, Object> childMap = new HashMap<String, Object>();
        		childMap.put("name", childList.get(i).get("LEDGER_NAME"));
        		double q = Double.parseDouble(childList.get(i).get("Q").toString());
        		childMap.put("Q", Math.round(q));        		
        		childMap.put("Qpercent", new BigDecimal(q).multiply(new BigDecimal(100)).divide(new BigDecimal(qTotal), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
        		list.add(childMap);
			}
        	params.put("table1", list);
        	type = "report2";
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
	 * 保存用户体验信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/insertVisitorRecord")
	public @ResponseBody boolean insertVisitorRecord(HttpServletRequest request){
		boolean isSuccess = true;
		
			String visitorInfo = getStrParam(request, "visitorInfo", "");
			messageService.insertVisitorRecord(visitorInfo);

		return isSuccess;
	}
	
	/**
	 * 得到事件类型列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryTypeList")
	public @ResponseBody List<Map<String,Object>> queryTypeList(HttpServletRequest request){
		List<Map<String, Object>> allEventTypeList = messageService.getAllEventTypeList();
		return allEventTypeList;
	}
}
