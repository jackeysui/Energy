package com.linyang.energy.model;

import java.io.Serializable;
/**
 * 企业调峰能力
 * @author Administrator
 *
 */
public class EntPeakAbility implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1327593522721019553L;

	private long ledgerId;
	
	private String ledgerName;
	
	private double monUpper;
	
	private double monLower;
	
	private double tuesUpper;
	
	private double tuesLower;
	
	private double wedUpper;
	
	private double wedLower;
	
	private double thurUpper;
	
	private double thurLower;
	
	private double friUpper;
	
	private double friLower;
	
	private double satUpper;
	
	private double satLower;
	
	private double sunUpper;
	
	private double sunLower;
	
	private String remarks;

	public long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}

	public double getMonUpper() {
		return monUpper;
	}

	public void setMonUpper(double monUpper) {
		this.monUpper = monUpper;
	}

	public double getMonLower() {
		return monLower;
	}

	public void setMonLower(double monLower) {
		this.monLower = monLower;
	}

	public double getTuesUpper() {
		return tuesUpper;
	}

	public void setTuesUpper(double tuesUpper) {
		this.tuesUpper = tuesUpper;
	}

	public double getTuesLower() {
		return tuesLower;
	}

	public void setTuesLower(double tuesLower) {
		this.tuesLower = tuesLower;
	}

	public double getWedUpper() {
		return wedUpper;
	}

	public void setWedUpper(double wedUpper) {
		this.wedUpper = wedUpper;
	}

	public double getWedLower() {
		return wedLower;
	}

	public void setWedLower(double wedLower) {
		this.wedLower = wedLower;
	}

	public double getThurUpper() {
		return thurUpper;
	}

	public void setThurUpper(double thurUpper) {
		this.thurUpper = thurUpper;
	}

	public double getThurLower() {
		return thurLower;
	}

	public void setThurLower(double thurLower) {
		this.thurLower = thurLower;
	}

	public double getFriUpper() {
		return friUpper;
	}

	public void setFriUpper(double friUpper) {
		this.friUpper = friUpper;
	}

	public double getFriLower() {
		return friLower;
	}

	public void setFriLower(double friLower) {
		this.friLower = friLower;
	}

	public double getSatUpper() {
		return satUpper;
	}

	public void setSatUpper(double satUpper) {
		this.satUpper = satUpper;
	}

	public double getSatLower() {
		return satLower;
	}

	public void setSatLower(double satLower) {
		this.satLower = satLower;
	}

	public double getSunUpper() {
		return sunUpper;
	}

	public void setSunUpper(double sunUpper) {
		this.sunUpper = sunUpper;
	}

	public double getSunLower() {
		return sunLower;
	}

	public void setSunLower(double sunLower) {
		this.sunLower = sunLower;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
