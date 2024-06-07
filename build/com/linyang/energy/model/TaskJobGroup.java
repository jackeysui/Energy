package com.linyang.energy.model;

import java.util.Date;

/*
 * Editor Gaop
 * Date 2014-09-04
 * 任务群组
 * */
public class TaskJobGroup {
	
	/**
	 * 任务组id 
	 */
	private long groupID;
	
	/*
	 * 任务组名称
	 * */
	private String groupName;
	
	 /*
	  * 开始时间
	  * */
	 private Date startTime;
	 private String startTimeStr;
	 /*
	  * 结束时间
	  * */
	 private Date endTime;
	 private String endTimeStr;
	 
	 /*
	  * 
	  * 任务组状态,0:停止,1:运行
	  * */
	 private long groupStatus;
	 
	 
	 
	 public Long getGroupID() {
	        return groupID;
	    }
	 public void setGroupID(Long groupID) {
	        this.groupID = groupID;
	    }

	 
	 public String getGroupName()
	 {
		 return groupName; 
	 }
	 public void setGroupName(String groupName)
	 { 
		 this.groupName=groupName== null ? null : groupName.trim();
	 }
	 
	 public Date getStartTime()
	 {
		 return startTime; 
	 }
	 public void setStartTime(Date startTime)
	 {
		 this.startTime=startTime;		 
	 }
	 
	 public Date getEndTime()
	 {
		 return endTime;
	 }
	 public void setEndTime(Date endTime)
	 {
		 this.endTime=endTime;
	 }
	 
	 public long getGroupStatus()
	 {
		 return groupStatus;
	 }
	 public void setGroupStatus(long groupStatus)
	 {
		 this.groupStatus=groupStatus;
	 }
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	 
}
