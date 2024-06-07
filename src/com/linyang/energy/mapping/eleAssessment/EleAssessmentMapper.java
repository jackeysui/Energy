package com.linyang.energy.mapping.eleAssessment;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 15-10-13.
 */
public interface EleAssessmentMapper {

    /**
     * 查询分户下的所有测量点
     * */
    public List<Long> getMeterIdsByLedgerId(@Param("ledgerId")Long ledgerId);

    /**
     * 查询分户下的"总加组"测量点
     * */
    public List<Long> getTmeterByLedgerId(@Param("ledgerId")Long ledgerId);

    /**
     * 获取指定时间段内，某测量点 功率曲线的最大值
     * */
    public Double getMaxPowerByMeter(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 电流曲线的最大值
     * */
    public Double getMaxIbyMeter(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 电压曲线的最大值
     * */
    public Double getMaxVbyMeter(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 电压曲线的最小值
     * */
    public Double getMinVbyMeter(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 电压不平衡度的最大值
     * */
    public Double getVolBalance(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 电流不平衡度的最大值
     * */
    public Double getIBalance(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 负载率的最大值
     * */
    public Double getMaxLoad(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 最小功率因数
     * */
    public Double getMinPf(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 功率因数点数
     * */
    public Integer getPfTotalNum(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 功率因数越限的点数
     * */
    public Integer getPfExceedNum(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime, @Param("stdPF")Double stdPF);

    /**
     * 获取指定时间段内，某测量点 电压不平衡度的日均值
     * */
    public Double getVBalanAvg(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 电压不平衡度的日均时间
     * */
    public Long getVBalanTimeAvg(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取指定时间段内，某测量点 电流不平衡度的日均值
     * */
    public Double getIBalanAvg(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     *  登陆次数 (flag=1表示Pc端，flag=2表示移动端)
     * */
    public Integer getLoginNum(@Param("accountId")Long accountId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime, @Param("flag")Integer flag);

    /**
     *  功能点击次数
     * */
    public Integer getUserClickNum(@Param("accountId")Long accountId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     *  用户点击业务数
     * */
    public Integer getCoverOperNum(@Param("accountId")Long accountId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     *  总业务数
     * */
    public Integer getTotalOperNum(@Param("accountId")Long accountId);

	/**
	 * 计算分户一段时间的分费率电量
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getTotalFeeEle(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	/**
	 * 分户月正向有功总电量
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getLedgerQ(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 测量点月正向有功总电量
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getMeterQ(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

	/**
	 * 分户月正向无功总电量
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getLedgerRQ(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 测量点月正向无功总电量
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getMeterRQ(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

	/**
	 * 测量点最小无功功率
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getMeterMinRP(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	/**
	 * 测量点无功倒送时间
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Integer getMeterMinRPTime(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	/**
	 * 测量点负载率均值
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getMeterAvgLoad(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取某级线损电量
	 * 
	 * @param ledgerId
	 * @param level
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getLedgerLinelossCoul(@Param("ledgerId") Long ledgerId, @Param("level") Integer level,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 取分户标准功率因数
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Double getThresholdValue(@Param("ledgerId") Long ledgerId);
	
	/**
	 * 取采集点额定功率
	 * 
	 * @param meterId
	 * @return
	 */
	public Double getThresholdPwrValue(@Param("meterId") Long meterId);
	
	/**
	 * 取分户下所有变压器测量点
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<Long> getTFMeterIdsByLedgerId(@Param("ledgerId") Long ledgerId);
	
	/**
	 * 取电表越电压上下限的点数
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Integer getMeterOverVol(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取电表曲线电压点数
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Integer getMeterVolCount(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取打分小项权重
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getScoreItemWeight();
	
	/**
	 * 保存分数
	 * 
	 * @param queryMap
	 */
	public void saveAccountScore(Map<String, Object> queryMap);
	
	/**
	 * 取测量点电压总谐波畸变率最大值
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getMeterMaxDisV(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取测量点各奇次谐波电压含有率最大值
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getMeterMaxHarV(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取测量点各奇次谐波电流含有率最大值
	 * 
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getMeterMaxHarI(@Param("meterId") Long meterId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取测量点电压等级
	 * 
	 * @param meterId
	 * @return
	 */
	public Integer getMeterVolLevel(@Param("meterId") Long meterId);
}
