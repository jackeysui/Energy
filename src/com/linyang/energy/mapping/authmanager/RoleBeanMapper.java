package com.linyang.energy.mapping.authmanager;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.RoleBean;

public interface RoleBeanMapper {
    int deleteByPrimaryKey(Long roleId);

    int insert(RoleBean record);

    int insertSelective(RoleBean record);

    RoleBean selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(RoleBean record);

    int updateByPrimaryKey(RoleBean record);

	/**  
	 * 函数功能说明  :查询角色列表
	 * @param page
	 * @param queryCondition
	 * @return      
	 * @return  List<Map<String,Object>> 
	 */
    List<Map<String,Object>>getRolePageData(Map<String, Object> queryCondition);

	/**  
	 * 函数功能说明  :新增角色
	 * @param role      
	 * @return  void     
	 */
	int addRoleInfo(RoleBean role);

	/**  
	 * 函数功能说明  :检查角色名是否重复
	 * @param roleName
	 * @param operType      
	 * @return  void     
	 */
	int checkRoleName(@Param("roleName")String roleName, @Param("operType")int operType);

	/**  
	 * 函数功能说明  :删除角色
	 * @param role      
	 * @return  void     
	 */
	int deleteRole(@Param("roleId")long roleId);

	/**  
	 * 函数功能说明  :更新角色
	 * @param role      
	 * @return  void     
	 */
	int updateRoleInfo(RoleBean role);
	/**
	 * 函数功能说明  :检查角色是否被用户使用
	 * @param roleId
	 * @return  int
	 */
	Long isUseRole(@Param("roleId")Long roleId);
	/**
	 * 函数功能说明  :根据角色id获取角色
	 * @param roleId
	 * @return  RoleBean
	 */
	RoleBean getRoleByRoleId(@Param("roleId")long roleId);
	/**
	 * 根据查询条件，查询是否有重复提交的rolebean
	 * */
	List<RoleBean> getFilterdRole(Map<String, Object> queryMap);
}