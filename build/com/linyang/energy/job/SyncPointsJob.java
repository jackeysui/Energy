package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.linyang.common.web.common.Log;

/**
 *  全局刷新de定时任务
 *
 */
public class SyncPointsJob {
	
	private CreateCalculatedPointJob createCalculatedPointJob;
	
	/**
	 * 间隔时间
	 */
	private int intervalHour = 24;

	/**
	 * 第几分钟执行
	 */
	private int startMinute = 50;
	
	/**
	 * 初始化方法
	 */
	public void init(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, startMinute);
		cal.set(Calendar.SECOND, 0);
		new Timer().scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				Log.info("start sync point job!");
				// 执行任务
                createCalculatedPointJob.processLedgerRelation();
			}
		}, cal.getTime(), 1000 * 60 * 60 * intervalHour);
	}

	public CreateCalculatedPointJob getCreateCalculatedPointJob() {
		return createCalculatedPointJob;
	}

	public void setCreateCalculatedPointJob(
			CreateCalculatedPointJob createCalculatedPointJob) {
		this.createCalculatedPointJob = createCalculatedPointJob;
	}

	public void setIntervalHour(int intervalHour) {
		this.intervalHour = intervalHour;
	}

	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}
}
