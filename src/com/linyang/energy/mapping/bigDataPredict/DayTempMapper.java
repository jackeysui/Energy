package com.linyang.energy.mapping.bigDataPredict;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.DayTempBean;

public interface DayTempMapper {

	/**
	 * 根据参数得到用户温度信息
	 * @param param
	 * @return
	 */
	public List<DayTempBean> getDayTempByParam(Map<String, Object> param); 
	
	/**
	 * 得到分户的曲线数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerAP(Map<String, Object> param);
	
	/**
	 * 得到分户的曲线数据
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerQ(Map<String, Object> param);
	
	/**
	 * 得到用户功率和
	 */
	public Double sumLedgerAP(Map<String, Object> param);
	
	/**
	 * 得到用户电量和
	 */
	public Double sumLedgerQ(Map<String, Object> param);

	/**
	 * 得到所有的区域代码
	 * @return
	 */
	public List<String> getRegionList();

	/**
	 * 根据区域代码得到区域名称
	 * @param region
	 * @return
	 */
	public String getNameByRegion(@Param("region") String region);
	
	/**
	 * 增加天气情况
	 * @param bean
	 */
	public void addDayTempInfo(DayTempBean bean);
	
}
