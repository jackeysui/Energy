package com.linyang.energy.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import com.linyang.energy.model.DesignBean;
import com.linyang.energy.model.PowerInputBean;

public interface DesignService {
	
	/**
	 * 
	 * 函数功能说明  :得到计划列表
	 * @return      
	 * @return  List<ProductsBean>     
	 * @throws
	 */
	Map<String,Object> getPlanListData();
	
	/**
	 * 
	 * 函数功能说明  :根据条件查询生产计划
	 * @param map
	 * @return      
	 * @return  List<DesignBean>     
	 * @throws
	 */
	public Map<String,Object> searchDesign(Map<String,Object> map);
	
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
	 * @return  List<Map<Long,String>>     
	 * @throws
	 */
	public List<Map<Long,String>> getProductInfo();
	
	/**
	 * 
	 * 函数功能说明  :获取流水线
	 * @return      
	 * @return  List<Map<Long,String>>     
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
	public boolean deletePlanData(Long planId);
	
	/**
	 * @throws IOException 
	 * 
	 * 函数功能说明  :添加产品信息
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean addPlanData(DesignBean plan,List<Map<String, Object>> powerList);
	
	/**
	 * 
	 * 函数功能说明  :根据产品Id获得相应的生产计划信息 
	 * @return      
	 * @return  productId     
	 * @throws
	 */
	public DesignBean getPlanInfo(Long planId);
	
	/**
	 * 
	 * 函数功能说明  :根据Id获得相应的耗能种类信息
	 * @param productId
	 * @return      
	 * @return  List<Map<String,Object>>    
	 * @throws
	 */
	public List<PowerInputBean> getPowerDataById(Long planId);
	
	/**
	 * 
	 * 函数功能说明  :更新计划信息
	 * @param rate
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean updatePlanData(DesignBean plan,List<Map<String, Object>> powerList);
}
