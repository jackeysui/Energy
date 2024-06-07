package com.linyang.energy.model;

/**
 * @Description 标准煤bean
 * @author Leegern
 * @date Feb 10, 2014 1:49:07 PM
 */
public class CoalBean {
	
	private long typeId;
	
	private double coalValue;
	
	
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
	 * @return the coalValue
	 */
	public double getCoalValue() {
		return coalValue;
	}

	/**
	 * @param coalValue the coalValue to set
	 */
	public void setCoalValue(double coalValue) {
		this.coalValue = coalValue;
	}
	
}
