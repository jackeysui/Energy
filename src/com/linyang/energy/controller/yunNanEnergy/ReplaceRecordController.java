package com.linyang.energy.controller.yunNanEnergy;

import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.ReplaceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:53 2020/5/20
 * @ Description：计量器具更换记录管理
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/replaceRecord")
public class ReplaceRecordController extends BaseController {
	
	@Autowired
	private ReplaceRecordService replaceRecordService;
	
	@RequestMapping( value = "/recordPage" )
	public ModelAndView gotoRecordPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.REPLACERECORD_PAGE);
	}
	
	/**
	 * 查询分页列表
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/queryPageList" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryPageList(HttpServletRequest request ){
		Map<String, Object> pageInfo = new HashMap<String,Object>( 0 );
		Map<String, Object> queryMap = new HashMap<String,Object>( 0 );
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		Boolean isExistData = false;
		Page page = null;
		Long ledgerId = 0L;
		try {
			pageInfo = jacksonUtils.readJSON2Map( request.getParameter( "pageInfo" ) );
			ledgerId = this.getSessionUserInfo(request).getLedgerId();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(pageInfo.get("pageIndex")!= null && pageInfo.get("pageSize") != null)
			page = new Page(Integer.valueOf(pageInfo.get("pageIndex").toString()),Integer.valueOf(pageInfo.get("pageSize").toString()));
		else
			page = new Page();
		if(pageInfo.containsKey("params")){
			queryMap = (Map<String,Object>)pageInfo.get("params");
			queryMap.put("ledgerId",ledgerId);
		}
		List<Map<String, Object>> map = replaceRecordService.queryRecordPageList( page, queryMap );
		
		if( map != null && map.size() > 0 )
			isExistData = true;
		resultMap.put( "isExistData", isExistData );
		resultMap.put( "datas", map );
		resultMap.put( "page", page );
		return resultMap;
	}
	
	@RequestMapping( value = "/saveorModify" , method = RequestMethod.POST)
	public @ResponseBody Integer saveorModify(HttpServletRequest request){
		Map<String,Object> params = new HashMap<String,Object>( 0 );
		Integer result = null;
		try {
			params = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = replaceRecordService.saveAndModify( params );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 根据id查询更换记录相关数据
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/queryDataById" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryDataById( HttpServletRequest request ){
		String oldMsiId = request.getParameter( "oldMsiId" );
		Map<String, Object> map = replaceRecordService.queryDataById( Long.parseLong( oldMsiId ));
		return map;
	}
	
	/**
	 * 删除更换记录
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/deleteDataById" , method = RequestMethod.POST)
	public @ResponseBody Boolean deleteDataById(HttpServletRequest request){
		String oldMsiId = request.getParameter( "oldMsiId" );
		return replaceRecordService.deleteRecordData( Long.parseLong( oldMsiId ) );
	}
	
	@RequestMapping( value = "/uploadData" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> uploadData(HttpServletRequest request){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = replaceRecordService.uploadData( map );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
}
