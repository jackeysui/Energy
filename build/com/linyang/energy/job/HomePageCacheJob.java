package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.IndexService;
import com.linyang.energy.utils.DateUtil;

/**
 * 缓存首页数据job
 * @author Administrator
 *
 */
public class HomePageCacheJob {
	
	@Autowired
	private IndexService indexService;

    //在用
    private int intervalHour = 24;  /** 间隔时间 */
    private int startHour = 1;  /** 执行小时点 */
    
	/**
	 * 初始化方法
	 */
	public void init() {
        Calendar cal = Calendar.getInstance();
        int nowHour = DateUtil.getHourFor24(new Date());//启动时默认会执行改init方法，如果启动时已超过1点，则明天一点再执行
        if(nowHour >= startHour){
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        cal.set(Calendar.HOUR_OF_DAY, startHour);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.info("缓存首页数据...begin");

                // 缓存“运营商首页”数据
                try {
                    indexService.SavePlatHomePageData();
                }
                catch (Exception e){
                    Log.error("indexService.SavePlatHomePageData 出错！");
                }

                // 缓存“管理者首页”数据
                try {
                    indexService.SaveHomePageData();
                }
                catch (Exception e){
                    Log.error("indexService.SaveHomePageData 出错！");
                }

                Log.info("缓存首页数据...end");
            }
        }, cal.getTime(), 1000 * 60 * 60 * intervalHour);
	}
}
