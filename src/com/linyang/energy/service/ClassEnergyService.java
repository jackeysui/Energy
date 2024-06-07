package com.linyang.energy.service;

import java.util.Map;

/**
 * 班组能耗service
 * @author Administrator
 *
 */
public interface ClassEnergyService {

	/**
	 * 查询能耗分析数据
	 * @param param
	 * @return
	 */
	Map<String, Object> search(Map<String, Object> param);

	/**
	 * 根据ClassId获取车间列表
	 * @param classId
	 * @return
	 */
	Map<String, Object> getWorkshopListByClassId(Long classId);

}
