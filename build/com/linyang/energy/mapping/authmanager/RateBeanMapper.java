package com.linyang.energy.mapping.authmanager;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.RateBean;
import com.linyang.energy.model.RateSectorBean;
import com.linyang.energy.model.RateSectorContentBean;
import org.apache.ibatis.annotations.Param;

public interface RateBeanMapper {
	/**
	 * 
	 * 函数功能说明  :分页获取费率列表
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<String, Object>>  getRatePageData(Map<String, Object> map);
	/**
	 * 
	 * 函数功能说明  :根据Id删除费率信息
	 * @param rateId      
	 * @return  void     
	 * @throws
	 */
	public void deleteRate(Long rateId);
	/**
	 * 
	 * 函数功能说明  :根据费率Id删除费率配置信息
	 * @param rateId      
	 * @return  void     
	 * @throws
	 */
	public void deleteRateDetail(Long rateId);
	/**
	 * 
	 * 函数功能说明  :根据费率Id删除费率配置详细信息
	 * @author guosen
	 * @date 2014-12-18
	 * @param rateId      
	 * @return  void     
	 * @throws
	 */
	public void deleteRateSectorContent(Long rateId);
	
	
	/**
	 * 
	 * 函数功能说明  :根据时段Id删除费率配置
	 * @param sectorId      
	 * @return  void     
	 * @throws
	 */
	public void deleteRateData(Long sectorId);
	/**
	 * 
	 * 函数功能说明  :检查是否有分户和费率关联
	 * @param rateId
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public long getRateLedgerLink(Long rateId);
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的费率名称
	 * @param rateName
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public long checkRateName(@Param("rateName")String rateName, @Param("rateId")Long rateId);
	
	/**
	 * 
	 * 函数功能说明  :插入费率信息
	 * @param rate      
	 * @return  void     
	 * @throws
	 */
	public void insertRateData(RateBean rate);
	/**
	 * 
	 * 函数功能说明  :插入费率配置信息
	 * @param sector      
	 * @return  void     
	 * @throws
	 */
	public void insertRateDetail(RateSectorBean sector);
	
	/**
	 * 
	 * 函数功能说明  :根据费率Id获得相应的费率信息 
	 * @return      
	 * @return  rateId     
	 * @throws
	 */
	public RateBean getRateData(Long rateId);
	/**
	 * 
	 * 函数功能说明  :根据费率Id获得相应的配置信息
	 * @param sectorId
	 * @return      
	 * @return  List<ScheduleDetailBean>     
	 * @throws
	 */
	public List<RateSectorBean> getSectorData(Long sectorId);
    
    /**
	 * 
	 * 函数功能说明  :根据rateId,sectorId获得相应的配置信息
	 * @param sectorId
	 * @return RateSectorBean     
	 * @throws
	 */
	public RateSectorBean getSectorData2(@Param("rateId")Long rateId,@Param("sectorId")Long sectorId);
	
	/**
	 * 
	 * 函数功能说明  :更新 电 费率信息
	 * @param rate      
	 * @return  void     
	 * @throws
	 */
	public void updateRateInfo(RateBean rate);

    /**
     *
     * 更新 水、气、热 费率信息
     *
     */
    public void updateOtherRate(@Param("rateType")Integer rateType, @Param("rateId")Long rateId, @Param("rateName")String rateName, @Param("rateRemark")String rateRemark);

    /**
     *
     * 新增 水、气、热 费率信息
     *
     */
    public void insertOtherRate(@Param("rateType")Integer rateType, @Param("rateId")Long rateId, @Param("rateName")String rateName, @Param("rateRemark")String rateRemark);


    /**
     *
     * 根据ID查询 水、气、热费率信息
     *
     */
    public List<Map<String,Object>> getWaterGasHotByRateId(@Param("rateId")Long rateId);

	/**
	 * 
	 * 函数功能说明  :更新费率配置信息
	 * @param sector      
	 * @return  void     
	 * @throws
	 */
	public void updateSectorInfo(RateSectorBean sector);
	
	/**
	 * 
	 * 函数功能说明  :新增费率配置详细信息
	 * @author guosen
	 * @date 2014-12-18
	 * @param RateSectorContentBean      
	 * @return  void     
	 * @throws
	 */
	public void insertSectorContent(RateSectorContentBean sectorContent);
	
	/**
	 * 函数功能说明  :得到费率配置详细信息
	 * @author guosen
	 * @date 2014-12-18
	 * @param rateId
	 * @return
	 */
	public List<RateSectorContentBean> getSectorContentData(Long rateId);
	/**
	 * 防止重放
	 * */
	public List<RateBean> getFilteredRate(Map<String, Object> queryMap);
	
}
