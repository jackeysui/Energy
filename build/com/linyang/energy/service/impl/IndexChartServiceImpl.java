package com.linyang.energy.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.index.IndexChartMapper;
import com.linyang.energy.model.PositionBean;
import com.linyang.energy.service.IndexChartService;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;
/**
 * 系统首页图表业务逻辑层接口实现
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 30, 2013
 */
@Service
public class IndexChartServiceImpl implements IndexChartService {

	@Autowired
	private IndexChartMapper indexChartMapper;
	
	private Timestamp today = new Timestamp(DateUtils.convertTimeToLong(DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT), DateUtils.FORMAT_SHORT)*1000) ;
	
	@Override
	public List<Map<String, Object>> getPicInfo(long lengerId) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> childLegers = indexChartMapper.getChildLegers(lengerId,today);
		if(CommonMethod.isCollectionNotEmpty(childLegers)){
			list.addAll(childLegers);
		}
		List<Map<String, Object>> legerMeters = indexChartMapper.getLegerMeters(lengerId,today);
		if(CommonMethod.isCollectionNotEmpty(legerMeters)){
			list.addAll(legerMeters);
		}
		return list;
	}

	@Override
	public Map<String,Object> getLegerPic(long lengerId) {
		 List<Map<String, Object>> legerPic = indexChartMapper.getLegerPic(lengerId);
		 if(CommonMethod.isCollectionNotEmpty(legerPic))
			 return  legerPic.get(0);
		 else
			 return new HashMap<String, Object>();
	}

	@Override
	public boolean updatePointPosition(PositionBean positionBean) {
		boolean isSuccess = false;
		if(positionBean != null){
		if(positionBean.isPointValidate()){
			positionBean.setNull2Point();
		}
			indexChartMapper.updatePointPosition(positionBean);
			isSuccess = true;
		}
		return isSuccess;
	}

	@Override
	public boolean isBunded(PositionBean positionBean) {
		return indexChartMapper.isBunded(positionBean)>0;
	}

	@Override
	public Map<String, Object> getStat(long legerId,int topN) {
		Map<String,Object> map = new HashMap<String, Object>();
		Timestamp monthFirstDay = new Timestamp(DateUtils.convertTimeToLong(DateUtils.getMonthFirstDay(),DateUtils.FORMAT_SHORT)*1000);
		Timestamp monthLastDay = new Timestamp(DateUtils.convertTimeToLong(DateUtils.getMonthLastDay(),DateUtils.FORMAT_SHORT)*1000);
		
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("legerId", legerId);
		//日的
		condition.put("beginDate", today);
		condition.put("endDate", today);
		condition.put("type", 1);
		List<Map<String, Object>> statDay = indexChartMapper.getStat(condition);
		if(CommonMethod.isNotEmpty(statDay))
			map.putAll(statDay.get(0));
		//月的
		condition.put("beginDate", monthFirstDay);
		condition.put("endDate", monthLastDay);
		condition.put("type",2);
		List<Map<String, Object>> statMon = indexChartMapper.getStat(condition);
		if(CommonMethod.isNotEmpty(statMon))
			map.putAll(statMon.get(0));
		
		//为了屏蔽oracle和mysql的问题
		condition.put(Dialect.pageNameField, new Page(1,topN));
		map.put("list", indexChartMapper.getElePageTopN(condition));
		return map;
	}

	@Override
	public List<Map<String, Object>> getSecondPicInfo(long legerId) {
		List<Map<String, Object>> legerMeters = indexChartMapper.getLegerMeters(legerId,today);
		return legerMeters==null?new ArrayList<Map<String,Object>>():legerMeters;
	}

	@Override
	public List<Map<String, Object>> getLedgerFloors(long legerId) {
		List<Map<String, Object>> ledgerFloors = indexChartMapper.getLedgerFloors(legerId);
		return ledgerFloors==null?new ArrayList<Map<String,Object>>():ledgerFloors;
	}

	@Override
	public List<Map<String, Object>> getFloolMeters(long legerId, long strucId) {
		List<Map<String, Object>> floolMeters = indexChartMapper.getFloolMeters(legerId,strucId,today);
		return floolMeters==null?new ArrayList<Map<String,Object>>():floolMeters;
	}

	@Override
	public Map<String, Object> getPositionCallBack(PositionBean positionBean) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("beginDate", today);
		condition.put("endDate", today);
		if(positionBean.getType()==1){
			condition.put("legerId", positionBean.getId());
			//水
			condition.put("type", 1);
			List<Map<String, Object>> statDay = indexChartMapper.getStat(condition);
			if(CommonMethod.isNotEmpty(statDay))
				map.putAll(statDay.get(0));
		}else{//计量点电量值
			condition.put("meterId", positionBean.getId());
			map.put("faqValue", indexChartMapper.getMeterQ(condition));
		}
		return map;
	}
}
