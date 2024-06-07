package com.linyang.energy.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.linyang.common.web.common.Log;
import com.linyang.energy.model.*;
import com.linyang.energy.service.*;
import com.linyang.energy.utils.*;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 小程序接口
 * 
 * @Description
 * @author Jijialu
 * @date 2018年5月7日 上午11:26:10
 */

@Controller
@RequestMapping("/miniAppInterface")
public class MiniAppController extends BaseController {

	@Autowired
	private PhoneService phoneService;
	@Autowired
	private LedgerManagerService ledgerManagerService;
	@Autowired
	private UserService userService;
	@Autowired
	private SuggestService suggestService;
	@Autowired
	private WxMaService wxService;// 微信小程序API
	@Autowired
	private WxMaMessageRouter router;// 消息路由
	@Autowired
	private MiniAppService miniAppService;
	@Autowired
	private UserAnalysisService userAnalysisService;

	/**
	 * 根据云终端地址获取小程序二维码
	 */
	// @RequestMapping(value = "/getWxQrcode")
	// public void getWxQrcode(HttpServletRequest request, HttpServletResponse
	// response) {
	// WxReturnModel returnModel = new WxReturnModel();
	// try {
	// // 二维码参数
	// String scene = getStrParam(request, "scene", "");
	// String page = getStrParam(request, "page", "");
	// int width = getIntParams(request, "width", 130);
	//
	// File qrcode = this.miniAppService.createWxCodeLimit(scene, page, width);
	//
	// // 生成成功
	// returnModel.setStatus(true);
	// returnModel.setResult(qrcode);
	//
	// System.out.println("请求二维码结果:" + qrcode.getPath());
	//
	// } catch (WxErrorException e) {
	// returnModel.setStatus(false);
	// returnModel.setErrorMessage(e.getError().toString());
	// System.out.println("获取小程序二维码失败:" + e.getError().toString());
	// }
	// // 结果返回
	// renderJson(response, GsonUtil.GsonString(returnModel));
	// }

	/**
	 * 微信服务器验证
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "/wxVerify")
	public void wxWerify(HttpServletRequest request, @RequestBody String requestBody, HttpServletResponse response)
			throws IOException {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 随机字符串
		String nonce = request.getParameter("nonce");
		// 时间戳
		String timestamp = request.getParameter("timestamp");

		// 获取输出流
		PrintWriter out = response.getWriter();

		// 参数检查
		if (StringUtils.isAnyBlank(signature, timestamp, nonce)) {
			out.print("参数不合法,验证失败");
			return;
		}

		// 消息签名检查
		if (!this.wxService.checkSignature(timestamp, nonce, signature)) {
			// 消息签名不正确，说明不是公众平台发过来的消息
			out.print("消息签名不正确,非法请求,验证失败");
			return;
		}

		// 随机字符串
		String echoStr = request.getParameter("echostr");
		// 通过检验signature对请求进行校验，若校验成功则原样返回echoStr，表示接入成功，否则接入失败
		if (!StringUtils.isBlank(echoStr)
				&& WxSignUtil.checkSignature(this.wxService.getWxMaConfig().getToken(), signature, timestamp, nonce)) {
			// 说明是一个仅仅用来验证的请求，回显echoStr,微信服务器认证完毕后运行
			out.print(echoStr);
			return;
		}

		// 获取传输类型（明文/加密）
		String encryptType = getStrParam(request, "encrypt_type", "");

		final boolean isJson = Objects.equals(this.wxService.getWxMaConfig().getMsgDataFormat(),
				WxMaConstants.MsgDataFormat.JSON);

		if (StringUtils.isBlank(encryptType)) { // 明文传输的消息
			WxMaMessage inMessage;
			if (isJson) {
				// JSON格式
				inMessage = WxMaMessage.fromJson(requestBody);
			} else {
				// XML格式
				inMessage = WxMaMessage.fromXml(requestBody);
			}
			this.router.route(inMessage);
			return;
		} else if ("aes".equals(encryptType)) {// AES加密的消息
			WxMaMessage inMessage;
			if (isJson) {
				// JSON格式
				inMessage = WxMaMessage.fromEncryptedJson(requestBody, this.wxService.getWxMaConfig());
			} else {
				// XML格式,获取加密秘钥
				String msgSignature = getStrParam(request, "msg_signature", "");
				inMessage = WxMaMessage.fromEncryptedXml(requestBody, this.wxService.getWxMaConfig(), timestamp, nonce,
						msgSignature);
			}
			this.router.route(inMessage);
			return;
		} else {
			throw new RuntimeException("不可识别的加密类型：" + encryptType);
		}
	}

	/**
	 * 小程序登陆接口,获取信用户信息(sessionKey + openId)
	 */
	@RequestMapping(value = "/wxLogin")
	public void wxLogin(HttpServletRequest request, HttpServletResponse response) {
		WxReturnModel returnModel = new WxReturnModel();
		try {
			// 获取临时登录凭证(code，有效期五分钟)
			String jsCode = getStrParam(request, "code", "");
			if (!StringUtils.isBlank(jsCode)) {
				// 根据临时登录凭证，换取微信用户信息(sessionKey + openId)
				WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(jsCode);
				// 获取成功
				returnModel.setStatus(true);
				returnModel.setResult(session);
			} else {
				returnModel.setErrorMessage("请求参数不合法");
			}
		} catch (WxErrorException e) {
			returnModel.setErrorMessage(e.getMessage());
		}
		// 结果返回
		renderJson(response, GsonUtil.GsonString(returnModel));
	}

	/**
	 * 进入云终端电量查询页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/gotoQueryData")
	public ModelAndView gotoQueryData(HttpServletRequest request) {
		String terAddressStr = getStrParam(request, "terAddress", "");// 用户名
		String password = getStrParam(request, "password", "");// 密码
		Long terId = getLongParam(request, "terId", 0l);// 终端id
		String shareDate = getStrParam(request, "shareDate", "");// 分享时间
		Integer shareLimit = getIntParams(request, "shareLimit", 0);// 分享限制时间间隔

		if (terId == 0l || "".equals(terAddressStr) || "".equals(password)) {
			return new ModelAndView("miniProgram/error");
		}

		Long terAddress = Long.valueOf(terAddressStr);
		UserTerminalBean userTerminalBean = null;
		userTerminalBean = phoneService.checkTerPassword(terAddress, password);
		if (userTerminalBean == null) {
			return new ModelAndView("miniProgram/error");
		}

		// 获取计量点id
		Long objId = phoneService.getMeterIdByTerId(terId);
		if (objId == null)
			return new ModelAndView("miniProgram/error");
	
		// 测试用1432005405450 1528682656297l
		//Long objId = 1432005405450l;
		//获取示值
		Double faeValue = phoneService.getFaeValue(objId);
		
		// 分享状态下无需验证用户
		if (!"".equals(shareDate) && shareLimit != 0) {
			long nowDateL = DateUtil.convertStrToDate(DateUtil.getCurrentDateStr()).getTime();
			long limitDateL = DateUtil.addDateDay(DateUtil.convertStrToDate(shareDate), Integer.valueOf(shareLimit))
					.getTime();
			if (nowDateL > limitDateL) {
				return new ModelAndView("miniProgram/share_error");
			}
		}

		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		request.setAttribute("objId", objId);
		request.setAttribute("fae", faeValue);//示值
		request.setAttribute("terAddress", terAddressStr);//终端地址
		request.setAttribute("baseDate", baseDate); // 默认今天
		request.setAttribute("dateType", 1); // 1 日,4 月,5 年
		return new ModelAndView("miniProgram/terminal_query");
	}

	/**
	 * 查询电量
	 * 
	 * @param request
	 * @param objId
	 * @param baseDate
	 * @param dateType
	 * @return
	 */
	@RequestMapping(value = "/queryData")
	public @ResponseBody Map<String, Object> queryData(final HttpServletRequest request, long objId,
			String baseDate, int dateType) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		//Map<String, Object> dataMap = new HashMap<String, Object>();
		Date sDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		//List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> chartList = new ArrayList<Map<String, Object>>();

		// 查询电量
		chartList = phoneService.getEnergyData(objId, 2, sDate, dateType);
		// 电量汇总
		double energySum = 0;
		for (Map<String, Object> map : chartList) {
			Object objData = map.get("DATA");
			if (objData != null)
				energySum = DataUtil.doubleAdd(energySum, Double.parseDouble(objData.toString()));
		}
		resultMap.put("energySum", new DecimalFormat("0.00").format(energySum));

		// 判断是否为VIP
		//list2 = phoneService.getPhoneVIPData(1, objId, 2, baseDate, dateType, list2);
		
		// 返回数据
		//list.add(dataMap);
		if (chartList != null & chartList.size() > 0){
			resultMap.put("hasData", true);
			if (dateType == 1)		
				resultMap.put("tableList", handleData(chartList));
			
			resultMap.put("chartList", chartList);
		}
		return resultMap;
	}
	
	/**
	 * 缺点补齐数据
	 * @param list
	 * @return
	 */
	private List<Map<String, Object>> handleData(List<Map<String, Object>> list){
		List<Map<String, Object>> chartList = new ArrayList<Map<String, Object>>();
		for (int i = 1; i < 25; i++) {
			String chartDate = "";
			if (i < 10) {
				chartDate = "0" + i + ":00";
			} else if (i == 24) {
				chartDate = "00:00";
			} else {
				chartDate = i + ":00";
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("DATA", "-");
			map.put("DATATIME", chartDate);
			chartList.add(map);
		}

		for (int i = 0; i < chartList.size(); i++) {
			for (Map<String, Object> map : list) {
				String date = map.get("DATATIME").toString().substring(11, 16);
				String chartDate = chartList.get(i).get("DATATIME").toString();
				if (chartDate.equals(date)) {
					chartList.get(i).put("DATA", map.get("DATA"));
				}
			}
		}
		return chartList;
	}

	/**
	 * 获取资源文件(首页广告列表banner/公众号二维码qrcode)
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getResources")
	public void getResources(HttpServletRequest request, HttpServletResponse response) {
		WxReturnModel returnModel = new WxReturnModel();
		// 获取请求的资源类型
		String resType = getStrParam(request, "resourceType", "");

		// 参数验证
		if (!StringUtils.isBlank(resType)) {
			// 获取指定资源文件夹(暂时为/miniProgram/resources/)
			String folderPath = request.getSession().getServletContext()
					.getRealPath(WxConstant.WX_RESOURCES_PATH + resType);

			File folder = new File(folderPath);
			if (null != folder && folder.exists()) {
				// 设置映射的资源路径(服务器域名/resources/资源文件类型/文件名.扩展名)
				String resourcePath = WxConstant.WX_SERVER + "/resources/" + resType + "/";
				// 遍历文件夹，获取所有文件列表
				List<String> fileList = new ArrayList<String>();
				File list[] = folder.listFiles();
				for (File file : list) {
					if (file.isFile()) {
						fileList.add(resourcePath + file.getName());
					}
				}
				// 未获取到文件
				if (fileList.size() != 0) {
					returnModel.setResult(fileList);
					returnModel.setStatus(true);
				} else {
					returnModel.setErrorMessage("资源文件不存在");
				}
			} else {
				returnModel.setErrorMessage("资源文件夹不存在");
			}
		} else {
			returnModel.setErrorMessage("该类型资源不存在:" + resType);
		}

		// 结果返回
		renderJson(response, GsonUtil.GsonString(returnModel));
	}

	/**
	 * 激活绑定终端
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/terminalBind")
	public void terminalBind(HttpServletRequest request, HttpServletResponse response) {

		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		String openId = getStrParam(request, "openId", "");// 微信openid
		String terAddressStr = getStrParam(request, "terAddress", "");// 终端地址
		Long terAddress = Long.valueOf(terAddressStr);

		UserTerminalBean userTerminal = new UserTerminalBean();
		Integer activeStatus = -1;//激活状态

		if ("".equals(openId) || terAddress == 0l) {
			status = false;
			errorMsg = "绑定异常";
		} else {
			status = true;

			// 获取终端激活状态
			Integer active = phoneService.getTerActiveStatus(terAddress);			
			if (active != null) {
				activeStatus = active;
			}
			
			if (activeStatus == 0) {// 进行激活并建立分户，计量点
				status = true;

				userTerminal.setOpenId(openId);
				userTerminal.setTerminalAddress(terAddress);
				// 先更新终端的激活状态
				phoneService.updateTerActiveStatus(terAddress);
				// 将激活的终端插入采集平台终端表
				Long terId = phoneService.addTerminal(terAddress);// 生成的终端id
				userTerminal.setTerminalId(terId);

				// 建立分户
				LedgerBean ledgerBean = new LedgerBean();
				Long ledgerId = SequenceUtils.getDBSequence();
				ledgerBean.setParentLedgerId(WebConstant.terParentLedgerId);
				ledgerBean.setLedgerId(ledgerId);
				ledgerBean.setLedgerName(terAddress.toString());
				short nop = 1;
				ledgerBean.setNumberOfPeople(nop);
				ledgerBean.setUseArea(1);
				ledgerBean.setRateId(0l);
				ledgerBean.setThresholdId(1);
				ledgerBean.setThresholdValue("0.90");
				ledgerBean.setAnalyType(102);
				ledgerBean.setLedgerType(1);
				ledgerBean.setEnergyType(1);
				ledgerBean.setCollmeterNumber(1);
				// 插入分户信息
				ledgerManagerService.insertBySelective(ledgerBean);

				phoneService.addMeterRelation(terId, terAddress, ledgerId);// 插入计量点信息及关联关系

				// 建立用户
				UserBean userBean = new UserBean();
				Long accountId = SequenceUtils.getDBSequence();
				userBean.setAccountId(accountId);
				userBean.setLoginPassword("111111");
				userBean.setLoginName(terAddress.toString());
				userBean.setRealName(terAddress.toString());
				short accountStatus = 1;
				userBean.setAccountStatus(accountStatus);
				userBean.setRoleId(WebConstant.terRoleId);
				userBean.setLedgerId(ledgerId);
				// 插入用户
				userService.addUserInfo(userBean);

				userTerminal.setAccountId(accountId);
				userTerminal.setLedgerId(ledgerId);

				// 绑定终端
				phoneService.bindTerminal(userTerminal.getOpenId(), userTerminal.getTerminalId(), ledgerId, accountId);
				// 插入微信信息表,首次创建的用户作为反馈信息账户(默认为0)
				phoneService.addOpenInfo(userTerminal.getOpenId(), 0l);
				errorMsg = "激活绑定成功";
			} else if (activeStatus == 1) {
				UserTerminalBean bean = phoneService.getTerminalInfo(terAddress);
				if (openId.equals(bean.getOpenId())) {
					errorMsg = "该终端已被您绑定";
				} else {
					errorMsg = "该终端已被其他用户绑定";
				}
				status = false;
			} else {
				errorMsg = "该终端地址有误";
				status = false;
			}
		}

		// json返回
		returnModel.setStatus(status);
		returnModel.setErrorMessage(errorMsg);
		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".terminalBind()", e);
		}

	}

	/**
	 * 获取微信用户信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getOpenInfo")
	public void getOpenInfo(HttpServletRequest request, HttpServletResponse response) {
		String openId = getStrParam(request, "openId", "");// 微信openid
		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		UserTerminalBean userTerminalBean = phoneService.getOpenInfo(openId);
		if (!"".equals(openId) && userTerminalBean != null) {
			errorMsg = "获取成功";
			returnModel.setResult(userTerminalBean);
		} else {
			errorMsg = "获取异常";
			status = false;
		}

		returnModel.setStatus(status);
		returnModel.setErrorMessage(errorMsg);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".getOpenInfo()", e);
		}
	}

	/**
	 * 更新微信用户信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateOpenInfo")
	public void updateOpenInfo(HttpServletRequest request, HttpServletResponse response) {
		String openId = getStrParam(request, "openId", "");// 微信openid
		String companyName = getStrParam(request, "companyName", "");// 微信openid
		String tel = getStrParam(request, "tel", "");// 微信openid
		String address = getStrParam(request, "address", "");// 微信openid
		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		//String errorMsg = "";

		UserTerminalBean userTerminalBean = phoneService.getOpenInfo(openId);
		//存在用户信息时更新用户信息
		if (!"".equals(openId) && userTerminalBean != null) {
			userTerminalBean = phoneService.updateOpenInfo(openId, companyName, tel, address);
			returnModel.setResult(userTerminalBean);
		} else {
			//不存在则插入账户
			phoneService.addOpenInfo(openId, 0l);
		}

		returnModel.setStatus(status);
		//returnModel.setErrorMessage(errorMsg);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".updateOpenInfo()", e);
		}
	}

	/**
	 * 获取终端信息列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getTerminalInfoList")
	public void getTerminalInfoList(HttpServletRequest request, HttpServletResponse response) {
		String openId = getStrParam(request, "openId", "");// 微信openid
		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		if ("".equals(openId)) {
			errorMsg = "获取异常";
			status = false;
		} else {
			List<UserTerminalBean> userTerList = phoneService.getTerminalInfoList(openId);
			errorMsg = "获取成功";
			returnModel.setResult(userTerList);
		}

		returnModel.setStatus(status);
		returnModel.setErrorMessage(errorMsg);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".getOpenInfo()", e);
		}
	}

	/**
	 * 更新终端信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateTerminalInfo")
	public void updateTerminalInfo(HttpServletRequest request, HttpServletResponse response) {
		String openId = getStrParam(request, "openId", "");// 微信openid
		Long terId = getLongParam(request, "terId", 0l);// 终端id
		String terName = getStrParam(request, "terName", "");// 终端名称
		Integer shareLimit = getIntParams(request, "shareLimit", 0);// 终端密码
		Integer pt = getIntParams(request, "pt", 0);// 电压互感器倍率
		Integer ct = getIntParams(request, "ct", 0);// 电压互感器倍率

		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		if ("".equals(openId)) {
			errorMsg = "操作异常";
			status = false;
		} else {
			errorMsg = phoneService.updateTerminalInfo(terId, terName, shareLimit, pt, ct);
			if (!"".equals(errorMsg)) {
				status = false;
			}
		}

		returnModel.setStatus(status);
		returnModel.setErrorMessage(errorMsg);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".getOpenInfo()", e);
		}
	}

	/**
	 * 解除激活绑定
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/removeBind")
	public void removeBind(HttpServletRequest request, HttpServletResponse response) {
		String openId = getStrParam(request, "openId", "");// 微信openId
		Long terId = getLongParam(request, "terId", 0l);// 终端id

		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		if ("".equals(openId) || terId == 0l) {
			errorMsg = "操作异常";
			status = false;
		} else {
			Long ledgerId = phoneService.removeBind(terId);
			// 删除分户操作
			ledgerManagerService.deleteByLedgerId(ledgerId);
			errorMsg = "操作成功";
		}

		returnModel.setStatus(status);
		returnModel.setErrorMessage(errorMsg);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".getOpenInfo()", e);
		}
	}

	/**
	 * 更新终端密码
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateTerminalPassword")
	public void updateTerminalPassword(HttpServletRequest request, HttpServletResponse response) {
		String openId = getStrParam(request, "openId", "");// 微信openid
		String password = getStrParam(request, "password", "");// 密码
		Long terId = getLongParam(request, "terId", 0l);// 终端id

		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		if ("".equals(openId) || terId == 0l || "".equals(password)) {
			errorMsg = "操作异常";
			status = false;
		} else {
			// 更改密码
			phoneService.updateTerminalPassword(terId, password);
			errorMsg = "操作成功";
		}

		returnModel.setStatus(status);
		returnModel.setErrorMessage(errorMsg);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".getOpenInfo()", e);
		}
	}

	/**
	 * 非绑定用户登陆
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/unUserLogin")
	public void unUserLogin(HttpServletRequest request, HttpServletResponse response) {
		String terAddressStr = getStrParam(request, "terAddress", "");// 终端地址
		String password = getStrParam(request, "password", "");// 密码

		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		if ("".equals(terAddressStr) || "".equals(password)) {
			errorMsg = "操作异常";
			status = false;
		} else {
			UserTerminalBean userTerminalBean = null;
			Long terAddress = Long.valueOf(terAddressStr);
			userTerminalBean = phoneService.checkTerPassword(terAddress, password);
			if (userTerminalBean == null) {
				errorMsg = "密码错误";
				status = false;
			} else {
				returnModel.setResult(userTerminalBean);
			}
		}

		returnModel.setStatus(status);
		returnModel.setErrorMessage(errorMsg);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".getOpenInfo()", e);
		}
	}

	/**
	 * 获取终端信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getTerminalInfo")
	public void getTerminalInfo(HttpServletRequest request, HttpServletResponse response) {
		String terAddressStr = getStrParam(request, "terAddress", "");// 终端地址

		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		if ("".equals(terAddressStr)) {
			errorMsg = "操作异常";
			status = false;
		} else {
			Long terAddress = Long.valueOf(terAddressStr);
			UserTerminalBean userTerminalBean = phoneService.getTerminalInfo(terAddress);
			errorMsg = "获取成功";
			returnModel.setResult(userTerminalBean);
		}

		returnModel.setStatus(status);
		returnModel.setErrorMessage(errorMsg);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".getOpenInfo()", e);
		}
	}

	/**
	 * 获取对话记录(聊天窗口) 小程序只有accountId以及pageNo
	 */
	@RequestMapping(value = "/queryChatRecordForApp")
	public void queryChatRecordForApp(HttpServletRequest request, HttpServletResponse response) {
		List<ReplyBean> result = new ArrayList<ReplyBean>();
		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		long accountId = getLongParam(request, "accountId", 0l);
		int pageNo = getIntParams(request, "pageNo", 0);
		String openId = getStrParam(request, "openId", "");
		Long sugId = 0l;
		try {
			if (accountId == 0 && openId != null && !openId.equals("")) {
				if(suggestService.toViewUserSugIdIsExist(openId) != null)
					sugId = suggestService.toViewUserSugIdIsExist(openId);
			} else if (accountId != 0l && suggestService.toViewUserSugIdIsExist(accountId) != null
					&& suggestService.toViewUserSugIdIsExist(accountId) != 0l)
				sugId = suggestService.toViewUserSugIdIsExist(accountId);
			result = suggestService.getChatRecord(accountId,openId,sugId, pageNo);

			if (result != null && result.size() > 0) {
				returnModel.setResult(result);
			} else {
				status = false;
				errorMsg = "暂无数据";
				returnModel.setErrorMessage(errorMsg);
			}
		} catch (Exception e) {
			status = false;
			errorMsg = "操作失败";
			returnModel.setErrorMessage(errorMsg);
		}
		returnModel.setStatus(status);
		// 修改record和reply表的status
		// suggestService.updateStatus(sugId, accountId);

		try {
			// 转为json字符串
			String returnModelStr = GsonUtil.GsonString(returnModel);
			renderJson(response, returnModelStr);
		} catch (Exception e) {
			Log.error(this.getClass().getName() + ".queryChatRecordForApp()", e);
		}
	}
	

	/**
	 * 小程序插入
	 * 
	 * @param request
	 * @return web端插入时需要sugid,小程序不需要
	 */
	@RequestMapping(value = "/interPositionRecord", method = RequestMethod.POST)
	public void interpositionRecord(HttpServletRequest request, HttpServletResponse response) {
		WxReturnModel returnModel = new WxReturnModel();
		boolean status = true;
		String errorMsg = "";

		long accountId = getLongParam(request, "accountId", 0l);
		String MSG = getStrParam(request, "MSG", "");
		String openId = getStrParam(request, "openId", "");

		List<UserTerminalBean> terminalInfoList = phoneService.getTerminalInfoList(openId);

		Long sugId = 0l;
		if (suggestService.toViewUserSugIdIsExist(openId) != null
				&& suggestService.toViewUserSugIdIsExist(openId) != 0l)
			sugId = suggestService.toViewUserSugIdIsExist(openId);

		Object interpositionRecord = null;
		if (terminalInfoList.size() > 0) {
			interpositionRecord = suggestService.interpositionRecord(accountId, MSG, openId);
		}

		if (interpositionRecord != null) {
			returnModel.setResult(interpositionRecord);
			suggestService.updateStatus(sugId, accountId);
		} else {
			errorMsg = "插入失败";
			status = false;
			if(terminalInfoList == null || terminalInfoList.size() <= 0){
				errorMsg = "未注册的用户";
				status = false;
			}
			returnModel.setErrorMessage(errorMsg);
		}

		returnModel.setStatus(status);

		try {
			// 转为json字符串
			String returnModelStr = jacksonUtils.beanToJson(returnModel);
			renderJson(response, returnModelStr);
		} catch (IOException e) {
			Log.error(this.getClass().getName() + ".queryChatRecordForApp()", e);
		}
	}

	/**
	 * 批量生成生成二维码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/createQRCode")
	public void createQRCode(HttpServletRequest request, HttpServletResponse response) {
		Integer num = getIntParams(request, "num", 0);// 批量生成数量
		String widthStr = getStrParam(request, "width", "");
		Integer width = 100;// 默认宽度

		if (!"".equals(widthStr)) {
			width = Integer.valueOf(widthStr);
		}

		// 生成唯一id
		Long cloudId = SequenceUtils.getDBSequence();
		List<Long> terAddressList = phoneService.createTerAddress(num, cloudId);

		String path = WebConstant.QRpath;// 定义指定文件路径
		String newPath = path + "/" + cloudId.toString();// 指定新路径
		File file = new File(newPath);// 定义一个文件流
		file.mkdirs();// 创建文件夹

		for (Long address : terAddressList) {
			try {
				this.miniAppService.createWxCodeLimit(file.getAbsolutePath(),
						StringUtil.fillWith(address.toString(), '0', 10, false), "", width);
			} catch (WxErrorException e) {
				e.printStackTrace();
			}
		}

		try {
			File zipFile = LoadZipUtil.createZip(file.getAbsolutePath(), path, cloudId.toString());
			// 如果图片名称是中文需要设置转码
			response.reset();
			response.setCharacterEncoding("GBK");
			response.setContentType("application/x-download");// 设置为下载application/x-download
			response.setHeader("content-disposition",
					"attachment;fileName=" + URLEncoder.encode("QRCode", "GBK") + ".zip");
			InputStream reader = null;
			OutputStream out = null;
			byte[] bytes = new byte[1024];
			int len = 0;
			try {
				// 读取文件
				reader = new FileInputStream(zipFile.getPath());
				// 写入浏览器的输出流
				out = response.getOutputStream();
				while ((len = reader.read(bytes)) > 0) {
					out.write(bytes, 0, len);
					out.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					reader.close();
				}
				if (out != null)
					out.close();
				try {
					// 删除压缩文件
					LoadZipUtil.delFile(zipFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_137, 229l, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 服务信息及详情4个页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/gotoServiceInfo")
	public ModelAndView gotoServiceInfo(HttpServletRequest request) {
		return new ModelAndView("miniProgram/service_info");
	}

	@RequestMapping("/gotoInfoDetail1")
	public ModelAndView gotoInfoDetail1(HttpServletRequest request) {
		return new ModelAndView("miniProgram/service_info_detail01");
	}

	@RequestMapping("/gotoInfoDetail2")
	public ModelAndView gotoInfoDetail2(HttpServletRequest request) {
		return new ModelAndView("miniProgram/service_info_detail02");
	}

	@RequestMapping("/gotoInfoDetail3")
	public ModelAndView gotoInfoDetail3(HttpServletRequest request) {
		return new ModelAndView("miniProgram/service_info_detail03");
	}

	@RequestMapping("/gotoInfoDetail4")
	public ModelAndView gotoInfoDetail4(HttpServletRequest request) {
		return new ModelAndView("miniProgram/service_info_detail04");
	}

	@RequestMapping("/gotoInfoDetail5")
	public ModelAndView gotoInfoDetail5(HttpServletRequest request) {
		return new ModelAndView("miniProgram/service_info_detail05");
	}
	
	/**
	 * 更多信息跳转页
	 * @param request
	 * @return
	 */
	@RequestMapping("/gotoMoreInfo")
	public ModelAndView gotoMoreInfo(HttpServletRequest request) {
		String terAddress = getStrParam(request, "terAddress", "");// 用户名,终端地址
		request.setAttribute("terAddress", terAddress);
		return new ModelAndView("miniProgram/more_info");
	}
	
	/**
	 * 二维码生成页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/gotoQR")
	public ModelAndView gotoQRCode(HttpServletRequest request) {
		return new ModelAndView("miniProgram/qr_code");
	}
}
