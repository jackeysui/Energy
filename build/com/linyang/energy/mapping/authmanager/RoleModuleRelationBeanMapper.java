package com.linyang.energy.mapping.authmanager;

import com.linyang.energy.model.RoleModuleRelationBean;

public interface RoleModuleRelationBeanMapper {
    int deleteByPrimaryKey(RoleModuleRelationBean key);

    int insert(RoleModuleRelationBean record);

    int insertSelective(RoleModuleRelationBean record);
}