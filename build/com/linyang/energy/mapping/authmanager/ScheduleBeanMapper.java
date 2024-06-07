package com.linyang.energy.mapping.authmanager;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.ScheduleBean;
import com.linyang.energy.model.ScheduleDetailBean;

public interface ScheduleBeanMapper {
	/**
	 * 
	 * 函数功能说明  :分页获取排班列表
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<String, Object>>  getSchedulePageData(Map<String, Object> map);
	/**
	 * 
	 * 函数功能说明  :根据Id删除排班计划
	 * @param scheduleId      
	 * @return  void     
	 * @throws
	 */
	public void deleteSchedule(Long scheduleId);
	/**
	 * 
	 * 函数功能说明  :根据Id删除排班计划配置 
	 * @param scheduleId      
	 * @return  void     
	 * @throws
	 */
	public void deleteScheduleDetail(Long scheduleId);
	
	/**
	 * 
	 * 函数功能说明  :
	 * @param scheduleId      
	 * @return  void     
	 * @throws
	 */
	public void deleteGradeData(Long scheduleId);
	/**
	 * 
	 * 函数功能说明  :检查是否有流水线和排班关联
	 * @param scheduleId
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public long getScheduleAssembleLink(Long scheduleId);
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的排班名称
	 * @param scheduleName
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public long checkScheduleName(String scheduleName);
	
	/**
	 * 
	 * 函数功能说明  :插入排班信息
	 * @param schedule      
	 * @return  void     
	 * @throws
	 */
	public void insertScheduleData(ScheduleBean schedule);
	/**
	 * 
	 * 函数功能说明  :插入班次信息
	 * @param grade      
	 * @return  void     
	 * @throws
	 */
	public void insertScheduleDetail(ScheduleDetailBean grade);
	
	/**
	 * 
	 * 函数功能说明  :根据排班Id获得相应的排班信息
	 * @return      
	 * @return  ScheduleBean     
	 * @throws
	 */
	public ScheduleBean getScheduleData(Long scheduleId);
	/**
	 * 
	 * 函数功能说明  :根据排班Id获得相应的班次信息
	 * @param scheduleId
	 * @return      
	 * @return  List<ScheduleDetailBean>     
	 * @throws
	 */
	public List<ScheduleDetailBean> getGradeData(Long scheduleId);
	
	/**
	 * 
	 * 函数功能说明  :更新排班信息
	 * @param schedule      
	 * @return  void     
	 * @throws
	 */
	public void updateScheduleInfo(ScheduleBean schedule);
	
	/**
	 * 
	 * 函数功能说明  :更新班次信息
	 * @param grade      
	 * @return  void     
	 * @throws
	 */
	public void updateGradeInfo(ScheduleDetailBean grade);
	
}
