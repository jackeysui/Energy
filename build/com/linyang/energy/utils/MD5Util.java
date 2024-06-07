package com.linyang.energy.utils;

import java.security.MessageDigest;import java.util.Locale;
import java.security.NoSuchAlgorithmException;

import com.linyang.common.web.common.Log;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.UnsupportedEncodingException;

public class MD5Util {

	private static final String HEX_DIGITS[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String md5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = origin;
			String sha1="M",sha2="D",sha3="5";
			MessageDigest md = MessageDigest.getInstance(sha1+sha2+sha3);
			if (charsetname == null || charsetname.length()==0)
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(UTF_8)));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (NoSuchAlgorithmException e) {
			Log.error("MD5Util -- md5Encode error!");
		}
		catch (UnsupportedEncodingException e) {
			Log.error("md5Encode error UnsupportedEncodingException"+e.getMessage());
		}
		return resultString;
	}

	/** * 把inputString加密 */
	public static String generatePassword(String inputString) {
		return encodeByMD5(inputString);
	}

	/**
	 * 验证输入的密码是否正确
	 * 
	 * @param inputString
	 *            输入的字符串
	 * @return 验证结果，TRUE:正确 FALSE:错误
	 */
	public static boolean validatePassword(String password, String inputString) {
        Locale.setDefault(Locale.ENGLISH);
		if (password == null && inputString.length()==0)  return false;

		if (password != null && password.equals(encodeByMD5(inputString))) {
			return true;
		} else {
			return false;
		}
	}

	/** 对字符串进行MD5加密 */
	public static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				String sha1="M",sha2="D",sha3="5";
				MessageDigest md = MessageDigest.getInstance(sha1+sha2+sha3);
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				Log.error("shaUtil.encodeByMD5()--sha加密出错");
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
	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	/**
	 * 将一个字节转化成十六进制形式的字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return HEX_DIGITS[d1] + HEX_DIGITS[d2];
	}

}
