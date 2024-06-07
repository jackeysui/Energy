package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.ModuleBean;
import com.linyang.energy.model.RoleBean;

/**
 *角色管理业务逻辑层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 4, 2013
 */
public interface RoleService {

	/**
	 * 分页查询用户信息列表，支持查询
	 * @param page 分页对象
	 * @param queryCondition 查询条件
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getRolePageList(Page page,Map<String,Object> queryCondition);
	/**
	 * 函数功能说明  :新增角色
	 * @param role  角色参数集合
	 * @return  boolean
	 */
	public boolean addRoleInfo(RoleBean role,List<Long> moduleIds);
	/**
	 * 函数功能说明  :更新角色信息
	 * @param role  角色参数集合
	 * @return  boolean
	 */
	public boolean updateRoleInfo(RoleBean role,List<Long> moduleIds);
	/**
	 * 函数功能说明  :删除角色
	 * @param role  角色参数集合
	 * @return  boolean
	 */
	public boolean deleteRole(long roleId);
	/**
	 * 函数功能说明  :检查角色名称是否重复
	 * @param role  角色参数集合
	 * @param operType  操作类型 1：表示新增  2：表示编辑
	 * @return  boolean
	 */
	public boolean checkRoleName(String roleName,int operType);
	/**
	 * 函数功能说明  :获取用户的菜单
	 * @param userId 用户的id
	 * @return  List<ModuleBean>
	 */
	public List<ModuleBean> getModule(long userId);
	/**
	 * 函数功能说明  :根据角色id获取角色菜单
	 * @param roleId
	 * @return  List<ModuleBean>
	 */
	public List<ModuleBean> getModuleByRoleId(long roleId);
	/**
	 * 函数功能说明  :检查角色是否被用户使用
	 * @param roleId
	 * @return  boolean
	 */
	public boolean isUseRole(Long roleId);
	/**
	 * 函数功能说明  :根据角色id获取角色信息
	 * @return  RoleBean
	 */
	public RoleBean getRoleByRoleId(long roleId);
}
