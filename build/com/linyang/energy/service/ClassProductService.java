package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.ProductConfigBean;
import com.linyang.energy.model.ProductOutputBean;

/**
 * 
 * @author Administrator
 *
 */
public interface ClassProductService {

	/**
	 * 根据ledgerId得到产品配置列表
	 * @param ledgerId
	 * @return
	 */
	List<ProductConfigBean> queryProductConfigPageList(Map<String, Object> param);
	
	/**
	 * 根据productId得到产品配置信息
	 * @param productId
	 * @return
	 */
	Map<String, Object> getProductConfigById(Long productId);
	
	/**
     * 新增产品配置
     * @return
     */
	Map<String, Object> insertProductConfig(Map<String, Object> productConfigInfo);

    /**
     * 修改 产品配置
     * @return
     */
    void updateProductConfig(Map<String, Object> productConfigInfo);
    
    /**
     * 删除 产品配置
     * @return
     */
    Map<String, Object> deleteProductConfig(Long productId);
    
    
    /**
     * 根据条件得到产量列表
     * @param param
     * @return
     */
    List<ProductOutputBean> queryProductOutputPageList(Map<String, Object> param);
    
    /**
     * 根据ledgerId获取产品列表
     * @param
     * @return
     */
    List<Map<String, Object>> getProductListByLedgerAll(Long ledgerId);

    List<Map<String, Object>> getProductListByLedger(Long ledgerId, Long classId, String workshopIds);
    
    /**
     * 根据outputId获取产量信息
     * @param outputId
     * @return
     */
    Map<String, Object> getProductOutputById(Long outputId);
    
    /**
     * 根据productId获取车间列表
     * @param
     * @return
     */
    List<Map<String, Object>> getWorkshopListByProductId(Long productId);
    
    /**
     * 根据productId,workshopId获取班组列表
     * @param
     * @return
     */
    List<Map<String, Object>> getTeamListByProductId(Long productId,Long workshopId);
    
    /**
     * 根据ledgerId获取车间列表
     * @param
     * @return
     */
    List<Map<String, Object>> getWorkshopListByLedgerId(Long ledgerId);
    
    /**
     * 根据workshopId获取班组列表
     * @param
     * @return
     */
    List<Map<String, Object>> getTeamListByWorkshopId(Long workshopId);
    
    /**
     * 根据productId获取单位
     * @param
     * @return
     */
    String getProductUnitByProductId(Long productId);
    
    /**
     * 插入T_PRODUCT_OUTPUT
     * @param
     * @return
     */
    Map<String, Object> insertOrUpdateProductOutput(Map<String, Object> productOutputInfo);
    
    /**
     * 根据outputId删除T_PRODUCT_OUTPUT
     * @param
     * @return
     */
    void deleteProductOutputById(Long outputId);
}
