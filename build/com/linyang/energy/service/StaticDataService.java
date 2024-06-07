package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.common.web.common.LanguageEnum;


/**
 * 静态数据字典相关
  @description:
  @version:0.1
  @author:Cherry
  @date:2013-7-4
 */
public interface StaticDataService {
	
	/**
	 * 初始化第一层数据
	 * @return
	 */
	public List<Map<String,Object>> initFirstLevelData(LanguageEnum language);
	
	/**
	 * 初始化第二层数据
	 * @return
	 */
	public List<Map<String,Object>> initSecondLevelData(LanguageEnum language);
	
	/**
	 * 动态sql
	 * @param sql 动态sql
	 * @return
	 */
	public List<Map<String,Object>> handleSecondLevelData(String sql);
	
	/**
	 * 刷心静态数据
	 * @param dataCode 数据字典码
	 * @param dataType 数据类型
	 * @return
	 */
	public List<Map<String,Object>> flushStaticData(String dataCode,String dataType);

}
