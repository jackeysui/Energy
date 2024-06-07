package com.linyang.energy.model;

import java.io.Serializable;

/**
 *@Description	设备类型bean
 *@author dingy
 *@date 2019-01-22
 *@version
 */
public class TypeBean implements Serializable {
	
    /**
	 * 序列号
	 */
	private static final long serialVersionUID = 1143678903115047643L;
	
	/**
	 * 区域ID
	 */
	private String typeId;
	
	/**
	 * 区域名称
	 */
    private String typeName;
    
    /**
     *  父区域ID
     */
    private Long parentTypeId;
	
	public String getTypeId() {
		return typeId;
	}
	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public Long getParentTypeId() {
		return parentTypeId;
	}
	
	public void setParentTypeId(Long parentTypeId) {
		this.parentTypeId = parentTypeId;
	}
}