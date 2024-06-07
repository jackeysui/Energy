package com.linyang.energy.job;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.TimeService;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author xs
 */
public class UpdateHolidayJob {
      
        @Autowired
        private TimeService timeService;
        /**
        * 间隔时间
        */
       private int intervalHour = 24;

       /**
        * 执行小时点
        */
       private int startHour = 1;

       /**
        * 执行分钟点
        */
       private int startMinute = 0;
       
        /**
        * 初始化方法
        */
       public void init(){
           Calendar cal = Calendar.getInstance();
           cal.set(Calendar.HOUR_OF_DAY, startHour);
           cal.set(Calendar.MINUTE, startMinute);
           cal.set(Calendar.SECOND, 0);
           new Timer().scheduleAtFixedRate(new TimerTask(){
               @Override
               public void run() {
                   Log.info("start update holiday job!");

                   // 执行任务
                   timeService.updateHolidaysByOfficalSetting();

                   Log.info("end update holiday job!");
               }
           }, cal.getTime(), 1000 * 60 * 60 * intervalHour);
       }
}
