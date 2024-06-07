package com.linyang.energy.mapping.energydataquery;

import com.linyang.energy.model.LedgerBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 数据查询数据访问层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 16, 2013
 */
public interface DataQueryMapper {
	
	/**
	 * 查询热工类报表数据
	  * @param queryMap 查询条件集合：
	  * 开始时间：key:"beginTime" value:{@code Date} 
	  * 结束时间：key:"endTime" value:{@code Date}
	  * 计量点号：key:"meterId"	value:{@code long} 
	  * 表名称：key:"tableName"	value:{@code String}
	  * 时间字段名称：key:"timeField"	value:{@code String}
	  * 字段值名称：key:"valueField"	value:{@code String}
	 * @return
	 */
	public List<Map<String,Object>> getThermalConsumptionDatas(Map<String,Object> map);
	
	/**
	 * 查询电力报表数据
	  * @param queryMap 查询条件集合：
	  * 开始时间：key:"beginTime" value:{@code Date} 
	  * 结束时间：key:"endTime" value:{@code Date}
	  * 计量点或者分户Id：key:"tId"	value:{@code long} 
	  * 类型：key:"type"	value:{@code long}1表示分户2表示计量点
	  * 时间类型：key:"timeType"	value:{@code int}1表示天2表示月3表示年
	  *  费率id：key:"rateId"	value:{@code long} 0表示没有费率（只针对计量点有效）
	 * @return
	 */
	public List<Map<String,Object>> getEleValue(Map<String,Object> map);
	/**
	 * 查询计量点名字
	  * @author 周礼
	  * @param queryMap 查询条件集合：
	  * 计量点Id：key:"tId"	value:{@code long} 
	 * @return String
	 */
	public String getMeterName(Map<String,Object> map);


    /**
     * 查询分户实时功率
     * */
    public List<Map<String, Object>> getLedger96ApData(Map<String, Object> param);

    /**
     * 查询分户实时无功功率
     * */
    public List<Map<String, Object>> getLedger96RpData(Map<String, Object> param);

    /**
     * 查询分户实时功率因数
     * */
    public List<Map<String, Object>> getLedger96PfData(Map<String, Object> param);

    /**
     * 查询采集点实时功率
     * */
    public List<Map<String, Object>> getMeterCurApData(Map<String, Object> param);

    /**
     * 查询采集点实时无功功率
     * */
    public List<Map<String, Object>> getMeterCurRpData(Map<String, Object> param);

    /**
     * 查询采集点实时功率因数
     * */
    public List<Map<String, Object>> getMeterCurPfData(Map<String, Object> param);

    /**
     * 查询采集点实时电压
     * */
    public List<Map<String, Object>> getMeterCurVData(Map<String, Object> param);

    /**
     * 查询采集点实时电流
     * */
    public List<Map<String, Object>> getMeterCurIData(Map<String, Object> param);

    /**
     * 获取ledger的t_cur_ap计算模型中,最新的时间点
     * */
    List<Date> getgetMaxDateListCurAp(@Param("ledgerId")long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 获取ledger的t_cur_ap计算模型中 某一时间点对应的值
     * */
    Double getCurPowData(@Param("ledgerId")long ledgerId, @Param("time")Date time);

    Double getRegionCurPowData(@Param("ledgers")List<LedgerBean> ledgers, @Param("time")Date time);

    Double getRegionLastMonQData(@Param("ledgers")List<LedgerBean> ledgers, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 查询EMO当日、当月总电量
     **/
    Double getCurrentTotalQ(@Param("ledgerId")long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
    
	/**
	 * 查询采集点实时电压(下限)
	 * */
	public List<Map<String, Object>> getMeterMinCurVData(Map<String, Object> param);
	
	/**
	 * 查询水量曲线数据
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> getMeterWaterData(Map<String,Object> param);
	
	/**
	 * 查询日水/气/电量数据
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> getLedgerDayData(Map<String,Object> param);
	
}
