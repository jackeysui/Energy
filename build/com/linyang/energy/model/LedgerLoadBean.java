package com.linyang.energy.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 分户近30天最大负荷
 * @author Administrator
 *
 */
public class LedgerLoadBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long ledgerId;
	
	/**
	 * 分户最大功率
	 */
	private Double maxP;
	
	/**
	 * 分户最大电流
	 */
	private Double maxI;
	
	/**
	 * 分户额定功率
	 */
	private Double ratio;
	
	/**
	 * 发生时间
	 */
	private String occuredTime;
	
	/**
	 * 入库标识时间
	 */
	private Date flagTime;

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public Double getMaxP() {
		return maxP;
	}

	public void setMaxP(Double maxP) {
		this.maxP = maxP;
	}

	public Double getMaxI() {
		return maxI;
	}

	public void setMaxI(Double maxI) {
		this.maxI = maxI;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public String getOccuredTime() {
		return occuredTime;
	}

	public void setOccuredTime(String occuredTime) {
		this.occuredTime = occuredTime;
	}

	public Date getFlagTime() {
		return flagTime;
	}

	public void setFlagTime(Date flagTime) {
		this.flagTime = flagTime;
	}
}
