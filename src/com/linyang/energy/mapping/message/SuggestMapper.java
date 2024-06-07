package com.linyang.energy.mapping.message;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.RecordBean;
import com.linyang.energy.model.ReplyBean;

public interface SuggestMapper {

	/**
	 * 分页查询反馈信息列表
	 * 
	 * @param param
	 *            查询条件
	 * @return List<ReplyBean>
	 */
	List<RecordBean> getSuggestPageList(Map<String, Object> param);

	/**
	 * 管理员获取reply表未处理信息
	 * 
	 * @return
	 */
	Long getReplyNumsForAdmin(Long ledgerId);

	/**
	 * 管理员获取record表未处理信息
	 * 
	 * @return
	 */
	Long getRocordNumsForAdmin(Long ledgerId);

	/**
	 * 用户获取未处理信息数量
	 * 
	 * @param accountId
	 * @return
	 */
	Long getSuggestNumsForUsers(Long accountId);

	/**
	 * 查看用户的gusId是否已经存在 (accountId)
	 * 
	 * @param accountId
	 * @return
	 */
	Long toViewUserSugIdIsExist(Long accountId);

	/**
	 * 查看用户的gusId是否已经存在 (openId)
	 * 
	 * @param accountId
	 * @return
	 */
	Long toViewSugIdIsExist(String openId);

	/**
	 * 获取聊天记录
	 * 
	 * @param accountId
	 * @param sugId
	 * @param pageNo
	 * @return
	 */
	List<ReplyBean> getChatRecordByAccountId(@Param("accountId") Long accountId,@Param("openId") String openId,@Param("sugId") Long sugId,
			@Param("pageNo") Integer pageNo);


	/**
	 * 插入record表
	 * 
	 * @param recordBean
	 * @return
	 */
	Integer interpositionRecord(RecordBean recordBean);

	/**
	 * 插入reply表
	 * 
	 * @param replyBean
	 * @return
	 */
	Integer interpositionReply(ReplyBean replyBean);

	/**
	 * 修改reply表记录状态 0未回复,1已回复
	 * 
	 * @param sugId
	 * @return
	 */
	Integer updateReplyStatus(@Param("sugId") Long sugId, @Param("accountId") Long accountId);

	/**
	 * 修改record表记录状态 0未回复,1已回复
	 * 
	 * @param sugId
	 * @return
	 */
	Integer updateRecordStatus(@Param("sugId") Long sugId, @Param("accountId") Long accountId);

	/**
	 * 导出excel
	 * 
	 * @param sugId
	 * @return
	 */
	List<ReplyBean> getExcelList();

	/**
	 * 查看改用户是否已经推送
	 * 
	 * @param accountId
	 * @return
	 */
	Map<String, Object> isPush(Long accountId);

	/**
	 * 修改isPush
	 * 
	 * @param accountId
	 * @return
	 */
	Integer updateIsPush(@Param("push") Integer push, @Param("accountId") Long accountId);

	/**
	 * 用户最后发送消息的时间
	 * 
	 * @param accountId
	 * @return
	 */
	Map<String, Object> lastDate(Long accountId);

	/**
	 * 获取扫入第一个终端创建出的用户
	 * @author      dingy
	 * @param openId
	 * @return      java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date        2018/7/12 13:07
	 */
	Map<String, Object> getAppUserForOpenId(String openId);
}
