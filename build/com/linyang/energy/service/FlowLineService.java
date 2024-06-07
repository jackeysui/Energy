package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.FlowLineBean;
import com.linyang.energy.model.ScheduleBean;

public interface FlowLineService {

	List<Map<String,Object>>  pageList(Page page,Map<String,Object> queryMap);
	
	boolean addFlowLine(FlowLineBean bean);
		
	boolean updateFlowLine(FlowLineBean bean);
	
	boolean deleteFlowLine(List<Long> flowLineIds);
	
	public List<ScheduleBean> getScheduleInfos();
	
	public FlowLineBean getInfo(long flowLineId); 
	
	public boolean getPlanLink(Long assembleId);
	
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的流水线名称
	 * @param assembleName
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean checkFlowLineName(String assembleName,String existName,String type);
	
	/**
	 * 
	 * 函数功能说明  :检查是否有测量点和流水线关联
	 * @param meterId
	 * @return      
	 * @return  Long     
	 * @throws
	 */
	public boolean getMeterLink(Long meterId);
}
