/**
 */
package com.linyang.energy.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.common.web.common.Log;
import com.linyang.energy.model.MsgHisBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.MessageService;
import com.linyang.energy.service.PhoneService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.JPushUtil;

/**
 * 推送屏蔽消息
 * 
 * @author chengq
 * @date 2015-12-22 下午01:41:50
 * @version 1.0
 */
public class PushBlockJob {
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private PhoneService phoneService;

	/**
	 * 间隔时间
	 */
	private int intervalMinute = 60;

	public void init() {
		Calendar cal = Calendar.getInstance();
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Log.info("start push block-time job!");
				pushBlockMsg();
			}
		}, cal.getTime(), 1000 * 60 * intervalMinute);
	}

	public void pushBlockMsg() {
		List<Long> userIds = messageService.getUserIdByMsgHis();
		if (userIds.isEmpty())
			return;
		for (Long userId : userIds) {
			
				UserBean userBean = phoneService.getUserByAccountId(userId);
				if (!JPushUtil.checkSendEnable(userBean.getFreeTimePeriod(),userBean.getIsShield()))
					continue;
				List<MsgHisBean> msgHisList = messageService.getMsgHisList(userId);
				int sendSize = msgHisList.size();
				if (sendSize == 0)
					continue;
				if (sendSize >= 3) {
					List<String> strUserIds = new ArrayList<String>();
					strUserIds.add(userId.toString());
					Map<String, String> para = new HashMap<String, String>();
					String content = "尊敬的" + userBean.getLoginName() + "，您共有"
							+ String.valueOf(sendSize) + "条新信息，敬请查看，可登陆系统进行相关处理!";
					para.put("triggerType", "1");
					JPushUtil.sendPushByAlias(strUserIds, "新信息条数提醒", content, para);
					//删除userId所有消息
					messageService.deleteMsgHis(userId,-1L);
				} else if (sendSize > 0 && sendSize < 3) { // 逐条发送
					List<String> strUserIds = new ArrayList<String>();
					strUserIds.add(userId.toString());
					Map<String, String> para;
					for (MsgHisBean hisBean : msgHisList) {
						try {
							para = new HashMap<String, String>();
							String tag, title, content;
							Map<String,Object> msgMap = messageService.getMsgInfo(hisBean.getMsgId(), hisBean.getMsgType());
							switch (hisBean.getMsgType().intValue()) {
							case 1: // 自定义消息
								tag = "definedMsg";
								title = msgMap.get("MESSAGE_TITLE").toString();
								content = "您有一条新的消息！";
								para.put("A", msgMap.get("MESSAGE_CONTENT").toString());
								break;
							case 2: // 新闻\政策
								int type = Integer.parseInt(msgMap.get("INFO_TYPE").toString()); // 记录类型:1,新闻;2,政策
								tag = "policy";
								title = "政策";
								if (type == 1) {
									tag = "news";
									title = "新闻";
								}
								content = msgMap.get("INFO_TITLE").toString();
								para.put("A", msgMap.get("INFO_PIC").toString());
								para.put("B", msgMap.get("INFO_CONTENT").toString());
								break;
							case 3: // 服务报告
								tag = "report";
								title = DateUtil.getDateMonth(DateUtil.convertStrToDate(msgMap.get("REPORT_TIME").toString())) + "月服务报告";
								content = "尊敬的用户，服务报告已生成，请查收！";
								break;
							case 4: // 事件
								tag = "event";
								title = msgMap.get("EVENT_NAME").toString();
								content = msgMap.get("EVENT_START_TIME").toString() + msgMap.get("CONTENT").toString() + (Integer.parseInt(msgMap.get("EVENT_STATUS").toString()) == 1 ? " 发生" : " 恢复");
								para.put("A", msgMap.get("EVENT_RECID").toString());
								para.put("B", msgMap.get("EVENT_ID").toString());
								para.put("C", msgMap.get("OBJECT_TYPE").toString());
								para.put("D", msgMap.get("OBJECT_ID").toString());
								para.put("E", title.length() <= 10 ? title : (title.substring(0, 10) + "..."));
								break;
							default:
								tag = "";
								title = "";
								content = "";
								break;
							}
							para.put("type", tag);
							para.put("msgId", String.valueOf(hisBean.getMsgId()));
							para.put("triggerType", "1");
							JPushUtil.sendPushByAlias(strUserIds, title, content, para);
							//删除userId所有消息
							messageService.deleteMsgHis(userId,hisBean.getMsgId());
						} catch (NumberFormatException e) {
							Log.info("Push block-time job error with userId is"+userId+",messageId is"+hisBean.getMsgId());
						}
					}
				}
		}
	}
}
