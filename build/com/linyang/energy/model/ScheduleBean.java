package com.linyang.energy.model;

public class ScheduleBean {
	/**
	 * 排班Id
	 */
	private long scheduleId;
	/**
	 * 排班名称
	 */
	private String scheduleName;
	/**
	 * 描述
	 */
	private String remark;
	/**
	 * 存放相关的配置的班次信息
	 */
	private String scheduleData;
	
	

	
	
	
	public String getScheduleData() {
		return scheduleData;
	}

	public void setScheduleData(String scheduleData) {
		this.scheduleData = scheduleData;
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
