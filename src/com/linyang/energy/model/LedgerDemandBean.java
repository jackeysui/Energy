package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 分户需量
 * @author Administrator
 *
 */
public class LedgerDemandBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long ledgerId;
	
	private int dataType;
	
	/**
	 * 数据类型：1-上月,2-本月,3-上月最大发生时间,4-本月最大发生时间
	 */
	public static final int lastMonPwr = 1;
	
	/**
	 * 数据类型：1-上月,2-本月,3-上月最大发生时间,4-本月最大发生时间
	 */
	public static final int currMonPwr = 2;
	
	/**
	 * 数据类型：1-上月,2-本月,3-上月最大发生时间,4-本月最大发生时间
	 */
	public static final int lastMonPwrDate = 3;
	
	/**
	 * 数据类型：1-上月,2-本月,3-上月最大发生时间,4-本月最大发生时间
	 */
	public static final int currMonPwrDate = 4;
	
	
	private String day;
	
	private Double maxAP;
	
	private Date occurredTime;
	
	private Date flagTime;

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Double getMaxAP() {
		return maxAP;
	}

	public void setMaxAP(Double maxAP) {
		this.maxAP = maxAP;
	}

	public Date getOccurredTime() {
		return occurredTime;
	}

	public void setOccurredTime(Date occurredTime) {
		this.occurredTime = occurredTime;
	}

	public Date getFlagTime() {
		return flagTime;
	}

	public void setFlagTime(Date flagTime) {
		this.flagTime = flagTime;
	}
	
	

}
