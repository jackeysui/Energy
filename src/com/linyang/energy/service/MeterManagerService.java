package com.linyang.energy.service;

import java.util.List;
import java.util.Map;


import com.linyang.common.web.page.Page;
import com.linyang.energy.model.MeterBean;

public interface MeterManagerService {
	
	/**
	 * 
	 * 函数功能说明  :分页查询采集点信息
	 * @param page
	 * @param meter
	 * @return      
	 * @return  List<MeterBean>     
	 * @throws
	 */
	List<Map<String,Object>> getMeterPageList(Page page,Map<String, Object> queryMa);

    /**
     * 函数功能说明  :分页查询虚拟采集点信息
     */
	List<Map<String,Object>> getVirtualMeterPageList(Page page,Map<String, Object> queryMa);

    /**
     * 函数功能说明:查询某一虚拟采集点包含的真实采集点
     */
    String getVirtualContains(Long meterId, int idFlag);
    List<String> getVirtualContainss(Long meterId, int idFlag);

    /**
     * 得到某一虚拟采集点信息
     */
    Map<String, Object> getVirtualMeterData(Long meterId);

	/**
	 * 
	 * 函数功能说明  : 删除采集点信息
	 * @param meterIds      
	 * @return  void     
	 * @throws
	 */
	boolean deleteMeterData(List<Long> meterIds);

	boolean deleteVirtualMeterData(List<Long> meterIds);

	/**
	 * 
	 * 函数功能说明  :得到所有的终端信息
	 * @return      
	 * @return  List<Map<String,Object>>     
	 * @throws
	 */
	List<Map<String,Object>> getTerminalData();

    /**
     *
     * 函数功能说明  :得到和达水表信息
     * @return
     * @return  List<Map<String,Object>>
     * @throws
     */
	List<Map<String,Object>> getHdWaterMeterData();

	/**
	 * 
	 * 函数功能说明  :验证采集点名称是否存在
	 * @param map
	 * @return      
	 * @return  long     
	 * @throws
	 */
	long checkMeterName(Map<String, Object> map);
	
	/**
	 * 
	 * 函数功能说明  :查询当前用户下的所有分户
	 * @param ledgerId
	 * @return      
	 * @return  List<LedgerBean>     
	 * @throws
	 */
	public List<Map<String,Object>> getLedgerData(long ledgerId);
	
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
	 * @return  void     
	 * @throws
	 */
	public boolean insertMeterInfo(MeterBean meter);

    /**
     * 新增虚拟采集点
     */
    public Map<String, Object> insertUpdateVirtualMeter(Long meterId, String meterName, Long ledgerId, Integer meterType, String relations);
	
	/**
	 * 
	 * 函数功能说明  :更新采集点信息
	 * @param meter
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	public boolean updateMeterInfo(MeterBean meter);
	
	/**
	 * 
	 * 函数功能说明  :根据Id得到相应的采集点信息
	 * @param meterId
	 * @return      
	 * @return  MeterBean     
	 * @throws
	 */
	public MeterBean getMeterDataById(long meterId);

	/**
	 * 
	 * 根据条件得到计量点列表
	 * @author guosen
	 * @data 2013-8-13
	 * @param param
	 * @return
	 */
	List<MeterBean> queryMeterList(Map<String, Object> param);
	
	/**
	 *@函数功能说明 : 根据Id得到相应的测量点信息
	 *
	 *@author chengq
	 *@date 2014-8-13
	 *@param
	 *@return
	 */
	public MeterBean getMpedInfoById(long mpedId);

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
	public String getThresholdById(Long meterId);
	
	//add by qwt 2015-6-4
	//add for 添加获取所有电表信息函数
	/**
	 * 
	 * 获取所有电表信息
	 * @author qwt
	 * @data 2015-6-4
	 * @param param
	 * @return
	 */
	public List<MeterBean> getAllMeter();
	//end

	List<Map<String, Object>> getLineLossMeterList(Long parseLong, Long meterId);

	/**
	 * 获取分户下面的接线方式
	 * @param ledgerId
	 * @return
	 */
	Integer getCommModeByLedgerId(Long ledgerId);
	
	/**
	 * 根据meterId获取父级meterId
	 * @param meterId
	 * @return
	 */
	Long getMeterParentByMeterId(Long meterId);
	
	/**
	 * 根据能管对象id查询测量点的id集合、能管对象id集合、所在结构树根id、测量点根id
	 * @param ledgerId
	 * @return
	 */
	Map<String, Object> getMeterInfoByLedgerId(Long ledgerId,Integer selectType);
	
	/**
	 * 工程师页面加载第一个节点
	 * @param ledgerId
	 * @return Map
	 */
	Map<String, Object> elePageLoadFirstMeter(Long ledgerId);
}
