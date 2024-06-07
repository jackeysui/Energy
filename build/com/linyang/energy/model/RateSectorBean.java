package com.linyang.energy.model;

/**
 * 
 * @类功能说明： 费率配置类
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2014-2-12 下午02:19:03  
 * @版本：V1.0
 */
public class RateSectorBean {
	/**
	 * 时段Id
	 */
	private long sectorId;
	/**
	 * 费率值
	 */
	private double rateValue;
	/**
	 * 费率Id
	 */
	private long rateId;
	/**
	 * 时段名称
	 */
	private String sectorName;
	

	
	
	public long getSectorId() {
		return sectorId;
	}
	public void setSectorId(long sectorId) {
		this.sectorId = sectorId;
	}
	public double getRateValue() {
		return rateValue;
	}
	public void setRateValue(double rateValue) {
		this.rateValue = rateValue;
	}
	public long getRateId() {
		return rateId;
	}
	public void setRateId(long rateId) {
		this.rateId = rateId;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
}
