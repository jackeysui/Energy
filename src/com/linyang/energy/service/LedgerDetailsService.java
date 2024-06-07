package com.linyang.energy.service;

import com.linyang.common.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 10:13 2019/9/7
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface LedgerDetailsService {
	
	/**
	 * 获取方案列表
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/10 11:06
	 */
	List<Map<String,Object>> queryPlanList();
	
	/**
	 * 根据方案id获取方案详细信息
	 * @author catkins.
	 * @param planId
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/7 10:17
	 */
	Map<String,Object> queryPlanDetailsById(long planId);
	
	/**
	 * 得到方案下企业列表
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/7 11:22
	 */
	List<Map<String,Object>> queryLedgerListPageData(Page page, Map<String, Object> queryMap) throws Exception;
	
	/**
	 * 得到需求响应概况列表信息
	 * @author catkins.
	 * @param queryMap
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/7 11:32
	 */
	Map<String,Object> queryOverview (Map<String,Object> queryMap) throws Exception;
	
	/**
	 * 得到需求响应方案图表信息
	 * @author catkins.
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/7 11:34
	 */
	List<Map<String,Object>> queryChartDatas(Map<String,Object> queryMap) throws Exception;
	

}
