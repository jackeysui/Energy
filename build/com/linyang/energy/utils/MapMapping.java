package com.linyang.energy.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.MapNodeBean;
import com.linyang.util.CommonMethod;

public class MapMapping {
	private static final String filePath = "com/linyang/energy/xml/map.xml";
	/**
	 * 这里放置map主要考虑到以后的扩展
	 */
	private static final Map<String,List<MapNodeBean>> mapping = new HashMap<String, List<MapNodeBean>>();
	
	private MapMapping(){}
	/**
	 * xml文件Document对象
	 */
	private static  Document doc = null;
	static{
		try {
			doc = XMLMethods.getDocument(filePath);
			initParams();
		} catch (DocumentException e) {
			Log.error("MapMapping initParam error");
		}
	}
	private static void initParams() {
		List<Element> pList = XMLMethods.getElement(doc,"map/nodeList");
		if(CommonMethod.isCollectionNotEmpty(pList)){
			for (Element element : pList) {
				String pClassName = element.attributeValue("className");
				List<MapNodeBean> ll = null;
				//得到对应类型的list<Element>
				List<Element> list = XMLMethods.getElement(doc,"map/nodeList[@className='"+pClassName+"']/node");
				if(CommonMethod.isCollectionNotEmpty(list)){
					ll = new ArrayList<MapNodeBean>();
					for (Element element2 : list) {
						String className = element2.attributeValue("className");
						String name = element2.attributeValue("name");
						String ledgerId = element2.attributeValue("ledgerId");
						if(CommonMethod.isAllNotEmpty(className,name,ledgerId)){
							ll.add(new MapNodeBean(Long.parseLong(ledgerId),className,name));
						}
					}
					mapping.put(pClassName, ll);
				}
			}
		}
	}
	
	public static Map<String, List<MapNodeBean>> getMapMapping() {
		return mapping;
	}
}
