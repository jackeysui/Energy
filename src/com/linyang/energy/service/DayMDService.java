package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.linyang.energy.model.CurveBean;

/**
 * 日需量分析
 * @author guosen
 * @date 2014-12-23
 */
public interface DayMDService {

	/**
	 * 得到数据
	 * @param params
	 * @param needMD 是否需要申报MD
	 * @return
	 */
	List<CurveBean> getDayMDData(Map<String, Object> params);

	/**
	 * 导出excel
	 * @param string
	 * @param outputStream
	 * @param list
	 */
	void exportExcel(ServletOutputStream outputStream, List<CurveBean> list,int objType);

	/**
	 * 得到分户容量
	 * @param ledgerId
	 * @return
	 */
	Long getLedgerVolumeByLedgerId(long ledgerId);

}
