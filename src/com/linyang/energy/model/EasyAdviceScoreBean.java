package com.linyang.energy.model;

/**
 * 小易建议各项分数bean
 * @author Administrator
 *
 */
public class EasyAdviceScoreBean {
	
//	//评分项编号
//	private int scoreType;
	
	//评分项名
	private String scoreName;
	
	//分数
	private Long score;
	
	//String型标准值
	private String stValStr;
	
	//String型
	private String acValStr;
 	
	//double型比较值
	private double dValDouble;
	
	//评分项权重
	private int weight;
	
	//规则
	private String rules;
	
	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public String getScoreName() {
		return scoreName;
	}

	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}

	public double getdValDouble() {
		return dValDouble;
	}

	public void setdValDouble(double dValDouble) {
		this.dValDouble = dValDouble;
	}

	public void setStValStr(String stValStr) {
		this.stValStr = stValStr;
	}

	public String getStValStr() {
		return stValStr;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getRules() {
		return rules;
	}

	public void setAcValStr(String acValStr) {
		this.acValStr = acValStr;
	}

	public String getAcValStr() {
		return acValStr;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}


}
