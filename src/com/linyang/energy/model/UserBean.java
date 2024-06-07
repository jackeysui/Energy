package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

import com.linyang.energy.dto.BaseUserBean;

public class UserBean extends BaseUserBean implements Serializable {
	/**
	 * 用户Id
	 */
    private Long accountId;
    /**
     * 用户的名称
     */
    private String loginName;
    /**
     * 用户的密码
     */
    private String loginPassword;
    /**
     * 用户的状态
     */
    private Short accountStatus;
    /**
     * 用户的真实名称
     */
    private String realName;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 最后登陆日期
     */
    private Date lastDate;
    
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 分户Id
     */
    private Long ledgerId;
    /**
     * 分户名称
     */
    private String ledgerName;
    /**
     * 分户分析类型
     */
    private Integer analyType;
	/**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    
    private Long loginTimes;
    
    /******* add by chengq 南通二期用户添加数据权限 *******/
    /**
     * 群组ids(群组可复选)
     */
    private String groupId;
    
    /**
     * 用户状态修改时间
     */
    private Date modifyTime;
    
    /**
     * 用户免扰时间段
     */
    private String freeTimePeriod;
    
    /**
     * 消息是否处于屏蔽状态(0否  1是)
     */
    private Integer isShield = 1; 
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户活跃度
     */
    private Integer activeStatus;
    
    
    private Integer todayTimes;
    
    private String	lockTime;
    
    

    public Integer getTodayTimes() {
		return todayTimes;
	}
	public void setTodayTimes(Integer todayTimes) {
		this.todayTimes = todayTimes;
	}
	public String getLockTime() {
		return lockTime;
	}
	public void setLockTime(String lockTime) {
		this.lockTime = lockTime;
	}
	public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
    	super.userId = ( accountId == null ? "" : String.valueOf(accountId));
        this.accountId = accountId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? "" : loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword == null ? "" : loginPassword.trim();
    }

    public Short getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Short accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? "" : realName.trim();
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }
    
    public Date getLastDate() {
        return lastDate;
    }

   

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? "" : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email.trim();
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }
	/**
	 * @return the ledgerName
	 */
	public String getLedgerName() {
		return ledgerName;
	}
	/**
	 * @param ledgerName the ledgerName to set
	 */
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName == null ? "" : ledgerName.trim();
	}
	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName == null ? "" : roleName.trim();
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public Long getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(Long loginTimes) {
		this.loginTimes = loginTimes;
	}
	
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public String getFreeTimePeriod() {
		return freeTimePeriod;
	}
	public void setFreeTimePeriod(String freeTimePeriod) {
		this.freeTimePeriod = freeTimePeriod;
	}
	
	public Integer getIsShield() {
		return isShield;
	}
	public void setIsShield(Integer isShield) {
		this.isShield = isShield;
	}
	@Override
	public String toString() {
		return "UserBean [userId=" + accountId + ", userName="
				+ loginName + ", realName=" + realName 
				+ "]";
	}
    public Integer getAnalyType() {
		return analyType;
	}
	public void setAnalyType(Integer analyType) {
		this.analyType = analyType;
	}
	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	public Integer getActiveStatus() {
		return activeStatus;
	}
}