package com.linyang.energy.mapping.yunNan;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 11:19 2020/5/27
 * @ Description：用能单位产品结构信息 + 非能源产品信息
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface StructureMapper {
	
	/**
	 * 查询产品结构信息分页列表
	 * @author catkins.
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/27 13:57
	 */
	List<Map<String, Object>> queryProductPageList(Map<String, Object> queryMap);

	Integer saveData(Map<String,Object> params);
	
	Integer modifyData(Map<String,Object> params);
	
	Map<String,Object> queryDataById(Long productId);
	
	Integer delStructureData(Long productId);
	
}
