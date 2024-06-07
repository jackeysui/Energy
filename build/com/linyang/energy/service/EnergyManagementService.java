package com.linyang.energy.service;

import com.linyang.common.web.page.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:43 2020/5/21
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface EnergyManagementService {
	
	/**
	 * 查询计量器管理列表数据
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/14 14:32
	 */
	List<Map<String,Object>> queryenManagementPageList(Page page, Map<String,Object> queryMap);

	Integer saveAndModify(Map<String,Object> params);
	
	Map<String, Object> queryDataById(Long enmId);
	
	boolean deleteRecordData(Long enmId);
	
	Map<String,Object> uploadData(Map<String, Object> map);
	
	Map<String,Object> uploadCompany(Map<String,Object> map);
	
	Map<String,Object> uploadMonitor(Map<String,Object> map);
	
	Integer saveAndModifyCal(Map<String,Object> params);
	
	Integer savePic(Map<String,Object> params);
	
	Boolean deleteCalData(Long enmId,Long cerId);
	
	Integer modifyCalData(Map<String,Object> map);
	
	boolean deleteFile(String fileName, HttpServletRequest request);
	
	String readThirdCompanyImg(String fileName, HttpServletRequest request);
	
	Map<String,Object> uploadCalculater(Map<String,Object> map, HttpServletRequest request);
	
	
	
	
}
