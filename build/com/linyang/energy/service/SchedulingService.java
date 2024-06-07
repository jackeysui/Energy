package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.dto.FinanceBean;
import com.linyang.energy.model.AssembleaBean;
import com.linyang.energy.model.LineLossBean;
import com.linyang.energy.model.MeterBean;
import net.sf.json.JSONObject;

/**
 * 排班分析业务逻辑层接口
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 18, 2014
 */
public interface SchedulingService {
	
	/**
	 * 班次用能图表
	 * @param queryMap
	 * @return
	 */
	public Map<String,Object> getEnergyUseDatas(HttpServletRequest request,Map<String,Object> queryMap);
	/**
	 * 班次产品产量图表
	 * @param queryMap
	 * @return
	 */
	public Map<String,Object> getProductsDatas(Map<String,Object> queryMap,long scheduleId);
	/**
	 * 得到所有流水线的信息
	 * @return
	 */
	public List<AssembleaBean> getAssembleInfo();
	/**
	 *  得到排班详细信息
	 * @param scheduleId
	 * @return
	 */
	public List<Map<String,Object>> getSchedulerDetail(long scheduleId);
	
	
	/**
	 * 是否正在计算中
	 * @return
	 */
	public boolean isComputing();
	
	
	/**
	 * 计算最优方案,改方法带有锁，只限定本系统只有一个用户可以操作
	 * @param queryMap
	 * @return
	 */
	public boolean computingOptimalSolution(Map<String,Object> queryMap);
	
	/**
	 * 得到计算的最优方案，需要在前台轮询
	 * @return
	 */
	public Map<String,Object> getOptimalSolution();
	
	/**
	 * 得到流水线下分时电价信息
	 * @param meterId
	 * @return
	 */
	public  List<Map<String,Object>> getRatePrice(long assembleId);
	
	/**
	 * 得到电统计折线图信息
	 * @param queryMap
	 * @return
	 */
	public Map<String,Object> getQStat(Map<String,Object> queryMap);
	
	/**
	 * 手动调节
	 * @param bean
	 * @return
	 */
	public Map<String,Object> getFutureQStat(FinanceBean bean);
	
	
	public Map<String,Object> getPowerFactorDatas(Map<String,Object> queryMap);
	
	public Double getFactor(double pf,double rate );
	
	public Map<String, Object> getPowerFactorStat(Map<String,Object> queryMap);
	
	/**
	 * 取负载率统计信息
	 * 
	 * @param queryMap
	 * @return
	 */
	public Map<String, Object> getLoadDataStat(Map<String, Object> queryMap);

	/**
	 * 取负载率曲线
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getCurveLoadData(Map<String, Object> queryMap);

	/**
	 * 取负载率发生时间曲线
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getCurveLoadDataTime(Map<String, Object> queryMap);

    /**
     * 取负载率分布曲线
     *
     * @param queryMap
     * @return
     */
    public List<Map<String, Object>> getCurveDistribution(Map<String, Object> queryMap);
	
	/**
	 * 取电流不平衡统计信息
	 * 
	 * @param queryMap
	 * @return
	 */
	public Map<String, Object> getIUDataStat(Map<String, Object> queryMap);
	
	/**
	 * 取电压不平衡统计信息
	 * 
	 * @param queryMap
	 * @return
	 */
	public Map<String, Object> getVUDataStat(Map<String, Object> queryMap);
	
	/**
	 * 取电流不平衡曲线
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getIUMaxData(Map<String, Object> queryMap);

	/**
	 * 取电压不平衡曲线
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getVUMaxData(Map<String, Object> queryMap);
	
	/**
	 * 取电流不平衡最大值发生时间曲线
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getIUMaxTimeData(Map<String, Object> queryMap);

	/**
	 * 取电压不平衡最大值发生时间曲线
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getVUMaxTimeData(Map<String, Object> queryMap);
	
	/**
	 * 取电流不平衡度越限日累计时间曲线
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getIULimitData(Map<String, Object> queryMap);

	/**
	 * 取电压不平衡度越限日累计时间曲线
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<Map<String, Object>> getVULimitData(Map<String, Object> queryMap);
	
	/**
	 * 根据电能示值计算线损的电量
	 * 
	 * @param queryMap
	 * @return
	 */
	public List<LineLossBean> getDayLineLossData(Map<String, Object> queryMap);
	
	/**
	 * 取一个线损的最大级数
	 * 
	 * @param ledgerId
	 * @return
	 */
	public Integer getLineMaxLevel(long ledgerId,int lossType);
	
	/**
	 * 根据分户id查询计量总表
	 * @param ledgerId
	 * @return List<MeterBean>
	 */
    public List<MeterBean> querySummaryMeterByLedgerId(long ledgerId);

    /**
     * 节能量统计 -- 数据查询
     */
    public Map<String, Object> getSaveEnergyData(Long ledgerId, Integer selectType, String beginTime, String endTime);

    /**
     * 节能量统计 -- 导出
     */
    public void exportSaveEnergyExcel(ServletOutputStream outputStream, JSONObject json);
}
