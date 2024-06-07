package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.UserBean;


/**
 * 用户管理业务逻辑层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 3, 2013
 */

public interface UserService {
	
	/**
	 * 根据用户登录名得到用户信息
	 * @param username 登录用户名
	 * @return
	 */
	public UserBean getUserByUserName(String username);
	/**
	 * 根据用户ID得到用户信息
	 * @param accountId 用户的Id
	 * @return
	 */
	public UserBean getUserByAccountId(Long accountId);
	
	/**
	 * 添加用户信息
	 * @param userBean 
	 * @return
	 */
	public boolean addUserInfo(UserBean userBean);
	
	/**
	 * 验证原密码是否正确
	 * @param userId 用户id
	 * @return
	 */
	public boolean isPasswordCorrect(long userId,String password);
	
	/**
	 *修改用户信息（可以支持修改密码)
	 * @param userBean 用户信息Bean
	 * @return
	 */
	public boolean updateUserInfo(UserBean loginUser,UserBean userBean);
	
	/**
	 *修改用户登陆日期
	 * @param userBean 用户信息Bean
	 * @return
	 */
	public boolean updateLastDate(UserBean userBean);
	
	/**
	 * 设置用户信息状态，支持删除、停用、启用操作
	 * @param userIds 用户id集合
	 * @param status  状态
	 * @return
	 */
	public boolean updateUserStatus(List<Long> userIds,int status);
	
	/**
	 * 分页查询用户信息列表，支持查询
	 * @param page 分页对象
	 * @param queryCondition 查询条件
	 * @return  List<Map<String,Object>> 
	 */
	public  List<Map<String,Object>> getUserPageList(Page page,Map<String,Object> queryCondition);
	/**
	 * 函数功能说明  :检查用户的名称是否重复
	 * @param loginName
	 * @return  boolean     
	 */
	public boolean checkLoginName(String loginName,int operType,Long accountId);
	/**
	 * 函数功能说明  :获取角色下来框
	 * @return  List<UserBean>
	 */
	public List<UserBean> getRoleList();
	/**
	 * 函数功能说明  :获取分户下来框
	 * @return  List<UserBean>
	 */
	public List<UserBean> getLedgerList(long ledgerId);
	
	/**
	 * 获取分户下来框id
	 * @param ledgerId
	 * @return
	 */
	public List<Long> getLedgerIdList(long ledgerId);
	/**
	 * 函数功能说明  :获取用户下来框
	 * @return  List<UserBean>
	 */
	public List<UserBean> getUserList();
	/**
	 * 函数功能说明  :获取分户下来框
	 * @return  List<UserBean>
	 */
	public List<UserBean> getLedgerData();
	
	/**
	 * 判断是否设置了大屏显示
	 * @param accountId
	 * @return
	 */
	public boolean checkFullScreen(Long ledgerId);

    /**
     * 更新用户的活跃状态
     */
    public void updateActiveStatus(Long accountId);
	
    /**
     * 更新用户免扰时段设置
     * @param accountId
     * @param freeTimeProid
     * @param isShield 是否启用消息屏蔽(0否  1是)
     * @return
     */
    public boolean updateFreeTimeProid(long accountId,String freeTimeProid,int isShield);
    
    /**
	 * 获取企业分户下拉框
	 * @param accountId
	 * @return  List<UserBean>
	 */
	public List<UserBean> getCompanyLedgerDataByAccountId(long accountId);
    
    public UserBean getCompanyLedgerDataByLedgerId(long ledgerId);

    public List<UserBean> getCanBookUsers(long ledgerId, long accountId);
    
    /**
     * 获取所有accountId
     * @return
     */
    public List<Long> getAccountIds();
    
    /**
     * 根据account配置获取群组名
     * @param accountId
     * @return
     */
    public List<String> getGroupNameByAccount(Long accountId);
    
    /**
     * 获取单点登录用户
     * @param partnerId
     * @return
     */
	public UserBean getssoLoginUser(String partnerId);
	
	/**
	 * 修改当前当天登陆次数
	 * @param todayTimes
	 * @param nowTime
	 */
	public Integer updateTodayTimes(Integer todayTimes, Long accountId);
	
	/**
	 * 修改被禁止登陆的时间
	 * @param date
	 * @return
	 */
	 public Integer updateLockTime(Date date, Long accountId);
	 
	 
	 
	 /**
	  * 修改用户手机号码
	  * @param userBean
	  * @return
	  */
	public Integer updatePhoneNum(Long accountId,String phoneNum);
	 
	 
	 
	 
	 
	 
	 
	 
	
}
