package com.linyang.energy.model;

import java.io.Serializable;

public class WorkshopMeterBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long workshopId;
	
	private Long meterId;
	
	private Integer meterType;
	
	private Integer addAttr;
	
	private Integer pct;

	public Long getWorkshopId() {
		return workshopId;
	}

	public void setWorkshopId(Long workshopId) {
		this.workshopId = workshopId;
	}

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Integer getMeterType() {
		return meterType;
	}

	public void setMeterType(Integer meterType) {
		this.meterType = meterType;
	}

	public Integer getAddAttr() {
		return addAttr;
	}

	public void setAddAttr(Integer addAttr) {
		this.addAttr = addAttr;
	}

	public Integer getPct() {
		return pct;
	}

	public void setPct(Integer pct) {
		this.pct = pct;
	}
	
}
