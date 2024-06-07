package com.linyang.energy.utils;

import java.math.BigDecimal;import java.math.RoundingMode;

import com.linyang.util.DoubleUtils;

/**
 * 数据处理工具类
 * @author guosen
 * @date 2015-1-14
 */
public class DataUtil {

	/**
	 * 保留几位小数
	 * @param d	原数字
	 * @param i	位数
	 * @return
	 */
	public static double retained(double d,int i){
		BigDecimal bg = new BigDecimal(d);
		return bg.setScale(i, BigDecimal.ROUND_HALF_UP).doubleValue();
	} 
	
	/**
	 * 根据有功功率和无功功率得到功率因数
	 * @param a 有功功率
	 * @param b	无功功率
	 * @param arg 保留几位小数
	 * @return
	 */
	public static double getPF(Double a, Double b){
		return getPF(a, b, 2);
	}
	
	/**
	 * 根据有功功率和无功功率得到功率因数
	 * @param a 有功功率
	 * @param b	无功功率
	 * @param arg 保留几位小数
	 * @return
	 */
	public static double getPF(Double a, Double b, int arg){
		double pf = 0;
		if(a != null && b != null && Math.hypot(a, b) != 0){
			pf = DoubleUtils.getDoubleValue(doubleDivide(Math.abs(a), Math.hypot(a, b), arg), arg);
		}
		return pf;
	}
	
	/**
	 * 为了搞定国网安全检测....    Double == null 或者 null == Double 都不行, 只能用!=判断...
	 * @param d
	 * @return
	 */
	public static boolean doubleIsNull(Double d) {
		if (d != null)
			return false;
		return true;
	}
	
	public static BigDecimal parseDouble2BigDecimal(Double d) {
		if (d != null)
			return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_DOWN);
		return null;
	}
	

	public static Double doubleAdd(double d1, double d2) {
		return new BigDecimal(d1).add(new BigDecimal(d2)).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	public static Double doubleSubtract(double d1, double d2) {
		return new BigDecimal(d1).subtract(new BigDecimal(d2)).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	public static Double doubleMultiply(double d1, double d2) {
		return new BigDecimal(d1).multiply(new BigDecimal(d2)).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	public static Double doubleDivide(double d1, double d2) {
		return new BigDecimal(d1).divide(new BigDecimal(d2), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	public static Double doubleDivide(double d1, double d2, int scale) {
		return new BigDecimal(d1).divide(new BigDecimal(d2), scale, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	public static Double doubleDivide(double d1, double d2, int scale, int mode) {
		return new BigDecimal(d1).divide(new BigDecimal(d2), scale, mode).doubleValue();
	}
	public static Double doubleDivide(double d1, double d2, int scale, RoundingMode mode) {
		return new BigDecimal(d1).divide(new BigDecimal(d2), scale, mode).doubleValue();
	}

    //文件路径合法性判断
    public static boolean checkFilePath(String filePath){
        String str = filePath.toLowerCase();//统一转为小写

        String badStr = "del|..|diskcomp|copy|dos|exit|find|goto|md|mkdir|move|pause|print|rd|set|rem|ren|rmdir|replace";    //过滤掉的关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
	
	/**
	 * 新增一个计算方法
	 * @param d1
	 * @param d2
	 * @param var	保留几位小数
	 * @return
	 */
	public static Double doubleMultiply_new(double d1, double d2 , int var) {
		return new BigDecimal(d1).multiply(new BigDecimal(d2)).setScale(var, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	
	/**
	 * 新增一个计算方法
	 * @param d1
	 * @param d2
	 * @param var	保留几位小数
	 * @return
	 */
	public static Double doubleAdd_new(double d1, double d2 , int var) {
		return new BigDecimal(d1).add(new BigDecimal(d2)).setScale(var, BigDecimal.ROUND_HALF_DOWN).doubleValue();
	}
	
}
