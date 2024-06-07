package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.LedgerBean;
import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.EntPeakAbility;
import com.linyang.energy.model.GridDemandPlanBean;

/**
 * Created by Administrator on 16-9-7.
 */
public interface GridDemandPlanService {

	/**
	 * 分页获取角色管理数据列表
	 * @param paraMap 页面参数集合
	 * @return
	 */
	List<GridDemandPlanBean> getGridDemandPlanListPageData(Map<String,Object> paraMap);

	 /**
	     * 根据methodId得到详细信息
	* @return
	*/
	GridDemandPlanBean getGridDemandPlan(Long planId);
	
	
	 /**
     * 根据methodId得到详细信息
	* @return
	*/
	int updatePlanStatus(Long planId,int planStatus);
	
	/**
	   * 插入T_POWERGRID_DEMAND_PLAN
	* @return
	*/
	int insertGridDemand(GridDemandPlanBean gridDemandPlanBean);
	
	/**
	* 检查响应名称是否重复
	* @return
	*/
	Integer getDemandNumByName(Long planId,String planName);
	
	/**
	    * 更新T_POWERGRID_DEMAND_PLAN
	* @return
	*/
	int updateGridDemand(GridDemandPlanBean gridDemandPlanBean);
	
	/**
	     * 删除T_RESPONSE_PLAN
	* @return
	*/
	int deleteGridDemand(Long planId);

	/**
	* 根据methodId得到详细信息
	* @return
	*/
	GridDemandPlanBean getPlanLedgerConfig(Long planId,Long ledgerId);
	
	/**
	* 根据methodId得到详细信息
	* @return
	*/
	int getPlanLedgerCount(Long planId);

	/**
	* 插入T_PLAN_LEDGER_CONFIG
	* @return
	*/
	int mergePlanLedgerConfig(GridDemandPlanBean gridDemandPlanBean,int type);
	
	/**
	     *  删除T_RESPONSE_PLAN_CONFIG
	* @return
	*/
	void deletePlanLedgerConfig(Long planId,Long ledgerId);
	/**
	 * 预估调峰量
	 * @param gridDemandPlanBean
	 * @return
	 */
	double estimagePitchPeak(GridDemandPlanBean gridDemandPlanBean);
	
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
	List<EntPeakAbility> getLedgerPeakConfigPageData(Page page,Map<String, Object> paraMap);
	
	/**
	 * 获取调峰能力
	 * @param paraMap 页面参数集合
	 * @return
	 */
	EntPeakAbility getLedgerPeakConfig(@Param("ledgerId")Long ledgerId);
	
	 /**
		* 删除调峰能力
	 * @return
	*/
	int deleteLedgerPeakConfig(@Param("ledgerId")Long ledgerId);
	
	List<LedgerBean> getLedgerList(Long ledgerId);
	
	
}
