package com.linyang.energy.dto;

public class DataQueryXmlBean {
	
	/**
	 * 查询表名称
	 */
	private String tableName;
	/**
	 * 时间字段名称
	 */
	private String timeField;
	/**
	 * 字段值名称
	 */
	private String valueField;
	/**
	 * 名称
	 */
	private String name;
	
	private String desc;
	public DataQueryXmlBean(String tableName, String timeField, String valueField) {
		super();
		this.tableName = tableName;
		this.timeField = timeField;
		this.valueField = valueField;
	}
	
	public String getTableName() {
		return tableName;
	}
	public String getTimeField() {
		return timeField;
	}
	public String getValueField() {
		return valueField;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "tableName:"+tableName+",timeField:"+timeField+",valueField:"+valueField+",desc:"+desc+",name:"+name;
	}

}
