package com.linyang.energy.mapping.energyanalysis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.AssembleaBean;
import com.linyang.energy.model.FactorBean;
import com.linyang.energy.model.LineLossBean;
import com.linyang.energy.model.PowerFactorStatBean;

/**
 * 班次能耗分析数据库访问层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 21, 2014
 */
public interface SchedulingMapper {
	
	/**
	 * 得到所有流水线的信息
	 * @return
	 */
	public List<AssembleaBean> getAssembleInfo();
	
	/**
	 * 得到排班详细信息
	 * @param scheduleId
	 * @return
	 */
	public List<Map<String,Object>> getSchedulerDetail(@Param("scheduleId")long scheduleId);
	
	/**
	 *  班次用能
	 * @param queryMap
	 * @return
	 */
	public List<Map<String,Object>> getEnergyUseDatas(Map<String,Object> queryMap);
	
	/**
	 * 班次产品
	 * @param queryMap
	 * @return
	 */
	public List<Map<String,Object>> getProductsDatas(Map<String,Object> queryMap);
	
	/**
	 * 得到一个流水线的计量点Id
	 * @param assembleId 
	 * @return
	 */
	public Long getAssembleMeterId(@Param("assembleId")long assembleId);
	/**
	 * 得到费率电价信息
	 * @param meterId
	 * @return
	 */
	public List<Map<String,Object>> getRateInfo(@Param("meterId")long meterId);
	
	/**
	 * 得到电曲线数据
	 * @param queryMap
	 * @return
	 */
	public List<Map<String,Object>> getQStat(Map<String,Object> queryMap);
	
	public List<PowerFactorStatBean> getPowerFactorStat(Map<String,Object> queryMap);
	
	public List<PowerFactorStatBean> getPowerFactorStat2(Map<String,Object> queryMap);
	/**
	 * 得到一个分户的标准功率因数
	 * @param ledgerId
	 * @return
	 */
	public Double getThresholdValue(@Param("ledgerId")long ledgerId);
	
	public Double getFactor(@Param("pf")double pf,@Param("rate")double rate);
	
	
	public FactorBean getFactorQ(Map<String,Object> queryMap);
	
	public Double getLastMonthCost(@Param("time")Date time,@Param("ledgerId")long ledgerId);
	
	/**
	 * 获取功率因数曲线
	 * @param queryMap
	 * @return
	 */
	public List<Map<String,Object>> getChartDatas(Map<String,Object> queryMap);
	
	/**
	 * 获取测量点的额定功率
	 * 
	 * @param pointId
	 * @return
	 */
	public Double getPointEPwr(@Param("pointId") long pointId);
	
	/**
	 * 取最大最小负载率
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> getMaxMinLoadData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取最大负载率发生时间
	 * 
	 * @param load
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Date getMaxLoadTime(@Param("load") double load, @Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取最小负载率发生时间
	 * 
	 * @param load
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Date getMinLoadTime(@Param("load") double load, @Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取平均最大负载率
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getAvgLoadMax(@Param("pointId") long pointId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 取负载率曲线
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getLoadData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime, @Param("dataSource") Integer dataSource, @Param("dayList") List<String> dayList);
	
	/**
	 * 取负载率发生时间曲线
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getLoadTimeData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime, @Param("dataSource") Integer dataSource, @Param("dayList") List<String> dayList);

    /**
     * 取负载率分布曲线
     *
     * @param pointId
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> getDistribution(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
                                                 @Param("endTime") Date endTime, @Param("dayList") List<String> dayList);
	
	/**
	 * 获取测量点的额定电压
	 * 
	 * @param pointId
	 * @return
	 */
	public Double getPointEVol(@Param("pointId") long pointId);
	
	/**
	 * 获取测量点的额定电流
	 * 
	 * @param pointId
	 * @return
	 */
	public Double getPointECur(@Param("pointId") long pointId);
	
	/**
	 * 取电流不平衡最大值
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getMaxUnI(@Param("pointId") long pointId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 取电压不平衡最大值
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getMaxUnV(@Param("pointId") long pointId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 取电流不平衡最大值发生时间
	 * 
	 * @param value
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Date getMaxUnTimeI(@Param("value") double value, @Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取电压不平衡最大值发生时间
	 * 
	 * @param value
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Date getMaxUnTimeV(@Param("value") double value, @Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取电流不平衡度越限累计时间
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Long getSumUnLimitI(@Param("pointId") long pointId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 取电压不平衡度越限累计时间
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Long getSumUnLimitV(@Param("pointId") long pointId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 取电流不平衡曲线
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getIUMaxData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取电压不平衡曲线
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getVUMaxData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取电流不平衡最大值发生时间曲线
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getIUMaxTimeData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取电压不平衡最大值发生时间曲线
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getVUMaxTimeData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取电流不平衡度越限日累计时间曲线 
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getIULimitData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取电压不平衡度越限日累计时间曲线 
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getVULimitData(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 根据电能示值计算线损的电量
	 * 
	 * @param pointIds 原是String  先修改为 List<Long>
	 * @param beginTime
	 * @param endTime
	 * @param attrId
	 * @return
	 */
	public List<LineLossBean> getDayLineLossInfo(@Param("pointIds") List<Long> pointIds, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

    public List<LineLossBean> getDayLineLossInfo_2(@Param("pointIds") List<Long> pointIds, @Param("beginTime") Date beginTime,
                                                 @Param("endTime") Date endTime,@Param("lossType")int lossType);
    public List<LineLossBean> getDayLineLossInfo_2new(@Param("pointIds") List<Long> pointIds, @Param("beginTime") Date beginTime,
                                                 @Param("endTime") Date endTime,@Param("lossType")int lossType);

	/**
	 * 根据级数获取线损表配置
	 * 
	 * @param meterLevel
	 * @return
	 */
	public List<Long> getLineLossByLevel(@Param("ledgerId") long ledgerId, @Param("meterLevel") int meterLevel);

    public List<Long> getLineLossByLevel_2(@Param("ledgerId") long ledgerId, @Param("meterLevel") int meterLevel,@Param("lossType")int lossType);

	/**
	 * 获取线损表的子表配置
	 * 
	 * @param meterId
	 * @param meterLevel
	 * @return
	 */
	public List<Long> getLineLossMeters(@Param("meterId") long meterId, @Param("meterLevel") int meterLevel);

	public List<Long> getVirtualLineLossMeters(@Param("meterId") long meterId, @Param("meterLevel") int meterLevel);

	/**
	 * 取一个线损的最大级数
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Integer getLineMaxLevel(@Param("ledgerId") long ledgerId,@Param("lossType")int lossType);
	
	/**
	 * 获取分户下总表中“电压日不平衡度最大值”最大的电表列表
	 * @param pointId 分户ID
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<Long> getMaxVuMaxListOfSP(@Param("ledgerId") long ledgerId,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 获取分户下所有表中“电压日不平衡度最大值”最大的电表列表
	 * @param pointId 分户ID
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public List<Long> getMaxVuMaxListOfAP(@Param("ledgerId") long ledgerId,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 获取分户下总表中“电流日不平衡度最大值”最大的电表列表
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	*/
	public List<Long> getMaxIuMaxListOfSP(@Param("ledgerId") long ledgerId,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 获取分户下所有表中“电流日不平衡度最大值”最大的电表列表
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Long> getMaxIuMaxListOfAP(@Param("ledgerId") long ledgerId,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 获取分户下时间段内发生最大负载率的测量点id
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Long getMaxAvrLoadPointId(@Param("ledgerId") long ledgerId,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 获取分户下时间段内发生最小负载率的测量点id
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Long getMinAvrLoadPointId(@Param("ledgerId") long ledgerId,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
	
	/**
	 * 取分户负载率曲线
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getLedgerLoadData(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取分户负载率发生时间曲线
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getLedgerLoadTimeData(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 获取分户的容量
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Double getLedgerEPwr(@Param("ledgerId") long ledgerId);
	
	/**
	 * 取分户最大最小负载率
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> getLedgerMaxMinLoadData(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取分户最大负载率发生时间
	 * 
	 * @param load
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Date getLedgerMaxLoadTime(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取分户最小负载率发生时间
	 * 
	 * @param load
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Date getLedgerMinLoadTime(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	/**
	 * 获取有功功率曲线
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getAPChartDatas(Map<String, Object> queryMap);
	
	/**
	 * 取分户平均最大负载率
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double getLedgerAvgLoadMax(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 得到所选时间段内，达到基准功率的所有时间点
     */
    public List<Date> getOverStartTimesBy(@Param("ledgerId") long ledgerId, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate, @Param("startPower") double startPower);

    /**
     * 用电能示值曲线计算电量
     */
    public Double getLedgerQDataByE(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
    
    
    public List<Map<String,Object>> getLineLossByLevel_new(@Param("ledgerId") long ledgerId, @Param("meterLevel") int meterLevel);
	
	/**
	 * 获取线损表的子表配置
	 *
	 * @param meterId
	 * @param meterLevel
	 * @return
	 */
	public List<Long> getLineLossMeters_new(@Param("meterIds") List<Long> meterIds, @Param("meterLevel") int meterLevel);
	
	
	/**
	 * 根据电能示值计算线损的电量
	 *
	 * @param pointIds 原是String  先修改为 List<Long>
	 * @param beginTime
	 * @param endTime
	 * @param attrId
	 * @return
	 */
	public Double getDayLineLossInfo_new(@Param("pointIds") List<Long> pointIds, @Param("beginTime") Date beginTime,
												 @Param("endTime") Date endTime);
}
