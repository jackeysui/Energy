package com.linyang.energy.service;

import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 框架业务逻辑层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 4, 2013
 */
public interface FrameService {
	
	/**
	 * 获取一个用户的模块信息
	 * @param userId 用户ID
	 * @return
	 */
    public Map<String,Object> getUserModules(long userId);

	public Map<String,Object> getUserModules(long userId, Integer roleType);
	 /**
	  * 得到用户的tab子模块信息
	  * @param userId 用户id
	  * @param parentModuleId 父模块id
	  * @return
	  */
	 public List<ModuleBean> getUserTabModules(long userId,long parentModuleId);
	 
	 /**
     * 得到一个用户的父类分户树
	 * @param isEleTopo 
     * @param userId 用户id 
     * @return
     */
    public List<LedgerTreeBean> getUserParentLedgerTree(long ledgerId, boolean isEleTopo);
    
	/**
	 * 根据组权限得到一个用户的父类分户树
	 * @param isEleTopo 
	 * @param userId 用户id
	 * @return
	 */
	public List<LedgerTreeBean> getUserParentLedgerTreeGroup(long accountId, boolean isEleTopo);
    
    /**
     * 得到子类分户树信息
     * @param parentLedgerId 父类分户ID
     * @return
     */
    public List<LedgerTreeBean> getChildLedgerTree(long parentLedgerId);
    
    /**
     * 得到企业分户电力拓扑树信息
     * @param parentLedgerId 父类分户ID
     * @return
     */
    public List<LedgerTreeBean> getChildEleTree(long parentId,int objType,Integer meterType);
    
    /**
     * 得到一个用户的父类分户树(不包括测量点)
     * @param userId 用户id
     * @return
     */
    List<LedgerTreeBean> getParentLedgerTree(long ledgerId);
    /**
     * 得到子类分户树信息(不包括测量点)
     * @param parentLedgerId 父类分户ID
     * @return
     */
    List<LedgerTreeBean> getSubLedgerTree(long parentLedgerId);
    /**
     *得到区域树信息
     *@author chengq
     *@date 2014-8-14
     *@param
     *@return
     */
	public List<RegionBean> queryRegionInfo(RegionBean regionBean);
    /**
     * 查询某个省或市下的区域树信息
     */
	public List<RegionBean> queryOnePointRegion(String regionId);

    /**
     *得到专业树信息
     *@author chengq
     *@date 2014-8-14
     *@param
     *@return
     */
	public List<IndustryBean> queryIndustryInfo(IndustryBean industryBean);
	
	/**
	 * 根据用户权限得到所有显示的分户和测量点数量
	 * 
	 * @param userBean
	 * @return
	 */
	public int getAllLedgerCountByUser(UserBean userBean);
	
	/**
	 * 获取线损树
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<LineLossTreeBean> getLineLossTreeData(long ledgerId);
	
	/**
	 * 保存线损配置关系
	 * 
	 * @param ledgerId
	 * @param line
	 */
	public void saveLineLossRelation(long ledgerId, List<LineLossTreeBean> line);

    public List<LedgerTreeBean> getUserGroupParentTree(Long nodeId, int groupType, int level,UserBean userBean, boolean isEleTopo);

    /**
     * 保存session信息
     * @param loginsessionkey 
     * @param userBean
     * @param roleType
     * @param request
     * @param username 
     */
	public void saveSession(String loginsessionkey, UserBean userBean, String roleType,
			HttpServletRequest request, String username);
	
	/**
	 * 获取设备类型树
	 * @return
	 */
	public List<TypeBean> queryTypeInfo();
	
	
	/**
	 *得到产污设备树
	 *@author dingy
	 *@date 2014-8-14
	 *@param
	 *@return
	 */
	public List<PollutBean> queryPollut(PollutBean pollutBean);
	
	
	/**
	 *得到治污设备树
	 *@author dingy
	 *@date 2014-8-14
	 *@param
	 *@return
	 */
	public List<PollutctlBean> queryPollutctl(PollutctlBean pollutctlBean);
}
