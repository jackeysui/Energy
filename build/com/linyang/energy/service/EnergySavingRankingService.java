package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.DeviceTypeBean;
import com.linyang.energy.model.LedgerStatBean;
import com.linyang.energy.model.ProductStatBean;

/**
 * @Description 节能潜力排名Service
 * @author Leegern
 * @date Feb 11, 2014 5:22:31 PM
 */
public interface EnergySavingRankingService {
	
	/**
	 * 查询能耗类型
	 * @param param 查询参数
	 * @return
	 */
	List<ProductStatBean> getEnergyTypes(Map<String, Object> param);
	
	/**
	 * 查询节能潜力统计Top数据
	 * @param param 查询参数
	 * @return
	 */
	Map<String, Object> queryStatTopData(Map<String, Object> param);
	
	/**
	 * 查询分项根节点数据
	 * @return
	 */
	List<DeviceTypeBean> getDeviceTypes();
	
	/**
	 * 查询分项用电、水统计数据
	 * @param param 查询参数
	 * @return
	 */
	List<LedgerStatBean> queryParticalStatData(Map<String, Object> param);
}
