package com.linyang.energy.mapping.yunNan;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 11:33 2020/5/19
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public interface InstitutionsMapper {
	
	List<Map<String, Object>> queryMsiData();
	
	List<Map<String,Object>> queryAoData();
	
	List<Map<String, Object>> queryInstitutionsPageList(Map<String,Object> params);
	
	Integer saveData(Map<String,Object> params);
	
	Integer modifyData(Map<String,Object> params);
	
	Integer modifyMeasuringAo(Map<String,Object> params);
	
	Map<String,Object> queryDataById(@Param( "msiId" )Long msiId,@Param( "aoId" )Long aoId);
	
	Integer deleteInsData(@Param( "msiId" )Long msiId,@Param( "aoId" )Long aoId);
	
	Map<String,Object> queryUploadData(@Param( "msiId" )Long msiId,@Param( "aoId" )Long aoId);
	
	Integer updateDataIndexById(Map<String,Object> params);
}
