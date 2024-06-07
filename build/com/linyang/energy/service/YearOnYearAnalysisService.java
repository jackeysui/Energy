package com.linyang.energy.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.RealCurveBean;

/**
 * @Description 对比分析Service
 * @author Leegern
 * @date Jan 14, 2014 3:55:33 PM
 */
public interface YearOnYearAnalysisService {
	
	/**
	 * 查询同比环比统计数据
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> queryEnergyStatData(Map<String, Object> param) throws ParseException;
	
	/**
	 * 查询耗电、水、气量(今日、昨日同期、上周同期、上月同期、去年同期、平均消耗)
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> getScaleInfo(Map<String, Object> param) throws ParseException;
	
	/**
	 * 查询平均值(电水气)
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> getAverageValue(Map<String, Object> param);
	
	/**
	 * 查询电水气峰值
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> getElecWaterGasPeak(Map<String, Object> param);
	
	/**
	 * 查询今年TopN日数据(电水气)
	 * @param param 查询参数
	 * @return
	 * @throws ParseException
	 */
	List<RealCurveBean> getCurrentYearTop(Map<String, Object> param) throws ParseException;
	
	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/**
	 * 查询分项占比数据
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> queryPartialScaleData(Map<String, Object> param);
	
	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/**
	 * 获取能耗排名数据(包括最大、最小、增幅、降幅)
	 * @param param 查询参数
	 * @return
	 * @throws ParseException
	 */
	Map<String, Object> queryEngergyRankingData(Map<String, Object> param) throws ParseException;
    Map<String, Object> queryEngergyRankingDataNew(Map<String, Object> param) throws ParseException;
	
	/**
	 * 查询耗电、水、气量(昨日统计、前日、平均消耗)
	 * @param param 查询参数
	 * @return
	 * @throws ParseException 
	 */
	Map<String, Object> getRankingScaleInfo(Map<String, Object> param) throws ParseException;
	
	/**
	 * 查询能耗排名增幅、降幅TopN日数据(电水气)
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> queryEnergyTop(Map<String, Object> param) throws IllegalAccessException, InvocationTargetException;
	
	/* ----------------------------- 华丽丽滴分割线 ----------------------------------- */
	/**
	 * 查询分户对比数据
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> queryHouseholdData(Map<String, Object> param) throws ParseException;
	
	/**
	 * 查询分户对比耗电、水、气量(昨日统计、前日、平均消耗)
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> getHouseholdScaleInfo(Map<String, Object> param) throws ParseException;
	
	/**
	 * 查询消耗(电水汽)前N名
	 * @param param 查询参数
	 * @return
	 */
	List<RealCurveBean> getEnergyTop(Map<String, Object> param);

    /**
     * 查询父分项下面的子分项
     * */
    List<Long> getChildDevice(Long typeId);

    /**
     * 对yearOnYearAnalysisMapper.queryEnergyStatData方法的扩充，同时支持EMO和DCP
     **/
    public List<RealCurveBean> getEmoDcpEnergyStatData(Map<String, Object> param);

    /**
     * 对yearOnYearAnalysisMapper.queryHouseholdData方法的扩充，同时支持EMO和DCP
     **/
    public List<RealCurveBean> getEmoDcpHouseholdData(Map<String, Object> param);

}
