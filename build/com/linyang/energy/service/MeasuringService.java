package com.linyang.energy.service;


import com.linyang.common.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:34 2020/5/14
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface MeasuringService {
	
	/**
	 * 查询计量器管理列表数据
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/14 14:32
	 */
	List<Map<String, Object>> queryMeasuringPageList(Page page, Map<String,Object> queryMap);
	
	/**
	 * 修改和新增计量器具方法
	 * @author catkins.
	 * @param params
	 * @return java.lang.Integer
	 * @exception
	 * @date 2020/5/15 10:47
	 */
	Integer saveAndModify(Map<String,Object> params);
	
	/**
	 * 根据msiId查询相关数据
	 * @author catkins.
	 * @param msiId
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/15 17:11
	 */
	Map<String,Object> queryDataById(Long msiId);
	
	/**
	 * 查询检测机构模糊查询框数据
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/15 17:14
	 */
	List<Map<String,Object>> queryAlignOrg();
	
	
	/**
	 * 查询计量器具类型查询框数据
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/18 9:56
	 */
	List<Map<String,Object>> queryMsiType();
	
	/**
	 * 根据计量器具id删除数据
	 * @author catkins.
	 * @param msiId
	 * @return boolean
	 * @exception
	 * @date 2020/5/18 9:57
	 */
	boolean deleteMsiDataById(Long msiId);
	
	
	Map<String,Object> uploadData(Map<String,Object> params);
	
	
	List<Map<String,Object>> queryDataCode();
	
	
	List<Map<String,Object>> queryEnType();
	
	List<Map<String,Object>> queryLedgerByRegion(Long ledgerId);
}
