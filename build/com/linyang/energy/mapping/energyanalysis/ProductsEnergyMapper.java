package com.linyang.energy.mapping.energyanalysis;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.CoalBean;
import com.linyang.energy.model.ProductsBean;

/**
 * @Description 单位产品能耗Mapper
 * @author Leegern
 * @date Dec 11, 2013 11:05:02 AM
 */
public interface ProductsEnergyMapper {
	/**
	 * 查询产品能耗信息
	 * @param param 查询参数
	 * @return
	 */
	List<ProductsBean> queryProductsEnergyInfo(Map<String, Object> param);
	
	/**
	 * 获取标准煤转换关系
	 * @return
	 */
	List<CoalBean> getCoalRelateValue();
}
