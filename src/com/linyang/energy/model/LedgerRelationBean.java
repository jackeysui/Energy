package com.linyang.energy.model;

import java.io.Serializable;

public class LedgerRelationBean implements Serializable{

	private static final long serialVersionUID = 8543340412649677200L;
	
	private Long recId;
	
	private Long ledgerId;
	
	private int type;
	
	private Long id;
	
	private int attr;
	
	private int pct;
	
	private String name;
    
    private int meterType;

	public Long getRecId() {
		return recId;
	}

	public void setRecId(Long recId) {
		this.recId = recId;
	}

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAttr() {
		return attr;
	}

	public void setAttr(int attr) {
		this.attr = attr;
	}

	public int getPct() {
		return pct;
	}

	public void setPct(int pct) {
		this.pct = pct;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public int getMeterType() {
        return meterType;
    }

    public void setMeterType(int meterType) {
        this.meterType = meterType;
    }
	
}
