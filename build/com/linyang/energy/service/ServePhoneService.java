package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.*;

/**
 * 运维APP接口业务逻辑层接口
 * @author chengq
 * @date 2015-12-23 下午02:04:47
 * @version 1.0
 */
public interface ServePhoneService {

	/**
	 * 加载电表结构树数据 
	 * @param ledgerName
	 * @return
	 */
	public List<LineLossTreeBean> getMeterTreeData(Map<String,Object> queryMap);

	/**
	 * 加载区域列表
	 * @param queryMap
	 * @return
	 */
	public List<LedgerBean> getRegionListByParam(Map<String, Object> queryMap);

}
