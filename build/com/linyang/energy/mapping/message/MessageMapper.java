package com.linyang.energy.mapping.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.dto.EventRelatedNodeTreeBean;
import com.linyang.energy.model.EventSettingRecBean;
import com.linyang.energy.model.MsgHisBean;
import com.linyang.energy.model.ServiceReportBean;
import com.linyang.energy.model.SuggestBean;

/**
 * 信息订阅数据访问层接口
 * 
 * @author:gaofeng
 * @date:2015.08.19
 */
public interface MessageMapper {
	/**
	 * 获取订阅的用户ID
	 * 
	 * @param bookId
	 * @param bookType
	 * @return
	 */
	public List<Long> getBookAccountId(@Param("bookId") long bookId, @Param("bookType") int bookType);

	/**
	 * 获取指定分户的订阅的用户ID
	 * 
	 * @param ledgerId
	 * @param bookId
	 * @param bookType
	 * @return
	 */
	public List<Long> getBookAccountIdByLedger(@Param("ledgerId") long ledgerId, @Param("bookId") long bookId,
			@Param("bookType") int bookType);

	/**
	 * 获取指定测量点的订阅的用户ID
	 * 
	 * @param meterId
	 * @param bookId
	 * @param bookType
	 * @return
	 */
	public List<Long> getBookAccountIdByMeter(@Param("meterId") long meterId, @Param("bookId") long bookId,
			@Param("bookType") int bookType);

	/**
	 * 获取所属组下面的所有用户ID
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Long> getAccountIdByGroup(@Param("groupId") long groupId);

	/**
	 * 获取所属分户下面的所有用户ID
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<Long> getAccountIdByLedger(@Param("ledgerId") long ledgerId);
	
	/**
	 * 保存订阅的用户
	 * 
	 * @param recId
	 * @param accountId
	 * @param bookId
	 * @param bookType
	 */
	public void insertBookInfo(@Param("recId") long recId, @Param("accountId") long accountId, @Param("bookId") long bookId,
			@Param("bookType") int bookType);
	
	/**
	 * 保存订阅的用户（如果存在则不保存）
	 * 
	 * @param recId
	 * @param accountId
	 * @param bookId
	 * @param bookType
	 */
	public void mergeBookInfo(@Param("recId") long recId, @Param("accountId") long accountId, @Param("bookId") long bookId,
			@Param("bookType") int bookType);
	
	/**
	 * 获取用户订阅的所有信息
	 * 
	 * @param accountId
	 * @return
	 */
	public List<Map<String, Object>> getUserBookInfo(@Param("accountId") long accountId);
	
	/**
	 * 删除用户订阅的所有信息
	 * 
	 * @param accountId
	 */
	public void deleteUserBookInfo(@Param("accountId") long accountId);
	
	/**
	 * 删除一条用户订阅的信息
	 * 
	 * @param accountId
	 * @param bookId
	 * @param bookType
	 */
	public void deleteOneBookInfo(@Param("accountId") long accountId, @Param("bookId") long bookId,
			@Param("bookType") int bookType);
	
	/**
	 * 保存自定义消息内容
	 * 
	 * @param msgId
	 * @param sendTime
	 * @param result
	 * @param title
	 * @param content
	 */
	public void insertDefinedMsg(@Param("msgId") long msgId, @Param("sendTime") Date sendTime, @Param("result") int result,
			@Param("title") String title, @Param("content") String content);
	
	/**
	 * 保存自定义消息发送的用户
	 * 
	 * @param msgId
	 * @param accountId
	 */
	public void insertDefinedMsgAccount(@Param("msgId") long msgId, @Param("accountId") long accountId);
	
	/**
	 * 保存新闻政策信息
	 * 
	 * @param infoId
	 * @param title
	 * @param type
	 * @param picUrl
	 * @param conUrl
	 */
	public void insertNewsInfo(@Param("infoId") long infoId, @Param("title") String title, @Param("type") int type,
			@Param("picUrl") String picUrl, @Param("conUrl") String conUrl, @Param("createTime") Date createTime);
	
	/**
	 * 保存报告
	 * 
	 * @param reportId
	 * @param ledgerId
	 * @param reportTime
	 * @param content
	 */
	public void insertReportInfo(@Param("reportId") long reportId, @Param("ledgerId") long ledgerId,
			@Param("reportTime") Date reportTime, @Param("reportModule") long reportModule, @Param("content") String content,@Param("date")Date createTime, @Param("status") int status);
	
	/**
	 * 取分户电量
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> queryLedgerCoul(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取分户名称
	 * 
	 * @param ledgerId
	 * @return
	 */
	public String getLedgerName(@Param("ledgerId") long ledgerId);

	/**
	 * 取表计名称
	 * 
	 * @param meterId
	 * @return
	 */
	public String getMeterName(@Param("meterId") long meterId);

	/**
	 * 保存用户自动提醒参数配置
	 * @param nologin
	 * @param news
	 * @param unRead
	 */
	public void saveAutoReminderSet(@Param("noLogin")String noLogin, @Param("news")String news, @Param("unRead")String unRead);

	/**
	 * 得到用户自动提醒参数配置
	 * @param nologin
	 * @param news
	 * @param unRead
	 */
	public Map<String, Object> getAutoReminderSet();

	public void deleteAutoReminderSet();
	
	/**
	 * 推送记录
	 * @param accountId
	 * @param msgId
	 * @param createTime
	 */
	public void insertMsgHis(@Param("accountId")long accountId,@Param("msgId")long msgId,@Param("createTime")Date createTime,@Param("msgType")int msgType);
	
	/**
	 * 获取待推送消息记录
	 * @param accountId
	 * @return
	 */
	public List<MsgHisBean> getMsgHisList(@Param("accountId")long accountId);
	
	/**
	 * 获取消息列表
	 * @param type  消息类型：自定义消息1、新闻/政策发布2、3服务报告、4事件
	 * @return
	 */
	public List<Map<String, Object>> getMsgList(@Param("type")int type);

	/**
	 * 获取消息详细信息
	 * @param msgId 消息ID
	 * @param type  消息类型：自定义消息1、新闻/政策发布2、3服务报告、4事件
	 * @return
	 */
	public Map<String, Object> getMsgInfo(@Param("msgId")long msgId, @Param("type")int type);

	/**
	 * 删除推送记录
	 * @param userId
	 * @param msgId
	 * @param date
	 * @return
	 */
	public void deleteMsgHis(@Param("userId")Long userId, @Param("msgId")Long msgId);
	
	/**
	 * 获取用户反馈列表
	 */
	List<SuggestBean> querySuggestPageList(Map<String, Object> param);
	
	/**
	 * 获取用户反馈列表(不带分页)
	 */
	List<SuggestBean> querySuggestList();
	
	/**
	 * 删除或提交回复
	 * @return
	 */
	int updateReply(Map<String, Object> param);

	/**
	 * 得到服务报告列表
	 * @param param
	 * @return
	 */
	public List<ServiceReportBean> searchSerivceReportPageList(
			Map<String, Object> param);

	/**
	 * 分页获取事件参数设置记录
	 * @param mapQuery
	 * @return
	 */
	public List<EventSettingRecBean> getEventSettingRecPageData(Map<String, Object> queryMap);
	
	/**
	 * 获取事件参数设置记录
	 * @param 记录Id
	 * @return
	 */
	public EventSettingRecBean getEventSettingRecByRecId(@Param("recId")long recId);

	/**
	 * 获取所有事件参数类型
	 * @return
	 */
	public List<Map<String,Object>> getAllEventType(@Param("roleType")Integer roleType);

	/**
	 * 获取事件相关节点
	 * @return
	 */
	public List<EventRelatedNodeTreeBean> getEventRelatedNodes(@Param("legerId")long legerId,@Param("treeType")int treeType,@Param("eventTypeId")long eventTypeId);

    /**
	 * 获取所有符合条件的事件参数设置记录
	 * @param queryMap
	 * @return
	 */
	public List<EventSettingRecBean> getFilteredEventSettingRecData(Map<String, Object> queryMap);
    
	/**
	 * 保存事件参数设置
	 * @return
	 */
	public int insertEventSettingData(EventSettingRecBean eventSettingData);

	/**
	 * 保存事件参数设置
	 * @return
	 */
	public int updateEventSettingData(EventSettingRecBean eventSettingData);

	/**
	 * 删除事件参数设置
	 * @return
	 */
	public int deleteEventSettingData(@Param("recId")long recId);
	
	/**
	 * 根据ID得到服务报告
	 * @return
	 */
	public ServiceReportBean queryReportInfoById(@Param("reportId") long reportId);

	/**
	 * 更新服务报告对象
	 * @param param
	 */
	public void updateServiceReport(Map<String, Object> param);
	
	
	/**
	 * 最大负载率
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getMaxLoadByLedgerId(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	/**
	 * 电压不平衡度日均值
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getVolBalanceByLedgerId(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	public List<Map<String, Object>> getVBalanAvgByLedgerId(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	
	/**
	 * 电流不平衡度日均值
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getIBalanceByLedgerId(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	public List<Map<String, Object>> getIBalanAvgByLedgerId(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	/**
	 * 最小功率因素
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getMinPfByLedgerId(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

	/**
	 * 是否生成了服务报告
	 * @param reportModule
	 * @return
	 */
	public int serviceReportIsCreate(@Param("reportModule")Long reportModule);

	/**
	 * 获取推送记录表所有用户ID
	 * @return
	 */
	public List<Long> getUserIdByMsgHis();
	
	/**
	 * 获取ledger的电费率id
	 * @param ledgerId
	 * @return
	 */
	Long getRateIdByLedger(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 查询有无配置标准功率因数
	 * @param ledgerId
	 * @return
	 */
	Integer getThresholdByLedger (@Param("ledgerId")Long ledgerId);
	
	/**
     * 获取ledger下的主变数量
     * @return
     */
    Integer getVolumeType1(@Param("ledgerId")Long ledgerId);
    
    /**
     * 获取ledger下是变压器的meter
     */
    public List<Map<String, Object>> getMeterByVolumeType(@Param("ledgerId")Long ledgerId);
    
    /**
     * 获取meter下配的额定电压数量
     * @param meterId
     * @return
     */
    Integer hasThresholdId1(@Param("meterId")Long meterId);
    
    /**
     * 获取meter下配的额定电流数量
     * @param meterId
     * @return
     */
    Integer hasThresholdId3(@Param("meterId")Long meterId);
    
    /**
     * 获取meter下配的额定功率数量
     * @param meterId
     * @return
     */
    Integer hasThresholdId4(@Param("meterId")Long meterId);
    
    /**
     * 保存体验用户信息
     * @param visitorId
     * @param visitorInfo
     * @param visitorTime
     */
    public void insertVisitorRecord(@Param("visitorId")Long visitorId,@Param("visitorInfo")String visitorInfo,@Param("visitorTime")Date visitorTime);
}
