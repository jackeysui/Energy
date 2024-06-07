package com.linyang.energy.mapping.authmanager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.dto.CatergoryTreeBean;
import com.linyang.energy.model.DictTypeDetailBean;

public interface DictTypeDetailBeanMapper {
    int deleteByPrimaryKey(Long detailId);

    int insert(DictTypeDetailBean record);

    int insertSelective(DictTypeDetailBean record);

    List<DictTypeDetailBean> selectByPrimaryKey(Long cateId);

    int updateByPrimaryKeySelective(DictTypeDetailBean record);

    int updateByPrimaryKey(DictTypeDetailBean record);
    /**
     * 函数功能说明  :获取数据字典父类 
     * @return  List<CatergoryTreeBean>
     */
    List<CatergoryTreeBean> getParentTree();
    /**
     * 函数功能说明  :获取数据字典子类 
     * @return  List<CatergoryTreeBean>
     */
    List<CatergoryTreeBean> getChildTree(long parentId);
    /**
     * 函数功能说明  :获取大类下面的子大类数量
     * @param parentId
     * @return  int
     */
    int getCountParentTreeById(long parentId);
    /**
     * 函数功能说明  :获取大类下面的子类数量
     * @param parentId
     * @return  int
     */
    int getCountChildTreeById(long parentId);
    /***
     * 函数功能说明  :校验名称
     * @param name
     * @return  int
     */
    int checkName(@Param("name")String name,@Param("operType")int operType);
}