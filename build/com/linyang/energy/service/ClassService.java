package com.linyang.energy.service;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.WorkingHourBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16-8-1.
 */
public interface ClassService {

    /**
     * 得到下拉de企业列表
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getCompLedgerList(Long ledgerId);

    /**
     * 根据ledgerId得到班制相关信息
     * @param ledgerId
     * @return
     */
    Map<String,Object> getLedgerClassMessage(Long ledgerId);

    /**
     * 根据 班制ID 查询班组
     * @param classId
     * @return
     */
    Map<String,Object> getTeamsByClassId(Long classId);

    /**
     * 新增、修改班制
     * @return
     */
    Map<String, Object> insertOrUpdateClass(Map<String, Object> classInfo);

    /**
     * 删除班制
     * @return
     */
    void deleteClass(Long classId);

    /**
     * 根据ledgerId获取生产单元配置列表
     * @return
     */
    List<Map<String, Object>> getWorkShopConfigs(Page page, Long ledgerId);

    /**
     * 根据ID查询车间详细信息
     * @return
     */
    Map<String,Object> getWorkShopDetail(Long ledgerId, Long workShopId);

    /**
     * 新增、修改 生产单元配置
     * @return
     */
    Map<String, Object> insertUpdateWorkshop(Map<String, Object> workInfo);

    /**
     * 删除 生产单元配置
     * @return
     */
    void deleteWorkshopConfig(Long workShopId);

    /**
     * 更新计算模型时，刷新T_WORKSHOP_METER
     * @return
     */
    void refreshWorkshopMeterAll(Long ledgerId);

    /**
     * 获取EMO下的DCP、EMO的Id拼成的String
     * @return
     */
    Map<String,Object> getLedgerCanChoose(Long ledgerId);

    /**
     * 产量录入 时段切割
     *
     */
    List<WorkingHourBean> getRecentClassWorkHours(Long classId);

    Map<String, Object> getProductSplitTime(Long teamId, Date beginTime, Date endTime);


    /**
     * 自定义排班计划详情
     * @param classId
     * @return
     */
    Map<String,Object> getDefineClassDetail(Long classId);

    /**
     * 自定义排班计划---保存
     * @return
     */
    Map<String, Object> insertUpdateDefineClass(Map<String, Object> workingInfo);

}
