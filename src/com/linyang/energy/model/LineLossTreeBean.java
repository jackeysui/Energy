package com.linyang.energy.model;

/**
 * 线损树节点
 * 
 * @author gaofeng
 * 
 */
public class LineLossTreeBean {
	
	private static final String imgPath ="/energy/images/icon/";
	
	private long id;

	private String name;

	private long pId;
	
	private int meterType;

	private int isVirtual;
	
	/**
	 * 节点小图标
	 */
	private String icon;
	
	/**
	 * 层级
	 */
	private int meterLevel;
	
	/**
	 * 是否可拖拽
	 */
	private boolean drag = true;

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

	public long getpId() {
		return pId;
	}

	public void setpId(long pId) {
		this.pId = pId;
	}

	public int getMeterLevel() {
		return meterLevel;
	}

	public void setMeterLevel(int meterLevel) {
		this.meterLevel = meterLevel;
	}

	public boolean isDrag() {
		return drag;
	}

	public void setDrag(boolean drag) {
		this.drag = drag;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = imgPath+"electricity_tree.png";
	}
	
	private Long ledgerId; // 归属分户ID
	private String ledgerName; // 归属分户名称

	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}

	public int getMeterType() {
		return meterType;
	}

	public void setMeterType(int meterType) {
		this.meterType = meterType;
		if(meterType ==1 && this.isVirtual == 0){
			this.icon = imgPath+"electricity_tree.png";
		}else if(meterType ==2){
			this.icon = imgPath+"water_tree.png";
		}else if(meterType ==3){
			this.icon = imgPath+"gas_tree.png";
		}else if(meterType ==4){
			this.icon = imgPath+"shine_tree.png";
		}
	}

    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
        if(this.meterType == 1 && isVirtual == 1){
            this.icon = imgPath+"virtual_ele_tree.png";
        }
    }
}
