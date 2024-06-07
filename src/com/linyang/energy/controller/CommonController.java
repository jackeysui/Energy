package com.linyang.energy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.util.CommonMethod;

/**
 * 用来写公共请求的Controller
  @description:
  @version:0.1
  @author:Cherry
  @date:Nov 29, 2013
 */
@Controller
@RequestMapping("/commonController")
public class CommonController extends BaseController{

	/**
	 * 跳转到500页面
	 * @return
	 */
	@RequestMapping(value="/show500Page")
	public String show500Page(){
		return "/framework/500";
	}
	
	/**
	 * 跳转到404页面
	 * @return
	 */
	@RequestMapping(value="/show404Page")
	public String show404Page(){
		return "/framework/404";
	}

    /**
     * 默认错误页面
     * @return
     */
    @RequestMapping(value="/showDefaultErrorPage")
    public String showDefaultErrorPage(){
        return "/framework/defaultError";
    }
	
	/**
	 * 动态设置语言
	 * @return
	 */
	@RequestMapping(value="/setDynamicLanguage")
	public @ResponseBody boolean setDynamicLanguage(HttpServletRequest request,String language){
		String str_language = language; if (str_language == null || str_language.trim().length()==0) str_language = "zh_CN"; request.getSession().setAttribute("localeValue", str_language);
		return true;
	}
	
	@RequestMapping(value="/getDataCodeDataList")
	public @ResponseBody List<Map<String, Object>> getDataCodeDataList(HttpServletRequest request,String dataCode){
		return getDataCodeList(request,dataCode);
	}
	
	private  List<Map<String, Object>> getDataCodeList(HttpServletRequest request,String dataCode) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, String> map = DictionaryDataFactory.getDictionaryData(request).get(dataCode);
		if (CommonMethod.isMapNotEmpty(map)) {
			Map<String,Object> lm = null;
			boolean isInteger = false;
			String key = new ArrayList<String>(map.keySet()).get(0);
			if(CommonMethod.isInteger(key))
				isInteger = true;
			if(isInteger){//如果是整数则排序	
				Map<Integer,String> map2 = new TreeMap<Integer, String>();
				for (Entry<String, String> o : map.entrySet())
					map2.put(Integer.parseInt(o.getKey()),o.getValue());
				for (Entry<Integer, String> entry : map2.entrySet()) {
					lm = new HashMap<String,Object>();
					lm.put("ID", entry.getKey());
					lm.put("VALUE",entry.getValue());
					list.add(lm);
				}
			}else{
				for (Entry<String, String> o : map.entrySet()) {
					lm = new HashMap<String,Object>();
					lm.put("ID", o.getKey());
					lm.put("VALUE", o.getValue());
					list.add(lm);
				}
			}
		}
		return list;
	}
}
