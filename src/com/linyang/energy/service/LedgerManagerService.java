package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.CateBean;
import com.linyang.energy.model.LedgerAllMeterBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.LedgerDeviceAllMeterBean;
import com.linyang.energy.model.LedgerMeterBean;
import com.linyang.energy.model.RateBean;

/**
 * @Description 分户管理Service
 * @author Leegern
 * @date Dec 9, 2013 10:53:35 AM
 */
public interface LedgerManagerService {
	
	/**
	 * 根据分户Id删除分户信息
	 * @param ledgerId 分户Id
	 * @return
	 */
    int deleteByLedgerId(Long ledgerId);
    
    /**
     * 添加分户信息(只插入不为空的字段)
     * @param ledger
     * @return
     */
    int insertBySelective(LedgerBean ledger);
    
    /**
     * 根据分户Id查询分户信息
     * @param ledgerId 分户Id
     * @return
     */
    LedgerBean selectByLedgerId(Long ledgerId);
    
    /**
     * 根据条件查询一条分户信息
     * @param ledger
     * @return
     */
    LedgerBean findOneLedger(LedgerBean ledger);
    
    /**
     * 更新分户信息(只更新不为空的字段)
     * @param ledger
     * @return
     */
    int updateBySelective(LedgerBean ledger);
    
    /**
     * 分页查询分户信息
     * @param ledger
     * @return
     */
    List<LedgerBean> getLedgerList(Page page, LedgerBean ledger, String parentName);
    
    /**
	 * 查询 电 费率信息
	 * @param currentDate 当前时间
	 * @return
	 */
	List<RateBean> queryRateInfo(Date currentDate);


    /**
     * 查询 水、气、热 费率信息
     * @return
     */
    List<Map<String, Object>> queryOtherRateInfo(int rateType);

	/**
	 * 查询分析类型
	 * @return
	 */
	List<CateBean> queryAnalyTypes();
	
	/**
	 * 根据分户Id查询分户关联数据
	 * @param ledgerId 分户Id
	 * @return
	 */
	int getLedgerRelatedByLedgerId(Long ledgerId);

	/**
	 * 得到所有分户及其所属计量点列表  (已废弃)
	 * 
	 * @return
	 */
	List<LedgerAllMeterBean> getLedgerAllMeter();
	
	/**
	 * 插入分户计量表 (已废弃)
	 * 
	 * @param meters
	 */
	void insertLedgerMeter(Map<Long, Map<Integer,List<LedgerMeterBean>>> meters, int type);
	
	/**
	 * 得到所有分项及其所属计量点列表 (已废弃)
	 * 
	 * @return
	 */
	List<LedgerAllMeterBean> getDeviceAllMeter();
	
	/**
	 * 得到所有分户分项及其所属计量点列表 (已废弃)
	 * 
	 * @return
	 */
	List<LedgerDeviceAllMeterBean> getLedgerDeviceMeter();
	
	/**
	 * 插入分户分项计量表 (已废弃)
	 *
	 */
	void insertLedgerDeviceMeter(Map<Long,Map<Long, Map<Integer, List<LedgerMeterBean>>>> result);
	
	/**
	 * 
	 * 函数功能说明  :检查同级下是否有相同的用户名称
	 * @param map
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	boolean checkLedgerName(Map<String, Object> map);
	
	/**
	 * 得到子类分户树信息(不包括测量点)
	 * 
	 * @param parentLedgerId
	 *            父类分户ID
	 * @return
	 */
	List<LedgerTreeBean> getSubLedgerTree(long parentLedgerId);
	
	/**
	 * 
	 * 根据查询条件得到分户对象列表
	 * @author guosen
	 * @param param
	 * @return
	 */
	List<LedgerBean> getLedgerListByParam(Map<String, Object> param);

	/**
	 * 获取用户群组分户
	 *@author chengq
	 *@date 2014-8-27
	 *@param
	 *@return
	 */
	List<LedgerBean> getUserLedger(long accountId);
	
	//add by qwt 2015-6-4
	//add for 添加获取全部分户的函数
	/**
	 * 获取全部分户
	 *@author qwt
	 *@date 2015-6-4
	 *@param
	 *@return
	 */
	List<LedgerBean> getAllLedger();
	//end

	/**
	 * 是否可以配置，true,可以，fasle：不可以
	 */
	boolean checkLineloss(Map<String, Object> map);

	/**
	 * 
	 * 根据Id得到相应的分户信息
	 * @param ledgerId
	 * @return  LedgerBean     
	 * @throws
	 */
	LedgerBean getLedgerDataById(Long ledgerId);


    /**  新的计算模型---刷新T_LEDGER_METER表 */
    void updateLedgerMeter();

    /**  配置EMO的计算模型 的时候需要执行该方法，刷新关联关系 */
    void updateOneLedger(Long ledgerId);


    /**  旧计算模型--新计算模型 */
    void copyOldModel();


	/**
	 * 保存第三方设置
	 * @param
	 */
	boolean saveThirdCompanySet(LedgerBean bean);

	/**
	 * 获取第三方配置
	 * @param ledgerId
	 * @param loginPath
	 * @param logoType 1大屏 2常规屏
	 * @return
	 */
	Map<String, Object> getLogoConfig(Long ledgerId,String loginPath,int logoType);

    /**
	 * 获取计算模型中dcp的所有类型
	 * @param ledgerId
	 * @return
	 */
    public List<Short> getLedgerCalcDCPType(long ledgerId);

    /**
     * 更新分户X、Y坐标
     */
    void saveLedgerPosition(LedgerBean bean);

    /**
     * 删除分户X、Y坐标
     */
    void deleteLedgerPosition(Long ledgerId);

    /**
     * 得到首页地图需要显示的企业的List
     */
    List<LedgerBean> getMapShowLedgerList(Long ledgerId, Integer searchModel, String selectIdStr, String keyWord);

    /**
     * 得到某个分户 地图上显示的用量数据
     */
    Map<String,Object> getLedgerUseData(LedgerBean ledger);

    /**
     * 获得某个EMO的企业户数、监测点个数、运营商个数
     */
    Map<String,Object> getLedgerMessageData(Long ledgerId);

    /**
     * 获得某个EMO的实时功率
     */
    Map<String,Object> getLedgerPowerData(Long ledgerId);

    /**
     * 得到emo下所有的子emo
     */
    public List<LedgerBean> getAllSubLedgersByLedgerId(Long ledgerId);

    /**
     *  保存第三方链接
     * @param ledgerId 企业ID
     * @param urlStr	url链接
     * @return
     */
	boolean saveCompanyLink(Long ledgerId, String urlStr);

	/**
	 * 得到第三方链接
	 * @param ledgerId
	 * @return
	 */
	Map<String, Object> checkThirdLink(Long ledgerId);

	/**
	 * 得到下拉能管单元列表 
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getLedgersByLedgerId(Long ledgerId);

	List<Map<String, Object>> getLedgersForGroup(Long accountId);

	List<Map<String, Object>> getLedgersForGroup2(Long accountId);

	/**
	 * 得到下拉采集点列表
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getMetersByLedgerId(Long ledgerId);

    List<Map<String, Object>> getMetersForGroup(Long accountId);
	
	/**
	 * 得到采集点得上级能管单元
	 * @param meterId
	 * @return
	 */
	Long getLedgerByMeterId(Long meterId);

    /**
     * 根据分户Id查询该分户是否为企业（或者在企业下）
     * @param ledgerId 分户Id
     * @return
     */
    int getIsLedgerFlag(Long ledgerId);
    
    /**
     * 如果为群组则获取群组中第一个ledgerId
     * @param accountId
     * @return
     */
    Long getLedgerIfNull(Long accountId);
    
    
    /**
	 * 根据测量点id查询能管对象id集合、所在结构树根id
	 * @param MeterId
	 * @return
	 */
	Map<String, Object> getLedgerInfoByMeterId(Long meterId,Long ledgerId);


    /**
     * 查询热泵类型
     */
    Map<String, Object> getLedgerHeatType(Long ledgerId);

    /**
     * 能源地图--数据查询
     */
    public Map<String, Object> getEnergyMapData(Long ledgerId);

    /**
     * 能源地图--保存背景图
     */
    public void saveLedgerBackImg(Long ledgerId, String fileName);

    /**
     * 能源地图--保存样式
     */
    public void saveLedgerStyle(Long ledgerId, Integer displayForm, Integer fontSize, String fontWeight, String fontColor, Double bubble, String backColor, String dataColor);

    /**
     * 能源地图--保存关联的坐标点（能管对象或者采集点）
     */
    public String saveLedgerRelate(Long ledgerId, Long oldObjectId, Integer oldObjectType, Long newObjectId, Integer newObjectType, String dataType, Double x, Double y, Integer newPosition);

    /**
     * 能源地图--删除 关联的坐标点（能管对象或者采集点）
     */
    public void removeLedgerRelate(Long ledgerId, Long objectId, Integer objectType);

    /**
     * 能源地图--查询每个坐标点的数据
     */
    public Map<String, Object> getOnePointDataInImg(Long objectId, Integer objectType, Long ledgerId);

    public Map<String, Object> getObjectNeedData(Long ledgerId, Long objectId, Integer objectType);

    /**
     * 能源地图--关联能管对象或采集点 自动完成搜索
     */
    public List<Map<String, Object>> getRelateLedgerMeterList(Long ledgerId, Integer searchModel, Long objectId, Integer objectType, String objectName);

    /**
     * 能源地图--判断是否能修改为液晶模式
     */
    public Long getIfCanChangeYj(Long ledgerId);

    /**
     * 重点数据维护--查询列表
     */
    public List<Map<String, Object>> getDataMaintainPage(Map<String, Object> queryMap);

    /**
     * 重点数据维护 -- 得到下拉de企业列表
     */
    public List<Map<String, Object>> getCompLedgerList(Long ledgerId);

    /**
     * 重点数据维护--根据Id查询
     */
    public Map<String, Object> getDataMaintain(Long ledgerId);

    /**
     * 重点数据维护--删除
     */
    public void deleteMaintain(Long ledgerId);

    /**
     * 重点数据维护--保存
     */
    public void saveDataMaintain(Map<String, Object> info);
	
	
	
	/**
	 * 删除产污/治污关联关系
	 * @param ledgerBean
	 * @return
	 */
	Integer deleteFacil(LedgerBean ledgerBean);
	
	
	/**
	 * 查询所选节点上级101/106节点是否已经设置了产污/治污
	 * @param ledgerId
	 * @return
	 */
	List<Map<String,Object>> selectParent (Long ledgerId);
	
	
	/**
	 * 查询所选节点是产污设施的对应关系
	 * @param ledgerId
	 * @return
	 */
	List<Map<String,Object>> queryPollut(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 查询所选节点是治污设施的对应关系
	 * @param ledgerId
	 * @return
	 */
	List<Map<String,Object>> queryPollutctl(@Param("ledgerId")Long ledgerId);
	
	
	/**
	 *
	 * 函数功能说明  :检查设备编号是否有重复
	 * @param map
	 * @return
	 * @return  boolean
	 * @throws
	 */
	boolean checkdeviceCode(Map<String, Object> map);

}
