package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 建议表
 *
 * @author Administrator
 */
public class RecordBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3490050376358076540L;

    /**
     * 记录id
     */
    private Long sugId;

    /**
     * 提交记录日期
     */
    private Date submitDate;

    /**
     * 用户id
     */
    private Long accountId;

    /**
     * 记录提交用户
     */
    private String submitUser;

    /**
     * 分户id
     */
    private Long ledgerId;

    /**
     * 记录提交者所属分户
     */
    private String submitLedger;

    /**
     * 建议内容
     */
    private String sugMsg;

    /**
     * 联系方式
     */
    private String contactWay;

    /**
     * (字段作废)
     */
    private Integer submitRole;

    /**
     * (字段作废)
     */
    private String sugReply;

    /**
     * 建议状态 0未回复 1已回复
     */
    private Integer status;

    /**
     * 建议时间String类型
     */
    private String submitDateStr;


    private Integer pageNo;

    private Integer tag;

    private String openId;

    private String replyMsg;

    public String getReplyMsg() {
        return replyMsg;
    }

    public void setReplyMsg(String replyMsg) {
        this.replyMsg = replyMsg;
    }

    public String getOpenId() {
        return openId;
    }


    public void setOpenId(String openId) {
        this.openId = openId;
    }


    public Integer getTag() {
        return tag;
    }


    public void setTag(Integer tag) {
        this.tag = tag;
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


    public String getSugMsg() {
        return sugMsg;
    }


    public void setSugMsg(String sugMsg) {
        this.sugMsg = sugMsg;
    }


    public String getContactWay() {
        return contactWay;
    }


    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }


    public Integer getSubmitRole() {
        return submitRole;
    }


    public void setSubmitRole(Integer submitRole) {
        this.submitRole = submitRole;
    }


    public String getSugReply() {
        return sugReply;
    }


    public void setSugReply(String sugReply) {
        this.sugReply = sugReply;
    }


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getSubmitDateStr() {
        return submitDateStr;
    }


    public void setSubmitDateStr(String submitDateStr) {
        this.submitDateStr = submitDateStr;
    }


    public Integer getPageNo() {
        return pageNo;
    }


    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }


}
