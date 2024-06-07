
package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.projectManager.InvestmentMapper;
import com.linyang.energy.mapping.projectManager.ProjectMapper;
import com.linyang.energy.model.InvestmentBean;
import com.linyang.energy.model.ProjectBean;
import com.linyang.energy.service.ProjectManagerService;
import com.linyang.util.CommonMethod;

/** 
 * @类功能说明： 
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-30 上午09:00:51  
 * @版本：V1.0  */
@Service 
public class ProjectManagerServiceImpl implements ProjectManagerService{
	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private InvestmentMapper investmentMapper;

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#addProjectInfo(com.linyang.energy.model.ProjectBean)
	 */
	@Override
	public boolean addProjectInfo(ProjectBean project) {
		boolean isSuccess = false;
        if(project != null && CommonMethod.isNotEmpty(project.getProjectName())){
			projectMapper.insertSelective(project);
			InvestmentBean investment = new InvestmentBean();
			investment.setPredictInvest(project.getPredictInvest());
			investment.setActualInvest(project.getActualInvest());
			investment.setPredictReturn(project.getPredictReturn());
			investment.setActualReturn(project.getActualReturn());
			investment.setPredictSave(project.getPredictSave());
			investment.setActualSave(project.getActualSave());
			investment.setProjectId(project.getProjectId());
			investment.setProjectName(project.getProjectName());
			investmentMapper.insertSelective(investment);
			isSuccess = true;
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#updateProjectInfo(com.linyang.energy.model.ProjectBean)
	 */
	@Override
	public boolean updateProjectInfo(ProjectBean project) {
	    boolean isSuccess = false;
		if(project != null && CommonMethod.isNotEmpty(project.getProjectName())){
			projectMapper.updateByPrimaryKeySelective(project);
			InvestmentBean investment = new InvestmentBean();
			investment.setProjectId(project.getProjectId());
			investment.setProjectName(project.getProjectName());
			investment.setPredictInvest(project.getPredictInvest());
			investment.setActualInvest(project.getActualInvest());
			investment.setPredictReturn(project.getPredictReturn());
			investment.setActualReturn(project.getActualReturn());
			investment.setPredictSave(project.getPredictSave());
			investment.setActualSave(project.getActualSave());
			investmentMapper.updateByPrimaryKeySelective(investment);
			isSuccess = true;
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#deleteProject(long)
	 */
	@Override
	public boolean deleteProject(String projectId) {
		boolean isSuccess = false;
		if(projectId != null && projectId.length()>0){
            String[] projectIds = projectId.split(",");
            List<String> projectIdList = new ArrayList<String>();
            Collections.addAll(projectIdList, projectIds);
			projectMapper.deleteByPrimaryKey(projectIdList);		
			investmentMapper.deleteByPrimaryKey(projectIdList);
			isSuccess = true;
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#getProjectByProjectId(long)
	 */
	@Override
	public ProjectBean getProjectByProjectId(long projectId) {
		if(CommonMethod.isNotEmpty(projectId)){
			return projectMapper.getProjectByProjectId(projectId);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#checkProjectName(java.lang.String, int)
	 */
	@Override
	public boolean checkProjectName(String projectName, int operType) {
		boolean isSuccess = false;
		if(CommonMethod.isNotEmpty(projectName)){
			if( projectMapper.checkProjectName(projectName,operType) > 0){
				isSuccess = false;
			}else{
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#getChartData(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getMonthChartData(Map<String, Object> map) {
		 List<Map<String,Object>> list = null;
		 if(map.get("BeginDate") != null && map.get("lastEndDate") != null &&map.get("nextEndDate") != null ){
			 list = projectMapper.getMonthChartData(map);
		 }
		  return list == null?new ArrayList<Map<String,Object>>():list;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#getMonthChartDataN(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getMonthChartDataN(Map<String, Object> map) {
		 List<Map<String,Object>> list = null;
		 if(map.get("BeginDate1") != null && map.get("lastEndDate") != null &&map.get("nextEndDate") != null ){
			 list = projectMapper.getMonthChartDataN(map);
		 }
		 return list == null?new ArrayList<Map<String,Object>>():list;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#getYearChartData(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getYearChartData(Map<String, Object> map) {
		 List<Map<String,Object>> list = null;
		 if(map.get("BeginDate") != null && map.get("lastEndDate") != null &&map.get("nextEndDate") != null ){
			 list = projectMapper.getYearChartData(map);
		 }
		  return list == null?new ArrayList<Map<String,Object>>():list;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#getYearChartDataN(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getYearChartDataN(Map<String, Object> map) {
		 List<Map<String,Object>> list = null;
		 if(map.get("BeginDate") != null && map.get("lastEndDate") != null &&map.get("nextEndDate") != null ){
			 list = projectMapper.getYearChartDataN(map);
		 }
		  return list == null?new ArrayList<Map<String,Object>>():list;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.ProjectManagerService#getprojectPagedata()
	 */
	@Override
	public List<ProjectBean> getprojectPagedata(Page page,Map<String,Object> queryCondition) {
		 List<ProjectBean> list = null;
		if(page != null){
				if(page != null){
					Map<String,Object> map = new HashMap<String, Object>(queryCondition);
					map.put(Dialect.pageNameField, page);
					list = projectMapper.getprojectPagedata(map);
				}
		}
		return list == null?new ArrayList<ProjectBean>():list;
	}

	@Override
	public List<Map<String, Object>> getQuarterChartData(Map<String, Object> map) {
		List<Map<String,Object>> list = null;
		 if(map.get("BeginDate") != null && map.get("lastEndDate") != null &&map.get("nextEndDate") != null ){
			 list = projectMapper.getQuarterChartData(map);
		 }
		  return list == null?new ArrayList<Map<String,Object>>():list;
	}

	@Override
	public List<Map<String, Object>> getQuarterChartDataN(Map<String, Object> map) {
		List<Map<String,Object>> list = null;
		if(map.get("BeginDate1") != null && map.get("lastEndDate") != null &&map.get("nextEndDate") != null ){
			 list = projectMapper.getQuarterChartDataN(map);
		}
		return list == null?new ArrayList<Map<String,Object>>():list;
	}

}
