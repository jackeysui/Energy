package com.linyang.energy.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 读取wx.properties文件中的微信小程序配置信息
 * 
 * @author fzJiang
 *
 */
public class WxConstant {

	private static final String BUNDLE_NAME = "wx";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private WxConstant() {

	}

	public static String getString(String key) {
		return getString(key, "");
	}

	public static String getString(String key, String defaultValue) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return defaultValue;
		}
	}

	/**
	 * appId
	 */
	public static final String WX_APPID = getString("wx.appid", "");

	/**
	 * appSecret
	 */
	public static final String WX_APPSECRET = getString("wx.appsecret", "");

	/**
	 * token
	 */
	public static final String WX_TOKEN = getString("wx.token", "");

	/**
	 * aesKey
	 */
	public static final String WX_AESKEY = getString("wx.aeskey", "");

	/**
	 * 小程序服务器地址(https://域名/)
	 */
	public static final String WX_SERVER = getString("wx.server", "");

	/**
	 * 小程序首页
	 */
	public static final String WX_HOME_PAGE = getString("wx.homePage", "");

	/**
	 * 用户绑定模板消息id
	 */
	public static final String WX_BIND_TEMPLATEID = getString("wx.bindTemplateId", "");

	/**
	 * 用户解除绑定模板消息id
	 */
	public static final String WX_UNBIND_TEMPLATEID = getString("wx.unBindTemplateId", "");

	/**
	 * 模板消息字体颜色
	 */
	public static final String WX_TEMPLATE_COLOR = getString("wx.templateColor", "");

	/**
	 * 资源文件夹路径(首页广告列表banner/公众号二维码qrCode)
	 */
	public static final String WX_RESOURCES_PATH = getString("wx.resourcesPath", "");

	/**
	 * 二维码底部地址字体大小,默认30
	 */
	public static final int WX_QRCODE_FONT_SIZE = Integer.valueOf(getString("wx.qrCodeFontSize", "30"));
}
