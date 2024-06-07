package com.linyang.energy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 电网需求响应方案
 * @author bowen
 * @date 2019-09-07
 */
public class GridDemandPlanBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9057164956517651375L;

	/**
	 * id
	 */
	private Long planId;
	
	/**
	 * 方案名称
	 */
	private String planName;
	
	/**
	 * 1：待审批；2：已生效
	 */
	private Integer planStatus;
	
	/**
	 * 方案开始日期
	 */
	private String startDate;

	/**
	 * 方案开始日期
	 */
	private String endDate;
	
	/**
	 * 方案开始时间
	 */
	private String startTime;
	
	/**
	 * 方案开始时间
	 */
	private String endTime;
	
	/**
	 * 电网调峰量
	 */
	private Integer pitchPeak;
	
	/**
	 * 方案备注
	 */
	private String remarks;
	
	/**
	 * 企业id
	 */
	private Long ledgerId;
	
	/**
	 * 企业调峰量
	 */
	private Integer ledgerPitchPeak;
	
	/**
	 * 消减备注
	 */
	private String subRemarks;
	/**
	 * 可中断能管对象Map<String, Object>  loadId  type=1是能管对象  2是采集点
	 */
	private List<Map<String, Object>> planLoads=new ArrayList<Map<String, Object>>();
	
	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public Integer getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(Integer planStatus) {
		this.planStatus = planStatus;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getPitchPeak() {
		return pitchPeak;
	}

	public void setPitchPeak(Integer pitchPeak) {
		this.pitchPeak = pitchPeak;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public Integer getLedgerPitchPeak() {
		return ledgerPitchPeak;
	}

	public void setLedgerPitchPeak(Integer ledgerPitchPeak) {
		this.ledgerPitchPeak = ledgerPitchPeak;
	}

	public String getSubRemarks() {
		return subRemarks;
	}

	public void setSubRemarks(String subRemarks) {
		this.subRemarks = subRemarks;
	}

	public List<Map<String, Object>> getPlanLoads() {
		return planLoads;
	}

	public void setPlanLoads(List<Map<String, Object>> planLoads) {
		this.planLoads = planLoads;
	}

}
