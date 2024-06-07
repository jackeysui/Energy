package com.linyang.energy.model;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 11:09 2018/12/20
 * @ Description：产污分布Bean
 * @ Modified By：:dingy
 * @Version: 1.0
 */
public class DistributionBean {
	
	/** 企业id  */
	private String id;
	
	/** 企业名称  */
	private String name;
	
	/** 企业产污设施数量  */
	private long pollutCount;
	
	/** 企业治污设施数量  */
	private long pollutctlCount;
	
	/** 企业数量  */
	private long ledgerCount;
	
	public long getLedgerCount() {
		return ledgerCount;
	}
	
	public void setLedgerCount(long ledgerCount) {
		this.ledgerCount = ledgerCount;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getPollutCount() {
		return pollutCount;
	}
	
	public void setPollutCount(long pollutCount) {
		this.pollutCount = pollutCount;
	}
	
	public long getPollutctlCount() {
		return pollutctlCount;
	}
	
	public void setPollutctlCount(long pollutctlCount) {
		this.pollutctlCount = pollutctlCount;
	}
	
	public DistributionBean(String id, String name, long pollutCount, long pollutctlCount, long ledgerCount) {
		this.id = id;
		this.name = name;
		this.pollutCount = pollutCount;
		this.pollutctlCount = pollutctlCount;
		this.ledgerCount = ledgerCount;
	}
	
	public DistributionBean(String name, long pollutCount, long pollutctlCount, long ledgerCount) {
		this.name = name;
		this.pollutCount = pollutCount;
		this.pollutctlCount = pollutctlCount;
		this.ledgerCount = ledgerCount;
	}
	
	public DistributionBean() { }
}
