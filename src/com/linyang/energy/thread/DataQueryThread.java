package com.linyang.energy.thread;

import java.util.Map;
import java.util.concurrent.Callable;

import com.linyang.energy.dto.DataQueryResultBean;
import com.linyang.energy.dto.DataQueryXmlBean;
import com.linyang.energy.mapping.energydataquery.DataQueryMapper;

/**
 * 查询数据线程
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 16, 2013
 */
public class DataQueryThread implements Callable<DataQueryResultBean> {
	
	private DataQueryMapper dataQueryMapper;
	
	private Map<String,Object> queryMap;
	/**
	 * 后面用来分析用的
	 */
	private DataQueryXmlBean dataQueryXmlBean;

	public DataQueryThread(DataQueryMapper dataQueryMapper,Map<String, Object> queryMap,DataQueryXmlBean dataQueryXmlBean) {
		if (dataQueryMapper == null || queryMap == null)
			throw new IllegalArgumentException("dataQueryMapper is null or queryMap is null ");
		this.dataQueryMapper = dataQueryMapper;
		this.queryMap = queryMap;
		this.dataQueryXmlBean = dataQueryXmlBean;
	}


	@Override
	public DataQueryResultBean call() throws Exception {
		return new DataQueryResultBean(dataQueryXmlBean,dataQueryMapper.getThermalConsumptionDatas(queryMap));
	}
	

}
