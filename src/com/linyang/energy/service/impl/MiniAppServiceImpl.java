package com.linyang.energy.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leegern.util.DateUtil;
import com.linyang.energy.service.MiniAppService;
import com.linyang.energy.utils.WxConstant;
import com.linyang.energy.utils.WxQrCodeRequestExecutor;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.binarywang.wx.miniapp.bean.WxMaQrcode;
import cn.binarywang.wx.miniapp.bean.WxMaTemplateMessage;
import cn.binarywang.wx.miniapp.bean.WxMaWxcode;
import cn.binarywang.wx.miniapp.bean.WxMaWxcodeLimit;
import me.chanjar.weixin.common.exception.WxErrorException;

/**
 * 微信小程序Service实现类
 * 
 * @author fzJiang
 *
 */
@Service
public class MiniAppServiceImpl implements MiniAppService {

	@Autowired
	private WxMaService wxService;// 微信小程序API

	@Override
	public File createQrcode(String filePath, String fileName, String path, int width) throws WxErrorException {
		return this.wxService.execute(new WxQrCodeRequestExecutor(this.wxService.getRequestHttp(), filePath, fileName,
				WxConstant.WX_QRCODE_FONT_SIZE), CREATE_QRCODE_URL, new WxMaQrcode(path, width));
	}

	@Override
	public File createQrcode(String filePath, String fileName, String path) throws WxErrorException {
		return this.createQrcode(filePath, fileName, path, 100);
	}

	@Override
	public File createWxCode(String filePath, String fileName, String path, int width, boolean autoColor,
			WxMaCodeLineColor lineColor) throws WxErrorException {
		WxMaWxcode wxMaWxcode = new WxMaWxcode();
		wxMaWxcode.setPath(path);
		wxMaWxcode.setWidth(width);
		wxMaWxcode.setAutoColor(autoColor);
		wxMaWxcode.setLineColor(lineColor);
		return this.wxService.execute(new WxQrCodeRequestExecutor(this.wxService.getRequestHttp(), filePath, fileName,
				WxConstant.WX_QRCODE_FONT_SIZE), GET_WXACODE_URL, wxMaWxcode);
	}

	@Override
	public File createWxCode(String filePath, String fileName, String path, int width) throws WxErrorException {
		return this.createWxCode(filePath, fileName, path, width, true, null);
	}

	@Override
	public File createWxCode(String filePath, String fileName, String path) throws WxErrorException {
		return this.createWxCode(filePath, fileName, path, 100, true, null);
	}

	@Override
	public File createWxCodeLimit(String filePath, String scene, String page, int width, boolean autoColor,
			WxMaCodeLineColor lineColor) throws WxErrorException {
		WxMaWxcodeLimit wxMaWxcodeLimit = new WxMaWxcodeLimit();
		wxMaWxcodeLimit.setScene(scene);
		wxMaWxcodeLimit.setPage(page);
		wxMaWxcodeLimit.setWidth(width);
		wxMaWxcodeLimit.setAutoColor(autoColor);
		wxMaWxcodeLimit.setLineColor(lineColor);
		return this.wxService.execute(new WxQrCodeRequestExecutor(this.wxService.getRequestHttp(), filePath, scene,
				WxConstant.WX_QRCODE_FONT_SIZE), GET_WXACODE_UNLIMIT_URL, wxMaWxcodeLimit);
	}

	@Override
	public File createWxCodeLimit(String filePath, String scene, String page, int width) throws WxErrorException {
		return this.createWxCodeLimit(filePath, scene, page, width, true, null);
	}

	@Override
	public void sendBindTemplate(String openId, String formId, String addr) {
		// 初始化模板消息
		WxMaTemplateMessage msg = new WxMaTemplateMessage();
		// 接收用户的openId
		msg.setToUser(openId);
		// 设置模板消息id
		msg.setTemplateId(WxConstant.WX_BIND_TEMPLATEID);
		// 模板字体的颜色
		msg.setColor(WxConstant.WX_TEMPLATE_COLOR);
		// 点击模板卡片后的跳转页面(默认小程序首页)
		msg.setPage(WxConstant.WX_HOME_PAGE);
		// 表单Id
		msg.setFormId(formId);
		// 模板需要放大的关键词
		// msg.setEmphasisKeyword("Emmm...");

		// 绑定提醒
		WxMaTemplateMessage.Data data = new WxMaTemplateMessage.Data("云终端绑定成功", WxConstant.WX_TEMPLATE_COLOR);
		msg.addData(data);
		// 绑定时间
		data = new WxMaTemplateMessage.Data(DateUtil.getCurrentDateStr(DateUtil.DEFAULT_FULL_PATTERN),
				WxConstant.WX_TEMPLATE_COLOR);
		msg.addData(data);
		// 设备编号,即云终端地址
		data = new WxMaTemplateMessage.Data(addr, WxConstant.WX_TEMPLATE_COLOR);
		msg.addData(data);
		// 提示信息
		data = new WxMaTemplateMessage.Data("云终端默认密码为【111111】,请尽快修改", WxConstant.WX_TEMPLATE_COLOR);
		msg.addData(data);
		// 发送模板消息
		try {
			this.wxService.getMsgService().sendTemplateMsg(msg);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendUnBindTemplate(String openId, String formId, String addr) {
		// 初始化模板消息
		WxMaTemplateMessage msg = new WxMaTemplateMessage();
		// 接收用户的openId
		msg.setToUser(openId);
		// 设置模板消息id
		msg.setTemplateId(WxConstant.WX_UNBIND_TEMPLATEID);
		// 模板字体的颜色
		msg.setColor(WxConstant.WX_TEMPLATE_COLOR);
		// 点击模板卡片后的跳转页面(默认小程序首页)
		msg.setPage(WxConstant.WX_HOME_PAGE);
		// 表单Id
		msg.setFormId(formId);
		// 模板需要放大的关键词
		// msg.setEmphasisKeyword("Emmm...");

		// 解除绑定提醒
		WxMaTemplateMessage.Data data = new WxMaTemplateMessage.Data("value1", "云终端删除成功", WxConstant.WX_TEMPLATE_COLOR);
		msg.addData(data);
		// 解除绑定时间
		data = new WxMaTemplateMessage.Data(DateUtil.getCurrentDateStr(DateUtil.DEFAULT_FULL_PATTERN),
				WxConstant.WX_TEMPLATE_COLOR);
		msg.addData(data);
		// 设备编号,即云终端地址
		data = new WxMaTemplateMessage.Data(addr, WxConstant.WX_TEMPLATE_COLOR);
		msg.addData(data);
		// 提示信息
		data = new WxMaTemplateMessage.Data("您可通过扫描二维码再次进行绑定", WxConstant.WX_TEMPLATE_COLOR);
		msg.addData(data);

		// 发送模板消息
		try {
			this.wxService.getMsgService().sendTemplateMsg(msg);
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}

}
