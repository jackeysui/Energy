package com.linyang.energy.job;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.energy.service.UserService;

/**
 * Created by Administrator on 16-12-01.
 */
public class updateActiveStatusJob {
	
    @Autowired
    private UserService userService;
    
    /**
    * 间隔时间
    */
   private int intervalHour = 24;

   /**
    * 执行小时点
    */
   private int startHour = 2;

   /**
    * 执行分钟点
    */
   private int startMinute = 00;
   
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
               List<Long> accountIds = userService.getAccountIds();
               for (Long accountId : accountIds) {
            	   userService.updateActiveStatus(accountId);
               }
           }
       }, cal.getTime(), 1000 * 60 * 60 * 1 * intervalHour);
   }
}
