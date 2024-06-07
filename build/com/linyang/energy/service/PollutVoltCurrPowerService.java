package com.linyang.energy.service;

import com.linyang.energy.model.CurveBean;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 8:43 2018/12/17
 * @ Description：class说明
 * @ Modified By：:dingy
 * @Version: $version$
 */
public interface PollutVoltCurrPowerService {
	
	
	/**
	 * 查询产污治污曲线
	 * @param param
	 * @return
	 */
	public List<CurveBean> queryPollutCur(Map<String, Object> param);
	
	/**
	 * 根据ledgerId查询设备是产污还是治污设备(会直接返回匹配的一对设施id)
	 * @param ledgerId
	 * @return
	 */
	public Map<String,Object> queryFacilRelation(Long ledgerId);
	
	/**
	 * 根据ledgerId查询设备是产污还是治污设备
	 * @param ledgerId
	 * @return
	 */
	public Map<String,Object> queryPolType(Long ledgerId);
	
	/**
	 * 导出excel
	 */
	void getEleExcel(String sheetName, OutputStream output, Map<String, Object> result);
	
	/**
	 * 查询某个id下事件的相应数据
	 * @return
	 */
	public List<Map<String,Object>> queryEventDeclar(Map<String, Object> param);
}
