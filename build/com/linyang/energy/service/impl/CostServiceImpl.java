package com.linyang.energy.service.impl;

import com.esotericsoftware.minlog.Log;
import com.linyang.energy.controller.DemandDeclareController;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.authmanager.RateBeanMapper;
import com.linyang.energy.mapping.energysavinganalysis.CostMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.*;
import com.linyang.energy.service.CostService;
import com.linyang.energy.service.DemandDeclareService;
import com.linyang.energy.utils.*;
import com.linyang.util.CommonMethod;
import com.linyang.util.DoubleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 电费业务逻辑层接口实现类
 *
 * @description:
 * @author:gaofeng
 * @date:2014.12.18
 * @Version 6154
 */
@Service
public class CostServiceImpl implements CostService {

    @Autowired
    private CostMapper costMapper;
    @Autowired
    private MeterManagerMapper meterManagerMapper;
    @Autowired
    private LedgerManagerMapper ledgerManagerMapper;
    @Autowired
    private RateBeanMapper rateBeanMapper;
    @Autowired
    private DemandDeclareService declareService;

    private String defaultRateNameSuffer = ChartNameUtils.getChartNameBean().getRateNameSuffer();

    static final Integer ZORE = 0;

    static final Integer ONE = 1;

    static final Integer TWO = 2;

    static final Integer FIVE = 5;

    static final Integer ANALY_TYPE_102 = 102;




    //容量1

    /*
     * (non-Javadoc)
     * @see com.linyang.energy.service.CostService#getElectricityChartData(java
     * .util.Map)
     */
    @Override
    public List<ChartItemWithTime> getDayMeterChartData(Map<String, Object> queryMap) {
        List<ChartItemWithTime> list = new ArrayList<ChartItemWithTime>();
        long rateId = -1;
        if (queryMap != null && (queryMap.get("isMulti") == null || !Boolean.valueOf(String.valueOf(queryMap.get("isMulti")))) && queryMap.get("pointId") != null) {
            List<RateSectorBean> rates = costMapper.getPointRateInfo(Long.valueOf(String.valueOf(queryMap.get("pointId"))));
            Integer objType = null;
            if (queryMap.get("objType") != null)
                objType = Integer.valueOf(String.valueOf(queryMap.get("objType")));
            if (rates != null && (rates.size() > 1 || (objType == 1 && rates.size() > 0))) {// 如果是单费率或没有配费率，默认查总
                rateId = rates.get(0).getRateId();
            }
        }
        queryMap.put("rateId", rateId);

        String baseTime = null;
        if (queryMap.get("baseTime") != null)
            baseTime = String.valueOf(queryMap.get("baseTime")) + "-01";
        Date beginTime = DateUtil.convertStrToDate(baseTime, DateUtil.SHORT_PATTERN);
        Date endTime = DateUtil.getMonthLastDay(beginTime);
        queryMap.put("beginTime", beginTime);
        queryMap.put("endTime", endTime);
        long beginDate = beginTime.getTime() / 1000;
        long endDate = endTime.getTime() / 1000;
        List<Map<String, Object>> eleList = costMapper.getEleValue(queryMap);
        boolean hasRates = queryMap.get("isMulti") != null && Boolean.valueOf(String.valueOf(queryMap.get("isMulti")));
        if (CommonMethod.isCollectionNotEmpty(eleList)) {
            if (rateId != -1 || hasRates) {// 多费率
                builderRateDatas(list, beginDate, endDate, eleList, rateId);
            } else {
                buildSingleDatas(list, beginDate, endDate, eleList,
                        Short.parseShort(String.valueOf(queryMap.get("meterType"))));
            }
        }
        return ChartConditionUtils.itemDataScale2(list);
    }

    @Override
    public List<ChartItemWithTime> getDayEmoDcpChartData(Map<String, Object> queryMap) {
        List<ChartItemWithTime> result = new ArrayList<ChartItemWithTime>();
        Integer objType = null;
        Long objId = null;
        if (queryMap != null) {
            if (queryMap.get("objType") != null)
                objType = Integer.valueOf(String.valueOf(queryMap.get("objType")));
            if (queryMap.get("objId") != null)
                objId = Long.valueOf(String.valueOf(queryMap.get("objId")));
        }
        if (objType == 2) {
            queryMap.put("pointId", objId);
            result = getDayMeterChartData(queryMap);
        } else if (objType == 1) {
            LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(objId);
            List<Short> meterTypes = null;
            List<Map<String, Object>> meterList = null;
            if (ledger != null) {
                meterTypes = costMapper.getComputeMeterTypes(objId, ledger.getAnalyType());
                meterList = this.costMapper.getComputeMeter(objId, ledger.getAnalyType());
            }
            Integer queryType = null;
            if (queryMap.get("queryType") != null)
                queryType = Integer.valueOf(String.valueOf(queryMap.get("queryType")));
            if (queryType == 1) {// 电能量
                Map<String, Object> queryMapTmp = new HashMap<String, Object>();
                String strBeginTime = null;
                if (queryMap.get("baseTime") != null)
                    strBeginTime = String.valueOf(queryMap.get("baseTime")) + "-01";
                Date beginTime = DateUtil.convertStrToDate(strBeginTime, DateUtil.SHORT_PATTERN);
                Date endTime = DateUtil.getNextMonthFirstDay(beginTime);
                queryMapTmp.put("strBeginTime", strBeginTime);
                queryMapTmp.put("strEndTime", DateUtil.convertDateToStr(endTime, DateUtil.SHORT_PATTERN));
                queryMapTmp.put("ledgerId", objId);
                queryMapTmp.put("analyType", ledger.getAnalyType());
                List<RateFeeBean> rateFeeBeans = this.costMapper.getEmoMonthRateEleValue(queryMapTmp); // 分户复(分)费率电量、电费
                boolean hasRates = false; // emo存在复费率
                if (rateFeeBeans != null && rateFeeBeans.size() > 0) {
                    hasRates = true;
                }
                queryMap.put("hasRates", hasRates);
                if (meterList != null && meterList.size() > 0)
                    for (int i = 0; i < meterList.size(); i++) {
                        Object oMeterType = meterList.get(i).get("METER_TYPE");
                        if (oMeterType == null || Short.valueOf(String.valueOf(oMeterType)) != 1) {// 要求电类型
                            continue;
                        }
                        int addAttr = Integer.valueOf(String.valueOf(meterList.get(i).get("ADD_ATTR")));
                        int percent = Integer.valueOf(String.valueOf(meterList.get(i).get("PCT")));
                        Long meterId = Long.valueOf(String.valueOf(meterList.get(i).get("METER_ID")));
                        queryMap.put("pointId", meterId);
                        queryMap.put("meterType", meterTypes.get(0));
                        List<ChartItemWithTime> temp = getDayMeterChartData(queryMap);
                        if (temp != null && temp.size() > 0) {
                            result = addChartItem(result, temp, addAttr, percent);
                        }
                    }
            } else {
                queryMap.put("isMulti", true);
                Map<Long, Double> sectorDataTemp = new HashMap<Long, Double>();
                for (short n = 1; n <= 4; n++) {// 目前要默认展示计量表类型，以后要展示所有或其他要求，修改这里
                    if (!meterTypes.contains(n)) {
                        continue;// 类型不存在暂不处理
                    }
                    for (int i = 0; i < meterList.size(); i++) {
                        Object oMeterType = meterList.get(i).get("METER_TYPE");
                        if (oMeterType == null || Short.parseShort(String.valueOf(oMeterType)) != n) {
                            continue;
                        }
                        int addAttr = Integer.valueOf(String.valueOf(meterList.get(i).get("ADD_ATTR")));
                        int percent = Integer.valueOf(String.valueOf(meterList.get(i).get("PCT")));
                        Long meterId = Long.valueOf(String.valueOf(meterList.get(i).get("METER_ID")));
                        queryMap.put("pointId", meterId);
                        queryMap.put("meterType", n);
                        List<ChartItemWithTime> temp = getAllEnergyChartData(queryMap, sectorDataTemp);
                        if (temp != null && temp.size() > 0) {
                            result = addChartItem(result, temp, addAttr, percent);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * @param //list
     * @param //beginDate
     * @param //endDate
     * @param //eleList
     */
    private List<ChartItemWithTime> getAllEnergyChartData(Map<String, Object> queryMap, Map<Long, Double> sectorDataTemp) {
        List<ChartItemWithTime> list = new ArrayList<ChartItemWithTime>();
        long rateId = -1;
        List<RateSectorBean> rates = null;
        if (queryMap != null && queryMap.get("pointId") != null) {
            rates = costMapper.getPointRateInfo(Long.valueOf(String.valueOf(queryMap.get("pointId"))));
        }
        if (rates != null && rates.size() > 1) {
            if (rates.size() > 0)
                rateId = rates.get(0).getRateId();
        }
        queryMap.put("rateId", rateId);

        String baseTime = null;
        if (queryMap != null && queryMap.get("baseTime") != null)
            baseTime = String.valueOf(queryMap.get("baseTime")) + "-01";
        Date beginTime = DateUtil.convertStrToDate(baseTime, DateUtil.SHORT_PATTERN);
        Date endTime = DateUtil.getMonthLastDay(beginTime);
        queryMap.put("beginTime", beginTime);
        queryMap.put("endTime", endTime);
        long beginDate = beginTime.getTime() / 1000;
        long endDate = endTime.getTime() / 1000;
        List<Map<String, Object>> eleList = costMapper.getEleValue(queryMap);
        Short meterType = null;
        if (queryMap != null && queryMap.get("meterType") != null)
            meterType = Short.parseShort(String.valueOf(queryMap.get("meterType")));
        Long ledgerId = null;
        if (queryMap != null && queryMap.get("objId") != null)
            ledgerId = Long.valueOf(String.valueOf(queryMap.get("objId")));
        if (CommonMethod.isCollectionNotEmpty(eleList)) {
            ChartItemWithTime item = null;
            for (Map<String, Object> map : eleList) {
                double value = Double.valueOf(map.get("ELE_VALUE") == null ? "0" : String.valueOf(map.get("ELE_VALUE")));
                if (item == null)
                    switch (meterType){     //meterType表机类型
                        case 1:         //电
                            item = new ChartItemWithTime(TimeEnum.DAY, "电度电费", beginDate, endDate);
                            break;
                        case 2:         //水
                            item = new ChartItemWithTime(TimeEnum.DAY, "水费", beginDate, endDate);
                            break;
                        case 3:         //气
                            item = new ChartItemWithTime(TimeEnum.DAY, "气费", beginDate, endDate);
                            break;
                        case 4:         //热
                            item = new ChartItemWithTime(TimeEnum.DAY, "热费", beginDate, endDate);
                            break;
                    }
                Date time = (Date) map.get("TIME_FIELD");
                if (rateId != -1 && time != null) {
                    String timeString = DateUtil.convertDateToStr(time, TimeEnum.DAY.getDateFormat());
                    Long sectorId = Long.valueOf(map.get("SECTOR_ID") == null ? "0" : String.valueOf(map.get("SECTOR_ID")));
                    double unitPrice = 0;
                    if (sectorDataTemp != null && sectorDataTemp.containsKey(sectorId)) {
                        unitPrice = sectorDataTemp.get(sectorId);
                    } else {
                        RateSectorBean sectorData = rateBeanMapper.getSectorData2(rateId, sectorId);
                        if (sectorData != null) {
                            unitPrice = sectorData.getRateValue();
                        }
                        sectorDataTemp.put(sectorId, unitPrice);
                    }
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    if (item != null) {
                        map2 = item.getMap();
                    }
                    if (map2.containsKey(timeString)) {
                        map2.put(timeString, DataUtil.doubleAdd(Double.valueOf(String.valueOf(map2.get(timeString))),
                                DataUtil.doubleMultiply(value, unitPrice)));
                    } else
                        map2.put(timeString, DataUtil.doubleMultiply(value, unitPrice));
                } else if (time != null) {
                    MeterBean mBean = new MeterBean();
                    mBean.setMeterType(meterType);
                    mBean.setLedgerId(ledgerId);
                    String timeString = DateUtil.convertDateToStr(time, TimeEnum.DAY.getDateFormat());
                    double unitPrice = calOtherEnergyRateValueWithMeter(mBean);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    if (item != null) {
                        map2 = item.getMap();
                    }
                    if (map2.containsKey(timeString)) {
                        map2.put(timeString, DataUtil.doubleAdd(Double.valueOf(String.valueOf(map2.get(timeString))),
                                DataUtil.doubleMultiply(value, unitPrice)));
                    } else
                        map2.put(timeString, DataUtil.doubleMultiply(value, unitPrice));
                }
            }
            if (item != null)
                list.add(item);
        }

        return ChartConditionUtils.itemDataScale(list);
    }

    /**
     * 整合图表时间和值
     * @param target
     * @param plus
     * @param addFlag
     * @param percent
     * @return List<ChartItemWithTime> target
     */
    private List<ChartItemWithTime> addChartItem(List<ChartItemWithTime> target, List<ChartItemWithTime> plus,
                                                 int addFlag, int percent) {
        int len1 = 0;
        int len2 = 0;
        /// 获取图表结果数量
        if (target.size() > 0) {
            len1 = target.size();
        }
        /// 获取另一组数据数量
        if (plus != null && plus.size() > 0) {
            len2 = plus.size();
        }

        if (len2 == 0) {
            return target;
        }
        for (int i = 0; i < len2; i++) {
            ChartItemWithTime plus_i = null;

            if (plus != null && plus.size() > 0) {
                plus_i = plus.get(i);
                int mark = -1;
                for (int j = 0; j < len1; j++) {
                    if (null != target && target.get(j).getName().equals(plus_i.getName())) {
                        mark = j;
                    }
                }
                //计算图表的时间单位 开始时间  结束时间  图表名称   值
                if (mark != -1 && len1 != 0) {
                    ChartItemWithTime target_tmp = new ChartItemWithTime(target.get(mark).getName(),
                            addMap(target.get(mark).getMap(), plus_i.getMap(), addFlag, percent), null);
                    target.set(mark, target_tmp);
                    mark = -1;
                } else {
                    ChartItemWithTime target_tmp = new ChartItemWithTime(plus_i.getName(),
                            addMap(new TreeMap<String, Object>(), plus_i.getMap(), addFlag, percent), null);
                    target.add(target_tmp);
                }
            }
        }
        return target;
    }

    /**
     *  获取图表数据名称
     * @param target
     * @param plus
     * @param addFlag
     * @param percent
     * @return  SortedMap<String, Object> target
     */
    private SortedMap<String, Object> addMap(SortedMap<String, Object> target, SortedMap<String, Object> plus, int addFlag, int percent) {
        Object[] treeMapObj = new Object[0];            //树对象
        if (plus != null) {
            treeMapObj = plus.keySet().toArray();
        }
        String key;
        for (int i = 0; i < treeMapObj.length; i++) {
            key = String.valueOf(treeMapObj[i]);
            if (addFlag == 1) { // 加
                if (target.containsKey(key)) {
                    target.put(key,
                            DoubleUtils
                                    .getDoubleValue(BigDecimal.valueOf(Double.valueOf(String.valueOf(target.get(key))))
                                            .add(BigDecimal.valueOf(Double.valueOf(String.valueOf(plus.get(key))))
                                                    .multiply(BigDecimal.valueOf(percent))
                                                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN))
                                            .doubleValue(), 0));
                } else {
                    target.put(key, DoubleUtils.getDoubleValue(BigDecimal.valueOf(Double.valueOf(String.valueOf(plus.get(key))))
                            .multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100)).doubleValue(), 0));
                }
            } else if (addFlag == -1) { // 减
                if (target.containsKey(key) && plus != null && plus.get(key)!=null) {
                    target.put(key,
                            DoubleUtils
                                    .getDoubleValue(BigDecimal.valueOf(Double.valueOf(String.valueOf(target.get(key))))
                                            .subtract(BigDecimal.valueOf(Double.valueOf(String.valueOf(plus.get(key))))
                                                    .multiply(BigDecimal.valueOf(percent))
                                                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN))
                                            .doubleValue(), 0));
                } else if(plus != null && plus.get(key)!=null){
                    target.put(key,
                            DoubleUtils.getDoubleValue(BigDecimal.valueOf(Double.valueOf(String.valueOf(plus.get(key))))
                                    .multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100)).negate()
                                    .doubleValue(), 0));
                }
            }
        }
        return target;
    }

    /**
     * @param list
     * @param beginDate
     * @param endDate
     * @param eleList
     */
    private void builderRateDatas(List<ChartItemWithTime> list, long beginDate, long endDate,
                                  List<Map<String, Object>> eleList, long rateId) {
        ChartItemWithTime item = null;
        Map<Long, ChartItemWithTime> mapping = new HashMap<Long, ChartItemWithTime>();
        if (eleList != null) {
            for (Map<String, Object> map : eleList) {
                double value = Double.valueOf(map.get("ELE_VALUE") == null ? "0" : String.valueOf(map.get("ELE_VALUE")));
                Date time = (Date) map.get("TIME_FIELD");
                if (rateId == -1) {// emo下的单费率
                    map.put("SECTOR_ID", 3);
                    map.put("SECTOR_NAME", "平");
                }
                Long sectorId = Long.valueOf(map.get("SECTOR_ID") == null ? "0" : String.valueOf(map.get("SECTOR_ID")));
                if (map.get("SECTOR_ID") != null && map.get("TIME_FIELD") != null && map.get("ELE_VALUE") != null
                        && !mapping.containsKey(sectorId)) {
                    String sectorName = map.get("SECTOR_NAME") == null ? (defaultRateNameSuffer + sectorId)
                            : String.valueOf(map.get("SECTOR_NAME"));
                    item = new ChartItemWithTime(TimeEnum.DAY, sectorName, beginDate, endDate);
                    mapping.put(sectorId, item);
                }
                if (time != null && mapping.get(sectorId) != null) {
                    String timeString = DateUtil.convertDateToStr(time, TimeEnum.DAY.getDateFormat());
                    Map<String, Object> map2 = mapping.get(sectorId).getMap();
                    if (map2.containsKey(timeString)) {
                        map2.put(timeString, DataUtil.doubleAdd(Double.valueOf(String.valueOf(map2.get(timeString))), value));
                    } else
                        map2.put(timeString, value);
                }
            }
        }
        list.addAll(mapping.values());
    }

    /**
     * @param list
     * @param beginDate
     * @param endDate
     * @param eleList
     */
    private void buildSingleDatas(List<ChartItemWithTime> list, long beginDate, long endDate,
                                  List<Map<String, Object>> eleList, short meterType) {
        ChartItemWithTime item = null;
        for (Map<String, Object> map : eleList) {
            double value = Double.valueOf(map.get("ELE_VALUE") == null ? "0" : String.valueOf(map.get("ELE_VALUE")));
            if (item == null)
                switch (meterType){     // 表机类型
                    case 2:     //水
                        item = new ChartItemWithTime(TimeEnum.DAY, ChartNameUtils.getChartNameBean().getWaterChartName(),
                                beginDate, endDate);
                        break;
                    case 3:     //气
                        item = new ChartItemWithTime(TimeEnum.DAY, ChartNameUtils.getChartNameBean().getGasChartName(),
                                beginDate, endDate);
                        break;
                    case 4:        //热
                        item = new ChartItemWithTime(TimeEnum.DAY, ChartNameUtils.getChartNameBean().getHeatChartName(),
                                beginDate, endDate);
                        break;
                    default:
                        item = new ChartItemWithTime(TimeEnum.DAY, "平", beginDate, endDate);
                        break;
                }
            Date time = (Date) map.get("TIME_FIELD");
            if (time != null) {
                String timeString = DateUtil.convertDateToStr(time, TimeEnum.DAY.getDateFormat());
                Map<String, Object> map2 = item.getMap();
                if (map2.containsKey(timeString)) {
                    map2.put(timeString, DataUtil.doubleAdd(Double.valueOf(String.valueOf(map2.get(timeString))), value));
                } else
                    map2.put(timeString, value);
            }
        }
        if (item != null)
            list.add(item);
    }

    @Override
    public Map<String, Object> calEmoDcpEleFee(Map<String, Object> queryMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 判断是否是计算本月，若为本月,则基本电费需要乘以相应日期比例
        boolean isThisMonth = false;
        String beginStr = null;
        if (queryMap != null && queryMap.get("baseTime") != null) {
            beginStr = String.valueOf(queryMap.get("baseTime"));
        }
        String nowStr = DateUtil.getCurrentDateStr(DateUtil.MONTH_PATTERN);

        if (beginStr.equals(nowStr)) {
            isThisMonth = true;
        }
        // 处理传参
        Integer objType = null;
        if (queryMap != null && queryMap.get("objType") != null)
            objType = Integer.valueOf(String.valueOf(queryMap.get("objType")));
        Long objId = null;
        if (queryMap != null && queryMap.get("objId") != null)
            objId = Long.valueOf(String.valueOf(queryMap.get("objId")));
        String baseTime = null;
        if (queryMap != null && queryMap.get("baseTime") != null)
            baseTime = queryMap.get("baseTime") + "-01";
        Date beginTime = DateUtil.convertStrToDate(baseTime, DateUtil.SHORT_PATTERN);
        Date endTime = DateUtil.getMonthLastDay(beginTime);
        queryMap.put("beginTime", beginTime);// 电费信息时间、月最大需量时间
        queryMap.put("endTime", endTime);
        queryMap.put("strBeginTime", baseTime);// 月分费率电量时间
        queryMap.put("strEndTime",
                DateUtil.convertDateToStr(DateUtil.getNextMonthDate(beginTime), DateUtil.SHORT_PATTERN));
        queryMap.put("sTime", beginTime);// 月总电量时间
        queryMap.put("eTime", endTime);
        // 采集点、分户分开计算
        ///// 修改为采集点不计算
        if (objType == TWO) {
            MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(objId);
            if (Integer.valueOf(meterBean.getMeterType()) == ONE) {
                if (meterBean.getVolType() == null || meterBean.getVolType() != 1) {
                    queryMap.put("notCompBaseAjust", true); // 不计算基本、力调
                }
                queryMap.put("pointId", objId);
                if(queryMap != null)
                    result = calAllEleFee(queryMap);
            } else {
                queryMap.put("pointId", objId);
                queryMap.put("meterType", meterBean.getMeterType());
                if(queryMap != null)
                    result = calOtherEnergyFee(queryMap);
            }
        } else if (objType == ONE) {
            LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(objId);
            List<Short> meterTypes = null;
            if (ledger != null)
                meterTypes = costMapper.getComputeMeterTypes(objId, ledger.getAnalyType());
            List<RateFeeBean> rateEles = new ArrayList<RateFeeBean>();// 电计算

            queryMap.put("ledgerId", objId);
            queryMap.put("analyType", ledger.getAnalyType());
            if (meterTypes != null && meterTypes.contains(new Short("1"))) {
                // 将复费率加到rateEles中
                List<RateFeeBean> rateFeeBeans = this.costMapper.getEmoMonthRateEleValue(queryMap); // 分户复(分)费率电量、电费
                // 尖峰平谷
                if (rateFeeBeans != null && rateFeeBeans.size() > 0) {
                    rateEles.addAll(rateFeeBeans);
                }
                // 将单费率加到rateEles中
                //RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValue(queryMap); // 分户单费率电量、电费
                RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValueNew(queryMap); // 分户单费率电量、电费
                if (singleFeeBean.getValue() != 0 || singleFeeBean.getFee() != 0) {
                    if (rateEles.size() > 0) {
                        for (int i = 0; i < rateEles.size(); i++) {
                            RateFeeBean temp = rateEles.get(i);
                            if (temp.getSectorId().equals(singleFeeBean.getSectorId())) {
                                temp.setValue(DataUtil.doubleAdd(temp.getValue(), singleFeeBean.getValue()));
                                temp.setFee(DataUtil.doubleAdd(temp.getFee(), singleFeeBean.getFee()));
                                rateEles.set(i, temp);
                            }
                        }
                    } else if (rateEles.size() == 0) {
                        rateEles.add(singleFeeBean);
                    }
                }

                // 容量需量相关
                Map<String, Object> volDemand = processLedgerDemand(queryMap);
                Integer declareType = null;
                if (volDemand != null && volDemand.keySet().contains("declareType") && volDemand.get("declareType") != null) {
                    declareType = Integer.valueOf(String.valueOf(volDemand.get("declareType")));
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("beginTime", beginTime);
                map.put("meterId", objId);
                map.put("flag", 0);        //此属性为选择sql语句条件,0为当前月,1为当月以前

                // 判断当月有没有用户的需量申报和容量申报
                Map<String, Object> emoDecalreByIdAndBeginTime = declareService.getEmoDecalreByIdAndBeginTime(map);

                // 此处做为如果按时间查询没有数据的话就查询历史申报的最后一次记录
                if (emoDecalreByIdAndBeginTime == null) {
                    map.put("flag", 1);
                    emoDecalreByIdAndBeginTime = declareService.getEmoDecalreByIdAndBeginTime(map);
                }

                // 如果依然为空则证明该用户从未申报过,按照最大容量计算
                if (emoDecalreByIdAndBeginTime == null || emoDecalreByIdAndBeginTime.size() == 0)
                    // 此方法作为没有需量申报也没有容量申报时使用
                    if (ledger.getAnalyType() == 102) {
                        RateFeeBean capacityFee = this.costMapper.getEmoCapacityFee(queryMap);
                        if (capacityFee != null)
                            capacityFee.setFee(plusThisMonthPer(isThisMonth, capacityFee.getFee()));
                        rateEles.add(capacityFee);
                    }

                // 将"容量申报"时的"基本电费"
                // 此处做为计算有容量申报的电费
                if (ledger.getAnalyType() == ANALY_TYPE_102 && emoDecalreByIdAndBeginTime != null) {
                    if ((declareType != null && declareType == 1)
                            || (emoDecalreByIdAndBeginTime.get("DECLARETYPE") != null && Integer
                            .valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLARETYPE"))) == 1)) {
                        double bsFee = 0;
                        RateFeeBean rf = new RateFeeBean(null, "基本", 4, 0, 0);
                        if (volDemand != null && volDemand.get("ledgerPrice") != null
                                && emoDecalreByIdAndBeginTime.get("DECLAREVALUE") != null) {
                            Map<String, Object> ledgerPrice = null;
                            if (volDemand.get("ledgerPrice") != null)
                                ledgerPrice = (Map<String, Object>) volDemand.get("ledgerPrice");
                            rf.setPrice(Double.valueOf(String.valueOf(ledgerPrice.get("DERATE"))));
                            Double declareValue = Double
                                    .valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLAREVALUE")));
                            rf.setValue(declareValue);
                            bsFee = DataUtil.doubleMultiply(rf.getValue(), rf.getPrice());
                            rf.setFee(plusThisMonthPer(isThisMonth, bsFee));
                        }
                        rateEles.add(rf);
                    }
                }

                // 功率因数
                double pf = 0;
                Map<String, Object> fq = costMapper.getLedgerMonthTotalEleValue(ledger.getLedgerId(),
                        ledger.getAnalyType(), beginTime, DateUtil.getMonthLastDay(beginTime));
                // EMO有功总、EMO无功总
                if (fq != null) {
                    result.put("faqValue", Double.valueOf(String.valueOf(fq.get("FAQVALUE"))));
                    result.put("frqValue", Double.valueOf(String.valueOf(fq.get("FRQVALUE"))));
                    pf = DataUtil.getPF(Double.valueOf(String.valueOf(fq.get("FAQVALUE"))),
                            Double.valueOf(String.valueOf(fq.get("FRQVALUE"))));
                    result.put("pf", pf);
                }
                // 标准功率因数
                Double std = costMapper.getEmoThresholdValue(objId);
                if (std != null) {
                    result.put("stdPF", std);
                }
                // 查调整电费幅度
                Double ratePF = null;
                if (std != null) {
                    ratePF = costMapper.getFactor(DataUtil.doubleSubtract(pf, 0.01), std);
                    if (ratePF != null) {
                        result.put("ratePF", ratePF);
                    }
                }

                // 将"需量申报"时的"基本电费"、"力调电费"
                // 此处做为有需量申报时的计算电费
                if (ledger.getAnalyType() == ANALY_TYPE_102) {
                    result.putAll(volDemand);

                    if (emoDecalreByIdAndBeginTime != null && emoDecalreByIdAndBeginTime.get("DECLARETYPE") != null
                            && Integer.valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLARETYPE"))) == 2) {
                        double bsFee = 0;
                        RateFeeBean rf = new RateFeeBean(null, "基本", 4, 0, 0);
                        if (volDemand.get("ledgerPrice") != null && emoDecalreByIdAndBeginTime.keySet().contains("DECLAREVALUE")
                                && emoDecalreByIdAndBeginTime.get("DECLAREVALUE") != null) {
                            Map<String, Object> ledgerPrice = (Map<String, Object>) volDemand.get("ledgerPrice");
                            Double declareValue = Double.valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLAREVALUE")));
                            if (ledgerPrice.get("DERATE") != null && ledgerPrice.get("DETH") != null) {

                                rf.setPrice(Double.valueOf(String.valueOf(ledgerPrice.get("DERATE"))));
                                rf.setValue(declareValue);
                                bsFee = DataUtil.doubleMultiply(rf.getValue(), rf.getPrice());
                                rf.setFee(plusThisMonthPer(isThisMonth, bsFee));
                            }
                        }
                        rateEles.add(rf);
                    }

                    if (ratePF != null) { // 力调电费
                        RateFeeBean br = new RateFeeBean();
                        br.setSectorName("力调");
                        br.setValue(pf);
                        br.setFeeId(5);
                        br.setPrice(DoubleUtils.getDoubleValue(ratePF, 2));
                        double totalEleFee = 0;
                        for (RateFeeBean r : rateEles) {
                            totalEleFee = DataUtil.doubleAdd(totalEleFee, r.getFee());
                        }
                        br.setFee(DataUtil.doubleDivide(DataUtil.doubleMultiply(totalEleFee, br.getPrice()), 100));
                        rateEles.add(br);
                    }
                }
            }

            Integer queryType = 0;
            if (queryMap.get("queryType") != null) {
                queryType = Integer.valueOf(String.valueOf(queryMap.get("queryType")));
            }
            if (meterTypes != null && ((meterTypes.size() == 1 && meterTypes.get(0) == 1) || queryType == 1)) {// 电费
                result.put("fee", rateEles);
            } else if (meterTypes != null) {
                List<Map<String, Object>> feeMapList = new ArrayList<Map<String, Object>>();
                for (Short meterType : meterTypes) {
                    Map<String, Object> feeMap = new HashMap<String, Object>();
                    feeMap.put("meterType", meterType);         //meterType 表机类型
                    Double monthValue = 0d;
                    if (Integer.valueOf(meterType) == ONE) {
                        List<RateFeeBean> newRateEles = new ArrayList<RateFeeBean>();// 不包含基本和力调电费
                        // 将复费率加到rateEles中
                        List<RateFeeBean> rateFeeBeans = this.costMapper.getEmoMonthRateEleValue(queryMap); // 分户复(分)费率电量、电费
                        if (rateFeeBeans != null && rateFeeBeans.size() > 0) {
                            newRateEles.addAll(rateFeeBeans);
                        }
                        // 将单费率加到rateEles中
                        RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValue(queryMap); // 分户单费率电量、电费
                        if (singleFeeBean.getValue() != 0 || singleFeeBean.getFee() != 0) {
                            if (newRateEles.size() > 0) {
                                for (int i = 0; i < newRateEles.size(); i++) {
                                    RateFeeBean temp = newRateEles.get(i);
                                    if (temp != null && temp.getSectorId().equals(singleFeeBean.getSectorId())) {
                                        temp.setValue(DataUtil.doubleAdd(temp.getValue(), singleFeeBean.getValue()));
                                        temp.setFee(DataUtil.doubleAdd(temp.getFee(), singleFeeBean.getFee()));
                                        newRateEles.set(i, temp);
                                    }
                                }
                            } else if (newRateEles.size() == 0) {
                                newRateEles.add(singleFeeBean);
                            }
                        }
                        double unitPrice = 1;// 单价
                        double price = 0;
                        for (RateFeeBean feeBean : newRateEles) {
                            price = DataUtil.doubleAdd(DoubleUtils.getDoubleValue(feeBean.getFee(), 0), price);
                            monthValue = DataUtil.doubleAdd(feeBean.getValue(), monthValue);
                            unitPrice = feeBean.getPrice();// 取其中一个单价，页面emo隐藏单价无影响
                        }
                        feeMap.put("itemName", "电");
                        feeMap.put("value", monthValue);
                        feeMap.put("unitPrice", unitPrice);
                        feeMap.put("price", price);
                        feeMapList.add(feeMap);
                    } else {
                        queryMap.put("meterType", meterType);
                        List<Map<String, Object>> monEles = costMapper.getMonthEleValue(queryMap);// 取月总用量
                        if (monEles != null && monEles.size() > 0) {
                            Map<String, Object> monEle = monEles.get(0);
                            if (monEle.get("ELE_VALUE") != null) {
                                monthValue = Double.valueOf(String.valueOf(monEle.get("ELE_VALUE")));
                            }
                        }
                        MeterBean meterBean = new MeterBean();
                        meterBean.setLedgerId(objId);
                        meterBean.setMeterType(meterType);

                        double unitPrice = 0;
                        double price = 0;

                        switch (meterType){ //meterType 表机类型
                            case 2:     //水
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "水");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            case 3:     //气
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "气");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            case 4:     //热
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "热");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            default:       //电
                                unitPrice = 1;// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "电");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                        }
                    }
                }
                result.put("fee", feeMapList);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> calEmoDcpEleFee2(Map<String, Object> queryMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 判断是否是计算本月，若为本月,则基本电费需要乘以相应日期比例
        boolean isThisMonth = false;
        String beginStr = null;
        if (queryMap != null && queryMap.get("baseTime") != null) {
            beginStr = String.valueOf(queryMap.get("baseTime"));
        }
        String nowStr = DateUtil.getCurrentDateStr(DateUtil.MONTH_PATTERN);

        if (beginStr.equals(nowStr)) {
            isThisMonth = true;
        }
        // 处理传参
        Integer objType = null;
        if (queryMap != null && queryMap.get("objType") != null)
            objType = Integer.valueOf(String.valueOf(queryMap.get("objType")));
        Long objId = null;
        if (queryMap != null && queryMap.get("objId") != null)
            objId = Long.valueOf(String.valueOf(queryMap.get("objId")));
        String baseTime = null;
        if (queryMap != null && queryMap.get("baseTime") != null)
            baseTime = queryMap.get("baseTime") + "-01";
        Date beginTime = DateUtil.convertStrToDate(baseTime, DateUtil.SHORT_PATTERN);
        Date endTime = DateUtil.getMonthLastDay(beginTime);
        queryMap.put("beginTime", beginTime);// 电费信息时间、月最大需量时间
        queryMap.put("endTime", endTime);
        queryMap.put("strBeginTime", baseTime);// 月分费率电量时间
        queryMap.put("strEndTime",
                DateUtil.convertDateToStr(DateUtil.getNextMonthDate(beginTime), DateUtil.SHORT_PATTERN));
        queryMap.put("sTime", beginTime);// 月总电量时间
        queryMap.put("eTime", endTime);
        // 采集点、分户分开计算
        ///// 修改为采集点不计算
        if (objType == TWO) {
            MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(objId);
            if (Integer.valueOf(meterBean.getMeterType()) == ONE) {
                if (meterBean.getVolType() == null || meterBean.getVolType() != 1) {
                    queryMap.put("notCompBaseAjust", true); // 不计算基本、力调
                }
                queryMap.put("pointId", objId);
                if(queryMap != null)
                    result = calAllEleFee(queryMap);
            } else {
                queryMap.put("pointId", objId);
                queryMap.put("meterType", meterBean.getMeterType());
                if(queryMap != null)
                    result = calOtherEnergyFee(queryMap);
            }
        } else if (objType == ONE) {
            LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(objId);
            List<Short> meterTypes = null;
            if (ledger != null)
                meterTypes = costMapper.getComputeMeterTypes(objId, ledger.getAnalyType());
            List<RateFeeBean> rateEles = new ArrayList<RateFeeBean>();// 电计算

            queryMap.put("ledgerId", objId);
            queryMap.put("analyType", ledger.getAnalyType());
            if (meterTypes != null && meterTypes.contains(new Short("1"))) {
                // 将复费率加到rateEles中
                List<RateFeeBean> rateFeeBeans = this.costMapper.getEmoMonthRateEleValue2(queryMap); // 分户复(分)费率电量、电费
                // 尖峰平谷
                if (rateFeeBeans != null && rateFeeBeans.size() > 0) {
                    rateEles.addAll(rateFeeBeans);
                }
                // 将单费率加到rateEles中
                //RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValue(queryMap); // 分户单费率电量、电费
                RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValueNew(queryMap); // 分户单费率电量、电费
                if (singleFeeBean.getValue() != 0 || singleFeeBean.getFee() != 0) {
                    if (rateEles.size() > 0) {
                        for (int i = 0; i < rateEles.size(); i++) {
                            RateFeeBean temp = rateEles.get(i);
                            if (temp.getSectorId().equals(singleFeeBean.getSectorId())) {
                                temp.setValue(DataUtil.doubleAdd(temp.getValue(), singleFeeBean.getValue()));
                                temp.setFee(DataUtil.doubleAdd(temp.getFee(), singleFeeBean.getFee()));
                                rateEles.set(i, temp);
                            }
                        }
                    } else if (rateEles.size() == 0) {
                        rateEles.add(singleFeeBean);
                    }
                }

                // 容量需量相关
                Map<String, Object> volDemand = processLedgerDemand(queryMap);
                Integer declareType = null;
                if (volDemand != null && volDemand.keySet().contains("declareType") && volDemand.get("declareType") != null) {
                    declareType = Integer.valueOf(String.valueOf(volDemand.get("declareType")));
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("beginTime", beginTime);
                map.put("meterId", objId);
                map.put("flag", 0);        //此属性为选择sql语句条件,0为当前月,1为当月以前

                // 判断当月有没有用户的需量申报和容量申报
                Map<String, Object> emoDecalreByIdAndBeginTime = declareService.getEmoDecalreByIdAndBeginTime(map);

                // 此处做为如果按时间查询没有数据的话就查询历史申报的最后一次记录
                if (emoDecalreByIdAndBeginTime == null) {
                    map.put("flag", 1);
                    emoDecalreByIdAndBeginTime = declareService.getEmoDecalreByIdAndBeginTime(map);
                }

                // 如果依然为空则证明该用户从未申报过,按照最大容量计算
                if (emoDecalreByIdAndBeginTime == null || emoDecalreByIdAndBeginTime.size() == 0)
                    // 此方法作为没有需量申报也没有容量申报时使用
                    if (ledger.getAnalyType() == 102) {
                        RateFeeBean capacityFee = this.costMapper.getEmoCapacityFee(queryMap);
                        if (capacityFee != null)
                            capacityFee.setFee(plusThisMonthPer(isThisMonth, capacityFee.getFee()));
                        rateEles.add(capacityFee);
                    }

                // 将"容量申报"时的"基本电费"
                // 此处做为计算有容量申报的电费
                if (ledger.getAnalyType() == ANALY_TYPE_102 && emoDecalreByIdAndBeginTime != null) {
                    if ((declareType != null && declareType == 1)
                            || (emoDecalreByIdAndBeginTime.get("DECLARETYPE") != null && Integer
                            .valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLARETYPE"))) == 1)) {
                        double bsFee = 0;
                        RateFeeBean rf = new RateFeeBean(null, "基本", 4, 0, 0);
                        if (volDemand != null && volDemand.get("ledgerPrice") != null
                                && emoDecalreByIdAndBeginTime.get("DECLAREVALUE") != null) {
                            Map<String, Object> ledgerPrice = null;
                            if (volDemand.get("ledgerPrice") != null)
                                ledgerPrice = (Map<String, Object>) volDemand.get("ledgerPrice");
                            rf.setPrice(Double.valueOf(String.valueOf(ledgerPrice.get("DERATE"))));
                            Double declareValue = Double
                                    .valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLAREVALUE")));
                            rf.setValue(declareValue);
                            bsFee = DataUtil.doubleMultiply(rf.getValue(), rf.getPrice());
                            rf.setFee(plusThisMonthPer(isThisMonth, bsFee));
                        }
                        rateEles.add(rf);
                    }
                }

                // 功率因数
                double pf = 0;
                Map<String, Object> fq = costMapper.getLedgerMonthTotalEleValue(ledger.getLedgerId(),
                        ledger.getAnalyType(), beginTime, DateUtil.getMonthLastDay(beginTime));
                // EMO有功总、EMO无功总
                if (fq != null) {
                    result.put("faqValue", Double.valueOf(String.valueOf(fq.get("FAQVALUE"))));
                    result.put("frqValue", Double.valueOf(String.valueOf(fq.get("FRQVALUE"))));
                    pf = DataUtil.getPF(Double.valueOf(String.valueOf(fq.get("FAQVALUE"))),
                            Double.valueOf(String.valueOf(fq.get("FRQVALUE"))));
                    result.put("pf", pf);
                }
                // 标准功率因数
                Double std = costMapper.getEmoThresholdValue(objId);
                if (std != null) {
                    result.put("stdPF", std);
                }
                // 查调整电费幅度
                Double ratePF = null;
                if (std != null) {
                    ratePF = costMapper.getFactor(DataUtil.doubleSubtract(pf, 0.01), std);
                    if (ratePF != null) {
                        result.put("ratePF", ratePF);
                    }
                }

                // 将"需量申报"时的"基本电费"、"力调电费"
                // 此处做为有需量申报时的计算电费
                if (ledger.getAnalyType() == ANALY_TYPE_102) {
                    result.putAll(volDemand);

                    if (emoDecalreByIdAndBeginTime != null && emoDecalreByIdAndBeginTime.get("DECLARETYPE") != null
                            && Integer.valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLARETYPE"))) == 2) {
                        double bsFee = 0;
                        RateFeeBean rf = new RateFeeBean(null, "基本", 4, 0, 0);
                        if (volDemand.get("ledgerPrice") != null && emoDecalreByIdAndBeginTime.keySet().contains("DECLAREVALUE")
                                && emoDecalreByIdAndBeginTime.get("DECLAREVALUE") != null) {
                            Map<String, Object> ledgerPrice = (Map<String, Object>) volDemand.get("ledgerPrice");
                            Double declareValue = Double.valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLAREVALUE")));
                            if (ledgerPrice.get("DERATE") != null && ledgerPrice.get("DETH") != null) {

                                rf.setPrice(Double.valueOf(String.valueOf(ledgerPrice.get("DERATE"))));
                                rf.setValue(declareValue);
                                bsFee = DataUtil.doubleMultiply(rf.getValue(), rf.getPrice());
                                rf.setFee(plusThisMonthPer(isThisMonth, bsFee));
                            }
                        }
                        rateEles.add(rf);
                    }

                    if (ratePF != null) { // 力调电费
                        RateFeeBean br = new RateFeeBean();
                        br.setSectorName("力调");
                        br.setValue(pf);
                        br.setFeeId(5);
                        br.setPrice(DoubleUtils.getDoubleValue(ratePF, 2));
                        double totalEleFee = 0;
                        for (RateFeeBean r : rateEles) {
                            totalEleFee = DataUtil.doubleAdd(totalEleFee, r.getFee());
                        }
                        br.setFee(DataUtil.doubleDivide(DataUtil.doubleMultiply(totalEleFee, br.getPrice()), 100));
                        rateEles.add(br);
                    }
                }
            }

            Integer queryType = 0;
            if (queryMap.get("queryType") != null) {
                queryType = Integer.valueOf(String.valueOf(queryMap.get("queryType")));
            }
            if (meterTypes != null && ((meterTypes.size() == 1 && meterTypes.get(0) == 1) || queryType == 1)) {// 电费
                result.put("fee", rateEles);
            } else if (meterTypes != null) {
                List<Map<String, Object>> feeMapList = new ArrayList<Map<String, Object>>();
                for (Short meterType : meterTypes) {
                    Map<String, Object> feeMap = new HashMap<String, Object>();
                    feeMap.put("meterType", meterType);         //meterType 表机类型
                    Double monthValue = 0d;
                    if (Integer.valueOf(meterType) == ONE) {
                        List<RateFeeBean> newRateEles = new ArrayList<RateFeeBean>();// 不包含基本和力调电费
                        // 将复费率加到rateEles中
                        List<RateFeeBean> rateFeeBeans = this.costMapper.getEmoMonthRateEleValue2(queryMap); // 分户复(分)费率电量、电费
                        if (rateFeeBeans != null && rateFeeBeans.size() > 0) {
                            newRateEles.addAll(rateFeeBeans);
                        }
                        // 将单费率加到rateEles中
                        RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValue(queryMap); // 分户单费率电量、电费
                        if (singleFeeBean.getValue() != 0 || singleFeeBean.getFee() != 0) {
                            if (newRateEles.size() > 0) {
                                for (int i = 0; i < newRateEles.size(); i++) {
                                    RateFeeBean temp = newRateEles.get(i);
                                    if (temp != null && temp.getSectorId().equals(singleFeeBean.getSectorId())) {
                                        temp.setValue(DataUtil.doubleAdd(temp.getValue(), singleFeeBean.getValue()));
                                        temp.setFee(DataUtil.doubleAdd(temp.getFee(), singleFeeBean.getFee()));
                                        newRateEles.set(i, temp);
                                    }
                                }
                            } else if (newRateEles.size() == 0) {
                                newRateEles.add(singleFeeBean);
                            }
                        }
                        double unitPrice = 1;// 单价
                        double price = 0;
                        for (RateFeeBean feeBean : newRateEles) {
                            price = DataUtil.doubleAdd(DoubleUtils.getDoubleValue(feeBean.getFee(), 0), price);
                            monthValue = DataUtil.doubleAdd(feeBean.getValue(), monthValue);
                            unitPrice = feeBean.getPrice();// 取其中一个单价，页面emo隐藏单价无影响
                        }
                        feeMap.put("itemName", "电");
                        feeMap.put("value", monthValue);
                        feeMap.put("unitPrice", unitPrice);
                        feeMap.put("price", price);
                        feeMapList.add(feeMap);
                    } else {
                        queryMap.put("meterType", meterType);
                        List<Map<String, Object>> monEles = costMapper.getMonthEleValue(queryMap);// 取月总用量
                        if (monEles != null && monEles.size() > 0) {
                            Map<String, Object> monEle = monEles.get(0);
                            if (monEle.get("ELE_VALUE") != null) {
                                monthValue = Double.valueOf(String.valueOf(monEle.get("ELE_VALUE")));
                            }
                        }
                        MeterBean meterBean = new MeterBean();
                        meterBean.setLedgerId(objId);
                        meterBean.setMeterType(meterType);

                        double unitPrice = 0;
                        double price = 0;

                        switch (meterType){ //meterType 表机类型
                            case 2:     //水
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "水");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            case 3:     //气
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "气");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            case 4:     //热
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "热");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            default:       //电
                                unitPrice = 1;// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "电");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                        }
                    }
                }
                result.put("fee", feeMapList);
            }
        }
        return result;
    }

    /**
     *  获取电表基本电费计算相关信息
     * @param queryMap
     * @return Map<String, Object>
     */
    private Map<String, Object> processLedgerDemand(Map<String, Object> queryMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();

        String baseTime = null;
        if (queryMap != null && queryMap.get("baseTime") != null) {
            baseTime = queryMap.get("baseTime") + "-01";
        }
        Date beginTime = DateUtil.convertStrToDate(baseTime, DateUtil.SHORT_PATTERN);
        Date endTime = DateUtil.getMonthLastDay(beginTime);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("pointId", "");
        if (queryMap != null && queryMap.get("objId") != null)
            map.put("pointId", queryMap.get("objId"));
        List<Map<String, Object>> ledgerBasicInfo = this.costMapper.getLedgerBasicFeeInfo(map);
        if (ledgerBasicInfo != null && ledgerBasicInfo.size() > 0) {
            Map<String, Object> info = ledgerBasicInfo.get(0);
            if(info != null) {
                if (info.keySet().contains("VOLUME") && info.get("VOLUME") != null) {
                    result.put("volume", info.get("VOLUME"));
                }
                if (info.keySet().contains("DECLARETYPE") && info.get("DECLARETYPE") != null) {
                    result.put("declareType", info.get("DECLARETYPE"));
                }
                if (info.keySet().contains("DECLAREVALUE") && info.get("DECLAREVALUE") != null) {
                    result.put("declareValue", info.get("DECLAREVALUE"));
                }
            }
        }
        // 分户费率
        if (queryMap != null && queryMap.get("objId") != null) {
            Map<String, Object> ledgerPrice = costMapper.getLedgerFeePrice(Long.valueOf(String.valueOf(queryMap.get("objId"))));
            result.put("ledgerPrice", ledgerPrice);
        }

        return result;
    }

    @Override
    public Map<String, Object> calEmoDcpPreMonthEleFee(Map<String, Object> queryMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 处理传参
        Integer objType = null;
        Long objId = null;
        String baseTime = null;
        if (queryMap != null) {
            if (queryMap.get("objType") != null)
                objType = Integer.valueOf(String.valueOf(queryMap.get("objType")));
            if (queryMap.get("objId") != null)
                objId = Long.valueOf(String.valueOf(queryMap.get("objId")));
            if (queryMap.get("baseTime") != null)
                baseTime = queryMap.get("baseTime") + "-01";
        }
        Date beginTime = DateUtil.getPreMonthFristDay(DateUtil.convertStrToDate(baseTime, DateUtil.SHORT_PATTERN));
        Date endTime = DateUtil.getNextMonthFirstDay(beginTime);
        queryMap.put("beginTime", beginTime); // 电费信息时间、月最大需量时间
        queryMap.put("endTime", DateUtil.getMonthLastDay(beginTime));
        queryMap.put("strBeginTime", DateUtil.convertDateToStr(beginTime, DateUtil.SHORT_PATTERN)); // 月分费率电量时间
        queryMap.put("strEndTime", DateUtil.convertDateToStr(endTime, DateUtil.SHORT_PATTERN));
        queryMap.put("sTime", beginTime); // 月总电量时间
        queryMap.put("eTime", DateUtil.getMonthLastDay(beginTime));

        // 采集点、分户分开计算
        if (objType == TWO) {
            MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(objId);
            if (meterBean.getVolType() == null || meterBean.getVolType() != 1) {
                queryMap.put("notCompBaseAjust", true); // 不计算基本、力调
            }
            queryMap.put("pointId", objId);
            result = calAllEleFee(queryMap);
        } else if (objType == ONE) {
            LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(objId);
            queryMap.put("ledgerId", objId);
            if (ledger != null)
                queryMap.put("analyType", ledger.getAnalyType());

            List<RateFeeBean> rateEles = new ArrayList<RateFeeBean>();
            // 将复费率加到rateEles中
            List<RateFeeBean> rateFeeBeans = null; // 分户复(分)费率电量、电费
            if (queryMap != null) {
                rateFeeBeans = this.costMapper.getEmoMonthRateEleValue(queryMap);
            }
            if (rateFeeBeans != null && rateFeeBeans.size() > 0) {
                rateEles.addAll(rateFeeBeans);
            }
            // 将单费率加到rateEles中
            RateFeeBean singleFeeBean = null; // 分户单费率电量、电费
            if (queryMap != null) {
                //singleFeeBean = this.costMapper.getEmoMonthSingleEleValue(queryMap);
            	singleFeeBean = this.costMapper.getEmoMonthSingleEleValueNew(queryMap);
            }
            if (singleFeeBean.getValue() != 0 || singleFeeBean.getFee() != 0) {
                if (rateEles.size() > 0) {
                    for (int i = 0; i < rateEles.size(); i++) {
                        RateFeeBean temp = rateEles.get(i);
                        if (temp != null && temp.getSectorId().equals(singleFeeBean.getSectorId())) {
                            temp.setValue(DataUtil.doubleAdd(temp.getValue(), singleFeeBean.getValue()));
                            temp.setFee(DataUtil.doubleAdd(temp.getFee(), singleFeeBean.getFee()));
                            rateEles.set(i, temp);
                        }
                    }
                } else if (rateEles.size() == 0) {
                    rateEles.add(singleFeeBean);
                }
            }

            // 容量需量相关
            Map<String, Object> volDemand = null;
            if (queryMap != null) {
                volDemand = processLedgerDemand(queryMap);
            }
            Integer declareType = null;
            if (volDemand.keySet().contains("declareType") && volDemand.get("declareType") != null) {
                declareType = Integer.valueOf(String.valueOf(volDemand.get("declareType")));
            }

            // 将"容量申报"时的"基本电费"
            if (queryMap != null && ledger.getAnalyType() == DemandDeclareController.ANALYTYPE_102) {
                if (declareType != null && declareType == 1) {
                    RateFeeBean capacityFee = this.costMapper.getEmoCapacityFee(queryMap);
                    rateEles.add(capacityFee);
                }
            }

            // 功率因数
            Map<String, Object> fq = costMapper.getLedgerMonthTotalEleValue(ledger.getLedgerId(), ledger.getAnalyType(),
                    beginTime, DateUtil.getMonthLastDay(beginTime));
            double pf = 0;
            if (fq != null && fq.get("FAQVALUE") != null) {
                pf = DataUtil.getPF(Double.valueOf(String.valueOf(fq.get("FAQVALUE"))),
                        Double.valueOf(String.valueOf(fq.get("FRQVALUE"))));
            }
            // 标准功率因数
            Double std = costMapper.getEmoThresholdValue(objId);
            // 查调整电费幅度
            Double ratePF = null;
            if (std != null) {
                ratePF = costMapper.getFactor(DataUtil.doubleSubtract(pf, 0.01), std);
            }
            // 将"需量申报"时的"基本电费"
            if (ledger.getAnalyType() == DemandDeclareController.ANALYTYPE_102) {
                if (declareType != null && declareType == 2) {
                    Double monthDemand = 0D;
                    if (volDemand.keySet().contains("maxDemand")) {
                        monthDemand = DoubleUtils.getDoubleValue(Double.valueOf(String.valueOf(volDemand.get("maxDemand"))),
                                2);
                    }
                    double bsFee = 0;
                    RateFeeBean rf = new RateFeeBean(null, "基本", 4, 0, 0);
                    if (volDemand.get("ledgerPrice") != null && volDemand.keySet().contains("declareValue")
                            && volDemand.keySet().contains("declareValue") && volDemand.get("declareValue") != null) {
                        Map<String, Object> ledgerPrice = (Map<String, Object>) volDemand.get("ledgerPrice");
                        Double declareValue = Double.valueOf(String.valueOf(volDemand.get("declareValue")));
                        if (ledgerPrice.get("DERATE") != null && ledgerPrice.get("DETH") != null) {
                            rf.setPrice(Double.valueOf(String.valueOf(ledgerPrice.get("DERATE"))));
                            double deth = DataUtil.doubleDivide(Double.valueOf(String.valueOf(ledgerPrice.get("DETH"))),
                                    100);
                            BigDecimal btemp = BigDecimal.ONE.add(BigDecimal.valueOf(deth))
                                    .multiply(BigDecimal.valueOf(declareValue));
                            if (monthDemand <= btemp.doubleValue()) {// 月最大需量值<=申报值*（1+超罚限值）
                                if (monthDemand >= declareValue) {
                                    rf.setValue(monthDemand);
                                } else {
                                    rf.setValue(declareValue);
                                }
                                bsFee = DataUtil.doubleMultiply(rf.getValue(), rf.getPrice());
                                rf.setFee(bsFee);
                            } else {
                                rf.setValue(monthDemand);
                                bsFee = btemp.multiply(BigDecimal.valueOf(rf.getPrice()))
                                        .add(BigDecimal.valueOf(monthDemand).subtract(btemp)
                                                .multiply(BigDecimal.valueOf(rf.getPrice())).multiply(BigDecimal.valueOf(2)))
                                        .doubleValue();
                                rf.setFee(bsFee);
                            }
                        }
                    }
                    rateEles.add(rf);
                }

                if (ratePF != null) { // 力调电费
                    RateFeeBean br = new RateFeeBean();
                    br.setSectorName("力调");
                    br.setValue(pf);
                    br.setFeeId(5);
                    br.setPrice(DoubleUtils.getDoubleValue(ratePF, 2));
                    double totalEleFee = 0;
                    for (RateFeeBean r : rateEles) {
                        totalEleFee = DataUtil.doubleAdd(totalEleFee, r.getFee());
                    }
                    br.setFee(BigDecimal.valueOf(totalEleFee).multiply(BigDecimal.valueOf(br.getPrice()))
                            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                    rateEles.add(br);
                }
            }

            // 电费
            result.put("fee", rateEles);
        }
        return result;
    }

    /**
     *  获取电费单价
     * @param queryMap
     * @return Map<String, Object>
     */
    private Map<String, Object> calOtherEnergyFee(Map<String, Object> queryMap) {
        Map<String, Object> data = new HashMap<String, Object>();
        Double monthValue = 0d;
        List<Map<String, Object>> monEles = null;// 取月总用量
        if (queryMap != null) {
            monEles = costMapper.getMonthEleValue(queryMap);
        }
        List<Map<String, Object>> feeMapList = new ArrayList<Map<String, Object>>();
        if (monEles != null && monEles.size() > 0) {
            Map<String, Object> feeMap = new HashMap<String, Object>();
            Map<String, Object> monEle = monEles.get(0);
            if (monEle.get("ELE_VALUE") != null) {
                monthValue = Double.valueOf(String.valueOf(monEle.get("ELE_VALUE")));
                Short meterType = null;
                if (queryMap != null && queryMap.get("meterType") != null)
                    meterType = Short.parseShort(String.valueOf(queryMap.get("meterType")));
                Long meterId = null;
                if (queryMap != null && queryMap.get("pointId") != null)
                    meterId = Long.parseLong(String.valueOf(queryMap.get("pointId")));
                MeterBean meterBean = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                feeMap.put("meterType", meterType);

                double unitPrice = 0;// 单价
                double price = 0;// 单价
                switch (meterType){
                    case 2://水
                        unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                        price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                        feeMap.put("itemName", "水");
                        feeMap.put("value", monthValue);
                        feeMap.put("unitPrice", unitPrice);
                        feeMap.put("price", price);
                        break;
                    case 3://气
                        unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                        price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                        feeMap.put("itemName", "气");
                        feeMap.put("value", monthValue);
                        feeMap.put("unitPrice", unitPrice);
                        feeMap.put("price", price);
                        break;
                    case 4://热
                        unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                        price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                        feeMap.put("itemName", "热");
                        feeMap.put("value", monthValue);
                        feeMap.put("unitPrice", unitPrice);
                        feeMap.put("price", price);
                        break;
                    default:
                        unitPrice = 1;// 单价
                        price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                        feeMap.put("itemName", "电");
                        feeMap.put("value", monthValue);
                        feeMap.put("unitPrice", unitPrice);
                        feeMap.put("price", price);
                        break;
                }
            }
            feeMapList.add(feeMap);
        }
        data.put("fee", feeMapList);
        return data;
    }

    /**
     * 查询 水、气、热费率信息
     * @param meterBean
     * @return double:unitPrice
     */
    private double calOtherEnergyRateValueWithMeter(MeterBean meterBean) {
        double unitPrice = 0;// 单价
        boolean falg = true;
        Long ledgerId = null;
        if (meterBean != null) {
            ledgerId = meterBean.getLedgerId();
        }
        while (falg) {
            if (ledgerId == null) {
                falg = false;
            }
            LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(ledgerId);
            if (ledgerBean == null) {
                falg = false;
            }
            RateBean rateBean = null;
            if(meterBean != null && meterBean.getMeterType() != null){
                switch (meterBean.getMeterType()){  //表机类型
                    case 2:     //水
                        if (ledgerBean.getRateWId() != null) {
                            rateBean = rateBeanMapper.getRateData(ledgerBean.getRateWId());
                        }
                        break;
                    case 3:     // 气
                        if (ledgerBean.getRateGId() != null) {
                            rateBean = rateBeanMapper.getRateData(ledgerBean.getRateGId());
                        }
                        break;
                    case 4:     //热
                        if (ledgerBean.getRateHId() != null) {
                            rateBean = rateBeanMapper.getRateData(ledgerBean.getRateHId());
                        }
                        break;
                    default:       //电
                        if (ledgerBean.getRateId() != null) {
                            rateBean = rateBeanMapper.getRateData(ledgerBean.getRateId());
                        }
                        break;
                }
            }
            if (rateBean != null) {
                List<Map<String, Object>> rateInfo = rateBeanMapper.getWaterGasHotByRateId(rateBean.getRateId());
                if (rateInfo != null && rateInfo.size() > 0) {
                    Object rateValue = rateInfo.get(0).get("RATE_VALUE");
                    if (rateValue != null) {
                        unitPrice = Double.parseDouble(String.valueOf(rateValue));
                    }
                }
                falg = false;
            }
            ledgerId = ledgerBean.getParentLedgerId();
        }
        return unitPrice;
    }

    /**
     *  计算电费
     * @param queryMap
     * @return Map<String, Object>
     */
    private Map<String, Object> calAllEleFee(Map<String, Object> queryMap) {
        Map<String, Object> data = new HashMap<String, Object>();
        // 判断是否是计算本月，若为本月,则基本电费需要乘以相应日期比例
        boolean isThisMonth = false;
        Date beginDate = null;
        if (queryMap != null && queryMap.get("beginTime") != null) {
            beginDate = (Date) queryMap.get("beginTime");
        }
        String beginStr = DateUtil.convertDateToStr(beginDate, DateUtil.MONTH_PATTERN);
        String nowStr = DateUtil.getCurrentDateStr(DateUtil.MONTH_PATTERN);
        if (beginStr.equals(nowStr)) {
            isThisMonth = true;
        }

        long pointId = 0;
        if (queryMap != null && queryMap.get("pointId") != null)
            pointId = Long.valueOf(String.valueOf(queryMap.get("pointId")));
        Double std = costMapper.getThresholdValue(pointId);// 取标准功率因数
        if (std != null)
            data.put("stdPF", std);

        Double ratePF = null;
        double pf = 0;
        Double faqValue = null;
        List<Map<String, Object>> monEles = null;// 取月总电量
        if (queryMap != null) {
            monEles = costMapper.getMonthTotalEleValue(queryMap);
        }
        if (monEles != null && monEles.size() > 0) {
            Map<String, Object> monEle = monEles.get(0);
            Double frqValue = 0D;
            if (monEle.get("FAQVALUE") != null) {// 有功总
                faqValue = Double.valueOf(String.valueOf(monEle.get("FAQVALUE")));
                data.put("faqValue", faqValue);
            }
            if (monEle.get("FRQVALUE") != null) {// 无功总
                frqValue = Double.valueOf(String.valueOf(monEle.get("FRQVALUE")));
                data.put("frqValue", frqValue);
            }
            pf = DataUtil.getPF(faqValue, frqValue);
            data.put("pf", pf);
        }
        if (queryMap != null && queryMap.get("pf") != null) {
            pf = Double.valueOf(String.valueOf(queryMap.get("pf")));
        }
        if (std != null) {
            ratePF = costMapper.getFactor(DoubleUtils.getDoubleValue(DataUtil.doubleSubtract(pf, 0.01), 2), std);// 查调整电费对照表
            if (ratePF != null)
                data.put("ratePF", ratePF);
        }

        Long volume = null;// 变压器容量
        Integer declareType = null;// 申报类型;1,容量;2,需量
        Double declareValue = null;// 申报值
        Double maxDemand = null;// 取月最大需量
        if (queryMap != null) {
            maxDemand = costMapper.getMaxDemandValue(queryMap);
        }
        if (maxDemand != null)
            data.put("maxDemand", maxDemand);

        List<RateSectorBean> rates = costMapper.getPointRateInfo(pointId);// 取测量点费率信息
        if (rates != null && rates.size() > 0) {
            List<RateFeeBean> rateEles = new ArrayList<RateFeeBean>();
            if (rates.size() == 1) {// 单费率需要特殊处理，只关注总
                if (faqValue != null && rates != null && rates.size() > 0) {
                    RateFeeBean rf = new RateFeeBean();
                    rf.setSectorId(Long.toString(rates.get(0).getSectorId()));
                    rf.setSectorName(rates.get(0).getSectorName());
                    rf.setValue(faqValue);
                    rf.setPrice(rates.get(0).getRateValue());
                    rf.setFee(DoubleUtils.getDoubleValue(DataUtil.doubleMultiply(rf.getValue(), rf.getPrice()), 2));
                    rateEles.add(rf);
                }
            } else if(queryMap != null){
                List<RateFeeBean> qryRateEles = costMapper.getMonthRateEleValue(queryMap);// 取月分费率电量
                rateEles.addAll(qryRateEles);
            }

            Map<String, Object> basicPrice = null;
            if (rates != null && rates.size() > 0) {
                basicPrice = costMapper.getBasicFeePrice(rates.get(0).getRateId());
            }
            if (basicPrice != null) {
                if (basicPrice.get("VOLRATE") != null) {
                    data.put("volRate", basicPrice.get("VOLRATE"));
                }
                if (basicPrice.get("DERATE") != null) {
                    data.put("deRate", basicPrice.get("DERATE"));
                }

                if (!queryMap.keySet().contains("notCompBaseAjust")) {
                    calcuteBasicFee(basicPrice, volume, declareType, declareValue, pf, ratePF, maxDemand, rateEles,
                            isThisMonth);
                }
            }
            data.put("fee", rateEles);
        }
        data.put("isOnlyElecMeter", true);
        return data;
    }

    /**
     * 计算基本电费
     *
     * @param
     * @param volume       变压器容量
     * @param declareType  计算类型
     * @param declareValue 申报值
     * @param pf           功率因数
     * @param ratePF       功率因数对照值
     * @param maxDemand    最大需量
     * @param rateEles     费率电量电费
     */
    private void calcuteBasicFee(Map<String, Object> basicPrice, Long volume, Integer declareType, Double declareValue,
                                 Double pf, Double ratePF, Double maxDemand, List<RateFeeBean> rateEles, boolean isThisMonth) {
        if (rateEles.size() == 0)// 缺参数，不计算
            return;

        double bsFee = 0;
        if (declareType != null) {// 计算基本电费
            if (basicPrice != null && declareType == ONE && volume != null && basicPrice.get("VOLRATE") != null) {// 按容量计算
                RateFeeBean rateFeeBean = new RateFeeBean();
                rateFeeBean.setSectorName("基本");
                rateFeeBean.setFeeId(4);
                rateFeeBean.setValue(volume);
                rateFeeBean.setPrice(Double.valueOf(String.valueOf(basicPrice.get("VOLRATE"))));
                bsFee = DataUtil.doubleMultiply(rateFeeBean.getValue(), rateFeeBean.getPrice());
                rateFeeBean.setFee(plusThisMonthPer(isThisMonth, bsFee));
                rateEles.add(rateFeeBean);
            }
            if (basicPrice != null && declareType == TWO && declareValue != null && maxDemand != null
                    && basicPrice.get("DERATE") != null && basicPrice.get("DETH") != null) {// 按需量计算
                RateFeeBean rateFeeBean = new RateFeeBean();
                rateFeeBean.setSectorName("基本");
                rateFeeBean.setFeeId(4);
                rateFeeBean.setPrice(Double.valueOf(String.valueOf(basicPrice.get("DERATE"))));
                rateEles.add(rateFeeBean);
                double deth = DataUtil.doubleDivide(Double.valueOf(String.valueOf(basicPrice.get("DETH"))), 100); // 超罚限值
                BigDecimal btemp = BigDecimal.valueOf(deth).add(BigDecimal.ONE).multiply(BigDecimal.valueOf(declareValue));
                if (maxDemand <= btemp.doubleValue()) {// 月最大需量值<=申报值*（1+超罚限值）
                    if (maxDemand >= declareValue)
                        rateFeeBean.setValue(maxDemand);
                    else
                        rateFeeBean.setValue(declareValue);
                    bsFee = DataUtil.doubleAdd(rateFeeBean.getValue(), rateFeeBean.getPrice());
                    rateFeeBean.setFee(plusThisMonthPer(isThisMonth, bsFee));
                } else {
                    rateFeeBean.setValue(maxDemand);
                    bsFee = btemp.multiply(BigDecimal.valueOf(rateFeeBean.getPrice())).add(BigDecimal.valueOf(maxDemand).subtract(btemp)
                            .multiply(BigDecimal.valueOf(rateFeeBean.getPrice()).multiply(BigDecimal.valueOf(2)))).doubleValue();
                    rateFeeBean.setFee(plusThisMonthPer(isThisMonth, bsFee));
                }
            }
        }

        if (pf != null && ratePF != null) {// 计算力调电费
            RateFeeBean br = new RateFeeBean();
            br.setSectorName("力调");
            br.setValue(pf);
            br.setFeeId(5);
            br.setPrice(DoubleUtils.getDoubleValue(ratePF, 2));
            rateEles.add(br);

            double totalEleFee = 0;
            for (RateFeeBean r : rateEles) {
                totalEleFee = DataUtil.doubleAdd(totalEleFee, r.getFee());
            }
            br.setFee(DataUtil.doubleDivide(DataUtil.doubleMultiply(totalEleFee, br.getPrice()), 100));
        }
    }

    /**
     * 若为本月,则基本电费需要乘以相应日期比例
     */
    private double plusThisMonthPer(boolean isThisMonth, double fee) {
        if (isThisMonth) {
            String nowStr = DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN);
            String endStr = DateUtil.getMonthLastDay(nowStr);
            String[] nowArr = nowStr.split("-");
            String[] endArr = endStr.split("-");
            double x = DataUtil.doubleSubtract(Double.valueOf(nowArr[nowArr.length - 1]), 1);
            double y = Double.valueOf(endArr[endArr.length - 1]);
            fee = DataUtil.doubleDivide(DataUtil.doubleMultiply(fee, x), y, 2);
        }
        return DoubleUtils.getDoubleValue(fee, 2);
    }

    /**
     * 按需量计算基本电费
     *
     * @param maxDemand
     * @param deth
     * @param price
     * @param declareValue
     * @return
     */
    private double calculateMDFee(double maxDemand, double deth, double price, double declareValue) {
        double bsFee;
        //deth = DataUtil.doubleSubtract(deth, 100);
        deth = DataUtil.doubleDivide(deth, 100);
        if (maxDemand <= DataUtil.doubleMultiply(declareValue, DataUtil.doubleAdd(1, deth))) {// 月最大需量值<=申报值*（1+超罚限值）
            if (maxDemand >= declareValue)
                bsFee = DataUtil.doubleMultiply(maxDemand, price);
            else
                bsFee = DataUtil.doubleMultiply(declareValue, price);
        } else {
            bsFee = BigDecimal.valueOf(declareValue).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(deth)))
                    .multiply(BigDecimal.valueOf(price))

                    .add((BigDecimal.valueOf(maxDemand)
                            .subtract(BigDecimal.valueOf(declareValue).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(deth)))))
                            .multiply(BigDecimal.valueOf(price)).multiply(BigDecimal.valueOf(2)))
                    .doubleValue();
        }
        return DoubleUtils.getDoubleValue(bsFee, 2);
    }

    @Override
    public List<ChartItemWithTime> getMonMaxDemandChartData(Map<String, Object> queryMap) {
        List<ChartItemWithTime> list = new ArrayList<ChartItemWithTime>();

        int treeType = 0;               //树类型
        if (queryMap != null && queryMap.get("treeType") != null) {
            transDate(queryMap);// 处理日期
            treeType = Integer.valueOf(String.valueOf(queryMap.get("treeType")));
        }
        Map<String, Object> feeInfo = null;
        List<Map<String, Object>> demandInfo = null;
        if (queryMap != null && queryMap.get("pointId") != null &&  treeType == 1) {
            feeInfo = costMapper.getLedgerIdBasicFeeInfo(Long.valueOf(String.valueOf(queryMap.get("pointId"))));// 取容量、电价等基本信息
            demandInfo = costMapper.getLedgerBasicFeeInfo(queryMap);
        } else if(queryMap != null && queryMap.get("pointId") != null){
            feeInfo = costMapper.getBasicFeeInfo(Long.valueOf(String.valueOf(queryMap.get("pointId"))));// 取容量、电价等基本信息
            demandInfo = costMapper.getMeterBasicFeeInfo(queryMap);// 取需量申报等信息
        }
        List<Map<String, Object>> monthMaxDemand = costMapper.getMonthMaxDemand(queryMap);// 取月最大需量
        if (queryMap != null && queryMap.get("pointId") != null &&  treeType == 1) {
            List<Long> pointIds = costMapper .getAllottedPointIdsByLedgerId(Long.valueOf(String.valueOf(queryMap.get("pointId"))));
            if (pointIds.size() == 1) {// 分户下只配置一个测量点，取该测量点的信息
                Map<String, Object> tempLastMap = new HashMap<String, Object>();
                tempLastMap.put("pointId", pointIds.get(0));
                if (queryMap.get("beginTime") != null)
                    tempLastMap.put("beginTime", queryMap.get("beginTime"));
                if (queryMap.get("endTime") != null)
                    tempLastMap.put("endTime", queryMap.get("endTime"));
                tempLastMap.put("treeType", 2);
                monthMaxDemand = costMapper.getMonthMaxDemand(tempLastMap);
            }
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);

        ChartItemWithTime actItem = null;
        if (queryMap != null && queryMap.get("beginTime") != null && queryMap.get("endTime") != null)
            try {
                actItem = new ChartItemWithTime(TimeEnum.MONTH, "最大需量",
                        (sdf1.parse(String.valueOf(queryMap.get("beginTime")))).getTime() / 1000, (sdf1.parse(String.valueOf(queryMap.get("endTime")))).getTime() / 1000,
                        "-");
            } catch (ParseException e) {
                Log.error("时间类型转换错误",e.getMessage());
            }
        ChartItemWithTime decItem = null;
        if (queryMap != null && queryMap.get("beginTime") != null && queryMap.get("endTime") != null)
            try {
                decItem = new ChartItemWithTime(TimeEnum.MONTH, "参考值",
                        (sdf1.parse(String.valueOf(queryMap.get("beginTime")))).getTime() / 1000, (sdf1.parse(String.valueOf(queryMap.get("endTime")))).getTime() / 1000,
                        "-");
            } catch (ParseException e) {
                Log.error("时间类型转换错误",e.getMessage());
            }

        for (Map<String, Object> maxDemand : monthMaxDemand) {// 处理实际MD
            if(maxDemand != null && maxDemand.get("MAXFAD") != null && maxDemand.get("TIME") != null)
            actItem.getMap().put(String.valueOf(maxDemand.get("TIME")),
                    Double.valueOf(String.valueOf(maxDemand.get("MAXFAD")) == null ? "0" : String.valueOf(maxDemand.get("MAXFAD"))));
        }
        for (Map<String, Object> di : demandInfo) {// 取申报MD
            if (di != null) {
                if (di.get("DECLARETYPE") != null) {
                    int declareType = Integer.valueOf(String.valueOf(di.get("DECLARETYPE")));
                    if (declareType == ONE) {// 按容量计算(参考值=（容量电价/需量电价）*申报容量)
                        if (feeInfo != null && feeInfo.get("VOLRATE") != null && feeInfo.get("DERATE") != null
                                && feeInfo.get("VOLUME") != null) {
                            double volDir = 0;
                            if (di.get("DECLAREVALUE") != null) {
                                volDir = BigDecimal.valueOf(Double.valueOf(String.valueOf(feeInfo.get("VOLRATE"))))
                                        .divide(BigDecimal.valueOf(Double.valueOf(String.valueOf(feeInfo.get("DERATE")))), 2,
                                                BigDecimal.ROUND_DOWN)
                                        .multiply(BigDecimal.valueOf(Double.valueOf(String.valueOf(di.get("DECLAREVALUE")))))
                                        .doubleValue();
                            } else {
                                volDir = BigDecimal.valueOf(Double.valueOf(String.valueOf(feeInfo.get("VOLRATE"))))
                                        .divide(BigDecimal.valueOf(Double.valueOf(String.valueOf(feeInfo.get("DERATE")))), 2,
                                                BigDecimal.ROUND_DOWN)
                                        .multiply(BigDecimal.valueOf(Double.valueOf(String.valueOf(feeInfo.get("VOLUME")))))
                                        .doubleValue();
                            }
                            decItem.getMap().put(
                                    DateUtil.convertDateToStr((Date) di.get("TIME"), TimeEnum.MONTH.getDateFormat()),
                                    volDir);
                        }

                    } else if (declareType == TWO) {// 按需量
                        if (di.get("DECLAREVALUE") != null) {
                            decItem.getMap()
                                    .put(DateUtil.convertDateToStr(DateUtil.parseDate(String.valueOf(di.get("TIME")),DateUtil.DEFAULT_PATTERN) ,
                                            TimeEnum.MONTH.getDateFormat()),
                                            Double.valueOf(
                                                    String.valueOf(di.get("DECLAREVALUE")) == null ? "0" : String.valueOf(di.get("DECLAREVALUE"))));
                        }
                    }
                }
            }
        }

        list.add(decItem);
        list.add(actItem);
        return ChartConditionUtils.itemDataScale(list);
    }

    /**
     * 处理月MD分析页面的日期
     *
     * @param queryMap
     */
    private void transDate(Map<String, Object> queryMap) {
        int timeType = 0;
        if (queryMap != null && queryMap.get("timeType") != null) {
            timeType = Integer.valueOf(String.valueOf(queryMap.get("timeType")));
        }
        Date beginTime = null, endTime = null;
        int year = 0;

        switch (timeType){
            case 1:     // 去年
                year = DateUtil.getCurrentYear() - 1;
                beginTime = DateUtil.convertStrToDate(year + "-01-01", DateUtil.SHORT_PATTERN);
                endTime = DateUtil.convertStrToDate(year + "-12-31 23:59", DateUtil.MOUDLE_PATTERN);
                break;
            case 2:        // 今年
                year = DateUtil.getCurrentYear();
                beginTime = DateUtil.convertStrToDate(year + "-01-01", DateUtil.SHORT_PATTERN);
                endTime = DateUtil.convertStrToDate(year + "-12-31 23:59", DateUtil.MOUDLE_PATTERN);
                break;
            case 3:// 自定义
                if (queryMap != null && queryMap.get("beginTime")!=null && queryMap.get("endTime") != null) {
                    beginTime = DateUtil.convertStrToDate(queryMap.get("beginTime") + "-01", DateUtil.SHORT_PATTERN);
                    endTime = DateUtil.getMonthLastDay(DateUtil.convertStrToDate(queryMap.get("endTime") + "-01", DateUtil.SHORT_PATTERN));
                }
                break;
        }
        queryMap.put("beginTime", beginTime);
        queryMap.put("endTime", endTime);
    }

    @Override
    public Map<String, Object> getMonMDFeeData(Map<String, Object> queryMap) {
        Map<String, Object> data = new HashMap<String, Object>();

        int treeType = 0;           //树类型
        if (queryMap != null && queryMap.get("treeType") != null) {
            transDate(queryMap);// 处理日期
            treeType = Integer.valueOf(String.valueOf(queryMap.get("treeType")));
        }
        Map<String, Object> feeInfo = null;
        List<Map<String, Object>> demandInfo = null;
        boolean showCommonOnly = false;
        if ( queryMap != null && treeType == DemandDeclareController.TREETYPE && queryMap.get("pointId") != null) {
            LedgerBean lBean = ledgerManagerMapper.selectByLedgerId(Long.valueOf(String.valueOf(queryMap.get("pointId"))));
            if (lBean != null && lBean.getAnalyType() != ANALY_TYPE_102) {
                showCommonOnly = true;
            }
            feeInfo = costMapper.getLedgerIdBasicFeeInfo(Long.valueOf(String.valueOf(queryMap.get("pointId"))));
            demandInfo = declareService.getEveryLsatDeclare(queryMap);
        } else if(queryMap != null && queryMap.get("pointId") != null){
            showCommonOnly = true;
            feeInfo = costMapper.getBasicFeeInfo(Long.valueOf(String.valueOf(queryMap.get("pointId"))));// 取容量、电价等基本信息
            demandInfo = declareService.getEveryLsatDeclare(queryMap);// 取需量申报等信息
        }
        data.put("showCommonOnly", showCommonOnly);
        List<Map<String, Object>> monthMaxDemand = null;// 取月最大需量
        if (queryMap != null) {
            monthMaxDemand = costMapper.getMonthMaxDemand(queryMap);
        }
        if (treeType == ONE) {
            List<Long> pointIds = costMapper
                    .getAllottedPointIdsByLedgerId(Long.valueOf(String.valueOf(queryMap.get("pointId"))));
            if (pointIds.size() == 1) {// 分户下只配置一个测量点，取该测量点的信息
                Map<String, Object> tempLastMap = new HashMap<String, Object>();
                tempLastMap.put("pointId", pointIds.get(0));
                if (queryMap != null && queryMap.get("beginTime") != null)
                    tempLastMap.put("beginTime", queryMap.get("beginTime"));
                if (queryMap != null && queryMap.get("endTime") != null)
                    tempLastMap.put("endTime", queryMap.get("endTime"));
                tempLastMap.put("treeType", 2);
                monthMaxDemand = costMapper.getMonthMaxDemand(tempLastMap);
            }
        }

  , vo  Long volume = null;// 变压器容量
        int declareType = 0;// 申报类型;1,容量;2,需量；3、二者都有
        Double volPrice = null; // 容量电价
        Double demandPrice = null;// 需量电价
        Double demandThres = null;// 超罚限值
        if (feeInfo != null && feeInfo.size() > 0) {
            if (feeInfo.get("VOLUME") != null) {// 变压器容量
                volume = Long.valueOf(String.valueOf(feeInfo.get("VOLUME")));
                data.put("volume", volume);
            }
            if (feeInfo.get("VOLRATE") != null) {// 容量电价
                volPrice = Double.valueOf(String.valueOf(feeInfo.get("VOLRATE")));
                data.put("volPrice", volPrice);
            }
            if (feeInfo.get("DERATE") != null) {// 需量电价
                demandPrice = Double.valueOf(String.valueOf(feeInfo.get("DERATE")));
                data.put("demandPrice", demandPrice);
            }
            if (feeInfo.get("DETH") != null) {// 超罚限值
                demandThres = Double.valueOf(String.valueOf(feeInfo.get("DETH")));
                data.put("demandThres", demandThres);
            }
            if (feeInfo.get("NAME") != null) {// 计量点名称
                data.put("name", feeInfo.get("NAME"));
            }
        } else
            return data;

        Map<String, Map<String, Object>> di = procssDemandInfoDate(demandInfo);// 提取日期
        Map<Integer, List<MonthMDFeeBean>> mdFee = new HashMap<Integer, List<MonthMDFeeBean>>();
        int year;
        for (Map<String, Object> md : monthMaxDemand) {
            String time = String.valueOf(md.get("TIME"));
            year = Integer.valueOf(time.substring(0, 4));
            if (mdFee.get(year) == null)
                mdFee.put(year, new ArrayList<MonthMDFeeBean>());

            Map<String, Object> dInfo = di.get(time);// 取申报信息
            MonthMDFeeBean fee = new MonthMDFeeBean();
            if (md.get("MAXFAD") == null)
                continue;
            fee.setMonth(Integer.valueOf(time.substring(5)));
            fee.setMonthMD(Double.valueOf(String.valueOf(md.get("MAXFAD"))));
            if (dInfo != null && dInfo.size() > 0 && dInfo.get("DECLARETYPE") != null) {
                Integer dc = Integer.valueOf(String.valueOf(dInfo.get("DECLARETYPE")));
                if (dc != null) {
                    if (declareType == ZORE)
                        declareType = dc;
                    else if (declareType != dc)
                        declareType = 3;
                }

                if (dc == ONE) {
                    if (dInfo.get("DECLAREVALUE") != null && volPrice != null && demandPrice != null
                            && demandPrice != 0) {
                        Double declareValue = Double.valueOf(String.valueOf(dInfo.get("DECLAREVALUE")));
                        fee.setDeclareMD(
                                DataUtil.doubleDivide(DataUtil.doubleMultiply(volPrice, declareValue), demandPrice, 2));
                    } else if (volPrice != null && demandPrice != null && demandPrice != 0) {
                        fee.setDeclareMD(
                                DataUtil.doubleDivide(DataUtil.doubleMultiply(volPrice, volume), demandPrice, 2));
                    }
                } else if (dc == TWO) {
                    if (dInfo.get("DECLAREVALUE") != null)
                        fee.setDeclareMD(Double.valueOf(String.valueOf(dInfo.get("DECLAREVALUE"))));
                }
                if (fee.getDeclareMD() != null) {
                    fee.setMdDeviation(DoubleUtils
                            .getDoubleValue(DataUtil.doubleSubtract(fee.getMonthMD(), fee.getDeclareMD()), 2));
                    fee.setMdDeviationRate(DataUtil.doubleDivide(DataUtil.doubleMultiply(fee.getMdDeviation(), 100),
                            fee.getDeclareMD(), 2, BigDecimal.ROUND_HALF_DOWN));
                    if (volume != null && volPrice != null && dc == ONE)// 计算MD电费
                        fee.setMdFee(DataUtil.doubleMultiply(fee.getDeclareMD(), volPrice));
                    else if (dc == TWO && demandThres != null && demandPrice != null)
                        fee.setMdFee(calculateMDFee(fee.getMonthMD(), demandThres, demandPrice, fee.getDeclareMD()));
                }
            }
            mdFee.get(year).add(fee);
        }

        // 只有申报值也做显示
        for (String timeString : di.keySet()) {
            if (timeString != null && timeString.length() > 0) {
                year = Integer.valueOf(timeString.substring(0, 4));
                if (mdFee.get(year) == null)
                    mdFee.put(year, new ArrayList<MonthMDFeeBean>());
                boolean isPresent = false;
                for (MonthMDFeeBean monthMD : mdFee.get(year)) {
                    if (monthMD.getMonth() == Integer.valueOf(timeString.substring(5))) {
                        isPresent = true;
                        break;
                    }
                }
                if (isPresent)
                    continue;
                Map<String, Object> dInfo = di.get(timeString);// 取申报信息
                MonthMDFeeBean fee = new MonthMDFeeBean();
                fee.setMonth(Integer.valueOf(timeString.substring(5)));
                if (dInfo != null && dInfo.size() > 0) {
                    Integer dc = Integer.valueOf(String.valueOf(dInfo.get("DECLARETYPE")));
                    if (dc == ONE) {
                        if (dInfo.get("DECLAREVALUE") != null && volPrice != null && demandPrice != null
                                && demandPrice != 0) {
                            Double declareValue = Double.valueOf(String.valueOf(dInfo.get("DECLAREVALUE")));
                            fee.setDeclareMD(DataUtil.doubleDivide(DataUtil.doubleMultiply(volPrice, declareValue),
                                    demandPrice, 2, BigDecimal.ROUND_HALF_DOWN));
                        } else if (volPrice != null && demandPrice != null && demandPrice != 0) {
                            fee.setDeclareMD(DataUtil.doubleDivide(DataUtil.doubleMultiply(volPrice, volume),
                                    demandPrice, 2, BigDecimal.ROUND_HALF_DOWN));
                        }
                    } else if (dc == TWO) {
                        if (dInfo.get("DECLAREVALUE") != null)
                            fee.setDeclareMD(Double.valueOf(String.valueOf(dInfo.get("DECLAREVALUE"))));
                    }
                }
                mdFee.get(year).add(fee);
            }
        }
        data.put("declareType", declareType);
        data.put("mdFee", mdFee);
        return data;
    }

    /**
     * 近12个月，月最大需量分析
     */
    @Override
    public Map<String, Object> nearYearMonMaxAnalysis(Long pointId, int treeType) {
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> lastMap = new HashMap<String, Object>();
        lastMap.put("pointId", pointId);
        Date overTime = DateUtil.getMonthLastDay(new Date());
        Date nextTime = DateUtil.getDateBetween(overTime, 1);
        Date startTime = DateUtil.getLastYearDate(nextTime);
        lastMap.put("beginTime", startTime);
        lastMap.put("endTime", overTime);
        lastMap.put("treeType", treeType);
        List<Map<String, Object>> monMaxDemand = costMapper.getMonthMaxDemand(lastMap);
        if (treeType == DemandDeclareController.TREETYPE) {
            List<Long> pointIds = costMapper.getAllottedPointIdsByLedgerId(pointId);
            if (pointIds.size() == 1) {// 分户下只配置一个测量点，取该测量点的信息
                Map<String, Object> tempLastMap = new HashMap<String, Object>();
                tempLastMap.put("pointId", pointIds.get(0));
                tempLastMap.put("beginTime", lastMap.get("beginTime"));
                tempLastMap.put("endTime", lastMap.get("endTime"));
                tempLastMap.put("treeType", 2);
                monMaxDemand = costMapper.getMonthMaxDemand(tempLastMap);
            }
        }
        String showAnalysis = "1"; /// 为1表示显示最近一年最大需量分析，为0表示不显示
        String showTime = "0"; /// 为1表示显示"最大需量发生时间"这一行，为0表示不显示
        StringBuffer showTimeVal = new StringBuffer(); ///
        if (CollectionUtils.isEmpty(monMaxDemand) || monMaxDemand.get(0).get("MAXFAD") == null) {
            showAnalysis = "0";
        } else {
            Long minMonFad = ((BigDecimal) monMaxDemand.get(0).get("MAXFAD")).longValue();
            Long maxMonFad = ((BigDecimal) monMaxDemand.get(0).get("MAXFAD")).longValue();
            Long sum = 0L;
            int size = monMaxDemand.size();
            for (int i = 0; i < size; i++) {
                Long maxfad = ((BigDecimal) monMaxDemand.get(i).get("MAXFAD")).longValue();
                // 计算最大、最小值、总和
                if (maxfad > maxMonFad) {
                    maxMonFad = maxfad;
                }
                if (maxfad < minMonFad) {
                    minMonFad = maxfad;
                }
                sum = sum + maxfad;
            }
            result.put("minMonFad", minMonFad);/// 近一年月最大需量 最小值
            result.put("maxMonFad", maxMonFad);/// 近一年月最大需量 最大值
            Long avgMonFad = sum / size;
            result.put("avgMonFad", avgMonFad);/// 近一年月最大需量的平均值

            Map<String, Object> feeInfo = null;
            if (treeType == DemandDeclareController.TREETYPE) {
                feeInfo = costMapper.getLedgerIdBasicFeeInfo(pointId);// 取容量、电价等基本信息
            } else {
                feeInfo = costMapper.getBasicFeeInfo(pointId);// 取容量、电价等基本信息
            }

            Long percent = 0L;
            Double volVsDemand = 0D;
            if (feeInfo != null && feeInfo.size() > 0) {
                if (feeInfo.get("VOLUME") != null) {
                    Long volume = Long.valueOf(String.valueOf(feeInfo.get("VOLUME")));
                    if (volume != 0) {
                        percent = 100 * avgMonFad / volume;
                    }
                }
                if (feeInfo.get("VOLRATE") != null && feeInfo.get("DERATE") != null) {
                    Double volPrice = Double.valueOf(String.valueOf(feeInfo.get("VOLRATE")));
                    Double demandPrice = Double.valueOf(String.valueOf(feeInfo.get("DERATE")));
                    if (demandPrice != 0) {
                        volVsDemand = BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(volPrice))
                                .divide(BigDecimal.valueOf(demandPrice), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();// 容量电价和需量电价百分比
                    }
                }
            }
            result.put("percent", percent);/// 平均值占变压器容量百分比
            int bestDeclare = 0;
            if (percent > volVsDemand) {
                bestDeclare = 1;
            } else {
                bestDeclare = 2;
            }
            // 当前申报类型;1,容量;2,需量
            int declareType = 0;
            String nowStr = DateUtil.convertDateToStr(new Date(), DateUtil.MONTH_PATTERN);
            List<Integer> declareTypeList = null;
            if (treeType == DemandDeclareController.TREETYPE) {
                declareTypeList = this.costMapper.getCurrentLedgerDeclareType(pointId, nowStr);
            } else {
                declareTypeList = this.costMapper.getCurrentDeclareType(pointId, nowStr);
            }
            if (declareTypeList != null && declareTypeList.size() > 0) {
                declareType = declareTypeList.get(0);
            }
            result.put("declareType", declareType);
            result.put("bestDeclare", bestDeclare);/// 最优申报类型
            if (size >= 3) {
                // 近12个月,月最大需量的MAX_FAD_TIME的list（按小时分钟排序）
                List<Map<String, Object>> maxfadTimeList = null;
                if (treeType == DemandDeclareController.TREETYPE) {
                    List<Long> pointIds = costMapper.getAllottedPointIdsByLedgerId(pointId);
                    if (pointIds.size() == 1) {// 分户下只配置一个测量点，取该测量点的信息
                        Map<String, Object> tempLastMap = new HashMap<String, Object>();
                        tempLastMap.put("pointId", pointIds.get(0));
                        tempLastMap.put("beginTime", lastMap.get("beginTime"));
                        tempLastMap.put("endTime", lastMap.get("endTime"));
                        tempLastMap.put("treeType", 2);
                        maxfadTimeList = costMapper.monthMaxFadTime(tempLastMap);
                    } else {
                        maxfadTimeList = costMapper.getLedgerMonthMaxFadTime(lastMap);
                    }
                } else {
                    maxfadTimeList = costMapper.monthMaxFadTime(lastMap);
                }
                int timeSize = maxfadTimeList.size();
                int num = timeSize / 2 + timeSize % 2;
                // 扫描最近12个月最大需量发生时间，查找最大值最多的1小时时间段且大于50%的覆盖度，
                // 分析就输出：集中发生在某个时间段，否则最大需量发生时间这一行不显示
                for (int i = 0; (i < timeSize) && (i + num - 1 < timeSize); i++) {
                    if (maxfadTimeList.get(i).get("FAD_TIME") == null
                            || maxfadTimeList.get(i + num - 1).get("FAD_TIME") == null) {
                        continue;
                    }
                    String timeBegin = String.valueOf(maxfadTimeList.get(i).get("FAD_TIME"));
                    String timeEnd = String.valueOf(maxfadTimeList.get(i + num - 1).get("FAD_TIME"));
                    int interval = getMinuteInterval(timeBegin, timeEnd);
                    if (interval <= 60) {
                        showTime = "1";
                        for (int j = i + num; j < timeSize; j++) {
                            String endTemp = String.valueOf(maxfadTimeList.get(j).get("FAD_TIME"));
                            int intervalTemp = getMinuteInterval(timeBegin, endTemp);
                            if (intervalTemp <= 60) {
                                timeEnd = endTemp;
                            }
                        }
                        showTimeVal.append(showTimeVal);
                        showTimeVal.append(timeBegin);
                        showTimeVal.append("--");
                        showTimeVal.append(timeEnd);
                        break;
                    }
                }
            }
        }
        result.put("showAnalysis", showAnalysis);
        result.put("showTime", showTime);
        result.put("showTimeVal", showTimeVal);
        return result;
    }

    /**
     * 计算开始时间到结束时间的差值
     * @param timeBegin
     * @param timeEnd
     * @return int:interval
     */
    private int getMinuteInterval(String timeBegin, String timeEnd) {
        int interval = 0;
        if (timeBegin == null || timeEnd == null) {
            return interval;
        }
        String[] timeBeginStr = timeBegin.split(":");     //开始时间字符串
        String[] timeEndStr = timeEnd.split(":");          //结束时间字符串
        int bTimeH = Integer.valueOf(timeBeginStr[0]);
        int bTimeM = Integer.valueOf(timeBeginStr[1]);
        int eTimeH = Integer.valueOf(timeEndStr[0]);
        int eTimeM = Integer.valueOf(timeEndStr[1]);
        interval = (eTimeH - bTimeH) * 60 + (eTimeM - bTimeM);
        return Math.abs(interval);
    }

    /**
     * 将日期提取出来便于后面处理
     *
     * @param demandInfo
     * @return
     */
    private Map<String, Map<String, Object>> procssDemandInfoDate(List<Map<String, Object>> demandInfo) {
        Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> di : demandInfo) {
            if (di != null) {
                Date time = (Date) di.get("TIME");
                data.put(DateUtil.convertDateToStr(time, "yyyy-MM"), di);
            }
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getPFEval(Map<String, Object> queryMap) {
        Map<String, Object> data = new HashMap<String, Object>();
        // 对象类型：1-EMO;2-DCP;
        Integer oType = null;
        long objectId = 0;
        Date startDate = null;
        Date endDate = null;
        if (queryMap != null) {
            if (queryMap.get("oType") != null)
                oType = Integer.valueOf(String.valueOf(queryMap.get("oType")));
            if (queryMap.get("objectId") != null)
                objectId = Long.valueOf(String.valueOf(queryMap.get("objectId")));
            if (queryMap.get("beginTime") != null)
                startDate = DateUtil.convertStrToDate(String.valueOf(queryMap.get("beginTime")), DateUtil.SHORT_PATTERN);
            if (queryMap.get("endTime") != null) endDate = DateUtil
                    .setDateEnd(DateUtil.convertStrToDate(String.valueOf(queryMap.get("endTime")), DateUtil.SHORT_PATTERN));
        }

        Double monq = 0d;
        Double monrq = 0d;
        int oflag = getOflag(oType, objectId);
        data.put("oflag", oflag);
        long ledgerId = 0;
        if (oType == ONE) {
            ledgerId = objectId;// EMO
            monq = costMapper.getLedgerMonQEnd(objectId, startDate, endDate);// 当前日期有功总
            monrq = costMapper.getLedgerMonRQEnd(objectId, startDate, endDate);// 当前日期无功总
        } else {// DCP
            monq = costMapper.getMeterMonQEnd(objectId, startDate, endDate);// 当前日期有功总
            monrq = costMapper.getMeterMonRQEnd(objectId, startDate, endDate);// 当前日期无功总
        }

        double currPF = DataUtil.getPF(monq, monrq);
        data.put("currPF", currPF);

        Date nDate = WebConstant.getChartBaseDate();
        Date preMon1 = DateUtil.getPreMonthFristDay(nDate);
        Date preMon2 = DateUtil.getPreMonthLastDay(nDate);
        Date currMon1 = DateUtil.getCurrMonthFirstDay(nDate);

        Double preq = 0D;
        Double prerq = 0D;
        if (oType == ONE) {// EMO
            preq = costMapper.getLedgerMonQEnd(objectId, preMon1, currMon1);// 上月有功总
            prerq = costMapper.getLedgerMonRQEnd(objectId, preMon1, currMon1);// 上月无功总
        } else {
            preq = costMapper.getMeterMonQEnd(objectId, preMon1, currMon1);// 上月有功总
            prerq = costMapper.getMeterMonRQEnd(objectId, preMon1, currMon1);// 上月无功总
        }
        double prePF = DataUtil.getPF(preq, prerq);
        data.put("preMonPF", prePF);

        Map<String, Object> parameterMap = new HashMap<String, Object>();       //封装参数map
        List<Long> meters = new ArrayList<Long>();
        if (oType == ONE) {// EMO
            meters = costMapper.getLedgerMeters(objectId);
        } else {
            meters.add(objectId);
        }
        if (meters.size() == 0)
            return data;

        parameterMap.put("beginTime", startDate);// 电费信息时间、月最大需量时间
        parameterMap.put("endTime", endDate);
        parameterMap.put("sTime", startDate);// 月总电量时间
        parameterMap.put("eTime", endDate);
        parameterMap.put("strBeginTime", DateUtil.convertDateToStr(startDate, DateUtil.SHORT_PATTERN));// 月分费率电量时间
        parameterMap.put("strEndTime", DateUtil.convertDateToStr(endDate, DateUtil.SHORT_PATTERN));

        Map<String, Object> preQM = new HashMap<String, Object>();
        preQM.put("beginTime", preMon1);// 电费信息时间、月最大需量时间
        preQM.put("endTime", preMon2);
        preQM.put("sTime", preMon1);// 月总电量时间
        preQM.put("eTime", preMon2);
        preQM.put("strBeginTime", DateUtil.convertDateToStr(preMon1, DateUtil.SHORT_PATTERN));// 月分费率电量时间
        preQM.put("strEndTime", DateUtil.convertDateToStr(DateUtil.addDateDay(preMon2, 1), DateUtil.SHORT_PATTERN));

        double currFee = 0, preFee = 0;
        Map<String, Object> fees;
        for (long pointId : meters) {
            Integer addAttr = 0;
            Integer pct = 0;
            if (oType == ONE) {// EMO
                List<Map<String, Object>> list = this.meterManagerMapper.getLedgerMeter(ledgerId, pointId);
                if (list != null && list.size() > 0) {
                    Map<String, Object> map = list.get(0);
                    addAttr = Integer.valueOf(String.valueOf(map.get("ADD_ATTR")));
                    pct = Integer.valueOf(String.valueOf(map.get("PCT")));
                }
            }

            if (currPF != 0) {
                parameterMap.put("pointId", pointId);
                fees = calAllEleFee(parameterMap);
                if (fees.get("stdPF") != null)
                    data.put("stdPF", fees.get("stdPF"));
                List<RateFeeBean> rb = (List<RateFeeBean>) fees.get("fee");
                double fee = 0;
                if (rb != null && rb.size() > 0) {
                    for (RateFeeBean r : rb) {
                        if (r.getFeeId() == FIVE) {
                            fee = DataUtil.doubleAdd(fee, r.getFee());
                        }
                    }
                }
                if (addAttr != 0) {
                    currFee = BigDecimal.valueOf(fee).multiply(BigDecimal.valueOf(addAttr)).multiply(BigDecimal.valueOf(pct))
                            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_DOWN).add(BigDecimal.valueOf(currFee))
                            .doubleValue();
                } else {
                    currFee = DataUtil.doubleAdd(currFee, fee);
                }
            }

            if (prePF != 0) {
                preQM.put("pointId", pointId);
                fees = calAllEleFee(preQM);
                List<RateFeeBean> rb = (List<RateFeeBean>) fees.get("fee");
                double fee = 0;
                if (rb != null && rb.size() > 0) {
                    for (RateFeeBean r : rb) {
                        if (r.getFeeId() == FIVE) {
                            fee = DataUtil.doubleAdd(fee, r.getFee());
                        }
                    }
                }
                if (addAttr != 0) {
                    preFee = BigDecimal.valueOf(fee).multiply(BigDecimal.valueOf(addAttr)).multiply(BigDecimal.valueOf(pct))
                            .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_DOWN).add(BigDecimal.valueOf(preFee))
                            .doubleValue();
                } else {
                    preFee = DataUtil.doubleAdd(preFee, fee);
                }
            }
        }

        if (currFee != 0)
            data.put("currFee", DoubleUtils.getDoubleValue(currFee, 2));
        if (preFee != 0)
            data.put("preFee", DoubleUtils.getDoubleValue(preFee, 2));

        return data;
    }

    /**
     * 得到对象类型：1-企业或者主变压器；0-其他
     *
     * @param oType    1-EMO;2-DCP
     * @param objectId
     * @return
     */
    private int getOflag(Integer oType, long objectId) {
        if (oType == ONE) {// EMO
            LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(objectId);
            if (ledger != null && ledger.getAnalyType() == ANALY_TYPE_102) {// 企业名
                return 1;
            } else {
                return 0;
            }
        } else {// DCP
            MeterBean meter = this.meterManagerMapper.getMeterDataByPrimaryKey(objectId);
            if (meter != null && meter.getVolType() != null && meter.getVolType() == ONE) {// 主变
                return 1;
            } else {
                return 0;
            }
        }
    }


    /**
     * 计算电费(容需对比)
     * @param queryMap
     * @return
     */
    @Override
    public Map<String, Object> calEmoDcpEleFee_new(Map<String, Object> queryMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 判断是否是计算本月，若为本月,则基本电费需要乘以相应日期比例
        boolean isThisMonth = false;
        String beginStr = null;
        if (queryMap != null && queryMap.get("baseTime") != null) {
            beginStr = String.valueOf(queryMap.get("baseTime"));
        }
        String nowStr = DateUtil.getCurrentDateStr(DateUtil.MONTH_PATTERN);

        if (beginStr.equals(nowStr)) {
            isThisMonth = true;
        }
        // 处理传参
        Integer objType = null;
        if (queryMap != null && queryMap.get("objType") != null)
            objType = Integer.valueOf(String.valueOf(queryMap.get("objType")));
        Long objId = null;
        if (queryMap != null && queryMap.get("objId") != null)
            objId = Long.valueOf(String.valueOf(queryMap.get("objId")));
        String baseTime = null;
        if (queryMap != null && queryMap.get("baseTime") != null)
            baseTime = queryMap.get("baseTime") + "-01";
        Date beginTime = DateUtil.convertStrToDate(baseTime, DateUtil.SHORT_PATTERN);
        Date endTime = DateUtil.getMonthLastDay(beginTime);
        queryMap.put("beginTime", beginTime);// 电费信息时间、月最大需量时间
        queryMap.put("endTime", endTime);
        queryMap.put("strBeginTime", baseTime);// 月分费率电量时间
        queryMap.put("strEndTime",
                DateUtil.convertDateToStr(DateUtil.getNextMonthDate(beginTime), DateUtil.SHORT_PATTERN));
        queryMap.put("sTime", beginTime);// 月总电量时间
        queryMap.put("eTime", endTime);
        // 采集点、分户分开计算
        ///// 修改为采集点不计算
        if (objType == TWO) {
            MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(objId);
            if (Integer.valueOf(meterBean.getMeterType()) == ONE) {
                if (meterBean.getVolType() == null || meterBean.getVolType() != 1) {
                    queryMap.put("notCompBaseAjust", true); // 不计算基本、力调
                }
                queryMap.put("pointId", objId);
                if(queryMap != null)
                    result = calAllEleFee(queryMap);
            } else {
                queryMap.put("pointId", objId);
                queryMap.put("meterType", meterBean.getMeterType());
                if(queryMap != null)
                    result = calOtherEnergyFee(queryMap);
            }
        } else if (objType == ONE) {
            LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(objId);
            List<Short> meterTypes = null;
            if (ledger != null)
                meterTypes = costMapper.getComputeMeterTypes(objId, ledger.getAnalyType());
            List<RateFeeBean> rateEles = new ArrayList<RateFeeBean>();// 电计算

            queryMap.put("ledgerId", objId);
            queryMap.put("analyType", ledger.getAnalyType());
            if (meterTypes != null && meterTypes.contains(new Short("1"))) {
                // 将复费率加到rateEles中
                List<RateFeeBean> rateFeeBeans = this.costMapper.getEmoMonthRateEleValue(queryMap); // 分户复(分)费率电量、电费
                // 尖峰平谷
                if (rateFeeBeans != null && rateFeeBeans.size() > 0) {
                    rateEles.addAll(rateFeeBeans);
                }
                // 将单费率加到rateEles中
                //RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValue(queryMap); // 分户单费率电量、电费
                RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValueNew(queryMap); // 分户单费率电量、电费
                if (singleFeeBean.getValue() != 0 || singleFeeBean.getFee() != 0) {
                    if (rateEles.size() > 0) {
                        for (int i = 0; i < rateEles.size(); i++) {
                            RateFeeBean temp = rateEles.get(i);
                            if (temp.getSectorId().equals(singleFeeBean.getSectorId())) {
                                temp.setValue(DataUtil.doubleAdd(temp.getValue(), singleFeeBean.getValue()));
                                temp.setFee(DataUtil.doubleAdd(temp.getFee(), singleFeeBean.getFee()));
                                rateEles.set(i, temp);
                            }
                        }
                    } else if (rateEles.size() == 0) {
                        rateEles.add(singleFeeBean);
                    }
                }

                // 容量需量相关
                Map<String, Object> volDemand = processLedgerDemand(queryMap);
                Integer declareType = null;
                if (volDemand != null && volDemand.keySet().contains("declareType") && volDemand.get("declareType") != null) {
                    declareType = Integer.valueOf(String.valueOf(volDemand.get("declareType")));
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("beginTime", beginTime);
                map.put("meterId", objId);
                map.put("flag", 0);        //此属性为选择sql语句条件,0为当前月,1为当月以前

                // 判断当月有没有用户的需量申报和容量申报
                Map<String, Object> emoDecalreByIdAndBeginTime = declareService.getEmoDecalreByIdAndBeginTime(map);

                // 此处做为如果按时间查询没有数据的话就查询历史申报的最后一次记录
                if (emoDecalreByIdAndBeginTime == null) {
                    map.put("flag", 1);
                    emoDecalreByIdAndBeginTime = declareService.getEmoDecalreByIdAndBeginTime(map);
                }

                // 如果依然为空则证明该用户从未申报过,按照最大容量计算
                if (emoDecalreByIdAndBeginTime == null || emoDecalreByIdAndBeginTime.size() == 0)
                    // 此方法作为没有需量申报也没有容量申报时使用
                    if (ledger.getAnalyType() == 102) {
                        RateFeeBean capacityFee = this.costMapper.getEmoCapacityFee(queryMap);
                        if (capacityFee != null)
                            capacityFee.setFee(plusThisMonthPer(isThisMonth, capacityFee.getFee()));
                        rateEles.add(capacityFee);
                    }

                // 将"容量申报"时的"基本电费"
                // 此处做为计算有容量申报的电费
                if (ledger.getAnalyType() == ANALY_TYPE_102 && emoDecalreByIdAndBeginTime != null) {
                    if ((declareType != null && declareType == 1)
                            || (emoDecalreByIdAndBeginTime.get("DECLARETYPE") != null && Integer
                            .valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLARETYPE"))) == 1)) {
                        double bsFee = 0;
                        RateFeeBean rf = new RateFeeBean(null, "基本", 4, 0, 0);
                        if (volDemand != null && volDemand.get("ledgerPrice") != null
                                && emoDecalreByIdAndBeginTime.get("DECLAREVALUE") != null) {
                            Map<String, Object> ledgerPrice = null;
                            if (volDemand.get("ledgerPrice") != null)
                                ledgerPrice = (Map<String, Object>) volDemand.get("ledgerPrice");
                            rf.setPrice(Double.valueOf(String.valueOf(ledgerPrice.get("DERATE"))));
                            Double declareValue = Double
                                    .valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLAREVALUE")));
                            rf.setValue(declareValue);
                            bsFee = DataUtil.doubleMultiply(rf.getValue(), rf.getPrice());
                            rf.setFee(plusThisMonthPer(isThisMonth, bsFee));
                        }
                        rateEles.add(rf);
                    }
                }

                // 功率因数
                double pf = 0;
                Map<String, Object> fq = costMapper.getLedgerMonthTotalEleValue(ledger.getLedgerId(),
                        ledger.getAnalyType(), beginTime, DateUtil.getMonthLastDay(beginTime));
                // EMO有功总、EMO无功总
                if (fq != null) {
                    result.put("faqValue", Double.valueOf(String.valueOf(fq.get("FAQVALUE"))));
                    result.put("frqValue", Double.valueOf(String.valueOf(fq.get("FRQVALUE"))));
                    pf = DataUtil.getPF(Double.valueOf(String.valueOf(fq.get("FAQVALUE"))),
                            Double.valueOf(String.valueOf(fq.get("FRQVALUE"))));
                    result.put("pf", pf);
                }
                // 标准功率因数
                Double std = costMapper.getEmoThresholdValue(objId);
                if (std != null) {
                    result.put("stdPF", std);
                }
                // 查调整电费幅度
                Double ratePF = null;
                if (std != null) {
                    ratePF = costMapper.getFactor(DataUtil.doubleSubtract(pf, 0.01), std);
                    if (ratePF != null) {
                        result.put("ratePF", ratePF);
                    }
                }

                // 将"需量申报"时的"基本电费"、"力调电费"
                // 此处做为有需量申报时的计算电费
                 if (ledger.getAnalyType() == ANALY_TYPE_102) {
                    result.putAll(volDemand);

                    if (emoDecalreByIdAndBeginTime != null && emoDecalreByIdAndBeginTime.get("DECLARETYPE") != null
                            && Integer.valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLARETYPE"))) == 2) {
                        double bsFee = 0;
                        RateFeeBean rf = new RateFeeBean(null, "基本", 4, 0, 0);
                        if (volDemand.get("ledgerPrice") != null && emoDecalreByIdAndBeginTime.keySet().contains("DECLAREVALUE")
                                && emoDecalreByIdAndBeginTime.get("DECLAREVALUE") != null) {
                            Map<String, Object> ledgerPrice = (Map<String, Object>) volDemand.get("ledgerPrice");
                            Double declareValue = Double.valueOf(String.valueOf(emoDecalreByIdAndBeginTime.get("DECLAREVALUE")));
                            if (ledgerPrice.get("DERATE") != null && ledgerPrice.get("DETH") != null) {

                                rf.setPrice(Double.valueOf(String.valueOf(ledgerPrice.get("DERATE"))));
                                rf.setValue(declareValue);
                                bsFee = DataUtil.doubleMultiply(rf.getValue(), rf.getPrice());
                                rf.setFee(plusThisMonthPer(isThisMonth, bsFee));
                            }
                        }
                        rateEles.add(rf);
                    }

                    if (ratePF != null) { // 力调电费
                        RateFeeBean br = new RateFeeBean();
                        br.setSectorName("力调");
                        br.setValue(pf);
                        br.setFeeId(5);
                        br.setPrice(DoubleUtils.getDoubleValue(ratePF, 2));
                        double totalEleFee = 0;
                        for (RateFeeBean r : rateEles) {
                            totalEleFee = DataUtil.doubleAdd(totalEleFee, r.getFee());
                        }
                        br.setFee(DataUtil.doubleDivide(DataUtil.doubleMultiply(totalEleFee, br.getPrice()), 100));
                        rateEles.add(br);
                    }
                }
            }

            Integer queryType = 0;
            if (queryMap.get("queryType") != null) {
                queryType = Integer.valueOf(String.valueOf(queryMap.get("queryType")));
            }


            // add or update method by catkins.
            // date 2020/9/22
            // Modify the content:
            // 此方法作为没有需量申报也没有容量申报时使用(用于保存一个容量的电费信息)
            if (ledger.getAnalyType() == 102) {
                RateFeeBean capacityFee = this.costMapper.getEmoCapacityFee_new(queryMap);
                if (capacityFee != null)
                    capacityFee.setFee(plusThisMonthPer(isThisMonth, capacityFee.getFee()));
                rateEles.add(capacityFee);
            }
            //end
            if (meterTypes != null && ((meterTypes.size() == 1 && meterTypes.get(0) == 1) || queryType == 1)) {// 电费
                result.put("fee", rateEles);
            } else if (meterTypes != null) {
                List<Map<String, Object>> feeMapList = new ArrayList<Map<String, Object>>();
                for (Short meterType : meterTypes) {
                    Map<String, Object> feeMap = new HashMap<String, Object>();
                    feeMap.put("meterType", meterType);         //meterType 表机类型
                    Double monthValue = 0d;
                    if (Integer.valueOf(meterType) == ONE) {
                        List<RateFeeBean> newRateEles = new ArrayList<RateFeeBean>();// 不包含基本和力调电费
                        // 将复费率加到rateEles中
                        List<RateFeeBean> rateFeeBeans = this.costMapper.getEmoMonthRateEleValue(queryMap); // 分户复(分)费率电量、电费
                        if (rateFeeBeans != null && rateFeeBeans.size() > 0) {
                            newRateEles.addAll(rateFeeBeans);
                        }
                        // 将单费率加到rateEles中
                        RateFeeBean singleFeeBean = this.costMapper.getEmoMonthSingleEleValue(queryMap); // 分户单费率电量、电费
                        if (singleFeeBean.getValue() != 0 || singleFeeBean.getFee() != 0) {
                            if (newRateEles.size() > 0) {
                                for (int i = 0; i < newRateEles.size(); i++) {
                                    RateFeeBean temp = newRateEles.get(i);
                                    if (temp != null && temp.getSectorId().equals(singleFeeBean.getSectorId())) {
                                        temp.setValue(DataUtil.doubleAdd(temp.getValue(), singleFeeBean.getValue()));
                                        temp.setFee(DataUtil.doubleAdd(temp.getFee(), singleFeeBean.getFee()));
                                        newRateEles.set(i, temp);
                                    }
                                }
                            } else if (newRateEles.size() == 0) {
                                newRateEles.add(singleFeeBean);
                            }
                        }
                        double unitPrice = 1;// 单价
                        double price = 0;
                        for (RateFeeBean feeBean : newRateEles) {
                            price = DataUtil.doubleAdd(DoubleUtils.getDoubleValue(feeBean.getFee(), 0), price);
                            monthValue = DataUtil.doubleAdd(feeBean.getValue(), monthValue);
                            unitPrice = feeBean.getPrice();// 取其中一个单价，页面emo隐藏单价无影响
                        }
                        feeMap.put("itemName", "电");
                        feeMap.put("value", monthValue);
                        feeMap.put("unitPrice", unitPrice);
                        feeMap.put("price", price);
                        feeMapList.add(feeMap);
                    } else {
                        queryMap.put("meterType", meterType);
                        List<Map<String, Object>> monEles = costMapper.getMonthEleValue(queryMap);// 取月总用量
                        if (monEles != null && monEles.size() > 0) {
                            Map<String, Object> monEle = monEles.get(0);
                            if (monEle.get("ELE_VALUE") != null) {
                                monthValue = Double.valueOf(String.valueOf(monEle.get("ELE_VALUE")));
                            }
                        }
                        MeterBean meterBean = new MeterBean();
                        meterBean.setLedgerId(objId);
                        meterBean.setMeterType(meterType);

                        double unitPrice = 0;
                        double price = 0;

                        switch (meterType){ //meterType 表机类型
                            case 2:     //水
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "水");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            case 3:     //气
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "气");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            case 4:     //热
                                unitPrice = calOtherEnergyRateValueWithMeter(meterBean);// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "热");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                            default:       //电
                                unitPrice = 1;// 单价
                                price = DataUtil.doubleMultiply(monthValue, unitPrice);// 单价
                                feeMap.put("itemName", "电");
                                feeMap.put("value", monthValue);
                                feeMap.put("unitPrice", unitPrice);
                                feeMap.put("price", price);
                                feeMapList.add(feeMap);
                                break;
                        }
                    }
                }
                result.put("fee", feeMapList);
            }
        }
        return result;
    }
}
