package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 事件记录Bean
 * @author Leegern
 * @date Dec 16, 2013 3:39:35 PM
 */
public class EventBean implements Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -1438371833841820774L;
	
	/**
	 * 事件记录编号
	 */
	private long eventRecId;
	
	/**
	 * 事件开始时间
	 */
	private Date eventStartTime;
	
	/**
	 * 事件开始时间字符串类型
	 */
	private String eventStartTimeStr;

	/**
	 * 事件名称
	 */
	private String eventName;
    
    /**
	 * 所属企业
	 */
	private String companyName;
	
	/**
	 * 事件类型
	 */
	private long eventId;
	
	/**
	 * 事件状态(0 恢复, 1 发生)
	 */
	private int eventStatus = -1;
	
	/**
	 * 对象id
	 */
	private long objectId;
	
	/**
	 * 对象类型, 1:分户, 2:计量点
	 */
	private int objectType;
	
	/**
	 * 处理结果
	 */
	private int dealResult;
	
	/**
	 * 重复次数
	 */
	private int refreshCount;
	
	/**
	 * 对象名称
	 */
	private String objectName;
	
	
	/***
	 * 事件内容
	 * @author Yaojiawei
	 * @date 2014-08-26
	 */
	private String eventContent;
	
	/**
	 * 事件结束时间
	 * @author Yaojiawei
	 * @date 2014-08-26
	 * */
	private Date eventEndTime;

    /**
     * 事件结束时间字符串类型
     */
    private String eventEndTimeStr;
	
	/**
	 * 功率因数越限最后一个点的时间
	 * @author qwt
	 * @date 2014-09-29
	 * */
	private Date pfEndTime;


    public EventBean(){

    }

    public EventBean(Date eventStartTime,String eventName,long eventId,int objectType,long objectId){
        this.eventStartTime = eventStartTime;
        this.eventName = eventName;
        this.eventId = eventId;
        this.objectType = objectType;
        this.objectId = objectId;
    }

	
	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return
	 * @see java.lang.String#toString()
	 */
	public String toString() {
		return super.toString();
	}
	
	/**
	 * @return the eventRecId
	 */
	public long getEventRecId() {
		return eventRecId;
	}

	/**
	 * @param eventRecId the eventRecId to set
	 */
	public void setEventRecId(long eventRecId) {
		this.eventRecId = eventRecId;
	}

	/**
	 * @return the eventStartTime
	 */
	public Date getEventStartTime() {
		return eventStartTime;
	}

	/**
	 * @param eventStartTime the eventStartTime to set
	 */
	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the eventId
	 */
	public long getEventId() {
		return eventId;
	}

	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the eventStatus
	 */
	public int getEventStatus() {
		return eventStatus;
	}

	/**
	 * @param eventStatus the eventStatus to set
	 */
	public void setEventStatus(int eventStatus) {
		this.eventStatus = eventStatus;
	}

	/**
	 * @return the objectId
	 */
	public long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the objectType
	 */
	public int getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return the dealResult
	 */
	public int getDealResult() {
		return dealResult;
	}

	/**
	 * @param dealResult the dealResult to set
	 */
	public void setDealResult(int dealResult) {
		this.dealResult = dealResult;
	}

	/**
	 * @return the refreshCount
	 */
	public int getRefreshCount() {
		return refreshCount;
	}

	/**
	 * @param refreshCount the refreshCount to set
	 */
	public void setRefreshCount(int refreshCount) {
		this.refreshCount = refreshCount;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
	}

	public Date getEventEndTime() {
		return eventEndTime;
	}
	
	public void setPfEndTime(Date pfEndTime) {
		this.pfEndTime = pfEndTime;
	}

	public Date getPfEndTime() {
		return pfEndTime;
	}

	public void setEventStartTimeStr(String eventStartTimeStr) {
		this.eventStartTimeStr = eventStartTimeStr;
	}

	public String getEventStartTimeStr() {
		return eventStartTimeStr;
	}

    public void setEventEndTimeStr(String eventEndTimeStr) {
        this.eventEndTimeStr = eventEndTimeStr;
    }

    public String getEventEndTimeStr() {
        return eventEndTimeStr;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
