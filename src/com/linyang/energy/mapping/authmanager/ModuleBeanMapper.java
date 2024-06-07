package com.linyang.energy.mapping.authmanager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.ModuleBean;

public interface ModuleBeanMapper {
    int deleteByPrimaryKey(Long moduleId);

    int insert(ModuleBean record);

    int insertSelective(ModuleBean record);

    ModuleBean selectByPrimaryKey(Long moduleId);

    int updateByPrimaryKeySelective(ModuleBean record);

    int updateByPrimaryKey(ModuleBean record);
    /**
     * 得到一个用户的模块信息
     * @param userId
     * @return
     */
    public List<ModuleBean> getUserModules(@Param("accountId")long userId);
    /**
     * 根据父类id得到子类模块信息
     * @param userId
     *  @param parentModuleId
     * @return
     */
    public List<ModuleBean> getTabModules(@Param("accountId")long userId,@Param("parentModuleId")long parentModuleId);
    
    /**
	 * 函数功能说明  :获取用户的菜单
	 * @param accountId 用户的id
	 * @return  List<ModuleBean>
	 */
	List<ModuleBean> getModule(@Param("accountId")long userId);
	/**
	 * 函数功能说明  :根据角色Id获取角色菜单
	 * @param roleId
	 * @return  List<ModuleBean>
	 */
	List<ModuleBean> getModuleByRoleId(@Param("roleId")long roleId);
}
