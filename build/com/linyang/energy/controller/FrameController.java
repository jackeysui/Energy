package com.linyang.energy.controller;

import cn.jpush.api.utils.StringUtils;
import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.*;
import com.linyang.energy.service.*;
import com.linyang.energy.service.impl.LimiteLogin;
import com.linyang.energy.utils.*;
import com.linyang.util.CommonMethod;
import com.sgcc.sip.access.rest.SipRestAccessAgent;
import com.sgcc.sip.access.rest.SipRestAccessAgentFactory;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.*;

/**
 * 
 * @description:
 * @version:0.1
 * @author:Cherry
 * @date:Nov 29, 2013
 */
	//

@Controller
@RequestMapping("/frameController")
public class FrameController extends BaseController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private FrameService frameService;
	@Autowired
	private UserAnalysisService userAnalysisService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private LedgerManagerService ledgerManagerService;
	@Autowired
	private PhoneService phoneService;
	@Autowired
	private LimiteLogin limiteLogin;

	/**
	 * 跳转到登陆页面
	 * @return
	 */
    @RequestMapping("/showLoginPage")
    public ModelAndView showLoginPage(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        String username = null;
//        String pwd = null;
//        String roleType = null;
//        String loginPath = "login";
//        Integer autoLogin = 0;
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (!validateData(cookie))
//                    continue;
//                String name = cookie.getName();
//                if(name.equals("user")) {
//                    String[] users = cookie.getValue().split("-");
//                    if(users != null && users.length == 3){
//                        username = users[0]; if (username == null || username.trim().length()==0) username = "";
//                        pwd = users[1];
//                        roleType = users[2]; if (roleType == null || roleType.trim().length()==0) roleType = "1";
//                    }
//                }
//                if(name.equals("auto_login")){
//                    String value = cookie.getValue();
//                    if(value!=null){
//                        autoLogin = Integer.parseInt(value);
//                    }
//                }
//                if("loginPath".equals(name)){
//                    String value = cookie.getValue();
//                    if (value != null && value.trim().length()==0)
//                        loginPath = value;
//                }
//            }
//        }
//			if (username != null && pwd != null && autoLogin == 1 && pwd.trim().length()>0) {
//				UserBean userBean = userService.getUserByUserName(username);
//				if(userBean != null ){
//					if (userBean.getAccountStatus()== 1 && userBean.getLoginPassword().substring(5).toUpperCase().equals(pwd.toUpperCase())) {
//						request.getSession().setAttribute("sessionInfo", userBean);
//						request.getSession().setAttribute("roleType", roleType);
//						request.getSession().setAttribute("loginPath", loginPath);
//
//						Long accountId = super.getSessionUserInfo(request).getAccountId();
//						int type = 1;
//						String osVersion = BrowerUtil.checkBrowse(request.getHeader("user-agent"));
//						this.userAnalysisService.addAccountLogin(accountId,new Date(),type,osVersion);
//
//						return  new ModelAndView("/energy/index/auto_login");
//					}
//				}
//			}
        // 判断加载页面
        request.setAttribute("loginPath", getStrParam(request, "loginPath", "login"));
        return new ModelAndView("/energy/framework/login","language", getStrParam(request, "language", "zh-CN"));
    }

    /**
	 * 判断是否设置了大屏显示功能
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkLinyang")
	public @ResponseBody Boolean checkLinyang(HttpServletRequest request) {
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		return this.userService.checkFullScreen(ledgerId);
	}
	
	/**
	 * 判断是否设置了第三方链接
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkThirdLink")
	public @ResponseBody Map<String,Object> checkThirdLink(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		result = this.ledgerManagerService.checkThirdLink(ledgerId);
		return result;
	}
	
	
	
	@RequestMapping("/checkRoleType")
	public @ResponseBody void checkRoleType(HttpServletRequest request,String roleType) {
		int type = 0; if (roleType == null || roleType.trim().length()==0) type = 1; else type = Integer.parseInt(roleType);
		if (type == 1 || type == 2) {
			request.getSession().setAttribute("roleType", String.valueOf(type));
		}
	}
	
	/**
	 * 大屏演示页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/linyang")
	public ModelAndView showLinyangDemoPage(HttpServletRequest request) {
		//设置session不会失效
		request.getSession().setMaxInactiveInterval(60*60);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("spec1", WebConstant.spec1);
		map.put("spec2", WebConstant.spec2);
		//得到Logo配置信息
		String loginPath = "login";
		if(request.getSession().getAttribute("loginPath")!=null)
			loginPath = request.getSession().getAttribute("loginPath").toString();
		UserBean userbean=userService.getUserByAccountId(super.getSessionUserInfo(request).getAccountId());
		Map<String,Object> logoConfig = ledgerManagerService.getLogoConfig(userbean.getLedgerId(),loginPath,1);
		map.putAll(logoConfig);
		map.put("ledgerId", super.getSessionUserInfo(request).getLedgerId());
		map.put("username", super.getSessionUserInfo(request).getLoginName());
		return new ModelAndView("energy/linyangdemo/main",map);
	}

    /**
     * 平台运营商大屏
     * @param request
     * @return
     */
    @RequestMapping("/platAdminDemoPage")
    public ModelAndView platAdminDemoPage(HttpServletRequest request) {
        request.getSession().setMaxInactiveInterval(60*60);    //设置session不会失效
        Map<String, Object> map = new HashMap<String, Object>();

        Long accountId = super.getSessionUserInfo(request).getAccountId();
        Long ledgerId = 0L;
        if (super.getSessionUserInfo(request).getLedgerId() == 0L) {
            // 权限为群组分配ledgerId
            ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
        }
        else {
            ledgerId = super.getSessionUserInfo(request).getLedgerId();
        }

        LedgerBean ledger = ledgerManagerService.selectByLedgerId(ledgerId);
        map.put("ledger", ledger);
        return new ModelAndView("energy/linyangdemo/platMain",map);
    }


    /**
     * 轮显---配置显示分户页面
     * */
    @RequestMapping("/lyConfig")
    public ModelAndView showLyConfigPage(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> ledgers = this.indexService.getLedgerLyConfig();
        map.put("ledgers", ledgers);
        return new ModelAndView("energy/linyangdemo/lyConfig", map);
    }
    
	
	@RequestMapping("/showMainPage")
	public ModelAndView showMainPage(HttpServletRequest request) throws UnsupportedEncodingException{
		request.setCharacterEncoding("UTF-8");
	    //return "redirect:/frameController/showCenterPage.htm";
        Map<String, Object> map = frameService.getUserModules(super.getSessionUserInfo(request).getAccountId());
        JSONObject srcInfoJSON = JSONObject.fromObject(request.getParameter("srcInfo"));
        map.put("src", srcInfoJSON.getString("src"));
        String position = srcInfoJSON.getString("position");
        map.put("position", position);
        if(srcInfoJSON.containsKey("selectedId") && srcInfoJSON.getString("selectedId") != null){
            map.put("selectedId",srcInfoJSON.getString("selectedId"));
            map.put("typeId",srcInfoJSON.getString("typeId"));
        }else{
            map.put("selectedId","0");
            map.put("typeId","0");
        }
        UserBean userbean=userService.getUserByAccountId(super.getSessionUserInfo(request).getAccountId());
        if(userbean==null||userbean.getLastDate()==null){
            map.put("isFirstLogin", "1");
        }
        else{
            map.put("isFirstLogin", "0");
        }
        map.put("roleType", super.getSessionRoleType(request));
        map.put("eleDoNotLoad", Arrays.asList(WebConstant.eleNotLoadModules));
        map.put("emoDoNotLoad", Arrays.asList(WebConstant.emoNotLoadModules));
        map.put("csrf", CSRFTokenManager.getTokenForSession(request.getSession()));
        Long ledgerId = 0L;
        if (super.getSessionUserInfo(request).getLedgerId() == 0L) {
            //权限为群组分配ledgerId
            ledgerId = ledgerManagerService.getLedgerIfNull(super.getSessionUserInfo(request).getAccountId());
        } else {
            ledgerId = super.getSessionUserInfo(request).getLedgerId();
        }
        LedgerBean ledger = ledgerManagerService.selectByLedgerId(ledgerId);
        map.put("analyType",  ledger.getAnalyType());
        map.put("isGuestUser",  (Boolean) request.getSession().getAttribute("isGuestUser"));
        //得到Logo配置信息
        String loginPath = "login";
        if(request.getSession().getAttribute("loginPath")!=null)
            loginPath = request.getSession().getAttribute("loginPath").toString();
        Map<String,Object> logoConfig = null; if(userbean!=null){logoConfig=ledgerManagerService.getLogoConfig(userbean.getLedgerId(),loginPath,2);}
        map.putAll(logoConfig);
        
        //add by zzy for 定位树节点
        try {
	        Long treeTypeId = srcInfoJSON.getLong("treeTypeId");	//treeTypeId: 1-管理者树定位企业(暂时只用到这个, 别的先不管..)
	        Long treeSelectedId = srcInfoJSON.getLong("treeSelectedId");	//节点id
	        if (treeTypeId != null && treeSelectedId != null) {
	        	map.put("treeTypeId", treeTypeId);
	        	map.put("treeSelectedId", treeSelectedId);
	        }
        } catch (JSONException e) {
        	// do nothing
        }
        
		//页面跳转可共用传参
		if(srcInfoJSON.containsKey("jumpParam") && srcInfoJSON.getString("jumpParam") != null){
			String jumpParamStr = srcInfoJSON.getString("jumpParam");
			JSONObject jumpParamJson = JSONObject.fromObject(jumpParamStr);
			map.put("jumpParam", jumpParamJson);
		}
        //end
        return new ModelAndView("/energy/framework/main",map);

    }

    //该方法已废弃，暂时没在用
//	@RequestMapping("/showCenterPage")
//	public  ModelAndView  showCenterPage(HttpServletRequest request){
//		Map<String, Object> map = frameService.getUserModules(super.getSessionUserInfo(request).getAccountId(), super.getSessionRoleType(request));
//		if(userService.getUserByAccountId(super.getSessionUserInfo(request).getAccountId())==null||userService.getUserByAccountId(super.getSessionUserInfo(request).getAccountId()).getLastDate()==null){
//			map.put("isFirstLogin", "1");
//		}
//		else{
//			map.put("isFirstLogin", "0");
//		}
//		UserBean userbean=userService.getUserByAccountId(super.getSessionUserInfo(request).getAccountId());
//		map.put("roleType", super.getSessionRoleType(request));
//        map.put("eleDoNotLoad", Arrays.asList(WebConstant.eleNotLoadModules));
//        map.put("emoDoNotLoad", Arrays.asList(WebConstant.emoNotLoadModules));
//        map.put("csrf", CSRFTokenManager.getTokenForSession(request.getSession()));
//        Long ledgerId = 0L;
//		if (super.getSessionUserInfo(request).getLedgerId() == 0L) {
//			//权限为群组分配ledgerId
//			ledgerId = ledgerManagerService.getLedgerIfNull(super.getSessionUserInfo(request).getAccountId());
//		} else {
//			ledgerId = super.getSessionUserInfo(request).getLedgerId();
//		}
//		LedgerBean ledger = null;if(ledgerManagerService.selectByLedgerId(ledgerId)!=null){ledger=ledgerManagerService.selectByLedgerId(ledgerId);}
//		if(ledger!=null)map.put("analyType",  ledger.getAnalyType());
//        map.put("isGuestUser",  (Boolean) request.getSession().getAttribute("isGuestUser"));
//		//得到Logo配置信息
//		String loginPath = "login";
//		if(request.getSession().getAttribute("loginPath")!=null)
//			loginPath = request.getSession().getAttribute("loginPath").toString();
//		Map<String,Object> logoConfig = ledgerManagerService.getLogoConfig(userbean.getLedgerId(),loginPath,2);
//		map.putAll(logoConfig);
//		// 判断返回页面
//		try{
//			map.putAll(RequestContextUtils.getInputFlashMap(request));
//			//得到一个用户的模块信息
//			return new ModelAndView("/energy/framework/main",map);
//		}catch (Exception e) {
//			map.put("mapData",MapMapping.getMapMapping());
//
//			return new ModelAndView("/energy/framework/main",map);
//		}
//	}
	
	@RequestMapping("/showMenuPage")
	public  ModelAndView  showMenuPage(HttpServletRequest request){
		UserBean userBean = super.getSessionUserInfo(request);
		Map<String,Object> resultMap = frameService.getUserModules(super.getSessionUserInfo(request).getAccountId(), super.getSessionRoleType(request));
		//得到Logo配置信息
		String loginPath = "login";
		if(request.getSession().getAttribute("loginPath")!=null)
			loginPath = request.getSession().getAttribute("loginPath").toString();
		Map<String,Object> logoConfig = ledgerManagerService.getLogoConfig(userBean.getLedgerId(),loginPath,2);
		resultMap.putAll(logoConfig);
        //首页是否开放地图功能
        boolean showMap = false;
        Long ledgerId = 0L;
		if (userBean.getLedgerId() == 0L) {
			//权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(userBean.getAccountId());
		} else {
			ledgerId = userBean.getLedgerId();
		}
        LedgerBean ledger = ledgerManagerService.selectByLedgerId(ledgerId);
        //用户类型
        resultMap.put("analyType", ledger.getAnalyType());
        if(ledger.getAnalyType()!=102 && ledger.getAnalyType()!=105){ //企业和平台运营商用户不展示菜单上的地图切换按钮
            showMap = true;
        }
        resultMap.put("showMap", showMap);

		//得到一个用户的模块信息
		return new ModelAndView("/energy/index/include",resultMap);
	}


    /**
     * 用户登录
     * @param username 登录用户名称
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> login(HttpServletRequest request,String username,String password,String roleType,String code, String display) throws ParseException {
        int errorCode = 0;
        String str_roleType = roleType;
        if (str_roleType == null || str_roleType.trim().length()==0)
        	str_roleType = "1";
        UserBean userBean = userService.getUserByUserName(username);
        Map<String, Object> map = new HashMap<String, Object>();
        ///判断用户名
        if(!validateData(username) || userBean == null){
            errorCode = 1;

            map.put("errorCode", errorCode);
            request.getSession().setAttribute("loginPath", getStrParam(request, "loginPath", "login"));
            return map;
        }
        ///判断是否被禁用
        if(userBean.getAccountStatus() != 1){
            errorCode = 3;

            map.put("errorCode", errorCode);
            request.getSession().setAttribute("loginPath", getStrParam(request, "loginPath", "login"));
            return map;
        }
        ///判断验证码
        if(display != null && !"none".equals(display) ){
	        Integer checkCode = checkCode(request.getSession(), code);
	        if(checkCode != 1000 && checkCode != 1001){
	            errorCode = 10000;
	
	            map.put("errorCode", errorCode);
	            request.getSession().setAttribute("loginPath", getStrParam(request, "loginPath", "login"));
	            return map;
	        }
        }
        
        //未被锁定
        Date today = new Date();
        String lockTime = userBean.getLockTime();
        if(null!=lockTime && !DateUtil.clearDate(DateUtil.convertStrToDate(lockTime, DateUtil.DEFAULT_PATTERN)).equals(DateUtil.clearDate(today))){
            userBean.setTodayTimes(0);
            userBean.setLockTime("");
            userService.updateTodayTimes(0, userBean.getAccountId());
        }
        
        ///判断密码
        if(StringUtils.isNotEmpty(userBean.getLockTime()) && userBean.getTodayTimes() != null && Integer.valueOf(userBean.getTodayTimes()) >= 4){  //账号锁定
            errorCode = 2;

            map.put("errorCode", errorCode);
            request.getSession().setAttribute("loginPath", getStrParam(request, "loginPath", "login"));
            return map;
        }

       
        // 验证密码
        if (!CipherUtil.validatePassword(userBean.getLoginPassword(), password, false)) {// 密码错误
            // 获取账号今日登录次数，并+1
            Integer times = 0;
            if(userBean.getTodayTimes()!=null){
                times = userBean.getTodayTimes() + 1;
            }else{
                times +=1;
            }
            if (times >= 4) {// 登录失败次数4次，账号锁定
                errorCode = 2;
            }
            else {
                errorCode = 1;
            }
            userBean.setTodayTimes(times);
            userBean.setLockTime(DateUtil.getCurrentDateStr());
            userService.updateTodayTimes(times, userBean.getAccountId());
            userService.updateLockTime(new Date(), userBean.getAccountId());
        }
        else{
            // 清除用户登录次数
            userBean.setTodayTimes(0);
            userBean.setLockTime(DateUtil.getCurrentDateStr());
            userService.updateTodayTimes(0, userBean.getAccountId());
	
            Date publishDate = WebConstant.publishDate;
            Long publishTime = publishDate.getTime();
            // 判断是否为版本更新后的第一次登录
            Date lastDate = userBean.getLastDate();
            if (lastDate == null || lastDate.getTime() < publishTime) {
                map.put("isFirst", true);
            }
            // 保存session信息
            frameService.saveSession(loginSessionKey, userBean, str_roleType, request, username);

            map.put("user", username + "-" + CipherUtil.encodeBySHA256(password).substring(5) + "-" + str_roleType);
			map.put("user", username + "-" + password.substring(5) + "-" + str_roleType);
            errorCode = 4;// 登录成功
            Long accountId = super.getSessionUserInfo(request).getAccountId();
            String osVersion = BrowerUtil.checkBrowse(request.getHeader("user-agent"));
            this.userAnalysisService.addAccountLogin(accountId, new Date(), 1, osVersion);
            request.getSession().removeAttribute("code");
        }

        map.put("errorCode", errorCode);
        // 设置路径
        request.getSession().setAttribute("loginPath", getStrParam(request, "loginPath", "login"));

		//判断用户是否已经在线及处理（已在线则剔除）
		String loginLimite = limiteLogin.loginLimite(request, username);
		//用户掉线，登录后重定向到保存的链接
		request.getSession().setAttribute("now_user", userBean);

		return map;
    }

    /**
     * 单点登录
     * @param request
     * @param token
     * @throws ParseException
     */
    @RequestMapping(value = "/ssoLogin")
    public ModelAndView ssoLogin(HttpServletRequest request,String token) throws ParseException {
        int errorCode = 0;
        Map<String,Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        UserBean userBean = null;
        
        //获取静态参数信息
        String ssoUrl = WebConstant.ssoGetInfoUrl;
    	String account = WebConstant.ssoAccount; 
    	String pwd = WebConstant.ssoPwd; 
    	String secretKey = WebConstant.ssoSecretKey;
    	String tokenUrl = WebConstant.ssoGetTokenUrl; 
    	SipRestAccessAgentFactory sipRestAccessAgentFactory = new SipRestAccessAgentFactory(tokenUrl, account, pwd, secretKey);
        	SipRestAccessAgent agent = sipRestAccessAgentFactory.getInstance();
    	Map<String, String> agentMap = new HashMap<String, String>();
    	
    	//放入参数
    	agentMap.put("token", token);
    	agentMap.put("jsonpCallback", "");

    	String httpsJsonRtn = "";
    	String rcode = "";
    	String userId = "";
		try {
			httpsJsonRtn = agent.post(ssoUrl, agentMap);
			rtnMap = jacksonUtils.readJSON2Map(httpsJsonRtn);
			
			if (rtnMap.containsKey("rcode")) {
				rcode = rtnMap.get("rcode").toString();//操作结果
			}
			
			if (rtnMap.containsKey("userId")) {
				userId = rtnMap.get("userId").toString();//综合能源平台用户关联id
			}
			        
			
	        if ("00".equals(rcode)) {
	        	userBean = userService.getssoLoginUser(userId);
		        if(userBean != null){
		            if (userBean.getAccountStatus()== 1) {//正常状态的用户
		                //单点登陆默认展示管理者首页
		                String roleType = "2";
		                //保存session信息
		                frameService.saveSession(loginSessionKey, userBean,roleType,request,userBean.getLoginName());
		                resultMap.put("user",userBean.getLoginName() + "-" +userBean.getLoginPassword().substring(5) + "-" +roleType);
		                errorCode = 4;//登录成功
		                
		                //记录日志
		                Long accountId = super.getSessionUserInfo(request).getAccountId();
		                int type = 1;
		                String osVersion = BrowerUtil.checkBrowse(request.getHeader("user-agent"));
		                this.userAnalysisService.addAccountLogin(accountId,new Date(),type,osVersion);
		            }
		            else{//被禁用或者删除
		                errorCode = 3;
		            }
		        }
		        else{
		            //用户名有误
		            errorCode = 1;
		        }
			} else {
				errorCode = 1;
			}
		} catch (Exception e) {
			Log.error(this.getClass() + ".ssoLogin--无法单点登录");
		}
             
        resultMap.put("errorCode", errorCode);
        // 设置路径
        request.getSession().setAttribute("loginPath", getStrParam(request, "loginPath", "login"));
        //return resultMap;
   
        
        if (errorCode == 4 && userBean!=null) {
        	
        	Map<String, Object> map = frameService.getUserModules(userBean.getAccountId(), 2);
    		map.put("mapData", MapMapping.getMapMapping());
    		map.put("csrf", CSRFTokenManager.getTokenForSession(request.getSession()));
    		Long accountId = super.getSessionUserInfo(request).getAccountId();
    		Long ledgerId = 0L;
    		if (super.getSessionUserInfo(request).getLedgerId() == 0L) {
    			// 权限为群组分配ledgerId
    			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
    		} else {
    			ledgerId = super.getSessionUserInfo(request).getLedgerId();
    		}

    		LedgerBean ledger = new LedgerBean();
    		if (ledgerManagerService.selectByLedgerId(ledgerId) != null) {
    			ledger = ledgerManagerService.selectByLedgerId(ledgerId);
    		}
    		map.put("analyType", ledger.getAnalyType());
    		map.put("energyType", ledger.getEnergyType());
    		map.put("isGuestUser", (Boolean) request.getSession().getAttribute("isGuestUser"));
    		if (ledger.getAnalyType() != 102) {
    			List<LedgerBean> ledgerList = ledgerManagerService.getAllSubLedgersByLedgerId(ledgerId);
    			if (ledgerList != null && ledgerList.size() > 0) {
    				map.put("energyType", 1);
    				for (LedgerBean lb : ledgerList) {
    					if (lb.getEnergyType() == 2) {// 综合能源
    						map.put("energyType", 2);
    						break;
    					}
    				}
    			}
    		}

    		int hasCheck = 0;
    		if (ledger.getAnalyType() == 102 && super.getSessionUserInfo(request).getLedgerId() != 0) {
    			Integer score = this.phoneService.getLastAssessScore(super.getSessionUserInfo(request).getAccountId());
    			String value = "";
    			if (score != null) {
    				if (score >= 100) {
    					value = "满";
    				} else {
    					value = score.toString();
    				}
    			}
    			map.put("score", value);
    			hasCheck = 1;
    		}
    		map.put("hasCheck", hasCheck);
    		Integer roleType = super.getSessionRoleType(request);
    		map.put("roleType", roleType);
    		// 电工模式下，获取常用功能
    		if (roleType == 1) {
    			List<Map<String, Object>> moduleList = this.userAnalysisService
    					.getUsuallyUseModel(super.getSessionUserInfo(request).getAccountId());
    			map.put("moduleList", moduleList);
    		}
    		//进入首页
        	return new ModelAndView("energy/index/index",resultMap);
		}
        	//返回登录页
      		return new ModelAndView("/energy/framework/login","language","zh-cn");
    }
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/isPasswordCorrect", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> isPasswordCorrect(HttpServletRequest request,String password){
		boolean isSuccess = false;
		UserBean userBean = super.getSessionUserInfo(request);
		if(CommonMethod.isNotEmpty(password) && userBean != null){
			isSuccess = userService.isPasswordCorrect(userBean.getAccountId(),password);
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("flag",isSuccess);
		return map;
	}
	
	
	/**
	 * @return
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public @ResponseBody boolean changePassword(HttpServletRequest request,String password){
		boolean result=isValidCsrfHeaderToken(request);	
		boolean isSuccess = false;
		if(result){
			UserBean userBean = super.getSessionUserInfo(request);
			if(CommonMethod.isNotEmpty(password) && userBean != null){
				userBean.setLoginPassword(password);
				isSuccess = userService.updateUserInfo(userBean,userBean);
			}
		}
		return isSuccess;
	}
	
	private boolean isValidCsrfHeaderToken(HttpServletRequest request) {
        if (request.getHeader("__RequestVerificationToken") == null
                || request.getSession()
                        .getAttribute(CSRFTokenManager.CSRF_TOKEN_FOR_SESSION_ATTR_NAME) == null
                || !request
                        .getHeader("__RequestVerificationToken")
                        .equals(request.getSession()
                                .getAttribute(
                                        CSRFTokenManager.CSRF_TOKEN_FOR_SESSION_ATTR_NAME)
                                .toString())) {
            return false;
        }
        return true;
    }
	
	@RequestMapping(value = "/updateLastDate", method = RequestMethod.POST)
	public @ResponseBody boolean updateLastDate(HttpServletRequest request){
		boolean isSuccess = false;
		UserBean userBean = super.getSessionUserInfo(request);
		if(userBean != null){
			Date date = new Date();
			userBean.setLastDate(date);
			Long loginTimes = userBean.getLoginTimes();
			if(loginTimes == null) {
				loginTimes = 0L;
			}
			userBean.setLoginTimes(loginTimes+1);
			isSuccess = userService.updateLastDate(userBean);
		}
		
		return isSuccess;
	}
	
	/**
	 * 退出或者注销
	 * @param request HttpServletRequest对象
	 * @return
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request) {
		//local信息	
		Object local = request.getSession().getAttribute("localeValue");     
		Locale locale = request.getLocale(); String localeStr = null; if (locale != null) localeStr = locale.toString().trim(); 
		if (localeStr == null || localeStr.length()==0) localeStr = "zh-CN"; 
		if(local != null){
			 request.getSession().setAttribute("localeValue", local);
		}
		// 根据session判断返回页面路径
		String loginPath = null; Object obj_loginPath = request.getSession().getAttribute("loginPath");
		if(obj_loginPath != null) loginPath = obj_loginPath.toString().trim();
		if (null != loginPath && loginPath.length()==0) loginPath = "login";
		request.setAttribute("loginPath", loginPath);
		// session注销
		request.getSession().invalidate();
		return new ModelAndView("/energy/framework/login","language",localeStr);
	}
	

	
	/**
	 * 获取天气信息
	 * @return
	 */
	@RequestMapping(value = "/getWeatherInfos", method = RequestMethod.POST)
	public @ResponseBody Object getWeatherInfos(HttpServletRequest request){
		String Ip=IpAddrUtils.getIpAddr(request);
		// System.out.println(Ip);
		if(IpAddrUtils.isInnerIP(Ip)){
			try {
				Ip=IpAddrUtils.GetPublicIP();
			} catch (IOException e) {
				Log.info("getWeatherInfos error IOException");
			}
		}
		String cityName=IpAddrUtils.GetCityNameByIP(Ip);
		WeatherUtils instance = WeatherUtils.getInstance();
		return instance.getWeatherbyCityName(cityName);
	}
	
	
	/**
	 * 得到一个用户的父类分户树
	 * @return
	 */
	@RequestMapping(value = "/parentTree", method = RequestMethod.POST)
	public @ResponseBody List<LedgerTreeBean> getUserParentLedgerTree(HttpServletRequest request) {
		String legerId = request.getParameter("ledgerId");
		Boolean isEleTopo = CommonMethod.isNotEmpty(request.getParameter("isEleTopo"))?Boolean.valueOf(request.getParameter("isEleTopo")):false;
		if (CommonMethod.isNotEmpty(legerId)) {// 为了适应加入跟节点
			return addIconPath(frameService.getUserParentLedgerTree(Long.parseLong(legerId),isEleTopo), request);
		} else {
			UserBean userBean = super.getSessionUserInfo(request);
			if (userBean != null) {
				if (userBean.getLedgerId() != null && userBean.getLedgerId() != 0) {
					Long ledgerId = userBean.getLedgerId();
					if (userBean.getAccountId() == 1){
						ledgerId = -100L;// 超级管理员特殊处理
					}
					return addIconPath(frameService.getUserParentLedgerTree(ledgerId,isEleTopo), request);
				} else
					return addIconPath(frameService.getUserParentLedgerTreeGroup(userBean.getAccountId(),isEleTopo), request);
			}
		}
		return new ArrayList<LedgerTreeBean>();
	}
    
    /**
	 * 得到一个用户的父类分组树
	 * @return
	 */
	@RequestMapping(value = "/groupFirstParentTree", method = RequestMethod.POST)
	public @ResponseBody List<LedgerTreeBean> getUserGroupFirstParentTree(HttpServletRequest request) {
        Boolean isEleTopo = CommonMethod.isNotEmpty(request.getParameter("isEleTopo"))?Boolean.valueOf(request.getParameter("isEleTopo")):false;
        UserBean userBean = super.getSessionUserInfo(request);
        if (userBean != null) {
            int groupType = 1;//客户组
//            if(isEleTopo){
//                groupType = 2;//计量点组
//            }
            if(userBean.getAccountId() == null){
                return new ArrayList<LedgerTreeBean>();
            }
            
            //List<LedgerTreeBean> list = frameService.getUserGroupParentTree(userBean.getAccountId(),groupType,1,userBean,isEleTopo);
            
            return addIconPath(frameService.getUserGroupParentTree(userBean.getAccountId(),groupType,1,userBean,isEleTopo), request);
        }
		return new ArrayList<LedgerTreeBean>();
	}
    /**
	 * 得到一个用户的父类分组树
	 * @return
	 */
	@RequestMapping(value = "/groupSecondParentTree", method = RequestMethod.POST)
	public @ResponseBody List<LedgerTreeBean> getUserGroupSecondParentTree(HttpServletRequest request) {
        String groupId = request.getParameter("groupId");
        UserBean userBean = super.getSessionUserInfo(request);
        Boolean isEleTopo = CommonMethod.isNotEmpty(request.getParameter("isEleTopo"))?Boolean.valueOf(request.getParameter("isEleTopo")):false;
        if (CommonMethod.isNotEmpty(groupId)) {
            int groupType = 1;//客户组
//            if(isEleTopo){
//                groupType = 2;//计量点组
//            }
            return addIconPath(frameService.getUserGroupParentTree(Long.parseLong(groupId),groupType,2,userBean,isEleTopo), request);
        }
		return new ArrayList<LedgerTreeBean>();
	}
	
	/**
	 * 得到子类分户树信息
	 * @param parentLedgerId
	 * @return
	 */
	@RequestMapping(value = "/childTree", method = RequestMethod.POST)
	public @ResponseBody List<LedgerTreeBean> getChildLedgerTree(HttpServletRequest request,long parentLedgerId){
		return addIconPath(frameService.getChildLedgerTree(parentLedgerId),request);
	}
	
	/**
	 * 得到企业分户电力拓扑树信息
	 * parentLedgerId
	 * @return
	 */
	@RequestMapping(value = "/childEleTree", method = RequestMethod.POST)
	public @ResponseBody List<LedgerTreeBean> getChildEleTree(HttpServletRequest request,long parentId,int objType,Integer meterType){
		return addIconPath(frameService.getChildEleTree(parentId,objType,meterType),request);
	}
	
	/**
	 * 得到用户的tab子模块信息 
	 * @param parentModuleId 父模块id
	 * @return
	 */
	@RequestMapping("/showSearchTabPage")
	public ModelAndView showSearchTabPage(HttpServletRequest request,long parentModuleId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("tabModules",frameService.getUserTabModules(super.getSessionUserInfo(request).getAccountId(), parentModuleId));
		map.put("eleNotLoadModules",Arrays.asList(WebConstant.eleNotLoadModules));
		map.put("emoNotLoadModules",Arrays.asList(WebConstant.emoNotLoadModules));
		return new ModelAndView("/energy/dataquery/query_main",map);
	}
	
	/**
	 * 得到子类分户树信息
	 * @param
	 * @return
	 */
	@RequestMapping("/showCommonTree")
	public ModelAndView  showMeterPointTree(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("callBack",getStrParam(request,"callBack", "callBack"));
		map.put("treeNodeType",getStrParam(request,"treeNodeType",null));
		map.put("meterType",getStrParam(request,"meterType",null));
		map.put("ledgerId",getStrParam(request,"ledgerId",null));
		return new ModelAndView("/energy/tree/meter_point_tree",map);
	}
	
	/**
	 * 得到一个用户的父类分户树(不包含测量点)
	 * @return
	 */
	@RequestMapping(value = "/getParentLedgerTree", method = RequestMethod.POST)
	public @ResponseBody List<LedgerTreeBean> getParentLedgerTree(HttpServletRequest request){
		UserBean userBean = super.getSessionUserInfo(request);
		if(userBean != null && userBean.getLedgerId() != null){
			Long ledgerId = userBean.getLedgerId();
			if(userBean.getAccountId() == 1)
				ledgerId = -100L; //超级管理员特殊处理
			return addIconPath(frameService.getParentLedgerTree(ledgerId), request);
		}
		return new ArrayList<LedgerTreeBean>();
	}
	
	/**
	 * 得到子类分户树信息(不包含测量点)
	 * @param parentLedgerId
	 * @return
	 */
	@RequestMapping(value = "/getSubLedgerTree", method = RequestMethod.POST)
	public @ResponseBody List<LedgerTreeBean> getSubLedgerTree(HttpServletRequest request, long parentLedgerId){
		return addIconPath(frameService.getSubLedgerTree(parentLedgerId),request);
	}
	
	private List<LedgerTreeBean> addIconPath(List<LedgerTreeBean> list,HttpServletRequest request){
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
				if(CommonMethod.isNotEmpty(ledgerTreeBean.getIcon()))
					ledgerTreeBean.setIcon(request.getContextPath()+ledgerTreeBean.getIcon());
			}
		}
		return list;
	}	
	
	/**
	 * 查询区域树信息
	 * @user cehngq
	 * @return
	 */
	@RequestMapping(value = "/queryRegionInfo", method = RequestMethod.POST)
	public @ResponseBody List<RegionBean> queryRegionInfo(){
		return frameService.queryRegionInfo(null);
	}
    /**
     * 查询某个省或市下的区域树信息
     */
    @RequestMapping(value = "/queryOnePointRegion", method = RequestMethod.POST)
    public @ResponseBody List<RegionBean> queryOnePointRegion(HttpServletRequest request){
        String regionId = super.getStrParam(request, "regionId", "530000");
        return frameService.queryOnePointRegion(regionId);
    }
	/**
	 * 查询区域树信息
	 * @user cehngq
	 * @return
	 */
	@RequestMapping(value = "/queryIndustryInfo", method = RequestMethod.POST)
	public @ResponseBody List<IndustryBean> queryIndustryInfo(){
		return frameService.queryIndustryInfo(null);
	}
	
	/**
	 * 根据用户权限得到所有显示的分户和测量点数量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAllLedgerCount")
	public @ResponseBody int getAllLedgerCount(HttpServletRequest request) {
		UserBean userBean = super.getSessionUserInfo(request);
		return frameService.getAllLedgerCountByUser(userBean);
	}
	
	/**
	 * 取线损树数据
	 * 
	 * @param request
	 * @param ledgerId
	 * @return
	 */
	@RequestMapping(value = "/getLineLossTreeData", method = RequestMethod.POST)
	public @ResponseBody List<LineLossTreeBean> getLineLossTreeData(HttpServletRequest request, long ledgerId) {
		return frameService.getLineLossTreeData(ledgerId);
	}
	
	/**
	 * 保存线损树数据
	 * 
	 * @param request
	 * @param ledgerId
	 * @param linelossMeters
	 * @return
	 */
	@RequestMapping(value = "/saveLineLossTree", method = RequestMethod.POST)
	public @ResponseBody boolean saveLineLossTree(HttpServletRequest request, long ledgerId, String linelossMeters) {
        boolean result = true;
		try {
			List<LineLossTreeBean> meters = jacksonUtils.readJSON2Genric(request.getParameter("linelossMeters"),
				new TypeReference<List<LineLossTreeBean>>() {
			});
			frameService.saveLineLossRelation(ledgerId, meters);
		} catch (IOException e) {
			Log.info("saveLineLossTree error IOException");
            result = false;
		}

        //记录日志
        StringBuilder sb = new StringBuilder();
        sb.append("update a ledger electricity model:").append(this.ledgerManagerService.getLedgerDataById(ledgerId).getLedgerName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(result == true){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_ID_ELECTRICITY_MODEL, CommonOperaDefine.MODULE_NAME_ELECTRICITY_MODEL, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_149, 118l, 1);
		return result;
	}
	
	/**
	 * 得到企业下拉框能管单元或采集点
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLedgerSelectData")
	public @ResponseBody String getLedgerSelectData(HttpServletRequest request) {
		String str = "";
		StringBuffer sb = new StringBuffer("[");
		//标识符，1为查询采集点，2为能管单元
		Integer type = super.getIntParams(request, "type", 1);
        UserBean userBean = super.getSessionUserInfo(request);
		Long ledgerId = super.getLongParam(request, "ledgerId", userBean.getLedgerId());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if(ledgerId == 0){  //群组
            if (type == 2) {
                list = ledgerManagerService.getLedgersForGroup(userBean.getAccountId());
            }
            else if (type == 1) {
                list = ledgerManagerService.getMetersForGroup(userBean.getAccountId());
            }
        }
        else {  //非群组
            if (type == 2) {
                list = ledgerManagerService.getLedgersByLedgerId(ledgerId);
            }
            else if (type == 1) {
                list = ledgerManagerService.getMetersByLedgerId(ledgerId);
            }
        }

		//按照插件所需要的格式拼接字符串
		if(list != null && list.size() > 0) {
			for(Map<String, Object> dataInfo : list) {
				sb.append("{id:'").append(dataInfo.get("ID"))
					.append("',label:'").append(dataInfo.get("NAME"))
					.append("',value:'").append(dataInfo.get("NAME")).append("',num:'")
					.append(dataInfo.get("ID")).append("',count:'0'},");
			}	
			str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		} else
			str = "[]";
		return str;
	}

	/**
	 * 刷新验证码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/check")  
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        // 通知浏览器不要缓存  
        response.setHeader("Expires", "-1");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setHeader("Pragma", "-1");  
        ValidationCodeUtil util = ValidationCodeUtil.Instance();  
        // 将验证码输入到session中，用来验证  
        String code = util.getString();  
        request.getSession().setAttribute("code", code);  
        // 输出打web页面  
        ImageIO.write(util.getImage(), "jpg", response.getOutputStream());  
    }
	
	/** 
     * 验证码验证 
     * @param session 
     * @param code 
     */  
    private Integer checkCode(HttpSession session, String code) {  
        String codeSession = (String) session.getAttribute("code");  
        if (StringUtils.isEmpty(codeSession)) {  
        	//没有生成验证码
            return 1001;
        }  
        if (StringUtils.isEmpty(code)) {  
        	//未填写验证码
            return 1002;
        }  
        if (codeSession.equalsIgnoreCase(code)) {  
            return 1000;	//验证通过  
        } else {  
        	//验证码错误" 
            return 10000;
        }  
    }

    /**
     * 平台运营商首页：
     */
    @RequestMapping(value = "/getPlatAdminIndexData", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getPlatAdminIndexData(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        Long accountId = super.getSessionUserInfo(request).getAccountId();
        return this.indexService.getPlatAdminIndexData(ledgerId, accountId);
    }
    /**
     * 平台运营商首页：
     */
    @RequestMapping(value = "/getPlatAdminIndexEnergy", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getPlatAdminIndexEnergy(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        return this.indexService.getPlatAdminIndexEnergy(ledgerId);
    }
    /**
     * 平台运营商首页：企业在线数曲线
     */
    @RequestMapping(value = "/getOnlineCompanyList", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getOnlineCompanyList(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        return this.indexService.getOnlineCompanyList(ledgerId);
    }
    /**
     * 平台运营商首页：企业在线总数
     */
    @RequestMapping(value = "/getOnlineCompanys", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getOnlineCompanys(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        return this.indexService.getOnlineCompanys(ledgerId);
    }
    /**
     * 平台运营商首页：监测点在线数曲线
     */
    @RequestMapping(value = "/getOnlineMeterList", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getOnlineMeterList(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        return this.indexService.getOnlineMeterList(ledgerId);
    }
    /**
     * 平台运营商首页：监测点在线总数
     */
    @RequestMapping(value = "/getOnlineMeters", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getOnlineMeters(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        return this.indexService.getOnlineMeters(ledgerId);
    }
    /**
     * 平台运营商首页：运营商在线总数
     */
    @RequestMapping(value = "/getOnlinePlats", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getOnlinePlats(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        return this.indexService.getOnlinePlats(ledgerId);
    }
    /**
     * 平台运营商首页：上月、本月用电负荷
     */
    @RequestMapping(value = "/getLastAndThisMonQ", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getLastAndThisMonQ(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        return this.indexService.getLastAndThisMonQ(ledgerId);
    }
    /**
     * 平台运营商首页：实时功率
     */
    @RequestMapping(value = "/getRealTimeAp", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getRealTimeAp(HttpServletRequest request) {
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        return this.indexService.getRealTimeAp(ledgerId);
    }
	
	/**
	 * 查询设备类型树
	 * @user cehngq
	 * @return
	 */
	@RequestMapping(value = "/queryTypeInfo", method = RequestMethod.POST)
	public @ResponseBody List<TypeBean> queryTypeInfo(){
		return frameService.queryTypeInfo();
	}
	
	
	
	// add or update method by dingy
	// date 2018/12/10
	// Modify the content:
	/**
	 * 得到产污设备信息
	 * @user dingy
	 * @return
	 */
	@RequestMapping(value = "/queryPollutInfo", method = RequestMethod.POST)
	public @ResponseBody List<PollutBean> queryPollutInfo(){
		return frameService.queryPollut(null);
	}
	
	/**
	 * 得到治污设备信息
	 * @user dingy
	 * @return
	 */
	@RequestMapping(value = "/queryPollutctlInfo", method = RequestMethod.POST)
	public @ResponseBody List<PollutctlBean> queryPollutctlInfo(){
		List<PollutctlBean> pollutctlBeans = frameService.queryPollutctl( null );
		return frameService.queryPollutctl(null);
	}
	
	//end
	
}
