package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16-9-7.
 */
public interface DemandService {

    /**
     * 得到下拉de企业列表
     * @return
     */
    List<Map<String, Object>> getCompLedgerList(Long ledgerId);

    /**
     * 根据ledgerId得到响应方案信息
     * @return
     */
    Map<String,Object> getLedgerDemandMethod(Long ledgerId);

    /**
     * 根据methodId得到详细信息
     * @return
     */
    Map<String,Object> getDetailsByDemand(Long planId);

    /**
     * 新增、修改 响应方案
     * @return
     */
    Map<String, Object> insertUpdateDemand(Map<String, Object> demandInfo);

    /**
     * 删除 响应方案
     * @return
     */
    void deleteDemand(Long planId);

    /**
     * 默认时间段
     * @return
     */
    Map<String, Object> getLastWorkDayDefaultTime(Long ledgerId);

    /**
     * 响应效果
     * @return
     */
    Map<String,Object> getDemandResponse(Long ledgerId, Long planId, String dayStr, String beginTime, String endTime);

    /**
     * 保存响应事件
     * @return
     */
    void insertResponse(Long ledgerId,Date beginDate,Date endDate,Double baseValBefore,Double baseValAfter,
                        Double adjust,Double average,Double cutDownVal);

    /**
     * 根据响应历史ID查询信息
     * @return
     */
    Map<String,Object> getHistoryDetailById(Long incidentId);

    /**
     * 根据ledgerId查询响应历史信息
     * @return
     */
    Map<String,Object> getLedgerDemandHistory(Long ledgerId);

    /**
     * 删除历史响应
     * @return
     */
    void deleteHistoryDetail(Long incidentId);

    /**
     * 电网需求响应：根据ledgerId得到 电网方案s
     * @return
     */
    Map<String,Object> getEleDemandPlan(Long ledgerId);

    /**
     * 电网需求响应：根据planId得到 电网方案信息
     * @return
     */
    Map<String,Object> getElePlanDetail(Long planId);

    /**
     * 电网需求响应：响应结果
     * @return
     */
    Map<String,Object> getEleDemandResult(Long ledgerId, Long planId, String planBegin, String planEnd, String beginTime, String endTime);

}
