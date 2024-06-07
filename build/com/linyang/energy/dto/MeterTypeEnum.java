package com.linyang.energy.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * 计量点类型枚举
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 16, 2013
 */
public enum MeterTypeEnum {
	ELE(1),WATER(2),GAS(3),HOT(4); 
	
	private static final Map<Integer,MeterTypeEnum> cache = new HashMap<Integer,MeterTypeEnum>();
	static{
		for (MeterTypeEnum meterTypeEnum :values())
			cache.put(meterTypeEnum.getMeterType(), meterTypeEnum);
	}
	private int meterType;

	private MeterTypeEnum(int meterType) {
		this.meterType = meterType;
	}

	public int getMeterType() {
		return meterType;
	}
	
	public static MeterTypeEnum formInt2MeterTypeEnum(int meterType){
		if(!cache.containsKey(meterType))
			throw new IllegalArgumentException("meterType is illegal,meterType must in "+cache.keySet());
		return 	cache.get(meterType);
	}

}
