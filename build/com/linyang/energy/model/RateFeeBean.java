package com.linyang.energy.model;

/**
 * 电费计算
 * 
 * @author gaofeng
 * 
 */
public class RateFeeBean {

	/**
	 * id
	 */
	private String sectorId;

	/**
	 * 时段名称
	 */
	private String sectorName;

	/**
	 * 电量
	 */
	private double value;

	/**
	 * 电价
	 */
	private double price;

	/**
	 * 电费
	 */
	private double fee;
	
	private int feeId;

    public RateFeeBean() {
    }

    public RateFeeBean(String sectorId, String sectorName,int feeId, double value, double fee) {
        this.sectorId = sectorId;
        this.sectorName = sectorName;
        this.feeId = feeId;
        this.value = value;
        this.fee = fee;
    }

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public int getFeeId() {
		return feeId;
	}

	public void setFeeId(int feeId) {
		this.feeId = feeId;
	}

}
