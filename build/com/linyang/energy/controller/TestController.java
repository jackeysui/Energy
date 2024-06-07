package com.linyang.energy.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linyang.common.web.page.Page;

@Controller
@RequestMapping("/test")
public class TestController extends BaseController{

	public static final List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
	static{
		Map<String,Object> dataMap = null;
		for (int i = 1; i < 100; i++) {
			dataMap = new HashMap<String, Object>();
			dataMap.put("userId",i);
			dataMap.put("username",RandomUtils.nextLong()+"");
			dataMap.put("password",RandomUtils.nextLong()+"");
			dataMap.put("date",new Date());
			dataMap.put("time",new Timestamp(System.currentTimeMillis()));
			resultList.add(dataMap);
		}
	}
	
	@RequestMapping(value = "/getPageData", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getAjaxPageDate(HttpServletRequest request) throws IOException{
		Map<String,Object> map = new HashMap<String, Object>();
		Page page = null;
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
		
		int beginIndex = (page.getPageIndex()-1)*page.getPageSize() ;
		int endIndex = page.getPageIndex()*page.getPageSize();
		if(endIndex >= resultList.size())
			endIndex = resultList.size();
		
		map.put("list", resultList.subList(beginIndex, endIndex));
		//设置总条数
		page.setRowCount(resultList.size());
		
		map.put("page", page);
		return map;
	}
	
	@RequestMapping(value = "/getSelectData", method = RequestMethod.POST)
	public   @ResponseBody String getSelectData(HttpServletResponse response) throws IOException{
		StringBuilder sb = new StringBuilder("[");
		for (int i = 1; i < 101; i++) {
			String randomString = RandomUtils.nextLong()+"";
			sb.append("{id:'"+i+"',label:'"+randomString+"',value:'"+randomString+"'}").append(",");
		}
		sb.deleteCharAt(sb.length()-1).append("]");
		return sb.toString();
		/*response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().write(sb.toString());
		response.getWriter().close();*/
	}
}
