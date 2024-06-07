package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.authmanager.ScheduleBeanMapper;
import com.linyang.energy.model.ScheduleBean;
import com.linyang.energy.model.ScheduleDetailBean;
import com.linyang.energy.service.ScheduleService;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;

@Service
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleBeanMapper scheduleMapper;

	@Override
	public List<Map<String, Object>> getSchedulePageData(Page page,
			Map<String, Object> queryMa) {
		queryMa.put(Dialect.pageNameField, page);
		List<Map<String,Object>> list = scheduleMapper.getSchedulePageData(queryMa);
		return list == null ? new ArrayList<Map<String,Object>>() : list;
	}

	@Override
	public boolean deleteScheduleData(Long scheduleId) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(scheduleId)){
			scheduleMapper.deleteScheduleDetail(scheduleId);//根据Id删除排班计划配置 
			scheduleMapper.deleteSchedule(scheduleId);//根据Id删除排班计划
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean getScheduleAssembleLink(Long scheduleId) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(scheduleId)){
			//检查是否有流水线和排班关联
			if(scheduleMapper.getScheduleAssembleLink(scheduleId)==0) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public boolean checkScheduleName(String scheduleName) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(scheduleName)){
			//检查是否有排班名称重复
			if(scheduleMapper.checkScheduleName(scheduleName)==0) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public boolean addScheduleData(ScheduleBean schedule,List<Map<String,Object>> map){
		boolean flag = false;
		if(schedule!=null) {
			if(CommonMethod.isCollectionNotEmpty(map)) {
				int count=0;
				scheduleMapper.insertScheduleData(schedule);
				for(Map<String, Object> grade : map) {
					count++;
					ScheduleDetailBean sdb = new ScheduleDetailBean();
					sdb.setGradeId(SequenceUtils.getDBSequence()+count);
					sdb.setGradeName(grade.get("gradeName").toString());
					sdb.setStartTime(grade.get("startTime").toString());
					sdb.setEndTime(grade.get("endTime").toString());
					sdb.setScheduleId(schedule.getScheduleId());
					scheduleMapper.insertScheduleDetail(sdb);
				}
			}
			flag=true;
		}
		return flag;
	}

	@Override
	public List<ScheduleDetailBean> getGradeData(Long scheduleId) {
		List<ScheduleDetailBean> list = scheduleMapper.getGradeData(scheduleId);
		return list==null?new ArrayList<ScheduleDetailBean>():list;
	}

	@Override
	public ScheduleBean getScheduleData(Long scheduleId) {
		ScheduleBean schedule = scheduleMapper.getScheduleData(scheduleId);
		return schedule==null? new ScheduleBean():schedule;
	}

	@Override
	public boolean updateScheduleData(ScheduleBean schedule,
			List<Map<String, Object>> map) {
		boolean flag = false;
		if(schedule!=null) {
			if(CommonMethod.isCollectionNotEmpty(map)) {
				List<ScheduleDetailBean> list = scheduleMapper.getGradeData(schedule.getScheduleId());
				Map<Long, Object> gradeMap = new HashMap<Long, Object> ();
				if(CommonMethod.isCollectionNotEmpty(list)) {
					for(ScheduleDetailBean detailData : list) {
						gradeMap.put(detailData.getGradeId(), detailData);
					}
				}
				scheduleMapper.updateScheduleInfo(schedule);
				int count=0;
				for(Map<String, Object> grade : map) {
					count++;
					ScheduleDetailBean sdb = new ScheduleDetailBean();
					long gradeId = Long.valueOf(grade.get("gradeId").toString());
					gradeMap.remove(gradeId);//去掉前台获取到的配置表中的已经存在的信息
					sdb.setGradeName(grade.get("gradeName").toString());
					sdb.setStartTime(grade.get("startTime").toString());
					sdb.setEndTime(grade.get("endTime").toString());
					sdb.setScheduleId(schedule.getScheduleId());
					if(gradeId==-100) {
						sdb.setGradeId(SequenceUtils.getDBSequence()+count);
						scheduleMapper.insertScheduleDetail(sdb);
					} else {
						sdb.setGradeId(gradeId);
						scheduleMapper.updateGradeInfo(sdb);
					}
				}
				if(CommonMethod.isMapNotEmpty(gradeMap)) {
					for(Long key : gradeMap.keySet()) {
						scheduleMapper.deleteGradeData(key);
					}
				}
			}
			flag=true;
		}
		return flag;
	}
	

}
