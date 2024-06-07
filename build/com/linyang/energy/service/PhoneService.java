package com.linyang.energy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.*;

/**
 * 手机接口业务逻辑层接口
 * 
 * @version:1.0
 * @author:gaofeng
 * @date:2014.8.18
 */
public interface PhoneService {

	/**
	 * 取功率因数数据
	 * 
	 * @param objId
	 * @param objType
	 *            1、分户；2、计量点
	 * @param baseDate
	 * @param dateType
	 *            1、日数据；2、3日数据；3、10日数据；4、30日数据；5、年数据；
	 * @param pageType    0.历史数据  1.首页头部数据
	 * @return
	 */
	public List<Map<String, Object>> getPFData(long objId, int objType, Date baseDate, int dateType,int pageType);
	/**
	 * 
	
	* @Title: getLedgerPFData 
	
	* @Description:  查询分户功率因数
	
	* @param @param objId
	* @param @return    设定文件 
	
	* @return Double    返回类型 
	
	* @throws
	 */
	public Double getLedgerPFData(Long objId, Date startDate, Date endDate, int dateType);

	/**
	 * 取有功功率数据
	 * @author guosen
	 * @data 2014-8-19
	 * @param objId
	 * @param objType
	 * @param baseDate
	 * @param dateType
	 * @return
	 */
	public List<Map<String, Object>> getAPData(long objId, int objType, Date baseDate, int dateType);

	/**
	 * 取无功功率数据
	 * @author guosen
	 * @data 2014-8-19
	 * @param objId
	 * @param objType
	 * @param baseDate
	 * @param dateType
	 * @return
	 */
	public List<Map<String, Object>> getRPData(long objId, int objType, Date baseDate, int dateType);
    
	/**
	 * 根据用户登录名得到用户信息
	 * @param username 登录用户名
	 * @author chengq 201-08-19
	 * @return
	 */
	public UserBean getUserByUserName(String username);
	
	/**
	 * 根据用户ID得到用户信息
	 * @param accountId
	 * @author chengq 201-08-19
	 * @return
	 */
	public UserBean getUserByAccountId(Long accountId);
	
	/**
	 * 更新用户信息
	 *@param userBean 更新信息
	 *@author chengq 2014-8-19
	 *@return
	 */
	public void updateUserInfo(UserBean userBean); 

	/**
	 * 根据条件获取群组分页数据获取
	 * @param queryInfo 查询条件
	 * @author chengq 201-08-19
	 * @return
	 */
	public List<GroupBean> getAllUserTeam(Map<String, Object> queryInfo);
	
	/**
	 * 获取群组的组员信息列表(分户)
	 * @param groupId 群组id
	 * @author chengq 201-08-20
	 * @return
	 */
	public List<LedgerBean> getLedgerByGroupId(Long groupId);
	
	/**
	 * 获取群组的组员信息列表(计量点)
	 * @param groupId 群组id
	 * @author chengq 201-08-20
	 * @return
	 */
	public List<MeterBean> getMeterByGroupId(Long groupId);

	/**
	 * 根据条件获取分户分页列表
	 *@param queryInfo 查询条件
	 *@author chengq 2014-8-19
	 *@return
	 */
	public List<LedgerBean> getPageLedger(Map<String, Object> queryInfo);
	
	/**
	 * 根据分户Id获取分户信息
	 *@param ledgerId 分户Id
	 *@author chengq 2014-8-19
	 *@return
	 */
	public LedgerBean getLedgerById(Long ledgerId);

	/**
	 * 获取测量点Id或分户Id获取测量点列表
	 *@author chengq 2014-8-19
	 *@return
	 */
	public List<MeterBean> getMeterListByLedgerOrMeterId(Long objId,Integer objType);


    /**
     * 由分户Id获取级别为level的计量点
     */
    public List<Map<String, Object>> getMeterListByLeger(Long ledgerId, int level);

	/**
	 * 取曲线电压电流数据
	 * 
	 * @param objId
	 * @param objType
	 * @param baseDate
	 * @param dateType
	 * @param dataType
	 * @return
	 */
	public List<Map<String, Object>> getVolCurData(long objId, int objType, Date baseDate, int dateType, int dataType);
	
	/***
	 * 
	
	* @Title: getEnergyDate 
	
	* @Description: 查询电量方法
	
	* @param @param objId
	* @param @param objType
	* @param @param baseDate
	* @param @param dateType
	* @param @param dataType
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	* @author yaojiawei
	 */
	public List<Map<String, Object>> getEnergyData(long objId, int objType, Date baseDate, int dateType);
	
	/***
	 * 
	
	* @Title: getLedgerEnergyData 
	
	* @Description: 查询分户的电量总和
	
	* @param @param objId
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	 */
	public Double  getLedgerEnergyData(Long objId, Date startDate, Date endDate, int dateType);
	
	/**
	 * 
	
	* @Title: getRecentAP 
	
	* @Description: 查最近一点的有功功率
	
	* @param @param objId 分户ID
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	 */
	public Double getRecentAP(long objId);
	
	/***
	 * 
	
	* @Title: getRecentRP 
	
	* @Description: 查最近一点的无功功率
	
	* @param @param objId
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	 */
	public Double getRecentRP(long objId);
	
	/**
	 *根据分户查询递归所有子分户
	 *@author chengq
	 *@date 2014-8-28
	 *@param
	 *@return
	 */
	List<LedgerBean> queryRecursiveLedgerById(Map<String,Object> queryInfo);
	
	/**
	 * 得到VIP数据
	 * @param dataType
	 * 	数据类型；1、电量数据；2、功率因数数据；3、有功功率；4、无功功率；5、电压；6、电流；
	 * @param objId
	 * 	 查询对象ID
	 * @param objType
	 * 	 对象类型（1、分户；2、计量点）
	 * @param baseDate
	 * 	 基准日期（格式为：yyyy-MM-dd）
	 * @param dateType
	 * 	日期类型（1、日数据；2、3日数据；3、10日数据；4、30日数据；5、年数据；）
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> getPhoneVIPData(int dataType, long objId,
			int objType, String baseDate, int dateType,
			List<Map<String, Object>> list);
	
	/**
	 * 根据meterId得到ledgerId
	 * @param meterId
	 * @return
	 */
	public Long getLedgerIdByMeterId(Long meterId);


    /**
     * 手机端首页所需数据
     * @param qType 查询类型: 0上月 1当月  2当月+上月
     * @return
     */
    public Map<String,Object> showPhoneIndex(UserBean userBean,int qType);


    /**
     * 功率因数分析
     */
    public Map<String, Object> pfAnalysis(Long ledgerId, String flag, String dateStr);


    /**
     * 需量分析
     */
    public Map<String, Object> demandAnalysis(Long pointId, String flag, String dateStr,Integer objType);


    /**
     * 电费分析
     */
    public Map<String, Object> feeAnalysis(Long pointId, Integer objType, String dateStr);


    /**
     * 事件详情接口
     */
    public Map<String, Object> getEventDetail(Map<String, Object> queryMap);

    /**
     * 根据meterId获取计量点级
     */
    public Integer getMeterLevelById(Long meterId);

    /**
     * 线损分析数据
     */
    public Map<String, Object> lossAnalysis(String beginTime, String endTime, Long objectId, int type);

    /**
     * 新闻详情接口
     */
    public Map<String, Object> getNewsDetail(Long infoId);

    /**
     * 根据类型获取新闻、政策分页列表
     */
    public List<Map<String, Object>> getNewsPageList(Map<String, Object> queryMap);

    /**
     * 根据用户Id获取事件 分页列表
     */
    public List<EventBean> getEventPageList(Map<String, Object> queryMap);

    /**
     * 根据用户Id获取报告分页列表
     */
    public List<Map<String, Object>> getReportPageList(Map<String, Object> queryMap);

    /**
     * 根据用户Id获取自定义消息分页列表
     */
    public List<Map<String, Object>> getMessagePageList(Map<String, Object> queryMap);


    /**
     * 根据用户Id获取自定义消息、报告分页列表
     */
    public List<Map<String, Object>> getMessageReportPageList(Map<String, Object> queryMap);

    /**
     * 报告详情接口
     */
    public Map<String, Object> getReportDetail(Long reportId);


    /**
     * 用户自定义消息
     */
    public Map<String, Object> getMessageDetail(Long messageId);


    /**
     * 订购记录接口
     */
    public List<Map<String, Object>> getBookRecord(Long accountId);


    /**
     * 长时间未登陆提醒
     * */
    public void longTimeNotLogin();

    /**
     * 服务报告未读提醒
     * */
    public void reportNotRead();

    /**
     * 检查用户消息是否达到配置的条数
     * */
    public void checkUserMessage(List<Long> list);

    /**
     *  获得用户当前等级、下一等级以及下一等级所需积分
     * */
    public Map<String, Integer> getLevelHelp(Long accountId);

    /**
     *  根据等级获取积分区间
     * */
    public Map<String, Integer> getLevelRegion(int level);

    /**
     *  用户最近一次用电评估记录
     * */
    public Map<String, Object> getLastAssessment(Long accountId);

    /**
     *  计算击败用户百分比
     **/
    public int getBeatUserPercent(Long accountId, Integer score);

    /**
     *  用户最近一次用电评估总分
     * */
    public Integer getLastAssessScore(Long accountId);
    
    /**
     * 得到角色为user的、所有企业用户的accountId
     * @param user
     * @return
     */
    public List<Long> getAllCompAccount(String user);

    /**
     * 获取下级能管对象列表
     * @param queryMap 查询条件
     * @return
     */
    public List<Map<String,Object>> getNextEMO(Map<String,Object> queryMap);
    
    /**
     * 获取下级电表(DCP)列表
     * @param ledgerId
     * @return
     */
    public List<Map<String,Object>> getNextDCP(long ledgerId);
    
    /**
     * 能管对象全路径搜索
     * @param queryMap
     * @return
     */
    public List<Map<String,Object>> getEMOPath(Map<String,Object> queryMap);
    
	/** 
	 * 总电量数据
	 * @return
	 */
	public Map<String,Object> getHeadChartData(UserBean userBean);
	public Map<String,Object> getHeadChartDataByScreen(UserBean userBean, LedgerBean ledgerBean); // 同步大屏数据
	
	/**
	 * App用户数据显示偏好设置
	 * @param accountId 用户Id
	 * @param config   配置
	 * @param type     类型
	 * @return
	 */
	public boolean modifyAppPrefer(long accountId,String config,int type);
	
	/**
	 * 获取App用户偏好设置数据
	 * @param accountId 用户Id
	 * @param type     类型
	 * @return
	 */
	public Map<String,Object> getAppPrefer(long accountId,int type);
	
	/**
	 * 获取App用户偏好设置数据
	 * @param accountId 用户Id
	 * @param type 		类型
	 * @return
	 */
	public AppPreferBean getAppPreferBean(long accountId,int type);
	
	/**
	 * 首页重点关注取消关注
	 * @param accountId
	 * @param objId
	 * @return
	 */
	public boolean disAppPrefer(long accountId, long objId);
	
	/**
	 * 获取消息列表
	 * @param type  消息类型：自定义消息1、新闻/政策发布2、3服务报告、4事件
	 * @return
	 */
	public List<Map<String, Object>> getMsgList(int type);
	
	/**
	 * 获取时间段分户电量汇总
	 * @param ledgerIds 分户Id列表
	 * @param dateBetween
	 * @param dateBetween2
	 * @return
	 */
	public List<Map<String, Object>> getLedgerQ(String ledgerIds,
			Date dateBetween, Date dateBetween2);
	
	
	//-------------------------------微信-----------------------------
	/**
	 * 查询时间范围内的分户无功电量之和
	 * @param objId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Double getLedgerSumRQ(Long objId, Date startDate, Date endDate);
	
	/**
	 * 查询时间范围内报警次数(事件数)
	 * @param ledgerId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Integer getEventNumById(Long ledgerId, Date beginDate, Date endDate);
	
	/**
	 * 微信首页体检tips
	 * @param score
	 * @return
	 */
	public String getScoreTips(Long accountId, Integer score);
	
	/**
	 * 获取用户最新一次体检分数
	 * @param accountId
	 * @return
	 */
	public Map<String, Object> getUserLastScores(Long accountId);
	
	/**
	 * 获取为企业的accountId
	 * @return
	 */
	public List<Long> getAccountIdByLedger();
	
	/**
	 * 获取实时功率因数
	 * @return
	 */
	public Double getCurrentPF(long ledgerId);
	
	/**
	 * 根据ledgerId获取企业所属地
	 * @param ledgerId
	 * @return
	 */
	public String getRegionById(long ledgerId);
	
	//----------------------------微信小程序--------------------
	/**
	 * 根据终端id查询能效平台对应的计量点id
	 * @param terminalId
	 * @return
	 */
	public Long getMeterIdByTerId(Long terId);
	
	/**
	 * 根据终端地址获取终端激活状态
	 * @param terminalAddress 终端地址
	 * @return
	 */
	public Integer getTerActiveStatus(Long terminalAddress);
	
	/**
	 * 根据终端地址更新终端激活状态
	 * @param terminalAddress 终端地址
	 */
	public void updateTerActiveStatus(Long terminalAddress);
	
	/**
	 * 插入终端信息
	 * @param terminalAddress
	 */
	public Long addTerminal(Long terAddress);
	
	/**
	 * 插入计量点信息
	 * @param terId
	 * @return
	 */
	public void addMeterRelation(Long terId,  Long terAddress, Long ledgerId);
	
	/**
	 * 绑定终端
	 * @param openId
	 * @param terId
	 * @param ledgerId
	 * @param accountId
	 */
	public void bindTerminal(String openId, Long terId, Long ledgerId, Long accountId);
	
	/**
	 * 插入微信信息表
	 * @param openId
	 */
	public void addOpenInfo(String openId, Long accountId);
	
	/**
	 * 更新微信信息表
	 * @param openId
	 * @param companyName
	 * @param tel
	 * @param address
	 */
	public UserTerminalBean updateOpenInfo(String openId, String companyName, String tel, String address);
	
	/**
	 * 获取微信信息
	 * @param openId
	 * @return
	 */
	public UserTerminalBean getOpenInfo(String openId); 
	
	/**
	 * 获取终端信息列表
	 * @param openId
	 * @return
	 */
	public List<UserTerminalBean> getTerminalInfoList(String openId);
	
	/**
	 * 更新终端信息
	 * @param terId
	 * @param terName
	 * @param password
	 * @param pt
	 * @param ct
	 */
	public String updateTerminalInfo(Long terId, String terName, Integer shareLimit, Integer pt, Integer ct);
	
	/**
	 * 更新终端密码
	 * @param terId
	 * @param password
	 */
	public void updateTerminalPassword(Long terId, String password);
	
	/**
	 * 解除终端绑定操作
	 * @param terId
	 */
	public Long removeBind(Long terId);
	
	/**
	 * 验证终端密码
	 * @param terAddress
	 * @param password
	 * @return
	 */
	public UserTerminalBean checkTerPassword(Long terAddress, String password);
	
	/**
	 * 获取终端信息
	 * @param terAddress
	 * @return
	 */
	public UserTerminalBean getTerminalInfo(Long terAddress);
	
	/**
	 * 生成并获取终端地址
	 * @param num 生成地址数
	 * @return
	 */
	public List<Long> createTerAddress(Integer num, Long cloudId);
	
	/**
	 * 获取云终端当前的总示值
	 * @param objId
	 * @return
	 */
	public Double getFaeValue(Long objId);


    /**
     *  第三方调用：获取曲线数据
     */
    public boolean getIfUserCanGetData(UserBean user, Long objId, Integer objType);

    public List<Map<String, Object>> getNeedCurData(long objId, int objType, int dataType, int density, Date beginDate, Date endDate);

    /**
     *  第三方调用：获取日数据
     */
    public List<Map<String, Object>> getNeedDayData(long objId, int objType, int dataType, Date beginDate, Date endDate);
	
	/**
	 * 查询复费率电量(能管对象)
	 * @param ledgerId
	 * @return
	 */
	public Map<String, Object> queryLedgerFeeData(long ledgerId,Integer dateType,Date baseDate);
	
	/**
	 * 查询复费率电量(测量点)
	 * @param meterId
	 * @return
	 */
	public Map<String, Object> queryMeterFeeData(long meterId,Integer dateType,Date baseDate);
	
	
	public List<Map<String,Object>> queryEnergyData(Long meterId,String baseDate,Integer dateType,Integer showType);

	public LedgerBean queryParentLedger(Long ledgerId);
	
	public List<Map<String,Object>> queryEnergyData4Parent(Long meterId,String baseDate,Integer dateType,Integer showType);
	
	/**
	 * 查询变压器列表信息
	 * @param ledgerId
	 * @param beginTime
	 * @return
	 */
	public List<Map<String,Object>> queryTransformerByLedgerId(Long ledgerId,Date beginTime,String showType);
	
	/**
	 * 根据meterId查询变压器详细信息
	 * @param equipId
	 * @return
	 */
	public Map<String,Object> queryTransformerData(Long equipId);
	
	/**
	 *
	 * @param objId 		变压器id
	 * @param startTime		启用时间
	 * @param stopTime		暂停时间
	 * @param runStatus		运行状态(01启用   02暂停)
	 * @return
	 */
	public Integer modifyTrans(long objId, String startTime,String stopTime,String runStatus);
	
	
	/**
	 * 查询变压器列表信息(根据启停查询)
	 * @param ledgerId
	 * @param beginTime
	 * @return
	 */
	public List<TransformerBean> queryTransformerByShowType(Long ledgerId,Date beginTime,String showType);
	
	
	/**
	 * 查询产量申报列表 + echarts数据
	 * @param objId				能管对象ID
	 * @param baseDate			基准时间
	 * @param showType			展示数据类型(1.全部数据 2.TOP10)
	 * @return
	 */
	public List<Map<String,Object>> queryProductionList(Long objId,Date baseDate,Integer showType);
	
	/**
	 * 查询能管对象下所有106能管对象的测量点
	 * @param objId
	 * @return
	 */
	public List<CapacityDeclarationBean> queryproductionList4APP(Long objId,Date baseDate);
	
	
	public Integer declarePorduction(long meterId,long ledgerId,String DATA_DATE,double yield96,double yield97,double yield98,double yieldOther,double yieldTotal);
	
	
	public Map<String,Object> queryEleData(Map<String,Object> param);
	public Map<String,Object> queryEleData2(Map<String,Object> param); // 更改企业获取计算电费方式

	
	public Map<String,Object> queryEleContrastData(Map<String,Object> param);
	
	/**
	 * 查询行业基准数据表
	 * @return
	 */
	public List<Map<String,Object>> queryTradeBenData(String tradeCode);
	
	
	
	public List<Map<String,Object>> queryDetailData(Long objId,Integer objType,String baseDate,Integer dateType);
	
	
	/**
	 * 线损分析数据
	 */
	public Map<String, Object> lossAnalysis_new(String beginTime, String endTime, Long objectId, int type);
	
	
	public Map<String,Object> queryIndustryData(long ledgerId);
	
}
