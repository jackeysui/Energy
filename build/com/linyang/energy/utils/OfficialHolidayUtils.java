package com.linyang.energy.utils;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.HolidayBean;
import com.linyang.util.CommonMethod;

/**
 * 读取查询配置文件内容
 * @author xs
 * @version
 * @since 2016年3月24日14:44:34
 */
public class OfficialHolidayUtils {
	private static final String filePath="com/linyang/energy/xml/holiday.xml";	
	private static  Document doc = null;
	static{
		try {
			doc = XMLMethods.getDocument(filePath);
		} catch (DocumentException e) {
			Log.error("OfficialHoliday -- get doc error");
		}
	}

	@SuppressWarnings("unchecked")
	public static List<HolidayBean> readOfficialHolidayList(){
		List<HolidayBean> holidayBeans = new ArrayList<HolidayBean>();
		//获取全部年份的数据
		List<Element> yearList = XMLMethods.getElement(doc,"holidays/year");
		if(CommonMethod.isCollectionNotEmpty(yearList)){
			for (Element yearElement : yearList) {
				String type = yearElement.attributeValue("type");
				//1表示为今年的数据，0为往年的数据
				if(type.equals("1")){
					String year = yearElement.attributeValue("name");
					//获取每一年的数据
					List<Element> list = yearElement.elements();
					if(CommonMethod.isCollectionNotEmpty(list)){
						for (Element element : list) {
							HolidayBean holidayBean = new HolidayBean();
							String name = element.attributeValue("name");
							String fromDate = element.attributeValue("fromDate");
							String endDate = element.attributeValue("endDate");
							holidayBean.setName(name);
							holidayBean.setFromDate(DateUtil.convertStrToDate(year+"-"+fromDate, DateUtil.SHORT_PATTERN));
							holidayBean.setEndDate(DateUtil.convertStrToDate(year+"-"+endDate, DateUtil.SHORT_PATTERN));
							holidayBeans.add(holidayBean);
						}
					}
				}
			}
		}
		return holidayBeans;
	
	}
	
	/**
	 * 获取历史包括几年的法定节假日数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<HolidayBean> readHisOfficialHolidayList(){
		List<HolidayBean> holidayBeans = new ArrayList<HolidayBean>();
		//获取全部年份的数据
		List<Element> yearList = XMLMethods.getElement(doc,"holidays/year");
		if(CommonMethod.isCollectionNotEmpty(yearList)){
			for (Element yearElement : yearList) {
				String year = yearElement.attributeValue("name");
				//获取每一年的数据
				List<Element> list = yearElement.elements();
				if(CommonMethod.isCollectionNotEmpty(list)){
					for (Element element : list) {
						HolidayBean holidayBean = new HolidayBean();
						String name = element.attributeValue("name");
						String fromDate = element.attributeValue("fromDate");
						String endDate = element.attributeValue("endDate");
						holidayBean.setName(name);
						holidayBean.setFromDate(DateUtil.convertStrToDate(year+"-"+fromDate, DateUtil.SHORT_PATTERN));
						holidayBean.setEndDate(DateUtil.convertStrToDate(year+"-"+endDate, DateUtil.SHORT_PATTERN));
						holidayBeans.add(holidayBean);
					}
				}
			}
		}
		return holidayBeans;
	}
}
