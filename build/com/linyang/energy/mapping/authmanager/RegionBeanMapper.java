package com.linyang.energy.mapping.authmanager;

import java.util.List;

import com.linyang.energy.model.RegionBean;

/**
 *@Description  区域操作Mapper
 *
 *@author chengq
 *@date 2014-8-14
 *@version
 */
public interface RegionBeanMapper {
    /**
     * @Description  :查询区域
     * @return  List<RegionBean>
     */
    public List<RegionBean> queryRegionInfo(RegionBean regionBean);

    public List<RegionBean> queryOnePointRegion(String regionId);

}