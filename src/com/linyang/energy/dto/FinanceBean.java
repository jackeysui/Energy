package com.linyang.energy.dto;

import java.util.List;

import com.linyang.util.CommonMethod;

public class FinanceBean {
	
	private List<TimeFieldBean> timeList;
	private String time;
	private long assembleId;
	private long scheduleId;
	public FinanceBean() {
		super();
	}
	public List<TimeFieldBean> getTimeList() {
		return timeList;
	}
	public void setTimeList(List<TimeFieldBean> timeList) {
		this.timeList = timeList;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
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
	
	public FinanceBean buildTime(){
		if(CommonMethod.isCollectionNotEmpty(timeList)){
			for (TimeFieldBean ll : timeList) {
				ll.buildTime(time);
			}
		}
		return this;
	}
}
