package com.linyang.energy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.dto.ChartItem;
import com.linyang.energy.dto.ChartItemWithDatas;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.contrastanalysis.TrendForecastMapper;
import com.linyang.energy.service.TrendForecastService;
import com.linyang.energy.utils.ChartConditionUtils;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.DateUtil;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;

/**
 * 趋势预测业务逻辑层接口实现类
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 15, 2014
 */
@Service
public class TrendForecastServiceImpl implements TrendForecastService {

	@Autowired
	private TrendForecastMapper trendForecastMapper;
	
	
	@Override
	public Map<String,Object> getTrendForecastChartDatas(Map<String, Object> queryCondition) {
		Map<String,Object> result = new HashMap<String, Object>();
		boolean isCanForecast = true;
		queryCondition = ChartConditionUtils.processDate(queryCondition);
		TimeEnum timeType =  TimeEnum.formInt2TimeEnum(Integer.valueOf(queryCondition.get("timeType").toString()));
		long nowTime = com.leegern.util.DateUtil.getCurrentDate(DateUtils.FORMAT_SHORT).getTime();
		Date beginTime = (Date)queryCondition.get("beginTime");
		Date endTime = (Date)queryCondition.get("endTime");
		ChartItem hostory = null;
		ChartItem future = null;
		Double growth = null;
		String historyChartName = queryCondition.get("historyChartName")==null?"历史数据":queryCondition.get("historyChartName").toString();
		String futureChartName = queryCondition.get("futureChartName")==null?"预测数据":queryCondition.get("futureChartName").toString();
		if(endTime.getTime() >= nowTime){//需要预测未来的
			if(timeType == TimeEnum.MINUTE){//查询曲线数据
				List<Map<String, Object>> curDatas = trendForecastMapper.getCurDatas(queryCondition);
				hostory = buildCurDatas(historyChartName, curDatas,false);
			}else if(beginTime.getTime() <= nowTime){//时间小于今天去查询
				List<Map<String, Object>> statDatas = trendForecastMapper.getStatDatas(queryCondition);
				hostory = buildSingleDatas(historyChartName,beginTime.getTime()/1000,endTime.getTime()/1000,timeType,statDatas,false);
			}
			
			//预测数据时拿到的去年同期的数据
			if(timeType == TimeEnum.MINUTE){//查询曲线数据
				if(beginTime.getTime()>System.currentTimeMillis())
					queryCondition.put("beginTime",DateUtil.getLastYearDate(beginTime));
				else
					queryCondition.put("beginTime",DateUtil.getLastYearDate(new Date()));
				
				queryCondition.put("endTime",DateUtil.getLastYearDate(endTime));
				List<Map<String, Object>> curDatas = trendForecastMapper.getCurDatas(queryCondition);
				//去年同期的数据
				future = buildCurDatas(futureChartName, curDatas,true);
			}else{
				Date first = null;
				//今天的数据还没出来，默认拿昨天的数据
				if(beginTime.getTime()>System.currentTimeMillis()){
					 first = beginTime;
					 if(timeType == TimeEnum.MONTH){
							first =  DateUtil.getCurrMonthFirstDay(beginTime);
						}
						if(timeType == TimeEnum.YEAR){	
							first =DateUtil.getYearFirstDate(beginTime);
						}
						if(timeType == TimeEnum.WEEK){
							first = DateUtils.convertShortDateStr2Date(DateUtils.getMonday(DateUtil.convertDateToStr(beginTime, DateUtils.FORMAT_SHORT)));
						} 
				}else{
					 first = DateUtils.convertShortDateStr2Date(DateUtil.getCurrentDateStr(DateUtils.FORMAT_SHORT));
					if(timeType == TimeEnum.MONTH){
						first = DateUtils.getMonthFirstDayDate();
					}
					if(timeType == TimeEnum.YEAR){	
						first =DateUtils.convertShortDateStr2Date(DateUtils.getCurrentYearFirst());
					}
					if(timeType == TimeEnum.WEEK){
						first = DateUtils.convertShortDateStr2Date(DateUtils.getMonday(DateUtil.getCurrentDateStr(DateUtils.FORMAT_SHORT)));
					}
				}
				queryCondition.put("beginTime",DateUtil.getLastYearDate(first));
				queryCondition.put("endTime",DateUtil.getLastYearDate(endTime));
				List<Map<String, Object>> statDatas = trendForecastMapper.getStatDatas(queryCondition);
				//去年同期的数据
				future = buildSingleDatas(futureChartName,((Date)queryCondition.get("beginTime")).getTime()/1000,((Date)queryCondition.get("endTime")).getTime()/1000,timeType,statDatas,true);
			}
			//得到增长比再计算
			if(future != null){
				growth = getDefaultGrowth(queryCondition);
				if(growth != null)//无法预测.
					future.dataScaleWithGrowth(growth);
				else
					isCanForecast = false;
			}else
				isCanForecast = false;
		}else{//不需要预测
			if(timeType == TimeEnum.MINUTE){//查询曲线数据
				List<Map<String, Object>> curDatas = trendForecastMapper.getCurDatas(queryCondition);
				hostory = buildCurDatas(historyChartName, curDatas,false);
			}else{//查询统计表
				List<Map<String, Object>> statDatas = trendForecastMapper.getStatDatas(queryCondition);
				hostory = buildSingleDatas(historyChartName,beginTime.getTime()/1000,endTime.getTime()/1000,timeType,statDatas,false);
			}
		}
		Set<String> legend = new TreeSet<String>();
		if(hostory != null){
			hostory.dataScale();
			legend.addAll(hostory.getMap().keySet());
		}
		if(future != null){
			Set<String> keySet = future.getMap().keySet();
			if(legend.size()>0){
				//强制把预测的和历史的连起来
                if(hostory != null && hostory.getMap() != null){
                	String lastKey = hostory.getMap().lastKey();
                    if(timeType == TimeEnum.YEAR){
                        if(!future.getMap().lastKey().equals(lastKey))
                            future.getMap().put(lastKey,hostory.getMap().get(lastKey));
                        else
                            hostory.getMap().remove(lastKey);
                    }else{
                        Object value = "-";
                        for (Map.Entry<String,Object> entry :  hostory.getMap().entrySet()) {
                            if("-".equals(entry.getValue().toString())) continue;
                            lastKey = entry.getKey();  value = entry.getValue();
                        }
                        future.getMap().put(lastKey,value);
                    }
                }
				//没有的加上占位符
				for (String object : legend) {
					if(!future.getMap().containsKey(object))
						future.getMap().put(object, "-");
				}
			}
			legend.addAll(keySet);
		}
		result.put("hostory", hostory);
		result.put("future", future);
		result.put("isCanForecast", isCanForecast);
		result.put("legend",legend);
		return result;
	}

	
	
	
	private ChartItem buildSingleDatas(String chartName,long beginDate, long endDate, TimeEnum timeType,List<Map<String, Object>> list,boolean isNeddAddYear) {
		ChartItemWithTime item = null;
		StringBuffer timeString = new StringBuffer();
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (Map<String, Object> map : list) {
				double value = Double.valueOf(map.get("VAL")==null?"0":map.get("VAL").toString());
				if(item == null && map.get("VAL") !=null){
					item = new ChartItemWithTime(timeType,chartName,beginDate,endDate,"-");
					if(item != null && isNeddAddYear)
						item.convert2NextYear();
				}
				Date time = (Date)map.get("TIME_FIELD");
				if(time != null && item != null){
					timeString.append(DateUtil.convertDateToStr(time, timeType.getDateFormat()));
					if (isNeddAddYear) {
						timeString.append((Integer.parseInt(timeString.toString().substring(0, 4)) + 1)
								+ timeString.toString().substring(4));
					}
					if (timeType == TimeEnum.WEEK) {
						timeString.append(DateUtil.getMonday(timeString.toString()));
					}
					Map<String, Object> map2 = item.getMap();
					if(map2.containsKey(timeString.toString())){
						if("-".equals(map2.get(timeString.toString()).toString()))
							map2.put(timeString.toString(),value);
						else
							map2.put(timeString.toString(),DataUtil.doubleAdd(Double.valueOf(map2.get(timeString.toString()).toString()), value));
					}
				}
			}
		}
		return item;
	} 
	
	private ChartItem buildCurDatas(String chartName,List<Map<String, Object>> list,boolean isNeddAddYear){
		ChartItemWithDatas item = null;
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (Map<String, Object> map : list) {
				double value = Double.valueOf(map.get("VAL")==null?"0":map.get("VAL").toString());
				if(item == null && map.get("VAL") != null)
					item = new ChartItemWithDatas(chartName);
				Date time = (Date)map.get("TIME_FIELD");
				if(time != null && item != null){
					String timeString = DateUtil.convertDateToStr(time,TimeEnum.MINUTE.getDateFormat());
					Map<String, Object> map2 = item.getMap();
					if(map2.containsKey(timeString)){
						map2.put(timeString,DataUtil.doubleAdd(Double.valueOf(map2.get(timeString).toString()), value));
					}else{
						map2.put(timeString,value);
					}
				}
			}
			if(item != null && isNeddAddYear)
				item.convert2NextYear();
		}
		return item;
	}
	
	public Double getDefaultGrowth(Map<String, Object> queryCondition){
		
		queryCondition.put("beginTime",DateUtils.getPreMonthFirstDayDate());
		queryCondition.put("endTime",DateUtils.getPreMonthLastDayDate());
		//设置成2
		queryCondition.put("timeType", TimeEnum.MONTH.getTimeType());
		List<Map<String, Object>> statDatas = trendForecastMapper.getStatDatas(queryCondition);
		queryCondition.put("beginTime",DateUtil.getLastYearDate(((Date)queryCondition.get("beginTime"))));
		queryCondition.put("endTime",DateUtil.getLastYearDate(((Date)queryCondition.get("endTime"))));
		List<Map<String, Object>> statDatas2 = trendForecastMapper.getStatDatas(queryCondition);
		if(CommonMethod.isAllNotEmpty(statDatas,statDatas2)){
			double sum = 0;
			for (Map<String, Object> map : statDatas) 
				sum = DataUtil.doubleAdd(sum, Double.valueOf(map.get("VAL")==null?"0":map.get("VAL").toString()));
			double sum2 = 0;
			for (Map<String, Object> map : statDatas2) 
				sum2 = DataUtil.doubleAdd(sum2, Double.valueOf(map.get("VAL")==null?"0":map.get("VAL").toString()));
			if(sum2!=0) 
				return DataUtil.doubleDivide(DataUtil.doubleSubtract(sum, sum2), sum2);
			else
				return null;
		}else{
			return null;
		}
	}
	
}
