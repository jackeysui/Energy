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

import com.linyang.common.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 10:15 2021/4/7
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface ReportDownloadService {
	
	List<Map<String,Object>> queryReportPageList(Map<String,Object> queryMap, Page page);
	
	Map<String,Object> queryReport(Map<String,Object> queryMap);
	
	
	
	List<Map<String, Object>> queryLedgerByRegion(Long ledgerId);
}
