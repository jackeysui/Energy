package com.linyang.energy.model;

/**
 * 产品配置表
 * @author Administrator
 *
 */
public class ProductConfigBean {
	
	/**
	 * 分户id
	 */
	private Long ledgerId;
	
	/**
	 * 产品id
	 */
	private Long productId;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 单位,跟静态初始数据表关联
	 */
	private String productUnit;
	
	/**
	 * 折算系数(百分制)
	 */
	private Integer convertRatio;
	
	/**
	 * 对标（参考）单耗
	 */
	private Double unitConsumption;


	
	
	
	
	public long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public int getConvertRatio() {
		return convertRatio;
	}

	public void setConvertRatio(int convertRatio) {
		this.convertRatio = convertRatio;
	}

	public Double getUnitConsumption() {
		return unitConsumption;
	}

	public void setUnitConsumption(Double unitConsumption) {
		this.unitConsumption = unitConsumption;
	}
}
