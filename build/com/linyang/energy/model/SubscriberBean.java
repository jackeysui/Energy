package com.linyang.energy.model;

/**
 * 信息订阅bean
 * 
 * @author gaofeng
 * 
 */
public class SubscriberBean {
	/**
	 * 对象id
	 */
	private long objId;

	/**
	 * 对象类型（1、群组；2、企业；3、账号）
	 */
	private int type;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
