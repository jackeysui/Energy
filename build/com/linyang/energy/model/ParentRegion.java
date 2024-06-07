package com.linyang.energy.model;

import java.util.List;

/**
 * Created by Administrator on 17-5-8.
 */
public class ParentRegion {
    private String regionId;

    private String regionName;

    private int regionLevel;

    private String pRegionId;

    private List<LedgerBean> ledgers;

    private Double curPower;

    private Double lastMonQ;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(int regionLevel) {
        this.regionLevel = regionLevel;
    }

    public String getpRegionId() {
        return pRegionId;
    }

    public void setpRegionId(String pRegionId) {
        this.pRegionId = pRegionId;
    }

    public List<LedgerBean> getLedgers() {
        return ledgers;
    }

    public void setLedgers(List<LedgerBean> ledgers) {
        this.ledgers = ledgers;
    }

    public Double getCurPower() {
        return curPower;
    }

    public void setCurPower(Double curPower) {
        this.curPower = curPower;
    }

    public Double getLastMonQ() {
        return lastMonQ;
    }

    public void setLastMonQ(Double lastMonQ) {
        this.lastMonQ = lastMonQ;
    }
}
