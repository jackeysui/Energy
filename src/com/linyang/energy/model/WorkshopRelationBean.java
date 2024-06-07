package com.linyang.energy.model;

import java.io.Serializable;

/**
 * 车间emo_dcp关联关系表
 * @author Administrator
 *
 */
public class WorkshopRelationBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 车间id
	 */
	private Long workshopId;
	
	/**
	 * 能管对象/采集点id
	 */
	private Long emoDcpId;
	
	/**
	 * 类型：1-能管对象；2-采集点
	 */
	private int type;
	
	/**
	 * 类型：1-能管对象
	 */
	public static final int TYPE_EMO = 1;
	
	/**
	 * 类型：2-采集点
	 */
	public static final int TYPE_DCP = 2;

	public Long getWorkshopId() {
		return workshopId;
	}

	public void setWorkshopId(Long workshopId) {
		this.workshopId = workshopId;
	}

	public Long getEmoDcpId() {
		return emoDcpId;
	}

	public void setEmoDcpId(Long emoDcpId) {
		this.emoDcpId = emoDcpId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
