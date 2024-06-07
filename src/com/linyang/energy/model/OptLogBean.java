package com.linyang.energy.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


public class OptLogBean implements Serializable {
	/**
     * 记录日志的id
     */
    private Long optlogId;
    /**
     * 操作时间
     */
    private Date optTime;
    /**
     * 操作用户id
     */
    private Long optId;
    /**
     * 操作用户名称
     */
    private String optName;
    /**
     * 操作类型
     */
    private Short optType;
    /**
     * 被操作对象的id
     */
    private Integer optedObject;
    /**
     * 被操作对象的名称
     */
    private String optedName;
    /**
     * 被操作对象的类型
     */
    private int objectType;
    /**
     * 操作用户IP
     */
    private String optIp;
    /**
     * 操作结果
     */
    private Short optResult;
    /**
     * 操作描述
     */
    private String optRemark;

    private static final long serialVersionUID = 1L;
  
    /**
     *@类名：OptLogBean.java  
     *@描述：{todo} 
     * @param optType 操作类型
     * @param optedObject 操作对象id
     * @param optedName 操作对象名称
     * @param objectType 操作对象类型
     * @param operatResult 操作结果
     * @param optRemark 描述
     */
    public OptLogBean(int optType,int optedObject,String optedName,int objectType,int operatResult,String optRemark){
    	this.optTime = new Timestamp(System.currentTimeMillis());
    	this.optResult = (short)operatResult;
    	this.optType = (short)optType;
    	this.objectType = objectType;
    	this.optRemark = optRemark;
    	this.optedObject = optedObject;
    	this.optedName = optedName;
    	
    }
    

    public Long getOptlogId() {
        return optlogId;
    }

    public void setOptlogId(Long optlogId) {
        this.optlogId = optlogId;
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    public Long getOptId() {
        return optId;
    }

    public void setOptId(Long optId) {
        this.optId = optId;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName == null ? null : optName.trim();
    }

    public Short getOptType() {
        return optType;
    }

    public void setOptType(Short optType) {
        this.optType = optType;
    }

    public Integer getOptedObject() {
        return optedObject;
    }

    public void setOptedObject(Integer optedObject) {
        this.optedObject = optedObject;
    }

    public String getOptedName() {
        return optedName;
    }

    public void setOptedName(String optedName) {
        this.optedName = optedName;
    }

    /**
	 * @return the objectType
	 */
	public int getObjectType() {
		return objectType;
	}


	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}


	public String getOptIp() {
        return optIp;
    }

    public void setOptIp(String optIp) {
        this.optIp = optIp == null ? null : optIp.trim();
    }

    public Short getOptResult() {
        return optResult;
    }

    public void setOptResult(Short optResult) {
        this.optResult = optResult;
    }

    public String getOptRemark() {
        return optRemark;
    }

    public void setOptRemark(String optRemark) {
        this.optRemark = optRemark == null ? null : StringUtils.substring(optRemark.trim(),0,170);
    }
}