package com.linyang.energy.mapping.authmanager;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.OptLogBean;

public interface OptLogBeanMapper {
    int deleteByPrimaryKey(Long optlogId);

    int insert(OptLogBean record);

    int insertSelective(OptLogBean record);

    OptLogBean selectByPrimaryKey(Long optlogId);

    int updateByPrimaryKeySelective(OptLogBean record);

    int updateByPrimaryKey(OptLogBean record);
	/**  
	 * 函数功能说明  :分页查询日志列表
	 * @param page 分页对象
	 * @param map 参数集合
	 * @return  List<Map<String,Object>>      
	 */
    List<Map<String,Object>>  getLogPageData(Map<String,Object> queryCondition);
	/**
	 * 函数功能说明  :写入日志
	 * @param record
	 * @return  int
	 */
	int writeLog(OptLogBean record);
}