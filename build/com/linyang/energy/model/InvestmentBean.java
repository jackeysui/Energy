package com.linyang.energy.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class InvestmentBean implements Serializable {
	/**
	 * 项目id
	 */
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
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

    public BigDecimal getPredictInvest() {
        return predictInvest;
    }

    public void setPredictInvest(BigDecimal predictInvest) {
        this.predictInvest = predictInvest;
    }

    public BigDecimal getActualInvest() {
        return actualInvest;
    }

    public void setActualInvest(BigDecimal actualInvest) {
        this.actualInvest = actualInvest;
    }

    public BigDecimal getPredictSave() {
        return predictSave;
    }

    public void setPredictSave(BigDecimal predictSave) {
        this.predictSave = predictSave;
    }

    public BigDecimal getActualSave() {
        return actualSave;
    }

    public void setActualSave(BigDecimal actualSave) {
        this.actualSave = actualSave;
    }

    public BigDecimal getPredictReturn() {
        return predictReturn;
    }

    public void setPredictReturn(BigDecimal predictReturn) {
        this.predictReturn = predictReturn;
    }

    public BigDecimal getActualReturn() {
        return actualReturn;
    }

    public void setActualReturn(BigDecimal actualReturn) {
        this.actualReturn = actualReturn;
    }
}