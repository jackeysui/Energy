package com.linyang.energy.utils;

import java.util.UUID;

/**
 * 负责生成主键，因为需要考虑到MYSQL的情况，所以不要用ORACLE的sequence
  @description:
  @version:0.1
  @author:Cherry
  @date:Dec 3, 2013
 */
public class SequenceUtils {
	
	private static Long seq = 0L;

	private SequenceUtils(){}
	
	/**
	 * 得到数据库序列（唯一主键，采用时间戳保证唯一）
	 * @return
	 */
	public synchronized static long getDBSequence(){
		 long sequence = System.currentTimeMillis();
		 if(seq>=sequence){  
		    seq++;
		 } else {
		    seq = sequence;
		 }
		 return seq;
	}
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
	 */
	public static String uuidWithSplit() {
		
		return UUID.randomUUID().toString();
	}

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
