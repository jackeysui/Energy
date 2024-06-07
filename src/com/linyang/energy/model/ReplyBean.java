package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 回复表
 * 
 * @author Administrator
 *
 */
public class ReplyBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7605539008720068810L;

	/**
	 * 回复id
	 */
	private Long replyId;

	/**
	 * 建议id
	 */
	private Long sugId;

	/**
	 * 回复提交日期
	 */
	private Date submitDate;

	/**
	 * 回复提交日期字符串
	 */
	private String submitDateStr;

	/**
	 * 用户id
	 */
	private Long accountId;

	/**
	 * 回复提交用户
	 */
	private String submitUser;

	/**
	 * 分户id
	 */
	private Long ledgerId;

	/**
	 * 回复提交这所属分户
	 */
	private String submitLedger;

	/**
	 * 回复内容
	 */
	private String replyMsg;

	/**
	 * 联系方式
	 */
	private String contactWay;

	/**
	 * 回复状态 0未回复 1已回复
	 */
	private Integer status;

	/**
	 * 序列,用来排序
	 */
	private Integer seq;
	
	private Integer tag;
	
	private String openId;
	
	
	
	

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSubmitDateStr() {
		return submitDateStr;
	}

	public void setSubmitDateStr(String submitDateStr) {
		this.submitDateStr = submitDateStr;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

	public Long getReplyId() {
		return replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}

	public Long getSugId() {
		return sugId;
	}

	public void setSugId(Long sugId) {
		this.sugId = sugId;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
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

	public String getReplyMsg() {
		return replyMsg;
	}

	public void setReplyMsg(String replyMsg) {
		this.replyMsg = replyMsg;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

}
