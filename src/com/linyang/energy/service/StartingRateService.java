package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.StartrateBean;

/**
 * 开机率分析Service
 * 
 * @author gaofeng
 * 
 */
public interface StartingRateService {

	/**
	 * 查询开机率
	 * 
	 * @param param
	 * @return
	 */
	public List<StartrateBean> queryStartRateData(Map<String, Object> param);

	/**
	 * 导出excel
	 * 
	 * @param string
	 * @param outputStream
	 * @param result
	 */
	public void optExcel(ServletOutputStream outputStream, List<StartrateBean> result, Map<String, Object> param);

	public List<LedgerTreeBean> getTree(Long ledgerId);
}
