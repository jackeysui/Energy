package com.linyang.energy.model;

public class EventSettingRecBean {
	private long recId;
	private Long ledgerId;
	private String ledgerName;
	private Long eventTypeId;
	private String eventTypeName;
	private Integer objType;
	private Long objId;
	private String objName;
	private String alarmValue;
	private String ratedValue;
	private Float alarmPercent;
	
	public long getRecId() {
		return recId;
	}
	public void setRecId(long recId) {
		this.recId = recId;
	}
	public Long getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public Long getEventTypeId() {
		return eventTypeId;
	}
	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}
	public String getEventTypeName() {
		return eventTypeName;
	}
	public void setEventTypeName(String eventName) {
		this.eventTypeName = eventName;
	}
	public Integer getObjType() {
		return objType;
	}
	public void setObjType(Integer objType) {
		this.objType = objType;
	}
	public void setObjId(Long objId) {
		this.objId = objId;
	}
	public Long getObjId() {
		return objId;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	public String getAlarmValue() {
		return alarmValue;
	}
	public void setAlarmValue(String alarmValue) {
		this.alarmValue = alarmValue;
	}
	public String getRatedValue() {
		return ratedValue;
	}
	public void setRatedValue(String ratedValue) {
		this.ratedValue = ratedValue;
	}
	public Float getAlarmPercent() {
		return alarmPercent;
	}
	public void setAlarmPercent(Float alarmPercent) {
		this.alarmPercent = alarmPercent;
	}
}
