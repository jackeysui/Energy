package com.linyang.energy.mapping.authmanager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.LedgerRelationBean;
import com.linyang.energy.model.LineLossTreeBean;
import com.linyang.energy.model.MeterBean;

public interface MeterBeanMapper {
    int deleteByPrimaryKey(Long meterId);

    int insert(MeterBean record);

    int insertSelective(MeterBean record);

    MeterBean selectByPrimaryKey(Long meterId);

    int updateByPrimaryKeySelective(MeterBean record);

    int updateByPrimaryKey(MeterBean record);

    /**
     * 查询一个分户地下计量点的个数
     * @param ledgerId 分户Id
     * @return
     */
    int countMeterByLedgerId(@Param("ledgerId")long ledgerId);
    
    /**
     * 查询一个计量点下子计量点的个数
     * @param meterId 计量点Id
     * @return
     */
    int countSubMeterByMeterId(@Param("meterId")long meterId);
    
	/**
	 * 获取线损树
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<LineLossTreeBean> getLineLossTreeData(@Param("ledgerId") long ledgerId);
	
	/**
	 * 删除线损配置关系
	 * 
	 * @param ledgerId
	 */
	public void deleteLinelossRelation(@Param("ledgerId") long ledgerId);

	/**
	 * 保存线损配置关系
	 * 
	 * @param line
	 */
	public void saveLineLossRelation(LineLossTreeBean line);
	
	/**
	 * 得到未配置的DCP
	 * @param ledgerId
	 * @return
	 */
	public List<LineLossTreeBean> getUnSetDCP(@Param("ledgerId") long ledgerId);

	/**
	 * 得到已经配置的DCP
	 * @param ledgerId
	 * @return
	 */
	List<LineLossTreeBean> getSetDCP(@Param("ledgerId") long ledgerId,@Param( "meterName" )String meterName);

	/**
	 * 得到EMO管理树
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getEMOTree(@Param("ledgerId")long ledgerId,@Param( "meterName" )String meterName);

	/**
	 * 得到EMO总加组模型配置
	 * @param ledgerId
	 * @return
	 */
	List<LedgerRelationBean> getEMOModel1(@Param("ledgerId")long ledgerId);
	
	/**
	 * 得到EMO表计模型配置
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getEMOModel2(@Param("ledgerId")long ledgerId);

	/**
	 * 删除T_LEDGER_RELATION
	 * @param ledgerId
	 */
	void deleteLedgerRelation(@Param("ledgerId")Long ledgerId);

	/**
	 * 删除T_LEDGER_SHOW
	 * @param ledgerId
	 */
	void deleteLedgerShow(@Param("ledgerId")Long ledgerId);

	/**
	 * 新增T_LEDGER_RELATION
	 * @param map
	 */
	void addLedgerRelation(Map<String, Object> map);

	/**
	 * 新增T_LEDGER_SHOW
	 * @param map
	 */
	void addLedgerShow(Map<String, Object> map);
	/**
	 * 根据id得到未配置的DCP
	 * @param ledgerId
	 * @return
	 */
	List<LineLossTreeBean> getUnSetDCPByName(@Param( "meterName" ) String meterName,@Param( "ledgerId" )Long ledgerId);
}