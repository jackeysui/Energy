package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.PositionBean;

/**
 * 首页分户计量点
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 30, 2013
 */
public interface IndexChartService {
	
	/**
	 * 得到系统首页一个分户下分户和计量点的信息
	 * @param legerId 分户ID
	 * @return
	 */
	public List<Map<String,Object>> getPicInfo(long legerId);
	
	/**
	 * 得到一个分户的图片信息
	 * @param legerId  分户ID
	 * @return
	 */
	public Map<String,Object> getLegerPic(long legerId);
	
	/**
	 * 删除点信息，或者更新点信息
	 * @param positionBean
	 * @return
	 */
	public boolean updatePointPosition(PositionBean positionBean);
	/**
	 * 绑定位置成功后得到统计数据
	 * @param positionBean
	 * @return
	 */
	public Map<String,Object> getPositionCallBack(PositionBean positionBean);
	/**
	 * 点是否已经绑定过了
	 * @param positionBean
	 * @return
	 */
	public boolean isBunded(PositionBean positionBean);
	/**
	 * 得到统计数据
	 * @param legerId
	 * @param topN
	 * @return
	 */
	public Map<String,Object> getStat(long legerId,int topN);
	
	/**
	 * 得到第二层图片信息
	 * @param legerId
	 * @return
	 */
	public List<Map<String,Object>> getSecondPicInfo(long legerId);
	/**
	 * 得到一个分户的楼层信息
	 * @param legerId
	 * @return
	 */
	public List<Map<String,Object>> getLedgerFloors(long legerId); 
	/**
	 * 得到一个楼层的计量点信息
	 * @param legerId 分户id
	 * @param strucId 楼层id
	 * @return
	 */
	public  List<Map<String,Object>> getFloolMeters(long legerId,long strucId);
}
