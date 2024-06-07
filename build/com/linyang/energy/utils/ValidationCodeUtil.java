package com.linyang.energy.utils;

import com.linyang.common.web.common.Log;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ValidationCodeUtil {

	//获取符号
	private final static Map<Integer, String> TYPE = new HashMap<>(2);
	private final static int SUB = 0;
	private final static int PLUS = 1;
	static {
		TYPE.put(SUB, "-");
		TYPE.put(PLUS, "+");
	}

	//获取验证码公式
	public static String[] generate() {
		Random random = new Random();
		int type = random.nextInt(2);
		int[] arr = new int[]{random.nextInt(50), random.nextInt(50), Integer.MAX_VALUE};
		switch (type) {
			case SUB :  // 不出现负数
				if (arr[0] < arr[1]) {
					arr[0] ^= arr[1];
					arr[1] ^= arr[0];
					arr[0] ^= arr[1];
				}
				arr[2] = arr[0] - arr[1];
				break;
			case PLUS :
				arr[2] = arr[0] + arr[1];
				break;
		}
		String[] ret = new String[] {arr[0] + "", arr[1] + "", arr[2] + ""};
		// 选一随机位置为"?"
		int pos = random.nextInt(3);
		String answer = ret[pos];
		ret[pos] = "?";
		return new String[]{ret[0]+TYPE.get(type) + ret[1] + "=" + ret[2], answer};
	}






	private BufferedImage image;// 图像
	private String str;// 验证码
	private static char code[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
			.toCharArray();

	public static final String SESSION_CODE_NAME = "code";

	private ValidationCodeUtil() {
		init();// 初始化属性
	}

	/*
	 * 取得RandomNumUtil实例
	 */
	public static ValidationCodeUtil Instance() {
		return new ValidationCodeUtil();
	}

	/*
	 * 取得验证码图片
	 */
	public BufferedImage getImage() {
		return this.image;
	}

	/*
	 * 取得图片的验证码
	 */
	public String getString() {
		return this.str;
	}

	private void init() {
		// 在内存中创建图象
		int width = 85, height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);



		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		// 随机产生干扰线，使图象中的验证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = Random(width);
			int y = Random(height);
			int xl =  Random(12);
			int yl =  Random(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		String[] s = generate();
		// 取随机产生的验证码(4位数字或字母)
//		StringBuffer sRand = new StringBuffer();
//		for (int i = 0; i < s[0].toString().length(); i++) {
//			String rand = String.valueOf(code[Random(code.length)]);
//			sRand.append(rand);
			// 将验证码显示到图象中
			g.setColor(new Color(20 +  Random(110), 20 +  Random(110), 20 +  Random(110)));
			g.drawString(s[0], 13 + 6, 16);
//		}
		// 赋值验证码
		this.str = s[1];
//		this.str = sRand.toString();

		// 图象生效
		g.dispose();
		this.image = image;/* 赋值图像 */
	}

	/*
	 * 给定范围获得随机颜色
	 */
	private Color getRandColor(int fc, int bc) {
		
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + Random(bc - fc);
		int g = fc + Random(bc - fc);
		int b = fc +  Random(bc - fc);
		return new Color(r, g, b);
	}
	
	
	private static int Random(int a){
	
		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {

			Log.error("get Random error!");
		}
		if(null != sr)
			return sr.nextInt(a);
		return 0;

	}

}
