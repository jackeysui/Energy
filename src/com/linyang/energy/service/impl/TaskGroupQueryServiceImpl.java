package com.linyang.energy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.mapping.JobInfo.JobUpdateMapper;
import com.linyang.energy.mapping.datataskquery.TaskGroupMapper;
import com.linyang.energy.model.TaskJobGroup;
import com.linyang.energy.service.TaskGroupQueryService;
@Service
public class TaskGroupQueryServiceImpl implements TaskGroupQueryService{
	@Autowired
	private TaskGroupMapper taskGroupQueryMapper;
	@Autowired
	private JobUpdateMapper jobuodateMapper;
	/**
	 * 任务群组查询
	 * @return
	 */
public	List<TaskJobGroup> queryTaskGroupList()
{
  return taskGroupQueryMapper.getTaskGroup();	
}

/**
 * 任务群组查询
 * @return
 */
public	int  updateTaskGroup(TaskJobGroup record)
{
return taskGroupQueryMapper.updateTaskGroup(record);	
}

/**
 * 任务群组查询
 * @return
 */
public	void updateJobStatus(Long groupID)
{
 jobuodateMapper.updateJobStatus(groupID);	
}
}
