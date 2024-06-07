package com.linyang.energy.model;

import java.math.BigDecimal;import com.linyang.util.DoubleUtils;

public class PowerFactorStatBean {
	/**
	 * 分户或者分项的id
	 */
	private long statId;
	
	private String name;
	/**
	 * 1表示分户2表示分项
	 */
	private int type=1;
	
	private Double nowStat;
	private Double preStat; 
	private Double percentage;
	
	
	public long getStatId() {
		return statId;
	}
	public void setStatId(long statId) {
		this.statId = statId;
	}
	public Double getNowStat() {
		return nowStat;
	}
	public void setNowStat(Double nowStat) {
		this.nowStat = nowStat;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		if(percentage != null)
			this.percentage = DoubleUtils.getDoubleValue(percentage,2);
	}
	public Double getPreStat() {
		return preStat;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * 计算增幅
	 */
	public void calculatePercentage(){
		if(nowStat != null && preStat != null)
			this.percentage = new BigDecimal(nowStat).subtract(new BigDecimal(preStat)).multiply(new BigDecimal(100)).divide(new BigDecimal(preStat), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}

}
