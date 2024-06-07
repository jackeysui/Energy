package com.linyang.energy.model;
/**
 * 流水线信息
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 20, 2014
 */
public class AssembleaBean {
	
	private long assembleId;
	
	private long scheduleId;
	
	private String assembleName;
	
	private String remark;

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
	
	

}
