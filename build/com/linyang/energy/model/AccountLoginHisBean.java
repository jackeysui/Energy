package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

public class AccountLoginHisBean implements Serializable{
	
	private static final long serialVersionUID = 6898056632078674738L;

	private Long accountId;
	
	private Date loginDate;
	
	private Integer loginSoftwareType;
	
	private String osVersion;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Integer getLoginSoftwareType() {
		return loginSoftwareType;
	}

	public void setLoginSoftwareType(Integer loginSoftwareType) {
		this.loginSoftwareType = loginSoftwareType;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

}
