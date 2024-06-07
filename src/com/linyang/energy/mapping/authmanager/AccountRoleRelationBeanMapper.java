package com.linyang.energy.mapping.authmanager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.AccountRoleRelationBean;

public interface AccountRoleRelationBeanMapper {
    int deleteByPrimaryKey(AccountRoleRelationBean key);

    int insert(AccountRoleRelationBean record);

    int insertSelective(AccountRoleRelationBean record);
    /**
     * 得到一个用户的角色信息列表
     * @param userId
     * @return
     */
    public List<AccountRoleRelationBean> getUserRoles(@Param("getUserRoles")long userId);
}