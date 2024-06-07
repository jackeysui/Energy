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

import net.coobird.thumbnailator.Thumbnails;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 17:42 2021/1/11
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public class ImgUtils {
	
	
	private static final Integer ZERO = 0;
	private static final Integer ONE_ZERO_TWO_FOUR = 1024;
	private static final Integer NINE_ZERO_ZERO = 900;
	private static final Integer THREE_TWO_SEVEN_FIVE = 3275;
	private static final Integer TWO_ZERO_FOUR_SEVEN = 2047;
	private static final Double ZERO_EIGHT_FIVE = 0.85;
	private static final Double ZERO_SIX = 0.6;
	private static final Double ZERO_FOUR_FOUR = 0.44;
	private static final Double ZERO_FOUR = 0.4;
	
	/**
	 * 根据指定大小压缩图片
	 *
	 * @param imageBytes  源图片字节数组
	 * @param desFileSize 指定图片大小，单位kb
	 * @return 压缩质量后的图片字节数组
	 */
	public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) {
		if (imageBytes == null || imageBytes.length <= ZERO || imageBytes.length < desFileSize * ONE_ZERO_TWO_FOUR) {
			return imageBytes;
		}
		long srcSize = imageBytes.length;
		double accuracy = getAccuracy( srcSize / ONE_ZERO_TWO_FOUR );
		try {
			while (imageBytes.length > desFileSize * ONE_ZERO_TWO_FOUR) {
				ByteArrayInputStream inputStream = new ByteArrayInputStream( imageBytes );
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( imageBytes.length );
				Thumbnails.of( inputStream )
						.scale( accuracy )
						.outputQuality( accuracy )
						.toOutputStream( outputStream );
				imageBytes = outputStream.toByteArray();
			}
		} catch (Exception e) {
		}
		return imageBytes;
	}
	
	/**
	 * 自动调节精度(经验数值)
	 *
	 * @param size 源图片大小
	 * @return 图片压缩质量比
	 */
	private static double getAccuracy(long size) {
		double accuracy;
		if (size < NINE_ZERO_ZERO) {
			accuracy = ZERO_EIGHT_FIVE;
		} else if (size < TWO_ZERO_FOUR_SEVEN) {
			accuracy = ZERO_SIX;
		} else if (size < THREE_TWO_SEVEN_FIVE) {
			accuracy = ZERO_FOUR_FOUR;
		} else {
			accuracy = ZERO_FOUR;
		}
		return accuracy;
	}
}




