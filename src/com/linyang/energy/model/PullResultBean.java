package com.linyang.energy.model;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:18 2019/12/9
 * @ Description：拉合闸页面结果bean
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public class PullResultBean {
	
	/**
	 * 企业名称
	 */
	private String ledgerName;
	
	/**
	 * 消息内容
	 */
	private String msg;
	
	/**
	 * 状态码
	 */
	private int statusCode;
	
	/**
	 * 能效平台,测量点id
	 */
	private long meterId;
	
	/**
	 * 能效平台,测量点名称
	 */
	private String meterName;
	
	/**
	 * 采集平台,终端id
	 */
	private long terminalId;
	
	/**
	 * 采集平台,终端名称
	 */
	private String terminalName;
	
	/**
	 * 采集平台,测量点id
	 */
	private long mpointId;
	
	/**
	 * 采集平台,测量点名称
	 */
	private String mpointName;
	
	
	public String getLedgerName() {
		return ledgerName;
	}
	
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public long getMeterId() {
		return meterId;
	}
	
	public void setMeterId(long meterId) {
		this.meterId = meterId;
	}
	
	public String getMeterName() {
		return meterName;
	}
	
	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	
	public long getTerminalId() {
		return terminalId;
	}
	
	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	
	public String getTerminalName() {
		return terminalName;
	}
	
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	
	public long getMpointId() {
		return mpointId;
	}
	
	public void setMpointId(long mpointId) {
		this.mpointId = mpointId;
	}
	
	public String getMpointName() {
		return mpointName;
	}
	
	public void setMpointName(String mpointName) {
		this.mpointName = mpointName;
	}
	
	
	
}
