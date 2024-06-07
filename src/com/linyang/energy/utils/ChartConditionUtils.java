package com.linyang.energy.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;

/**
 * 此类仅限于Cherry自己用，不会用者请不要用
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 7, 2014
 */
public class ChartConditionUtils {
	
	
	private ChartConditionUtils(){}
	
	/**
	 * 时间转换
	 * @param queryMap
	 * @return
	 */
	public static  Map<String,Object> processDate(Map<String,Object> queryMap){
		TimeEnum timeType =  TimeEnum.formInt2TimeEnum(Integer.valueOf(queryMap.get("timeType").toString()));
		long beginDate = Long.valueOf(queryMap.get("beginDate").toString());
		long endDate =  Long.valueOf(queryMap.get("endDate").toString());
		if(timeType !=TimeEnum.DAY){
			if(timeType==TimeEnum.MONTH){
				queryMap.put("beginTime",new Date(DateUtils.convertTimeToLong(DateUtils.convertTimeToString(beginDate, timeType.getDateFormat())+"-01", DateUtils.FORMAT_SHORT)*1000));
				queryMap.put("endTime",new Date(DateUtils.convertTimeToLong(DateUtil.getMonthLastDay(DateUtils.convertTimeToString(endDate,DateUtils.FORMAT_SHORT)), DateUtils.FORMAT_SHORT)*1000));
			}else if(timeType==TimeEnum.YEAR){
				queryMap.put("beginTime",new Date(DateUtils.convertTimeToLong(DateUtils.convertTimeToString(beginDate, timeType.getDateFormat())+"-01-01", DateUtils.FORMAT_SHORT)*1000));
				queryMap.put("endTime",new Date(DateUtils.convertTimeToLong(DateUtils.convertTimeToString(endDate, timeType.getDateFormat())+"-12-31", DateUtils.FORMAT_SHORT)*1000));
			}else if(timeType==TimeEnum.WEEK){//周
				queryMap.put("beginTime",new Date(DateUtils.convertTimeToLong(DateUtil.getMonday(DateUtils.convertTimeToString(beginDate, timeType.getDateFormat())), DateUtils.FORMAT_SHORT)*1000));
				queryMap.put("endTime",new Date(DateUtils.convertTimeToLong(DateUtil.getSunday(DateUtils.convertTimeToString(endDate, timeType.getDateFormat())), DateUtils.FORMAT_SHORT)*1000));
			}else{//时 分 秒
				queryMap.put("beginTime",new Date(beginDate*1000));
				queryMap.put("endTime",new Date(DateUtils.convertTimeToLong(DateUtils.convertTimeToString(endDate, DateUtils.FORMAT_SHORT)+" 23:59:59")*1000));
			}
		}else{
			Date beginTime = new Date(beginDate*1000);
			queryMap.put("beginTime",beginTime);
			Date endTime = new Date(endDate*1000);
			queryMap.put("endTime",endTime);
		}
		return queryMap;
	}
	
	/**
	 * 保留2位小数
	 * @param list
	 * @return
	 */
	public static  List<ChartItemWithTime> itemDataScale(List<ChartItemWithTime> list){
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (ChartItemWithTime chartItemWithTime : list) {
				chartItemWithTime.dataScale();
			}
		}
		return list;
	}
	
	/**
	 * 保留2位小数
	 * @param list
	 * @return
	 */
	public static  List<ChartItemWithTime> itemDataScale2(List<ChartItemWithTime> list){
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (ChartItemWithTime chartItemWithTime : list) {
				chartItemWithTime.dataScale2();
			}
		}
		return list;
	}
}
