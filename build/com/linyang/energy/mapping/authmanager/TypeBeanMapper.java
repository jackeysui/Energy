package com.linyang.energy.mapping.authmanager;

import com.linyang.energy.model.TypeBean;

import java.util.List;

/**
 *@Description  设备类型操作Mapper
 *
 *@author dingy
 *@date 2019-01-22
 *@version
 */
public interface TypeBeanMapper {
    /**
     * @Description  :查询设备类型
     * @return  List<TypeBean>
     */
    public List<TypeBean> queryTypeInfo();
}