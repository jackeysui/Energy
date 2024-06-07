package com.linyang.energy.mapping;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.*;
import org.apache.ibatis.annotations.Param;

/**
 * @Description 首页Mapper
 * @author Leegern
 * @date Dec 16, 2013 3:12:56 PM
 */
public interface IndexMapper {

	/**
	 * 查询系统事件
	 * @param param 查询参数
	 * @return
	 */
	List<EventBean> querySysEventPageInfo(Map<String, Object> param);
	
	/**
	 * 查询计量点统计信息
	 * @param meterStatus 计量点状态 0:停止;1:正常
	 * @return
	 */
	List<Map<String, Object>> queryMeterCountInfo(Map<String, Object> param);
	
	/**
	 * 查询实时曲线统计数据(电水汽)
	 * @param param
	 * @return
	 */
	List<RealCurveBean> queryRealCurveInfo(Map<String, Object> param);
	
	/**
	 * 查询实时曲线统计数据(各种峰值)
	 * @param param
	 * @return
	 */
	Map<String, Object> getRealCurveMaxData(Map<String, Object> param);
	
	/**
	 * 查询电费信息(本月和上月)
	 * @param param 查询参数
	 * @return
	 */
	List<ElecBillBean> queryElecBillInfo(Map<String, Object> param);
	
	/**
	 * 查询能耗排名、用能分布
	 * @param param 查询参数
	 * @return
	 */
	List<Map<String, Object>> queryUseEnergyRanking(Map<String, Object> param);
	
//	/**
//	 * 查询本月用能分布
//	 * @param param 查询参数
//	 * @return
//	 */
//	List<Map<String, Object>> queryCurrMonthEnergyDist(Map<String, Object> param);
	
	/**
	 * 根据分户Id查询描述信息
	 * @param ledgerId 分户Id
	 * @return
	 */
	LedgerBean getEnterpriseDesc(long ledgerId);
	
	/**
	 * 查询分户月电量
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double queryLedgerMonQ(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 查询分户费率月电量
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerFeeMonQ(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	/**
	 * 查询分户费率月电量汇总
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerFeeMonRateQ(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 查询分户日最大功率曲线
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerDayMaxPwr(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);


    /**
     * 查询分户月最大功率曲线
     *
     * @param ledgerId
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> queryLedgerMonMaxPwr(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
                                                          @Param("endTime") Date endTime);
    /**
     *查询分户月最大需量
     *
     * @param ledgerId
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<Map<String, Object>> getMonthMaxDemand(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
            @Param("endTime") Date endTime);

	/**
	 * 查询分户最大功率
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double queryLedgerMaxPwr(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 查询分户最大电流
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double queryLedgerMaxI(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 查询分户日电量之和
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double queryLedgerDayQ(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 查询分户日无功电量之和
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double queryLedgerDayRQ(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 查询测量点时间范围内电量
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> queryPointMonQ(@Param("pointId") long pointId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	/**
	 * 获取分户下的测量点、加减属性、百分比属性
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<Map<String, Object>> getLedgerMeters(@Param("ledgerId") long ledgerId);
	
	/**
	 * 获取分户的额定功率
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Double getLedgerEPwr(@Param("ledgerId") long ledgerId);
	
	/**
	 * 获取分户最大功率的发生时间
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @param value
	 * @return
	 */
	public Date getLedgerMaxTime(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime, @Param("value") double value);
	
	/**
	 * 查询分户费率电量
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerFeeCoul(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 查询分户费率电量最大值
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double queryLedgerMaxFeeCoul(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 查询分户总电量
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerQ(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);


    /**
     * 查询分户光伏近30天电量
     *
     */
    public List<Map<String, Object>> getLightDaysPower(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
                                    @Param("endTime") Date endTime);

	/**
	 * 查询分户总电量
	 * 
	 * @param pointId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Double queryLedgerMaxQ(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);

	/**
	 * 查询分户功率曲线
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> queryLedgerCurAp(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	/**
	 * 取分户电压最大最小值
	 * 
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> queryLedgerMaxMinVol(@Param("ledgerId") long ledgerId, @Param("beginTime") Date beginTime,
			@Param("endTime") Date endTime);
	
	public List<Map<String, Object>> getTotal1Data(Map<String,Object> map);
	
	public Double getTotal2Data(Map<String,Object> map);
	
	public List<Map<String, Object>> getTotal3Data(Map<String,Object> map);
	
	public List<Map<String, Object>> getTotal4Data(Map<String,Object> map);

    /**
     *   删除轮显分户配置
     * */
    public void deleteLyConfig();
    /**
     *   保存轮显分户配置
     * */
    public void insertLyConfig(@Param("ledgerId") long ledgerId, @Param("second") int second);
    /**
     *   得到所有分户轮显配置
     * */
    public List<Map<String, Object>> getLedgerLyConfig();
    public List<Map<String,Object>> getLedgerIdsConfig();
    
    /**
     * 用户提交建议
     */
    int addSug(SuggestBean suggest);
    
    /**
     * 获取已提交的建议
     */
    public List<SuggestBean> getSugInfo(SuggestBean suggest);
    
    /**
	 * 得到企业下面的部门
	 * @param ledgerId
	 * @return
	 */
    List<Map<String, Object>> getChildLedger(Long ledgerId);

    /**
	 * 得到企业下面被选中的部门
	 * @param ledgerId
	 * @return
	 */
	List<Long> getCompanyRelation(Long ledgerId);

	/**
	 * 删除企业下面被选中的部门
	 * @param ledgerId
	 */
	void delCompanyRelation(@Param("ledgerId")Long ledgerId);

	/**
	 * 添加企业下面被选中的部门
	 * @param ledgerId
	 */
	void addCompanyRelation(@Param("ledgerId")Long ledgerId, @Param("departId")Long departId);

	/**
	 * 得到大屏设置
	 * @param ledgerId
	 * @return
	 */
	CompanyDisplaySet getScreenSet(@Param("ledgerId")Long ledgerId);

	/**
	 * 新增大屏设置
	 * @param newSet
	 */
	void addScreenSet(CompanyDisplaySet newSet);

	/**
	 * 修改大屏设置
	 * @param newSet
	 */
	void updateScreenSet(CompanyDisplaySet newSet);
	
	/**
	 * 得到光伏发电的电表个数
	 * @param ledgerId
	 * @return
	 */
	Integer getPhotovoltaic(@Param("ledgerId")Long ledgerId);

	List<Map<String, Object>> getTotal11Data(Map<String, Object> curparam);


    /**
     * 得到分户某个属性（电、水、气、热）、某个时段内的用量
     *
     *更具 meter示值表 计算
     */
    Double getLedgerOneUseByStat(@Param("ledger")LedgerBean ledger, @Param("meterType")Integer meterType, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);

    // 20230201-首页-峰平谷耗电监测>尖峰平谷时耗电量 = T_DAY_LEDGER_Q_RATE.RATE_NUMBER=1 &&DAY_FAQ...
	Double getSYLedgerOneUseByStat(@Param("ledgerId")Long ledgerId, @Param("meterType")Integer meterType, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	Double getSYLedgerOneUseByQRate(@Param("ledgerId")Long ledgerId, @Param("meterType")Integer meterType, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	Double getSYLedgerOneUseByQRate2(@Param("ledgerId")Long ledgerId, @Param("meterType")Integer meterType, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);


    /**
     * 获取企业分户的水价
     *
     */
    public Double getLedgerWaterPrice(@Param("ledgerId") long ledgerId);

    /**
     * 获取企业分户的气价
     *
     */
    public Double getLedgerGasPrice(@Param("ledgerId") long ledgerId);

    /**
     * 获取企业分户的热价
     *
     */
    public Double getLedgerHotPrice(@Param("ledgerId") long ledgerId);

    /**
     * 得到用户所属的分户ID
     * @return
     */
	List<Long> getLedgerIdByAccount(@Param("analyType") Integer analyType);

	/**
	 * 保存分户负荷
	 * @param bean
	 */
	void saveLedgerLoad(LedgerLoadBean bean);

	/**
	 * 读取分户近30天负荷数据
	 * @param ledgerId
	 * @return
	 */
	LedgerLoadBean queryLedgerLoad(long ledgerId);

	/**
	 * 删除分户需量所有数据
	 */
	void deleteLedgerDemand();

	/**
	 * 保存分户需量
	 * @param lastMonBean
	 */
	void saveLedgerDemand(LedgerDemandBean lastMonBean);

	/**
	 * 读取分户需量
	 * @param ledgerId
	 * @return
	 */
	List<LedgerDemandBean> queryLedgerDemand(long ledgerId);


    /**
     * 计算某个时间段内的EMO的最大需量值
     *
     */
    public Double getLedgerMaxPwr(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);


    /**
     * 某个能管对象下，未处理的服务报告数
     */
    Long getLedgerReportNum(@Param("ledgerId") Long ledgerId);

    /**
     * 平台管理员首页，用电负荷
     */
    List<Map<String, Object>> getLedgerPower(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 某段时间，平台登陆次数
     */
    Long getTotalLoginTimes(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 企业在线数曲线
     */
    List<Map<String, Object>> getLedgerOnlineList(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
    List<Map<String, Object>> getLedgerOnlineQuick(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 在线分户数
     */
    Long getLedgerOnlineNum(@Param("ledgerId") Long ledgerId, @Param("analyType") Integer analyType, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 监测点在线数曲线
     */
    List<Map<String, Object>> getMeterOnlineList(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
    List<Map<String, Object>> getMeterOnlineQuick(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 在线监测点数
     */
    Long getMeterOnlineNum(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 活跃企业数
     */
    Long getActiveCompanyNum(Map<String, Object> queryMap);

    /**
     * 入库：平台运营商在线企业和监测点表
     */
    void addPlatOnline(@Param("ledgerId") Long ledgerId, @Param("begin") Date begin, @Param("ledgerCount") Long ledgerCount, @Param("meterCount") Long meterCount);

    /**
     * 查询 平台运营商在线企业和监测点表 条数
     */
    int getPlatOnlineNum(@Param("ledgerId") Long ledgerId, @Param("begin") Date begin);

    /**
     * 地图数据
     */
    List<Map<String, Object>> getBdMapDataList(@Param("ledgerId") Long ledgerId);

    List<ParentRegion> getRegionLedgers(@Param("ledgerId") Long ledgerId, @Param("regionLevel") Integer regionLevel);

    ParentRegion getTopRegionByChild(@Param("regionId") String regionId, @Param("regionLevel") Integer regionLevel);

    /** 根据regionId 获取子regionId */
    List<String> getSubRegionIds(@Param("regionId") String regionId);

    /** 根据筛选条件获取首页跳转至的企业列表 */
	List<LedgerBean> getCompanyPageList(@Param("accountId") Long accountId, @Param("companyName") String companyName,
			@Param("operator") String operator, @Param("regionIds") List<String> regionIds, @Param("page") Page page);

	/** 根据分户获取获取电表的离线数及总数(t_cur_e_total表中前45min是否有数据判断) */
	List<Map<String, Object>> getCompanyMeterOnlineData(@Param("ledgerList") List<LedgerBean> ledgerList, @Param("now") Date now);
	
	/** 根据父区域id获取指定级别子区域, 若父区域id为0, 则获取该级别全部区域 */
	List<RegionBean> getSubRegions(@Param("level") int level, @Param("parentId") String parentId);
}
