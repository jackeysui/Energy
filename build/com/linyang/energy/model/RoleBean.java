package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

public class RoleBean implements Serializable {
	/**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色状态
     */
    private int roleStatus;
    /**
     * 角色类型
     */
    private Short roleType;
    /**
     * 创建用户id
     */
    private Long createUserid;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改用户id
     */
    private Long modifyUserid;
    /**
     * 修改日期
     */
    private Date modifyDate;
    /**
     * 角色描述
     */
    private String roleDesc;
    

	private static final long serialVersionUID = 1L;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? "" : roleName.trim();
    }

    public int getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(int roleStatus) {
        this.roleStatus = roleStatus;
    }
    public Short getRoleT() {
        return roleType;
    }

    public void setRoleType(Short roleType) {
        this.roleType = roleType;
    }

    public Long getCreateUserid() {
        return createUserid;
    }

    public void setCreateUserid(Long createUserid) {
        this.createUserid = createUserid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getModifyUserid() {
        return modifyUserid;
    }

    public void setModifyUserid(Long modifyUserid) {
        this.modifyUserid = modifyUserid;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc == null ? "" : roleDesc.trim();
    }
	@Override
	public String toString() {
		return "RoleBean [roleId=" + roleId + ", roleName="
				+ roleName 
				+ "]";
	}
}