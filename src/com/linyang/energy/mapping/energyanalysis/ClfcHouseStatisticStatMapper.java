package com.linyang.energy.mapping.energyanalysis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.LegdeStatBean;

/**
 * 能耗分类统计、能耗分户统计数据库访问层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 3, 2014
 */
public interface ClfcHouseStatisticStatMapper {
	/**
	 * 得到一个分户下的费用信息
	 * @param queryMap
	 * @return
	 */
	public List<Map<String,Object>> getLegerConst(Map<String,Object> queryMap);

	/**
	 * 得到一个分户下第一级分户的费用信息
	 * @param queryMap
	 * @return
	 */
	public List<Map<String,Object>> getLegerChildConst(Map<String,Object> queryMap);
	
	/**
	 * 得到一个分户的下第一级能耗统计
	 * @param queryMap
	 * @return
	 */
	public List<LegdeStatBean> getHouseStatPage(Map<String,Object> queryMap);
	
	
	public List<Map<String,Object>> getRateDatas(Map<String,Object> queryMap);
	
	//原public List<Map<String, Object>> getLegerdsPics(@Param("legedIdsStr")Long[] legerdIds);
	public List<Map<String, Object>> getLegerdsPics(Long[] legerdIds);
}
