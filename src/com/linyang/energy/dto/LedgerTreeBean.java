package com.linyang.energy.dto;

import com.leegern.util.StringUtil;
import com.linyang.common.web.ztree.ZtreeNode;

/**
 * 分户计量点树节点信息
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 5, 2013
 */
public class LedgerTreeBean extends ZtreeNode{
	
	private static final String imgPath ="/energy/images/icon/";
	
	/**
	 * 树节点类型：1表示的是分户，2表示的计量点
	 */
	private int treeNodeType;
	/**
	 * 如果是计量点，则需要加上计量点类型(计量点类型.1:电;2:水;3:气;4:热)
	 */
	private int meterType;

    /**
     * 如果是计量点，则需要加上(0:实际采集点;1:虚拟采集点)
     */
    private int isVirtual;

    /**
     * 异步加载的节点是否能点击（暂时定的是虚拟采集点不能点击）
     */
    private boolean chkDisabled;

	/**
	 * 费率id，如果是分户需要带上rateId
	 */
	private Long rateId;
	/**
	 * 标识分户是否为企业：1-企业；0-非企业
	 */
    private int isCompany;

	/**
	 * 若是分户---带上polluteId
	 */
	private String polluteId;

	/**
	 * 若是分户---带上controlId
	 */
	private String controlId;

    
	public int getTreeNodeType() {
		return treeNodeType;
	}

	public void setTreeNodeType(int treeNodeType) {
		this.treeNodeType = treeNodeType;
		if(treeNodeType == 1){
			super.setIcon(imgPath+"point_tree.png");
		}else if(treeNodeType == 100){
            super.setIcon(imgPath+"group-icon.png");
        }
	}

	public int getMeterType() {
		return meterType;
	}

	public void setMeterType(int meterType) {
		this.meterType = meterType;
		if(meterType ==1 && this.isVirtual == 0){
			super.setIcon(imgPath+"electricity_tree.png");
		}else if(meterType ==2){
			super.setIcon(imgPath+"water_tree.png");
		}else if(meterType ==3){
			super.setIcon(imgPath+"gas_tree.png");
		}else if(meterType ==4){
			super.setIcon(imgPath+"shine_tree.png");
		}
	}

    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
        if(this.meterType == 1 && isVirtual == 1){
            super.setIcon(imgPath+"virtual_ele_tree.png");
        }

        if(isVirtual == 1){
            this.chkDisabled = true;
        }
        else{
            this.chkDisabled = false;
        }
    }

	public Long getRateId() {
		return rateId;
	}

	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}

    public int getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(int isCompany) {
        this.isCompany = isCompany;
    }

    public boolean getChkDisabled() {
        return chkDisabled;
    }

    public void setChkDisabled(boolean chkDisabled) {
        this.chkDisabled = chkDisabled;
    }

	public String getPolluteId() {
		return polluteId;
	}

	public void setPolluteId(String polluteId) {
		this.polluteId = polluteId;

		if(this.treeNodeType == 1 && StringUtil.isNotEmpty(polluteId)){
			super.setIcon(imgPath + "pollute_tree.png");
		}
	}

	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId){
		this.controlId = controlId;

		if(this.treeNodeType == 1 && StringUtil.isNotEmpty(controlId)){
			super.setIcon(imgPath + "control_tree.png");
		}
	}
    
}
