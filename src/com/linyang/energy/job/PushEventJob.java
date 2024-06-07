package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.linyang.energy.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.EventQueryService;

/**
 * 事件统计推送
 * 
 * @author gaofeng
 * 
 */
public class PushEventJob {
	
	@Autowired
	private EventQueryService eventQueryService;

    //在用
    private int intervalHour = 24;  /** 间隔时间 */
    private int startHour = 10;  /** 执行小时点 */
	
	/**
	 * 初始化方法
	 */
	public void init() {
        Calendar cal = Calendar.getInstance();
        int nowHour = DateUtil.getHourFor24(new Date());//启动时默认会执行改init方法，如果启动时已超过10点，则明天十点再执行
        if(nowHour >= startHour){
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, startHour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.info("开始事件统计定时任务推送...begin");

                // 执行任务
                eventQueryService.processEventCount();

                Log.info("开始事件统计定时任务推送...end");
            }
        }, cal.getTime(), 1000 * 60 * 60 * intervalHour);
	}

}
