package com.linyang.energy.model;

import java.io.Serializable;

public class RoleModuleRelationBean implements Serializable {
	 /**
     * 菜单的id
     */
    private Long moduleId;
    /**
     * 角色id
     */
    private Long roleId;

    private static final long serialVersionUID = 1L;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}