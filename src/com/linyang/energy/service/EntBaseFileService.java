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
public interface EntBaseFileService {

	/**
	 * 根据Id获取一个能源管理信息
	 */
	public Map<String,Object> getAoInfo(long entId);	
    /**  
	 * 函数功能说明  :保存能源管理信息
	 */
    int saveEmInfo(Map<String,Object> aoInfo);
    
    /**  
	 * 函数功能说明  :上传个工序0：上传 1修改2删除
	 */
    Map<String, String> uploadEnergyManage(String entId,String typeId);
    
    /**
	 * 根据Id获取一个企业认证信息
	 */
	public Map<String,Object> getEntIdentInfo(long entId);	
    /**  
	 * 函数功能说明  :保存企业认证信息
	 */
    int saveEntIdentInfo(Map<String,Object> aoInfo);
    /**  
	 * 函数功能说明  :上传个工序0：上传 1修改2删除
	 */
    Map<String, String> uploadEntIdent(String entId,String typeId);
    
    /**
	 * 根据Id获取一个水电气户号
	 */
	public Map<String,Object> getWaterPowerGasInfo(long entId,int accType);	
    /**  
	 * 函数功能说明  :保存水电气户号
	 */
    int saveWaterPowerGasInfo(Map<String,Object> aoInfo);
    /**  
	 * 函数功能说明  :上传个工序0：上传 1修改2删除
	 */
    Map<String, String> uploadWaterPowerGas(String entId,String accType,String typeId);
    
    /**
	 * 分页查询生产工序，支持查询
	 * @param page 分页对象
	 * @param queryCondition 查询条件
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getRunYearPageList(Page page,Map<String,Object> queryCondition);
	
	/**
	 * 根据Id获取一个年度经营信息
	 */
	public Map<String,Object> getRunYearInfo(Map<String, Object> queryCondition);	
    /**  
	 * 函数功能说明  :新增一个年度经营信息
	 */
    int saveRunYearInfo(Map<String,Object> proc);
    /**  
	 * 函数功能说明  :删除一个年度经营信息
	 */
    int deleteRunYearInfo(Map<String, Object> queryCondition);
    /**  
	 * 函数功能说明  :上传个年度经营信息0：上传 1修改2删除
	 */
    Map<String, String> uploadRunYearInfo(String entId,String year,String typeId);
}
