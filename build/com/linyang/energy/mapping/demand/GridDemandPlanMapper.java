package com.linyang.energy.mapping.demand;

import com.linyang.energy.model.LedgerBean;
import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.EntPeakAbility;
import com.linyang.energy.model.GridDemandPlanBean;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16-9-7.
 */
public interface GridDemandPlanMapper {

	/**
	 * 分页获取角色管理数据列表
	 * @param paraMap 页面参数集合
	 * @return
	 */
	public List<GridDemandPlanBean> getGridDemandPlanListPageData(Map<String,Object> paraMap);

    /**
               * 根据methodId得到详细信息
     * @return
     */
	GridDemandPlanBean getGridDemandPlan(@Param("planId")Long planId);
	
	 /**
	     * 根据methodId得到详细信息
	* @return
	*/
	GridDemandPlanBean getPlanLedgerConfig(@Param("planId")Long planId,@Param("ledgerId")Long ledgerId);

    /**
          * 检查响应名称是否重复
     * @return
     */
    Integer getDemandNumByName(@Param("planId")Long planId,@Param("planName")String planName);

    /**
             * 插入T_POWERGRID_DEMAND_PLAN
     * @return
     */
    int insertGridDemand(GridDemandPlanBean gridDemandPlanBean);

    /**
          * 插入T_PLAN_LEDGER_CONFIG
     * @return
     */
    int insertPlanLedgerConfig(GridDemandPlanBean gridDemandPlanBean);

    /**
              * 更新T_POWERGRID_DEMAND_PLAN
     * @return
     */
    int updateGridDemand(GridDemandPlanBean gridDemandPlanBean);
    
    /**
            * 更新T_POWERGRID_DEMAND_PLAN
     * @return
     */
    int updatePlanLedgerConfig(GridDemandPlanBean gridDemandPlanBean);
    

    /**
            * 更新方案状态
     * @return
     */
    int updatePlanStatus(@Param("planId")Long planId,@Param("planStatus")int planStatus);

    /**
                * 删除T_RESPONSE_PLAN
     * @return
     */
    void deleteGridDemand(@Param("planId")Long planId);

    /**
               *  删除T_RESPONSE_PLAN_CONFIG
     * @return
     */
    void deletePlanLedgerConfig(@Param("planId")Long planId,@Param("ledgerId")Long ledgerId);

    /**
                * 插入T_PLAN_LOAD_CONFIG
     * @return
     */
    void insertPlanLoadConfig(@Param("planId")Long planId, @Param("ledgerId")Long ledgerId, @Param("loadId")long loadId,
                        @Param("type")int type);

    /**
     	* 删除T_PLAN_LOAD_CONFIG
     * @return
	*/
    void deletePlanLoadConfig(@Param("planId")Long planId, @Param("ledgerId")Long ledgerId);

    /**
             * 获取能管对象可中断负荷
     * @return
     */
    List<Map<String, Object>> getPlanLedgerLoad(@Param("planId")Long planId,@Param("ledgerId")Long ledgerId);

    /**
	     *获取采集点可中断负荷
	* @return
	*/
	List<Map<String, Object>> getPlanMeterLoad(@Param("planId")Long planId,@Param("ledgerId")Long ledgerId);

	 /**
     *  删除T_RESPONSE_PLAN_CONFIG
     * @return
     */
	int getPlanLedgerCount(@Param("planId")Long planId);
	
	/**
	     * 插入T_LEDGER_PEAKLOAD_CONFIG
	* @return
	*/
	int saveLedgerPeakloadConfig(EntPeakAbility entPeakAbility);
	
	/**
	 * 分页获取调峰能力列表
	 * @param paraMap 页面参数集合
	 * @return
	 */
	public List<EntPeakAbility> getLedgerPeakConfigPageData(Map<String,Object> paraMap);
	
	/**
	 * 获取调峰能力
	 * @param paraMap 页面参数集合
	 * @return
	 */
	public EntPeakAbility getLedgerPeakConfig(@Param("ledgerId")Long ledgerId);
	
	 /**
 	* 删除调峰能力
	 * @return
	*/
	int deleteLedgerPeakConfig(@Param("ledgerId")Long ledgerId);
	
	
	List<LedgerBean> getLedgerList(@Param( "ledgerId" )Long ledgerId);
}
