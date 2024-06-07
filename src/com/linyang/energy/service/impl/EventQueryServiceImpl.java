package com.linyang.energy.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import cn.jpush.api.utils.StringUtils;
import com.leegern.util.NumberUtil;
import com.linyang.energy.dto.HolidayBean;
import com.linyang.energy.mapping.authmanager.UserBeanMapper;
import com.linyang.energy.mapping.energydataquery.DataQueryMapper;
import com.linyang.energy.mapping.energysavinganalysis.CostMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.*;
import com.linyang.energy.service.MessageService;
import com.linyang.energy.service.PhoneService;
import com.linyang.energy.utils.*;
import com.linyang.util.DoubleUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.common.Log;
import com.linyang.energy.mapping.authmanager.MeterBeanMapper;
import com.linyang.energy.mapping.energydataquery.EventQueryMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.service.EventQueryService;

/**
 * @Description 事件查询service实现
 * @author Leegern
 * @date Jan 21, 2014 10:27:59 AM
 */
@Service
public class EventQueryServiceImpl implements EventQueryService {
	
	@Autowired
	private EventQueryMapper eventQueryMapper;
	@Autowired
	private CostMapper costMapper;
    @Autowired
	private DataQueryMapper dataQueryMapper;
    @Autowired
	private LedgerManagerMapper ledgerManagerMapper;
    @Autowired
    private MeterManagerMapper meterManagerMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserBeanMapper userBeanMapper;
    @Autowired
    private PhoneService phoneService;

    //“功率越安全限”事件
    Map<Long,EventBean> lastLedgerPowerSafety = new HashMap<Long, EventBean>();
    Map<Long,EventBean> lastMeterPowerSafety = new HashMap<Long, EventBean>();
    //“功率越经济限（休息时段）”事件
    Map<Long,EventBean> lastLedgerPowerEconomy = new HashMap<Long, EventBean>();
    Map<Long,EventBean> lastMeterPowerEconomy = new HashMap<Long, EventBean>();
    //“反向功率越限”事件
    Map<Long,EventBean> lastLedgerPowerReverse = new HashMap<Long, EventBean>();
    Map<Long,EventBean> lastMeterPowerReverse = new HashMap<Long, EventBean>();
    //“功率因数越限”事件
    Map<Long,EventBean> lastLedgerPf = new HashMap<Long, EventBean>();
    Map<Long,EventBean> lastMeterPf = new HashMap<Long, EventBean>();
    //“需量预警”事件
    Map<Long,EventBean> lastLedgerDemand = new HashMap<Long, EventBean>();
    Map<Long,EventBean> lastMeterDemand = new HashMap<Long, EventBean>();
    Map<Long, EventBean> lastMeterV = new HashMap<Long, EventBean>();
    //电压越下限
    static Map<Long, EventBean> lastMeterLowerV = new HashMap<Long, EventBean>();
    Map<Long, EventBean> lastMeterI = new HashMap<Long, EventBean>();
    //“污染源、治理源”事件
    Map<Long,EventPolluteBean> lastPollute = new HashMap<Long, EventPolluteBean>();
    //"水量越限"事件
    static Map<Long, EventBean> lastMeterW = new HashMap<>(  );
    //"日配额"事件(水)
    static Map<Long,EventBean> lastLedgerW_D = new HashMap<>(  );
    //"日配额"事件(气)
    static Map<Long,EventBean> lastLegderG_D = new HashMap<>(  );
    //"日配额"事件(电)
    static Map<Long,EventBean> lastLedgerE_D = new HashMap<>(  );
    //停上电事件
    static Map<String,EventBean> lastMeterPull = new HashMap<String, EventBean>();
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.EventQueryService#queryEventPageList(java.util.Map)
	 */
	@Override
	public List<EventBean> queryEventPageList(Map<String, Object> param) {
		List<EventBean> eventBeans= eventQueryMapper.queryEventPageList(param);
        //乘以ct、pt, 并且加入中文
        //eventBeans = processEventCtpts(eventBeans);
		return eventBeans;
	}

	@Override
	public List<EventBean> queryEventPageList2(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<EventBean> eventBeans= eventQueryMapper.queryEventPageList2(param);
        
		return eventBeans;
	}
	
	@Override
	public List<EventBean> queryEventPageList3(Map<String, Object> param) {
		// TODO Auto-generated method stub
		List<EventBean> eventBeans= eventQueryMapper.queryEventPageList3(param);
        
		return eventBeans;
	}
	
	@Override
	public List<EventBean> queryLedgerMinusPF(Long ledgerId,
			Date beginTime, Date endTime) {
		return eventQueryMapper.queryLedgerMinusPF(ledgerId, beginTime, endTime);
	}

	@Override
	public List<EventBean> queryMeterMinusPF(Long meterId, Date beginTime,
			Date endTime) {
		return eventQueryMapper.queryMeterMinusPF(meterId, beginTime, endTime);
	}

	@Override
	public List<EventBean> queryEventWarningList(Map<String, Object> param) {
        List<EventBean> list = this.eventQueryMapper.queryEventPageList(param);

        return list;
	}

	@Override
	public List<EventBean> queryRecentEventInfo(Date startTime) {
		return eventQueryMapper.queryRecentEventInfo(startTime);
	}

    @Override
    public List<EventBean> processEventCtpts(List<EventBean> eventBeans){
        if(eventBeans!=null && eventBeans.size()>0){
            for(int i=0; i<eventBeans.size(); i++){
                eventBeans.set(i, processEventCtpt(eventBeans.get(i)));
            }
        }
        return eventBeans;
    }

    @Override
    public EventBean processEventCtpt(EventBean eventBean){
        
            int objectType = eventBean.getObjectType();
            if(objectType == 2){
                long pointId = eventBean.getObjectId();
                List<Map<String, BigDecimal>> list = this.eventQueryMapper.getCtptByMeter(pointId);
                if(list!=null && list.size()>0){
                    Integer ct = list.get(0).get("CT").intValue();
                    Integer pt = list.get(0).get("PT").intValue();
                    if(ct!=null && pt!=null){
                        eventBean = handleEvent(eventBean, ct, pt);
                    }
                }
            }
        return eventBean;
    }

    private EventBean handleEvent(EventBean eventBean, Integer ct, Integer pt){
        long eventId = eventBean.getEventId();
        String content = eventBean.getEventContent();
        String[] items = content.split(",");
        int length = items.length;
        if(eventId==5 && length>=4){
            items[2] = multiCtPt(items[2], ct, pt);
            items[3] = multiCtPt(items[3], ct, pt);
        }
        else if(eventId==6 && length>=7){
            items[4] = multiCtPt(items[4], ct, pt);
            items[5] = multiCtPt(items[5], ct, pt);
            items[6] = multiCtPt(items[6], ct, pt);
        }
        else if(eventId==7 && length>=6){
            items[4] = multiCtPt(items[4], ct, pt);
            items[5] = multiCtPt(items[5], ct, pt);
        }
        else if(eventId==9 && length>=11){
            items[5] = multiPt(items[5], pt);
            items[7] = multiCt(items[7], ct);
            items[8] = multiCt(items[8], ct);
            items[9] = multiCt(items[9], ct);
        }
        else if(eventId==10 && length>=11){
            items[5] = multiPt(items[5], pt);
            items[7] = multiCt(items[7], ct);
            items[8] = multiCt(items[8], ct);
            items[9] = multiCt(items[9], ct);
        }
        else if(eventId==15 && length>=24){
            String message = items[3];
            if(message.contains("电流")){
                for(int i=5; i<=23; i++){
                    items[i] = multiCt(items[i], ct);
                }
            }
            else if(message.contains("电压")){
                for(int i=5; i<=23; i++){
                    items[i] = multiPt(items[i], pt);
                }
            }
        }
        else if(eventId==17 && length>=12){
            items[7] = multiPt(items[7], pt);
            items[9] = multiCt(items[9], ct);
            items[10] = multiCt(items[10], ct);
            items[11] = multiCt(items[11], ct);
        }
        else if(eventId==23 && length>=6){
            items[4] = multiCtPt(items[4], ct, pt);
        }
        else if(eventId==24 && length>=7){
            items[5] = multiPt(items[5], pt);
        }
        else if(eventId==25 && length>=7){
            items[4] = multiCt(items[4], ct);
            items[5] = multiCt(items[5], ct);
            items[6] = multiCt(items[6], ct);
        }
        else if(eventId==26 && length>=6){
            items[4] = multiCtPt(items[4], ct, pt);
            items[5] = multiCtPt(items[5], ct, pt);
        }

        StringBuffer newContent = new StringBuffer();
        if(length > 1){
            for( int i = 0; i < length-1; i++ )
            {	
            	newContent.append(newContent);  newContent.append(items[i]);	newContent.append(",");
            }
        }
        newContent.append(newContent);  newContent.append(items[length-1]);
        eventBean.setEventContent(newContent.toString());
        return eventAddChinese(eventBean);
    }
    //乘以ctpt
    private String multiCtPt(String str, Integer ct, Integer pt){
        if(StringUtils.isNotEmpty(str)){
            Double d = Double.parseDouble(str);
            d = DataUtil.doubleMultiply(d, ct * pt);
            DecimalFormat df=new DecimalFormat("0.##");
            str=df.format(d);
        }
        return str;
    }
    //乘以ct
    private String multiCt(String str, Integer ct){
        if(StringUtils.isNotEmpty(str)){
            Double d = Double.parseDouble(str);
            d = DataUtil.doubleMultiply(d, ct);
            DecimalFormat df=new DecimalFormat("0.##");
            str=df.format(d);
        }
        return str;
    }
    //乘以pt
    private String multiPt(String str, Integer pt){
        if(StringUtils.isNotEmpty(str)){
            Double d = Double.parseDouble(str);
            d = DataUtil.doubleMultiply(d, pt);
            DecimalFormat df=new DecimalFormat("0.##");
            str=df.format(d);
        }
        return str;
    }
    //事件内容加入中文
    private EventBean eventAddChinese(EventBean eventBean){
        // 解析事件内容,加入中文
        String content = eventBean.getEventContent();
        StringBuilder sb = new StringBuilder();
        String[] items;
        if (eventBean.getObjectType() == 2){// 只对测量点做处理
            List<EventSpecCfgBean> cfgBeans = eventQueryMapper.queryEventSpecInfo(eventBean.getObjectId(), eventBean.getEventId());

            if ((content != null) && content.length()>0) {
                items = content.split(",");
                if (items.length > 0 && cfgBeans.size() > 0) {
                    // 判断两边长度,防止越界问题
                    if (items.length <= cfgBeans.size()) {
                        // 拼接异常概述,格式:序号->名称->事件内容Item
                        for (int i = 0; i < items.length; i++) {
                            if (i == 0) {
                                sb.append(cfgBeans.get(i).getName()).append(":").append(items[i]);
                            } else {
                                sb.append(";").append(cfgBeans.get(i).getName()).append(":").append(items[i]);
                            }
                        }
                    } else {
                        // 拼接异常概述,格式:序号->名称->事件内容Item
                        for (int i = 0; i < cfgBeans.size(); i++) {
                            if (i == 0) {
                                sb.append(cfgBeans.get(i).getName()).append(":").append(items[i]);
                            } else {
                                sb.append(";").append(cfgBeans.get(i).getName()).append(":").append(items[i]);
                            }
                        }
                    }
                    // 替换拼接好的异常概述
                    eventBean.setEventContent(sb.toString());
                }
            }
        }
        return eventBean;
    }


    @Override
    public void analysisPowerSafety(Long eventId, Date beginTime, Date endTime){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startDate", beginTime);
        queryMap.put("endDate", endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的EMO和DCP
        List<Map<String, Object>> emoList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 1);
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);
  
        //分析EMO
        if(emoList != null && emoList.size() > 0){
            for(int i = 0; i < emoList.size(); i++){
                Map<String, Object> map = emoList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long ledgerId = Long.valueOf(map.get("OBJECT_ID").toString());
                LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                if(alarmVal==null)
                {
                	continue;
                }
                Map<String, Object> thresholdInfo = ledgerManagerMapper.getLedgerThresholdInfo(ledgerId, 4l);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = new BigDecimal(standard).multiply(new BigDecimal(alarmVal)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                queryMap.put("ledgerId", ledgerId);
                List<Map<String, Object>> values = this.dataQueryMapper.getLedger96ApData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,1,ledgerId);
                        if(!lastLedgerPowerSafety.keySet().contains(ledgerId)){
                            if(data > alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastLedgerPowerSafety.put(ledgerId,eventBean);
                        }
                        else{
                            EventBean lastEvent = lastLedgerPowerSafety.get(ledgerId);
                            if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastLedgerPowerSafety.put(ledgerId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastLedgerPowerSafety.remove(ledgerId);
                            }                         
                        }
                    }
                }
            }
        }
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
                MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                if(alarmVal==null)
                {
                	continue;
                }
                Map<String, Object> thresholdInfo = meterManagerMapper.getMeterThresholdInfo(meterId, 4l);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = new BigDecimal(standard).multiply(new BigDecimal(alarmVal)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                queryMap.put("meterId", meterId);
                List<Map<String, Object>> values = this.dataQueryMapper.getMeterCurApData(queryMap);

                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
                        if(!lastMeterPowerSafety.keySet().contains(meterId)){
                            if(data > alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastMeterPowerSafety.put(meterId,eventBean);
                        }
                        else{
                            EventBean lastEvent = lastMeterPowerSafety.get(meterId);
                            if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastMeterPowerSafety.put(meterId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastMeterPowerSafety.remove(meterId);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void analysisPolluteControl(Long eventId, Date beginTime, Date endTime){
        beginTime = DateUtil.clearMinute(beginTime);
        //处理时段为list
        List<Date> dateList = processTimeToList(beginTime, endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //“污染源、治理源”列表
        List<EventPolluteBean> list = this.eventQueryMapper.getPolluteResolveList();
        if(CollectionUtils.isNotEmpty(list)){
            for(int i = 0; i < list.size(); i++){
                EventPolluteBean temp = list.get(i);
                for(int j = 0; j < dateList.size(); j++){
                    Date time = dateList.get(i);
                    //如果缓存中没有该条污染、治理源数据，则放入缓存Map （初始化）
                    if(!lastPollute.keySet().contains(temp.getPolluteId())){
                        lastPollute.put(temp.getPolluteId(), temp);
                    }
                    else if(j == 0){   //如果缓存中有该条污染、治理源数据，防止出现污染源、治理源信息被修改的情况
                        lastPollute.get(temp.getPolluteId()).setPolluteDoor(temp.getPolluteDoor());
                        lastPollute.get(temp.getPolluteId()).setPolluteName(temp.getPolluteName());
                        lastPollute.get(temp.getPolluteId()).setResolveDoor(temp.getResolveDoor());
                        lastPollute.get(temp.getPolluteId()).setResolveName(temp.getResolveName());
                    }
                    //拿出缓存中的这条数据
                    EventPolluteBean oneData = lastPollute.get(temp.getPolluteId());
                    Map<String, Object> polluteCheck = checkPolluteResolveOpen(time, oneData, 1);
                    if(polluteCheck.get("isOpen").toString().equals("1")){   //污染源开启

                        if(oneData.getEventFlag() == 0){  //该闭环内，事件尚未发生
                            //记录的是该闭环内，污染源开启的最早的那个时间点
                            if(oneData.getPolluteStartTime() == null){
                                oneData.setPolluteStartTime(time);
                                double polluteStartVal = Double.valueOf(polluteCheck.get("value").toString());
                                oneData.setPolluteStartTimeValue(NumberUtil.formatDouble(polluteStartVal, NumberUtil.PATTERN_DOUBLE));
                            }
                            //若治理源关闭，事件发生
                            Map<String, Object> resolveCheck = checkPolluteResolveOpen(time, oneData, 2);
                            if(!resolveCheck.get("isOpen").toString().equals("1")){
                                oneData.setResolveEndTime(time);
                                double resolveEndTimeVal = Double.valueOf(resolveCheck.get("value").toString());
                                oneData.setResolveEndTimeValue(NumberUtil.formatDouble(resolveEndTimeVal, NumberUtil.PATTERN_DOUBLE));
                                oneData.setEventFlag(1);
                                //入数据库、推送
                                EventBean eventBean = new EventBean(oneData.getPolluteStartTime(), eventName,eventId,1,oneData.getParentLedgerId());
                                eventBean.setEventStatus(1);
                                String content = oneData.getParentLedgerName() + "企业" + DateUtil.convertDateToStr(oneData.getPolluteStartTime(), DateUtil.MOUDLE_PATTERN)
                                       + "污染源设备（" + oneData.getPolluteName() + "）开启，功率" + oneData.getPolluteStartTimeValue().toString() + "kW，" + DateUtil.convertDateToStr(oneData.getResolveEndTime(), DateUtil.MOUDLE_PATTERN)
                                       + "治理源设备（" + oneData.getResolveName() + "）未在规定时间内开启，功率" + oneData.getResolveEndTimeValue().toString() + "kW";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, oneData.getParentLedgerId());
                            }
                            //lastPollute.put(oneData.getPolluteId(), oneData);
                        }
                    }
                    else if(oneData.getPolluteStartTime() != null){   //污染源关闭
                        oneData.setPolluteEndTime(time);
                        this.eventQueryMapper.updatePolluteEventEndtime(oneData.getPolluteStartTime(), eventId, oneData.getParentLedgerId(), 1, time);  //更新数据库（闭环结束时间）
                        lastPollute.remove(oneData.getPolluteId());        //清除内存中该条数据
                    }
                }

            }
        }
    }

    //处理时段为list
    private List<Date> processTimeToList(Date beginTime, Date endTime){
        List<Date> result = new ArrayList<Date>();
        while(beginTime.before(endTime)){
            result.add(beginTime);
            beginTime = DateUtil.getNextMinuteDate(beginTime, 15);
        }
        return result;
    }

    //得到污染源或治理源在某个时刻是否开启(time必须是15分钟时刻点; flag为1表示判断的是污染源; flag为2表示判断的是治理源;)
    private Map<String, Object> checkPolluteResolveOpen(Date time, EventPolluteBean bean, int flag){
        Map<String, Object> result = new HashMap<String, Object>();
        String isOpen = "0";
        double value = 0;

        Long objectId = 0L;
        Double openValue = 0D;
        if(flag == 1){
            objectId = bean.getPolluteId();
            openValue = bean.getPolluteDoor();
        }
        else{
            objectId = bean.getResolveId();
            openValue = bean.getResolveDoor();
        }
        //若总和大于，则表示开启了
        Double totalVal = this.eventQueryMapper.getOneTimeLedgerPower(objectId, time);
        if(totalVal != null && totalVal >= openValue){
            isOpen = "1";
            value = totalVal;
        }
        //若总和没大于，则循环逐个判断计算
        List<Map<String, Object>> meterList = this.eventQueryMapper.getComputeMeters(objectId);
        if(CollectionUtils.isNotEmpty(meterList)){
            Double sumVal = 0D;
            for(int i = 0; i < meterList.size(); i++){
                Map<String, Object> map = meterList.get(i);
                long meterId = Long.valueOf(map.get("METER_ID").toString());
                long pct = Long.valueOf(map.get("PCT").toString());
                long addAttr = Long.valueOf(map.get("ADD_ATTR").toString());
                Date lastDate = this.eventQueryMapper.getTimeLast(meterId, time);
                Double lastVal = this.eventQueryMapper.getTimeLastPower(meterId, lastDate);
                if(lastVal != null){
                    sumVal = new BigDecimal(lastVal).multiply(new BigDecimal(addAttr)).multiply(new BigDecimal(pct)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).add(new BigDecimal(sumVal)).doubleValue();
                }
            }

            if(sumVal >= openValue){
                isOpen = "1";
            }
            else{
                isOpen = "0";
            }
            value = sumVal;
        }

        result.put("isOpen", isOpen);
        result.put("value", value);
        return result;
    }

    @Override
    public void analysisPowerEconomy(Long eventId, Date beginTime, Date endTime){
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的EMO和DCP
        List<Map<String, Object>> emoList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 1);
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);
        //分析EMO
        if(emoList != null && emoList.size() > 0){
            for(int i = 0; i < emoList.size(); i++){
                Map<String, Object> map = emoList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long ledgerId = Long.valueOf(map.get("OBJECT_ID").toString());
                LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = ledgerManagerMapper.getLedgerThresholdInfo(ledgerId, 4l);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = new BigDecimal(standard).multiply(new BigDecimal(alarmVal)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                Map<String, Object> queryMap = new HashMap<String, Object>();
                queryMap.put("startDate", beginTime);
                queryMap.put("endDate", endTime);
                queryMap.put("ledgerId", ledgerId);
                //处理休息时段
                processRestTime(parentId, queryMap);

                if(queryMap.keySet().contains("startDate") && queryMap.keySet().contains("endDate")){ //是休息时间
                    List<Map<String, Object>> values = this.dataQueryMapper.getLedger96ApData(queryMap);
                    if(values != null && values.size() > 0){
                        for(int j = 0; j < values.size(); j++){
                            Map<String, Object> value = values.get(j);
                            Date dataTime = (Date) value.get("DATATIME");
                            Double data = Double.valueOf(value.get("DATA").toString());
                            EventBean eventBean = new EventBean(dataTime,eventName,eventId,1,ledgerId);
                            if(!lastLedgerPowerEconomy.keySet().contains(ledgerId)){
                                if(data > alarmVal){
                                    eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                    String content = eventName + "; "
                                            + "能管单元: " + ledger.getLedgerName() + "; "
                                            + "报警值: " + alarmVal.toString() + "kW; "
                                            + "实际值: " + data.toString() + "kW;";
                                    eventBean.setEventContent(content);
                                    insetNewEvent(eventBean, parentId); //入数据库、推送
                                }
                                lastLedgerPowerEconomy.put(ledgerId,eventBean);
                            }
                            else{
                                EventBean lastEvent = lastLedgerPowerEconomy.get(ledgerId);
                                if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                    eventBean.setEventStatus(1);
                                    String content = eventName + "; "
                                            + "能管单元: " + ledger.getLedgerName() + "; "
                                            + "报警值: " + alarmVal.toString() + "kW; "
                                            + "实际值: " + data.toString() + "kW;";
                                    eventBean.setEventContent(content);
                                    insetNewEvent(eventBean, parentId); //入数据库、推送
                                    lastLedgerPowerEconomy.put(ledgerId,eventBean);
                                }
                                else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                    updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                    lastLedgerPowerEconomy.remove(ledgerId);
                                }
                                else if(lastEvent.getEventStatus() == 1 && data > alarmVal){
                                    lastEvent.setEventEndTime(dataTime);
                                    lastLedgerPowerEconomy.put(ledgerId,lastEvent);
                                }                            
                            }
                        }
                    }
                }
                else{ //不是休息时间
                    if(lastLedgerPowerEconomy.keySet().contains(ledgerId)){
                        EventBean lastEvent = lastLedgerPowerEconomy.get(ledgerId);
                        if(lastEvent.getEventStatus() == 1){
                            Date recoverTime = DateUtil.getDateMinusMinute(lastLedgerPowerEconomy.get(ledgerId).getEventStartTime(), -15);
                            if(lastLedgerPowerEconomy.get(ledgerId).getEventEndTime() != null){
                                recoverTime = DateUtil.getDateMinusMinute(lastLedgerPowerEconomy.get(ledgerId).getEventEndTime(), -15);
                            }
                            updateNewEventEndtime(recoverTime, lastEvent);  //更新数据库（恢复时间）
                            lastLedgerPowerEconomy.remove(ledgerId);
                        }
                    }
                }
            }
        }
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
                MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = meterManagerMapper.getMeterThresholdInfo(meterId, 4l);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = new BigDecimal(standard).multiply(new BigDecimal(alarmVal)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                Map<String, Object> queryMap = new HashMap<String, Object>();
                queryMap.put("startDate", beginTime);
                queryMap.put("endDate", endTime);
                queryMap.put("meterId", meterId);
                //处理休息时段
                processRestTime(parentId, queryMap);

                if(queryMap.keySet().contains("startDate") && queryMap.keySet().contains("endDate")){ //是休息时间
                    List<Map<String, Object>> values = this.dataQueryMapper.getMeterCurApData(queryMap);
                    if(values != null && values.size() > 0){
                        for(int j = 0; j < values.size(); j++){
                            Map<String, Object> value = values.get(j);
                            Date dataTime = (Date) value.get("DATATIME");
                            Double data = Double.valueOf(value.get("DATA").toString());
                            EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
                            if(!lastMeterPowerEconomy.keySet().contains(meterId)){
                                if(data > alarmVal){
                                    eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                    String content = eventName + "; "
                                            + "采集点: " + meter.getMeterName() + "; "
                                            + "报警值: " + alarmVal.toString() + "kW; "
                                            + "实际值: " + data.toString() + "kW;";
                                    eventBean.setEventContent(content);
                                    insetNewEvent(eventBean, parentId); //入数据库、推送
                                }
                                lastMeterPowerEconomy.put(meterId,eventBean);
                            }
                            else{
                                EventBean lastEvent = lastMeterPowerEconomy.get(meterId);
                                if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                    eventBean.setEventStatus(1);
                                    String content = eventName + "; "
                                            + "采集点: " + meter.getMeterName() + "; "
                                            + "报警值: " + alarmVal.toString() + "kW; "
                                            + "实际值: " + data.toString() + "kW;";
                                    eventBean.setEventContent(content);
                                    insetNewEvent(eventBean, parentId); //入数据库、推送
                                    lastMeterPowerEconomy.put(meterId,eventBean);
                                }
                                else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                    updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                    lastMeterPowerEconomy.remove(meterId);
                                }
                                else if(lastEvent.getEventStatus() == 1 && data > alarmVal){
                                    lastEvent.setEventEndTime(dataTime);
                                    lastMeterPowerEconomy.put(meterId,lastEvent);
                                }                              
                            }
                        }
                    }
                }
                else{ //不是休息时间
                    if(lastMeterPowerEconomy.keySet().contains(meterId)){
                        EventBean lastEvent = lastMeterPowerEconomy.get(meterId);
                        if(lastEvent.getEventStatus() == 1){
                            Date recoverTime = DateUtil.getDateMinusMinute(lastLedgerPowerEconomy.get(meterId).getEventStartTime(), -15);
                            if(lastLedgerPowerEconomy.get(meterId).getEventEndTime() != null){
                                recoverTime = DateUtil.getDateMinusMinute(lastLedgerPowerEconomy.get(meterId).getEventEndTime(), -15);
                            }
                            updateNewEventEndtime(recoverTime, lastEvent);  //更新数据库（恢复时间）
                            lastMeterPowerEconomy.remove(meterId);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void analysisPowerReverse(Long eventId, Date beginTime, Date endTime){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startDate", beginTime);
        queryMap.put("endDate", endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的EMO和DCP
        List<Map<String, Object>> emoList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 1);
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);     
        //分析EMO
        if(emoList != null && emoList.size() > 0){
            for(int i = 0; i < emoList.size(); i++){
                Map<String, Object> map = emoList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long ledgerId = Long.valueOf(map.get("OBJECT_ID").toString());
                LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = ledgerManagerMapper.getLedgerThresholdInfo(ledgerId, 4l);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, alarmVal), 100, 2);
                queryMap.put("ledgerId", ledgerId);
                List<Map<String, Object>> values = this.dataQueryMapper.getLedger96ApData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,1,ledgerId);
                        if(!lastLedgerPowerReverse.keySet().contains(ledgerId)){
                            if(data < alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastLedgerPowerReverse.put(ledgerId,eventBean);
                        }
                        else{
                            EventBean lastEvent = lastLedgerPowerReverse.get(ledgerId);
                            if(lastEvent.getEventStatus() == -1 && data < alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastLedgerPowerReverse.put(ledgerId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data >= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastLedgerPowerReverse.remove(ledgerId);
                            }          
                        }
                    }
                }
            }
        }
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
                MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = meterManagerMapper.getMeterThresholdInfo(meterId, 4l);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, alarmVal), 100, 2);
                queryMap.put("meterId", meterId);
                List<Map<String, Object>> values = this.dataQueryMapper.getMeterCurApData(queryMap);

                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
                        if(!lastMeterPowerReverse.keySet().contains(meterId)){
                            if(data < alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastMeterPowerReverse.put(meterId,eventBean);
                        }
                        else{
                            EventBean lastEvent = lastMeterPowerReverse.get(meterId);
                            if(lastEvent.getEventStatus() == -1 && data < alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastMeterPowerReverse.put(meterId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data >= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastMeterPowerReverse.remove(meterId);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void analysisPf(Long eventId, Date beginTime, Date endTime){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startDate", beginTime);
        queryMap.put("endDate", endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的EMO和DCP
        List<Map<String, Object>> emoList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 1);
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);
        if(emoList != null &&dcpList!= null)

        //分析EMO
        if(emoList != null && emoList.size() > 0){
            for(int i = 0; i < emoList.size(); i++){
                Map<String, Object> map = emoList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long ledgerId = Long.valueOf(map.get("OBJECT_ID").toString());
                LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
//                alarmVal = DataUtil.doubleSubtract(alarmVal, 100);
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(1, alarmVal), 100, 2);
                queryMap.put("ledgerId", ledgerId);
                List<Map<String, Object>> values = this.dataQueryMapper.getLedger96PfData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,1,ledgerId);
                        if(!lastLedgerPf.keySet().contains(ledgerId)){
                            if(data < alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "; "
                                        + "实际值: " + data.toString() + ";";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastLedgerPf.put(ledgerId,eventBean);
                        }
                        else{
                            EventBean lastEvent = lastLedgerPf.get(ledgerId);
                            if(lastEvent.getEventStatus() == -1 && data < alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "; "
                                        + "实际值: " + data.toString() + ";";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastLedgerPf.put(ledgerId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data >= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastLedgerPf.remove(ledgerId);
                            }
                        }
                    }
                }
            }
        }
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
                MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
//                alarmVal = DataUtil.doubleSubtract(alarmVal, 100);
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(1, alarmVal), 100, 2);
                queryMap.put("meterId", meterId);
                List<Map<String, Object>> values = this.dataQueryMapper.getMeterCurPfData(queryMap);

                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
                        if(!lastMeterPf.keySet().contains(meterId)){
                            if(data < alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "; "
                                        + "实际值: " + data.toString() + ";";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastMeterPf.put(meterId,eventBean);
                        }
                        else{
                            EventBean lastEvent = lastMeterPf.get(meterId);
                            if(lastEvent.getEventStatus() == -1 && data < alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "; "
                                        + "实际值: " + data.toString() + ";";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastMeterPf.put(meterId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data >= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastMeterPf.remove(meterId);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void analysisDemand(Long eventId, Date beginTime, Date endTime){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startDate", beginTime);
        queryMap.put("endDate", endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的EMO和DCP
        List<Map<String, Object>> emoList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 1);
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);
        //分析EMO
        if(emoList != null && emoList.size() > 0){
            for(int i = 0; i < emoList.size(); i++){
                Map<String, Object> map = emoList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long ledgerId = Long.valueOf(map.get("OBJECT_ID").toString());
                LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
                queryMap.put("ledgerId", ledgerId);
                List<Map<String, Object>> values = this.dataQueryMapper.getLedger96ApData(queryMap);  //计算值
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        //计算申报需量
                        Map<String, Object> declare = getBasicFeeInfo(ledgerId, 1, dataTime);
                        Integer declareType = (Integer) declare.get("declareType");
                        Double declareValue = (Double) declare.get("declareValue");if(declareValue==null)continue;
                        Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                        alarmVal = new BigDecimal(declareValue).multiply(new BigDecimal(alarmVal)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,1,ledgerId);
                        if(!lastLedgerDemand.keySet().contains(ledgerId)){
                            if( checkDeclare(declareType, declareValue) ){
                                if(data > alarmVal){
                                    eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                    String content = eventName + "; "
                                            + "能管单元: " + ledger.getLedgerName() + "; "
                                            + "报警值: " + alarmVal.toString() + "kW; "
                                            + "实际值: " + data.toString() + "kW;";
                                    eventBean.setEventContent(content);
                                    insetNewEvent(eventBean, parentId); //入数据库、推送
                                }
                                lastLedgerDemand.put(ledgerId,eventBean);
                            }
                        }
                        else{
                            EventBean lastEvent = lastLedgerDemand.get(ledgerId);
                            if( checkDeclare(declareType, declareValue) && (lastEvent.getEventStatus() == -1 && data > alarmVal) ){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastLedgerDemand.put(ledgerId, eventBean);
                            }
                            else if( !checkDeclare(declareType, declareValue) || (lastEvent.getEventStatus() == 1 && data <= alarmVal) ){
                            updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastLedgerDemand.remove(ledgerId);
                            }           
                        }
                    }
                }
            }
        }
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
                MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                queryMap.put("meterId", meterId);
                List<Map<String, Object>> values = this.dataQueryMapper.getMeterCurApData(queryMap);  //计算值
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        //计算申报需量
                        Map<String, Object> declare = getBasicFeeInfo(meterId, 2, dataTime);
                        Integer declareType = (Integer) declare.get("declareType");
                        Double declareValue = (Double) declare.get("declareValue");if(declareValue==null)continue;
                        Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                        alarmVal = new BigDecimal(declareValue).multiply(new BigDecimal(alarmVal)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
                        if(!lastMeterDemand.keySet().contains(meterId)){
                            if( checkDeclare(declareType, declareValue) ){
                                if(data > alarmVal){
                                    eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                    String content = eventName + "; "
                                            + "采集点: " + meter.getMeterName() + "; "
                                            + "报警值: " + alarmVal.toString() + "kW; "
                                            + "实际值: " + data.toString() + "kW;";
                                    eventBean.setEventContent(content);
                                    insetNewEvent(eventBean, parentId); //入数据库、推送
                                }
                                lastMeterDemand.put(meterId,eventBean);
                            }
                        }
                        else{
                            EventBean lastEvent = lastMeterDemand.get(meterId);
                            if( checkDeclare(declareType, declareValue) && (lastEvent.getEventStatus() == -1 && data > alarmVal) ){
                            	eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "kW; "
                                        + "实际值: " + data.toString() + "kW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastMeterDemand.put(meterId, eventBean);
                            }
                            else if( !checkDeclare(declareType, declareValue) || (lastEvent.getEventStatus() == 1 && data <= alarmVal) ){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastMeterDemand.remove(meterId);
                            }   
                        }
                    }
                }
            }
        }
    }

    @Override
    public void analysisV(Long eventId, Date beginTime, Date endTime){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startDate", beginTime);
        queryMap.put("endDate", endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的DCP
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);  
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
                MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = meterManagerMapper.getMeterThresholdInfo(meterId, 1L);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, alarmVal), 100, 2);
                queryMap.put("meterId", meterId);
                queryMap.put("ratio", getMeterVICommonMode(meterId));
                List<Map<String, Object>> values = this.dataQueryMapper.getMeterCurVData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
                        if(!lastMeterV.keySet().contains(meterId)){
                            if(data > alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "V; "
                                        + "实际值: " + data.toString() + "V;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastMeterV.put(meterId,eventBean);
                        }
                        else{
                            EventBean lastEvent = lastMeterV.get(meterId);
                            if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "V; "
                                        + "实际值: " + data.toString() + "V;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastMeterV.put(meterId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastMeterV.remove(meterId);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void analysisI(Long eventId, Date beginTime, Date endTime){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startDate", beginTime);
        queryMap.put("endDate", endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的DCP
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);   
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
                MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = meterManagerMapper.getMeterThresholdInfo(meterId, 3L);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = new BigDecimal(standard).multiply(new BigDecimal(alarmVal)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                queryMap.put("meterId", meterId);
                List<Map<String, Object>> values = this.dataQueryMapper.getMeterCurIData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
                        if(!lastMeterI.keySet().contains(meterId)){
                            if(data > alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "A; "
                                        + "实际值: " + data.toString() + "A;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastMeterI.put(meterId,eventBean);
                        }
                        else{
                            EventBean lastEvent = lastMeterI.get(meterId);
                            if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "A; "
                                        + "实际值: " + data.toString() + "A;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastMeterI.put(meterId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastMeterI.remove(meterId);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void processEventCount(){
        Map<Long, String> result = new HashMap<Long, String>();
        Date temp = DateUtil.clearDate(new Date());
        Date endTime = DateUtil.getSomeNextSecondDate(temp, -1);
        Date beginTime = DateUtil.clearDate(endTime);
        List<Map<String, Object>> mapList = this.eventQueryMapper.getLedgerEventCount(beginTime, endTime);
        int allNum = this.eventQueryMapper.getAllEventIdNum();
        if(mapList != null && mapList.size() > 0){
            int listSize = mapList.size();
            for(int i = 0; i < listSize; i++){
                Long ledgerId = Long.valueOf(mapList.get(i).get("LEDGER_ID").toString());
                String eventName = mapList.get(i).get("EVENT_NAME").toString();
                Object count = mapList.get(i).get("COUNT");
                if(count == null){
                    eventName = eventName + "0次";
                }
                else {
                    eventName = eventName + count.toString() + "次";
                }

                if((i % allNum) == 0){
                    result.put(ledgerId, DateUtil.convertDateToStr(beginTime, DateUtil.MOUDLE_PATTERN) + "至"
                            + DateUtil.convertDateToStr(endTime, DateUtil.MOUDLE_PATTERN)
                            + ",贵单位监测区域 " + eventName + ",");
                }
                else {
                    result.put(ledgerId, result.get(ledgerId) + eventName + ",");
                    if((i % allNum) == (allNum-1)){
                        result.put(ledgerId, result.get(ledgerId) + "详情请查看事件查询。");
                    }
                }
            }
        }
        //事件统计推送
        for (Long key : result.keySet()) {
            List<Long> userIds = this.userBeanMapper.getAccountIds(key);
            if(userIds != null && userIds.size() > 0){
                for(int i = 0; i < userIds.size(); i++){
                    Long userId = userIds.get(i);
                    UserBean userBean = userBeanMapper.getUserByAccountId(userId);
                    List<String> strUserIds = new ArrayList<String>();
                    strUserIds.add(userId.toString());
                    Map<String, String> para = new HashMap<String, String>();
                    if(JPushUtil.checkSendEnable(userBean.getFreeTimePeriod(),userBean.getIsShield())){ // 不在时段内则立即推送, TODO 在时段内暂不处理
                        para.put("triggerType", "1");
                        JPushUtil.sendPushByAlias(strUserIds, "昨日事件统计", result.get(key), para);
                    }
                }
            }
        }
    }


    //事件入数据库
    private void insetNewEvent(EventBean eventBean, Long pLedgerId){
        int existNum = this.eventQueryMapper.getExistEventNum(eventBean);
        if(existNum == 0){
            eventBean.setEventRecId(SequenceUtils.getDBSequence());
           try{
           	eventQueryMapper.addEvent(eventBean);
           }catch(Exception ec){
           	Log.error("插入事件失败--"+eventBean.getEventName()+"--"+eventBean.getEventId()+"--"+eventBean.getEventContent());
           }

            ////推送
            List<Long> userIds = this.eventQueryMapper.getBookedUserByLedgerEvent(pLedgerId, eventBean.getEventId());
            if(userIds != null && userIds.size() > 0){
                Map<String, String> para = new HashMap<String, String>();
                para.put("type", "event");
                para.put("A", Long.toString(eventBean.getEventRecId()));
                para.put("B", Long.toString(eventBean.getEventId()));
                para.put("C", Integer.toString(eventBean.getObjectType()));
                para.put("D", Long.toString(eventBean.getObjectId()));
                String content = DateUtil.convertDateToStr(eventBean.getEventStartTime(), DateUtil.DEFAULT_PATTERN)
                                 + eventBean.getEventContent();
                // 判断是否在屏蔽时间段内，拼接接收规则
                for(int i = 0; i < userIds.size(); i++){
                    List<String> alias = new ArrayList<String>();
                    Long userId = userIds.get(i);
                    alias.add(userId.toString());
                    UserBean userBean = userBeanMapper.getUserByAccountId(userId);

                   
                        if(JPushUtil.checkSendEnable(userBean.getFreeTimePeriod(),userBean.getIsShield())){ // 立即推送
                            para.put("triggerType", "1");
                            JPushUtil.sendPushByAlias(alias, eventBean.getEventName(), content, para);
                        } else {
                            messageService.insertMsgHis(userId, eventBean.getEventRecId(), new Date(), 4);
                        }
                }
                //// 检查这些user所接收到的消息是否达到配置的条数
                this.phoneService.checkUserMessage(userIds);
            }
        }
    }

    //更新事件结束时间到数据库
    private void updateNewEventEndtime(Date dataTime, EventBean lastEvent){
        List<Long> recidList = this.eventQueryMapper.getRecIdsByEvent(lastEvent);
        if(recidList != null && recidList.size() == 1){
            Long recid = recidList.get(0);
            try{this.eventQueryMapper.updateEventEndtime(recid, dataTime);}catch(Exception ec){Log.error("更新事件失败--"+lastEvent.getEventName()+"--"+lastEvent.getEventId()+"--"+lastEvent.getEventContent());}
        }
    }

    //处理休息时段
    private void processRestTime(Long parentId, Map<String, Object> queryMap){
        Date beginTime = (Date) queryMap.get("startDate");
        Date endTime = (Date) queryMap.get("endDate");
        //判断是否法定节假日
        int defaultHoliday = this.eventQueryMapper.includeDefaultHoliday(parentId);
        if(defaultHoliday > 0){
            List<HolidayBean> holidayList = OfficialHolidayUtils.readOfficialHolidayList();
            if(holidayList != null && holidayList.size() > 0){
                for(int i = 0; i < holidayList.size(); i++){
                    HolidayBean holidayBean = holidayList.get(i);
                    int minusFromDate = DateUtil.daysBetweenDates(DateUtil.clearDate(beginTime), holidayBean.getFromDate());
                    int minusEndDate = DateUtil.daysBetweenDates(DateUtil.clearDate(beginTime), holidayBean.getEndDate());
                    if(minusFromDate >= 0 && minusEndDate <= 0){
                        return;
                    }
                }
            }
        }
        //判断是否是自定义节假日
        int ifHoliday = this.eventQueryMapper.judgeHoliday(parentId, DateUtil.clearDate(beginTime));
        if(ifHoliday > 0){
            return;
        }
        //判断是否休息日
        Integer ifRestDay = this.eventQueryMapper.judgeRestDay(parentId, DateUtil.getDateWeekNum(beginTime));
        if(ifRestDay != null && ifRestDay == 1){
            return;
        }
        else{
            //判断是否工作日的休息时间段
            List<Map<String, Object>> beginRange = this.eventQueryMapper.getTimeRangeByTime(parentId, beginTime);
            if(beginRange != null && beginRange.size() > 0){
                String range_2_Str = DateUtil.convertDateToStr(endTime, DateUtil.SHORT_PATTERN)
                                     + " " + beginRange.get(0).get("REST_END").toString();
                Date range_2 = DateUtil.convertStrToDate(range_2_Str, DateUtil.MOUDLE_PATTERN);
                if(range_2.before(endTime)){
                    queryMap.put("endDate", range_2);
                }
            }
            else{
                List<Map<String, Object>> endRange = this.eventQueryMapper.getTimeRangeByTime(parentId, endTime);
                if(endRange != null && endRange.size() > 0){
                    String range_1_Str = DateUtil.convertDateToStr(beginTime, DateUtil.SHORT_PATTERN)
                                         + " " + endRange.get(0).get("REST_BEGIN").toString();
                    Date range_1 = DateUtil.convertStrToDate(range_1_Str, DateUtil.MOUDLE_PATTERN);
                    queryMap.put("startDate", range_1);
                }
                else{
                    queryMap.remove("startDate");
                    queryMap.remove("endDate");
                }
            }
        }
    }

    //计算EMO、DCP某个月的需量申报模式和申报值
    private Map<String, Object> getBasicFeeInfo(Long id, Integer objType, Date dataTime){
        Map<String, Object> result = new HashMap<String, Object>();

        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("pointId", id);
        Date beginTime = DateUtil.getCurrMonthFirstDay(dataTime);
        Date endTime = DateUtil.getMonthLastDay(dataTime);
        queryMap.put("beginTime", beginTime);
        queryMap.put("endTime", endTime);
        List<Map<String, Object>> basicFeeInfos = new ArrayList<Map<String, Object>>();
        if(objType == 1){
            basicFeeInfos = costMapper.getLedgerBasicFeeInfo(queryMap);
        }
        else if(objType == 2){
            basicFeeInfos = costMapper.getMeterBasicFeeInfo(queryMap);
        }
        Integer declareType = null;// 申报类型;1,容量;2,需量
        Double declareValue = null;// 申报值
        if (basicFeeInfos != null && basicFeeInfos.size() > 0){
            Map<String, Object> basicFeeInfo = basicFeeInfos.get(0);
            if (basicFeeInfo != null && basicFeeInfo.size() > 0) {
                if (basicFeeInfo.get("DECLARETYPE") != null) {
                    declareType = Integer.valueOf(basicFeeInfo.get("DECLARETYPE").toString());
                }
                if (basicFeeInfo.get("DECLAREVALUE") != null) {
                    declareValue = Double.valueOf(basicFeeInfo.get("DECLAREVALUE").toString());
                }
            }
        }

        result.put("declareType", declareType);
        result.put("declareValue", declareValue);
        return result;
    }

    //检查需量申报模式和申报值
    private boolean checkDeclare(Integer declareType, Double declareValue){
        return (declareType != null && declareType == 2 && declareValue != null);
    }

    //根据采集点标记的连线方式 得到是否需要乘以相应系数
    private double getMeterVICommonMode(Long meterId){
        double result = 1;
        Integer commonMode = meterManagerMapper.getCommModeByMeterId(meterId);
        if(commonMode != null && commonMode == 2){
            result = 1.732;
        }
        return result;
    }
    
    /**
     * 新增一个分析电压越下限方法
     * @author
     * @param eventId
     * @param beginTime
     * @param endTime
     * @return void
     * @exception
     * @date 2019/5/23 18:12
     */
    @Override
    public void analysisLowerV(Long eventId, Date beginTime, Date endTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("startDate", beginTime);
        queryMap.put("endDate", endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的DCP
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
                MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = meterManagerMapper.getMeterThresholdInfo(meterId, 1L);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, alarmVal), 100, 2);
                queryMap.put("meterId", meterId);
                queryMap.put("ratio", getMeterVICommonMode(meterId));
                Integer comm = meterManagerMapper.getCommModeByMeterId( meterId );
                queryMap.put( "comm", comm );   //增加判断三相三线,还是三相四线
                List<Map<String, Object>> values = this.dataQueryMapper.getMeterMinCurVData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
                        if(!lastMeterLowerV.keySet().contains(meterId)){
                            if(data < alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "V; "
                                        + "实际值: " + data.toString() + "V;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastMeterLowerV.put(meterId,eventBean);
                        } else{
                            EventBean lastEvent = lastMeterLowerV.get(meterId);
                            if(lastEvent.getEventStatus() == -1 && data < alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "采集点: " + meter.getMeterName() + "; "
                                        + "报警值: " + alarmVal.toString() + "V; "
                                        + "实际值: " + data.toString() + "V;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastMeterLowerV.put(meterId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data >= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastMeterLowerV.remove(meterId);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 水量超限事件分析(曲线)
     * @author catkins.
     * @param eventId
     * @param beginTime
     * @param endTime
     * @return void
     * @exception
     * @date 2019/10/8 9:03
     */
    @Override
    public void analysisW(Long eventId, Date beginTime, Date endTime) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("startDate", beginTime);
		queryMap.put("endDate", endTime);
		//获取事件类型
		Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
		String eventName = eventType.get("EVENT_NAME").toString();
		//查询设置该事件为关注的DCP
		List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 2);
		//分析DCP
		if(dcpList != null && dcpList.size() > 0){
			for(int i = 0; i < dcpList.size(); i++){
				Map<String, Object> map = dcpList.get(i);
				Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
				Long meterId = Long.valueOf(map.get("OBJECT_ID").toString());
				MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(meterId);
				Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
				Map<String, Object> thresholdInfo = meterManagerMapper.getMeterThresholdInfo(meterId, 5L);
				if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
					continue; //没有标准值的就放弃
				}
				Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
				alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, alarmVal), 100, 2);
				queryMap.put("meterId", meterId);
				List<Map<String, Object>> values = this.dataQueryMapper.getMeterWaterData(queryMap);
				if(values != null && values.size() > 0){
					for(int j = 0; j < values.size(); j++){
						Map<String, Object> value = values.get(j);
						Date dataTime = (Date) value.get("DATATIME");
						Double data = Double.valueOf(value.get("DATA").toString());
						EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,meterId);
						if(!lastMeterW.keySet().contains(meterId)){
							if(data > alarmVal){
								eventBean.setEventStatus(1); //1表示发生、-1为默认值
								String content = eventName + "; "
										+ "采集点: " + meter.getMeterName() + "; "
										+ "报警值: " + alarmVal.toString() + "m³; "
										+ "实际值: " + data.toString() + "m³;";
								eventBean.setEventContent(content);
								insetNewEvent(eventBean, parentId); //入数据库、推送
							}
							lastMeterW.put(meterId,eventBean);
						} else{
							EventBean lastEvent = lastMeterW.get(meterId);
							if(lastEvent.getEventStatus() == -1 && data > alarmVal){
								eventBean.setEventStatus(1);
								String content = eventName + "; "
										+ "采集点: " + meter.getMeterName() + "; "
										+ "报警值: " + alarmVal.toString() + "m³; "
										+ "实际值: " + data.toString() + "m³;";
								eventBean.setEventContent(content);
								insetNewEvent(eventBean, parentId); //入数据库、推送
								lastMeterW.put(meterId,eventBean);
							}
							else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
								updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
								lastMeterW.remove(meterId);
							}
						}
					}
				}
			}
		}
    }
	
	/**
	 * 分析"日用能超配额告警"事件:水
     * 每日执行,与以上任务不同
	 * @author catkins.
	 * @param eventId
	 * @param beginTime
	 * @param endTime
	 * @return void
	 * @exception
	 * @date 2019/10/9 14:28
	 */
	@Override
	public void analysisW_D(Long eventId, Date dateTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("dateTime", dateTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的DCP
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 1);
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long ledgerId = Long.valueOf(map.get("OBJECT_ID").toString());
                LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = ledgerManagerMapper.getLedgerThresholdValue(ledgerId,eventId);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, alarmVal), 100, 2);
                queryMap.put( "ledgerId" , ledgerId);
                queryMap.put( "eventId" , eventId );
                List<Map<String, Object>> values = null;
                values = this.dataQueryMapper.getLedgerDayData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,1,ledgerId);
                        if(!lastLedgerW_D.keySet().contains(ledgerId)){
                            if(data > alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "m³; "
                                        + "实际值: " + data.toString() + "m³;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastLedgerW_D.put(ledgerId,eventBean);
                        } else{
                            EventBean lastEvent = lastLedgerW_D.get(ledgerId);
                            if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "m³; "
                                        + "实际值: " + data.toString() + "m³;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastLedgerW_D.put(ledgerId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastLedgerW_D.remove(ledgerId);
                            }
                        }
                    }
                }
            }
        }
	}
    
    /**
     * 分析"日用能超配额告警"事件:气
     * 每日执行,与以上任务不同
     * @author catkins.
     * @param eventId
     * @param beginTime
     * @param endTime
     * @return void
     * @exception
     * @date 2019/10/9 14:28
     */
    @Override
    public void analysisG_D(Long eventId, Date dateTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("dateTime", dateTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的DCP
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 1);
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long ledgerId = Long.valueOf(map.get("OBJECT_ID").toString());
                LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = ledgerManagerMapper.getLedgerThresholdValue(ledgerId,eventId);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, alarmVal), 100, 2);
                queryMap.put( "ledgerId" , ledgerId);
                queryMap.put( "eventId" , eventId );
                List<Map<String, Object>> values = null;
                values = this.dataQueryMapper.getLedgerDayData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,1,ledgerId);
                        if(!lastLegderG_D.keySet().contains(ledgerId)){
                            if(data > alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "m³; "
                                        + "实际值: " + data.toString() + "m³;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastLegderG_D.put(ledgerId,eventBean);
                        } else{
                            EventBean lastEvent = lastLegderG_D.get(ledgerId);
                            if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "m³; "
                                        + "实际值: " + data.toString() + "m³;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastLegderG_D.put(ledgerId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastLegderG_D.remove(ledgerId);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 分析"日用能超配额告警"事件:电
     * 每日执行,与以上任务不同
     * @author catkins.
     * @param eventId
     * @param beginTime
     * @param endTime
     * @return void
     * @exception
     * @date 2019/10/9 14:28
     */
    @Override
    public void analysisE_D(Long eventId, Date dateTime) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("dateTime", dateTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        //查询设置该事件为关注的DCP
        List<Map<String, Object>> dcpList = this.eventQueryMapper.getSettingEmoIdsByEvent(eventId, 1);
        //分析DCP
        if(dcpList != null && dcpList.size() > 0){
            for(int i = 0; i < dcpList.size(); i++){
                Map<String, Object> map = dcpList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                Long ledgerId = Long.valueOf(map.get("OBJECT_ID").toString());
                LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
                Double alarmVal = Double.valueOf(map.get("ALARM_VALUE").toString());
                Map<String, Object> thresholdInfo = ledgerManagerMapper.getLedgerThresholdValue(ledgerId,eventId);
                if(thresholdInfo == null || !thresholdInfo.keySet().contains("THRESHOLDVALUE")){
                    continue; //没有标准值的就放弃
                }
                Double standard = Double.valueOf(thresholdInfo.get("THRESHOLDVALUE").toString());
                alarmVal = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, alarmVal), 100, 2);
                queryMap.put( "ledgerId" , ledgerId);
                 queryMap.put( "eventId" , eventId );
                List<Map<String, Object>> values = null;
                values = this.dataQueryMapper.getLedgerDayData(queryMap);
                if(values != null && values.size() > 0){
                    for(int j = 0; j < values.size(); j++){
                        Map<String, Object> value = values.get(j);
                        Date dataTime = (Date) value.get("DATATIME");
                        Double data = Double.valueOf(value.get("DATA").toString());
                        EventBean eventBean = new EventBean(dataTime,eventName,eventId,1,ledgerId);
                        if(!lastLedgerE_D.keySet().contains(ledgerId)){
                            if(data > alarmVal){
                                eventBean.setEventStatus(1); //1表示发生、-1为默认值
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "KW; "
                                        + "实际值: " + data.toString() + "KW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                            }
                            lastLedgerE_D.put(ledgerId,eventBean);
                        } else{
                            EventBean lastEvent = lastLedgerE_D.get(ledgerId);
                            if(lastEvent.getEventStatus() == -1 && data > alarmVal){
                                eventBean.setEventStatus(1);
                                String content = eventName + "; "
                                        + "能管单元: " + ledger.getLedgerName() + "; "
                                        + "报警值: " + alarmVal.toString() + "KW; "
                                        + "实际值: " + data.toString() + "KW;";
                                eventBean.setEventContent(content);
                                insetNewEvent(eventBean, parentId); //入数据库、推送
                                lastLedgerE_D.put(ledgerId,eventBean);
                            }
                            else if(lastEvent.getEventStatus() == 1 && data <= alarmVal){
                                updateNewEventEndtime(dataTime, lastEvent);  //更新数据库（恢复时间）
                                lastLedgerE_D.remove(ledgerId);
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 停上电事件
     * @author catkins.
     * @param eventId
     * @param dateTime
     * @return void
     * @exception
     * @date 2019/11/29 9:56
     */
    @Override
    public void failure(Long eventId, Date beginTime, Date endTime) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("startDate", beginTime);
        queryMap.put("endDate", endTime);
        //获取事件类型
        Map<String, Object> eventType = this.eventQueryMapper.getEventTypeByID(eventId);
        String eventName = eventType.get("EVENT_NAME").toString();
        List<Map<String, Object>> values = this.eventQueryMapper.getPullData(queryMap);
        if(values != null && values.size() > 0){
            for(int j = 0; j < values.size(); j++){
                Map<String, Object> value = values.get(j);
                Map<String, Object> ledgerMap = this.eventQueryMapper.getEmo4pull( Long.parseLong( value.get( "METER_ID" ).toString() ));
                Date dataTime = (Date) value.get("CREATE_TIME");
                 EventBean eventBean = new EventBean(dataTime,eventName,eventId,2,Long.parseLong( value.get( "METER_ID" ).toString() ));
                 eventBean.setEventStatus(0); //1表示发生、-1为默认值
                String content = eventName + "; "
                        + "采集点: " + value.get( "METER_NAME" ).toString() + "; "
                        + "终端: " + value.get( "TERMINAL_NAME" ).toString() + "; ";
                eventBean.setEventContent(content);
                String endDate = value.get( "ENDTIME" ).toString();
                eventBean.setEventEndTime( DateUtil.convertStrToDate(endDate,DateUtil.DEFAULT_PATTERN) );
                if(ledgerMap != null && ledgerMap.containsKey( "LEDGER_ID" ))
                    insetNewEvent(eventBean, Long.parseLong( ledgerMap.get( "LEDGER_ID" ).toString() )); //入数据库、推送
            }
        }
    }
}
