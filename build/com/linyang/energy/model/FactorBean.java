package com.linyang.energy.model;

import com.linyang.util.DoubleUtils;

public class FactorBean {
	/**
	 * 有功功率
	 */
	private Double totalFaq;
	/**
	 * 无功功率
	 */
	private Double totalFrq;
	/**
	 * 标准cosφ
	 */
	private Double  factor;
	/**
	 * 上月电费
	 */
	private Double qcost;
	/**
	 * 功率因数
	 */
	private Double powerFactor;
	 /**
	  * 幅度
	  */
	private Double rate;
	

	public Double getTotalFaq() {
		return totalFaq;
	}

	public void setTotalFaq(Double totalFaq) {
		if(totalFaq != null)
			this.totalFaq =DoubleUtils.getDoubleValue(totalFaq, 2);
	}

	public Double getTotalFrq() {
		return totalFrq;
	}

	public void setTotalFrq(Double totalFrq) {
		if(totalFrq != null)
			this.totalFrq = DoubleUtils.getDoubleValue(totalFrq, 2);
	}

	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		if(factor != null)
			this.factor = DoubleUtils.getDoubleValue(factor, 2);
	}

	public Double getQcost() {
		return qcost;
	}

	public void setQcost(Double qcost) {
		if(qcost != null)
			this.qcost = DoubleUtils.getDoubleValue(qcost, 2);
	}

	public Double getPowerFactor() {
		return powerFactor;
	}

	public void setPowerFactor(Double powerFactor) {
		if(powerFactor != null)
			this.powerFactor = DoubleUtils.getDoubleValue(powerFactor, 2);
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		if(rate != null)
			this.rate = DoubleUtils.getDoubleValue(rate, 2);
	}
	
	

}
