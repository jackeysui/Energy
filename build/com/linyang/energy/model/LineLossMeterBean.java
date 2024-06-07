package com.linyang.energy.model;

public class LineLossMeterBean {
	
	private Long meterId;
	
	private Long parementMeterId;

	private String parementMeterName;

	private Integer meterLevel;

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Long getParementMeterId() {
		return parementMeterId;
	}

	public void setParementMeterId(Long parementMeterId) {
		this.parementMeterId = parementMeterId;
	}
	
	public String getParementMeterName() {
		return parementMeterName;
	}

	public void setParementMeterName(String parementMeterName) {
		this.parementMeterName = parementMeterName;
	}

	public Integer getMeterLevel() {
		return meterLevel;
	}

	public void setMeterLevel(Integer meterLevel) {
		this.meterLevel = meterLevel;
	}
}
