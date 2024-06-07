package com.linyang.energy.model;

import java.io.Serializable;

/**
 *@Description 行业bean
 *@author chengq
 *@date 2014-8-12
 *@version
 */
public class IndustryBean implements Serializable {
	
    /**
	 * 序列号
	 */
	private static final long serialVersionUID = 1143678903115047643L;
	
	/**
	 * 区域ID
	 */
	private String industryId;
	
	/**
	 * 区域名称
	 */
    private String industryName;
    
    /**
     * 区域级别
     */
    private Integer industryLevel;
    
    /**
     *  父区域ID
     */
    private String parentIndustryId;

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Integer getIndustryLevel() {
		return industryLevel;
	}

	public void setIndustryLevel(Integer industryLevel) {
		this.industryLevel = industryLevel;
	}

	public String getParentIndustryId() {
		return parentIndustryId;
	}

	public void setParentIndustryId(String parentIndustryId) {
		this.parentIndustryId = parentIndustryId;
	}
}