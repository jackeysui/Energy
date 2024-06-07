package com.linyang.energy.dto;

import com.linyang.common.web.ztree.ZtreeNode;

/**
 * @类功能说明： 数据字典树
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-11 上午10:39:11  
 * @版本：V1.0
 */
public class CatergoryTreeBean extends ZtreeNode{
	
	/**
	 * 树节点类型：1表示大类，2表示小类
	 */
	private int treeNodeType;

	public int getTreeNodeType() {
		return treeNodeType;
	}

	public void setTreeNodeType(int treeNodeType) {
		this.treeNodeType = treeNodeType;
	}


	
	
	
}
