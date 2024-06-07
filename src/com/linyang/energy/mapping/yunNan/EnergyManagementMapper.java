package com.linyang.energy.mapping.yunNan;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:44 2020/5/21
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface EnergyManagementMapper {
	
	/**
	 * 查询计量器管理列表数据
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/14 14:32
	 */
	List<Map<String,Object>> queryenManagementPageList(Map<String,Object> queryMap);
	
	Integer saveData(Map<String,Object> params);
	
	Integer modifyData(Map<String,Object> params);
	
	Map<String, Object> queryDataById(@Param( "enmId" ) Long enmId);
	
	Integer deleteRecordData(@Param( "enmId" )Long enmId);
	
	Integer saveCalData(Map<String,Object> map);
	
	Integer modifyCalData(Map<String,Object> map);
	
	Integer deleteCalData(@Param( "certId" )Long certId);
}
