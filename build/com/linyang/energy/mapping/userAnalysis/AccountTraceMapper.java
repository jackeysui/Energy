package com.linyang.energy.mapping.userAnalysis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.AccountTraceBean;

/**
 * 用户足迹
 * @author guosen
 *
 */
public interface AccountTraceMapper {

	/**
	 * 保存用户足迹
	 * @param trace
	 */
	public void addAccountTrace(AccountTraceBean trace);

	/**
	 * 功能点击率分析
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> clickRateAnalysis(
			Map<String, Object> queryMap);

	/**
	 * 用户活跃度分析
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> clickActivity(Map<String, Object> queryMap);
	
	/**
	 * 获取用户登录次数
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getAccountLoginNum(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 获取用户点击次数
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getAccountTraceNum(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 更新用户等级
	 * 
	 * @param accountId
	 * @param num
	 */
	public void updateAccountLevel(@Param("accountId") long accountId, @Param("num") int num);
	
	public List<Long> getAllLedgerId(Map<String, Object> queryMap);

    /**
     * 获取用户常用功能（按使用倒序）
     *
     */
    public List<Map<String, Object>> getMostAccountTrace(@Param("accountId") Long accountId);

}
