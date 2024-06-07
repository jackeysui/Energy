package com.linyang.energy.model;

/**
 * Created by Administrator on 16-12-27.
 */
public class ClassRestDayBean {
    private long classId;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
    private int includeDefaultHoliday;
    private int includeCustomHoliday;

    public long getClassId() {
        return classId;
    }
    public void setClassId(long classId) {
        this.classId = classId;
    }
    public int getMonday() {
        return monday;
    }
    public void setMonday(int monday) {
        this.monday = monday;
    }
    public int getTuesday() {
        return tuesday;
    }
    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }
    public int getWednesday() {
        return wednesday;
    }
    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }
    public int getThursday() {
        return thursday;
    }
    public void setThursday(int thursday) {
        this.thursday = thursday;
    }
    public int getFriday() {
        return friday;
    }
    public void setFriday(int friday) {
        this.friday = friday;
    }
    public int getSaturday() {
        return saturday;
    }
    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }
    public int getSunday() {
        return sunday;
    }
    public void setSunday(int sunday) {
        this.sunday = sunday;
    }
    public int getIncludeDefaultHoliday() {
        return includeDefaultHoliday;
    }
    public void setIncludeDefaultHoliday(int includeDefaultHoliday) {
        this.includeDefaultHoliday = includeDefaultHoliday;
    }
    public int getIncludeCustomHoliday() {
        return includeCustomHoliday;
    }
    public void setIncludeCustomHoliday(int includeCustomHoliday) {
        this.includeCustomHoliday = includeCustomHoliday;
    }
}
