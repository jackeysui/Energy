package com.linyang.energy.service;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.EventRelatedNodeTreeBean;
import com.linyang.energy.model.EventSettingRecBean;
import com.linyang.energy.model.MsgHisBean;
import com.linyang.energy.model.ServiceReportBean;
import com.linyang.energy.model.SubscriberBean;
import com.linyang.energy.model.SuggestBean;

/**
 * 信息订阅接口
 * 
 * @description:
 * @author:gaofeng
 * @date:2015.08.19
 */
public interface MessageService {
	/**
	 * 获取订阅的用户ID
	 * 
	 * @param bookId
	 * @param bookType
	 * @return
	 */
	public List<Long> getBookAccountId(long bookId, int bookType);

	/**
	 * 获取指定分户的订阅的用户ID
	 * 
	 * @param ledgerId
	 * @param bookId
	 * @param bookType
	 * @return
	 */
	public List<Long> getBookAccountIdByLedger(long ledgerId, long bookId, int bookType);

	/**
	 * 获取指定测量点的订阅的用户ID
	 * 
	 * @param meterId
	 * @param bookId
	 * @param bookType
	 * @return
	 */
	public List<Long> getBookAccountIdByMeter(long meterId, long bookId, int bookType);
	
	/**
	 * 批量保存信息订阅信息
	 * 
	 * @param subscriber
	 * @param eventId
	 * @param infoId
	 * @param type
	 *            1、追加选中订阅；2、 删除选中订阅；3、 覆盖订阅
	 */
	public void saveBatchSubscriptionInfo(List<SubscriberBean> subscriber, List<Integer> eventId, List<Integer> infoId, int type);

	/**
	 * 保存信息订阅信息
	 * 
	 * @param accountId
	 * @param eventId
	 * @param infoId
	 */
	public void saveSubscriptionInfo(long accountId, List<Integer> eventId, List<Integer> infoId);
	
	/**
	 * 获取用户订阅的所有信息
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Map<String, Object>> getUserBookInfo(long accountId);
	
	/**
	 * 发送自定义消息
	 * 
	 * @param subscriber
	 * @param title
	 * @param content
	 */
	public void saveDefinedMsg(List<SubscriberBean> subscriber, String title, String content);
	
	public void saveDefinedMsg(Long accountId, String title, String conUrl);


    public List<Long> getSubscriberUsers(List<SubscriberBean> subscriber);

	/**
	 * 发送新闻政策消息
	 * 
	 * @param title
	 * @param type
	 * @param picUrl
	 * @param conUrl
	 */
	public void saveNewsInfo(String title, int type, String picUrl, String conUrl, String content);
	
	/**
	 * 发送服务报告
	 * 
	 * @param ledgerId
	 * @param month
	 * @param content
	 */
	public void saveReportInfo(long ledgerId, Date baseDate, String content);
	
	/**
	 * 获取服务报告需要的数据
	 * 
	 * @param ledgerId
	 * @param baseDate
	 * @return
	 */
	public Map<String, Object> queryReportInfoData(long ledgerId, Date baseDate);
	
	/**
	 * 获取服务报告需要的数据
	 * 
	 * @param reportId
	 * @return
	 */
	public Map<String, Object> queryReportInfoData2(long reportId,long accountId);
	
	/**
	 * 获取服务报告提示
	 * @param reportId
	 * @param accountId
	 * @return
	 */
	public String getReportTips(long reportId,long accountId);
	
	/**
	 * 取分户名称
	 * 
	 * @param ledgerId
	 * @return
	 */
	public String getLedgerName(long ledgerId);

	/**
	 * 取表计名称
	 * 
	 * @param meterId
	 * @return
	 */
	public String getMeterName(long meterId);

	/**
	 * 保存用户自动提醒参数配置
	 * @param nologin
	 * @param news
	 * @param unRead
	 */
	public void saveAutoReminderSet(String nologin, String news, String unRead);

	/**
	 * 得到用户自动提醒参数配置
	 * @param nologin
	 * @param news
	 * @param unRead
	 */
	public Map<String, Object> getAutoReminderSet();

	/**
	 * 推送记录
	 * @param accountId
	 * @param msgId
	 * @param createTime
	 * @param type
	 */
	public void insertMsgHis(long accountId, long msgId, Date createTime, int type);
	
	/**
	 * 获取待推送消息记录
	 * @param accountId
	 * @return
	 */
	public List<MsgHisBean> getMsgHisList(long accountId);
	
	/**
	 * 获取消息详细信息
	 * @param msgId 消息ID
	 * @param type  消息类型：自定义消息1、新闻/政策发布2、3服务报告、4事件
	 * @return
	 */
	public Map<String,Object> getMsgInfo(long msgId,int type);

	/**
	 * 删除推送记录
	 * @param userId
	 * @param msgId
	 * @param date
	 */
	public void deleteMsgHis(Long userId, Long msgId);

	/**
	 * 获取用户反馈列表
	 */
	List<SuggestBean> querySuggestPageList(Map<String, Object> param);
	
	/**
	 * 获取用户反馈列表(不带分页)
	 */
	List<SuggestBean> querySuggestList();
	
	/**
	 * 删除或提交建议
	 */
	public boolean updateReply(Map<String, Object> param);
	
	/**
	 * 得到用户反馈的Excel
	 * @param 参数 table名字sheetName，输出流output，结果集map
	 * @throws Exception 
	 */
	void getSugExcel(String sheetName, OutputStream output,List<SuggestBean> sugList);

	/**
	 * 得到服务报告列表
	 * @param ledgerId
	 * @param ledgerType
	 * @param baseDate
	 * @param status
	 * @return
	 */
	public List<ServiceReportBean> searchSerivceReport(Map<String, Object> param);

	/**
	 * 获取事件参数设置记录
	 * @param page
	 * @param mapQuery
	 * @return
	 */
	public List<EventSettingRecBean> getEventSettingRecPageData(Page page, Map<String, Object> queryMap);

	/**
	 * 获取所有事件参数类型
	 * @return
	 */
	public List<Map<String,Object>> getAllEventTypeList();
    
    /**
	 * 获取所有事件参数类型
     * @param Integer roleType
	 * @return
	 */
	public List<Map<String,Object>> getEventTypeListByRoleType(Integer roleType);

	/**
	 * 获取事件相关节点
	 * @param eventTypeId 
	 * @return
	 */
	public List<EventRelatedNodeTreeBean> getEventRelatedNodes(long legerId,int treeType, long eventTypeId, HttpServletRequest request);

	/**
	 * 保存事件参数设置
	 * @param page
	 * @return
	 */
	public Map<String, Object> saveEventSettingData(Map<String, Object> param);

	/**
	 * 修改事件参数设置
	 * @param page
	 * @return
	 */
	public boolean updateEventSettingData(List<Map<String, Object>> itemMapList);

	/**
	 * 删除事件参数设置
	 * @param page
	 * @return
	 */
	public boolean deleteEventSettingData(List<Long> recIdStrList);

	/**
	 * 自动生成服务报告
	 */
	public void createServiceReport();

	/**
	 * 推送服务报告
	 * @param reportId
	 * @param spAdvise
	 * @param spName
	 * @param spPhone
	 */
	public void pushServiceReport(Long reportId, String spAdvise,
			String spName, String spPhone);
	
	/**
	 * 获取推送记录表所有用户ID
	 * @return
	 */
	public List<Long> getUserIdByMsgHis();
	
	/**
	 * 保存体验用户信息
	 * @param visitorInfo
	 */
	public void insertVisitorRecord(String visitorInfo);
	
}
