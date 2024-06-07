package com.linyang.energy.service.impl;

import com.leegern.util.NumberUtil;
import com.linyang.energy.dto.HolidayBean;
import com.linyang.energy.mapping.demand.DemandMapper;
import com.linyang.energy.mapping.energydataquery.EventQueryMapper;
import com.linyang.energy.service.DemandService;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OfficialHolidayUtils;
import com.linyang.energy.utils.SequenceUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;import java.util.*;

/**
 * Created by Administrator on 16-9-7.
 */
@Service
public class DemandServiceImpl implements DemandService{

    @Autowired
    private DemandMapper demandMapper;

    @Autowired
    private EventQueryMapper eventQueryMapper;

    @Override
    public List<Map<String, Object>> getCompLedgerList(Long ledgerId){
        List<Map<String, Object>> list = this.demandMapper.getCompLedgerList(ledgerId);
        return list;
    }

    @Override
    public Map<String,Object> getLedgerDemandMethod(Long ledgerId){
        Map<String,Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> demands = this.demandMapper.getLedgerDemandMethod(ledgerId);
        if(demands != null && demands.size() > 0){
            result.put("demands", demands);
        }

        return result;
    }

    @Override
    public Map<String,Object> getDetailsByDemand(Long planId){
        Map<String,Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> relations = this.demandMapper.getDetailsByDemand(planId);
        if(relations != null && relations.size() > 0){
            result.put("relations", relations);
        }

        return result;
    }

    @Override
    public Map<String, Object> insertUpdateDemand(Map<String, Object> demandInfo){
        Map<String, Object> result = new HashMap<String, Object>();

        Long ledgerId = Long.valueOf(demandInfo.get("ledgerId").toString());
        Long planId = Long.valueOf(demandInfo.get("planId").toString());
        String planName = demandInfo.get("planName").toString();
        List<Map<String, Object>> relationList = (List<Map<String, Object>>) demandInfo.get("relationList");

        //先检查名称是否重复
        int num = this.demandMapper.getDemandNumBy(planId, ledgerId, planName);
        if(num > 0){
            result.put("message", "方案名称重复");
            return result;
        }
        if(planId == 0){
            result.put("message", "请先点击'新增方案'或‘选择方案’");
            return result;
        }
        if(planId < 0){ //新增

            //T_RESPONSE_PLAN
            planId = SequenceUtils.getDBSequence();
            this.demandMapper.insertDemand(planId,planName,ledgerId);
            //T_RESPONSE_PLAN_CONFIG
            if(relationList != null && relationList.size() > 0){
                for(int i = 0; i < relationList.size(); i++){
                    Map<String, Object> temp = relationList.get(i);
                    Integer type = Integer.valueOf(temp.get("type").toString());
                    Long id = Long.valueOf(temp.get("id").toString());
                    this.demandMapper.insertDemandRelation(planId,type,id);
                }
            }
        }
        else if(planId > 0){ //修改

            //T_RESPONSE_PLAN
            this.demandMapper.updateDemand(planId, planName, ledgerId);
            //T_RESPONSE_PLAN_CONFIG
            this.demandMapper.deleteDemandRelation(planId);
            if(relationList != null && relationList.size() > 0){
                for(int i = 0; i < relationList.size(); i++){
                    Map<String, Object> temp = relationList.get(i);
                    Integer type = Integer.valueOf(temp.get("type").toString());
                    Long id = Long.valueOf(temp.get("id").toString());
                    this.demandMapper.insertDemandRelation(planId,type,id);
                }
            }
        }
        result.put("planId", planId);
        result.put("message", "");
        return result;
    }

    @Override
    public void deleteDemand(Long planId){
        //删除T_RESPONSE_PLAN
        this.demandMapper.deleteDemand(planId);
        //T_RESPONSE_PLAN_CONFIG
        this.demandMapper.deleteDemandRelation(planId);
    }

    @Override
    public Map<String, Object> getLastWorkDayDefaultTime(Long ledgerId){
        Map<String, Object> result = new HashMap<String, Object>();

        String temp =  DateUtil.addDay(DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN), -1);
        int num = 0;
        while(num < 1){
            if(!ifIsRestDay(ledgerId, temp) ){
                String begin = temp + " " + "14:00";
                String end = temp + " " + "15:30";
                result.put("beginTime", begin);
                result.put("endTime", end);

                num++;
            }
            temp = DateUtil.addDay(temp, -1);
        }
        return result;
    }

    @Override
    public Map<String,Object> getDemandResponse(Long ledgerId, Long planId, String dayStr, String beginTime, String endTime){
        Map<String,Object> result = new HashMap<String, Object>();
        //计算“基线负荷(修正前)”
        List<Map<String, Date>> baseList = new ArrayList<Map<String, Date>>();
        if( ifIsRestDay(ledgerId, dayStr) ){
            //非工作日
            String temp =  DateUtil.addDay(dayStr, -2);
            int num = 0;
            while(num < 4){
                if( ifIsRestDay(ledgerId, temp) ){
                    Map<String, Date> map = new HashMap<String, Date>();
                    Date begin = DateUtil.convertStrToDate(temp + " " + beginTime, DateUtil.MOUDLE_PATTERN);
                    Date end = DateUtil.convertStrToDate(temp + " " + endTime, DateUtil.MOUDLE_PATTERN);
                    map.put("begin", begin);
                    map.put("end", end);
                    baseList.add(map);

                    num++;
                }
                temp = DateUtil.addDay(temp, -1);
            }
        }
        else{
            //工作日
            String temp =  DateUtil.addDay(dayStr, -2);
            int num = 0;
            while(num < 10){
                if( !ifIsRestDay(ledgerId, temp) ){
                    Map<String, Date> map = new HashMap<String, Date>();
                    Date begin = DateUtil.convertStrToDate(temp + " " + beginTime, DateUtil.MOUDLE_PATTERN);
                    Date end = DateUtil.convertStrToDate(temp + " " + endTime, DateUtil.MOUDLE_PATTERN);
                    map.put("begin", begin);
                    map.put("end", end);
                    baseList.add(map);

                    num++;
                }
                temp = DateUtil.addDay(temp, -1);
            }
        }
        BigDecimal baseValBefore = null;
        if(baseList != null && baseList.size() > 0){
            baseValBefore = DataUtil.parseDouble2BigDecimal(this.demandMapper.getLedgerBaseAp(ledgerId, baseList));
        }
        if(baseValBefore == null){
            baseValBefore = BigDecimal.ZERO;
        }
        else{
            baseValBefore.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        result.put("baseValBefore", baseValBefore);

        //计算“调整因子”
        Double adjust = 0D;
        BigDecimal adjust_up;
        Double adjust_down = 0D;
        Date up_end = DateUtil.convertStrToDate(dayStr + " " + beginTime, DateUtil.MOUDLE_PATTERN);
        Date up_begin = DateUtil.getSomeNextHourDate(up_end, -2);
        adjust_up = DataUtil.parseDouble2BigDecimal(this.demandMapper.getLedgerAverageAp(ledgerId, null, up_begin, up_end, 0));
        if(adjust_up == null){
            adjust_up = BigDecimal.ZERO;
        }
        if(baseList != null && baseList.size() > 0){
            adjust_down = this.demandMapper.getLedgerBaseAp(ledgerId, processBaseTimeList(baseList));
        }
        if(adjust_down != null && adjust_down != 0){
            adjust = adjust_up.divide(new BigDecimal(adjust_down), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if(adjust > 1.2){
            adjust = 1.2;
        }
        else if(adjust < 0.8){
            adjust = 0.8;
        }
        adjust = Double.valueOf(NumberUtil.formatDouble(adjust, "0.##"));
        result.put("adjust", adjust);

        //计算“基线负荷(修正后)”
        Double baseValAfter = 0D;
        if(adjust != null && null != baseValBefore ){
            baseValAfter = baseValBefore.multiply(new BigDecimal(adjust)).doubleValue();
        }
        baseValAfter = Double.valueOf(NumberUtil.formatDouble(baseValAfter, "0.##"));
        result.put("baseValAfter", baseValAfter);

        //计算"响应时段平均负荷"
        Date beginDate = DateUtil.convertStrToDate(dayStr + " " + beginTime, DateUtil.MOUDLE_PATTERN);
        Date endDate = DateUtil.convertStrToDate(dayStr + " " + endTime, DateUtil.MOUDLE_PATTERN);
        BigDecimal average = DataUtil.parseDouble2BigDecimal(this.demandMapper.getLedgerAverageAp(ledgerId, planId, beginDate, endDate, 1));
        if(average == null){
            average = BigDecimal.ZERO;
        }
        average.setScale(2, BigDecimal.ROUND_HALF_UP);
        result.put("average", average);

        //计算"负荷消减量"
        Double cutDownVal = new BigDecimal(baseValAfter).subtract(average).doubleValue();
        result.put("cutDownVal", NumberUtil.formatDouble(cutDownVal, "0.##"));

        //“实际负荷曲线”
        List<Map<String, Object>> actualLine = this.demandMapper.getLedgerActualAp(ledgerId, planId, beginDate, endDate);
        result.put("actualLine", actualLine);

        //该企业、这个时段，是否保存过响应
        int existNum = this.demandMapper.getExistDemandNumBy(ledgerId, beginDate, endDate);
        result.put("existNum", existNum);

        return result;
    }

    @Override
    public void insertResponse(Long ledgerId,Date beginDate,Date endDate,Double baseValBefore,Double baseValAfter,
                               Double adjust,Double average,Double cutDownVal){
        Long incidentId = SequenceUtils.getDBSequence();
        this.demandMapper.insertResponse(incidentId, ledgerId, beginDate, endDate, baseValBefore, baseValAfter,
                                         adjust, average, cutDownVal);
    }

    @Override
    public Map<String,Object> getHistoryDetailById(Long incidentId){
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> demandRes = this.demandMapper.getHistoryDetailById(incidentId);
        result.put("demandRes", demandRes);
        Long ledgerId = Long.valueOf(demandRes.get("LEDGER_ID").toString());
        Date startTime = DateUtil.convertStrToDate(demandRes.get("START_TIME").toString(), DateUtil.MOUDLE_PATTERN);
        Date endTime = DateUtil.convertStrToDate(demandRes.get("END_TIME").toString(), DateUtil.MOUDLE_PATTERN);
        //“实际负荷曲线”
        List<Map<String, Object>> actualLine = this.demandMapper.getLedgerActualAp(ledgerId, null, startTime, endTime);
        result.put("actualLine", actualLine);

        return result;
    }

    @Override
    public Map<String,Object> getLedgerDemandHistory(Long ledgerId){
        Map<String,Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> historys = this.demandMapper.getLedgerDemandHistory(ledgerId);
        if(historys != null && historys.size() > 0){
            result.put("historys", historys);
        }

        return result;
    }


    //判断是否休息日
    private boolean ifIsRestDay(Long ledgerId, String dayStr){
        Date baseDate = DateUtil.convertStrToDate(dayStr + " " + "00:00", DateUtil.MOUDLE_PATTERN);
        //判断是否配置过“厂休时段”(判断标准：是否点击过“厂休时段”的保存按钮)
        int restDayNum = this.demandMapper.getRestDayNumByLedger(ledgerId);
        if(restDayNum > 0){

            //---配置过
            //判断是否法定节假日
            int defaultHoliday = this.eventQueryMapper.includeDefaultHoliday(ledgerId);
            if(defaultHoliday > 0){
                List<HolidayBean> holidayList = OfficialHolidayUtils.readOfficialHolidayList();
                if(holidayList != null && holidayList.size() > 0){
                    for(int i = 0; i < holidayList.size(); i++){
                        HolidayBean holidayBean = holidayList.get(i);
                        int minusFromDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseDate), holidayBean.getFromDate());
                        int minusEndDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseDate), holidayBean.getEndDate());
                        if(minusFromDate >= 0 && minusEndDate <= 0){
                            return true;
                        }
                    }
                }
            }
            //判断是否是自定义节假日
            int ifHoliday = this.eventQueryMapper.judgeHoliday(ledgerId, DateUtil.clearDate(baseDate));
            if(ifHoliday > 0){
                return true;
            }
            //判断是否休息日
            Integer ifRestDay = this.eventQueryMapper.judgeRestDay(ledgerId, DateUtil.getDateWeekNum(baseDate));
            if(ifRestDay != null && ifRestDay == 1){
                return true;
            }
        }
        else{

            //---未配置过
            //判断是否法定节假日
            List<HolidayBean> holidayList = OfficialHolidayUtils.readOfficialHolidayList();
            if(holidayList != null && holidayList.size() > 0){
                for(int i = 0; i < holidayList.size(); i++){
                    HolidayBean holidayBean = holidayList.get(i);
                    int minusFromDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseDate), holidayBean.getFromDate());
                    int minusEndDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseDate), holidayBean.getEndDate());
                    if(minusFromDate >= 0 && minusEndDate <= 0){
                        return true;
                    }
                }
            }
            //判断是否周六周日
            int weekNum = DateUtil.getDateWeekNum(baseDate);
            if(weekNum == 7 || weekNum == 1){
                return true;
            }
        }

        return false;
    }

    private List<Map<String, Date>> processBaseTimeList(List<Map<String, Date>> baseList){
        List<Map<String, Date>> result = new ArrayList<Map<String, Date>>();
        if(baseList != null && baseList.size() > 0){
            for(int i = 0; i < baseList.size(); i++){
                Date up_end = baseList.get(i).get("begin");
                Date up_begin = DateUtil.getSomeNextHourDate(up_end, -2);

                Map<String, Date> temp = new HashMap<String, Date>();
                temp.put("begin", up_begin);
                temp.put("end", DateUtil.getSomeNextSecondDate(up_end, -1));
                result.add(temp);
            }
        }
        return result;
    }

    @Override
    public void deleteHistoryDetail(Long incidentId){
        this.demandMapper.deleteHistoryDetail(incidentId);
    }

    @Override
    public Map<String,Object> getEleDemandPlan(Long ledgerId){
        Map<String,Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> demands = this.demandMapper.getEleDemandPlanBy(ledgerId);
        if(demands != null && demands.size() > 0){
            result.put("demands", demands);
        }

        return result;
    }

    @Override
    public Map<String,Object> getElePlanDetail(Long planId){
        Map<String, Object> result = this.demandMapper.getElePlanDetailBy(planId);
        return result;
    }

    @Override
    public Map<String,Object> getEleDemandResult(Long ledgerId, Long planId, String planBegin, String planEnd, String beginTime, String endTime){
        Map<String,Object> result = new HashMap<String, Object>();

        List<String> xList = new ArrayList<>();               //X轴数据
        List<Map<String, Object>> yList = new ArrayList<>();  //y轴数据
        String tempX = getNearByDateStr(beginTime);
        Integer int1 = Integer.valueOf(tempX.replace(":", ""));
        Integer int2 = Integer.valueOf(endTime.replace(":", ""));
        while(int1 <= int2){
            xList.add(tempX);

            tempX = DateUtil.convertDateToStr(DateUtil.getNextMinuteDate(DateUtil.convertStrToDate(tempX, DateUtil.HOUR_MINUTE_PATTERN), 15), DateUtil.HOUR_MINUTE_PATTERN);
            if(tempX.equals("00:00")){  //跨天临界
                break;
            }
            int1 = Integer.valueOf(tempX.replace(":", ""));
        }
        result.put("xList", xList);

        if(CollectionUtils.isNotEmpty(xList)){
            List<Map<String, Object>> afterList = this.demandMapper.getEleDemandData(ledgerId, planBegin, planEnd, beginTime, endTime);
            String beforeBegin = DateUtil.convertDateToStr(DateUtil.getSomeNextDayDate(DateUtil.convertStrToDate(planBegin, DateUtil.SHORT_PATTERN), -7), DateUtil.SHORT_PATTERN);
            String beforeEnd = DateUtil.convertDateToStr(DateUtil.getSomeNextDayDate(DateUtil.convertStrToDate(planBegin, DateUtil.SHORT_PATTERN), -1), DateUtil.SHORT_PATTERN);
            List<Map<String, Object>> beforeList = this.demandMapper.getEleDemandData(ledgerId, beforeBegin, beforeEnd, beginTime, endTime);

            int i = 0;
            int j = 0;
            int k = 0;
            while (i < xList.size()){
                Map<String, Object> temp = new HashMap<>();
                String key = xList.get(i);
                temp.put("xData", key);

                if( CollectionUtils.isNotEmpty(beforeList) && j < beforeList.size() && key.equals(beforeList.get(j).get("FREEZE_TIME").toString()) ){
                    temp.put("yData1", NumberUtil.formatDouble(Double.valueOf(beforeList.get(j).get("DATA").toString()), "0.#") );
                    j++;
                }
                else{
                    temp.put("yData1", "-");
                }

                if( CollectionUtils.isNotEmpty(afterList) && k < afterList.size() && key.equals(afterList.get(k).get("FREEZE_TIME").toString()) ){
                    temp.put("yData2", NumberUtil.formatDouble(Double.valueOf(afterList.get(k).get("DATA").toString()), "0.#") );
                    k++;
                }
                else{
                    temp.put("yData2", "-");
                }

                i++;
                yList.add(temp);
            }
        }
        result.put("yList", yList);

        return result;
    }

    private String getNearByDateStr(String time){
        Date date = DateUtil.convertStrToDate(time, DateUtil.HOUR_MINUTE_PATTERN);
        Integer min = Integer.valueOf(DateUtil.convertDateToStr(date, "mm"));
        Integer near = 0;
        if(min%15 != 0){
            near = 15 - min%15;
        }
        return DateUtil.convertDateToStr(DateUtil.getNextMinuteDate(date, near), DateUtil.HOUR_MINUTE_PATTERN);
    }
}
