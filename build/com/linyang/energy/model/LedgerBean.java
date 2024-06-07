package com.linyang.energy.model;

import java.io.Serializable;

/**
 * @Description 分户Bean
 * @author Leegern
 * @date Dec 9, 2013 5:22:39 PM
 */
public class LedgerBean implements Serializable {
	
	/**
	 * 普通用户
	 */
	public static final int LEDGER_TYPE_NORMAL = 1;
	
	/**
	 * 分户类型：VIP
	 */
	public static final int LEDGER_TYPE_VIP = 2;
	
    /**
	 * 序列号
	 */
	private static final long serialVersionUID = 1147028903128587179L;
	
	/**
	 * 分户id
	 */
	private Long ledgerId;
	
	/**
	 * 分户名称
	 */
    private String ledgerName;
    
    /**
     * 分户类型
     */
    private Integer ledgerType;
    
    /**
     * 人数
     */
    private Short numberOfPeople = 1;
    
    /**
     * 面积
     */
    private Integer useArea = 1;
    
    /**
     * 深度
     */
    private Integer depth;
    
    /**
     * 左范围
     */
    private Integer ledgerLft;
    
    /**
     * 右范围
     */
    private Integer ledgerRgt;
    
    /**
     * 父分户id
     */
    private Long parentLedgerId;
    
    /**
     * 父分户名称
     */
    private String parentLedgerName;
    
    /**
     * 采集点数量
     */
    private Integer collmeterNumber;
    
    /**
     * 费率id
     */
    private Long rateId;
    
    /**
     * 分户描述
     */
    private String ledgerRemark;
    
    /**
     * 阀值id
     */
    private long thresholdId;
    
    /**
     * 阀值名称
     */
    private String thresholdName;
    
    /**
     * 阀值设置值
     */
    private String thresholdValue;
    
    /**
     * 横坐标
     */
    private Double x;
    
    /**
     * 纵坐标
     */
    private Double y;
    
    /**
     * 分户关联图片的URL
     */
    private long picId;
    
    /**
     * 分户分析类型
     */
    private Integer analyType;
    
    /**
     * 是否继承父分户费率配置,0:否;1:是
     */
    private int inherit = -1;
    
    /**************add by chengq 140811 二期新增字段*****************/
    /**
     * 用户号
     */
    private String userNo;
    /**
     * 所属行业
     */
    private String industryType;
    /**
     * 所属行业名称
     */
    private String industryName;
    /**
     * 所属区域编码
     */
    private String region;
    /**
     * 所属区域名称
     */
    private String regionName;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系方式
     */
    private String contactInfo;
    /**
     * 地址
     */
    private String address;
    
    /**
     * 线损配置
     */
    private Integer lineloss;

    /**
     * 累加属性
     */
    private Integer addAttr;

    /**
     * 所属企业
     */
    private String company;
    
    /**
     * 变压器总容量
     */
    private Long volume;
    
    /**************add by chengq 20160330 第三方新增字段*****************/
    /**
     * 背景颜色
     */
    private String logoColor;
    /**
     * 图标
     */
    private String logoIcon;
    
    /**************add by xues 20160425 四表集抄新增字段*****************/
    /**
     * 费率id--水
     */
    private Long rateWId;
    /**
     * 费率id--气
     */
    private Long rateGId;
    
    /**
     * 费率id--热
     */
    private Long rateHId;

    /************** 20160606 地图新增字段*****************/
    private String showPicUrl;
    
    /************** 20160627 能效新增字段*****************/
    
    /**
     * 第三方网页链接
     */
    private String url;
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
     * 能源类型
     */
    private Integer energyType;

    /**
     *能管对象启动功率(kW)
     */
    private double startPower;

    /**
     * 能管对象基准能耗(kWh)
     */
    private Double baseQ;
    
    /************** 20180509 能效3.8首页-企业列表*****************/
    /** 分户下电表总数量 */
    private Integer allMeterNum;
    
    /** 分户下电表离线数量 */
    private Integer offLineMeterNum;
    
    /** 企业运营商 */
    private String operator;

    /** 增加父节点分类*/
    private Integer parentAnalyType;

	/************** end *****************/
	
	/************** 20190122 能效4.4结构树配置*****************/
	/**
	 * 设备类型 EQPTYPE_ID
	 */
	private Integer eqpTypeId;
	
	private String eqpTypeName;
	
	//水日限额
	private Double waterLimitThres;
	
	//气日限额
	private Double gasLimitThres;
	
	//电日限额
	private Double electLimitThres;
	
	/**
	 * 能管对象报表结算日
	 */
	private Integer qCalcDate;
	
	/**
	 * 是否继承父分户结算日配置,0:否;1:是
	 */
	private int qCalcInherit = -1;
	
	/************** 20181207 环保1.0-企业下信息*****************/
	private String pollutId;
	
	private String pollutctlId;
	
	private String pollutName;
	
	private String pollutctlName;
	
	//产污类型(作为标记0产污 1治污 2都不是)
	private Integer pollutType;
	
	//设施是否可设置产污/治污
	private boolean canSet;
	
	//设施是否可以删除
	private boolean isDelete;
	
	/************** end *****************/
	
	public Integer getqCalcDate() {
		return qCalcDate;
	}
	
	public void setqCalcDate(Integer qCalcDate) {
		this.qCalcDate = qCalcDate;
	}
	
	public int getqCalcInherit() {
		return qCalcInherit;
	}
	
	public void setqCalcInherit(int qCalcInherit) {
		this.qCalcInherit = qCalcInherit;
	}
	
	public Integer getEqpTypeId() {
		return eqpTypeId;
	}
	
	public void setEqpTypeId(Integer eqpTypeId) {
		this.eqpTypeId = eqpTypeId;
	}
	
	public String getEqpTypeName() {
		return eqpTypeName;
	}
	
	public void setEqpTypeName(String eqpTypeName) {
		this.eqpTypeName = eqpTypeName;
	}
	
	/************** end *****************/
	
	public Integer getParentAnalyType() {
		return parentAnalyType;
	}

	public void setParentAnalyType(Integer parentAnalyType) {
		this.parentAnalyType = parentAnalyType;
	}

	public String getLogoColor() {
		return logoColor;
	}

	public void setLogoColor(String logoColor) {
		this.logoColor = logoColor;
	}

	public String getLogoIcon() {
		return logoIcon;
	}

	public void setLogoIcon(String logoIcon) {
		this.logoIcon = logoIcon;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}
	
	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the parentLedgerName
	 */
	public String getParentLedgerName() {
		return parentLedgerName;
	}

	/**
	 * @param parentLedgerName the parentLedgerName to set
	 */
	public void setParentLedgerName(String parentLedgerName) {
		this.parentLedgerName = parentLedgerName;
	}

	/**
	 * @return the x
	 */
	public Double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public Double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the picId
	 */
	public long getPicId() {
		return picId;
	}

	/**
	 * @param picId the picId to set
	 */
	public void setPicId(long picId) {
		this.picId = picId;
	}

	/**
	 * @return the analyType
	 */
	public Integer getAnalyType() {
		return analyType;
	}

	/**
	 * @param analyType the analyType to set
	 */
	public void setAnalyType(Integer analyType) {
		this.analyType = analyType;
	}

	/**
	 * @return the inherit
	 */
	public int getInherit() {
		return inherit;
	}

	/**
	 * @param inherit the inherit to set
	 */
	public void setInherit(int inherit) {
		this.inherit = inherit;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName == null ? "" : ledgerName.trim();
    }

    public Integer getLedgerType() {
		return ledgerType;
	}

	public void setLedgerType(Integer ledgerType) {
		this.ledgerType = ledgerType;
	}

	public Short getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Short numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Integer getUseArea() {
        return useArea;
    }

    public void setUseArea(Integer useArea) {
        this.useArea = useArea;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Integer getLedgerLft() {
        return ledgerLft;
    }

    public void setLedgerLft(Integer ledgerLft) {
        this.ledgerLft = ledgerLft;
    }

    public Integer getLedgerRgt() {
        return ledgerRgt;
    }

    public void setLedgerRgt(Integer ledgerRgt) {
        this.ledgerRgt = ledgerRgt;
    }

    public Long getParentLedgerId() {
        return parentLedgerId;
    }

    public void setParentLedgerId(Long parentLedgerId) {
        this.parentLedgerId = parentLedgerId;
    }

    public Integer getCollmeterNumber() {
        return collmeterNumber;
    }

    public void setCollmeterNumber(Integer collmeterNumber) {
        this.collmeterNumber = collmeterNumber;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public String getLedgerRemark() {
        return ledgerRemark;
    }

    public void setLedgerRemark(String ledgerRemark) {
        this.ledgerRemark = ledgerRemark == null ? "" : ledgerRemark.trim();
    }

	/**
	 * @return the thresholdId
	 */
	public long getThresholdId() {
		return thresholdId;
	}

	/**
	 * @param thresholdId the thresholdId to set
	 */
	public void setThresholdId(long thresholdId) {
		this.thresholdId = thresholdId;
	}

	/**
	 * @return the thresholdName
	 */
	public String getThresholdName() {
		return thresholdName;
	}

	/**
	 * @param thresholdName the thresholdName to set
	 */
	public void setThresholdName(String thresholdName) {
		this.thresholdName = thresholdName;
	}

	/**
	 * @return the thresholdValue
	 */
	public String getThresholdValue() {
		return thresholdValue;
	}

	/**
	 * @param thresholdValue the thresholdValue to set
	 */
	public void setThresholdValue(String thresholdValue) {
		this.thresholdValue = thresholdValue == null ? "" : thresholdValue.trim();
	}

	public Integer getLineloss() {
		return lineloss;
	}

	public void setLineloss(Integer lineloss) {
		this.lineloss = lineloss;
	}

    public Integer getAddAttr() {
        return addAttr;
    }

    public void setAddAttr(Integer addAttr) {
        this.addAttr = addAttr;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public Long getVolume() {
		return volume;
	}

    public Long getRateWId() {
        return rateWId;
    }

    public void setRateWId(Long rateWId) {
        this.rateWId = rateWId;
    }

    public Long getRateGId() {
        return rateGId;
    }

    public void setRateGId(Long rateGId) {
        this.rateGId = rateGId;
    }

    public Long getRateHId() {
        return rateHId;
    }

    public void setRateHId(Long rateHId) {
        this.rateHId = rateHId;
    }

    public String getShowPicUrl() {
        return showPicUrl;
    }

    public void setShowPicUrl(String showPicUrl) {
        this.showPicUrl = showPicUrl;
    }

    public Integer getEnergyType() {
        return energyType;
    }

    public void setEnergyType(Integer energyType) {
        this.energyType = energyType;
    }

	public Integer getOffLineMeterNum() {
		return offLineMeterNum;
	}

	public void setOffLineMeterNum(Integer offLineMeterNum) {
		this.offLineMeterNum = offLineMeterNum;
	}

	public Integer getAllMeterNum() {
		return allMeterNum;
	}

	public void setAllMeterNum(Integer allMeterNum) {
		this.allMeterNum = allMeterNum;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

    public double getStartPower() {
        return startPower;
    }

    public void setStartPower(double startPower) {
        this.startPower = startPower;
    }

    public Double getBaseQ() {
        return baseQ;
    }

    public void setBaseQ(Double baseQ) {
        this.baseQ = baseQ;
    }
	
	public Double getWaterLimitThres() {
		return waterLimitThres;
	}
	
	public void setWaterLimitThres(Double waterLimitThres) {
		this.waterLimitThres = waterLimitThres;
	}
	
	public Double getGasLimitThres() {
		return gasLimitThres;
	}
	
	public void setGasLimitThres(Double gasLimitThres) {
		this.gasLimitThres = gasLimitThres;
	}
	
	public Double getElectLimitThres() {
		return electLimitThres;
	}
	
	public void setElectLimitThres(Double electLimitThres) {
		this.electLimitThres = electLimitThres;
	}
	
	
	// 环保配置
	public String getPollutId() {
		return pollutId;
	}
	
	public void setPollutId(String pollutId) {
		this.pollutId = pollutId;
	}
	
	public String getPollutctlId() {
		return pollutctlId;
	}
	
	public void setPollutctlId(String pollutctlId) {
		this.pollutctlId = pollutctlId;
	}
	
	public String getPollutName() {
		return pollutName;
	}
	
	public void setPollutName(String pollutName) {
		this.pollutName = pollutName;
	}
	
	public String getPollutctlName() {
		return pollutctlName;
	}
	
	public void setPollutctlName(String pollutctlName) {
		this.pollutctlName = pollutctlName;
	}
	
	public Integer getPollutType() {
		return pollutType;
	}
	
	public void setPollutType(Integer pollutType) {
		this.pollutType = pollutType;
	}
	
	public boolean isCanSet() {
		return canSet;
	}
	
	public void setCanSet(boolean canSet) {
		this.canSet = canSet;
	}
	
	public boolean isDelete() {
		return isDelete;
	}
	
	public void setDelete(boolean delete) {
		isDelete = delete;
	}
}