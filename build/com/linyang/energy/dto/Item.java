package com.linyang.energy.dto;

import com.linyang.energy.utils.DataUtil;import com.linyang.util.DoubleUtils;

/**
 * echart饼图等最基本的数据元素
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 13, 2013
 */
public class Item {
	
	private String name;
	
	private double value;
	

	public Item() {
		super();
	}

	public Item(String name) {
		super();
		this.name = name;
	}

	public Item(String name, double value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public void addValue(double val){
		this.value = DoubleUtils.getDoubleValue(DataUtil.doubleAdd(value, val), 2);
	}
	

}
