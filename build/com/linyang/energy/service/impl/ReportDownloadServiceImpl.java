package com.linyang.energy.service.impl;/*
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
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.reportanalysis.ReportDownloadMapper;
import com.linyang.energy.service.ReportDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 10:15 2021/4/7
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class ReportDownloadServiceImpl implements ReportDownloadService {
	
	@Autowired
	private ReportDownloadMapper reportDownloadMapper;
	
	@Override
	public List<Map<String, Object>> queryReportPageList(Map<String, Object> queryMap, Page page) {
		List<Map<String, Object>> maps = null;
		if (page != null) {
			Map<String, Object> map = new HashMap<String, Object>( queryMap );
			map.put( Dialect.pageNameField, page );
			maps = reportDownloadMapper.queryReportPageList( map );
		}
		return maps;
	}
	
	@Override
	public Map<String, Object> queryReport(Map<String, Object> queryMap) {
		return reportDownloadMapper.queryReport( queryMap );
	}
	
	@Override
	public List<Map<String, Object>> queryLedgerByRegion(Long ledgerId) {
		return reportDownloadMapper.queryLedgerByRegion( ledgerId );
	}
}
