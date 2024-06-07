package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.BigDataService;
import com.linyang.energy.utils.DateUtil;

/**
 * 计算预测模型的神经元类型
 * @author guosen
 * @date 2016-11-28
 *
 */
public class CalPredictModelJob {
	@Autowired
	private BigDataService bigDataService;
	
    private int intervalHour = 24;  /** 间隔时间 */
    private int startHour = 2;  /** 执行小时点 */
	
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
                Log.info("计算神经元类型...begin");
                bigDataService.CalPredictModel();
                Log.info("计算神经元类型...end");
            }
        }, cal.getTime(), 1000 * 60 * 60 * intervalHour);
	}

}
