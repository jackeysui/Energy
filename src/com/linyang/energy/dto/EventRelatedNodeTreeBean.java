package com.linyang.energy.dto;

public class EventRelatedNodeTreeBean extends LedgerTreeBean {
	private int isConfiged;//是否已配置
    private int isValid;//是否为有效节点
    

	public void setIsConfiged(int isConfiged) {
		this.isConfiged = isConfiged;
	}

	public int getIsConfiged() {
		return isConfiged;
	}

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }
    
}
