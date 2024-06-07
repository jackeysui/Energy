package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户足迹表
 * @author guosen
 *
 */
public class AccountTraceBean implements Serializable{

	private static final long serialVersionUID = -466813637273008682L;

	private Long traceId;
	
	private Long accountId;
	
	private Date operateTime;
	
	private Long moduleId;
	
	private Long operItemId;

    private int operClient;

	public Long getTraceId() {
		return traceId;
	}

	public void setTraceId(Long traceId) {
		this.traceId = traceId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getOperItemId() {
		return operItemId;
	}

	public void setOperItemId(Long operItemId) {
		this.operItemId = operItemId;
	}

    public int getOperClient() {
        return operClient;
    }

    public void setOperClient(int operClient) {
        this.operClient = operClient;
    }
}
