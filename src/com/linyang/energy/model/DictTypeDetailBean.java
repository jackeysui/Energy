package com.linyang.energy.model;

import java.io.Serializable;

public class DictTypeDetailBean implements Serializable {
	/**
     * 明细id
     */
    private Long detailId;
    /**
     * 明细名称
     */
    private String detailName;
    /**
     * 明细名称
     */
    private Short status;
    /**
     * 描述信息
     */
    private String typeRemark;
    /**
     * 顺序序号
     */
    private Short serialNumber;
    /**
     * 大类id
     */
    private Long cateId;

    private static final long serialVersionUID = 1L;

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName == null ? null : detailName.trim();
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }


    /**
	 * @return the typeRemark
	 */
	public String getTypeRemark() {
		return typeRemark;
	}

	/**
	 * @param typeRemark the typeRemark to set
	 */
	public void setTypeRemark(String typeRemark) {
		this.typeRemark = typeRemark;
	}

	public Short getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Short serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getCateId() {
        return cateId;
    }

    public void setCateId(Long cateId) {
        this.cateId = cateId;
    }
}