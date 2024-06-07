package com.linyang.energy.mapping.yunNan;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 20-5-12.
 */
public interface CollectConfigManageMapper {

	/**  
	 * 函数功能说明  :生产工序列表
	 * @param page
	 * @param queryCondition
	 * @return      
	 * @return  List<Map<String,Object>> 
	 */
    List<Map<String,Object>> getProdProcPageData(Map<String, Object> queryCondition);
    /**  
	 * 函数功能说明  :生产工序列表
	 * @param queryCondition
	 * @return      
	 * @return  List<Map<String,Object>> 
	 */
    List<Map<String,Object>> getAllProdProcData(Map<String, Object> queryCondition);
    /**  
	 * 函数功能说明  :生产工序单元列表
	 * @param queryCondition
	 * @return      
	 * @return  List<Map<String,Object>> 
	 */
    List<Map<String,Object>> getAllProcUnitData(Map<String, Object> queryCondition);
    
    /**
     * 获取工序编码静态数据
     * @return
     */
    List<Map<String,Object>> getProdPCode(@Param( "entId" )long entId);
    /**
     * 获取工序单元编码静态数据
     * @return
     */
    List<Map<String,Object>> getProdPUnitCode(@Param( "entId" )long entId);
    /** 获取采集数据类型静态数据
    * @return
    */
   List<Map<String,Object>> getDataType();
   /** 获取采集数据用途静态数据
    * @return
    */
   List<Map<String,Object>> getDataUsage();
   
   /** 获取数据采集来源静态数据
    * @return
    */
   List<Map<String,Object>> getDataSrc();
    /**
     * 获取设备分类静态数据
     * @return
     */
    List<Map<String,Object>> getEqpType();
    /**
              * 获取能源分类静态数据
     * @return
     */
    List<Map<String,Object>> getEnType();
    /**
     * 获取能源分类静态数据
     * @return
	*/
    List<Map<String,Object>> getWPED();
    /**
           * 获取能源分类静态数据
     * @return
	*/
    List<Map<String,Object>> getNonenprod();
    
    /**
     	* 获取能源分类静态数据
     * @return
     */
    List<Map<String,Object>> getEEI();
    
    /**
     	* 获取能源分类静态数据
     * @return
     */
    List<Map<String,Object>> getOtherDataCode();
    
    /**  
	 * 函数功能说明  :获取单个工序
	 */
    Map<String,Object> getProcById(long procId);
    
    /**  
   	 * 函数功能说明  :获取依赖工序的子元素数量
   	 */
     int getIfCanDeleteProc(long procId);
    
    /**  
	 * 函数功能说明  :新增一个工序
	 */
    int addProdProc(Map<String,Object> proc);
    /**  
	 * 函数功能说明  :修改一个工序
	 */
    int updateProdProc(Map<String,Object> proc);
    /**  
   	 * 函数功能说明  :修改一个工序
   	 */
    int updateProdProcStatus(Map<String,Object> proc);
    /**  
	 * 函数功能说明  :删除一个工序
	 */
    int deleteProdProc(long procId);
    
    
    /**  
	 * 函数功能说明  :生产工序单元列表
	 */
    List<Map<String,Object>> getProcUnitPageData(Map<String, Object> queryCondition);
    /**  
	 * 函数功能说明  :获取单个工序单元
	 */
    Map<String,Object> getProcUnitById(long procId);
    /**  
   	 * 函数功能说明  :获取依赖工序的子元素数量
   	 */
     int getPUnitSonCounts(long unitId);
    /**  
	 * 函数功能说明  :新增一个工序单元
	 */
    int addProcUnit(Map<String,Object> proc);
    /**  
	 * 函数功能说明  :修改一个工序单元
	 */
    int updateProcUnit(Map<String,Object> proc);
    /**  
   	 * 函数功能说明  :修改一个工序单元
   	 */
       int updateProcUnitStatus(Map<String,Object> proc);
    /**  
	 * 函数功能说明  :删除一个工序单元
	 */
    int deleteProcUnit(long procId);
    
    /**  
	 * 函数功能说明  :耗能设备单元列表
	 */
    List<Map<String,Object>> getConDeivcePageData(Map<String, Object> queryCondition);
    /**  
   	 * 函数功能说明  :生产工序单元列表
   	 * @param queryCondition
   	 * @return      
   	 * @return  List<Map<String,Object>> 
   	 */
       List<Map<String,Object>> getAllDeviceData(Map<String, Object> queryCondition);
    /**  
	 * 函数功能说明  :获取单个耗能设备
	 */
    Map<String,Object> getConDeivceById(long deviceId);
    
    /**  
   	 * 函数功能说明  :获取设备的子元素数量
   	 */
     int getDeviceSonCounts(long deviceId);
    /**  
	 * 函数功能说明  :新增一个耗能设备
	 */
    int addConDeivce(Map<String,Object> device);
    /**  
	 * 函数功能说明  :修改一个耗能设备
	 */
    int updateConDeivce(Map<String,Object> device);
    /**  
   	 * 函数功能说明  :修改一个耗能设备
   	 */
       int updateConDeivceStatus(Map<String,Object> device);
    /**  
	 * 函数功能说明  :删除一个耗能设备
	 */
    int deleteConDeivce(long deviceId);
    
    
    /**  
	 * 函数功能说明  :采集数据配置项
	 */
    List<Map<String,Object>> getDataConfigPageData(Map<String, Object> queryCondition);
    
    /**  
   	 * 函数功能说明  :采集数据配置项
   	 */
       List<Map<String,Object>> getDataConfigData(Map<String, Object> queryCondition);
    
    /**  
	 * 函数功能说明  :采集数据配置项
	 */
    Map<String,Object> getDataConfigById(long dataId);
    /**  
	 * 函数功能说明  :根据企业id获取下面的测量点
	 */
    List<Map<String,Object>> getMpedListByLedgerID(long entId);
    /**  
	 * 函数功能说明  :新增一个采集数据配置项
	 */
    int addDataConfig(Map<String,Object> data);
    /**  
	 * 函数功能说明  :修改一个采集数据配置项
	 */
    int updateDataConfig(Map<String,Object> data);
    /**  
   	 * 函数功能说明  :修改一个采集数据配置项
   	 */
    int updateDataConfigStatus(Map<String,Object> data);
    /**  
	 * 函数功能说明  :删除一个数据配置项
	 */
    int deleteDataConfig(long dataId);
}
