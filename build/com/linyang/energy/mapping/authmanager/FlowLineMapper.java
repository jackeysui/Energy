package com.linyang.energy.mapping.authmanager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.FlowLineBean;
import com.linyang.energy.model.ScheduleBean;

public interface FlowLineMapper {

	
	List<Map<String,Object>> getPageList(Map<String,Object> queryMap);
	
	void addFlowLine(FlowLineBean bean);
	
	int addMetersFlowLine(Map<String,Object> queryMap);
		
	void updateFlowLine(FlowLineBean bean);
	
	void deleteFlowLine(List<Long> flowLineIds);		//原	void deleteFlowLine(@Param("flowLineIds")String flowLineIds);
	
	void deleteMetersFlowLine(@Param("flowLineId")long flowLineId);
	
	public List<ScheduleBean> getScheduleInfos();
	
	public FlowLineBean getFlowLine(@Param("flowLineId")long flowLineId);
	
	public List<Map<String,Object>> getFlowLineMeters(@Param("flowLineId")long flowLineId);
	
	public Long getPlanLink(Long assembleId);
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的流水线名称
	 * @param assembleName
	 * @return      
	 * @return  Long     
	 * @throws
	 */
	public Long checkFlowLineName(String assembleName);
	/**
	 * 
	 * 函数功能说明  :获取某计量点是否和流水线关联
	 * @param meterId
	 * @return      
	 * @return  Long     
	 * @throws
	 */
	public Long getMeterLink(Long meterId);
}
