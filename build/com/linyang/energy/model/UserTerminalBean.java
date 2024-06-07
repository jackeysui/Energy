package com.linyang.energy.model;
/** 
* @Description 用户终端bean
* @author Jijialu
* @date 2018年5月11日 下午4:06:45 
*/

public class UserTerminalBean {
	
	/**
	 * 微信id
	 */
	private String openId;
	
	/**
	 * 分户id
	 */
	private Long ledgerId;
	
	/**
	 * 账户id，可作为反馈信息账户
	 */
	private Long accountId;
	
	/**
	 * 终端id
	 */
	private Long terminalId;

	/**
	 * 终端地址
	 */
	private Long terminalAddress;
	
	/**
	 * 补齐12位格式终端地址
	 */
	private String terminalAddress12;

	/**
	 * 终端名称
	 */
	private String terminalName;

	/**
	 * 用户密码
	 */
	private String password;
	
	/**
	 * 电压互感器倍率
	 */
	private Integer pt;
	
	/**
	 * 电流互感器倍率
	 */
	private Integer ct;

	/**
	 * 公司名称
	 */
	private String companyName;
	
	/**
	 * 电话
	 */
	private String tel;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 分享限制天数
	 */
	private Integer shareLimit;
	
	public String getTerminalAddress12() {
		return terminalAddress12;
	}

	public void setTerminalAddress12(String terminalAddress12) {
		this.terminalAddress12 = terminalAddress12;
	}
	
	public Integer getShareLimit() {
		return shareLimit;
	}

	public void setShareLimit(Integer shareLimit) {
		this.shareLimit = shareLimit;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPt() {
		return pt;
	}

	public void setPt(Integer pt) {
		this.pt = pt;
	}

	public Integer getCt() {
		return ct;
	}

	public void setCt(Integer ct) {
		this.ct = ct;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	public Long getTerminalAddress() {
		return terminalAddress;
	}

	public void setTerminalAddress(Long terminalAddress) {
		this.terminalAddress = terminalAddress;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
}
