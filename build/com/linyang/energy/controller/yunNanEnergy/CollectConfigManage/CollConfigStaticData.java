package com.linyang.energy.controller.yunNanEnergy.CollectConfigManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linyang.energy.utils.DateUtil;

public class CollConfigStaticData {
	/**
	 * 采集范围静态数据
	 * @return
	 */
	public static List<Map<String,Object>> getStaticScopes()
	{
		List<Map<String,Object>> scopes=new ArrayList<Map<String,Object>>();
		Map<String,Object> s1=new HashMap<String,Object>();
		s1.put("SCOPE", "1");
		s1.put("SCOPE_NAME", "全厂");
		scopes.add(s1);
		Map<String,Object> s2=new HashMap<String,Object>();
		s2.put("SCOPE", "2");
		s2.put("SCOPE_NAME", "生产工序");
		scopes.add(s2);
		Map<String,Object> s3=new HashMap<String,Object>();
		s3.put("SCOPE", "3");
		s3.put("SCOPE_NAME", "生产工序单元");
		scopes.add(s3);
		Map<String,Object> s4=new HashMap<String,Object>();
		s4.put("SCOPE", "4");
		s4.put("SCOPE_NAME", "重点耗能设备");
		scopes.add(s4);
		return scopes;
	}
	
	/**
	 * 采集年度静态数据
	 * @return
	 */
	public static List<Map<String,Object>> getStaticYears(){
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		int currentYear=DateUtil.getCurrentYear();
		for(int i=-2;i<8;i++) {
			Map<String,Object> s4=new HashMap<String,Object>();
			s4.put("YEAR", currentYear+i);
			result.add(s4);
		}
		return result;
	}
	
	/**
	 * 采集频率
	 * @return
	 */
	public static List<Map<String,Object>> getStaticFrequce()
	{
		List<Map<String,Object>> frequces=new ArrayList<Map<String,Object>>();
		Map<String,Object> s1=new HashMap<String,Object>();
		s1.put("FREQUCE", "0");
		s1.put("FREQUCE_NAME", "实时");
		frequces.add(s1);
		Map<String,Object> s2=new HashMap<String,Object>();
		s2.put("FREQUCE", "1");
		s2.put("FREQUCE_NAME", "日");
		frequces.add(s2);
		Map<String,Object> s3=new HashMap<String,Object>();
		s3.put("FREQUCE", "2");
		s3.put("FREQUCE_NAME", "月");
		frequces.add(s3);
		Map<String,Object> s4=new HashMap<String,Object>();
		s4.put("FREQUCE", "3");
		s4.put("FREQUCE_NAME", "年");
		frequces.add(s4);
		return frequces;
	}
	
	/**
	 * 采集频率
	 * @return
	 */
	public static String convertFrequenceStr(String frequence)
	{
		String result="";
		switch(frequence)
		{
			case "0":
				result="实时";
				break;
			case "1":
				result="日";
				break;
			case "2":
				result="月";
				break;
			case "3":
				result="年";
				break;
		}		
		return result;
	}
	
	/**
	 * 数据采集来源 (1-管理信息系统,2-生产监控管理系统,3-工业控制系统,4-现场仪表,5-手工填报,
	 * 6-能源供应单位,7-其他
	 * @return
	 */
	public static List<Map<String,Object>> getStaticInputTypes()
	{
		List<Map<String,Object>> frequces=new ArrayList<Map<String,Object>>();
		Map<String,Object> s1=new HashMap<String,Object>();
		s1.put("INPUT_TYPE", "1");
		s1.put("INPUT_TYPE_NAME", "管理信息系统");
		frequces.add(s1);
		Map<String,Object> s2=new HashMap<String,Object>();
		s2.put("INPUT_TYPE", "2");
		s2.put("INPUT_TYPE_NAME", "生产监控管理系统");
		frequces.add(s2);
		Map<String,Object> s3=new HashMap<String,Object>();
		s3.put("INPUT_TYPE", "3");
		s3.put("INPUT_TYPE_NAME", "工业控制系统");
		frequces.add(s3);
		Map<String,Object> s4=new HashMap<String,Object>();
		s4.put("INPUT_TYPE", "4");
		s4.put("INPUT_TYPE_NAME", "现场仪表");
		frequces.add(s4);
		Map<String,Object> s5=new HashMap<String,Object>();
		s1.put("INPUT_TYPE", "5");
		s1.put("INPUT_TYPE_NAME", "手工填报");
		frequces.add(s5);
		Map<String,Object> s6=new HashMap<String,Object>();
		s2.put("INPUT_TYPE", "6");
		s2.put("INPUT_TYPE_NAME", "能源供应单位");
		frequces.add(s6);
		Map<String,Object> s7=new HashMap<String,Object>();
		s3.put("INPUT_TYPE", "7");
		s3.put("INPUT_TYPE_NAME", "其他");
		frequces.add(s7);
		return frequces;
	}
	
	/**
	 * 采集采集系统静态数据
	 * @return
	 */
	public static List<Map<String,Object>> getStaticCollplateSystems()
	{
		List<Map<String,Object>> scopes=new ArrayList<Map<String,Object>>();
		Map<String,Object> s1=new HashMap<String,Object>();
		s1.put("COLLPLAT_ID", "1");
		s1.put("COLLPLAT_NAME", "林洋能源管理系统");
		scopes.add(s1);
		return scopes;
	}
}
