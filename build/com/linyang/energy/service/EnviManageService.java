package com.linyang.energy.service;

import com.linyang.energy.model.EnviManageBean;
import com.linyang.energy.model.IndustryBean;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 18-12-11.
 */
public interface EnviManageService {

    /**
     * 查询产污/治污树
     */
    public List<IndustryBean> getPolluteControlTree(Integer type, String parentId);


    /**
     * ajax分页搜索
     */
    public List<EnviManageBean> ajaxEnviManagePageList(Map<String, Object> paramInfo);


    /**
     *  删除 产污/治污关联关系
     */
    public void deletePolluteControlRelation(Long relateId);

    /**
     *  查询是否配置了"产污"事件
     */
    public int getEventSetNumBy(Long relateId);


    /**
     *  根据 ledgerId、产污/治污ID、type获取产污/治污信息
     */
    public Map<String, Object> getPolluteControlMessage(Long ledgerId, Long id, Integer type);


    /**
     *  根据 relateId 产污/治污
     */
    public EnviManageBean getEnviManageByRelateId(Long relateId);

    /**
     *  根据 ledgerId、relateId 获取未关联的产污/治污列表
     */
    public Map<String, Object> getLedgerNotRelated(Long ledgerId, Long relateId);


    /**
     *  产污/治污关联 保存
     */
    public void insertUpdateEnviManage(EnviManageBean enviManageBean);

    public List<Map<String, Object>> getChildLedgerList(Long ledgerId);

    /**
     *  污染源治理效果分析 搜索
     */
    public Map<String, Object> ajaxEnviContolResult(Long partId, String ledgerName, String baseTime, String beginTime, String endTime, Long accountId, int isGroup, Long userLedgerId);

    public void getEleExcel(String sheetName, OutputStream output, Map<String,Object> queryMap) throws UnsupportedEncodingException;

    /**
     * 污染源"开机分析" 搜索
     */
    public Map<String, Object> ajaxEnviPolluteOpen(Long partId, String ledgerName, Integer hasControl, String beginTime, String endTime, Long accountId, int isGroup, Long userLedgerId);

    public void getOpenExcel(String sheetName, OutputStream output, Map<String,Object> queryMap) throws UnsupportedEncodingException;

    /**
     * 减排企业计划 搜索
     */
    public List<Map<String, Object>> queryReducePlanList(Map<String,Object> queryMap);

    /**
     * 减排企业列表 搜索
     */
    public List<Map<String, Object>> queryReduceList(Map<String,Object> queryMap);

    /**
     * 审核状态修改
     */
    public void updateLimitResult(Map<String,Object> queryMap);

    /**
     * 减排效果排名 搜索
     */
    public List<Map<String, Object>> queryReduceRanking(Map<String,Object> queryMap);
}
