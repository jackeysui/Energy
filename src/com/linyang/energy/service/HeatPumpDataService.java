package com.linyang.energy.service;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.FlowLineBean;
import com.linyang.energy.model.HeatPumpDataBean;
import com.linyang.energy.model.ScheduleBean;

public interface HeatPumpDataService {

	Map<String, Object> getHeatPumpData(Map<String,Object> queryMap);
	
	/**
	 * 得到Excel，数据填充
	 * @author 周礼
	 * @param 参数 table名字sheetName，输出流output，结果集map,页面请求信息queryMap
	 */
	void getEleExcel(String sheetName, OutputStream output,Map<String,Object> queryMap) throws UnsupportedEncodingException;
}
