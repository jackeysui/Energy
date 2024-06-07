package com.linyang.energy.model;

import java.util.Date;

/**
 * Created by Administrator on 16-12-27.
 */
public class ClassHolidayBean {
    private Long classId;
    private String name;
    private Date fromDate;
    private Date endDate;
    public Long getClassId() {
        return classId;
    }
    public void setClassId(Long classId) {
        this.classId = classId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
