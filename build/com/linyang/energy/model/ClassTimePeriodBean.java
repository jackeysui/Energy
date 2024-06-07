package com.linyang.energy.model;

import java.util.Date;

/**
 * Created by Administrator on 16-12-27.
 */
public class ClassTimePeriodBean {
    private Long classId;
    private Date beginTime;
    private Date endTime;
    public Long getClassId() {
        return classId;
    }
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    public Date getBeginTime() {
        return beginTime;
    }
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
