package com.linyang.energy.service.impl;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.DateUtil;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.demanddeclare.DemandDeclareMapper;
import com.linyang.energy.mapping.energysavinganalysis.CostMapper;
import com.linyang.energy.model.DemandRecBean;
import com.linyang.energy.service.DemandDeclareService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * @author guosen
 * @date 2014-12-22
 * @Modified By DINGYANG
 * @Description 需量申报
 * @Version 6294
 */
@Service
public class DemandDeclareServiceImpl implements DemandDeclareService {

    @Autowired
    private DemandDeclareMapper demandDeclareMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private CostMapper costMapper;

    @Override
    public boolean update(Long recId, Double declareValue) {
        boolean isSuccess = false;
        DemandRecBean demandRecBean = null;
        if (recId != null && recId != 0l) {
            demandRecBean = this.demandDeclareMapper.getBeanById(recId);
        }
        if (demandRecBean != null && declareValue != null && declareValue != 0d) {
            demandRecBean.setDeclareValue(declareValue);
            this.demandDeclareMapper.update(demandRecBean);
            isSuccess = true;
        }
        return isSuccess;
    }

    @Override
    public Map<String, Object> insert(Map<String, Object> param) {
        Map<String, Object> resultMap = new HashMap<String, Object>(2);
        String retMsg = "保存失败";
        boolean isSuccess = false;
        Long meterId = null;
        Integer declareType = null;
        Double declareValue = null;
        Date beginTime = null;
        Date endTime = null;
        if (param != null) {
            try {
                meterId = Long.valueOf(String.valueOf(param.get("meterId")));
                declareType = Integer.valueOf(String.valueOf(param.get("declareType")));
                declareValue = Double.valueOf(String.valueOf(param.get("declareValue")));
                beginTime = DateUtil.convertStrToDate(String.valueOf(param.get("beginTime")));
                endTime = DateUtil.convertStrToDate(String.valueOf(param.get("endTime")));
            } catch (ParseException e) {
                Log.info("insertDemandDeclare error ParseException");
            }
        }

        int betweenMonthes = com.linyang.energy.utils.DateUtil.getBetweenMonthes(beginTime, endTime);
        DemandRecBean demandRecBean = new DemandRecBean();
        demandRecBean.setMeterId(meterId);
        demandRecBean.setDeclareType(declareType);
        demandRecBean.setDeclareValue(declareValue);
        demandRecBean.setDeclareTime(new Date());
        Date firstDate = DateUtil.getMonthFirstDay(beginTime);// 起始月的第一天
        boolean isRepeat = false;
        for (int i = 0; i <= betweenMonthes; i++) {
            demandRecBean.setBeginTime(DateUtil.getCalculateMonth(firstDate, i));
            // 判断是否重复---专变+月份必须唯一
            Integer count = this.demandDeclareMapper.getBeanByTimeAndMeterId(demandRecBean);
            if (count != null && count > 0) {
                retMsg = "专变+月份不能重复";
                isRepeat = true;
                break;
            }
        }
        if (!isRepeat) {// 无重复
            for (int i = 0; i <= betweenMonthes; i++) {
                demandRecBean.setBeginTime(DateUtil.getCalculateMonth(firstDate, i));
                this.demandDeclareMapper.insert(demandRecBean);
            }
            retMsg = "保存成功";
            isSuccess = true;
        }
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("retMsg", retMsg);
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> getDecalrePageData(Page page, Map<String, Object> mapQuery) {
        if(mapQuery != null)
            mapQuery.put(Dialect.pageNameField, page);
        List<Map<String, Object>> list = null;
        int treeType = 0;
        if (mapQuery != null && mapQuery.get("treeType") != null) {
            treeType = Integer.valueOf(String.valueOf(mapQuery.get("treeType")));
        }
        if (treeType == 1) {
            list = demandDeclareMapper.getEmoDecalrePageData(mapQuery);
        }
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }

    @Override
    public Map<String, Object> getEmoDecalreData(long ledgerId) {
        Date beginTime = DateUtil.getLastMonthFirstDay();
        Date lastTime = DateUtil.getLastDayOfMonth(beginTime);

        Map<String, Object> emoDecalreData = null;
        if (ledgerId != 0l) {
            emoDecalreData = demandDeclareMapper.getEmoDecalreData(ledgerId, beginTime);
        }

        if (emoDecalreData == null)
            return emoDecalreData;

        // 以下是单采集点获取日冻结表数据
        if (demandDeclareMapper.getPointNum(ledgerId) == 1) {
            Long meterId = demandDeclareMapper.getMeterIdByLedgerId(ledgerId);
            Map<String, Object> tempLastMap = new HashMap<String, Object>();
            tempLastMap.put("pointId", meterId);
            tempLastMap.put("beginTime", beginTime);
            tempLastMap.put("endTime", lastTime);
            tempLastMap.put("treeType", 2);
            List<Map<String, Object>> monthMaxDemand = costMapper.getMonthMaxDemand(tempLastMap);
            boolean stop = true;
            while(stop){
                if(monthMaxDemand == null || monthMaxDemand.size()<=0){
                    beginTime = DateUtil.getLastMonthDate(beginTime);
                    lastTime = DateUtil.getLastMonthDate(lastTime);
                    tempLastMap.put("beginTime", beginTime);
                    tempLastMap.put("endTime", lastTime);
                    monthMaxDemand = costMapper.getMonthMaxDemand(tempLastMap);
                }
                if(monthMaxDemand != null && monthMaxDemand.size()>0)
                    stop = false;
            }
            emoDecalreData.put("MAXFAD1", monthMaxDemand.get(0).get("MAXFAD"));
        }

        Map<String, Object> queryMap = new HashMap<String, Object>(4);
        queryMap.put("treeType", 1);
        queryMap.put("pointId", ledgerId);
        queryMap.put("beginTime", beginTime);
        queryMap.put("endTime", lastTime);


        // 查询上月最大需量
        Map<String, Object> lsatDeclare = demandDeclareMapper.getLsatDeclare(ledgerId, beginTime, 0);
        // 如果上月最大需量为空,则查询历史最后一次申报的最大需量
        if (lsatDeclare == null) {
            lsatDeclare = demandDeclareMapper.getLsatDeclare(ledgerId, beginTime, 1);
        }

        if (lsatDeclare != null) {
            emoDecalreData.put("MAXFAD1", lsatDeclare.get("MAXFAD"));
        }
        return emoDecalreData;
    }

    @Override
    public boolean delete(Date declareTime) {
        boolean falg = false;
        if (declareTime != null) {
            falg = this.demandDeclareMapper.deleteBoByDeclareTime(declareTime) > 0;
        }
        return falg;
    }


    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public Map<String, Object> insertDemandDeclare(List<Map<String, Object>> paramList) {
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String retMsg = "保存失败";
        boolean isSuccess = false;
        try {
            DemandDeclareMapper declareMapper = session.getMapper(DemandDeclareMapper.class);

            if (paramList != null && paramList.size() > 0) {
                for (Map<String, Object> param : paramList) {
                    Long meterId = Long.parseLong(String.valueOf(param.get("meterId")));
                    Integer declareType = Integer.parseInt(String.valueOf(param.get("declareType")));
                    Double declareValue = Double.parseDouble(String.valueOf(param.get("declareValue")));
                    Date beginTime = DateUtil.convertStrToDate(String.valueOf(param.get("beginTime")),DateUtil.DEFAULT_SIMPLE_PATTERN);
                    StringBuilder bt = new StringBuilder().append(( param.get("beginTime").toString()).substring(0, 4))
                            .append(( param.get("beginTime").toString()).substring(5, 7));
                    StringBuilder et = new StringBuilder().append(((String) param.get("endTime")).substring(0, 4))
                            .append((param.get("endTime").toString()).substring(5, 7));
                    int calBetweenTwoMonth = com.linyang.energy.utils.DateUtil.calBetweenTwoMonth(et.toString(),
                            bt.toString());
                    for (int i = 0; i <= calBetweenTwoMonth; i++) {
                        Date monthDate = com.linyang.energy.utils.DateUtil.getMonthDate(beginTime, i);
                        DemandRecBean demandRecBean = new DemandRecBean();
                        demandRecBean.setMeterId(meterId);
                        demandRecBean.setDeclareType(declareType);
                        demandRecBean.setDeclareValue(declareValue); // 申报值
                        demandRecBean.setDeclareTime(new Date());
                        Date firstDate = DateUtil.getMonthFirstDay(monthDate);// 起始月的第一天
                        demandRecBean.setBeginTime(firstDate);
                        declareMapper.insert(demandRecBean);
                    }
                }
            }
            retMsg = "保存成功";
            isSuccess = true;
        } catch (NumberFormatException e) {

            Log.info("insertDemandDeclare error NumberFormatException");
        } catch (ParseException e) {

            Log.info("insertDemandDeclare error ParseException");
        } finally {
            session.commit();
            session.close();
        }
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("retMsg", retMsg);
        return resultMap;
    }

    @Override
    public Integer getEmoDecalreType(Map<String, Object> map) {
        Integer type = null;
        if (map != null) {
            type = demandDeclareMapper.getEmoDecalreType(map);
        }
        return type;
    }

    @Override
    public Integer getPointNum(long ledgerId) {
        Integer num = null;
        if (ledgerId != 0l) {
            num = demandDeclareMapper.getPointNum(ledgerId);
        }
        return num;
    }

    @Override
    public Long getMeterIdByLedgerId(long ledgerId) {
        Long meterId = null;
        if (ledgerId != 0l) {
            meterId = demandDeclareMapper.getMeterIdByLedgerId(ledgerId);
        }
        return meterId;
    }

    @Override
    public Integer getmaxFaqValue(long meterId) {
        Integer value = null;
        if (meterId != 0l) {
            value = demandDeclareMapper.getmaxFaqValue(meterId);
        }
        return value;
    }

    @Override
    public Map<String, Object> getEmoDecalreByIdAndBeginTime(Map<String, Object> map) {
        Map<String, Object> emoDecalreByIdAndBeginTime = null;
        if (map != null) {
            emoDecalreByIdAndBeginTime = demandDeclareMapper.getEmoDecalreByIdAndBeginTime(map);
        }
        return emoDecalreByIdAndBeginTime;
    }

    @Override
    public Map<String, Object> getLsatDeclare(Long pointId, Date beginTime, Integer flag) {
        if(pointId != null && pointId != 0l && beginTime != null && flag != null && flag != 0l)
            return demandDeclareMapper.getLsatDeclare(pointId, beginTime, flag);
        return null;
    }


    @Override
    public List<Map<String, Object>> getEveryLsatDeclare(Map<String, Object> map) {
        if (map != null)
            return demandDeclareMapper.getEveryLsatDeclare(map);
        return null;
    }
}

