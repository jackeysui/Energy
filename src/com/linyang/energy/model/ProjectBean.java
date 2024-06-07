package com.linyang.energy.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.linyang.energy.utils.DateUtil;
import com.linyang.util.CommonMethod;

public class ProjectBean implements Serializable {
	/**
	 * 项目id
	 */
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目负责人
     */
    private String projectAdmin;
    /**
     * 对象id
     */
    private Long objectId;
    /**
     * 对象名称
     */
    private String objectName;
    /**
     * 开始时间
     */
    private Date beginTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 开始时间String
     */
    private String beginTimeOne;
    /**
     * 结束时间String
     */
    private String endTimeOne;
    /**
     * 施工单位
     */
    private String carryUnit;
    /**
     * 监理单位
     */
    private String regularUnit;
    /**
     * 项目描述
     */
    private String projectRemark;
    /**
     * 预计总投资
     */
    private BigDecimal predictInvest;
    /**
     * 实际总投资
     */
    private BigDecimal actualInvest;
    /**
     * 预计节省
     */
    private BigDecimal predictSave;
    /**
     * 实际节省
     */
    private BigDecimal actualSave;
    /**
     * 预计回报
     */
    private BigDecimal predictReturn;
    /**
     * 实际回报
     */
    private BigDecimal actualReturn;


    private static final long serialVersionUID = 1L;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectAdmin() {
        return projectAdmin;
    }

    public void setProjectAdmin(String projectAdmin) {
        this.projectAdmin = projectAdmin == null ? null : projectAdmin.trim();
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName == null ? null : objectName.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCarryUnit() {
        return carryUnit;
    }

    public void setCarryUnit(String carryUnit) {
        this.carryUnit = carryUnit == null ? null : carryUnit.trim();
    }

    public String getRegularUnit() {
        return regularUnit;
    }

    public void setRegularUnit(String regularUnit) {
        this.regularUnit = regularUnit == null ? null : regularUnit.trim();
    }

    public String getProjectRemark() {
        return projectRemark;
    }

    public void setProjectRemark(String projectRemark) {
        this.projectRemark = projectRemark == null ? null : projectRemark.trim();
    }
    
    /**
	 * @return the predictInvest
	 */
	public BigDecimal getPredictInvest() {
		return predictInvest;
	}

	/**
	 * @param predictInvest the predictInvest to set
	 */
	public void setPredictInvest(BigDecimal predictInvest) {
		this.predictInvest = predictInvest;
	}

	/**
	 * @return the actualInvest
	 */
	public BigDecimal getActualInvest() {
		return actualInvest;
	}

	/**
	 * @param actualInvest the actualInvest to set
	 */
	public void setActualInvest(BigDecimal actualInvest) {
		this.actualInvest = actualInvest;
	}

	/**
	 * @return the predictSave
	 */
	public BigDecimal getPredictSave() {
		return predictSave;
	}

	/**
	 * @param predictSave the predictSave to set
	 */
	public void setPredictSave(BigDecimal predictSave) {
		this.predictSave = predictSave;
	}

	/**
	 * @return the actualSave
	 */
	public BigDecimal getActualSave() {
		return actualSave;
	}

	/**
	 * @param actualSave the actualSave to set
	 */
	public void setActualSave(BigDecimal actualSave) {
		this.actualSave = actualSave;
	}

	/**
	 * @return the predictReturn
	 */
	public BigDecimal getPredictReturn() {
		return predictReturn;
	}

	/**
	 * @param predictReturn the predictReturn to set
	 */
	public void setPredictReturn(BigDecimal predictReturn) {
		this.predictReturn = predictReturn;
	}

	/**
	 * @return the actualReturn
	 */
	public BigDecimal getActualReturn() {
		return actualReturn;
	}

	/**
	 * @param actualReturn the actualReturn to set
	 */
	public void setActualReturn(BigDecimal actualReturn) {
		this.actualReturn = actualReturn;
	}

	/**
	 * @return the beginTimeOne
	 */
	public String getBeginTimeOne() {
		return beginTimeOne;
	}

	/**
	 * @param beginTimeOne the beginTimeOne to set
	 */
	public void setBeginTimeOne(String beginTimeOne) {
		this.beginTimeOne = beginTimeOne;
		if(CommonMethod.isNotEmpty(beginTimeOne)){
			this.setBeginTime(DateUtil.parseDate(beginTimeOne,"yyyy-MM-dd"));
		}
	}

	/**
	 * @return the endTimeOne
	 */
	public String getEndTimeOne() {
		return endTimeOne;
	}

	/**
	 * @param endTimeOne the endTimeOne to set
	 */
	public void setEndTimeOne(String endTimeOne) {
		this.endTimeOne = endTimeOne;
		if(CommonMethod.isNotEmpty(endTimeOne)){
			this.setEndTime(DateUtil.parseDate(endTimeOne,"yyyy-MM-dd"));
		}
	}

	@Override
	public String toString() {
		return "ProjectBean [projectId=" + projectId + ", projectName="
				+ projectName 
				+ "]";
	}
}