package com.linyang.energy.service;

import java.io.File;

import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * 微信小程序Service
 * 
 * @author fzJiang
 *
 */
public interface MiniAppService {

	String CREATE_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode";
	String GET_WXACODE_URL = "https://api.weixin.qq.com/wxa/getwxacode";
	String GET_WXACODE_UNLIMIT_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";

	/**
	 * 接口C: 获取小程序页面二维码.
	 * 
	 * <pre>
	 * 适用于需要的码数量较少的业务场景
	 * 通过该接口，仅能生成已发布的小程序的二维码。
	 * 可以在开发者工具预览时生成开发版的带参二维码。
	 * 带参二维码只有 100000 个，请谨慎调用。
	 * </pre>
	 *
	 * @param fileName
	 *            文件名
	 * @param path
	 *            不能为空，最大长度 128 字节
	 * @param width
	 *            默认430 二维码的宽度
	 */
	File createQrcode(String filePath, String fileName, String path, int width) throws WxErrorException;

	File createQrcode(String filePath, String fileName, String path) throws WxErrorException;

	/**
	 * 接口A: 获取小程序码.
	 * 
	 * @param fileName
	 *            文件名
	 * @param path
	 *            不能为空，最大长度 128 字节
	 * @param width
	 *            默认430 二维码的宽度
	 * @param autoColor
	 *            默认true 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
	 * @param lineColor
	 *            auth_color 为 false 时生效，使用 rgb 设置颜色 例如
	 *            {"r":"xxx","g":"xxx","b":"xxx"}
	 */
	File createWxCode(String filePath, String fileName, String path, int width, boolean autoColor, WxMaCodeLineColor lineColor)
			throws WxErrorException;

	File createWxCode(String filePath, String fileName, String path, int width) throws WxErrorException;

	File createWxCode(String filePath, String fileName, String path) throws WxErrorException;

	/**
	 * 接口B: 获取小程序码（永久有效、数量暂无限制）,并添加云终端地址,即scene.
	 * 
	 * <pre>
	 * 通过该接口生成的小程序码，永久有效，数量暂无限制。
	 * 用户扫描该码进入小程序后，将统一打开首页，开发者需在对应页面根据获取的码中 scene 字段的值，再做处理逻辑。
	 * 使用如下代码可以获取到二维码中的 scene 字段的值。
	 * 调试阶段可以使用开发工具的条件编译自定义参数 scene=xxxx 进行模拟，开发工具模拟时的 scene 的参数值需要进行 urlencode
	 * </pre>
	 * 
	 * @param scene
	 *            最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，
	 *            其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
	 * @param page
	 *            必须是已经发布的小程序页面，例如 "pages/index/index" ,如果不填写这个字段，默认跳主页面
	 * @param width
	 *            默认false 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
	 * @param autoColor
	 *            默认true 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
	 * @param lineColor
	 *            auth_color 为 false 时生效，使用 rgb 设置颜色 例如
	 *            {"r":"xxx","g":"xxx","b":"xxx"}
	 */
	File createWxCodeLimit(String filePath, String scene, String page, int width, boolean autoColor,
			WxMaCodeLineColor lineColor) throws WxErrorException;

	File createWxCodeLimit(String filePath, String scene, String page, int width) throws WxErrorException;

	/**
	 * 发送用户绑定消息到小程序
	 * 
	 * @param openId
	 *            用户openId
	 * @param formId
	 *            提交表单id
	 * @param addr
	 *            云终端地址
	 * @throws WxErrorException
	 */
	void sendBindTemplate(String openId, String formId, String addr);

	/**
	 * 发送用户解除绑定消息到小程序
	 * 
	 * @param openId
	 *            用户openId
	 * @param formId
	 *            提交表单id
	 * @param addr
	 *            云终端地址
	 * @throws WxErrorException
	 */
	void sendUnBindTemplate(String openId, String formId, String addr);
}
