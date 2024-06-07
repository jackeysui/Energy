/**
 * 
 */
package com.linyang.energy.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.page.Page;
import com.linyang.energy.service.LogService;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.energy.utils.DateUtil;


/** 
 * @类功能说明： 日志管理控制类
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-5 下午03:57:49  
 * @版本：V1.0  */
@Controller
@RequestMapping("/logController")
public class LogController extends BaseController{
	@Autowired
	private LogService logService;
	/**
	 * 
	 * 函数功能说明 :跳转到日志列表页面
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoLogManage")
	public ModelAndView gotoLogManage(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String endDate = DateUtil.getCurrentDateStr("yyyy-MM-dd");
		String beginDate = DateUtil.getYesterdayDateStr("yyyy-MM-dd");
		Map<String, String> map2 = DictionaryDataFactory.getDictionaryData(request).get("opt_type");
		Map<String, String> map3 = DictionaryDataFactory.getDictionaryData(request).get("object_type");
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		map.put("optList", map2);
		map.put("objectList",map3);
		return new ModelAndView("energy/system/logManage",map);
	}
	/**
	 * 
	 * 函数功能说明  :查询日志列表
	 * @param pageInfo 前台参数集合
	 * @return  Map<String,Object>
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getLogPageData")
	public @ResponseBody Map<String, Object> getLogPageData(HttpServletRequest request) throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		Page page = null;
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
		if(queryMap.containsKey("queryMap"))
			mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
		    List<Map<String,Object>> list = logService.getLogPageData(page,mapQuery);
		    Map<String, String> map2 = DictionaryDataFactory.getDictionaryData(request).get("opt_type");
		    Map<String, String> map3 = DictionaryDataFactory.getDictionaryData(request).get("object_type");
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 	for (Map<String,Object> mm : list) {
				mm.put("OPT_TYPE", map2.get(mm.get("OPTTYPE").toString()));
				mm.put("OBJECT_TYPE", map3.get(mm.get("OBJECTTYPE").toString()));
				if(mm.get("OPTTIME") != null)
					mm.put("OPTTIME", df.format(mm.get("OPTTIME")));
			}
			 map.put("list", list);
			 map.put("page", page);
			 map.put("queryMap",mapQuery);
		return map;
	}


}
