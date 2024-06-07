package com.linyang.energy.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.linyang.util.CommonMethod;

public class ProductGradeBean {
	
	private long productId;
	
	private String productName;
	
	private List<GradeBean> list = new ArrayList<GradeBean>();
	private Map<Long,GradeBean> map = new TreeMap<Long, GradeBean>();

	public ProductGradeBean(long productId, String productName,Collection<Long> gradeIds) {
		super();
		this.productId = productId;
		this.productName = productName;
		if(CommonMethod.isCollectionNotEmpty(gradeIds)){
			for (Long gradeId : gradeIds) {
				map.put(gradeId, new GradeBean(gradeId));
			}
		}
	}



	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<GradeBean> getList() {
		return list;
	}

	public void addBean(GradeBean bean){
		map.put(bean.getGradeId(), bean);
	}
	
	public void add2List(){
		list.addAll(map.values());
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (productId ^ (productId >>> 32));
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
		final ProductGradeBean other = (ProductGradeBean) obj;
		if (productId != other.productId)
			return false;
		return true;
	}
	

}
