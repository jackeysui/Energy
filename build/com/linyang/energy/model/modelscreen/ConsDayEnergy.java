package com.linyang.energy.model.modelscreen;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConsDayEnergy implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String consNo;

    /**
     * 数据日期，格式 yyyy-MM-dd
     */
    private String dataDate;

    /**
     * 有功总电量
     */
    private BigDecimal papE;

    /**
     * 有功尖电量
     */
    private BigDecimal papE1;

    /**
     * 有功峰电量
     */
    private BigDecimal papE2;

    /**
     * 有功平电量
     */
    private BigDecimal papE3;

    /**
     * 有功谷电量
     */
    private BigDecimal papE4;

    public String getConsNo() {
        return consNo;
    }

    public void setConsNo(String consNo) {
        this.consNo = consNo;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public BigDecimal getPapE() {
        return papE;
    }

    public void setPapE(BigDecimal papE) {
        this.papE = papE;
    }

    public BigDecimal getPapE1() {
        return papE1;
    }

    public void setPapE1(BigDecimal papE1) {
        this.papE1 = papE1;
    }

    public BigDecimal getPapE2() {
        return papE2;
    }

    public void setPapE2(BigDecimal papE2) {
        this.papE2 = papE2;
    }

    public BigDecimal getPapE3() {
        return papE3;
    }

    public void setPapE3(BigDecimal papE3) {
        this.papE3 = papE3;
    }

    public BigDecimal getPapE4() {
        return papE4;
    }

    public void setPapE4(BigDecimal papE4) {
        this.papE4 = papE4;
    }
}
