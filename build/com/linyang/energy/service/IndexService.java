package com.linyang.energy.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.CompanyDisplaySet;
import com.linyang.energy.model.EventBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.RealCurveBean;
import com.linyang.energy.model.RegionBean;
import com.linyang.energy.model.SuggestBean;

/**
 * @Description 首页Service
 * @author Leegern
 * @date Dec 16, 2013 3:13:35 PM
 */
public interface IndexService {
	
	/**
	 * 查询系统事件
	 * @param param 查询参数
	 * @param page  分页参数
	 * @return
	 */
	List<EventBean> querySysEventInfo(Map<String, Object> param, Page page);
	
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
	List<RealCurveBean> queryRealCurveInfo(Map<String, Object> param) throws ParseException;
	
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
	Map<String, Object> queryElecBillInfo(Map<String, Object> param);
	
	/**
	 * 查询能耗排名
	 * @param param 查询参数
	 * @return
	 */
	List<Map<String, Object>> queryUseEnergyRanking(Map<String, Object> param);
	
	/**
	 * 查询本月用能分布
	 * @param param 查询参数
	 * @return
	 */
	Map<String, List<Map<String, Object>>> queryCurrMonthEnergyDist(Map<String, Object> param);
	
	/**
	 * 获取企业描述
	 * @param   ledgerId 分户Id
	 * @return 
	 */
	Map<String, String> getEnterpriseDesc(long ledgerId);
	
	/**
	 * 获取第一个图的数据
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Map<String, Object> queryChart1Data(long ledgerId);
	
	/**
	 * 获取第二个图的数据
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<Map<String, Object>> queryChart2Data(long ledgerId);

    public Map<String, Object> queryChart2DataNew(long ledgerId);
	
	/**
	 * 获取第四个图的数据
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Map<String, Object> queryChart4Data(long ledgerId);
	
	/**
	 * 获取第五个图的数据---日电量
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Map<String, Object> queryChart5Data(long ledgerId);
	
	/**
	 * 获取第六个图的数据
	 * 
	 * @param ledgerId
	 * @return
	 */
	public List<Map<String, Object>> queryChart6Data(long ledgerId);
	
	/**
	 * 获取第七个图的数据
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Map<String, Object> queryChart7Data(long ledgerId);

    public Map<String, Object> getChart7DataNew(long ledgerId);
	
	/**
	 * 获取第八个图的数据
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Map<String, Object> queryChart8Data(long ledgerId);
	
	/**
	 * 获取第三个图的数据
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Map<String, Object> queryChart3Data(long ledgerId);

	public Map<String, Object> queryChart3DataNew(long ledgerId);
    
    public Map<String, Object> getChart3DataPartner(long ledgerId);

	List<Map<String, Object>> getTotal1Data(Map<String, Object> map);

	List<Map<String, Object>> getTotal2Data(Map<String, Object> map);

	Map<String, Object> getTotal3Data(Map<String, Object> map);

	Map<String, Object> getTotal4Data(Map<String, Object> map);

    /**
     * 保存轮显分户配置
     * */
    void saveLyConfig(String data);

    /**
     * 得到所有分户轮显配置
     * */
    List<Map<String, Object>> getLedgerLyConfig();
    
    
    Map<String, Object> getShowLedgerUrls(Long ledgerId);
    
    
    /**
	 * 用户提交建议
	 * 
	 * @param ledgerId
	 * @return
	 */
	public boolean addSug(SuggestBean suggest); 
	
	/**
	 * 获取用户已提交建议
	 */
	List<SuggestBean> getSugInfo(SuggestBean suggest);

	/**
	 * 得到企业下面选中的部门
	 * @param ledgerId
	 * @return
	 */
	List<Long> getCompanyRelation(Long ledgerId);
	
	/**
	 * 得到企业下面的部门
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getChildLedger(Long ledgerId);

	/**
	 * 保存大屏幕设置
	 * @param ledgerId
	 * @param departAry
	 * @param menu
	 * @param gatherShow
	 * @param gather
	 * @param depart
	 */
	void saveScreen(Long ledgerId, List<Long> departAry, Integer menu,
			Integer gatherShow, Integer gather, Integer depart);

	CompanyDisplaySet showScreenSet(Long ledgerId);

    /**
     * 电工首页--本月能耗总览
     */
    Map<String, Object> indexEnergyAll(Long ledgerId);

    /**
     * 缓存首页数据
     */
	void SaveHomePageData();
	void SavePlatHomePageData();

    /**
     * 计算某个时间段内的EMO的最大需量值
     */
    Double getLedgerMaxPwr(Long ledgerId, Date beginTime, Date endTime);

    /**
     * 平台运营商首页，获取数据
     */
    Map<String, Object> getPlatAdminIndexData(Long ledgerId, Long accountId);
    Map<String, Object> getPlatAdminIndexEnergy(Long ledgerId);
    Map<String, Object> getOnlineCompanyList(Long ledgerId);
    Map<String, Object> getOnlineCompanys(Long ledgerId);
    Map<String, Object> getOnlineMeterList(Long ledgerId);
    Map<String, Object> getOnlineMeters(Long ledgerId);
    Map<String, Object> getOnlinePlats(Long ledgerId);
    Map<String, Object> getLastAndThisMonQ(Long ledgerId);
    Map<String, Object> getRealTimeAp(Long ledgerId);

    /**
     * 企业列表
     * @param requstParams
     * @return
     */
	List<LedgerBean> getCompanyPageList(Map<String, Object> requstParams);
	
	/**
	 * 根据父区域id获取指定级别子区域, 若父区域id为0, 则获取该级别全部区域
	 * @param parentId	父区域id
	 * @return
	 */
	List<RegionBean> getSubRegions(int level, String parentId);

}
