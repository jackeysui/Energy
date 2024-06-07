/**
 * 
 */
package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.linyang.energy.utils.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.common.web.common.Log;
import com.linyang.energy.service.EventQueryService;
import com.linyang.energy.utils.DateUtil;
/**
 * 事件计算并推送
 *
 */

public class CalPFEventJob {
	
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
		new Timer().scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				/**
				 * 由于功率因数和电流事件计算时间过长,导致后面任务无法正常执行
				 * 故增加一个[CalPFEEventJobBranch]任务,来专门对这两个事件进行处理
				 */
				Date endTime = DateUtil.getPreNDate(new Date(), 0);
                endTime = DateUtil.clearMinute(endTime);
                Date beginTime = DateUtil.getPreNDate(endTime, intervalHour);
                beginTime = DateUtil.getDateMinusSeconde(beginTime, -1);
                //功率越安全限
               try{ eventQueryService.analysisPowerSafety(1100L, beginTime, endTime);}catch(Exception ec){Log.error("功率越安全限出错"+ec.getMessage());}
                //功率越经济限
               try{  eventQueryService.analysisPowerEconomy(1101L, beginTime, endTime);}catch(Exception ec){Log.error("功率越经济限出错"+ec.getMessage());}
                //反向功率越限
               try{  eventQueryService.analysisPowerReverse(1102L, beginTime, endTime);}catch(Exception ec){Log.error("反向功率越限出错"+ec.getMessage());}
                //电压越安全限 //事件名称修改为 电压越上限
               try{  eventQueryService.analysisV(1104L, beginTime, endTime);}catch(Exception ec){Log.error("电压越上限出错"+ec.getMessage());}
                //需量告警
               try{ eventQueryService.analysisDemand(1106L, beginTime, endTime);}catch(Exception ec){Log.error("需量告警出错"+ec.getMessage());}
                //电压越下限事件
				try{ eventQueryService.analysisLowerV(1108L, beginTime, endTime);}catch(Exception ec){Log.error("电压越下限出错"+ec.getMessage());}
				//水量越上限事件
				try{ eventQueryService.analysisW(1109L,beginTime,endTime); }catch (Exception ce){Log.error("水量越上限出错"+ce.getMessage());}
				
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
