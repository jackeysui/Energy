package com.linyang.energy.dto;

import java.util.SortedMap;


public class ChartItemWithDatas extends ChartItem{
	
	
	public ChartItemWithDatas(String name){
		super.name = name;
	}
	
	public ChartItemWithDatas setDatas(SortedMap<String,Object> map){
		super.map = map;
		super.dataScale();
		return this;
	}

}
