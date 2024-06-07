package com.linyang.energy.utils;/*
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  - /// |    |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.'  >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 *
 *
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *            佛祖保佑     永不宕机     永无BUG
 */

import com.linyang.energy.model.DistributionBean;

import java.util.*;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 11:29 2021/1/5
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public class MapKeyComparator implements Comparator<String> {
	@Override
	public int compare(String str1, String str2) {
		return str1.compareTo(str2);
	}
	
	/**
	 * 使用 Map按key进行排序
	 * @param map
	 * @return
	 */
	public static Map<String, Object> sortMapByKeyForChartItem(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<>(new MapKeyComparator());
		sortMap.putAll(map);
		return sortMap;
	}


//	/**
//	 * 使用 Map按key进行排序
//	 * @author dingy
//	 * @param map
//	 * @return java.util.Map<java.lang.String,com.linyang.dc.dto.ChartItem>
//	 * @exception
//	 * @date 2018/10/25 14:27
//	 */
//	public static Map<String, String> sortMapByKeyForChartItem(Map<String, String> map) {
//		if (map == null || map.isEmpty()) {
//			return null;
//		}
//
//		Map<String, String> sortMap = new TreeMap<>(new MapKeyComparator());
//
//		sortMap.putAll(map);
//
//		return sortMap;
//	}
//
//
//	/**
//	 * 根据Map的value进行排序
//	 * @author dingy
//	 * @param map
//	 * @return java.util.Map<java.lang.String,java.lang.String>
//	 * @exception
//	 * @date 2018/10/25 14:26
//	 */
//	public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
//		if (oriMap == null || oriMap.isEmpty()) {
//			return null;
//		}
//		Map<String, String> sortedMap = new LinkedHashMap<String, String>();
//		List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(
//				oriMap.entrySet());
//		Collections.sort(entryList, new MapValueComparator());
//
//		Iterator<Map.Entry<String, String>> iter = entryList.iterator();
//		Map.Entry<String, String> tmpEntry = null;
//		while (iter.hasNext()) {
//			tmpEntry = iter.next();
//			sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
//		}
//		return sortedMap;
//	}

	/**
	 * 对传进来的bean的产污数量进行排序(从大到小)
	 * @author dingy
	 * @param list
	 * @return void
	 * @exception
	 * @date 2018/10/31 15:27
	 */
	public static void ListSort(List<DistributionBean> list) {
		Collections.sort( list, new Comparator<DistributionBean>() {
			@Override
			public int compare(DistributionBean o1, DistributionBean o2) {
				try {
					if (o1.getPollutCount() < o2.getPollutCount()) {
						return 1;
					} else if (o1.getPollutCount() > o2.getPollutCount()) {
						return -1;
					} else {
						return 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		} );
	}
}

