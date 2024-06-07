package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 需量申报记录表
 * @author guosen
 * @date 2014-12-19
 */
public class DemandRecBean implements Serializable{
	
	private static final long serialVersionUID = 1623725761890625083L;

	
	/**
	 * 计量点id
	 */
	private Long ledgerId;
	
	/**
	 * 开始时间
	 */
	private Date beginTime;
	
	/**
	 * 申报类型;1,容量;2,需量
	 */
	private Integer declareType;
	
	/**
	 * 申报值
	 */
	private Double declareValue;
	
	/**
	 * 申报时间
	 */
	private Date declareTime;

	public Long getMeterId() {
		return ledgerId;
	}

	public void setMeterId(Long meterId) {
		this.ledgerId = meterId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Integer getDeclareType() {
		return declareType;
	}

	public void setDeclareType(Integer declareType) {
		this.declareType = declareType;
	}

	public Double getDeclareValue() {
		return declareValue;
	}

	public void setDeclareValue(Double declareValue) {
		this.declareValue = declareValue;
	}

	public Date getDeclareTime() {
		return declareTime;
	}

	public void setDeclareTime(Date declareTime) {
		this.declareTime = declareTime;
	}
}
