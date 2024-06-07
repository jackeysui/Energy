package com.linyang.energy.controller.reportanalysis;/*
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
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.ReportDownloadService;
import com.linyang.energy.utils.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 10:13 2021/4/7
 * @ Description：服务报告下载
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/reportDownload")
public class ReportDownloadController extends BaseController {
	
	@Autowired
	private ReportDownloadService reportDownloadService;
	
	@RequestMapping("/templateView")
	public ModelAndView gotoTranscribingPage(HttpServletRequest request) {
		Map<String, String> param = new HashMap<String, String>();
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr( WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.MONTH_PATTERN );
		Long ledgerId = this.getSessionUserInfo( request ).getLedgerId();
		param.put( "baseTime", currentTime );
		param.put( "pledgerId", ledgerId.toString() );
		return new ModelAndView( "/energy/report/report_download", "params", param );
	}
	
	@RequestMapping(value = "/queryPageList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> queryPageList(HttpServletRequest request) {
		Map<String, Object> pageInfo = new HashMap<String, Object>( 0 );
		Map<String, Object> queryMap = new HashMap<String, Object>( 0 );
		Map<String, Object> resultMap = new HashMap<>( 0 );
		Boolean isExistData = false;
		Page page = null;
		try {
			pageInfo = jacksonUtils.readJSON2Map( request.getParameter( "pageInfo" ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (pageInfo.containsKey( "params" )) {
			queryMap = (Map<String, Object>) pageInfo.get( "params" );
		}
		
		if (pageInfo.get( "pageIndex" ) != null && pageInfo.get( "pageSize" ) != null)
			page = new Page( Integer.valueOf( pageInfo.get( "pageIndex" ).toString() ), Integer.valueOf( pageInfo.get( "pageSize" ).toString() ) );
		else
			page = new Page();
		
		List<Map<String, Object>> maps = reportDownloadService.queryReportPageList( queryMap, page );
		
		if (maps != null && maps.size() > 0)
			isExistData = true;
		resultMap.put( "isExistData", isExistData );
		resultMap.put( "datas", maps );
		resultMap.put( "page", page );
		return resultMap;
	}
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void downloadReportFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> pageInfo = new HashMap<String, Object>( 0 );
		try {
			pageInfo = jacksonUtils.readJSON2Map( request.getParameter( "pageInfo" ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> map = reportDownloadService.queryReport( pageInfo );
		if( !map.containsKey( "reportPath" ) )
			return;
		String filePath = map.get( "reportPath" ).toString();
		response.setContentType( "text/html;charset=utf-8" );
		String name = map.get( "ledgerName" ).toString()+"-"+map.get( "reportDate" )+".docx";
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			//文件
			File file = new File( filePath );
			if (file.exists()) {
				response.addHeader( "Content-Disposition", "attachment;filename=" + new String(name.getBytes("utf-8"),"iso-8859-1") );
				byte buf[] = new byte[1024];
				fis = new FileInputStream( file );
				bis = new BufferedInputStream( fis );
				os = response.getOutputStream();
				int i = bis.read( buf );
				while (i != -1) {
					os.write( buf, 0, i );
					i = bis.read( buf );
				}
			} else {
				System.out.println( "文件不存在" );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			os.close();
			bis.close();
			fis.close();
		}
	}
	
	
	/**
	 * 根据权限获得企业列表
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLedgerByRegion")
	public @ResponseBody
	String getLedgerByRegion(HttpServletRequest request) {
		String str = "";
		Long ledgerId = this.getSessionUserInfo( request ).getLedgerId();
		List<Map<String, Object>> dataList = reportDownloadService.queryLedgerByRegion( ledgerId );
		//按照插件所需要的格式拼接字符串
		if (!CollectionUtils.isEmpty( dataList )) {
			StringBuffer sb = new StringBuffer( "[" );
			for (Map<String, Object> data : dataList) {
				sb.append( "{id:'" ).append( data.get( "ID" ) )
						.append( "',label:'" ).append( data.get( "NAME" ) )
						.append( "',value:'" ).append( data.get( "ID" ) )
						.append( "',num:'" ).append( data.get( "ID" ) )
						.append( "',count:'0'}," );
			}
			str = sb.deleteCharAt( sb.lastIndexOf( "," ) ).append( "]" ).toString();
		} else {
			str = "[]";
		}
		return str;
	}
	
	
}
