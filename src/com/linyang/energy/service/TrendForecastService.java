package com.linyang.energy.service;

import java.util.Map;

/**
 * 趋势预测业务逻辑层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 15, 2014
 */
public interface TrendForecastService {
	
	/**
	 * 得到趋势预测的图形数据
	 * @param queryCondition
	 * @return
	 */
	public Map<String,Object> getTrendForecastChartDatas(Map<String,Object> queryCondition);

}
