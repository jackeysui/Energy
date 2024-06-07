package com.linyang.energy.model;

import java.util.Date;

/**
 * @Description 建筑统计bean
 * @author Leegern
 * @date Feb 12, 2014 8:54:50 AM
 */
public class LedgerStatBean {
	/**
	 * 分户id
	 */
	private long ledgerId;
	
	/**
	 * 分户名称
	 */
	private String ledgerName;
	
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
	 * 占比值
	 */
	private double rate;
	
	/**
	 * 温度
	 */
	private double temperature;
	
	/**
	 * 面积
	 */
	private double useArea;
	
	
	/**
	 * @return the useArea
	 */
	public double getUseArea() {
		return useArea;
	}

	/**
	 * @param useArea the useArea to set
	 */
	public void setUseArea(double useArea) {
		this.useArea = useArea;
	}

	/**
	 * @return the ledgerId
	 */
	public long getLedgerId() {
		return ledgerId;
	}

	/**
	 * @param ledgerId the ledgerId to set
	 */
	public void setLedgerId(long ledgerId) {
		this.ledgerId = ledgerId;
	}

	/**
	 * @return the ledgerName
	 */
	public String getLedgerName() {
		return ledgerName;
	}

	/**
	 * @param ledgerName the ledgerName to set
	 */
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
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
	 * @return the temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
}
