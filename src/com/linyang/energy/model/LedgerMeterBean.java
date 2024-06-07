package com.linyang.energy.model;

/**
 * 分户下的一个直接计量点信息
 * 
 * @author gaof
 * 
 */
public class LedgerMeterBean {
	private long meterId;

	private int meterAttr;

	private int meterType;

	public long getMeterId() {
		return meterId;
	}

	public void setMeterId(long meterId) {
		this.meterId = meterId;
	}

	public int getMeterAttr() {
		return meterAttr;
	}

	public void setMeterAttr(int meterAttr) {
		this.meterAttr = meterAttr;
	}

	public int getMeterType() {
		return meterType;
	}

	public void setMeterType(int meterType) {
		this.meterType = meterType;
	}
}
