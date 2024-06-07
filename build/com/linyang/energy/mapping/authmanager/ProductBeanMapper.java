package com.linyang.energy.mapping.authmanager;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.ProductsBean;

public interface ProductBeanMapper {
	/**
	 * 
	 * 函数功能说明  :分页获取费率列表
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<ProductsBean>  getProductList();
	
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
	 * 函数功能说明  :根据Id删除费率信息
	 * @param rateId      
	 * @return  void     
	 * @throws
	 */
	public void deleteProduct(Long productId);
	
	/**
	 * 
	 * 函数功能说明  :根据产品Id删除耗能种类关联信息
	 * @param productId      
	 * @return  void     
	 * @throws
	 */
	public void deletePowerDetail(Long productId);
	
	/**
	 * 
	 * 函数功能说明  :根据Id查询与计划表的关联关系
	 * @param productId
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public long getProductLink(Long productId);
	
	/**
	 * 
	 * 函数功能说明  :根据图片Id删除图片
	 * @param sectorId      
	 * @return  void     
	 * @throws
	 */
	public void deletePicData(Long picId);
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的费率名称
	 * @param rateName
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public long checkProductName(String productName);
	
	/**
	 * 
	 * 函数功能说明  :插入产品信息
	 * @param rate      
	 * @return  void     
	 * @throws
	 */
	public void insertProductData(ProductsBean product);
	/**
	 * 
	 * 函数功能说明  :插入图片信息
	 * @param sector      
	 * @return  void     
	 * @throws
	 */
	public void insertPicData(ProductsBean product);
	
	/**
	 * 
	 * 函数功能说明  :插入耗能种类信息
	 * @param map      
	 * @return  void     
	 * @throws
	 */
	public void insertPowerData(Map<String, Object> map);
	
	/**
	 * 
	 * 函数功能说明  :根据产品Id获得相应的产品信息 
	 * @return      
	 * @return  productId     
	 * @throws
	 */
	public ProductsBean getProdInfo(Long productId);
	/**
	 * 
	 * 函数功能说明  :根据产品Id获得相应的耗能种类信息
	 * @param productId
	 * @return      
	 * @return  List<Map<String,Object>>    
	 * @throws
	 */
	public List<Map<String,Object>> getPowerDataById(Long productId);
	
	/**
	 * 
	 * 函数功能说明  :更新产品信息
	 * @param rate      
	 * @return  void     
	 * @throws
	 */
	public void updateProductInfo(ProductsBean product);
	
	/**
	 * 
	 * 函数功能说明  :更新图片信息
	 * @param sector      
	 * @return  void     
	 * @throws
	 */
	public void updatePicInfo(ProductsBean product);
	
}
