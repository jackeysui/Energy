package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

public class SuggestBean implements Serializable {
	
	private static final long serialVersionUID = 2748850637264922679L;
	/**
	 * 建议id
	 */
	private Long sugId;
	/**
	 * 建议时间
	 */
	private String submitDateStr;
	
	private Date submitDate;
	
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	/**
	 * 用户id
	 */
	private Long accountId;
	/**
	 * 用户名
	 */
	private String submitUser;
	/**
	 * 分户id
	 */
	private Long ledgerId;
	/**
	 * 分户名
	 */
	private String submitLedger;
	/**
	 * 建议角色
	 */
	private int submitRole;
	/**
	 * 建议
	 */
	private String sugMsg;
	/**
	 * 回复
	 */
	private String sugReply;
	/**
	 * 联系方式
	 */
	private String contactWay;
	
	
	public String getContactWay() {
		return contactWay;
	}
	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}
	public Long getSugId() {
		return sugId;
	}
	public void setSugId(Long sugId) {
		this.sugId = sugId;
	}
	public String getSubmitDateStr() {
		return submitDateStr;
	}
	public void setSubmitDateStr(String submitDateStr) {
		this.submitDateStr = submitDateStr;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getSubmitUser() {
		return submitUser;
	}
	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}
	public Long getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getSubmitLedger() {
		return submitLedger;
	}
	public void setSubmitLedger(String submitLedger) {
		this.submitLedger = submitLedger;
	}
	public int getSubmitRole() {
		return submitRole;
	}
	public void setSubmitRole(int submitRole) {
		this.submitRole = submitRole;
	}
	public String getSugMsg() {
		return sugMsg;
	}
	public void setSugMsg(String sugMsg) {
		this.sugMsg = sugMsg;
	}
	public String getSugReply() {
		return sugReply;
	}
	public void setSugReply(String sugReply) {
		this.sugReply = sugReply;
	}
	
	
}
