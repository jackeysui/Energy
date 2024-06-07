package com.linyang.energy.mapping.authmanager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.GroupBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import java.util.Map;

/** 

* @ClassName: GroupBeanMapper 

* @Description: 群组

* @author Yaojiawei 

* @date 2014-8-12 上午10:29:53 

* 
 

*/ 

public interface GroupBeanMapper {
	
	/**
	 * 
	
	* @Title: getGroupById 
	
	* @Description: 根据ID获取群组
	
	* @param @param groupId
	* @param @return    设定文件 
	
	* @return GroupBean    返回类型 
	
	* @throws
	 */
	public GroupBean getGroupById(@Param("groupId")Long groupId);
    
    public List<GroupBean> getGroupsByParam(Map<String,Object> Param);
	
	/**
	 * 
	
	* @Title: getGroupByPublicType 
	
	* @Description: 根据私有性公开性获取群组
	
	* @param @param publicType
	* @param @return    设定文件 
	
	* @return List<GroupBean>    返回类型 
	
	* @throws
	 */
	public List<GroupBean> getGroupByPublicType(@Param("publicType")Integer publicType);
	
	/**
	 * 
	
	* @Title: getGroupByGroupType 
	
	* @Description: 根据群组类型（测量点组， 客户组）获取群组
	
	* @param @param groupType
	* @param @return    设定文件 
	
	* @return List<GroupBean>    返回类型 
	
	* @throws
	 */
	public List<GroupBean> getGroupByGroupType(@Param("groupType")Integer groupType);
	
	/**
	 * 
	
	* @Title: addGroup 
	
	* @Description: 新加一个群组
	
	* @param @param groupBean
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public int addGroup(GroupBean groupBean);
	
	/**
	 * 
	
	* @Title: updateGroup 
	
	* @Description: 更新一个群组
	
	* @param @param groupBean
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public int updateGroup(GroupBean groupBean);
	
	/**
	 * 
	
	* @Title: deleteGroup 
	
	* @Description:  删除群组
	
	* @param @param groupId
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public int deleteGroup(@Param("groupId")Long groupId);
	
	/**
	 * 
	
	* @Title: getUserGroupByPublicType 
	
	* @Description: 获取用户某个类型的群组
	
	* @param @param publicType
	* @param @return    设定文件 
	
	* @return List<GroupBean>    返回类型 
	
	* @throws
	 */
	public List<GroupBean> getUserGroupByPublicType(@Param("accountId")Long accountId, @Param("publicType")Integer publicType);
	/**
	 * 
	
	* @Title: addAccountGroupRelation 
	
	* @Description: 添加用户和群组之间的联系
	
	* @param @param accountId
	* @param @param groupId
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public int addAccountGroupRelation(@Param("accountId")Long accountId, @Param("groupId")Long groupId);

   /**
	* @Description: 根据组类型\公开性获取该用户创建的群组
	* @param  groupType 组类型
	* @param  publicType 公开类型
	* @param     设定文件 
	* @user chengq 
	* @date 2014-8-14
	* @return List<GroupBean>    返回类型 
	*/
	public List<GroupBean> getUserGroupByType(@Param("accountId")Long accountId, @Param("groupType")Integer groupType,@Param("publicType")Integer publicType);
	
	
	public List<GroupBean> getGroupPageList(@Param("page")Page page, @Param("accountId")Long accountId, @Param("groupType")Integer groupType, @Param("groupName")String groupName);

   /**
	* @Description: 根据组类型\公开性获取该用户关联的群组
	* @param  groupType 组类型
	* @param  publicType 公开类型
	* @param     设定文件 
	* @user chengq 
	* @date 2014-8-14
	* @return List<GroupBean>    返回类型 
	*/
	public List<GroupBean> getUserGroupRealtionByType(@Param("accountId")Long accountId, @Param("groupType")Integer groupType,@Param("publicType")Integer publicType);
	
	/**
	 * 
	
	* @Title: getGroupLedger 
	
	* @Description: 根据群组ID，获取该群组下所有的分户
	
	* @param @param groupId
	* @param @return    设定文件 
	
	* @return List<LedgerBean>    返回类型 
	
	* @throws
	 */
	public List<LedgerBean> getGroupLedger(@Param("groupId")Long groupId, @Param("accountId")Long accountId);
	
	/***
	 * 
	
	* @Title: deleteGroupLedgerRelation 
	
	* @Description: 删除群组和分户的关系
	
	* @param @param groupId
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public int deleteGroupLedgerRelation(@Param("groupId")Long groupId);

	/***
	 * 
	
	* @Title: deleteAccountGroupRelation 
	
	* @Description: 删除群组和账户的关系
	
	* @param @param groupId
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public int deleteAccountGroupRelation(@Param("groupId")Long groupId);	

   /**
	* @Description:  根据id删除用户与群组关系
	* @param  accountId  
	* @param  accountId  
	* @user chengq 
	* @date 2014-8-14
	* @return  
	*/
	public int deleteAccountGroupRelationById(@Param("accountId")Long accountId,@Param("groupId")Long groupId);
	
	/**
	 * 
	
	* @Title: getGroupLedger2 
	
	* @Description: 如果当前账户是分户账户，就要根据分户左右深度删选分户
	
	* @param @param groupId
	* @param @param ledgerId
	* @param @return    设定文件 
	
	* @return List<LedgerBean>    返回类型 
	
	* @throws
	 */
	public List<LedgerBean> getGroupLedger2(@Param("groupId")Long groupId, @Param("lft")Integer lft, @Param("rgt")Integer rgt);
	/***
	 * @Title: getSuperGroupLedger
	 * @Description: 如果当前账户是超级管理员账户，则直接获取该群组所有的分户
	 * @param groupId
	 * @return
	 * @author Yaojiawei
	 * @date 2014-09-02
	 */
	public List<LedgerBean> getSuperGroupLedger(@Param("groupId")Long groupId);
	
	/***
	 * 
	
	* @Title: deleteLedgerFromGroup 
	
	* @Description: 从群组下删除一个分户
	
	* @param @param groupId
	* @param @param ledgerId
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public Integer deleteLedgerFromGroup(@Param("groupId")Long groupId, @Param("ledgerId")Long ledgerId);
	
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
	public int addGroupLedgerRelation(@Param("groupId")Long groupId, @Param("ledgerId")Long ledgerId);
	
	/**
	 * 
	
	* @Title: addGroupLedgerRelation 
	
	* @Description: 向群组下添加计量点
	
	* @param @param groupId
	* @param @param meterId
	* @param @return    设定文件 
	
	* @return int    返回类型 
	
	* @throws
	 */
	public int addGroupMpedRelation(@Param("groupId")Long groupId, @Param("meterId")Long meterId);
	
	/***
	 * @Title: getGroupMeter 
	 * @Description: 根据群组和当前用户获取用户应该看到的计量点
	 * @param groupId
	 * @param accountId
	 * @return
	 */
	public List<MeterBean> getGroupMeter(@Param("groupId")Long groupId, @Param("accountId")Long accountId);
	
	/***
	 * @Title: deleteMeterFromGroup 
	 * @Description: 从群组下删除一个计量点
	 * @param groupId
	 * @param meterId
	 * @return
	 */
	public Integer deleteMeterFromGroup(@Param("groupId")Long groupId, @Param("meterId")Long meterId);
	
	/***
	 * @Title:deleteMeterGroupRelation
	 * @Description: 清空群组下所有计量点
	 * @param groupId
	 * @return
	 */
	public Integer deleteMeterGroupRelation(@Param("groupId")Long groupId);
	
	/***
	 * @Title: getGroupMeter2 
	 * @Description: 根据群组和当前用户获取用户应该看到的计量点,该群组只有分户权限，而没有群组权限，因此需要从分户获取所有子分户，然后查到所有计量点，做个比对
	 * @param groupId
	 * @param accountId
	 * @return
	 */
	public List<MeterBean> getGroupMeter2(@Param("groupId")Long groupId, @Param("accountId")Long accountId);
	
	/**
	 * @Title: getSuperGroupMeter 
	 * @Description:如果当前账户是超级管理员账户，则直接获取该群组所有的计量点
	 * @param groupId
	 * @return
	 * @author Yaojiawei
	 * @date 2014-09-02
	 */
	public List<MeterBean> getSuperGroupMeter(@Param("groupId")Long groupId);
	
	/**
	 * @Title: checkGroupName 
	 * @Description:检查当前群组名字是否可用
	 * @param groupId
	 * @return
	 * @author Yaojiawei
	 * @date 2014-09-03
	 */
	public String checkGroupName(@Param("groupName")String groupName);
	
	/**
	 *  @description 获取所有除自己的群组之外的所有群组，按所有者名字排序
	 * @author yaojiawei
	 * @date 2014-09-29
	 * */
	public List<GroupBean> getAllOtherTeam(@Param("accountId")Long accountId);

    public List<LedgerTreeBean> getGroupTree(@Param("accountId")Long accountId, @Param("groupType")Integer groupType);

    public List<LedgerTreeBean> getCompanyTree(@Param("ledgerId")Long ledgerId,@Param("ledgerName")String ledgerName);

    public List<LedgerTreeBean> getDcpTree(@Param("meterId")Long meterId);
}
