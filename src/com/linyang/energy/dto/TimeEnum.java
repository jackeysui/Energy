package com.linyang.energy.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 时间类型枚举类
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 13, 2013
 */
public enum TimeEnum {

	DAY(1,"yyyy-MM-dd"),MONTH(2,"yyyy-MM"),YEAR(3,"yyyy"),WEEK(4,"yyyy-MM-dd"),HOUR(5,"yyyy-MM-dd HH"),MINUTE(6,"yyyy-MM-dd HH:mm"),MINUTE15(7,"yyyy-MM-dd HH:mm"),DAYMINUTE(8,"yyyy-MM-dd HH:mm");
	
	private static final Map<Integer,TimeEnum> cache = new HashMap<Integer,TimeEnum>();
	
	static{
		for (TimeEnum timeEnum : values()) {
			cache.put(timeEnum.getTimeType(), timeEnum);
		}
	}
	
	private int timeType;
	private String dateFormat;

	private TimeEnum(int timeType,String dateFormat) {
		this.timeType = timeType;
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public int getTimeType() {
		return timeType;
	}
	/**
	 * 时间类型 转换成枚举对象
	 * @param timeType 时间类型，目前只能是 1 2 3 4 5 6 这6个数字
	 * @return
	 */
	public static TimeEnum formInt2TimeEnum(int timeType){
		if(!cache.containsKey(timeType))
			throw new IllegalArgumentException("timeType is illegal,timeType must in "+cache.keySet());
		return 	cache.get(timeType);
	}
	
}
