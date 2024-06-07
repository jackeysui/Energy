package com.linyang.energy.service;/*
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


import com.linyang.energy.model.ServiceReportBean;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:56 2020/11/27
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface AnShanMessageService {
	
	/**
	 * 创建报告
	 */
	public void createServiceReport();
	
	/**
	 * 查询服务报告
	 * @return
	 */
	public List<ServiceReportBean> searchSerivceReport(Map<String, Object> param);
	
	/**
	 * 获取服务报告需要的数据
	 * @param reportId
	 * @param accountId
	 * @return
	 */
	public Map<String, Object> queryReportInfoData(long reportId, long accountId);
	
	/**
	 * 获取服务报告提示
	 * @param reportId
	 * @param accountId
	 * @return
	 */
	public String getReportTips(long reportId, long accountId);
	
	
	public Map<String,Object> queryReportInfoData2(long reportId, long accountId);
	
	
}
