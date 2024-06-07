package com.linyang.energy.mapping.yunNan;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:58 2020/5/25
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface EnsavingManageMapper {
	
	List<Map<String, Object>> queryEnsavingPageList( Map<String,Object> params );
	
	Integer saveData(Map<String,Object> params);
	
	Integer modifyData(Map<String,Object> params);
	
	Map<String,Object> queryDataById(Long ensId);
	
	Integer deleteDataById(Long ensId);
	
	Integer updateDataIndexById(Map<String,Object> params);
	
}
