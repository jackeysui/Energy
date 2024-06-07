package com.linyang.energy.model;

/**
 * 打分bean
 * 
 * @author gaofeng
 * 
 */
public class ScoreItemBean {
	/**
	 * id
	 */
	private long id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 分数
	 */
	private double score;
	
	public ScoreItemBean(long id, String name, double score) {
		this.id = id;
		this.name = name;
		this.score = score;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
}
