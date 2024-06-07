package com.linyang.energy.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.linyang.energy.dto.ChartItemWithTime;

/**
 * 电费业务逻辑层接口
 * 
 * @description:
 * @author:gaofeng
 * @date:2014.12.18
 */
public interface CostService {

	/**
	 * 得到电量图表
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<ChartItemWithTime> getDayMeterChartData(Map<String, Object> queryMap);

    public List<ChartItemWithTime> getDayEmoDcpChartData(Map<String, Object> queryMap);

	/**
	 * 计算电费
	 * @return
	 */
    public Map<String, Object> calEmoDcpEleFee(Map<String, Object> queryMap);
    public Map<String, Object> calEmoDcpEleFee2(Map<String, Object> queryMap); // 更改企业获取电费sql

	/**
	 * 计算上个月电费
	 * 
	 * @param queryMap
	 * @return
	 */
    public Map<String, Object> calEmoDcpPreMonthEleFee(Map<String, Object> queryMap);

	
	/**
	 * 取月最大需量图数据
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<ChartItemWithTime> getMonMaxDemandChartData(Map<String, Object> queryMap);
	
	/**
	 * 获取需量电费数据
	 * 
	 * @param queryMap
	 * @return
	 */
	public Map<String, Object> getMonMDFeeData(Map<String, Object> queryMap);


    /**
     * 近12个月 月最大需量分析
     */
    public Map<String, Object> nearYearMonMaxAnalysis(Long pointId,int treeType);
	
	/**
	 * 功率因数评价
	 * 
	 * @param queryMap
	 * @return
	 */
	public Map<String, Object> getPFEval(Map<String, Object> queryMap);
	
	
	/**
	 * 计算电费(容需对比)
	 * @return
	 */
	public Map<String, Object> calEmoDcpEleFee_new(Map<String, Object> queryMap);
	
}
