package com.linyang.energy.service;

import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：${date} ${time}
 * @ Description：企业产能申报
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface EquipYieldService {
	
	
	/**
	 * 查询页面表格头部数据
	 * @param params
	 * @return
	 */
	List<Map<String,Object>> queryTableHead(Map<String,Object> params);
	
	/**
	 * 查询企业产能申报列表数据
	 * @param params
	 * @return
	 */
	Map<String,Object> queryEquipList(Map<String,Object> params);
	
	/**
	 * 导出excel模板文件
	 * @param name
	 * @param outputStream
	 * @param params
	 */
	void getExcelTemplate (String name, ServletOutputStream outputStream, Map<String,Object> params);
	
	/**
	 * 插入数据
	 * @param datas
	 * @return
	 */
	boolean insertExcelData(List<Object[]> datas);
	
	/**
	 * 导出excel
	 * @param name
	 * @param outputStream
	 * @param params
	 */
	void getExcel(String name, ServletOutputStream outputStream, Map<String,Object> params);
	
	
}
