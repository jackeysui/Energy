package com.linyang.energy.controller.yunNanEnergy;

import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.StructureService;
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
 * @ Date       ：Created in 11:15 2020/5/27
 * @ Description：用能单位产品结构信息 + 非能源产品信息
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/structure")
public class StructureController extends BaseController {
	
	@Autowired
	private StructureService structureService;
	
	/**
	 * 跳转到用能单位产品结构信息页面
	 * @author catkins.
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @exception
	 * @date 2020/5/27 15:35
	 */
	@RequestMapping( value = "/structurePage" )
	public ModelAndView gotoStructurePage(HttpServletRequest request){
		return new ModelAndView(URLConstant.PRODUCT_STRUCTURE);
	}
	
	/**
	 * 跳转到非能源产品信息页面
	 * @author catkins.
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @exception
	 * @date 2020/5/27 15:35
	 */
	@RequestMapping( value = "/materielPage" )
	public ModelAndView gotoMaterielPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.MATERIEL_PRODUCT);
	}
	
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
			queryMap.put("pId",ledgerId);
		}
		List<Map<String, Object>> map = structureService.queryenProductPageList( page, queryMap );
		
		if( map != null && map.size() > 0 )
			isExistData = true;
		resultMap.put( "isExistData", isExistData );
		resultMap.put( "datas", map );
		resultMap.put( "page", page );
		return resultMap;
	}
	
	@RequestMapping( value = "/saveAndModify" , method = RequestMethod.POST)
	public @ResponseBody Integer saveAndModify(HttpServletRequest request){
		Map<String,Object> params = new HashMap<String,Object>( 0 );
		Integer result = null;
		try {
			params = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = structureService.saveAndModify( params );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据id查询产品结构相关数据
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/queryDataById" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryDataById( HttpServletRequest request ){
		String productId = request.getParameter( "productId" );
		Map<String, Object> map = structureService.queryDataById( Long.parseLong( productId ));
		return map;
	}
	
	
	/**
	 * 删除产品结构数据
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/deleteDataById" , method = RequestMethod.POST)
	public @ResponseBody Boolean deleteDataById(HttpServletRequest request){
		String productId = request.getParameter( "productId" );
		return structureService.delStructureData( Long.parseLong( productId ) );
	}
	
	//uploadStructure
	
	/**
	 * 产品上传
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:53
	 */
	@RequestMapping( value = "/uploadStructure" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> uploadStructure(HttpServletRequest request){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = structureService.uploadStructure( map );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
}
