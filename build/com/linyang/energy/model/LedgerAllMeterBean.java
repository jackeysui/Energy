package com.linyang.energy.model;

import java.util.List;

/**
 * 分户及其所有直接计量点信息
 * 
 * @author gaof
 * 
 */
public class LedgerAllMeterBean {
	private long ledgerId;

	private long parentLedgerId;

	private List<LedgerMeterBean> meters;

	public long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public long getParentLedgerId() {
		return parentLedgerId;
	}

	public void setParentLedgerId(long parentLedgerId) {
		this.parentLedgerId = parentLedgerId;
	}

	public List<LedgerMeterBean> getMeters() {
		return meters;
	}

	public void setMeters(List<LedgerMeterBean> meters) {
		this.meters = meters;
	}
}
