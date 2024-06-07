package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.UserAnalysisService;

/**
 * 定时计算用户等级
 * 
 * @author gaofeng
 * 
 */
public class CalAccountLevelJob {
	
	@Autowired
	private UserAnalysisService userAnalysisService;

	/**
	 * 间隔时间
	 */
	private int intervalHour = 24;

	/**
	 * 第几分钟执行
	 */
	private int startMinute = 20;

	/**
	 * 初始化方法
	 */
	public void init() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, startMinute);
		cal.set(Calendar.SECOND, 0);
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Log.info("start calculate account level job!");
				// 执行任务
				try {
					userAnalysisService.calAccountLevel();
				} catch (Exception e) {
					Log.error("CalAccountLevelJob--无法初始化");
				}
			}
		}, cal.getTime(), 1000 * 60 * 60 * intervalHour);
	}

	public void setIntervalHour(int intervalHour) {
		this.intervalHour = intervalHour;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}
}
