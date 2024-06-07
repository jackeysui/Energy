/**
 */
package com.linyang.energy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.mapping.phone.ServePhoneMapper;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.LineLossTreeBean;
import com.linyang.energy.service.ServePhoneService;

/**
 * 
 * @author chengq
 * @date 2015-12-24 上午08:37:23
 * @version 1.0
 */
@Service
public class ServePhoneServiceImpl implements ServePhoneService {
	
	@Autowired
	private ServePhoneMapper servePhoneMapper;

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ServePhoneService#getMeterTreeData(java.util.Map)
	 */
	@Override
	public List<LineLossTreeBean> getMeterTreeData(Map<String,Object> queryMap) {
		return servePhoneMapper.getMeterTreeData(queryMap);
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ServePhoneService#getRegionListByParam(java.util.Map)
	 */
	@Override
	public List<LedgerBean> getRegionListByParam(Map<String, Object> queryMap) {
		return servePhoneMapper.getRegionListByParam(queryMap);
	}

	
}
