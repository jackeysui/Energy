package com.linyang.energy.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * 小程序二维码图片操作工具类
 * 
 * @author fzJiang
 *
 */
public class WxQrCodeUtil {

	/**
	 * 创建小程序二维码图片文件
	 * 
	 * @param inputStream
	 *            图片流
	 * @param fileName
	 *            文件名,即表计地址
	 * @throws IOException
	 */
	public static File createQrcodeFile(InputStream inputStream, String fileName) throws IOException {
		//
		// 创建原文件
		File aCodeFile = File.createTempFile(fileName + "_", ".jpg");
		aCodeFile.deleteOnExit();

		try (FileOutputStream fos = new FileOutputStream(aCodeFile)) {
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}
			fos.flush();
			return aCodeFile;
		}
	}

	/**
	 * 创建带地址等文字说明的小程序二维码图片文件
	 * 
	 * @param inputStream
	 *            图片流
	 * @param fontSize
	 *            提示文字大小
	 * @param fileName
	 *            文件名,即表计地址
	 * @return
	 */
	public static File createQrcodeFile(InputStream inputStream, int fontSize, String filePath, String fileName) {
		try {
			// 读取原二维码图片，并构建绘图对象
			BufferedImage srcImage = ImageIO.read(inputStream);
			// 原二维码宽度与高度
			int imageWidth = srcImage.getWidth();
			int imageHeight = srcImage.getHeight();

			// 设置字体风格及大小
			Font font = new Font("微软雅黑", Font.BOLD, fontSize);
			// 文字区域初始化,高度为文字的高度
			Graphics2D g = (Graphics2D) srcImage.getGraphics();
			FontMetrics textArea = g.getFontMetrics(font);
			// 文字区域高度
			int textAreaHeight = textArea.getHeight();

			// 创建新的二维码绘图对象(原图片高度+文字高度)
			BufferedImage aCodeImage = new BufferedImage(imageWidth, imageHeight + textAreaHeight,
					BufferedImage.TYPE_INT_RGB);

			// 得到Graphics2D图片处理对象
			Graphics2D graphics2D = (Graphics2D) aCodeImage.getGraphics();

			// 在新的绘图区域绘制二维码
			graphics2D.drawImage(srcImage, 0, 0, imageWidth, imageHeight, null);

			// 绘制二维码额外信息区域
			// graphics2D.setColor(Color.GRAY);
			graphics2D.fillRect(0, imageHeight, imageWidth, textAreaHeight);

			// 绘制底部文字提示,文字在图片中的坐标,二维码底部居中
			graphics2D.setColor(Color.BLACK);
			graphics2D.setFont(font);
			graphics2D.drawString(fileName, (imageWidth - textArea.stringWidth(fileName)) / 2,
					imageHeight + textArea.getAscent());

			// 绘制结束,销毁绘制对象
			g.dispose();
			graphics2D.dispose();

			// 生成新的二维码文件
			File aCodeFile = new File(filePath, fileName + ".jpg");
			// 文件存在则删除
			if (aCodeFile.exists()) {
				aCodeFile.delete();
			}
			// 文件创建不成功
			if (!aCodeFile.createNewFile()) {
				return null;
			}

			FileOutputStream out = new FileOutputStream(aCodeFile);
			ImageIO.write(aCodeImage, "JPEG", out);
			out.close();
			return aCodeFile;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

}
