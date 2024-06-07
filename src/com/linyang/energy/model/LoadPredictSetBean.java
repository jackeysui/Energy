package com.linyang.energy.model;

import java.io.Serializable;

/**
 * 负荷预测设置Bean
 * @author Administrator
 *
 */
public class LoadPredictSetBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String historyDay;
	
	private String tempHighLine;
	
	private String tempLowLine;
	
	private String proportion;
	
	private String pointNum;

	public String getHistoryDay() {
		return historyDay;
	}

	public void setHistoryDay(String historyDay) {
		this.historyDay = historyDay;
	}

	public String getTempHighLine() {
		return tempHighLine;
	}

	public void setTempHighLine(String tempHighLine) {
		this.tempHighLine = tempHighLine;
	}

	public String getTempLowLine() {
		return tempLowLine;
	}

	public void setTempLowLine(String tempLowLine) {
		this.tempLowLine = tempLowLine;
	}

	public String getProportion() {
		return proportion;
	}

	public void setProportion(String proportion) {
		this.proportion = proportion;
	}

	public String getPointNum() {
		return pointNum;
	}

	public void setPointNum(String pointNum) {
		this.pointNum = pointNum;
	}

}
