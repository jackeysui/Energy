package com.linyang.energy.mapping.phone;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.*;

/**
 * 运维APP手机接口数据访问层接口
 * 
 * @author chengq
 * @date 2015-12-24 上午08:37:23
 * @version 1.0
 */
public interface ServePhoneMapper {

	/**
	 * 加载电表结构树数据
	 * @param ledgerId
	 * @return
	 */
	public List<LineLossTreeBean> getMeterTreeData(Map<String,Object> param);

	/**
	 * 加载区域列表
	 * @param queryMap
	 * @return
	 */
	public List<LedgerBean> getRegionListByParam(Map<String, Object> queryMap);

}
