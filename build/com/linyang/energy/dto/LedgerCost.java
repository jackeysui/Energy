package com.linyang.energy.dto;

import com.linyang.energy.utils.DataUtil;import com.linyang.util.DoubleUtils;

/**
 * 分户费用bean
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 7, 2014
 */
public class LedgerCost {
	/**
	 * 分户名称
	 */
	private String ledgerName;
	/**
	 * 分户的电费
	 */
	private double eleCost;
	/**
	 * 分户的水
	 */
	private double waterCost;
	/**
	 * 分户的气费
	 */
	private double gasCost;
	
	private boolean isParent;
	
	
	public LedgerCost() {
		super();
	}
	public LedgerCost(String ledgerName, double eleCost, double waterCost,
			double gasCost) {
		super();
		this.ledgerName = ledgerName;
		this.eleCost = eleCost;
		this.waterCost = waterCost;
		this.gasCost = gasCost;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public double getEleCost() {
		return eleCost;
	}
	public void setEleCost(double eleCost) {
		this.eleCost = eleCost;
	}
	public double getWaterCost() {
		return waterCost;
	}
	public void setWaterCost(double waterCost) {
		this.waterCost = waterCost;
	}
	public double getGasCost() {
		return gasCost;
	}
	public void setGasCost(double gasCost) {
		this.gasCost = gasCost;
	}
	
	public void addEleCost(double cost){
		this.eleCost = DataUtil.doubleAdd(this.eleCost, cost);
	}
	
	public void addWaterCost(double cost){
		this.waterCost = DataUtil.doubleAdd(this.waterCost, cost);
	}
	
	public void addGasCost(double cost){
		this.gasCost = DataUtil.doubleAdd(gasCost, cost);
	}
	
	public void addAll(double eleCost,double waterCost ,double gasCost){
		this.eleCost = DataUtil.doubleAdd(this.eleCost, eleCost);
		this.waterCost = DataUtil.doubleAdd(this.waterCost, waterCost);
		this.gasCost = DataUtil.doubleAdd(this.gasCost, gasCost);
	}
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	
	public void buildResult(){
		this.eleCost = DoubleUtils.getDoubleValue(eleCost, 2);
		this.waterCost = DoubleUtils.getDoubleValue(waterCost, 2);
		this.gasCost = DoubleUtils.getDoubleValue(gasCost, 2);
	}
}
