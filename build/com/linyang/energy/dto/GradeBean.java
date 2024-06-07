package com.linyang.energy.dto;

public class GradeBean {
	
	private long gradeId;
	
	private String gradeName;
	
	private Double GradeValue;

	
	public GradeBean(long gradeId) {
		super();
		this.gradeId = gradeId;
	}

	
	public GradeBean(long gradeId, String gradeName, Double gradeValue) {
		super();
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		GradeValue = gradeValue;
	}


	public long getGradeId() {
		return gradeId;
	}

	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Double getGradeValue() {
		return GradeValue;
	}

	public void setGradeValue(Double gradeValue) {
		GradeValue = gradeValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (gradeId ^ (gradeId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GradeBean other = (GradeBean) obj;
		if (gradeId != other.gradeId)
			return false;
		return true;
	}
	
	

}
