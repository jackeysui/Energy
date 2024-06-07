package com.linyang.energy.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linyang.energy.service.PhoneService;
import com.linyang.energy.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.IndexMapper;
import com.linyang.energy.mapping.authmanager.GroupBeanMapper;
import com.linyang.energy.mapping.authmanager.UserBeanMapper;
import com.linyang.energy.model.CompanyDisplaySet;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.UserService;
import com.linyang.energy.utils.CipherUtil;
import com.linyang.util.CommonMethod;

/**
 * 
  @description:用户管理业务逻辑层接口实现类
  @version:0.1
  @author:Cherry
  @date:Dec 3, 2013
 */

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserBeanMapper userBeanMapper;
	@Autowired
	private GroupBeanMapper groupBeanMapper;
    @Autowired
    private PhoneService phoneService;
    @Autowired
    private IndexMapper indexMapper;

	@Override
	public boolean addUserInfo(UserBean userBean) {
		boolean isSuccess = false;
		
		if(userBean != null && CommonMethod.isNotEmpty(userBean.getLoginName())){
			//默认密码
			userBean.setLoginPassword(CipherUtil.generatePasswordSha256(userBean.getLoginPassword()));
			//默认时间
			userBean.setCreateDate(new Timestamp(System.currentTimeMillis()));
			//默认用户的状态
			userBean.setAccountStatus((short)1);
			//默认活跃度为“新用户”
			userBean.setActiveStatus(1);
			
			
			Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("loginName", userBean.getLoginName());
            queryMap.put("realName", userBean.getRealName());
			List<UserBean> userList = userBeanMapper.getFilteredUser(queryMap);
			if(userList.size()>0)
			{
				isSuccess =false;
			}
			else
			{
				userBeanMapper.insertSelective(userBean);
				userBeanMapper.addUserRoleData(userBean);
				// 若选择群组
				this.assignAuthority(userBean);
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	/**
	 *@函数功能说明 : 赋予用户新的群组数据权限
	 *
	 *@author chengq
	 *@date 2014-8-14
	 *@param
	 *@return
	 */
	private void assignAuthority(UserBean userBean){
		String groupIds = userBean.getGroupId();
		if(StringUtils.isNotBlank(groupIds)){
			// 清空数据权限：分户
			userBean.setLedgerId(0L);
			userBeanMapper.updateByPrimaryKeySelective(userBean);
			// 清空数据权限: 群组
			groupBeanMapper.deleteAccountGroupRelationById(userBean.getAccountId(), null);
			// 添加新的群组权限
			String[] groupIdArray = groupIds.split("-");
			for (String groupId : groupIdArray) {
				groupBeanMapper.addAccountGroupRelation(userBean.getAccountId(), Long.valueOf(groupId));
			}
		}
	}

	@Override
	public boolean updateUserStatus(List<Long> userIds,int status) {
		boolean isSuccess = false;
		if(CommonMethod.isCollectionNotEmpty(userIds)){
			userBeanMapper.updateUserStatus( userIds, status, new Date());		//原	userBeanMapper.updateUserStatus(CommonMethod.getSeparaterStringNumber(",", userIds), status, new Date());
			isSuccess = true;
		}
		
		return isSuccess;
	}
	
	@Override
	public boolean updateLastDate(UserBean userBean) {
		return userBeanMapper.updateLastDate(userBean)>0;
	}

	@Override
	public UserBean getUserByUserName(String username) {
		if(CommonMethod.isNotEmpty(username)){
			return userBeanMapper.getUserByUserName(username.trim());
		}
		return null;
	}

	@Override
	public  List<Map<String,Object>> getUserPageList(Page page,Map<String, Object> queryCondition) {
		 List<Map<String,Object>> list = null;
		if(page != null){
			Map<String,Object> map = new HashMap<String, Object>(queryCondition);
			map.put(Dialect.pageNameField, page);
            //等级搜索
            Integer begin = -1;
            Integer end = -1;
            if(map.keySet().contains("level") && map.get("level").toString().length()>0){
                Integer level = Integer.valueOf(map.get("level").toString());
                Map<String, Integer> region = this.phoneService.getLevelRegion(level);
                begin = region.get("begin");
                end = region.get("end");
            }
            map.put("begin", begin);
            map.put("end", end);
            if (map.containsKey("message")) {
            	list = userBeanMapper.getMessageUserPageData(map);
			}else {
				list = userBeanMapper.getUserPageData(map);
			}
			
		}
		return list == null?new ArrayList<Map<String,Object>>():list;
	}

	@Override
	public boolean isPasswordCorrect(long userId, String password) {
		boolean isSuccess = false;
		if(userId >0 && CommonMethod.isNotEmpty(password)){
            String pwd = CipherUtil.generatePasswordSha256(password);
			isSuccess = userBeanMapper.getUserCount(userId, pwd)>0;
		}
		return isSuccess;
	}

	@Override
	public boolean updateUserInfo(UserBean loginUser,UserBean userBean) {
		boolean isSuccess = false;
		if(userBean != null && userBean.getAccountId() != null){
			String oldpassword = getUserByAccountId(userBean.getAccountId()).getLoginPassword();
			if(!oldpassword.equals(userBean.getLoginPassword())) {
				if(CommonMethod.isNotEmpty(userBean.getLoginPassword())) {
					userBean.setLoginPassword(CipherUtil.encodeBySHA256(userBean.getLoginPassword()));
				} else {
					userBean.setLoginPassword("");
				}
			}
			userBean.setModifyTime(new Date());
			userBeanMapper.updateByPrimaryKeySelective(userBean);
			if(userBean.getRoleId()!=null&&userBean.getRoleId()>0)
				userBeanMapper.updateUserRoleData(userBean);
			// 若选择群组
			this.assignAuthority(userBean);
			// 若选择分户,清除用户与群组关系
			if(userBean.getLedgerId() != null && userBean.getLedgerId() != 0){
				groupBeanMapper.deleteAccountGroupRelationById(userBean.getAccountId(), null);
			}
			// 权限变更之后，针对session变化
			if(loginUser.getAccountId().longValue() == userBean.getAccountId().longValue()){
				loginUser.setLedgerId(userBean.getLedgerId());
			}
			isSuccess = true;
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.UserService#getUserByAccountId(java.lang.String)
	 */
	@Override
	public UserBean getUserByAccountId(Long accountId) {
		if(CommonMethod.isNotEmpty(accountId)){
			return userBeanMapper.getUserByAccountId(accountId);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.UserService#checkLoginName(java.lang.String)
	 */
	@Override
	public boolean checkLoginName(String loginName,int operType,Long accountId) {
		boolean isSuccess = false;
		if(CommonMethod.isNotEmpty(loginName)){
			if( userBeanMapper.checkLoginName(loginName,operType,accountId) > 0){
				isSuccess = false;
			}else{
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.UserService#getRoleList()
	 */
	@Override
	public List<UserBean> getRoleList() {
		return  userBeanMapper.getRoleList();
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.UserService#getLedgerList()
	 */
	@Override
	public List<UserBean> getLedgerList(long ledgerId) {
		return  userBeanMapper.getLedgerList(ledgerId);
	}

	@Override
	public List<Long> getLedgerIdList(long ledgerId) {
		return userBeanMapper.getLedgerIdList(ledgerId);
	}
	
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.UserService#getUserList()
	 */
	@Override
	public List<UserBean> getUserList() {
		return  userBeanMapper.getUserList();
	}

    @Override
    public List<UserBean> getCanBookUsers(long ledgerId, long accountId) {
        return  userBeanMapper.getCanBookUsers(ledgerId, accountId);
    }


	@Override
	public List<UserBean> getLedgerData() {
		return userBeanMapper.getLedgerData();
	}

	@Override
	public boolean checkFullScreen(Long ledgerId) {
		boolean isLinyang = false;
		CompanyDisplaySet set = this.indexMapper.getScreenSet(ledgerId);
		if(set != null){
			isLinyang = true;
		}
		return isLinyang;
	}

    @Override
    public void updateActiveStatus(Long accountId){
        int activeType = 0;//1.休眠用户 2.新用户 3.留存用户 4.活跃用户 
        Date date5 = DateUtil.clearDate(new Date());
        Date date4 = DateUtil.clearDate(DateUtil.getDateBetween(new Date(), -7));
        Date date3 = DateUtil.clearDate(DateUtil.getDateBetween(new Date(), -14));
        Date date2 = DateUtil.clearDate(DateUtil.getDateBetween(new Date(), -21));
        Date date1 = DateUtil.clearDate(DateUtil.getDateBetween(new Date(), -30));

        UserBean user = this.userBeanMapper.selectByPrimaryKey(accountId);
        Date createDate = user.getCreateDate();
        if(createDate.after(date1)){
            activeType = 2;
        }
        else {
            int loginCount4 = this.userBeanMapper.getUserLoginCount(accountId, date4, date5);
            int loginCount3 = this.userBeanMapper.getUserLoginCount(accountId, date3, date4);
            int loginCount2 = this.userBeanMapper.getUserLoginCount(accountId, date2, date3);
            int loginCount1 = this.userBeanMapper.getUserLoginCount(accountId, date1, date2);
            if(loginCount1 == 0 && loginCount2 == 0 && loginCount3 == 0 && loginCount4 == 0){
                activeType = 1;
            }
            else if(loginCount1 >= 1 && loginCount2 >= 1 && loginCount3 >= 1 && loginCount4 >= 1){
                activeType = 4;
            }
            else {
                activeType = 3;
            }
        }
        //更新状态
        userBeanMapper.updateActiveStatus(accountId, activeType);
    }

    @Override
    public boolean updateFreeTimeProid(long accountId,String freeTimeProid, int isShield){
    	userBeanMapper.updateFreeTimeSetByAccountId(accountId, freeTimeProid, isShield);
    	return true;
    }

	@Override
	public List<UserBean> getCompanyLedgerDataByAccountId(long accountId) {
		return userBeanMapper.getCompanyLedgerDataByAccountId(accountId);
	}
    
    @Override
	public UserBean getCompanyLedgerDataByLedgerId(long ledgerId) {
        List<UserBean> uBean = userBeanMapper.getCompanyLedgerDataByLedgerId(ledgerId);
        if(uBean.size() <= 0){
            uBean = userBeanMapper.getParentCompanyLedgerDataByLedgerId(ledgerId);
        }
		return uBean.get(0);
	}

	@Override
	public List<Long> getAccountIds() {
		return userBeanMapper.getAllAccountIds();
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.UserService#getGroupNameByAccount(java.lang.Long)
	 */
	@Override
	public List<String> getGroupNameByAccount(Long accountId) {
		return userBeanMapper.getGroupNameByAccount(accountId);
	}

	@Override
	public UserBean getssoLoginUser(String partnerId) {
		if(CommonMethod.isNotEmpty(partnerId)){
			return userBeanMapper.getssoLoginUser(partnerId);
		}
		return null;
	}

	@Override
	public Integer updateTodayTimes(Integer todayTimes,Long accountId) {
		if (CommonMethod.isNotEmpty(todayTimes)) {
			return userBeanMapper.updateTodayTimes(todayTimes,accountId);
		}
		return null;
	}

	@Override
	public Integer updateLockTime(Date lockTime,Long accountId) {
		return userBeanMapper.updateLockTime(lockTime,accountId);
	}

	@Override
	public Integer updatePhoneNum(Long accountId,String phoneNum) {
		return userBeanMapper.updatePhoneNum(accountId, phoneNum);
	}


}
