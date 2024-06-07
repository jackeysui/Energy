package com.linyang.energy.controller.phone;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.leegern.util.CollectionUtil;
import com.linyang.energy.job.AppDataCacheJob;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.*;
import com.linyang.energy.service.*;
import com.linyang.energy.utils.OperItemConstant;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.helpers.Transform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.utils.APKFileUtil;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.energy.utils.WebConstant;

/**
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
 **/

/**
 * 手机接口
 * 
 * @version:1.0
 * @author:gaofeng
 * @date:2014.8.18
 */
@Controller
@RequestMapping("/phoneInterface")
public class PhoneController extends BaseController {

	@Autowired
	private PhoneService phoneService;
	@Autowired
	private GroupService groupService; 
	@Autowired
	private LedgerManagerService ledgerService;
	@Autowired
	private MeterManagerService meterService;
	@Autowired
	private UserService userService;
    @Autowired
    private UserAnalysisService userAnalysisService;
    @Autowired
    private EleAssessment eleAssessment;
    @Autowired
    private IndexService indexService;
    @Autowired
	private LedgerManagerMapper ledgerManagerMapper;

	/**
	 * 手机历史数据查询接口
	 * 
	 * @param request
	 * @param dataType
	 *            数据类型；1、电量数据；2、功率因数数据；3、有功功率；4、无功功率；5、电压；6、电流；
	 * @param objId
	 *            查询对象ID
	 * @param objType
	 *            对象类型（1、分户；2、计量点）
	 * @param baseDate
	 *            基准日期（格式为：yyyy-MM-dd）
	 * @param dateType
	 *            日期类型（1、日数据；2、3日数据；3、10日数据；4、30日数据；5、年数据；）
	 * @return
	 */
	@RequestMapping(value = "/queryHistoryData")
	public @ResponseBody List<Map<String, Object>> queryHistoryData(final HttpServletRequest request, Integer traceFlag, String userName, String password,
                                                                    int dataType, long objId, int objType,String baseDate, int dateType) {
        //身份认证
        UserBean user = viewUserVerification(userName, password);
		if (user == null){
            return null;
        }
        long operItemId = 0l;
        Log.info("/queryHistoryData调用：userName=" + userName + ",password=" + password
                + ",traceFlag=" + traceFlag + ",dataType=" + dataType + ",objId=" + objId + ",objType=" + objType
                + ",baseDate=" + baseDate + ",dateType=" + dateType);

        Long accountId = user.getAccountId();
        Map<String, Object>  pfMap = new HashMap<String, Object>();
		//查询分户的pf
		if(objType==1) {
			LedgerBean ledger = ledgerService.selectByLedgerId(objId);
			if(ledger != null)
				pfMap.put("ledgerpf", ledger.getThresholdValue());
		} else if(objType==2) {
			pfMap.put("ledgerpf", meterService.getThresholdById(objId));
		}
		Map<String, Object> countMap = new HashMap<String, Object>();
		countMap.put("datacount", 1440 / WebConstant.density);
		Date sDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		switch (dataType) {
			case 1:// 电量
				list2=phoneService.getEnergyData(objId, objType, sDate, dateType);
				// 电量汇总
				double energySum = 0;
				for (Map<String, Object> map : list2) {
					Object objData = map.get("DATA");
					if(objData != null)
						energySum = DataUtil.doubleAdd(energySum, Double.parseDouble(objData.toString()));
				}
				pfMap.put("energySum", new DecimalFormat("0.00").format(energySum));
				if (traceFlag != null && traceFlag > 0)
					this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_155, 12L, 2);
//				operItemId = OperItemConstant.OPER_ITEM_155;
				break;
			case 2:// 功率因数
				list2=phoneService.getPFData(objId, objType, sDate, dateType,0);
				if (traceFlag != null && traceFlag > 0)
                    this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_156, 13L, 2);
//				operItemId = OperItemConstant.OPER_ITEM_156;
				break;
			case 3:// 有功功率
				list2=phoneService.getAPData(objId, objType, sDate, dateType);
                if (traceFlag != null && traceFlag > 0)
                    this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_157, 13L, 2);
//				operItemId = OperItemConstant.OPER_ITEM_157;
				break;
			case 4:// 无功功率
				list2=phoneService.getRPData(objId, objType, sDate, dateType);
                if (traceFlag != null && traceFlag > 0)
                    this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_158, 13L, 2);
//				operItemId = OperItemConstant.OPER_ITEM_158;
				break;
			case 5:// 电压
				list2=phoneService.getVolCurData(objId, objType, sDate, dateType, dataType);
                if (traceFlag != null && traceFlag > 0)
                    this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_159, 13L, 2);
//				operItemId = OperItemConstant.OPER_ITEM_159;
				break;
			case 6:// 电流
				list2=phoneService.getVolCurData(objId, objType, sDate, dateType, dataType);
                if (traceFlag != null && traceFlag > 0)
                    this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_160, 13L, 2);
//				operItemId = OperItemConstant.OPER_ITEM_160;
				break;
		}
		
		//判断是否为VIP
		//list2 = phoneService.getPhoneVIPData(dataType, objId, objType, baseDate, dateType, list2);
		// 返回数据
		list.add(pfMap);
		list.add(countMap);
		if(list2!=null)
			list.addAll(list2);
		// 请求时间戳
		list.add(new HashMap<String, Object>(){{put("systemNo", getStrParam(request, "systemNo", ""));}});
		
		//记录用户足迹
//		this.userAnalysisService.addAccountTrace(accountId, operItemId , null, 2);
		return list;
	}
	
	/**
	 * 用户登录  
	 * @param userName 登录用户名称
	 * @return Map<String, Object> 包括loginFlag登陆是否成功 accountId登陆用户Id  ledgerId关联分户Id  version版本号 
	 * @author chengq 2014-08-19
	 */
	@RequestMapping(value = "/login")
	public @ResponseBody Map<String, Object> login(HttpServletRequest request, Integer type, String osVersion, String userName, String password,String version) {
		boolean loginFlag = false;
		int errorCode = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = phoneService.getUserByUserName(userName);
		long ledgerId = 0;
		// web登录、IOS登录不限制版本;Android登录限制版本号;
//		if((type==2) || (type == 3 && StringUtils.isNotBlank(version) && Float.parseFloat(version) >= APKFileUtil.getCurrentVersion(getIntParams(request, "apktype", 1)))){
		if((type==1) || (type==2) || (type == 3)){
			if (userBean != null && userBean.getAccountStatus() == 1) {
				if (userBean.getLoginPassword().equals(password)) {
					// 免扰时段设置
					map.put("isShield", userBean.getIsShield());
					map.put("freetime", userBean.getFreeTimePeriod());
					// 数据权限：用户
					ledgerId = userBean.getLedgerId();
					// 数据权限: 群组
					if(userBean.getLedgerId()== null || userBean.getLedgerId() == 0){
						Map<String, Object> queryInfo = new HashMap<String, Object>();
						queryInfo.put("accountId", userBean.getAccountId());
						queryInfo.put(Dialect.pageNameField, new Page());
						List<LedgerBean> ledgerList = phoneService.getPageLedger(queryInfo);
						ledgerId = ledgerList.get(0).getLedgerId();
					}
					loginFlag = true;
                    //更新用户登陆记录
					if (userBean.getLastDate() != null){
						userBean.setLastDate(new Date());
                        Long loginTimes = userBean.getLoginTimes();
                        if(loginTimes == null){
                            loginTimes = 1L;
                        }
                        else{
                            loginTimes = loginTimes + 1;
                        }
                        userBean.setLoginTimes(loginTimes);
						userService.updateLastDate(userBean);
					}
					this.userAnalysisService.addAccountLogin(userBean.getAccountId(), new Date(), type, osVersion);
//                    //首页所需数据
//                    Map<String,Object> phoneIndex = phoneService.showPhoneIndex(userBean);
//                    map.put("phoneIndex", phoneIndex);

                    //用户订阅记录
                    List<Map<String, Object>> bookInfo = this.phoneService.getBookRecord(userBean.getAccountId());
                    map.put("bookInfo", bookInfo);
				} else {
					errorCode = 1;
				}
				map.put("firstFlag", userBean.getLastDate() == null ? 1 : 0);
			} else {
				errorCode = 1;
			}
		} else {
			errorCode = 3;
		}
		map.put("loginFlag", loginFlag);
		map.put("errorCode", errorCode);
		map.put("accountId", (userBean==null?-1:userBean.getAccountId()));
		map.put("ledgerId", ledgerId);
        LedgerBean ledgerBean = ledgerService.selectByLedgerId(ledgerId);
        map.put("ledgerName", (ledgerBean==null?"":ledgerBean.getLedgerName()) );
        boolean isAdmin = false;	/* 是否是管理员账号  */
        if(ledgerBean != null && (ledgerBean.getAnalyType() == 104 || ledgerBean.getAnalyType() == 105) ){
			isAdmin = true;
		}
		map.put( "isAdmin", isAdmin );
		return map;
	}
	
	/**
	 * 修改密码 
	 * @return boolean 是否成功
	 * @author chengq 2014-08-19
	 */
	@RequestMapping(value = "/changePassword")
	public @ResponseBody Map<String,Object> changePassword(HttpServletRequest request,Long accountId,String oldPassword,String password){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean isSuccess = false;
		UserBean userBean = phoneService.getUserByAccountId(accountId);
		if(userBean != null){
			if(oldPassword != null && oldPassword.equals(userBean.getLoginPassword())){
				userBean.setLoginPassword(password);
				phoneService.updateUserInfo(userBean);
				isSuccess = true;
				if (userBean.getLastDate() == null){
					userBean.setLastDate(new Date());
                    Long loginTimes = userBean.getLoginTimes();
                    if(loginTimes == null) {
                        loginTimes = 0L;
                    }
                    userBean.setLoginTimes(loginTimes+1);
					userService.updateLastDate(userBean);
				}
			}
		}
		resultMap.put("flag", isSuccess);
		return resultMap;
	}
	
	/**
	 * 群组数据获取  
	 * @param accountId 登陆账户id
	 * @param groupType 组类型(没有则传递-1)
	 * @param publicType 公开类型(没有则传递-1)
	 * @param pageIndex 页码
	 * @param pageSize 每页条数
	 * @return List<GroupBean>
	 * @author chengq 2014-08-19
	 * @deprecated 智慧能效废弃该接口  edit by 20160112
	 */
	@RequestMapping(value = "/getAllUserTeam")
	public @ResponseBody List<GroupBean> getAllUserTeam(HttpServletRequest request, String userName,String password,
                     Long accountId,Integer groupType, Integer publicType,Integer pageIndex,Integer pageSize) {
        //身份认证
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

		Map<String, Object> queryInfo = new HashMap<String, Object>();
		Page page = new Page(Integer.valueOf(pageIndex),Integer.valueOf(pageSize));
		
		queryInfo.put("accountId", accountId);
		queryInfo.put("groupType", groupType);
		queryInfo.put("publicType", publicType);
		queryInfo.put(Dialect.pageNameField, page);
		List<GroupBean> groupList = phoneService.getAllUserTeam(queryInfo);
		return groupList;
	}
	
	/**
	 * 获取用户所属分户列表   
	 * @param accountId 登陆账户id
	 * @param pageIndex 页码
	 * @param pageSize 每页条数
	 * @return List<LedgerBean>
	 * @author chengq 2014-08-19
	 */
	@RequestMapping(value = "/getUserLedger")
	public @ResponseBody Map<String,Object> getUserLedger(HttpServletRequest request,String userName,String password,
                              Long accountId,Integer pageIndex,Integer pageSize){
        //身份认证
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<LedgerBean> ledgerList = new ArrayList<LedgerBean>();
		UserBean userBean = phoneService.getUserByAccountId(accountId);
		Page page = null;
//		try {
			page = new Page(Integer.valueOf(pageIndex),Integer.valueOf(pageSize));
//		} catch (Exception e) {
//			page = new Page();
//		}
		if(userBean != null && accountId != 1){
			// 数据权限: 群组
			if(userBean.getLedgerId()==null || userBean.getLedgerId()==0){
				Map<String, Object> queryInfo = new HashMap<String, Object>();
				queryInfo.put("accountId", userBean.getAccountId());
				queryInfo.put(Dialect.pageNameField, page);
				ledgerList = phoneService.getPageLedger(queryInfo);
			//  数据权限: 分户(单个)
			} else {
				Map<String, Object> queryInfo = new HashMap<String, Object>();
				queryInfo.put("ledgerId", userBean.getLedgerId());
				queryInfo.put(Dialect.pageNameField, page);
				ledgerList = phoneService.queryRecursiveLedgerById(queryInfo);
			}
		// admin用户获取全部分户列表
		} else if(userBean != null && accountId == 1){
			Map<String, Object> queryInfo = new HashMap<String, Object>();
			queryInfo.put("ledgerId", -1);
			queryInfo.put(Dialect.pageNameField, page);
			ledgerList = phoneService.queryRecursiveLedgerById(queryInfo);
		}
		List<Map<String,Object>> objList = new ArrayList<Map<String,Object>>();
		Map<String,Object> beanMap = null;
		for (LedgerBean ledgerBean : ledgerList) {
			beanMap = new HashMap<String, Object>();
			beanMap.put("ledgerId", ledgerBean.getLedgerId());
			beanMap.put("ledgerName", ledgerBean.getLedgerName());
			objList.add(beanMap);
		}
		resultMap.put("ledgerList", objList);
		return resultMap;
	}
	
	/**
	 * 获取测量点Id或分户Id获取测量点列表
	 * @param objId 分户Id或测量点Id
	 * @param objType 类型: 1 分户,2测量点
	 * @return Map<String,Object>
	 * @author chengq 2014-08-19
	 */
	@RequestMapping(value = "/getMeterListByLedgerOrMeterId")
	public @ResponseBody Map<String,Object> getMeterListByLedgerOrMeterId(HttpServletRequest request,String userName,String password,
                                                    Long objId,Integer objType){
        //身份认证
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

		Map<String,Object> resultMap = new HashMap<String, Object>();
		List<MeterBean> meterList = null;
		long ledgerId = 0; if (objId != null) ledgerId = objId;
		String ledgerName = "";
		if (objId != null && objType != null
				&& (objType.longValue() == 1 || objType.longValue() == 2)) {
			meterList = phoneService.getMeterListByLedgerOrMeterId(objId,objType);
			if(meterList.size()>0 && meterList.get(0)!= null){
				ledgerId = meterList.get(0).getLedgerId();
			} 
		} 
		// 获取分户信息
		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
		if(ledgerBean != null){
			ledgerName = ledgerBean.getLedgerName();
		}
		resultMap.put("ledgerId", ledgerId);
		resultMap.put("ledgerName", ledgerName);
		// 解析计量点列表
		List<Map<String,Object>> objList = new ArrayList<Map<String,Object>>();
		if(meterList != null && meterList.size() > 0){
			Map<String,Object> beanMap = null;
			for (MeterBean meterBean : meterList) {
				beanMap = new HashMap<String, Object>();
				beanMap.put("meterId", meterBean.getMeterId());
				beanMap.put("meterName", meterBean.getMeterName());
				objList.add(beanMap);
			}
		}
		resultMap.put("meterList", objList);
		return resultMap;
	}
	
	/**
	 * 根据组ID与与组类型获取当前组成员列表(和web端处理方式同步)
	 * @param objId 组Id
	 * @param objType 组类型: 1 客户,2计量点
	 * @param accountId 用户Id
	 * @return Map<String,Object>
	 * @author chengq 2014-08-19
	 * @deprecated 智慧能效废弃该接口  edit by 20160112
	 */
	@RequestMapping(value = "/getGroupUserByGroupId")
	public @ResponseBody Map<String,Object> getGroupUserByGroupId(HttpServletRequest request,String userName,String password,
                                              Long objId,Integer objType,Long accountId){
        //身份认证
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        int i_objType = -1;if (objType != null) i_objType = objType;
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if(objId != null && objType != null){
			Map<String,Object> beanMap = null;
			List<Map<String,Object>> objList = null;
			List<LedgerBean> ledgerList = null;
			List<MeterBean> meterList = null;
			UserBean userBean = phoneService.getUserByAccountId(accountId);
			long ledgerId = userBean.getLedgerId();
			if(accountId == 1){//如果是超级管理员,直接获取分户或者计量点，不需要考虑权限
				if(objType.longValue()==1)//如果是分户组
				{
					ledgerList = groupService.getSuperGroupLedger(objId);
				}
				else if(objType.longValue()==2) // 计量点组
				{
					meterList = groupService.getSuperGroupMeter(objId);
				}
			}else {
				if(objType.longValue()==1)//如果是分户组
				{
					if(ledgerId==0) //分户权限
					{
						ledgerList = groupService.getGroupLedger(objId, accountId);
					}
					else // 群组权限
					{
						LedgerBean ledger = ledgerService.selectByLedgerId(ledgerId);
						ledgerList = groupService.getGroupLedger2(objId, ledger.getLedgerLft(), ledger.getLedgerRgt());
					}
				}else if(objType.longValue()==2)//如果是计量点组
				{
					if(ledgerId==0) // 分户权限
					{
						meterList = groupService.getGroupMeter(objId, accountId);
					}
					else//去t_ledger_tree查到所有的子分户节点，然后查出电表，两边比对一下即可 
					{
						meterList = groupService.getGroupMeter2(objId, accountId);
					}
				}
			}
			// 解析
			objList = new ArrayList<Map<String,Object>>();
			if(objType.longValue() == 1){
                if(ledgerList != null && ledgerList.size() > 0){
                    for (LedgerBean ledgerBean : ledgerList) {
                        beanMap = new HashMap<String, Object>(); beanMap.put("objId", ledgerBean.getLedgerId());
                        beanMap.put("objName", ledgerBean.getLedgerName()); objList.add(beanMap);
                    }
                }
			} else if(objType.longValue() == 2){
                if(meterList != null && meterList.size() > 0){
                    for (MeterBean meterBean : meterList) {
                        beanMap = new HashMap<String, Object>();  beanMap.put("objId", meterBean.getMeterId());
                        beanMap.put("objName", meterBean.getMeterName());  objList.add(beanMap);
                    }
                }
			}
			resultMap.put("objType", i_objType);
			resultMap.put("objList", objList);
		}
		return resultMap;
	}
	
	/**
	 * @description手机实时数据查询接口
	 *  获取当前分户今日电量、最新有功功率、最新无功功率、昨日电量与功率因数、上月电量与功率因数。
	 * 
	 * @param request
	 *            数据类型；1、今日电量数据、2 最新有功功率、3最新无功功率、4昨日电量 5功率因数、6上月电量7功率因数；
	 * @param objId
	 *            查询对象ID
	 * @return
	 * @author Yaojiawei
	 */
	@RequestMapping(value = "/queryRealtimeData")
	public @ResponseBody List<Map<String, Object>> queryRealtimeData(HttpServletRequest request,String userName,String password,
                                                                     long objId)
	{
        //身份认证
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

		String baseDate =  DateUtil.getCurrentDateStr("yyyy-MM-dd");
		Date endDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		Date startDate = DateUtil.convertStrToDate(baseDate+" 00:00:00");
		/**
		 * 获取今日电量
		 * */	
		Double todayQ =  phoneService.getLedgerEnergyData(objId, startDate, endDate, 1);
		/***
		 * 获取最新有功功率
		 */
		Double newAP = phoneService.getRecentAP(objId);
		/***
		 * 获取最新无功功率
		 */
		Double newRP = phoneService.getRecentRP(objId);
		
		/***
		 * 获取昨日电量
		 */
		baseDate =  DateUtil.getYesterdayDateStr("yyyy-MM-dd");
		endDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		startDate = DateUtil.convertStrToDate(baseDate+" 00:00:00");
		
		Double yesterdayQ =  phoneService.getLedgerEnergyData(objId, startDate, endDate, 1);		
		/**
		 * 获取昨日功率因数
		 * */

		Double yesterdayPF = phoneService.getLedgerPFData(objId, startDate,endDate, 1);
		
		/**
		 * 获取上月电量
		 * */
		baseDate = DateUtil.convertDateToStr(DateUtil.getPreMonthFristDay(new Date()), "yyyy-MM-dd");
		endDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		startDate = DateUtil.convertStrToDate(baseDate+" 00:00:00");
		Double lastMonQ =  phoneService.getLedgerEnergyData(objId, startDate, endDate, 2);
		
		/**
		 * 获取上个月的功率因数
		 * */
		Double lastMonPF = phoneService.getLedgerPFData(objId, startDate,endDate, 2);

		List<Map<String, Object>> myList = new ArrayList<Map<String, Object>>();
		Map<String, Object> myMap = new HashMap<String, Object>();
		
		myMap.put("todayQ", todayQ);
		myMap.put("newAP", newAP);
		myMap.put("newRP", newRP);
		myMap.put("yesterdayQ", yesterdayQ);
		myMap.put("yesterdayPF", yesterdayPF);
		myMap.put("lastMonQ", lastMonQ);
		myMap.put("lastMonPF", lastMonPF);
		
		myList.add(myMap);
		
		
		return myList;
	}
	
	/**
	 * 获取手机APK下载URL
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAPKUrl")
	public @ResponseBody String getAPKUrl(HttpServletRequest request) {
		return APKFileUtil.getNewApkName(1,1);
	}
	
	/**
	 * 下载手机APK
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadAPK")
	public ModelAndView downloadAPK(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("redirect:/" + APKFileUtil.getNewApkName(getIntParams(request, "type", 1),1)); // 默认林洋APP：1林洋标识;2中性APP; 默认安卓App:1安卓App 2 IOS App;
	}
	
	/**
	 * 手机数据查询页面
	 * @return
	 */
	@RequestMapping("/cydata")
	public @ResponseBody ModelAndView gotoCyData(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
         if(userName==null||password==null){
         	return null;
         }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        request.setAttribute("userName",user.getLoginName());
        request.setAttribute("password",user.getLoginPassword());

		long objId = getLongParam(request, "objId", 1403589441615L);
		int dataType = getIntParams(request, "dataType", 1);
		int objType = getIntParams(request, "objType", 1);
        String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		request.setAttribute("dataTypeName",getDataTypeName(dataType));//数据类型名称
		Long ledgerId = null;
		if(objType == 1){//分户
			ledgerId = objId;
		}else {//测量点
			ledgerId = this.phoneService.getLedgerIdByMeterId(objId);
		}
		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);// 获取分户信息
		String ledgerName = "";
		if(ledgerBean != null){
			ledgerName = ledgerBean.getLedgerName();
		}
		request.setAttribute("ledgerId", ledgerId);
		request.setAttribute("ledgerName", ledgerName);
		// 解析计量点列表
		List<Map<String,Object>> objList = new ArrayList<Map<String,Object>>();
		List<MeterBean> meterList = phoneService.getMeterListByLedgerOrMeterId(objId,objType);
   		if(meterList != null && meterList.size() > 0){
			Map<String,Object> beanMap = null;
			for (int i = 0; i < meterList.size(); i++) {
				MeterBean meterBean = meterList.get(i);
				if(dataType > 2 && i == 0){
					objId = meterBean.getMeterId();
					objType = 2;
				}
				beanMap = new HashMap<String, Object>();
				beanMap.put("meterId", meterBean.getMeterId());
				beanMap.put("meterName", meterBean.getMeterName());
				objList.add(beanMap);
			}
		}
		request.setAttribute("meterList", objList);
		request.setAttribute("objId",objId);//对象ID
		request.setAttribute("dataType",dataType);//数据类型
		request.setAttribute("objType",objType);//对象类型
		request.setAttribute("qryDate",baseDate);
		
		int dateType = 1;
		//日期类型：
		if(dataType == 1 || dataType == 2 ){//点量,功率因数
			dateType = 3; //10日
		}
		request.setAttribute("dateType",dateType);//对象类型
		
		
		return new ModelAndView("phoneData/cy_data");
	}


    /**
     * 需量分析接口
     */
    @RequestMapping("/demandPage")
    public @ResponseBody ModelAndView demandPage(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        request.setAttribute("userName",user.getLoginName());
        request.setAttribute("password",user.getLoginPassword());

        long ledgerId = getLongParam(request, "ledgerId", -3L);
        long pointId = getLongParam(request, "pointId", -3L);
        String flag = getStrParam(request, "flag", "0"); //0表示月，1表示年
        String dateStr = getStrParam(request, "dateStr", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));//日期，如：2015-07-10
        //分户
        LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
        String ledgerName = "";
        if(ledgerBean != null){
            ledgerName = ledgerBean.getLedgerName();
        }
        request.setAttribute("ledgerId", ledgerId);
        request.setAttribute("ledgerName", ledgerName);
        //根据分户获取计量点列表
        List<Map<String,Object>> objList = new ArrayList<Map<String,Object>>();
        List<MeterBean> meterList = phoneService.getMeterListByLedgerOrMeterId(ledgerId,1);
        if(meterList != null && meterList.size() > 0){
            Map<String,Object> beanMap = null;
            for (int i = 0; i < meterList.size(); i++) {
                MeterBean meterBean = meterList.get(i);
                beanMap = new HashMap<String, Object>();
                beanMap.put("meterId", meterBean.getMeterId());
                beanMap.put("meterName", meterBean.getMeterName());
                objList.add(beanMap);
            }
            if(pointId == -3L){
                pointId = meterList.get(0).getMeterId();
            }
        }
        request.setAttribute("meterList", objList);
        request.setAttribute("objId", pointId);

        request.setAttribute("flag", flag);
        request.setAttribute("dateStr", dateStr);
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(user.getAccountId(), OperItemConstant.OPER_ITEM_165, null, 1);
        return new ModelAndView("phoneData/demand_page");
    }

    @RequestMapping(value = "/demandAnalysis")
    public @ResponseBody Map<String,Object> demandAnalysis(HttpServletRequest request, Integer traceFlag){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        Long accountId = user.getAccountId();

        Map<String,Object> result = new HashMap<String, Object>();
        Long pointId = getLongParam(request, "objId", -3L);
        int objType = getIntParams(request, "objType", 2);	//1、分户；2、计量点
        String flag = getStrParam(request, "dateType", "0"); //0表示月，1表示年
        if(flag==null){return null;}
        String dateStr = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN)); //日期，如：2015-07-10
        if(pointId > 0){
        	result.putAll(this.phoneService.demandAnalysis(pointId, flag, dateStr,objType));
        }

        //记录用户使用记录
        if(traceFlag != null && traceFlag > 0){
            this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_165, 43L, 2);
        }
        result.put("systemNo", getStrParam(request, "systemNo", ""));	
        return result;
    }


    /**
     * 电费分析接口
     */
    @RequestMapping("/feePage")
    public @ResponseBody ModelAndView feePage(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        request.setAttribute("userName",user.getLoginName());
        request.setAttribute("password",user.getLoginPassword());

        long ledgerId = getLongParam(request, "ledgerId", -3L);
        long pointId = getLongParam(request, "pointId", -3L);
        Date defaultDate = DateUtil.getPreMonthLastDay(new Date());
        String dateStr = getStrParam(request, "dateStr", DateUtil.convertDateToStr(defaultDate,DateUtil.SHORT_PATTERN));//日期，如：2015-07-10
        //分户
        LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
        String ledgerName = "";
        if(ledgerBean != null){
            ledgerName = ledgerBean.getLedgerName();
        }
        request.setAttribute("ledgerId", ledgerId);
        request.setAttribute("ledgerName", ledgerName);
        //根据分户获取计量点列表
        List<Map<String,Object>> objList = new ArrayList<Map<String,Object>>();
        List<MeterBean> meterList = phoneService.getMeterListByLedgerOrMeterId(ledgerId,1);
        if(meterList != null && meterList.size() > 0){
            Map<String,Object> beanMap = null;
            for (int i = 0; i < meterList.size(); i++) {
                MeterBean meterBean = meterList.get(i);
                beanMap = new HashMap<String, Object>();
                beanMap.put("meterId", meterBean.getMeterId());
                beanMap.put("meterName", meterBean.getMeterName());
                objList.add(beanMap);
            }
            if(pointId == -3L){
                pointId = meterList.get(0).getMeterId();
            }
        }
        request.setAttribute("meterList", objList);
        request.setAttribute("objId", pointId);

        request.setAttribute("dateStr", dateStr);
	
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(user.getAccountId(), OperItemConstant.OPER_ITEM_164, null, 1);
        return new ModelAndView("phoneData/fee_page");
    }

    @RequestMapping(value = "/feeAnalysis-old")
    public @ResponseBody Map<String,Object> feeAnalysis(HttpServletRequest request, Integer traceFlag){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        Long accountId = user.getAccountId();

        Map<String,Object> result = new HashMap<String, Object>();
        Long objId = getLongParam(request, "objId", -3L);
        Integer objType = getIntParams(request, "objType", 2);  //1表示分户，2表示采集点
		Integer dateType = getIntParams(request, "dateType", 2);  //2前月  1上月  0本月
        Date defaultDate = DateUtil.getPreMonthLastDay(new Date());
        String dateStr = getStrParam(request, "baseDate", DateUtil.convertDateToStr(defaultDate,DateUtil.SHORT_PATTERN));//日期，如：2015-07-10
//		dateStr = "2017-07-07";
//        if(objId > 0){
//            result.putAll(this.phoneService.feeAnalysis(objId, objType, dateStr));
//        }
	
		result.put("analyType", -1);
		if( objId > 0 && objType == 1 ){
			LedgerBean ledger = ledgerService.selectByLedgerId(objId);
			result.put("analyType", ledger.getAnalyType());
		}
		
		if(objId > 0){
			Map<String, Object> param = this.restructuringMap( objId, objType, dateStr,dateType );
			result.putAll( this.phoneService.queryEleData( param ) );
        }
	
		//记录用户使用记录
        if(traceFlag != null && traceFlag > 0){
            this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_164, 42L, 2);
        }
        result.put("systemNo", getStrParam(request, "systemNo", ""));
        return result;
    }


	@RequestMapping(value = "/feeAnalysis")
	public @ResponseBody Map<String,Object> feeAnalysis2(HttpServletRequest request, Integer traceFlag){
		//身份认证
		String userName = getStrParam(request, "userName","");
		String password = getStrParam(request, "password","");
		if(userName==null||password==null){
			return null;
		}
		UserBean user = viewUserVerification(userName, password);
		if(user == null){
			return null;
		}
		Long accountId = user.getAccountId();

		Map<String,Object> result = new HashMap<String, Object>();
		Long objId = getLongParam(request, "objId", -3L);
		Integer objType = getIntParams(request, "objType", 2);  //1表示分户，2表示采集点
		Integer dateType = getIntParams(request, "dateType", 2);  //2前月  1上月  0本月
		Date defaultDate = DateUtil.getPreMonthLastDay(new Date());
		String dateStr = getStrParam(request, "baseDate", DateUtil.convertDateToStr(defaultDate,DateUtil.SHORT_PATTERN));//日期，如：2015-07-10
//		dateStr = "2017-07-07";
//        if(objId > 0){
//            result.putAll(this.phoneService.feeAnalysis(objId, objType, dateStr));
//        }

		result.put("analyType", -1);
		if( objId > 0 && objType == 1 ){
			LedgerBean ledger = ledgerService.selectByLedgerId(objId);
			result.put("analyType", ledger.getAnalyType());
		}

		if(objId > 0){
			Map<String, Object> param = this.restructuringMap( objId, objType, dateStr,dateType );
			result.putAll( this.phoneService.queryEleData2( param ) );
		}

		//记录用户使用记录
		if(traceFlag != null && traceFlag > 0){
			this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_164, 42L, 2);
		}
		result.put("systemNo", getStrParam(request, "systemNo", ""));
		return result;
	}

	/**
     * 功率因数分析接口
     */
    @RequestMapping("/pfPage")
    public @ResponseBody ModelAndView pfPage(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        request.setAttribute("userName",user.getLoginName());
        request.setAttribute("password",user.getLoginPassword());

        long ledgerId = getLongParam(request, "ledgerId",-3L);
        String flag = getStrParam(request, "flag", "2"); //”0”表示日，”1”表示3日,”2”表示10日,”3”表示30日,”4”表示年
        String dateStr = getStrParam(request, "dateStr", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN)); //基准日期，如：2015-07-10
        //分户
        LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
        String ledgerName = "";
        if(ledgerBean != null){
            ledgerName = ledgerBean.getLedgerName();
        }
        request.setAttribute("ledgerId", ledgerId);
        request.setAttribute("ledgerName", ledgerName);

        request.setAttribute("flag", flag);
        request.setAttribute("dateStr", dateStr);
	
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(user.getAccountId(), OperItemConstant.OPER_ITEM_162, null, 1);
        return new ModelAndView("phoneData/pf_page");
    }

    @RequestMapping(value = "/pfAnalysis")
    public @ResponseBody Map<String,Object> pfAnalysis(HttpServletRequest request, Integer traceFlag){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){return null;}
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        Long accountId = user.getAccountId();

        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = getLongParam(request, "ledgerId",-3L);
        String flag = getStrParam(request, "dateType", "2"); //”0”表示日，”1”表示3日,”2”表示10日,”3”表示30日,”4”表示年
        String dateStr = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN)); //基准日期，如：2015-07-10
		Integer objType = getIntParams( request , "type",1 );
		if ( objType == 2 ) {
			ledgerId = this.phoneService.getLedgerIdByMeterId(ledgerId);
		}
	
		if(ledgerId != -3L){
            result.putAll(this.phoneService.pfAnalysis(ledgerId, flag, dateStr));
        }
        //记录用户使用记录0
        if(traceFlag != null && traceFlag > 0){
            this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_162, 29L, 2);
        }
        result.put("systemNo", getStrParam(request, "systemNo", ""));
        return result;
    }

    /**
     * 线损分析接口
     */
    @RequestMapping("/linelosePage")
    public @ResponseBody ModelAndView linelosePage(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        request.setAttribute("userName",user.getLoginName());
        request.setAttribute("password",user.getLoginPassword());

        String beginTime = getStrParam(request, "beginTime", DateUtil.convertDateToStr(DateUtil.getDateBetween(new Date(), -2), DateUtil.SHORT_PATTERN));
        String endTime = getStrParam(request, "endTime", DateUtil.getYesterdayDateStr(DateUtil.SHORT_PATTERN));
        long objectId = getLongParam(request, "objectId",-3L);
        int type = getIntParams(request, "type", 1); //1表示第一级（分户），2表示第二级（级别1的计量点）
        request.setAttribute("beginTime", beginTime);
        request.setAttribute("endTime", endTime);
        request.setAttribute("type", type);
        request.setAttribute("objId", objectId);
        Long ledgerId = null;
        if(type == 1){//分户
            ledgerId = objectId;
        }else {//测量点
            ledgerId = this.phoneService.getLedgerIdByMeterId(objectId);
        }
        LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);// 获取分户信息
        String ledgerName = "";
        if(ledgerBean != null){
            ledgerName = ledgerBean.getLedgerName();
        }
        request.setAttribute("ledgerId", ledgerId);
        request.setAttribute("ledgerName", ledgerName);
        // 计量点列表
        List<Map<String,Object>> meterList = this.phoneService.getMeterListByLeger(ledgerId, 1);
        request.setAttribute("meterList", meterList);
	
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(user.getAccountId(), OperItemConstant.OPER_ITEM_161, null, 1);
        return new ModelAndView("phoneData/linelose_page");
    }

    @RequestMapping(value = "/lineloseAnalysis")
    public @ResponseBody Map<String,Object> lineloseAnalysis(HttpServletRequest request, Integer traceFlag){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        Long accountId = user.getAccountId();
        Map<String,Object> result = new HashMap<String, Object>();
        String beginTime = getStrParam(request, "beginTime", DateUtil.convertDateToStr(DateUtil.getDateBetween(new Date(), -2), DateUtil.SHORT_PATTERN));
        String endTime = getStrParam(request, "endTime", DateUtil.getYesterdayDateStr(DateUtil.SHORT_PATTERN));
        long objectId = getLongParam(request, "objectId",-3L);
        int type = getIntParams(request, "type", 1); //1表示（分户），2表示（级别1的计量点）

        Long ledgerId = null;
        if(type == 1){
            ledgerId = objectId;
        }
        else{
            type = 2;
            ledgerId = this.phoneService.getLedgerIdByMeterId(objectId);
        }
        //分户信息
        LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
        String ledgerName = "";
        if(ledgerBean != null){
            ledgerName = ledgerBean.getLedgerName();
        }
        result.put("ledgerId", ledgerId);
        result.put("ledgerName", ledgerName);
        // 1级别计量点列表
        List<Map<String,Object>> meterList = this.phoneService.getMeterListByLeger(ledgerId, 1);
        result.put("meterList", meterList);
        //饼图、柱状图所需数据
        result.putAll(this.phoneService.lossAnalysis(beginTime, endTime, objectId, type));

        //记录用户使用记录
        if(traceFlag != null && traceFlag > 0){
            this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_161, 104L, 2);
        }
        result.put("systemNo", getStrParam(request, "systemNo", ""));
        return result;
    }

    /**
     * 事件详情接口
     */
    @RequestMapping(value = "/eventDetail")
    public @ResponseBody Map<String,Object> eventDetail(HttpServletRequest request)  {
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

        Map<String,Object> result = new HashMap<String, Object>();
        String eventStartTime = getStrParam(request, "eventStartTime","");
        try {
        	eventStartTime = URLDecoder.decode(eventStartTime,"UTF-8");//URLDecoder.decode(eventStartTime, "utf-8");
		} catch (UnsupportedEncodingException e) {
			Log.info("eventDetail error UnsupportedEncodingException");
		}
        Long eventId = getLongParam(request, "eventId", -1L);
        int objectType = getIntParams(request, "objectType", -1);
        Long objectId = getLongParam(request, "objectId", -1L);
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("eventStartTime", eventStartTime);
        queryMap.put("eventId", eventId);
        queryMap.put("objectType", objectType);
        queryMap.put("objectId", objectId);
        Map<String, Object> event = this.phoneService.getEventDetail(queryMap);
        if(event != null){
            result.put("event", event);
        }
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(user.getAccountId(), OperItemConstant.OPER_ITEM_171, null, 1);
        return result;
    }

    /**
     * 根据新闻ID获取新闻
     */
    @RequestMapping(value = "/newsDetail")
    public @ResponseBody Map<String,Object> newsDetail(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

        Map<String,Object> result = new HashMap<String, Object>();
        Long infoId = getLongParam(request, "infoId", -1L);
        Map<String, Object> news = this.phoneService.getNewsDetail(infoId);
        if(news != null){
            result.put("news", news);
        }
        return result;
    }

    /**
     * 根据类型获取新闻、政策分页列表
     */
    @RequestMapping(value = "/getNewsPageList")
    public @ResponseBody Map<String,Object> getNewsPageList(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

        Map<String,Object> result = new HashMap<String, Object>();
        int type = getIntParams(request, "type", 1); //1表示新闻，2表示政策
        int  pageIndex = getIntParams(request, "pageIndex", 1);
        int  pageSize = getIntParams(request, "pageSize", 50);
        Page page = new Page(pageIndex, pageSize);
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("type", type);
        queryMap.put("page", page);
        List<Map<String, Object>> news = this.phoneService.getNewsPageList(queryMap);
        result.put("news", news);
        return result;
    }

    /**
     * 根据用户Id获取 自定义消息、报告、事件 分页列表
     */
    @RequestMapping(value = "/getMessagePageList")
    public @ResponseBody Map<String,Object> getMessagePageList(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

        Map<String,Object> result = new HashMap<String, Object>();
        Long accountId = getLongParam(request, "accountId", -1L);
        int type = getIntParams(request, "type", 1); //1表示自定义消息、报告,2表示事件(订阅的)
        int  pageIndex = getIntParams(request, "pageIndex", 1);
        int  pageSize = getIntParams(request, "pageSize", 50);
        Page page = new Page(pageIndex, pageSize);
        UserBean userBean= this.phoneService.getUserByAccountId(accountId);
        Long ledgerId = userBean.getLedgerId();
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("accountId", accountId);
        queryMap.put("ledgerId",ledgerId);
        queryMap.put("page", page);
        if(type == 1){
              List<Map<String,Object>> list = this.phoneService.getMessageReportPageList(queryMap);
              result.put("list", list);
        }
        else if(type == 2){
            List<EventBean> events = this.phoneService.getEventPageList(queryMap);
            result.put("eventList", events);
        }
        return result;
    }

    /**
     * 根据ID获取报告
     */
    @RequestMapping(value = "/reportDetail")
    public @ResponseBody Map<String,Object> reportDetail(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

        Map<String,Object> result = new HashMap<String, Object>();
        Long reportId = getLongParam(request, "reportId", -1L);
        Map<String, Object> report = this.phoneService.getReportDetail(reportId);
        if(report != null){
            result.put("report", report);
        }
        return result;
    }

    /**
     * 用户自定义消息
     */
    @RequestMapping(value = "/messageDetail")
    public @ResponseBody Map<String,Object> messageDetail(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }

        Map<String,Object> result = new HashMap<String, Object>();
        Long messageId = getLongParam(request, "messageId", -1L);
        Map<String, Object> message = this.phoneService.getMessageDetail(messageId);
        if(message != null){
            result.put("message", message);
        }
        return result;
    }


    /**
     * 订购记录接口
    */
    @RequestMapping(value = "/bookRecord")
    public @ResponseBody Map<String,Object> bookRecord(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        UserBean user = null;
        if(null!=userName&&null!=password){
        	user=viewUserVerification(userName, password);
        }
        if(user == null){
            return null;
        }

        Map<String,Object> result = new HashMap<String, Object>();
        Long accountId = getLongParam(request, "accountId", -1L);
        List<Map<String, Object>> bookInfo = this.phoneService.getBookRecord(accountId);
        result.put("bookInfo", bookInfo);
        return result;
    }

    /**
     *  手机端等级帮助接口
     * */
    @RequestMapping(value = "/levelHelp")
    public @ResponseBody Map<String,Object> levelHelp(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        Map<String,Object> result = new HashMap<String, Object>();
        Long accountId = user.getAccountId();
        result.putAll(this.phoneService.getLevelHelp(accountId));
        return result;
    }

    /**
     *  得到用户最近一次用电评估记录
     * */
    @RequestMapping(value = "/lastAssessment")
    public @ResponseBody Map<String,Object> lastAssessment(HttpServletRequest request){
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
        Map<String,Object> result = new HashMap<String, Object>();
        Long accountId = user.getAccountId();
        result.putAll(this.phoneService.getLastAssessment(accountId));
        return result;
    }

    /**
     * 点击立即体检
     **/
    @RequestMapping(value="/beginCheck")
    public @ResponseBody Map<String, Object> beginCheck(HttpServletRequest request) {
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean userBean = viewUserVerification(userName, password);
        if(userBean == null){
            return null;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        Long accountId = userBean.getAccountId();
        Object obj = this.eleAssessment.getAssessmentCache().get(accountId);
        if(obj != null){
            Map<String, Object> accountCache = (Map<String, Object>) obj;
            int complete = Integer.valueOf(accountCache.get("complete").toString());
            

            
            if(0 != complete){
                this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_163, null, 2);  //记录用户使用记录

                this.eleAssessment.assessment(accountId);
            }
        }
        else {
            this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_163, null, 2); //记录用户使用记录

            this.eleAssessment.assessment(accountId);
        }
        return result;
    }

    /**
     * 点击立即体检后，不断查询缓存中该accountId对应的结果
     **/
    @RequestMapping(value="/getAssessCache")
    public @ResponseBody Map<String, Object> getAssessCache(HttpServletRequest request) {
        //身份认证
        String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean userBean = viewUserVerification(userName, password);
        if(userBean == null){
            return null;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        Long accountId = userBean.getAccountId();
        Object obj = this.eleAssessment.getAssessmentCache().get(accountId);
        if(obj != null){
            Map<String, Object> accountCache = (Map<String, Object>) obj;
            result.putAll(accountCache);
            int complete = Integer.valueOf(accountCache.get("complete").toString());
            if(complete == 1){
                //计算击败用户百分比
                int score = Integer.valueOf(accountCache.get("score").toString());
                int percent = this.phoneService.getBeatUserPercent(accountId, score);
                result.put("beatPercent", percent);
            }
        }
        return result;
    }
    
    /**
	 * 用户提交建议接口
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/submitSuggestion")
	public @ResponseBody Map<String, Object> submitSuggestion(HttpServletRequest request){
		//身份认证
		String userName = getStrParam(request, "userName","");
        String password = getStrParam(request, "password","");
        if(userName==null||password==null){
        	return null;
        }
        UserBean user = viewUserVerification(userName, password);
        if(user == null){
            return null;
        }
		
        Map<String, Object> result = new HashMap<String, Object>();
        int isOk = 0;//1为成功，2为失败，0为未找到对应用户
		SuggestBean suggest = new SuggestBean();
		
		suggest.setSugId(SequenceUtils.getDBSequence());
		suggest.setSubmitDate(DateUtil.convertStrToDate(DateUtil.getCurrentDateStr()));
		suggest.setAccountId(user.getAccountId());
		suggest.setSubmitUser(user.getLoginName());
		suggest.setLedgerId(user.getLedgerId());
		LedgerBean ledgerBean = phoneService.getLedgerById(user.getLedgerId());
		suggest.setSubmitLedger(ledgerBean.getLedgerName());
		suggest.setSugMsg(getStrParam(request, "sugmsg",""));
		suggest.setContactWay(getStrParam(request, "contactway",""));
		
		boolean isSuccess = false;
		isSuccess = indexService.addSug(suggest);
		
		if(isSuccess){	
			isOk = 1;
		}else{
			isOk = 2;
		}
		
		result.put("isOk", isOk); //TOOD 为统一, 新版本上线后去掉该参数
		result.put("flag", isOk);
		return result;
	}


	/**
	 * 得到数据类型名称
	 * @param dataType
	 * @return
	 */
	private String getDataTypeName(int dataType) {
		String dataTypeName = "";
		switch (dataType) {
			case 1:
				dataTypeName = "电量";
				break;
			case 2:
				dataTypeName = "功率因数";
				break;
			case 3:
				dataTypeName = "有功功率";
				break;
			case 4:
				dataTypeName = "无功功率";
				break;
			case 5:
				dataTypeName = "电压";
				break;
			case 6:
				dataTypeName = "电流";
				break;
			default:
				break;
		}
		return dataTypeName;
	}

    /**
     * 身份验证,手动转码
     */
    private UserBean viewUserVerification(String userName, String password){
		UserBean userBean = null;
		try {
			userBean = phoneService.getUserByUserName(new String(userName.getBytes("ISO-8859-1"), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			Log.error("decode username error:" + userName);
		}

        if(WebConstant.appVerify.equals("1")){
            if (userBean!=null && userBean.getLoginPassword().equals(password) && userBean.getAccountStatus() != 0){
                return userBean;
            } else{
                return userVerification(userName, password);
            }
        }
        return userBean;
    }

    /**
     * 身份验证
     */
    private UserBean userVerification(String userName, String password){
        UserBean userBean = phoneService.getUserByUserName(userName);
        if(WebConstant.appVerify.equals("1")){
            if (userBean!=null && userBean.getLoginPassword().equals(password)){
                return userBean;
            } else{
                return null;
            }
        }
        return userBean;
    }
	
    
    // ********************add by chengq 20160304能效预研项目新增接口************************
    /**
     * 能管对象搜索(全路径)
     * @param dataType
	 * @return
     */
    @RequestMapping("/getEMOPath")
    public @ResponseBody Map<String,Object> getEMOPath(HttpServletRequest request) {
    	String userName=getStrParam(request, "userName","");String password=getStrParam(request, "password","");if(userName==null||password==null){return null;}
		UserBean userBean = viewUserVerification(userName, password);
		if (userBean == null)
			return null;
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	boolean isSuccess = false;
    	
    		Map<String,Object> queryMap = new HashMap<String, Object>();
    		queryMap.put("ledgerId", userBean.getLedgerId());
    		queryMap.put("keyword", getStrParam(request, "keyword", ""));
    		resultMap.put("data", phoneService.getEMOPath(queryMap));
    		isSuccess = true;
		
		resultMap.put("flag", isSuccess);
    	return resultMap;
    }
    
    /**
     * 企业EMO加载
     * @param request
     * @return
     */
    @RequestMapping("/getCompanyEMO")
    public @ResponseBody Map<String,Object> getCompanyEMO(HttpServletRequest request){
    	String userName=getStrParam(request, "userName","");
    	String password=getStrParam(request, "password","");
    	if(userName==null||password==null){
    		return null;
    	}
		UserBean userBean = viewUserVerification(userName, password);
		if (userBean == null)
			return null;
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	boolean isSuccess = false;
//    	try {
    		Map<String,Object> queryMap = new HashMap<String, Object>();
    		queryMap.put("ledgerId", userBean.getLedgerId());
    		int depth = getIntParams(request, "depth", -1);
    		if(depth != -1) {
    			queryMap.put("depth", depth);
    			queryMap.put("depthLedgerId", getLongParam(request, "ledgerId", -1));
    		} else {
    			queryMap.put("analyType", getIntParams(request, "analyType", 102));
    		}
    		// 过滤多余字段
			List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			List<LedgerBean> ledgerList = ledgerService.getLedgerListByParam(queryMap);
			for (LedgerBean ledgerBean : ledgerList) {
				Map<String,Object> ledgerMap = new HashMap<String, Object>();
				ledgerMap.put("id", ledgerBean.getLedgerId());
				ledgerMap.put("name", ledgerBean.getLedgerName());
				ledgerMap.put("depth", ledgerBean.getDepth());
				dataList.add(ledgerMap);
			}
			// 默认加载第一级能管对象  see getNextEMO()
			if(!ledgerList.isEmpty()) {
				Map<String,Object> queryNextMap = new HashMap<String, Object>();
				long depthLedgerId = getLongParam(request, "ledgerId", -1);
				queryNextMap.put("parentLedgerId", depthLedgerId!=-1?depthLedgerId:ledgerList.get(0).getLedgerId());
				resultMap.put("firstEMO", phoneService.getNextEMO(queryNextMap));
			}
			resultMap.put("data", dataList);
    		isSuccess = true;
//		} catch (Exception e) {
//			Log.info(this.getClass() + ".getCompanyEMO()",e);
//		}
		resultMap.put("flag", isSuccess);
    	return resultMap;
    }
    
    /**
     * 获取下一级EMO
     * @param request
     * @return
     */
    @RequestMapping("/getNextEMO")
    public @ResponseBody Map<String,Object> getNextEMO(HttpServletRequest request) {
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	boolean isSuccess = false;
    	
    		Map<String,Object> queryMap = new HashMap<String, Object>();
    		queryMap.put("parentLedgerId", getLongParam(request, "ledgerId", -1));
			resultMap.put("data", phoneService.getNextEMO(queryMap));
    		isSuccess = true;
		
		resultMap.put("flag", isSuccess);
    	return resultMap;
    }
    
    /**
     * 获取下一级表计(计算模型)
     * @param request
     * @return
     */
    @RequestMapping("/getNextDCP")
    public @ResponseBody Map<String,Object> getNextDCP(HttpServletRequest request) {
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	boolean isSuccess = true;   	
		long ledgerId = super.getLongParam(request, "ledgerId", -1);
		resultMap.put("data", phoneService.getNextDCP(ledgerId));
    	resultMap.put("flag", isSuccess);
    	return resultMap;
    }
    
    /**
     * 数据页面:缺点：web端页面,考虑放在APP端
     * @param request
     * @param dataType 数据类型（1、电量数据；2、功率因数数据；3、有功功率；4、无功功率；5、电压；6、电流；7、线损分析；8、功率因数分析；9、需量分析；10、电费分析；11、用电体验）
     * @param objId    查询对象ID
     * @param objType  对象类型（1、分户；2、计量点）
     * @param baseDate 基准日期（格式为：yyyy-MM-dd）
     * @param dateType 日期类型（针对1~6数据查询：1、日数据；2、3日数据；3、10日数据；4、30日数据；5、年数据；
     * 							针对7~11分析功能：按页面顺序排列）
     * @return
     */
    @RequestMapping("/analysis")
    public ModelAndView gotoChartData(HttpServletRequest request) {
		String userName = getStrParam(request, "userName", "");
		String password = getStrParam(request, "password", "");
		if(userName==null||password==null){
			return null;
		}
		// 身份认证
		UserBean userBean = viewUserVerification(userName, password);
		if (userBean == null)
			return null;
		// 获取参数
		long objId = getLongParam(request, "objId", -1);
		int dataType = getIntParams(request, "dataType", 1);
		int objType = getIntParams(request, "objType", 1);
		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		int dateType = getIntParams(request, "dateType", 1);
		long ledgerId = objId;
		if (objType != 1){ // 如果是计量点,获取分户Id
			ledgerId = this.phoneService.getLedgerIdByMeterId(objId);
		} else {
			if (objId == -1)
				objId = userBean.getLedgerId();
		}
		// 获取分户信息
		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
        String ledgerName = "";
		if (ledgerBean != null)
			ledgerName = ledgerBean.getLedgerName();
		else{
			ledgerBean = phoneService.getLedgerById(objId);
			ledgerName = ledgerBean.getLedgerName();
		}
			
		
        request.setAttribute("ledgerId", ledgerId); // 分户Id
        request.setAttribute("ledgerName", ledgerName); // 分户名称
        request.setAttribute("userName", userBean.getLoginName()); // 用户名
		request.setAttribute("password", userBean.getLoginPassword()); // 密码
		request.setAttribute("dataType", dataType); // 默认数据类型
		request.setAttribute("objType", objType);  // 对象类型,1 分户   2计量点
		request.setAttribute("objId", objId); // 如果未传入objId,默认取所属分户ID
		// 根据数据类型判断跳转页面(1~6为数据查询,进入同一的数据查询页面;7~11为分析功能,进入各分析页面)
		switch (dataType) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
//		case 12:
//			request.setAttribute("baseDate", baseDate); // 默认今天
			request.setAttribute("baseDate", DateUtil.getYesterdayDateStr(DateUtil.SHORT_PATTERN)); // 根据需求调整，默认今天-1
			request.setAttribute("dateType", dateType); // 0日,1 3日,2 10日,3 30日,4 年
			return new ModelAndView("energyApp/app_data_data");
		case 7: // 线损分析(EMO+DCP)
	        request.setAttribute("beginTime", DateUtil.convertDateToStr(DateUtil.getDateBetween(new Date(), -2), DateUtil.SHORT_PATTERN));
	        request.setAttribute("endTime", DateUtil.getYesterdayDateStr(DateUtil.SHORT_PATTERN));
			request.setAttribute("analyType", ledgerBean.getAnalyType());
	        return new ModelAndView("energyApp/analysis/linelose_page");
		case 8: // 功率因数分析(EMO)
	        request.setAttribute("flag", dateType); // 0日,1 3日,2 10日,3 30日,4 年
	        request.setAttribute("dateStr", baseDate);
	        return new ModelAndView("energyApp/analysis/pf_page");
		case 9: // 需量分析(DCP)
			dateType = getIntParams(request, "dateType", 0);
	        request.setAttribute("flag", dateType); // 0表示月，1表示年
	        request.setAttribute("dateStr", baseDate);
	        return new ModelAndView("energyApp/analysis/demand_page");
		case 10:// 电费分析(EMO+DCP) // 0前月 1上月 2本月
			request.setAttribute("dateStr", DateUtil.convertDateToStr(DateUtil.getPreMonthLastDay(new Date()),DateUtil.SHORT_PATTERN));
		    return new ModelAndView("energyApp/analysis/fee_page");
		case 11:// 用电体验(EMO)(手机本地处理)
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(userBean.getAccountId(), OperItemConstant.OPER_ITEM_163, null, 1);
		case 12://电耗分析
			request.setAttribute("baseDate", baseDate); // 默认今天
			request.setAttribute("dateType", 2); // 0日,1 3日,2 10日,3 30日,4 年
			return new ModelAndView("energyApp/app_data_data");
//			return new ModelAndView("energyApp/app_energy_data");
		case 15://电耗查询
				request.setAttribute("baseDate", baseDate); // 默认今天
				request.setAttribute("dateType", 2); // 0日,1 3日,2 10日,3 30日,4 年
			return new ModelAndView("energyApp/app_data_data");
	    default:
	    	return null;
		}
    }
    
    /**
     * 数据页面:缺点：web端页面,考虑放在APP端
     * @param request
     * @param dataType 数据类型（1、电量数据；2、功率因数数据；3、有功功率；4、无功功率；5、电压；6、电流；7、线损分析；8、功率因数分析；9、需量分析；10、电费分析；11、用电体验）
     * @param objId    查询对象ID
     * @param objType  对象类型（1、分户；2、计量点）
     * @param baseDate 基准日期（格式为：yyyy-MM-dd）
     * @param dateType 日期类型（针对1~6数据查询：1、日数据；2、3日数据；3、10日数据；4、30日数据；5、年数据；
     * 							针对7~11分析功能：按页面顺序排列）
     * @return
     */
    @RequestMapping("/wechat_analysis")
    public ModelAndView gotoWechatChartData(HttpServletRequest request) {
		String userName = getStrParam(request, "userName", "");
		String password = getStrParam(request, "password", "");
		if(userName==null||password==null){
			return null;
		}
		// 身份认证
		UserBean userBean = viewUserVerification(userName, password);
		if (userBean == null)
			return null;
		// 获取参数
		long objId = getLongParam(request, "objId", -1);
		int dataType = getIntParams(request, "dataType", 1);
		int objType = getIntParams(request, "objType", 1);
		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		int dateType = getIntParams(request, "dateType", 1);
		long ledgerId = objId;
		if (objType != 1){ // 如果是计量点,获取分户Id
			ledgerId = this.phoneService.getLedgerIdByMeterId(objId);
		} else {
			if (objId == -1)
				objId = userBean.getLedgerId();
		}
		// 获取分户信息
		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
        String ledgerName = "";
		if (ledgerBean != null)
			ledgerName = ledgerBean.getLedgerName();
        request.setAttribute("ledgerId", ledgerId); // 分户Id
        request.setAttribute("ledgerName", ledgerName); // 分户名称
        request.setAttribute("userName", userBean.getLoginName()); // 用户名
		request.setAttribute("password", userBean.getLoginPassword()); // 密码
		request.setAttribute("dataType", dataType); // 默认数据类型
		request.setAttribute("objType", objType);  // 对象类型,1 分户   2计量点
		request.setAttribute("objId", objId); // 如果未传入objId,默认取所属分户ID
		// 根据数据类型判断跳转页面(1~6为数据查询,进入同一的数据查询页面;7~11为分析功能,进入各分析页面)
		switch (dataType) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			request.setAttribute("baseDate", baseDate); // 默认今天
			request.setAttribute("dateType", dateType); // 0日,1 3日,2 10日,3 30日,4 年
			return new ModelAndView("energyApp/app_data_wechat");
	    default:
	    	return null;
		}
		
    }
    
    /**
     * APP用户数据显示偏好设置
     * @param request
     * @param userName 用户名
     * @param config 配置信息,如："1,4,10" 表示勾选中1 4 10三项数据显示
     * @param type   标识：1-轮循数据,2-重点关注
     * @return
     */
    @RequestMapping("/setAppPrefer")
    public @ResponseBody Map<String,Object> setAppPrefer(HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	boolean isSuccess = false;
    	
    		String userName = getStrParam(request, "userName","");
        	String password = getStrParam(request, "password","");
        	if(userName==null||password==null){return null;}
        	//身份认证
            UserBean userBean = viewUserVerification(userName, password);
            if(userBean == null)
            	return null;
            // 参数设置
            String config = getStrParam(request, "config", "");
            int type = getIntParams(request, "type", 1);
            isSuccess = phoneService.modifyAppPrefer(userBean.getAccountId(),config,type);

        resultMap.put("flag", isSuccess);
	
        long operItemId = OperItemConstant.OPER_ITEM_166;
        if ( type != 1 )
			operItemId = OperItemConstant.OPER_ITEM_167;
	
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(userBean.getAccountId(), operItemId, null, 1);
    	return resultMap;
    }
    
    /**
     * 获取APP用户数据显示偏好设置
     * @param type   标识：1-轮循数据,2-重点关注
     * @return
     */
    @RequestMapping("/getAppPrefer")
    public @ResponseBody Map<String,Object> getAppPrefer(HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	boolean isSuccess = false;
    	try {
    		String userName = getStrParam(request, "userName","");
        	String password = getStrParam(request, "password","");
        	if(userName==null||password==null){
        		return null;
        	}
        	//身份认证
            UserBean userBean = viewUserVerification(userName, password);
            if(userBean == null)
            	return null;
        	// 获取参数
    		Map<String,Object> headConfig = phoneService.getAppPrefer(userBean.getAccountId(), getIntParams(request, "type", 1));
            if(headConfig!= null && !headConfig.isEmpty())
            	resultMap.put("config", headConfig.get("OPTIONS"));
            isSuccess = true;
		} catch (RuntimeException e) {
			Log.error(this.getClass() + ".getAppPrefer()--无法获取app偏好");
		}
    	resultMap.put("flag", isSuccess);
    	return resultMap;
    }
    
    /**
     * APP 取消重点关注
     * @param request
     * @param objId
     * @return
     */
    @RequestMapping("/disAppPrefer")
    public @ResponseBody Map<String,Object> disAppPrefer(HttpServletRequest request){
    	Map<String,Object> resultMap = new HashMap<String, Object>();
    	boolean isSuccess = false;
    	long objId = -1;
    	//try {
    		UserBean userBean=null;
    		String username=getStrParam(request, "userName","");
    		String passwd=getStrParam(request, "password","");
    		if(null!=username&&null!=passwd){
    		userBean = viewUserVerification(username, passwd);}
            if(userBean == null)
            	return null;
            // 参数设置
            objId = getLongParam(request, "objId", -1);
            isSuccess = phoneService.disAppPrefer(userBean.getAccountId(),objId);
		/*} catch (Exception e) {
			Log.error(this.getClass() + ".disAppPrefer()--无法取消重点关注");
		}*/
        resultMap.put("flag", isSuccess);
        resultMap.put("objId", objId);
    	return resultMap;
    }
    
    /**
     * 首页头部页面
     * @param request
     * @param dataType 数据类型（0、电量数据汇总；1、上月能耗统计（非图表）；2、7日复费率电量；3、上月能耗财务数据；4、月功率因数；5、30日电量6；6、日功率曲线；7、月需量曲线对比；8、系统运行概况（非图表）；9、耗电排名；）
     * @return
     */
    @RequestMapping("/headData")
    public ModelAndView gotoIndexChartData(HttpServletRequest request) {
    	String userName = getStrParam(request, "userName","");
    	String password = getStrParam(request, "password","");
    	 if(userName==null||password==null){
    	 	return null;
    	 }
    	//身份认证
        UserBean userBean = viewUserVerification(userName, password);
        if(userBean == null)
            return null;
        long objId = getLongParam(request, "objId", -1);
        if (objId == -1)
        	objId = userBean.getLedgerId();
        // 获取要加载的首页数据配置信息
        AppPreferBean preferBean = phoneService.getAppPreferBean(userBean.getAccountId(), 1);
        String preferSet = "0,4";
		if (preferBean != null && StringUtils.isNotBlank(preferBean.getOptions()))
			preferSet = preferBean.getOptions();
		LedgerBean ledgerBean = null;
		if(userBean != null && userBean.getLedgerId() != null){
			ledgerBean = ledgerService.selectByLedgerId(userBean.getLedgerId());
		}
		request.setAttribute("preferSet", preferSet); // 循环区域类别
        request.setAttribute("userName", userBean.getLoginName()); // 用户名
        request.setAttribute("password", userBean.getLoginPassword()); // 密码
		request.setAttribute("analyType", ledgerBean.getAnalyType()); // 企业类型
    	return new ModelAndView("energyApp/app_index_data");
    }
    
    /**
     * 首页头部数据
     * @param request
     */
    @RequestMapping("/getHeadChartData")
    public @ResponseBody Map<String,Object> getHeadChartData(HttpServletRequest request) {
    	String userName=getStrParam(request, "userName","");
    	String password=getStrParam(request, "password","");
    	if(userName==null||password==null){
    		return null;
    	}
        UserBean user = viewUserVerification(userName, password);
        if(user == null)
            return null;
        Map<String,Object> queryMap,resultMap,cacheMap;
    	int dataType = getIntParams(request, "dataType", 0);
    	switch (dataType) {
			case 0: // 总电量数据
				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 0);
				if(cacheMap != null)
					return cacheMap;
				//同步大屏
				LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(user.getLedgerId());
				if(StringUtils.isNotBlank(ledgerBean.getUserNo())){
					return phoneService.getHeadChartDataByScreen(user, ledgerBean);
				}
				return phoneService.getHeadChartData(user);
			case 1: // 上月能耗统计
				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 1);
				if(cacheMap != null)
					return cacheMap;
				return indexService.queryChart2DataNew(user.getLedgerId());
			case 2: // 7日复费率电量
				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 2);
				if(cacheMap != null)
					return cacheMap;
				return indexService.queryChart5Data(user.getLedgerId());
			case 3: // 上月能耗财务数据
				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 3);
				if(cacheMap != null)
					return cacheMap;
				return indexService.queryChart3DataNew(user.getLedgerId());
			case 4: // 月功率因数
				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 4);
				if(cacheMap != null)
					return cacheMap;
				resultMap = new HashMap<String, Object>();
				LedgerBean ledger = ledgerService.selectByLedgerId(user.getLedgerId());
				if(ledger != null)
					resultMap.put("ledgerpf", ledger.getThresholdValue());
				resultMap.put("data", phoneService.getPFData(user.getLedgerId(), 1, new Date(), 5,1));
				return resultMap;
			case 5: // 30日电量
				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 5);
				if(cacheMap != null)
					return cacheMap;
				return indexService.getChart7DataNew(user.getLedgerId());
			case 6: // 日功率曲线
				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 6);
				if(cacheMap != null)
					return cacheMap;
				resultMap = new HashMap<String, Object>();
				resultMap.put("data", indexService.queryChart6Data(user.getLedgerId()));
				return resultMap;
			case 7: // 月需量曲线
				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 7);
				if(cacheMap != null)
					return cacheMap;
				return indexService.queryChart4Data(user.getLedgerId());
			case 8: // 系统运行概况
				long ledgerId = user.getLedgerId();
				if(user.getAccountId().longValue() == 1)
					ledgerId = -100L;
		        return indexService.getChart3DataPartner(ledgerId);
			case 9: // 耗电排名
//				cacheMap = AppDataCacheJob.getCacheData(user.getAccountId(), 9);
//				if(cacheMap != null)
//					return cacheMap;
				resultMap = new HashMap<String, Object>();
				queryMap = new HashMap<String, Object>();
				queryMap.put("ledgerId",  user.getLedgerId()); // 分户Id
				queryMap.put("meterType", 1); // 计量点类型: 0:综合, 1:电, 2:水, 3:气, 4:热
				queryMap.put("beginTime", com.leegern.util.DateUtil.getLastMonthFirstDay());
				queryMap.put("endTime",   com.leegern.util.DateUtil.getLastDayOfMonth(com.leegern.util.DateUtil.getLastMonthFirstDay()));
				resultMap.put("data", indexService.queryUseEnergyRanking(queryMap));
				return resultMap;
			default:
				return null;
		}
    }
    
    /**
	 * 首页政策
	 * @return
	 */
	@RequestMapping("/news")
	public ModelAndView gotoNews(HttpServletRequest request){
		String userName=getStrParam(request, "userName","");
		String password=getStrParam(request, "password","");
		if(userName==null||password==null){
			return null;
		}
        UserBean userBean = viewUserVerification(userName, password);
        if(userBean == null)
            return null;
        // 获取政策列表
        request.setAttribute("data", phoneService.getMsgList(2));
		return new ModelAndView("energyApp/app_index_news");
	}
	
	/**
	 * 点击进入政策列表页面(不分页,App滑屏)
	 * @return
	 */
	@RequestMapping("/newslist")
	public ModelAndView gotoNewsList(HttpServletRequest request){
		String userName=getStrParam(request, "userName","");
		String password=getStrParam(request, "password","");
		if(userName==null||password==null){
			return null;
		}
		//身份认证
        UserBean userBean = viewUserVerification(userName, password);
        if(userBean == null)
            return null;
        // 获取政策列表
        request.setAttribute("data", phoneService.getMsgList(2));
		return new ModelAndView("energyApp/app_index_news_list");
	}
	
	/**
	 * 首页小常识
	 * @return
	 */
	@RequestMapping("/knowledge")
	public ModelAndView gotoKnowledges(HttpServletRequest request,String userName,String password){
		UserBean userBean=null;
		String userName1=getStrParam(request, "userName","");
		String password1=getStrParam(request, "password","");
		if(userName!=null&&password!=null){
			userBean = viewUserVerification(userName1, password1);
		}
        if(userBean == null)
            return null;
        // TODO 获取小常识类型
        return new ModelAndView("energyApp/app_index_common_sense");
	}
	
	/**
	 * 点击进入小常识列表页面(不分页,App滑屏)
	 * @return
	 */
	@RequestMapping("/knowledgelist")
	public ModelAndView gotoKnowledgeList(HttpServletRequest request){
		UserBean userBean=null;
		String userName=getStrParam(request, "userName","");
		String password=getStrParam(request, "password","");
		if(userName==null||password==null){
			return null;
		}
		if(userName!=null&&password!=null){
			userBean = viewUserVerification(userName, password);
		}
        if(userBean == null)
            return null;
        // TODO 根据传入类型获取小常识列表
        int key = getIntParams(request, "key", 0);
        String titleName = "";
        long operItemId = 0l;
        
        switch (key) {
		case 0:
			titleName = "小常识列表";
			operItemId = OperItemConstant.OPER_ITEM_168;
			break;
		case 1:
			titleName = "购电须知";
			operItemId = OperItemConstant.OPER_ITEM_169;
			break;
		case 2:
			titleName = "服务专家";
			operItemId = OperItemConstant.OPER_ITEM_170;
			break;
		}
        request.setAttribute("titleName", titleName);
        List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
        data.add(new HashMap<String, Object>(){{put("INFO_TITLE", "为何中国电压是220V？");put("key", 0);}});
        data.add(new HashMap<String, Object>(){{put("INFO_TITLE", "详解电力变压器型号");put("key", 1);}});
        data.add(new HashMap<String, Object>(){{put("INFO_TITLE", "关于电力信息安全的小常识");put("key", 2);}});
        request.setAttribute("data", data);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(userBean.getAccountId(), operItemId, null, 1);
        
        return new ModelAndView("energyApp/app_index_common_sense_list");
	}
	
	/**
	 * 进入小常识详细信息页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/knowledgeDetail")
	public ModelAndView getknowledgeDetail(HttpServletRequest request){
		// TODO 
		return null;
	}
	
	 /**
	 * 重点关注页面
	 * @return
	 */
	@RequestMapping("/focus")
	public ModelAndView gotoFocus(HttpServletRequest request){
		String userName=getStrParam(request, "userName","");String password=getStrParam(request, "password","");if(userName==null||password==null){return null;}
        UserBean userBean = viewUserVerification(userName, password);
        if(userBean == null){
            return null;
        }
        // 加载重点关注对象、(前30日电量)数据
        String preferSet = String.valueOf(userBean.getLedgerId());
        AppPreferBean preferBean = phoneService.getAppPreferBean(userBean.getAccountId(), 2);
        if(preferBean != null && StringUtils.isNotBlank(preferBean.getOptions()))
        	preferSet = preferBean.getOptions();
        Date baseDate = DateUtil.clearDate(WebConstant.getChartBaseDate());
        request.setAttribute("data", phoneService.getLedgerQ(preferSet,DateUtil.getDateBetween(baseDate, -30),DateUtil.getDateBetween(baseDate,-0)));
		return new ModelAndView("energyApp/app_index_focus");
	}
	
	/**
	 * Android手机获取版本
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getAPKVersion")
	public @ResponseBody Map<String,Object> getAPKVersion(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isSuccess = false;
		int type  = getIntParams(request, "type", 1);	// 默认林洋APP：1林洋标识;2中性APP
		int key = getIntParams(request, "key", 1);		// 默认安卓App:1安卓App 2 IOS App
    	String version = APKFileUtil.getCurrentVersion(type,key);
    	String path = "";String scheme = request.getScheme();String serverName = request.getServerName();int port = request.getServerPort();String contextPath = request.getContextPath();
    	if (scheme != null && scheme.trim().length()>0 && serverName != null &&serverName.trim().length()>0 && port > 0 && contextPath != null && contextPath.trim().length()>0) {
    		path = scheme+"://"+serverName+":"+port+contextPath+"/phoneInterface/downloadAPK.htm?type="+type;}
    	isSuccess = true; resultMap.put("version", version); resultMap.put("path", path);
		resultMap.put("flag", isSuccess);
		return resultMap;
	}
	
	/**
	 * Android手机获取增量包版本
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getPatchVersion")
	public @ResponseBody Map<String,Object> getPatchVersion(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isSuccess = false;
		int type = getIntParams(request, "type", 1);// 默认林洋APP：1林洋标识;2中性APP
    	String version = APKFileUtil.getCurrentPatchVersion(type);
    	String path = "";String scheme = request.getScheme();String serverName = request.getServerName();int port = request.getServerPort();String contextPath = request.getContextPath(); 
    	if (scheme != null && scheme.trim().length()>0 && serverName != null && serverName.trim().length()>0 && port > 0 && contextPath != null && contextPath.trim().length()>0) {path = scheme+"://"+serverName+":"+port+contextPath+"/phoneInterface/downloadPatch.htm?type="+type;}
		isSuccess = true;
		resultMap.put("version", version);
		resultMap.put("path", path);
		resultMap.put("flag", isSuccess);
		return resultMap;
	}
	
	/**
	 * 下载手机APK增量包
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadPatch")
	public ModelAndView downloadPatch(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("redirect:/" + APKFileUtil.getNewPatchName(getIntParams(request, "type", 1))); // 默认林洋APP：1林洋标识;2中性APP;
	}
	
	/**
	 * 用户免扰设置
	 * @param request
	 * @return
	 */
	@RequestMapping("/setFreeTime")
	public @ResponseBody Map<String,Object> setFreeTime(HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isSuccess = false;
		String userName=getStrParam(request, "userName","");String password=getStrParam(request, "password","");if(userName==null||password==null){return null;}
        	//身份认证
            UserBean userBean = viewUserVerification(userName, password);
            if(userBean == null)
                return null;
            // 更新用户时段
            isSuccess = userService.updateFreeTimeProid(userBean.getAccountId(), getStrParam(request, "freetime", ""), getIntParams(request, "isShield", 1));

		resultMap.put("flag", isSuccess);
		return resultMap;
	}

    /**
     * 第三方调用：获取曲线数据接口
     *
     * @param objId
     *            查询对象ID
     * @param objType
     *            对象类型（1、能管对象；2、计量点）
     * @param dataType
     *            数据类型:1.电压;2.电流;3.有功功率;4.无功功率;5.功率因数;6.电量;7.电压相位角;8.电流相位角;9.需量曲线;10.谐波电压;11.谐波电流;12.电能示值;13.水量;14.水示值;
     * @param density
     *            数据密度（1、15分钟; 2、30分钟; 3、60分钟）
     * @param beginTime
     *            开始时间（格式为：yyyyMMddHHmm）
     * @param endTime
     *            结束时间（格式为：yyyyMMddHHmm）
     * @return
     */
    @RequestMapping(value = "/getNeedCurData")
    public @ResponseBody List<Map<String, Object>> getNeedCurData(final HttpServletRequest request, String userName, String password,
                                                                  long objId, int objType, int dataType, Integer density, String beginTime, String endTime) {

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        //数据密度不传的话默认15分钟
        if(null == density){
            density = 1;
        }

        //userName、password 验证
        UserBean user = viewUserVerification(userName, password);
        if (user == null){
            returnList.add(new HashMap<String, Object>(){{put("msg", "用户名密码验证失败");}});
            return returnList;
        }

        //objType 验证
        if(objType != 1 && objType != 2){
            returnList.add(new HashMap<String, Object>(){{put("msg", "objType参数不正确");}});
            return returnList;
        }

        //beginTime、endTime 验证
        Date beginDate = DateUtil.convertStrToDate(beginTime, "yyyyMMddHHmm");
        Date endDate = DateUtil.convertStrToDate(endTime, "yyyyMMddHHmm");
        if(null == beginDate || null == endDate){
            returnList.add(new HashMap<String, Object>(){{put("msg", "日期格式错误");}});
            return returnList;
        }

        //验证用户数据权限
        boolean canFlag = this.phoneService.getIfUserCanGetData(user, objId, objType);
        if(!canFlag){
            returnList.add(new HashMap<String, Object>(){{put("msg", "此用户没有权限访问该对象");}});
            return returnList;
        }

        List<Map<String, Object>> list2 = this.phoneService.getNeedCurData(objId, objType, dataType, density, beginDate, endDate);

        // 返回数据
        if(CollectionUtil.isNotEmpty(list2)){
            returnList.addAll(list2);
        }
        return returnList;
    }

    /**
     * 第三方调用：获取日数据接口
     *
     * @param objId
     *            查询对象ID
     * @param objType
     *            对象类型（1、能管对象；2、计量点）
     * @param dataType
     *            数据类型:
     * @param beginTime
     *            开始时间（格式为：yyyyMMdd）
     * @param endTime
     *            结束时间（格式为：yyyyMMdd）
     * @return
     */
    @RequestMapping(value = "/getNeedDayData")
    public @ResponseBody List<Map<String, Object>> getNeedDayData(final HttpServletRequest request, String userName, String password,
                                                                  long objId, int objType, int dataType, String beginTime, String endTime) {

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        //userName、password 验证
        UserBean user = viewUserVerification(userName, password);
        if (user == null){
            returnList.add(new HashMap<String, Object>(){{put("msg", "用户名密码验证失败");}});
            return returnList;
        }

        //objType 验证
        if(objType != 1 && objType != 2){
            returnList.add(new HashMap<String, Object>(){{put("msg", "objType参数不正确");}});
            return returnList;
        }

        //beginTime、endTime 验证
        Date beginDate = DateUtil.convertStrToDate(beginTime, "yyyyMMdd");
        Date endDate = DateUtil.convertStrToDate(endTime, "yyyyMMdd");
        if(null == beginDate || null == endDate){
            returnList.add(new HashMap<String, Object>(){{put("msg", "日期格式错误");}});
            return returnList;
        }

        //验证用户数据权限
        boolean canFlag = this.phoneService.getIfUserCanGetData(user, objId, objType);
        if(!canFlag){
            returnList.add(new HashMap<String, Object>(){{put("msg", "此用户没有权限访问该对象");}});
            return returnList;
        }

        List<Map<String, Object>> list2 = this.phoneService.getNeedDayData(objId, objType, dataType, beginDate, endDate);

        // 返回数据
        if(CollectionUtil.isNotEmpty(list2)){
            returnList.addAll(list2);
        }
        return returnList;
    }
	
	
	/**
	 * 新增一个查询付费率电量方法
	 * 用于替换电量10天以上的图表展示
	 * @author catkins.
	 * @param request
	 * @param traceFlag
	 * @param userName
	 * @param password
	 * @param dataType
	 * @param objId
	 * @param objType	1.能管对象,2.测量点
	 * @param baseDate
	 * @param dateType
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/2/28 16:26
	 */
	@RequestMapping(value = "/queryERateData")
	public @ResponseBody Map<String, Object> queryERateData(final HttpServletRequest request, Integer traceFlag, String userName, String password,
																	int dataType, long objId, int objType,String baseDate, int dateType) {
		//身份认证
		UserBean user = viewUserVerification(userName, password);
		if (user == null){
			return null;
		}
		Log.info("/queryERateData调用：userName=" + userName + ",password=" + password
				+ ",traceFlag=" + traceFlag + ",dataType=" + dataType + ",objId=" + objId + ",objType=" + objType
				+ ",baseDate=" + baseDate + ",dateType=" + dateType);

		Long accountId = user.getAccountId();
		Date sDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");
		Map<String, Object> map = null;
		if (objType == 1) {
			map = phoneService.queryLedgerFeeData( objId, dateType, sDate );
		} else {
			map = phoneService.queryMeterFeeData( objId, dateType, sDate );
		}

		if (traceFlag != null && traceFlag > 0)
			this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_164, 12L, 2);
    	return map;
	}
	
	
	/**
	 * 跳转到能耗数据页面
	 * @param request
	 * @return
	 */
//	@RequestMapping("/energyData")
//	public ModelAndView gotoEnergyDataPage(HttpServletRequest request) {
//		String userName = getStrParam(request, "userName", "");
//		String password = getStrParam(request, "password", "");
//		if(userName==null||password==null){
//			return null;
//		}
//		// 身份认证
//		UserBean userBean = viewUserVerification(userName, password);
//		if (userBean == null)
//			return null;
//		// 获取参数
//		long objId = getLongParam(request, "objId", -1);
//		int dataType = getIntParams(request, "dataType", 12);
//		int objType = getIntParams(request, "objType", 1);
//		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
//		int dateType = getIntParams(request, "dateType", 1);
//		long ledgerId = objId;
//		if (objType != 1){ // 如果是计量点,获取分户Id
//			ledgerId = this.phoneService.getLedgerIdByMeterId(objId);
//		} else {
//			if (objId == -1)
//				objId = userBean.getLedgerId();
//		}
//		// 获取分户信息
//		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
//		String ledgerName = "";
//		if (ledgerBean != null)
//			ledgerName = ledgerBean.getLedgerName();
//		request.setAttribute("ledgerId", ledgerId); // 分户Id
//		request.setAttribute("ledgerName", ledgerName); // 分户名称
//		request.setAttribute("userName", userBean.getLoginName()); // 用户名
//		request.setAttribute("password", userBean.getLoginPassword()); // 密码
//		request.setAttribute("dataType", dataType); // 默认数据类型
//		request.setAttribute("objType", objType);  // 对象类型,1 分户   2计量点
//		request.setAttribute("objId", objId); // 如果未传入objId,默认取所属分户ID
//		request.setAttribute("baseDate", baseDate); // 默认今天
//		request.setAttribute("dateType", dateType); // 0日,1 3日,2 10日,3 30日,4 年
//		return new ModelAndView("energyApp/app_energy_data");
//	}
	
	
	@RequestMapping( value="/queryEnergyData" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryEnergyData(HttpServletRequest request){
		String userName = getStrParam(request, "userName", "");
		String password = getStrParam(request, "password", "");
		if(userName==null||password==null){
			return null;
		}
		// 身份认证
		UserBean userBean = viewUserVerification(userName, password);
		if (userBean == null)
			return null;
		// 获取参数
		long objId = getLongParam(request, "objId", -1);
		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		int dateType = getIntParams(request, "dateType", 1);
		int objType = getIntParams(request, "objType", 1);
		int showType = getIntParams(request, "showType", 1);
		
		Map<String,Object> result = new HashMap<>( 0 );
		List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>(0);
		
		long ledgerId = objId;
		LedgerBean ledgerBean = null;
		if (objType != 1) { // 如果是计量点,获取分户Id
			objId = this.phoneService.getLedgerIdByMeterId( objId );
			ledgerBean = this.phoneService.getLedgerById( objId );
			ledgerId = ledgerBean.getLedgerId();
		} else {
			ledgerBean = this.phoneService.getLedgerById( objId );
			ledgerId = ledgerBean.getLedgerId();
		}
		
		if ( ledgerBean != null && ledgerBean.getAnalyType() != 102 &&
				ledgerBean.getAnalyType() != 104 && ledgerBean.getAnalyType() != 105) {
			ledgerBean = this.phoneService.queryParentLedger( ledgerBean.getLedgerId() );
			ledgerId = ledgerBean.getLedgerId();
		}
		
		
		if ( ledgerBean.getAnalyType() != 104 && ledgerBean.getAnalyType() != 105 ) {
			datas = this.phoneService.queryEnergyData(ledgerId,baseDate,dateType,showType);
		} else {
			datas = this.phoneService.queryEnergyData4Parent(ledgerId,baseDate,dateType,showType);
		}
		
		List<Map<String, Object>> lm = this.phoneService.queryTradeBenData( "LM" );
		
		result.put( "datas",datas );
		result.put( "tradeBen",lm );
		Long accountId = userBean.getAccountId();
		this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_172, 12L, 2);
		return result;
	}
	
	/**
	 * 跳转到能耗数据页面
	 * dataType == 13
	 * @param request
	 * @return
	 */
	@RequestMapping("/revstop")
	public ModelAndView gotorevstopPage(HttpServletRequest request) {
		String userName = getStrParam(request, "userName", "");
		String password = getStrParam(request, "password", "");
		if(userName==null||password==null){
			return null;
		}
		// 身份认证
		UserBean userBean = viewUserVerification(userName, password);
		if (userBean == null)
			return null;
		// 获取参数
		long objId = getLongParam(request, "objId", -1);
		int dataType = getIntParams(request, "dataType", 13);
		int objType = getIntParams(request, "objType", 1);
		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		long ledgerId = objId;
		if (objType != 1){ // 如果是计量点,获取分户Id
			ledgerId = this.phoneService.getLedgerIdByMeterId(objId);
		} else {
			if (objId == -1)
				objId = userBean.getLedgerId();
		}
		// 获取分户信息
		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
		String ledgerName = "";
		if (ledgerBean != null)
			ledgerName = ledgerBean.getLedgerName();
		
		/* 获取当前用户下能看到的所有企业 */
		List<Map<String, Object>> companyList = ledgerManagerMapper.getCompanyList( userBean.getLedgerId(), 102 );
		request.setAttribute("ledgerId", ledgerId); // 分户Id
		request.setAttribute("ledgerName", ledgerName); // 分户名称
		request.setAttribute("userName", userBean.getLoginName()); // 用户名
		request.setAttribute("password", userBean.getLoginPassword()); // 密码
		request.setAttribute("dataType", dataType); // 默认数据类型
		request.setAttribute("objType", objType);  // 对象类型,1 分户   2计量点
		request.setAttribute("objId", objId); // 如果未传入objId,默认取所属分户ID
		request.setAttribute("baseDate", baseDate); // 默认今天
		request.setAttribute("companyList", companyList); // 企业下拉框数据
		this.userAnalysisService.addAccountTrace(userBean.getAccountId(), OperItemConstant.OPER_ITEM_173, 12L, 2);
		return new ModelAndView("energyApp/app_revstop_data");
	}
	
	/**
	 * 			获取企业下变压器列表
	 * @param request
	 * @param objId    企业id
	 * @param baseDate 页面选择的时间
	 * @param showType 02 暂停 || 01 启用
	 * @return
	 */
	@RequestMapping( value="/queryTransData" , method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> queryTransData(final HttpServletRequest request,long objId,String baseDate,String showType) {
		Date beginTime = DateUtil.convertStrToDate( baseDate+" 00:00:00" );
		List<Map<String, Object>> trans = phoneService.queryTransformerByLedgerId( objId, beginTime, showType );
		return trans;
	}
	
	/**
	 * 			根据启用暂停获取企业下变压器列表,选择暂停获取所有启用变压器
	 * 					选择启用获取所有暂停变压器
	 * @param request
	 * @param objId    企业id
	 * @param baseDate 页面选择的时间
	 * @param showType 02 暂停 || 01 启用
	 * @return
	 */
	@RequestMapping( value="/queryTransListData" , method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryTransListData(final HttpServletRequest request,long objId,String baseDate,String showType) {
		Map<String,Object> result = new HashMap<String,Object>( 0 );
		Date beginTime = DateUtil.convertStrToDate( baseDate+" 00:00:00" );
		try {
			List<TransformerBean> trans = phoneService.queryTransformerByShowType( objId, beginTime, showType );
			if( trans.size() > 0){
				result.put( "code","200" );
				result.put( "msg","请求成功" );
				result.put( "result",trans );
			} else {
				result.put( "code","100" );
				result.put( "msg","暂无数据" );
			}
		} catch (Exception e) {
			result.put( "code","500" );
			result.put( "msg","请求失败" );
		}
		return result;
	}
	
	
	
	/**
	 *		获取变压器详细信息
	 * @param request
	 * @param objId		  变压器ID
	 * @return
	 */
	@RequestMapping( value="/getTransDataById" , method = RequestMethod.POST )
	public @ResponseBody Map<String,Object> getTransDataById(final HttpServletRequest request,long objId){
		return phoneService.queryTransformerData(objId);
	}
	
	/**
	 *		变更变压器状态方法
	 * @param request
	 * @param objId		  变压器ID
	 * @param startTime	  启用时间
	 * @param stopTime    停用时间
	 * @param runStatus   (启停状态   01 ||  02  )
	 * @return
	 */
	@RequestMapping( value="/modifyTrans" , method = RequestMethod.POST )
	public @ResponseBody Map<String,Object> modifyTrans(final HttpServletRequest request,long objId, String startTime,String stopTime,String runStatus){
		Map<String,Object> result = new HashMap<String,Object>( 0 );
		Integer line = phoneService.modifyTrans( objId, startTime, stopTime, runStatus );
		result.put( "code", line>0?200:500);
		return result;
	}
	
	
	
	
	
	/**
	 * 跳转到产量申报页面
	 * dataType == 14
	 * @param request
	 * @return
	 */
	@RequestMapping("/declarePage")
	public ModelAndView gotoDeclarePage(HttpServletRequest request) {
		String userName = getStrParam(request, "userName", "");
		String password = getStrParam(request, "password", "");
		if(userName==null||password==null){
			return null;
		}
		// 身份认证
		UserBean userBean = viewUserVerification(userName, password);
		if (userBean == null)
			return null;
		// 获取参数
		long objId = getLongParam(request, "objId", -1);
		int dataType = getIntParams(request, "dataType", 14);
		int objType = getIntParams(request, "objType", 1);
		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		long ledgerId = objId;
		if (objType != 1){ // 如果是计量点,获取分户Id
			ledgerId = this.phoneService.getLedgerIdByMeterId(objId);
		} else {
			if (objId == -1)
				objId = userBean.getLedgerId();
		}
		// 获取分户信息
		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
		String ledgerName = "";
		if (ledgerBean != null)
			ledgerName = ledgerBean.getLedgerName();
		
		/* 获取当前用户下能看到的所有企业 */
		List<Map<String, Object>> companyList = ledgerManagerMapper.getCompanyList( userBean.getLedgerId(), 102 );
		request.setAttribute("ledgerId", ledgerId); // 分户Id
		request.setAttribute("ledgerName", ledgerName); // 分户名称
		request.setAttribute("userName", userBean.getLoginName()); // 用户名
		request.setAttribute("password", userBean.getLoginPassword()); // 密码
		request.setAttribute("dataType", dataType); // 默认数据类型
		request.setAttribute("objType", objType);  // 对象类型,1 分户   2计量点
		request.setAttribute("objId", objId); // 如果未传入objId,默认取所属分户ID
		request.setAttribute("baseDate", baseDate); // 默认今天
		request.setAttribute("companyList", companyList); // 企业下拉框数据
		this.userAnalysisService.addAccountTrace(userBean.getAccountId(), OperItemConstant.OPER_ITEM_173, 12L, 2);
		return new ModelAndView("energyApp/app_declare_data");
	}
	
	
	/**
	 * 	查询产量申报列表
	 * @param request
	 * @param objId			能管对象ID
	 * @param showType		展示数据类型
	 * @param baseDate		基准时间
	 * @return
	 */
	@RequestMapping( value="/getProduction" , method = RequestMethod.POST )
	public @ResponseBody List<Map<String,Object>> getProductionList(final HttpServletRequest request,long objId,int showType,String baseDate){
		Date beginTime = DateUtil.convertStrToDate( baseDate+" 00:00:00" );
		return phoneService.queryProductionList(objId,beginTime,showType);
	}
	
	/**
	 * 查询能管对象下所有106能管对象的测量点
	 * @param request
	 * @param objId
	 * @return
	 */
	@RequestMapping( value="/getPorductList" , method = RequestMethod.POST )
	public @ResponseBody Map<String,Object> queryPorductionList4APP(HttpServletRequest request,long objId,String baseDate){
		Map<String,Object> result = new HashMap<String,Object>( 0 );
		try {
			Date beginTime = DateUtil.convertStrToDate( baseDate+" 00:00:00" );
			List<CapacityDeclarationBean> datas = phoneService.queryproductionList4APP( objId,beginTime );
			if( datas.size() > 0){
				result.put( "code","200" );
				result.put( "msg","请求成功" );
				result.put( "result",datas );
			} else {
				result.put( "code","100" );
				result.put( "msg","暂无数据" );
			}
		} catch (Exception e) {
			result.put( "code","500" );
			result.put( "msg","请求失败" );
		}
		return result;
	}
	
	/**
	 * 保存申报的产量信息
	 * @param request
	 * @param meterId		测量点ID
	 * @param ledgerId		能管对象ID
	 * @param dataDate		数据时间
	 * @param yield96		96%产量
	 * @param yield97		97%产量
	 * @param yield98		98%产量
	 * @param yieldOther	其他纯度产量
	 * @param yieldTotal	合计产量
	 * @return
	 */
	@RequestMapping( value="/declarePorduction" , method = RequestMethod.POST )
	public @ResponseBody Map<String,Object> declarePorduction(HttpServletRequest request,long meterId,long ledgerId,String dataDate,
															  double yield96,double yield97,double yield98,double yieldOther,double yieldTotal){
		Map<String,Object> result = new HashMap<String,Object>( 0 );
		Integer line = phoneService.declarePorduction( meterId, ledgerId, dataDate, yield96, yield97, yield98, yieldOther, yieldTotal );
		result.put( "code",line>0?200:500 );
		return result;
	}
	
	/**
	 * 电费计算所需参数
	 * @param objId		对象id
	 * @param objType	对象类型(1能管对象  2测量点)
	 * @param baseTime	基准时间
	 * @param dateType	时间类型(2前月  1上月  0本月)
	 * @return
	 */
	private Map<String,Object> restructuringMap(Long objId,Integer objType,String baseTime,Integer dateType){
		Map<String,Object> param = new HashMap<String,Object>( 0 );
		param.put( "meterType", 1 );
		param.put( "queryType", 1 );
		param.put( "objId", objId );
		param.put( "objType", objType );
		param.put( "dateType", dateType );
		param.put( "baseTime", baseTime+" 00:00:00" );
		return param;
	}
	
	
	/**
	 * 跳转容需对比页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/contrastPage")
	public ModelAndView gotoContrastPage(HttpServletRequest request) {
		String userName = getStrParam(request, "userName", "");
		String password = getStrParam(request, "password", "");
		if(userName==null||password==null){
			return null;
		}
		// 身份认证
		UserBean userBean = viewUserVerification(userName, password);
		if (userBean == null)
			return null;
		// 获取参数
		long objId = getLongParam(request, "objId", -1);
		int objType = getIntParams(request, "objType", 1);
		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		long ledgerId = objId;
		if (objType != 1){ // 如果是计量点,获取分户Id
			ledgerId = this.phoneService.getLedgerIdByMeterId(objId);
		} else {
			if (objId == -1)
				objId = userBean.getLedgerId();
		}
		// 获取分户信息
		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
		String ledgerName = "";
		if (ledgerBean != null)
			ledgerName = ledgerBean.getLedgerName();
		request.setAttribute("ledgerId", ledgerId); // 分户Id
		request.setAttribute("ledgerName", ledgerName); // 分户名称
		request.setAttribute("userName", userBean.getLoginName()); // 用户名
		request.setAttribute("password", userBean.getLoginPassword()); // 密码
		request.setAttribute("objType", objType);  // 对象类型,1 分户   2计量点
		request.setAttribute("objId", objId); // 如果未传入objId,默认取所属分户ID
		request.setAttribute("baseDate", baseDate); // 默认今天
		return new ModelAndView("energyApp/analysis/fee_contrast_page");
	}
	
	/**
	 * 查询容需对比数据
	 * @param request
	 * @param objId 		对象id
	 * @param objType		对象类型(1能管对象  2测量点)
	 * @param baseTime		基准时间
	 * @return
	 */
	@RequestMapping(value = "/getContrastData", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryContrastData(HttpServletRequest request,Long objId,Integer objType,String baseDate){
		Map<String, Object> param = restructuringMap( objId, objType, baseDate==null||baseDate.equals( "" )?DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN):baseDate, 0 );
		Map<String, Object> map = phoneService.queryEleContrastData( param );
		return map;
	}
	
	
	/**
	 * 能耗查询数据
	 * @param request
	 * @param objId 		对象id
	 * @param objType		对象类型(1能管对象  2测量点)
	 * @param baseTime		基准时间
	 * @return
	 */
	@RequestMapping(value = "/queryDetailData", method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> queryDetailData(HttpServletRequest request){
		// 获取参数
		long objId = getLongParam(request, "objId", -1);
		String baseDate = getStrParam(request, "baseDate", DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
		int dateType = getIntParams(request, "dateType", 1);
		int objType = getIntParams(request, "objType", 1);
		return phoneService.queryDetailData( objId,objType,baseDate,dateType );
	}
	
	
	
	@RequestMapping(value = "/lineloseAnalysisNew")
	public @ResponseBody Map<String,Object> lineloseAnalysis_new(HttpServletRequest request, Integer traceFlag){
		//身份认证
		String userName = getStrParam(request, "userName","");
		String password = getStrParam(request, "password","");
		if(userName==null||password==null){
			return null;
		}
		UserBean user = viewUserVerification(userName, password);
		if(user == null){
			return null;
		}
		Long accountId = user.getAccountId();
		Map<String,Object> result = new HashMap<String, Object>();
		String beginTime = getStrParam(request, "beginTime", DateUtil.convertDateToStr(DateUtil.getDateBetween(new Date(), -2), DateUtil.SHORT_PATTERN));
		String endTime = getStrParam(request, "endTime", DateUtil.getYesterdayDateStr(DateUtil.SHORT_PATTERN));
		long objectId = getLongParam(request, "objectId",-3L);
		int type = getIntParams(request, "type", 1); //1表示（分户），2表示（级别1的计量点）
		int analyType = getIntParams(request, "analyType", 102);	// 104,105走管理员支线
		
		Long ledgerId = null;
		if(type == 1){
			ledgerId = objectId;
		}
		else{
			type = 2;
			ledgerId = this.phoneService.getLedgerIdByMeterId(objectId);
		}
		//分户信息
		LedgerBean ledgerBean = phoneService.getLedgerById(ledgerId);
		String ledgerName = "";
		if(ledgerBean != null){
			ledgerName = ledgerBean.getLedgerName();
		}
		result.put("ledgerId", ledgerId);
		result.put("ledgerName", ledgerName);
		// 1级别计量点列表
		List<Map<String,Object>> meterList = this.phoneService.getMeterListByLeger(ledgerId, 1);
		result.put("meterList", meterList);
		//饼图、柱状图所需数据
		result.putAll(this.phoneService.lossAnalysis_new(beginTime, endTime, objectId, type));
		
		//记录用户使用记录
		if(traceFlag != null && traceFlag > 0){
			this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_161, 104L, 2);
		}
		result.put("systemNo", getStrParam(request, "systemNo", ""));
		return result;
	}
	
	
	/**
	 * 线损子页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/lineloseChild")
	public ModelAndView gotoLineloseChildPage(HttpServletRequest request) {
		//身份认证
		String userName = getStrParam(request, "userName","");
		String password = getStrParam(request, "password","");
		if(userName==null||password==null){
			return null;
		}
		UserBean user = viewUserVerification(userName, password);
		if(user == null){
			return null;
		}
		// 获取参数
		String beginTime = getStrParam(request, "beginTime", DateUtil.convertDateToStr(DateUtil.getDateBetween(new Date(), -2), DateUtil.SHORT_PATTERN));
		String endTime = getStrParam(request, "endTime", DateUtil.getYesterdayDateStr(DateUtil.SHORT_PATTERN));
		long objectId = getLongParam(request, "objId",-3L);
		int type = getIntParams(request, "objType", 1); //1表示（分户），2表示（级别1的计量点）
		request.setAttribute("ledgerId", objectId); // 分户Id
		request.setAttribute("objType", type);  // 对象类型,1 分户   2计量点
		request.setAttribute("beginTime", beginTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("userName", user.getLoginName()); // 用户名
		request.setAttribute("password", user.getLoginPassword()); // 密码
		return new ModelAndView("energyApp/analysis/linelose_child");
	}
	
	/**
	 * 行业对标功能页
	 * 2020-11-18
	 * @param request
	 * @return
	 */
	@RequestMapping("/industryPage")
	public ModelAndView gotoIndustryPage(HttpServletRequest request) {
		//身份认证
		String userName = getStrParam(request, "userName","");
		String password = getStrParam(request, "password","");
		if(userName==null||password==null){
			return null;
		}
		UserBean user = viewUserVerification(userName, password);
		if(user == null){
			return null;
		}
		// 获取参数
		long objectId = getLongParam(request, "objId",-3L);
		int type = getIntParams(request, "objType", 1); //1表示（分户），2表示（级别1的计量点）
		request.setAttribute("objId", objectId); // 分户Id
		request.setAttribute("objType", type);  // 对象类型,1 分户   2计量点
		request.setAttribute("userName", user.getLoginName()); // 用户名
		request.setAttribute("password", user.getLoginPassword()); // 密码
		return new ModelAndView("energyApp/app_industry_data");
	}
	
	
	/**
	 * 查询行业对标数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryIndustryData", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryIndustryData(HttpServletRequest request){
		// 获取参数
		long objId = getLongParam(request, "objId", -1);
		int objType = getIntParams(request, "objType", 1);
		long ledgerId = objId;
		if (objType != 1){ // 如果是计量点,获取分户Id
			ledgerId = this.phoneService.getLedgerIdByMeterId(objId);
		}
//		return phoneService.queryIndustryData( ledgerId );
		return phoneService.queryIndustryData( 1l );
	}
	
}
