package com.linyang.energy.staticdata;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.esotericsoftware.minlog.Log;

/**
 * 数据字典翻译
  @description:
  @version:0.2
  @author:Cherry
  @date:2013-7-4
  2013-9-6修改，支持多种语言
 */
public class DictionaryDataFactory {
	
	public static final String DATA_TYPE_ONE = "1";
	public static final String DATA_TYPE_TWO = "2";
	
	private  static  ConcurrentMap<String,Map<String,String>>  chineseLanguageData = new ConcurrentHashMap<String,Map<String,String>>();
	private  static  ConcurrentMap<String,Map<String,String>>  otherLanguageData = new ConcurrentHashMap<String,Map<String,String>>();
	
	
	public static Map<String, Map<String, String>> getDictionaryData(){
		return chineseLanguageData;
	}
	/**
	 * 得到静态字典
	 * @return the dictionaryData
	 */
	public static Map<String, Map<String, String>> getDictionaryData(HttpServletRequest request) {
		if(request == null)
			return  getDictionaryData();
		else {
			if(isChineseLanguage(request))
				return chineseLanguageData;
			else
				return 	otherLanguageData;
		}
	}

	/**
	 * 刷心静态数据
	 * @param datacode 字典码
	 * @param datatype 类型
	 */
	public static void flushStaticData(String datacode,String datatype,HttpServletRequest request){
		StaticData.getInstance().flushStaticData(datacode, datatype);
		//需要更具语言重新放置缓存
		if(isChineseLanguage(request))
			chineseLanguageData.put(datacode,StaticData.getStaticData().get(datacode));
		else
			otherLanguageData.put(datacode,StaticData.getStaticData().get(datacode));
		StaticData.getStaticData().clear();
	}
	
	
	/**
	 * 重新加载静态数据，主要用来中英文切换用的
	 * @param isChinese true表示的加载中文，false表示加载其他语言
	 */
	public static void initStaticData(){
		//初始化中文版静态数据
		StaticData.getInstance().initStaticData();
		chineseLanguageData.putAll(StaticData.getStaticData());
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			Log.info("sleep interrupted!");
		}
		//初始化英文版静态数据
		StaticData.getInstance().loadOtherLanguageStaticData();
		otherLanguageData.putAll(StaticData.getStaticData());
		//加载结束后清除掉原来的数据
		StaticData.getStaticData().clear();
	}
	
	/**
	 * 用来标识是否是正文环境
	 * @param request
	 * @return true：中文环境，false:其他语言环境
	 */
	public static boolean isChineseLanguage(HttpServletRequest request){
		Locale locale = request.getLocale();
		if("zh".equalsIgnoreCase(locale.getLanguage())||"zh_cn".equalsIgnoreCase(locale.getLanguage()))
			return true;
		else
			return false;
	}

}
