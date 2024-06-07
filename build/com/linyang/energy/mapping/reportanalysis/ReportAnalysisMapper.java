package com.linyang.energy.mapping.reportanalysis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ReportAnalysisMapper {

	/**
	 * 得到分户报表数据
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getLedgerReportData(Map<String, Object> param);

	/**
	 * 得到测量点报表数据
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getMeterReportData(Map<String, Object> param);

	/**
	 * 得到表的读数
	 * @param dataType
	 * @param objectId
	 * @param beginDate
	 * @return
	 */
	Map<String, Object> getMeterReading(@Param("dataType") int dataType, @Param("objectId")Long objectId,
			@Param("dateTime")Date dateTime);

	/**
	 * 得到不同类型的电表个数
	 * @param ledgerId
	 * @param i
	 * @return
	 */
	Integer countMeterByType(@Param("ledgerId")Long ledgerId, @Param("meterType")int meterType);

	/**
	 * 得到商户月电费数据
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getMonthEle(Map<String, Object> param);

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
	 * 新建一个查询所有测量点方法
	 * @param param
	 * @return
	 */
	List<String> getEleChildMeterNew(Map<String,Object> param);

    /**
     *  负荷分析报表
     */
    List<Long> getFhAnalysisMeters(@Param("ledgerList")List<Long> ledgerList, @Param("meterList")List<Long> meterList, @Param("objType")int objType);

    List<Map<String, Object>> getFhIDataPageList(Map<String, Object> param);

    List<Map<String, Object>> getFhIDataList(Map<String, Object> param);

    /**
     *  电耗分析
     */
    List<Map<String, Object>> getEleAnalysisData(@Param("ledgerList")List<Long> ledgerList, @Param("meterList")List<Long> meterList, @Param("orderType")int orderType,
                                                 @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);

    List<Map<String, Object>> energyDetailDataPageList(Map<String, Object> param);

    List<Map<String, Object>> energyDetailDataList(Map<String, Object> param);


}
