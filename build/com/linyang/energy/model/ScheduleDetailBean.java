package com.linyang.energy.model;

/**
 * 
 * @类功能说明： 班次类
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2014-2-11 上午11:28:31  
 * @版本：V1.0
 */
public class ScheduleDetailBean {
	/**
	 * 班次ID
	 */
	private long gradeId;
	/**
	 * 班次名称
	 */
	private String gradeName;
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;
	/**
	 * 排班ID
	 */
	private long scheduleId;
	

	
	
	public long getGradeId() {
		return gradeId;
	}
	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public long getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}
}
