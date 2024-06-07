package com.linyang.energy.service.impl;/*
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  - /// |    |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.'  >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 *
 *
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *            佛祖保佑     永不宕机     永无BUG
 */

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.IndexMapper;
import com.linyang.energy.mapping.eleAssessment.EleAssessmentMapper;
import com.linyang.energy.mapping.energyanalysis.SchedulingMapper;
import com.linyang.energy.mapping.energydataquery.DayHarMapper;
import com.linyang.energy.mapping.message.AnShanMessageMapper;
import com.linyang.energy.mapping.message.MessageMapper;
import com.linyang.energy.mapping.phone.PhoneMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.ServiceReportBean;
import com.linyang.energy.service.AnShanMessageService;
import com.linyang.energy.service.CostService;
import com.linyang.energy.utils.*;
import com.linyang.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:57 2020/11/27
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class AnShanMessageServiceImpl implements AnShanMessageService {
	
	@Autowired
	private AnShanMessageMapper anShanMessageMapper;
	
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	
	@Autowired
	private IndexMapper indexMapper;
	
	@Autowired
	private CostService costService;
	
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private EleAssessmentMapper eleAssessmentMapper;
	
	@Autowired
	private PhoneMapper phoneMapper;
	
	@Autowired
	private SchedulingMapper schedulingMapper;
	
	/**
	 * 创建报告
	 */
	@Override
	public void createServiceReport() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get( Calendar.YEAR );
		int month = cal.get( Calendar.MONTH );
		String reportModule = year + "" + month;
		int count = this.anShanMessageMapper.serviceReportIsCreate( Long.parseLong( reportModule ) );
		//这个月的还没有生成
		if (count == 0) {
			//得到所有的企业列表
			List<LedgerBean> ledgerList = ledgerManagerMapper.getAllCompanyList();
			if (ledgerList != null && ledgerList.size() > 0) {
				for (LedgerBean ledgerBean : ledgerList) {
					
					//生成每个企业的月度服务报告
					Long ledgerId = ledgerBean.getLedgerId();
					//获取上月第一天
					Date startDate = DateUtil.getPreMonthFristDay( cal.getTime() );
					List<Map<String, Object>> feeList = indexMapper.queryLedgerFeeMonQ( ledgerId, startDate, DateUtil.getNextMonthFirstDay( startDate ) );
					
					//没有电量数据时不生成服务报告
					if (feeList != null && feeList.size() > 0) {
						long infoId = SequenceUtils.getDBSequence();
						//EMO类型--1：普通用户；2-VIP
						Integer ledgerType = ledgerBean.getLedgerType();
						if (ledgerType == null) {
							ledgerType = ServiceReportBean.LEDGER_TYPE_NORMAL;
						}
						Date reportDate = ledgerType.equals( ServiceReportBean.LEDGER_TYPE_NORMAL ) ? new Date() : null;
						String url = "/anShanMessage/pushServiceReportPage.htm?reportId=" + infoId;
						
						//状态：1-未推送；2-推送
						Integer status = ledgerType.equals( ServiceReportBean.LEDGER_TYPE_NORMAL ) ? ServiceReportBean.STATUS_PUSH : ServiceReportBean.STATUS_UNPUSH;
						anShanMessageMapper.insertReportInfo( infoId, ledgerId, reportDate, Long.parseLong( reportModule ), url, new Date(), status );
					}
				}
			}
		}
	}
	
	
	@Override
	public List<ServiceReportBean> searchSerivceReport(Map<String, Object> param) {
		return anShanMessageMapper.searchSerivceReportPageList( param );
	}
	
	/**
	 * 获取服务报告需要的数据
	 * @param reportId
	 * @param accountId
	 * @return
	 */
	@Override
	public Map<String, Object> queryReportInfoData(long reportId, long accountId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		ServiceReportBean bean = this.anShanMessageMapper.queryReportInfoById( reportId );
		Long ledgerId = bean.getLedgerId();
		Date createDate = bean.getCreateTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime( createDate );
		cal.add( Calendar.MONTH, -1 );
		int year = cal.get( Calendar.YEAR );
		int month = cal.get( Calendar.MONTH ) + 1;
		result.put( "baseTime", year + "-" + month );
		result.put( "bean", bean );
		result.put( "accountId", accountId );
		
		Map<String, Object> curparam = new HashMap<String, Object>();
		curparam.put( "photovoltaicId", 0 );
		curparam.put( "ledgerId", ledgerId );
		
		Date baseDate = cal.getTime();
		//获取本月第一天
		Date startDate = DateUtil.getCurrMonthFirstDay( baseDate );
		//获取本月最后
		Date endDate = DateUtil.getMonthLastDay( baseDate );
		
		//获取上月第一天
		cal.add( Calendar.MONTH, -1 );
		Date lastMonth = cal.getTime();
		Date startDate2 = DateUtil.getCurrMonthFirstDay( lastMonth );
		Date endDate2 = DateUtil.getMonthLastDay( lastMonth );
		curparam.put( "startDate", startDate );
		curparam.put( "endDate", endDate );
		curparam.put( "startDate2", startDate2 );
		curparam.put( "endDate2", endDate2 );
		List<Map<String, Object>> curList = this.indexMapper.getTotal3Data( curparam );
		ChartItemWithTime item1 = new ChartItemWithTime( "",
				DateUtils.convertTimeToLong( DateUtil.convertDateToStr( startDate, DateUtil.DEFAULT_PATTERN ) ),
				DateUtils.convertTimeToLong( DateUtil.convertDateToStr( endDate, DateUtil.DEFAULT_PATTERN ) ) );
		SortedMap<String, Object> item1Map = item1.getMap();
		ChartItemWithTime item2 = new ChartItemWithTime( "",
				DateUtils.convertTimeToLong( DateUtil.convertDateToStr( startDate2, DateUtil.DEFAULT_PATTERN ) ),
				DateUtils.convertTimeToLong( DateUtil.convertDateToStr( endDate2, DateUtil.DEFAULT_PATTERN ) ) );
		SortedMap<String, Object> item2Map = item2.getMap();
		List<String> dateAry = new ArrayList<String>();
		for (int i = 0; i < 31; i++) {
			Integer day = i + 1;
			if (day < 10) {
				dateAry.add( "0" + day );
			} else {
				dateAry.add( day.toString() );
			}
		}
		if (curList != null && curList.size() > 0) {
			for (Map<String, Object> map : curList) {
				String type = map.get( "TYPE" ).toString();
				String val = "0";
				if (map.get( "Q" ) != null) {
					val = map.get( "Q" ).toString();
				} else {
					/*System.out.println(map);*/
					Log.info( map );
				}
				Date time = (Date) map.get( "STAT_DATE" );
				String timeString = DateUtil.convertDateToStr( time, TimeEnum.DAY.getDateFormat() );
//					String day = timeString.substring(8, 10);
//					if(!dateAry.contains(day)){
//						dateAry.add(day);
//					}
				if (type.equals( "1" )) {//本月
					if (item1Map.containsKey( timeString )) {
						item1Map.put( timeString, val );
					}
				} else {
					if (item2Map.containsKey( timeString )) {
						item2Map.put( timeString, val );
					}
				}
			}
		}
		result.put( "dateAry", dateAry );
		result.put( "item1", item1Map );
		result.put( "item2", item2Map );
		
		//本月电量
		List<Map<String, Object>> feeList = indexMapper.queryLedgerFeeMonQ( ledgerId, startDate, DateUtil.getNextMonthFirstDay( startDate ) );
		result.put( "feeList", feeList );
		
		//本月电费
		Map<String, Object> feeMap = new HashMap<String, Object>();
		feeMap.put( "baseTime", year + "-" + month );
		feeMap.put( "objId", ledgerId );
		feeMap.put( "objType", 1 );
		feeMap.put( "queryType", 1 );
		
		try {
			//计算电费
			Map<String, Object> fee = costService.calEmoDcpEleFee( feeMap );
			result.put( "fee", fee );
			
			//各厂区用电量
//		List<Map<String, Object>> childList = this.indexMapper.getTotal11Data( curparam );
//		result.put( "childList", childList );
			
			// 最大负载率用电单元
			List<Map<String, Object>> maxLoad = this.messageMapper.getMaxLoadByLedgerId( ledgerId, startDate, endDate );
			result.put( "maxLoad", maxLoad );
			
			// 最小功率因数
			List<Map<String, Object>> minPF = this.messageMapper.getMinPfByLedgerId( ledgerId, startDate, endDate );
			result.put( "minPF", minPF );
			
			// 电压不平衡度
			List<Map<String, Object>> vBalanAvg = this.messageMapper.getVBalanAvgByLedgerId( ledgerId, startDate, endDate );
			result.put( "VBalanAvg", vBalanAvg );
			
			// 电流不平衡度
			List<Map<String, Object>> iBalanAvg = this.messageMapper.getIBalanAvgByLedgerId( ledgerId, startDate, endDate );
			result.put( "IBalanAvg", iBalanAvg );
			
			// 企业下所有测量点的负载率曲线(key是meterName)
//		result.put( "lfs", this.assessmentLF( ledgerId, startDate, endDate ) );
			
			// 查询企业总有功功率曲线+图表数据
			result.put( "powerData",this.assessmentPowerDatas(ledgerId,startDate,endDate) );
			
			result.put( "powerLineData",this.assessmentPowerLineDatas(ledgerId,startDate,endDate) );
			
			// 最小负载率用电单元
			List<Map<String, Object>> minLoad = this.anShanMessageMapper.getMinLoadByLedgerId( ledgerId, startDate, endDate );
			result.put( "minLoad", minLoad );
			
			// 谐波电流
			Map<String, Object> harMap1 = assessmentHar( ledgerId, startDate, endDate, 1 );
			Map<String, Object> newHarMap1 = MapKeyComparator.sortMapByKeyForChartItem( harMap1 );
			result.put( "harMap1", newHarMap1 );
			
			// 谐波电压
			Map<String, Object> harMap2 = assessmentHar(ledgerId, startDate, endDate,2);
			Map<String, Object> newHarMap2 = MapKeyComparator.sortMapByKeyForChartItem( harMap2 );
			result.put( "harMap2", newHarMap2 );
		} catch (Exception e) {
			Log.error( "method:queryReportInfoData出错"+e.getMessage() );
		}
		
		result.put( "startDate",startDate );
		result.put( "endDate",endDate );
		return result;
	}
	
	/**
	 * 获取服务报告提示
	 * @param reportId
	 * @param accountId
	 * @return
	 */
	@Override
	public String getReportTips(long reportId, long accountId) {
		String tips = " ";
		ServiceReportBean bean = this.anShanMessageMapper.queryReportInfoById( reportId );
		Long ledgerId = bean.getLedgerId();
		Date createDate = bean.getCreateTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime( createDate );
		cal.add( Calendar.MONTH, -1 );
		int year = cal.get( Calendar.YEAR );
		int month = cal.get( Calendar.MONTH ) + 1;
		//本月电费
		Map<String, Object> feeMap = new HashMap<String, Object>();
		feeMap.put( "baseTime", year + "-" + month );
		feeMap.put( "objId", ledgerId );
		feeMap.put( "objType", 1 );
		feeMap.put( "queryType", 1 );
		//计算电费
		Map<String, Object> fee = costService.calEmoDcpEleFee( feeMap );
		
		
		//查询电费率
		long rateId = messageMapper.getRateIdByLedger( ledgerId );
		if (rateId == 0) {
			tips = tips + " 未配置电费模板(能管对象管理中配置电费率)；\n";
		}
		
		//查询是否配置功率因数
		int thresholdNum = messageMapper.getThresholdByLedger( ledgerId );
		if (thresholdNum == 0) {
			tips = tips + " 未配置功率因数(能管对象管理中配置标准功率因数)；\n";
		}
		
		//申报方式
		if (!fee.containsKey( "declareType" ) && fee.get( "declareType" ) == null) {
			tips = tips + " 未配置基本电费的申报方式:按变压器容量申报或按月最大需量申报(需量分析中申报)；\n";
		} else {
			Integer type = Integer.valueOf( fee.get( "declareType" ).toString() );
			if (type == 2) {
				if (!fee.containsKey( "declareValue" ) && fee.get( "declareValue" ) == null) {
					tips = tips + "需量申报未配置月申报量；";
				}
			}
		}
		
		//主变
		int volumeNum = messageMapper.getVolumeType1( ledgerId );
		if (volumeNum < 1) {
			tips = tips + " 未配置主变(结构树配置中配置)；\n";
		}
		
		List<Map<String, Object>> meterList = messageMapper.getMeterByVolumeType( ledgerId );
		
		boolean meterConfig = true;
		for (Map<String, Object> meter : meterList) {
			long meterId = Long.valueOf( meter.get( "METER_ID" ).toString() );
			int num1 = messageMapper.hasThresholdId1( meterId );
			int num2 = messageMapper.hasThresholdId3( meterId );
			int num3 = messageMapper.hasThresholdId4( meterId );
			if (num1 < 1) {
				meterConfig = false;
			}
			if (num2 < 1) {
				meterConfig = false;
			}
			if (num3 < 1) {
				meterConfig = false;
			}
			
		}
		
		if (!meterConfig) {
			tips = tips + " 存在变压器未配置额定参数(结构树配置中配置)；\n";
		}
		
		return tips;
	}
	
	/**
	 * 处理拼装负载率曲线数据
	 * @param ledgerId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private Map<String,Object> assessmentLF(long ledgerId,Date beginDate,Date endDate){
		Map<String,Object> lfResult = new HashMap<>( 0 );//负载率曲线的最终结果集(以meterName作为key)
		List<Map<String,Object>> meterIds = this.anShanMessageMapper.queryLedgerMeter( ledgerId );
		for (Map<String,Object> meter : meterIds ) {
			List<Map<String, Object>> lfs = this.anShanMessageMapper.queryLFbyMeterId( Long.parseLong( meter.get( "METER_ID" ).toString() ),beginDate,endDate );
			if( lfs != null && lfs.size() > 0 )
				lfResult.put( lfs.get( 0 ).get( "meterName" ).toString(),lfs );
		}
		return lfResult;
	}
	
	/**
	 * 查询企业有功功率表格数据
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> assessmentPowerDatas(long ledgerId,Date beginTime,Date endTime){
		List<Map<String, Object>> maps = this.anShanMessageMapper.queryPowerDatas( ledgerId, beginTime, endTime );
		return maps;
	}
	
	/**
	 * 查询企业有功功率曲线数据
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> assessmentPowerLineDatas(long ledgerId,Date beginTime,Date endTime){
		List<Map<String,Object>> maps = this.anShanMessageMapper.queryPowerLineDatas( ledgerId, beginTime, endTime );
		return maps;
	}
	
	
	
	/**
	 * 处理拼装谐波数据
	 * @param ledgerId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private Map<String,Object> assessmentHar(long ledgerId,Date beginDate,Date endDate,int curveType){
		Map<String,Object> harResult = new HashMap<>( 0 );//负载率曲线的最终结果集(以meterName作为key)
		List<Map<String,Object>> meterIds = this.anShanMessageMapper.queryLedgerMeter( ledgerId );
		int betweenDates = DateUtil.daysBetweenDates( endDate, beginDate );
		double harThreshold = WebConstant.harThreshold;
		for ( Map<String,Object> meter: meterIds ) {
			Integer volLevel = eleAssessmentMapper.getMeterVolLevel(Long.parseLong( meter.get( "METER_ID" ).toString() ));
			if (volLevel != null) {
				if (volLevel.intValue() == 1)
					volLevel = 380;
				else if (volLevel.intValue() == 3)
					volLevel = 6;
				else if (volLevel.intValue() == 20)
					volLevel = 10;
				else if (volLevel.intValue() > 110)
					volLevel = 110;
			}
			Date endTime = beginDate;
			Date beginTime = beginDate;
			for (int i = 0 ; i < betweenDates ; i++) {
				boolean flag = false;
				List<Map<String, Object>> hars = this.anShanMessageMapper.getDayHarChartDatas(curveType,Long.parseLong( meter.get( "METER_ID" ).toString() ),beginTime,endTime);
				if( hars != null && hars.size() > 0 && curveType == 1){
					/**
					 * 遍历所有数据和电流标准值对比,大于电流标准值则判定为不满足
					 */
					for (Map<String, Object> har:hars) {
						if( har == null )
							continue;
						if (!har.get("num").toString().equals( "0" )) {
							Double harIStand = HarConstant.getHarIStand( volLevel, Integer.parseInt( har.get( "num" ).toString() ));	//谐波电流标准值
							double harIstand = harIStand*(1+harThreshold);// 专门用来比较用
							if (har.containsKey( "a_max" ) &&  har.get("a_max") != null && Double.valueOf(har.get("a_max").toString()) > harIstand) {
								har.put("a_max",har.get( "a_max" )+"_1");
								har.put( "harIStand",harIStand );
								flag = true;
							}
							if (har.containsKey( "b_max" ) && har.get("b_max") != null && Double.valueOf(har.get("b_max").toString()) > harIstand) {
								har.put("b_max",har.get( "b_max" )+"_1");
								har.put( "harIStand",harIStand );
								flag = true;
							}
							if (har.containsKey( "c_max" ) && har.get("c_max") != null && Double.valueOf(har.get("c_max").toString()) > harIstand) {
								har.put("c_max",har.get( "c_max" )+"_1");
								har.put( "harIStand",harIStand );
								flag = true;
							}
						}
					}
					if(flag){
						harResult.put( hars.get( 0 ).get( "meterName" ).toString()+","+hars.get( 0 ).get( "dataTime" ).toString(),hars );
					}
				}
				
				if( hars != null && hars.size() > 0 && curveType == 2) {
					for (Map<String, Object> har:hars) {
						if (!har.get("num").toString().equals( "0" )) {
							Double harVolStand = HarConstant.getHarVolStand( volLevel );//谐波电压标准值
							double harVstand = harVolStand*(1+harThreshold); // 比较用
							if (har.containsKey( "a_max" ) && har.get( "a_max" ) != null && Double.valueOf( har.get( "a_max" ).toString() ) > harVstand ) {
								har.put("a_max",har.get( "a_max" )+"_1");
								har.put( "harVolStand",harVolStand );
								flag = true;
							}
							if (har.containsKey( "b_max" ) && har.get( "b_max" ) != null && Double.valueOf( har.get( "b_max" ).toString() ) > harVstand ) {
								har.put("b_max",har.get( "b_max" )+"_1");
								har.put( "harVolStand",harVolStand );
								flag = true;
							}
							if (har.containsKey( "c_max" ) && har.get( "c_max" ) != null && Double.valueOf( har.get( "c_max" ).toString() ) > harVstand ) {
								har.put("c_max",har.get( "c_max" )+"_1");
								har.put( "harVolStand",harVolStand );
								flag = true;
							}
						}
					}
					if(flag)
						harResult.put( hars.get( 0 ).get( "meterName" ).toString()+","+hars.get( 0 ).get( "dataTime" ).toString(),hars );
				}
				endTime = DateUtil.getNextDayDate(endTime);
				beginTime = DateUtil.getNextDayDate(beginTime);
			}
		}
		return harResult;
	}
	
	/**
	 * 请求能效分析,日不平衡度分析数据
	 * @param reportId
	 * @param accountId
	 * @return
	 */
	@Override
	public Map<String, Object> queryReportInfoData2(long reportId, long accountId) {
		Map<String, Object> result = new HashMap<String, Object>();
		ServiceReportBean bean = this.anShanMessageMapper.queryReportInfoById( reportId );
		Long ledgerId = bean.getLedgerId();
		Date createDate = bean.getCreateTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime( createDate );
		cal.add( Calendar.MONTH, -1 );
		Date baseDate = cal.getTime();
		//获取本月第一天
		Date startDate = DateUtil.getCurrMonthFirstDay( baseDate );
		//获取本月最后
		Date endDate = DateUtil.getMonthLastDay( baseDate );
		//获取上月第一天
		cal.add( Calendar.MONTH, -1 );
		Date lastMonth = cal.getTime();
		Date startDate2 = DateUtil.getCurrMonthFirstDay( lastMonth );
		Date endDate2 = DateUtil.getMonthLastDay( lastMonth );
		
		try {
			// 行业能效情况-->包含了当前企业的电耗,电费平均值
			Map<String, Object> industryEData = this.assessmentIndustryEE( ledgerId, startDate, endDate );
			result.put( "industryEData",industryEData );
			
			// 企业设备能效情况-->包含了柱状图和表格数据
			List<Map<String, Object>> meterEDatas = this.assessmentMeterEE( ledgerId, startDate, endDate );
			result.put( "meterEDatas",meterEDatas );
			
			//查询企业下各表负载率曲线
			Map<String, Object> loadData = this.aassessmentLoadData( ledgerId, startDate, endDate );
			Map<String, Object> newLoadData = MapKeyComparator.sortMapByKeyForChartItem( loadData );
			result.put( "loadData",newLoadData );
		} catch (Exception e) {
			Log.error( "method:queryReportInfoData2出错"+e.getMessage() );
		}
		
		return result;
	}
	
	
	
	
	/**
	 * 处理服务报告里能效分析数据的方法(行业能效情况)
	 * @param ledgerId		能管对象ID
	 * @param beginDate		开始时间
	 * @param endDate		结束时间
	 * @return
	 */
	private Map<String,Object> assessmentIndustryEE(long ledgerId,Date beginDate,Date endDate){
		Map<String,Object> result = new HashMap<>( 0 );
		// add or update method by catkins.
		// date 2020/12/3
		// Modify the content:查询能耗分析行业平均值,最大值(行业最差),最小值(行业最优)
		if( phoneMapper.queryLedgerMaxPWIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryLedgerMaxPWIndustryData(1l,beginDate,endDate) );
		if( phoneMapper.queryLedgerMinPWIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryLedgerMinPWIndustryData(1l,beginDate,endDate) );
		if( phoneMapper.queryLedgerMaxFeeIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryLedgerMaxFeeIndustryData(1l,beginDate,endDate) );
		if( phoneMapper.queryLedgerMinFeeIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryLedgerMinFeeIndustryData(1l,beginDate,endDate) );
		if( phoneMapper.queryLedgerAvgIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryLedgerAvgIndustryData(1l,beginDate,endDate) );
		//end
		
		// add or update method by catkins.
		// date 2020/12/3
		// Modify the content:查询能耗分析企业平均值
		result.put( "ledgerPW",0 );
		result.put( "ledgerFEE",0 );
		if( phoneMapper.queryLedgerAvgIndustryData(ledgerId,beginDate,endDate) != null ){
			Map<String, Object> ledgerAvg = phoneMapper.queryLedgerAvgIndustryData( ledgerId, beginDate, endDate );
			result.put( "ledgerPW",ledgerAvg.get( "AVGPW" ) );
			result.put( "ledgerFEE",ledgerAvg.get( "AVGFEE" ) );
		}
		//end
		
		// add or update method by catkins.
		// date 2020/12/3
		// Modify the content:查询能耗分析所有设备平均值,最大值,最小值(同上)
		if( phoneMapper.queryMeterMaxPWIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryMeterMaxPWIndustryData(1l,beginDate,endDate) );
		if( phoneMapper.queryMeterMinPWIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryMeterMinPWIndustryData(1l,beginDate,endDate) );
		if( phoneMapper.queryMeterMaxFeeIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryMeterMaxFeeIndustryData(1l,beginDate,endDate) );
		if( phoneMapper.queryMeterMinFeeIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryMeterMinFeeIndustryData(1l,beginDate,endDate) );
		if( phoneMapper.queryMeterAvgIndustryData(1l,beginDate,endDate) != null )
			result.putAll( phoneMapper.queryMeterAvgIndustryData(1l,beginDate,endDate) );
		//end
		
		// add or update method by catkins.
		// date 2020/12/3
		// Modify the content:查询能耗分析企业平均值
		result.put( "meterMAXPW",0 );
		result.put( "meterMAXName","-" );
		result.put( "meterMINPW",0 );
		result.put( "meterMINName","-" );
		if( phoneMapper.queryMeterMaxPWIndustryData(ledgerId,beginDate,endDate) != null ){
			Map<String, Object> map = phoneMapper.queryMeterMaxPWIndustryData( ledgerId, beginDate, endDate );
			result.put( "meterMAXName",map.get( "MMAXPWMETERNAME" ) );
			result.put("meterMAXPW",map.get( "MMAXPW" ));
		}
		
		if( phoneMapper.queryMeterMinPWIndustryData(ledgerId,beginDate,endDate) != null ){
			Map<String, Object> map = phoneMapper.queryMeterMinPWIndustryData( ledgerId, beginDate, endDate );
			result.put( "meterMINName",map.get( "MMINPWMETERNAME" )  );
			result.put("meterMINPW",map.get( "MMINPW" ));
		}
		
		//end
		return result;
	}
	
	
	/**
	 * 处理服务报告里能效分析数据的方法(企业能效情况)
	 * @return
	 */
	private List<Map<String, Object>> assessmentMeterEE(long ledgerId,Date beginDate,Date endDate){
		List<Map<String, Object>> maps = this.anShanMessageMapper.queryEnergyData4Parent( ledgerId, beginDate, endDate,-1 );
		return maps;
	}
	
	/**
	 * 处理负载率曲线数据
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private Map<String,Object> aassessmentLoadData(long ledgerId,Date beginTime,Date endTime){
		Map<String,Object> result = new HashMap<>( 0 );
		Map<String,Object> loadMap = null;
		List<Map<String,Object>> meterIds = this.anShanMessageMapper.queryLedgerMeter( ledgerId );
		for (Map<String,Object> meter: meterIds) {
			Long meterId = Long.parseLong( meter.get( "METER_ID" ).toString() );
			
			loadMap = new HashMap<>( 0 );
			// 查询负载率曲线
			List<Map<String, Object>> loadData = this.anShanMessageMapper.getLoadData( meterId, beginTime, endTime );
			// 查询负载率时间曲线
			List<Map<String, Object>> loadTimeData = this.anShanMessageMapper.getLoadTimeData( meterId, beginTime, endTime );
			// 电压不平衡曲线
			List<Map<String, Object>> vuMaxData = schedulingMapper.getVUMaxData( meterId, beginTime, endTime );
			// 电压不平衡时间曲线
			List<Map<String,Object>> vuMaxTimeData = schedulingMapper.getVUMaxTimeData( meterId,beginTime,endTime );
			// 电压不平衡日累计时间曲线
			List<Map<String, Object>> vuLimitData = schedulingMapper.getVULimitData( meterId, beginTime, endTime );
			// 电流不平衡曲线
			List<Map<String, Object>> iuMaxData = schedulingMapper.getIUMaxData( meterId, beginTime, endTime );
			// 电流不平衡时间曲线
			List<Map<String, Object>> iuMaxTimeData = schedulingMapper.getIUMaxTimeData( meterId, beginTime, endTime );
			// 电流不平衡日累计时间曲线
			List<Map<String, Object>> iuLimitData = schedulingMapper.getIULimitData( meterId, beginTime, endTime );
			
			if( loadData != null && loadData.size() > 0 )
				loadMap.put( "loadData",loadData );
			if( loadTimeData != null && loadTimeData.size() > 0 )
				loadMap.put( "loadTimeData",loadTimeData );
			if( vuMaxData != null && vuMaxData.size() > 0 )
				loadMap.put( "vuMaxData",vuMaxData );
			if( vuMaxTimeData != null && vuMaxTimeData.size() > 0 )
				loadMap.put( "vuMaxTimeData",vuMaxTimeData );
			if( vuLimitData != null && vuLimitData.size() > 0 )
				loadMap.put( "vuLimitData",vuLimitData );
			if( iuMaxData != null && iuMaxData.size() > 0 )
				loadMap.put( "iuMaxData",iuMaxData );
			if( iuMaxTimeData != null && iuMaxTimeData.size() > 0 )
				loadMap.put( "iuMaxTimeData",iuMaxTimeData );
			if( iuLimitData != null && iuLimitData.size() > 0 )
				loadMap.put( "iuLimitData",iuLimitData );
			
			result.put( meter.get( "METER_NAME" ).toString() ,loadMap );
		}
		return result;
	}
	
	
	
	
}
