package com.linyang.energy.mapping.industryStandard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


/** 
* @Description
* @author Jijialu
* @date 2017年4月19日 下午6:52:43 
*/

public interface IndustryStandardMapper {
	
	/**
	 * 获得行业指标列表
	 * @return
	 */
	List<Map<String, Object>> getIndustryStandardIdList();
	
	/**
	 * 通过id获取行业指标列表
	 * @return
	 */
	List<Map<String, Object>> getIndustryStandardPageList(Map<String, Object> params);
	
	/**
	 * 根据id获取行业指标信息
	 * @param standardId
	 * @return
	 */
	Map<String, Object> getIndustryStandardById(@Param("standardId")Long standardId);
	
	/**
	 * 先检查指标名是否重复
	 * @param standardName
	 * @return
	 */
	Integer getStandardNum(@Param("standardName")String standardName);
	
	/**
	 * 插入t_industry_standard
	 * @param standardId
	 * @param standardName
	 * @param standardData
	 * @param unitType
	 * @param aboveStandardData
	 * @param belowStandardData
	 */
	void insertStandard(@Param("standardId")Long standardId, @Param("standardName")String standardName, @Param("standardData")Double standardData, @Param("unitType")String unitType, @Param("aboveStandardData")Double aboveStandardData, @Param("belowStandardData")Double belowStandardData);

	/**
	 * 更新t_industry_standard
	 * @param standardName
	 * @param standardData
	 * @param unitType
	 * @param aboveStandardData
	 * @param belowStandardData
	 */
	void updateStandard(@Param("standardId")Long standardId, @Param("standardName")String standardName, @Param("standardData")Double standardData, @Param("unitType")String unitType, @Param("aboveStandardData")Double aboveStandardData, @Param("belowStandardData")Double belowStandardData);

	/**
	 * 删除t_industry_standard
	 * @param standardId
	 */
	void deleteStandard(@Param("standardId")Long standardId);
	
	/**
	 * 获得企业指标id列表
	 * @return
	 */
	List<Map<String, Object>> getLedgerStandardIdList(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 先检查企业指标是否存在
	 * @param ledgerId
	 * @param standardId
	 * @return
	 */
	Integer getLedgerStandardNum(@Param("ledgerId")Long ledgerId, @Param("standardId")Long standardId);
	
	/**
	 * 插入t_ledger_standard
	 * @param ledgerId
	 * @param standardId
	 * @param ledgerData
	 * @param unitConsumption
	 * @param standardDiff
	 * @param startTime
	 * @param endTime
	 */
	void insertLedgerStandard(@Param("ledgerId")Long ledgerId, @Param("standardId")Long standardId, @Param("ledgerData")Double ledgerData, @Param("unitConsumption")Double unitConsumption, @Param("standardDiff")Double standardDiff, @Param("startTime")Date startTime, @Param("endTime")Date endTime);
	
	/**
	 * 更新t_ledger_standard
	 * @param ledgerData
	 * @param unitConsumption
	 * @param standardDiff
	 * @param startTime
	 * @param endTime
	 */
	void updateLedgerStandard(@Param("ledgerId")Long ledgerId, @Param("standardId")Long standardId, @Param("ledgerData")Double ledgerData, @Param("unitConsumption")Double unitConsumption, @Param("standardDiff")Double standardDiff, @Param("startTime")Date startTime, @Param("endTime")Date endTime);

	/**
	 * 获取企业指标信息
	 * @param ledgerId
	 * @param standardId
	 * @return
	 */
	Map<String, Object> getLedgerStandardInfo(@Param("ledgerId")Long ledgerId, @Param("standardId")Long standardId);

	/**
	 * 根据id获取某指标的企业总数
	 * @param standardId
	 * @return
	 */
	Integer getStandardSumById(@Param("standardId")Long standardId);
	
	/**
	 * 获取企业某指标击败的用户数
	 * @param standardId
	 * @param unitConsumption
	 * @return
	 */
	Integer getStandardDefeat(@Param("standardId")Long standardId, @Param("unitConsumption")Double unitConsumption);
	
	/**
	 * 查询分户日电量之和
	 * @param ledgerId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Double queryLedgerQSum(@Param("ledgerId")Long ledgerId, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);
}
