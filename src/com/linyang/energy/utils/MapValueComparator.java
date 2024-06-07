package com.linyang.energy.utils;

import java.util.Comparator;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 10:56 2018/12/20
 * @ Description：比较器(用于给map进行排序)
 * @ Modified By：:dingy
 * @Version: $version$
 */
public class MapValueComparator implements Comparator<Map.Entry<String, String>> {
	@Override
	public int compare(Map.Entry<String, String> me1, Map.Entry<String, String> me2) {
		
		return me1.getValue().compareTo(me2.getValue());
	}
}
