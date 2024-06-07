package com.linyang.energy.mapping.energydataquery;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.EventPolluteBean;
import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.EventBean;
import com.linyang.energy.model.EventSpecCfgBean;

/**
 * @Description 事件查询mapper
 * @author Leegern
 * @date Jan 21, 2014 10:24:53 AM
 */
public interface EventQueryMapper {


    List<EventBean> eventDetail(Map<String, Object> queryMap);

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
	 * @param param
	 * @return
	 */
	List<EventBean> queryEventPageList3(Map<String, Object> param);
	
	/**
	 * 从t_cur_ledger_pf查询出所有的小于0的分户下的功率因数
	 * */
	List<EventBean> queryLedgerMinusPF(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	/***
	 * 将功率因数事件存入T_EVENT
	 * @author Yaojiawei
	 * @date 2014-08-26
	 */
	public int savePFEvent(EventBean eventBean);
	
	public int savePFREvent(EventBean eventBean);
	
	/**
	 * 从t_cur_pf查询出所有的小于0的计量点下的功率因数
	 * @author Yaojiawei
	 * @date 2014-08-26
	 * */
	List<EventBean> queryMeterMinusPF(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	/***
	 * 从t_cur_ledger_pf查询出分户下所有的记录低于标准值的上报信息
	 * @author Yaojiawei
	 * @date 2014-08-26
	 */
	List<EventBean> queryLedgerPF(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	/**
	 * 根据分户一个功率因数点，取它的前一个分户功率因数点
	 * */
	EventBean getPreLedgerPF(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime);
	
	/***
	 * 去查数据库，是否该点在一个事件中，判断方法，从t_event中取该分户的功率因数事件，
	 * 而该功率因数事件的时间范围则包含这个时间点
	 */
	EventBean ifLedgerEvent(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime);
	
	/***
	 * 从t_cur_pf查询出计量点下所有的记录低于标准值的上报信息
	 * @author Yaojiawei
	 * @date 2014-08-28
	 */
	List<EventBean> queryMeterPF(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	/**
	 * 根据计量点一个功率因数点，取它的前一个计量点功率因数点
	 * @author Yaojiawei
	 * @date 2014-08-27
	 * */
	EventBean getPreMeterPF(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime);
	
	/***
	 * 去查数据库，是否该点在一个事件中，判断方法，从t_event中取该计量点的功率因数事件，
	 * 而该功率因数事件的时间范围则包含这个时间点
	 * @author Yaojiawei
	 * @date 2014-08-28
	 */
	EventBean ifMeterEvent(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime);
	
	/**
	 * 根据计量点ID和时间戳取功率因数点
	 * */
	EventBean queryMeterPFByIdAndTime(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime);
	
	/**
	 * 根据分户ID和时间戳取功率因数点
	 * */
	EventBean queryLedgerPFByIdAndTime(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime);
	
	/**
	 * 根据事件记录ID删除事件记录
	 * @author qwt
	 * @date 2014-09-18
	 */
	void delEventRecord(@Param("objType")Long objType, @Param("objID")Long objID, @Param("beginTime")Date beginTime);
	
	/**
	 * 查询事件规约配置
	 * 
	 * @param meterId
	 * @param eventId
	 * @return
	 */
	public List<EventSpecCfgBean> queryEventSpecInfo(@Param(value = "meterId") long meterId,
			@Param(value = "eventId") long eventId);
	
	/**
	 * 查询最近发生的事件
	 * 
	 * @param startTime
	 * @return
	 */
	public List<EventBean> queryRecentEventInfo(@Param("startTime") Date startTime);

    /**
     * 得到ct、pt
     * */
    public List<Map<String, BigDecimal>> getCtptByMeter(@Param("pointId") long pointId);

    /**
     * 得到用户 订购的事件的eventId的List
     * */
    public List<Long> getEventBookListByUser(@Param("accountId") long accountId);

    /**
     * 查出反向功率
     * @param meterId
     * @param beginTime
     * @param endTime
     * @return
     */
	List<EventBean> queryMeterReverseRP(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime,
			@Param("endTime")Date endTime);

	/**
	 * 查询反向电流
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String, Object>> queryMeterReverseI(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime,
			@Param("endTime")Date endTime);
	
	/**
	 * 去查数据库，是否该点在一个事件中
	 * @param meterId
	 * @param beginTime
	 * @param eventId
	 * @return
	 */
	List<EventBean> getMeterEvent(@Param("meterId")Long meterId, @Param("beginTime")Date beginTime, @Param("eventId")Long eventId);

	/**
	 * 增加事件
	 * @param eventBean
	 */
	void addEvent(EventBean eventBean);


    /**
     * 根据ID获取事件类型
     */
    Map<String, Object> getEventTypeByID(@Param("eventId") Long eventId);


    /**
     * 查询设置某事件为关注的EMO、DCP
     */
    List<Map<String, Object>> getSettingEmoIdsByEvent(@Param("id") Long id, @Param("objectType") Integer objectType);


    /**
     * 查询该事件是否已在数据库中存在
     */
    int getExistEventNum(EventBean eventBean);

    /**
     * 查询该事件已存在数据库中的recid
     */
    List<Long> getRecIdsByEvent(EventBean eventBean);

    /**
     * 更新事件结束时间
     */
    void updateEventEndtime(@Param("recid") Long recid, @Param("dataTime") Date dataTime);

    void updatePolluteEventEndtime(@Param("startTime") Date startTime, @Param("eventId") Long eventId, @Param("objectId") Long objectId,
                                   @Param("objectType") int objectType, @Param("endTime") Date endTime);


    /**
     * 判断是否法定节假日
     */
    int judgeHoliday(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime);

    /**
     * 判断是否休息日
     */
    Integer judgeRestDay(@Param("ledgerId") Long ledgerId, @Param("weekNumber") int weekNumber);

    /**
     *  根据时间得到该时间在该分户的“工作日休息时段”中的时间段
     */
    List<Map<String, Object>> getTimeRangeByTime(@Param("ledgerId") Long ledgerId, @Param("date") Date date);


    /**
     * 查询该分户 对应的 订阅某事件的用户
     */
    List<Long> getBookedUserByLedgerEvent(@Param("ledgerId") Long ledgerId, @Param("eventId") Long eventId);


    /**
     *  统计分户事件
     */
    List<Map<String, Object>> getLedgerEventCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     *  所有事件类型个数
     */
    int getAllEventIdNum();

    /**
     *  得到EMO的休息日是否包含法定节假日
     */
    int includeDefaultHoliday(@Param("ledgerId") Long ledgerId);

    /**
     * 查询“污染源、治理源”列表
     */
    List<EventPolluteBean> getPolluteResolveList();

    /**
     *  用计算模型计算某个能管对象某个时刻的功率
     */
    Double getOneTimeLedgerPower(@Param("ledgerId") Long ledgerId, @Param("time") Date time);

    /**
     *  得到某个能管对象的计算模型相关参数
     */
    List<Map<String, Object>> getComputeMeters(@Param("ledgerId") Long ledgerId);


    /**
     *  得到某个采集点最近de时刻
     */
    Date getTimeLast(@Param("meterId") Long meterId, @Param("time") Date time);
    /**
     *  得到某个采集点最近时刻的功率
     */
    Double getTimeLastPower(@Param("meterId") Long meterId, @Param("time") Date time);

    /**
     *  得到某个能管对象下，某个时段内的所有事件数
     */
    Long getLedgerEventNum(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime);
	
	
	/**
	 * 查询停上电事件采集点关联的能管对象
	 */
	Map<String, Object> getEmo4pull(@Param("meterId") Long meterId);
	
	
	List<Map<String,Object>> getPullData(Map<String,Object> param);

}
