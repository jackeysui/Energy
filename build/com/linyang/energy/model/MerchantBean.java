package com.linyang.energy.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 16:48 2019/7/15
 * @ Description：商户报表bean
 * @ Modified By：:dingy
 * @Version: v4.6
 */
public class MerchantBean {
	
	// add or update method by dingy
	// date 2019/7/15
	// Modify the content: 用于对其列表,以及正确展示数据使用
	/**
	 * 第三级父节点
	 */
	private String thirdName = "-";
	
	/**
	 * 第二级父节点
	 */
	private String secondName = "-";
	
	/**
	 * 第一级父节点
	 */
	private String firstName = "-";
	//end
	
	/**
	 * 企业id
	 */
	private long ledgerId;
	
	/**
	 * 能管对象名称
	 */
	private String ledgerName;
	
	/**
	 * 户号
	 */
	private String userNo;
	
	/**
	 * 起始时间
	 */
	private Date startTime;
	
	/**
	 * 起始示值
	 */
	private double startValue;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 结束示值
	 */
	private double endValue;
	
	/**
	 * 耗能量
	 */
	private double coalValue;
	
	/**
	 * 单价
	 */
	private double unitPrice;
	
	/**
	 * 总价
	 */
	private double totalPrice;
	
	/**
	 * 缴费状态
	 */
	private int status;
	
	/**
	 * 费率号(电)
	 */
	private Long rateId;
	
	/**
	 * 费率号(水)
	 */
	private Long wRateId;
	
	/**
	 * 费率号(气)
	 */
	private Long gRateId;
	
	/**
	 * 费率号(热)
	 */
	private Long hRateId;
	
	/**
	 * 测量点数量(用于跨行)
	 */
	private int size;
	
	/**
	 * 是否继承父节点费率
	 */
	private int inherit;
	
	/**
	 * 是否可以点击缴费按钮标识(默认-1,可以缴费则大于0)
	 */
	private int purview = -1;
	
	
	public int getInherit() {
		return inherit;
	}
	
	public void setInherit(int inherit) {
		this.inherit = inherit;
	}
	
	private List<Map<String, Object>> rateList;
	
	
	public List<Map<String, Object>> getRateList() {
		return rateList;
	}
	
	public void setRateList(List<Map<String, Object>> rateList) {
		this.rateList = rateList;
	}
	
	public Long getRateId() {
		return rateId;
	}
	
	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}
	
	public String getThirdName() {
		return thirdName;
	}
	
	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}
	
	public String getSecondName() {
		return secondName;
	}
	
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public long getLedgerId() {
		return ledgerId;
	}
	
	public void setLedgerId(long ledgerId) {
		this.ledgerId = ledgerId;
	}
	
	public String getLedgerName() {
		return ledgerName;
	}
	
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	
	public String getUserNo() {
		return userNo;
	}
	
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public double getStartValue() {
		return startValue;
	}
	
	public void setStartValue(double startValue) {
		this.startValue = startValue;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public double getEndValue() {
		return endValue;
	}
	
	public void setEndValue(double endValue) {
		this.endValue = endValue;
	}
	
	public double getCoalValue() {
		return coalValue;
	}
	
	public void setCoalValue(double coalValue) {
		this.coalValue = coalValue;
	}
	
	public double getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public double getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public Long getwRateId() {
		return wRateId;
	}
	
	public void setwRateId(Long wRateId) {
		this.wRateId = wRateId;
	}
	
	public Long getgRateId() {
		return gRateId;
	}
	
	public void setgRateId(Long gRateId) {
		this.gRateId = gRateId;
	}
	
	public Long gethRateId() {
		return hRateId;
	}
	
	public void sethRateId(Long hRateId) {
		this.hRateId = hRateId;
	}
	
	public int getPurview() {
		return purview;
	}
	
	public void setPurview(int purview) {
		this.purview = purview;
	}
	
	/**
	 * 为了方便我存放数据而创建
	 * @author catkins
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @param rateList
	 * @param purview
	 * @return void
	 * @exception
	 * @date 2019/7/17 13:22
	 */
	public void setValues(Date startTime, Date endTime,	int status, List<Map<String, Object>> rateList,int purview) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.rateList = rateList;
		this.purview = purview;
	}
	
	@Override
	public String toString() {
		return "MerchantBean{" +
				"thirdName='" + thirdName + '\'' +
				", secondName='" + secondName + '\'' +
				", firstName='" + firstName + '\'' +
				", ledgerId=" + ledgerId +
				", ledgerName='" + ledgerName + '\'' +
				", userNo='" + userNo + '\'' +
				", startTime=" + startTime +
				", startValue=" + startValue +
				", endTime=" + endTime +
				", endValue=" + endValue +
				", coalValue=" + coalValue +
				", unitPrice=" + unitPrice +
				", totalPrice=" + totalPrice +
				", status=" + status +
				", rateId=" + rateId +
				", wRateId=" + wRateId +
				", gRateId=" + gRateId +
				", hRateId=" + hRateId +
				", size=" + size +
				", rateList=" + rateList +
				'}';
	}
}
