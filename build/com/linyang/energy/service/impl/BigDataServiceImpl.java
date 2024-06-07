package com.linyang.energy.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.leegern.util.NumberUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.webxml.ArrayOfString;
import cn.com.webxml.WeatherWS;
import cn.com.webxml.WeatherWSSoap;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.DateUtil;
import com.leegern.util.StringUtil;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.HolidayBean;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.authmanager.UserBeanMapper;
import com.linyang.energy.mapping.bigDataPredict.DayTempMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.timeManager.RestTimeMapper;
import com.linyang.energy.model.DayTempBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.LedgerRestDayBean;
import com.linyang.energy.model.LoadPredictSetBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.BigDataService;import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.OfficialHolidayUtils;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.XMLMethods;

/**
 * 大数据预测
 * @author Administrator
 *
 */

@Service
public class BigDataServiceImpl implements BigDataService {
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	@Autowired
	private DayTempMapper dayTempMapper;
	@Autowired
	private RestTimeMapper restTimeMapper;
	@Autowired
	private UserBeanMapper userBeanMapper;

	@Override
	public Map<String, Object> loadPredict(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			LoadPredictSetBean setInfo = this.getSetInfo();
			String baseDateStr = (String) param.get("baseDateStr");
			//企业ID
			Long ledgerId = (Long) param.get("ledgerId");
			Long regionId = getRegionId(ledgerId);
			//得到需要预测的时间的天气情况
			Map<String, Object> predictDayParam = new HashMap<String, Object>();
			predictDayParam.put("regionId", regionId);
			Date dailyTime = DateUtil.convertStrToDate(baseDateStr,DateUtil.DEFAULT_SHORT_PATTERN);
			predictDayParam.put("dailyTime", dailyTime);
			List<DayTempBean> predictDayList = this.dayTempMapper.getDayTempByParam(predictDayParam);
			if(predictDayList != null && predictDayList.size() > 0){
				DayTempBean predictDay = predictDayList.get(0);
				//预测的天气是否属于高温天，低温天还是常温天
				int type = 0;
				if(predictDay.getTempMax() >= Integer.parseInt(setInfo.getTempHighLine())){
					//高温天
					type = 1;
				}else if(predictDay.getTempMin() <= Integer.parseInt(setInfo.getTempLowLine())){
					//低温天
					type = 2;
				}else {
					//常温天
					type = 3;
				}
				param.put("type", type);
				param.put("tempMax", Integer.parseInt(setInfo.getTempHighLine()));
				param.put("tempMin", Integer.parseInt(setInfo.getTempLowLine()));
				param.put("regionId", regionId);
				
				Date endTime = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
				Date startTime = DateUtil.getSomeDateInYear(endTime,Integer.parseInt(setInfo.getHistoryDay()) * -1);
				param.put("endTime", endTime);
				param.put("startTime", startTime);
				/**
				 * 得到符合条件的温度天数
				 */
				List<DayTempBean> dayTempList = this.dayTempMapper.getDayTempByParam(param);
				
				//有温度的历史数据
				if(dayTempList != null && dayTempList.size() > 0){
					/**
					 * 查看是否符合工作日或者非工作日
					 */
					List<DayTempBean> newDayTempList = filterWorkdayOrHoilday(ledgerId, dailyTime, dayTempList);
					if(newDayTempList == null){
						//没有设置工作日和非工作日
						result.put("unSetWorkDay", true);
					}else if(newDayTempList.size() == 0){
						//没有对应的工作日或者非工作日
						result.put("noWorkDay", true);
					}else if (newDayTempList.size() > 0){
						//生成图标模板
						Date beginDate = DateUtil.convertStrToDate(baseDateStr + " 00:00:00");
						Date endDate = DateUtil.convertStrToDate(baseDateStr + " 23:59:59");
						ChartItemWithTime chartItem = new ChartItemWithTime(TimeEnum.DAYMINUTE, "", beginDate.getTime()/1000, endDate.getTime()/1000, 0);
						SortedMap<String, Object> chartMap = chartItem.getMap();
						
						//一共的有效天数
						int totalDay = 0;
						
						for (DayTempBean dayTempBean : newDayTempList) {
							Date date = dayTempBean.getDailyTime();
							Map<String, Object> queryParam = new HashMap<String, Object>();
							queryParam.put("ledgerId", ledgerId);
							queryParam.put("beginTime", date);
							queryParam.put("endTime", DateUtil.getSomeDateInYear(date, 1));
							//查看每一天的数据，根据值的大小倒序排列
							List<Map<String, Object>> apList = this.dayTempMapper.queryLedgerAP(queryParam);
							if(apList != null && apList.size() > 0){
								totalDay++;
								double maxValue = 0;
								int i=0;
								for (Map<String, Object> map : apList) {
									Date freezeTime = (Date) map.get("FREEZE_TIME");
									String freezeStr = DateUtil.convertDateToStr(freezeTime, "HH:mm");
									BigDecimal apValue = (BigDecimal)map.get("AP");
									if(i == 0){//最大值
										maxValue = apValue.doubleValue();
										Integer count = (Integer) chartMap.get(freezeStr);
										chartMap.put(freezeStr, count+1);
									}else {//判断有几个最大值
										if(apValue.compareTo(new BigDecimal(maxValue)) == 0){
											Integer count = (Integer) chartMap.get(freezeStr);
											chartMap.put(freezeStr, count+1);
										}else {
											break;//如果不同了，表示没有最大值了，跳出循环
										}
									}
									i++;
								}
							}
							//一共有多少有效天数
							result.put("totalDay", totalDay);
						}
						//有历史数据
						if(totalDay > 0){
							//map转化为list
							List<Map<String, Object>> list = mapToList(chartMap, Integer.parseInt(setInfo.getPointNum()));
							result.put("list", list);
							//判断发生概率最大的区间
							List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>();
							newList.addAll(list);
							String maxRegion = searchMaxRegion(newList, totalDay,Integer.parseInt(setInfo.getProportion()),Integer.parseInt(setInfo.getPointNum()));
							result.put("maxRegion", maxRegion);
						}else {
							//没有历史数据
							result.put("noDataHistory", true);
						}
						
					}
				}else {
					//没有温度的历史数据
					result.put("noTempHistory", true);
				}
			}else {
				//没有预测当天的温度数据
				result.put("noTempCurrent", true);
			}
		} catch (ParseException e) {
			Log.error(this.getClass() + ".loadPredict()--解析出错");
		}
		
		return result;
	}

	/**
	 * 判断发生概率最大的区间
	 * @param list
	 * @return
	 */
	private String searchMaxRegion(List<Map<String, Object>> list, int totalDay, int setProportion, int pointNum) {
		//查找概率最大的时间。
		int max = 0;
		String timeStr = "";
		int index = 0;
		int i = 0;
		for (Map<String, Object> map : list) {
			if(map.get("proportion") != null){
				int proportion = (Integer)map.get("proportion");
				if(max < proportion){
					max = proportion;
					timeStr = (String) map.get("time");
					index = i;
				}
			}
			i++;
		}
		//占比是否大于占比线,大于的话只去一个值，小于的话取两个值
		if(new BigDecimal(max).divide(new BigDecimal(totalDay), 2, BigDecimal.ROUND_HALF_UP).compareTo(new BigDecimal(setProportion).multiply(new BigDecimal(0.01))) >= 0){
			return "最大负荷时间区间为：<span style='color:#ff7800' >"+ getBeforeTime(timeStr, pointNum)+"~" + timeStr + "</span>,合计概率为:" + DataUtil.doubleDivide(max, totalDay, 2).floatValue();
		}else {
			//没有超过，再取第二大的点
			
			//把之前取出来的区间数据先删除掉
			int maxSpace = index+pointNum;
			if(maxSpace > list.size()){
				maxSpace = list.size();
			}
			for(int k = maxSpace; k>index-pointNum; k--){
				list.remove(k);
			}
			
			int max2 = 0;
			String timeStr2 = "";
			for (Map<String, Object> map : list) {
				if(map.get("proportion") != null){
					int proportion = (Integer)map.get("proportion");
					if(max2 < proportion){
						max2 = proportion;
						timeStr2 = (String) map.get("time");
					}
				}
			}
			return "最大负荷时间区间为：<span style='color:#ff7800' >"+getBeforeTime(timeStr,pointNum)+"~" + timeStr +  "</span>和<span style='color:#ff7800' >" 
			+ getBeforeTime(timeStr2,pointNum) + "~" + timeStr2 + "</span>,合计概率为:" + DataUtil.doubleDivide(max + max2, totalDay, 2).floatValue();
		}
	}

	/**
	 * 得到该时间之前的时间
	 * @param timeStr
	 * @return
	 */
	private String getBeforeTime(String timeStr, int pointNum) {
		if(StringUtils.isNotEmpty(timeStr)){
			String[] timeAry = timeStr.split(":");
			int spaceHour = pointNum/4;
			int spaceMinutes = pointNum%4;
			Integer hour = Integer.parseInt(timeAry[0]) - spaceHour;
			Integer minutes = Integer.parseInt(timeAry[1]) - spaceMinutes*15;
			if(minutes < 0){
				hour = hour-1;
				minutes = Math.abs(minutes);
			}
			String hourStr = hour.toString();
			String minutesStr = minutes.toString();
			if(hour < 10){
				hourStr = "0" + hour;
			}
			if(minutes < 10){
				minutesStr = "0" + minutes;
			}
			return hourStr + ":" + minutesStr;
		}
		return "";
	}

	/**
	 * 查看是否符合工作日或者非工作日
	 * @param ledgerId
	 * @param dailyTime
	 * @param dayTempList
	 * @return
	 */
	private List<DayTempBean> filterWorkdayOrHoilday(Long ledgerId,
			Date dailyTime, List<DayTempBean> dayTempList) {
		List<DayTempBean>  newDayTempList = new ArrayList<DayTempBean>();
		//判断预测的天数是工作日还是非工作日
		LedgerRestDayBean restDayBean = restTimeMapper.getLedgerRestDayByLedgerId(ledgerId);
		//节假日信息
		List<HolidayBean> holidayList = null;
		
		if (restDayBean != null) {
			if(restDayBean.getIncludeDefaultHoliday() == 1){
				//默认是法定节假日
				holidayList = OfficialHolidayUtils.readHisOfficialHolidayList();
			}else if(restDayBean.getIncludeCustomHoliday() == 1){
				//自定义节假日
				holidayList = restTimeMapper.getLedgerHolidaySettingByLedgerId(ledgerId);
			}
			
			int isWeekend = isWeekend(dailyTime, restDayBean, holidayList);
			for (DayTempBean dayTempBean : dayTempList) {
				//和预测日期的类型相同，要么都是工作日，要么都是非工作日
				if(isWeekend(dayTempBean.getDailyTime(), restDayBean, holidayList) == isWeekend){
					newDayTempList.add(dayTempBean);
				}
			}
		}else {
			//没有设置工作日和非工作日,返回Null
			return null;
		}
		
		return newDayTempList;
	}

	private int isWeekend(Date date, LedgerRestDayBean restDayBean, List<HolidayBean> holidayList) {
		//得到预测日是星期几
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);	
		
		int isWeekend = 0;
		switch (dayOfWeek) {
			case 1:
				isWeekend = restDayBean.getSunday();
				break;
			case 2:
				isWeekend = restDayBean.getMonday();
				break;
			case 3:
				isWeekend = restDayBean.getTuesday();
				break;
			case 4:
				isWeekend = restDayBean.getWednesday();
				break;
			case 5:
				isWeekend = restDayBean.getThursday();
				break;
			case 6:
				isWeekend = restDayBean.getFriday();
				break;
			case 7:
				isWeekend = restDayBean.getSaturday();
				break;
			default:
				break;
		}
		if(isWeekend == 0){
			//工作日
			for (HolidayBean holidayBean : holidayList) {
				Long begin = holidayBean.getFromDate().getTime();
				Long end = holidayBean.getEndDate().getTime();
				Long day = date.getTime();
				if(day >= begin && day <= end){
					//在这个范围内，是节假日
					isWeekend = 1;
					break;
				}
			}
		}

		
		return isWeekend;
	}

	/**
	 * Map转换为list
	 * @param chartMap
	 * @return
	 */
	private List<Map<String, Object>> mapToList(
			SortedMap<String, Object> chartMap,int pointNum) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Iterator<Entry<String, Object>> it = chartMap.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			i++;
			Entry<String, Object> entry = it.next();
			Map<String, Object> entryMap = new HashMap<String, Object>();
			entryMap.put("time", entry.getKey());
			entryMap.put("count",entry.getValue());
			entryMap.put("num", i);//第几个点
			//判断连续多少个点
			if(i > pointNum){
				int proportion = 0;
				for (int j = list.size()-1; j > list.size()-pointNum; j--) {
					proportion = (Integer)list.get(j).get("count") + proportion;
				}
				entryMap.put("proportion", proportion);
			}
			list.add(entryMap);
		}
		return list;
	}

	@Override
	public void insertWeatherToDB() {
		try {
			//得到需要查询的区域代码
			List<String> regionList = this.dayTempMapper.getRegionList();
			if(regionList != null && regionList.size() > 0){
				//连接ws服务
				WeatherWS wws = new WeatherWS();
		        WeatherWSSoap wwsp = wws.getWeatherWSSoap();  
				for (String region : regionList) {

					//根据区域代码得到区域名称
					String cityName = this.dayTempMapper.getNameByRegion(region);
					if(cityName.contains("市")){
						cityName = cityName.replace("市", "");
					}else if(cityName.contains("县")){
						cityName = cityName.replace("县", "");
					}
					ArrayOfString aos = wwsp.getWeather(cityName, WebConstant.weatherUserId);
					if(aos != null){
			        	List<String> info = aos.getString();
			        	if(info != null && info.size() != 1){
			        		Date date1 = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
							String day1 = info.get(8);
							insertDayTempInfo(region, date1, day1);
							
							String day2 = info.get(13);
							Date date2 = DateUtil.getSomeDateInYear(date1, 1);
							insertDayTempInfo(region, date2, day2);
							
							String day3 = info.get(18);
							Date date3 = DateUtil.getSomeDateInYear(date2, 1);
							insertDayTempInfo(region, date3, day3);
							
							String day4 = info.get(23);
							Date date4 = DateUtil.getSomeDateInYear(date3, 1); 
							insertDayTempInfo(region, date4, day4);
							
							String day5 = info.get(28);
							Date date5 = DateUtil.getSomeDateInYear(date4, 1);
							insertDayTempInfo(region, date5, day5);
			        	}else{
							Log.info(cityName+"====该区域没有天气预报信息");
						}
			        }
					//连续调用查询天气函数时，返回数据有问题，需要时间间隔
					Thread.sleep(1000 * 5);
				}
			}
		} catch (InterruptedException e) {
			Log.info("insertWeatherToDB error InterruptedException");
		}
	}
	

	/**
	 * 把数据存入数据库
	 * @param region
	 * @param date
	 * @param day
	 */
	private void insertDayTempInfo(String region, Date date, String day) {
		if(StringUtil.isNotEmpty(day)){
			//格式为：26℃/32℃
			String[] tempAry = day.split("/");
			//高温
			String lowTemp = tempAry[0].replace("℃", "");
			//低温
			String highTemp = tempAry[1].replace("℃", "");
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			//得到星期几
			int week = cal.get(Calendar.DAY_OF_WEEK);
			
			DayTempBean bean = new DayTempBean();
			bean.setDailyTime(date);
			bean.setRegionId(region);
			bean.setTempMax(Integer.parseInt(highTemp));
			bean.setTempMin(Integer.parseInt(lowTemp));
			bean.setWeek(week);
			
			this.dayTempMapper.addDayTempInfo(bean);
		}
	}

	@Override
	public LoadPredictSetBean getSetInfo() {
		LoadPredictSetBean bean = new LoadPredictSetBean();
		try {
			 Document dom = XMLMethods.getDocument("com/linyang/energy/xml/loadPredictSet.xml");
			 List<Element> list = XMLMethods.getElement(dom, "loadPredict");
			 if(list != null && list.size() > 0){
				 Element element = list.get(0);
				 bean.setHistoryDay(element.attributeValue("historyDay"));
				 bean.setTempHighLine(element.attributeValue("tempHighLine"));
				 bean.setTempLowLine(element.attributeValue("tempLowLine"));
				 bean.setProportion(element.attributeValue("proportion"));
				 bean.setPointNum(element.attributeValue("pointNum"));
			 }
		} catch (DocumentException e) {
			Log.info("getSetInfo error DocumentException");
		}
		return bean;
	}

	@Override
	public boolean saveSetInfo(LoadPredictSetBean bean) {
		boolean flag = false;
		try {
			String filePath = "com/linyang/energy/xml/loadPredictSet.xml";
			Document dom = XMLMethods.getDocument(filePath);
			List<Element> list = XMLMethods.getElement(dom, "loadPredict");
			if(list != null && list.size() > 0){
				Element element = list.get(0);
				element.addAttribute("historyDay", bean.getHistoryDay());
				element.addAttribute("tempHighLine", bean.getTempHighLine());
				element.addAttribute("tempLowLine", bean.getTempLowLine());
				element.addAttribute("proportion", bean.getProportion());
				element.addAttribute("pointNum", bean.getPointNum());
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			File file = new File(XMLMethods.loadPath(filePath));
			XMLWriter writer = new XMLWriter(new FileWriter(file), format);
			writer.write(dom);
			writer.close();
			flag = true;
		} catch (IOException e) {
			flag = false;
			Log.info("saveSetInfo error IOException");
		}
		catch (DocumentException e) {
			flag = false;
			Log.info("saveSetInfo error DocumentException");
		}
		return flag;
	}

	/**
	 * 日期类型：1-工作日
	 */
	private final static int WORKING_DAY_TYPE = 1;
	/**
	 * 日期类型：2-休息日
	 */
	private final static int NONWORKING_DAY_TYPE = 2;
	
	/**
	 * 数据类型：电量
	 */
	private final static int DATA_TYPE_Q = 1;
	/**
	 * 数据类型：负荷（电量）
	 */
//	private final static int DATA_TYPE_AP = 2;
	/**
	 * 日神经元
	 */
	private final static int NEURON_DAY_TYPE = 1;
	/**
	 * 周神经元
	 */
	private final static int NEURON_WEEK_TYPE = 2;
	/**
	 * 月神经元
	 */
	private final static int NEURON_MONTH_TYPE = 3;
	/**
	 * 负荷预测
	 */
	@Override
	public Map<String, Object> dataPredict(Map<String, Object> param) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//数据类型：1-电量；2-功率
			int dataType = (Integer) param.get("dataType");
			//企业ID
			Long ledgerId = (Long) param.get("ledgerId");
			//前台选择的日期
			String baseDateStr = (String) param.get("baseDateStr");
			
			//预测日期
			Date predictDate = DateUtil.convertStrToDate(baseDateStr,DateUtil.DEFAULT_SHORT_PATTERN);
			//当前日期
			Date now = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
			
			//预测类型：1-预测模式；2-校验模式
			int type = 0;
			//预测时间大于当前时间。是预测模式
			if(predictDate.getTime() >= now.getTime()){
				type = 1;
			}
			//当前时间大于预测时间。是模型检验模式
			else {
				type = 2;
				//把当前日期设置为你选择的那天
				now = predictDate;
			}
			
			//判断预测日的类型：1-工作日；2-休息日
			int baseDayType = judgeDayType(predictDate, ledgerId);
			
			//神经元类型
			int neuronType = 0;

			//预测数据的参考原型数据
			SortedMap<String, Object> prototypeMap = new TreeMap<String, Object>();
			//预测数据的参考原型日期
			Date prototypeDate = new Date();
			
			//从缓存中读取神经元类型
			Map<String, Object> map = dataCacheMap.get(ledgerId);
			if(map != null ){
				neuronType = (Integer)map.get("neuronType");
				prototypeDate = getReferenceDate(ledgerId, predictDate, now, baseDayType, neuronType, dataType);
				prototypeMap = getBaseDataByDate(neuronType, prototypeDate, ledgerId, dataType);
				
			}
			//缓存中没有数据，重新计算神经元数据
			else {
				//昨天预测数据，与昨天预测日期类型相同
				Date yesterday = DateUtil.getSomeDateInYear(now, -1);
				yesterday = getSameDateTypeDay(yesterday,ledgerId,baseDayType,dataType);
				
				//前天预测数据，与昨天预测日期类型相同
				Date before = DateUtil.getSomeDateInYear(yesterday, -1);
				before = getSameDateTypeDay(before,ledgerId,baseDayType,dataType);
				
				//昨天数据的上周同期
				Date yesterdayWeek = DateUtil.getSomeDateInYear(yesterday,-7);
				yesterdayWeek = getSameDateTypeDay(yesterdayWeek,ledgerId,baseDayType,dataType);
				
				//预测日期的上周同期
				Date lastWeek = DateUtil.getSomeDateInYear(predictDate,-7);
				lastWeek = getSameDateTypeDay(lastWeek,ledgerId,baseDayType,dataType);
				
				//昨天数据的上月同期
				Date yesterdayMonth = DateUtil.getLastMonthDate(yesterday);
				yesterdayMonth = getSameDateTypeDay(yesterdayMonth,ledgerId,baseDayType,dataType);
				
				//预测日期的上月同期
				Date lastMonth = DateUtil.getLastMonthDate(predictDate);
				lastMonth = getSameDateTypeDay(lastMonth,ledgerId,baseDayType,dataType);
				
				//取各个类型的数据：
				//昨日数据：
				SortedMap<String, Object> yesterdayData = getDataByDate(yesterday, ledgerId, dataType);
				//前天数据
				SortedMap<String, Object> beforeData = getDataByDate(before, ledgerId, dataType);
				//上周数据
				SortedMap<String, Object> lastWeekData = getDataByDate(yesterdayWeek, ledgerId, dataType);
				//上月数据
				SortedMap<String, Object> lastMonthData = getDataByDate(yesterdayMonth, ledgerId, dataType);
				
				//比较个神经元的数据，得到神经元类型
				neuronType = compareNeuronType(yesterdayData, beforeData, lastWeekData, lastMonthData);
				
				Map<String, Object> ledgerMap = new HashMap<String, Object>();
				ledgerMap.put("neuronType", neuronType);
				//存放数据到缓存中
				dataCacheMap.put(ledgerId, ledgerMap);
				
				if(neuronType == NEURON_DAY_TYPE){
					prototypeMap = getBaseDataByDate(neuronType, before, ledgerId, dataType);
					prototypeDate = before;
				}else if(neuronType == NEURON_WEEK_TYPE){
					prototypeMap = getBaseDataByDate(neuronType, lastWeek, ledgerId, dataType);
					prototypeDate = lastWeek;
				}else if(neuronType == NEURON_MONTH_TYPE){
					prototypeMap = getBaseDataByDate(neuronType, lastMonth, ledgerId, dataType);
					prototypeDate = lastMonth;
				}
			}

            //计算 乐观曲线、悲观曲线、最可能曲线
            List<String> timeList = new ArrayList<String>();
            List<Double> middleCurve = new ArrayList<Double>();
            for (Map.Entry<String, Object> entry: prototypeMap.entrySet()) {
                timeList.add((String) entry.getKey());
                double val = entry.getValue()==null?0:(Double) entry.getValue();
                middleCurve.add(Math.rint(val));
            }
            result.put("timeList", timeList);
            result.put("middleCurve", middleCurve);
			processBestWorstCurve(result, neuronType, prototypeDate, ledgerId, dataType);


			//检测模型下获取当天真实数据
			if(type == 2){
				//模型检验模式下，获取当天数据
				SortedMap<String, Object> nowDataMap = getDataByDate(now, ledgerId, dataType);
				//当前数据
				List<Double> realCurve = new ArrayList<Double>();
				for (Map.Entry<String, Object> entry: nowDataMap.entrySet()) {
					double val = entry.getValue()==null?0:(Double) entry.getValue();
					realCurve.add(val);
				}
				result.put("realCurve", realCurve);
				//计算平均偏差率
				double deviation = calculateDeviation(middleCurve, realCurve);
				result.put("deviation", String.format("%.2f", deviation));
			}

		} catch (ParseException e1) {
			Log.error("预测日期格式有问题--无法符合预测");
			return null;
		}
		return result;
	}

	/**
	 * 得到需要模拟的日期
	 * @param ledgerId
	 * @param predictDate	预测日期
	 * @param now			当前日期
	 * @param baseDayType	日期类型：工作日，非工作日
	 * @param neuronType	神经元类型
	 * @return
	 * @throws ParseException 
	 */
	private Date getReferenceDate(Long ledgerId, Date predictDate, Date now,
			int baseDayType, int neuronType, int dataType) throws ParseException {
		Date referenceDate = new Date();
		if(neuronType == NEURON_DAY_TYPE){
			referenceDate = DateUtil.getSomeDateInYear(now, -2);
			referenceDate = getSameDateTypeDay(referenceDate,ledgerId,baseDayType,dataType);
		}else if(neuronType == NEURON_WEEK_TYPE){
			referenceDate = DateUtil.getSomeDateInYear(predictDate,-7);
			referenceDate = getSameDateTypeDay(referenceDate,ledgerId,baseDayType,dataType);
		}else if(neuronType == NEURON_MONTH_TYPE){
			referenceDate = DateUtil.getLastMonthDate(predictDate);
			referenceDate = getSameDateTypeDay(referenceDate,ledgerId,baseDayType,dataType);
		}
		return referenceDate;
	}

	/**
	 * 比较个神经元的数据，得到神经元类型
	 * @param yesterdayData
	 * @param beforeData
	 * @param lastWeekData
	 * @param lastMonthData
	 * @return
	 */
	private int compareNeuronType( SortedMap<String, Object> yesterdayData,
			SortedMap<String, Object> beforeData,
			SortedMap<String, Object> lastWeekData,
			SortedMap<String, Object> lastMonthData) {
		List<Double> beforeList = new ArrayList<Double>();
		List<Double> lastWeekList = new ArrayList<Double>();
		List<Double> lastMonthList = new ArrayList<Double>();
		//数据比较
		Iterator<Entry<String, Object>> it = yesterdayData.entrySet().iterator();
		
		while(it.hasNext()){
			Entry<String, Object> entry = it.next();
			String freezeStr = (String) entry.getKey();
			Double yesterdayVal = yesterdayData.get(freezeStr)==null?0:(Double) yesterdayData.get(freezeStr);
			Double beforeVal = beforeData.get(freezeStr)==null?0:(Double) beforeData.get(freezeStr);
			Double lastWeekVal = lastWeekData.get(freezeStr)==null?0:(Double) lastWeekData.get(freezeStr);
			Double lastMonthVal = lastMonthData.get(freezeStr)==null?0:(Double) lastMonthData.get(freezeStr);
			//该点有数据
			if(yesterdayVal != 0){
				if(beforeVal != 0){
					beforeList.add(BigDecimal.ONE.subtract(new BigDecimal(yesterdayVal).divide(new BigDecimal(beforeVal), 2, BigDecimal.ROUND_HALF_UP)).abs().doubleValue());
				}
				if(lastWeekVal != 0){
					lastWeekList.add(BigDecimal.ONE.subtract(new BigDecimal(yesterdayVal).divide(new BigDecimal(lastWeekVal), 2, BigDecimal.ROUND_HALF_UP)).abs().doubleValue());
				}
				if(lastMonthVal != 0){
					lastMonthList.add(BigDecimal.ONE.subtract(new BigDecimal(yesterdayVal).divide(new BigDecimal(lastMonthVal), 2, BigDecimal.ROUND_HALF_UP)).abs().doubleValue());
				}
			}
			
		}
		
		//前日平均误差百分比
		double beforeRate = getListAvg(beforeList);
		if(new BigDecimal(beforeRate).compareTo(BigDecimal.ZERO) == 0){
			beforeRate = 100;
		}
		//上周同期平均误差百分比
		double lastWeekRate = getListAvg(lastWeekList);
		if(new BigDecimal(lastWeekRate).compareTo(BigDecimal.ZERO) == 0){
			lastWeekRate = 100;
		}
		//上月同期平均误差百分比
		double lastMonthRate = getListAvg(lastMonthList);
		if(new BigDecimal(lastMonthRate).compareTo(BigDecimal.ZERO) == 0){
			lastMonthRate = 100;
		}
		
		Double[] array = {beforeRate,lastWeekRate,lastMonthRate};
		//数组排序，误差最小的排在最前面
		Arrays.sort(array);
		
		int neuronType = 0;
		//误差最小的是
		if(array[0].equals(beforeRate)){
			//日神经元
			neuronType = NEURON_DAY_TYPE;
		}else if(array[0].equals(lastWeekRate)){
			//周神经元
			neuronType = NEURON_WEEK_TYPE;
		}else if(array[0].equals(lastMonthRate)){
			//月神经元
			neuronType = NEURON_MONTH_TYPE;
		}
		return neuronType;
	}

	/**
	 * 得到和选择日期相同类型的最近一天
	 * @param yesterday
	 * @param ledgerId
	 * @param baseDayType
	 * @param dataType 数据类型
	 * @return
	 * @throws ParseException 
	 */
	private Date getSameDateTypeDay(Date date, Long ledgerId,
			int baseDayType, int dataType) throws ParseException {
		int i =0;
		while(true){
			//日期类型
			int beforeDayType = judgeDayType(date, ledgerId);
			//日期类型相同
			if(baseDayType == beforeDayType){
				//判断是否有数据，如果没有数据，舍弃
				List<Map<String, Object>> list = getLedgerData(ledgerId, dataType, date, DateUtil.getSomeDateInYear(date, 1));
				if(list != null && list.size() > 0){
					break;
				}
			}
			//往前推天数
			date =  DateUtil.getSomeDateInYear(date, -1);
			//防止死循环
			i++;
			if(i>100){
				break;
			}
			
		}
		return date;
	}

	/**
	 * 计算平均偏差率
	 * @param middleCurve 预测曲线
	 * @param realCurve	真实曲线
	 * @return
	 */
	private double calculateDeviation(List<Double> middleCurve,
			List<Double> realCurve) {
		double deviation = 0;
		int j = 0;
		double sum = 0;
		for(int i = 0; i < middleCurve.size();i++){
			double val1 = middleCurve.get(i);
			double val2 = realCurve.get(i);
			if(val1 != 0 && val2 != 0){
				j++;
				sum = new BigDecimal(val2).subtract(new BigDecimal(val1)).divide(new BigDecimal(val2), 2, BigDecimal.ROUND_HALF_DOWN).abs().add(new BigDecimal(sum)).doubleValue();
			}
		}
		if(j != 0){
			deviation = DataUtil.doubleDivide(sum, j, 2);
		}
		return deviation;
	}

	/*** 计算乐观悲观的加权系数* @param ledgerId
	 * @param yesterday
	 * @param before
	 * @param lastWeek
	 * @param lastMonth
	 * @param neuronType
	 * @return
	 * @throws ParseException
	 * 
	 */
//	@SuppressWarnings("unused")
//	private double[] caculateWeightRatio(Long ledgerId, Date sampleDate, int dataType)
//			throws ParseException {
//		double[] ratioArray = {0,0};
//		
//		Date beginTime = sampleDate;
//		Date endTime = DateUtil.getSomeDateInYear(sampleDate, 1);
//		
//		List<Map<String, Object>> list = getLedgerData(ledgerId, dataType, beginTime, endTime);
//		
//		if(list != null && list.size() > 0){
//			List<Double> dateSection = new ArrayList<Double>();
//			for (Map<String, Object> map : list) {
//				BigDecimal apValue = (BigDecimal)map.get("AP");
//				dateSection.add(apValue.doubleValue());
//			}
//			int size = list.size();
//			int pecent5 = (int) (list.size() * 0.05);
//			//去掉最高的5%和最低的5%
//			dateSection = dateSection.subList(pecent5, size-pecent5);
//			
//			int pecent20 = (int) (dateSection.size() * 0.2);
//			int pecent50 = (int) (dateSection.size() * 0.5);
//			//乐观值，最高的20%；
//			List<Double> bestSection = dateSection.subList(0, pecent20);
//			Double bestVal = getListAvg(bestSection);
//			//悲观值，最低的20%；
//			List<Double> worstSection = dateSection.subList(dateSection.size()-pecent20, dateSection.size());
//			Double worstVal = getListAvg(worstSection);
//			//最可能值：中间数50%数据
//			List<Double> middleSection = dateSection.subList((pecent50-pecent50/2), (pecent50+pecent50/2));
//			Double middleVal = getListAvg(middleSection);
//			
//			//乐观系数
//			Double bestRatio = 1.2;
//			if(middleVal != 0){
//				bestRatio = 1 + (bestVal - middleVal)/middleVal;
//			}
//			
//			//乐观系数最大值不超过1.2
//			if(bestRatio > 1.2){
//				bestRatio = 1.2;
//			}
//			ratioArray[0] = bestRatio;
//			//悲观系数
//			Double worstRatio = 0.8;
//			if(middleVal != 0){
//				worstRatio = 1 + (worstVal - middleVal)/middleVal;
//			}
//			if(worstRatio < 0.8){
//				worstRatio = 0.8;
//			}
//			ratioArray[1] = worstRatio;
//		}
//		return ratioArray;
//	}
	
	/**
	 * 得到集合的平均值
	 * @param bestSection
	 * @return
	 */
	private Double getListAvg(List<Double> list) {
		double sum = 0;
		if(list !=null && list.size() > 0){
			for (Double val : list) {
				sum = DataUtil.doubleAdd(sum, val);
			}
			return DataUtil.doubleDivide(sum, list.size(), 2);
		}
		return 0d;
	}

	/**
	 * 计算温度系数
	 * @param ledgerId
	 * @param predictDate
	 * @param sampleList
	 * @param dataSourceDate 
	 * @return
	 */
	private double[] caculateTempRatio(Long ledgerId, Date predictDate,
			List<Date> sampleList, int dataType, Date dataSourceDate) {
		double tempRatioAry[] = {2,1};
		//得到企业区域编号
		Long regionId = getRegionId(ledgerId);
		//得到需要预测的时间的天气情况
		LoadPredictSetBean setInfo = this.getSetInfo();
		//得到数据源的值
		double dataSource = sumLedgerData(ledgerId, dataSourceDate,dataType);
		
		//得到某一天的温度类型 1-高温；2-低温；3-常温
		int predictTempType = getTempType(predictDate, regionId, setInfo);
		List<Double> normalList = new ArrayList<Double>();
		List<Double> unnormalList = new ArrayList<Double>();
		for (Date date : sampleList) {
			double val = sumLedgerData(ledgerId, date,dataType);
			if(val > 0){
				int dateTempType = getTempType(date, regionId, setInfo);
				if(dateTempType == 1 || dateTempType == 2){
					unnormalList.add(val);
				}else if(dateTempType == 3){
					normalList.add(val);
				}
			}
		}
		
		//全部是高温，低温，或者常温
		if( (predictTempType == 1 || predictTempType == 2)&& unnormalList.size() == sampleList.size() 
				|| predictTempType == 3 && normalList.size() == sampleList.size()){
			 tempRatioAry[1] = 1;
		}else if(unnormalList.size() > 0 && normalList.size() > 0){
			//计算样本区数据高低温负荷加权平均值：
			//P1=高低温负荷加权平均值-正常温度负荷加权平均值；
			//P2=高低温负荷加权平均值/正常温度负荷加权平均值
			double p1 = DataUtil.doubleSubtract(getListAvg(unnormalList), getListAvg(normalList));
			double p2 = DataUtil.doubleDivide(getListAvg(normalList), getListAvg(unnormalList), 2);
			
			//相似度判断：
			//叠加模型相似度=（高低温负荷-正常温度负荷加权平均值）/P1。
			//比例模型相似度=（高低温负荷*P2）/正常温度负荷加权平均值。
			//叠加模型
			double r1 = DataUtil.doubleDivide(DataUtil.doubleSubtract(dataSource, getListAvg(normalList)), p1, 2);
			//比例模型
			double r2 = DataUtil.doubleDivide(DataUtil.doubleMultiply(dataSource, p2), getListAvg(normalList), 2);
			//那么高低温切换就以相似度最高的模型±P1或乘以P2
			if(BigDecimal.ONE.subtract(new BigDecimal(r1)).abs().compareTo(BigDecimal.ONE.subtract(new BigDecimal(r2))) < 0){
				tempRatioAry[0] = 1;
				tempRatioAry[1] = p1;
			}else {
				tempRatioAry[0] = 2;
				tempRatioAry[1] = p2;
			}
		}
		
		return tempRatioAry;
	}

	/**
	 * 得到某一天的温度类型 1-高温；2-低温；3-常温
	 * @param date
	 * @param regionId
	 * @param setInfo
	 * @return
	 */
	private int getTempType(Date date, Long regionId,
			LoadPredictSetBean setInfo) {
		int type = 3;
		Map<String, Object> predictDayParam = new HashMap<String, Object>();
		predictDayParam.put("regionId", regionId);
		predictDayParam.put("dailyTime", date);
		List<DayTempBean> predictDayList = this.dayTempMapper.getDayTempByParam(predictDayParam);
		if(predictDayList != null && predictDayList.size() > 0){
			DayTempBean predictDay = predictDayList.get(0);
			//预测的天气是否属于高温天，低温天还是常温天
			if(predictDay.getTempMax() >= Integer.parseInt(setInfo.getTempHighLine())){
				//高温天
				type = 1;
			}else if(predictDay.getTempMin() <= Integer.parseInt(setInfo.getTempLowLine())){
				//低温天
				type = 2;
			}else {
				//常温天
				type = 3;
			}
		}
		return type;
	}

	/**
	 * 得到区域ID
	 * @param ledgerId
	 * @return
	 */
	private Long getRegionId(Long ledgerId) {
		LedgerBean ledgerBean = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
		String region = ledgerBean.getRegion();
		Long regionId = 0L;
		if(region != null){
			regionId = Long.parseLong(region);
		}
		return regionId;
	}

	/**
	 * 计算调整因子
	 * @param ledgerId
	 * @param yesterday
	 * @param sampleList
	 * @throws ParseException
	 */
	private double caculateFactor(Long ledgerId, Date dataSourceDate,
			List<Date> sampleList, int dataType) throws ParseException {
		
		//昨日数据
		double dataSourceData = sumLedgerData(ledgerId, dataSourceDate,dataType);
		//10个工作日数据，或者4个非工作日数据
		double last10Data = sumLedgerData2(ledgerId, sampleList,dataType);
		if(new BigDecimal(last10Data).compareTo(BigDecimal.ZERO) == 0 || new BigDecimal(dataSourceData).compareTo(BigDecimal.ZERO) == 0){
			return 1;
		}
		return DataUtil.doubleDivide(dataSourceData, last10Data);
		
	}

	/**
	 * 得到样本区间集合，工作日的话需要10个，非工作日的话是4个
	 * @param now
	 * @param ledgerId
	 * @param baseDayType
	 * @return
	 * @throws ParseException 
	 */
	private List<Date> getSampleList(Date now, Long ledgerId, int baseDayType, int dataType) throws ParseException {
		List<Date> sampleList = new ArrayList<Date>();
		
		//从昨天开始
		Date itemDay = DateUtil.getSomeDateInYear(now,  -1);
		
		int i =0;
		while(true){
			int itemDayType = judgeDayType(itemDay, ledgerId);
			//判断是否有数据：
			//判断是否有数据，如果没有数据，舍弃
			List<Map<String, Object>> list = getLedgerData(ledgerId, dataType, itemDay, DateUtil.getSomeDateInYear(itemDay, 1) );
			//这一天有数据且日期类型相同
			if(list != null && list.size() > 0 && baseDayType == itemDayType){
				sampleList.add(itemDay);
				//工作日，10个样本
				if(sampleList.size() == 10 && baseDayType == WORKING_DAY_TYPE){
					//区间满足，跳出循环
					break;
				}
				//非工作日，4个样本
				else if(sampleList.size() == 4 && baseDayType == NONWORKING_DAY_TYPE){
					//区间满足，跳出循环
					break;
				}
			}
			//往前推一天
			itemDay =  DateUtil.getSomeDateInYear(itemDay, -1);
			//防止死循环
			i++;
			if(i>100){
				break;
			}
		}
		return sampleList;
	}

	/**
	 * 判断日期类型：1-工作日；2-休息日
	 * @param date
	 * @param ledgerId
	 * @return
	 */
	public int judgeDayType(Date date, Long ledgerId) {
		int type = WORKING_DAY_TYPE;
		
		//获取用户休息日设置
		LedgerRestDayBean restDayBean = restTimeMapper.getLedgerRestDayByLedgerId(ledgerId);
		
		//判断该日期是星期几
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		//设置了厂休日
		if(restDayBean != null){
			if( (restDayBean.getSunday() == 1 && dayOfWeek == 1) 
					|| (restDayBean.getMonday() == 1 && dayOfWeek == 2) 
					|| (restDayBean.getTuesday() == 1 && dayOfWeek == 3) 
					|| (restDayBean.getWednesday() == 1 && dayOfWeek == 4) 
					|| (restDayBean.getThursday() == 1 && dayOfWeek == 5) 
					|| (restDayBean.getFriday() == 1 && dayOfWeek == 6) 
					|| (restDayBean.getSaturday() == 1 && dayOfWeek == 7) ){
				//当天为休息日
				type = NONWORKING_DAY_TYPE;
			}
			else {
				List<HolidayBean> list = new ArrayList<HolidayBean>();
				//默认法定节假日
				if(restDayBean.getIncludeDefaultHoliday() == 1){
					list = OfficialHolidayUtils.readOfficialHolidayList();
				}
				//自定义节假日
				else if(restDayBean.getIncludeCustomHoliday() == 1){
					list = this.restTimeMapper.getLedgerHolidaySettingByLedgerId(ledgerId);
				}
				
				//设置了节假日
				if(list != null && list.size() > 0){
					//判断这一天是否属于休息日
					for (HolidayBean holidayBean : list) {
						Date beginDate = holidayBean.getFromDate();
						Date endDate = holidayBean.getEndDate();
						//判断时间大于开始时间，小于结束时间。说明属于这个假期之内,是节假日
						if(date.getTime() > beginDate.getTime() &&  date.getTime() < endDate.getTime() ){
							type = NONWORKING_DAY_TYPE;
							break;
						}
					}
				}
			}
		}
		//没有设置了厂休日。按国家规定来算
		else {
			//周六，周日直接设为休息日
			if(dayOfWeek == 1 || dayOfWeek == 7){
				type = NONWORKING_DAY_TYPE;
			}else {
				List<HolidayBean> list = OfficialHolidayUtils.readOfficialHolidayList();
				if(list != null && list.size() > 0){
					//判断这一天是否属于休息日
					for (HolidayBean holidayBean : list) {
						Date beginDate = holidayBean.getFromDate();
						Date endDate = holidayBean.getEndDate();
						//判断时间大于开始时间，小于结束时间。说明属于这个假期之内,是节假日
						if(date.getTime() > beginDate.getTime() &&  date.getTime() < endDate.getTime() ){
							type = NONWORKING_DAY_TYPE;
							break;
						}
					}
				}
			}
		}
		return type;
	}
	
	/**
	 * 求该分户某一区间段的总负荷
	 * @param ledgerId
	 * @param list
	 * @return
	 * @throws ParseException
	 */
	private Double sumLedgerData(Long ledgerId, Date date, int dataType) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		
			queryParam.put("ledgerId", ledgerId);
			queryParam.put("beginTime", date);
			queryParam.put("endTime", DateUtil.getSomeDateInYear(date, 1));
			Double sum = null;
			if(dataType == DATA_TYPE_Q){
				sum = this.dayTempMapper.sumLedgerQ(queryParam);
			}else {
				sum = this.dayTempMapper.sumLedgerAP(queryParam);
			}
			if(DataUtil.doubleIsNull(sum)) {
				sum = 0d;
			}
			return sum;
		
	}

	/**
	 * 求该分户某一区间段的总负荷
	 * @param ledgerId
	 * @param list
	 * @return
	 * @throws ParseException
	 */
	private Double sumLedgerData2(Long ledgerId, List<Date> list, int dataType) throws ParseException{
		double sum = 0;
		if(list !=null && list.size() > 0){
			for (Date date : list) {
				Map<String, Object> queryParam = new HashMap<String, Object>();
				queryParam.put("ledgerId", ledgerId);
				queryParam.put("beginTime", date);
				queryParam.put("endTime", DateUtil.getSomeDateInYear(date, 1));
				
				Double itemVal = null;
				switch (dataType) {
				case DATA_TYPE_Q:
					itemVal = this.dayTempMapper.sumLedgerQ(queryParam);break;
				default:
					itemVal = this.dayTempMapper.sumLedgerAP(queryParam);}
				if(itemVal != null ){
					sum = DataUtil.doubleAdd(sum, itemVal);
				}
			}
			return DataUtil.doubleDivide(sum, list.size(), 2);
		}
		return 0d;
	}

    /**
     * 计算 乐观曲线、悲观曲线
     * @param result 存放结果
     * @param neuronType 神经元类型
     * @param date1 基准时间
     * @param ledgerId 分户ID
     * @param dataType 1-电量；2-负荷
     * @return
     */
    private void processBestWorstCurve(Map<String, Object> result, int neuronType, Date date1, Long ledgerId, int dataType) throws ParseException {
        List<Double> bestCurve = new ArrayList<Double>();
        List<Double> worstCurve = new ArrayList<Double>();

        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        Date date5 = null;
        if(neuronType == NEURON_DAY_TYPE){
            date2 = DateUtil.getSomeDateInYear(date1, -1);
            date3 = DateUtil.getSomeDateInYear(date2, -1);
            date4 = DateUtil.getSomeDateInYear(date3, -1);
            date5 = DateUtil.getSomeDateInYear(date4, -1);
        }
        else if(neuronType == NEURON_WEEK_TYPE){
            date2 = DateUtil.getSomeDateInYear(date1, -7);
            date3 = DateUtil.getSomeDateInYear(date2, -7);
            date4 = DateUtil.getSomeDateInYear(date3, -7);
            date5 = DateUtil.getSomeDateInYear(date4, -7);
        }
        else if(neuronType == NEURON_MONTH_TYPE){
            date2 = DateUtil.getLastMonthDate(date1);
            date3 = DateUtil.getLastMonthDate(date2);
            date4 = DateUtil.getLastMonthDate(date3);
            date5 = DateUtil.getLastMonthDate(date4);
        }
        SortedMap<String, Object> map1 = getDataByDate(date1, ledgerId, dataType);
        SortedMap<String, Object> map2 = getDataByDate(date2, ledgerId, dataType);
        SortedMap<String, Object> map3 = getDataByDate(date3, ledgerId, dataType);
        SortedMap<String, Object> map4 = getDataByDate(date4, ledgerId, dataType);
        SortedMap<String, Object> map5 = getDataByDate(date5, ledgerId, dataType);
        Object[] keys = map1.keySet().toArray();
        for(int i = 0; i < keys.length; i++){
            String oneKey = keys[i].toString();
            Double max = 0D;
            Double min = null;
            if(map1.get(oneKey) != null){
                double one = (Double) map1.get(oneKey);
                if(DataUtil.parseDouble2BigDecimal(min) == null ){
                    max = one;
                    min = one;
                }
                else{
                    if(one > max){
                        max = one;
                    }
                    else if(one < min){
                        min = one;
                    }
                }
            }
            if(map2.get(oneKey) != null){
                double one = (Double) map2.get(oneKey);
                if(DataUtil.parseDouble2BigDecimal(min) == null ){
                    max = one;
                    min = one;
                }
                else{
                    if(one > max){
                        max = one;
                    }
                    else if(one < min){
                        min = one;
                    }
                }
            }
            if(map3.get(oneKey) != null){
                double one = (Double) map3.get(oneKey);
                if(DataUtil.parseDouble2BigDecimal(min) == null ){
                    max = one;
                    min = one;
                }
                else{
                    if(one > max){
                        max = one;
                    }
                    else if(one < min){
                        min = one;
                    }
                }
            }
            if(map4.get(oneKey) != null){
                double one = (Double) map4.get(oneKey);
                if(DataUtil.parseDouble2BigDecimal(min) == null ){
                    max = one;
                    min = one;
                }
                else{
                    if(one > max){
                        max = one;
                    }
                    else if(one < min){
                        min = one;
                    }
                }
            }
            if(map5.get(oneKey) != null){
                double one = (Double) map5.get(oneKey);
                if(DataUtil.parseDouble2BigDecimal(min) == null ){
                    max = one;
                    min = one;
                }
                else{
                    if(one > max){
                        max = one;
                    }
                    else if(one < min){
                        min = one;
                    }
                }
            }
            bestCurve.add(min!=null?min : 0);
            worstCurve.add(max);
        }

        result.put("bestCurve", bestCurve);
        result.put("worstCurve", worstCurve);
    }

    /**
     * 计算 基准数据
     * @param neuronType 神经元类型
     * @param date1 基准时间
     * @param ledgerId 分户ID
     * @param dataType 1-电量；2-负荷
     * @return
     * @throws ParseException
     */
    private SortedMap<String, Object> getBaseDataByDate(int neuronType, Date date1, Long ledgerId, int dataType) throws ParseException {
        Date date2 = null;
        Date date3 = null;
        if(neuronType == NEURON_DAY_TYPE){
            date2 = DateUtil.getSomeDateInYear(date1, -1);
            date3 = DateUtil.getSomeDateInYear(date2, -1);
        }
        else if(neuronType == NEURON_WEEK_TYPE){
            date2 = DateUtil.getSomeDateInYear(date1, -7);
            date3 = DateUtil.getSomeDateInYear(date2, -7);
        }
        else if(neuronType == NEURON_MONTH_TYPE){
            date2 = DateUtil.getLastMonthDate(date1);
            date3 = DateUtil.getLastMonthDate(date2);
        }
        SortedMap<String, Object> map1 = getDataByDate(date1, ledgerId, dataType);
        SortedMap<String, Object> map2 = getDataByDate(date2, ledgerId, dataType);
        SortedMap<String, Object> map3 = getDataByDate(date3, ledgerId, dataType);
        Object[] keys = map1.keySet().toArray();
        for(int i = 0; i < keys.length; i++){
            String oneKey = keys[i].toString();
            int flag1 = map1.get(oneKey)==null?0:1;
            int flag2 = map2.get(oneKey)==null?0:1;
            int flag3 = map3.get(oneKey)==null?0:1;
            Double average = 0D;
            if(flag1+flag2+flag3 > 0){
                BigDecimal total = BigDecimal.ZERO;
                if(flag1 == 1){
                    total = total.add(new BigDecimal((Double) map1.get(oneKey)));
                }
                if(flag2 == 1) {
                    total = total.add(new BigDecimal((Double) map2.get(oneKey)));
                }
                if(flag3 == 1){
                    total = total.add(new BigDecimal((Double) map3.get(oneKey)));
                }
                average = NumberUtil.formatDouble(total.divide(new BigDecimal(flag1+flag2+flag3), 2, BigDecimal.ROUND_DOWN).doubleValue(), NumberUtil.PATTERN_DOUBLE);
                map1.put(oneKey, average);
            }
        }
        return map1;
    }


	/**
	 * 得到某一天的负荷数据
	 * @param date
	 * @param ledgerId
	 * @param dataType 1-电量；2-负荷
	 * @return
	 * @throws ParseException
	 */
	private SortedMap<String, Object> getDataByDate(Date date, Long ledgerId, int dataType)
			throws ParseException {
		Date beginTime = date;
		Date endTime = DateUtil.getSomeDateInYear(date, 1);
		
		//得到分户点量或者负荷数据
		List<Map<String, Object>> list = getLedgerData(ledgerId, dataType, beginTime, endTime);
		
		ChartItemWithTime chartItem = new ChartItemWithTime(TimeEnum.DAYMINUTE, "", beginTime.getTime()/1000, endTime.getTime()/1000, null);
		SortedMap<String, Object> chartMap = chartItem.getMap();
		if(list != null && list.size() > 0){
			for (Map<String, Object> map : list) {
				Date freezeTime = (Date) map.get("FREEZE_TIME");
				String freezeStr = DateUtil.convertDateToStr(freezeTime, "HH:mm");
				BigDecimal apValue = (BigDecimal)map.get("AP");
				chartMap.put(freezeStr, apValue.doubleValue());
			}
		}
		return chartMap;
	}


	/**
	 * 得到分户点量或者负荷数据
	 * @param ledgerId
	 * @param dataType
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String, Object>> getLedgerData(Long ledgerId, int dataType, Date beginTime,
			Date endTime) {
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("ledgerId", ledgerId);
		queryParam.put("beginTime", beginTime);
		queryParam.put("endTime", endTime);
		//电量
		if(dataType == DATA_TYPE_Q){
			return this.dayTempMapper.queryLedgerQ(queryParam);
		}
		//负荷
		else {
			return this.dayTempMapper.queryLedgerAP(queryParam);
		}
	}

	/**
	 * 存放预测数据
	 */
	private final static ConcurrentHashMap<Long,Map<String,Object>> dataCacheMap = new ConcurrentHashMap<Long,Map<String,Object>>();
	

	
	/**
	 * 误差线
	 */
	private final static double ERROR_RATE_LINE = 0.2;
	
	
	@Override
	public void CalPredictModel() {
		try {
			//得到当前日期,同时也是预测日期
			Date now = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
			//得到所有的企业
			List<UserBean> userList = userBeanMapper.getCompanyLedgerDataByAccountId(1);
			
			//TODO:测试数据
//			List<UserBean> userList  = new ArrayList<UserBean>();
//			UserBean user = new UserBean();
//			user.setLedgerId(1431943331800L);
//			userList.add(user);
//			Map<String, Object> maps = new HashMap<String, Object>();
//			maps.put("neuronType", 2);
//			dataCacheMap.put(1431943331800L, maps);
			
			//实际误差
			double errorRate = 0d;
			for (UserBean userBean : userList) {
				Long ledgerId = userBean.getLedgerId();
				//从缓存中读取数据
				Map<String, Object> ledgerMap = dataCacheMap.get(ledgerId);
				//缓存中存在数据
				if(ledgerMap != null){
					//得到神经元类型
					int neuronType = (Integer)ledgerMap.get("neuronType");
					
					//判断预测日的类型：1-工作日；2-休息日
					int baseDayType = judgeDayType(now, ledgerId);
					
					//昨天预测数据，与昨天预测日期类型相同
					Date yesterday = DateUtil.getSomeDateInYear(now, -1);
					yesterday = getSameDateTypeDay(yesterday,ledgerId,baseDayType,DATA_TYPE_Q);
					SortedMap<String, Object> yesterdayData = getDataByDate(yesterday, ledgerId, DATA_TYPE_Q);
					
					//得到需要模拟的日期
					Date referenceDate = getReferenceDate(ledgerId, now, now, baseDayType, neuronType,DATA_TYPE_Q);
					//计算误差率
					errorRate = calErrorRate(ledgerId, yesterdayData, referenceDate, DATA_TYPE_Q);
					
					//实际误差大于误差线，需要重新计算
					if(errorRate > ERROR_RATE_LINE){
						//计算神经元类型
						neuronType = calNeuronType(now, ledgerId, DATA_TYPE_Q);
						ledgerMap.put("neuronType", neuronType);
					}
					
					//得到日预测数据
					double dayData = getPredictDayDate(now, ledgerId, neuronType);
					ledgerMap.put("dayData", Math.rint(dayData));
					
					//判断是否月初
					Date firstDayOfMonth = DateUtil.getMonthFirstDay(now);
					
					//数值相同，说明是本月第一天，统计这个月的月电量数据
					if(DateUtil.getDayOfMonth(firstDayOfMonth) == DateUtil.getDayOfMonth(now) ){
						//得到月预测数据
						double monthData = getMonthPredictData(now, ledgerId);
						ledgerMap.put("monthData", Math.rint(monthData));
					}
					
					dataCacheMap.put(ledgerId, ledgerMap);
				}
				//缓存中不存在数据，计算神经元类型
				else {
					int neuronType = calNeuronType(now, ledgerId, DATA_TYPE_Q);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("neuronType", neuronType);
					
					//得到预测日数据
					double dayData = getPredictDayDate(now, ledgerId, neuronType);
					map.put("dayData", Math.rint(dayData));
					
					//得到月预测数据
					double monthData = getMonthPredictData(now, ledgerId);
					map.put("monthData", Math.rint(monthData));
					
					dataCacheMap.put(ledgerId, map);
				}
			}
		} catch (ParseException e) {
			Log.info("CalPredictModel error ParseException");
		}
	}

	
	/**
	 * 得到月预测电量数据
	 * @param now
	 * @param predictDate
	 * @param ledgerId
	 * @param dayData
	 * @return
	 * @throws ParseException
	 */
	private double getMonthPredictData(Date now, Long ledgerId) throws ParseException {
		//计算月预测电量
		double monthData = 0d;

		//得到当前日前一天的工作日类型
		Date yesterday = DateUtil.getSomeDateInYear(now, -1);
		
		int i = 0;
		while(true){
			//判断是否有数据，如果没有数据，舍弃
			Date beginTime = yesterday;
			Date endTime = DateUtil.getSomeDateInYear(yesterday, 1);
			List<Map<String, Object>> list = getLedgerData(ledgerId, DATA_TYPE_Q, beginTime, endTime);
			if(list != null && list.size() > 0){
				break;
			}
			//往前推天数
			yesterday =  DateUtil.getSomeDateInYear(yesterday, -1);
			
			//避免死循环
			i++;
			if(i > 50){
				break;
			}
		}
		
		int dayType = judgeDayType(yesterday, ledgerId);
		
		//判断与前一天工作日类型不相同的一天
		Date before = DateUtil.getSomeDateInYear(yesterday,-1);
		for(;;){
			//判断是否有数据，如果没有数据，舍弃
			Date beginTime = before;
			Date endTime = DateUtil.getSomeDateInYear(before, 1);
			List<Map<String, Object>> list = getLedgerData(ledgerId, DATA_TYPE_Q, beginTime, endTime);
			if(list != null && list.size() > 0){
				int dayType2 = judgeDayType(before, ledgerId);
				//工作日类型不相同
				if(dayType2 != dayType){
					break;
				}
			}
			//如果相同，继续往前推
			before = DateUtil.getSomeDateInYear(before,-1);
			
			//避免死循环
			i++;
			if(i > 100){
				break;
			}
			
		}
		
		//得到当前日期的这个月的一号
		Date itemDate = DateUtil.getMonthFirstDay(now);
		
		//得到预测的是第几个月
		int predictMonth = DateUtil.getMonth(now);
		
		//下个月循环
		for(;;){
			int dayType3 = judgeDayType(itemDate, ledgerId);
			double dayData = 0d;
			
			if(dayType == dayType3){
				//与yesterday工作日类型相同
				dayData = getPredictDayDate2(itemDate, ledgerId, dayType3, yesterday);
			}else {
				//与before工作日类型相同
				dayData = getPredictDayDate2(itemDate, ledgerId, dayType3, before);
			}
			
			monthData = DataUtil.doubleAdd(monthData, dayData);
			itemDate = DateUtil.getSomeDateInYear(itemDate,1);
			//进入了下一个月，跳出循环
			if(predictMonth != DateUtil.getMonth(itemDate)){
				break;
			}
		}
		return monthData;
	}

	/**
	 * 得到日预测数据
	 * @param now
	 * @param predictDate
	 * @param ledgerId
	 * @param neuronType
	 * @return
	 * @throws ParseException
	 */
	private double getPredictDayDate(Date now, Long ledgerId,
			int neuronType) throws ParseException {
		//判断预测日的类型：1-工作日；2-休息日
		int baseDayType = judgeDayType(now, ledgerId);
		//预测参考日期
		Date referenceDate = getReferenceDate(ledgerId, now, now, baseDayType, neuronType,DATA_TYPE_Q);
		
		double dayData = getPredictDayDate2(now, ledgerId,
				baseDayType, referenceDate);
		return dayData;
	}

	/**
	 * 得到预测日数据
	 * @param now
	 * @param predictDate
	 * @param ledgerId
	 * @param baseDayType
	 * @param referenceDate
	 * @return
	 * @throws ParseException
	 */
	private double getPredictDayDate2(Date now, Long ledgerId, int baseDayType, Date referenceDate)
			throws ParseException {
		//得到模拟日的数据
		SortedMap<String, Object> referenceData = getDataByDate(referenceDate, ledgerId, DATA_TYPE_Q);
		//得到样本区间，工作日的话要10个，非工作日的话4个。
		List<Date> sampleList = getSampleList(now, ledgerId, baseDayType,DATA_TYPE_Q);
		//2-计算调整因子
		double factor = caculateFactor(ledgerId, referenceDate, sampleList,DATA_TYPE_Q);
		//3-判断高低温的影响； 1-叠加模型；2-比例模型
		double[] tempRatioAry = caculateTempRatio(ledgerId, now, sampleList,DATA_TYPE_Q, referenceDate);
		//日电量
		double dayData = 0d;
		for (Map.Entry<String, Object> entry: referenceData.entrySet()) {
			double val = entry.getValue()==null?0:(Double) entry.getValue();
			double val2 = DataUtil.doubleMultiply(val, factor);
			//叠加模型
			if(new BigDecimal(tempRatioAry[0]).compareTo(BigDecimal.ONE) == 0){
				val2 = DataUtil.doubleAdd(val2, tempRatioAry[1]);
			}
			//比例模型
			else {
				val2 = DataUtil.doubleMultiply(val2, tempRatioAry[1]);
			}
			dayData = DataUtil.doubleAdd(dayData, val2);
		}
		return dayData;
	}

	/**
	 * 计算误差值
	 * @param ledgerId
	 * @param yesterdayData	昨日
	 * @param neuronDate	根据神经元类型取得日期
	 * @return
	 * @throws ParseException
	 */
	private double calErrorRate(Long ledgerId,
			SortedMap<String, Object> yesterdayData, Date neuronDate, int dataType)
			throws ParseException {
		
		//得到某一天的负荷数据
		SortedMap<String, Object> neuronData = getDataByDate(neuronDate, ledgerId, dataType);
		
		List<Double> neuronList = new ArrayList<Double>();
		
		//昨天数据的iterator
		Iterator<Entry<String, Object>> it = yesterdayData.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Object> entry = it.next();
			String freezeStr = (String) entry.getKey();
			Double yesterdayVal = yesterdayData.get(freezeStr)==null?0:(Double) yesterdayData.get(freezeStr);
			Double neuronVal = neuronData.get(freezeStr)==null?0:(Double) neuronData.get(freezeStr);
			
			//该点有数据
			if(yesterdayVal != 0 && neuronVal != 0){
				neuronList.add(BigDecimal.ONE.subtract(new BigDecimal(yesterdayVal).divide(new BigDecimal(neuronVal), 2, BigDecimal.ROUND_HALF_DOWN)).abs().doubleValue());
			}
		}
		//得到该类型的误差值
		return getListAvg(neuronList);
	}

	/**
	 * 计算神经元类型
	 * @param now	当前日期
	 * @param predictDate	预测日期
	 * @param errorRateLine	误差线
	 * @param ledgerId	分户ID
	 * @throws ParseException
	 */
	private int calNeuronType(Date now, Long ledgerId, int dataType) throws ParseException {
		
		//判断预测日的类型：1-工作日；2-休息日
		int baseDayType = judgeDayType(now, ledgerId);
		
		//昨天预测数据，与昨天预测日期类型相同
		Date yesterday = DateUtil.getSomeDateInYear(now, -1);
		yesterday = getSameDateTypeDay(yesterday,ledgerId,baseDayType,DATA_TYPE_Q);
		
		//前天预测数据，与昨天预测日期类型相同
		Date day = DateUtil.getSomeDateInYear(yesterday, -1);
		day = getSameDateTypeDay(day,ledgerId,baseDayType,DATA_TYPE_Q);
		
		//昨日的上周同期
		Date week = DateUtil.getSomeDateInYear(yesterday,-7);
		week = getSameDateTypeDay(week,ledgerId,baseDayType,DATA_TYPE_Q);
		
		//昨日的上月同期
		Date month = DateUtil.getLastMonthDate(yesterday);
		month = getSameDateTypeDay(month,ledgerId,baseDayType,DATA_TYPE_Q);
		
		//昨日数据：
		SortedMap<String, Object> yesterdayData = getDataByDate(yesterday, ledgerId, DATA_TYPE_Q);
		//日神经元数据
		SortedMap<String, Object> dayData = getDataByDate(day, ledgerId, DATA_TYPE_Q);
		//周神经元数据
		SortedMap<String, Object> weekData = getDataByDate(week, ledgerId, DATA_TYPE_Q);
		//月神经元数据
		SortedMap<String, Object> monthData = getDataByDate(month, ledgerId, DATA_TYPE_Q);
		
		//误差集合
		List<Double> dayList = new ArrayList<Double>();
		List<Double> weekList = new ArrayList<Double>();
		List<Double> monthList = new ArrayList<Double>();
		
		//昨天数据的iterator
		Iterator<Entry<String, Object>> it = yesterdayData.entrySet().iterator();
		
		while(it.hasNext()){
			Entry<String, Object> entry = it.next();
			String freezeStr = (String) entry.getKey();
			Double yesterdayVal = yesterdayData.get(freezeStr)==null?0:(Double) yesterdayData.get(freezeStr);
			Double dayVal = dayData.get(freezeStr)==null?0:(Double) dayData.get(freezeStr);
			Double weekVal = weekData.get(freezeStr)==null?0:(Double) weekData.get(freezeStr);
			Double monthVal = monthData.get(freezeStr)==null?0:(Double) monthData.get(freezeStr);
			//该点有数据
			if(yesterdayVal != 0){
				if(dayVal != 0){
					double errorRate = BigDecimal.ONE.subtract(new BigDecimal(yesterdayVal).divide(new BigDecimal(dayVal), 2, BigDecimal.ROUND_HALF_DOWN)).abs().doubleValue();
					dayList.add(errorRate);
				}
				if(weekVal != 0){
					double errorRate = BigDecimal.ONE.subtract(new BigDecimal(yesterdayVal).divide(new BigDecimal(weekVal), 2, BigDecimal.ROUND_HALF_DOWN)).abs().doubleValue();
					weekList.add(errorRate);
				}
				if(monthVal != 0){
					double errorRate = BigDecimal.ONE.subtract(new BigDecimal(yesterdayVal).divide(new BigDecimal(monthVal), 2, BigDecimal.ROUND_HALF_DOWN)).abs().doubleValue();
					monthList.add(errorRate);
				}
			}
		}

		//日神经元的误差集合
		List<Double> dayRateList = new ArrayList<Double>();
		
		//日神经元误差百分比
		Double dayRate = getListAvg(dayList);
		
		int i = 0;
		//如果小于某个值，继续往前推
		while (dayRate < ERROR_RATE_LINE && dayRate > 0) {
			dayRateList.add(dayRate);
			//前天预测日期，与昨天预测日期类型相同
			day = DateUtil.getSomeDateInYear(day, -1);
			day = getSameDateTypeDay(day,ledgerId,baseDayType,DATA_TYPE_Q);
			
			dayRate = calErrorRate(ledgerId, yesterdayData, day, DATA_TYPE_Q);
			//防止死循环
			i++;
			if(i>50){
				break;
			}
			
		}
		
		List<Double> weekRateList = new ArrayList<Double>();
		//上周同期平均误差百分比
		Double weekRate = getListAvg(weekList);
		//如果小于某个值，继续往前推
		while (weekRate < ERROR_RATE_LINE && weekRate > 0) {
			weekRateList.add(weekRate);
			//前天预测数据，与昨天预测日期类型相同
			//预测日期的上周同期
			week = DateUtil.getSomeDateInYear(week,-7);
			week = getSameDateTypeDay(week,ledgerId,baseDayType,DATA_TYPE_Q);
			
			weekRate = calErrorRate(ledgerId, yesterdayData, week, DATA_TYPE_Q);
			//防止死循环
			i++;
			if(i>100){
				break;
			}
		}
		
		List<Double> monthRateList = new ArrayList<Double>();
		//上月同期平均误差百分比
		Double monthRate = getListAvg(monthList);
		//如果小于某个值，继续往前推
		while (monthRate < ERROR_RATE_LINE && monthRate > 0) {
			monthRateList.add(monthRate);
			month = DateUtil.getLastMonthDate(month);
			month = getSameDateTypeDay(month,ledgerId,baseDayType,DATA_TYPE_Q);
			
			monthRate = calErrorRate(ledgerId, yesterdayData, month, DATA_TYPE_Q);
			//防止死循环
			i++;
			if(i>150){
				break;
			}
		}
		
		//平均误差率
		dayRate =  getListAvg(dayRateList);
		weekRate = getListAvg(weekRateList);
		monthRate = getListAvg(monthRateList);
		//计算平均误差值
		double[] array = {dayRate,weekRate,monthRate};
		
		//数组排序，误差最小的排在最前面
		Arrays.sort(array);
		
		//神经元类型
		int neuronType = 0;
		//误差最小的是
		if(dayRate.equals(array[0])){
			//日神经元
			neuronType = NEURON_DAY_TYPE;
		}else if(weekRate.equals(array[0])){
			//周神经元
			neuronType = NEURON_WEEK_TYPE;
		}else if(monthRate.equals(array[0])){
			//月神经元
			neuronType = NEURON_MONTH_TYPE;
		}
		return neuronType;
	}

	@Override
	public double getDayPredictData(Long ledgerId) {
		try {
			Map<String,Object> map = dataCacheMap.get(ledgerId);
			Integer neuronType = null;
			if(map != null){
				Double data = (Double)map.get("dayData");
				if(data != null){
					return data;
				}
				//判断有没有神经元类型
				neuronType = (Integer) map.get("neuronType");
			}else {
				//新建一个MAP
				map = new HashMap<String, Object>();
			}
			//临时计算
			
			Date now = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
			//没有神经元类型
			if(neuronType == null){
				neuronType = calNeuronType(now, ledgerId,DATA_TYPE_Q);
			}
			//得到日预测数据
			double dayData = getPredictDayDate(now, ledgerId, neuronType);
			map.put("neuronType", neuronType);
			map.put("dayData", Math.rint(dayData));
			dataCacheMap.put(ledgerId, map);
			return dayData;
			
		} catch (ParseException e) {
			Log.info("getDayPredictData error ParseException");
		}
		return 0;
	}

	@Override
	public double getMonthPredictData(Long ledgerId) {
		try {
			Map<String,Object> map = dataCacheMap.get(ledgerId);
			Integer neuronType = null;
			if(map != null){
				Double data = (Double)map.get("monthData");
				if(data != null){
					return data;
				}
				//判断有没有神经元类型
				neuronType = (Integer) map.get("neuronType");
			}else {
				//新建一个MAP
				map = new HashMap<String, Object>();
			}
			//临时计算
			
			Date now = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
			//没有神经元类型
			if(neuronType == null){
				neuronType = calNeuronType(now, ledgerId,DATA_TYPE_Q);
			}
			//得到日预测数据
			double monthData = getMonthPredictData(now, ledgerId);
			map.put("monthData", Math.rint(monthData));
			map.put("neuronType", neuronType);
			dataCacheMap.put(ledgerId, map);
			return monthData;
			
		} catch (ParseException e) {
			Log.info("getMonthPredictData error ParseException");
		}
		return 0;
	}
	
}
