package com.linyang.energy.model;

/**
 * 微信小程序API接口返回基类
 * 
 * @author fzJiang
 *
 * @param <T>
 */
public class BaseWxReturnModel<T> {

	private boolean status;// 是否请求成功标识（boolean：1.true请求成功 2.false请求失败）

	private String errorMessage;// 请求失败原因（String）

	private T result;// 返回数据字符串（JSON）

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
