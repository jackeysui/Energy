package com.linyang.energy.mapping.demand;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.MeterBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 10:14 2019/9/7
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface LedgerDetailsMapper {
	
	
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
	List<Map<String,Object>> queryLedgerListPageData(Map<String, Object> queryMap);
	
	/**
	 * 得到需求响应概况列表信息
	 * @author catkins.
	 * @param queryMap
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/7 11:32
	 */
	Map<String,Object> queryOverview (Map<String,Object> queryMap);
	
	/**
	 * 通过用户权限的ledgerid获得权限下所有企业id(进行数据查询时使用)
	 * @param ledgerId
	 * @return
	 */
	List<Long> queryLedgerIds(long ledgerId);
	
	/**
	 * 得到需求响应方案图表信息
	 * @author catkins.
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/7 11:34
	 */
	List<Map<String,Object>> queryChartDatas(Map<String,Object> queryMap);
	
	/**
	 * 查询某事件段的电量
	 * @author catkins.
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/7 13:37
	 */
	Double queryAPavg(@Param( "ledgerId" ) Long ledgerId,@Param( "beginTime" ) Date beginTime,@Param( "endTime" )Date endTime);
	
	Double queryOneMeterFaq(@Param( "meterId" ) Long meterId,@Param( "beginTime" ) String beginTime,@Param( "endTime" )String endTime);
	
	Double queryOneLedgerFaq(@Param( "ledgerId" ) Long ledgerId,@Param( "beginTime" ) String beginTime,@Param( "endTime" )String endTime);
}
