/**
 * 
 */
package com.linyang.energy.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;

/** 
 * @类功能说明： 日志管理业务层接口 
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-6 上午10:45:17  
 * @版本：V1.0  */
public interface LogService {

	/**  
	 * 函数功能说明  :分页获取日志列表
	 * @param page 分页对象
	 * @param map 参数集合
	 * @return  List<Map<String,Object>>     
	 * @throws ParseException 
	 */
	public List<Map<String,Object>>   getLogPageData(Page page, Map<String, Object> map) throws ParseException;

}
