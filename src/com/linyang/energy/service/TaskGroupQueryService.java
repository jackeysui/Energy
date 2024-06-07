package com.linyang.energy.service;

import java.util.List;

import com.linyang.energy.model.TaskJobGroup;

public interface TaskGroupQueryService {
	/**
	 * 任务群组查询
	 * @return
	 */
	List<TaskJobGroup> queryTaskGroupList();
	/**
	 * 任务群组查询
	 * @return
	 */
	int updateTaskGroup(TaskJobGroup record);
	
  /**
   * 更新任务状态
   * 
   * */
	void updateJobStatus(Long groupID);
	
}
