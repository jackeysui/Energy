package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;

/**
 * 需量申报
 * @author guosen
 * @date 2014-12-22
 */
public interface DemandDeclareService {

	/**
	 * 更新申报值
	 * @param recId
	 * @param declareValue
	 * @return
	 */
	boolean update(Long recId, Double declareValue);
	
	/**
	 * 保存
	 * @param param
	 * @return
	 */
	Map<String, Object> insert(Map<String, Object> param);

	/**
	 * 得到申报记录列表
	 * @param page
	 * @param mapQuery
	 * @return
	 */
	List<Map<String, Object>> getDecalrePageData(Page page, Map<String, Object> mapQuery);

	/**
	 * 删除
	 * @param recId
	 * @return
	 */
	boolean delete(Date declareTime);

	/**
	 * 获取emo申报列表数据
	 * @param ledgerId
	 * @return
	 */
	Map<String, Object> getEmoDecalreData(long ledgerId);

	/**
	 * 保存
	 * @param paramList
	 * @return	T_LEDGER_DECLARE
	 */
	Map<String, Object> insertDemandDeclare(List<Map<String, Object>> paramList);

    public Integer getEmoDecalreType(Map<String, Object> map);
    
    /**
     * 获取采集点数量
     * @param ledgerId
     * @return
     */
    Integer getPointNum(long ledgerId);
    
    
    /**
     * 根据ledgerid查询meterid
     * @param ledgerId
     * @return
     */
    Long getMeterIdByLedgerId(long ledgerId);
    
    /**
     * 根据meterId查询上月(根据数据库服务器时间)日冻结最大值
     * @param meterId
     * @return
     */
    Integer getmaxFaqValue(long meterId);
    
    /**
     * 根据时间和id查询企业申报值
     * @param page
     * @param mapQuery
     * @return
     */
	Map<String, Object> getEmoDecalreByIdAndBeginTime(Map<String, Object> map);
    
	
	
	/**
	 * 获取上个月的需量记录,如果没有就查询最后一次申报记录
	 * @param map
	 * @return
	 */
	Map<String, Object> getLsatDeclare(Long pointId,Date beginTime,Integer flag);


	/**
	 * 获取每个月最后一条申报记录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getEveryLsatDeclare(Map<String, Object> map);


}
