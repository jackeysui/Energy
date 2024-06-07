package com.linyang.energy.utils;

import java.security.MessageDigest;
import java.util.Locale;
import java.security.NoSuchAlgorithmException;

import com.linyang.common.web.common.Log;

/**
 * 对密码进行加密和验证的类
 * 
 * @author gaofeng
 * 
 */
public class CipherUtil {

	// 十六进制下数字到字符的映射数组
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/** * 把inputString加密 */
	public static String generatePasswordSha256(String inputString) {
		return encodeBySHA256(inputString);
	}

	/**
	 * 验证输入的密码是否正确
	 * 
	 * @param inputString
	 *            输入的字符串
	 * @return 验证结果，TRUE:正确 FALSE:错误
	 */
	public static boolean validatePassword(String password, String inputString, boolean needMd5Encode) {
		Locale.setDefault(Locale.ENGLISH);
		if (password == null && inputString.length() == 0)
			return false;
		if ((null != password && needMd5Encode && encodeBySHA256(inputString).equals(password))
				|| (null != password && !needMd5Encode && inputString.equalsIgnoreCase(password))) {

			return true;
		} else {
			return false;
		}
	}

	/** 对字符串进行MD5加密 */
	public static String encodeBySHA256(String originString) {
		if (originString != null) {
			try {
				String sha1 = "SHA-256";
				MessageDigest md = MessageDigest.getInstance(sha1);
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				Log.info("encodeBySHA256 error NoSuchAlgorithmException");
			}
		}
		return null;
	}

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param 字节数组
	 * @return 十六进制字符串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/** 将一个字节转化成十六进制形式的字符串 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	

//	public static void main(String[] args){
//		System.out.println(encodeBySHA256("111111"));
//	}

}
