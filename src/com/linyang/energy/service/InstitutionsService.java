package com.linyang.energy.service;

import com.linyang.common.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 11:25 2020/5/19
 * @ Description：计量器具检定/校准管理
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface InstitutionsService {
	
	/**
	 * 查询计量器具信息
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/19 14:37
	 */
	List<Map<String, Object>> queryMsiData();
	
	/**
	 * 查询检测单位信息
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/19 14:37
	 */
	List<Map<String,Object>> queryAoData();
	
	/**
	 * 查询检定校准分页信息
	 * @author catkins.
	 * @param page
	 * @param params
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/19 14:38
	 */
	List<Map<String, Object>> queryInstitutionsPageList(Page page,Map<String,Object> params);
	
	/**
	 * 新增和修改方法
	 * @author catkins.
	 * @param params
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/19 14:39
	 */
	Integer saveAndModify(Map<String,Object> params);
	
	/**
	 * 根据id查询检定校准记录
	 * @param msiId
	 * @param aoId
	 * @return
	 */
	Map<String,Object> queryDataById(Long msiId,Long aoId);
	
	/**
	 * 删除检定校准记录
	 * @param msiId
	 * @param aoId
	 * @return
	 */
	Boolean deleteInsData(Long msiId,Long aoId);
	
	/**
	 * 上传数据
	 * @param params
	 * @return
	 */
	Map<String,Object> uploadData(Map<String,Object> params);
	
}
