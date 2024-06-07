package com.linyang.energy.mapping.energydataquery;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.CurveBean;

/**
 * @Description 电压电流功率Mapper
 * @author Leegern
 * @date Dec 4, 2013 5:18:10 PM
 */
public interface VoltCurrPowerMapper {
	
	/**
	 * 查询曲线数据
	 * @param params 查询参数
	 * @return
	 */
	List<CurveBean> queryVoltCurrPowerInfo(Map<String, Object> params);

    /**
     * 查询数据库中电压不平衡度、电流不平衡度列表
     * @param params 查询参数
     * @return
     */
    List<CurveBean> getNoblanceDataList(Map<String, Object> params);

    /**
     * 查询分户光伏曲线数据
     */
    List<CurveBean> getLightPower(Map<String, Object> params);
	
	/**
	 * 查询电压电流功率图表,整点数据，用于报表
	 * @author 周礼
	 * @start_time 2014.08.19 16:50:43 
	 * @param params 查询参数
	 * @return
	 */
	List<CurveBean> queryVoltCurrPowerReportInfo(Map<String, Object> params);
	
	/**
	 * 查询电压电流功率列表
	 * @param params 查询参数
	 * @return
	 */
	List<CurveBean> queryVoltCurrPowerList(Map<String, Object> params);
	
	/**
	 * 查询电压、电流、有功功率、无功功率、功率因数水平线
	 * @param param 查询参数
	 * @return
	 */
	List<Map<String, Object>> queryStraightLine(Map<String, Object> param);
	
	/**
	 * 查询测量点对应分户的标准功率因数
	 * 
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> queryStandPF(Map<String, Object> param);

	/**
	 * 查询曲线数据
	 * @param params
	 * @return
	 */
	List<CurveBean> queryCurveData(Map<String, Object> params);

	/**
	 * 查询曲线数据(用于报表)
	 * @param params
	 * @return
	 */
	List<CurveBean> queryCurveReportData(Map<String, Object> params);

	/**
	 * EMO,电压情况下，得到多块表的ID
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getVolMeters(Map<String, Object> params);
	
	/**
	 * 获取用户的y轴最小值设置
	 * @param accountId
	 * @return
	 */
	Integer getUserYMin(@Param("accountId")Long accountId);
	
	/**
	 * 更新用户y轴最小值设置
	 * @param accountId
	 * @param yMin
	 */
	void setUserYMin(@Param("accountId")Long accountId, @Param("yMin")Integer yMin);
}
