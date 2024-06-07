package com.linyang.energy.mapping.recordmanager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.*;

import org.apache.ibatis.annotations.Param;

import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.LedgerTreeBean;

/**
 * @Description 分户管理Mapper
 * @author Leegern
 * @date Dec 9, 2013 11:16:29 AM
 */
public interface LedgerManagerMapper {
	
	/**
	 * 查询 电 费率信息
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
     * 分页查询分户信息
     * @param ledger
     * @return
     */
    List<LedgerBean> getLedgerPageList(@Param("page")Page page, @Param("ledger")LedgerBean ledger, @Param("parentName")String parentName);
    
    /**
     * 获取最大边距值
     * @return
     */
    Integer getMaxBorder();
    
    /**
     * 查询此节点的前一个节点
     * @param ledgerId
     * @return
     */
    LedgerBean findPreviousLedger(long ledgerId);
    
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
     * 添加分户费率历史信息(应用与修改费率时)
     * @param param 参数
     * @return
     */
    int addLedgerRateHisInfo(Map<String, Object> param);
    
    /**
     * 添加分户阀值关联关系
     * @param param 参数
     * @return
     */
    int addLedgerThreshold(Map<String, Object> param);
    
    /**
     * 删除分户阀值关联关系
     * @param ledgerId 分户Id
     * @return
     */
    int deleteLedgerThreshold(long ledgerId);
    
    /**
     * 更新分户信息(只更新不为空的字段)
     * @param ledger
     * @return
     */
    int updateBySelective(LedgerBean ledger);

    void updateRelationLedgerName(LedgerBean ledger);

    /**
     * 删除分户坐标
     */
    int deleteLedgerPosition(Long ledgerId);
    
    /**
     * 更新分户左右边距(用于添加)
     * @param ledger
     * @return
     */
    int updateLftRgtForAdd(LedgerBean ledger);
    
    /**
     * 更新分户左右边距(用于删除)
     * @param ledger
     * @return
     */
    int updateLftRgtForDel(LedgerBean ledger);
    
	/**
	 * 更新分户右边距
	 * 
	 * @param span
	 * @param ledgerRgt
	 */
	public void updateLedgerRgt(@Param("span") int span, @Param("ledgerRgt") int ledgerRgt);
	
	/**
	 * 更新分户左边距
	 * 
	 * @param span
	 * @param ledgerRgt
	 */
	public void updateLedgerLft(@Param("span") int span, @Param("ledgerLft") int ledgerLft);
	
	/**
	 * 更新分户左右边距
	 * 
	 * @param ledger
	 */
	public void updateLftRgt(LedgerBean ledger);
	
	/**
	 * 根据父分户查询子分户列表
	 * 
	 * @param ledgerLft
	 * @param ledgerRgt
	 * @return
	 */
	public List<LedgerBean> getLedgerListByParent(@Param("ledgerLft") int ledgerLft, @Param("ledgerRgt") int ledgerRgt);
	
    /**
     * 根据条件查询分户信息
     * @param ledger
     * @return
     */
    List<LedgerBean> getLedgerList(LedgerBean ledger);
    
    /**
	 * 根据分户Id查询分户关联数据
	 * @param ledgerId 分户Id
	 * @return
	 */
	int getLedgerRelatedByLedgerId(Long ledgerId);
    
	/**
	 * 根据分户Ids查询分户信息
	 * @param ids 分户Ids
	 * @return
	 */
	List<LedgerBean> getLedgerByIds(List<Long> ids);
	
    /**
     * 查询一个分户下子分户的个数
     * @param parentLedgerId 父类分户Id
     * @return
     */
    int countLedgerByLedgerId(@Param("parentLedgerId")long parentLedgerId);
    
    /**
     * 电力拓扑树下查询一个企业分户下子节点的个数
     * @param ledgerId 分户Id
     * @param childObjType 分户Id
     * @return
     */
    int countSubNodeByLedgerId(@Param("ledgerId")long ledgerId,@Param("childObjType")int childObjType);
    
    /**
     * 得到一个用户的父类分户树(如果是超级管理员的话那么ledgerId=-100,特殊处理)
     * @param userId 用户id
     * @return
     */
    List<LedgerTreeBean> getUserParentLedgerTree(@Param("ledgerId")long ledgerId);
    
	/**
	 * 根据组权限得到一个用户的父类分户树
	 * 
	 * @param acountId
	 * @return
	 */
	public List<LedgerTreeBean> getUserParentLedgerTreeGroup(@Param("acountId") long acountId);
    
    /**
     * 得到子类分户树信息
     * @param parentLedgerId 父类分户ID
     * @return
     */
    List<LedgerTreeBean> getChildLedgerTree(@Param("parentLedgerId")long parentLedgerId);
    
    /**
     * 得到企业分户电力拓扑树信息
     * @param parentLedgerId 父类分户ID
     * @return
     */
    List<LedgerTreeBean> getChildEleTree(@Param("parentId")long parentId,@Param("objType")int objType,@Param("childObjType")int childObjType,@Param("meterType")Integer meterType);
    
	/**
	 * 得到所有分户及其所属计量点列表
	 * 
	 * @return
	 */
	List<LedgerAllMeterBean> getLedgerAllMeter();
	
	/**
	 * 清空分户计量表
	 */
	void delLedgerMeter(@Param("ledgerId")Long ledgerId);

    /**
     * 为了便于计算，分户计量表中，1表示"+"、-1表示"-"
     */
    void changeLedgerMeter();
	
	/**
	 * 插入分户计量表
	 */
	void insertLedgerMeter(Map<String, Long> param);
	
	/**
	 * 得到所有分项及其所属计量点列表
	 * 
	 * @return
	 */
	List<LedgerAllMeterBean> getDeviceAllMeter();
	
	/**
	 * 清空分项计量表
	 */
	void delDeviceMeter();
	
	/**
	 * 插入分项计量表
	 */
	void insertDeviceMeter(Map<String, Long> param);
	
	/**
	 * 得到所有分户分项及其所属计量点列表
	 * 
	 * @return
	 */
	List<LedgerDeviceAllMeterBean> getLedgerDeviceMeter();
	
	/**
	 * 清空分户分项计量表
	 */
	void delLedgerDeviceMeter();

	/**
	 * 插入分户分项计量表
	 * 
	 * @param param
	 */
	void insertLedgerDeviceMeter(Map<String, Long> param);
	
	/**
	 * 得到一个用户的父类分户树(不包含测量点)(如果是超级管理员的话那么ledgerId=-100,特殊处理)
	 * @param ledgerId 分户Id
	 * @return
	 */
	List<LedgerTreeBean> getParentLedgerTree(@Param("ledgerId")long ledgerId);
	
	/**
	 * 得到子类分户树信息(不包含测量点)
	 * @param parentLedgerId parentLedgerId 父类分户Id
	 * @return
	 */
	List<LedgerTreeBean> getSubLedgerTree(@Param("parentLedgerId")long parentLedgerId);
	
	/**
	 * 
	 * 函数功能说明  :检查同级下是否有相同的用户名称
	 * @param map
	 * @return      
	 * @return  Long     
	 * @throws
	 */
	Long checkLedgerName(Map<String, Object> map);
	
	/**
	 * 
	 * 函数功能说明  :更新子分户的费率
	 * @param ledger      
	 * @return  void     
	 * @throws
	 */
	public void updateNodeRate(LedgerBean ledger);
	
	/**
	 * 
	 * 根据查询条件得到分户对象列表
	 * @author guosen
	 * @param param
	 * @return
	 */
	List<LedgerBean> getLedgerInfoPageList(Map<String, Object> param);
	
	/**
	 * 
	 * 根据查询条件得到分户对象列表(不分页)
	 * @param param
	 * @return
	 */
	List<LedgerBean> getLedgerInfoList(Map<String, Object> param);
	
	/**
	 * 查询一个分户下所有分户和测量点的数量
	 * 
	 * @param ledgerId
	 * @return
	 */
	public int getAllLedgerCount(long ledgerId);
	
	/**
	 * 根据组权限查询一个分户下所有分户和测量点的数量
	 * 
	 * @param accountId
	 * @return
	 */
	public int getAllLedgerCountByGroup(long accountId);

	/**
	 *获取用户群组分户
	 *@author chengq
	 *@date 2014-8-27
	 *@param
	 *@return
	 */
	public List<LedgerBean> getUserLedger(@Param("accountId")Long accountId);

	/**
	 *根据底层分户查询递归所有祖先分户
	 *@author chengq
	 *@date 2014-8-28
	 *@param
	 *@return
	 */
	List<LedgerBean> queryRecursiveLedgerById(@Param("ledgerId")Long ledgerId);

	/**
	 * 更新子分户的分户类型
	 * @author guosen
	 * @date 2014-12-16
	 * @param ledger
	 */
	void updateNodeLedgerType(LedgerBean ledger);
	
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
	 * 是否可以配置，0,可以，1：不可以
	 */
	Integer checkLineloss(Map<String, Object> map);
	
	/**
	 * 分户是否有企业属性，0,无，1：有
	 */
	Integer isFirmAnalyType(long ledgerId);

    /**
     * 得到父级的"累加属性"和ID
     *@param type=1表示分户，type=2表示测量点
     *@return 父级的ID和ADD_ATTR组成的map
     **/
    Map<String, Object> getParentAddAttr(@Param("id")Long id, @Param("type")Integer type);


    /**
     * 得到分户上级的名称、和企业类型
     **/
    Map<String, Object> getParentLedgerCompany(@Param("ledgerId")Long ledgerId);
    
	/**
	 * 根据分户id查询计量总表
	 */
	List<MeterBean> querySummaryMeterByLedgerId(long ledgerId);


    /**
     * 得到配置了计算模型的所有分户
     * */
    List<Long> getAllCompLedgerIds();

    /**
     * 根据ledgerId得到该分户所配置的计算模型
     * */
    List<LedgerRelationBean> getLedgerMeterRelation(@Param("ledgerId")Long ledgerId, @Param("relationType")Integer relationType);

    /**
     * 根据ledgerId得到配置父级
     * */
    List<LedgerRelationBean> getParentRelation(@Param("ledgerId")Long ledgerId);

    /**
     *
     * */
    List<Map<String, Object>> getChildLedgerMeter(@Param("attrType")Integer attrType, @Param("percent")Integer percent, @Param("ledgerId")Long ledgerId);

    /**
     * 得到ledger_meter数据条数
     */
    int getOneRelationcount(@Param("ledgerId")Long ledgerId, @Param("meterId")Long meterId);

    /**
     * 插分户计量表
     */
    void insertLedgerMeterNew(Map<String, Object> param);

    /**
     * 更新户计量表
     */
    void updateLedgerMeterNew(Map<String, Object> param);


	Integer checkLinelossByParentId(Map<String, Object> map);

	/**
	 * 得到所属企业Id
	 * @param ledgerId
	 * @return
	 */
	Long getCompanyId(@Param("ledgerId")long ledgerId);


    /** 得到旧计算模型中，分户下的总、分表 对应到t_ledger_relation表的数据 */
    List<LedgerRelationBean> getPointRelations(@Param("ledgerId")long ledgerId, @Param("meterType")Integer meterType);

    /** 得到旧计算模型中，分户下的子分户 对应到t_ledger_relation表的数据 */
    List<LedgerRelationBean> getLedgerRelations(@Param("ledgerId")long ledgerId);

    /** 删T_LEDGER_RELATION */
    void deleteLedgerRelation();
    /** 插T_LEDGER_RELATION */
    void addLedgerRelation(LedgerRelationBean ledgerRelationBean);
    
    /**
     * 得到企业列表
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getCompanyList(@Param("ledgerId")long ledgerId, @Param("analyType")Integer analyType);


    /**
     * 得到管辖区域内未设置经纬度的企业列表
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getNoPositionLedgerList(@Param("ledgerId")Long ledgerId);

    /**
     * 得到首页地图 某种搜索模式 下的自动完成结果
     * @return
     */
    List<Map<String, Object>> getSearchModelDataList(@Param("ledgerId")Long ledgerId, @Param("searchModel")Integer searchModel);

    /**
     * 得到父级企业
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getParentCompany(@Param("ledgerId")long ledgerId, @Param("analyType")Integer analyType);

    /**
     * 得到某一分户下面的子分户Id列表
     * @param ledgerId
     * @return
     */
	List<Long> getChildLedgerIds(@Param("ledgerId") Long ledgerId);

	/**
	 * @函数功能说明 : 根据EMO ID和阈值类型得到阈值信息
	 * @author xuesai
	 * @param ledgerId,thresholdId
	 * @return String
	 */
	Map<String, Object> getLedgerThresholdInfo(@Param("ledgerId")long ledgerId, @Param("thresholdId")long thresholdId);
    
    /**
     * 获取下一级子节点数量
     * @param ledgerId
     * @return
     */
    public int getNextLedgerAmount(@Param("ledgerId")long ledgerId);
    
    /**
     * 获取能管对象及路径
     * @param queryMap
     * @return
     */
    public List<Map<String,Object>> getEMOPath(Map<String,Object> queryMap);
    
    public List<LedgerBean> getAllCompanyList();
    
    /**
     * 获取能管对象得到所有父级能管对象
     * @param ledgerId
     * @return
     */
    public List<LedgerBean> getAllParentLedgersByLedgerId(@Param("ledgerId")long ledgerId);
    /**
     * 获取能管对象下所有企业分户的个数
     * @param ledgerId
     * @return
     */
    public int countCompanyLedgerByLedgerId(@Param("ledgerId")long ledgerId);
    
    /**
     * 获取能管对象下所有计量点的个数
     * @param ledgerId
     * @return
     */
    public int countMeterByLedgerId(@Param("ledgerId")long ledgerId);
    
    /**
     * 获取能管对象下所有省的个数
     * @param ledgerId
     * @return
     */
    public int countProvinceByLedgerId(@Param("ledgerId")long ledgerId);
    
    /**
     * 获取能管对象下所有市的个数
     * @param ledgerId
     * @return
     */
    public int countCityByLedgerId(@Param("ledgerId")long ledgerId);
    
    /**
     * 获取能管对象下所有运营商的个数
     * @param ledgerId
     * @return
     */
    public int countPartnerByLedgerId(@Param("ledgerId")long ledgerId);

    /**
     * 得到首页地图需要显示的企业的List
     */
    List<LedgerBean> getMapShowLedgerList1(@Param("ledgerId")long ledgerId, @Param("searchModel")int searchModel, @Param("selectId") String selectId);
    List<LedgerBean> getMapShowLedgerList2(@Param("ledgerId")long ledgerId, @Param("keyWord") String keyWord);

    /**
     * 获取群组树
     */
    public List<LedgerTreeBean> getUserGroupParentLedgerTree(@Param("nodeId")Long nodeId,@Param("groupType")int groupType,@Param("level")int level,@Param("ledgerId")long ledgerId);

    /**
     * 计算群组树的子节点个数
     */
    public int countSubNodesByGroupIdAndLedgerId(@Param("groupId")long groupId, @Param("groupType")int groupType, @Param("ledgerId")long ledgerId);

    /**
     * 计算emo需量申报值
     */
    public Double getEmoDemandDecalre(@Param("ledgerId")long ledgerId,@Param("date")Date date);

    /**
     * 获取emo下所有的emo
     */
    public List<LedgerBean> getAllSubLedgersByLedgerId(Long ledgerId);

    /**
     * 得到企业和平台运营商
     * @param ledgerId
     * @param typeList
     * @return
     */
	List<Map<String, Object>> getEMOList(@Param("ledgerId")Long ledgerId);

	/**
     * 得到企业和平台运营商
     * @param ledgerId
     * @param typeList
     * @return
     */
	List<Map<String, Object>> getParentEMO(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 得到下拉能管单元列表 
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getLedgersByLedgerId(@Param("ledgerId")Long ledgerId);

	List<Map<String, Object>> getLedgersForGroup(@Param("accountId")Long accountId);

	List<Map<String, Object>> getLedgersForGroup2(@Param("accountId")Long accountId);

	/**
	 * 得到下拉采集点列表
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getMetersByLedgerId(@Param("ledgerId")Long ledgerId);

	List<Map<String, Object>> getMetersForGroup(@Param("accountId")Long accountId);

	/**
	 * 得到采集点得上级能管单元
	 * @param meterId
	 * @return
	 */
	List<Long> getLedgerByMeterId(@Param("meterId")Long meterId);

    /**
     * 得到分户上级能管单元
     * @param ledgerId
     * @return
     */
    List<Long> getParentCompanys(@Param("ledgerId")Long ledgerId);
    
    /**
     * 如果为群组则获取群组中第一个ledgerId
     * @param accountId
     * @return
     */
    Long getLedgerIfNull(@Param("accountId")Long accountId);
    
    /**
     *通过测量点id,找能管对象id
     *从T_LEDGER_SHOW
     * @param meterId
     * @return
     */
    List<Long> getLegerIdByMeterId(@Param("meterId")Long meterId);
    /**
     *通过测量点id,找能管对象id
     *从 T_LEDGER_RELATION
     * @param meterId
     * @return
     */
    List<Long> getLegerIdByMeterId1(@Param("meterId")Long meterId);
    
    /**
     *通过测量点ids,找能管对象id
     *从T_LEDGER_SHOW
     * @param meterIds
     * @return
     */
    List<Long> getLedgerIdByMeterIds(@Param("meterIds")List<Long> meterIds);
    
    /**
     *通过测量点ids,找能管对象id
     *从T_LEDGER_RELATION
     * @param meterIds
     * @return
     */
    List<Long> getLedgerIdByMeterIds1(@Param("meterIds")List<Long> meterIds);
    
    /**
     * 获取测量点的父测量点集
     * T_LINELOSS_METER_INFO
     * @param meterId
     * @return
     */
    List<Long> getMeterIdsByMeterId(@Param("meterId")Long meterId);
    
    
    /**
     *通过测量点id,找能管对象根id
     * @param meterId
     * @return
     */
    Long getRootLedgerIdByMeterId(@Param("meterId")Long meterId);
    
    /**
     *通过测量点id,查询企业id
     * @param meterId
     * @return
     */
    Long getCompanyIdByMeterId(@Param("meterId")Long meterId);
    
    /**
     *通过能管对象id,查询相关联能管对象id集合
     * @param meterId
     * @return
     */
    List<Long> getLedgerIdsByLedgerId(@Param("ledgerId")Long ledgerId);

    /**
     * ledger的计算模型中包含的meter
     * t_ledger_meter
     * @param ledgerId
     * @return
     */
    List<Long> getMeterIdsByLedgerId(@Param("ledgerId")Long ledgerId);

    /**
     * 查询热泵最新状态
     */
    List<Integer> getHeatStatusBy(@Param("meterId")Long meterId, @Param("tag")Integer tag, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    List<Double> getHeatRoomTempBy(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
    List<Double> getHeatWaterTempBy(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
    List<Double> getHeatUbBy(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
    List<Double> getHeatIbBy(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    List<Map<String, Object>> getHeatQVal2(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
    List<Double> getHeatQVal1(@Param("meterId")Long meterId, @Param("dateTime")String dateTime);

    List<Map<String, Object>> getHeatHotVal2(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
    List<Double> getHeatHotVal1(@Param("meterId")Long meterId, @Param("dateTime")String dateTime);

    /**
     *  能源地图 相关查询 begin
     */
    long getLedgerFontstyleNumBy(@Param("ledgerId")Long ledgerId);

    void updateLedgerBackImg(@Param("ledgerId")Long ledgerId, @Param("fileName")String fileName);

    void addLedgerBackImg(@Param("ledgerId")Long ledgerId, @Param("fileName")String fileName);

    Map<String, Object> getLedgerFontstyleById(@Param("ledgerId")Long ledgerId);

    void updateLedgerStyle(@Param("ledgerId")Long ledgerId, @Param("displayForm")Integer displayForm, @Param("fontSize")Integer fontSize,@Param("fontWeight")String fontWeight,
                           @Param("fontColor")String fontColor, @Param("bubble")Double bubble, @Param("backColor")String backColor, @Param("dataColor")String dataColor);

    void addLedgerStyle(@Param("ledgerId")Long ledgerId, @Param("displayForm")Integer displayForm, @Param("fontSize")Integer fontSize,@Param("fontWeight")String fontWeight,
                        @Param("fontColor")String fontColor, @Param("bubble")Double bubble, @Param("backColor")String backColor, @Param("dataColor")String dataColor);

    long getLedgerRelateNumBy(@Param("ledgerId")Long ledgerId, @Param("objectId")Long objectId, @Param("objectType")Integer objectType);

    void updateLedgerRelate(@Param("ledgerId")Long ledgerId, @Param("oldObjectId")Long oldObjectId, @Param("oldObjectType")Integer oldObjectType,
                            @Param("newObjectId")Long newObjectId, @Param("newObjectType")Integer newObjectType, @Param("dataType")String dataType,
                            @Param("x")Double x, @Param("y")Double y, @Param("newPosition")Integer newPosition);

    void addLedgerRelate(@Param("ledgerId")Long ledgerId, @Param("objectId")Long objectId, @Param("objectType")Integer objectType,
                                                         @Param("dataType")String dataType, @Param("x")Double x, @Param("y")Double y, @Param("newPosition")Integer newPosition);

    void removeLedgerRelate(@Param("ledgerId")Long ledgerId, @Param("objectId")Long objectId, @Param("objectType")Integer objectType);

    List<Map<String, Object>> getLedgerRelatesById(@Param("ledgerId")Long ledgerId);

    Map<String, Object> getLedgerMeterFontstyleBy(@Param("ledgerId")Long ledgerId, @Param("objectId")Long objectId, @Param("objectType")Integer objectType);

    Double getDayDataInImg(@Param("objectId")Long objectId, @Param("objectType")Integer objectType, @Param("temp")Integer temp, @Param("yesterday")String yesterday);

    Double getCurApInImg(@Param("objectId")Long objectId, @Param("objectType")Integer objectType, @Param("curTime")Date curTime);

    Map<String, Object> getCurVInImg(Map<String, Object> param);

    Map<String, Object> getCurIInImg(Map<String, Object> param);

    Double getCurEInImg(@Param("objectId")Long objectId, @Param("curTime")Date curTime);

    List<Long> getParentCompLedger(@Param("ledgerId")Long ledgerId);

    List<Map<String, Object>> getRelateLedgerMeterList(@Param("ledgerId")Long ledgerId, @Param("searchModel")Integer searchModel);

    Long getDataTypeCount(@Param("ledgerId")Long ledgerId);
    /**
     *  能源地图 相关查询 end
     */


	Map<String, Object> getLedgerThresholdValue(@Param( "ledgerId" ) Long ledgerId , @Param( "eventTypeId" )Long eventTypeId);

    /**
     *  重点数据维护 -- 列表
     */
    List<Map<String, Object>> getDataMaintainPage(Map<String, Object> queryMap);

    /**
     *  重点数据维护 -- 得到下拉de企业列表
     */
    List<Map<String, Object>> getCompLedgerList(@Param("ledgerId" ) Long ledgerId);

    /**
     *  重点数据维护 -- 根据Id查询
     */
    Map<String, Object> getDataMaintain(@Param("ledgerId" ) Long ledgerId);

    /**
     *  重点数据维护 -- 删除
     */
    void deleteMaintain(@Param("ledgerId" ) Long ledgerId);

    /**
     *  重点数据维护 -- 插入
     */
    void insertDataMaintain(Map<String, Object> info);
	
	
	/**
	 * 删除产污/治污关联关系
	 * @param meterId
	 * @return
	 */
	Integer deleteFacil(LedgerBean ledgerBean);
	
	/**
	 * 查询所选节点上级和下级所有101/106节点是否已经设置了产污/治污
	 * @param meterId
	 * @return
	 */
	List<Map<String,Object>> selectParent (@Param("ledgerId")Long ledgerId);
	
	/**
	 * 查询所选节点是产污设施的对应关系
	 * @param meterId
	 * @return
	 */
	List<Map<String,Object>> queryPollut(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 查询所选节点是治污设施的对应关系
	 * @param meterId
	 * @return
	 */
	List<Map<String,Object>> queryPollutctl(@Param("ledgerId")Long ledgerId);
	
	/**
	 *
	 * 函数功能说明  :检查同级下是否有相同的用户名称
	 * @param map
	 * @return
	 * @return  Long
	 * @throws
	 */
	Long checkdeviceCode(Map<String, Object> map);

}