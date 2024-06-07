package com.linyang.energy.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import com.linyang.energy.model.ProductsBean;

public interface ProductService {
	
	/**
	 * 
	 * 函数功能说明  :分页查询费率信息
	 * @param page
	 * @param queryMa
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	List<ProductsBean> getProductListData();
	
	/**
	 * 
	 * 函数功能说明  :获取耗能种类
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<Long,String>> getPowerData();
	
	/**
	 * 
	 * 函数功能说明  :获取计量单位
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<Long,String>> getUnitData();
	
	/**
	 * 
	 * 函数功能说明  :得到关联计划的个数
	 * @param productId
	 * @return      
	 * @return  Long     
	 * @throws
	 */
	public boolean getProductLink(Long productId);
	
	/**
	 * 
	 * 函数功能说明  :根据Id删除产品信息
	 * @param rateId      
	 * @return  void     
	 * @throws
	 */
	public boolean deleteProductData(Long productId,Long picId);
	
	
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的产品名称
	 * @param rateName
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public boolean checkProductName(String productName);
	
	/**
	 * @throws IOException 
	 * 
	 * 函数功能说明  :添加产品信息
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean addProductData(ProductsBean product,String powerIds);
	
	/**
	 * 
	 * 函数功能说明  :根据产品Id获得相应的产品信息 
	 * @return      
	 * @return  productId     
	 * @throws
	 */
	public ProductsBean getProductData(Long productId);
	
	/**
	 * 
	 * 函数功能说明  :根据产品Id获得相应的耗能种类信息
	 * @param productId
	 * @return      
	 * @return  List<Map<String,Object>>    
	 * @throws
	 */
	public List<Map<String,Object>> getPowerData(Long productId);
	
	/**
	 * 
	 * 函数功能说明  :更新费率信息
	 * @param rate
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean updateProductData(ProductsBean product,String powerIds);
}
