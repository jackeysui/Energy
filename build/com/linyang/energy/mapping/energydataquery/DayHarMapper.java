package com.linyang.energy.mapping.energydataquery;

import java.util.List;
import java.util.Map;

/**
 * 日谐波数据查询数据访问层接口
  @description:
  @version:0.1
  @author:周礼
  @date:2014.08.28 09:11:26
 */
public interface DayHarMapper {
	/**
	 * 查询日谐波柱状图数据
	  * @param queryMap 查询条件集合：
	  * 选择时间：key:"Time" value:{@code Date} ,"endTime" value:{@code Date} 
	  * 计量点号：key:"meterId"	value:{@code long} 
	  * 查询类型：key:"curveType"	value:{@code short}
	 * @return
	 */
	public List<Map<String,Object>> getDayHarChartDatas(Map<String,Object> map);
	/**
	 * 查询畸变柱状图数据
	  * @param queryMap 查询条件集合：
	  * 选择时间：key:"Time" value:{@code Date} ,"endTime" value:{@code Date} 
	  * 计量点号：key:"meterId"	value:{@code long} 
	  * 查询类型：key:"curveType"	value:{@code short}
	 * @return
	 */
	public List<Map<String,Object>> getDayDisChartDatas(Map<String,Object> map);
	/**
	 * 查询日谐波报表数据
	  * @param queryMap 查询条件集合：
	  * 选择时间：key:"Time" value:{@code Date} ,"endTime" value:{@code Date} 
	  * 计量点号：key:"meterId"	value:{@code long} 
	  * 查询类型：key:"curveType"	value:{@code short}
	 * @return
	 */
	public List<Map<String,Object>> getDayHarReportDatas(Map<String,Object> map);
	/**
	 * 查询日谐波畸变报表数据
	  * @param queryMap 查询条件集合：
	  * 选择时间：key:"Time" value:{@code Date} ,"endTime" value:{@code Date} 
	  * 计量点号：key:"meterId"	value:{@code long} 
	  * 查询类型：key:"curveType"	value:{@code short}
	 * @return
	 */
	public List<Map<String,Object>> getDayDisReportDatas(Map<String,Object> map);
	/** 
	 * 查询计量点名字
	  * @author 周礼
	  * @param queryMap 查询条件集合：
	  * 计量点Id：key:"meterId"	value:{@code long} 
	 * @return String
	 */
	public String getMeterName(Map<String,Object> map);
	
	/**
	 * 选择EMO，得到多块总表个数
	 * @param params
	 * @return
	 */
	public int getNum(Map<String, Object> params);
	
	/**
	 * 得到曲线有功功率
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getCurChartData(Map<String, Object> param);
	
	/**
	 * 查询三相曲线谐波电流，电压表 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getHarIVData(Map<String, Object> param);
}
