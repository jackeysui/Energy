package com.linyang.energy.model;

import java.io.Serializable;

public class AccountRoleRelationBean implements Serializable {
	/**
	 * 角色id
	 */
    private Long roleId;
    /**
	 * 账户id
	 */
    private Long accountId;

    private static final long serialVersionUID = 1L;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}