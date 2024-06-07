package com.linyang.energy.model;

import java.util.Date;

/**
 * @Description 曲线数据bean
 * @author Leegern
 * @date Jan 7, 2014 5:06:31 PM
 */
public class CurveBean {
	
	/**
	 * 数据类型：电压=1
	 */
	public static final int CURVE_TYPE_V = 1;
	
	/**
	 * 数据类型：电流=2
	 */
	public static final int CURVE_TYPE_I = 2;
	
	/**
	 * 数据类型：有功功率=3
	 */
	public static final int CURVE_TYPE_AP = 3;
	
	/**
	 * 数据类型：无功功率=4
	 */
	public static final int CURVE_TYPE_RP = 4;
	
	/**
	 * 数据类型：功率因数=5
	 */
	public static final int CURVE_TYPE_PF = 5;
	
	/**
	 * 数据类型：电量=6
	 */
	public static final int CURVE_TYPE_ELE = 6;
	
	/**
	 * 数据类型：需量=7
	 */
	public static final int CURVE_TYPE_D = 7;

    /**
     * 数据类型：电压相位角=8
     */
    public static final int CURVE_TYPE_V_DU = 8;

    /**
     * 数据类型：电压相位角=9
     */
    public static final int CURVE_TYPE_I_DU = 9;

    /**
     * 数据类型：电网频率=10
     */
    public static final int CURVE_TYPE_FREQ = 10;

    /**
     * 数据类型：水量=11
     */
    public static final int CURVE_TYPE_WATER = 11;
    
	/**
	 * 计量点id
	 */
	private long meterId;
	
	/**
	 * 冻结时间
	 */
	private Date freezeTime;
	
	/**
	 * A相曲线
	 */
	private Double a;
	
	/**
	 * B相曲线
	 */
	private Double b;
	
	/**
	 * C相曲线
	 */
	private Double c;
	
	/**
	 * 总线
	 */
	private Double d;
	
	/**
	 * A相有功
	 */
	private Double apA;
	
	/**
	 * A相无功
	 */
	private Double rpA;
	
	/**
	 * B相有功
	 */
	private Double apB;
	
	/**
	 * B相无功
	 */
	private Double rpB;
	
	/**
	 * C相有功
	 */
	private Double apC;
	
	/**
	 * C相无功
	 */
	private Double rpC;
	
	/**
	 * 总线有功
	 */
	private Double apD;
	
	/**
	 * 总线无功
	 */
	private Double rpD;
	
	/**
	 * 当前行最大值
	 */
	private double dm;

	/**
	 * 电压最大值
	 */
	private double max;
	
	/**
	 * 电压最大值发生时间
	 */
	private Date maxTime;
	
	/**
	 * 电压最小值
	 */
	private double min;
	
	/**
	 * 电压最小值发生时间
	 */
	private Date minTime;
	
	/**
	 * 电压越上限月累计时间
	 */
	private int upperTime;
	
	/**
	 * 电压越下限月累计时间
	 */
	private int lowerTime;
	
	/**
	 * 电压超限率
	 */
	private double limitRate;
	
	/**
	 * 电压合格率
	 */
	private double fpy;
	
	/**
	 * 电压监测时间
	 */
	private int monTime;
	
	/**
	 * 申报md
	 */
	private Double md;
	
	/**
	 * 最大需量超过 需量申报值*（1+超罚限值）
	 */
	private boolean hasOver;
	
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
	 */
	public void setFreezeTime(Date freezeTime) {
		this.freezeTime = freezeTime;
	}

	/**
	 * @return the a
	 */
	public Double getA() {
		return a;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(Double a) {
		this.a = a;
	}

	/**
	 * @return the b
	 */
	public Double getB() {
		return b;
	}

	/**
	 * @param b the b to set
	 */
	public void setB(Double b) {
		this.b = b;
	}

	/**
	 * @return the c
	 */
	public Double getC() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public void setC(Double c) {
		this.c = c;
	}

	/**
	 * @return the d
	 */
	public Double getD() {
		return d;
	}

	/**
	 * @param d the d to set
	 */
	public void setD(Double d) {
		this.d = d;
	}

	/**
	 * @return the max
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * @return the maxTime
	 */
	public Date getMaxTime() {
		return maxTime;
	}

	/**
	 * @param maxTime the maxTime to set
	 */
	public void setMaxTime(Date maxTime) {
		this.maxTime = maxTime;
	}

	/**
	 * @return the min
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 * @return the minTime
	 */
	public Date getMinTime() {
		return minTime;
	}

	/**
	 * @param minTime the minTime to set
	 */
	public void setMinTime(Date minTime) {
		this.minTime = minTime;
	}

	/**
	 * @return the upperTime
	 */
	public int getUpperTime() {
		return upperTime;
	}

	/**
	 * @param upperTime the upperTime to set
	 */
	public void setUpperTime(int upperTime) {
		this.upperTime = upperTime;
	}

	/**
	 * @return the lowerTime
	 */
	public int getLowerTime() {
		return lowerTime;
	}

	/**
	 * @param lowerTime the lowerTime to set
	 */
	public void setLowerTime(int lowerTime) {
		this.lowerTime = lowerTime;
	}

	/**
	 * @return the limitRate
	 */
	public double getLimitRate() {
		return limitRate;
	}

	/**
	 * @param limitRate the limitRate to set
	 */
	public void setLimitRate(double limitRate) {
		this.limitRate = limitRate;
	}

	/**
	 * @return the fpy
	 */
	public double getFpy() {
		return fpy;
	}

	/**
	 * @param fpy the fpy to set
	 */
	public void setFpy(double fpy) {
		this.fpy = fpy;
	}

	/**
	 * @return the monTime
	 */
	public int getMonTime() {
		return monTime;
	}

	/**
	 * @param monTime the monTime to set
	 */
	public void setMonTime(int monTime) {
		this.monTime = monTime;
	}

	public double getDm() {
		return dm;
	}

	public void setDm(double dm) {
		this.dm = dm;
	}

	public Double getMd() {
		return md;
	}

	public void setMd(Double md) {
		this.md = md;
	}

	public boolean isHasOver() {
		return hasOver;
	}

	public void setHasOver(boolean hasOver) {
		this.hasOver = hasOver;
	}
	
	public Double getApA() {
		return apA;
	}

	public void setApA(Double apA) {
		this.apA = apA;
	}

	public Double getRpA() {
		return rpA;
	}

	public void setRpA(Double rpA) {
		this.rpA = rpA;
	}

	public Double getApB() {
		return apB;
	}

	public void setApB(Double apB) {
		this.apB = apB;
	}

	public Double getRpB() {
		return rpB;
	}

	public void setRpB(Double rpB) {
		this.rpB = rpB;
	}

	public Double getApC() {
		return apC;
	}

	public void setApC(Double apC) {
		this.apC = apC;
	}

	public Double getRpC() {
		return rpC;
	}

	public void setRpC(Double rpC) {
		this.rpC = rpC;
	}

	public Double getApD() {
		return apD;
	}

	public void setApD(Double apD) {
		this.apD = apD;
	}

	public Double getRpD() {
		return rpD;
	}

	public void setRpD(Double rpD) {
		this.rpD = rpD;
	}
}
