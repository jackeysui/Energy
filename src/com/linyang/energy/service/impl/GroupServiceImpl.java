package com.linyang.energy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esotericsoftware.minlog.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.mapping.authmanager.GroupBeanMapper;
import com.linyang.energy.model.GroupBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.GroupService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** 

* @ClassName: GroupServiceImpl 

* @Description: Group操作执行类

* @author Yaojiawei 

* @date 2014-8-12 上午10:28:00 

* 
 

*/ 
@Service
public class GroupServiceImpl implements GroupService{
	
	@Autowired
	private GroupBeanMapper groupBeanMapper;
	
	@Override
	public List<GroupBean> getGroupByPublicType(Integer publicType) {
		return groupBeanMapper.getGroupByPublicType(publicType);
	}

	@Override
	public List<GroupBean> getUserGroupByPublicType(Long accountId,
			Integer publicType) {
		return groupBeanMapper.getUserGroupByPublicType(accountId, publicType);
	}
	
	@Override
	public List<GroupBean> getUserGroupByType(Long accountId,
			Integer groupType,Integer publicType) {
		return groupBeanMapper.getUserGroupByType(accountId, groupType, publicType);
	}
	
	@Override
	public List<GroupBean> getUserGroupRealtionByType(Long accountId,
			Integer groupType,Integer publicType) {
		return groupBeanMapper.getUserGroupRealtionByType(accountId, groupType, publicType);
	}

	@Override
	public boolean addGroupInfo(GroupBean groupBean) {
		boolean isSuccess = false;
		if(groupBean!=null)
		{
			Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("groupName", groupBean.getGroupName());
			
			List<GroupBean> groupList = groupBeanMapper.getGroupsByParam(queryMap);
			if(groupList.size()>0)
			{
				isSuccess = false;
			}
			else
			{
				groupBeanMapper.addGroup(groupBean);
			//根据张工所述，创建用户后，群组数据权限不应该同时被创建，因此去掉
//			groupBeanMapper.addAccountGroupRelation(groupBean.getAccountId(), groupBean.getGroupId());
				isSuccess = true;
			}
		}
		
		return isSuccess;
	}

	@Override
	public List<LedgerBean> getGroupLedger(Long groupId, Long accountId) {	
		return groupBeanMapper.getGroupLedger(groupId, accountId);
	}

	@Override
	public GroupBean getGroupById(Long groupId) {
		return groupBeanMapper.getGroupById(groupId);
	}

	@Override
	public boolean deleteGroup(Long groupId) {
		boolean isSuccess = false;
		if(groupId!=null)
		{
            groupBeanMapper.deleteAccountGroupRelation(groupId);
			groupBeanMapper.deleteGroup(groupId);
			isSuccess = true;
		}
		
		return isSuccess;
	}

	@Override
	public List<LedgerBean> getGroupLedger2(Long groupId, Integer lft, Integer rgt) {
		
		return groupBeanMapper.getGroupLedger2(groupId, lft, rgt);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteLedgerFromGroup(Long groupId, Long ledgerId) {
		
		boolean isSuccess = false;
		if(groupId!=null)
		{
			groupBeanMapper.deleteLedgerFromGroup(groupId, ledgerId);
			isSuccess = true;
		}
		
		return isSuccess;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteGroupLedgerRelation(Long groupId) {
		boolean isSuccess = false;
		if(groupId!=null)
		{
		try{	groupBeanMapper.deleteGroupLedgerRelation(groupId);
			isSuccess = true;}catch(Exception ec){Log.error(this.getClass()+" deleteGroupLedgerRelation fail!");}
		}
		
		return isSuccess;
	}

	@Override
	public boolean addGroupLedgerRelation(String[] ledgerMap, Long groupId) {
		boolean isSuccess = false;
		if(groupId!=null)
		{
			GroupBean group = groupBeanMapper.getGroupById(groupId);
			if(group.getGroupType()==1)
			{
				for (String str : ledgerMap) {
					Long ledgerId = Long.parseLong(str);
					groupBeanMapper.addGroupLedgerRelation(groupId, ledgerId);
				}
			}
			isSuccess = true;
		}
		return isSuccess;
	}

	@Override
	public boolean addGroupMpedRelation(String[] meterMap, Long groupId) {
		boolean isSuccess = false;
		if(groupId!=null)
		{
			GroupBean group = groupBeanMapper.getGroupById(groupId);
			if(group.getGroupType()==2)
			{
				for (String str : meterMap) {
					Long meterId = Long.parseLong(str);
					groupBeanMapper.addGroupMpedRelation(groupId, meterId);
				}
			}
			isSuccess = true;
		}
		return isSuccess;
	}

	@Override
	public List<MeterBean> getGroupMeter(Long groupId, Long accountId) {
		
		return groupBeanMapper.getGroupMeter(groupId, accountId);
	}

	@Override
	public boolean deleteMeterFromGroup(Long groupId, Long meterId) {
		boolean isSuccess = false;
		if((groupId!=null)&&(meterId!=null))
		{
			groupBeanMapper.deleteMeterFromGroup(groupId, meterId);
			isSuccess = true;
		}		
		return isSuccess;
	}

	@Override
	public boolean deleteMeterGroupRelation(Long groupId) {
		boolean isSuccess = false;
		if(groupId!=null)
		{
			try{groupBeanMapper.deleteMeterGroupRelation(groupId);
			isSuccess = true;}catch(Exception ec){Log.error(this.getClass()+" deleteMeterGroupRelation fail!");}
		}
		return isSuccess;
	}

	@Override
	public List<MeterBean> getGroupMeter2(Long groupId, Long accountId) {
		return groupBeanMapper.getGroupMeter2(groupId, accountId);
	}

	@Override
	public List<LedgerBean> getSuperGroupLedger(Long groupId) {
		return groupBeanMapper.getSuperGroupLedger(groupId);
	}

	@Override
	public List<MeterBean> getSuperGroupMeter(Long groupId) {
		return groupBeanMapper.getSuperGroupMeter(groupId);
	}

	@Override
	public boolean checkGroupName(String groupName) {
		boolean isSuccess = true;
		String myName =  groupBeanMapper.checkGroupName(groupName);
		if(myName!=null&&myName.length()>0)
			isSuccess = false;
	
		return isSuccess;
	}
    
    @Override
	public boolean checkNewGroupName(String groupName,Long groupId) {
		boolean isSuccess = true;
        if(groupId == null || groupId == 0){
            String myName =  groupBeanMapper.checkGroupName(groupName);
            if(myName!=null&&myName.length()>0)
                isSuccess = false;
        }else{
            Map param = new HashMap();
            param.put("groupName", groupName);
            List<GroupBean> groups = groupBeanMapper.getGroupsByParam(param);
            for (Iterator<GroupBean> it = groups.iterator(); it.hasNext();) {
                GroupBean groupBean = it.next();
                if(groupBean.getGroupId().compareTo(groupId) == 0){
                    it.remove();
                }
            }
            if(groups.size() >= 1){
                isSuccess = false;
            }
        }
	
		return isSuccess;
	}

	@Override
	public boolean updateGroupInfo(GroupBean groupBean) {
		boolean isSuccess = false;
		if(groupBean!=null)
		{
			groupBeanMapper.updateGroup(groupBean);
			isSuccess = true;
		}
		
		return isSuccess;
	}

	@Override
	public List<GroupBean> getAllOtherTeam(Long accountId) {
		
		return groupBeanMapper.getAllOtherTeam(accountId);
	}

	@Override
	public List<GroupBean> getGroupPageList(Page page, Long accountId, Integer groupType,
			String groupName) {
		return this.groupBeanMapper.getGroupPageList(page, accountId, groupType, groupName);
	}

    @Override
    public List<LedgerTreeBean> getCompanyTree(long ledgerId,String ledgerName) {
        return groupBeanMapper.getCompanyTree(ledgerId,ledgerName);
    }

    @Override
    public List<LedgerTreeBean> getDcpTree(long meterId) {
        return groupBeanMapper.getDcpTree(meterId);
    }

    @Override
    public List<LedgerTreeBean> getGroupTree(Long accountId, int nodeType) {
        if(nodeType == 1){
            return groupBeanMapper.getGroupTree(accountId,1);
        }
        return groupBeanMapper.getGroupTree(accountId,2);
    }

    @Override
    public boolean updateGroupLedgerRelation(String[] ledgerMap, Long groupId) {
        boolean isSuccess = false;
		if(groupId!=null)
		{
			GroupBean group = groupBeanMapper.getGroupById(groupId);
			if(group.getGroupType()==1){
                groupBeanMapper.deleteGroupLedgerRelation(groupId);
                if(ledgerMap != null){
                    for (String str : ledgerMap) {
                        Long ledgerId = Long.parseLong(str);
                        groupBeanMapper.addGroupLedgerRelation(groupId, ledgerId);
                    }
                }
			}else if(group.getGroupType()==2){
                groupBeanMapper.deleteMeterGroupRelation(groupId);
                if(ledgerMap != null){
                    for (String str : ledgerMap) {
                        Long meterId = Long.parseLong(str);
                        groupBeanMapper.addGroupMpedRelation(groupId, meterId);
                    }
                }
			}
			isSuccess = true;
		}
		return isSuccess;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean truncAllLedger(Long groupId) {
		boolean isSuccess = false;
		if(groupId!=null)
		{
			try
			{	
				groupBeanMapper.deleteGroupLedgerRelation(groupId);
				groupBeanMapper.deleteMeterGroupRelation(groupId);
				isSuccess = true;
			}
			catch (Exception ec)
			{
				Log.error(this.getClass()+" truncAllLedger fail!");
			}
		}

		return isSuccess;
	}
    
    
}
