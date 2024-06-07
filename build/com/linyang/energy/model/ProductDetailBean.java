package com.linyang.energy.model;

import java.util.Date;

/**
 * 产量明细对象
 * @author guosen
 * @date 2016-8-24
 *
 */
public class ProductDetailBean {
	
	private Date startTime;
	
	private Date endTime;
	
	private double output;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(double output) {
		this.output = output;
	}
}
