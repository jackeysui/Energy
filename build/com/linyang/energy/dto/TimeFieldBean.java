package com.linyang.energy.dto;

import com.linyang.util.DateUtils;

public class TimeFieldBean{
	
	private long gradeId;
	private String startTime;
	private String endTime;
	private String gradeName;
	
	private long longTime1;
	
	private long longTime2;
	
	public TimeFieldBean() {
		super();
	}
	public long getGradeId() {
		return gradeId;
	}
	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
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
	public long getLongTime1() {
		return longTime1;
	}
	public long getLongTime2() {
		return longTime2;
	}
	
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public void buildTime(String time){
		 longTime1 =DateUtils.convertTimeToLong(time +" "+startTime, "yyyy-MM-dd HH:mm");
		 longTime2 =DateUtils.convertTimeToLong(time +" "+endTime, "yyyy-MM-dd HH:mm");
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (gradeId ^ (gradeId >>> 32));
		result = prime * result + (int) (longTime1 ^ (longTime1 >>> 32));
		result = prime * result + (int) (longTime2 ^ (longTime2 >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TimeFieldBean other = (TimeFieldBean) obj;
		if (gradeId != other.gradeId)
			return false;
		if (longTime1 != other.longTime1)
			return false;
		if (longTime2 != other.longTime2)
			return false;
		return true;
	}
	
	

}
