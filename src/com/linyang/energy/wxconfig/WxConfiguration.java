package com.linyang.energy.wxconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linyang.energy.utils.WxConstant;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;

/**
 * 小程序配置信息
 * 
 */
@Configuration
public class WxConfiguration {

	@Bean
	public WxMaInMemoryConfig wxMaInMemoryConfig() {
		// 初始化小程序配置信息
		WxMaInMemoryConfig config = new WxMaInMemoryConfig();
		// appId
		config.setAppid(WxConstant.WX_APPID);
		config.setSecret(WxConstant.WX_APPSECRET);
		config.setToken(WxConstant.WX_TOKEN);
		config.setAesKey(WxConstant.WX_AESKEY);
		return config;
	}

	@Bean
	public WxMaService wxMaService() {
		// 初始化微信API服务
		WxMaService service = new WxMaServiceImpl();
		service.setWxMaConfig(wxMaInMemoryConfig());

		return service;
	}

	@Bean
	public WxMaMessageRouter router() {
		// 配置路由
		WxMaMessageRouter router = new WxMaMessageRouter(wxMaService());
		// router.rule().handler(logHandler).end();
		// .rule().async(false).content("模板").handler(templateMsgHandler).end()
		// .rule().async(false).content("文本").handler(textHandler).end()
		// .rule().async(false).content("图片").handler(picHandler).end()
		// .rule().async(false).content("二维码").handler(qrcodeHandler).end();
		return router;
	}
}
