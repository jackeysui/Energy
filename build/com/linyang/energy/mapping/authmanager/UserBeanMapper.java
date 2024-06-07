package com.linyang.energy.mapping.authmanager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.UserBean;

public interface UserBeanMapper {
    int deleteByPrimaryKey(Long accountId);

    /**
     * 重复无使用,已废弃,请使用/维护insertSelective<br>
     * edit by cehngq 20151212
     */
    @Deprecated
    int insert(UserBean record);
   /**
    * 函数功能说明  :插入用户表
    * @param record
    * @return  int
    */
    int insertSelective(UserBean record);
    /**
     * 函数功能说明  :插入用户角色表
     * @param record
     * @return  int
     */
    int addUserRoleData(UserBean record);

    UserBean selectByPrimaryKey(Long accountId);
    /**
     * 函数功能说明  :更新用户表
     * @param record
     * @return  int
     */
    int updateByPrimaryKeySelective(UserBean record);
    /**
     * 函数功能说明  :更新用户角色关系表
     * @return  int
     */
    int updateUserRoleData(UserBean record);

    /**
     * 重复无使用,已废弃,请使用/维护updateByPrimaryKeySelective<br>
     * edit by cehngq 20151212
     */
    @Deprecated
    int updateByPrimaryKey(UserBean record);
    
    /**
     * 更新用户最后一次登录时间、次数
     * @param userbean
     * @return
     */
    int updateLastDate(UserBean userbean);
    /**
     * 更新用户状态
     * @param userIds 逗号隔开的用户id
     * @param status
     * @return
     */
    int updateUserStatus(@Param("userIds")List<Long> userIds,@Param("status")int status,@Param("nowDate") Date nowDate);	//原int updateUserStatus(@Param("userIds")String userIds,@Param("status")int status,@Param("nowDate") Date nowDate);
    /**
     * 根据用户登录名称得到用户信息
     * @param username用户登录名称
     * @return
     */
    UserBean getUserByUserName(@Param("username")String username);
    /**
     * 根据用户ID得到用户信息
     * @param accountId 用户登录名称
     * @return
     */
    UserBean getUserByAccountId(@Param("accountId")long accountId);
    
    int getUserCount(@Param("userId")long userId,@Param("password") String password);
    /**
     * 函数功能说明  :分页查询用户的列表
     * @param queryCondition
     * @return   List<Map<String,Object>>   
     * @throws
     */
    List<Map<String,Object>> getUserPageData(Map<String,Object> queryCondition);
    
    /**
     * 函数功能说明  :自定义信息页分页查询用户的列表
     * @param queryCondition
     * @return   List<Map<String,Object>>   
     * @throws
     */
    List<Map<String, Object>> getMessageUserPageData(Map<String,Object> queryCondition);
    
   /**
    * 函数功能说明  :检查用户名称是否重复
    * @param loginName
    * @return  int     
    * @throws
    */
    int  checkLoginName(@Param("loginName")String loginName,@Param("operType")int operType,@Param("accountId")Long accountId);
    /**
     * 函数功能说明  :查询角色下来框
     * @return  List<UserBean>
     */
    List<UserBean> getRoleList();
    /**
     * 函数功能说明  :查询分户下来框
     * @return  List<UserBean>
     */
    List<UserBean> getLedgerList(@Param("ledgerId")long ledgerId);
    /**
     * 查询分户下拉框id
     * @param ledgerId
     * @return
     */
    List<Long> getLedgerIdList(@Param("ledgerId")long ledgerId);
    /**
     * 函数功能说明  :查询用户下来框
     * @return  List<UserBean>
     */
    List<UserBean> getUserList();

	List<UserBean> getLedgerData();

	/**
	 * 得到分户下的用户
	 * @param ledgerId
	 * @return
	 */
	List<Long> getAccountIds(Long ledgerId);

    /**
     * 获得用户一段时间内的登陆次数
     * */
    int getUserLoginCount(@Param("accountId")long accountId,@Param("beginTime") Date beginTime,@Param("endTime") Date endTime);

    /**
     * 更新用户免扰时间
     * @param accountId
     * @param freeTimeProid
     * @param isShield 是否启用消息屏蔽(0否  1是)
     * @return
     */
    int updateFreeTimeSetByAccountId(@Param("accountId")long accountId,@Param("freeTimeProid")String freeTimeProid,@Param("isShield")int isShield);

    /**
	 * 获取企业分户下拉框
	 * @return  List<UserBean>
	 */
	List<UserBean> getCompanyLedgerDataByAccountId(@Param("accountId")long accountId);
    
    List<UserBean> getCompanyLedgerDataByLedgerId(@Param("ledgerId")long ledgerId);
    
    List<UserBean> getParentCompanyLedgerDataByLedgerId(@Param("ledgerId")long ledgerId);

    List<UserBean> getCanBookUsers(@Param("ledgerId")long ledgerId, @Param("accountId")long accountId);
    
    List<Long> getAllAccountIds();
    
    int updateActiveStatus(@Param("accountId")long accountId, @Param("activeType")int activeType);
    
    /**
     * 根据account配置获取群组名
     * @param accountId
     * @return
     */
    List<String> getGroupNameByAccount(@Param("accountId")Long accountId);
    
    /**
     * 获取单点登录用户
     * @param username用户登录名称
     * @return
     */
    UserBean getssoLoginUser(@Param("partnerId")String partnerId);
    
    /**
     * 检查用户名和真实名重复的，防止重放
     * */
    List<UserBean> getFilteredUser(Map<String, Object> queryMap);

    
    /**
	 * 修改当前当天登陆次数
	 * @param todayTimes
	 * @param nowTime
	 */
	Integer updateTodayTimes(@Param("todayTimes")Integer  todayTimes,@Param("accountId")Long accountId);
	
	/**
	 * 修改被禁止登陆的时间
	 * @param lockTime
	 * @return
	 */
	 Integer updateLockTime(@Param("lockTime")Date lockTime,@Param("accountId")Long accountId);
	
	
	 /**
	  * 修改用户手机号码
	  * @param userBean
	  * @return
	  */
	public Integer updatePhoneNum(@Param("accountId")Long accountId,@Param("phoneNum")String phoneNum);

}