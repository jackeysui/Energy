package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.MessageService;

/**
 * 自动推送服务报告
 * @author Administrator
 *
 */
public class PushServiceReportJob {
   
	@Autowired
	private MessageService messageService;
	
	private int beginDay;
	
	private int beginHour;
	
	private int beginMinute;

	/**
	 * 初始化方法
	 */
	public void init() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, beginHour);
		cal.set(Calendar.MINUTE, beginMinute);
		cal.set(Calendar.SECOND, 0);
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Log.info("start calculate account level job!");
				// 执行任务
				try {
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					int day=c.get(Calendar.DAY_OF_MONTH);
					if (day == beginDay){//第一天执行
						messageService.createServiceReport();
					}
				} catch (Exception e) {
					Log.error("CalAccountLevelJob--无法初始化");
				}
			}
		}, cal.getTime(), 1000 * 60 * 60 * 24);
	}

	public int getBeginDay() {
		return beginDay;
	}

	public void setBeginDay(int beginDay) {
		this.beginDay = beginDay;
	}

	public int getBeginHour() {
		return beginHour;
	}

	public void setBeginHour(int beginHour) {
		this.beginHour = beginHour;
	}

	public int getBeginMinute() {
		return beginMinute;
	}

	public void setBeginMinute(int beginMinute) {
		this.beginMinute = beginMinute;
	}
}
