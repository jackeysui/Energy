/**
 * 
 */
package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.ProjectBean;

/** 
 * @类功能说明： 
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-30 上午08:59:55  
 * @版本：V1.0  */
public interface ProjectManagerService {
	/**
	 * 函数功能说明  :查询项目
	 * @param page
	 * @param queryCondition
	 * @return  List<ProjectBean>
	 */
	List<ProjectBean>  getprojectPagedata(Page page,Map<String,Object> queryCondition);
	/**
	 * 函数功能说明  :新增节能改造项目
	 * @param project 项目实体类
	 * @param investment 投资管理实体类
	 * @return  boolean
	 */
	public boolean addProjectInfo(ProjectBean project);
	/**
	 * 函数功能说明  :修改节能改造项目
	 * @param project 项目实体类
	 * @param investment 投资管理实体类
	 * @return  boolean
	 */
	public boolean updateProjectInfo(ProjectBean project);
	/**
	 * 函数功能说明  :删除项目
	 * @param projectId
	 * @return  boolean
	 */
	public boolean deleteProject(String projectId);
	/**
	 * 函数功能说明  :检查项目名称是否重复
	 * @param projectName
	 * @param operType
	 * @return  boolean
	 */
	public boolean checkProjectName(String projectName,int operType);
	/**
	 * 函数功能说明  :根据id获取项目信息
	 * @param projectId
	 * @return  ProjectBean
	 */
	public  ProjectBean  getProjectByProjectId(long projectId );
	/**
	 * 函数功能说明  :获取月历史图形数据
	 * @param map
	 * @return  List<Map<String,Object>>
	 */
	public  List<Map<String,Object>> getMonthChartData(Map<String, Object> map);
	/**
	 * 
	 * 函数功能说明  :获取季度历史图形数据
	 * @param map
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public  List<Map<String,Object>> getQuarterChartData(Map<String, Object> map);
	/**
	 * 函数功能说明  :获取月现在图形数据
	 * @param map
	 * @return  List<Map<String,Object>>
	 */
	public  List<Map<String,Object>> getMonthChartDataN(Map<String, Object> map);
	/**
	 * 
	 * 函数功能说明  :获取季度现在图形数据
	 * @param map
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public  List<Map<String,Object>> getQuarterChartDataN(Map<String, Object> map);
	/**
	 * 函数功能说明  :获取年历史图形数据
	 * @param map
	 * @return  List<Map<String,Object>>
	 */
	public  List<Map<String,Object>> getYearChartData(Map<String, Object> map);
	/**
	 * 函数功能说明  :获取年现在图形数据
	 * @param map
	 * @return  List<Map<String,Object>>
	 */
	public  List<Map<String,Object>> getYearChartDataN(Map<String, Object> map);
	

}
