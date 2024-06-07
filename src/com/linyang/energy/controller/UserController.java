/**
 * 
 */
package com.linyang.energy.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.model.*;
import com.linyang.energy.service.*;

import com.linyang.energy.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.page.Page;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;
import com.linyang.util.JacksonUtils;

/**
 * @类功能说明：用户管理的控制类
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄
 * @创建时间：2013-12-4 上午08:50:36
 * @版本：V1.0
 */
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private LedgerManagerService ledgerService;
    @Autowired
    private MeterManagerService meterService;
    @Autowired
    private PhoneService phoneService;
	@Autowired
	private LedgerManagerService ledgerManagerService;
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	
	protected JacksonUtils jacksonUtils = JacksonUtils.getInstance();
	/**
	 * 
	 * 函数功能说明 :跳转到用户列表页面
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoUserManage")
	public ModelAndView gotoUserManage(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentTime = DateUtils.getCurrentTime(DateUtils.FORMAT_LONG);
		map.put("currentTime", currentTime);
		map.put("roleList",userService.getRoleList());
		Long accountId = super.getSessionUserInfo(request).getAccountId();
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		List<GroupBean> groupList = groupService.getUserGroupByType(accountId, GroupBean.GROUP_LEDGER, null);
		map.put("groupList", groupList);
		map.put("ledgerId", ledgerId);
		return new ModelAndView("energy/system/userManage",map);
	}
	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param accountId 用户ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoUserAddUpdate")
	public ModelAndView gotoUserAddUpdate(HttpServletRequest request,String accountId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> returnGroups = new ArrayList<Map<String,Object>>();
		List<GroupBean> selectedGroupList = new ArrayList<GroupBean>();
		StringBuffer selectedGroupStr = new StringBuffer();
		if(CommonMethod.isNotEmpty(accountId)){
			UserBean userBean = userService.getUserByAccountId(Long.valueOf(accountId));
			map.put("user",userBean );
			map.put("type",1);//修改
			map.put("status", Long.valueOf(userBean.getAccountStatus().toString()));
			Map<String, String> map2 = DictionaryDataFactory.getDictionaryData(request).get("system_status");
			Map<Long, String> mapList = new HashMap<Long, String>();
			for (String key : map2.keySet()) {
				mapList.put(Long.parseLong(key), map2.get(key));
			}
			map.put("statusMap",mapList);
			// 获取与该用户关联的客户群组
			selectedGroupList = groupService.getUserGroupRealtionByType(Long.valueOf(accountId), GroupBean.GROUP_LEDGER, null);
			// 标识数据权限为：群组
			if(selectedGroupList.size() > 0){
				map.put("authorityGroup",true);
			} 
			
		}else{
			map.put("type",0);//添加
		}
		// 获取登陆用户创建的客户群组下拉列表
		long loginAccountId = this.getSessionUserInfo(request).getAccountId();
		
			List<GroupBean> groupList = groupService.getUserGroupByType(loginAccountId, GroupBean.GROUP_LEDGER, null);
			Map<String,Object> resultGroup = null;
			for (GroupBean bean : selectedGroupList) {
				resultGroup = new HashMap<String, Object>();
				if(selectedGroupStr.toString().length()>0){
					selectedGroupStr.append("|"); selectedGroupStr.append(bean.getGroupName());
				} else {
					selectedGroupStr.append(bean.getGroupName());
				}
				for (GroupBean groupBean : groupList) {
					if(bean.getGroupId().longValue() == groupBean.getGroupId().longValue()){
						resultGroup.put("GROUPID", bean.getGroupId());
						resultGroup.put("GROUPNAME", bean.getGroupName());
						resultGroup.put("PUBLICTYPE", bean.getPublicType());
						resultGroup.put("CHECKED", true);
						returnGroups.add(resultGroup);
						groupList.remove(groupBean);
						break;
					}
				}
			}
			for (GroupBean bean : groupList) {
				resultGroup = new HashMap<String, Object>();
				resultGroup.put("GROUPID", bean.getGroupId());
				resultGroup.put("GROUPNAME", bean.getGroupName());
				resultGroup.put("PUBLICTYPE", bean.getPublicType());
				resultGroup.put("CHECKED", false);
				returnGroups.add(resultGroup);
			}
			map.put("groupMaps",returnGroups);
		map.put("selectedGroupStr", selectedGroupStr);
		map.put("roleList",userService.getRoleList());
		// 获取登陆用户的分户下拉列表
//		if(ledgerId==0){
//			map.put("ledgerList",ledgerService.getUserLedger(loginAccountId));
//		} else {
//			map.put("ledgerList",userService.getLedgerList(ledgerId));
//		}
		return new ModelAndView("energy/system/user_add_update",map);
	}
	
	/**
	 * 
	 * 函数功能说明  :查询用户的列表
	 * @param pageInfo 前台参数集合
	 * @return  Map<String,Object>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getAjaxPageData")
	public @ResponseBody Map<String, Object> getAjaxPageData(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		int single = 0;
		Long ledgerId = 0L;
			Page page = null;
			Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
			if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
				page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
			else
				page = new Page();
			
			if(queryMap.containsKey("queryMap")){
                mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
                ledgerId = Long.valueOf(mapQuery.get("ledgerId").toString());
            }
			UserBean userBean = super.getSessionUserInfo(request);
			mapQuery.put("accountId", userBean.getAccountId());
			if (ledgerId == 0L) {
				//plat admin可查看所有用户
				//ledgerId = super.getSessionUserInfo(request).getLedgerId();		
				if (userBean.getLedgerId() == 0L) {
					//权限为群组分配ledgerId
					ledgerId = ledgerManagerService.getLedgerIfNull(userBean.getAccountId());
				} else {
					ledgerId = userBean.getLedgerId();
				}
				LedgerBean ledgerBean = ledgerService.selectByLedgerId(ledgerId);
				Integer analyType = ledgerBean.getAnalyType();
				if (ledgerId != 1 && analyType != 105) {
                    single = 1;
				}
                else if (ledgerId != 1 && analyType == 105) {
					//平台运营商能看到下属企业及能管单元
                    single = 0;
				}				
			}
            else{
                single = 1;
			}
			mapQuery.put("ledgerId", ledgerId);
			mapQuery.put("single", single);
			List<Map<String,Object>> list = userService.getUserPageList(page,mapQuery);
			Map<String, String> map2 = DictionaryDataFactory.getDictionaryData(request).get("system_status");
			for (Map<String,Object> mm : list) {
                long accountId = mm.get("ACCOUNTID")!=null?Long.valueOf(mm.get("ACCOUNTID").toString()):0;
				mm.put("ACCOUNT_STATUS", map2.get(mm.get("ACCOUNTSTATUS").toString()));
                //等级、最后登陆时间、活跃状态
                Map<String, Integer> levelMap = this.phoneService.getLevelHelp(accountId);
                mm.put("curLevel", levelMap.get("curLevel"));
                String lastLogin = "";
                UserBean user = this.phoneService.getUserByAccountId(accountId);
                Date lastDate = user.getLastDate();
                if(lastDate != null){
                    lastLogin = DateUtil.convertDateToStr(lastDate, DateUtil.MOUDLE_PATTERN);
                }
                mm.put("lastLogin", lastLogin);
                int activeType = Integer.valueOf(mm.get("ACTIVESTATUS").toString());
                String activeStr = "";
                if (activeType == 1) {
					activeStr = "休眠用户";
				}else if (activeType == 2) {
					activeStr = "新用户";
				}else if (activeType == 3) {
					activeStr = "留存用户";
				}else if (activeType == 4) {
					activeStr = "活跃用户";
				}
                mm.put("activeStr", activeStr);

				// 针对群组,显示群组名
				StringBuffer groupName = new StringBuffer();
				String groupNameReplace = "";
				if(mm.get("LEDGERID")==null){
					List<String> groupNames = userService.getGroupNameByAccount(accountId);
					for (String name : groupNames) {
						
							groupName.append(","); groupName.append(name);		
					}
					groupNameReplace = groupName.toString().replaceFirst(",", "");
					mm.put("LEDGERNAME", groupNameReplace);

				}
			}
			map.put("queryMap",mapQuery);
			map.put("list",list);
			map.put("page", page);
		return map;
	}
    /**
     * 
     * 函数功能说明  :新增用户
     * @param user  用户参数信息
     * @return  boolean     
     */
	@RequestMapping("/addUserInfo")
	public @ResponseBody boolean addUserInfo (HttpServletRequest request,UserBean user){
		//默认的用户序列
		user.setAccountId(SequenceUtils.getDBSequence());
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("add a user:" + user.getLoginName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		isSuccess = userService.addUserInfo(user);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_USER,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
		
	}
	/**
     * 
     * 函数功能说明  :修改用户信息
     * @param user  用户参数信息
     * @return  boolean     
     */ 
	@RequestMapping("/updateUserInfo")
	public @ResponseBody boolean updateUserInfo (HttpServletRequest request,UserBean user){
		UserBean loginUser = this.getSessionUserInfo(request);
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("update a user:" + user.getLedgerName()).
        append(" by ").
        append(loginUser.getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		isSuccess = userService.updateUserInfo(loginUser,user);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_USER,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		  return isSuccess;	
			
	}
	/**
	 * 
	 * 函数功能说明  :删除用户
	 * @param userIds 选中的用户ID集合
	 * @param accoutStatus 用户的状态 0 ：停用，1：启用 ，2 ：删除
	 * @return  boolean     
	 */
	@RequestMapping("/updateUserStatus")
	public @ResponseBody boolean updateUserStatus (HttpServletRequest request,Long[] userIds,int accoutStatus){
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("delete users:");
        for(int i = 0; i < userIds.length; i++){
            Long userId = userIds[i];
            UserBean user = this.userService.getUserByAccountId(userId);
            desc.append(user.getLoginName() + ",");
        }
        UserBean loginUser = this.getSessionUserInfo(request);
        desc.append(" by ").
        append(loginUser.getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = userService.updateUserStatus(Arrays.asList(userIds), accoutStatus);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_USER,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		  return isSuccess;	
			
	}

	/**
	 * 函数功能说明  :检查登陆名称是否重复
	 * @param user 用户参数信息
	 * @param operType 操作类型 0：表示新增  1：表示编辑
	 * @return  boolean
	 */
	@RequestMapping("/checkLoginName")
	public @ResponseBody boolean checkLoginName (String loginName,int operType,Long accountId){
	     return userService.checkLoginName(loginName,operType,accountId);	
				
	}
	/**
	 * 函数功能说明  :获取角色下来框
	 * @return  List<UserBean>
	 */
	@RequestMapping("/getRoleList")
    public @ResponseBody List<UserBean> getRoleList(){
    	return userService.getRoleList();
    }
	/**
	 * 函数功能说明  :获取分户下来框
	 * @return  List<UserBean>
	 */
	@RequestMapping("/getLedgerList")
    public @ResponseBody List<UserBean> getLedgerList(HttpServletRequest request){
		long ledgerId = this.getSessionUserInfo(request).getLedgerId();
    	return userService.getLedgerList(ledgerId);
    }
	/**
	 * 函数功能说明  :获取用户下来框
	 * @return  List<UserBean>
	 */
	@RequestMapping("/getUserList")
    public @ResponseBody List<UserBean> getUserList(){
    	return userService.getUserList();
    }
	
	/**
	 * @description 获取该用户的所有群组
	 * @author yaojiawei
	 * @date 2014-08-11
	 * */
	@RequestMapping("/getAllTeam")
	public @ResponseBody List<GroupBean> getAllTeam(HttpServletRequest request)
	{
		UserBean user= this.getSessionUserInfo(request);
		return groupService.getUserGroupByPublicType(user.getAccountId(), null);
	}
	
	/**
	 * @description 获取该用户的所有私有群组
	 * @author yaojiawei
	 * @date 2014-08-12
	 * */
	@RequestMapping("/getAllPrivateTeam")
	public @ResponseBody List<GroupBean> getAllPrivateTeam(HttpServletRequest request)
	{
		UserBean user= this.getSessionUserInfo(request);
		return groupService.getUserGroupByPublicType(user.getAccountId(), GroupBean.TYPE_PRIVATE);
	}
	
	/**
	 * @description 获取所有公共组
	 * @author yaojiawei
	 * @date 2014-08-12
	 * */
	@RequestMapping("/getAllPublicTeam")
	public @ResponseBody List<GroupBean> getAllPublicTeam(HttpServletRequest request)
	{
		return groupService.getGroupByPublicType(GroupBean.TYPE_PUBLIC);
	}
	
	/**
	 * @description 获取所有我的公共组
	 * @author yaojiawei
	 * @date 2014-09-10
	 * */
	@RequestMapping("/getAllMyPublicTeam")
	public @ResponseBody List<GroupBean> getAllMyPublicTeam(HttpServletRequest request)
	{
		UserBean user= this.getSessionUserInfo(request);
		if(user.getAccountId()==1)//如果是超级管理员,直接获取分户或者计量点，不需要考虑权限
		{
			return groupService.getGroupByPublicType(GroupBean.TYPE_PUBLIC);
		}
		else
		{
			return groupService.getUserGroupByPublicType(user.getAccountId(), GroupBean.TYPE_PUBLIC);
		}
	}
	
	/**
	 *  @description 新增一个群组
	 * @author yaojiawei
	 * @date 2014-08-12
	 * */
	@RequestMapping("/addGroupInfo")
	public @ResponseBody boolean addGroupInfo(HttpServletRequest request,GroupBean group){
		//默认的用户序列
		group.setGroupId(SequenceUtils.getDBSequence());
		
		UserBean user= this.getSessionUserInfo(request);
		group.setAccountId(user.getAccountId());
		
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("add a group:" + group.getGroupName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = groupService.addGroupInfo(group);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
		
	}
	
	/**
	 *  @description 根据groupId获取group和当前用户所可以看到的分户
	 *  @description 感谢凌佳彦写了根据groupId和当前用户Id查询权限内的分户的方法，以资纪念
	 * @author yaojiawei
	 * @date 2014-08-14
	 * */
	@RequestMapping("/queryGroupInfo")
	public @ResponseBody GroupBean queryGroupInfo(HttpServletRequest request)
	{
		Long groupId = Long.parseLong(request.getParameter("groupId"));
		GroupBean group = groupService.getGroupById(groupId);
		
		UserBean user= this.getSessionUserInfo(request);
		Long ledgerId = user.getLedgerId();
		Integer groupType = group.getGroupType();
		if(user.getAccountId()==1)//如果是超级管理员,直接获取分户或者计量点，不需要考虑权限
		{
			if(groupType==1)//如果是分户组
			{
				group.setLedgerList(groupService.getSuperGroupLedger(groupId));
			}
			else
			{
				group.setMeterList(groupService.getSuperGroupMeter(groupId));
			}
		}
		else
		{
			if(groupType==1)//如果是分户组
			{
				if(!(ledgerId>0))
				{
					group.setLedgerList(groupService.getGroupLedger(groupId, user.getAccountId()));
				}
				else
				{
					LedgerBean ledger = ledgerService.selectByLedgerId(ledgerId);
			
					group.setLedgerList(groupService.getGroupLedger2(groupId, ledger.getLedgerLft(), ledger.getLedgerRgt()));
				}
			}
			else if(groupType==2)//如果是计量点组
			{
				if(!(ledgerId>0))
				{
					group.setMeterList(groupService.getGroupMeter(groupId, user.getAccountId()));
				}
				else//去t_ledger_tree查到所有的子分户节点，然后查出电表，两边比对一下即可 
				{
					group.setMeterList(groupService.getGroupMeter2(groupId, user.getAccountId()));
				}
			}
		}
		
		if( group.getAccountId().compareTo(user.getAccountId())==0)//如果群组的创建ID和当前用户ID相等，则说明这个组是这个用户创建的，拥有修改权
			group.setIfCurrentUserGroup(true);
		else
			group.setIfCurrentUserGroup(false);
		
		return group;
	}
	
	/**
	 *  @description 删除一个群组
	 * @author yaojiawei
	 * @date 2014-08-15
	 * */
	@RequestMapping("/deleteGroup")
	public @ResponseBody boolean deleteGroup(HttpServletRequest request){
		Long groupId = Long.parseLong(request.getParameter("groupId"));
		
		boolean isSuccess = false;
		
		UserBean user= this.getSessionUserInfo(request);
		
		StringBuffer desc = new StringBuffer();
		desc.append("delete a group:").
		append(groupService.getGroupById(groupId).getGroupName())
        .append(" by ").
		append(user.getLoginName()).
		append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		
			isSuccess = groupService.deleteGroup(groupId);
		
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
	}
	
	/**
	 *  @description 从群组下删除一个分户
	 * @author yaojiawei
	 * @date 2014-08-15
	 * */
	@RequestMapping("/deleteLedger")
	public @ResponseBody boolean deleteLedger(HttpServletRequest request){
		Long groupId = Long.parseLong(request.getParameter("groupId"));
		Long ledgerId = Long.parseLong(request.getParameter("ledgerId"));
        GroupBean groupBean = this.groupService.getGroupById(groupId);
        LedgerBean ledgerBean = this.ledgerService.getLedgerDataById(ledgerId);
		boolean isSuccess = false;
		
		UserBean user= this.getSessionUserInfo(request);
		
		StringBuffer desc = new StringBuffer();
		desc.append("delete a ledger:" + ledgerBean.getLedgerName() + " from group:" + groupBean.getGroupName()).
		append(" by ").
		append(user.getLoginName()).
		append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		
			isSuccess = groupService.deleteLedgerFromGroup(groupId, ledgerId);
		
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
	}
	
	/**
	 *  @description 从群组下删除一个计量点
	 * @author yaojiawei
	 * @date 2014-08-25
	 * */
	@RequestMapping("/deleteMeter")
	public @ResponseBody boolean deleteMeter(HttpServletRequest request){
		Long groupId = Long.parseLong(request.getParameter("groupId"));
		Long meterId = Long.parseLong(request.getParameter("meterId"));
        GroupBean groupBean = this.groupService.getGroupById(groupId);
        MeterBean meterBean = this.meterService.getMeterDataById(meterId);
		UserBean user= this.getSessionUserInfo(request);
		
		boolean isSuccess = false;

		StringBuffer desc = new StringBuffer();
		desc.append("delete a meter:" + meterBean.getMeterName() + " from group:" + groupBean.getGroupName()).
		append(" by ").
		append(user.getLoginName()).
		append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		
			isSuccess = groupService.deleteMeterFromGroup(groupId, meterId);
		
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
	}
	
	
	/**
	 *  @description 从群组下删除所有分户或计量点
	 * @author yaojiawei
	 * @date 2014-08-15
	 * */
	@RequestMapping("/truncAllLedger")
	public @ResponseBody boolean truncAllLedger(HttpServletRequest request){
		Long groupId = Long.parseLong(request.getParameter("groupId"));
        GroupBean groupBean = this.groupService.getGroupById(groupId);
		UserBean user= this.getSessionUserInfo(request);
		
		boolean isSuccess = false;

		StringBuffer desc = new StringBuffer();
		desc.append("delete all ledgers from group:" + groupBean.getGroupName()).
		append(" by ").
		append(user.getLoginName()).
		append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
//			groupService.deleteGroupLedgerRelation(groupId);
//			groupService.deleteMeterGroupRelation(groupId);
			isSuccess = groupService.truncAllLedger(groupId);
		
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
	}
	
	@RequestMapping("/addToGroup")
	public @ResponseBody boolean addToGroup(HttpServletRequest request) throws UnsupportedEncodingException, IOException{
		boolean isSuccess = false;
		String ledgerIds = request.getParameter("ledgerIds");
		if(null==ledgerIds){return isSuccess;}
		String groupIds = request.getParameter("groupIds");
		if(null==groupIds){return isSuccess;}
		String pointType = request.getParameter("pointType");
		if(null==pointType){return isSuccess;}
		String[] ledgerIdStr = ledgerIds.split(",");
		String[] groupIdStr = groupIds.split(",");
		
		if(pointType.equals("1"))//如果是分户
		{
			for (String str : groupIdStr) {
				Long groupId = Long.parseLong(str);
				isSuccess = groupService.addGroupLedgerRelation(ledgerIdStr, groupId);
			}
		}
		else if(pointType.equals("2"))//如果是计量点
		{
			for (String str : groupIdStr) {
				Long groupId = Long.parseLong(str);
				isSuccess = groupService.addGroupMpedRelation(ledgerIdStr, groupId);
			}
		}
		
		return isSuccess;	
	}
	
	@RequestMapping("/checkGroupName")
	public @ResponseBody boolean checkGroupName(HttpServletRequest request) 
	{
		boolean isSuccess = true;
		String groupName = request.getParameter("groupName");
		isSuccess = groupService.checkGroupName(groupName);
		return isSuccess;	
	}
	
	/**
	 *  @description 修改一个群组
	 * @author yaojiawei
	 * @date 2014-08-12
	 * */
	@RequestMapping("/updateGroupInfo")
	public @ResponseBody boolean updateGroupInfo(HttpServletRequest request,GroupBean group){	
		UserBean user= this.getSessionUserInfo(request);

		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("update a group:" + group.getGroupName()).
		append(" by ").
		append(user.getLoginName()).
		append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		group.setAccountId(user.getAccountId());
		
		isSuccess = groupService.updateGroupInfo(group);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
		
	}
	
	
	/**
	 *  @description 获取所有除自己的群组之外的所有群组，按所有者名字排序
	 * @author yaojiawei
	 * @date 2014-09-29
	 * */
	@RequestMapping("/getAllOtherTeam")
	public @ResponseBody List<GroupBean> getAllOtherTeam(HttpServletRequest request)
	{
		UserBean user= this.getSessionUserInfo(request);
		if(user.getAccountId()==1)//如果是超级管理员
		{
			return groupService.getAllOtherTeam(user.getAccountId());
		}
		return new ArrayList<GroupBean>();
	}
	
	/**
	 *  @description 显示管理其他群组的标志位
	 * @author yaojiawei
	 * @date 2014-09-30
	 * */
	@RequestMapping("/getManageFlag")
	public @ResponseBody boolean getManageFlag(HttpServletRequest request)
	{
		UserBean user= this.getSessionUserInfo(request);
		if(user.getAccountId()==1)//如果是超级管理员
			return true;
		return false;
	}
	
	/**
	 * 
	 * 函数功能说明  :得到该用户下可以有权限的用户
	 * @author guosen
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getLedgerData", method = RequestMethod.POST)
	public @ResponseBody String getLedgerData(HttpServletRequest request) {
		String str = "";
		StringBuffer sb = new StringBuffer("[");
		Long ledgerId = this.getSessionUserInfo(request).getLedgerId();
		Long accountId = this.getSessionUserInfo(request).getAccountId();
		if( accountId != null && accountId == 1)
			ledgerId = -100L;//超级管理员特殊处理
		
		// 获取登陆用户的分户下拉列表
		if(ledgerId==0){
			List<LedgerBean> ledgerList = ledgerService.getUserLedger(accountId);
			//按照插件所需要的格式拼接字符串
			if(ledgerList.size()>0) {
				/**
				 *  id为每个选项的ID,默认为"id"
					value为每个选项的值,默认为'value'
					label为每个选项显示的值，默认为'label'
					num为数字搜索,默认为'num'
				 */
				for(LedgerBean user : ledgerList) {
					sb.append("{id:'").append(user.getLedgerId())
						.append("',label:'").append(user.getLedgerName())
						.append("',value:'").append(user.getLedgerId()).append("',num:'")
						.append(user.getLedgerId()).append("',count:'0'},");
				}	
				str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
			} 	
			return str;
		} else {
			List<UserBean> userList = userService.getLedgerList(ledgerId);
			//按照插件所需要的格式拼接字符串
			if(userList.size()>0) {
				for(UserBean user : userList) {
					sb.append("{id:'").append(user.getLedgerId())
						.append("',label:'").append(user.getLedgerName())
						.append("',value:'").append(user.getLedgerId()).append("',num:'")
						.append(user.getLedgerId()).append("',count:'0'},");
				}	
				str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
			} 	
			return str;
		}
	}

	/**
	 * 进入用户免扰设置页面
	 * @param request
	 * @param accountId
	 * @return
	 */
	@RequestMapping("/gotoFreeTimeSet")
	public ModelAndView gotoFreeTimeSet(HttpServletRequest request,Model model){
		Map<String,Object> map = new HashMap<String, Object>();
		long accountId = super.getSessionUserInfo(request).getAccountId();
		UserBean userBean = userService.getUserByAccountId(accountId);
		if(userBean!=null && StringUtils.isNotBlank(userBean.getFreeTimePeriod())){
			String[] times = userBean.getFreeTimePeriod().split("-");
			map.put("startTime", times[0]);
			map.put("endTime", times[1]);
		}
		
		String tokenForSession = CSRFTokenManager.getTokenForSession(request.getSession());
		model.addAttribute("csrf",tokenForSession );
		return new ModelAndView("energy/system/user_freetime_set",map);
	}
	
	/**
	 * 更新用户免扰时段
	 * @param request
	 * @param freeTimeProid
	 * @return
	 */
	@RequestMapping("/updateFreeTimeProid")
	public @ResponseBody boolean updateFreeTimeProid(HttpServletRequest request,String freeTimeProid){
		boolean isSuccess = false;
		
		String tokenFromRequest = CSRFTokenManager.getTokenFromRequest(request);		
		
		String tokenForSession = CSRFTokenManager.getTokenForSession(request.getSession());		
		
		if(null!=tokenFromRequest&&tokenFromRequest.equals(tokenForSession))	{
			
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		
			long accountId = super.getSessionUserInfo(request).getAccountId();
			
			isSuccess = userService.updateFreeTimeProid(accountId, freeTimeProid, 1);	
			
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;

        //记录日志
        StringBuilder sb = new StringBuilder();
        sb.append("update no disturb set:")
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_ID_NO_DISTURB_SET, CommonOperaDefine.MODULE_NAME_NO_DISTURB_SET, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst, sb.toString()), request);
		}
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_131, 203l, 1);
		
        return isSuccess;
	}
	
	// add or update method by catkins.
	// date 2020/3/3
	// Modify the content: 解锁方法
	@RequestMapping(value = "/unclock" , method = RequestMethod.POST)
	public @ResponseBody boolean unclock(HttpServletRequest request){
		String accountId = request.getParameter( "accountId" );
		Integer line = userService.updateTodayTimes( 0, Long.parseLong( accountId ) );
		return line > 0;
	}
	//end
	
	
}
