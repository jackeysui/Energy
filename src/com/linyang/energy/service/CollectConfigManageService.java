package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.ModuleBean;
import com.linyang.energy.model.RoleBean;

/**
 *角色管理业务逻辑层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 4, 2013
 */
public interface CollectConfigManageService {

	/**
	 * 分页查询生产工序，支持查询
	 * @param page 分页对象
	 * @param queryCondition 查询条件
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getProdProcPageList(Page page,Map<String,Object> queryCondition);
	/**
	 * 不分页查询生产工序，支持查询
	 * @param page 分页对象
	 * @param queryCondition 查询条件
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getAllProdProcList(Map<String,Object> queryCondition);
	/**
	 * 根据Id获取一个生产工序
	 */
	public Map<String,Object> getProdProcById(long procId);	
	/**
	 * 获取依赖工序的子元素数量
	 */
	int getProcSonCounts(long procId);
    /**  
	 * 函数功能说明  :新增一个工序
	 */
    int addProdProc(Map<String,Object> proc);
    /**  
	 * 函数功能说明  :修改一个工序
	 */
    int updateProdProc(Map<String,Object> proc);
    /**  
	 * 函数功能说明  :删除一个工序
	 */
    int deleteProdProc(long procId);
    /**  
	 * 函数功能说明  :上传个工序0：上传 1修改2删除
	 */
    Map<String, String> uploadProdProc(String procId,String typeId);
    /**
     * 获取工序编码静态数据
     * @return
     */
    List<Map<String,Object>> getProdPCode(long entId);
    /**
     * 获取工序单元编码静态数据
     * @return
     */
    List<Map<String,Object>> getProdPUnitCode(long entId);
    /**
              * 获取采集数据类型静态数据
     * @return
     */
    List<Map<String,Object>> getDataType();
    /**
           * 获取采集数据用途静态数据
     * @return
	*/
    List<Map<String,Object>> getDataUsage();
    /**
     * 获取数据采集来源静态数据
     * @return
     */
    List<Map<String,Object>> getDataSrc();
    /**
     * 获取设备分类静态数据
     * @return
     */
    List<Map<String,Object>> getEqpType();
    /**
     * 获取能源种类静态数据
     * @return
     */
    List<Map<String,Object>> getEnType();
    
    
    
    
    /**
	 * 分页查询生产工序单元
	 */
	public List<Map<String,Object>> getProcUnitPageList(Page page,Map<String,Object> queryCondition);
	/**
	 * 不分页查询生产工序单元，支持查询
	 * @param page 分页对象
	 * @param queryCondition 查询条件
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getAllProcUnitList(Map<String,Object> queryCondition);
	
	/**
	 * 不分页查询耗能设备
	 * @param page 分页对象
	 * @param queryCondition 查询条件
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getAllDeviceList(Map<String,Object> queryCondition);
	
	/**
	 * 根据数据类型获取能源类型
	 * @param page 分页对象
	 * @param queryCondition 查询条件
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getEnTypeList(String dTypeId);
	
	/**
	 * 根据Id获取一个生产工序单元
	 */
	public Map<String,Object> getProcUnitById(long procId);	
	
	/**
	 * 获取依赖工序的子元素数量
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
	 * 函数功能说明  :删除一个工序单元
	 */
    int deleteProcUnit(long procId);
    
    /**  
   	 * 函数功能说明  :上传个工序单元0：上传 1修改2删除
   	 */
       Map<String, String> uploadProcUnit(String unitId,String typeId);
    
    /**
   	 * 分页查询耗能设备
   	 */
   	public List<Map<String,Object>> getConDevicePageList(Page page,Map<String,Object> queryCondition);
   	/**
   	 * 根据Id获取一个耗能设备
   	 */
   	public Map<String,Object> getConDeviceById(long procId);
   	/**
	 * 获取依赖工序的子元素数量
	 */
	int getDeviceSonCounts(long deviceId);
       /**  
   	 * 函数功能说明  :新增一个耗能设备
   	 */
       int addConDevice(Map<String,Object> proc);
       /**  
   	 * 函数功能说明  :修改一个耗能设备
   	 */
       int updateConDevice(Map<String,Object> proc);
       /**  
   	 * 函数功能说明  :删除一个耗能设备
   	 */
       int deleteConDevice(long procId);
       /**  
   	 * 函数功能说明  :上传个工序0：上传 1修改2删除
   	 */
       Map<String, String> uploadConsumeDevice(String deviceId,String typeId);
       
       /**
      	 * 分页查询采集数据配置项
      	 */
      	 List<Map<String,Object>> getDataConfigPageList(Page page,Map<String,Object> queryCondition);
      	/**
       	 * 分页查询采集数据配置项
       	 */
       	 List<Map<String,Object>> getDataConfigList(Map<String,Object> queryCondition);
      	/**
      	 * 根据Id获取一个采集数据配置项
      	 */
      	 Map<String,Object> getDataConfigById(long procId);	
      	 /**
      	  * 根据企业Id获取测量点
      	  * @param entId
      	  * @return
      	  */
      	List<Map<String,Object>> getMpedListByLedgerID(long entId);
          /**  
      	 * 函数功能说明  :新增一个采集数据配置项
      	 */
          int addDataConfig(Map<String,Object> proc);
          /**  
      	 * 函数功能说明  :修改一个采集数据配置项
      	 */
          int updateDataConfig(Map<String,Object> proc);
          /**  
      	 * 函数功能说明  :删除一个采集数据配置项
      	 */
          int deleteDataConfig(long procId);
          
          /**  
      	 * 函数功能说明  :上传个工序0：上传 1修改2删除
      	 */
          Map<String, String> uploadDataConfig(String dataId,String typeId);
	
}
