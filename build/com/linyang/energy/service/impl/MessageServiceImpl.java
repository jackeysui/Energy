package com.linyang.energy.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.service.CostService;
import com.linyang.energy.service.PhoneService;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.EventRelatedNodeTreeBean;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.IndexMapper;
import com.linyang.energy.mapping.authmanager.UserBeanMapper;
import com.linyang.energy.mapping.message.MessageMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.EventSettingRecBean;
import com.linyang.energy.model.MsgHisBean;
import com.linyang.energy.model.ServiceReportBean;
import com.linyang.energy.model.SubscriberBean;
import com.linyang.energy.model.SuggestBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.MessageService;

import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.JPushUtil;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;

/**
 * 信息订阅接口实现类
 *
 * @description:
 * @author:gaofeng
 * @date:2015.08.19
 */
@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageMapper messageMapper;
	
	@Autowired
	private IndexMapper indexMapper;
	
	@Autowired
	private PhoneService phoneService;
	
	@Autowired
	private UserBeanMapper userBeanMapper;
	
	@Autowired
	private MeterManagerMapper meterManagerMapper;
	
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	
	@Autowired
	private CostService costService;
	
	@Override
	public List<Long> getBookAccountId(long bookId, int bookType) {
		return messageMapper.getBookAccountId( bookId, bookType );
	}
	
	@Override
	public List<Long> getBookAccountIdByLedger(long ledgerId, long bookId, int bookType) {
		return messageMapper.getBookAccountIdByLedger( ledgerId, bookId, bookType );
	}
	
	@Override
	public List<Long> getBookAccountIdByMeter(long meterId, long bookId, int bookType) {
		return messageMapper.getBookAccountIdByMeter( meterId, bookId, bookType );
	}
	
	@Override
	public void saveBatchSubscriptionInfo(List<SubscriberBean> subscriber, List<Integer> eventId, List<Integer> infoId, int type) {
		List<Long> userIds = getSubscriberUsers( subscriber );
		if (userIds.size() == 0)
			return;
		
		List<String> strUserIds = new ArrayList<String>();
		for (Long userId : userIds) {
			strUserIds.add( userId.toString() );
			if (type == 3)
				messageMapper.deleteUserBookInfo( userId );
			
			for (Integer event : eventId) {
				if (type == 1) { // 追加选中订阅
					messageMapper.mergeBookInfo( SequenceUtils.getDBSequence(), userId, event, 1 );
				} else if (type == 2) { // 删除选中订阅
					messageMapper.deleteOneBookInfo( userId, event, 1 );
				} else if (type == 3) { // 覆盖订阅
					messageMapper.mergeBookInfo( SequenceUtils.getDBSequence(), userId, event, 1 );
				}
			}
			
			for (Integer info : infoId) {
				if (type == 1) { // 追加选中订阅
					messageMapper.mergeBookInfo( SequenceUtils.getDBSequence(), userId, info, 2 );
				} else if (type == 2) { // 删除选中订阅
					messageMapper.deleteOneBookInfo( userId, info, 2 );
				} else if (type == 3) { // 覆盖订阅
					messageMapper.mergeBookInfo( SequenceUtils.getDBSequence(), userId, info, 2 );
				}
			}
		}
		
		// 发送tag变更通知手机
		Map<String, String> para = new HashMap<String, String>();
		para.put( "type", "tagChanged" );
		para.put( "triggerType", "1" );
		JPushUtil.sendPushByAlias( strUserIds, "", "", para );
	}
	
	@Override
	public List<Map<String, Object>> getUserBookInfo(long accountId) {
		return messageMapper.getUserBookInfo( accountId );
	}
	
	@Override
	public void saveSubscriptionInfo(long accountId, List<Integer> eventId, List<Integer> infoId) {
		messageMapper.deleteUserBookInfo( accountId );
		for (Integer event : eventId) {
			messageMapper.insertBookInfo( SequenceUtils.getDBSequence(), accountId, event, 1 );
		}
		for (Integer info : infoId) {
			messageMapper.insertBookInfo( SequenceUtils.getDBSequence(), accountId, info, 2 );
		}
		
		// 发送tag变更通知手机
		List<String> strUserIds = new ArrayList<String>();
		strUserIds.add( String.valueOf( accountId ) );
		Map<String, String> para = new HashMap<String, String>();
		para.put( "type", "tagChanged" );
		// 判断用户是否在屏蔽时间段内,拼接接收规则,变更通知立即推送
		para.put( "triggerType", "1" );
		JPushUtil.sendPushByAlias( strUserIds, "", "", para );
	}
	
	@Override
	public void saveDefinedMsg(List<SubscriberBean> subscriber, String title, String conUrl) {
		List<Long> userIds = getSubscriberUsers( subscriber );
		
		long msgId = SequenceUtils.getDBSequence();
		messageMapper.insertDefinedMsg( msgId, new Date(), 1, title, conUrl );
		if (userIds.size() > 0) {
			for (long userId : userIds)
				messageMapper.insertDefinedMsgAccount( msgId, userId );
		} else
			messageMapper.insertDefinedMsgAccount( msgId, -1 );
		
		// 发送消息给手机
		if (userIds.size() == 0) {
			userIds = messageMapper.getBookAccountId( 1, 2 );
			if (userIds == null || userIds.size() == 0)
				return;
		}
		List<String> strUserIds = new ArrayList<String>();
		for (Long userId : userIds)
			strUserIds.add( userId.toString() );
		Map<String, String> para = new HashMap<String, String>();
		para.put( "msgId", String.valueOf( msgId ) );
		para.put( "A", conUrl );
		// 判断是否在屏蔽时间段内，拼接接收规则
		for (String userId : strUserIds) {
		para.put( "type", "definedMsg" );
			List<String> alias = new ArrayList<String>();
			alias.add( userId );
			UserBean userBean = userBeanMapper.getUserByAccountId( Long.parseLong( userId ) );
			if (JPushUtil.checkSendEnable( userBean.getFreeTimePeriod(), userBean.getIsShield() )) { // 立即推送
				para.put( "triggerType", "1" );
				JPushUtil.sendPushByAlias( alias, title, "您有一条新的消息！", para );
			} else { // 不推送,仅入库等待定时推送
				messageMapper.insertMsgHis( Long.parseLong( userId ), msgId, new Date(), 1 );
			}
		}
		//// 检查这些user所接收到的消息是否达到配置的条数
		this.phoneService.checkUserMessage( null );
	}
	
	@Override
	public void saveDefinedMsg(Long accountId, String title, String conUrl) {
		Long userId = accountId;
		
		long msgId = SequenceUtils.getDBSequence();
		messageMapper.insertDefinedMsg( msgId, new Date(), 1, title, conUrl );
		
		messageMapper.insertDefinedMsgAccount( msgId, userId );
		
		Map<String, String> para = new HashMap<String, String>();
		para.put( "type", "definedMsg" );
		para.put( "msgId", String.valueOf( msgId ) );
		para.put( "A", conUrl );
		
		// 判断是否在屏蔽时间段内，拼接接收规则
		List<String> alias = new ArrayList<String>();
		alias.add( userId.toString() );
		UserBean userBean = userBeanMapper.getUserByAccountId( userId );
		if (JPushUtil.checkSendEnable( userBean.getFreeTimePeriod(), userBean.getIsShield() )) { // 立即推送
			para.put( "triggerType", "1" );
			JPushUtil.sendPushByAlias( alias, title, "您的反馈得到回复！", para );
		} else { // 不推送,仅入库等待定时推送
			messageMapper.insertMsgHis( userId, msgId, new Date(), 1 );
		}
		
		//// 检查这些user所接收到的消息是否达到配置的条数
		this.phoneService.checkUserMessage( null );
	}
	
	@Override
	public List<Long> getSubscriberUsers(List<SubscriberBean> subscriber) {
		Set<Long> userIds = new HashSet<Long>();
		List<Long> ids = new ArrayList<Long>();
		if (subscriber != null && subscriber.size() > 0) {
			for (SubscriberBean sb : subscriber) {
				if (sb.getType() == 1 && ids != null) {
					ids = messageMapper.getAccountIdByGroup( sb.getObjId() );
				} else if (sb.getType() == 2 && ids != null) {
					ids = messageMapper.getAccountIdByLedger( sb.getObjId() );
				} else if (sb.getType() == 3 && ids != null) {
					ids.clear();
					ids.add( sb.getObjId() );
				}
				if (ids != null && ids.size() > 0)
					userIds.addAll( ids );
			}
		}
		return new ArrayList<Long>( userIds );
	}
	
	@Override
	public void saveNewsInfo(String title, int type, String picUrl, String conUrl, String content) {
		long infoId = SequenceUtils.getDBSequence();
		messageMapper.insertNewsInfo( infoId, title, type, picUrl, conUrl, new Date() );
		
		String tag = "policy";
		String t = "政策";
		if (type == 1) {
			tag = "news";
			t = "新闻";
		}
		//String con = content.length() > 20 ? content.substring(0, 20) : content;
		Map<String, String> para = new HashMap<String, String>();
		para.put( "type", tag );
		para.put( "msgId", String.valueOf( infoId ) );
		para.put( "A", picUrl );
		para.put( "B", conUrl );
		// 判断是否在屏蔽时间段内，拼接接收规则
		List<Long> userIds = phoneService.getAllCompAccount( "user" );
		for (Long userId : userIds) {
			List<String> alias = new ArrayList<String>();
			alias.add( userId.toString() );
			UserBean userBean = userBeanMapper.getUserByAccountId( userId );
			if (JPushUtil.checkSendEnable( userBean.getFreeTimePeriod(), userBean.getIsShield() )) { // 立即推送
				para.put( "triggerType", "1" );
				JPushUtil.sendPushByAlias( alias, t, title, para );
			} else {
				messageMapper.insertMsgHis( userId, infoId, new Date(), 2 );
			}
		}
		//// 检查这些user所接收到的消息是否达到配置的条数
		this.phoneService.checkUserMessage( null );
	}
	
	@Override
	public void saveReportInfo(long ledgerId, Date baseDate, String content) {
		long infoId = SequenceUtils.getDBSequence();
		messageMapper.insertReportInfo( infoId, ledgerId, baseDate, 1L, content, new Date(), 1 );
		
		// 发送消息给手机
		List<Long> userIds = messageMapper.getBookAccountIdByLedger( ledgerId, 1, 2 );
		if (userIds == null || userIds.size() == 0)
			return;
		List<String> strUserIds = new ArrayList<String>();
		for (Long userId : userIds)
			strUserIds.add( userId.toString() );
		Map<String, String> para = new HashMap<String, String>();
		para.put( "type", "report" );
		para.put( "msgId", String.valueOf( infoId ) );
		// 判断是否在屏蔽时间段内，拼接接收规则
		for (String userId : strUserIds) {
			List<String> alias = new ArrayList<String>();
			alias.add( userId );
			UserBean userBean = userBeanMapper.getUserByAccountId( Long.parseLong( userId ) );
			if (JPushUtil.checkSendEnable( userBean.getFreeTimePeriod(), userBean.getIsShield() )) { // 立即推送
				para.put( "triggerType", "1" );
				JPushUtil.sendPushByAlias( alias, DateUtil.getDateMonth( baseDate ) + "月服务报告", "尊敬的用户，服务报告已生成，请查收！", para );
			} else {
				messageMapper.insertMsgHis( Long.parseLong( userId ), infoId, new Date(), 3 );
			}
			
		}
		//// 检查这些user所接收到的消息是否达到配置的条数
		this.phoneService.checkUserMessage( null );
	}
	
	@Override
	public Map<String, Object> queryReportInfoData(long ledgerId, Date baseDate) {
		Date preDate7 = DateUtil.getDateBetween( baseDate, -7 );
		Date preDate30 = DateUtil.getDateBetween( baseDate, -30 );
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 最近7天用电量
		Map<String, Object> coul7 = messageMapper.queryLedgerCoul( ledgerId, preDate7, baseDate );
		processLedgerCoul( coul7, map, "7" );
		
		// 最近30天用电量
		Map<String, Object> coul30 = messageMapper.queryLedgerCoul( ledgerId, preDate30, baseDate );
		processLedgerCoul( coul30, map, "30" );
		
		Double maxPwr = indexMapper.queryLedgerMaxPwr( ledgerId, preDate30, baseDate );
		if (maxPwr != null) {
			map.put( "maxPwr", maxPwr );
			map.put( "occuredTime", DateUtil.convertDateToStr( indexMapper.getLedgerMaxTime( ledgerId, preDate30, baseDate, maxPwr ),
					DateUtil.MOUDLE_PATTERN ) );
		}
		
		map.put( "maxI", indexMapper.queryLedgerMaxI( ledgerId, preDate30, baseDate ) );
		
		Map<String, Object> vol = indexMapper.queryLedgerMaxMinVol( ledgerId, preDate30, baseDate );
		if (vol != null && vol.size() > 0) {
			map.put( "maxv", Double.valueOf( vol.get( "MAXV" ).toString() ) );
			map.put( "minv", Double.valueOf( vol.get( "MINV" ).toString() ) );
		}
		
		return map;
	}
	
	private void processLedgerCoul(Map<String, Object> coul, Map<String, Object> result, String mark) {
		double q, rq, pf;
		if (coul != null && coul.size() > 0) {
			if (coul.get( "Q" ) != null) {
				q = Double.parseDouble( coul.get( "Q" ).toString() );
				result.put( "q" + mark, q );
				if (coul.get( "RQ" ) != null) {
					rq = Double.parseDouble( coul.get( "RQ" ).toString() );
					pf = DataUtil.getPF( q, rq );
					if (pf != 0) {
						result.put( "pf" + mark, pf );
					}
				}
			}
		}
	}
	
	@Override
	public Map<String, Object> queryReportInfoData2(long reportId, long accountId) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		ServiceReportBean bean = this.messageMapper.queryReportInfoById( reportId );
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
		//计算电费
		Map<String, Object> fee = costService.calEmoDcpEleFee( feeMap );
		result.put( "fee", fee );
		
		//各厂区用电量
		List<Map<String, Object>> childList = this.indexMapper.getTotal11Data( curparam );
		result.put( "childList", childList );
		
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
		
		return result;
	}
	
	@Override
	public String getReportTips(long reportId, long accountId) {
		String tips = " ";
		ServiceReportBean bean = this.messageMapper.queryReportInfoById( reportId );
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
	
	@Override
	public String getLedgerName(long ledgerId) {
		return messageMapper.getLedgerName( ledgerId );
	}
	
	@Override
	public String getMeterName(long meterId) {
		return messageMapper.getMeterName( meterId );
	}
	
	@Override
	public void saveAutoReminderSet(String nologin, String news, String unRead) {
		messageMapper.deleteAutoReminderSet();
		messageMapper.saveAutoReminderSet( nologin, news, unRead );
	}
	
	@Override
	public Map<String, Object> getAutoReminderSet() {
		return messageMapper.getAutoReminderSet();
	}
	
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.MessageService#insertBlockRec(long, long, java.util.Date)
	 */
	@Override
	public void insertMsgHis(long accountId, long msgId, Date createTime, int type) {
		messageMapper.insertMsgHis( accountId, msgId, createTime, type );
	}
	
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.MessageService#getMsgHisList(long)
	 */
	@Override
	public List<MsgHisBean> getMsgHisList(long accountId) {
		return messageMapper.getMsgHisList( accountId );
	}
	
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.MessageService#getMsgInfo(long, int)
	 */
	@Override
	public Map<String, Object> getMsgInfo(long msgId, int type) {
		return messageMapper.getMsgInfo( msgId, type );
	}
	
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.MessageService#deleteMsgHis(java.lang.Long, java.lang.Long, java.util.Date)
	 */
	@Override
	public void deleteMsgHis(Long userId, Long msgId) {
		messageMapper.deleteMsgHis( userId, msgId );
	}
	
	@Override
	public List<SuggestBean> querySuggestPageList(Map<String, Object> param) {
		List<SuggestBean> suggestBeans = messageMapper.querySuggestPageList( param );
		
		return suggestBeans;
	}
	
	@Override
	public boolean updateReply(Map<String, Object> param) {
		boolean isSuccess = false;
		if (param != null) {
			messageMapper.updateReply( param );
			isSuccess = true;
		}
		return isSuccess;
	}
	
	/**
	 * 得到用户反馈的Excel
	 *
	 * @param 参数 table名字sheetName，输出流output，结果集map
	 * @throws Exception
	 */
	public void getSugExcel(String sheetName, OutputStream output, List<SuggestBean> sugList) {
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		//生成一个表格
		HSSFSheet sheet = wb.createSheet( "用户反馈信息" );
		//设置默认宽度为25字节
		sheet.setDefaultColumnWidth( 25 );
		
		HSSFCellStyle titlestyle = wb.createCellStyle();
		titlestyle.setFillPattern( HSSFCellStyle.SOLID_FOREGROUND );
		titlestyle.setFillForegroundColor( HSSFColor.PALE_BLUE.index );
		titlestyle.setVerticalAlignment( HSSFCellStyle.VERTICAL_CENTER );
		titlestyle.setAlignment( HSSFCellStyle.ALIGN_CENTER );
		titlestyle.setRightBorderColor( HSSFColor.BLACK.index );
		titlestyle.setBorderRight( HSSFCellStyle.BORDER_THIN );
		titlestyle.setLeftBorderColor( HSSFColor.BLACK.index );
		titlestyle.setBorderLeft( HSSFCellStyle.BORDER_THIN );
		titlestyle.setTopBorderColor( HSSFColor.BLACK.index );
		titlestyle.setBorderTop( HSSFCellStyle.BORDER_THIN );
		titlestyle.setBottomBorderColor( HSSFColor.BLACK.index );
		titlestyle.setBorderBottom( HSSFCellStyle.BORDER_THIN );
		HSSFFont font = wb.createFont();
		font.setColor( HSSFColor.WHITE.index );
		titlestyle.setFont( font );
		
		//生成一个表头样式
//		HSSFCellStyle style = wb.createCellStyle();
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		//生成一个表头字体
//		HSSFFont font =wb.createFont();
//		font.setFontHeightInPoints((short) 12);
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//
//		//把字体应用到当前的样式
//		style.setFont(font);
		
		//生成并设置另一个表格内容样式
		HSSFCellStyle styleAno = wb.createCellStyle();
		styleAno.setRightBorderColor( HSSFColor.BLACK.index );
		styleAno.setBorderRight( HSSFCellStyle.BORDER_THIN );
		styleAno.setLeftBorderColor( HSSFColor.BLACK.index );
		styleAno.setBorderLeft( HSSFCellStyle.BORDER_THIN );
		styleAno.setTopBorderColor( HSSFColor.BLACK.index );
		styleAno.setBorderTop( HSSFCellStyle.BORDER_THIN );
		styleAno.setBottomBorderColor( HSSFColor.BLACK.index );
		styleAno.setBorderBottom( HSSFCellStyle.BORDER_THIN );
		styleAno.setAlignment( HSSFCellStyle.ALIGN_CENTER );
		
		
		//生成另一个字体
		HSSFFont fontAno = wb.createFont();
		fontAno.setBoldweight( HSSFFont.BOLDWEIGHT_NORMAL );
		styleAno.setFont( fontAno );
		
		//生成表头，也就是第一行
		HSSFRow row = sheet.createRow( 1 );
		HSSFCell cellA = row.createCell( 0 );
		HSSFCell cellB = row.createCell( 1 );
		HSSFCell cellC = row.createCell( 2 );
		HSSFCell cellD = row.createCell( 3 );
		HSSFCell cellE = row.createCell( 4 );
		HSSFCell cellF = row.createCell( 5 );
		HSSFCell cellG = row.createCell( 6 );
		HSSFCell cellH = row.createCell( 7 );
		cellA.setCellStyle( titlestyle );
		cellB.setCellStyle( titlestyle );
		cellC.setCellStyle( titlestyle );
		cellD.setCellStyle( titlestyle );
		cellE.setCellStyle( titlestyle );
		cellF.setCellStyle( titlestyle );
		cellG.setCellStyle( titlestyle );
		
		cellA.setCellValue( "时间" );
		cellB.setCellValue( "用户" );
		cellC.setCellValue( "企业名" );
		cellD.setCellValue( "角色类型" );
		cellE.setCellValue( "联系方式" );
		cellF.setCellValue( "意见" );
		cellG.setCellValue( "回复" );
		cellH.setCellValue( "" );
		
		//excel表格内容填充
		int i = 2;
		
		for (SuggestBean bean : sugList) {
			HSSFRow row1 = sheet.createRow( i );
			HSSFCell cell1A = row1.createCell( 0 );
			HSSFCell cell1B = row1.createCell( 1 );
			HSSFCell cell1C = row1.createCell( 2 );
			HSSFCell cell1D = row1.createCell( 3 );
			HSSFCell cell1E = row1.createCell( 4 );
			HSSFCell cell1F = row1.createCell( 5 );
			HSSFCell cell1G = row1.createCell( 6 );
			HSSFCell cell1H = row1.createCell( 7 );
			cell1A.setCellStyle( styleAno );
			cell1B.setCellStyle( styleAno );
			cell1C.setCellStyle( styleAno );
			cell1D.setCellStyle( styleAno );
			cell1E.setCellStyle( styleAno );
			cell1F.setCellStyle( styleAno );
			cell1G.setCellStyle( styleAno );
			
			cell1A.setCellValue( bean.getSubmitDateStr() );
			cell1B.setCellValue( bean.getSubmitUser() );
			cell1C.setCellValue( bean.getSubmitLedger() );
			cell1D.setCellValue( bean.getSubmitRole() );
			cell1E.setCellValue( bean.getContactWay() == null ? "无" : bean.getContactWay() );
			cell1F.setCellValue( bean.getSugMsg() );
			cell1G.setCellValue( bean.getSugReply() == null ? "暂无" : bean.getSugReply() );
			cell1H.setCellValue( "" );
			i++;
		}
		try {
			output.flush();
			wb.write( output );
			output.close();
		} catch (IOException e) {
			Log.info( "getSugExcel error IOException" );
		}
	}
	
	@Override
	public List<SuggestBean> querySuggestList() {
		List<SuggestBean> suggestBeans = messageMapper.querySuggestList();
		
		return suggestBeans;
	}
	
	@Override
	public List<ServiceReportBean> searchSerivceReport(Map<String, Object> param) {
		return messageMapper.searchSerivceReportPageList( param );
	}
	
	@Override
	public List<EventSettingRecBean> getEventSettingRecPageData(Page page,
																Map<String, Object> queryMap) {
		queryMap.put( Dialect.pageNameField, page );
		List<EventSettingRecBean> eRecBeans = messageMapper.getEventSettingRecPageData( queryMap );
		if (eRecBeans != null) {
			for (EventSettingRecBean eventSettingRecBean : eRecBeans) {
				Float alarmValue = 0f;
				if (eventSettingRecBean.getEventTypeId() == 1100 || eventSettingRecBean.getEventTypeId() == 1101 || eventSettingRecBean.getEventTypeId() == 1102) {//功率越安全限;功率越经济限;反向功率越限
					Map<String, Object> thresholdInfo = null;
					if (eventSettingRecBean.getObjType() == 2 && Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 1) {//测量点
						//获取额定功率
						if (meterManagerMapper.getMeterThresholdInfo( eventSettingRecBean.getObjId(), 4l ) != null) {
							thresholdInfo = meterManagerMapper.getMeterThresholdInfo( eventSettingRecBean.getObjId(), 4l );
							eventSettingRecBean.setObjName( thresholdInfo.get( "METERNAME" ).toString() );
						} else if(Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 2){
							MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey( eventSettingRecBean.getObjId() );
							eventSettingRecBean.setObjName( meter.getMeterName() );
							eventSettingRecBean.setRatedValue( " - " );
							eventSettingRecBean.setAlarmValue( " - " );
							continue;
						}
					} else {
						if (ledgerManagerMapper.getLedgerThresholdInfo( eventSettingRecBean.getObjId(), 4l ) != null) {
							thresholdInfo = ledgerManagerMapper.getLedgerThresholdInfo( eventSettingRecBean.getObjId(), 4l );
							eventSettingRecBean.setObjName( thresholdInfo.get( "LEDGERNAME" ).toString() );
						} else {
							LedgerBean ledger = ledgerManagerMapper.selectByLedgerId( eventSettingRecBean.getObjId() );
							eventSettingRecBean.setObjName( ledger.getLedgerName() );
							eventSettingRecBean.setRatedValue( " - " );
							eventSettingRecBean.setAlarmValue( " - " );
							continue;
						}
					}
					if (thresholdInfo != null)
						eventSettingRecBean.setRatedValue( thresholdInfo.get( "THRESHOLDVALUE" ).toString() );
					float ratedValue = Float.parseFloat( thresholdInfo.get( "THRESHOLDVALUE" ).toString() );
					Float alarmPercent = eventSettingRecBean.getAlarmPercent();
					alarmValue = DataUtil.doubleDivide( DataUtil.doubleMultiply( ratedValue, alarmPercent ), 100 ).floatValue();
					eventSettingRecBean.setAlarmValue( String.format( "%.2f", alarmValue ) );
				} else if (eventSettingRecBean.getEventTypeId() == 1103) {//功率因数越限
					if (eventSettingRecBean.getObjType() == 2 && Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 1) {//测量点
						MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey( eventSettingRecBean.getObjId() );
						eventSettingRecBean.setObjName( meter.getMeterName() );
					} else if(Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 2){
						LedgerBean ledger = ledgerManagerMapper.selectByLedgerId( eventSettingRecBean.getObjId() );
						eventSettingRecBean.setObjName( ledger.getLedgerName() );
					}
					eventSettingRecBean.setRatedValue( "1" );
					
					Float alarmPercent = eventSettingRecBean.getAlarmPercent();
					alarmValue = DataUtil.doubleDivide( alarmPercent, 100 ).floatValue();
					eventSettingRecBean.setAlarmValue( String.format( "%.2f", alarmValue ) );
					
				} else if (eventSettingRecBean.getEventTypeId() == 1104 || eventSettingRecBean.getEventTypeId() == 1108) {//电压越安全限 //update 20190523 此事件修改为电压越上限
					Map<String, Object> thresholdInfo1 = null;
					float ratedValue = 0f;
					if (eventSettingRecBean.getObjType() == 2 && Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 1) {
						thresholdInfo1 = meterManagerMapper.getMeterThresholdInfo( eventSettingRecBean.getObjId(), 1l );//获取额定电压
						if (thresholdInfo1 == null) {
							MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey( eventSettingRecBean.getObjId() );
							eventSettingRecBean.setObjName( meter.getMeterName() );
							eventSettingRecBean.setRatedValue( " - " );
							eventSettingRecBean.setAlarmValue( " - " );
							continue;
						} else {
							eventSettingRecBean.setRatedValue( thresholdInfo1.get( "THRESHOLDVALUE" ).toString() );
							ratedValue = Float.parseFloat( thresholdInfo1.get( "THRESHOLDVALUE" ).toString() );
						}
						eventSettingRecBean.setObjName( thresholdInfo1.get( "METERNAME" ).toString() );
					} else {
						continue;
					}
					
					Float alarmPercent = eventSettingRecBean.getAlarmPercent();
					alarmValue = DataUtil.doubleDivide( DataUtil.doubleMultiply( ratedValue, alarmPercent ), 100 ).floatValue();
					eventSettingRecBean.setAlarmValue( String.format( "%.2f", alarmValue ) );
				} else if (eventSettingRecBean.getEventTypeId() == 1105) {//电流越安全限
					Map<String, Object> thresholdInfo2 = null;
					float ratedValue = 0f;
					if (eventSettingRecBean.getObjType() == 2 && Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 1) {
						thresholdInfo2 = meterManagerMapper.getMeterThresholdInfo( eventSettingRecBean.getObjId(), 3l );//获取额定电流
						if (thresholdInfo2 == null) {
							MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey( eventSettingRecBean.getObjId() );
							eventSettingRecBean.setObjName( meter.getMeterName() );
							eventSettingRecBean.setRatedValue( " - " );
							eventSettingRecBean.setAlarmValue( " - " );
							continue;
						} else {
							eventSettingRecBean.setRatedValue( thresholdInfo2.get( "THRESHOLDVALUE" ).toString() );
							ratedValue = Float.parseFloat( thresholdInfo2.get( "THRESHOLDVALUE" ).toString() );
						}
						eventSettingRecBean.setObjName( thresholdInfo2.get( "METERNAME" ).toString() );
					} else {
						continue;
					}
					
					Float alarmPercent = eventSettingRecBean.getAlarmPercent();
					alarmValue = DataUtil.doubleDivide( DataUtil.doubleMultiply( ratedValue, alarmPercent ), 100 ).floatValue();
					eventSettingRecBean.setAlarmValue( String.format( "%.2f", alarmValue ) );
				} else if (eventSettingRecBean.getEventTypeId() == 1106) {//需量报警
					LedgerBean ledger = ledgerManagerMapper.selectByLedgerId( eventSettingRecBean.getObjId() );
					eventSettingRecBean.setObjName( ledger.getLedgerName() );
					Double ratedValue = ledgerManagerMapper.getEmoDemandDecalre( eventSettingRecBean.getObjId(), DateUtil.getCurrMonthFirstDay( new Date() ) );
					
					Float alarmPercent = eventSettingRecBean.getAlarmPercent();
					if (ratedValue != null) {
						eventSettingRecBean.setRatedValue( String.format( "%.2f", ratedValue ) );
						Double alarm = DataUtil.doubleDivide( DataUtil.doubleMultiply( ratedValue, alarmPercent ), 100 );
						eventSettingRecBean.setAlarmValue( String.format( "%.2f", alarm ) );
					} else {
						eventSettingRecBean.setRatedValue( " - " );
						eventSettingRecBean.setAlarmValue( " - " );
					}
				} else if (eventSettingRecBean.getEventTypeId() == 1109 || eventSettingRecBean.getEventTypeId() == 1110
						|| eventSettingRecBean.getEventTypeId() == 1111 || eventSettingRecBean.getEventTypeId() == 1112) {//曲线水量 || 日超配额
					Map<String, Object> thresholdInfo1 = null;
					float ratedValue = 0f;
					if (eventSettingRecBean.getObjType() == 2 && Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 1) {
						thresholdInfo1 = meterManagerMapper.getMeterThresholdInfo( eventSettingRecBean.getObjId(), 5l );//获取水量阀值
						if (thresholdInfo1 == null) {
							MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey( eventSettingRecBean.getObjId() );
							eventSettingRecBean.setObjName( meter.getMeterName() );
							eventSettingRecBean.setRatedValue( " - " );
							eventSettingRecBean.setAlarmValue( " - " );
							continue;
						} else {
							eventSettingRecBean.setRatedValue( thresholdInfo1.get( "THRESHOLDVALUE" ).toString() );
							ratedValue = Float.parseFloat( thresholdInfo1.get( "THRESHOLDVALUE" ).toString() );
						}
						eventSettingRecBean.setObjName( thresholdInfo1.get( "METERNAME" ).toString() );
					} else if(Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 2) {
						if (ledgerManagerMapper.getLedgerThresholdValue( eventSettingRecBean.getObjId(), eventSettingRecBean.getEventTypeId() ) != null) {
							thresholdInfo1 = ledgerManagerMapper.getLedgerThresholdValue( eventSettingRecBean.getObjId(), eventSettingRecBean.getEventTypeId() );
							eventSettingRecBean.setObjName( thresholdInfo1.get( "LEDGERNAME" ).toString() );
							eventSettingRecBean.setRatedValue( thresholdInfo1.get( "THRESHOLDVALUE" ).toString() );
							ratedValue = Float.parseFloat( thresholdInfo1.get( "THRESHOLDVALUE" ).toString() );
						} else {
							LedgerBean ledger = ledgerManagerMapper.selectByLedgerId( eventSettingRecBean.getObjId() );
							eventSettingRecBean.setObjName( ledger.getLedgerName() );
							eventSettingRecBean.setRatedValue( " - " );
							eventSettingRecBean.setAlarmValue( " - " );
							continue;
						}
					}
					Float alarmPercent = eventSettingRecBean.getAlarmPercent();
					alarmValue = DataUtil.doubleDivide( DataUtil.doubleMultiply( ratedValue, alarmPercent ), 100 ).floatValue();
					eventSettingRecBean.setAlarmValue( String.format( "%.2f", alarmValue ) );
				} else if (eventSettingRecBean.getEventTypeId() == 1113) {
					if (eventSettingRecBean.getObjType() == 2 && Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 1) {
						MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey( eventSettingRecBean.getObjId() );
						eventSettingRecBean.setObjName( meter.getMeterName() );
						eventSettingRecBean.setRatedValue( " - " );
						eventSettingRecBean.setAlarmValue( " - " );
					} else if(Integer.parseInt( queryMap.get( "roleType" ).toString() ) == 2) {
						LedgerBean ledger = ledgerManagerMapper.selectByLedgerId( eventSettingRecBean.getObjId() );
						eventSettingRecBean.setObjName( ledger.getLedgerName() );
						eventSettingRecBean.setRatedValue( " - " );
						eventSettingRecBean.setAlarmValue( " - " );
					}
				}
			}
			return eRecBeans;
		} else {
			return new ArrayList<EventSettingRecBean>();
		}
	}
	
	@Override
	public List<Map<String, Object>> getAllEventTypeList() {
		return messageMapper.getAllEventType( null );
	}
	
	@Override
	public List<Map<String, Object>> getEventTypeListByRoleType(Integer roleType) {
		return messageMapper.getAllEventType( roleType );
	}
	
	@Override
	public List<EventRelatedNodeTreeBean> getEventRelatedNodes(long legerId, int treeType, long eventTypeId, HttpServletRequest request) {
		List<EventRelatedNodeTreeBean> eventNodes = messageMapper.getEventRelatedNodes( legerId, treeType, eventTypeId );
		if (CommonMethod.isCollectionNotEmpty( eventNodes )) {
			for (EventRelatedNodeTreeBean node : eventNodes) {
				if (eventTypeId == 1100 || eventTypeId == 1101 || eventTypeId == 1102) {//功率越安全限;功率越经济限;反向功率越限
					Map<String, Object> thresholdInfo = null;
					if (treeType == 1) {
						thresholdInfo = meterManagerMapper.getMeterThresholdInfo( Long.parseLong( node.getId() ), 4l );//获取额定功率
						if (thresholdInfo != null) {
							node.setIsValid( 1 );
						}
					} else {
						thresholdInfo = ledgerManagerMapper.getLedgerThresholdInfo( Long.parseLong( node.getId() ), 4l );//获取额定功率
						if (thresholdInfo != null) {
							node.setIsValid( 1 );
						}
					}
				}
				if (eventTypeId == 1103) {//功率因数越限
					node.setIsValid( 1 );
				}
				if (eventTypeId == 1104 || eventTypeId == 1108) {//电压越安全限 //update 20190523 此事件修改为电压越上限
					Map<String, Object> thresholdInfo = null;
					if (treeType == 1) {
						thresholdInfo = meterManagerMapper.getMeterThresholdInfo( Long.parseLong( node.getId() ), 1l );//获取额定电压
						if (thresholdInfo != null) {
							node.setIsValid( 1 );
						}
					}
				}
				if (eventTypeId == 1105) {//电流越安全限
					Map<String, Object> thresholdInfo = null;
					if (treeType == 1) {
						thresholdInfo = meterManagerMapper.getMeterThresholdInfo( Long.parseLong( node.getId() ), 3l );//获取额定电流
						if (thresholdInfo != null) {
							node.setIsValid( 1 );
						}
					}
				}
				//水,电,气
				if (eventTypeId == 1109 || eventTypeId == 1110 || eventTypeId == 1111 || eventTypeId == 1112) {
					Map<String, Object> thresholdInfo = null;
					if (treeType == 1) {
						thresholdInfo = meterManagerMapper.getMeterThresholdInfo( Long.parseLong( node.getId() ), 5l );//
						if (thresholdInfo != null) {
							node.setIsValid( 1 );
						}
					} else {
						thresholdInfo = ledgerManagerMapper.getLedgerThresholdValue( Long.parseLong( node.getId() ), eventTypeId );//
						if (thresholdInfo != null) {
							node.setIsValid( 1 );
						}
					}
				}
				//停上电事件
				if (eventTypeId == 1113) {
					node.setIsValid( 1 );
				}
				if (CommonMethod.isNotEmpty( node.getIcon() ))
					node.setIcon( request.getContextPath() + node.getIcon() );
			}
		}
		return eventNodes;
	}
	
	@Override
	public Map<String, Object> saveEventSettingData(Map<String, Object> settingInfo) {
		boolean isSuccess = true;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> objWithoutThreshold = new ArrayList<String>();
		long ledgerId = Long.parseLong( settingInfo.get( "ledgerId" ).toString() );
		long eventTypeId = Long.parseLong( settingInfo.get( "eventTypeId" ).toString() );
		int objectType = Integer.parseInt( settingInfo.get( "objectType" ).toString() );
		float alarmPercent = 0;
		if (settingInfo.containsKey( "alarmPercent" )) {
			alarmPercent = Float.parseFloat( settingInfo.get( "alarmPercent" ).toString() );
		}
		
		if (eventTypeId == 1106) { //需量报警
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put( "ledgerId", ledgerId );
			queryMap.put( "eventTypeId", eventTypeId );
			queryMap.put( "objectType", objectType );
			queryMap.put( "objId", ledgerId );
			List<EventSettingRecBean> esrList = messageMapper.getFilteredEventSettingRecData( queryMap );
			if (esrList.size() > 0) {
				EventSettingRecBean eventSettingData = esrList.get( 0 );
				eventSettingData.setAlarmPercent( alarmPercent );
				isSuccess = isSuccess && messageMapper.updateEventSettingData( eventSettingData ) > 0;
			} else {
				EventSettingRecBean eventSettingData = new EventSettingRecBean();
				eventSettingData.setRecId( SequenceUtils.getDBSequence() );
				eventSettingData.setLedgerId( ledgerId );
				eventSettingData.setEventTypeId( eventTypeId );
				eventSettingData.setObjType( objectType );
				eventSettingData.setObjId( ledgerId );
				eventSettingData.setAlarmPercent( alarmPercent );
				isSuccess = isSuccess && messageMapper.insertEventSettingData( eventSettingData ) > 0;
			}
			resultMap.put( "isSuccess", isSuccess );
			return resultMap;
		}
		String objectIds = settingInfo.get( "objectIds" ).toString();
		String[] objectIdArr = objectIds.split( "," );
		for (String objectIdStr : objectIdArr) {
			Long objId = Long.parseLong( objectIdStr );
			
			/**
			 * 检测一下，如果有相同对象的设置，就更新，只保留一条数据
			 * */
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put( "ledgerId", ledgerId );
			queryMap.put( "eventTypeId", eventTypeId );
			queryMap.put( "objectType", objectType );
			queryMap.put( "objId", objId );
			List<EventSettingRecBean> eventList = messageMapper.getFilteredEventSettingRecData( queryMap );
			
			
			if (eventList.size() > 0) {//如果查询得出数据，则说明已经有设置了，更新之
				EventSettingRecBean upEventSettingData = eventList.get( 0 );
				upEventSettingData.setAlarmPercent( alarmPercent );
				isSuccess = isSuccess && messageMapper.updateEventSettingData( upEventSettingData ) > 0;
			} else {
				EventSettingRecBean eventSettingData = new EventSettingRecBean();
				eventSettingData.setRecId( SequenceUtils.getDBSequence() );
				eventSettingData.setLedgerId( ledgerId );
				eventSettingData.setEventTypeId( eventTypeId );
				eventSettingData.setObjType( objectType );
				eventSettingData.setObjId( objId );
				if (eventTypeId != 1113) {
					eventSettingData.setAlarmPercent( alarmPercent );
				}
				isSuccess = isSuccess && messageMapper.insertEventSettingData( eventSettingData ) > 0;
			}


//			if (objectType == 2) {//测量点
//				if (eventTypeId==1100 || eventTypeId==1101 || eventTypeId==1102) {//功率越安全限;功率越经济限;反向功率越限
//					Map<String, Object> thresholdInfo = meterManagerMapper.getMeterThresholdInfo(objId,4l);//获取额定功率
//					if (thresholdInfo == null) {
//                        MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(objId);
//						objWithoutThreshold.add(meter.getMeterName()); 
//						continue;
//					}
//					eventSettingData.setObjName(thresholdInfo.get("METERNAME").toString());
//					float ratedValue = Float.parseFloat(thresholdInfo.get("THRESHOLDVALUE").toString());
//					Float alarmValue = ratedValue*alarmPercent/100;
//					eventSettingData.setAlarmValue(String.format("%.2f", alarmValue));
//				}else if (eventTypeId==1103) {//功率因数越限
//					MeterBean meter = meterManagerMapper.getMeterDataByPrimaryKey(objId);
//					eventSettingData.setObjName(meter.getMeterName());
//					Float alarmValue = alarmPercent/100;
//					eventSettingData.setAlarmValue(String.format("%.2f", alarmValue));
//				}
//			} else {
//				if (eventTypeId==1100 || eventTypeId==1101 || eventTypeId==1102) {//功率越安全限;功率越经济限;反向功率越限
//					Map<String, Object> thresholdInfo = ledgerManagerMapper.getLedgerThresholdInfo(objId,4l);//获取额定功率
//					if (thresholdInfo == null) {
//                        LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(objId);
//						objWithoutThreshold.add(ledger.getLedgerName()); 
//						continue;
//					}
//					eventSettingData.setObjName(thresholdInfo.get("LEDGERNAME").toString());
//					float ratedValue = Float.parseFloat(thresholdInfo.get("THRESHOLDVALUE").toString());
//					Float alarmValue = ratedValue*alarmPercent/100;
//					eventSettingData.setAlarmValue(String.format("%.2f", alarmValue));
//				}else if (eventTypeId==1103) {//功率因数越限
//					LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(objId);
//					eventSettingData.setObjName(ledger.getLedgerName());
//					Float alarmValue = alarmPercent/100;
//					eventSettingData.setAlarmValue(String.format("%.2f", alarmValue));
//				}
//			}
			
		}
		resultMap.put( "isSuccess", isSuccess );
		resultMap.put( "objWithoutThreshold", objWithoutThreshold );
		return resultMap;
	}
	
	@Override
	public boolean updateEventSettingData(List<Map<String, Object>> itemMapList) {
		boolean isSuccess = true;
		for (Map<String, Object> itemMap : itemMapList) {
//			Map<String, Object> queryMap = new HashMap<String, Object>();
			long recId = Long.parseLong( itemMap.get( "recId" ).toString() );
//			BigDecimal alarmPercent = new BigDecimal(itemMap.get("alarmPercent").toString());
//			queryMap.put("recId", recId);
//            queryMap.put("alarmPercent", Float.parseFloat(itemMap.get("alarmPercent").toString()));
			EventSettingRecBean eRecBean = messageMapper.getEventSettingRecByRecId( recId );
			eRecBean.setAlarmPercent( Float.parseFloat( itemMap.get( "alarmPercent" ).toString() ) );
//			if (eRecBean != null) {
//				if (eRecBean.getEventTypeId()==1100 || eRecBean.getEventTypeId()==1101 || eRecBean.getEventTypeId()==1102) {//功率越安全限;功率越经济限;反向功率越限
//					Map<String, Object> thresholdInfo = null;
//					if (eRecBean.getObjType() == 2) {//测量点
//						thresholdInfo  = meterManagerMapper.getMeterThresholdInfo(eRecBean.getObjId(),4l);//获取额定功率
//					} else {
//						thresholdInfo = ledgerManagerMapper.getLedgerThresholdInfo(eRecBean.getObjId(),4l);//获取额定功率
//					}
//						
//					if (thresholdInfo == null) {
//						continue;
//					}						
//					BigDecimal ratedValue = new BigDecimal(thresholdInfo.get("THRESHOLDVALUE").toString());
//					BigDecimal alarmPercentBD = ratedValue.multiply(alarmPercent).divide(new BigDecimal(100));
//					String alarmValue = alarmPercentBD.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
//					queryMap.put("alarmValue", alarmValue);
//				}else if (eRecBean.getEventTypeId()==1103) {//功率因数越限
//					String alarmValue = alarmPercent.divide(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString();
//					queryMap.put("alarmValue", alarmValue);
//				}
			isSuccess = isSuccess && messageMapper.updateEventSettingData( eRecBean ) > 0;
		}
//		}
		return isSuccess;
	}
	
	@Override
	public boolean deleteEventSettingData(List<Long> recIdList) {
		boolean isSuccess = true;
		for (Long recId : recIdList) {
			isSuccess = isSuccess && messageMapper.deleteEventSettingData( recId ) > 0;
		}
		return isSuccess;
	}
	
	@Override
	public void createServiceReport() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get( Calendar.YEAR );
		int month = cal.get( Calendar.MONTH );
		String reportModule = year + "" + month;
		int count = this.messageMapper.serviceReportIsCreate( Long.parseLong( reportModule ) );
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
						String url = "/message/pushServiceReportPage.htm?reportId=" + infoId;
						
						//状态：1-未推送；2-推送
						Integer status = ledgerType.equals( ServiceReportBean.LEDGER_TYPE_NORMAL ) ? ServiceReportBean.STATUS_PUSH : ServiceReportBean.STATUS_UNPUSH;
						messageMapper.insertReportInfo( infoId, ledgerId, reportDate, Long.parseLong( reportModule ), url, new Date(), status );
						
						
						//普通用户，直接推送
//						if(ledgerType == ServiceReportBean.LEDGER_TYPE_NORMAL){
//							// 发送消息给手机
//							List<Long> userIds = messageMapper.getBookAccountIdByLedger(ledgerId, 1, 2);
//							if (userIds == null || userIds.size() == 0){
//								return;
//							}
//							List<String> strUserIds = new ArrayList<String>();
//							for (Long userId : userIds){
//								strUserIds.add(userId.toString());
//							}
//							Map<String, String> para = new HashMap<String, String>();
//							para.put("type", "report");
//							para.put("msgId", String.valueOf(infoId));
//							// 判断是否在屏蔽时间段内，拼接接收规则
//							for (String userId : strUserIds) {
//								List<String> alias = new ArrayList<String>();
//								alias.add(userId);
//								UserBean userBean = userBeanMapper.getUserByAccountId(Long.parseLong(userId));
//								if(JPushUtil.checkSendEnable(userBean.getFreeTimePeriod())){ // 立即推送
//									para.put("triggerType", "1");
//									JPushUtil.sendPushByTagAlias(alias, "report", DateUtil.getDateMonth(reportDate) + "月服务报告", "尊敬的用户，服务报告已生成，请查收！", para);
//								} else {
//									messageMapper.insertMsgHis(Long.parseLong(userId), infoId, new Date() ,3);
//								}
//								
//							}
//					        //// 检查这些user所接收到的消息是否达到配置的条数
//					        this.phoneService.checkUserMessage(null);
//						}
					}
				}
			}
		}
	}
	
	@Override
	public void pushServiceReport(Long reportId, String spAdvise,
								  String spName, String spPhone) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put( "reportId", reportId );
		param.put( "spAdvise", spAdvise );
		param.put( "spName", spName );
		param.put( "spPhone", spPhone );
		param.put( "status", 2 );//状态变为已推送
		//更新ServiceReport
		this.messageMapper.updateServiceReport( param );
		//TODO:预留-及时推送服务报告给移动端
	}
	
	/* (non-Javadoc)
	 * @see com.linyang.energy.service.MessageService#getUserIdByMsgHis()
	 */
	@Override
	public List<Long> getUserIdByMsgHis() {
		return this.messageMapper.getUserIdByMsgHis();
	}
	
	@Override
	public void insertVisitorRecord(String visitorInfo) {
		Long visitorId = SequenceUtils.getDBSequence();
		Date visitorTime = new Date();
		messageMapper.insertVisitorRecord( visitorId, visitorInfo, visitorTime );
	}
	
}
