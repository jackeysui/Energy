package com.linyang.energy.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 谐波标注值对照表
 * 
 * @author gaofeng
 * 
 */
public class HarConstant {
	private static Map<Integer, Double> harVolTotal;// 电压总谐波畸变率

	private static Map<Integer, Double> harVol;// 各奇次谐波电压含有率

	private static Map<Integer, Map<Integer, Double>> harCur;// 各奇次谐波电流含有率

	static {
		harVolTotal = new HashMap<Integer, Double>();
		harVol = new HashMap<Integer, Double>();
		harCur = new HashMap<Integer, Map<Integer, Double>>();

		harVolTotal.put(380, 5.0);
		harVolTotal.put(6, 4.0);
		harVolTotal.put(10, 4.0);
		harVolTotal.put(35, 3.0);
		harVolTotal.put(66, 3.0);
		harVolTotal.put(110, 2.0);

		harVol.put(380, 4.0);
		harVol.put(6, 3.2);
		harVol.put(10, 3.2);
		harVol.put(35, 2.4);
		harVol.put(66, 2.4);
		harVol.put(110, 1.6);

		Map<Integer, Double> tmp = new HashMap<Integer, Double>();
		tmp.put(2, 78.0);
		tmp.put(3, 62.0);
		tmp.put(4, 39.0);
		tmp.put(5, 62.0);
		tmp.put(6, 26.0);
		tmp.put(7, 44.0);
		tmp.put(8, 19.0);
		tmp.put(9, 21.0);
		tmp.put(10, 16.0);
		tmp.put(11, 28.0);
		tmp.put(12, 13.0);
		tmp.put(13, 24.0);
		tmp.put(14, 11.0);
		tmp.put(15, 12.0);
		tmp.put(16, 9.7);
		tmp.put(17, 18.0);
		tmp.put(18, 8.6);
		tmp.put(19, 16.0);
		tmp.put(20, 7.8);
		tmp.put(21, 8.9);
		tmp.put(22, 7.1);
		tmp.put(23, 14.0);
		tmp.put(24, 6.5);
		tmp.put(25, 12.0);
		harCur.put(380, tmp);

		tmp = new HashMap<Integer, Double>();
		tmp.put(2, 43.0);
		tmp.put(3, 34.0);
		tmp.put(4, 21.0);
		tmp.put(5, 34.0);
		tmp.put(6, 14.0);
		tmp.put(7, 24.0);
		tmp.put(8, 11.0);
		tmp.put(9, 11.0);
		tmp.put(10, 8.5);
		tmp.put(11, 16.0);
		tmp.put(12, 7.1);
		tmp.put(13, 13.0);
		tmp.put(14, 6.1);
		tmp.put(15, 6.8);
		tmp.put(16, 5.3);
		tmp.put(17, 10.0);
		tmp.put(18, 4.7);
		tmp.put(19, 9.0);
		tmp.put(20, 4.3);
		tmp.put(21, 4.9);
		tmp.put(22, 3.9);
		tmp.put(23, 7.4);
		tmp.put(24, 3.6);
		tmp.put(25, 6.8);
		harCur.put(6, tmp);

		tmp = new HashMap<Integer, Double>();
		tmp.put(2, 26.0);
		tmp.put(3, 20.0);
		tmp.put(4, 13.0);
		tmp.put(5, 20.0);
		tmp.put(6, 8.5);
		tmp.put(7, 15.0);
		tmp.put(8, 6.4);
		tmp.put(9, 6.8);
		tmp.put(10, 5.1);
		tmp.put(11, 9.3);
		tmp.put(12, 4.3);
		tmp.put(13, 7.9);
		tmp.put(14, 3.7);
		tmp.put(15, 4.1);
		tmp.put(16, 3.2);
		tmp.put(17, 6.0);
		tmp.put(18, 2.8);
		tmp.put(19, 5.4);
		tmp.put(20, 2.6);
		tmp.put(21, 2.9);
		tmp.put(22, 2.3);
		tmp.put(23, 4.5);
		tmp.put(24, 2.1);
		tmp.put(25, 4.1);
		harCur.put(10, tmp);

		tmp = new HashMap<Integer, Double>();
		tmp.put(2, 15.0);
		tmp.put(3, 12.0);
		tmp.put(4, 7.7);
		tmp.put(5, 12.0);
		tmp.put(6, 5.1);
		tmp.put(7, 8.8);
		tmp.put(8, 3.8);
		tmp.put(9, 4.1);
		tmp.put(10, 3.1);
		tmp.put(11, 5.6);
		tmp.put(12, 2.6);
		tmp.put(13, 4.7);
		tmp.put(14, 2.2);
		tmp.put(15, 2.5);
		tmp.put(16, 1.9);
		tmp.put(17, 3.6);
		tmp.put(18, 1.7);
		tmp.put(19, 3.2);
		tmp.put(20, 1.5);
		tmp.put(21, 1.8);
		tmp.put(22, 1.4);
		tmp.put(23, 2.7);
		tmp.put(24, 1.3);
		tmp.put(25, 2.5);
		harCur.put(35, tmp);

		tmp = new HashMap<Integer, Double>();
		tmp.put(2, 16.0);
		tmp.put(3, 13.0);
		tmp.put(4, 8.1);
		tmp.put(5, 13.0);
		tmp.put(6, 5.4);
		tmp.put(7, 9.3);
		tmp.put(8, 4.1);
		tmp.put(9, 4.3);
		tmp.put(10, 3.3);
		tmp.put(11, 5.9);
		tmp.put(12, 2.7);
		tmp.put(13, 5.0);
		tmp.put(14, 2.6);
		tmp.put(15, 2.6);
		tmp.put(16, 2.0);
		tmp.put(17, 3.8);
		tmp.put(18, 1.8);
		tmp.put(19, 3.4);
		tmp.put(20, 1.6);
		tmp.put(21, 1.9);
		tmp.put(22, 1.5);
		tmp.put(23, 2.8);
		tmp.put(24, 1.4);
		tmp.put(25, 2.6);
		harCur.put(66, tmp);

		tmp = new HashMap<Integer, Double>();
		tmp.put(2, 12.0);
		tmp.put(3, 9.6);
		tmp.put(4, 8.1);
		tmp.put(5, 6.0);
		tmp.put(6, 4.0);
		tmp.put(7, 6.8);
		tmp.put(8, 3.0);
		tmp.put(9, 3.2);
		tmp.put(10, 2.4);
		tmp.put(11, 4.3);
		tmp.put(12, 2.0);
		tmp.put(13, 3.7);
		tmp.put(14, 1.9);
		tmp.put(15, 1.9);
		tmp.put(16, 1.5);
		tmp.put(17, 2.8);
		tmp.put(18, 1.3);
		tmp.put(19, 2.5);
		tmp.put(20, 1.2);
		tmp.put(21, 1.4);
		tmp.put(22, 1.1);
		tmp.put(23, 2.1);
		tmp.put(24, 1.0);
		tmp.put(25, 1.9);
		harCur.put(110, tmp);
	}

	public static Double getHarVolTotalStand(int volLevel) {
		return harVolTotal.get(volLevel);
	}

	public static Double getHarVolStand(int volLevel) {
		return harVol.get(volLevel);
	}

	public static Double getHarIStand(int volLevel, int harNo) {
		Map<Integer, Double> h = harCur.get(volLevel);
		if (h != null) {
			return h.get(harNo);
		}
		return null;
	}
}
