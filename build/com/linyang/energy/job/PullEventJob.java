package com.linyang.energy.job;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.EventQueryService;
import com.linyang.energy.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 16:31 2019/12/2
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public class PullEventJob {
	/**
	 * 执行间隔分钟数
	 */
	private int interval;
	
	@Autowired
	private EventQueryService eventQueryService;
	
	/**
	 * 初始化方法
	 */
	public void init(){
		Calendar cal = Calendar.getInstance();
		new Timer().scheduleAtFixedRate( new TimerTask(){
			@Override
			public void run() {
				Date endTime = new Date();
				Date beginTime = DateUtil.getDateMinusMinute(endTime, interval);
//				beginTime = DateUtil.getDateMinusSeconde(beginTime, -1);
				//停上电事件
				try{ eventQueryService.failure(1113L,beginTime,endTime); }catch (Exception ce){Log.error("停上电事件出错"+ce.getMessage());}
			}
		}, cal.getTime(), 1000 * 60 * interval);
	}
	
	public int getInterval() {
		return interval;
	}
	
	public void setInterval(int interval) {
		this.interval = interval;
	}
}
