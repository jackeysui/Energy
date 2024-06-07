package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.linyang.energy.service.PhoneService;
import com.linyang.energy.utils.WebConstant;

import com.linyang.common.web.common.Log;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 15-10-8.
 */
public class UserRemindJob {
   
    @Autowired
    private PhoneService phoneService;

    /**
     * 间隔时间
     */
    private int intervalHour = 24;

    /**
     * 执行小时点
     */
    private int startHour = WebConstant.startHour;

    /**
     * 执行分钟点
     */
    private int startMinute = WebConstant.startMinute;

    /**
     * 初始化方法
     */
    public void init(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, startHour);
        cal.set(Calendar.MINUTE, startMinute);
        cal.set(Calendar.SECOND, 0);
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                Log.info("start user remind job!");

                // 执行任务
                phoneService.longTimeNotLogin();
                phoneService.reportNotRead();

                Log.info("end user remind job!");
            }
        }, cal.getTime(), 1000 * 60 * 60 * intervalHour);
    }

}
