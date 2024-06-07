package com.linyang.energy.model;

import java.io.Serializable;

public class ModuleBean implements Serializable {
    private Long moduleId;

    private String moduleName;

    private Short moduleOrder;

    private String moduleUrl;

    private String moduleIcon;

    private Short isvisible;

    private Long moduleParentId;
    
    private Short istabs;

    private static final long serialVersionUID = 1L;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? "" : moduleName.trim();
    }

    public Short getModuleOrder() {
        return moduleOrder;
    }

    public void setModuleOrder(Short moduleOrder) {
        this.moduleOrder = moduleOrder;
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl == null ? "" : moduleUrl.trim();
    }

    public String getModuleIcon() {
        return moduleIcon;
    }

    public void setModuleIcon(String moduleIcon) {
        this.moduleIcon = moduleIcon == null ? "" : moduleIcon.trim();
    }

    public Short getIsvisible() {
        return isvisible;
    }

    public void setIsvisible(Short isvisible) {
        this.isvisible = isvisible;
    }

    public Long getModuleParentId() {
        return moduleParentId;
    }

    public Short getIstabs() {
		return istabs;
	}

	public void setIstabs(Short istabs) {
		this.istabs = istabs;
	}

	public void setModuleParentId(Long moduleParentId) {
        this.moduleParentId = moduleParentId;
    }
}