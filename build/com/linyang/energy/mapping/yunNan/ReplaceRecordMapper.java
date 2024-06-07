package com.linyang.energy.mapping.yunNan;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:53 2020/5/20
 * @ Description：计量器具更换记录管理
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface ReplaceRecordMapper {
	
	List<Map<String,Object>> queryRecordPageList(Map<String,Object> params);
	
	Integer saveData(Map<String,Object> params);
	
	Integer modifyData(Map<String,Object> params);
	
	Map<String,Object> queryDataById(@Param( "oldMsiId" ) Long oldMsiId);
	
	Integer deleteRecordData(@Param( "oldMsiId" ) Long oldMsiId);
	
	Integer updateDataIndexById(Map<String,Object> updateMap);
	
	Map<String,Object> queryUploadData(@Param( "oldMsiId" ) Long oldMsiId);
	
	
	
}
