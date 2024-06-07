package com.linyang.energy.mapping.equipYield;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：${date} ${time}
 * @ Description：企业产能申报
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface EquipYieldMapper {
	
	/**
	 * 查询表格头部数据
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> queryTableHead(Map<String,Object> params);
	
	/**
	 * 查询企业产能申报列表数据
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> queryEquipList(Map<String,Object> params);
	
	/**
	 * 根据企业id查询下属所有的测量点id
	 * @param params
	 * @return
	 */
	List<Long> getMeterIdsByLedgerId(Map<String,Object> params);
	
	/**
	 * 根据企业id查询下属所有测量点信息
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> queryMeterDataByLedgerId(Map<String,Object> params);
	
	/**
	 * 根据测量点名称查询测量点信息
	 * @param meterName
	 * @return
	 */
	Map<String,Object> queryMeterDataByMeterName(String meterName);
	
	

}
