package com.linyang.energy.model;

import java.io.Serializable;

/**
 *@Description 治污设施bean
 *@author dingy
 *@date 2018-12-10
 *@version
 */
public class PollutctlBean implements Serializable {
	
    /**
	 * 序列号
	 */
	private static final long serialVersionUID = 1143678903115047643L;
	
	/**
	 * 治污设施类型id
	 */
	private String pollutctlId;
	
	/**
	 * 治污设施名称
	 */
    private String pollutctlName;
    
    /**
     *  治污污设施ID
     */
    private String parentPollutctlId;
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public String getPollutctlId() {
		return pollutctlId;
	}
	
	public void setPollutctlId(String pollutctlId) {
		this.pollutctlId = pollutctlId;
	}
	
	public String getPollutctlName() {
		return pollutctlName;
	}
	
	public void setPollutctlName(String pollutctlName) {
		this.pollutctlName = pollutctlName;
	}
	
	public String getParentPollutctlId() {
		return parentPollutctlId;
	}
	
	public void setParentPollutctlId(String parentPollutctlId) {
		this.parentPollutctlId = parentPollutctlId;
	}
}