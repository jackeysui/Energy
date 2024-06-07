package com.linyang.energy.model;

import java.io.Serializable;

/**
 * 费率时段明细表
 * @author guosen
 * @date 2014-12-17
 */
public class RateSectorContentBean implements Serializable{
	
	private static final long serialVersionUID = 8192097329056756849L;

	/**
	 * 记录ID
	 */
	private long contentId;
	
	/**
	 * 费率ID
	 */
	private long rateId;
	
	/**
	 * 费率时段ID
	 */
	private long sectorId;
	
	/**
	 * 开始时间
	 */
	private String beginTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 时段名称
	 */
	private String sectorName;
	
	public long getContentId() {
		return contentId;
	}

	public void setContentId(long contentId) {
		this.contentId = contentId;
	}

	public long getRateId() {
		return rateId;
	}

	public void setRateId(long rateId) {
		this.rateId = rateId;
	}

	public long getSectorId() {
		return sectorId;
	}

	public void setSectorId(long sectorId) {
		this.sectorId = sectorId;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
}
