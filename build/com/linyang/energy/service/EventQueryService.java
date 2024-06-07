package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.EventBean;

/**
 * @Description 事件查询service
 * @author Leegern
 * @date Jan 21, 2014 10:27:12 AM
 */
public interface EventQueryService {

	/**
	 * 事件查询
	 * @param param 查询参数
	 * @return
	 */
	List<EventBean> queryEventPageList(Map<String, Object> param);
	
	/**
	 * 带筛选条件的事件查询
	 * @param param 查询参数
	 * @return
	 */
	List<EventBean> queryEventPageList2(Map<String, Object> param);
	
	/**
	 * 电工首页时间带格式的事件查询
	 */
	List<EventBean> queryEventPageList3(Map<String, Object> param);
	
	/**
	 * 从t_cur_ledger_pf查询出所有的小于0的分户下的功率因数
	 * @author Yaojiawei
	 * @date 2014-08-26
	 * */
	List<EventBean> queryLedgerMinusPF(Long ledgerId, Date beginTime, Date endTime);
	
	/**
	 * 从t_cur_pf查询出所有的小于0的分户下的功率因数
	 * @author Yaojiawei
	 * @date 2014-08-26
	 * */
	List<EventBean> queryMeterMinusPF(Long meterId, Date beginTime, Date endTime);

	
	List<EventBean> queryEventWarningList(Map<String, Object> param);
	
	/**
	 * 查询最近发生的事件
	 * 
	 * @param startTime
	 * @return
	 */
	public List<EventBean> queryRecentEventInfo(Date startTime);

    /**
     * 事件告警内容乘以ct、pt
     */
    public List<EventBean> processEventCtpts(List<EventBean> eventBeans);
    
    public EventBean processEventCtpt(EventBean eventBean);


    /**
     * 分析“功率越安全限”事件
     */
    void analysisPowerSafety(Long eventId, Date beginTime, Date endTime);

    /**
     * 分析“功率越经济限”事件
     */
    void analysisPowerEconomy(Long eventId, Date beginTime, Date endTime);

    /**
     * 分析“反向功率越限”事件
     */
    void analysisPowerReverse(Long eventId, Date beginTime, Date endTime);

    /**
     * 分析“功率因数越限”事件
     */
    void analysisPf(Long eventId, Date beginTime, Date endTime);

    /**
     * 分析“需量预警”事件
     */
    void analysisDemand(Long eventId, Date beginTime, Date endTime);

    /**
     * 分析“电压越安全限”事件
	 * update 电压越上限事件
     */
    void analysisV(Long eventId, Date beginTime, Date endTime);

    /**
     * 分析“电流越安全限”事件
     */
    void analysisI(Long eventId, Date beginTime, Date endTime);

    /**
     * 分析“污染源、治理源”事件
     */
    void analysisPolluteControl(Long eventId, Date beginTime, Date endTime);

    /**
     * 事件统计
     */
    void processEventCount();
    
    // add or update method
    // date 2019/5/23
    // Modify the content: 新增
	/**
	 * 分析“电压越下限事件”事件
	 */
    void analysisLowerV(Long eventId, Date beginTime, Date endTime);
    //end
	
	// add or update method
	// date 2019/5/23
	// Modify the content:V4.7 新增
	
	/**
	 * 分析“水量越限”事件
	 * @param eventId
	 * @param beginTime
	 * @param endTime
	 */
	void analysisW(Long eventId, Date beginTime, Date endTime);
	
	/**
	 * 分析"日用能超配额告警"事件:水
	 * @param eventId		事件id
	 * @param beginTime		开始时间
	 * @param endTime		结束时间
	 */
	void analysisW_D(Long eventId,Date dateTime);
	
	/**
	 * 分析"日用能超配额告警"事件:气
	 * @param eventId		事件id
	 * @param beginTime		开始时间
	 * @param endTime		结束时间
	 */
	void analysisG_D(Long eventId,Date dateTime);
	
	/**
	 * 分析"日用能超配额告警"事件:电
	 * @param eventId		事件id
	 * @param beginTime		开始时间
	 * @param endTime		结束时间
	 */
	void analysisE_D(Long eventId,Date dateTime);
	
	//end
	
	// add or update method by catkins.
	// date 2019/11/29
	// Modify the content: V4.8新增
	
	/**
	 * 停上电事件
	 * @param eventId
	 * @param dateTime
	 */
	void failure(Long eventId,Date beginTime, Date endTime);
	
	//end
    

}
