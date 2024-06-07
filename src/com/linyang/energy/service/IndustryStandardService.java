package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/** 
* @Description
* @author Jijialu
* @date 2017年4月19日 下午6:35:58 
*/

public interface IndustryStandardService {
	
	/**
	 * 获得行业指标列表
	 * @return
	 */
	List<Map<String, Object>> getIndustryStandardIdList();
	/**
	 * 根据id获得行业指标列表
	 * @return
	 */
	List<Map<String, Object>> getIndustryStandardPageList(Map<String, Object> params);
	
	/**
	 * 根据id获得行业指标信息
	 * @param standardId
	 * @return
	 */
	Map<String, Object> getIndustryStandardById(Long standardId);
	
	/**
	 * 新增行业指标
	 * @param standardInfos
	 * @return
	 */
	Map<String, Object> insertStandard(Map<String, Object> standardInfo);
	
	/**
	 * 修改行业指标
	 * @param standardInfos
	 */
	void updateStandard(Map<String, Object> standardInfo);
	
	/**
	 * 删除行业指标
	 * @param standardId
	 */
	void deleteStandard(Long standardId);
	
	/**
	 * 获得企业指标id列表
	 * @return
	 */
	List<Map<String, Object>> getLedgerStandardIdList(Long ledgerId);
	
	/**
	 * 新增或修改企业指标
	 * @param standardInfos
	 * @return
	 */
	void insertOrUpdateLedgerStandard(Map<String, Object> standardInfo);
	
	/**
	 * 获取企业指标信息
	 * @param ledgerId
	 * @param standardId
	 * @return
	 */
	Map<String, Object> getLedgerStandardInfo(Long ledgerId, Long standardId);
	
	/**
	 * 查询分户日电量之和
	 * @param ledgerId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Double queryLedgerQSum(Long ledgerId, Date beginDate, Date endDate);
}
