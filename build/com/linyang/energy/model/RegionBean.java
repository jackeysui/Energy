package com.linyang.energy.model;

import java.io.Serializable;

/**
 *@Description 区域bean
 *@author chengq
 *@date 2014-8-12
 *@version
 */
public class RegionBean implements Serializable {
	
    /**
	 * 序列号
	 */
	private static final long serialVersionUID = 1143678903115047643L;
	
	/**
	 * 区域ID
	 */
	private String regionId;
	
	/**
	 * 区域名称
	 */
    private String regionName;
    
    /**
     * 区域级别
     */
    private Integer regionLevel;
    
    /**
     *  父区域ID
     */
    private Long parentRegionId;

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Integer getRegionLevel() {
		return regionLevel;
	}

	public void setRegionLevel(Integer regionLevel) {
		this.regionLevel = regionLevel;
	}

	public Long getParentRegionId() {
		return parentRegionId;
	}

	public void setParentRegionId(Long parentRegionId) {
		this.parentRegionId = parentRegionId;
	}
}