package com.linyang.energy.service;

import java.util.Map;

/**
 * @Description 单位产品能耗Service
 * @author Leegern
 * @date Dec 11, 2013 11:05:38 AM
 */
public interface ProductsEnergyService {

	/**
	 * 查询产品能耗信息
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> queryProductsEnergyInfo(Map<String, Object> param);

}
