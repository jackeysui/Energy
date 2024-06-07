package com.linyang.energy.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

public interface ReportService {

	
	/**
	 * 查询数据
	 * @param param
	 */
	Map<String, Object> search(Map<String, Object> param) throws ParseException;

	/**
	 * 导出报表
	 * @param string
	 * @param outputStream
	 * @param list
	 * @param reportType
	 * @param title
	 */
	void reportExcel(String str, ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param, String title);

	/**
	 * 查看该用户下的水电气热电表情况
	 * @param ledgerId
	 * @return
	 */
	Map<String, Object> getMeterTypeMap(Long ledgerId);

	/**
	 * 查询商户月电费数据
	 * @param param
	 * @return
	 */
	Map<String, Object> searchMonthEle(Map<String, Object> param);

	/**
	 * 导出商户月电费数据
	 * @param string
	 * @param outputStream
	 * @param result
	 * @param param
	 * @param title
	 */
	void reportMonthExcel(String string, ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param, String title);

	/**
	 * 管理树获取子节点（能管单元）
	 * @param param
	 * @return
	 */
	List<String> getChildLedger(Map<String, Object> param);
	
	/**
	 * 管理树获取子节点（计量点）
	 * @param param
	 * @return
	 */
	List<String> getChildMeter(Map<String, Object> param);
	
	/**
	 * 拓扑树企业获取计量点 
	 * @param param
	 * @return
	 */
	List<String> getEleChildMeterByL(Map<String, Object> param);
	
	/**
	 *  拓扑树计量点获取子节点（计量点）
	 * @param param
	 * @return
	 */
	List<String> getEleChildMeterByM(Map<String, Object> param);
	
	/**
	 * 新建一个查询所有测量点的方法
	 * @param param
	 * @return
	 */
	List<String> getEleChildMeterNew(Map<String,Object> param);
	
	
	/**
	 * 得到测量点报表数据(缺失表计)
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getMeterReportData4Lost(Map<String,Object> param,Date beginDate, Date endDate, int dataType, List<String> objectId);


    /**
     * 负荷分析报表 --- 加载数据
     */
    Map<String, Object> fhAnalysisDataPage(Map<String, Object> paramInfo);

    /**
     * 负荷分析报表 --- 导出EXCEL
     */
    void fhAnalysisExcel(ServletOutputStream outputStream, Map<String, Object> paramInfo, String title);

    /**
     * 电耗分析 --- 加载数据
     */
    List<Map<String, Object>> energyAnalysisData(Map<String, Object> paramInfo);

    /**
     * 电耗分析 --- 导出EXCEL
     */
    void energyAnalysisExcel(ServletOutputStream outputStream, Map<String, Object> paramInfo, String title);

    /**
     * 电耗明细 --- 加载数据
     */
    Map<String, Object> energyDetailDataPage(Map<String, Object> paramInfo);

    /**
     * 电耗明细 --- 导出EXCEL
     */
    void energyDetailExcel(ServletOutputStream outputStream, Map<String, Object> paramInfo, String title);

}
