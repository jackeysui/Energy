package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leegern.util.CollectionUtil;
import com.leegern.util.DateUtil;
import com.linyang.energy.mapping.energyanalysis.ProductsEnergyMapper;
import com.linyang.energy.model.ProductsBean;
import com.linyang.energy.service.ProductsEnergyService;import com.linyang.energy.utils.DataUtil;

/**
 * @Description 单位产品能耗Service实现
 * @author Leegern
 * @date Dec 11, 2013 11:06:15 AM
 */
@Service
public class ProductsEnergyServiceImpl implements ProductsEnergyService {
	@Autowired
	private ProductsEnergyMapper productsEnergyMapper;
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.ProductsEnergyService#queryProductsEnergyInfo(java.util.Map)
	 */
	@Override
	public Map<String, Object> queryProductsEnergyInfo(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		// productName - > item
		Map<String, ProductsBean> productsMap = new HashMap<String, ProductsBean>();
		// productName_picUrl - > typeName - > item
		Map<String, Map<String, ProductsBean>> energyMap = new HashMap<String, Map<String, ProductsBean>>();
		Date beginTime = (Date)param.get("beginTime");
		Date endTime = (Date)param.get("endTime");
		// 标准煤转换关系
//		Map<String, CoalBean> coalMap = this.getCoalRelateValue();
		ProductsBean product = null;
		double statValue1 = 0;
		double statValue2 = 0;
		double statValue3 = 0;
		double statValue4 = 0;
		
		// ------------- 计算单位产品产量 ------------ //
		// ---- 查询当前周期产品产量
		List<ProductsBean> products = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(products)) {
			// 转换成k - v结构
			for (ProductsBean bean : products) {
				productsMap.put(bean.getProductName() + "", bean);
			}
		}
		// ---- 查询同期产量
		param.put("beginTime", param.get("samePeriodBeginTime"));
		param.put("endTime",   param.get("samePeriodEndTime"));
		List<ProductsBean> samePeriodProducts = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(samePeriodProducts)) {
			for (ProductsBean bean : samePeriodProducts) {
				if (productsMap.containsKey(bean.getProductName() + "")) {
					productsMap.get(bean.getProductName() + "").setSamePeriodValue(bean.getProductValue());
				}
			}
		}
		// ---- 查询本期累计产量
		param.put("beginTime", param.get("thisPeriodTotalBeginTime"));
		param.put("endTime",   param.get("thisPeriodTotalEndTime"));
		List<ProductsBean> thisPeriodTotalProducts = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(thisPeriodTotalProducts)) {
			for (ProductsBean bean : thisPeriodTotalProducts) {
				if (productsMap.containsKey(bean.getProductName() + "")) {
					productsMap.get(bean.getProductName() + "").setGrandTotalValue(bean.getProductValue());
				}
			}
		}
		// ---- 查询同期累计产量
		param.put("beginTime", param.get("samePeriodTotalBeginTime"));
		param.put("endTime",   param.get("samePeriodTotalEndTime"));
		List<ProductsBean> samePeriodTotalProducts = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(samePeriodTotalProducts)) {
			for (ProductsBean bean : samePeriodTotalProducts) {
				if (productsMap.containsKey(bean.getProductName() + "")) {
					productsMap.get(bean.getProductName() + "").setSamePeriodTotalValue(bean.getProductValue());
				}
			}
		}
		
		// ---- 查询本期年累计产量
		param.put("beginTime", param.get("thisPeriodTotalBeginTime"));
		param.put("endTime",   DateUtil.getYearLastDay((Date)param.get("thisPeriodTotalBeginTime")));
		List<ProductsBean> thisPeriodYearProducts = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(thisPeriodYearProducts)) {
			for (ProductsBean bean : thisPeriodYearProducts) {
				if (productsMap.containsKey(bean.getProductName() + "")) {
					productsMap.get(bean.getProductName() + "").setThisYearProducts(bean.getProductValue());
				}
			}
		}
		// ---- 查询上一年年累计产量
		param.put("beginTime", param.get("samePeriodTotalBeginTime"));
		param.put("endTime",   DateUtil.getYearLastDay((Date)param.get("samePeriodTotalBeginTime")));
		List<ProductsBean> samePeriodYearProducts = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(samePeriodYearProducts)) {
			for (ProductsBean bean : samePeriodYearProducts) {
				if (productsMap.containsKey(bean.getProductName() + "")) {
					productsMap.get(bean.getProductName() + "").setLastYearProducts(bean.getProductValue());
				}
			}
		}
		
		// ------------- 计算单位产品能源消耗量 ------------ //
		// ---- 查询本周期能耗
		param.put("queryType", 2);
		param.put("beginTime", beginTime);
		param.put("endTime",   endTime);
		List<ProductsBean> energys = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(energys)) {
			for (ProductsBean bean : energys) {
				String key = bean.getProductName() + "_" + bean.getPicUrl();
				if (! energyMap.containsKey(key)) {
					energyMap.put(key, new HashMap<String, ProductsBean>());
				}
//				bean.setConsumerCoalValue(bean.getConsumerValue() * coalMap.get(bean.getTypeId() + "").getCoalValue());       // 计算本期标准煤
				bean.setConsumerCoalValue(bean.getConvertValue());
				energyMap.get(key).put(bean.getTypeName(), bean);
			}
		}
		// ---- 查询同期能耗
		param.put("beginTime", param.get("samePeriodBeginTime"));
		param.put("endTime",   param.get("samePeriodEndTime"));
		List<ProductsBean> samePeriodEnergys = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(samePeriodEnergys)) {
			for (ProductsBean bean : samePeriodEnergys) {
				String key = bean.getProductName() + "_" + bean.getPicUrl();
				if (energyMap.containsKey(key) && energyMap.get(key).containsKey(bean.getTypeName())) {
					product = energyMap.get(key).get(bean.getTypeName());
					product.setSamePeriodEnergy(bean.getConsumerValue());
//					product.setSamePeriodCoal(bean.getConsumerValue() * coalMap.get(bean.getTypeId() + "").getCoalValue()); // 计算同期标准煤
					product.setSamePeriodCoal(bean.getConvertValue());
				}
			}
		}
		// ---- 查询本期年累计能耗
		param.put("beginTime", param.get("thisPeriodTotalBeginTime"));
		param.put("endTime",   DateUtil.getYearLastDay((Date)param.get("thisPeriodTotalBeginTime")));
		List<ProductsBean> thisPeriodTotalEnergys = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(thisPeriodTotalEnergys)) {
			for (ProductsBean bean : thisPeriodTotalEnergys) {
				String key = bean.getProductName() + "_" + bean.getPicUrl();
				if (energyMap.containsKey(key) && energyMap.get(key).containsKey(bean.getTypeName())) {
					product = energyMap.get(key).get(bean.getTypeName());
					product.setGrandTotalEnergy(bean.getConsumerValue());
//					product.setGrandTotalCoal(bean.getConsumerValue() * coalMap.get(bean.getTypeId() + "").getCoalValue()); // 计算本期年累计标准煤
					product.setGrandTotalCoal(bean.getConvertValue());
				}
			}
		}
		// ---- 查询同期年累计能耗
		param.put("beginTime", param.get("samePeriodTotalBeginTime"));
		param.put("endTime",   DateUtil.getYearLastDay((Date)param.get("samePeriodTotalBeginTime")));
		List<ProductsBean> samePeriodTotalEnergys = productsEnergyMapper.queryProductsEnergyInfo(param);
		if (CollectionUtil.isNotEmpty(samePeriodTotalEnergys)) {
			for (ProductsBean bean : samePeriodTotalEnergys) {
				String key = bean.getProductName() + "_" + bean.getPicUrl();
				if (energyMap.containsKey(key) && energyMap.get(key).containsKey(bean.getTypeName())) {
					product = energyMap.get(key).get(bean.getTypeName());
					product.setSamePeriodTotalEnergy(bean.getConsumerValue()); 
//					product.setSamePeriodTotalCoal(bean.getConsumerValue() * coalMap.get(bean.getTypeId() + "").getCoalValue()); // 计算去年年累计标准煤
					product.setSamePeriodTotalCoal(bean.getConvertValue());
				}
			}
		}
		
		// ------------- 计算单位产品综合能耗 ------------ //
		if (CollectionUtil.isNotEmpty(energyMap)) {
			for (Map.Entry<String, Map<String, ProductsBean>> entry : energyMap.entrySet()) {
				String key = entry.getKey().split("_")[0];
				Map<String, ProductsBean> items = entry.getValue();
				statValue1 = 0;
				statValue2 = 0;
				statValue3 = 0;
				statValue4 = 0;
				for (Map.Entry<String, ProductsBean> item : items.entrySet()) {
					statValue1 = DataUtil.doubleAdd(statValue1, item.getValue().getConsumerCoalValue());
					statValue2 = DataUtil.doubleAdd(statValue2, item.getValue().getSamePeriodCoal());
					statValue3 = DataUtil.doubleAdd(statValue3, item.getValue().getGrandTotalCoal());
					statValue4 = DataUtil.doubleAdd(statValue4, item.getValue().getSamePeriodTotalCoal());
				}
				if (productsMap.containsKey(key)) {
					ProductsBean p = productsMap.get(key);
					if (p.getProductValue() != 0) p.setThisPeriodUnit(DataUtil.doubleDivide(statValue1, p.getProductValue(), 2));               // 本期能耗平均值
					if (p.getSamePeriodValue() != 0) p.setSamePeriodUnit(DataUtil.doubleDivide(statValue2, p.getSamePeriodValue(), 2));         // 同期能耗平均值
					if (p.getGrandTotalValue() != 0) p.setThisYearUnit(DataUtil.doubleDivide(statValue3, p.getThisYearProducts(), 2));          // 本年能耗平均值
					if (p.getSamePeriodTotalValue() != 0) p.setLastYearUnit(DataUtil.doubleDivide(statValue4, p.getLastYearProducts(), 2));     // 去年能耗平均值
				}
			}
		}
		
		result.put("products", new ArrayList<ProductsBean>(productsMap.values()));  // 产量
		result.put("energy",   energyMap);                                          // 能耗
		return result;
	}
	
//	/**
//	 * 获取标准煤转换关系
//	 * @return
//	 */
//	private Map<String, CoalBean> getCoalRelateValue() {
//		List<CoalBean> list = productsEnergyMapper.getCoalRelateValue();
//		Map<String, CoalBean> map = null;
//		if (CollectionUtil.isNotEmpty(list)) {
//			map = new HashMap<String, CoalBean>();
//			for (CoalBean bean : list) {
//				map.put(bean.getTypeId() + "", bean);
//			}
//		}
//		return map;
//	}
}
