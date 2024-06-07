package com.linyang.energy.model;

import java.util.List;

/**
 * 分户及其所有直接计量点信息
 * 
 * @author gaof
 * 
 */
public class LedgerDeviceAllMeterBean {
	private long bigLedgerId;

	private List<LedgerAllMeterBean> allDevices;

	public long getBigLedgerId() {
		return bigLedgerId;
	}

	public void setBigLedgerId(long bigLedgerId) {
		this.bigLedgerId = bigLedgerId;
	}

	public List<LedgerAllMeterBean> getAllDevices() {
		return allDevices;
	}

	public void setAllDevices(List<LedgerAllMeterBean> allDevices) {
		this.allDevices = allDevices;
	}
}
