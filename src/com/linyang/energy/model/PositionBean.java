package com.linyang.energy.model;

import com.linyang.energy.utils.DataUtil;import com.linyang.util.CommonMethod;

public class PositionBean {
	/**
	 * 分户或者计量点id
	 */
	private long id;
	/**
	 * 类型;1表示分户，2表示计量点，默认是分户
	 */
	private int type = 1;
	/**
	 * X坐标
	 */
	private Double pointX;
	/**
	 * Y坐标
	 */
	private Double pointY;
	/**
	 * 计量点或者分户id
	 */
	private Long picId; 
	
	private String picStr;

	public PositionBean(long id, int type, Double pointX, Double pointY,Long picId) {
		super();
		this.id = id;
		this.type = type;
		this.pointX = pointX;
		this.pointY = pointY;
		this.picId = picId;
	}

	public PositionBean() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Double getPointX() {
		return pointX;
	}

	public void setPointX(Double pointX) {
		this.pointX = pointX;
	}

	public Double getPointY() {
		return pointY;
	}

	public void setPointY(Double pointY) {
		this.pointY = pointY;
	}
	
	public Long getPicId() {
		return picId;
	}

	public void setPicId(Long picId) {
		this.picId = picId;
	}

	public boolean isPointValidate(){
		return (DataUtil.doubleIsNull(this.pointX)) || (DataUtil.doubleIsNull(this.pointY));
	}
	public void setNull2Point(){
		this.pointX = null;
		this.pointY = null;
		this.picId = 0L;
	}

	public String getPicStr() {
		return picStr;
	}

	public void setPicStr(String picStr) {
		this.picStr = picStr;
		if(CommonMethod.isEmpty(picStr))
			this.setPicId(0L);
		else
			this.setPicId(Long.parseLong(picStr));
	}
	
}
