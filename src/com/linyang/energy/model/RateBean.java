package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 费率实体Bean
 * @author Leegern
 * @date Dec 10, 2013 10:22:11 AM
 */
public class RateBean implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -5939152485186738086L;
	
	/**
	 * 费率id
	 */
	private long rateId;
	
	/**
	 * 费率名称
	 */
	private String rateName;
	
	/**
	 * 有效日期起始时间
	 */
	private Date endEffectDate;
	
	/**
	 * 有效日期结束时间
	 */
	private Date beginEffectDate;
	
	/**
	 * 费率描述
	 */
	private String rateRemark;
	
	/**
	 * 容量电价
	 */
	private Double volumeRate;
	
	/**
	 * 需量电价
	 */
	private Double demandRate;
	
	/**
	 * 需量阀值
	 */
	private Double demandThres;
	
	/**
	 * 费率个数
	 */
	private Integer rateNumber;

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("rateId:").append(rateId).append(", rateName:").append(rateName).append(", beginEffectDate:").append(beginEffectDate)
				 .append(", endEffectDate:").append(endEffectDate).append(", rateRemark:").append(rateRemark).toString();
	}

	/**
	 * @return the rateId
	 */
	public long getRateId() {
		return rateId;
	}

	/**
	 * @param rateId the rateId to set
	 */
	public void setRateId(long rateId) {
		this.rateId = rateId;
	}

	/**
	 * @return the rateName
	 */
	public String getRateName() {
		return rateName;
	}

	/**
	 * @param rateName the rateName to set
	 */
	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	/**
	 * @return the endEffectDate
	 */
	public Date getEndEffectDate() {
		return endEffectDate;
	}

	/**
	 * @param endEffectDate the endEffectDate to set
	 */
	public void setEndEffectDate(Date endEffectDate) {
		this.endEffectDate = endEffectDate;
	}

	/**
	 * @return the beginEffectDate
	 */
	public Date getBeginEffectDate() {
		return beginEffectDate;
	}

	/**
	 * @param beginEffectDate the beginEffectDate to set
	 */
	public void setBeginEffectDate(Date beginEffectDate) {
		this.beginEffectDate = beginEffectDate;
	}

	/**
	 * @return the rateRemark
	 */
	public String getRateRemark() {
		return rateRemark;
	}

	/**
	 * @param rateRemark the rateRemark to set
	 */
	public void setRateRemark(String rateRemark) {
		this.rateRemark = rateRemark;
	}

	public Double getVolumeRate() {
		return volumeRate;
	}

	public void setVolumeRate(Double volumeRate) {
		this.volumeRate = volumeRate;
	}

	public Double getDemandRate() {
		return demandRate;
	}

	public void setDemandRate(Double demandRate) {
		this.demandRate = demandRate;
	}

	public Double getDemandThres() {
		return demandThres;
	}

	public void setDemandThres(Double demandThres) {
		this.demandThres = demandThres;
	}

	public Integer getRateNumber() {
		return rateNumber;
	}

	public void setRateNumber(Integer rateNumber) {
		this.rateNumber = rateNumber;
	}
}
