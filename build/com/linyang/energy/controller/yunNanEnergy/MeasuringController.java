package com.linyang.energy.controller.yunNanEnergy;

import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.MeasuringService;
import net.sf.json.util.JSONUtils;
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
 * @ Date       ：Created in 13:33 2020/5/14
 * @ Description：计量器具管理
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/measuring")
public class MeasuringController extends BaseController {
	
	@Autowired
	private MeasuringService measuringService;

	@RequestMapping( value = "/measuringPage" )
	public ModelAndView gotoMeasuringPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.MEASURING_DEVICE);
	}

	
	@RequestMapping( value = "/queryPageList" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryPageList( HttpServletRequest request ){
		Map<String, Object> pageInfo = new HashMap<String,Object>( 0 );
		Map<String, Object> queryMap = new HashMap<String,Object>( 0 );
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		Boolean isExistData = false;
		Page page = null;
		try {
			pageInfo = jacksonUtils.readJSON2Map( request.getParameter( "pageInfo" ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		Long ledgerId = this.getSessionUserInfo(request).getLedgerId();
		if(pageInfo.get("pageIndex")!= null && pageInfo.get("pageSize") != null)
			page = new Page(Integer.valueOf(pageInfo.get("pageIndex").toString()),Integer.valueOf(pageInfo.get("pageSize").toString()));
		else
			page = new Page();
		if(pageInfo.containsKey("params")){
			queryMap = (Map<String,Object>)pageInfo.get("params");
			queryMap.put("ledgerId",ledgerId);
		}
		List<Map<String, Object>> map = measuringService.queryMeasuringPageList( page, queryMap );
		
		if( map != null && map.size() > 0 )
			isExistData = true;
		resultMap.put( "isExistData", isExistData );
		resultMap.put( "datas", map );
		resultMap.put( "page", page );
		return resultMap;
	}
	
	@RequestMapping( value = "/saveAndModify" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> saveAndModify(HttpServletRequest request){
		Map<String, Object> params = new HashMap<String,Object>( 0 );
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		try {
			params = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			Integer code = measuringService.saveAndModify( params );
			resultMap.put( "code" , code );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@RequestMapping( value = "/queryDataById" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryDataById(HttpServletRequest request){
		String msiId = request.getParameter( "msiId" );
		Map<String, Object> map = measuringService.queryDataById( Long.parseLong( msiId ) );
		return map;
	}
	
	@RequestMapping( value = "/deleteDataById" , method = RequestMethod.POST)
	public @ResponseBody Boolean deleteDataById(HttpServletRequest request){
		String msiId = request.getParameter( "msiId" );
		return measuringService.deleteMsiDataById(Long.parseLong( msiId ));
	}
	
	
	/**
	 * 得到检测机构数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAOMessage")
	public @ResponseBody String getAOMessage(HttpServletRequest request) {
		String str = "";
		List<Map<String, Object>> dataList = measuringService.queryAlignOrg();
		//按照插件所需要的格式拼接字符串
		if(!CollectionUtils.isEmpty(dataList)) {
			StringBuffer sb = new StringBuffer("[");
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("AOID"))
						.append("',label:'").append(data.get("AONAME"))
						.append("',value:'").append(data.get("AOID"))
						.append("',num:'").append(data.get("AOID"))
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
	 * 得到设备类型数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getMsiType")
	public @ResponseBody String getMsiType(HttpServletRequest request) {
		String str = "";
		List<Map<String, Object>> dataList = measuringService.queryMsiType();
		//按照插件所需要的格式拼接字符串
		if(!CollectionUtils.isEmpty(dataList)) {
			StringBuffer sb = new StringBuffer("[");
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("MSITYPEID"))
						.append("',label:'").append(data.get("MSITYPENAME"))
						.append("',value:'").append(data.get("MSITYPEID"))
						.append("',num:'").append(data.get("MSITYPEID"))
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
	 * 页面上传数据按钮的方法
	 * @return
	 */
	@RequestMapping( value = "/uploadData" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> uploadData(HttpServletRequest request){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = measuringService.uploadData( map );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 得到设备类型数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getDataCode")
	public @ResponseBody String getDataCode(HttpServletRequest request) {
		String str = "";
		List<Map<String, Object>> dataList = measuringService.queryDataCode();
		
		//按照插件所需要的格式拼接字符串
		if(!CollectionUtils.isEmpty(dataList)) {
			StringBuffer sb = new StringBuffer("[");
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("DATAID"))
						.append("',label:'").append(data.get("DATANAME"))
						.append("',value:'").append(data.get("DATAID"))
						.append("',num:'").append(data.get("DATAID"))
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
	 * 得到设备类型数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getEntype")
	public @ResponseBody String getEntype(HttpServletRequest request) {
		String str = "";
		List<Map<String, Object>> dataList = measuringService.queryEnType();
		
		//按照插件所需要的格式拼接字符串
		if(!CollectionUtils.isEmpty(dataList)) {
			StringBuffer sb = new StringBuffer("[");
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("ENTYPEID"))
						.append("',label:'").append(data.get("ENTYPENAME"))
						.append("',value:'").append(data.get("ENTYPEID"))
						.append("',num:'").append(data.get("ENTYPEID"))
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
	 * 根据权限获得企业列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLedgerByRegion")
	public @ResponseBody String getLedgerByRegion(HttpServletRequest request) {
		String str = "";
		Long ledgerId = this.getSessionUserInfo(request).getLedgerId();
		List<Map<String, Object>> dataList = measuringService.queryLedgerByRegion(ledgerId);
		//按照插件所需要的格式拼接字符串
		if(!CollectionUtils.isEmpty(dataList)) {
			StringBuffer sb = new StringBuffer("[");
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("ID"))
						.append("',label:'").append(data.get("NAME"))
						.append("',value:'").append(data.get("ID"))
						.append("',num:'").append(data.get("ID"))
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
