package com.linyang.energy.controller.yunNanEnergy;

import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.InstitutionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
 * @ Date       ：Created in 11:20 2020/5/19
 * @ Description：计量器具检定/校准管理
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/institutions")
public class InstitutionsController extends BaseController {
	
	@Autowired
	private InstitutionsService institutionsService;
	
	@RequestMapping( value = "/institutionsPage" )
	public ModelAndView gotoInstitutionsPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.INSTITUTIONS_DEVICE);
	}
	
	/**
	 * 查询分页列表
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/queryPageList" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryPageList( HttpServletRequest request ){
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
		
		List<Map<String, Object>> map = institutionsService.queryInstitutionsPageList( page, queryMap );
		
		if( map != null && map.size() > 0 )
			isExistData = true;
		resultMap.put( "isExistData", isExistData );
		resultMap.put( "datas", map );
		resultMap.put( "page", page );
		return resultMap;
	}
	
	/**
	 * 新增和修改方法
	 * @return
	 */
	@RequestMapping( value = "/saveorModify" ,method = RequestMethod.POST )
	public @ResponseBody Integer saveorModify(HttpServletRequest request){
		Map<String,Object> params = new HashMap<String,Object>( 0 );
		Integer result = null;
		try {
			params = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = institutionsService.saveAndModify( params );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据id查询检定校准相关数据
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/queryDataById" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryDataById( HttpServletRequest request ){
		String msiId = request.getParameter( "msiId" );
		String aoId = request.getParameter( "aoId" );
		Map<String, Object> map = institutionsService.queryDataById( Long.parseLong( msiId ), Long.parseLong( aoId ) );
		return map;
	}
	
	@RequestMapping( value = "/deleteDataById" , method = RequestMethod.POST)
	public @ResponseBody Boolean deleteDataById(HttpServletRequest request){
		String msiId = request.getParameter( "msiId" );
		String aoId = request.getParameter( "aoId" );
		return institutionsService.deleteInsData( Long.parseLong( msiId ),Long.parseLong( aoId ) );
	}
	
	@RequestMapping( value = "/uploadData" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> uploadData(HttpServletRequest request){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = institutionsService.uploadData( map );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	
	
	/**
	 * 得到计量设备数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryMsiData")
	public @ResponseBody String queryMsiData(HttpServletRequest request) {
		String str = "";
		List<Map<String, Object>> dataList = institutionsService.queryMsiData();
		//按照插件所需要的格式拼接字符串
		if(!CollectionUtils.isEmpty(dataList)) {
			StringBuffer sb = new StringBuffer("[");
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("MSI_ID"))
						.append("',label:'").append(data.get("MSI_NAME"))
						.append("',value:'").append(data.get("MSI_ID"))
						.append("',num:'").append(data.get("MSI_ID"))
						.append("',count:'0'},");
			}
			str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		}
		else{
			str = "[]";
		}
		return str;
	}
	
	/**
	 * 得到检测单位数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryAoData")
	public @ResponseBody String queryAoData(HttpServletRequest request) {
		String str = "";
		List<Map<String, Object>> dataList = institutionsService.queryAoData();
		//按照插件所需要的格式拼接字符串
		if(!CollectionUtils.isEmpty(dataList)) {
			StringBuffer sb = new StringBuffer("[");
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("AO_ID"))
						.append("',label:'").append(data.get("AO_NAME"))
						.append("',value:'").append(data.get("AO_ID"))
						.append("',num:'").append(data.get("AO_ID"))
						.append("',count:'0'},");
			}
			str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		}
		else{
			str = "[]";
		}
		return str;
	}
	

}
