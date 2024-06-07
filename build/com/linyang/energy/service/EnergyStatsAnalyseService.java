package com.linyang.energy.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.RealCurveBean;

/**
 * @Description 能耗分项统计分析Service
 * @author Leegern
 * @date Dec 11, 2013 10:50:32 AM
 */
public interface EnergyStatsAnalyseService {

	/**
	 * 查询能耗分项统计数据
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> queryStatData(Map<String, Object> param) throws ParseException;
	
	/**
	 * 查询电水气峰值
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> getElecWaterGasPeak(Map<String, Object> param);
	
	/**
	 * 查询耗电、水、气量(今日、去年同期)
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> getUseElecInfo(Map<String, Object> param) throws ParseException;
	
//	/**
//	 * 查询平均值(电水气)
//	 * @param param 查询参数
//	 * @return
//	 */
//	Map<String, Object> getAverageValue(Map<String, Object> param);
	
	/**
	 * 查询今年TopN日数据(电水气)
	 * @param param 查询参数
	 * @return
	 */
	List<RealCurveBean> getCurrentYearTop(Map<String, Object> param) throws ParseException;
}
