package com.linyang.energy.model;

import java.io.Serializable;

/**
 * Created by Administrator on 18-12-11.
 */
public class EnviManageBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long relateId;

    private Long ledgerId;              //所属企业ID

    private String ledgerName;          //所属企业名

    private Long polluteId;              //产污ledger_id

    private String polluteName;         //产污名

    private String polluteTypeName;     //产污类型名

    private Double polluteVal;          //产污启动阀值

    private Long controlId;              //治污ledger_id

    private String controlName;         //治污名

    private String controlTypeName;     //治污类型名

    private Double controlVal;          //治污启动阀值


    public Long getRelateId() {
        return relateId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getPolluteName() {
        return polluteName;
    }

    public void setPolluteName(String polluteName) {
        this.polluteName = polluteName;
    }

    public String getPolluteTypeName() {
        return polluteTypeName;
    }

    public void setPolluteTypeName(String polluteTypeName) {
        this.polluteTypeName = polluteTypeName;
    }

    public Double getPolluteVal() {
        return polluteVal;
    }

    public void setPolluteVal(Double polluteVal) {
        this.polluteVal = polluteVal;
    }

    public String getControlName() {
        return controlName;
    }

    public void setControlName(String controlName) {
        this.controlName = controlName;
    }

    public String getControlTypeName() {
        return controlTypeName;
    }

    public void setControlTypeName(String controlTypeName) {
        this.controlTypeName = controlTypeName;
    }

    public Double getControlVal() {
        return controlVal;
    }

    public void setControlVal(Double controlVal) {
        this.controlVal = controlVal;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Long getPolluteId() {
        return polluteId;
    }

    public void setPolluteId(Long polluteId) {
        this.polluteId = polluteId;
    }

    public Long getControlId() {
        return controlId;
    }

    public void setControlId(Long controlId) {
        this.controlId = controlId;
    }
}
