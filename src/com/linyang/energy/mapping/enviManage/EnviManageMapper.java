package com.linyang.energy.mapping.enviManage;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.EnviManageBean;
import com.linyang.energy.model.IndustryBean;
import org.apache.ibatis.annotations.Param;

public interface EnviManageMapper {

    /**
     * 查询产污类型树
     */
    public List<IndustryBean> queryPolluteInfo(@Param("parentId")String parentId);

    /**
     * 查询治污类型树
     */
    public List<IndustryBean> queryControlInfo(@Param("parentId")String parentId);

    /**
     * ajax分页搜索
     */
    public List<EnviManageBean> ajaxEnviManagePageList(Map<String, Object> paramInfo);

    /**
     *  删除 产污/治污关联关系
     */
    public void deletePolluteControlRelation(@Param("relateId")Long relateId);

    /**
     * 查询是否配置了"产污"事件
     */
    public int getEventSetNumBy(@Param("relateId")Long relateId);

    /**
     *  根据 ledgerId、产污/治污ID、type获取产污/治污信息
     */
    public List<Map<String, Object>> getPolluteControlMessage1(@Param("id")Long id, @Param("type")Integer type);

    public Double getPolluteControlMessage2(@Param("ledgerId")Long ledgerId, @Param("id")Long id, @Param("type")Integer type);


    /**
     * 根据relateId查询 产污/治污关联关系
     */
    public EnviManageBean getEnviManageByRelateId(@Param("relateId")Long relateId);


    /**
     * 根据ledgerId获取未关联的 产污/治污
     */
    public List<Map<String, Object>> getNotRelateEnviManage(@Param("ledgerId")Long ledgerId, @Param("type")Integer type);

    /**
     * 新增 产污/治污关联关系
     */
    public void insertEnviManage(EnviManageBean enviManageBean);

    /**
     * 修改 产污/治污关联关系
     */
    public void updateEnviManage(EnviManageBean enviManageBean);

    /**
     * 得到"非群组"用户的 下一级能管对象（机构）列表
     */
    public List<Map<String, Object>> getChildLedgerList(@Param("ledgerId")Long ledgerId);


    /**
     * 用区域去查下面的企业(PART_NAME, LEDGER_ID, LEDGER_NAME)
     */
    public List<Map<String, Object>> getLedgersOfPart(@Param("partId")Long partId, @Param("partName")String partName, @Param("ledgerName")String ledgerName);

    /**
     * 根据ledgerId获取下面一级的能管对象
     */
    public List<Map<String, Object>> getPartsOfNotGroup(@Param("ledgerId")Long ledgerId);

    /**
     * 群组用户查询ledger
     */
    public List<Map<String, Object>> getLedgersOfGroup(@Param("accountId")Long accountId, @Param("ledgerName")String ledgerName);

    /**
     * 查该企业下的产污列表
     */
    public List<Map<String, Object>> getPolluteDetailByLedgerId(@Param("ledgerId")Long ledgerId, @Param("baseTime")Date baseTime, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    /**
     * 查该企业下的产污开机列表
     */
    public List<Map<String, Object>> getPolluteOpenDetailBy(@Param("ledgerId")Long ledgerId, @Param("hasControl")Integer hasControl);

    /**
     * 得到ledger的额定功率
     */
    public Double getLedgerEpower(@Param("ledgerId")Long ledgerId);

    /**
     * 得到ledger的电量
     */
    public Double getLedgerQby(@Param("ledgerId")Long ledgerId, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

    /**
     * 得到产污、治污开机时间列表
     */
    public List<String> getPolluteControlOpenTimes(@Param("ledgerId")Long ledgerId, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate, @Param("val")Double val);

    /**
     * 减排企业计划 搜索
     */
    public List<Map<String, Object>> queryReducePlanPageList(Map<String, Object> queryMap);

    /**
     * 减排企业列表 搜索
     */
    public List<Map<String, Object>> getReduceLedgerPageList(Map<String, Object> queryMap);

    public List<Map<String, Object>> getReducePolluteList(@Param("ledgerId")Long ledgerId, @Param("beginTime")String beginTime, @Param("endTime")String endTime,
                                                          @Param("planType")Integer planType, @Param("limitResult")Integer limitResult);

    public Double getPolluteLedgerQBy(@Param("polluteId")Long polluteId, @Param("start")String start, @Param("over")String over);

    public Double getLedgerAverageQ(@Param("ledgerId")Long ledgerId);

    public void updateLimitResult(Map<String,Object> queryMap);

    /**
     * 减排效果排名 搜索
     */
    public List<Map<String, Object>> getReduceLedgerRanking(Map<String, Object> queryMap);

}