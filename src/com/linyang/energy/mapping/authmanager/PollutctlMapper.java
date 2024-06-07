package com.linyang.energy.mapping.authmanager;

import com.linyang.energy.model.PollutctlBean;

import java.util.List;

public interface PollutctlMapper {
	/**
	 * @Description  :查询治污设施
	 * @return  List<PollutctlBean>
	 */
	List<PollutctlBean> queryPollutctlInfo(PollutctlBean pollutctlBean);
}
