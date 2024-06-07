package com.linyang.energy.model;

import java.io.Serializable;

/**
 *@Description 产污设施bean
 *@author dingy
 *@date 2018-12-10
 *@version
 */
public class PollutBean implements Serializable {
	
    /**
	 * 序列号
	 */
	private static final long serialVersionUID = 1143678903115047643L;
	
	/**
	 * 产污设施类型id
	 */
	private String pollutId;
	
	/**
	 * 产污设施名称
	 */
    private String pollutName;
    
    /**
     *  父产污设施ID
     */
    private String parentPollutId;
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public String getPollutId() {
		return pollutId;
	}
	
	public void setPollutId(String pollutId) {
		this.pollutId = pollutId;
	}
	
	public String getPollutName() {
		return pollutName;
	}
	
	public void setPollutName(String pollutName) {
		this.pollutName = pollutName;
	}
	
	public String getParentPollutId() {
		return parentPollutId;
	}
	
	public void setParentPollutId(String parentPollutId) {
		this.parentPollutId = parentPollutId;
	}
}