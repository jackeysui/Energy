package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

/**
 * 
 * @author guosen
 * @date 2014-11-24
 */
public interface ERateQueryService {

	/**
	 * 查询EMO、DCP电能示值
	 * @param param
	 * @return
	 */
	Map<String, Object> queryIndicationInfo(Map<String, Object> param);


    /**
     * 查询分户下的子EMO和DCP的某段时间段的电量
     * @param param
     * @return
     */
    Map<String, Object> getChildQData(Map<String, Object> param);

	/**
	 * 导出excel
	 * @param string
	 * @param outputStream
	 * @param result
	 */
	void optExcel(ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param);
}
