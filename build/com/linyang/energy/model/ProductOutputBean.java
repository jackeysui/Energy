package com.linyang.energy.model;

import java.util.Date;

public class ProductOutputBean {
	
	/**
	 * 产量id
	 */
	private Long outputId;

	/**
	 * 分户id
	 */
	private Long ledgerId;
	
	/**
	 * 产品id
	 */
	private Long productId;
	
	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 车间id
	 */
	private Long workshopId;
	
	/**
	 * 车间名称
	 */
	private String workshopName;
	
	/**
	 * 班组id
	 */
	private Long teamId;
	
	/**
	 * 班组名称
	 */
	private String teamName;
	
	/**
	 * 生产开始时间
	 */
	private Date startTime;
	
	private String startTimeStr;
	
	/**
	 * 生产结束时间
	 */
	private Date endTime;
	
	private String endTimeStr;
	
	/**
	 * 产量
	 */
	private Double productOutput;
	
	/**
	 * 产品单位
	 */
	private String productUnit;
	
	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public Long getOutputId() {
		return outputId;
	}

	public void setOutputId(Long outputId) {
		this.outputId = outputId;
	}
	
	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getWorkshopId() {
		return workshopId;
	}

	public void setWorkshopId(Long workshopId) {
		this.workshopId = workshopId;
	}

	public String getWorkshopName() {
		return workshopName;
	}

	public void setWorkshopName(String workshopName) {
		this.workshopName = workshopName;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

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

	public Double getProductOutput() {
		return productOutput;
	}

	public void setProductOutput(Double productOutput) {
		this.productOutput = productOutput;
	}
	
	
}
