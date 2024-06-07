package com.linyang.energy.mapping.authmanager;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.DesignBean;
import com.linyang.energy.model.PowerInputBean;

public interface DesignBeanMapper {
	/**
	 * 
	 * 函数功能说明  :获取产品列表
	 * @return      
	 * @return  List<DesignBean>     
	 * @throws
	 */
	public List<DesignBean>  getDesignList();
	
	/**
	 * 
	 * 函数功能说明  :根据条件查询生产计划
	 * @param map
	 * @return      
	 * @return  List<DesignBean>     
	 * @throws
	 */
	public List<DesignBean> searchDesign(Map<String,Object> map);
	
	/**
	 * 
	 * 函数功能说明  :录入能耗列表
	 * @return      
	 * @return  List<PowerInputBean>     
	 * @throws
	 */
	public List<PowerInputBean>  getPowerList();
	
	/**
	 * 
	 * 函数功能说明  :获取耗能种类
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<Long,String>> getPowerData(Long productId);
	
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
	 * 函数功能说明  :获取产品种类
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<Long,String>> getProductData();
	
	/**
	 * 
	 * 函数功能说明  :获取流水线
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<Long,String>> getAssembleData();
	
	/**
	 * 
	 * 函数功能说明  :根据Id删除生产计划信息
	 * @param rateId      
	 * @return  void     
	 * @throws
	 */
	public void deletePlan(Long planId);
	
	/**
	 * 
	 * 函数功能说明  :根据产品Id删除能耗信息
	 * @param planId      
	 * @return  void     
	 * @throws
	 */
	public void deletePower(Long planId);
	
	/**
	 * 
	 * 函数功能说明  :根据产品Id删除耗能种类关联信息
	 * @param productId      
	 * @return  void     
	 * @throws
	 */
	public void deletePowerDetail(Map<String,Object> map);
	
	/**
	 * 
	 * 函数功能说明  :插入计划信息
	 * @param rate      
	 * @return  void     
	 * @throws
	 */
	public void insertPlanData(DesignBean design);
	
	/**
	 * 
	 * 函数功能说明  :插入能耗信息
	 * @param sector      
	 * @return  void     
	 * @throws
	 */
	public void insertPowerInfo(PowerInputBean power);
	
	/**
	 * 
	 * 函数功能说明  :根据计划Id获得相应的产品信息 
	 * @return      
	 * @return  productId     
	 * @throws
	 */
	public DesignBean getPlanInfo(Long planId);
	/**
	 * 
	 * 函数功能说明  :根据计划Id获得相应的耗能种类信息
	 * @param productId
	 * @return      
	 * @return  List<Map<String,Object>>    
	 * @throws
	 */
	public List<PowerInputBean> getPowerDataById(Long planId);
	
	/**
	 * 
	 * 函数功能说明  :更新产品信息
	 * @param rate      
	 * @return  void     
	 * @throws
	 */
	public void updatePlanInfo(DesignBean design);
	
	/**
	 * 
	 * 函数功能说明  :更新图片信息
	 * @param sector      
	 * @return  void     
	 * @throws
	 */
	public void updatePowerInfo(PowerInputBean power);
	
}
