package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 电能示值Bean
 * @author guosen
 * @date 2014-11-24
 */
public class ERateBean implements Serializable{
	
	private static final long serialVersionUID = -3068645076516116284L;

	private Long meterId;
	
	private Date freezeTime;

	private int rateNumber;
	
	private Double faeRate;
	
	private Double freRate;
	
	private Double baeRate;
	
	private Double breRate;
	
	private Double comRate;

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Date getFreezeTime() {
		return freezeTime;
	}

	public void setFreezeTime(Date freezeTime) {
		this.freezeTime = freezeTime;
	}

	public int getRateNumber() {
		return rateNumber;
	}

	public void setRateNumber(int rateNumber) {
		this.rateNumber = rateNumber;
	}

	public Double getFaeRate() {
		return faeRate;
	}

	public void setFaeRate(Double faeRate) {
		this.faeRate = faeRate;
	}

	public Double getFreRate() {
		return freRate;
	}

	public void setFreRate(Double freRate) {
		this.freRate = freRate;
	}

	public Double getBaeRate() {
		return baeRate;
	}

	public void setBaeRate(Double baeRate) {
		this.baeRate = baeRate;
	}

	public Double getBreRate() {
		return breRate;
	}

	public void setBreRate(Double breRate) {
		this.breRate = breRate;
	}

	public Double getComRate() {
		return comRate;
	}

	public void setComRate(Double comRate) {
		this.comRate = comRate;
	}
}
