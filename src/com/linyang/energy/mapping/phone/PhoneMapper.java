package com.linyang.energy.mapping.phone;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.*;

import org.apache.ibatis.annotations.Param;

/**
 * 手机接口数据访问层接口
 * 
 * @author:gaofeng
 * @date:2014.8.18
 */
public interface PhoneMapper {
	/**
	 * 查询测量点曲线功率因数
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getPointCurPFData(Map<String, Object> param);
	/***
	 * 
	
	* @Title: getLedgerPFData 
	
	* @Description: 查询分户功率因数
	
	* @param @param objId
	* @param @param baseDate
	* @param @param dateType
	* @param @return    设定文件 
	
	* @return Double    返回类型 
	
	* @throws
	 */

	/**
	 * 查询分户功率因数数据
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getLedgerPFData(Map<String, Object> param);

	/***
	 * 
	
	* @Title: getLedgerPFData2 
	
	* @Description: 查询分户的某天的功率因数
	
	* @param @param param
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	* @author Yaojiawei
	 */
	public Double getLedgerPFData2(Map<String, Object> param);

	/**
	 * 查询测量点有功功率
	 * 
	 * @author guosen
	 * @data 2014-8-19
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getPointCurAPData(Map<String, Object> param);

	/**
	 * 查询测量点无功功率
	 * 
	 * @author guosen
	 * @data 2014-8-19
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getPointCurRPData(Map<String, Object> param);

	/**
	 * 根据用户登录名得到用户信息
	 * @param username 登录用户名
	 * @author chengq 201-08-19
	 * @return UserBean
	 */
	public UserBean getUserByUserName(@Param("userName")String userName);
	
	/**
	 * 根据用户ID得到用户信息
	 * @param accountId 登录用户Id
	 * @author chengq 201-08-19
	 * @return UserBean
	 */
	public UserBean getUserByAccountId(@Param("accountId")Long accountId);
	
	/**
	 * 更新用户信息
	 * @param userBean 登录用户
	 * @author chengq 201-08-19
	 * @return UserBean
	 */
	public void updateUserInfo(UserBean userBean);
	
	/**
	 * 获取用户群组分页数据
	 * @param queryInfo 查询条件
	 * @author chengq 201-08-19
	 * @return UserBean
	 */
	public List<GroupBean> getPageUserGroupByType(Map<String, Object> queryInfo);
	
	/**
	 * 获取群组的组员分户信息列表
	 * @param groupId 群组id
	 * @author chengq 201-08-20
	 * @return
	 */
	public List<LedgerBean> getLedgerByGroupId(@Param("groupId")Long groupId);
	
	/**
	 * 获取群组的组员计量点信息列表
	 * @param groupId 群组id
	 * @author chengq 201-08-20
	 * @return
	 */
	public List<MeterBean> getMeterByGroupId(@Param("groupId")Long groupId);
	
	/**
	 * 根据条件获取分户分页数据
	 * @param queryInfo 查询条件
	 * @author chengq 201-08-19
	 * @return List<LedgerBean>
	 */
	public List<LedgerBean> getPageLedger(Map<String, Object> queryInfo);
	
	/**
	 * 根据分户Id获取分户信息
	 * @param ledgerId 分户Id
	 * @author chengq 201-08-19
	 * @return UserBean
	 */
	public LedgerBean getLedgerById(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 获取测量点Id或分户Id获取测量点列表
	 * @author chengq 201-08-19
	 * @return  
	 */
	public List<MeterBean> getMeterListByLedgerOrMeterId(@Param("objId")Long objId,@Param("objType")Integer objType);

    /**
     * 由分户Id获取级别level的计量点
     */
    public List<Map<String, Object>> getMeterListByLeger(@Param("ledgerId")Long ledgerId, @Param("level")int level);

	/**
	 * 查询测量点三相电压曲线
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getPointVol(Map<String, Object> param);
	public List<Map<String, Object>> getNeedPointVol(Map<String, Object> param);

	/**
	 * 查询测量点三相电流曲线
	 * 
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getPointCur(Map<String, Object> param);
	public List<Map<String, Object>> getNeedPointCur(Map<String, Object> param);

	/**
	 * 
	
	* @Title: getLedger96EnergyData 
	
	* @Description: 查询分户96点电量
	
	* @param @param param
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	 */
	public List<Map<String, Object>> getLedger96EnergyData(Map<String, Object> param);
	
	/***
	 * 
	
	* @Title: getLedgerEnergyData 
	
	* @Description: 查询分户电量方法
	
	* @param @param objId
	* @param @param objType
	* @param @param baseDate
	* @param @param dateType
	* @param @param dataType
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	* @author Yaojiawei
	 */
	public List<Map<String, Object>> getLedgerEnergyData(Map<String, Object> param);
    /***
	* @Description: 查询分户电量方法2，由尖峰平谷值计算
	* @author xuesai
	 */
    public List<Map<String, Object>> getLedgerEnergyData2(Map<String, Object> param);
	
	/***
	 * 
	
	* @Title: getLedgerSumEnergyData 
	
	* @Description: 时间范围内的分户电量之和
	
	* @param @param param
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	* @author Yaojiawei
	 */
	public Double getLedgerSumEnergyData(Map<String, Object> param);
	/***
	 * 
	
	* @Title: getPoint96EnergyData 
	
	* @Description: 查询计量点96点电量方法
	
	* @param @param param
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	 */
	public List<Map<String, Object>> getPoint96EnergyData(Map<String, Object> param);

	/**
	 * 
	
	* @Title: getPointEnergyData 
	
	* @Description: 查询计量点电量方法
	
	* @param @param param
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	 */
	public List<Map<String, Object>> getPointEnergyData(Map<String, Object> param);
    
    /**
	* @Description: 查询计量点电量方法
    * @author xuesai
	 */
	public List<Map<String, Object>> getPointEnergyData2(Map<String, Object> param);
	
	/**
	 * 
	
	* @Title: getRecentAP 
	
	* @Description: 查最近一点的有功功率
	
	* @param @param objId 分户ID
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	 */
	public Double getRecentAP(@Param("objId")long objId, @Param("curdate")Date curdate);
	
	/***
	 * 
	
	* @Title: getRecentRP 
	
	* @Description: 查最近一点的无功功率
	
	* @param @param objId
	* @param @return    设定文件 
	
	* @return List<Map<String,Object>>    返回类型 
	
	* @throws
	 */
	public Double getRecentRP(@Param("objId")long objId, @Param("curdate")Date curdate);
	
	/**
	 *根据分户查询递归所有子分户
	 *@author chengq
	 *@date 2014-8-28
	 *@param
	 *@return
	 */
	List<LedgerBean> queryPageRecursiveLedgerById(Map<String,Object> queryInfo);
	
	/**
	 * 根据meterID得到电源接线方式1,三相三线;2,三相四线;3:单相表
	 * @param meterId
	 * @return
	 */
	public Integer queryCommMode(Long meterId);

	/**
	 * 根据meterId得到ledgerId
	 * @param meterId
	 * @return
	 */
	public Long getLedgerIdByMeterId(Long meterId);

    /**
     * 按时间段获得月功率因数
     * @return
     */
    public List<Map<String, Object>> getMonLedgerFactor(Map<String,Object> queryMap);

    /**
     * 按时间段获得日功率因数
     * @return
     */
    public List<Map<String, Object>> getDayLedgerFactor(Map<String,Object> queryMap);

    /**
     * 按时间段获得当天功率因数
     * @return
     */
    public List<Map<String, Object>> getMinuteLedgerFactor(Map<String,Object> queryMap);

    /**日最大需量分析图表数据 */
    public List<Map<String, Object>> queryDayMDData(Map<String,Object> queryMap);
    /**月最大需量分析图表数据 */
    public List<Map<String, Object>> queryMonMDData(Map<String,Object> queryMap);
    public List<Double> getDeclareVal(@Param("pointId")long pointId, @Param("time")String time);


    /**
     * 根据新闻ID获取新闻
     */
    public List<Map<String, Object>> getNewsById(@Param("infoId") Long infoId);

    /**
     * 根据类型获取新闻、政策分页列表
     */
    public List<Map<String, Object>> getNewsPageList(Map<String,Object> queryMap);

    /**
     * 根据用户Id获取事件 分页列表
     */
    public List<EventBean> getEventPageList(Map<String,Object> queryMap);

    /**
     * 根据用户Id获取自定义消息分页列表
     */
    public List<Map<String, Object>> messagePageList(Map<String,Object> queryMap);

    /**
     * 根据用户Id获取报告分页列表
     */
    public List<Map<String, Object>> reportPageList(Map<String,Object> queryMap);

    /**
     * 根据用户Id获取自定义消息、报告分页列表
     */
    public List<Map<String, Object>> getMessageReportPageList(Map<String,Object> queryMap);

    /**
     * 根据ID获取报告
     */
    public List<Map<String, Object>> getReportById(@Param("reportId") Long reportId);


    /**
     * 根据ID获取用户自定义消息
     */
    public List<Map<String, Object>> getMessagebyId(@Param("messageId") Long messageId);


    /**
     * 根据accountId获取订购记录
     */
    public List<Map<String, Object>> getBookIdByAccount(@Param("accountId") Long accountId, @Param("bookType") int bookType);

    /**
     * 根据meterId获取计量点级
     */
    public Integer getMeterLevelById(@Param("meterId") Long meterId);

    /**
     * 根据父meterId获取子meterId
     */
    public List<Long> getChildMetersByParent(@Param("meterId") Long meterId);

    /**
     * 得到角色为user的、所有企业用户的accountId
     */
    public List<Long> getAllCompAccount(@Param("user") String user);

    /**
     * 某个企业用户从最后一次登陆时间到现在的报告数
     */
    public Integer getReportNumByUser(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("ledgerId") Long ledgerId);

    /**
     * 某个企业用户从最后一次登陆时间到现在的自定义消息数
     */
    public Integer getDefineMessageNum(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 某个企业用户从最后一次登陆时间到现在的新闻、政策数
     */
    public Integer getNewsPlicyNum(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 某个企业用户从最后一次登陆时间到现在的事件数
     */
    public Integer getEventNum(@Param("accountId") Long accountId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("ledgerId") Long ledgerId);

    /**
     * 得到用户当前等级积分数
     * */
    public Integer getUserCurScore(@Param("accountId") Long accountId);

    /**
     * 根据用户ID获取最近一次用电评估记录
     */
    public List<Map<String, Object>> getUserLastAssessment(@Param("accountId") Long accountId);

    /**
     *  得到所有参加体检的用户数
     **/
    public Integer getAllCheckNum();

    /**
     *  得到击败的用户数
     **/
    public Integer getBeatCheckNum(@Param("accountId") Long accountId, @Param("score") Integer score);

    /**
     * 得到分户的有功功率
     * @param param
     * @return
     */
	public List<Map<String, Object>> getLedgerAPData(Map<String, Object> param);

	/**
	 * 得到分户的无功功率
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getLedgerRPData(Map<String, Object> param);

	/**
	 * 获取分户计算模型中的计量点列表
	 * @param objId
	 * @return
	 */
	public List<MeterBean> getEMOModel1(@Param("ledgerId")Long ledgerId);

	/**
	 * 获取分户下挂显示计量点列表
	 * @param objId
	 * @return
	 */
	public List<MeterBean> getEMOModel2(@Param("ledgerId")Long ledgerId);

	/**
	 * 获取费率电量
	 * @param objType
	 * @param objId
	 */
	public List<Map<String,Object>> getRateEnergy(Map<String, Object> param);

	/**
	 * 获取App用户偏好设置数据
	 * @param accountId
	 * @param ledgerId
	 * @param type
	 * @return
	 */
	public AppPreferBean getAppPrefer(@Param("accountId")long accountId,@Param("type")int type);
	
	/**
	 * 新增App用户偏好数据
	 * @param accountId
	 * @param ledgerId
	 * @param config
	 * @param type
	 * @return
	 */
	public void insertAppPrefer(@Param("accountId")long accountId,@Param("config")String config,@Param("type")int type);
	
	/**
	 * 修改App用户偏好数据
	 * @param accountId
	 * @param ledgerId
	 * @param config
	 * @param type
	 * @return
	 */
	public void updateAppPrefer(@Param("accountId")long accountId,@Param("config")String config,@Param("type")int type);
	
	//-------------------------------微信-----------------------------
	/**
	 * 查询时间范围内的分户无功电量之和
	 * @param objId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Double getLedgerSumRQ(@Param("objId")Long objId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	/**
	 * 查询时间范围内报警次数(事件数)
	 * @param ledgerId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Integer getEventNumById(@Param("ledgerId")Long ledgerId,@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);
	
	/**
	 * 获取最高分
	 * @return
	 */
	public Integer getFirstScore();
	
	/**
	 * 获取排名前二的分数
	 * @return
	 */
	public List<Map<String, Object>> getSecondScores();
	
	/**
	 * 获取排名前三的分数
	 * @return
	 */
	public List<Map<String, Object>> getThirdScores();
	
	/**
	 * 获取排名前十的分数
	 * @return
	 */
	public List<Map<String, Object>> getTenthScores();
	
	/**
	 * 获取用户最新一次体检分数
	 * @param accountId
	 * @return
	 */
	public Map<String, Object> getUserLastScores(@Param("accountId")long accountId);
	
	/**
	 * 获取为企业的accountId
	 * @return
	 */
	public List<Long> getAccountIdByLedger();
	
	/**
	 * 获取实时功率因数
	 * @param ledgerId
	 * @return
	 */
	public Double getCurrentPF(@Param("ledgerId")long ledgerId);
	
	/**
	 * 根据ledgerId获取企业所属地
	 * @param ledgerId
	 * @return
	 */
	public String getRegionById(@Param("ledgerId")long ledgerId);
	
	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param username
	 *            用户名
	 * @return
	 */
	public UserBean findUserByLoginName(@Param("loginName") String loginName);
	
	//-----------------------------微信小程序-------------------------------
	/**
	 * 根据终端id查询能效平台对应的计量点id
	 * @param terminalId
	 * @return
	 */
	public Long getMeterIdByTerId(@Param("terId")Long terId);
	
	/**
	 * 根据终端地址获取终端激活状态
	 * @param terminalId 终端地址
	 * @return
	 */
	public Integer getTerActiveStatus(@Param("terminalAddress")Long terminalAddress);
	
	/**
	 * 根据终端地址更新终端激活状态
	 * @param terminalAddress 终端地址
	 */
	public void updateTerActiveStatus(@Param("terminalAddress")Long terminalAddress);
	
	/**
	 * 添加采集平台终端信息
	 * @param terId
	 * @param terAddress
	 * @param createDate
	 * @param terName
	 * @param operTime
	 */
	public void addTerminal(@Param("terId")Long terId, @Param("terAddress")Long terAddress, @Param("createDate")Date createDate,
			@Param("terName")String terName, @Param("operTime")Date operTime);
	
	/**
	 * 添加能效终端信息
	 * @param terId
	 * @param terAddress
	 * @param terName
	 */
	public void addEnergyTer(@Param("terId")Long terId, @Param("terAddress")Long terAddress,
			@Param("terName")String terName);
	
	/**
	 * 添加计量点信息
	 * @param meterId
	 * @param meterName
	 * @param ledgerId
	 * @param terId
	 * @param mpedId
	 */
	public void addMeter(@Param("meterId")Long meterId, @Param("meterName")String meterName, @Param("ledgerId")Long ledgerId, @Param("terId")Long terId, @Param("mpedId")Long mpedId);

	/**
	 * 插入计量点级别关系
	 * @param meterId
	 */
	public void addLinelossMeter(@Param("meterId")Long meterId);
	
	/**
	 *  插入总加组t_ledger_relation
	 * @param recId
	 * @param meterId
	 * @param meterName
	 * @param ledgerId
	 */
	public void addLedgerRelation(@Param("recId")Long recId, @Param("meterId")Long meterId, @Param("meterName")String meterName, @Param("ledgerId")Long ledgerId);
	
	/**
	 * 插入t_ledger_meter关系表
	 * @param ledgerId
	 * @param meterId
	 */
	public void addLedgerMeter(@Param("ledgerId")Long ledgerId, @Param("meterId")Long meterId);
	
	/**
	 * 加入表计展示表t_ledger_show
	 * @param ledgerId
	 * @param meterId
	 */
	public void addLedgerShow(@Param("ledgerId")Long ledgerId, @Param("meterId")Long meterId);
	
	/**
	 * 绑定终端
	 * @param openId
	 * @param terId
	 * @param ledgerId
	 * @param accountId
	 */
	public void bindTerminal(@Param("openId")String openId, @Param("terId")Long terId, @Param("ledgerId")Long ledgerId, @Param("accountId")Long accountId);

	/**
	 * 查看该openid信息是否已存在 
	 * @param openId
	 * @return
	 */
	public Integer getOpenNumById(@Param("openId")String openId);
	
	/**
	 * 插入微信信息表
	 * @param openId
	 */
	public void addOpenInfo(@Param("openId")String openId, @Param("accountId")Long accountId);
	
	/**
	 * 更新微信信息表
	 * @param openId
	 * @param companyName
	 * @param tel
	 * @param address
	 */
	public void updateOpenInfo(@Param("openId")String openId, @Param("companyName")String companyName, 
			@Param("tel")String tel, @Param("address")String address);
	
	/**
	 * 获取微信信息
	 * @param openId
	 * @return
	 */
	public UserTerminalBean getOpenInfo(@Param("openId")String openId);
	
	
	/**
	 * 获取微信绑定信息
	 * @param terId
	 * @return
	 */
	public UserTerminalBean getOpenTerInfo(@Param("terId")Long terId);
	
	/**
	 * 获取终端信息列表
	 * @param openId
	 * @return
	 */
	public List<UserTerminalBean> getTerminalInfoList(@Param("openId")String openId);
	
	/**
	 * 检查终端名称是否重复
	 * @param terName
	 * @return
	 */
	public Integer checkTerName(@Param("terName")String terName);
	
	/**
	 * 检查ledger名称是否重复
	 * @param terName
	 * @return
	 */
	public Integer checkLedgerName(@Param("terName")String terName);
	
	/**
	 * 更改终端名称 
	 * @param terId
	 */
	public void updateTerName(@Param("terId")Long terId, @Param("terName")String terName);
	
	/**
	 * 更改终端对应的ledger名称
	 * @param terId
	 * @param terName
	 */
	public void updateTerLedgerName(@Param("terId")Long terId, @Param("terName")String terName);
	
	/**
	 * 更改分享限制天数 
	 * @param terId
	 * @param shareLimit
	 */
	public void updateShareLimit(@Param("terId")Long terId, @Param("shareLimit")Integer shareLimit);
	
	/**
	 * 更改终端密码(能效平台用户密码) 
	 * @param accountId
	 */
	public void updateTerPassword(@Param("accountId")Long accountId, @Param("password")String password);
	
	/**
	 * 更改电压电流互感器倍率
	 * @param terId
	 */
	public void updatePtCt(@Param("terId")Long terId, @Param("pt")Integer pt, @Param("ct")Integer ct);
	
	/**
	 * 删除计量点
	 * @param ledgerId
	 */
	public void deleteMeter(@Param("ledgerId")Long ledgerId);
	
	/**
	 * 删除用户
	 * @param accountId
	 */
	public void deleteUser(@Param("accountId")Long accountId);
	
	/**
	 * 删除终端
	 * @param terId
	 */
	public void deleteTerminal(@Param("terId")Long terId);
	
	/**
	 * 删除微信终端关系
	 * @param terId
	 */
	public void deleteOpenTer(@Param("terId")Long terId);
	
	/**
	 * 删除采集平台mpoint
	 * @param terId
	 */
	public void deleteMpoint(@Param("terId")Long terId);
	
	/**
	 * 删除终端
	 * @param terAddress
	 */
	public void setTerActiveStatus(@Param("terAddress")Long terAddress);
	
	/**
	 * 调用采集平台存储过程
	 * @param terId
	 * @param terAddress
	 */
	public void pCollPar(@Param("terId")Long terId, @Param("terAddress")Long terAddress);
	
	/**
	 * 获取终端绑定的账户密码
	 * @param terAddress
	 * @return
	 */
	public String getPasswordByTer(@Param("terAddress")Long terAddress);
	
	/**
	 * 获取终端信息
	 * @param terAddress
	 * @return
	 */
	public UserTerminalBean getTerminalInfo(@Param("terAddress")Long terAddress);
	
	/**
	 * 生成唯一地址，插入云终端地址表
	 * @param cloudId
	 */
	public void createTerAddress(@Param("cloudId")Long cloudId);
	
	/**
	 * 获取云终端地址列表
	 * @param cloudId
	 * @return
	 */
	public List<Long> getCloudAddressList(@Param("cloudId")Long cloudId);
	
	/**
	 * 获取云终端当前的总示值
	 * @param objId
	 * @return
	 */
	public Double getFaeValue(@Param("objId")Long objId);

    /**
     * 第三方企业获取数据：曲线数据
     */
    public List<Long> getGroupUserLedgerIds(@Param("accountId")Long accountId);
    public List<Map<String, Object>> getNeedCurApData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurRpData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurPfData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurQData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurAngleVData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurAngleIData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurDData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurHarVData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurHarIData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurEData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurWflowData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedCurWData(Map<String, Object> param);
    /**
     * 第三方企业获取数据：日数据
     */
    public List<Map<String, Object>> getNeedDayQData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayQRateData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayEData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayERateData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayHarVData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayHarIData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayDData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayWData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayW2Data(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayGData(Map<String, Object> param);
    public List<Map<String, Object>> getNeedDayG2Data(Map<String, Object> param);
    
    // add or update method by catkins.
    // date 2020/3/2
    // Modify the content:
	
	
	/**
	 * 查询能管对象月费率电量
	 * @param ledgerId
	 * @param baseDate
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerFeeCoul(@Param("ledgerId") Long ledgerId,@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endDate,@Param( "rateId" )Long rateId);
	
	public Double queryLedgerMaxFeeCoul(@Param("ledgerId") Long ledgerId,@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endDate,@Param( "rateId" )Long rateId);
	
	
	/**
	 * 查询能管对象月费率电量
	 * @param ledgerId
	 * @param baseDate
	 * @return
	 */
	public List<Map<String, Object>> getMonERateData(@Param("ledgerId") Long ledgerId,@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endDate,@Param( "rateId" )Long rateId);
	
	public Double getMonEMaxRateData(@Param("ledgerId") Long ledgerId,@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endDate,@Param( "rateId" )Long rateId);
	
	/**
	 * 查询测量点月费率电量
	 * @param meterId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<Map<String,Object>> getMeterMonERateData(@Param("meterId") Long meterId,@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endDate);
	
	public Double getMeterMonEMaxRateData(@Param("meterId") Long meterId,@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endDate);
	
	/**
	 * 查询测量点日费率电量
	 * @param meterId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<Map<String,Object>> getMeterDayERateData(@Param("meterId") Long meterId,@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endDate);
	
	public Double getMeterDayEMaxRateData(@Param("meterId") Long meterId,@Param( "beginDate" ) Date beginDate,@Param( "endDate" ) Date endTime);
 
    
    
	public List<Map<String,Object>> queryLedgerFeeSum(@Param("ledgerId") Long objId,@Param( "beginTime" ) Date beginTime,@Param( "endTime" ) Date endTime,@Param("dateType")Integer dateType,@Param( "rateId" )Long rateId);
	
	
	public List<Map<String,Object>> queryMeterFeeSum(@Param("meterId") Long meterId,@Param( "beginTime" ) Date beginTime,@Param( "endTime" ) Date endTime,@Param("dateType")Integer dateType);
	//end
	/**
	 * 查询APP电耗数据
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,Object>> queryEnergyData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" ) Date beginTime,@Param( "endTime" ) Date endTime,@Param( "showType" )Integer showType);
	
	public List<Map<String,Object>> queryEnergyData4Parent(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" ) Date beginTime,@Param( "endTime" ) Date endTime,@Param( "showType" )Integer showType);
	
	public LedgerBean queryParentLedger(@Param( "ledgerId" )Long ledgerId);
	
	/**
	 * 查询变压器列表信息
	 * @param ledgerId
	 * @param beginTime
	 * @return
	 */
	public List<Map<String,Object>> queryTransformerByLedgerId(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "showType" )String showType);
	
	/**
	 * 根据meterId查询变压器详细信息
	 * @param equipId
	 * @return
	 */
	public Map<String,Object> queryTransformerData(@Param( "equipId" )Long equipId);
	
	
	public Integer modifyTrans(@Param( "objId" ) long objId,@Param( "startTime" ) Date startTime,@Param( "stopTime" ) Date stopTime,@Param( "runStatus" ) String runStatus);
	
	/**
	 * 查询变压器列表信息
	 * @param ledgerId
	 * @param beginTime
	 * @return
	 */
	public List<TransformerBean> queryTransformerByShowType(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "showType" )String showType);
	
	
	public List<Map<String,Object>> queryProductionList(@Param( "objId" )Long objId,@Param( "baseDate" )Date baseDate,@Param( "showType" )Integer showType);
	
	
	public List<CapacityDeclarationBean> queryproductionList4APP(@Param( "objId" )Long objId,@Param( "baseDate" )Date baseDate);
	
	/**
	 * 保存产量申报信息
	 * @param param
	 * @return
	 */
	public Integer declarePorduction(Map<String,Object> param);
	
	/**
	 * 查询行业基准数据表
	 * @return
	 */
	public List<Map<String,Object>> queryTradeBenData(String tradeCode);
	
	/**
	 * 查询能效详情页面数据
	 * @param objId
	 * @param objType
	 * @param baseDate
	 * @param dateType
	 * @return
	 */
	public List<Map<String,Object>> queryDetailData(@Param( "objId" ) Long objId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询企业最大电耗
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryLedgerMaxPWIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询企业最小电耗
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryLedgerMinPWIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询企业最大电费
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryLedgerMaxFeeIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询企业最小电费
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryLedgerMinFeeIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询企业平均"电耗","电费"
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryLedgerAvgIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询测量点吨电耗最大值
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryMeterMaxPWIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询测量点吨电耗最小值
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryMeterMinPWIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询测量点吨电费最大值
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryMeterMaxFeeIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询测量点吨电费最小值
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryMeterMinFeeIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询测量点 吨电耗,吨电费 平均值
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> queryMeterAvgIndustryData(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
