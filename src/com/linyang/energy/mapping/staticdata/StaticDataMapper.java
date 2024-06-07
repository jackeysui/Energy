package com.linyang.energy.mapping.staticdata;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface StaticDataMapper {

	/**
	 * 初始化第一层数据
	 * @param languageType 1表示中文，2表示其他语言
	 * @return
	 */
	public List<Map<String,Object>> initFirstLevelData(@Param("languageType")int languageType);
	
	/**
	 * 初始化第二层数据
	 * @param languageType languageType 1表示中文，2表示其他语言
	 * @return
	 */
	public List<Map<String,Object>> initSecondLevelData(@Param("languageType")int languageType);
	
	/**
	 * 动态execSql
	 * @param execSql 动态执行的sql
	 * @return
	 */
	public List<Map<String,Object>> handleSecondLevelData(@Param("execSql")String execSql);
	
	/**
	 * 刷心静态数据
	 * @param dataCode 数据项
	 * @param dataType 数据类型
	 * @return
	 */
	public List<Map<String,Object>> flushStaticData(@Param("dataCode")String dataCode,@Param("dataType")String dataType);
	
}
