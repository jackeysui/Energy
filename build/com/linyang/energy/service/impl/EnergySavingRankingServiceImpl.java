package com.linyang.energy.service.impl;

import java.math.BigDecimal;import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leegern.util.CollectionUtil;
import com.leegern.util.NumberUtil;
import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.energy.common.CommonResource;
import com.linyang.energy.mapping.energyanalysis.ProductsEnergyMapper;
import com.linyang.energy.mapping.energysavinganalysis.EnergySavingRankingMapper;
import com.linyang.energy.model.CoalBean;
import com.linyang.energy.model.DeviceTypeBean;
import com.linyang.energy.model.LedgerStatBean;
import com.linyang.energy.model.ProductStatBean;
import com.linyang.energy.model.ProductsBean;
import com.linyang.energy.service.EnergySavingRankingService;import com.linyang.energy.utils.DataUtil;

/**
 * @Description 节能潜力排名Service实现
 * @author Leegern
 * @date Feb 11, 2014 5:23:07 PM
 */
@Service
public class EnergySavingRankingServiceImpl implements EnergySavingRankingService {
	@Autowired
	private EnergySavingRankingMapper energySavingRankingMapper;
	@Autowired
	private ProductsEnergyMapper productsEnergyMapper;
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EnergySavingRankingService#queryStatTopData(java.util.Map)
	 */
	@Override
	public Map<String, Object> queryStatTopData(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		int analyseType = (Integer)param.get("analyseType");  // 分析类型
		int queryType = (Integer)param.get("queryType");      // 查询类型
		// 标准煤转换关系
		Map<String, CoalBean> coalMap = null;
		
		// 表示产品
		if (analyseType == 1) {
			// 查询产品统计Top数据 
			List<ProductStatBean> list = energySavingRankingMapper.queryProductStatTopData(param);
			if (CollectionUtil.isNotEmpty(list)) {
				// 能耗对标
				if (queryType == 1) {
					for (ProductStatBean bean : list) {
						bean.setRate(new BigDecimal(bean.getTotalUse()).subtract(new BigDecimal(bean.getUnitConsumer())).multiply(new BigDecimal(100)).divide(new BigDecimal(bean.getUnitConsumer()), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue()); // 占比
					}
				}
				// 综合能耗需要查询产量
				if (queryType == 2) {
					// 获取产品产量
					Map<Long, Double> productsMap = this.getProducts((Date)param.get("beginTime"), (Date)param.get("endTime"));
					for (ProductStatBean bean : list) {
						if (productsMap.containsKey(bean.getProductId())) {
							// 设置产品产量
							bean.setProductValue(productsMap.get(bean.getProductId()));
						}
					}
				}
				// 分项耗电、水量需要转换标准煤
				else if (queryType == 4) {
					// 获取标准煤转换关系 
					coalMap = this.getCoalRelateValue();
					for (ProductStatBean bean : list) {
						if (coalMap.containsKey(bean.getTypeId() + "")) {
							bean.setCommValue(DataUtil.doubleMultiply(coalMap.get(bean.getTypeId() + "").getCoalValue(), bean.getCostValue())); // 耗费量计算标准煤
						}
					}
				}
				// 按照占比降序
				if (CollectionUtil.isNotEmpty(list) && queryType == 1) {
					Collections.sort(list, new Comparator<ProductStatBean>(){
						@Override
						public int compare(ProductStatBean o1, ProductStatBean o2) {
							return DataUtil.doubleSubtract(o1.getRate(), o2.getRate()) <= 0 ? 1 : -1;
						}
					});
				}
			}
			result.put("products", list);
		}
		// 表示建筑
		else {
			// 查询产品统计Top数据 
			List<LedgerStatBean> list = energySavingRankingMapper.queryLedgerStatTopData(param);
			CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
			if (CollectionUtil.isNotEmpty(list)) {
				// 综合能效
				if (queryType == 1) {
					for (LedgerStatBean bean : list) {
						bean.setTotalUse(DataUtil.doubleDivide(bean.getTotalUse(), bean.getUseArea()));	// 能效除以面积
						bean.setRate(new BigDecimal(bean.getTotalUse()).subtract(new BigDecimal(resource.getEnergyValue())).multiply(new BigDecimal(100)).divide(new BigDecimal(resource.getEnergyValue()), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue());  // 占比
					}
				}
				// 空调能效
				else if (queryType == 2) {
					// 获取空调温度
					Map<Long, Double> tempMap = this.getTemperature((Date)param.get("beginTime"), (Date)param.get("endTime"));
					for (LedgerStatBean bean : list) {
						bean.setRate(new BigDecimal(bean.getTotalUse()).subtract(new BigDecimal(resource.getAirValue())).multiply(new BigDecimal(100)).divide(new BigDecimal(resource.getAirValue()), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue());  // 占比
						// 设置温度 ...
						if (tempMap.containsKey(bean.getLedgerId())) {
							bean.setTemperature(NumberUtil.formatDouble(tempMap.get(bean.getLedgerId())*100/100, NumberUtil.PATTERN_DOUBLE));
						}
					}
				}
				// 分项耗电、水量需要转换标准煤 
				else if (queryType == 4) {
					// 获取标准煤转换关系 
					coalMap = this.getCoalRelateValue();
					for (LedgerStatBean bean : list) {
						if (coalMap.containsKey(bean.getTypeId() + "")) {
							bean.setCommValue(DataUtil.doubleMultiply(coalMap.get(bean.getTypeId() + "").getCoalValue(), bean.getCostValue())); // 耗费量计算标准煤
						}
					}
				}
				// 按照占比降序
				if (CollectionUtil.isNotEmpty(list) && (queryType == 1 || queryType == 2) ) {
					Collections.sort(list, new Comparator<LedgerStatBean>(){
						@Override
						public int compare(LedgerStatBean o1, LedgerStatBean o2) {
							return DataUtil.doubleSubtract(o1.getRate(), o2.getRate()) <= 0 ? 1 : -1;
						}
					});
				}
			}
			result.put("ledgers", list);
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EnergySavingRankingService#getEnergyTypes(java.util.Map)
	 */
	@Override
	public List<ProductStatBean> getEnergyTypes(Map<String, Object> param) {
		return energySavingRankingMapper.getEnergyTypes(param);
	}
	
	/**
	 * 获取标准煤转换关系
	 * @return
	 */
	private Map<String, CoalBean> getCoalRelateValue() {
		List<CoalBean> list = productsEnergyMapper.getCoalRelateValue();
		Map<String, CoalBean> map = null;
		if (CollectionUtil.isNotEmpty(list)) {
			map = new HashMap<String, CoalBean>();
			for (CoalBean bean : list) {
				map.put(bean.getTypeId() + "", bean);
			}
		}
		return map;
	}
	
	/**
	 * 查询产品产量
	 * @param beginTime 开始时间
	 * @param endTime   截止时间
	 * @return
	 */
	private Map<Long, Double> getProducts(Date beginTime, Date endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<Long, Double> resultMap = new HashMap<Long, Double>();
		param.put("queryType", 1);
		param.put("beginTime", beginTime);
		param.put("endTime",   endTime);
		List<ProductsBean> list = productsEnergyMapper.queryProductsEnergyInfo(param);
		for (ProductsBean bean : list) {
			resultMap.put(bean.getProductId(), bean.getProductValue());
		}
		return resultMap;
	}
	
	/**
	 * 获取空调温度
	 * @param beginTime 开始时间
	 * @param endTime   截止时间
	 * @return
	 */
	private Map<Long, Double> getTemperature(Date beginTime, Date endTime) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<Long, Double> resultMap = null;
		param.put("beginTime", beginTime);
		param.put("endTime",   endTime);
		List<LedgerStatBean> list = energySavingRankingMapper.queryLedgerBaseTemp(param);
		if (CollectionUtil.isNotEmpty(list)) {
			resultMap = new HashMap<Long, Double>();
			for (LedgerStatBean bean : list) {
				resultMap.put(bean.getLedgerId(), bean.getTemperature());
			}
		}
		return resultMap;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EnergySavingRankingService#getDeviceTypes()
	 */
	@Override
	public List<DeviceTypeBean> getDeviceTypes() {
		return energySavingRankingMapper.getDeviceTypes();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EnergySavingRankingService#querParticalStatData(java.util.Map)
	 */
	@Override
	public List<LedgerStatBean> queryParticalStatData(Map<String, Object> param) {
		return energySavingRankingMapper.queryParticalStatData(param);
	}
}
