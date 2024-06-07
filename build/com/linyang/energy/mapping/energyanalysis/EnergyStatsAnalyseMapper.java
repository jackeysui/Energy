package com.linyang.energy.mapping.energyanalysis;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.RealCurveBean;

/**
 * @Description 能耗分项统计分析Mapper
 * @author Leegern
 * @date Dec 11, 2013 10:49:47 AM
 */
public interface EnergyStatsAnalyseMapper {
	
	/**
	 * 查询能耗分项统计数据
	 * @param param 查询参数
	 * @return
	 */
	List<RealCurveBean> queryStatData(Map<String, Object> param);
	
	/**
	 * 查询电水气峰值
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> getElecWaterGasPeak(Map<String, Object> param);
	
	/**
	 * 查询电水气TopN日数据
	 * @param param 查询条件
	 * @return
	 */
	List<RealCurveBean> getElecWaterGasTopNData(Map<String, Object> param);

    List<RealCurveBean> getMeterElecWaterGasTopN(Map<String, Object> param);
	
	/**
	 * 查询曲线当天点数
	 * @param param 查询参数
	 * @return
	 */
	int queryCurveCount(Map<String, Object> param);
	
	/**
	 * 根据分项Id查询子分项
	 * @param typeId 分项Id
	 * @return
	 */
	List<RealCurveBean> getSubDeviceTypes(long typeId);
}
