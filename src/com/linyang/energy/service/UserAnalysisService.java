package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

/**
 * 
 * @author guosen
 *
 */
public interface UserAnalysisService {
	
	/**
	 * 保存用户足迹
	 * @param accountId
	 * @param operItemId
	 * @param moduleId
	 * @param operClient
	 */
	public void addAccountTrace(Long accountId, Long operItemId, Long moduleId, int operClient);
	
	/**
	 * 保存用户登录信息
	 * @param osVersion 
	 * @param type 
	 * @param date 
	 * @param accountId 
	 */
	public void addAccountLogin(Long accountId, Date date, int type, String osVersion);

	/**
	 * 功能点击率分析
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> clickRateAnalysis(Map<String, Object> queryMap);

	/**
	 * 用户活跃度分析
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> clickActivity(Map<String, Object> queryMap);

	/**
	 * 组装excel所需要的数据
	 * @param param
	 * @return
	 */
	public Map<String, Object> buildExcelData(Map<String, Object> param);

	/**
	 * 导出excel
	 * @param outputStream
	 * @param result
	 * @param param
	 */
	public void optExcel(ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param);
	
	/**
	 * 计算用户等级
	 */
	public void calAccountLevel();

    /**
     *  电工首页 常用功能
     */
    public List<Map<String, Object>> getUsuallyUseModel(Long accountId);

}
