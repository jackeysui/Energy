package com.linyang.energy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeamConfigBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private long teamId;
	
	private String teamName;
	
	private Long classId;
	
	/**
	 * 上班时间
	 */
	private List<Date> onDutyList = new ArrayList<Date>();
	
	/**
	 * 上班时间所属星期
	 */
	private List<Integer> onDutyWeekList = new ArrayList<Integer>();

	/**
	 * 下班时间
	 */
	private List<Date> offDutyList = new ArrayList<Date>();
	
	/**
	 * 下班时间所属星期
	 */
	private List<Integer> offDutyWeekList = new ArrayList<Integer>();
	
	/**
	 * 开始时间
	 */
	private List<Date> beginList = new ArrayList<Date>();
	
	/**
	 * 结束时间
	 */
	private List<Date> endList = new ArrayList<Date>();

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public List<Date> getOnDutyList() {
		return onDutyList;
	}

	public void setOnDutyList(List<Date> onDutyList) {
		this.onDutyList = onDutyList;
	}

	public List<Integer> getOnDutyWeekList() {
		return onDutyWeekList;
	}

	public void setOnDutyWeekList(List<Integer> onDutyWeekList) {
		this.onDutyWeekList = onDutyWeekList;
	}

	public List<Date> getOffDutyList() {
		return offDutyList;
	}

	public void setOffDutyList(List<Date> offDutyList) {
		this.offDutyList = offDutyList;
	}

	public List<Integer> getOffDutyWeekList() {
		return offDutyWeekList;
	}

	public void setOffDutyWeekList(List<Integer> offDutyWeekList) {
		this.offDutyWeekList = offDutyWeekList;
	}

	public List<Date> getBeginList() {
		return beginList;
	}

	public void setBeginList(List<Date> beginList) {
		this.beginList = beginList;
	}

	public List<Date> getEndList() {
		return endList;
	}

	public void setEndList(List<Date> endList) {
		this.endList = endList;
	}
}
