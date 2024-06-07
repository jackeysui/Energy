package com.linyang.energy.model;

public class PowerInputBean {
	/**
	 * 计划Id
	 */
	private long planId;
	/**
	 * 能耗ID
	 */
	private long typeId;
	/**
	 * 能耗值
	 */
	private double typeValue;
	/**
	 * 单位ID
	 */
	private long unitId;
	/**
	 * 单位名称
	 */
	private String unitName;
	/**
	 * 能耗名称
	 */
	private String typeName;
	

	
	
	
	public long getPlanId() {
		return planId;
	}
	public void setPlanId(long planId) {
		this.planId = planId;
	}
	public long getTypeId() {
		return typeId;
	}
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	public double getTypeValue() {
		return typeValue;
	}
	public void setTypeValue(double typeValue) {
		this.typeValue = typeValue;
	}
	public long getUnitId() {
		return unitId;
	}
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
