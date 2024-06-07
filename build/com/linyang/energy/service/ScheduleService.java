package com.linyang.energy.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import com.linyang.common.web.page.Page;
import com.linyang.energy.model.ScheduleBean;
import com.linyang.energy.model.ScheduleDetailBean;

public interface ScheduleService {
	
	/**
	 * 
	 * 函数功能说明  :分页查询排班信息
	 * @param page
	 * @param queryMa
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	List<Map<String,Object>> getSchedulePageData(Page page,Map<String, Object> queryMa);
	
	/**
	 * 
	 * 函数功能说明  :根据Id删除排班计划
	 * @param scheduleId      
	 * @return  void     
	 * @throws
	 */
	public boolean deleteScheduleData(Long scheduleId);
	
	/**
	 * 
	 * 函数功能说明  :检查是否有流水线和排班关联
	 * @param scheduleId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean getScheduleAssembleLink(Long scheduleId);
	
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的排班名称
	 * @param scheduleName
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public boolean checkScheduleName(String scheduleName);
	
	/**
	 * @throws IOException 
	 * 
	 * 函数功能说明  :添加排班信息
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean addScheduleData(ScheduleBean schedule,List<Map<String,Object>> map);
	
	/**
	 * 
	 * 函数功能说明  :根据排班Id获得相应的排班信息
	 * @param scheduleId
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
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean updateScheduleData(ScheduleBean schedule,List<Map<String,Object>> map);
}
