package com.linyang.energy.service.impl;

import com.leegern.util.DateUtil;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.demand.LedgerDetailsMapper;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.LedgerDetailsService;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.ListSortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linyang.energy.utils.DateUtil.getPreMonthLastDay2;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 10:13 2019/9/7
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class LedgerDetailsServiceImpl implements LedgerDetailsService {
	
	/**
	 * 数据保留位数
	 */
	private final String KEEP_FEW = "0.00";
	
	@Autowired
	private LedgerDetailsMapper ledgerDetailsMapper;
	
	/**
	 * 获取方案列表
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/10 11:06
	 */
	@Override
	public List<Map<String, Object>> queryPlanList() {
		return ledgerDetailsMapper.queryPlanList();
	}
	
	/**
	 * 根据方案id获取方案详细信息
	 * @author catkins.
	 * @param planId
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/7 10:17
	 */
	@Override
	public Map<String, Object> queryPlanDetailsById(long planId) {
		return ledgerDetailsMapper.queryPlanDetailsById( planId );
	}
	
	/**
	 * 得到方案下企业列表
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/7 11:22
	 */
	@Override
	public List<Map<String, Object>> queryLedgerListPageData(Page page, Map<String, Object> queryMap) throws Exception {
		queryMap.put(Dialect.pageNameField, page);
		List<Map<String,Object>> ledgerList = ledgerDetailsMapper.queryLedgerListPageData(queryMap);
		
		for (Map<String,Object> map:ledgerList) {
			
			if(DateUtil.convertStrToDate(queryMap.get( "endDate" ).toString(), DateUtil.DEFAULT_SHORT_PATTERN ).after( new Date() )){
				continue;
			}
			
			//方案响应时间段的平均负荷
			Double realAP = this.dataProcess4Ledgers( Long.parseLong( map.get( "LEDGERID" ).toString() ),
					queryMap.get( "beginDate" ).toString(), queryMap.get( "endDate" ).toString(),
					queryMap.get( "beginTime" ).toString(), queryMap.get( "endTime" ).toString());
			
			if(realAP == null){
				realAP = 0.0;
			}
			
			//实际调峰量
			map.put( "realAP" , this.keepDigits( String.valueOf( realAP ), KEEP_FEW ) );
			
			//调峰误差
			map.put( "mistake" , this.keepDigits( String.valueOf( realAP - Double.parseDouble( map.get( "LEDGERPITCHPEAK" ).toString() ) ),KEEP_FEW ) );
			
			// 调峰占比
			map.put( "scale" , realAP / Double.parseDouble( map.get( "PITCHPEAK" ).toString()  )); // 调峰占比
		}
		return ledgerList;
	}
	
	/**
	 * 得到需求响应概况列表信息
	 * @author catkins.
	 * @param queryMap
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/7 11:32
	 */
	@Override
	public Map<String, Object> queryOverview(Map<String, Object> queryMap) throws Exception{
		//使用权限获得的ledgerId进行查询
		//得到 权限下企业数量,加入方案企业数量,计划总调峰量
		Map<String, Object> overviewMap = ledgerDetailsMapper.queryOverview( queryMap );
		
		//得到总调峰量
		double totalAP = 0.0;
		
		List<Map<String,Object>> ledgerIds = ledgerDetailsMapper.queryChartDatas( queryMap );
		
		for (Map<String,Object> map:ledgerIds) {
			if(DateUtil.convertStrToDate(queryMap.get( "endDate" ).toString(), DateUtil.DEFAULT_SHORT_PATTERN ).after( new Date() )){
				continue;
			}
			
			Double seachAP = this.dataProcess4Ledgers( Long.parseLong( map.get( "LEDGERID" ).toString() ),
					queryMap.get( "beginDate" ).toString(), queryMap.get( "endDate" ).toString(),
					queryMap.get( "beginTime" ).toString(), queryMap.get( "endTime" ).toString() );
			if (seachAP != null) {
				totalAP = totalAP + seachAP;
			}
		}
		if(DateUtil.convertStrToDate(queryMap.get( "endDate" ).toString(), DateUtil.DEFAULT_SHORT_PATTERN ).before( new Date() ))
			overviewMap.put( "totalAP" , this.keepDigits( String.valueOf( totalAP ), KEEP_FEW ) );
		return overviewMap;
	}
	
	/**
	 * 得到需求响应方案图表信息
	 * @author catkins.
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/7 11:34
	 */
	@Override
	public List<Map<String, Object>> queryChartDatas(Map<String, Object> queryMap) throws Exception{
		List<Map<String,Object>> ledgerList = ledgerDetailsMapper.queryChartDatas(queryMap);
		for (Map<String,Object> map:ledgerList) {
			
			if(DateUtil.convertStrToDate(queryMap.get( "endDate" ).toString(), DateUtil.DEFAULT_SHORT_PATTERN ).after( new Date() )){
				continue;
			}
			
			Double totalAP = 0.0;
			try {
				//方案响应时间段的平均负荷
				totalAP = this.dataProcess4Ledgers( Long.parseLong( map.get( "LEDGERID" ).toString() ),
						queryMap.get( "beginDate" ).toString(), queryMap.get( "endDate" ).toString(),
						queryMap.get( "beginTime" ).toString(), queryMap.get( "endTime" ).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(totalAP == null)
				totalAP = 0.0;
			
			map.put( "aptotal" , this.keepDigits( String.valueOf( totalAP ), KEEP_FEW ) );	//实际调峰量
			
			map.put( "mistake" ,this.keepDigits( String.valueOf( totalAP - Double.parseDouble( map.get( "LEDGERPITCHPEAK" ).toString() ) ), KEEP_FEW ) );	//调峰误差
			
			map.put( "scale" , totalAP / Double.parseDouble( map.get( "PITCHPEAK" ).toString()  )); // 调峰占比
		}
		
		
		if (null != queryMap && queryMap.containsKey( "sortType" )) {
			if ( Integer.parseInt( queryMap.get( "sortType" ).toString() ) == 2 ) {
				// 按照企业计划调峰量排序
				ListSortUtil.ListSort4Chpeak( ledgerList );
			} else {
				ListSortUtil.ListSort4AP( ledgerList );
			}
		}
		return ledgerList;
	}
	
	/**
	 * 企业列表处理结果集方法
	 * @author catkins.
	 * @param ledgerId		企业id
	 * @param beginDate		开始日期
	 * @param endDate		结束日期
	 * @param beginTime		开始时间(时分秒)
	 * @param endTime		结束时间
	 * @return java.lang.Double
	 * @exception
	 * @date 2019/9/9 13:21
	 */
	private Double dataProcess4Ledgers(long ledgerId, String beginDate,String endDate,String beginTime,String endTime) throws Exception{
		double realAP = 0.0;	//实际调峰量
		//得到两个日期差
		int betweenDays = DateUtil.getBetweenDays( DateUtil.convertStrToDate(beginDate, DateUtil.DEFAULT_SHORT_PATTERN ),
				DateUtil.convertStrToDate(endDate, DateUtil.DEFAULT_SHORT_PATTERN ) )+1;
		double avgData = 0.0;	// 时段内平均功率
		Double avgSearch = null;
		
		double beforeData = 0.0;	// 前7天时段内功率
		Double beforeSearch = null;
		
		//调峰时段内的数据
		for(int i = 0 ; i < betweenDays ; i++ ){
			avgSearch = ledgerDetailsMapper.queryAPavg( ledgerId,
					DateUtil.convertStrToDate(com.linyang.energy.utils.DateUtil.addDay(beginDate,i)+" "+beginTime, DateUtil.DEFAULT_FULL_PATTERN ),
					DateUtil.convertStrToDate(com.linyang.energy.utils.DateUtil.addDay(beginDate,i)+" "+endTime, DateUtil.DEFAULT_FULL_PATTERN  ));
			if(avgSearch != null)
				avgData += avgSearch;
		}
		//7天前日期
		String ltDate = com.linyang.energy.utils.DateUtil.addDay(beginDate,-7);
		for(int i = 0 ; i < 7 ; i++ ){
			beforeSearch = ledgerDetailsMapper.queryAPavg( ledgerId,
					DateUtil.convertStrToDate(com.linyang.energy.utils.DateUtil.addDay(ltDate,i)+" "+beginTime, DateUtil.DEFAULT_FULL_PATTERN ),
					DateUtil.convertStrToDate(com.linyang.energy.utils.DateUtil.addDay(ltDate,i)+" "+endTime, DateUtil.DEFAULT_FULL_PATTERN  ));
			if(beforeSearch != null)
				beforeData += beforeSearch;
		}
		// 计算方案时段内的平均功率
		if(avgData != 0)
			avgData = avgData/betweenDays;
		//计算实际调峰量
		if(beforeData != 0)
			realAP = avgData - beforeData/7;
		
		return realAP;
	}
	
	/**
	 * 保留两位小数
	 * @author catkins
	 * @param dataNumber
	 * @return java.lang.String
	 * @exception
	 * @date 2019/7/29 13:33
	 */
	private String keepDigits(String dataNumber,String pattern) {
		double num = Double.parseDouble( dataNumber );
		DecimalFormat df = null;
		if(pattern == null || pattern.equals( "" ))
			df = new DecimalFormat("#0.0000");
		else
			df = new DecimalFormat(pattern);
		return df.format( num );
	}
	
	
	
	
	
	
}
