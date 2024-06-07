package com.linyang.energy.mapping.index;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.PositionBean;

public interface IndexChartMapper {
	
	/**
	 * 得到一个分户下
	 * @param legerId 分户id
	 * @param today 当天的时间
	 * @return
	 */
	public List<Map<String,Object>> getChildLegers(@Param("legerId")long legerId,@Param("today")Timestamp today);
	/**
	 * 得到一个分户下计量点的信息
	 * @param legerId 计量点id
	 * @param today 当天的时间
	 * @return
	 */
	public List<Map<String,Object>> getLegerMeters(@Param("legerId")long legerId,@Param("today")Timestamp today);
	
	
	/**
	 * 得到一个分户的图片信息
	 * @param lengerId  分户ID
	 * @return
	 */
	public List<Map<String,Object>> getLegerPic(@Param("legerId")long lengerId);
	
	
	public void updatePointPosition(PositionBean positionBean);
	/**
	 * 点是否已经绑定过了
	 * @param positionBean
	 * @return
	 */
	public int isBunded(PositionBean positionBean);
	/**
	 * 得到统计数据
	 * @param queryMap
	 * @return
	 */
	public List<Map<String,Object>> getStat(Map<String,Object> queryMap);
	/**
	 * 得到计量点的当天的电量值
	 * @param queryMap
	 * @return
	 */
	public Double getMeterQ(Map<String,Object> queryMap);
	
	/**
	 * 本月用电量排名
	 * @param queryMap
	 * @return
	 */
	public List<Map<String,Object>> getElePageTopN(Map<String,Object> queryMap);
	
	/**
	 * 得到一个分户的楼层信息
	 * @param legerId
	 * @return
	 */
	public List<Map<String, Object>> getLedgerFloors(@Param("legerId")long legerId);
	
	/**
	 * 得到一个楼层的计量点信息
	 * @param legerId
	 * @param strucId
	 * @param today
	 * @return
	 */
	public List<Map<String, Object>> getFloolMeters(@Param("legerId")long legerId,@Param("strucId")long strucId,@Param("today")Timestamp today);

}
