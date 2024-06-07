package com.linyang.energy.mapping.yunNan;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 20-5-12.
 */
public interface EntBaseFileMapper {
	
	 /**  
	* 函数功能说明  :获取能源管理信息
	*/
    Map<String,Object> getEmInfoById(long entId);
	 /**  
	* 函数功能说明  :保存能源管理信息
	*/
    int mergeEmInfo(Map<String,Object> proc);
    
    /**  
   	 * 函数功能说明  :修改一个工序
   	 */
    int updateEnergyManageStatus(Map<String,Object> proc);
    
      /**  
   	 * 函数功能说明  :获取能源管理信息
   	 */
       Map<String,Object> getEntIdentInfoById(long entId);
   	 /**  
   	  * 函数功能说明  :保存能源管理信息
   	  */
     int mergeEntIdentInfo(Map<String,Object> proc);
     
     /**  
               * 函数功能说明  :修改一个工序
     */
     int updateEntIdentStatus(Map<String,Object> proc);
       
     /**  
                * 函数功能说明  :获取水电气户号
      */
      Map<String,Object> getWaterPowerGasInfoById(@Param( "entId" )long entId,@Param( "accType" )int accType);
      /**  
                    * 函数功能说明  :保存水电气户号
      */
     int mergeWaterPowerGasInfo(Map<String,Object> proc);
     
     /**  
 	     * 函数功能说明  :修改水电气户号状态
 	 */
    int updateWPGStatus(Map<String,Object> proc);
     
     /** 函数功能说明  :年度经营列表
	 * @param page
	 * @param queryCondition
	 * @return      
	 * @return  List<Map<String,Object>> 
	 */
    List<Map<String,Object>> getRunYearPageData(Map<String, Object> queryCondition);
    
    /**  
     * 函数功能说明  :获取年度经营信息
     */
     Map<String,Object> getRunYearInfo(Map<String, Object> queryCondition);
     
     /**  
      * 函数功能说明  :保存年度经营信息
      */
     int mergeRunYearInfo(Map<String,Object> proc);
     
     /**  
 	 * 函数功能说明  :删除一个年度经营信息
 	 */
     int deleteRunYearInfo(Map<String,Object> proc);
     /**  
    	 * 函数功能说明  :修改一个企业年度信息
    	 */
     int updateRunYearInfoStatus(Map<String,Object> proc);
	
}
