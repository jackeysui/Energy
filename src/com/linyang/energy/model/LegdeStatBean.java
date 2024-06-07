package com.linyang.energy.model;

import java.math.BigDecimal;import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.linyang.util.DoubleUtils;

public class LegdeStatBean {
	/**
	 * 分户id
	 */
	private long legdeId;
	
	/**
	 * 分户的电费
	 */
	private Double eleNow;
	private Double elePre;
	private Double eleCostNow;
	private Double eleCostPre;
	private Double elePercentage;
	
	/**
	 * 分户的水
	 */
	private Double waterNow;
	private Double waterPre;
	private Double waterCostNow;
	private Double waterCostPre;
	private Double waterPercentage;
	
	/**
	 * 分户的气费
	 */
	private Double gasNow;
	private Double gasPre;
	private Double gasCostNow;
	private Double gasCostPre;
	private Double gasPercentage;
	
	/**
	 * 费率数据
	 */
	List<Map<String,Object>> rateList = new ArrayList<Map<String,Object>>();
	
	public List<Map<String, Object>> getRateList() {
		return rateList;
	}
	
	public LegdeStatBean() {
		super();
	}

	public LegdeStatBean(long legdeId) {
		super();
		this.legdeId = legdeId;
	}
	public long getLegdeId() {
		return legdeId;
	}
	public Double getEleNow() {
		return eleNow;
	}
	public void setEleNow(Double eleNow) {
		this.eleNow = eleNow;
	}
	public Double getElePre() {
		return elePre;
	}
	public void setElePre(Double elePre) {
		this.elePre = elePre;
	}
	public Double getElePercentage() {
		return elePercentage;
	}
	public void setElePercentage(Double elePercentage) {
		this.elePercentage = elePercentage;
	}
	public Double getWaterNow() {
		return waterNow;
	}
	public void setWaterNow(Double waterNow) {
		this.waterNow = waterNow;
	}
	public Double getWaterPre() {
		return waterPre;
	}
	public void setWaterPre(Double waterPre) {
		this.waterPre = waterPre;
	}
	public Double getWaterPercentage() {
		return waterPercentage;
	}
	public void setWaterPercentage(Double waterPercentage) {
		this.waterPercentage = waterPercentage;
	}
	public Double getGasNow() {
		return gasNow;
	}
	public void setGasNow(Double gasNow) {
		this.gasNow = gasNow;
	}
	public Double getGasPre() {
		return gasPre;
	}
	public void setGasPre(Double gasPre) {
		this.gasPre = gasPre;
	}
	public Double getGasPercentage() {
		return gasPercentage;
	}
	public void setGasPercentage(Double gasPercentage) {
		this.gasPercentage = gasPercentage;
	}
	public void setLegdeId(long legdeId) {
		this.legdeId = legdeId;
	}
	
	
	public Double getEleCostNow() {
		return eleCostNow;
	}
	public void setEleCostNow(Double eleCostNow) {
		this.eleCostNow = eleCostNow;
	}
	public Double getWaterCostNow() {
		return waterCostNow;
	}
	public void setWaterCostNow(Double waterCostNow) {
		this.waterCostNow = waterCostNow;
	}
	public Double getGasCostNow() {
		return gasCostNow;
	}
	public void setGasCostNow(Double gasCostNow) {
		this.gasCostNow = gasCostNow;
	}
	
	public Double getEleCostPre() {
		return eleCostPre;
	}
	public void setEleCostPre(Double eleCostPre) {
		this.eleCostPre = eleCostPre;
	}
	public Double getWaterCostPre() {
		return waterCostPre;
	}
	public void setWaterCostPre(Double waterCostPre) {
		this.waterCostPre = waterCostPre;
	}
	public Double getGasCostPre() {
		return gasCostPre;
	}
	public void setGasCostPre(Double gasCostPre) {
		this.gasCostPre = gasCostPre;
	}
	
	
	public void setRateList(List<Map<String, Object>> rateList) {
		this.rateList = rateList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (legdeId ^ (legdeId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LegdeStatBean other = (LegdeStatBean) obj;
		if (legdeId != other.legdeId)
			return false;
		return true;
	}
	/**
	 * 计算百分比
	 */
	public void calculatePercentage(){
		if(waterNow != null && waterPre != null)
			this.waterPercentage = new BigDecimal(waterNow).subtract(new BigDecimal(waterPre)).multiply(new BigDecimal(100)).divide(new BigDecimal(waterPre), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(eleNow != null && elePre != null)
			this.elePercentage = new BigDecimal(eleNow).subtract(new BigDecimal(elePre)).multiply(new BigDecimal(100)).divide(new BigDecimal(elePre), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(gasNow != null && gasPre != null)
			this.gasPercentage = new BigDecimal(gasNow).subtract(new BigDecimal(gasPre)).multiply(new BigDecimal(100)).divide(new BigDecimal(gasPre), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	
	/**
	 * 计算花费百分比
	 */
	public void calculateCostPercentage(){
		if(waterCostNow != null && waterCostPre != null)
			this.waterPercentage = new BigDecimal(waterCostNow).subtract(new BigDecimal(waterCostPre)).multiply(new BigDecimal(100)).divide(new BigDecimal(waterCostPre), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(eleCostNow != null && eleCostPre != null)
			this.elePercentage = new BigDecimal(eleCostNow).subtract(new BigDecimal(eleCostPre)).multiply(new BigDecimal(100)).divide(new BigDecimal(eleCostPre), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		if(gasCostNow != null && gasCostPre != null)
			this.gasPercentage = new BigDecimal(gasCostNow).subtract(new BigDecimal(gasCostPre)).multiply(new BigDecimal(100)).divide(new BigDecimal(gasCostPre), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	
}
