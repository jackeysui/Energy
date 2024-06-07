package com.linyang.energy.model;

import java.io.Serializable;

/**
 * 
 * @类功能说明： 
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2013-12-12 上午11:00:16  
 * @版本：V1.0
 */
public class MeterBean implements Serializable {
	/**
	 * 计量点id
	 */
    private Long meterId;
    /**
     * 计量点名称
     */
    private String meterName;
    /**
     * 计量点类型.1:电;2:水;3:气;4:热
     */
    private Short meterType;
    /**
     * 计量点属性.0:总,1:分
     */
    private int meterAttr;
    /**
     * 分户id
     */
    private Long ledgerId;
    /**
     * 终端id
     */
    private Long terminalId;
    /**
     * 计量点id
     */
    private Long mpedId;
    /**
     * 设备集id
     */
    private Long typeId;
    /**
     * 设备类型id
     */
    private Long deviceId;
    /**
     * 累加属性
     */
    private Long attributeId;
    /**
     * 分户类
     */
    private LedgerBean ledger;
    /**
     * 采集点类型名称
     */
    private String meterTypeName;
    /**
     * 采集点状态：1：正常；2：停止；
     */
    private int meterStatus;
    /**
     * 额定电压
     */
    private String ratedVol;
    /**
     * 电压下限
     */
    private String lowerVol;
    /**
     * 额定电流
     */
    private String ratedCur;
    /**
     * 额定功率
     */
    private String ratedPower;
    /**
     * 终端名称
     */
    private String terminalName;
    /**
     * 测量点名称
     */
    private String mpedName;
    
    /**
     * 原所属分户ID
     */
    private String oldLedgerId;
    
    /**
     * 所属分户名称
     */
    private String ledgerName;
        
    /**
     * 资产编号
     */
    private String assetNumber;
    /**
     * 表计地址
     */
    private String ammeterAddress;
    
    /**
     * 费率id
     * add by guosen 2014-9-16
     */
    private Long rateId;
    
    /**
     * PT
     * add by guosen 2014-11-25
     */
    private Double pt;
    
    /**
     * CT
     * add by guosen 2014-11-25
     */
    private Double ct;
    /**
     * 电压等级
     */
    private Long voltageLevel;
    /**
     * 变压器容量
     */
    private Long volume;
    
    /**
     * 线损计算用到
     */
    private Long lineLoss;
    
    /**
     * 线损计算用到
     */
    private String lineLossName;
    
	/**
	 * 设备启动电流
	 */
	private Double startCur;
	
	/**
	 * 变压器类型
	 */
	private Integer volType;
    
    private Float translossFactor;

    /**
     * 是否虚拟点
     */
    private Integer isVirtual;
	
	/**
	 * 水曲线超限阀值
	 */
	private Float waterLimitThres;
    
    private static final long serialVersionUID = 123456789654321L;
    
    
	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}
	
	public String getAmmeterAddress() {
		return ammeterAddress;
	}

	public void setAmmeterAddress(String ammeterAddress) {
		this.ammeterAddress = ammeterAddress;
	}

	public String getOldLedgerId() {
		return oldLedgerId;
	}

	public void setOldLedgerId(String oldLedgerId) {
		this.oldLedgerId = oldLedgerId;
	}

	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getMpedName() {
		return mpedName;
	}

	public void setMpedName(String mpedName) {
		this.mpedName = mpedName;
	}

	public String getRatedVol() {
		return ratedVol;
	}

	public void setRatedVol(String ratedVol) {
		this.ratedVol = ratedVol;
	}

	public String getLowerVol() {
		return lowerVol;
	}

	public void setLowerVol(String lowerVol) {
		this.lowerVol = lowerVol;
	}

	public String getRatedCur() {
		return ratedCur;
	}

	public void setRatedCur(String ratedCur) {
		this.ratedCur = ratedCur;
	}

	public String getRatedPower() {
		return ratedPower;
	}

	public void setRatedPower(String ratedPower) {
		this.ratedPower = ratedPower;
	}

	public String getMeterTypeName() {
		return meterTypeName;
	}

	public void setMeterTypeName(String meterTypeName) {
		this.meterTypeName = meterTypeName;
	}

	public LedgerBean getLedger() {
		return ledger;
	}

	public void setLedger(LedgerBean ledger) {
		this.ledger = ledger;
	}

	public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public String getMeterName() {
        return meterName;
    }

    public void setMeterName(String meterName) {
        this.meterName = meterName == null ? "" : meterName.trim();
    }

    public Short getMeterType() {
        return meterType;
    }

    public void setMeterType(Short meterType) {
        this.meterType = meterType;
    }

    public int getMeterAttr() {
		return meterAttr;
	}

	public void setMeterAttr(int meterAttr) {
		this.meterAttr = meterAttr;
	}

	public int getMeterStatus() {
		return meterStatus;
	}

	public void setMeterStatus(int meterStatus) {
		this.meterStatus = meterStatus;
	}

	public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public Long getMpedId() {
        return mpedId;
    }

    public void setMpedId(Long mpedId) {
        this.mpedId = mpedId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}

	public Double getPt() {
		return pt;
	}

	public void setPt(Double pt) {
		this.pt = pt;
	}

	public Double getCt() {
		return ct;
	}

	public void setCt(Double ct) {
		this.ct = ct;
	}
	

	public Long getVoltageLevel() {
		return voltageLevel;
	}

	public void setVoltageLevel(Long voltageLevel) {
		this.voltageLevel = voltageLevel;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public Long getLineLoss() {
		return lineLoss;
	}

	public void setLineLoss(Long lineLoss) {
		this.lineLoss = lineLoss;
	}

	public String getLineLossName() {
		return lineLossName;
	}

	public void setLineLossName(String lineLossName) {
		this.lineLossName = lineLossName;
	}

	public Double getStartCur() {
		return startCur;
	}

	public void setStartCur(Double startCur) {
		this.startCur = startCur;
	}

	public Integer getVolType() {
		return volType;
	}

	public void setVolType(Integer volType) {
		this.volType = volType;
	}

    public Float getTranslossFactor() {
        return translossFactor;
    }

    public void setTranslossFactor(Float translossFactor) {
        this.translossFactor = translossFactor;
    }

    public Integer getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Integer isVirtual) {
        this.isVirtual = isVirtual;
    }
	
	public Float getWaterLimitThres() {
		return waterLimitThres;
	}
	
	public void setWaterLimitThres(Float waterLimitThres) {
		this.waterLimitThres = waterLimitThres;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((meterId == null) ? 0 : meterId.hashCode());
		result = prime * result
				+ ((meterName == null) ? 0 : meterName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeterBean other = (MeterBean) obj;
		if (meterId == null) {
			if (other.meterId != null)
				return false;
		} else if (!meterId.equals(other.meterId))
			return false;
		if (meterName == null) {
			if (other.meterName != null)
				return false;
		} else if (!meterName.equals(other.meterName))
			return false;
		return true;
	}
}