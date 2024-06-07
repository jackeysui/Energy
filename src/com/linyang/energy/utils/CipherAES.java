package com.linyang.energy.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 15:56 2019/12/13
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public class CipherAES {
	
	//解密
	public static String decryptNew(byte[] sSrc, String sKey) throws Exception {
		//sSrc 报文
		//sKey 秘钥
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(sSrc);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		
	}
	
	// 加密
	public static byte[] encrypt(String sSrc, String sKey) throws Exception
	{
		if (sKey == null)
		{
			System.out.print("Key为空null");
			return null;
		}
		if (sKey.length() != 16)
		{
			System.out.print("Key长度不是16位");
			return null;
		}
		// byte[] raw = sKey.getBytes();
		byte[] raw = sKey.getBytes(Charset.forName("utf-8"));
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"
		IvParameterSpec iv = new IvParameterSpec(sKey.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		return encrypted;
		
	}
}
