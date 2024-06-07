package com.linyang.energy.mapping.authmanager;

import java.util.List;

import com.linyang.energy.model.IndustryBean;

/**
 *@Description  行业操作Mapper
 *
 *@author chengq
 *@date 2014-8-14
 *@version
 */
public interface IndustryBeanMapper {
    /**
     * @Description  :查询行业
     * @return  List<IndustyrBean>
     */
    public List<IndustryBean> queryIndustryInfo(IndustryBean industryBean);


    public List<IndustryBean> queryIndustryTypeInfo(IndustryBean industryBean);
}