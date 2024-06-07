package com.linyang.energy.model.modelscreen;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConsMonthAmt implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    private String consNo;

    /**
     * 电费月份
     */
    private String ym;

    /**
     * 总结算电量
     */
    private BigDecimal tSettlePq;

    /**
     * 总结算电费
     */
    private BigDecimal tAmt;

    /**
     * 尖结算电量
     */
    private BigDecimal jSettlePq;

    /**
     * 尖结算电费
     */
    private BigDecimal jAmt;

    /**
     * 峰结算电量
     */
    private BigDecimal fSettlePq;

    /**
     * 峰结算电费
     */
    private BigDecimal fAmt;

    /**
     * 平结算电量
     */
    private BigDecimal pSettlePq;

    /**
     * 平结算电费
     */
    private BigDecimal pAmt;

    /**
     * 谷结算电量
     */
    private BigDecimal gSettlePq;

    /**
     * 谷结算电费
     */
    private BigDecimal gAmt;

    /**
     * 力率调整系数
     */
    private BigDecimal adjFactor;

    /**
     * 力率调整电费
     */
    private BigDecimal pfAdjAmt;

    /**
     * 基本电费分类（容量、需量）
     */
    private String baTypeCode;

    /**
     * 基本计费电量
     */
    private BigDecimal baValue;

    /**
     * 基本电费
     */
    private BigDecimal ba;

    /**
     * 上月总电量
     */
    private BigDecimal preTSettlePq;

    /**
     * 上月总电费
     */
    private BigDecimal preTAmt;

    /**
     * 电费均价，总结算电费 / 总结算电量
     */
    private BigDecimal avgAmt;

    /**
     * 均价离散 X轴坐标
     */
    private BigDecimal posX;

    /**
     * 均价离散 Y轴坐标
     */
    private BigDecimal posY;

    public String getConsNo() {
        return consNo;
    }

    public void setConsNo(String consNo) {
        this.consNo = consNo;
    }

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public BigDecimal gettSettlePq() {
        return tSettlePq;
    }

    public void settSettlePq(BigDecimal tSettlePq) {
        this.tSettlePq = tSettlePq;
    }

    public BigDecimal gettAmt() {
        return tAmt;
    }

    public void settAmt(BigDecimal tAmt) {
        this.tAmt = tAmt;
    }

    public BigDecimal getjSettlePq() {
        return jSettlePq;
    }

    public void setjSettlePq(BigDecimal jSettlePq) {
        this.jSettlePq = jSettlePq;
    }

    public BigDecimal getjAmt() {
        return jAmt;
    }

    public void setjAmt(BigDecimal jAmt) {
        this.jAmt = jAmt;
    }

    public BigDecimal getfSettlePq() {
        return fSettlePq;
    }

    public void setfSettlePq(BigDecimal fSettlePq) {
        this.fSettlePq = fSettlePq;
    }

    public BigDecimal getfAmt() {
        return fAmt;
    }

    public void setfAmt(BigDecimal fAmt) {
        this.fAmt = fAmt;
    }

    public BigDecimal getpSettlePq() {
        return pSettlePq;
    }

    public void setpSettlePq(BigDecimal pSettlePq) {
        this.pSettlePq = pSettlePq;
    }

    public BigDecimal getpAmt() {
        return pAmt;
    }

    public void setpAmt(BigDecimal pAmt) {
        this.pAmt = pAmt;
    }

    public BigDecimal getgSettlePq() {
        return gSettlePq;
    }

    public void setgSettlePq(BigDecimal gSettlePq) {
        this.gSettlePq = gSettlePq;
    }

    public BigDecimal getgAmt() {
        return gAmt;
    }

    public void setgAmt(BigDecimal gAmt) {
        this.gAmt = gAmt;
    }

    public BigDecimal getAdjFactor() {
        return adjFactor;
    }

    public void setAdjFactor(BigDecimal adjFactor) {
        this.adjFactor = adjFactor;
    }

    public BigDecimal getPfAdjAmt() {
        return pfAdjAmt;
    }

    public void setPfAdjAmt(BigDecimal pfAdjAmt) {
        this.pfAdjAmt = pfAdjAmt;
    }

    public String getBaTypeCode() {
        return baTypeCode;
    }

    public void setBaTypeCode(String baTypeCode) {
        this.baTypeCode = baTypeCode;
    }

    public BigDecimal getBaValue() {
        return baValue;
    }

    public void setBaValue(BigDecimal baValue) {
        this.baValue = baValue;
    }

    public BigDecimal getBa() {
        return ba;
    }

    public void setBa(BigDecimal ba) {
        this.ba = ba;
    }

    public BigDecimal getPreTSettlePq() {
        return preTSettlePq;
    }

    public void setPreTSettlePq(BigDecimal preTSettlePq) {
        this.preTSettlePq = preTSettlePq;
    }

    public BigDecimal getPreTAmt() {
        return preTAmt;
    }

    public void setPreTAmt(BigDecimal preTAmt) {
        this.preTAmt = preTAmt;
    }

    public BigDecimal getAvgAmt() {
        return avgAmt;
    }

    public void setAvgAmt(BigDecimal avgAmt) {
        this.avgAmt = avgAmt;
    }

    public BigDecimal getPosX() {
        return posX;
    }

    public void setPosX(BigDecimal posX) {
        this.posX = posX;
    }

    public BigDecimal getPosY() {
        return posY;
    }

    public void setPosY(BigDecimal posY) {
        this.posY = posY;
    }
}
