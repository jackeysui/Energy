package com.linyang.energy.service;

import com.linyang.common.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 11:18 2020/5/27
 * @ Description：用能单位产品结构信息 + 非能源产品信息
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface StructureService {
	
	/**
	 * 查询产品结构信息分页列表
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/27 13:57
	 */
	List<Map<String, Object>> queryenProductPageList(Page page, Map<String, Object> queryMap);
	
	Integer saveAndModify(Map<String,Object> params);
	
	Map<String,Object> queryDataById(Long productId);
	
	Boolean delStructureData(Long productId);
	
	Map<String,Object> uploadStructure(Map<String,Object> params);
	
}
