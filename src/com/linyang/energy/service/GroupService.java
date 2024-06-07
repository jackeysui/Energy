package com.linyang.energy.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.GroupBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;

/** 

* @ClassName: GroupService 

* @Description: Group操作逻辑层接口

* @author Yaojiawei 

* @date 2014-8-12 上午10:27:38 

* 
 

*/ 
public interface GroupService {
	public List<GroupBean> getGroupByPublicType(Integer publicType);
	public List<GroupBean> getUserGroupByPublicType(Long accountId, Integer publicType);
	public boolean addGroupInfo(GroupBean groupBean);
	public List<LedgerBean> getGroupLedger (Long groupId, Long accountId);
	public GroupBean getGroupById(Long groupId);
	public boolean deleteGroup(Long groupId);
	/**
	 *@函数功能说明 : 根据组类型\公开性获取该用户的所有群组
	 *
	 *@author chengq
	 *@date 2014-8-14
	 *@return
	 */
	public List<GroupBean> getUserGroupByType(Long accountId, Integer groupType, Integer publicType);
	public List<LedgerBean> getGroupLedger2(Long groupId, Integer lft, Integer rgt);
	public boolean deleteLedgerFromGroup(Long groupId, Long ledgerId);
	public boolean deleteGroupLedgerRelation(Long groupId);

	public List<GroupBean> getGroupPageList(Page page, Long accountId, Integer groupType,String groupName);
	/**
	 *@函数功能说明 : 根据组类型\公开性获取该用户关联的群组
	 *
	 *@author chengq
	 *@date 2014-8-14
	 *@return
	 */
	public List<GroupBean> getUserGroupRealtionByType(Long accountId, Integer groupType, Integer publicType);
	
	/**
	 * 
	
	* @Title: addGroupLedgerRelation 
	
	* @Description: 向群组下添加分户
	
	* @param @param groupId
	* @param @param ledgerId
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public boolean addGroupLedgerRelation(String[] ledgerMap, Long groupId);
	/***
	 * 
	
	* @Title: addGroupMpedRelation 
	
	* @Description: 向群组下添加计量点
	
	* @param @param meterMap
	* @param @param groupId
	* @param @return    设定文件 
	
	* @return boolean    返回类型 
	
	* @throws
	 */
	public boolean addGroupMpedRelation(String[] meterMap, Long groupId);
	
	/***
	 * @Title: getGroupMeter 
	 * @Description: 根据群组和当前用户获取用户应该看到的计量点
	 * @param groupId
	 * @param accountId
	 * @return
	 */
	public List<MeterBean> getGroupMeter(Long groupId, Long accountId);
	
	/***
	 * @Title:deleteMeterFromGroup
	 * @Description: 从群组下删除一个计量点
	 * @param groupId
	 * @param meterId
	 * @return
	 */
	public boolean deleteMeterFromGroup(Long groupId, Long meterId);
	
	/***
	 * @Title:deleteMeterGroupRelation
	 * @Description: 清空群组下所有计量点
	 * @param groupId
	 * @return
	 */
	public boolean deleteMeterGroupRelation(Long groupId);
	
	/***
	 * @Title: getGroupMeter2 
	 * @Description: 根据群组和当前用户获取用户应该看到的计量点,该群组只有分户权限，而没有群组权限，因此需要从分户获取所有子分户，然后查到所有计量点，做个比对
	 * @param groupId
	 * @param accountId
	 * @return
	 */
	public List<MeterBean> getGroupMeter2(Long groupId, Long accountId);
	
	/***
	 * @Title: getSuperGroupLedger
	 * @Description: 如果当前账户是超级管理员账户，则直接获取该群组所有的分户
	 * @param groupId
	 * @return
	 */
	public List<LedgerBean> getSuperGroupLedger(Long groupId);
	
	/**
	 * @Title: getSuperGroupMeter 
	 * @Description:如果当前账户是超级管理员账户，则直接获取该群组所有的计量点
	 * @param groupId
	 * @return
	 */
	public List<MeterBean> getSuperGroupMeter(Long groupId);
	
	/***
	 * @Title: getSuperGroupMeter 
	 * @Description:检查当前群组名字是否可用
	 * @param groupName
	 * @return
	 * @date 2014-09-03
	 */
	public boolean checkGroupName(String groupName);
	
	/***
	 * @Title: updateGroupInfo 
	 * @Description:更新当前群组信息
	 * @param group
	 * @return
	 * @date 2014-09-03
	 */
	public boolean updateGroupInfo(GroupBean groupBean);
	
	/**
	 *  @description 获取所有除自己的群组之外的所有群组，按所有者名字排序
	 * @author yaojiawei
	 * @date 2014-09-29
	 * */
	public List<GroupBean> getAllOtherTeam(Long accountId);
    
	/***
	 * 检查当前群组名字是否可用
	 * @param groupName
	 * @return
	 */
    public boolean checkNewGroupName(String groupName,Long groupId);

    public List<LedgerTreeBean> getCompanyTree(long ledgerId,String ledgerName);

    public List<LedgerTreeBean> getDcpTree(long meterId);

    public List<LedgerTreeBean> getGroupTree(Long accountId, int nodeType);

    public boolean updateGroupLedgerRelation(String[] ledgerMap, Long groupId);
    /**
     * @description 从群组下删除所有分户或计量点.由于原来的删除方法，需要先删除群组和分户的关系，再删除群组和用户的关系，两个不是事务，所以重新写一下
     * @author yaojiawei
	 * @date 2018-04-18
     * */
    public boolean truncAllLedger(Long groupId);
}
