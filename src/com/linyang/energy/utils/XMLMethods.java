package com.linyang.energy.utils;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.linyang.util.CommonMethod;

/**
 * 简单操作XML文件的操作类
 * @description:
 * @version:0.1
 * @author:Cherry
 * @date:Sep 12, 2013
 */
public class XMLMethods {

	/**
	 * 得到Document对象
	 * @param filePath 文件路径，包括文件名称
	 * @return
	 * @throws DocumentException
	 */
	public static  Document getDocument(String filePath) throws DocumentException {
		return new SAXReader().read(new File(loadPath(filePath)));
	}
	
	/**
	 * 得到List<Element>对象
	 * @param dom
	 * @param xpath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> getElement(Document dom, String xpath) {
		return (List<Element>) dom.selectNodes(xpath);
	}

	
	public static String loadPath(String relative_Dir) {
		return CommonMethod.getAbsoluteFilePath(XMLMethods.class.getResource("/")+ relative_Dir);
	}

}
