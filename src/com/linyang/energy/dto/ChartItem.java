package com.linyang.energy.dto;

import java.math.BigDecimal;import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.linyang.util.DoubleUtils;
/**
 * 图表项信息类
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 12, 2013
 */
public class ChartItem {

	/**
	 * 排序的map
	 */
	protected SortedMap<String,Object> map = new TreeMap<String, Object>();
	/**
	 * 图表名称
	 */
	protected String name;

	public SortedMap<String, Object> getMap() {
		return map;
	}
	
	public String getName() {
		return name;
	}
	/**
	 * 浮点数运算会导致小数点后很多位，需要截取
	 */
	public Map<String,Object> dataScale(){
		if(!map.isEmpty()){
			for (Map.Entry<String,Object> entry : map.entrySet()) {
				if(DoubleUtils.isDoubleType(entry.getValue()==null?"":entry.getValue().toString()))
					entry.setValue(DoubleUtils.getDoubleValue(Double.valueOf(entry.getValue().toString()), 2));
			}
		}
		return map;
	}
	
	public Map<String,Object> dataScale2(){
		if(!map.isEmpty()){
			for (Map.Entry<String,Object> entry : map.entrySet()) {
				if(DoubleUtils.isDoubleType(entry.getValue()==null?"":entry.getValue().toString()))
					entry.setValue(DoubleUtils.getDoubleValue(Double.valueOf(entry.getValue().toString()), 5));
			}
		}
		return map;
	}
	
	public Map<String,Object> dataScaleWithGrowth(double growth){
		if(!map.isEmpty()){
			for (Map.Entry<String,Object> entry : map.entrySet()) {
				if(DoubleUtils.isDoubleType(entry.getValue()==null?"":entry.getValue().toString()))
					entry.setValue(DoubleUtils.getDoubleValue(BigDecimal.ONE.add(new BigDecimal(growth)).multiply(new BigDecimal(Double.valueOf(entry.getValue().toString()))).doubleValue(), 2));
			}
		}
		return map;
	}
	
	public void convert2NextYear(){
		String firstKey = map.firstKey().toString();
		int year = Integer.parseInt(firstKey.substring(0, 4))+1;
		TreeMap<String, Object> treeMap = new TreeMap<String,Object>();
		for (Map.Entry<String,Object> entry : map.entrySet()) {
			String time = entry.getKey().toString().substring(4);
			treeMap.put(year+time, entry.getValue());
		}
		this.map = treeMap;
	}
}
