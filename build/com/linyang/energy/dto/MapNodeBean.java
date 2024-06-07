package com.linyang.energy.dto;

public class MapNodeBean {
	/**
	 * 一个区域的类名称
	 */
	private String className;
	/**
	 * 一个区域的名称
	 */
	private String name;
	/**
	 * 分户id
	 */
	private long ledgerId;
	
	public MapNodeBean(long ledgerId,String className, String name) {
		super();
		this.ledgerId = ledgerId;
		this.className = className;
		this.name = name;
	}

	public String getClassName() {
		return className;
	}
	public String getName() {
		return name;
	}
	public long getLedgerId() {
		return ledgerId;
	}

	@Override
	public String toString() {
		return "ledgerId:"+ledgerId+",className:"+className+",name:"+name;
	}
	
	
}
