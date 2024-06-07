package com.linyang.energy.model;

import java.util.Date;

/**
 * @Description 产品能耗bean
 * @author Leegern
 * @date Feb 8, 2014 2:22:46 PM
 */
public class ProductsBean {
	/**
	 * 产品id
	 */
	private long productId;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 图片id
	 */
	private long picId;
	
	/**
	 * 计量单位id
	 */
	private long measureUnitId;
	
	/**
	 * 计量名称
	 */
	private String unitName;
	
	/**
	 * 单位能耗
	 */
	private double unitConsumer;
	
	/**
	 * 图片名称
	 */
	private String picName;
	
	/**
	 * 图片地址
	 */
	private String picUrl;
	
	/**
	 * 统计日期
	 */
	private Date statDate;
	
	/**
	 * 统计产量(本期)
	 */
	private double productValue;
	
	/**
	 * 能效类型id
	 */
	private long typeId;
	
	/**
	 * 能效类型名称
	 */
	private String typeName;
	
	/**
	 * 耗能量
	 */
	private double consumerValue;
	
	/**
	 * 折标量
	 */
	private double convertValue;
	
	/**
	 * 明细名称
	 */
	private String detailName;

	/**
	 * 同期产量
	 */
	private double samePeriodValue;
	
	/**
	 * 本期累计产量
	 */
	private double grandTotalValue;
	
	/**
	 * 同期累计产量
	 */
	private double samePeriodTotalValue;
	
	/**
	 * 同期能耗
	 */
	private double samePeriodEnergy;
	
	/**
	 * 本期累计能耗
	 */
	private double grandTotalEnergy;
	
	/**
	 * 同期累计能耗
	 */
	private double samePeriodTotalEnergy; 
	
	/**
	 * 本期标准煤
	 */
	private double consumerCoalValue;
	
	/**
	 * 同期标准煤
	 */
	private double samePeriodCoal;
	
	/**
	 * 本周期年累计标准煤
	 */
	private double grandTotalCoal;
	
	/**
	 * 去年同期年累计标准煤
	 */
	private double samePeriodTotalCoal; 
	
	/**
	 * 本期能耗平均值
	 */
	private double thisPeriodUnit;
	
	/**
	 * 同期能耗平均值
	 */
	private double samePeriodUnit;
	
	/**
	 * 本年能耗平均值
	 */
	private double thisYearUnit;
	
	/**
	 * 上年能耗平均值
	 */
	private double lastYearUnit;
	
	/**
	 * 本期年总产量
	 */
	private double thisYearProducts;
	
	/**
	 * 同期年总产量
	 */
	private double lastYearProducts;

	
	/**
	 * @return the thisYearProducts
	 */
	public double getThisYearProducts() {
		return thisYearProducts;
	}

	/**
	 * @param thisYearProducts the thisYearProducts to set
	 */
	public void setThisYearProducts(double thisYearProducts) {
		this.thisYearProducts = thisYearProducts;
	}

	/**
	 * @return the lastYearProducts
	 */
	public double getLastYearProducts() {
		return lastYearProducts;
	}

	/**
	 * @param lastYearProducts the lastYearProducts to set
	 */
	public void setLastYearProducts(double lastYearProducts) {
		this.lastYearProducts = lastYearProducts;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the consumerCoalValue
	 */
	public double getConsumerCoalValue() {
		return consumerCoalValue;
	}

	/**
	 * @param consumerCoalValue the consumerCoalValue to set
	 */
	public void setConsumerCoalValue(double consumerCoalValue) {
		this.consumerCoalValue = consumerCoalValue;
	}

	/**
	 * @return the samePeriodCoal
	 */
	public double getSamePeriodCoal() {
		return samePeriodCoal;
	}

	/**
	 * @param samePeriodCoal the samePeriodCoal to set
	 */
	public void setSamePeriodCoal(double samePeriodCoal) {
		this.samePeriodCoal = samePeriodCoal;
	}

	/**
	 * @return the grandTotalCoal
	 */
	public double getGrandTotalCoal() {
		return grandTotalCoal;
	}

	/**
	 * @param grandTotalCoal the grandTotalCoal to set
	 */
	public void setGrandTotalCoal(double grandTotalCoal) {
		this.grandTotalCoal = grandTotalCoal;
	}

	/**
	 * @return the samePeriodTotalCoal
	 */
	public double getSamePeriodTotalCoal() {
		return samePeriodTotalCoal;
	}

	/**
	 * @param samePeriodTotalCoal the samePeriodTotalCoal to set
	 */
	public void setSamePeriodTotalCoal(double samePeriodTotalCoal) {
		this.samePeriodTotalCoal = samePeriodTotalCoal;
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
	 * @return the picId
	 */
	public long getPicId() {
		return picId;
	}

	/**
	 * @param picId the picId to set
	 */
	public void setPicId(long picId) {
		this.picId = picId;
	}

	/**
	 * @return the measureUnitId
	 */
	public long getMeasureUnitId() {
		return measureUnitId;
	}

	/**
	 * @param measureUnitId the measureUnitId to set
	 */
	public void setMeasureUnitId(long measureUnitId) {
		this.measureUnitId = measureUnitId;
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
	 * @return the picName
	 */
	public String getPicName() {
		return picName;
	}

	/**
	 * @param picName the picName to set
	 */
	public void setPicName(String picName) {
		this.picName = picName;
	}

	/**
	 * @return the picUrl
	 */
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * @param picUrl the picUrl to set
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	/**
	 * @return the statDate
	 */
	public Date getStatDate() {
		return statDate;
	}

	/**
	 * @param statDate the statDate to set
	 */
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
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
	 * @return the consumerValue
	 */
	public double getConsumerValue() {
		return consumerValue;
	}

	/**
	 * @param consumerValue the consumerValue to set
	 */
	public void setConsumerValue(double consumerValue) {
		this.consumerValue = consumerValue;
	}

	/**
	 * @return the convertValue
	 */
	public double getConvertValue() {
		return convertValue;
	}

	/**
	 * @param convertValue the convertValue to set
	 */
	public void setConvertValue(double convertValue) {
		this.convertValue = convertValue;
	}

	/**
	 * @return the detailName
	 */
	public String getDetailName() {
		return detailName;
	}

	/**
	 * @param detailName the detailName to set
	 */
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	/**
	 * @return the samePeriodValue
	 */
	public double getSamePeriodValue() {
		return samePeriodValue;
	}

	/**
	 * @param samePeriodValue the samePeriodValue to set
	 */
	public void setSamePeriodValue(double samePeriodValue) {
		this.samePeriodValue = samePeriodValue;
	}

	/**
	 * @return the grandTotalValue
	 */
	public double getGrandTotalValue() {
		return grandTotalValue;
	}

	/**
	 * @param grandTotalValue the grandTotalValue to set
	 */
	public void setGrandTotalValue(double grandTotalValue) {
		this.grandTotalValue = grandTotalValue;
	}

	/**
	 * @return the samePeriodTotalValue
	 */
	public double getSamePeriodTotalValue() {
		return samePeriodTotalValue;
	}

	/**
	 * @param samePeriodTotalValue the samePeriodTotalValue to set
	 */
	public void setSamePeriodTotalValue(double samePeriodTotalValue) {
		this.samePeriodTotalValue = samePeriodTotalValue;
	}

	/**
	 * @return the samePeriodEnergy
	 */
	public double getSamePeriodEnergy() {
		return samePeriodEnergy;
	}

	/**
	 * @param samePeriodEnergy the samePeriodEnergy to set
	 */
	public void setSamePeriodEnergy(double samePeriodEnergy) {
		this.samePeriodEnergy = samePeriodEnergy;
	}

	/**
	 * @return the grandTotalEnergy
	 */
	public double getGrandTotalEnergy() {
		return grandTotalEnergy;
	}

	/**
	 * @param grandTotalEnergy the grandTotalEnergy to set
	 */
	public void setGrandTotalEnergy(double grandTotalEnergy) {
		this.grandTotalEnergy = grandTotalEnergy;
	}

	/**
	 * @return the samePeriodTotalEnergy
	 */
	public double getSamePeriodTotalEnergy() {
		return samePeriodTotalEnergy;
	}

	/**
	 * @param samePeriodTotalEnergy the samePeriodTotalEnergy to set
	 */
	public void setSamePeriodTotalEnergy(double samePeriodTotalEnergy) {
		this.samePeriodTotalEnergy = samePeriodTotalEnergy;
	}

	/**
	 * @return the thisPeriodUnit
	 */
	public double getThisPeriodUnit() {
		return thisPeriodUnit;
	}

	/**
	 * @param thisPeriodUnit the thisPeriodUnit to set
	 */
	public void setThisPeriodUnit(double thisPeriodUnit) {
		this.thisPeriodUnit = thisPeriodUnit;
	}

	/**
	 * @return the samePeriodUnit
	 */
	public double getSamePeriodUnit() {
		return samePeriodUnit;
	}

	/**
	 * @param samePeriodUnit the samePeriodUnit to set
	 */
	public void setSamePeriodUnit(double samePeriodUnit) {
		this.samePeriodUnit = samePeriodUnit;
	}

	/**
	 * @return the thisYearUnit
	 */
	public double getThisYearUnit() {
		return thisYearUnit;
	}

	/**
	 * @param thisYearUnit the thisYearUnit to set
	 */
	public void setThisYearUnit(double thisYearUnit) {
		this.thisYearUnit = thisYearUnit;
	}

	/**
	 * @return the lastYearUnit
	 */
	public double getLastYearUnit() {
		return lastYearUnit;
	}

	/**
	 * @param lastYearUnit the lastYearUnit to set
	 */
	public void setLastYearUnit(double lastYearUnit) {
		this.lastYearUnit = lastYearUnit;
	}
}
