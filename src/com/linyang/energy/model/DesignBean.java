package com.linyang.energy.model;

import java.util.Date;

/**
 * 
 * @类功能说明： 生产计划类
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2014-2-20 上午10:25:34  
 * @版本：V1.0
 */
public class DesignBean {
	/**
	 * 计划ID
	 */
	private long planId;
	/**
	 * 产品ID
	 */
	private long productId;
	/**
	 * 流水线ID
	 */
	private long assembleId;
	/**
	 * 产品名称
	 */
	private String productName;
	/**
	 * 流水线名称
	 */
	private String assembleName;
	/**
	 * 产量
	 */
	private double output;
	
	/**
	 * 产量单位
	 */
	private String outPutUnit;
	/**
	 * 开始时间
	 */
	private Date beginTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	

	
	
	
	
	
	public String getOutPutUnit() {
		return outPutUnit;
	}
	public void setOutPutUnit(String outPutUnit) {
		this.outPutUnit = outPutUnit;
	}
	public long getPlanId() {
		return planId;
	}
	public void setPlanId(long planId) {
		this.planId = planId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public long getAssembleId() {
		return assembleId;
	}
	public void setAssembleId(long assembleId) {
		this.assembleId = assembleId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAssembleName() {
		return assembleName;
	}
	public void setAssembleName(String assembleName) {
		this.assembleName = assembleName;
	}
	public double getOutput() {
		return output;
	}
	public void setOutput(double output) {
		this.output = output;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
