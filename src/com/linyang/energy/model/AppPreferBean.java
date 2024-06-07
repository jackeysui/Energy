/**
 */
package com.linyang.energy.model;

import java.io.Serializable;

/**
 * 
 * @author Angelo
 * @date 2016-6-13 上午09:42:54
 * @version 1.0
 */
public class AppPreferBean implements Serializable {
	private static final long serialVersionUID = -8623550034120948924L;
	/**
	 * 账户Id
	 */
	private Long accountId;
	/**
	 * 标识：1-轮循数据,2-重点关注
	 */
	private Integer flag;
	/**
	 * App配置
	 */
	private String options;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "AppPreferBean [accountId=" + accountId + ", flag=" + flag
				+ ", options=" + options + "]";
	}
}
