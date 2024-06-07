package com.linyang.energy.model;

import java.io.Serializable;
import java.util.List;

/**
 * 班制配置表
 * @author Administrator
 *
 */
public class ClassConfigBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long classId;
	
	private Long ledgerId;
	
	private String className;
	
	private Integer cycle;

	private Integer restType;

	private Integer circleType;

	/**
	 * 班组信息
	 */
	private List<TeamConfigBean> teams;
	
	/**
	 * 循环周期:1-天 
	 */
	public static final int CYCLE_DAY = 1;
	
	/**
	 * 循环周期:2-周
	 */
	public static final int CYCLE_WEEK = 2;
	
	/**
	 * 循环周期:3-自定义
	 */
//	public static final int CYCLE_CUSTOM = 3;

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

    public Integer getRestType() {
        return restType;
    }

    public void setRestType(Integer restType) {
        this.restType = restType;
    }

    public Integer getCircleType() {
        return circleType;
    }

    public void setCircleType(Integer circleType) {
        this.circleType = circleType;
    }

	public List<TeamConfigBean> getTeams() {
		return teams;
	}

	public void setTeams(List<TeamConfigBean> teams) {
		this.teams = teams;
	}
	
}
