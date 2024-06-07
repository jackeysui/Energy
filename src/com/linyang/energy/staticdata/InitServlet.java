package com.linyang.energy.staticdata;

/**
 * 
 */

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.energy.utils.MapMapping;
import com.linyang.util.LogUtils;
import com.linyang.util.cache.CacheFactory;
import com.linyang.util.cache.ProviderName;


/**
 * 初始化静态数据
  @description:
  @version:0.1
  @author:Cherry
  @date:2013-7-4
 */
public class InitServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4476124607471199907L;

	@Override
    public void init() throws ServletException{
		String initParameter = getInitParameter("isNeedStaticDatas");
		//初始化日志文件，把引用的日志文件加载到本模块日志中
		LogUtils.initLog4OtherSystem(Log.logger.getName());
		//为了防止登录后第一次加载首页初始化xml数据慢，直接在系统启动时就加载进内存
		Log.info("配置地图数据加载:"+MapMapping.getMapMapping());
		if(initParameter !=null && "true".equals(initParameter)){
			//加载静态数据
            Log.info("开始初始化静态数据");
            DictionaryDataFactory.initStaticData();
            Log.info("初始化静态数据成功");
		}
		CacheFactory.getCache(ProviderName.EhCacheProvider,"defaultCache");
	}
	
	
}
