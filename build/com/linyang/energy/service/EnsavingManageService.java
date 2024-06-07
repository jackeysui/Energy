package com.linyang.energy.service;

import com.linyang.common.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:57 2020/5/25
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface EnsavingManageService {
	
	List<Map<String, Object>> queryEnsavingPageList(Page page, Map<String, Object> queryMap);
	
	Integer saveAndModify(Map<String, Object> params);
	
	Map<String,Object> queryDataById(Long ensId);
	
	Boolean deleteDataById(Long ensId);
	
	/**
	 * 上传数据
	 * @param params
	 * @return
	 */
	Map<String,Object> uploadData(Map<String,Object> params);
	
}
