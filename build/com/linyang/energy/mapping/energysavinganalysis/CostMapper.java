package com.linyang.energy.mapping.energysavinganalysis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.RateFeeBean;
import com.linyang.energy.model.RateSectorBean;

/**
 * 电费数据访问层接口
 * 
 * @description:
 * @author:gaofeng
 * @date:014.12.18
 */
public interface CostMapper {
	/**
	 * 取测量点对应的费率信息
	 * 
	 * @param pointId
	 * @return
	 */
	public List<RateSectorBean> getPointRateInfo(long pointId);

	/**
	 * 查询测量点日电量
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getEleValue(Map<String, Object> map);

	/**
	 * 得到电表的标准功率因数
	 * 
	 * @param pointId
	 * @return
	 */
	public Double getThresholdValue(@Param("pointId") long pointId);

    public Double getEmoThresholdValue(@Param("ledgerId") long ledgerId);

	/**
	 * 查询功率因数对照表
	 * 
	 * @param pf
	 * @param rate
	 * @return
	 */
	public Double getFactor(@Param("pf") double pf, @Param("rate") double rate);
	
	/**
	 * 获取电表基本电费计算相关信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMeterBasicFeeInfo(Map<String, Object> map);
	/**
	 * 获取emo电表基本电费计算相关信息
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getLedgerBasicFeeInfo(Map<String, Object> map);
	
	/**
	 * 查询测量点月总电量
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMonthTotalEleValue(Map<String, Object> map);

    /**
     * 查询分户月总电量
     * @return
     */
    public Map<String, Object> getLedgerMonthTotalEleValue(@Param("ledgerId") long ledgerId, @Param("analyType") int analyType, @Param("sTime") Date sTime,@Param("eTime") Date eTime);
	
	/**
	 * 查询测量点分费率月电量、电费
	 * 
	 * @param map
	 * @return
	 */
	public List<RateFeeBean> getMonthRateEleValue(Map<String, Object> map);

    /**
     * 查询分户分费率月电量、电费
     *
     * @param map
     * @return
     */
    public List<RateFeeBean> getEmoMonthRateEleValue(Map<String, Object> map);
    public List<RateFeeBean> getEmoMonthRateEleValue2(Map<String, Object> map);

    /**
     * 查询分户单费率月电量、电费
     *
     * @param map
     * @return
     */
    public RateFeeBean getEmoMonthSingleEleValue(Map<String, Object> map);
    
    /**
     * 查询分户单费率月电量、电费(从日视图取数据)
     *
     * @param map
     * @return
     */
    public RateFeeBean getEmoMonthSingleEleValueNew(Map<String, Object> map);

    /**
     * 查询分户容量电费
     *
     * @param map
     * @return
     */
    public RateFeeBean getEmoCapacityFee(Map<String, Object> map);
	
	/**
	 * 取月最大需量
	 * 
	 * @param map
	 * @return
	 */
	public Double getMaxDemandValue(Map<String, Object> map);
	
	/**
	 * 取容量需量电价
	 * 
	 * @param rateId
	 * @return
	 */
	public Map<String, Object> getBasicFeePrice(@Param("rateId") long rateId);

    /**
     * 取分户容量需量电价
     *
     * @param ledgerId
     * @return
     */
    public Map<String, Object> getLedgerFeePrice(@Param("ledgerId") long ledgerId);

	/**
	 * 取计算基本电费相关信息
	 * 
	 * @param pointId
	 * @return
	 */
	public Map<String, Object> getBasicFeeInfo(@Param("pointId") long pointId);
	/**
	 * 取计算emo基本电费相关信息
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Map<String, Object> getLedgerIdBasicFeeInfo(@Param("ledgerId") long ledgerId);
	
	/**
	 * 按月取最大需量
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMonthMaxDemand(Map<String, Object> map);

    /**
     * 近12个月,月最大需量的MAX_FAD_TIME的list（按小时分钟排序）
     * @return
     */
    public List<Map<String, Object>> monthMaxFadTime(Map<String, Object> map);
    
    /**
     * 近12个月,emo月最大需量的MAX_FAD_TIME的list（按小时分钟排序）
     * @return
     */
    public List<Map<String, Object>> getLedgerMonthMaxFadTime(Map<String, Object> map);

    /**
     * 按月取分户计算模型中主变压器的最大需量
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> getLedgerMainDemand(Map<String, Object> map);
	/**
	 * 获取分户下的测量点
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<Long> getLedgerMeters(@Param("ledgerId") long ledgerId);

    /**
     * 获取当前申报类型
     * @param pointId
     * @return
     */
    public List<Integer> getCurrentDeclareType(@Param("pointId") long pointId, @Param("nowStr") String nowStr);
    
    /**
     * 获取当前申报类型
     * @param ledgerId
     * @return
     */
    public List<Integer> getCurrentLedgerDeclareType(@Param("ledgerId") long ledgerId, @Param("nowStr") String nowStr);
    
    /**
	 * 分户月正向有功总电量
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getLedgerMonQEnd(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 分户月正向无功总电量
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getLedgerMonRQEnd(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

    /**
     * 得到EMO计算所需的DCP
     *
     * @param ledgerId
     * @return
     */
    public List<Map<String, Object>> getComputeMeter(@Param("ledgerId") long ledgerId, @Param("analyType") int analyType);

	public Double getMeterMonQEnd(@Param("objectId") long objectId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	public Double getMeterMonRQEnd(@Param("objectId") long objectId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	/**
	 * 获取企业分户下配置的计量点
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<Long> getAllottedPointIdsByLedgerId(@Param("ledgerId")Long ledgerId);

    /**
     * 得到EMO计算所需的DCP类型
     *
     * @param ledgerId
     * @return
     */
    public List<Short> getComputeMeterTypes(@Param("ledgerId") long ledgerId, @Param("analyType") int analyType);

    /**
     * 得到各类型计量点或分户月数据
     *
     */
    public List<Map<String, Object>> getMonthEleValue(Map<String, Object> queryMap);
	
	/**
	 * 查询分户容量电费(容需对比)
	 *
	 * @param map
	 * @return
	 */
	public RateFeeBean getEmoCapacityFee_new(Map<String, Object> map);

}
