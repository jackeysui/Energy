package com.linyang.energy.mapping.yunNan;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:34 2020/5/14
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface MeasuringMapper {
	
	/**
	 * 查询计量器管理列表数据
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/14 14:32
	 */
	List<Map<String,Object>> queryMeasuringPageList(Map<String,Object> queryMap);
	
	/**
	 * 新增计量器具信息
	 * @author catkins.
	 * @param params
	 * @return java.lang.Integer
	 * @exception
	 * @date 2020/5/15 10:47
	 */
	Integer saveMeasuring(Map<String,Object> params);
	
	/**
	 * 修改计量器具信息
	 * @author catkins.
	 * @param params
	 * @return java.lang.Integer
	 * @exception
	 * @date 2020/5/15 10:47
	 */
	Integer modifyMeasuring(Map<String,Object> params);
	
	/**
	 * 根据msiId查询计量器具相关信息
	 * @param msiId
	 * @return
	 */
	Map<String,Object> queryDataById(@Param( "msiId" ) Long msiId);
	
	/**
	 * 查询检测机构模糊查询框数据
	 * @return
	 */
	List<Map<String,Object>> queryAlignOrg();
	
	/**
	 * 查询计量器具类型
	 * @return
	 */
	List<Map<String,Object>> queryMsiType();
	
	/**
	 * 根据计量器具id删除数据
	 * @param msiId
	 * @return
	 */
	Integer deleteMsiDataById(Long msiId);
	
	/**
	 * 获取上传需要的信息
	 * @param msiId
	 * @return
	 */
	Map<String,Object> queryUploadData(@Param( "msiId" ) Long msiId);
	
	/**
	 * 把得到的indexData入库
	 * @param msiId
	 * @return
	 */
	Integer updateDataIndexById(@Param( "msiId" ) Long msiId,@Param( "dataIndex" )String dataIndex,@Param( "uploadStatus" )Integer uploadStatus);
	
	/**
	 * 保存计量器校验记录数据
	 * @param params
	 * @return
	 */
	Integer saveRecordData(Map<String,Object> params);
	
	
	List<Map<String,Object>> queryDataCode();
	
	List<Map<String,Object>> queryEnType();
	
	//USAGE_STATUS
	Integer updateUsageStatus(Long dataCode);
	
	
	List<Map<String,Object>> queryLedgerByRegion(Long ledgerId);
	
}
