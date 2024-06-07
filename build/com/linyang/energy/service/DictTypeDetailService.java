/**
 * 
 */
package com.linyang.energy.service;

import java.util.List;

import com.linyang.energy.dto.CatergoryTreeBean;
import com.linyang.energy.model.DictTypeDetailBean;

/** 
 * @类功能说明： 数据字典接口
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-11 上午11:20:49  
 * @版本：V1.0  */
public interface DictTypeDetailService {
	/**
	 * 函数功能说明  :插入子类
	 * @param dictTypeDetailBean
	 * @return  boolean
	 */
	boolean addDetail(DictTypeDetailBean dictTypeDetailBean);
	/**
	 * 函数功能说明  :更新子类
	 * @param dictTypeDetailBean
	 * @return  boolean
	 */
	boolean updateDetail(DictTypeDetailBean dictTypeDetailBean);
	/**
	 * 函数功能说明  :删除子类
	 * @param detailId
	 * @return  boolean
	 */
	boolean deleteDetail(long detailId);
	/**
	 * 函数功能说明  :校验名称
	 * @param name operType
	 * @return  boolean
	 */
	boolean checkName(String name,int operType);
	/**
	 * 函数功能说明  :大类下面的直接子类
	 * @param cateId
	 * @return  DictTypeDetailBean
	 */
	List<DictTypeDetailBean> getDirectChildTree(long cateId);
	/**
	 * 函数功能说明  :数据字典父类节点
	 * @return  List<CatergoryTreeBean>
	 */
	List<CatergoryTreeBean> getParentTree();
	/**
	 * 函数功能说明  :数据字典子类节点
	 * @return  List<CatergoryTreeBean>
	 */
	List<CatergoryTreeBean> getChildTree(long parentId);
}
