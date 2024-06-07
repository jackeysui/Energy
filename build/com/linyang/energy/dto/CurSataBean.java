package com.linyang.energy.dto;

import java.util.Map;
import java.util.TreeMap;

import com.linyang.energy.utils.DataUtil;import com.linyang.util.DateUtils;
import com.linyang.util.DoubleUtils;

public class CurSataBean {
	
	private double totalMoney;
	private TreeMap<Long,Double> timeMap = new TreeMap<Long, Double>();
	
	private TreeMap<String,Double> map = new TreeMap<String, Double>();
	
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public TreeMap<Long, Double> getTimeMap() {
		return timeMap;
	}
	public TreeMap<String, Double> getMap() {
		return map;
	}
	
	public CurSataBean buildResult(){
		if(!timeMap.isEmpty()){
			for (Map.Entry<Long,Double> entry : timeMap.entrySet()) {
				totalMoney = DataUtil.doubleAdd(totalMoney, entry.getValue());
				map.put(DateUtils.convertTimeToString(entry.getKey(), "HH:mm"),DoubleUtils.getDoubleValue( entry.getValue(), 2));
			}
		}
		return this;
	}
	public CurSataBean(String time,long density) {
		long beginTime = DateUtils.convertTimeToLong(time,DateUtils.FORMAT_SHORT);
		long endTime = DateUtils.convertTimeToLong(time+" 23:59:59", DateUtils.FORMAT_LONG);
		long m = (endTime - beginTime)/(density*60);
		for (long i = 0; i <= m; i++) {
			timeMap.put(beginTime+(i*density*60),Double.valueOf("0"));
		}
	}

}
