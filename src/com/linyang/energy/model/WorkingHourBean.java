package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作时间段对象
 * @author Administrator
 *
 */
public class WorkingHourBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long teamId;
	
	private String teamName;
	
	private int onDutyWeek;
	
	private Date onDuty;
	
	private int offDutyWeek;
	
	private Date offDuty;

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public int getOnDutyWeek() {
		return onDutyWeek;
	}

	public void setOnDutyWeek(int onDutyWeek) {
		this.onDutyWeek = onDutyWeek;
	}

	public Date getOnDuty() {
		return onDuty;
	}

	public void setOnDuty(Date onDuty) {
		this.onDuty = onDuty;
	}

	public int getOffDutyWeek() {
		return offDutyWeek;
	}

	public void setOffDutyWeek(int offDutyWeek) {
		this.offDutyWeek = offDutyWeek;
	}

	public Date getOffDuty() {
		return offDuty;
	}

	public void setOffDuty(Date offDuty) {
		this.offDuty = offDuty;
	}

}
