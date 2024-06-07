/**
 */
package com.linyang.energy.controller.phone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.common.Log;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.LineLossTreeBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.MeterManagerService;
import com.linyang.energy.service.PhoneService;
import com.linyang.energy.service.ServePhoneService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.service.UserService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.SequenceUtils;

/**
 * 运维APP接口
 * @author chengq
 * @date 2015-12-23 下午02:04:47
 * @version 1.0
 */
@Controller
@RequestMapping("/servePhoneInterface")
public class ServePhoneController extends BaseController {

	@Autowired
	private ServePhoneService servephoneService;
	
	@Autowired
	private PhoneService phoneService; 
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	@Autowired
	private LedgerManagerService ledgerManagerService;
	
	@Autowired
	private MeterManagerService meterManagerService;
	
	/**
	 * 用户登录接口
	 * @param type 登录软件类型：1表示Web，2表示IOSApp，3表示安卓App
	 * @param osVersion 操作系统版本
	 * @param userName 登录用户名称
	 * @return Map<String, Object>包括loginFlag登陆是否成功  accountId登陆用户Id firstFlag是否首次登陆  errorCode登陆失败错误码
	 */
	@RequestMapping(value = "/login")
	public @ResponseBody Map<String, Object> login(HttpServletRequest request, Integer type,
			String osVersion, String userName, String password) {
		boolean loginFlag = false;
		int errorCode = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = phoneService.getUserByUserName(userName);
		if (userBean != null && userBean.getAccountStatus() == 1) {
			if (userBean.getLoginPassword().equals(password)) {
				loginFlag = true;
				// 更新用户登陆记录
				if (userBean.getLastDate() != null) {
					userBean.setLastDate(new Date());
					Long loginTimes = userBean.getLoginTimes() == null ? 1L
							: userBean.getLoginTimes() + 1;
					userBean.setLoginTimes(loginTimes);
					userService.updateLastDate(userBean);
				}
				if (type != null && type > 0 && osVersion != null) { // type:1表示Web，2表示IOSApp，3表示安卓App
					this.userAnalysisService.addAccountLogin(userBean
							.getAccountId(), new Date(), type, osVersion);
				}
			} else {
				errorCode = 1;
			}
			map.put("firstFlag", userBean.getLastDate() == null ? true : false);
		} else {
			errorCode = 2;
		}
		map.put("loginFlag", loginFlag);
		map.put("errorCode", errorCode);
		map.put("accountId", (userBean == null ? -1 : userBean.getAccountId()));
		return map;
	}
	
	/**
	 * 加载预加载信息
	 * @param request
	 * @param type 预加载数据类型： 0所属区域 ,1企业列表,2使用费率,3终端列表,4采集点列表,5计量点列表(某分户下),6电表信息
	 * @param 
	 * @return Map<String,Object>包括flag是否成功标识, data获取的数据
	 */
	@RequestMapping(value = "/getLoadingInfo")
	public @ResponseBody Map<String,Object> getLoadingInfo(HttpServletRequest request){
		UserBean userBean = userVerification(request);
		if (userBean == null)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> queryMap;
		boolean isSuccess = false;
		long userLedgerId;
		int type = getIntParams(request, "type", -1);
		switch (type) {
		case 0: // 参数： type=0
			queryMap = new HashMap<String, Object>();
			userLedgerId = userBean.getLedgerId();
			if(userLedgerId > 0) {
				queryMap.put("ledgerId", userLedgerId); // 用户权限限制
			} else {
				queryMap.put("accountId", userBean.getAccountId()); // 群组权限限制
			}
			queryMap.put("ledgerName", getStrParam(request, "keyword", "")); // 模糊查询
			// 过滤多余字段
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
			List<LedgerBean> regionList = servephoneService.getRegionListByParam(queryMap);
			for (LedgerBean ledgerBean : regionList) {
				Map<String,Object> ledgerMap = new HashMap<String, Object>();
				ledgerMap.put("ledgerId", ledgerBean.getLedgerId());
				ledgerMap.put("ledgerName", ledgerBean.getLedgerName());
				resultList.add(ledgerMap);
			}
			map.put("data", resultList);
			isSuccess = true;
			break;
		case 1: // 参数： type=1,keyword=?
			queryMap = new HashMap<String, Object>();
			userLedgerId = userBean.getLedgerId();
			if(userLedgerId > 0) {
				queryMap.put("ledgerId", userLedgerId); // 用户权限限制
			} else {
				queryMap.put("accountId", userBean.getAccountId()); // 群组权限限制
			}
			queryMap.put("ledgerName", getStrParam(request, "keyword", "")); // 模糊查询
			queryMap.put("analyType", getIntParams(request, "analyType", 102));
			// 过滤多余字段
			List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			List<LedgerBean> ledgerList = ledgerManagerService.getLedgerListByParam(queryMap);
			for (LedgerBean ledgerBean : ledgerList) {
				Map<String,Object> ledgerMap = new HashMap<String, Object>();
				ledgerMap.put("ledgerId", ledgerBean.getLedgerId());
				ledgerMap.put("ledgerName", ledgerBean.getLedgerName());
				dataList.add(ledgerMap);
			}
			map.put("data", dataList);
			isSuccess = true;
			break;
		case 2: // 参数： type=2
			map.put("data", ledgerManagerService.queryRateInfo(new Date()));
			isSuccess = true;
			break;
		case 3: // 参数： type=3
			map.put("data", meterManagerService.getTerminalData());
			isSuccess = true;
			break;
		case 4: // 参数： type=4,terminalId=?
			queryMap = new HashMap<String, Object>();
			queryMap.put("terminalId", getLongParam(request, "terminalId", -1));
			queryMap.put("powerType", getIntParams(request, "powerType", 1)); // 默认为电表
			map.put("data", meterManagerService.getMpedDataByTerId(queryMap));
			isSuccess = true;
			break;
		case 5: // 参数： type=5,ledgerId=?,keyword=?
			queryMap = new HashMap<String, Object>();
			queryMap.put("meterName", getStrParam(request, "keyword", "")); // 模糊查询
			queryMap.put("ledgerId", getLongParam(request, "ledgerId", -1)); // 所属企业
			queryMap.put("meterLimit", getLongParam(request, "meterId", -1)); // 非该电表自身以及其子节点
			map.put("data", meterManagerService.queryMeterList(queryMap));
			isSuccess = true;
			break;
		case 6:// 参数：type=6,meterId=?
			map.put("data", meterManagerService.getMeterDataById(getLongParam(request, "meterId", -1)));
			isSuccess = true;
			break;
		default:
			break;
		}
		map.put("flag", isSuccess);
		return map;
	}
	
	/**
	 * 保存企业信息
	 * @param request
	 * @param paramInfo,企业参数信息,JSON字符串
	 * @return Map<String,Object>,包括flag是否成功标识  errorCode 0成功  1重名 2保存操作失败 
	 */
	@RequestMapping(value = "/saveNewCompany")
	public @ResponseBody Map<String,Object> saveNewCompany(HttpServletRequest request){
		UserBean userBean = userVerification(request);
		if (userBean == null)
			return null;
		Map<String,Object> map = new HashMap<String, Object>();
		boolean isSuccess = false;
		StringBuilder sb = new StringBuilder();
		LedgerBean ledger = null;
		int errorCode = 0;
		try {
			ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, getStrParam(request, "paramInfo", ""));
			// 验证名称是否重复
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("ledgerName", ledger.getLedgerName());
			queryMap.put("parentId",ledger.getParentLedgerId());
			queryMap.put("ledgerId", 0);
			isSuccess = ledgerManagerService.checkLedgerName(queryMap);
			if(!isSuccess){
				errorCode = 1; 
			} else {
				// 保存企业信息
				ledger.setAnalyType(102);
				ledger.setAddAttr(3);
				ledger.setThresholdId(1);
				ledgerManagerService.insertBySelective(ledger);
				isSuccess = true;
			}
		} catch (IOException e) {
			errorCode = 2;
			ledger = new LedgerBean();
			Log.info("saveNewCompany error IOException");
		}
		sb.append(" add ledger , ledgerId is ").append(ledger.getLedgerId());
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess)
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		OptLogBean optLogBean = new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString());
		optLogBean.setOptId(userBean.getAccountId());
		optLogBean.setOptName(userBean.getLoginName());
		super.writePhoneLog(optLogBean, request);
		map.put("flag", isSuccess);
		map.put("errorCode", errorCode);
		return map;
	}
	
	/**
	 * 身份验证
	 */
	private UserBean userVerification(HttpServletRequest request) {
		UserBean userBean = phoneService.getUserByUserName(getStrParam(request, "userName", ""));
		if (userBean != null && userBean.getLoginPassword().equals(getStrParam(request, "password", ""))) {
			return userBean;
		} else {
			return null;
		}
	}
	
	 /**
	 * 电力拓扑结构树
	 * @param request
	 * @return
	 */
	@RequestMapping("/showMeterTreePage")
	public ModelAndView showMeterTreePage(HttpServletRequest request) {
		UserBean userBean = userVerification(request);
        if(userBean == null)
            return null;
        request.setAttribute("ledgerId", userBean.getLedgerId());
        request.setAttribute("accountId", userBean.getAccountId());
		return new ModelAndView("/energy/serveapp/meterTreePage");
	}
	
	/**
	 * 加载可移动电力拓扑树结构
	 * @param request
	 * @return
	 */
	@RequestMapping("/showParentMeterTreePage")
	public ModelAndView showParentMeterTreePage(HttpServletRequest request){
		UserBean userBean = userVerification(request);
		if (userBean == null)
			return null;
		request.setAttribute("userLedgerId", userBean.getLedgerId());
		request.setAttribute("accountId", userBean.getAccountId());
		request.setAttribute("ledgerId", getLongParam(request, "ledgerId", -1));
		LedgerBean ledger = ledgerManagerService.getLedgerDataById(getLongParam(request, "ledgerId", -1));
		if (ledger != null)
			request.setAttribute("ledgerName", ledger.getLedgerName());
		else
			request.setAttribute("ledgerName", getStrParam(request,"ledgerName", ""));
		request.setAttribute("meterId", getLongParam(request, "meterId", -1));
		return new ModelAndView("/energy/serveapp/parentMeterTreePage");
	}
	
	/**
	 * 加载电力拓扑结构树数据
	 * @param request
	 * @param ledgerId
	 * @return
	 */
	@RequestMapping(value = "/getMeterTreeData")
	public @ResponseBody List<LineLossTreeBean> getMeterTreeData(HttpServletRequest request) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		long ledgerId = getLongParam(request, "ledgerId", -1); // 企业ID
		String ledgerName = getStrParam(request, "ledgerName", ""); // 企业名称
		long userledgerId = getLongParam(request, "userLedgerId", -1); // 用户所属分户
		if (userledgerId > 0) {
			queryMap.put("userledgerId", userledgerId); // 用户权限限制
		} else {
			queryMap.put("accountId", getLongParam(request, "accountId", -1)); // 群组权限限制
		}
		queryMap.put("ledgerId", ledgerId);
		queryMap.put("meterLimit", getLongParam(request, "meterId", -1)); // 非该电表自身以及其子节点
		List<LineLossTreeBean> lineTreeData = servephoneService.getMeterTreeData(queryMap);
		// 填充企业信息为根节点
		LineLossTreeBean treeBean = new LineLossTreeBean();
		treeBean.setId(0);
		treeBean.setpId(-1);
		treeBean.setName(ledgerName);
		treeBean.setLedgerId(ledgerId);
		treeBean.setLedgerName(ledgerName);
		lineTreeData.add(treeBean);
		return lineTreeData;
	}
	
	/**
	 * 管理电力拓扑结构树（DCP操作）
	 * @param type 节点操作类型： 1新增 2修改 3删除
	 * @return Map<String,Object>,包括flag是否成功标识
	 */
	@RequestMapping(value = "/manageMeterTree")
	public @ResponseBody Map<String,Object> manageMeterTree(HttpServletRequest request){
		UserBean userBean = userVerification(request);
		Map<String,Object> map = new HashMap<String, Object>();
		boolean isSuccess = false;
		StringBuilder sb = new StringBuilder();
		int type = getIntParams(request, "type", -1);
		int operatType = 0;
		try {
			switch (type) {
			case 1:
			case 2:
				MeterBean meter = super.jacksonUtils.readJSON2Bean(MeterBean.class, getStrParam(request, "paramInfo", ""));
				if(meter.getLineLoss()== null)
					meter.setMeterAttr(4);
				else 
					meter.setMeterAttr(1);
				meter.setMeterType(new Short("1"));
				meter.setTypeId(1l);
				meter.setMeterStatus(1);
				if(type==1){
					meter.setMeterId(SequenceUtils.getDBSequence());
					isSuccess = meterManagerService.insertMeterInfo(meter);
					sb.append("add a meter, ").append(meter.getMeterName());
					operatType = CommonOperaDefine.LOG_TYPE_INSERT;
				} else {
					meter.setOldLedgerId(String.valueOf(meter.getLedgerId()));
					isSuccess = meterManagerService.updateMeterInfo(meter);
					sb.append("update a meter, ").append(meter.getMeterName());
					operatType = CommonOperaDefine.LOG_TYPE_UPDATE;
				}
				break;
			case 3:
				long meterId = getLongParam(request, "meterId", -1);
				List<Long> meterIds = new ArrayList<Long>();
				meterIds.add(meterId);
				isSuccess = meterManagerService.deleteMeterData(meterIds);			//调用
				operatType = CommonOperaDefine.LOG_TYPE_DELETE;
				sb.append(" delete meter , meterId is ").append(meterId);
				break;
			default:
				break;
			}
			int rst = CommonOperaDefine.OPRATOR_FAIL;
			if(isSuccess)
				rst =  CommonOperaDefine.OPRATOR_SUCCESS;
			if(type > 0 && type <= 3 ) {
				OptLogBean optLogBean = new OptLogBean(operatType, CommonOperaDefine.MODULE_NAME_METER_ID, CommonOperaDefine.MODULE_NAME_METER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_POINT, rst, sb.toString());
				optLogBean.setOptId(userBean.getAccountId());
				optLogBean.setOptName(userBean.getLoginName());
				super.writePhoneLog(optLogBean, request);
			}
		} catch (NumberFormatException e) {
			isSuccess = false;
			Log.info("manageMeterTree error NumberFormatException");
		}
		catch (IOException e) {
			isSuccess = false;
			Log.info("manageMeterTree error IOException");
		}
		map.put("flag", isSuccess);
		return map;
	}

	/**
	 * 验证企业名称是否重复
	 * @param request
	 * @return isSuccess true可用/false不可用
	 */
	public @ResponseBody Map<String, Object> checkLedgerName(HttpServletRequest request) {
		UserBean userBean = userVerification(request);
		if (userBean == null)
			return null;
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isSuccess;
//		try {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("ledgerName", getStrParam(request, "ledgerName", ""));
			queryMap.put("parentId",getLongParam(request, "regionId", -1));
			isSuccess = ledgerManagerService.checkLedgerName(queryMap);
//		} catch (Exception e) {
//			isSuccess = false;
//			Log.info(this.getClass() + ".checkLedgerName()", e);
//		}
		map.put("flag", isSuccess);
		return map;
	}
}
