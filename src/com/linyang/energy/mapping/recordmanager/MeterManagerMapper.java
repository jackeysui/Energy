package com.linyang.energy.mapping.recordmanager;

import com.linyang.energy.model.LedgerBean;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.LineLossMeterBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.model.RoleBean;

/**
 * 
 * @类功能说明： 采集点管理mapper
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2013-12-12 下午01:32:37  
 * @版本：V1.0
 */
public interface MeterManagerMapper {
	/**
	 * 
	 * 函数功能说明  : 分页查询采集点信息
	 * @param page 页数
	 * @param meter 采集点相关信息
	 * @return      
	 * @return  List<MeterBean>    采集点列表 
	 * @throws
	 */
	List<Map<String,Object>> getMeterPageList(Map<String,Object> map);

	List<Map<String,Object>> getVirtualMeterPageList(Map<String,Object> map);


    /**
     * 查询某一虚拟采集点包含的真实采集点
     */
	String getVirtualContains(@Param("meterId")Long meterId, @Param("idFlag")Integer idFlag);
	List<String> getVirtualContainss(@Param("meterId")Long meterId, @Param("idFlag")Integer idFlag);

    List<Map<String, Object>> getVirtualRelations(@Param("meterId") Long meterId);

	/**
	 * 
	 * 函数功能说明  :删除采集点信息
	 * @param meterId      
	 * @return  void     
	 * @throws
	 */
	public void deleteMeterData(@Param("meterIds")List<Long> meterIds);

    /**
     *
     * 函数功能说明:删除虚拟采集点
     * @param meterId
     * @return  void
     * @throws
     */
	public void deleteVirtualMeter(@Param("meterIds")List<Long> meterIds);
	public void deleteVirtualRelation(@Param("meterIds")List<Long> meterIds);
	
	/**
	 * 
	 * 函数功能说明  :获取所有的终端信息
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<String,Object>> getTerminalData();


    /**
     *
     * 函数功能说明  :得到和达水表信息
     * @return
     * @return  List<Map<String,Object>>
     * @throws
     */
	public List<Map<String,Object>> getHdWaterMeterData();

	/**
	 * 
	 * 函数功能说明  :查验采集点名称正确与否
	 * @param map
	 * @return      
	 * @return  long     
	 * @throws
	 */
	public long checkMeterName(Map<String, Object> map);
	
	/**
	 * 
	 * 函数功能说明  :查询当前用户下的所有分户
	 * @param ledgerId
	 * @return      
	 * @return  List<LedgerBean>     
	 * @throws
	 */
	public List<Map<String,Object>> getLedgerData(@Param("ledgerId")long ledgerId);
	
	/**
	 * 
	 * 函数功能说明  :查询某终端下的所有测量点
	 * @param map
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<String,Object>> getMpedDataByTerId(Map<String, Object> map);
	
	/**
	 * 
	 * 函数功能说明  :查询某终端下的所有已经关联的测量点
	 * @param map
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<String,Object>> getLinkedMpedData(Map<String,Object> map);
	
	/**
	 * 
	 * 函数功能说明  :查询设备集信息
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<String,Object>> getDeviceData();
	
	/**
	 * 
	 * 函数功能说明  :新增采集点信息
	 * @param meter
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public void insertMeterInfo(MeterBean meter);
	
	/**
	 * 
	 * 函数功能说明  :新增采集点配置
	 * @param map      
	 * @return  void     
	 * @throws
	 */
	public void insertThresholdInfo(Map<String, Object> map);
	
	/**
	 * 
	 * 函数功能说明  :更新采集点信息
	 * @param meter      
	 * @return  void     
	 * @throws
	 */
	public void updateMeterInfo(MeterBean meter);

	public void updateRelationMeterName(MeterBean meter);
	
	/**
	 * 
	 * 函数功能说明  :更新采集点配置信息
	 * @param map      
	 * @return  void     
	 * @throws
	 */
	public void updateByPrimaryKeySelective(Map<String, Object> map);
	
	/**
	 * 
	 * 函数功能说明  :根据Id得到相应的采集点信息
	 * @param meterId
	 * @return      
	 * @return  MeterBean     
	 * @throws
	 */
	public MeterBean getMeterDataByPrimaryKey(long meterId);
	
	/**
	 * 
	 * 函数功能说明  :获取采集点配置信息
	 * @param meterId
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	public List<Map<String,Object>> getMeterInfoById(long meterId);
	
	/**
	 * 
	 * 函数功能说明  :删除采集点配置信息
	 * @param map      
	 * @return  void     
	 * @throws
	 */
	public void deleteMeterDetail(Map<String,Object> map);
	
	/**
	 * 
	 * 函数功能说明  :删除采集点所有电相关的额定电压等的配置
	 * @param meterId      
	 * @return  void     
	 * @throws
	 */
	public void deleteDetailData(Long meterId);
	
	
	/**
	 * 
	 * 函数功能说明  :新增分户下的采集点的数量
	 * @param map      
	 * @return  void     
	 * @throws
	 */
	public void addMeterNum(@Param("ledgerId")long ledgerId);
	/**
	 * 
	 * 函数功能说明  :减少分户下的采集点的数量
	 * @param map      
	 * @return  void     
	 * @throws
	 */
	public void delMeterNum(@Param("ledgerId")long ledgerId);
	
    
    /**
     * 根据条件得到计量点列表
     * @author guosen
     * @data 2014-8-13
     * @param param
     * @return
     */
    List<MeterBean> queryMeterPageList(Map<String, Object> param);
    
    /**
     * 根据条件得到计量点列表(不分页)
     * @author guosen
     * @data 2014-8-13
     * @param param
     * @return
     */
    List<MeterBean> queryMeterList(Map<String, Object> param);
	
	/**
	 *@函数功能说明 : 根据id获取测量点信息
	 *
	 *@author chengq
	 *@date 2014-8-13
	 *@param
	 *@return
	 */
	public MeterBean getMpedInfoById(@Param("mpedId")long mpedId);
	
	/**
	 * @函数功能说明 : 得到该计量点的接线方式
	 * @author guosen
	 * @param meterId
	 * @return 1,三相三线;2,三相四线;3:单相表
	 */
	Integer getCommModeByMeterId(Long meterId);
	/**
	 * @函数功能说明 : 根据电表ID得到阈值
	 * @author yaojiawei
	 * @param meterId
	 * @return String
	 */
	public String getThresholdById(@Param("meterId")Long meterId);
	
	/**
	 * @函数功能说明 : 根据电表ID和阈值类型得到阈值信息
	 * @author xuesai
	 * @param meterId,thresholdId
	 * @return String
	 */
	public Map<String, Object> getMeterThresholdInfo(@Param("meterId")Long meterId,@Param("thresholdId")Long thresholdId);
	
	//add by qwt 2015-6-4
	//add for 添加获取全部电表信息函数
	/**
	 * 获取全部电表信息
	 * @author qwt
	 * @param 
	 * @return
	 */
	public List<MeterBean> getAllMeter();
	//end
	
	/**
	 * 得到计算线损需要的测量点
	 */
	public List<Map<String, Object>> getLineLossMeterList(@Param("ledgerId")Long ledgerId,@Param("meterId")Long meterId);

	/**
	 * 得到计量点级别
	 * @param lineLoss
	 * @return
	 */
	Integer getMeterLevel(@Param("meterId")Long lineLoss);

	/**
	 * 线损相关计量点信息表
	 * @param lineLossMeter
	 */
	void addLineLossMeter(LineLossMeterBean lineLossMeter);

	/**
	 * 得到线损相关计量点信息表
	 * @param meterId
	 * @return
	 */
	LineLossMeterBean getLineLossMeter(@Param("meterId")long meterId);
	
	/**
	 * 删除线损相关计量点信息表
	 * @param meterId
	 */
	void deleteLineLossMeter(@Param("meterId")long meterId);

	/**
	 * 得到分户接线方式
	 * @param ledgerId
	 * @return
	 */
	List<Integer> getCommModeByLedgerId(@Param("ledgerId") Long ledgerId);


    /**
     * 根据ledgerId得到该分户“计算模型”中的计量点列表
     */
    public List<MeterBean> getMeterListByLedgerId(@Param("ledgerId") Long ledgerId);

    /**
     * 根据ledgerId得到该分户需要显示的计量点列表
     */
    public List<MeterBean> getShowMeterByLedgerId(@Param("ledgerId") Long ledgerId);

    /**
     * 根据父ledgerId得到分户列表
     */
    public List<Long> getLedgerIdsByParent(@Param("ledgerId") Long ledgerId);

    /**
     * 根据ledgerId、meterId获取t_ledger_meter表数据
     * */
    List<Map<String, Object>> getLedgerMeter(@Param("ledgerId")Long ledgerId, @Param("meterId")Long meterId);

    /**
     * 根据ledgerId、relationId获取t_ledger_relation表数据
     * */
    List<Map<String, Object>> getLedgerRelation(@Param("ledgerId")Long ledgerId, @Param("relationType")Integer relationType, @Param("relationId")Long relationId);

    /***
     * 删除T_LEDGER_METER表的关联数据
     * @param meterId
     */
	void deleteLedgerMeter(@Param("meterId") Long meterId);

	/**
	 * 删除T_LEDGER_RELATION表的关联数据
	 * @param meterId
	 */
	void deleteLedgerRelation(@Param("meterId")Long meterId);

	/**
	 * 删除T_LEDGER_SHOW表的关联数据
	 * @param meterId
	 */
	void deleteLedgerShow(Long meterId);

    /**
     * 根据ledgerId得到该分户配置的计算模型计量点列表
     */
    public List<MeterBean> getCalcMeterByLedgerId(@Param("ledgerId") Long ledgerId);
    
    /**
     * 根据meterId获取父级meterId
     * @param meterId
     * @return
     */
    Long getMeterParentByMeterId(@Param("meterId") Long meterId);


    /**
     * 插虚拟采集点(t_meter)
     */
    void insertVirtualMeter(@Param("meterId") Long meterId, @Param("meterName") String meterName, @Param("meterType") Integer meterType,
                            @Param("ledgerId") Long ledgerId);

    void updateVirtualMeter(@Param("meterId") Long meterId, @Param("meterName") String meterName, @Param("meterType") Integer meterType,
                            @Param("ledgerId") Long ledgerId);

    /**
     * 插虚拟采集点关联关系(T_VIRTUAL_METER_RELATION)
     */
    void insertVirtualMeterRelation(@Param("meterId") Long meterId, @Param("relationId") Long relationId);

    void deleteVirtualMeterRelation(@Param("meterId") Long meterId);

    /**
     * 判断某采集点是否包含在其它虚拟采集点中
     */
    int getVirtualExistMeterNum(@Param("meterId") Long meterId, @Param("relationId") Long relationId);
    /**
	 * 根据查询条件，查询是否有重复提交的rolebean
	 * */
	List<MeterBean> getFilteredMeter(Map<String, Object> queryMap);
	
	/**
	 * 依据能管对象Id，查询测量点信息
	 * 从 T_LEDGER_SHOW
	 * @param ledgerId
	 * @return
	 */
	List<Long> getMeterInfo(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 依据能管对象Id，查询测量点信息
	 * 从 T_LEDGER_RELATION  类型为测量点
	 * @param ledgerId
	 * @return
	 */
	List<Long> getMeterInfo1(@Param("ledgerId")Long ledgerId);
	
	
	/**
	 * 依据能管对象Id，查询测量点信息
	 * 从 T_LEDGER_RELATION  类型为总加组
	 * @param ledgerId
	 * @return
	 */
	List<Long> getMeterInfo2(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 依据能管对象Id，查询根id
	 * 从 T_LEDGER
	 * @param ledgerId
	 * @return
	 */
	Long getRootIdByLegerId(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 依据测量点Id，查询根id
	 * @param meterId
	 * @return
	 */
	Long getRootIdByMeterId(@Param("meterId")Long meterId);
	
	/**
	 * 依据测量点Id，查询父测量点id集合
	 * @param meterId
	 * @return
	 */
	List<Long> getParentMeterIdsById(@Param("meterId")Long meterId);
	
	/**
	 * 依据测量点Id，查询父能管对象id集合
	 * @param meterId   T_LEDGER_SHOW
	 * @return
	 */
	List<Long> getParentLedgerIdsById(@Param("meterId")Long meterId);
	
	/**
	 * 依据测量点Id,查询测量点的根节点(关于测量点)
	 * @param meterId   
	 * @return Long
	 */
	Long getMeterParentRootIdByMeterId(@Param("meterId")Long meterId);
	/**
	 * 根据能管对象id获取所属企业id
	 * @param ledgerId
	 * @return
	 */
	Long getCompanyIdByLedgerId(@Param("ledgerId")Long ledgerId);
	/**
	 * 根据能管对象id获取需要选中的meterId
	 * @param ledgerId
	 * @return Long
	 */
	Long getCheckedMeterIdByLegerId(@Param("ledgerId")Long ledgerId);
	/**
	 * 根据能管对象id获取关联的总加组对象的id集合
	 * @param ledgerId
	 * @return List<Long>
	 */
	List<Long> getRelationIdsByLedgerId(@Param("ledgerId")Long ledgerId);
	/**
	 * 根据能管对象id获取区域下的公司的id集合
	 * @param ledgerId
	 * @return List<Long>
	 */
	List<Long> getCompanyIdsByLedgerId(@Param("ledgerId")Long ledgerId);
	/**
	 * 根据能管对象id获取需要选中的meterId
	 * @param ledgerId
	 * @return Long
	 */
	Long  getCheckedMeterIdByLegerId2(@Param("ledgerIds")List<Long> ledgerIds);
	
	/**
	 * 加载根节点下一级节点(工程师页面加载第一个节点用)
	 * @param ledgerId
	 * @return Long
	 */
	List<Long> getChildIdsByLegerId(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 获得分户类型
	 * @param ledgerId
	 * @return int
	 */
	int getLedgerAnalyType(@Param("ledgerId")Long ledgerId);
}
