package com.linyang.energy.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import cn.binarywang.wx.miniapp.bean.AbstractWxMaQrcodeWrapper;
import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.common.util.http.RequestHttp;
import me.chanjar.weixin.common.util.http.apache.InputStreamResponseHandler;
import me.chanjar.weixin.common.util.http.apache.Utf8ResponseHandler;

/**
 * 微信小程序二维码的POST请求执行器，请求的参数为String, 返回的结果为图片文件
 * 
 * @author fzJiang
 *
 */
public class WxQrCodeRequestExecutor implements RequestExecutor<File, AbstractWxMaQrcodeWrapper> {

	protected RequestHttp<CloseableHttpClient, HttpHost> requestHttp;
	private String filePath;// 生成文件路径
	private String fileName;// 生成文件名称,以地址命名
	private int fontSize;// 提示信息字体大小

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public WxQrCodeRequestExecutor(RequestHttp requestHttp, String filePath, String fileName, int fontSize) {
		this.requestHttp = requestHttp;
		this.filePath = filePath;
		this.fileName = fileName;
		this.fontSize = fontSize;
	}

	@Override
	public File execute(String uri, AbstractWxMaQrcodeWrapper ticket) throws WxErrorException, IOException {
		HttpPost httpPost = new HttpPost(uri);
		if (requestHttp.getRequestHttpProxy() != null) {
			httpPost.setConfig(RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build());
		}
		httpPost.setEntity(new StringEntity(ticket.toJson()));

		try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost);
				InputStream inputStream = InputStreamResponseHandler.INSTANCE.handleResponse(response);) {
			Header[] contentTypeHeader = response.getHeaders("Content-Type");
			if (contentTypeHeader != null && contentTypeHeader.length > 0 && ContentType.APPLICATION_JSON.getMimeType()
					.equals(ContentType.parse(contentTypeHeader[0].getValue()).getMimeType())) {
				String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
				throw new WxErrorException(WxError.fromJson(responseContent));
			}
			// 创建带地址等文字说明的小程序二维码图片文件
			return WxQrCodeUtil.createQrcodeFile(inputStream, fontSize, filePath, fileName);

		} finally {
			httpPost.releaseConnection();
		}
	}
}