package com.linyang.energy.mapping.energydataquery;

import com.linyang.energy.model.CurveBean;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 9:07 2018/12/17
 * @ Description：class说明
 * @ Modified By：:dingy
 * @Version: $version$
 */
public interface PollutVoltCurrPowerMapper {
	
	/**
	 * 查询产污治污曲线
	 * @param param
	 * @return
	 */
	public List<CurveBean> queryPollutCur(Map<String, Object> param);
	
	
	/**
	 * 根据ledgerId查询设备是产污还是治污设备
	 * @param ledgerId
	 * @return
	 */
	public Map<String,Object> queryPolType(Long ledgerId);
	
	
	/**
	 * 查询该产(治)污设施关联的设施
	 * @return
	 */
	public Map<String,Object> queryFacilRelation(Map<String, Object> param);
	
	/**
	 * 查询某个id下事件的相应数据
	 * @return
	 */
	public List<Map<String,Object>> queryEventDeclar(Map<String, Object> param);
	
}
