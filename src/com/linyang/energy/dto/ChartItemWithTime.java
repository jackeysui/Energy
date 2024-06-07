package com.linyang.energy.dto;

import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DateUtils;

import java.util.*;

/**
 * 图表项信息类(专门用来计算时间的)
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 12, 2013
 */
public class ChartItemWithTime extends ChartItem{
	/**
	 * 时间
	 */
	private TimeEnum timeEnum = TimeEnum.DAY;
	private static final String SHORT_PATTERN = "yyyy-MM-dd";

	/**
	 * 
	 * @param name 图表数据的名称
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 */
	public ChartItemWithTime(String name,long beginDate,long endDate) {
		this(null,name,beginDate,endDate);
	}
	
	public ChartItemWithTime(String name,SortedMap<String,Object> map,Object defaultValue) {
		super.name = name;
		this.map = map;
		if(null!=defaultValue){
			for (Map.Entry<String,Object> entry : map.entrySet()) {
				entry.setValue(defaultValue);
			}
		}
	}
	/**
	 * 
	 * @param timeEnum 时间单位
	 * @param name 图表数据的名称
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 */
	public ChartItemWithTime(TimeEnum timeEnum,String name,long beginDate,long endDate) {
		this(timeEnum,name,beginDate,endDate,0);
	}
	/**
	 * 
	 * @param timeEnum 时间单位
	 * @param name 图表数据的名称
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 * @param defaultValue 值
	 */
	public ChartItemWithTime(TimeEnum timeEnum,String name,long beginDate,long endDate,Object defaultValue) {
		super.name = name;
		if(timeEnum != null)
			this.timeEnum = timeEnum;
		if(beginDate > endDate)
			initMap(endDate,beginDate,defaultValue);
		else
			initMap(beginDate,endDate,defaultValue);
	}
	
	

	public TimeEnum getTimeEnum() {
		return timeEnum;
	}

	public void setTimeEnum(TimeEnum timeEnum) {
		this.timeEnum = timeEnum;
	}

	

	/**
	 * 初始化map
	 * @param beginDate 开始时间
	 * @param endDate 结束时间
	 */
	private void initMap(long beginDate,long endDate,Object defaultValue){
		String dateFormat = timeEnum.getDateFormat();
		String beginTime = DateUtils.convertTimeToString(beginDate, dateFormat);
		String endTime = DateUtils.convertTimeToString(endDate, dateFormat);
		
		if(timeEnum == TimeEnum.YEAR){
			Long beginTimeYear = Long.valueOf(beginTime);
			Long endTimeYear = Long.valueOf(endTime);
			for (int i = 0; i < (endTimeYear-beginTimeYear); i++) {
				map.put(String.valueOf(beginTimeYear+i),defaultValue);
			}
			map.put(endTime,defaultValue);
		}else if(timeEnum == TimeEnum.MONTH){
			String[] split = beginTime.split("-");
			int year = Integer.valueOf(split[0]);
			int month = Integer.valueOf(split[1]);
			GregorianCalendar calendar1=new GregorianCalendar();
			GregorianCalendar calendar2=new GregorianCalendar();
			calendar1.setTime(new Date(beginDate*1000));
			calendar2.setTime(new Date(endDate*1000));
			int betweenMonths = (calendar2.get(Calendar.YEAR)-calendar1.get(Calendar.YEAR))*12+calendar2.get(Calendar.MONTH)-calendar1.get(Calendar.MONTH);//月差
			map.put(beginTime,defaultValue);
			for (int i = 1; i < betweenMonths; i++) {
				month++;
				if(month >12){
					year++;
					month = 1;
				}
				map.put(String.valueOf(year)+"-"+String.valueOf(month<10?("0"+month):month),defaultValue);
			}
			map.put(endTime,defaultValue);
		}else if(timeEnum == TimeEnum.WEEK){
			//String monday = DateUtil.getMonday(beginTime);
			//map.put(monday,0);
			//String sunday = DateUtil.getSunday(endTime);
			long betweenDates = DateUtils.getDayBetweenDateAndDate(beginTime, endTime);
			String timeString = DateUtil.getMonday(beginTime);
			int count = 0;
			String dateStr = DateUtil.getMonday(beginTime);
			while(true){
				Date date = DateUtil.convertStrToDate(DateUtils.getBeforeAfterDate(dateStr,count+7),SHORT_PATTERN);
				Date endT = DateUtil.convertStrToDate(endTime,SHORT_PATTERN);
				if(!date.before(endT)){
					break;
				}
				if((betweenDates==7)&&(timeString.equals(beginTime))) {
					break;
				} else {
					map.put(DateUtils.getBeforeAfterDate(timeString,count), defaultValue);
					dateStr = DateUtils.getBeforeAfterDate(timeString,count);
				}
				count += 7;
			}
		}else if(timeEnum == TimeEnum.DAY){//日
			long betweenDates = DateUtils.getDayBetweenDateAndDate(beginTime, endTime);
			for (int i = 0; i <= betweenDates; i++) {
				map.put(DateUtils.getBeforeAfterDate(beginTime, i), defaultValue);
			}
		}else if(timeEnum == TimeEnum.MINUTE){//分
			int density = WebConstant.density;
			long m = (endDate - beginDate)/(density*60);
			for (long i = 0; i <= m; i++) {
				String timeString = DateUtils.convertTimeToString(beginDate+(i*density*60),timeEnum.getDateFormat());
				map.put(timeString,defaultValue);
			}
		}else if(timeEnum == TimeEnum.HOUR){//时
			long m = (endDate - beginDate)/(60*60);
			for (long i = 0; i <= m; i++) {
				String timeString = DateUtils.convertTimeToString(beginDate+(i*60*60),DateUtils.FORMAT_LONG);
				map.put(timeString,defaultValue);
			}
		}else if(timeEnum == TimeEnum.MINUTE15){//15分钟
			long m = (endDate - beginDate)/(15*60);
			for (long i = 0; i <= m; i++) {
				String timeString = DateUtils.convertTimeToString(beginDate+(i*15*60),DateUtils.FORMAT_LONG);
				map.put(timeString,defaultValue);
			}
		}else if(timeEnum == TimeEnum.DAYMINUTE){//一日
			int density = WebConstant.density;
			long m = (endDate - beginDate)/(density*60);
			for (long i = 0; i <= m; i++) {
				String timeString = DateUtils.convertTimeToString(beginDate+(i*density*60),timeEnum.getDateFormat());
				map.put(timeString.substring(11, 16),defaultValue);
			}
		}
	}
	
	
	public void convertMinuteMap(){
		Map<String, Object> tempMap = new LinkedHashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey().toString();
			tempMap.put(key.split(" ")[1], entry.getValue());
		}
		this.map.clear();
		this.map.putAll(tempMap);
	}
}
