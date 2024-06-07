package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 18-1-29.
 */
public class EventPolluteBean implements Serializable{

    /**
     * 序列号
     */
    private static final long serialVersionUID = -1438371833841820773L;

    /**
     * 污染源ID
     */
    private Long polluteId;   //

    /**
     * 污染源名称
     */
    private String polluteName;   //

    /**
     * 污染源启动阀值
     */
    private Double polluteDoor;   //

    /**
     * 污染源启动时间
     */
    private Date polluteStartTime;

    /**
     * 污染源启动时,对应的值
     */
    private Double polluteStartTimeValue;

    /**
     * 污染源关闭时间
     */
    private Date polluteEndTime;

    /**
     * 治理源ID
     */
    private Long resolveId;    //

    /**
     * 治理源名称
     */
    private String resolveName;    //

    /**
     * 治理源启动阀值
     */
    private Double resolveDoor;    //

    /**
     * 治理源关闭时间
     */
    private Date resolveEndTime;

    /**
     * 治理源关闭时,对应的值
     */
    private Double resolveEndTimeValue;

    /**
     * 所属企业Id
     */
    private Long parentLedgerId;       //

    /**
     * 所属企业名称
     */
    private String parentLedgerName;   //

    /***
     * 事件发生标记; 默认0表示未发生，为1表示发生
     */
    private int eventFlag;


    public EventPolluteBean(){

    }

    public Long getParentLedgerId() {
        return parentLedgerId;
    }

    public void setParentLedgerId(Long parentLedgerId) {
        this.parentLedgerId = parentLedgerId;
    }

    public String getParentLedgerName() {
        return parentLedgerName;
    }

    public void setParentLedgerName(String parentLedgerName) {
        this.parentLedgerName = parentLedgerName;
    }

    public Long getPolluteId() {
        return polluteId;
    }

    public void setPolluteId(Long polluteId) {
        this.polluteId = polluteId;
    }

    public String getPolluteName() {
        return polluteName;
    }

    public void setPolluteName(String polluteName) {
        this.polluteName = polluteName;
    }

    public Double getPolluteDoor() {
        return polluteDoor;
    }

    public void setPolluteDoor(Double polluteDoor) {
        this.polluteDoor = polluteDoor;
    }

    public Date getPolluteStartTime() {
        return polluteStartTime;
    }

    public void setPolluteStartTime(Date polluteStartTime) {
        this.polluteStartTime = polluteStartTime;
    }

    public Date getPolluteEndTime() {
        return polluteEndTime;
    }

    public void setPolluteEndTime(Date polluteEndTime) {
        this.polluteEndTime = polluteEndTime;
    }

    public Long getResolveId() {
        return resolveId;
    }

    public void setResolveId(Long resolveId) {
        this.resolveId = resolveId;
    }

    public String getResolveName() {
        return resolveName;
    }

    public void setResolveName(String resolveName) {
        this.resolveName = resolveName;
    }

    public Double getResolveDoor() {
        return resolveDoor;
    }

    public void setResolveDoor(Double resolveDoor) {
        this.resolveDoor = resolveDoor;
    }

    public Date getResolveEndTime() {
        return resolveEndTime;
    }

    public void setResolveEndTime(Date resolveEndTime) {
        this.resolveEndTime = resolveEndTime;
    }

    public int getEventFlag() {
        return eventFlag;
    }

    public void setEventFlag(int eventFlag) {
        this.eventFlag = eventFlag;
    }

    public Double getPolluteStartTimeValue() {
        return polluteStartTimeValue;
    }

    public void setPolluteStartTimeValue(Double polluteStartTimeValue) {
        this.polluteStartTimeValue = polluteStartTimeValue;
    }

    public Double getResolveEndTimeValue() {
        return resolveEndTimeValue;
    }

    public void setResolveEndTimeValue(Double resolveEndTimeValue) {
        this.resolveEndTimeValue = resolveEndTimeValue;
    }
}
