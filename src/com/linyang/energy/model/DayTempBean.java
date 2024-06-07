package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 区域温度
 * @author Administrator
 *
 */
public class DayTempBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Date dailyTime;
	
	private Integer tempMax;
	
	private Integer tempMin;
	
	private String regionId;
	
	private Integer week;
	
	public Date getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(Date dailyTime) {
		this.dailyTime = dailyTime;
	}

	public Integer getTempMax() {
		return tempMax;
	}

	public void setTempMax(Integer tempMax) {
		this.tempMax = tempMax;
	}

	public Integer getTempMin() {
		return tempMin;
	}

	public void setTempMin(Integer tempMin) {
		this.tempMin = tempMin;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

}
