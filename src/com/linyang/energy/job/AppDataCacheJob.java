/**
 */
package com.linyang.energy.job;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.common.web.common.Log;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.IndexService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.PhoneService;
import com.linyang.energy.service.UserService;
import com.linyang.energy.utils.WebConstant;

/**
 * 手机APP头部缓存数据(每天3点做缓存)
 * @author Angelo
 * @date 2016-8-4 下午04:16:48
 * @version 1.0
 */
public class AppDataCacheJob {
	private final static ConcurrentHashMap<Long, Map<Integer,Map<String,Object>>> dataCacheMap = new ConcurrentHashMap<Long,Map<Integer,Map<String,Object>>>();
	@Autowired
	private UserService userService;
	@Autowired
	private PhoneService phoneService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private LedgerManagerService ledgerService;

	public void init() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, WebConstant.appDataCacheTime);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
            	Log.info("start create App head-data cache!");
            	createAppDataCache();
            	Log.info("end create App head-data cache!");
            }
        }, cal.getTime(), 1000 * 60 * 60 * 24);
	}

	/**
	 * 缓存数据
	 */
	public void createAppDataCache() {
		List<UserBean> userList = userService.getUserList();
		Map<Integer,Map<String,Object>> dataMap;
		for (UserBean user : userList) {
			dataMap = new HashMap<Integer, Map<String,Object>>();
			// 总电量 0
			
				if(dataMap.containsKey(0))
					dataMap.remove(0);
				dataMap.put(0, phoneService.getHeadChartData(user));

			// 上月能耗统计 1
			
				if(dataMap.containsKey(1))
					dataMap.remove(1);
				dataMap.put(1, indexService.queryChart2DataNew(user.getLedgerId()));

			// 7日复费率电量 2
			
				if(dataMap.containsKey(2))
					dataMap.remove(2);
				dataMap.put(2, indexService.queryChart5Data(user.getLedgerId()));

			// 上月能耗财务数据 3
			
				if(dataMap.containsKey(3))
					dataMap.remove(3);
				dataMap.put(3, indexService.queryChart3DataNew(user.getLedgerId()));

			// 月功率因数 4
			
				if(dataMap.containsKey(4))
					dataMap.remove(4);
				Map<String,Object> resultMap4 = new HashMap<String, Object>();
				LedgerBean ledger = ledgerService.selectByLedgerId(user.getLedgerId());
				if(ledger != null)
					resultMap4.put("ledgerpf", ledger.getThresholdValue());
				resultMap4.put("data", phoneService.getPFData(user.getLedgerId(), 1, new Date(), 5,1));
				dataMap.put(4, resultMap4);
			

			// 30日电量 5
			
				if(dataMap.containsKey(5))
					dataMap.remove(5);
				dataMap.put(5, indexService.getChart7DataNew(user.getLedgerId()));
			// 月需量曲线 7
			
				if(dataMap.containsKey(7))
					dataMap.remove(7);
				dataMap.put(7, indexService.queryChart4Data(user.getLedgerId()));
			// 耗电排名 9
			
				if(dataMap.containsKey(9))
					dataMap.remove(9);
				Map<String,Object> resultMap = new HashMap<String, Object>();
				Map<String,Object> queryMap = new HashMap<String, Object>();
				queryMap.put("ledgerId",  user.getLedgerId()); // 分户Id
				queryMap.put("meterType", 1); // 计量点类型: 0:综合, 1:电, 2:水, 3:气, 4:热
				queryMap.put("beginTime", com.leegern.util.DateUtil.getCurrMonthFirstDay());
				queryMap.put("endTime",   com.leegern.util.DateUtil.getCurrentDate(com.leegern.util.DateUtil.DEFAULT_SHORT_PATTERN));
				resultMap.put("data", indexService.queryUseEnergyRanking(queryMap));
				dataMap.put(9, resultMap);
			dataCacheMap.put(user.getAccountId(), dataMap);
		}
		Log.info("For monitor,App data cache current size is "+ dataCacheMap.size()+",expected size is "+userList.size());
	}
	
	/**
	 * 根据用户以及数据类型获取数据
	 * @param accountId 用户Id
	 * @param type 数据类型
	 * @return
	 */
	public static Map<String,Object> getCacheData(Long accountId,Integer type){
		if(dataCacheMap.containsKey(accountId)){
			Map<Integer,Map<String,Object>> dataMap = dataCacheMap.get(accountId);
			if(dataMap.containsKey(type))
				return dataMap.get(type);
		}
		return null;
	}
}
