package com.linyang.energy.dto;

import java.util.ArrayList;
import java.util.List;

public class SeriesBean {
	
	private String name;
	
	private String type="bar";
	
	private long seriesId;
	
	private List<Double> data = new ArrayList<Double>();

	
	public SeriesBean(long seriesId,String name) {
		super();
		this.name = name;
		this.seriesId = seriesId;
	}


	public SeriesBean(long seriesId,String name, String type) {
		super();
		this.name = name;
		this.type = type;
		this.seriesId = seriesId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Double> getData() {
		return data;
	}

	public void setData(List<Double> data) {
		this.data = data;
	}

	
	public long getSeriesId() {
		return seriesId;
	}


	public void setSeriesId(long seriesId) {
		this.seriesId = seriesId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (seriesId ^ (seriesId >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SeriesBean other = (SeriesBean) obj;
		if (seriesId != other.seriesId)
			return false;
		return true;
	}
	
	
}
