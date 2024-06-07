package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

public class ServiceReportBean implements Serializable{

	private static final long serialVersionUID = 775139955108636635L;
	
	private Long reportId;
	
	private Long ledgerId;
	
	private String ledgerName;
	
	private Date reportTime;
	
	private Long reportModule;
	
	private Integer ledgerType;
	/**
	 * 普通用户
	 */
	public static final Integer LEDGER_TYPE_NORMAL = 1;
	/**
	 * VIP
	 */
	public static final Integer LEDGER_TYPE_VIP = 2;
	
	private String reportContent;
	
	private Date createTime;
	
	private Integer status;
	
	private String spAdvise;
	
	private String spName;
	
	private String spPhone;
	
	/**
	 * 未推送
	 */
	public static final Integer STATUS_UNPUSH = 1;
	
	/**
	 * 已推送
	 */
	public static final Integer STATUS_PUSH = 2;

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}
	
	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}

	public Date getReportTime() {
		return reportTime;
	}
	
	public Integer getLedgerType() {
		return ledgerType;
	}

	public void setLedgerType(Integer ledgerType) {
		this.ledgerType = ledgerType;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Long getReportModule() {
		return reportModule;
	}

	public void setReportModule(Long reportModule) {
		this.reportModule = reportModule;
	}

	public String getReportContent() {
		return reportContent;
	}

	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSpAdvise() {
		return spAdvise;
	}

	public void setSpAdvise(String spAdvise) {
		this.spAdvise = spAdvise;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getSpPhone() {
		return spPhone;
	}

	public void setSpPhone(String spPhone) {
		this.spPhone = spPhone;
	}
	
}
