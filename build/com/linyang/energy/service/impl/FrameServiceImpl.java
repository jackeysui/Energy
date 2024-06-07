package com.linyang.energy.service.impl;

import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.mapping.authmanager.*;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.*;
import com.linyang.energy.service.FrameService;
import com.linyang.energy.service.PhoneService;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 框架业务逻辑层接口实现类
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 4, 2013
 */
@Service
public class FrameServiceImpl implements FrameService{
	
	@Autowired
	private ModuleBeanMapper moduleBeanMapper;
	
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	
	@Autowired
	private MeterBeanMapper meterBeanMapper;
	
	@Autowired
	private RegionBeanMapper regionBeanMapper;
	
	@Autowired
	private TypeBeanMapper typeBeanMapper;

	@Autowired
	private IndustryBeanMapper industryBeanMapper;

    @Autowired
	private PhoneService phoneService;
    
    @Autowired
    private PollutMapper pollutMapper;
    
    @Autowired
    private PollutctlMapper pollutctlMapper;

    @Override
    public Map<String,Object> getUserModules(long userId) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<ModuleBean> rootList = new ArrayList<ModuleBean>();
        Map<Long, List<ModuleBean>> childrenMap = new HashMap<Long, List<ModuleBean>>();

        List<ModuleBean> userModules = moduleBeanMapper.getUserModules(userId);
        if(CommonMethod.isCollectionNotEmpty(userModules)){
            for (ModuleBean moduleBean : userModules) {
                if (moduleBean.getModuleParentId() == null ) {

                    rootList.add(moduleBean);
                } else {
                    if (childrenMap.containsKey(moduleBean.getModuleParentId())) {
                        childrenMap.get(moduleBean.getModuleParentId()).add(moduleBean);
                    } else {
                        List<ModuleBean> list = new ArrayList<ModuleBean>();
                        list.add(moduleBean);
                        childrenMap.put(moduleBean.getModuleParentId(),list);
                    }

                }
            }
        }

        map.put("rootList", rootList);
        map.put("childrenMap",childrenMap);
        map.putAll(this.phoneService.getLevelHelp(userId));
        return map;
    }

    @Override
    public Map<String,Object> getUserModules(long userId, Integer roleType) {
        Map<String,Object> map = new HashMap<String, Object>();
        List<ModuleBean> rootList = new ArrayList<ModuleBean>();
        Map<Long, List<ModuleBean>> childrenMap = new HashMap<Long, List<ModuleBean>>();
        List<String> notLoad = null;
        if(roleType != null && roleType == 1){
            notLoad = Arrays.asList(WebConstant.eleNotLoadModules);
        }
        else if(roleType != null && roleType == 2){
            notLoad = Arrays.asList(WebConstant.emoNotLoadModules);
        }

        List<ModuleBean> userModules = moduleBeanMapper.getUserModules(userId);
        if(CommonMethod.isCollectionNotEmpty(userModules)){
            for (ModuleBean moduleBean : userModules) {
                if(notLoad!=null && !notLoad.contains(moduleBean.getModuleId().toString())){ //不在不显示列表中
                    if(moduleBean.getModuleParentId() == null){ //顶层功能模块
                        rootList.add(moduleBean);
                    }
                    else if (childrenMap.containsKey(moduleBean.getModuleParentId())) {    //子模块
                        childrenMap.get(moduleBean.getModuleParentId()).add(moduleBean);
                    }
                    else {
                        List<ModuleBean> list = new ArrayList<ModuleBean>();
                        list.add(moduleBean);
                        childrenMap.put(moduleBean.getModuleParentId(),list);
                    }
                }
            }
        }

        map.put("rootList", rootList);
        map.put("childrenMap",childrenMap);
        map.putAll(this.phoneService.getLevelHelp(userId));
        return map;
    }

	@Override
	public List<LedgerTreeBean> getChildLedgerTree(long parentLedgerId) {
		List<LedgerTreeBean> list = ledgerManagerMapper.getChildLedgerTree(parentLedgerId);
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
				int treeNodeType = ledgerTreeBean.getTreeNodeType();
				if(treeNodeType == 1){//是分户的节点计算节点数
					long ledgerId = Long.parseLong(ledgerTreeBean.getId());
					int count = ledgerManagerMapper.countLedgerByLedgerId(ledgerId);
					if(count <= 0)
						count = meterBeanMapper.countMeterByLedgerId(ledgerId);
					if(count >0)
						ledgerTreeBean.setIsParent(true);
				}
			}
			return list;
		}
		return new ArrayList<LedgerTreeBean>();
	}

	@Override
	public List<LedgerTreeBean> getChildEleTree(long parentId,int objType,Integer meterType) {
		int childObjType = getChildEleNodeType(parentId,objType);
        List<LedgerTreeBean> list = ledgerManagerMapper.getChildEleTree(parentId,objType,childObjType,meterType);
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
				int treeNodeType = ledgerTreeBean.getTreeNodeType();
				long nodeId = Long.parseLong(ledgerTreeBean.getId());
				if(treeNodeType == 1){//是分户的节点计算节点数
					int subTreeNodeType = getChildEleNodeType(nodeId,treeNodeType);
					int count = ledgerManagerMapper.countSubNodeByLedgerId(nodeId,subTreeNodeType);
					if(count >0)
						ledgerTreeBean.setIsParent(true);
				}else if (treeNodeType == 2) {
					int count = meterBeanMapper.countSubMeterByMeterId(nodeId);
					if(count >0)
						ledgerTreeBean.setIsParent(true);
					
				}
			}
			return list;
		}
		return new ArrayList<LedgerTreeBean>();
	}
	
	private int getChildEleNodeType(long parentId,int objType) {
		int childObjType = 2;//测量点
		if (objType == 1) {
			int isLineloss = ledgerManagerMapper.isFirmAnalyType(parentId);
			if (isLineloss <= 0) {
				childObjType = 1;//分户
			}
		}
		return childObjType;
	}
	
	@Override
	public List<LedgerTreeBean> getUserParentLedgerTree(long ledgerId, boolean isEleTopo) {
		List<LedgerTreeBean> list = ledgerManagerMapper.getUserParentLedgerTree(ledgerId);
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
				long parentLedgerId = Long.parseLong(ledgerTreeBean.getId());
				int count = ledgerManagerMapper.countLedgerByLedgerId(parentLedgerId);
				if(count <= 0 && isEleTopo){
					count = ledgerManagerMapper.countSubNodeByLedgerId(parentLedgerId,2);
				}else if(count <= 0){
					count = meterBeanMapper.countMeterByLedgerId(parentLedgerId);
				}
				if(count >0)
					ledgerTreeBean.setIsParent(true);
			}
			return list;
		}
		return new ArrayList<LedgerTreeBean>();
	}
	
	@Override
	public List<LedgerTreeBean> getUserParentLedgerTreeGroup(long accountId, boolean isEleTopo) {
		List<LedgerTreeBean> list = ledgerManagerMapper.getUserParentLedgerTreeGroup(accountId);
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
				long parentLedgerId = Long.parseLong(ledgerTreeBean.getId());
				int count = ledgerManagerMapper.countLedgerByLedgerId(parentLedgerId);
				if(count <= 0 && isEleTopo){
					count = ledgerManagerMapper.countSubNodeByLedgerId(parentLedgerId,2);
				}else if(count <= 0){
					count = meterBeanMapper.countMeterByLedgerId(parentLedgerId);
				}
				if(count >0)
					ledgerTreeBean.setIsParent(true);
			}
			return list;
		}
		return new ArrayList<LedgerTreeBean>();
	}

	@Override
	public List<ModuleBean> getUserTabModules(long userId, long parentModuleId) {
		List<ModuleBean> tabModules = moduleBeanMapper.getTabModules(userId, parentModuleId);
		return tabModules == null ? new ArrayList<ModuleBean>() : tabModules;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.FrameService#getParentLedgerTree(long)
	 */
	@Override
	public List<LedgerTreeBean> getParentLedgerTree(long ledgerId) {
		List<LedgerTreeBean> list = ledgerManagerMapper.getParentLedgerTree(ledgerId);
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
				long parentLedgerId = Long.parseLong(ledgerTreeBean.getId());
				int count = ledgerManagerMapper.countLedgerByLedgerId(parentLedgerId);
				if(count >0)
					ledgerTreeBean.setIsParent(true);
			}
			return list;
		}
		return new ArrayList<LedgerTreeBean>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.FrameService#getSubLedgerTree(long)
	 */
	@Override
	public List<LedgerTreeBean> getSubLedgerTree(long parentLedgerId) {
		List<LedgerTreeBean> list = ledgerManagerMapper.getSubLedgerTree(parentLedgerId);
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
				long ledgerId = Long.parseLong(ledgerTreeBean.getId());
				int count = ledgerManagerMapper.countLedgerByLedgerId(ledgerId);
				if(count >0) {
					ledgerTreeBean.setIsParent(true);
				}
			}
			return list;
		}
		return new ArrayList<LedgerTreeBean>();
	}

	@Override
	public List<RegionBean> queryRegionInfo(RegionBean regionBean) {
		return regionBeanMapper.queryRegionInfo(regionBean);
	}

    @Override
    public List<RegionBean> queryOnePointRegion(String regionId){
        return regionBeanMapper.queryOnePointRegion(regionId);
    }
	
	@Override
	public List<IndustryBean> queryIndustryInfo(IndustryBean industryBean) {
		return industryBeanMapper.queryIndustryInfo(industryBean);
	}

	@Override
	public int getAllLedgerCountByUser(UserBean userBean) {
		if (userBean.getLedgerId() != null && userBean.getLedgerId() != 0)
			return ledgerManagerMapper.getAllLedgerCount(userBean.getLedgerId());
		else
			return ledgerManagerMapper.getAllLedgerCountByGroup(userBean.getAccountId());
	}

	@Override
	public List<LineLossTreeBean> getLineLossTreeData(long ledgerId) {
		List<LineLossTreeBean> data = meterBeanMapper.getLineLossTreeData(ledgerId);
		LineLossTreeBean l = new LineLossTreeBean();
		l.setId(-1);
		l.setName("不参加线损计算");
		l.setDrag(false);
		l.setpId(0);
		data.add(l);
		return data;
	}

	@Override
	public void saveLineLossRelation(long ledgerId, List<LineLossTreeBean> line) {
		meterBeanMapper.deleteLinelossRelation(ledgerId);
		Map<Long, Long> nosave = new HashMap<Long, Long>();
		nosave.put(-1L, -1L);
		for (LineLossTreeBean lb : line) {
			if ((lb.getId() != -1) && (nosave.get(lb.getpId()) == null))
				meterBeanMapper.saveLineLossRelation(lb);
			else {
				if (lb.getId() != -1)
					nosave.put(lb.getId(), lb.getId());
			}
		}
	}

    @Override
    public List<LedgerTreeBean> getUserGroupParentTree(Long nodeId, int groupType, int level,UserBean userBean, boolean isEleTopo) {
        List<LedgerTreeBean> list = null;
//        if(level == 1 && nodeId == 1){//管理员
//            list = ledgerManagerMapper.getUserGroupParentLedgerTree(null,groupType,level);
//        }else{
//        }
        if(userBean.getLedgerId() == null){
            return new ArrayList<LedgerTreeBean>();
        }
        list = ledgerManagerMapper.getUserGroupParentLedgerTree(nodeId,groupType,level,userBean.getLedgerId());
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
                int count = 0;
                if(level == 1){
                    long groupId = Long.parseLong(ledgerTreeBean.getId());
                    count = ledgerManagerMapper.countSubNodesByGroupIdAndLedgerId(groupId,groupType,userBean.getLedgerId());
                }else{
                    long parentLedgerId = Long.parseLong(ledgerTreeBean.getId());
                    count = ledgerManagerMapper.countLedgerByLedgerId(parentLedgerId);
                    if(count <= 0 && isEleTopo){
                        count = ledgerManagerMapper.countSubNodeByLedgerId(parentLedgerId,2);
                    }else if(count <= 0){
                        count = meterBeanMapper.countMeterByLedgerId(parentLedgerId);
                    }
                }
                if(count >0)
                    ledgerTreeBean.setIsParent(true);
			}
			return list;
		}
		return new ArrayList<LedgerTreeBean>();
    }

	@Override
	public void saveSession(String loginsessionkey, UserBean userBean, String roleType,
			HttpServletRequest request, String username) {
		request.getSession().setAttribute(loginsessionkey, userBean);
		if (roleType == null || roleType.length()==0) roleType = "1"; request.getSession().setAttribute("roleType", roleType);
        if("guest".equals(username)){
            request.getSession().setAttribute("isGuestUser", true);
        }else{
            request.getSession().setAttribute("isGuestUser", false);
        }
		
	}
	
	
	@Override
	public List<TypeBean> queryTypeInfo() {return typeBeanMapper.queryTypeInfo(); }
	
	@Override
	public List<PollutBean> queryPollut(PollutBean pollutBean) {
		return pollutMapper.queryPollutInfo( pollutBean );
	}
	
	@Override
	public List<PollutctlBean> queryPollutctl(PollutctlBean pollutctlBean) {
		return pollutctlMapper.queryPollutctlInfo( pollutctlBean );
	}
}
