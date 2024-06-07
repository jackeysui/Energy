package com.linyang.energy.mapping.energysavinganalysis;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.DeviceTypeBean;
import com.linyang.energy.model.LedgerStatBean;
import com.linyang.energy.model.ProductStatBean;

/**
 * @Description 节能潜力排名Mapper
 * @author Leegern
 * @date Feb 11, 2014 5:20:28 PM
 */
public interface EnergySavingRankingMapper {
	
	/**
	 * 查询能耗类型
	 * @param param 查询参数
	 * @return
	 */
	List<ProductStatBean> getEnergyTypes(Map<String, Object> param);
	
	/**
	 * 查询产品统计Top数据
	 * @param param 查询参数
	 * @return
	 */
	List<ProductStatBean> queryProductStatTopData(Map<String, Object> param);
	
	/**
	 * 查询建筑统计Top数据
	 * @param param 查询参数
	 * @return
	 */
	List<LedgerStatBean> queryLedgerStatTopData(Map<String, Object> param);
	
	/**
	 * 查询分项根节点数据
	 * @return
	 */
	List<DeviceTypeBean> getDeviceTypes();
	
	/**
	 * 查询建筑能效温度
	 * @param param 查询参数
	 * @return
	 */
	List<LedgerStatBean> queryLedgerBaseTemp(Map<String, Object> param);
	
	/**
	 * 查询分项用电、水统计数据
	 * @param param 查询参数
	 * @return
	 */
	List<LedgerStatBean> queryParticalStatData(Map<String, Object> param);
}
