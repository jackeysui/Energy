package com.linyang.energy.model;

import java.io.Serializable;

/**
 * 开机率bean
 * 
 * @author gaofeng
 * 
 */
public class StartrateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2873622116014432342L;

	private String ledgerName;

	private String meterName;

	private Double total;

	private Double rate1;

	private Double rate2;

	private Double rate3;

	private Double rate4;

	private Double startMin;

	private Double startRate;

	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getRate1() {
		return rate1;
	}

	public void setRate1(Double rate1) {
		this.rate1 = rate1;
	}

	public Double getRate2() {
		return rate2;
	}

	public void setRate2(Double rate2) {
		this.rate2 = rate2;
	}

	public Double getRate3() {
		return rate3;
	}

	public void setRate3(Double rate3) {
		this.rate3 = rate3;
	}

	public Double getRate4() {
		return rate4;
	}

	public void setRate4(Double rate4) {
		this.rate4 = rate4;
	}

	public Double getStartMin() {
		return startMin;
	}

	public void setStartMin(Double startMin) {
		this.startMin = startMin;
	}

	public Double getStartRate() {
		return startRate;
	}

	public void setStartRate(Double startRate) {
		this.startRate = startRate;
	}
}
