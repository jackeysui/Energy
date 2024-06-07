package com.linyang.energy.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import com.linyang.common.web.page.Page;
import com.linyang.energy.model.RateBean;
import com.linyang.energy.model.RateSectorBean;
import com.linyang.energy.model.RateSectorContentBean;

public interface RateService {
	
	/**
	 * 
	 * 函数功能说明  :分页查询费率信息
	 * @param page
	 * @param queryMa
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	List<Map<String,Object>> getRatePageData(Page page,Map<String, Object> queryMa);
	
	/**
	 * 
	 * 函数功能说明  :根据Id删除费率
	 * @param rateId      
	 * @return  void     
	 * @throws
	 */
	public boolean deleteRateData(Long rateId);
	
	/**
	 * 
	 * 函数功能说明  :检查是否有分户和费率关联
	 * @param rateId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean getRateLedgerLink(Long rateId);
	
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的费率名称
	 * @param rateName
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public boolean checkRateName(String rateName, Long rateId);
	
	/**
	 * @throws IOException 
	 * 
	 * 函数功能说明  :添加费率信息
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean addRateData(RateBean rate,List<Map<String,Object>> sectorList, List<Map<String,Object>> sectorContentList);
	
	/**
	 * 
	 * 函数功能说明  :根据费率Id获得相应的费率信息
	 * @param rateId
	 * @return      
	 * @return  RateBean     
	 * @throws
	 */
	public RateBean getRateData(Long rateId);
	
	/**
	 * 
	 * 函数功能说明  :根据费率Id获得相应的费率配置信息
	 * @param rateId
	 * @return      
	 * @return  List<RateSectorBean>     
	 * @throws
	 */
	public List<RateSectorBean> getSectorData(Long rateId);
	
	/**
	 * 得到费率详细信息
	 * @author guosen
	 * @date 2014-12-18
	 * @param rateId
	 * @return
	 */
	List<RateSectorContentBean> getSectorContentData(Long rateId);
	
	/**
	 * 
	 * 函数功能说明  :更新 电 费率信息
	 * @param rate
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean updateRateData(RateBean rate,List<Map<String,Object>> sectorList, List<Map<String,Object>> sectorContentList);

    /**
     *
     *  新增/更新 水、气、热费率信息
     *
     */
    public void updateOtherRateData(int rateType, Long rateId, String rateName, String rateRemark, Double rateValue);

    /**
     *
     *  根据ID查询 水、气、热费率信息
     *
     */
    public List<Map<String,Object>> getWaterGasHotByRateId(Long rateId);
	
}
