package com.linyang.energy.mapping.demand;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16-9-7.
 */
public interface DemandMapper {

    /**
     * 得到下拉de企业列表
     * @return
     */
    List<Map<String, Object>> getCompLedgerList(@Param("ledgerId")Long ledgerId);

    /**
     * 根据ledgerId得到响应方案列表
     * @return
     */
    List<Map<String, Object>> getLedgerDemandMethod(@Param("ledgerId")Long ledgerId);

    /**
     * 根据methodId得到详细信息
     * @return
     */
    List<Map<String, Object>> getDetailsByDemand(@Param("planId")Long planId);

    /**
     * 先检查响应名称是否重复
     * @return
     */
    Integer getDemandNumBy(@Param("planId")Long planId,@Param("ledgerId")Long ledgerId,@Param("planName")String planName);

    /**
     * 插入T_RESPONSE_PLAN
     * @return
     */
    void insertDemand(@Param("planId")Long planId,@Param("planName")String planName,@Param("ledgerId")Long ledgerId);

    /**
     * 插入T_RESPONSE_PLAN_CONFIG
     * @return
     */
    void insertDemandRelation(@Param("planId")Long planId,@Param("type")Integer type,@Param("id")Long id);

    /**
     * 更新T_RESPONSE_PLAN
     * @return
     */
    void updateDemand(@Param("planId")Long planId,@Param("planName")String planName,@Param("ledgerId")Long ledgerId);

    /**
     * 删除T_RESPONSE_PLAN
     * @return
     */
    void deleteDemand(@Param("planId")Long planId);

    /**
     * 删除T_RESPONSE_PLAN_CONFIG
     * @return
     */
    void deleteDemandRelation(@Param("planId")Long planId);

    /**
     * 计算“基线负荷”
     * @return
     */
    Double getLedgerBaseAp(@Param("ledgerId")Long ledgerId, @Param("baseList")List<Map<String, Date>> baseList);


    /**
     * 计算“响应时段平均负荷”
     * @return
     */
    Double getLedgerAverageAp(@Param("ledgerId")Long ledgerId, @Param("planId")Long planId, @Param("beginTime")Date beginTime,
                                @Param("endTime")Date endTime,@Param("timeFlag")Integer timeFlag);

    /**
     * 计算“实际负荷曲线”
     * @return
     */
    List<Map<String, Object>> getLedgerActualAp(@Param("ledgerId")Long ledgerId, @Param("planId")Long planId,
                                @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 按条件得到已保存的响应的个数
     * @return
     */
    Integer getExistDemandNumBy(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);


    /**
     * 插入t_response_incident
     * @return
     */
    void insertResponse(@Param("incidentId")Long incidentId, @Param("ledgerId")Long ledgerId, @Param("beginDate")Date beginDate,
                        @Param("endDate")Date endDate,@Param("baseValBefore")Double baseValBefore,
                        @Param("baseValAfter")Double baseValAfter,@Param("adjust")Double adjust, @Param("average")Double average,
                        @Param("cutDownVal")Double cutDownVal);

    /**
     * 根据响应历史ID查询信息
     * @return
     */
    Map<String,Object> getHistoryDetailById(@Param("incidentId")Long incidentId);

    /**
     * 根据ledgerId得到响应历史列表
     * @return
     */
    List<Map<String, Object>> getLedgerDemandHistory(@Param("ledgerId")Long ledgerId);

    /**
     * 删除历史响应
     * @return
     */
    void deleteHistoryDetail(@Param("incidentId")Long incidentId);

    /**
     * 用来判断是否配置过“厂休时段”
     * @return
     */
    Integer getRestDayNumByLedger(@Param("ledgerId")Long ledgerId);


    /**
     *  电网需求响应：根据ledgerId得到响应方案列表
     * @return
     */
    List<Map<String, Object>> getEleDemandPlanBy(@Param("ledgerId")Long ledgerId);

    /**
     *  电网需求响应：根据planId得到 电网方案信息
     * @return
     */
    Map<String, Object> getElePlanDetailBy(@Param("planId")Long planId);

    /**
     *  电网需求响应：查数据
     * @return
     */
    List<Map<String, Object>> getEleDemandData(@Param("ledgerId")Long ledgerId, @Param("planBegin")String planBegin, @Param("planEnd")String planEnd,
                                                @Param("beginTime")String beginTime, @Param("endTime")String endTime);

}
