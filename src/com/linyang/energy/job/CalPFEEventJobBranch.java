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
 * @ Date       ：Created in 13:27 2019/11/4
 * @ Description：原事件方法过多,执行效率慢导致有些事件不能正常运行,故增加此方法
 * @ Modified By：:catkins.
 * @Version: 4.7
 */
public class CalPFEEventJobBranch {
	
	/***
	 * 事件结束时间为当前时间往前推小时数
	 */
	private int calHour = WebConstant.eventCalHour;
	
	/**
	 * 执行间隔小时数
	 */
	private int intervalHour = WebConstant.eventIntervalHour;
	
	private int beginMinute;
	
	@Autowired
	private EventQueryService eventQueryService;
	
	/**
	 * 初始化方法
	 */
	public void init(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, beginMinute);
		new Timer().scheduleAtFixedRate( new TimerTask(){
			@Override
			public void run() {
				Date endTime = DateUtil.getPreNDate(new Date(), 0);
				endTime = DateUtil.clearMinute(endTime);
				Date beginTime = DateUtil.getPreNDate(endTime, intervalHour);
				beginTime = DateUtil.getDateMinusSeconde(beginTime, -1);
				//功率因数越限
				try{ eventQueryService.analysisPf(1103L, beginTime, endTime);}catch(Exception ec){Log.error("功率因数越限出错"+ec.getMessage());}
				//电流越安全限
				try{ eventQueryService.analysisI(1105L, beginTime, endTime);}catch(Exception ec){Log.error("电流越安全限出错"+ec.getMessage());}
			}
		}, cal.getTime(), 1000 * 60 * 60 * intervalHour);
	}
	
	public void setIntervalHour(int intervalHour) {
		this.intervalHour = intervalHour;
	}
	
	public void setCalHour(int calHour) {
		this.calHour = calHour;
	}
	
	public int getBeginMinute() {
		return beginMinute;
	}
	
	public void setBeginMinute(int beginMinute) {
		this.beginMinute = beginMinute;
	}

}
