package com.linyang.energy.model;

import java.util.List;

/**
 * 线损bean
 * 
 * @author gaofeng
 * 
 */
public class LineLossBean {
	/**
	 * 表计ID
	 */
	private long meterId;

	/**
	 * 表计名称
	 */
	private String meterName;

	/**
	 * 电量
	 */
	private double coul;

	/**
	 * 用电属性 （1、用电；2、发电；3、自发自用）
	 */
	private int attributeId;

	/**
	 * 线损末端表
	 */
	private List<LineLossBean> childMeters;

	public long getMeterId() {
		return meterId;
	}

	public void setMeterId(long meterId) {
		this.meterId = meterId;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public double getCoul() {
		return coul;
	}

	public void setCoul(double coul) {
		this.coul = coul;
	}

	public List<LineLossBean> getChildMeters() {
		return childMeters;
	}

	public void setChildMeters(List<LineLossBean> childMeters) {
		this.childMeters = childMeters;
	}

	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}
}
