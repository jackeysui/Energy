package com.linyang.energy.model;

import java.text.ParseException;
import java.util.Date;

import com.leegern.util.DateUtil;

/**
 * @Description 实时曲线bean
 * @author Leegern
 * @date Jan 2, 2014 3:22:17 PM
 */
public class RealCurveBean {
	/**
	 * 计量点Id
	 */
	private long meterId;
	
	/**
	 * 冻结时间
	 */
	private Date freezeTime;
	
	/**
	 * 正向有功
	 */
	private double dataValue;

	/**
	 * 冻结时间
	 */
	private String freezeTimeStr;
	
	/**
	 * 统计时间
	 */
	private String statDate;
	
	/**
	 * 设备类型Id
	 */
	private long typeId;
	
	/**
	 * 设备类型名称
	 */
	private String typeName;
	
	@Override
	public String toString() {
		return "{meterId:" + meterId + ", freezeTime:" + freezeTime + ", dataValue:" + dataValue + ", freezeTimeStr:" + freezeTimeStr + ", typeId:" + typeId + ", typeName:" + typeName + "}";
	}
	
	/**
	 * @return the meterId
	 */
	public long getMeterId() {
		return meterId;
	}

	/**
	 * @param meterId the meterId to set
	 */
	public void setMeterId(long meterId) {
		this.meterId = meterId;
	}

	/**
	 * @return the freezeTime
	 */
	public Date getFreezeTime() {
		return freezeTime;
	}

	/**
	 * @param freezeTime the freezeTime to set
	 * @throws ParseException 
	 */
	public void setFreezeTime(Date freezeTime) throws ParseException {
		this.freezeTime = freezeTime;
		if (null != this.freezeTime) {
			this.freezeTimeStr = DateUtil.convertDateToStr(this.freezeTime, DateUtil.DEFAULT_SHORT_PATTERN);
		}
	}

	/**
	 * @return the dataValue
	 */
	public double getDataValue() {
		return dataValue;
	}

	/**
	 * @param dataValue the dataValue to set
	 */
	public void setDataValue(double dataValue) {
		this.dataValue = dataValue;
	}

	/**
	 * @return the freezeTimeStr
	 */
	public String getFreezeTimeStr() {
		return freezeTimeStr;
	}

	/**
	 * @param freezeTimeStr the freezeTimeStr to set
	 * @throws ParseException 
	 */
	public void setFreezeTimeStr(String freezeTimeStr) throws ParseException {
		if (null != this.freezeTime) {
			this.freezeTimeStr = DateUtil.convertDateToStr(this.freezeTime, DateUtil.DEFAULT_SHORT_PATTERN);
		}
	}

	/**
	 * @return the typeId
	 */
	public long getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the statDate
	 */
	public String getStatDate() {
		return statDate;
	}

	/**
	 * @param statDate the statDate to set
	 */
	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}
}

