package com.linyang.energy.utils;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:03 2019/9/10
 * @ Description：map集合排序
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public class ListSortUtil {
	
	/**
	 * 给list<Map<String,Object>>类型的集合排序
	 * 按照实际调峰量排序
	 * @author catkins.
	 * @param list		需要排序的集合
	 * @return void
	 * @exception
	 * @date 2019/9/10 14:10
	 */
	public static void ListSort4AP(List<Map<String,Object>> list) {
		Collections.sort( list, new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String,Object> o1, Map<String,Object> o2) {
				try {
					double dt1 = Double.parseDouble( o1.get( "aptotal" ).toString() );
					double dt2 = Double.parseDouble( o2.get( "aptotal" ).toString() );
					if (dt1 < dt2) {
						return 1;
					} else if (dt1 > dt2) {
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
	
	/**
	 * 给list<Map<String,Object>>类型的集合排序
	 * 按照企业计划调峰量排序
	 * @author catkins.
	 * @param list		需要排序的集合
	 * @return void
	 * @exception
	 * @date 2019/9/10 14:10
	 */
	public static void ListSort4Chpeak(List<Map<String,Object>> list) {
		Collections.sort( list, new Comparator<Map<String,Object>>() {
			@Override
			public int compare(Map<String,Object> o1, Map<String,Object> o2) {
				try {
					double dt1 = Double.parseDouble( o1.get( "LEDGERPITCHPEAK" ).toString() );
					double dt2 = Double.parseDouble( o2.get( "LEDGERPITCHPEAK" ).toString() );
					if (dt1 < dt2) {
						return 1;
					} else if (dt1 > dt2) {
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
