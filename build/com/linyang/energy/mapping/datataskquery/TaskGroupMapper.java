package com.linyang.energy.mapping.datataskquery;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.ProjectBean;
import com.linyang.energy.model.TaskJobGroup;;

public interface TaskGroupMapper {
	/**
	 * 
	 * 函数功能说明  :获取任务群组列表
	 * @return      
	 * @return  List<TaskJobGroup>     
	 * @throws
	 */
	public List<TaskJobGroup>  getTaskGroup();
	/**
	 * 
	 * 函数功能说明  :更新任务群组列表
	 * @return      
	 * @return  int 
	 * @throws
	 */
	int updateTaskGroup(TaskJobGroup record);
}
