package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.ChartNameBean;
import com.linyang.energy.dto.Item;
import com.linyang.energy.dto.LedgerCost;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.energyanalysis.ClfcHouseStatisticStatMapper;
import com.linyang.energy.model.LegdeStatBean;
import com.linyang.energy.service.ClfcHouseStatisticStatService;
import com.linyang.energy.utils.ChartConditionUtils;
import com.linyang.energy.utils.ChartNameUtils;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.DateUtil;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;
import com.linyang.util.DoubleUtils;
/**
 * 能耗分类统计、能耗分户统计业务逻辑层接口实现类
  @description:
  @version:0.1
  @author:Cherry
  @date:Jan 3, 2014
 */
@Service
public class ClfcHouseStatisticStatServiceImpl implements ClfcHouseStatisticStatService {

	@Autowired
	private ClfcHouseStatisticStatMapper clfcHouseStatisticStatMapper;
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.ClfcHouseStatisticStatService#getClfcChartDatas(java.util.Map)
	 */
	@Override
	public Map<String, Object> getClfcChartDatas(Map<String, Object> queryMap) {
		Map<String,Object> result = new HashMap<String, Object>();
		if(queryMap.containsKey("beginDate") && queryMap.containsKey("endDate")&& queryMap.containsKey("legerdId")&& queryMap.containsKey("timeType")){
			queryMap = ChartConditionUtils.processDate(queryMap);
			List<Map<String, Object>> legerConst = clfcHouseStatisticStatMapper.getLegerConst(queryMap);
			TimeEnum timeType =  TimeEnum.formInt2TimeEnum(Integer.valueOf(queryMap.get("timeType").toString()));
			if(CommonMethod.isCollectionNotEmpty(legerConst)){
				long beginDate = Long.valueOf(queryMap.get("beginDate").toString());
				long endDate =  Long.valueOf(queryMap.get("endDate").toString());
				ChartNameBean chartNameBean = ChartNameUtils.getChartNameBean();
				List<ChartItemWithTime> list = new ArrayList<ChartItemWithTime>();
				ChartItemWithTime eleItem = new ChartItemWithTime(timeType,chartNameBean.getEleName(),beginDate,endDate);
				ChartItemWithTime waterItem = new ChartItemWithTime(timeType,chartNameBean.getWaterName(),beginDate,endDate);
				ChartItemWithTime gasItem = new ChartItemWithTime(timeType,chartNameBean.getGasName(),beginDate,endDate);
				ChartItemWithTime totalItem = new ChartItemWithTime(timeType,"总费用",beginDate,endDate);
				for (Map<String, Object> map : legerConst) {
					Date time = (Date)map.get("TIME_FIELD");
					double eleCost = Double.valueOf(map.get("Q_VALUE")==null?"0":map.get("Q_VALUE").toString());
					double waterCost = Double.valueOf(map.get("G_VALUE")==null?"0":map.get("G_VALUE").toString());
					double gasCost = Double.valueOf(map.get("W_VALUE")==null?"0":map.get("W_VALUE").toString());
					if(time != null){
						String timeString = DateUtil.convertDateToStr(time,timeType.getDateFormat());
						if(timeType == TimeEnum.WEEK)
							timeString = DateUtil.getMonday(timeString);
						Map<String, Object> eleMap = eleItem.getMap();
						double tmp ;
						if(eleMap.containsKey(timeString)){
							tmp = DataUtil.doubleAdd(Double.valueOf(eleMap.get(timeString).toString()), eleCost);
							eleMap.put(timeString,tmp);
							totalItem.getMap().put(timeString,DataUtil.doubleAdd(Double.valueOf(totalItem.getMap().get(timeString).toString()), tmp));
						}
						Map<String, Object> waterMap = waterItem.getMap();
						if(waterMap.containsKey(timeString)){
							tmp = DataUtil.doubleAdd(Double.valueOf(waterMap.get(timeString).toString()), waterCost);
							waterMap.put(timeString,tmp);
							totalItem.getMap().put(timeString,DataUtil.doubleAdd(Double.valueOf(totalItem.getMap().get(timeString).toString()), tmp));
						}
						Map<String, Object> gasMap = gasItem.getMap();
						if(gasMap.containsKey(timeString)){
							tmp = DataUtil.doubleAdd(Double.valueOf(gasMap.get(timeString).toString()), gasCost);
							gasMap.put(timeString,tmp);
							totalItem.getMap().put(timeString,DataUtil.doubleAdd(Double.valueOf(totalItem.getMap().get(timeString).toString()), tmp));
						}
					}
				}
				
				list.addAll(Arrays.asList(new ChartItemWithTime[]{eleItem,waterItem,gasItem}));
				list = ChartConditionUtils.itemDataScale(list);
				//三个折线图
				result.put("list", list);
				
				//一个总图
				result.put("totalCostDatas", totalItem.dataScale().values());
				result.put("legend", totalItem.getMap().keySet());
				
				
				//饼图,柱状图
				List<Item> pieDatas = new ArrayList<Item>();
				Item item = null;
				for (ChartItemWithTime chartItemWithTime : list) {
					item = new Item(chartItemWithTime.getName());
					Collection<Object> values = chartItemWithTime.getMap().values();
					double totalValues = 0;
					for (Object object : values) {
						if(DoubleUtils.isDoubleType(object.toString()))
							totalValues = DataUtil.doubleAdd(totalValues, Double.valueOf(object.toString()));
					}
					double doubleValue = DoubleUtils.getDoubleValue(totalValues, 2);
					item.setValue(doubleValue);
					pieDatas.add(item);
				}
				//饼图,柱状图
				result.put("pieDatas",pieDatas);
			}
			result.put("beginTime",DateUtil.convertDateToStr((Date)queryMap.get("beginTime"), timeType.getDateFormat()));
			result.put("endTime",DateUtil.convertDateToStr((Date)queryMap.get("endTime"), timeType.getDateFormat()));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.ClfcHouseStatisticStatService#getLegderChildCost(java.util.Map)
	 */
	@Override
	public List<LedgerCost> getLegderChildStat(Map<String, Object> queryMap) {
		List<LedgerCost> list = new ArrayList<LedgerCost>();
		if(queryMap.containsKey("beginDate") && queryMap.containsKey("endDate")&& queryMap.containsKey("legerdId")&& queryMap.containsKey("timeType")){
			List<Map<String, Object>> legerConst = clfcHouseStatisticStatMapper.getLegerChildConst(ChartConditionUtils.processDate(queryMap));
			if(CommonMethod.isCollectionNotEmpty(legerConst)){
				Long parentLedgerId = Long.valueOf(queryMap.get("legerdId").toString());
				Map<Long,LedgerCost> groupMap = new HashMap<Long, LedgerCost>();
				for (Map<String, Object> map : legerConst) {
					Long ledgerId = Long.valueOf(map.get("LEDGER_ID").toString());
					double eleCost = Double.valueOf(map.get("Q_VALUE")==null?"0":map.get("Q_VALUE").toString());
					double waterCost = Double.valueOf(map.get("G_VALUE")==null?"0":map.get("G_VALUE").toString());
					double gasCost = Double.valueOf(map.get("W_VALUE")==null?"0":map.get("W_VALUE").toString());
					if(!groupMap.containsKey(ledgerId)){
						 LedgerCost ledgerCost = new LedgerCost(map.get("LEDGER_NAME").toString(),eleCost,waterCost,gasCost);
						 if(ledgerId.equals(parentLedgerId))
							 ledgerCost.setParent(true);
						 groupMap.put(ledgerId,ledgerCost);
					}else{
						groupMap.get(ledgerId).addAll(eleCost, waterCost, gasCost);
					}
				}
				list.addAll(groupMap.values());
			}
		}
		for (LedgerCost ledgerCost : list) {
			ledgerCost.buildResult();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public  Map<String, Object>  getHouseStat(Page page,Map<String, Object> queryMap) {
		Map<String,Object> result = new HashMap<String, Object>();
		if(queryMap.containsKey("time") && queryMap.containsKey("timeType")){
			TimeEnum timeType =  TimeEnum.formInt2TimeEnum(Integer.valueOf(queryMap.get("timeType").toString()));
			//分页的
			queryMap.put(Dialect.pageNameField, page);
			if(queryMap.containsKey("queryLedgerIds")){
				List<Long> queryLedgerIds = (List<Long>)queryMap.get("queryLedgerIds");
				queryMap.put("queryLedgerIds", queryLedgerIds);			//	原来			queryMap.put("queryLedgerIds", CommonMethod.getSeparaterString(",",queryLedgerIds));
			}
			
			if(timeType == TimeEnum.DAY || timeType == TimeEnum.WEEK)
				queryMap.put("type", 1);
			else
				queryMap.put("type",2);
			long time = DateUtils.convertTimeToLong(queryMap.get("time").toString(), DateUtils.FORMAT_SHORT);
			String formatTime =  DateUtils.convertTimeToString(time, timeType.getDateFormat());
			boolean isNeedCalculate = true;
			long nowTime = DateUtils.convertTimeToLong(DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT), DateUtils.FORMAT_SHORT);
			if(timeType == TimeEnum.DAY){//日
				queryMap.put("beginTime",new Date(time*1000));
				queryMap.put("endTime",new Date(time*1000));
				queryMap.put("beginTime1",new Date((time-86400)*1000));
				queryMap.put("endTime1",new Date((time-86400)*1000));
				if(time >= nowTime)
					isNeedCalculate = false;
			}else if(timeType == TimeEnum.WEEK){//周
				long monday = DateUtils.convertTimeToLong(DateUtil.getMonday(formatTime), DateUtils.FORMAT_SHORT);
				long sunday = DateUtils.convertTimeToLong(DateUtil.getSunday(formatTime), DateUtils.FORMAT_SHORT);
				queryMap.put("beginTime",new Date(monday*1000));
				queryMap.put("endTime",new Date(sunday*1000));
				queryMap.put("beginTime1",new Date((monday-7*86400)*1000));
				queryMap.put("endTime1",new Date((sunday-7*86400)*1000));
				if(sunday >= nowTime)
					isNeedCalculate = false;
			}else if(timeType == TimeEnum.MONTH){//月
				Date firstDay = new Date(DateUtils.convertTimeToLong((formatTime+"-01"), DateUtils.FORMAT_SHORT)*1000);
				long lastDay = DateUtils.convertTimeToLong(DateUtil.getMonthLastDay(formatTime+"-01"), DateUtils.FORMAT_SHORT);
				queryMap.put("beginTime",firstDay);
				queryMap.put("endTime",new Date(lastDay*1000));
				Date lastMonthDate = DateUtil.getLastMonthDate(firstDay);
				queryMap.put("beginTime1",lastMonthDate);
				queryMap.put("endTime1",DateUtil.parseDate(DateUtil.getMonthLastDay(DateUtil.convertDateToStr(lastMonthDate, DateUtils.FORMAT_SHORT)),DateUtils.FORMAT_SHORT));
				if(lastDay >= nowTime)
					isNeedCalculate = false;
			}else{//年
				long date = DateUtils.convertTimeToLong(formatTime+"-12-31", DateUtils.FORMAT_SHORT);
				queryMap.put("beginTime",new Date(DateUtils.convertTimeToLong(formatTime+"-01-01", DateUtils.FORMAT_SHORT)*1000));
				queryMap.put("endTime",new Date(date*1000));
				int yearLast = Integer.valueOf(formatTime)-1;
				queryMap.put("beginTime1",new Date(DateUtils.convertTimeToLong(yearLast+"-01-01", DateUtils.FORMAT_SHORT)*1000));
				queryMap.put("endTime1",new Date(DateUtils.convertTimeToLong(yearLast+"-12-31", DateUtils.FORMAT_SHORT)*1000));
				if(date >= nowTime)
					isNeedCalculate = false;
			}
			
			List<LegdeStatBean> houseStatPage = clfcHouseStatisticStatMapper.getHouseStatPage(queryMap);
			if(houseStatPage != null && houseStatPage.size() > 0){
				Map<Long,LegdeStatBean> legerdMapping = new HashMap<Long, LegdeStatBean>();
				for (LegdeStatBean legdeStatBean : houseStatPage) {
					if(isNeedCalculate){
						if(timeType == TimeEnum.DAY || timeType == TimeEnum.WEEK)
							legdeStatBean.calculatePercentage();
						else
							legdeStatBean.calculateCostPercentage();
					}
					legerdMapping.put(legdeStatBean.getLegdeId(),legdeStatBean);
				}
				//费率电量
				if(timeType == TimeEnum.DAY || timeType == TimeEnum.WEEK){
					queryMap.put("ledgerIds", legerdMapping.keySet());						//	原		queryMap.put("ledgerIds", CommonMethod.getSeparaterString(",", legerdMapping.keySet()));
					List<Map<String, Object>> rateDatas = clfcHouseStatisticStatMapper.getRateDatas(queryMap);
					if(CommonMethod.isCollectionNotEmpty(rateDatas)){
						for (Map<String, Object> map2 : rateDatas) {
							Long ledgerId = Long.valueOf(map2.get("LEDGER_ID").toString());
							if(legerdMapping.containsKey(ledgerId))
								legerdMapping.get(ledgerId).getRateList().add(map2);
						}
					}
				}
				for (LegdeStatBean legdeStatBean2 : houseStatPage) {
					long legdeId = legdeStatBean2.getLegdeId();
					if(legerdMapping.containsKey(legdeId))
						legdeStatBean2.setRateList(legerdMapping.get(legdeId).getRateList());
				}
			}
			result.put("list",houseStatPage==null?new ArrayList<LegdeStatBean>():houseStatPage);
			result.put("page", page);
		}
		return result;
	}

	
	@Override
	public List<Map<String, Object>> getLegerdsPics(Long[] legerdIds) {
		List<Map<String, Object>> list = null;
		if(!ArrayUtils.isEmpty(legerdIds))
			list = clfcHouseStatisticStatMapper.getLegerdsPics(legerdIds);          //原list = clfcHouseStatisticStatMapper.getLegerdsPics(CommonMethod.getSeparaterString(",", Arrays.asList(legerdIds)));
		return list == null?new ArrayList<Map<String,Object>>():list;
	}

}
