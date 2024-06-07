package com.linyang.energy.service;

import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.CurveBean;



/**
 * @Description 曲线查询Service
 * @author Leegern
 * @date Dec 4, 2013 5:15:59 PM
 */
public interface VoltCurrPowerService {
	
	/**
	 * 查询曲线图表数据
	 * @param params 查询参数
	 * @return
	 */
	List<CurveBean> queryVoltCurrPowerInfo(Map<String, Object> params) throws ParseException;

    /**
     * 得到数据库中最新的 电压不平衡度、电流不平衡度
     * @param params 查询参数
     * @return
     */
    CurveBean getLastNoblanceData(Map<String, Object> params);

    /**
     *  查询光伏功率曲线
     */
    List<CurveBean> getLightPower(Map<String, Object> params) throws ParseException;
	
	/**
	 * 查询曲线报表数据
	 * @param params 查询参数
	 * @return
	 */
	List<CurveBean> queryVoltCurrPowerReportInfo(Map<String, Object> params) throws ParseException;

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
	Map<String, Object> queryStraightLine(Map<String, Object> param);
	

	/**
	 * 导出excel
	 */
	void getEleExcel(String sheetName, OutputStream output , Map<String, Object> result );

	/**
	 * 根据curveType得到标题
	 * @param curveType
	 * @return
	 */
	String getTitleByCurveType(int curveType);

	/**
	 * EMO,电压情况下，得到多块表
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getVolMeterIds(Map<String, Object> params);
	
	/**
	 * 获取用户的y轴最小值设置
	 * @param accountId
	 * @return
	 */
	Integer getUserYMin(Long accountId);
	
	/**
	 * 更新用户y轴最小值设置
	 * @param accountId
	 * @param yMin
	 */
	void setUserYMin(Long accountId, Integer yMin);
	
	/**
	 * 查询电表接线方式
	 * @param objId
	 * @return
	 */
	Integer queryCommMode(Long objId);
}
