package com.linyang.energy.staticdata;

import java.util.concurrent.TimeUnit;

import com.linyang.common.web.common.Log;


/**
 * 数据初始化线程类
  @description:
  @version:0.1
  @author:Cherry
  @date:Sep 4, 2013
 */
public class InitDataThread extends Thread{
	@Override
	public void run() {
		try {
			TimeUnit.SECONDS.sleep(1);
			Log.info("开始初始化静态数据");
			DictionaryDataFactory.initStaticData();
			Log.info("初始化静态数据成功");
		} catch (InterruptedException e) {
			Log.error(this.getClass() + ".run()--初始化静态数据失败");
		}
	}

}
