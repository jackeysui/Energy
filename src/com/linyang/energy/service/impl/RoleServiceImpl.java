package com.linyang.energy.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.authmanager.ModuleBeanMapper;
import com.linyang.energy.mapping.authmanager.RoleBeanMapper;
import com.linyang.energy.mapping.authmanager.RoleModuleRelationBeanMapper;
import com.linyang.energy.model.EventSettingRecBean;
import com.linyang.energy.model.ModuleBean;
import com.linyang.energy.model.RoleBean;
import com.linyang.energy.model.RoleModuleRelationBean;
import com.linyang.energy.service.RoleService;
import com.linyang.util.CommonMethod;
/**
 * @类功能说明： 角色业务逻辑层接口实现类
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-6 下午03:45:47  
 * @版本：V1.0
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleBeanMapper roleBeanMapper;
	@Autowired
	private ModuleBeanMapper moduleBeanMapper;
	@Autowired
	private RoleModuleRelationBeanMapper roleModuleRelationBeanMapper;
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#getRolePageList(com.linyang.common.web.page.Page, java.util.Map)
	 */
	@Override
	public List<Map<String,Object>> getRolePageList(Page page,Map<String, Object> queryCondition) {
		 List<Map<String,Object>> list = null;
			if(page != null){
				Map<String,Object> map = new HashMap<String, Object>(queryCondition);
				map.put(Dialect.pageNameField, page);
//				if(queryCondition.get("roleStatus")!= null && CommonMethod.isNotEmpty(queryCondition.get("roleStatus").toString()))
//				    map.put("roleStatus", queryCondition.get("roleStatus"));
//				if(queryCondition.get("roleType")!= null && CommonMethod.isNotEmpty(queryCondition.get("roleType").toString()))
//				    map.put("roleType", queryCondition.get("roleType"));
				if((queryCondition.get("beginTime")!= null && CommonMethod.isNotEmpty(queryCondition.get("beginTime").toString()))  && (queryCondition.get("endTime")!= null && CommonMethod.isNotEmpty(queryCondition.get("endTime").toString())) ){
					map.put("beginTime", new Date(Long.valueOf(queryCondition.get("beginTime").toString())*1000));
					map.put("endTime", new Date(Long.valueOf(queryCondition.get("endTime").toString())*1000));
				}
				if((queryCondition.get("beginTime1")!= null && CommonMethod.isNotEmpty(queryCondition.get("beginTime1").toString()))  && (queryCondition.get("endTime1")!= null && CommonMethod.isNotEmpty(queryCondition.get("endTime1").toString())) ){
					map.put("beginTime1", new Date(Long.valueOf(queryCondition.get("beginTime1").toString())*1000));
					map.put("endTime1", new Date(Long.valueOf(queryCondition.get("endTime1").toString())*1000));
				}
				list = roleBeanMapper.getRolePageData(map);
			}
			return list == null?new ArrayList<Map<String,Object>>():list;
	}
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#addRoleInfo(com.linyang.energy.model.RoleBean)
	 */
	@Override
	public boolean addRoleInfo(RoleBean role,List<Long> moduleIds) {
		boolean isSuccess = false;
		if(role != null&&moduleIds != null){
			
			/**
			 * 检测一下，如果有相同对象的设置，就更新，只保留一条数据
			 * */
			Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("roleName", role.getRoleName());
            queryMap.put("roleStatus", role.getRoleStatus());
            queryMap.put("roleDesc", role.getRoleDesc());
            List<RoleBean> eventList = roleBeanMapper.getFilterdRole(queryMap);
            if(eventList.size()>0)
            {
            	isSuccess = false;
            	
            }
            else
            {		
            	//默认时间
            	role.setCreateDate(new Timestamp(System.currentTimeMillis()));
            	//默认角色的状态
            	role.setRoleStatus(1);
            	roleBeanMapper.addRoleInfo(role);
				RoleModuleRelationBean recordBean = new RoleModuleRelationBean();
				recordBean.setRoleId(role.getRoleId());
				for (Long m : moduleIds) {
					recordBean.setModuleId(m);
					roleModuleRelationBeanMapper.insert(recordBean);
					}
				isSuccess = true;
            }
		}
		return isSuccess;
	}
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#checkRoleName(com.linyang.energy.model.RoleBean, int)
	 */
	@Override
	public boolean checkRoleName(String roleName, int operType) {
		boolean isSuccess = false;
		if(roleName!= null){
			if( roleBeanMapper.checkRoleName(roleName,operType) > 0){
				isSuccess = false;
			}else{
				isSuccess = true;
			}
			
		}
		return isSuccess;
	}
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#deleteRole(com.linyang.energy.model.RoleBean)
	 */
	@Override
	public boolean deleteRole(long roleId) {
		boolean isSuccess = false;
		if(CommonMethod.isNotEmpty(roleId)){
			RoleModuleRelationBean recordBean = new RoleModuleRelationBean();
			recordBean.setRoleId(roleId);
			roleModuleRelationBeanMapper.deleteByPrimaryKey(recordBean);
			roleBeanMapper.deleteRole(roleId);
			isSuccess = true;
		}
		return isSuccess;
	}
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#updateRoleInfo(com.linyang.energy.model.RoleBean)
	 */
	@Override
	public boolean updateRoleInfo(RoleBean role,List<Long> moduleIds) {
		boolean isSuccess = false;
		if(role != null&&role.getRoleId() != null){
			//默认时间
			role.setModifyDate(new Timestamp(System.currentTimeMillis()));
			roleBeanMapper.updateRoleInfo(role);
			RoleModuleRelationBean recordBean = new RoleModuleRelationBean();
			recordBean.setRoleId(role.getRoleId());
			roleModuleRelationBeanMapper.deleteByPrimaryKey(recordBean);
			for (Long m : moduleIds) {
				recordBean.setModuleId(m);
				roleModuleRelationBeanMapper.insert(recordBean);
			}
			isSuccess = true;
		}
		return isSuccess;
	}
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#getModule(long)
	 */
	@Override
	public List<ModuleBean> getModule(long accountId) {
		return moduleBeanMapper.getModule(accountId);
	}
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#isUseRole(long)
	 */
	@Override
	public boolean isUseRole(Long roleId) {
		if(CommonMethod.isNotEmpty(roleId)){
			if(roleBeanMapper.isUseRole(roleId)==0){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#getModuleByRoleId(long)
	 */
	@Override
	public List<ModuleBean> getModuleByRoleId(long roleId) {
		return moduleBeanMapper.getModuleByRoleId(roleId);
	}
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.RoleService#getRoleByRoleId()
	 */
	@Override
	public RoleBean getRoleByRoleId(long roleId) {
		if(CommonMethod.isNotEmpty(roleId)){
			return roleBeanMapper.getRoleByRoleId(roleId);
		}
		return null;
	}
	

}
