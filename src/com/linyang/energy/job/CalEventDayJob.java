package com.linyang.energy.job;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.EventQueryService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:02 2019/10/10
 * @ Description：事件日任务类
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public class CalEventDayJob {
	
	//日用能超配额告警事件
	
	/**
	 * 执行间隔小时数  (一天执行一次)
	 */
	private int intervalHour = 24;
//	private int intervalHour = 1;
	
	private int beginHour;
	
	private int beginMinute;
	
	@Autowired
	private EventQueryService eventQueryService;
	
	/**
	 * 初始化方法
	 */
	public void init(){
		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.HOUR , beginHour );
		cal.set( Calendar.MINUTE , beginMinute );
		//早上7点40开始
		new Timer().scheduleAtFixedRate( new TimerTask(){
			@Override
			public void run() {
				//设置一下时间的时分秒
				Calendar c = Calendar.getInstance();
				//取昨天的时间来设置
				c.setTime(DateUtil.addDateDay(new Date(), -1));
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Date dateTime = c.getTime();
				//'日配额超限'事件
				try{ eventQueryService.analysisW_D(1110L, dateTime);}catch(Exception ec){Log.error("日用能超配额(水)告警事件出错"+ec.getMessage());}
				//'日配额超限'事件
				try{ eventQueryService.analysisG_D(1111L, dateTime);}catch(Exception ec){Log.error("日用能超配额(气)告警事件出错"+ec.getMessage());}
				//'日配额超限'事件
				try{ eventQueryService.analysisE_D(1112L, dateTime);}catch(Exception ec){Log.error("日用能超配额(电)告警事件出错"+ec.getMessage());}
			}
		}, cal.getTime(), 1000 * 60 * 60 * intervalHour);
	}
	
	public int getBeginMinute() {
		return beginMinute;
	}
	
	public void setBeginMinute(int beginMinute) {
		this.beginMinute = beginMinute;
	}
	
	public int getBeginHour() {
		return beginHour;
	}
	
	public void setBeginHour(int beginHour) {
		this.beginHour = beginHour;
	}
}
