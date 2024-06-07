package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.LedgerCost;

/**
 * 能耗分类统计、能耗分户统计业务逻辑层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 3, 2014
 */
public interface ClfcHouseStatisticStatService {

	/**
	 * 能耗分类统计图表
	 * @param queryMap 查询条件集合：
	 * 时间类型:key:"timeType" value:{@code int} 1表示day，2表示month，3表示year，4表示week
	 * 开始时间：key:"beginDate" value:{@code long} 
	 * 结束时间：key:"endDate" value:{@code long}
	 * 分户Id：key:"legerId"	value:{@code long} 
	 * @return
	 */
	Map<String,Object> getClfcChartDatas(Map<String,Object> queryMap);
	
	/**
	 * 子类一级分户的费用信息
	 * @param queryMap  查询条件集合：
	 * 时间类型:key:"timeType" value:{@code int} 1表示day，2表示month，3表示year，4表示week
	 * 开始时间：key:"beginDate" value:{@code long} 
	 * 结束时间：key:"endDate" value:{@code long}
	 * 分户Id：key:"legerId"	value:{@code long}
	 * @return
	 */
	public	List<LedgerCost> getLegderChildStat(Map<String,Object> queryMap);
	
	/**
	 * 
	 * @param page 分页对象
	 * @param queryMap 查询条件集合
	 * 时间:key:"time" value:{@code String} 格式:yyyy-MM-dd 例如2014-01-12
	 * 父类分户Id：key:"parentLegerId"	value:{@code long}
	 * 时间类型:key:"timeType" value:{@code int} 1表示day，2表示month，3表示year，4表示week
	 * @return
	 */
	public Map<String,Object> getHouseStat(Page page,Map<String,Object> queryMap);
	/**
	 * 得到分户的图片信息
	 * @param legerdIds
	 * @return
	 */
	public List<Map<String,Object>> getLegerdsPics(Long[] legerdIds);
	
}
