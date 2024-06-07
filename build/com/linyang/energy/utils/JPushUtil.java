package com.linyang.energy.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.TimeUnit;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleClient;
import cn.jpush.api.schedule.ScheduleListResult;
import cn.jpush.api.schedule.ScheduleResult;
import cn.jpush.api.schedule.model.SchedulePayload;
import cn.jpush.api.schedule.model.TriggerPayload;

/**
 * 极光信息推送工具类
 * 
 * @author gaofeng
 * 
 */
public class JPushUtil {
	protected static final Logger LOG = LoggerFactory.getLogger(JPushUtil.class);

	private static final String appKey = WebConstant.appKey;
	private static final String masterSecret = WebConstant.masterSecret;
	private static final String appKeyNeuter = WebConstant.appKeyNeuter;
	private static final String masterSecretNeuter = WebConstant.masterSecretNeuter;
	private static final boolean apnsProduction = WebConstant.apnsProduction;

	private static final long timeToLive = 86400; // 离线消息存活时间（秒）

	private static JPushClient jpushClient;
	
	private static JPushClient jpushClientNeuter;
	
	private static ScheduleClient scheduleClient;
	private static ScheduleClient scheduleClientNeuter;

	/**
	 * 组织发送内容(tag)
	 * 
	 * @param tag
	 * @param title
	 * @param content
	 * @param para
	 * @return
	 */
	public static PushPayload buildPushObjectByTag(String tag, String title, String content, Map<String, String> para) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.tag(tag)).setNotification(
				Notification.newBuilder().setAlert(content).addPlatformNotification(
						AndroidNotification.newBuilder().setTitle(title).addExtras(para).build()).addPlatformNotification(
						IosNotification.newBuilder().incrBadge(1).addExtras(para).build()).build()).setOptions(
				Options.newBuilder().setTimeToLive(timeToLive).setApnsProduction(apnsProduction).build()).build();
	}

	/**
	 * 组织发送内容(tag and alias)
	 * 
	 * @param alias
	 * @param tag
	 * @param title
	 * @param content
	 * @param para
	 * @return
	 */
	public static PushPayload buildPushObjectByTagAlias(List<String> alias, String tag, String title, String content,
			Map<String, String> para) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(
				Audience.newBuilder().addAudienceTarget(AudienceTarget.tag(tag)).addAudienceTarget(AudienceTarget.alias(alias))
						.build()).setNotification(
				Notification.newBuilder().setAlert(content).addPlatformNotification(
						AndroidNotification.newBuilder().setTitle(title).addExtras(para).build()).addPlatformNotification(
						IosNotification.newBuilder().incrBadge(1).addExtras(para).build()).build()).setOptions(
				Options.newBuilder().setTimeToLive(timeToLive).setApnsProduction(apnsProduction).build()).build();
	}
	
	/**
	 * 组织发送内容(alias)
	 * 
	 * @param alias
	 * @param title
	 * @param content
	 * @param para
	 * @return
	 */
	public static PushPayload buildPushObjectByAlias(List<String> alias, String title, String content, Map<String, String> para) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(
				Audience.newBuilder().addAudienceTarget(AudienceTarget.alias(alias)).build()).setNotification(
				Notification.newBuilder().setAlert(content).addPlatformNotification(
						AndroidNotification.newBuilder().setTitle(title).addExtras(para).build()).addPlatformNotification(
						IosNotification.newBuilder().incrBadge(1).addExtras(para).build()).build()).setOptions(
				Options.newBuilder().setTimeToLive(timeToLive).setApnsProduction(apnsProduction).build()).build();
	}

	/**
	 * 生成定时发送规则
	 * @param pushPayload
	 * @param para
	 * @param triggerType
	 * 如果{@code triggerType=1} 直接发送,无需设置其他参数;
	 * 如果{@code triggerType=2}定时发送,需要设置参数{@code triggerStart}“定时任务执行时间”,{@code triggerTitle}“定时任务名称”;
	 * 如果{@code triggerType=3} 定期发送,需要设置参数{@code triggerStart}“定时任务有效开始时间”,{@code triggerEnd}“定时任务失效时间”,{@code triggerTime}“定时任务执行时间”,{@code triggerTimeUtil}“执行单位”,{@code triggerFrequency}“执行频率”,{@code triggerTitle}“定时任务名称”,{@code triggerPoint};
	 * @return
	 */
	public static SchedulePayload buildPushSchedules(PushPayload pushPayload, Map<String, String> para){
		TriggerPayload trigger = null;
		int triggerType = para.get("triggerType") == null?1:Integer.parseInt(para.get("triggerType").toString());
		if(triggerType == 2){// 定时发送
			trigger = TriggerPayload.newBuilder().setSingleTime(para.get("triggerStart").toString()).buildSingle();
		} else if(triggerType == 3){ // 定期任务
			trigger = TriggerPayload.newBuilder()
				.setPeriodTime(para.get("triggerStart").toString(),para.get("triggerEnd").toString(),para.get("triggerTime").toString())
				.setTimeFrequency(convertToTimeUtil(Integer.parseInt(para.get("triggerTimeUtil").toString())),Integer.parseInt(para.get("triggerFrequency").toString()),
						para.get("triggerPoint").toString().split(","))
				.buildPeriodical();
		}
		return SchedulePayload.newBuilder().setName(para.get("triggerTitle").toString()).setEnabled(true).setTrigger(trigger).setPush(pushPayload).build();
	}
	
	/**
	 * 发送信息
	 * @param tag
	 * @param title
	 * @param content
	 * @param para
	 */
	public static void sendPushByTag(String tag, String title, String content, Map<String, String> para) {
		int triggerType = para.get("triggerType") == null?1:Integer.parseInt(para.get("triggerType").toString());
		if(triggerType == 1){ // 立即发送
			sendToMsg(buildPushObjectByTag(tag, title, content, para));
		} else { // 定时/定期发送
			sendToScheduleMsg(buildPushSchedules(buildPushObjectByTag(tag, title, content, para), para));
		}
		
	}
	public static void sendPushByTagAlias(List<String> alias, String tag, String title, String content, Map<String, String> para) {
		int triggerType = para.get("triggerType") == null?1:Integer.parseInt(para.get("triggerType").toString());
		if(triggerType == 1){
			sendToMsg(buildPushObjectByTagAlias(alias, tag, title, content, para));
		} else {
			sendToScheduleMsg(buildPushSchedules(buildPushObjectByTagAlias(alias, tag, title, content, para), para));
		}
	}
	public static void sendPushByAlias(List<String> alias, String title, String content, Map<String, String> para) {
		int triggerType = para.get("triggerType") == null?1:Integer.parseInt(para.get("triggerType").toString());
		if(triggerType == 1){
			sendToMsg(buildPushObjectByAlias(alias, title, content, para));
		} else {
			sendToScheduleMsg(buildPushSchedules(buildPushObjectByAlias(alias, title, content, para), para));
		}
	}

	/**
	 * 发送即时消息
	 * 
	 * @param payload
	 */
	private static void sendToMsg(PushPayload payload) {
		// HttpProxy proxy = new HttpProxy("localhost", 3128);
		// Can use this https proxy: https://github.com/Exa-Networks/exaproxy
		if (jpushClient == null)
			jpushClient = new JPushClient(masterSecret, appKey, 3);
		if(jpushClientNeuter == null)
			jpushClientNeuter = new JPushClient(masterSecretNeuter, appKeyNeuter, 3);

		try {
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);
			PushResult resultNeuter = jpushClientNeuter.sendPush(payload);
			LOG.info("Got result - " + resultNeuter);

		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ");

		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ");
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
		}
	}
	
	/**
	 * 发送定时消息
	 * 
	 * @param payload
	 */
	private static void sendToScheduleMsg(SchedulePayload payload) {
		if (scheduleClient == null)
			scheduleClient = new ScheduleClient(masterSecret, appKey, 3);
		if(scheduleClientNeuter == null)
			scheduleClientNeuter = new ScheduleClient(masterSecretNeuter, appKeyNeuter,3);
		try {
			scheduleClient.createSchedule(payload);
			scheduleClientNeuter.createSchedule(payload);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ");
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ");
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
		}
	}
	
	/**
	 * 数字与TimeUtil枚举转换
	 * @param number
	 * 1 DAY;2 WEEK;3 MONTH
	 * @return
	 */
	private static TimeUnit convertToTimeUtil(int number) {
		switch (number) {
		case 1:
			return TimeUnit.DAY;
		case 2:
			return TimeUnit.WEEK;
		case 3:
			return TimeUnit.MONTH;
		default:
			return TimeUnit.DAY;
		}
	}
	
	/**
	 * 根据屏蔽时间生成推送规则
	 * @param {@code para}入参,包括<br> 
	 * ①、{@code periodTimes}“屏蔽时间段”,24时制,HH(屏蔽起始小时)-HH(屏蔽结束小时);<br>
	 * ②、{@code triggerTitle}“任务名称”,定时或定期任务需要该字段;<br>
	 * @return {@code para}出参,包括:<br/>
	 * ①、{@code triggerType}“推送类型”,1立即推送 2定时推送 3定期推送;<br>
	 * ②、其它参数参照{@link buildPushSchedules}方法规则说明;<br>
	 */
	public static Map<String,String> createSendRule(Map<String,String> para){
		// 屏蔽时间段
		String times = para.get("periodTimes")==null?"":para.get("periodTimes").toString();
		if(StringUtils.isBlank(times)){ // 屏蔽时间为空,立即发送
			para.put("triggerType", "1");
			return para;
		}
		// 根据屏蔽时间计算发送类型以及发送时间
		String hours[] = times.split("-");
		String startTime = DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN)+" "+hours[0]+":00:00"; 
		String endTime = DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN)+" "+hours[1]+":00:00";
		if(DateUtil.convertStrToDate(startTime).getTime() > DateUtil.convertStrToDate(endTime).getTime())// 第二天 如：20:12 > 8:12
			 endTime = DateUtil.convertDateToStr(DateUtil.getNextDayDate(DateUtil.convertStrToDate(endTime)),DateUtil.DEFAULT_PATTERN);
		long nowTimes = new Date().getTime();
		long startTimes = DateUtil.convertStrToDate(startTime).getTime();
		long endTimes = DateUtil.convertStrToDate(endTime).getTime();
		if(nowTimes < startTimes || nowTimes > endTimes){ // 屏蔽之前 或者屏蔽之后,立即发送
			para.put("triggerType", "1");
			return para;
		} else { // 屏蔽中,定时发送
			para.put("triggerType", "2");
			para.put("triggerStart", endTime);
			para.put("triggerTitle", para.get("triggerTitle")==null?"定时发送":para.get("triggerTitle").toString());
			return para;
		}
	}
	
	/**
	 * 删除定时任务列表内事件(推送服务器最多保留100条)
	 * @param size 待发送信息条数
	 * @param filterStr 可删除的任务名称列表
	 * @return
	 */
	public static void deleteEnableSchedule(int size, String filterStr) {
		if (scheduleClient == null)
			scheduleClient = new ScheduleClient(masterSecret, appKey, 3);
		try {
			List<String> scheduleIds = new ArrayList<String>();
			ScheduleListResult scheduleListResult = scheduleClient.getScheduleList(1);
			List<ScheduleResult> scheduleList = scheduleListResult.getSchedules();
			int totalCount = scheduleListResult.getTotal_count();
			if(totalCount > 50)
				scheduleList.addAll(scheduleClient.getScheduleList(2).getSchedules());
			int deleteSize = (totalCount + size) - 100; // 待删除条数
			if (deleteSize > 0) {
				int i = 0;
				for (ScheduleResult scheduleResult : scheduleList) {
					if (i < deleteSize) {
						if (filterStr.indexOf(scheduleResult.getName()) > -1) {
							scheduleIds.add(scheduleResult.getSchedule_id());
							i = i + 1;
						}
					} else {
						break;
					}
				}
			}
			if (!scheduleIds.isEmpty()) {
				for (int i = 0; i < scheduleIds.size(); i++) {
					scheduleClient.deleteSchedule(scheduleIds.get(i));
				}
			}
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ");
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ");
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
		}
	}
	
	/**
	 * 根据屏蔽时段判断是否立即发送
	 * @param times 屏蔽时段:小时制
	 * @param isShield 是否启用消息屏蔽(0否  1是)
	 * @return
	 */
	public static boolean checkSendEnable(String times,int isShield){
		// 是否启用消息屏蔽(0否  1是)
		if (isShield == 0)
			return true; // 不启动消息屏蔽,立即发送 
		boolean isSend = false;
		// 屏蔽时间段
		if(StringUtils.isBlank(times)){ // 屏蔽时间为空,立即发送
			isSend = true;
		} else {
			// 根据屏蔽时间计算发送类型以及发送时间
			String hours[] = times.split("-");
			int intCurHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			int intStartHour = Integer.parseInt(hours[0]);
			int intEndHour = Integer.parseInt(hours[1]);
			if(intStartHour > intEndHour){ // 跨天 intStartHour:24  0:intEndHour
				if ((intStartHour <= intCurHour && intCurHour < 24) || (0 <= intCurHour && intCurHour < intEndHour)) {
					isSend = false;
				} else {
					isSend = true;
				}
			} else {
				if (intStartHour <= intCurHour && intCurHour < intEndHour) {
					isSend = false;
				} else {
					isSend = true;
				}
			}
		}
		return isSend;
	}
}
