package com.linyang.energy.model;

import java.util.List;

public class HeatPumpDataBean {
	private String name;
	
	private List<String> xData;
	
	private List<Double> yData;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getxData() {
		return xData;
	}

	public void setxData(List<String> xData) {
		this.xData = xData;
	}

	public List<Double> getyData() {
		return yData;
	}

	public void setyData(List<Double> yData) {
		this.yData = yData;
	}
	
	
}
