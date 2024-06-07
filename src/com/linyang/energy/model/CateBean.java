package com.linyang.energy.model;

/**
 * @Description 字典大项小项数据bean
 * @author Leegern
 * @date Feb 18, 2014 5:35:33 PM
 */
public class CateBean {
	
	/**
	 * 大类Id
	 */
	private long cateId;
	
	/**
	 * 大类名称
	 */
	private String cateName;
	
	/**
	 * 明细Id
	 */
	private long detailId;
	
	/**
	 * 明细名称
	 */
	private String detailName;

	/**
	 * @return the cateId
	 */
	public long getCateId() {
		return cateId;
	}

	/**
	 * @param cateId the cateId to set
	 */
	public void setCateId(long cateId) {
		this.cateId = cateId;
	}

	/**
	 * @return the cateName
	 */
	public String getCateName() {
		return cateName;
	}

	/**
	 * @param cateName the cateName to set
	 */
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	/**
	 * @return the detailId
	 */
	public long getDetailId() {
		return detailId;
	}

	/**
	 * @param detailId the detailId to set
	 */
	public void setDetailId(long detailId) {
		this.detailId = detailId;
	}

	/**
	 * @return the detailName
	 */
	public String getDetailName() {
		return detailName;
	}

	/**
	 * @param detailName the detailName to set
	 */
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}
}
