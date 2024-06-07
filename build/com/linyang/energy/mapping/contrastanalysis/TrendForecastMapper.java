package com.linyang.energy.mapping.contrastanalysis;

import java.util.List;
import java.util.Map;



/**
 * 趋势预测数据访问层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 16, 2014
 */
public interface TrendForecastMapper {
	/**
	 * 获取曲线数据，此接口只能是选择分钟的时候才能调用
	 * @param queryCondition
	 * @return
	 */
	public List<Map<String,Object>> getCurDatas(Map<String,Object> queryCondition);
	
	/**
	 * 得到日 周 月 年的数据
	 * @param queryCondition
	 * @return
	 */
	public List<Map<String,Object>> getStatDatas(Map<String,Object> queryCondition);

}
