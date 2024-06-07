package com.linyang.energy.staticdata;
import java.sql.SQLException;
/**
 * 
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.linyang.common.web.common.LanguageEnum;
import com.linyang.common.web.common.Log;
import com.linyang.energy.service.StaticDataService;
import com.linyang.util.CommonMethod;


/**
 * 静态数据用来初始化数据字典的
  @description:
  @version:0.1
  @author:Cherry
  @date:2013-7-4
 */
public class StaticData {
	/**
	 * 默认是中文环境
	 */
	private static LanguageEnum language = LanguageEnum.CHINESE;
	
	/**
	 * 获取实例的静态内部类，jvm来保证唯一的实例
	  @description:
	  @version:0.1
	  @author:Cherry
	  @date:Sep 4, 2013
	 */
	private static class StaticDataHolder{
		private static final StaticData instance = new StaticData();
	}
	
	public StaticData(){}
	/**
	 * 获取实例的唯一方法
	 * @return
	 */
	public static StaticData getInstance(){
		return StaticDataHolder.instance;
	}
	
	StaticDataService staticDataServiceImpl;
	
	/**
	 * 静态数据，第一层是dataCode,第二层是对应的值
	 */
	private  static  ConcurrentMap<String,Map<String,String>>  STATICDATA = new ConcurrentHashMap<String,Map<String,String>>();
	
	public static ConcurrentMap<String, Map<String, String>> getStaticData() {
		return STATICDATA;
	}
	/**
	 * 初始化静态数据
	 */
	public synchronized void initStaticData(){
		initData();
		Log.debug("初始化中文版静态数据:"+STATICDATA);
	}
	
	/**
	 * 刷新静态数据
	 * @param dataCode
	 */
	public synchronized void flushStaticData(String dataCode ,String dataType){
		if(CommonMethod.isEmpty(dataCode)){
			Log.error("刷新静态数失败，dataCode为空");
			return ;
		}
		STATICDATA.remove(dataCode);
		List<Map<String,Object>> result = staticDataServiceImpl.flushStaticData(dataCode, dataType);
		if(DictionaryDataFactory.DATA_TYPE_ONE.equals(dataType)){
			handleFirstLevelData(result);
		}else if(DictionaryDataFactory.DATA_TYPE_TWO.equals(dataType)){
			handleSecondLevelData(result);
		}
	}
	
	
	/**
	 * 重新加载所有静态数据
	 * @param language
	 */
	public synchronized void loadOtherLanguageStaticData(){
			language = LanguageEnum.OTHER;
			STATICDATA.clear();
			initData();
			Log.debug("初始化其他语言静态数据:"+STATICDATA);
	}
	/**
	 * 初始化数据
	 */
	private  void initData(){
		initFirstLevelData();
		initSecondLevelData();
	}
	
	/**
	 *初始化第一层数据
	 */
	private void initFirstLevelData(){
		handleFirstLevelData(staticDataServiceImpl.initFirstLevelData(language));
	}
	
	/**
	 *处理第一层数据
	 */
	private void handleFirstLevelData(List<Map<String,Object>> result){
		if(result != null && result.size() > 0){
			for (Map<String, Object> mapResult : result) {
				//try{
					String dataCode =  mapResult.get("DATACODE")== null ?"":mapResult.get("DATACODE").toString();
					String key = mapResult.get("KEY").toString();
					String value = mapResult.get("VALUE") == null?"":mapResult.get("VALUE").toString();
					processData(dataCode, key, value);
				//}catch (Exception e) {
				//	Log.error("初始化第一层静态数据出错");
				//	continue;
				//}
					
			}
		}
	}


	
	
	/**
	 * 初始化第二层数据
	 */
	private void initSecondLevelData(){
		handleSecondLevelData(staticDataServiceImpl.initSecondLevelData(language));
		
	}


	/**
	 * 处理第二层数据
	 * @param queryForList
	 */
	private void handleSecondLevelData(List<Map<String, Object>> result) {
		if(result != null && result.size() > 0){
			for (Map<String, Object> map : result) {
				StringBuffer sbsql = new StringBuffer();
				//try{
					String tableName =  map.get("TABLENAME")== null ?"":map.get("TABLENAME").toString();
					String key =  map.get("KEY")== null ?"":map.get("KEY").toString();
					String value =  map.get("VALUE")== null ?"":map.get("VALUE").toString();
					String condition =  map.get("CONDITION")== null ?"":map.get("CONDITION").toString();
					String dataCode =  map.get("DATACODE")== null ?"":map.get("DATACODE").toString(); 
					
					sbsql.append(" SELECT ").append(key).append(",").append(value).append("  FROM  ")
						 .append(tableName).append("  ").append(condition);
					addSecondLevelData(key,value,dataCode,staticDataServiceImpl.handleSecondLevelData(sbsql.toString()));
				//}catch (Exception e) {
				//	Log.error("执行sql:"+sbsql.toString());
				//	Log.error("处理第二层静态数据出错");
				//	continue;
				//}
			}
		}
	}
	
	/**
	 * 添加第二层数据
	 */
	private void addSecondLevelData(String key,String value,String dataCode ,List<Map<String, Object>> result){
		if(result != null && result.size() > 0){
			for (Map<String, Object> map : result) {
				
					String KEY = map.get(key.toUpperCase()).toString();
					String VALUE = map.get(value.toUpperCase())==null?"":map.get(value.toUpperCase()).toString() ;
					processData(dataCode, KEY, VALUE);
			}
		}
	}
	
	/**
	 * 处理数据
	 * @param dataCode
	 * @param key 字段名称key
	 * @param value 字段名称value
	 */
	private void processData(String dataCode, String key, String value) {
		if(STATICDATA.get(dataCode) == null){
			Map<String,String> map = new HashMap<String,String>();
			map.put(key, value);
			STATICDATA.put(dataCode, map);
		}else{
			Map<String,String> map = STATICDATA.get(dataCode);
			map.put(key, value);
			STATICDATA.put(dataCode, map);
		}
	}
	public void setStaticDataServiceImpl(StaticDataService staticDataServiceImpl) {
		this.staticDataServiceImpl = staticDataServiceImpl;
	}
	
}
