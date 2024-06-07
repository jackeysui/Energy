package com.linyang.energy.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.junit.runners.Parameterized.Parameters;

import com.linyang.energy.model.RecordBean;
import com.linyang.energy.model.ReplyBean;

/**
 * 反馈信息接口
 */
public interface SuggestService {

	/**
	 * 分页查询反馈信息列表
	 * 
	 * @param param
	 *            查询条件
	 * @return List<RecodeBean>
	 */
	List<RecordBean> getSuggestPageList(Map<String, Object> param);

	/**
	 * 管理员获取未处理信息数量
	 * 
	 * @return
	 */
	Long getSuggestNumsForAdmin(Long accountId);

	/**
	 * 用户获取未处理信息数量
	 * 
	 * @param accountId
	 * @return
	 */
	Long getSuggestNumsForUsers(Long accountId);

	/**
	 * 查看用户的gusId是否已经存在,并返回sugId (accountId)
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
	Long toViewUserSugIdIsExist(String openId);

	/**
	 * 获取聊天记录
	 * 
	 * @param accountId
	 * @param sugId
	 * @param pageNo
	 * @return
	 */
	List<ReplyBean> getChatRecord(@Param("accountId") Long accountId,@Param("openId") String openId,@Param("sugId") Long sugId, @Param("pageNo") Integer pageNo);

	/**
	 * 插入record表
	 * 
	 * @param recordBean
	 * @return
	 */
	Object interpositionRecord(@Param("accountId") Long accountId, @Param("MSG") String MSG,
			@Param("openId") String openId);

	/**
	 * web插入record表
	 * 
	 * @param recordBean
	 * @return
	 */
	Object interpositionRecordForWeb(@Param("accountId") Long accountId, @Param("MSG") String MSG,
			@Param("sugId") Long sugId, @Param("contactWay") String contactWay);

	/**
	 * 修改记录状态 0未回复,1已回复
	 * 
	 * @param sugId
	 * @return
	 */
	Integer updateStatus(@Param("sugId") Long sugId, @Param("accountId") Long accountId);

	/**
	 * 导出excel
	 * 
	 * @param sugId
	 * @return
	 */
	public void getExcel(String sheetName, OutputStream output, List<ReplyBean> sugList);

	/**
	 * 获取excel数据
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
	Integer updateIsPush(Integer push, Long accountId);

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
