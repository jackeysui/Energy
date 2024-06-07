package com.linyang.energy.mapping.contrastanalysis;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.RealCurveBean;
import org.apache.ibatis.annotations.Param;

/**
 * @Description 对比分析mapper
 * @author Leegern
 * @date Jan 15, 2014 2:46:36 PM
 */
public interface YearOnYearAnalysisMapper {
	
	/**
	 * 查询同比环比统计数据
	 * @param param 查询参数s
	 * @return
	 */
	List<RealCurveBean> queryEnergyStatData(Map<String, Object> param);

    List<RealCurveBean> queryMeterEnergyStatData(Map<String, Object> param);
	
	/**
	 * 查询电水气峰值
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> getElecWaterGasPeak(Map<String, Object> param);
	
	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/**
	 * 查询分项占比数据(按照分项分组)
	 * @param param 查询参数
	 * @return
	 */
	List<RealCurveBean> queryPartialScaleData(Map<String, Object> param);
	
	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/**
	 * 查询能耗排名数据(按照分户分组)
	 * @param param 查询参数
	 * @return
	 */
	List<RealCurveBean> queryEnergyRankingData(Map<String, Object> param);
    List<Map<String, Object>> queryEnergyRankingDataNew(Map<String, Object> param);
	
	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/**
	 * 查询分户对比数据
	 * @param param 查询参数
	 * @return
	 */
	List<RealCurveBean> queryHouseholdData(Map<String, Object> param);

	List<RealCurveBean> queryHouseholdMeterData(Map<String, Object> param);

	/**
	 * 查询分户Top
	 * @param param 查询参数
	 * @return
	 */
	List<RealCurveBean> getEnergyTop(Map<String, Object> param);

    List<Long> getChildDevice(@Param("typeId") Long typeId);
}
