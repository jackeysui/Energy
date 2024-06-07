/**
 * 
 */
package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.dto.CatergoryTreeBean;
import com.linyang.energy.mapping.authmanager.DictTypeDetailBeanMapper;
import com.linyang.energy.model.DictTypeDetailBean;
import com.linyang.energy.service.DictTypeDetailService;
import com.linyang.util.CommonMethod;

/** 
 * @类功能说明： 数据字典接口的实现类
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-11 上午11:21:44  
 * @版本：V1.0  */
@Service
public class DictTypeDetailServiceImpl implements DictTypeDetailService{

	@Autowired
	private DictTypeDetailBeanMapper dictTypeDetailBeanMapper;

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.DictTypeDetailService#getParentTree()
	 */
	@Override
	public List<CatergoryTreeBean> getParentTree() {
		List<CatergoryTreeBean> list = dictTypeDetailBeanMapper.getParentTree();
 		if(CommonMethod.isCollectionNotEmpty(list)){
			for (CatergoryTreeBean catergoryTreeBean : list) {
				long parentId = Long.parseLong(catergoryTreeBean.getId());
				//查询大类下面是否有子大类
				int count = dictTypeDetailBeanMapper.getCountParentTreeById(parentId);
				if(count <= 0)
					//没有子大类 查询下面是否有小类
					count = dictTypeDetailBeanMapper.getCountChildTreeById(parentId);
				if(count >0)
					catergoryTreeBean.setIsParent(true);
			}
			return list;
		}
		return new ArrayList<CatergoryTreeBean>();
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.DictTypeDetailService#getChildTree(long)
	 */
	@Override
	public List<CatergoryTreeBean> getChildTree(long parentId) {
		List<CatergoryTreeBean> list = dictTypeDetailBeanMapper.getChildTree(parentId);
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (CatergoryTreeBean catergoryTreeBean : list) {
				int treeNodeType = catergoryTreeBean.getTreeNodeType();
				long parentId1 = Long.parseLong(catergoryTreeBean.getId());
				if(treeNodeType == 1){
				    //查询大类下面是否有子大类
				int count = dictTypeDetailBeanMapper.getCountParentTreeById(parentId1);
				if(count <= 0)
					//没有子大类 查询下面是否有小类
					count = dictTypeDetailBeanMapper.getCountChildTreeById(parentId1);
				if(count >0)
					catergoryTreeBean.setIsParent(true);
				}
			}
			return list;
		}
		return new ArrayList<CatergoryTreeBean>();
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.DictTypeDetailService#getDirectChildTree(long)
	 */
	@Override
	public List<DictTypeDetailBean> getDirectChildTree(long cateId) {
		if(CommonMethod.isNotEmpty(cateId)){
			return dictTypeDetailBeanMapper.selectByPrimaryKey(cateId);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.DictTypeDetailService#updateDetail(com.linyang.energy.model.DictTypeDetailBean)
	 */
	@Override
	public boolean updateDetail(DictTypeDetailBean dictTypeDetailBean) {
		boolean isSuccess = false;
		if(dictTypeDetailBean != null){
			dictTypeDetailBean.setStatus((short)1);
			dictTypeDetailBeanMapper.updateByPrimaryKeySelective(dictTypeDetailBean);
			isSuccess = true;
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.DictTypeDetailService#addDetail(com.linyang.energy.model.DictTypeDetailBean)
	 */
	@Override
	public boolean addDetail(DictTypeDetailBean dictTypeDetailBean) {
		boolean isSuccess = false;
		if(dictTypeDetailBean != null){
			dictTypeDetailBean.setStatus((short)1);
			dictTypeDetailBeanMapper.insertSelective(dictTypeDetailBean);
			isSuccess = true;
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.DictTypeDetailService#checkName(java.lang.String)
	 */
	@Override
	public boolean checkName(String name,int operType) {
		boolean isSuccess = false;
		if(name!= null){
			if( dictTypeDetailBeanMapper.checkName(name,operType) > 0){
				isSuccess = false;
			}else{
				isSuccess = true;
			}
			
		}
		return isSuccess;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.DictTypeDetailService#deleteDetail(long)
	 */
	@Override
	public boolean deleteDetail(long detailId) {
		boolean isSuccess = false;
		if(CommonMethod.isNotEmpty(detailId)){
			dictTypeDetailBeanMapper.deleteByPrimaryKey(detailId);
			isSuccess = true;
		}
		return isSuccess;
	}
	

}
