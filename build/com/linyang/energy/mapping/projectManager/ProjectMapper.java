package com.linyang.energy.mapping.projectManager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.ProjectBean;

public interface ProjectMapper {
	 int deleteByPrimaryKey(List<String> projectIdList);//原int deleteByPrimaryKey(@Param("projectId")String projectId);

    int insert(ProjectBean record);

    int insertSelective(ProjectBean record);

    ProjectBean selectByPrimaryKey(Long projectId);

    int updateByPrimaryKeySelective(ProjectBean record);

    int updateByPrimaryKey(ProjectBean record);
    /**
     * 函数功能说明  :查询项目
     * @param queryCondition
     * @return  List<ProjectBean>
     */
    List<ProjectBean> getprojectPagedata(Map<String,Object> queryCondition);
    /**
     * 函数功能说明  :检查项目名称是否重复
     * @param projectName 项目名称
     * @param operType 操作类型
     * @return  int
     */
    int checkProjectName(@Param("projectName")String projectName,@Param("operType")int operType);
    /**
     * 函数功能说明  :根据id查询项目信息
     * @param projectId
     * @return  ProjectBean
     */
    ProjectBean getProjectByProjectId(@Param("projectId")long projectId);
    /**
     * 函数功能说明  :获取历史月数据
     * @param queryCondition
     * @return  List<Map<String,Object>>
     */
    List<Map<String,Object>> getMonthChartData(Map<String,Object> queryCondition);
    /**
     * 
     * 函数功能说明  :获取历史季度数据
     * @param queryCondition
     * @return      
     * @return  List<Map<String,Object>>     
     * @throws
     */
    List<Map<String,Object>> getQuarterChartData(Map<String,Object> queryCondition);
    /**
     * 函数功能说明  :获取现在月数据
     * @param queryCondition
     * @return  List<Map<String,Object>>
     */
    List<Map<String,Object>> getMonthChartDataN(Map<String,Object> queryCondition);
    /**
     * 
     * 函数功能说明  :获取现在季度数据
     * @param queryCondition
     * @return      
     * @return  List<Map<String,Object>>     
     * @throws
     */
    List<Map<String,Object>> getQuarterChartDataN(Map<String,Object> queryCondition);
    /**
     * 函数功能说明  :获取历史年数据
     * @param queryCondition
     * @return  List<Map<String,Object>>
     */
    List<Map<String,Object>> getYearChartData(Map<String,Object> queryCondition);
    /**
     * 函数功能说明  :获取现在年数据
     * @param queryCondition
     * @return  List<Map<String,Object>>
     */
    List<Map<String,Object>> getYearChartDataN(Map<String,Object> queryCondition);
    
}