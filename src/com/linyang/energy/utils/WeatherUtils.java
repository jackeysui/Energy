package com.linyang.energy.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.com.webxml.ArrayOfString;
import cn.com.webxml.WeatherWS;
import cn.com.webxml.WeatherWSSoap;

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.WeatherBean;
import com.linyang.util.DateUtils;

/**
 * 获取天气信息
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 4, 2013
 */
public class WeatherUtils {
	
	private static Map<String,WeatherBean> cache = Collections.synchronizedMap(new HashMap<String,WeatherBean>());
	private WeatherUtils(){}
	
	private static class WeatherUtilsHolder{
		private static  final WeatherUtils instance = new WeatherUtils();
	}
	
	public static WeatherUtils getInstance(){
		return WeatherUtilsHolder.instance;
	}
	
	/**
	 * 获取城市天气信息（三天的天气信息）
	 * @param cityName 城市名称(南京、北京...)
	 * @return
	 */
	public List<String> getWeatherbyCityName(String cityName){
		if(isNeedReLoad(cityName)){
			Log.info("开始调用获取天气的WebService接口");
			
				WeatherWS wws = new WeatherWS();
		        WeatherWSSoap wwsp = wws.getWeatherWSSoap();  
		        ArrayOfString aos = wwsp.getWeather(cityName, WebConstant.weatherUserId);
		        if(aos != null){
		        	List<String> infos = aos.getString();
		        	if(infos != null && infos.size() != 1){
		        		List<String> list = aos.getString();
		        		//获取天气发布的时间
		        		String publishTime = list.get(3);
		        		cache.put(cityName,new WeatherBean(DateUtils.convertTimeToLong(publishTime,"yyyy/MM/dd HH:mm:ss")*1000,list));
		        	}
		        }
		}
		return cache.get(cityName)==null?new ArrayList<String>():cache.get(cityName).getInfos();
	}
	
	/**
	 * 验证是否需要调用接口
	 * @param cityName
	 * @return
	 */
	private  boolean isNeedReLoad(String cityName){
		if(!cache.containsKey(cityName))
			return true;
		else{
			return ((System.currentTimeMillis() - cache.get(cityName).getWetherTime())/(1000*60*60)>2);
		}
	}
}
