package com.linyang.energy.model;

/**
 * @Description 设备集Bean
 * @author Leegern
 * @date Jan 8, 2014 4:38:37 PM
 */
public class DeviceTypeBean {
	/**
	 * 设备类型Id
	 */
	private long typeId;
	
	/**
	 * 设备类型名称
	 */
	private String typeName;
	
	/**
	 * 父设备类型Id
	 */
	private long parentTypeId = -1;
	
	/**
	 * 设备描述
	 */
	private String typeRemark;
	
	/**
	 * 左边界
	 */
	private int lft;
	
	/**
	 * 右边界
	 */
	private int rgt;

	
	/**
	 * @return the typeId
	 */
	public long getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the parentTypeId
	 */
	public long getParentTypeId() {
		return parentTypeId;
	}

	/**
	 * @param parentTypeId the parentTypeId to set
	 */
	public void setParentTypeId(long parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	/**
	 * @return the typeRemark
	 */
	public String getTypeRemark() {
		return typeRemark;
	}

	/**
	 * @param typeRemark the typeRemark to set
	 */
	public void setTypeRemark(String typeRemark) {
		this.typeRemark = typeRemark;
	}

	/**
	 * @return the lft
	 */
	public int getLft() {
		return lft;
	}

	/**
	 * @param lft the lft to set
	 */
	public void setLft(int lft) {
		this.lft = lft;
	}

	/**
	 * @return the rgt
	 */
	public int getRgt() {
		return rgt;
	}

	/**
	 * @param rgt the rgt to set
	 */
	public void setRgt(int rgt) {
		this.rgt = rgt;
	}
}
