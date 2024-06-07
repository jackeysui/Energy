/**
 * 
 */
package com.linyang.energy.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leegern.util.DateUtil;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.authmanager.OptLogBeanMapper;
import com.linyang.energy.service.LogService;
import com.linyang.util.CommonMethod;

/** 
 * @类功能说明： 日志业务逻辑层接口实现类
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-6 上午10:47:24  
 * @版本：V1.0  */
@Service 
public class LogServiceImpl implements LogService{
	@Autowired
	private OptLogBeanMapper optLogBeanMapper;
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.LogService#getLogPageData(com.linyang.common.web.page.Page, java.util.Map)
	 */
	@Override
	public List<Map<String,Object>> getLogPageData(Page page, Map<String, Object> queryCondition) throws ParseException {
		 List<Map<String,Object>> list = null;
		if(page != null){
			Map<String,Object> map = new HashMap<String, Object>(queryCondition);
			map.put(Dialect.pageNameField, page);
			if((queryCondition.get("beginTime")!= null && CommonMethod.isNotEmpty(queryCondition.get("beginTime").toString()))  && (queryCondition.get("endTime")!= null && CommonMethod.isNotEmpty(queryCondition.get("endTime").toString())) ){
				map.put("beginTime", DateUtil.convertStrToDate(queryCondition.get("beginTime")+" 00:00:00"));
				map.put("endTime", DateUtil.convertStrToDate(queryCondition.get("endTime")+" 23:59:59"));
			}
			list = optLogBeanMapper.getLogPageData(map);
			
		}
		return list == null?new ArrayList<Map<String,Object>>():list;
	}
	

}
