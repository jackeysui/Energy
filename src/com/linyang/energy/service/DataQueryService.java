package com.linyang.energy.service;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.dto.ChartItemWithTime;


/**
 * 数据查询业务逻辑层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 12, 2013
 */
public interface DataQueryService {
	
	/**
	 * 得到热工消耗图表
	 * @param queryMap 查询条件集合：
	 * 开始时间：key:"beginDate" value:{@code long} 
	 * 结束时间：key:"endDate" value:{@code long}
	 * 计量点号：key:"meterId"	value:{@code long} 
	 * @return
	 */
	List<ChartItemWithTime> getThermalConsumptionChartData(Map<String,Object> queryMap);
	
	/**
	 * 得到电量图表
	 * @param queryMap 查询条件集合：
	 * 时间类型:key:"timeType" value:{@code int} 1表示day，2表示month，3表示year
	 * 开始时间：key:"beginDate" value:{@code long} 
	 * 结束时间：key:"endDate" value:{@code long}
	 * 计量点号：key:"meterId"	value:{@code long} 
	 * @return
	 */
	List<ChartItemWithTime> getElectricityChartData(Map<String,Object> queryMap);
	/**
	 * 得到Excel，数据填充
	 * @author 周礼
	 * @param 参数 table名字sheetName，输出流output，结果集map,页面请求信息queryMap
	 */
	void getEleExcel(String sheetName, OutputStream output,Map<String,Object> map,Map<String,Object> queryMap) throws UnsupportedEncodingException;


    /**
     * 分户实时功率查询
     * */
    List<Map<String, Object>> getPowData(Long ledgerId, Date baseDate);


    /**
     * 分户实时谐波数据
     * */
    Map<String, Object> getHarData(Long ledgerId);


    /**
     * 查询EMO当日、当月总电量
     * */
    Double getCurrentTotalQ(Long ledgerId, Date beginTime, Date endTime);

}
