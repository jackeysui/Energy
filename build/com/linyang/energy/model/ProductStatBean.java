package com.linyang.energy.model;

import java.util.Date;

/**
 * @Description 产品统计bean
 * @author Leegern
 * @date Feb 12, 2014 8:54:50 AM
 */
public class ProductStatBean {
	/**
	 * 产品id
	 */
	private long productId;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 单位能耗
	 */
	private double unitConsumer;
	
	/**
	 * 统计产量
	 */
	private double productValue;
	
	/**
	 * 流水线id
	 */
	private long assembleId;
	
	/**
	 * 流水线名称
	 */
	private String assembleName;
	
	/**
	 * 分析日期
	 */
	private Date analyDate;
	
	/**
	 * 能耗对标
	 */
	private double convertData;
	
	/**
	 * 综合能耗
	 */
	private double totalUse;
	
	/**
	 * 能效类型id
	 */
	private long typeId;
	
	/**
	 * 能效类型名称
	 */
	private String typeName;
	
	/**
	 * 耗费量
	 */
	private double costValue;
	
	/**
	 * 单位
	 */
	private String unit;
	
	/**
	 * 标准煤
	 */
	private double commValue;
	
	/**
	 * 分项大类Id
	 */
	private long cateId;
	
	/**
	 * 分项大类名称
	 */
	private String cateName;
	
	/**
	 * 占比
	 */
	private double rate;
	
	
	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @return the cateId
	 */
	public long getCateId() {
		return cateId;
	}

	/**
	 * @param cateId the cateId to set
	 */
	public void setCateId(long cateId) {
		this.cateId = cateId;
	}

	/**
	 * @return the cateName
	 */
	public String getCateName() {
		return cateName;
	}

	/**
	 * @param cateName the cateName to set
	 */
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	/**
	 * @return the commValue
	 */
	public double getCommValue() {
		return commValue;
	}

	/**
	 * @param commValue the commValue to set
	 */
	public void setCommValue(double commValue) {
		this.commValue = commValue;
	}

	/**
	 * @return the productId
	 */
	public long getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @return the unitConsumer
	 */
	public double getUnitConsumer() {
		return unitConsumer;
	}

	/**
	 * @param unitConsumer the unitConsumer to set
	 */
	public void setUnitConsumer(double unitConsumer) {
		this.unitConsumer = unitConsumer;
	}

	/**
	 * @return the productValue
	 */
	public double getProductValue() {
		return productValue;
	}

	/**
	 * @param productValue the productValue to set
	 */
	public void setProductValue(double productValue) {
		this.productValue = productValue;
	}

	/**
	 * @return the assembleId
	 */
	public long getAssembleId() {
		return assembleId;
	}

	/**
	 * @param assembleId the assembleId to set
	 */
	public void setAssembleId(long assembleId) {
		this.assembleId = assembleId;
	}

	/**
	 * @return the assembleName
	 */
	public String getAssembleName() {
		return assembleName;
	}

	/**
	 * @param assembleName the assembleName to set
	 */
	public void setAssembleName(String assembleName) {
		this.assembleName = assembleName;
	}

	/**
	 * @return the analyDate
	 */
	public Date getAnalyDate() {
		return analyDate;
	}

	/**
	 * @param analyDate the analyDate to set
	 */
	public void setAnalyDate(Date analyDate) {
		this.analyDate = analyDate;
	}

	/**
	 * @return the convertData
	 */
	public double getConvertData() {
		return convertData;
	}

	/**
	 * @param convertData the convertData to set
	 */
	public void setConvertData(double convertData) {
		this.convertData = convertData;
	}

	/**
	 * @return the totalUse
	 */
	public double getTotalUse() {
		return totalUse;
	}

	/**
	 * @param totalUse the totalUse to set
	 */
	public void setTotalUse(double totalUse) {
		this.totalUse = totalUse;
	}

	/**
	 * @return the typeId
	 */
	public long getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the costValue
	 */
	public double getCostValue() {
		return costValue;
	}

	/**
	 * @param costValue the costValue to set
	 */
	public void setCostValue(double costValue) {
		this.costValue = costValue;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
}
