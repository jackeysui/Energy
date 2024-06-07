package com.linyang.energy.mapping.authmanager;

import com.linyang.energy.model.PollutBean;

import java.util.List;

public interface PollutMapper {
	/**
	 * @Description  :查询产污设施
	 * @return  List<RegionBean>
	 */
	List<PollutBean> queryPollutInfo(PollutBean pollutBean);
}
