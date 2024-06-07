package com.linyang.energy.model;

import java.io.Serializable;

public class CompanyDisplaySet implements Serializable{

	private static final long serialVersionUID = -6443656115176444660L;

	public Long companyId;
	
	public Integer menuContinued;
	
	public Integer gatherShow;
	
	public Integer gatherContinued;
	
	public Integer departContinued;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Integer getMenuContinued() {
		return menuContinued;
	}

	public void setMenuContinued(Integer menuContinued) {
		this.menuContinued = menuContinued;
	}

	public Integer getGatherShow() {
		return gatherShow;
	}

	public void setGatherShow(Integer gatherShow) {
		this.gatherShow = gatherShow;
	}

	public Integer getGatherContinued() {
		return gatherContinued;
	}

	public void setGatherContinued(Integer gatherContinued) {
		this.gatherContinued = gatherContinued;
	}

	public Integer getDepartContinued() {
		return departContinued;
	}

	public void setDepartContinued(Integer departContinued) {
		this.departContinued = departContinued;
	}
	
	
}
