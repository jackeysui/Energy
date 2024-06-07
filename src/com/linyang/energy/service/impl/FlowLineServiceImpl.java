package com.linyang.energy.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.MeterBeanInfo;
import com.linyang.energy.mapping.authmanager.FlowLineMapper;
import com.linyang.energy.model.FlowLineBean;
import com.linyang.energy.model.ScheduleBean;
import com.linyang.energy.service.FlowLineService;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;


@Service
public class FlowLineServiceImpl implements FlowLineService {

	@Autowired
	private FlowLineMapper flowLineMapper;
	
	
	@Override
	public boolean addFlowLine(FlowLineBean bean) {
		boolean flag = false;
		bean.setAssembleId(SequenceUtils.getDBSequence());
		flowLineMapper.addFlowLine(bean);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("flowLineId", bean.getAssembleId());
		queryMap.put("ids", bean.getMeterIds());				//原			queryMap.put("ids",CommonMethod.getSeparaterStringNumber(",", bean.getMeterIds()));
		try{
			flowLineMapper.addMetersFlowLine(queryMap);
			flag = true;
		}
		catch (RuntimeException e) {
			Log.error(this.getClass()+".addFlowLine()--add flow line fail!");
		}
		return flag;
	}

	@Override
	public boolean deleteFlowLine(List<Long> flowLineIds){
		boolean flag = false;
		try{
			flowLineMapper.deleteFlowLine(flowLineIds);				//原flowLineMapper.deleteFlowLine(CommonMethod.getSeparaterStringNumber(",",flowLineIds));
		for (Long flowLineId : flowLineIds) {
			flowLineMapper.deleteMetersFlowLine(flowLineId);
		}
		flag = true;}catch(RuntimeException e){Log.info(this.getClass()+".deleteFlowLine()---delete flow line fail!");}
		return flag;
	}


	@Override
	public List<Map<String,Object>> pageList(Page page, Map<String, Object> queryMap) {
		queryMap.put("page", page);
		return flowLineMapper.getPageList(queryMap);
	}

	@Override
	public boolean updateFlowLine(FlowLineBean bean) {
		boolean flag = false;
		flowLineMapper.updateFlowLine(bean);
		flowLineMapper.deleteMetersFlowLine(bean.getAssembleId());
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("flowLineId", bean.getAssembleId());
		queryMap.put("ids",bean.getMeterIds());				//原queryMap.put("ids",CommonMethod.getSeparaterStringNumber(",", bean.getMeterIds()));
		//try{
		flag=flowLineMapper.addMetersFlowLine(queryMap)>0;
//		flag = true;
		//}catch(Exception e){Log.info(this.getClass()+".updateFlowLine()--update flow line fail");}
		return flag;
	}

	@Override
	public List<ScheduleBean> getScheduleInfos() {
		List<ScheduleBean> scheduleInfos = flowLineMapper.getScheduleInfos();
		return scheduleInfos==null?new ArrayList<ScheduleBean>():scheduleInfos;
	}

	@Override
	public FlowLineBean getInfo(long flowLineId) {
		FlowLineBean flowLine = flowLineMapper.getFlowLine(flowLineId);
		List<Map<String, Object>> flowLineMeters = flowLineMapper.getFlowLineMeters(flowLineId);
		if(CommonMethod.isCollectionNotEmpty(flowLineMeters)){
			for (Map<String, Object> map : flowLineMeters) {
				flowLine.getList().add(new MeterBeanInfo(Long.valueOf(map.get("METER_ID").toString()),map.get("METER_NAME").toString()));
			}
		}
		return flowLine;
	}

	@Override
	public boolean getPlanLink(Long assembleId) {
		boolean flag = true;
		if(flowLineMapper.getPlanLink(assembleId)!=0) {
			flag = false;
		}
		return flag;
	}

	@Override
	public boolean checkFlowLineName(String assembleName,String existName,String type) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(assembleName)){
			long count = flowLineMapper.checkFlowLineName(assembleName);//该名称存在的数量
			if("1".equals(type)) {
				if(count==0) {
					flag = true;//可以新增
				} else {
					flag = false;
				}
			} else if("2".equals(type)) {//修改
				if(assembleName.equals(existName)) {
					flag = true;
				} else {
					if(count == 0) {
						flag = true;
					} else {
						flag = false;
					}
				}
			} 
		}
		return flag;
	}

	@Override
	public boolean getMeterLink(Long meterId) {
		Long count = flowLineMapper.getMeterLink(meterId);
		if(count!=null) {
			return true;
		}
		return false;
	}

}
