package com.linyang.energy.service.impl;

import com.leegern.util.CollectionUtil;
import com.leegern.util.NumberUtil;
import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.common.web.page.Page;
import com.linyang.energy.common.CommonResource;
import com.linyang.energy.mapping.IndexMapper;
import com.linyang.energy.mapping.energydataquery.DataQueryMapper;
import com.linyang.energy.mapping.energydataquery.EventQueryMapper;
import com.linyang.energy.mapping.energysavinganalysis.CostMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.*;
import com.linyang.energy.service.*;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.DoubleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.linyang.common.web.common.Log;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 首页Service实现
 * @author Leegern
 * @date Dec 16, 2013 3:14:20 PM
 */
@Service
public class IndexServiceImpl implements IndexService {
	
	/**
	 * 合并类型: 按照分户合并
	 */
	private final static int TOTAL_BY_LEDGER = 1;
	
	/**
	 * 合并类型: 按照计量点类型合并
	 */
	private final static int TOTAL_BY_TYPE = 2;
	
	/**
	 * 计量点类型：电
	 */
	private final static long METER_TYPE_ELEC = 1;
	
	/**
	 * 计量点类型：水
	 */
	private final static long METER_TYPE_WATER = 2;
	
	/**
	 * 计量点类型：气
	 */
	private final static long METER_TYPE_GAS = 3;
	
	
	@Autowired
	private IndexMapper indexMapper;
	@Autowired
	private CostMapper costMapper;
    @Autowired
	private LedgerManagerMapper ledgerManagerMapper;
    @Autowired
    private EventQueryMapper eventQueryMapper;
    @Autowired
    private DataQueryMapper dataQueryMapper;
    @Autowired
    private SuggestService suggestService;
	@Autowired
	private DemandDeclareService declareService;
	@Autowired
	private CostService costService;
	@Autowired
	private PhoneService phoneService;
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.IndexService#querySysEventInfo(java.util.Map)
	 */
	@Override
	public List<EventBean> querySysEventInfo(Map<String, Object> param, Page page) {
		param.put("page", page);
		return indexMapper.querySysEventPageInfo(param);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.IndexService#queryMeterCountInfo(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> queryMeterCountInfo(Map<String, Object> param) {
		return indexMapper.queryMeterCountInfo(param);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.IndexService#queryElecBillInfo(java.util.Map)
	 */
	@Override
	public Map<String, Object> queryElecBillInfo(Map<String, Object> param) {
		// 查询电费信息(本月和上月) 
		List<ElecBillBean> list = indexMapper.queryElecBillInfo(param);
		Map<String, Object> result = new HashMap<String, Object>();
		Double overLast = 0D; // 
		if (CollectionUtil.isNotEmpty(list)) {
			for (ElecBillBean bean : list) {
				if (DataUtil.doubleIsNull(bean.getFeeValue()))
					continue;
				
				// 判断是否是当前月
				if ( ((Date)param.get("beginTime")).getTime() == bean.getStatDate().getTime()) {
					result.put("currentDate", bean.getFeeValue());
				}
				// 判断是否是上一月
				else if ( ((Date)param.get("lastBeginTime")).getTime() == bean.getStatDate().getTime() ) {
					result.put("lastDate", bean.getFeeValue());
				}
				// 判断是否是上一月同期
				else if ( ((Date)param.get("lastEndTime")).getTime() == bean.getStatDate().getTime() ) {
					result.put("lastSameDate", bean.getFeeValue());
				}
			}
			if (result.containsKey("currentDate") && result.containsKey("lastSameDate") && null != result.get("lastSameDate")) {
				// 计算本月当前电费和上月同期比较(环比)
				Double current = (Double)result.get("currentDate");
				Double last = (Double)result.get("lastSameDate");
				// 格式化数字, 保留两位小数
				overLast = NumberUtil.formatDouble(new BigDecimal(current).subtract(new BigDecimal(last)).divide(new BigDecimal(last), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).doubleValue(), NumberUtil.PATTERN_INTEGER);
				result.put("overLast", overLast);
			}
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.IndexService#queryUseEnergyRanking(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> queryUseEnergyRanking(Map<String, Object> param) {
		// 查询能耗排名
		param.put("type", TOTAL_BY_LEDGER);
		List<Map<String, Object>> list = indexMapper.queryUseEnergyRanking(param);
		// 表示计量点类型 0:综合
		if ((Integer)param.get("meterType") == 0 && CollectionUtil.isNotEmpty(list)) {
			return this.processData(list, TOTAL_BY_LEDGER);
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.IndexService#queryCurrMonthEnergyDist(java.util.Map)
	 */
	@Override
	public Map<String, List<Map<String, Object>>> queryCurrMonthEnergyDist(Map<String, Object> param) {
		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String,Object>>>();
		param.put("type", TOTAL_BY_LEDGER);
 		// 查询电水气统计(按分户统计数据)
		List<Map<String, Object>> list = indexMapper.queryUseEnergyRanking(param);  
		param.put("type", TOTAL_BY_TYPE);
		// 查询电水气统计(按测量点类型统计数据)
		List<Map<String, Object>> list2 = indexMapper.queryUseEnergyRanking(param);  
		// 获取通用资源对象
		CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
		if (CollectionUtil.isNotEmpty(list)) {
			for (Map<String, Object> map : list) {
				if (CollectionUtil.isNotEmpty(map)) {
					double elec = Double.valueOf(map.get("Q") != null ? map.get("Q").toString() : "0");                    // 电量
					double water = Double.valueOf(map.get("WATER_FLOW") != null ? map.get("WATER_FLOW").toString() : "0"); // 水量
					double gas = Double.valueOf(map.get("GAS_FLOW") != null ? map.get("GAS_FLOW").toString() : "0");       // 气量
					// 电能量 
					map.put("Q", DataUtil.doubleMultiply(elec, resource.getElecUnit()));
					// 水流量
					map.put("WATER_FLOW", DataUtil.doubleMultiply(water, resource.getWaterUnit()));
					// 气流量
					map.put("GAS_FLOW", DataUtil.doubleMultiply(gas, resource.getGasUnit()));
					// 算综合标准煤
					Double sumValue = new BigDecimal((Double)map.get("Q")).add(new BigDecimal((Double)map.get("WATER_FLOW"))).add(new BigDecimal((Double)map.get("GAS_FLOW"))).doubleValue();
					map.put("SUM_VALUE", sumValue);
				}
			}
		}
		if (CollectionUtil.isNotEmpty(list2)) {
			if (null == list2.get(0)) {
				list2 = new ArrayList<Map<String,Object>>();
			}
			else {
				for (Map<String, Object> map : list2) {
					if (CollectionUtil.isNotEmpty(map)) {
						double elec = Double.valueOf(map.get("Q") != null ? map.get("Q").toString() : "0");                    // 电量
						double water = Double.valueOf(map.get("WATER_FLOW") != null ? map.get("WATER_FLOW").toString() : "0"); // 水量
						double gas = Double.valueOf(map.get("GAS_FLOW") != null ? map.get("GAS_FLOW").toString() : "0");       // 气量
						// 电能量 
						map.put("Q", DataUtil.doubleMultiply(elec, resource.getElecUnit()));
						// 水流量
						map.put("WATER_FLOW", DataUtil.doubleMultiply(water, resource.getWaterUnit()));
						// 气流量
						map.put("GAS_FLOW", DataUtil.doubleMultiply(gas, resource.getGasUnit()));
					}
				}
			}
		}
		result.put("totalByLedger", list);
		result.put("totalByType",   list2);
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.IndexService#queryRealCurveInfo(java.util.Map)
	 */
	@Override
	public List<RealCurveBean> queryRealCurveInfo(Map<String, Object> param) throws ParseException {
		CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
		Map<Long, RealCurveBean> tmpMap = new HashMap<Long, RealCurveBean>();
		List<RealCurveBean> list = null;
		// 曲线类型
		int curveType = (Integer)param.get("curveType");
		// 表示综合
		if (curveType == 4) {
			for (int i = 1; i <= 3; i++) {
				param.put("curveType", i);
				// 查询电水气曲线
				List<RealCurveBean> tmpList = indexMapper.queryRealCurveInfo(param);
				double unit = 0;
				for (RealCurveBean bean : tmpList) {
					// 电转化标准煤
					if (i == 1) {
						unit = resource.getElecUnit();
					}
					// 水转化标准煤
					else if (i == 2) {
						unit = resource.getWaterUnit();
					}
					// 气转化标准煤
					else if (i ==3) {
						unit = resource.getGasUnit();
					}
					long key = bean.getFreezeTime().getTime();
					double dataValue = DataUtil.doubleMultiply(bean.getDataValue(), unit);  // 转换标准煤
					bean.setDataValue(dataValue);
					if (! tmpMap.containsKey(key) || null == tmpMap.get(key)) {
						// 格式化时间
						bean.setStatDate(DateUtil.convertDateToStr(bean.getFreezeTime(), DateUtil.DEFAULT_TIME_PATTERN));
						tmpMap.put(key, bean);
					}
					else {
						tmpMap.get(key).setDataValue(DataUtil.doubleAdd(tmpMap.get(key).getDataValue(), dataValue)); // 累加标准煤
					}
				}
			}
			list = new ArrayList<RealCurveBean>(tmpMap.values());
		}
		else {
			// 查询实时曲线统计数据(电水汽) 
			list = indexMapper.queryRealCurveInfo(param);
			if (CollectionUtil.isNotEmpty(list)) {
				for (RealCurveBean bean : list) {
					if (null != bean.getFreezeTime()) {
						// 格式化时间
						bean.setStatDate(DateUtil.convertDateToStr(bean.getFreezeTime(), DateUtil.DEFAULT_TIME_PATTERN));
					}
				}
			}
		}
		if (CollectionUtil.isNotEmpty(list)) {
			// 时间升序
			Collections.sort(list, new Comparator<RealCurveBean>() {
				@Override
				public int compare(RealCurveBean o1, RealCurveBean o2) {
					return o1.getFreezeTime().getTime() - o2.getFreezeTime().getTime() < 0 ? -1 : 1;
				}
			});
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.IndexService#getRealCurveMaxData(java.util.Map)
	 */
	@Override
	public Map<String, Object> getRealCurveMaxData(Map<String, Object> param) {
		return indexMapper.getRealCurveMaxData(param);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.IndexService#getEnterpriseDesc(long)
	 */
	@Override
	public Map<String, String> getEnterpriseDesc(long ledgerId) {
		// 获取当前用户分户信息
		LedgerBean ledgerBean = indexMapper.getEnterpriseDesc(ledgerId);
		Map<String, String> map = new HashMap<String, String>();
		String remark = "";
		if (null != ledgerBean) {
			// 表示根节点
			if (null == ledgerBean.getParentLedgerId() || 0 == ledgerBean.getParentLedgerId()) {
				remark = ledgerBean.getLedgerRemark();
			}
			else {
				// 递归查询分户描述信息 
				remark = this.getLedgerInfo(ledgerBean.getParentLedgerId());
			}
		}
		map.put("remark", remark);
		return map;
	}
	
	/**
	 * 递归查询分户描述信息
	 * @param ledgerId 分户Id
	 * @return
	 */
	private String getLedgerInfo(long ledgerId) {
		// 根据分户Id查询描述信息
		LedgerBean ledgerBean = indexMapper.getEnterpriseDesc(ledgerId);
		if (null != ledgerBean) {
			if (null == ledgerBean.getParentLedgerId()) {
				return null;
			}
			else if ((ledgerBean.getLedgerRemark() != null) && (ledgerBean.getLedgerRemark().trim().length()>0)) {
				return ledgerBean.getLedgerRemark();
			}
			else {
				getLedgerInfo(ledgerBean.getParentLedgerId());
			}
		}
		return null;
	}
	
	/**
	 * 合并电、水、气统计(换算成标准煤)
	 * @param data 数据项
	 * @param type 合并类型
	 * @return
	 */
	private List<Map<String, Object>> processData(List<Map<String, Object>> data, int type) {
		Map<Long, Map<String, Object>> result = new HashMap<Long, Map<String,Object>>();
		// 获取通用资源对象
		CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
		Map<String, Object> dataMap = null;
		// 按照分户合并
		switch(type){
		case TOTAL_BY_LEDGER:
			for (Map<String, Object> map : data) {
				dataMap = new HashMap<String, Object>();
				long ledgerId = Long.valueOf(map.get("LEDGER_ID").toString());   // 分户Id
				double elec = Double.valueOf(map.get("Q").toString());           // 电量
				double water = Double.valueOf(map.get("WATER_FLOW").toString()); // 水量
				double gas = Double.valueOf(map.get("GAS_FLOW").toString());     // 气量
				// 电能量 
				dataMap.put("Q", DataUtil.doubleMultiply(elec, resource.getElecUnit()));
				// 水流量
				dataMap.put("WATER_FLOW", DataUtil.doubleMultiply(water, resource.getWaterUnit()));
				// 气流量
				dataMap.put("GAS_FLOW", DataUtil.doubleMultiply(gas, resource.getGasUnit()));
				// 算综合标准煤
				Double sumValue = new BigDecimal((Double)dataMap.get("Q")).add(new BigDecimal((Double)dataMap.get("WATER_FLOW"))).add(new BigDecimal((Double)dataMap.get("GAS_FLOW"))).doubleValue();
				if (result.containsKey(ledgerId) && null != result.get(ledgerId)) {
					Map<String, Object> tmpMap = result.get(ledgerId);
					// 同一分户计算和
					tmpMap.put("SUM_VALUE", DataUtil.doubleAdd((Double)tmpMap.get("SUM_VALUE"), sumValue));
				}
				else {
					map.put("SUM_VALUE", sumValue);
					result.put(ledgerId, map);
				}
			}
			break;
		case TOTAL_BY_TYPE:
			result.put(METER_TYPE_ELEC,  new HashMap<String, Object>());         // 电
			result.put(METER_TYPE_WATER, new HashMap<String, Object>());         // 水
			result.put(METER_TYPE_GAS,   new HashMap<String, Object>());         // 气
			for (Map<String, Object> map : data) {
				double elec = Double.valueOf(map.get("Q").toString());           // 电量
				double water = Double.valueOf(map.get("WATER_FLOW").toString()); // 水量
				double gas = Double.valueOf(map.get("GAS_FLOW").toString());     // 气量
				
				Map<String, Object> tmpMap1 = result.get(METER_TYPE_ELEC);
				Map<String, Object> tmpMap2 = result.get(METER_TYPE_WATER);
				Map<String, Object> tmpMap3 = result.get(METER_TYPE_GAS);
				// 电能量
				if (tmpMap1.containsKey("SUM_VALUE")) {
					tmpMap1.put("SUM_VALUE", new BigDecimal(elec).multiply(new BigDecimal(resource.getElecUnit())).add(new BigDecimal((null != tmpMap1.get("Q") ? (Double)tmpMap1.get("Q") : 0))).doubleValue());
				}
				else {
					tmpMap1.put("SUM_VALUE", DataUtil.doubleMultiply(elec, resource.getElecUnit()));
					tmpMap1.put("METER_TYPE", METER_TYPE_ELEC);
				}
				// 水流量
				if (tmpMap2.containsKey("SUM_VALUE")) {
					tmpMap2.put("SUM_VALUE", new BigDecimal(water).multiply(new BigDecimal(resource.getWaterUnit())).add(new BigDecimal((null != tmpMap2.get("WATER_FLOW") ? (Double)tmpMap2.get("WATER_FLOW") : 0))).doubleValue());
				}
				else {
					tmpMap2.put("WATER_FLOW", DataUtil.doubleMultiply(water, resource.getWaterUnit()));
					tmpMap2.put("METER_TYPE", METER_TYPE_WATER);
				}
				// 气流量
				if (tmpMap3.containsKey("SUM_VALUE")) {
					tmpMap3.put("SUM_VALUE", new BigDecimal(gas).multiply(new BigDecimal(resource.getGasUnit())).add(new BigDecimal((null != tmpMap3.get("GAS_FLOW") ? (Double)tmpMap3.get("GAS_FLOW") : 0))).doubleValue());
				}
				else {
					tmpMap3.put("SUM_VALUE", DataUtil.doubleMultiply(gas, resource.getGasUnit()));
					tmpMap3.put("METER_TYPE", METER_TYPE_GAS);
				}
			}
			break;		}
		return new ArrayList<Map<String,Object>>(result.values());
	}

	@Override
	public Map<String, Object> queryChart1Data(long ledgerId) {
		Date baseDate = WebConstant.getChartBaseDate();
		Map<String, Object> map = new HashMap<String, Object>();
		Double mon = indexMapper.queryLedgerMonQ(ledgerId, DateUtil.getPreMonthFristDay(baseDate), DateUtil.getCurrMonthFirstDay(baseDate));
		if (mon != null){
            CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
            map.put("standard", new BigDecimal(mon).multiply(new BigDecimal(resource.getElecUnit())).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
            map.put("co2", new BigDecimal(mon).multiply(new BigDecimal(resource.getEnergyValue())).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }

		return map;
	}

	@Override
	public List<Map<String, Object>> queryChart2Data(long ledgerId) {
		Date baseDate = WebConstant.getChartBaseDate();

		List<Map<String, Object>> r = new ArrayList<Map<String, Object>>();
	    r = indexMapper.queryLedgerFeeMonQ(ledgerId, DateUtil.getPreMonthFristDay(baseDate),
					DateUtil.getCurrMonthFirstDay(baseDate));

		return r;
	}

    @Override
    public Map<String, Object> queryChart2DataNew(long ledgerId) {
        Map<String, Object> result = new HashMap<String, Object>();
        Date baseDate = WebConstant.getChartBaseDate();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Date beginTime = DateUtil.getPreMonthFristDay(baseDate);
        Date endTime = DateUtil.getPreMonthLastDay(baseDate);
        CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
        LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
        //电
        double ele = indexMapper.getLedgerOneUseByStat(ledger, 1, beginTime, endTime);
        result.put("total_ele", ele);
        ele = DataUtil.doubleDivide(DataUtil.doubleMultiply(ele, resource.getElecUnit()), 1000, 2);//换算成标准煤,单位:吨
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("name", "电");
        map1.put("value", ele);
        list.add(map1);
        //水
        double water = indexMapper.getLedgerOneUseByStat(ledger, 2, beginTime, endTime);
        result.put("total_water", water);
        water = DataUtil.doubleDivide(DataUtil.doubleMultiply(water, resource.getWaterUnit()), 1000, 2);//换算成标准煤,单位:吨
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("name", "水");
        map2.put("value", water);
        list.add(map2);
        //气
        double gas = indexMapper.getLedgerOneUseByStat(ledger, 3, beginTime, endTime);
        result.put("total_gas", gas);
        gas = DataUtil.doubleDivide(DataUtil.doubleMultiply(gas, resource.getGasUnit()), 1000, 2);//换算成标准煤,单位:吨
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("name", "气");
        map3.put("value", gas);
        list.add(map3);
        //热
        double hot = indexMapper.getLedgerOneUseByStat(ledger, 4, beginTime, endTime);
        result.put("total_hot", hot);
        hot = DataUtil.doubleDivide(DataUtil.doubleMultiply(hot, resource.getHotUnit()), 1000, 2);//换算成标准煤,单位:吨
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("name", "热");
        map4.put("value", hot);
        list.add(map4);
        result.put("list", list);
        //单位
        int unitFlag = 0;   //单位标识(0表示 默认；1表示 万吨标准煤)
        if(ele > 100000 || water > 100000 || gas > 100000 || hot > 100000){
            unitFlag = 1;
        }
        result.put("unitFlag", unitFlag);
        //总能耗
        double standard = new BigDecimal(ele).add(new BigDecimal(water)).add(new BigDecimal(gas)).add(new BigDecimal(hot)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        result.put("standard", standard);
        //CO2
        double co2 = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, resource.getEnergyValue()), resource.getElecUnit(), 2);
        result.put("co2", co2);

        return result;
    }

	@Override
	public Map<String, Object> queryChart4Data(long ledgerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//从缓存表中读取数据
		List<LedgerDemandBean> list = this.indexMapper.queryLedgerDemand(ledgerId);
		if(list != null && list.size() > 0){
			List<Map<String, Object>> lastMonList = new ArrayList<Map<String,Object>>();
			List<Map<String, Object>> currMonList = new ArrayList<Map<String,Object>>();
			for (LedgerDemandBean ledgerDemandBean : list) {
				int type = ledgerDemandBean.getDataType();
				switch (type) {
					case LedgerDemandBean.lastMonPwr:
						Map<String, Object> lastMap = new HashMap<String, Object>();
						lastMap.put("DAY", ledgerDemandBean.getDay());
						lastMap.put("AP", ledgerDemandBean.getMaxAP());
						lastMonList.add(lastMap);
						break;
					case LedgerDemandBean.currMonPwr:
						Map<String, Object> currMap = new HashMap<String, Object>();
						currMap.put("DAY", ledgerDemandBean.getDay());
						currMap.put("AP", ledgerDemandBean.getMaxAP());
						currMonList.add(currMap);				
						break;
					case LedgerDemandBean.lastMonPwrDate:
						map.put("lastMonPwrDate", ledgerDemandBean.getOccurredTime());
						break;
					case LedgerDemandBean.currMonPwrDate:
						map.put("currMonPwrDate", ledgerDemandBean.getOccurredTime());
						break;
				}
			}
			map.put("lastMonPwr", lastMonList);
			map.put("currMonPwr", currMonList);
		}else {
			//缓存表没有数据，重新计算读取数据
			Date baseDate = DateUtil.clearDate(new Date());
			Date preMon1 = DateUtil.getPreMonthFristDay(baseDate);
			Date preMon2 = DateUtil.getPreMonthLastDay(baseDate);
			Date currMon1 = DateUtil.getCurrMonthFirstDay(baseDate);

			
			List<Map<String, Object>> lastMonPwr = indexMapper.queryLedgerDayMaxPwr(ledgerId, preMon1, preMon2);
			List<Map<String, Object>> currMonPwr = indexMapper.queryLedgerDayMaxPwr(ledgerId, currMon1, baseDate);

			map.put("lastMonPwr", lastMonPwr);
			map.put("currMonPwr", currMonPwr);
			map.put("lastMonPwrDate", indexMapper.getLedgerMaxTime(ledgerId, preMon1, preMon2, getMaxPwr(lastMonPwr)));
			map.put("currMonPwrDate", indexMapper.getLedgerMaxTime(ledgerId, currMon1, baseDate, getMaxPwr(currMonPwr)));
		}

        //计算最大值（供前端页面调节单位用）
        double chartMax = 0D;
        List<Map<String, Object>> lastMonPwr = (List<Map<String, Object>>) map.get("lastMonPwr");
        List<Map<String, Object>> currMonPwr = (List<Map<String, Object>>) map.get("currMonPwr");
        if(!CollectionUtils.isEmpty(lastMonPwr)){
            for(int i = 0; i < lastMonPwr.size(); i++){
                double oneVal = Double.valueOf(lastMonPwr.get(i).get("AP").toString());
                if(oneVal > chartMax){
                    chartMax = oneVal;
                }
            }
        }
        if(!CollectionUtils.isEmpty(currMonPwr)){
            for(int i = 0; i < currMonPwr.size(); i++){
                double oneVal = Double.valueOf(currMonPwr.get(i).get("AP").toString());
                if(oneVal > chartMax){
                    chartMax = oneVal;
                }
            }
        }
        map.put("chartMax", chartMax);

		return map;
	}
	
	private double getMaxPwr(List<Map<String, Object>> pwr) {
		double maxpwr = 0, tmp = 0;

		for (Map<String, Object> p : pwr) {
			if(p.get("AP") != null){
				tmp = Double.valueOf(p.get("AP").toString());
				if (maxpwr < tmp)
					maxpwr = tmp;
			}
		}

		return maxpwr;
	}
	
	@Override
	public Map<String, Object> queryChart5Data(long ledgerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Date baseDate = DateUtil.clearDate(WebConstant.getChartBaseDate());
		Date beginTime = DateUtil.getDateBetween(baseDate, -8);
		Date endTime = DateUtil.getDateBetween(baseDate, -1);
		map.put("list", indexMapper.queryLedgerFeeCoul(ledgerId, beginTime, endTime));
		map.put("max", indexMapper.queryLedgerMaxFeeCoul(ledgerId, beginTime, endTime));
		return map;
	}

	@Override
	public List<Map<String, Object>> queryChart6Data(long ledgerId) {
     	Date baseDate = DateUtil.clearDate(WebConstant.getChartBaseDate());
		Date endDate = DateUtil.setDateEnd(baseDate);
		List<Map<String, Object>> list = indexMapper.queryLedgerCurAp(ledgerId, baseDate, endDate);
        if(CollectionUtil.isNotEmpty(list)){
            list.remove(list.size() - 1);
        }
		return list;
	}
	
	@Override
	public Map<String, Object> queryChart7Data(long ledgerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Date baseDate = DateUtil.clearDate(WebConstant.getChartBaseDate());
		Date beginDate = DateUtil.getDateBetween(baseDate, -30);
		Date endDate = DateUtil.getDateBetween(baseDate, -1);
		map.put("list", indexMapper.queryLedgerQ(ledgerId, beginDate,endDate));
		map.put("max", indexMapper.queryLedgerMaxQ(ledgerId, beginDate,endDate));
		return map;
	}

    @Override
    public Map<String, Object> getChart7DataNew(long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        Date baseDate = DateUtil.clearDate(WebConstant.getChartBaseDate());
        Date beginDate = DateUtil.getDateBetween(baseDate, -30);
        Date endDate = DateUtil.getDateBetween(baseDate, -1);
        List<Map<String, Object>> list1 = indexMapper.queryLedgerQ(ledgerId, beginDate,endDate);
        map.put("list1", list1);
        List<Map<String, Object>> list2 = indexMapper.getLightDaysPower(ledgerId, beginDate,endDate);
        map.put("list2", list2);
        Double max = indexMapper.queryLedgerMaxQ(ledgerId, beginDate,endDate);
        if(list2 != null && list2.size() > 0){
            for(int i = 0; i < list2.size(); i++){
                Map<String, Object> m = list2.get(i);
                Double temp = Double.valueOf(m.get("ELE_VALUE").toString());
                if(temp > max){
                    max = temp;
                }
            }
        }
        map.put("max", max);
        return map;
    }

	@Override
	public Map<String, Object> queryChart8Data(long ledgerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		//从缓存表中读取数据
		LedgerLoadBean bean = this.indexMapper.queryLedgerLoad(ledgerId);
		if(bean != null ){
			map.put("maxPwr", bean.getMaxP());
			map.put("occuredTime", bean.getOccuredTime());
			map.put("maxI", bean.getMaxI());
			map.put("ratio", bean.getRatio());
		}else {
			//缓存表没有数据，重新计算读取数据
			Date baseDate = DateUtil.clearDate(WebConstant.getChartBaseDate());
			Date startDate = DateUtil.getDateBetween(baseDate, -30);
			Date endDate = DateUtil.getDateBetween(baseDate, -1);
			Double maxPwr = indexMapper.queryLedgerMaxPwr(ledgerId, startDate, endDate);
			if (maxPwr != null) {
				map.put("maxPwr", maxPwr);
				map.put("occuredTime", DateUtil.convertDateToStr(indexMapper.getLedgerMaxTime(
						ledgerId, startDate, endDate, maxPwr), DateUtil.MOUDLE_PATTERN));
			}
			map.put("maxI", indexMapper.queryLedgerMaxI(ledgerId, startDate, endDate));
			map.put("ratio", indexMapper.getLedgerEPwr(ledgerId));
		}
		return map;
	}
	
	@Override
	public Map<String, Object> queryChart3Data(long ledgerId) {
		Date baseDate = WebConstant.getChartBaseDate();
        LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
		Map<String, Object> map = new HashMap<String, Object>(3);
		//Map<String, Object> queryMap = new HashMap<String, Object>(3);

		List<Map<String, Object>> meters = this.costMapper.getComputeMeter(ledgerId, ledger.getAnalyType());
		//double preMonFee = 0;
		double	currMonFee = 0;
		//Date preMonStart = DateUtil.getPreMonthFristDay(baseDate);//上月第一天
		//Date preMonEnd = DateUtil.getPreMonthLastDay(baseDate);//上月最后一天
		Date currMonStart = DateUtil.getCurrMonthFirstDay(baseDate);//本月第一天
		int betweenDays = DateUtil.daysBetweenDates(baseDate, currMonStart);//与当前时间相差天数
		double ratio = DataUtil.doubleDivide(betweenDays,DateUtil.getMonthDays(baseDate), 2);

        //Map<String, Object> map_1 = ledgerDemandBaseFee(preMonStart, ledgerId, 1,ledger);
        Map<String, Object> map_2 = ledgerDemandBaseFee(currMonStart, ledgerId, ratio,ledger);
        //Integer declareType1 = Integer.valueOf(map_1.get("declareType").toString());
        Integer declareType2 = Integer.valueOf(map_2.get("declareType").toString());
        if(meters != null && meters.size() > 0){
            for(int i = 0; i < meters.size(); i++){
                Map<String, Object> temp = meters.get(i);
                long pointId = Long.valueOf(temp.get("METER_ID").toString());
                int addAttr = Integer.valueOf(temp.get("ADD_ATTR").toString());
                int percent = Integer.valueOf(temp.get("PCT").toString());
                //preMonFee = DataUtil.doubleAdd(preMonFee, calEleFee(pointId,addAttr,percent,preMonStart,preMonEnd,1, declareType1));
                currMonFee = DataUtil.doubleAdd(currMonFee, calEleFee(pointId,addAttr,percent,currMonStart,baseDate,ratio, declareType2));
            }
        }

		//需量模式时，分户的"基本电费"
        //preMonFee = DoubleUtils.getDoubleValue(DataUtil.doubleAdd(preMonFee, Double.valueOf(map_1.get("bsFee").toString())), 2);
        currMonFee = DoubleUtils.getDoubleValue(DataUtil.doubleAdd(currMonFee, Double.valueOf(map_2.get("bsFee").toString())), 2);
        //分户的"力调电费"
        //Map<String, Object> monEle_1 = costMapper.getLedgerMonthTotalEleValue(ledgerId,ledger.getAnalyType(),preMonStart,preMonEnd);
        Map<String, Object> monEle_2 = costMapper.getLedgerMonthTotalEleValue(ledgerId,ledger.getAnalyType(),currMonStart,baseDate);
        //double pf_1 = 0; //功率因数
        double pf_2 = 0;
        //if(monEle_1 != null){
        //    pf_1 = DataUtil.getPF(Double.valueOf(monEle_1.get("FAQVALUE").toString()), Double.valueOf(monEle_1.get("FRQVALUE").toString()));
        //}
        if(monEle_2 != null){
            pf_2 = DataUtil.getPF(Double.valueOf(monEle_2.get("FAQVALUE").toString()), Double.valueOf(monEle_2.get("FRQVALUE").toString()));
        }
        Double std = costMapper.getEmoThresholdValue(ledgerId); //标准功率因数
        //Double ratePF_1 = null; // 查调整电费幅度
        Double ratePF_2 = null;
        if (std != null) {
            //ratePF_1 = costMapper.getFactor(DoubleUtils.getDoubleValue(DataUtil.doubleSubtract(pf_1, 0.01), 2), std);
            ratePF_2 = costMapper.getFactor(DoubleUtils.getDoubleValue(DataUtil.doubleSubtract(pf_2, 0.01), 2), std);
        }
        /*if (ratePF_1 != null) {
            double price = DoubleUtils.getDoubleValue(ratePF_1, 2);
            preMonFee = DataUtil.doubleAdd(DataUtil.doubleDivide(DataUtil.doubleMultiply(preMonFee, price), 100, 2), preMonFee);
        }*/
        if (ratePF_2 != null) {
            double price = DoubleUtils.getDoubleValue(ratePF_2, 2);
            currMonFee = DataUtil.doubleAdd(DataUtil.doubleDivide(DataUtil.doubleMultiply(currMonFee, price), 100, 2), currMonFee);
        }
		Map<String, Object> param = this.restructuringMap( ledgerId, 1, DateUtil.getCurrentDateStr( DateUtil.SHORT_PATTERN ), 0 );
//		Map<String, Object> param = this.restructuringMap( ledgerId, 1, "2017-04-04", 0 );
		Map<String, Object> feeMap = phoneService.queryEleData( param );
		//map.put("preMonFee", DoubleUtils.getDoubleValue(preMonFee, 2));
        map.put("preMonFee", getPreMonFee(ledgerId));
//		map.put("currMonFee", DoubleUtils.getDoubleValue(currMonFee, 2));
		map.put("currMonFee", DoubleUtils.getDoubleValue(this.calMonFee(feeMap), 2));
		return map;
	}
	
	private Double calMonFee(Map<String,Object> map){
		double totalFee = 0;
		if(map != null){
			if( map.containsKey( "jFee" ) ){
				totalFee += Double.parseDouble( map.get( "jFee" ).toString() );
			}
			if( map.containsKey( "fFee" ) ){
				totalFee += Double.parseDouble( map.get( "fFee" ).toString() );
			}
			if( map.containsKey( "pFee" ) ){
				totalFee += Double.parseDouble( map.get( "pFee" ).toString() );
			}
			if( map.containsKey( "gFee" ) ){
				totalFee += Double.parseDouble( map.get( "gFee" ).toString() );
			}
			if( map.containsKey( "baseFee" ) ){
				totalFee += Double.parseDouble( map.get( "baseFee" ).toString() );
			}
			if( map.containsKey( "adjustFee" ) ){
				totalFee += Double.parseDouble( map.get( "adjustFee" ).toString() );
			}
		}
		return totalFee;
	}
	
	
	
	
	/**
	 * 电费计算所需参数
	 * @param objId		对象id
	 * @param objType	对象类型(1能管对象  2测量点)
	 * @param baseTime	基准时间
	 * @param dateType	时间类型(2前月  1上月  0本月)
	 * @return
	 */
	private Map<String,Object> restructuringMap(Long objId,Integer objType,String baseTime,Integer dateType){
		Map<String,Object> param = new HashMap<String,Object>( 0 );
		param.put( "meterType", 1 );
		param.put( "queryType", 1 );
		param.put( "objId", objId );
		param.put( "objType", objType );
		param.put( "dateType", dateType );
		param.put( "baseTime", baseTime+" 00:00:00" );
		return param;
	}

	private double getPreMonFee(long ledgerId)
	{
		double preMonFee=0d;
		Map<String,Object> queryMap=new HashMap<String,Object>();
		//从能耗成本页面查询数据，让数据保持一致
		queryMap.put("baseTime", DateUtil.getLastMonthFormat());
		queryMap.put("meterType", 0);
		queryMap.put("objId", ledgerId);
		queryMap.put("objType", 1);
		queryMap.put("queryType", 1);
		List<RateFeeBean> rateEles=null;
		Object temp=costService.calEmoDcpEleFee(queryMap).get("fee");
		if(temp!=null) {
			rateEles=(List<RateFeeBean>)temp;
		}
		
		if(rateEles!=null&&rateEles.size()>0) {
			for(RateFeeBean item:rateEles) {
				preMonFee+=item.getFee();
			}
		}
		
		return DoubleUtils.getDoubleValue(preMonFee, 2);
	}



    @Override
    public Map<String, Object> queryChart3DataNew(long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        Date baseDate = WebConstant.getChartBaseDate();
        Date preMonStart = DateUtil.getPreMonthFristDay(baseDate);
        Date preMonEnd = DateUtil.getPreMonthLastDay(baseDate);
        Date currMonStart = DateUtil.getCurrMonthFirstDay(baseDate);
        //电
        double preMonFee = 0, currMonFee = 0;
        LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
        List<Map<String, Object>> meters = this.costMapper.getComputeMeter(ledgerId, ledger.getAnalyType());

        int betweenDays = DateUtil.daysBetweenDates(baseDate, currMonStart);
        double ratio = DataUtil.doubleDivide(betweenDays, DateUtil.getMonthDays(baseDate), 2);

        Map<String, Object> map_1 = ledgerDemandBaseFee(preMonStart, ledgerId, 1,ledger);
        Map<String, Object> map_2 = ledgerDemandBaseFee(currMonStart, ledgerId, ratio,ledger);
        Integer declareType1 = Integer.valueOf(map_1.get("declareType").toString());
        Integer declareType2 = Integer.valueOf(map_2.get("declareType").toString());
        if(meters != null && meters.size() > 0){
            for(int i = 0; i < meters.size(); i++){
                Map<String, Object> temp = meters.get(i);
                long pointId = Long.valueOf(temp.get("METER_ID").toString());
                int addAttr = Integer.valueOf(temp.get("ADD_ATTR").toString());
                int percent = Integer.valueOf(temp.get("PCT").toString());
                preMonFee = DataUtil.doubleAdd(preMonFee, calEleFee(pointId,addAttr,percent,preMonStart,preMonEnd,1, declareType1));
                currMonFee = DataUtil.doubleAdd(currMonFee, calEleFee(pointId,addAttr,percent,currMonStart,baseDate,ratio, declareType2));
            }
        }
        //需量模式时，分户的"基本电费"
        preMonFee = DoubleUtils.getDoubleValue(DataUtil.doubleAdd(preMonFee, Double.valueOf(map_1.get("bsFee").toString())), 2);
        currMonFee = DoubleUtils.getDoubleValue(DataUtil.doubleAdd(currMonFee, Double.valueOf(map_2.get("bsFee").toString())), 2);
        //分户的"力调电费"
        Map<String, Object> monEle_1 = costMapper.getLedgerMonthTotalEleValue(ledgerId,ledger.getAnalyType(),preMonStart,preMonEnd);
        Map<String, Object> monEle_2 = costMapper.getLedgerMonthTotalEleValue(ledgerId,ledger.getAnalyType(),currMonStart,baseDate);
        double pf_1 = 0; //功率因数
        double pf_2 = 0;
        if(monEle_1 != null){
            pf_1 = DataUtil.getPF(Double.valueOf(monEle_1.get("FAQVALUE").toString()), Double.valueOf(monEle_1.get("FRQVALUE").toString()));
        }
        if(monEle_2 != null){
            pf_2 = DataUtil.getPF(Double.valueOf(monEle_2.get("FAQVALUE").toString()), Double.valueOf(monEle_2.get("FRQVALUE").toString()));
        }
        Double std = costMapper.getEmoThresholdValue(ledgerId); //标准功率因数
        Double ratePF_1 = null; // 查调整电费幅度
        Double ratePF_2 = null;
        if (std != null) {
            ratePF_1 = costMapper.getFactor(DoubleUtils.getDoubleValue(DataUtil.doubleSubtract(pf_1, 0.01), 2), std);
            ratePF_2 = costMapper.getFactor(DoubleUtils.getDoubleValue(DataUtil.doubleSubtract(pf_2, 0.01), 2), std);
        }
        if (ratePF_1 != null) {
            double price = DoubleUtils.getDoubleValue(ratePF_1, 2);
            preMonFee = DataUtil.doubleAdd(DataUtil.doubleDivide(DataUtil.doubleMultiply(preMonFee, price), 100, 2), preMonFee);
        }
        if (ratePF_2 != null) {
            double price = DoubleUtils.getDoubleValue(ratePF_2, 2);
            preMonFee = DataUtil.doubleAdd(DataUtil.doubleDivide(DataUtil.doubleMultiply(currMonFee, price), 100, 2), currMonFee);
        }
        map.put("ele_1", DataUtil.doubleDivide(preMonFee, 10000, 2));	//上月电费(单位：万元)
        map.put("ele_2", DataUtil.doubleDivide(currMonFee, 10000, 2));	//本月电费(单位：万元)
        //水、气、热
        Map<String, Double> priceMap = waterGasHotPrice(ledgerId);
        double waterVol_1 = indexMapper.getLedgerOneUseByStat(ledger, 2, preMonStart, preMonEnd);
        double waterVol_2 = indexMapper.getLedgerOneUseByStat(ledger, 2, currMonStart, baseDate);
        map.put("water_1", DataUtil.doubleDivide(DataUtil.doubleMultiply(priceMap.get("waterPrice"), waterVol_1), 10000, 2));
        map.put("water_2", DataUtil.doubleDivide(DataUtil.doubleMultiply(priceMap.get("waterPrice"), waterVol_2), 10000, 2));
        double gasVol_1 = indexMapper.getLedgerOneUseByStat(ledger, 3, preMonStart, preMonEnd);
        double gasVol_2 = indexMapper.getLedgerOneUseByStat(ledger, 3, currMonStart, baseDate);
        map.put("gas_1", DataUtil.doubleDivide(DataUtil.doubleMultiply(priceMap.get("gasPrice"), gasVol_1), 10000, 2));
        map.put("gas_2", DataUtil.doubleDivide(DataUtil.doubleMultiply(priceMap.get("gasPrice"), gasVol_2), 10000, 2));
        double hotVol_1 = indexMapper.getLedgerOneUseByStat(ledger, 4, preMonStart, preMonEnd);
        double hotVol_2 = indexMapper.getLedgerOneUseByStat(ledger, 4, currMonStart, baseDate);
        map.put("hot_1", DataUtil.doubleDivide(DataUtil.doubleMultiply(priceMap.get("hotPrice"), hotVol_1), 10000, 2));
        map.put("hot_2", DataUtil.doubleDivide(DataUtil.doubleMultiply(priceMap.get("hotPrice"), hotVol_1), 10000, 2));
        double thisMonFee = new BigDecimal(DataUtil.doubleAdd(currMonFee, DataUtil.doubleMultiply(priceMap.get("waterPrice"), waterVol_2)))
                .add(new BigDecimal(DataUtil.doubleMultiply(priceMap.get("gasPrice"), gasVol_2))).add(new BigDecimal(DataUtil.doubleMultiply(priceMap.get("hotPrice"), hotVol_2))).doubleValue();
        map.put("thisMonFee", NumberUtil.formatDouble(thisMonFee, NumberUtil.PATTERN_DOUBLE));//本月能耗成本(单位：元)

        return map;
    }

    @Override
    public Map<String, Object> getChart3DataPartner(long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        long companyCount = ledgerManagerMapper.countCompanyLedgerByLedgerId(ledgerId);
        map.put("companyCount",companyCount);
        long pointCount = ledgerManagerMapper.countMeterByLedgerId(ledgerId);
        map.put("pointCount",pointCount);
        long provinceCount = ledgerManagerMapper.countProvinceByLedgerId(ledgerId);
        map.put("provinceCount",provinceCount);
        long cityCount = ledgerManagerMapper.countCityByLedgerId(ledgerId);
        map.put("cityCount",cityCount);
        long partnerCount = ledgerManagerMapper.countPartnerByLedgerId(ledgerId);
        map.put("partnerCount",partnerCount);
        // 能管对象建立时长(排除手工添加数据)
        if(ledgerId > 10000)
        	map.put("runningDay", (System.currentTimeMillis() - ledgerId)/1000/60/60/24);
        else {
			map.put("runningDay", (System.currentTimeMillis() - 1402882435387l)/1000/60/60/24);
		}
        // 上月耗电量
        Date baseDate = WebConstant.getChartBaseDate();
        Double lastMonCountQ = indexMapper.queryLedgerMonQ(ledgerId, DateUtil.getPreMonthFristDay(baseDate), DateUtil.getCurrMonthFirstDay(baseDate));
        if(lastMonCountQ>100000)
		{
        	lastMonCountQ=(double)Math.round(lastMonCountQ/1000*100)*10;
		}
        else {
        	lastMonCountQ=(double)(Math.round(lastMonCountQ*100))/100;
        }

		map.put("lastMonCountQ", lastMonCountQ);
        return map;
    }
    /**
     * 查询企业分户的 水、气、热 单价（没配按0计算）
     *
     */
    private Map<String, Double> waterGasHotPrice(long ledgerId){
        Map<String, Double> result = new HashMap<String, Double>();

        BigDecimal waterPrice = DataUtil.parseDouble2BigDecimal(this.indexMapper.getLedgerWaterPrice(ledgerId));
        if(waterPrice == null || waterPrice.compareTo(BigDecimal.ZERO) <= 0){
            waterPrice = new BigDecimal(0D);
        }
        result.put("waterPrice", waterPrice.doubleValue());
        BigDecimal gasPrice = DataUtil.parseDouble2BigDecimal(this.indexMapper.getLedgerGasPrice(ledgerId));
        if(gasPrice == null || gasPrice.compareTo(BigDecimal.ZERO) <= 0){
            gasPrice = new BigDecimal(0D);
        }
        result.put("gasPrice", gasPrice.doubleValue());
        BigDecimal hotPrice = DataUtil.parseDouble2BigDecimal(this.indexMapper.getLedgerHotPrice(ledgerId));
        if(hotPrice == null || hotPrice.compareTo(BigDecimal.ZERO) <= 0){
            hotPrice = new BigDecimal(0D);
        }
        result.put("hotPrice", hotPrice.doubleValue());
        return result;
    }

    //判断是否是本月
	public boolean thisDate(Date beginTime){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		String date=df.format(new Date());
		String MM=date.substring(4, 6);//截取系统月份
		String beginDate=df.format(new Date());
		String mm=beginDate.substring(4, 6);//截取系统月份
		if(MM.equals(mm)){
			return true;
		}else{
			return false;
		}
	}


    private Map<String, Object> ledgerDemandBaseFee(Date beginTime, Long ledgerId, double ratio,LedgerBean ledger){
        Map<String, Object> result = new HashMap<String, Object>();

        Date endTime = DateUtil.getMonthLastDay(beginTime);
        Map<String, Object> map = new HashMap<String, Object>(3);
		Map<String, Object> queryMap = new HashMap<String, Object>();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        map.put("pointId", ledgerId);
        //容量、需量申报
        Integer declareType = 0;
        Double declareValue = 0D;


		queryMap.put("beginTime", beginTime);
		queryMap.put("meterId", ledgerId);
		if(thisDate(beginTime)) {
			queryMap.put("flag", 0);        //此属性为选择sql语句条件,0为当前月,1为当月以前
		}else{
			queryMap.put("flag", 1);
		}
		// 判断当月有没有用户的需量申报和容量申报
		Map<String, Object> emoDecalreByIdAndBeginTime = declareService.getEmoDecalreByIdAndBeginTime(queryMap);

		// 此处做为如果按时间查询没有数据的话就查询历史申报的最后一次记录
		if (emoDecalreByIdAndBeginTime == null) {
			queryMap.put("flag", 1);
			emoDecalreByIdAndBeginTime = declareService.getEmoDecalreByIdAndBeginTime(queryMap);
		}

        if(emoDecalreByIdAndBeginTime != null){
            if(emoDecalreByIdAndBeginTime.keySet().contains("DECLARETYPE") && emoDecalreByIdAndBeginTime.get("DECLARETYPE") != null){
                declareType = Integer.valueOf(emoDecalreByIdAndBeginTime.get("DECLARETYPE").toString());
            }
            if(emoDecalreByIdAndBeginTime.keySet().contains("DECLAREVALUE") && emoDecalreByIdAndBeginTime.get("DECLAREVALUE") != null){
                declareValue = Double.valueOf(emoDecalreByIdAndBeginTime.get("DECLAREVALUE").toString());
            }
        }
        result.put("declareType", declareType);
        //分户费率
        Map<String, Object> ledgerPrice = costMapper.getLedgerFeePrice(ledgerId);

        ///开始计算“需量模式下”的分户"基本电费"
        double bsFee = 0;
        if(declareType == 2 && ledgerPrice != null && ledgerPrice.size() > 0){
            if (ledgerPrice.get("DERATE") != null && ledgerPrice.get("DETH") != null) {
                double price = Double.valueOf(ledgerPrice.get("DERATE").toString());
                BigDecimal product = (BigDecimal.valueOf(price)).multiply(BigDecimal.valueOf(ratio));
                bsFee = BigDecimal.valueOf(declareValue).multiply(product).doubleValue();
            }
        }

		///开始计算“容量模式下”的分户"基本电费"
		if(declareType == 1 && ledgerPrice != null && ledgerPrice.size() > 0){
			if (ledgerPrice.get("DERATE") != null && ledgerPrice.get("DETH") != null) {
				double price = Double.valueOf(ledgerPrice.get("DERATE").toString());
				bsFee = BigDecimal.valueOf(declareValue).multiply(BigDecimal.valueOf(price)).multiply(BigDecimal.valueOf(ratio)).doubleValue();
			}
		}
        result.put("bsFee", bsFee);

        return result;
    }
	
	private double calEleFee(long pointId, int addAttr, int percent, Date beginTime, Date endTime, double ratio,
                             Integer declare) {
		double eleFee = 0;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pointId", pointId);
		queryMap.put("beginTime", beginTime);
		queryMap.put("endTime", endTime);
		queryMap.put("strBeginTime", DateUtil.convertDateToStr(beginTime, DateUtil.SHORT_PATTERN));
		queryMap.put("strEndTime", DateUtil.convertDateToStr(DateUtil.getNextMonthFirstDay(beginTime), DateUtil.SHORT_PATTERN));

		Double faqValue = null; // 有功总
		Map<String, Object> monEle = indexMapper.queryPointMonQ(pointId,beginTime,endTime);// 取总电量
		if (monEle != null && monEle.size() > 0) {
			if (monEle.get("FAQVALUE") != null) {
				faqValue = Double.valueOf(monEle.get("FAQVALUE").toString());
			}
		}

        Long volume = null;// 变压器容量
		List<Map<String, Object>> basicFeeInfos = costMapper.getMeterBasicFeeInfo(queryMap);// 取基本电费信息
		if (basicFeeInfos != null && basicFeeInfos.size() > 0) {
			Map<String, Object> basicFeeInfo = basicFeeInfos.get(0);
			if (basicFeeInfo != null && basicFeeInfo.size() > 0) {
				if (basicFeeInfo.get("VOLUME") != null) {
					volume = Long.valueOf(basicFeeInfo.get("VOLUME").toString());
				}
			}
		}
		
		List<RateSectorBean> rates = costMapper.getPointRateInfo(pointId);// 取测量点费率信息
		if (rates != null && rates.size() > 0) {
			List<RateFeeBean> rateEles;
			if (rates.size() == 1) {// 单费率需要特殊处理，只关注总
				rateEles = new ArrayList<RateFeeBean>();
				RateFeeBean rf = new RateFeeBean();
                rf.setSectorId(Long.toString(rates.get(0).getSectorId()));
                rf.setSectorName(rates.get(0).getSectorName());
				if(faqValue!=null){
					rf.setValue(faqValue);
				}
				rf.setPrice(rates.get(0).getRateValue());
				rf.setFee(DoubleUtils.getDoubleValue(DataUtil.doubleMultiply(rf.getValue(), rf.getPrice()), 2));
				rateEles.add(rf);
			} else {
				rateEles = costMapper.getMonthRateEleValue(queryMap); //取月分费率电量
			}

            if(rateEles.size() > 0 && declare != null && declare == 1){ //容量模式时，分户的"基本电费"
                double bsFee = 0;
                Map<String, Object> basicPrice = costMapper.getBasicFeePrice(rates.get(0).getRateId());
                if (basicPrice != null && declare == 1 && volume != null && basicPrice.get("VOLRATE") != null) {
                    RateFeeBean br = new RateFeeBean();
                    br.setSectorName("基本");
                    br.setFeeId(4);
                    br.setValue(volume);
                    br.setPrice(Double.valueOf(basicPrice.get("VOLRATE").toString()));
                    bsFee = DataUtil.doubleMultiply(DataUtil.doubleMultiply(br.getValue(), br.getPrice()), ratio);
                    br.setFee(DoubleUtils.getDoubleValue(bsFee, 2));
                    rateEles.add(br);
                }
            }

			for (RateFeeBean rf : rateEles) {
                eleFee = DataUtil.doubleAdd(eleFee, DataUtil.doubleDivide(DataUtil.doubleMultiply(DataUtil.doubleMultiply(rf.getFee(), addAttr), percent), 100));
            }
		}

		return eleFee;
	}

	@Override
	public List<Map<String, Object>> getTotal1Data(Map<String, Object> map) {
		return this.indexMapper.getTotal1Data(map);
	}

	@Override
	public List<Map<String, Object>> getTotal2Data(Map<String, Object> map) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Long parentId = (Long) map.get("ledgerId");
		List<Long> ledgerIds = this.ledgerManagerMapper.getChildLedgerIds(parentId);
		if(ledgerIds != null && ledgerIds.size() > 0){
			for (Long ledgerId : ledgerIds) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("LEDGERID", ledgerId);
				LedgerBean bean = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
				item.put("LEDGERNAME", bean.getLedgerName());
				List<Map<String, Object>>  meterList = this.indexMapper.getLedgerMeters(ledgerId);
				if(meterList!= null && meterList.size() ==1){//只有一个点，取测量点的数据；
					map.put("type", 1);
					map.put("objectId", meterList.get(0).get("METER_ID"));
				}else {
					map.put("type", 2);
					map.put("objectId", ledgerId);
				}
				Double maxFad = this.indexMapper.getTotal2Data(map);
				item.put("MAXFAD", maxFad);
				list.add(item);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getTotal3Data(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long ledgerId = (Long)map.get("ledgerId");
		//先查询该企业下有没有光伏发电的分户
		Long photovoltaicId = getPhotovoltaicId(ledgerId);
		map.put("photovoltaicId", photovoltaicId);
		List<Map<String, Object>> data = this.indexMapper.getTotal3Data(map);
		result.put("photovoltaicId", photovoltaicId);
		result.put("list",data);
		return result;
	}

	private Long getPhotovoltaicId(Long ledgerId) {
		List<Map<String, Object>> list = this.indexMapper.getChildLedger(ledgerId);
		Long photovoltaicId = 0L;
		if(list != null && list.size() > 0){
			for (Map<String, Object> ledger : list) {
				Long departId = ((BigDecimal)ledger.get("LEDGERID")).longValue();
				//查询该分户下是否有光伏DCP
				int count = this.indexMapper.getPhotovoltaic(departId);
				if(count > 0){
					photovoltaicId = departId;
					break;
				}
			}
		}
		return photovoltaicId;
	}

	@Override
	public Map<String, Object> getTotal4Data(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long ledgerId = (Long)map.get("ledgerId");
		Long photovoltaicId = getPhotovoltaicId(ledgerId);
		map.put("photovoltaicId", photovoltaicId);
		List<Map<String, Object>> data = this.indexMapper.getTotal4Data(map);
		result.put("photovoltaicId", photovoltaicId);
		result.put("list",data);
		return result;
	}

    @Override
    public void saveLyConfig(String data){
        this.indexMapper.deleteLyConfig();

        String[] dataStr = data.split(",");
        for(int i = 0; i < dataStr.length; i++){
            String oneData = dataStr[i];
            String ledgerIdStr = oneData.split("_")[0];
            String secondStr = oneData.split("_")[1];
            Long ledgerId = Long.valueOf(ledgerIdStr);
            Integer second = Integer.valueOf(secondStr);
            this.indexMapper.insertLyConfig(ledgerId, second);
        }
    }

    @Override
    public List<Map<String, Object>> getLedgerLyConfig(){
        return this.indexMapper.getLedgerLyConfig();
    }

    @Override
    public Map<String, Object> getShowLedgerUrls(Long ledgerId){
    	Map<String, Object> result = new HashMap<String, Object>();
    	LedgerBean bean = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
    	result.put("ledgerName",bean.getLedgerName());
    	List<Long> departIds = this.indexMapper.getCompanyRelation(ledgerId);
    	List<String> names = new ArrayList<String>();
    	for (Long id : departIds) {
			LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(id);
			names.add(ledger.getLedgerName());
		}
    	CompanyDisplaySet set = this.indexMapper.getScreenSet(ledgerId);
    	result.put("departIds", departIds);
    	result.put("names", names);
    	result.put("set", set);
        return result;
    }
    

	@Override
	public boolean addSug(SuggestBean suggest) {
		boolean isSuccess = false;
		if(suggest != null){			
			indexMapper.addSug(suggest);
			isSuccess = true;
		}
		return isSuccess;
	}

	@Override
	public List<SuggestBean> getSugInfo(SuggestBean suggest) {
		List<SuggestBean> sugList = this.indexMapper.getSugInfo(suggest);
		return sugList;
	}

	@Override
	public List<Long> getCompanyRelation(Long ledgerId) {
		return this.indexMapper.getCompanyRelation(ledgerId);
	}

	@Override
	public List<Map<String, Object>> getChildLedger(Long ledgerId) {
		return this.indexMapper.getChildLedger(ledgerId);
	}

	@Override
	public void saveScreen(Long ledgerId, List<Long> departAry, Integer menu,
			Integer gatherShow, Integer gather, Integer depart) {
		List<Long> departList = this.indexMapper.getCompanyRelation(ledgerId);
		if(departList.size() > 0){
			this.indexMapper.delCompanyRelation(ledgerId);
		}
		if(departAry.size() > 0){
			for (Long departId : departAry) {
				this.indexMapper.addCompanyRelation(ledgerId, departId);
			}
		}
		CompanyDisplaySet set = this.indexMapper.getScreenSet(ledgerId);
		CompanyDisplaySet newSet = new CompanyDisplaySet();
		newSet.setCompanyId(ledgerId);
		newSet.setMenuContinued(menu);
		newSet.setGatherShow(gatherShow);
		newSet.setGatherContinued(gather);
		newSet.setDepartContinued(depart);
		if(set == null){//新增
			this.indexMapper.addScreenSet(newSet);
		}else {//修改
			this.indexMapper.updateScreenSet(newSet);
		}
		
	}

	@Override
	public CompanyDisplaySet showScreenSet(Long ledgerId) {
		return this.indexMapper.getScreenSet(ledgerId);
	}

    @Override
    public Map<String, Object> indexEnergyAll(Long ledgerId){
        Map<String, Object> result = new HashMap<String, Object>();
        LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
        Map<String, Date> timeMap = processIndexEnergyTime();
        double ele_pct = 0; //电增加百分比
        double water_pct = 0;
        double gas_pct = 0;
        double hot_pct = 0;
        //电
        double ele_1 = indexMapper.getLedgerOneUseByStat(ledger, 1, timeMap.get("beginTime1"), timeMap.get("endTime1"));
        double ele_2 = indexMapper.getLedgerOneUseByStat(ledger, 1, timeMap.get("beginTime2"), timeMap.get("endTime2"));
        if(ele_1 > 0){
            ele_pct = new BigDecimal(ele_2).subtract(new BigDecimal(ele_1)).multiply(new BigDecimal(100)).divide(new BigDecimal(ele_1), 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }
        result.put("ele_val", ele_2);
        result.put("ele_pct", ele_pct);
        //水
        double water_1 = indexMapper.getLedgerOneUseByStat(ledger, 2, timeMap.get("beginTime1"), timeMap.get("endTime1"));
        double water_2 = indexMapper.getLedgerOneUseByStat(ledger, 2, timeMap.get("beginTime2"), timeMap.get("endTime2"));
        if(water_1 > 0){
            water_pct = new BigDecimal(water_2).subtract(new BigDecimal(water_1)).multiply(new BigDecimal(100)).divide(new BigDecimal(water_1), 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }
        result.put("water_val", water_2);
        result.put("water_pct", water_pct);
        //气
        double gas_1 = indexMapper.getLedgerOneUseByStat(ledger, 3, timeMap.get("beginTime1"), timeMap.get("endTime1"));
        double gas_2 = indexMapper.getLedgerOneUseByStat(ledger, 3, timeMap.get("beginTime2"), timeMap.get("endTime2"));
        if(gas_1 > 0){
            gas_pct = new BigDecimal(gas_2).subtract(new BigDecimal(gas_1)).multiply(new BigDecimal(100)).divide(new BigDecimal(gas_1), 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }
        result.put("gas_val", gas_2);
        result.put("gas_pct", gas_pct);
        //热
        double hot_1 = indexMapper.getLedgerOneUseByStat(ledger, 4, timeMap.get("beginTime1"), timeMap.get("endTime1"));
        double hot_2 = indexMapper.getLedgerOneUseByStat(ledger, 4, timeMap.get("beginTime2"), timeMap.get("endTime2"));
        if(hot_1 > 0){
            hot_pct = new BigDecimal(hot_2).subtract(new BigDecimal(hot_1)).multiply(new BigDecimal(100)).divide(new BigDecimal(hot_1), 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }
        result.put("hot_val", hot_2);
        result.put("hot_pct", hot_pct);
        //总能耗（换算成标准煤,单位:吨）
        double standard_1 = 0;
        double standard_2 = 0;
        CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
        standard_1 = new BigDecimal(ele_1).multiply(new BigDecimal(resource.getElecUnit()))
        		.add(new BigDecimal(water_1).multiply(new BigDecimal(resource.getWaterUnit()))).add(new BigDecimal(gas_1).multiply(new BigDecimal(resource.getGasUnit())))
        		.add(new BigDecimal(hot_1).multiply(new BigDecimal(resource.getHotUnit()))).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        standard_2 = new BigDecimal(ele_2).multiply(new BigDecimal(resource.getElecUnit()))
        		.add(new BigDecimal(water_2).multiply(new BigDecimal(resource.getWaterUnit()))).add(new BigDecimal(gas_2).multiply(new BigDecimal(resource.getGasUnit())))
        		.add(new BigDecimal(hot_2).multiply(new BigDecimal(resource.getHotUnit()))).divide(new BigDecimal(1000), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        double standard_pct = 0;
        if(standard_1 > 0){
            standard_pct = new BigDecimal(standard_2).subtract(new BigDecimal(standard_1)).multiply(new BigDecimal(100)).divide(new BigDecimal(standard_1), 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        }
        result.put("standard_val", standard_2);
        result.put("standard_pct", standard_pct);
        String title = DateUtil.getMonth(timeMap.get("beginTime2")) + "月能耗总览";
        result.put("title", title);
        return result;
    }

    private Map<String, Date> processIndexEnergyTime(){
        Map<String, Date> result = new HashMap<String, Date>();
        Date beginTime1 = null;
        Date endTime1 = null;
        Date beginTime2 = null;
        Date endTime2 = null;
        Date now = new Date();
        int num = DateUtil.getDay(now) - 1;
        if(num > 0){
            beginTime2 = DateUtil.getCurrMonthFirstDay(now);
            endTime2 = DateUtil.setDateEnd(DateUtil.getSomeNextDayDate(beginTime2, num-1));
            beginTime1 = DateUtil.getPreMonthFristDay(now);

            int lastMonthNum = DateUtil.getMonthDays(DateUtil.getPreMonthLastDay(now));
            if(num <= lastMonthNum){
                endTime1 = DateUtil.setDateEnd(DateUtil.getSomeNextDayDate(beginTime1, num-1));
            }
            else {
                endTime1 = DateUtil.getPreMonthLastDay(now);
            }
        }
        else {
            beginTime2 = DateUtil.getPreMonthFristDay(now);
            endTime2 = DateUtil.getPreMonthLastDay(now);
            beginTime1 = DateUtil.getPrePreMonthFirstDay(now);
            endTime1 = DateUtil.getPrePreMonthLastDay(now);
        }
        result.put("beginTime1", beginTime1);
        result.put("endTime1", endTime1);
        result.put("beginTime2", beginTime2);
        result.put("endTime2", endTime2);
        return result;
    }

	@Override
	public void SaveHomePageData(){
		//删除分户需量所有数据
		this.indexMapper.deleteLedgerDemand();
		//得到用户所属的分户ID；
		List<Long> ledgerIds = this.indexMapper.getLedgerIdByAccount(0);
		if(ledgerIds != null && ledgerIds.size() != 0){
			for (Long ledgerId : ledgerIds) {
				//缓存图标四的数据
				saveChart4Data(ledgerId);
				//缓存图标八的数据
				saveChart8Data(ledgerId);
			}
		}
	}

    @Override
    public void SavePlatHomePageData(){
        Date now = new Date();
        //得到运营商类型的所有ledgerId
        List<Long> yyLedgerIds = this.indexMapper.getLedgerIdByAccount(105);
        if(yyLedgerIds != null && yyLedgerIds.size() != 0){
            for (Long ledgerId : yyLedgerIds){
                //获取近30日日期
                for(int i = 30; i >= 1; i--){
                    Date begin = DateUtil.clearDate(DateUtil.getSomeNextDayDate(now, -i));
                    Date end =  DateUtil.setDateEnd(begin);
                    //查询数据库缓存表中是否有：该运营商、该天的在线企业数和在线监测点数
                    int count = this.indexMapper.getPlatOnlineNum(ledgerId, begin);
                    if(count == 0){  //若没有，则生成,并入库
                        long ledgerCount = this.indexMapper.getLedgerOnlineNum(ledgerId, 102, begin, end);
                        long meterCount = this.indexMapper.getMeterOnlineNum(ledgerId, begin, end);
                        //入库
                        this.indexMapper.addPlatOnline(ledgerId, begin, ledgerCount, meterCount);
                    }
                }
            }
        }
    }

    @Override
    public Double getLedgerMaxPwr(Long ledgerId, Date beginTime, Date endTime){
        double result =  this.indexMapper.getLedgerMaxPwr(ledgerId, beginTime, endTime);
        return NumberUtil.formatDouble(result, NumberUtil.PATTERN_FLOAT);
    }


    @Override
    public Map<String, Object> getPlatAdminIndexData(Long ledgerId, Long accountId){
        Map<String, Object> map = new HashMap<String, Object>();

        //运营商个数
        long partnerCount = ledgerManagerMapper.countPartnerByLedgerId(ledgerId);
        map.put("partnerCount",partnerCount);
        //企业总数
        long companyCount = ledgerManagerMapper.countCompanyLedgerByLedgerId(ledgerId);
        map.put("companyCount",companyCount);
        //监测点个数
        long pointCount = ledgerManagerMapper.countMeterByLedgerId(ledgerId);
        map.put("pointCount",pointCount);
        //覆盖省数
        long provinceCount = ledgerManagerMapper.countProvinceByLedgerId(ledgerId);
        map.put("provinceCount",provinceCount);
        //覆盖市数
        long cityCount = ledgerManagerMapper.countCityByLedgerId(ledgerId);
        map.put("cityCount",cityCount);
        //今日事件数
        Date toDayEnd = new Date();
        Date toDayBegin = DateUtil.clearDate(toDayEnd);
        long eventNum = this.eventQueryMapper.getLedgerEventNum(ledgerId, toDayBegin);
        map.put("eventNum",eventNum);
        //服务报告数
        long reportNum = this.indexMapper.getLedgerReportNum(ledgerId);
        map.put("reportNum",reportNum);
        //互动信息数
        Long replyNum = suggestService.getSuggestNumsForAdmin(accountId);
        map.put("replyNum",replyNum);
        //活跃企业
        Map<String, Object> queryMap = new HashMap<String, Object>();
        Date curDate = DateUtil.clearDate(new Date());
        queryMap.put("ledgerId",ledgerId);
        queryMap.put("nowDate",curDate);
        queryMap.put("7days", DateUtil.getDateBetween(curDate, -7));//7天
        queryMap.put("14days", DateUtil.getDateBetween(curDate, -14));//14天
        queryMap.put("21days", DateUtil.getDateBetween(curDate, -21));//21天
        queryMap.put("30days", DateUtil.getDateBetween(curDate, -30));//30天
        long activeCompanyNum = this.indexMapper.getActiveCompanyNum(queryMap);
        map.put("activeCompanyNum",activeCompanyNum);
        //地图数据
        List<Map<String, Object>> bdMapList = this.indexMapper.getBdMapDataList(ledgerId);
        map.put("bdMapList",bdMapList);
        //30天内访问人数
        long loginNum = this.indexMapper.getTotalLoginTimes(DateUtil.getSomeNextDayDate(new Date(), -30), new Date());
        map.put("loginNum", loginNum);

        return map;
    }

    @Override
    public Map<String, Object> getPlatAdminIndexEnergy(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        Date prePreMonFirst = DateUtil.getPrePreMonthFirstDay(new Date());
        Date prePreMonLast = DateUtil.getPrePreMonthLastDay(new Date());
        Date preMonFirst = DateUtil.getPreMonthFristDay(new Date());
        Date preMonLast = DateUtil.getPreMonthLastDay(new Date());
        Date curMonFirst = DateUtil.getCurrMonthFirstDay(new Date());
        LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(ledgerId);

        //本月尖、峰、平、谷
        List<Map<String, Object>> curMonQ = indexMapper.queryLedgerFeeMonQ(ledgerId, curMonFirst, new Date());
        map.put("curMonQ", curMonQ);

        //20230201-首页-峰平谷耗电监测-总耗电量、尖峰平谷时耗电量（本月）
        double monQ = indexMapper.getSYLedgerOneUseByStat(ledgerId, 1, curMonFirst, new Date());
        map.put("monQ", NumberUtil.formatDouble(monQ, NumberUtil.PATTERN_INTEGER));
        double monEle1 = indexMapper.getSYLedgerOneUseByQRate2(ledgerId, 1, curMonFirst, new Date());
        map.put("monEle1", NumberUtil.formatDouble(monEle1, NumberUtil.PATTERN_INTEGER));
		double monEle2 = indexMapper.getSYLedgerOneUseByQRate2(ledgerId, 2, curMonFirst, new Date());
        map.put("monEle2", NumberUtil.formatDouble(monEle2, NumberUtil.PATTERN_INTEGER));
		double monEle3 = indexMapper.getSYLedgerOneUseByQRate2(ledgerId, 3, curMonFirst, new Date());
        map.put("monEle3", NumberUtil.formatDouble(monEle3, NumberUtil.PATTERN_INTEGER));
		double monEle4 = indexMapper.getSYLedgerOneUseByQRate2(ledgerId, 4, curMonFirst, new Date());
        map.put("monEle4", NumberUtil.formatDouble(monEle4, NumberUtil.PATTERN_INTEGER));


        //上月总电量
//        double lastMonQ = indexMapper.getLedgerOneUseByStat(ledger, 1, preMonFirst, preMonLast);
        double lastMonQ = indexMapper.getSYLedgerOneUseByStat(ledgerId, 1, preMonFirst, preMonLast);
        double e_val = lastMonQ;      //存下来用于下面的标煤计算
        lastMonQ = NumberUtil.formatDouble(lastMonQ, NumberUtil.PATTERN_INTEGER);
        map.put("lastMonQ", lastMonQ);
        //上上月总电量
        int prePreMon = DateUtil.getMonth(prePreMonFirst);
        map.put("prePreMon", prePreMon);
        double prePreMonQ = indexMapper.getLedgerOneUseByStat(ledger, 1, prePreMonFirst, prePreMonLast);
        double prePreMonPer = 0;
        if(prePreMonQ > 0){
            prePreMonPer = (e_val - prePreMonQ) * 100/prePreMonQ;
            prePreMonPer = NumberUtil.formatDouble(prePreMonPer, NumberUtil.PATTERN_FLOAT);
        }
        else if(prePreMonQ == 0 && e_val > 0){
            prePreMonPer = 100;
        }
        map.put("prePreMonPer", prePreMonPer);

        //上月其它能源: 水、气、热
        String water_unit = "m³";
        double water = indexMapper.getLedgerOneUseByStat(ledger, 2, preMonFirst, preMonLast);
        double w_val = water;         //存下来用于下面的标煤计算
        water = NumberUtil.formatDouble(water, NumberUtil.PATTERN_INTEGER);
        if(water > 100000){
            water = DataUtil.doubleDivide(water, 1000, 0);
            water_unit = "km³";
        }
        map.put("water", water);
        map.put("waterUnit", water_unit);

        String gas_unit = "m³";
        double gas = indexMapper.getLedgerOneUseByStat(ledger, 3, preMonFirst, preMonLast);
        double g_val = gas;       //存下来用于下面的标煤计算
        gas = NumberUtil.formatDouble(gas, NumberUtil.PATTERN_INTEGER);
        if(gas > 100000){
            gas = DataUtil.doubleDivide(gas, 1000, 0);
            gas_unit = "km³";
        }
        map.put("gas", gas);
        map.put("gasUnit", gas_unit);

        String hot_unit = "kWh";
        double hot = indexMapper.getLedgerOneUseByStat(ledger, 4, preMonFirst, preMonLast);
        double h_val = hot;       //存下来用于下面的标煤计算
        hot = NumberUtil.formatDouble(hot, NumberUtil.PATTERN_INTEGER);
        if(hot > 100000){
            hot = DataUtil.doubleDivide(hot, 1000, 0);
            hot_unit = "MWh";
        }
        map.put("hot", hot);
        map.put("hotUnit", hot_unit);
        //上月能耗折算: 标准煤、CO2
        CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
        double standard = new BigDecimal(e_val).multiply(new BigDecimal(resource.getElecUnit()))
                .add(new BigDecimal(w_val).multiply(new BigDecimal(resource.getWaterUnit())))
                .add(new BigDecimal(g_val).multiply(new BigDecimal(resource.getGasUnit())))
                .add(new BigDecimal(h_val).multiply(new BigDecimal(resource.getHotUnit())))
                .divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        map.put("standard", standard);
        double co2 = DataUtil.doubleDivide(DataUtil.doubleMultiply(standard, resource.getEnergyValue()), resource.getElecUnit(), 0);
        map.put("co2", co2);

        return map;
    }

    @Override
    public Map<String, Object> getOnlineCompanyList(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        //企业在线数曲线
        Date now = new Date();
        Date nearThirtyFirst = DateUtil.clearDate(DateUtil.getSomeNextDayDate(now, -29));
        List<Map<String, Object>> onlines = this.indexMapper.getLedgerOnlineQuick(ledgerId, nearThirtyFirst, now);    //从缓存表查
        if(onlines.size() < 29){   //从曲线表查，并按时间排顺序
            List<Map<String, Object>> companyOnlines = new ArrayList<Map<String, Object>>();
            for(int i = 29; i >= 0; i--){
                Map<String, Object> temp = new HashMap<String, Object>();
                String str = DateUtil.convertDateToStr(DateUtil.getSomeNextDayDate(now, -i), DateUtil.SHORT_PATTERN);
                temp.put("X_TIME", str);
                temp.put("Y_VALUE", 0);
                companyOnlines.add(temp);
            }
            onlines = this.indexMapper.getLedgerOnlineList(ledgerId, nearThirtyFirst, now);
            if(!CollectionUtils.isEmpty(onlines)){
                for(int i = 0; i < onlines.size(); i++){
                    Map<String, Object> temp = onlines.get(i);
                    String x_time = temp.get("X_TIME").toString();
                    Date x_date = DateUtil.convertStrToDate(x_time, DateUtil.SHORT_PATTERN);
                    int index = DateUtil.daysBetweenDates(x_date, nearThirtyFirst);
                    if(index < companyOnlines.size()){
                        companyOnlines.set(index, temp);
                    }
                }
            }
            map.put("companyOnlines",companyOnlines);
        }
        else {  //从缓存表查时，把当天的数据补上
            if(onlines.size() == 29){
                Date tempDate = DateUtil.clearDate(now);
                Map<String, Object> tempMap = new HashMap<String, Object>();
                tempMap.put("X_TIME", DateUtil.convertDateToStr(tempDate, DateUtil.SHORT_PATTERN));
                tempMap.put("Y_VALUE", this.indexMapper.getLedgerOnlineNum(ledgerId, 102, tempDate, now));
                onlines.add(tempMap);
            }
            map.put("companyOnlines",onlines);
        }

        return map;
    }

    @Override
    public Map<String, Object> getOnlineCompanys(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        //在线企业数
        long conmanyOnlineNum = this.indexMapper.getLedgerOnlineNum(ledgerId, 102, DateUtil.getNextMinuteDate(new Date(), -45), new Date());
        map.put("conmanyOnlineNum",conmanyOnlineNum);
        return map;
    }

    @Override
    public Map<String, Object> getOnlineMeterList(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        //监测点在线数曲线
        Date now = new Date();
        Date nearThirtyFirst = DateUtil.clearDate(DateUtil.getSomeNextDayDate(now, -29));
        List<Map<String, Object>> onlines = this.indexMapper.getMeterOnlineQuick(ledgerId, nearThirtyFirst, now);   //从缓存表查
        if(onlines.size() < 29){   //从曲线表查，并按时间排顺序
            List<Map<String, Object>> meterOnlines = new ArrayList<Map<String, Object>>();
            for(int i = 29; i >= 0; i--){
                Map<String, Object> temp = new HashMap<String, Object>();
                String str = DateUtil.convertDateToStr(DateUtil.getSomeNextDayDate(now, -i), DateUtil.SHORT_PATTERN);
                temp.put("X_TIME", str);
                temp.put("Y_VALUE", 0);
                meterOnlines.add(temp);
            }
            onlines = this.indexMapper.getMeterOnlineList(ledgerId, nearThirtyFirst, now);
            if(!CollectionUtils.isEmpty(onlines)){
                for(int i = 0; i < onlines.size(); i++){
                    Map<String, Object> temp = onlines.get(i);
                    String x_time = temp.get("X_TIME").toString();
                    Date x_date = DateUtil.convertStrToDate(x_time, DateUtil.SHORT_PATTERN);
                    int index = DateUtil.daysBetweenDates(x_date, nearThirtyFirst);
                    if(index < meterOnlines.size()){
                        meterOnlines.set(index, temp);
                    }
                }
            }
            map.put("meterOnlines",meterOnlines);
        }
        else {  //从缓存表查时，把当天的数据补上
            if(onlines.size() == 29){
                Date tempDate = DateUtil.clearDate(now);
                Map<String, Object> tempMap = new HashMap<String, Object>();
                tempMap.put("X_TIME", DateUtil.convertDateToStr(tempDate, DateUtil.SHORT_PATTERN));
                tempMap.put("Y_VALUE", this.indexMapper.getMeterOnlineNum(ledgerId, tempDate, now));
                onlines.add(tempMap);
            }
            map.put("meterOnlines",onlines);
        }

        return map;
    }

    @Override
    public Map<String, Object> getOnlineMeters(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        //在线监测点数
        long meterOnlineNum = this.indexMapper.getMeterOnlineNum(ledgerId, DateUtil.getNextMinuteDate(new Date(), -45), new Date());
        map.put("meterOnlineNum",meterOnlineNum);
        return map;
    }

    @Override
    public Map<String, Object> getOnlinePlats(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        //在线运营
        long yyOnlineNum = this.indexMapper.getLedgerOnlineNum(ledgerId, 105, DateUtil.getNextMinuteDate(new Date(), -45), new Date());
        map.put("yyOnlineNum",yyOnlineNum);
        return map;
    }

	public static void main(String[] args) {
		System.out.println(DateUtil.getPreMonthFristDay(new Date()));
		System.out.println(DateUtil.getPreMonthLastDay(new Date()));
		System.out.println(DateUtil.getCurrMonthFirstDay(new Date()));
	}

    @Override
    public Map<String, Object> getLastAndThisMonQ(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        //上月、本月用电负荷
        Date preMonFirst = DateUtil.getPreMonthFristDay(new Date());
        Date preMonLast = DateUtil.getPreMonthLastDay(new Date());
        Date curMonFirst = DateUtil.getCurrMonthFirstDay(new Date());
        List<Map<String, Object>> list1 = indexMapper.getLedgerPower(ledgerId, preMonFirst, preMonLast);
        map.put("list1", list1);
        List<Map<String, Object>> list2 = indexMapper.getLedgerPower(ledgerId, curMonFirst, new Date());
        map.put("list2", list2);
        return map;
    }

    @Override
    public Map<String, Object> getRealTimeAp(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();
        //实时功率
        double power = 0;
        String power_unit = "kW";
        //先计算实时时间点time
        Date nowDate = new Date();
        String minutes = DateUtil.convertDateToStr(nowDate, DateUtil.MINUTE_PATTERN);
        int minuteNum = Integer.valueOf(minutes);
        minuteNum = minuteNum % 15;
        Date time = DateUtil.getNextMinuteDate(DateUtil.clearSecond(nowDate), -(minuteNum+15));
        Double tempPower = this.dataQueryMapper.getCurPowData(ledgerId, time);

        if(null != tempPower){
            power = tempPower;
        }
        power = NumberUtil.formatDouble(power, NumberUtil.PATTERN_INTEGER);
//        if(power > 9999999){
//            power = NumberUtil.formatDouble(DataUtil.doubleDivide(power, 1000).doubleValue(), NumberUtil.PATTERN_FLOAT);
//            power_unit = "MW";
//        }
        map.put("power", power);
        map.put("powerUnit", power_unit);
        //地图中Top3的数据
        List<ParentRegion> provinces = this.indexMapper.getRegionLedgers(ledgerId, 0);
        if(null == provinces){
            provinces = new ArrayList<ParentRegion>();
        }
        List<ParentRegion> citys = this.indexMapper.getRegionLedgers(ledgerId, 1);
        List<ParentRegion> countys = this.indexMapper.getRegionLedgers(ledgerId, 2);
        if(!CollectionUtils.isEmpty(citys)){
            for(int i = 0; i < citys.size(); i++){
                ParentRegion child = citys.get(i);
                //根据子区域ID和区域级别，查询顶层省
                ParentRegion parent = this.indexMapper.getTopRegionByChild(child.getRegionId(), 1);
                //判断改顶层省是否在provinces中，并做相应处理
                if(null != parent){
                    doInProvinces(parent, provinces, child);
                }
            }
        }
        if(!CollectionUtils.isEmpty(countys)){
            for(int i = 0; i < countys.size(); i++){
                ParentRegion child = countys.get(i);
                //根据子区域ID和区域级别，查询顶层省
                ParentRegion parent = this.indexMapper.getTopRegionByChild(child.getRegionId(), 2);
                //判断改顶层省parent是否在provinces中，并做相应处理
                if(null != parent){
                    doInProvinces(parent, provinces, child);
                }
            }
        }
        //对provinces,按企业数排序（冒泡排序）
        if(!CollectionUtils.isEmpty(provinces)){
            for(int i = 0;i < provinces.size(); i++){
                for(int j = i;j < provinces.size(); j++){
                    int item_i = provinces.get(i).getLedgers().size();
                    int item_j = provinces.get(j).getLedgers().size();
                    if(item_i < item_j){
                        //交换两个元素的位置
                        ParentRegion temp = provinces.get(i);
                        provinces.set(i, provinces.get(j));
                        provinces.set(j, temp);
                    }
                }
            }
            //排完序，查前3的实时功率
            Date preMonFirst = DateUtil.getPreMonthFristDay(new Date());
            Date preMonLast = DateUtil.getPreMonthLastDay(new Date());
            for(int i = 0; i < 3; i++){
                if(i < provinces.size()){
                    ParentRegion temp = provinces.get(i);
                    List<LedgerBean> ledgers = temp.getLedgers();
                    Double curPower = 0D;
                    Double lastMonQ = 0D;
                    if(!CollectionUtils.isEmpty(ledgers)){
                        curPower = this.dataQueryMapper.getRegionCurPowData(ledgers, time);

						// 20230201
						lastMonQ= indexMapper.getSYLedgerOneUseByStat(ledgerId, 1, preMonFirst, preMonLast);
//                        lastMonQ = this.dataQueryMapper.getRegionLastMonQData(ledgers, preMonFirst, preMonLast);
                    }
                    temp.setCurPower(curPower);
                    temp.setLastMonQ(lastMonQ);
                }
            }
        }

        map.put("provinces", provinces);

        return map;
    }

    /**
     * 企业列表
     */
    @Override
    public List<LedgerBean> getCompanyPageList(Map<String, Object> requestParams) {
    	Long accountId = (Long) requestParams.get("accountId");
    	String companyName = (String) requestParams.get("companyName");
    	String operator = (String) requestParams.get("operator");
    	String regionId_0 = (String) requestParams.get("regionId_0");		//省, region_level 0
    	String regionId_1 = (String) requestParams.get("regionId_1");		//市, region_level 1
    	String regionId_2 = (String) requestParams.get("regionId_2");		//县, region_level 2
		Page page = (Page) requestParams.get("page");
		List<String> regionIds = new ArrayList<String>();
		
		if (!regionId_2.isEmpty() && !regionId_2.equals("0"))
			regionIds.add(regionId_2);
		else if (!regionId_1.isEmpty() && !regionId_1.equals("0"))
			regionIds.addAll(indexMapper.getSubRegionIds(regionId_1));
		else if (!regionId_0.isEmpty() && !regionId_0.equals("0"))
			regionIds.addAll(indexMapper.getSubRegionIds(regionId_0));
		
		List<LedgerBean> ledgerList = indexMapper.getCompanyPageList(accountId, companyName, operator, regionIds, page);
		Map<Long, LedgerBean> ledgerMap = new HashMap<Long, LedgerBean>();
		for (LedgerBean tmp : ledgerList) {
			ledgerMap.put(tmp.getLedgerId(), tmp);
		}
		
		if (ledgerList != null && ledgerList.size() > 0) {
			Date now = new Date();
			List<Map<String, Object>> meterData = indexMapper.getCompanyMeterOnlineData(ledgerList, now);
			for (Map<String, Object> tmpMap : meterData) {
				Long tmpLedgerId = ((BigDecimal) tmpMap.get("LEDGERID")).longValue();
				LedgerBean tmpBean = ledgerMap.get(tmpLedgerId);
				tmpBean.setOffLineMeterNum(((BigDecimal) tmpMap.get("OFFLINENUM")).intValue());
				tmpBean.setAllMeterNum(((BigDecimal) tmpMap.get("ALLNUM")).intValue());
			}
		}
		
		return ledgerList;
    }
    
    /**
	 * 根据父区域id获取指定级别子区域, 若父区域id为0, 则获取该级别全部区域
	 * @param parentId	父区域id
	 * @return
	 */
	public List<RegionBean> getSubRegions(int level, String parentId) {
		return indexMapper.getSubRegions(level, parentId);
	}
    
    private void doInProvinces(ParentRegion parent, List<ParentRegion> provinces, ParentRegion child){
        if(!CollectionUtils.isEmpty(provinces)){
            for(int i = 0;i < provinces.size(); i++){
                ParentRegion temp = provinces.get(i);
                if(parent.getRegionId().equals(temp.getRegionId())){
                    temp.getLedgers().addAll(child.getLedgers());
                    return;
                }
            }
            parent.setLedgers(child.getLedgers());
            provinces.add(parent);
        }
        else{
            parent.setLedgers(child.getLedgers());
            provinces.add(parent);
        }
    }

	/**
	 * 保存图标四的数据
	 * @param ledgerId
	 */
	private void saveChart4Data(Long ledgerId) {
		Date baseDate = DateUtil.clearDate(WebConstant.getChartBaseDate());
		Date preMon1 = DateUtil.getPreMonthFristDay(baseDate);
		Date preMon2 = DateUtil.getPreMonthLastDay(baseDate);
		Date currMon1 = DateUtil.getCurrMonthFirstDay(baseDate);

		/**
		 * 保存上月需量
		 */
		List<Map<String, Object>> lastMonPwr = indexMapper.queryLedgerDayMaxPwr(ledgerId, preMon1, preMon2);
		if(lastMonPwr != null && lastMonPwr.size() > 0 ){
			for (Map<String, Object> lastMonMap : lastMonPwr) {
				saveLedgerDemand(ledgerId, lastMonMap,LedgerDemandBean.lastMonPwr);
			}
		}
		/**
		 * 保存本月需量
		 */
		List<Map<String, Object>> currMonPwr = indexMapper.queryLedgerDayMaxPwr(ledgerId, currMon1, baseDate);
		if(currMonPwr != null && currMonPwr.size() > 0){
			for (Map<String, Object> currMonMap : currMonPwr) {
				saveLedgerDemand(ledgerId, currMonMap,LedgerDemandBean.currMonPwr);
			}
		}
		
		/**
		 * 保存上月发生时间
		 */
		Date lastMonPwrDate = indexMapper.getLedgerMaxTime(ledgerId, preMon1, preMon2, getMaxPwr(lastMonPwr));
		LedgerDemandBean lastMonBean = new LedgerDemandBean();
		lastMonBean.setLedgerId(ledgerId);
		lastMonBean.setDataType(LedgerDemandBean.lastMonPwrDate);
		lastMonBean.setOccurredTime(lastMonPwrDate);
		this.indexMapper.saveLedgerDemand(lastMonBean);
		
		/**
		 * 保存本月发生时间
		 */
		Date currMonPwrDate = indexMapper.getLedgerMaxTime(ledgerId, currMon1, baseDate, getMaxPwr(currMonPwr));
		LedgerDemandBean currMonBean = new LedgerDemandBean();
		currMonBean.setLedgerId(ledgerId);
		currMonBean.setDataType(LedgerDemandBean.currMonPwrDate);
		currMonBean.setOccurredTime(currMonPwrDate);
		this.indexMapper.saveLedgerDemand(currMonBean);
	}

	/**
	 * 保存分户需量
	 * @param ledgerId
	 * @param map
	 */
	private void saveLedgerDemand(Long ledgerId, Map<String, Object> map, int type) {
		String day = (String) map.get("DAY");
		BigDecimal ap = (BigDecimal) map.get("AP");
		LedgerDemandBean bean = new LedgerDemandBean();
		bean.setLedgerId(ledgerId);
		bean.setDataType(type);
		bean.setDay(day);
		if(ap != null){
			bean.setMaxAP(ap.doubleValue());
		}
		this.indexMapper.saveLedgerDemand(bean);
	}

	/**
	 * 保存图标八的数据
	 * @param ledgerId
	 */
	private void saveChart8Data(Long ledgerId) {
		Date baseDate = DateUtil.clearDate(WebConstant.getChartBaseDate());
		Date startDate = DateUtil.getDateBetween(baseDate, -30);
		Date endDate = DateUtil.getDateBetween(baseDate, -1);

		LedgerLoadBean bean = new LedgerLoadBean();
		bean.setLedgerId(ledgerId);
		//分户最大功率
		Double maxPwr = indexMapper.queryLedgerMaxPwr(ledgerId, startDate, endDate);
		if (maxPwr != null) {
			bean.setMaxP(maxPwr);
			Date occuredDate = indexMapper.getLedgerMaxTime(ledgerId, startDate, endDate, maxPwr);
			bean.setOccuredTime(DateUtil.convertDateToStr(occuredDate, DateUtil.MOUDLE_PATTERN));
		}
		//分户最大电流
		bean.setMaxI(indexMapper.queryLedgerMaxI(ledgerId, startDate, endDate));
		//分户的额定功率
		bean.setRatio(indexMapper.getLedgerEPwr(ledgerId));
		bean.setFlagTime(new Date());
		
		this.indexMapper.saveLedgerLoad(bean);
	}

}
