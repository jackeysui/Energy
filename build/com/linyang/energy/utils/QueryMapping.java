package com.linyang.energy.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.DataQueryXmlBean;
import com.linyang.energy.dto.MeterTypeEnum;
import com.linyang.util.CommonMethod;

/**
 * 读取查询配置文件内容
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 16, 2013
 */
public class QueryMapping {
	private static final String filePath="com/linyang/energy/xml/data_query.xml";
	
	private static final Map<Integer,List<DataQueryXmlBean>> mapping = new HashMap<Integer, List<DataQueryXmlBean>>();
	
	private QueryMapping(){}
	/**
	 * xml文件Document对象
	 */
	private static  Document doc = null;
	static{
		try {
			doc = XMLMethods.getDocument(filePath);
			for (MeterTypeEnum meterType : MeterTypeEnum.values()) {
				mapping.put(meterType.getMeterType(), new ArrayList<DataQueryXmlBean>());
				initParams(meterType.getMeterType());
			}
		} catch (DocumentException e) {
			Log.error("QueryMapping.getDoc--无法查询和初始化参数");
		}
	}
	/**
	 * 根据计量点类型得到查询信息
	 * @param meterType
	 * @return
	 */
	public static List<DataQueryXmlBean> getQueryMapping(MeterTypeEnum meterType){
		return mapping.get(meterType.getMeterType());
	}
	private static void initParams(int meterType) {
		//得到对应类型的list<Element>
		List<Element> list = XMLMethods.getElement(doc,"dataQuery/queryTypeList/list[@type='"+meterType+"']/element");
		if(CommonMethod.isCollectionNotEmpty(list)){
			DataQueryXmlBean bean = null;
			for (Element element : list) {
				String tableName = element.attributeValue("tableName");
				String timeField = element.attributeValue("timeField");
				String valueField = element.attributeValue("valueField");
				if(CommonMethod.isAllNotEmpty(timeField,tableName,valueField)){
					bean = new DataQueryXmlBean(tableName, timeField, valueField);
					bean.setDesc(element.attributeValue("desc"));
					bean.setName(element.attributeValue("name"));
					mapping.get(meterType).add(bean);
				}
			}
		}
	}
	
}
