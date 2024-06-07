package com.linyang.energy.mapping.reportanalysis;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportAnalysis4LostMapper {

	
	/**
	 * 得到测量点报表数据(缺失表计)
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> getMeterReportData4Lost(@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endDate,@Param( "dataType" ) int dataType,@Param( "objectId" ) Long objectId);
	
	
}
