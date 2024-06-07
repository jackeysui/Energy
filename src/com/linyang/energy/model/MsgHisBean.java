/**
 */
package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author chengq
 * @date 2015-12-22 下午01:52:04
 * @version 1.0
 */
public class MsgHisBean implements Serializable{
	private static final long serialVersionUID = 9121216297142189032L;
	private Long accountId;
	private Long msgId;
	private Date createTime;
	private Integer msgType;
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	@Override
	public String toString() {
		return "MsgHisBean [accountId=" + accountId + ", createTime="
				+ createTime + ", msgId=" + msgId + ", msgType=" + msgType
				+ "]";
	} 
}
