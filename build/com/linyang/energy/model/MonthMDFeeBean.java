package com.linyang.energy.model;

/**
 * 月需量电费计算bean
 * 
 * @author gaofeng
 * 
 */
public class MonthMDFeeBean {
	
	/**
	 * 月份
	 */
	private int month;
	
	/**
	 * 申报MD
	 */
	private Double declareMD;

	/**
	 * 月MD
	 */
	private Double monthMD;

	/**
	 * MD偏差值
	 */
	private Double mdDeviation;

	/**
	 * MD偏差率
	 */
	private Double mdDeviationRate;

	/**
	 * MD电费
	 */
	private Double mdFee;

	public Double getDeclareMD() {
		return declareMD;
	}

	public void setDeclareMD(double declareMD) {
		this.declareMD = declareMD;
	}

	public Double getMonthMD() {
		return monthMD;
	}

	public void setMonthMD(double monthMD) {
		this.monthMD = monthMD;
	}

	public Double getMdDeviation() {
		return mdDeviation;
	}

	public void setMdDeviation(double mdDeviation) {
		this.mdDeviation = mdDeviation;
	}

	public Double getMdDeviationRate() {
		return mdDeviationRate;
	}

	public void setMdDeviationRate(double mdDeviationRate) {
		this.mdDeviationRate = mdDeviationRate;
	}

	public Double getMdFee() {
		return mdFee;
	}

	public void setMdFee(double mdFee) {
		this.mdFee = mdFee;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
}
