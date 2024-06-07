package com.linyang.energy.dto;

import java.util.ArrayList;
import java.util.List;

public class WeatherBean {
	
	private long wetherTime;
	
	private List<String> infos ;

	public WeatherBean(long wetherTime, List<String> infos) {
		super();
		this.wetherTime = wetherTime;
		this.infos = infos;
	}

	public long getWetherTime() {
		return wetherTime;
	}

	public void setWetherTime(long wetherTime) {
		this.wetherTime = wetherTime;
	}

	public List<String> getInfos() {
		return infos==null?new ArrayList<String>():infos;
	}

	public void setInfos(List<String> infos) {
		this.infos = infos;
	}
	
	

}
