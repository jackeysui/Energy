package com.linyang.energy.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataQueryResultBean {
	/**
	 * 用来分组的
	 */
	private DataQueryXmlBean dataQueryXmlBean;
	/**
	 * 查询回来的数据
	 */
	private List<Map<String,Object>> datas;
	
	public DataQueryResultBean(DataQueryXmlBean dataQueryXmlBean, List<Map<String, Object>> datas) {
		super();
		this.dataQueryXmlBean = dataQueryXmlBean;
		this.datas = datas;
	}
	
	public DataQueryXmlBean getDataQueryXmlBean() {
		return dataQueryXmlBean;
	}

	public List<Map<String, Object>> getDatas() {
		return datas==null?new ArrayList<Map<String,Object>>():datas;
	}
	
}
