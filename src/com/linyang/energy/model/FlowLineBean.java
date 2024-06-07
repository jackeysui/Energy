package com.linyang.energy.model;

import java.util.ArrayList;
import java.util.List;

import com.linyang.energy.dto.MeterBeanInfo;

public class FlowLineBean {
	
	private long assembleId;
	private long scheduleId;
	private String assembleName;
	private String remark;
	private List<Long> meterIds;
	
	private List<MeterBeanInfo> list = new ArrayList<MeterBeanInfo>();
	
	public long getAssembleId() {
		return assembleId;
	}
	public void setAssembleId(long assembleId) {
		this.assembleId = assembleId;
	}
	public long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getAssembleName() {
		return assembleName;
	}
	public void setAssembleName(String assembleName) {
		this.assembleName = assembleName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<Long> getMeterIds() {
		return meterIds;
	}
	public void setMeterIds(List<Long> meterIds) {
		this.meterIds = meterIds;
	}
	public List<MeterBeanInfo> getList() {
		return list;
	}
	public void setList(List<MeterBeanInfo> list) {
		this.list = list;
	}
	
	
}
