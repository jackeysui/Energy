package com.linyang.energy.service;


import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

/**
  @description:日谐波数据查询业务逻辑层接口
  @version:0.1
  @author:周礼
  @date:2014.08.28 13:24:36
 */

public interface DayHarService {
	/**
	 * 查询日谐波柱状图数据
	 * @param queryMap 查询条件集合：
	 * 选择时间：key:"Time" value:{@code Date} ,"endTime" value:{@code Date} 
	 * 计量点号：key:"mpointId"	value:{@code long} 
	 * 查询类型：key:"checkType"	value:{@code short}
	 * @return
	 */
	List<Map<String,Object>> getDayHarChartDatas(Map<String,Object> queryMap);
	/**
	 * 查询畸变柱状图数据
	 * @param queryMap 查询条件集合：
	 * 选择时间：key:"Time" value:{@code Date} ,"endTime" value:{@code Date} 
	 * 计量点号：key:"mpointId"	value:{@code long} 
	 * 查询类型：key:"checkType"	value:{@code short}
	 * @return
	 */
	List<Map<String,Object>> getDayDisChartDatas(Map<String,Object> queryMap);
	/**
	 * 查询日谐波报表数据
	 * @param queryMap 查询条件集合：
	 * 选择时间：key:"Time" value:{@code Date} ,"endTime" value:{@code Date} 
	 * 计量点号：key:"mpointId"	value:{@code long} 
	 * 查询类型：key:"checkType"	value:{@code short}
	 * @return
	 */
	List<Map<String,Object>> getDayHarReportDatas(Map<String,Object> queryMap);
	/**
	 * 查询日谐波畸变报表数据
	 * @param queryMap 查询条件集合：
	 * 选择时间：key:"Time" value:{@code Date} ,"endTime" value:{@code Date} 
	 * 计量点号：key:"mpointId"	value:{@code long} 
	 * 查询类型：key:"checkType"	value:{@code short}
	 * @return
	 */
	List<Map<String,Object>> getDayDisReportDatas(Map<String,Object> queryMap);
	/**
	 * 得到Excel，数据填充
	 * @author 周礼
	 * @param 参数 table名字sheetName，输出流output，结果集map,页面请求信息queryMap
	 */
	void getHarExcel(String sheetName, OutputStream output,Map<String,Object> map,Map<String,Object> queryMap);

	/**
	 * 选择EMO，得到多块总表
	 * @param params
	 * @return
	 */
	int getNum(Map<String, Object> params);
	
	/**
	 * 得到曲线有功功率
	 * @param param
	 * @return
	 */
	Map<String, Object> getCurChartData(Map<String, Object> param) throws ParseException;
	
	/**
	 * 导出曲线谐波有功功率，曲线基波有功功率
	 * @param string
	 * @param outputStream
	 * @param map
	 * @param param
	 * @param title 
	 */
	void exportExcel(String string, ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param, String title) throws UnsupportedEncodingException;
	
	/**
	 * 得到谐波电流电压曲线数据
	 * @param param
	 * @return
	 */
	Map<String, Object> getHarIVData(Map<String, Object> param) throws ParseException;
}
