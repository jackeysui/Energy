package com.linyang.energy.service;

import com.linyang.common.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:53 2020/5/20
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface ReplaceRecordService {

	List<Map<String,Object>> queryRecordPageList(Page page, Map<String,Object> params);
	
	Integer saveAndModify(Map<String,Object> params);
	
	Map<String,Object> queryDataById(Long oldMsiId);
	
	boolean deleteRecordData(Long oldMsiId);
	
	Map<String,Object> uploadData(Map<String,Object> params);

}
