package com.linyang.energy.model;

import java.util.Date;

/**
 * @Description 电费bean
 * @author Leegern
 * @date Jan 2, 2014 11:02:03 AM
 */
public class ElecBillBean {
	/**
	 * 分户Id
	 */
	private long ledgerId;
	
	/**
	 * 统计日期
	 */
	private Date statDate;
	
	/**
	 * 费用值
	 */
	private Double feeValue;

	
	/**
	 * @return the ledgerId
	 */
	public long getLedgerId() {
		return ledgerId;
	}

	/**
	 * @param ledgerId the ledgerId to set
	 */
	public void setLedgerId(long ledgerId) {
		this.ledgerId = ledgerId;
	}

	/**
	 * @return the statDate
	 */
	public Date getStatDate() {
		return statDate;
	}

	/**
	 * @param statDate the statDate to set
	 */
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}

	/**
	 * @return the feeValue
	 */
	public Double getFeeValue() {
		return feeValue;
	}

	/**
	 * @param feeValue the feeValue to set
	 */
	public void setFeeValue(Double feeValue) {
		this.feeValue = feeValue;
	}
}
