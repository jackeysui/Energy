package com.linyang.energy.controller.yunNanEnergy;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.EnergyManagementService;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.SequenceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:45 2020/5/21
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/energyManagement")
public class EnergyManagementController extends BaseController {
	
	@Autowired
	private EnergyManagementService energyManagementService;
	
	/**
	 * 跳转到企业联系人页面
	 * @author catkins.
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @exception
	 * @date 2020/5/22 13:17
	 */
	@RequestMapping( value = "/enManagePage" )
	public ModelAndView gotoEnManagePage(HttpServletRequest request){
		return new ModelAndView(URLConstant.MANAGEMENT_PAGE);
	}
	
	/**
	 * 跳转到能源管理人员页面
	 * @author catkins.
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @exception
	 * @date 2020/5/22 13:17
	 */
	@RequestMapping( value = "/conEnManagePage" )
	public ModelAndView gotoConEnManagePage(HttpServletRequest request){
		return new ModelAndView(URLConstant.COMPANY_MANAGER);
	}
	
	/**
	 * 跳转到项目负责人页面
	 * @author catkins.
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @exception
	 * @date 2020/5/22 13:17
	 */
	@RequestMapping( value = "/enMonitorPage" )
	public ModelAndView gotoMonitorPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.MONITOR_MANAGER);
	}
	
	/**
	 * 跳转到计量人员配置页面
	 * @author catkins.
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @exception
	 * @date 2020/5/22 13:17
	 */
	@RequestMapping( value = "/calculaterPage" )
	public ModelAndView gotoCalculaterPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.CALCULATER_MANAGER);
	}
	
	@RequestMapping( value = "/queryPageList" , method = RequestMethod.POST)
	public @ResponseBody
	Map<String,Object> queryPageList(HttpServletRequest request ){
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
		List<Map<String, Object>> map = energyManagementService.queryenManagementPageList( page, queryMap );
		
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
			result = energyManagementService.saveAndModify( params );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据id查询企业联系人相关数据
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/queryDataById" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryDataById( HttpServletRequest request ){
		String enmId = request.getParameter( "enmId" );
		Map<String, Object> map = energyManagementService.queryDataById( Long.parseLong( enmId ));
		return map;
	}
	
	/**
	 * 删除企业联系人记录
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/deleteDataById" , method = RequestMethod.POST)
	public @ResponseBody Boolean deleteDataById(HttpServletRequest request){
		String enmId = request.getParameter( "enmId" );
		return energyManagementService.deleteRecordData( Long.parseLong( enmId ) );
	}
	
	/**
	 * 联系人信息上传
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:53
	 */
	@RequestMapping( value = "/uploadData" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> uploadData(HttpServletRequest request){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = energyManagementService.uploadData( map );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 管理人员信息上传
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:53
	 */
	@RequestMapping( value = "/uploadCompany" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> uploadCompany(HttpServletRequest request){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = energyManagementService.uploadCompany( map );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 项目负责人信息上传
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:53
	 */
	@RequestMapping( value = "/uploadMonitor" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> uploadMonitor(HttpServletRequest request){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = energyManagementService.uploadMonitor( map );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 计量人员配置新增和修改方法
	 * @author catkins.
	 * @param request
	 * @return java.lang.Integer
	 * @exception
	 * @date 2020/5/22 15:27
	 */
	@RequestMapping( value = "/saveorModifyCal" , method = RequestMethod.POST)
	public @ResponseBody Integer saveorModifyCal(HttpServletRequest request){
		Map<String,Object> params = new HashMap<String,Object>( 0 );
		Integer result = null;
		try {
			params = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = energyManagementService.saveAndModifyCal( params );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 *  专业技术证书上传
	 */
	@RequestMapping(value="/uploadShowCompanyImg")
	public @ResponseBody Map<String,Object> uploadShowCompanyImg(HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		String cerId = request.getParameter( "cerId" );
		String delFileName = request.getParameter( "fileName" );
		
		boolean isSuccess = false;
		String msg = "获取文件失败";
		resultMap.put("isSuccess", isSuccess);
		resultMap.put("msg", msg);
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
		MultipartFile multipartFile = multipartRequest.getFile("filePath");
		if(!validateData(multipartFile))
			return resultMap;
		InputStream inputStream = null;
		try {
			inputStream = multipartFile.getInputStream();
			if(!validateData(inputStream))
				return resultMap;
			BufferedImage image = ImageIO.read(inputStream);
			if(multipartFile.getSize() > 400*1024){
				isSuccess = false;
				msg = "上传失败,图片大小超出范围";
			} else if(image.getWidth() > 800 || image.getHeight() > 600){
				isSuccess = false;
				msg = "上传失败,图片尺寸超出范围";
			} else {
				String filePath = System.getProperty( "user.dir" ).substring( 0,System.getProperty( "user.dir" ).lastIndexOf( "\\" ) ) + File.separator + "webapps"
						+ request.getContextPath() + File.separator + "upload" + File.separator + "yunNan" + File.separator + "calculater_chart" + File.separator; // 文件存储文件夹路径
				String fileName = "";
				if(multipartFile.getOriginalFilename().length()>0&&multipartFile.getOriginalFilename()!=null){
					fileName = multipartFile.getOriginalFilename();
				}// 图片原名
				long dbSequence = SequenceUtils.getDBSequence();
				String saveName =  dbSequence + fileName.substring(fileName.lastIndexOf(".")); // 图片存储名
				File file = null;
				if(!DataUtil.checkFilePath(filePath) && !DataUtil.checkFilePath(saveName)){
					file = new File(filePath + saveName);
				}
				File parentFile = null;
				if(file!=null) {
					parentFile= file.getParentFile();
				}
				if(file == null || parentFile == null || !file.exists())
					if(!parentFile.exists()&&!parentFile.mkdirs()){
						throw new IOException("创建目录失败");
					}
				if(!file.exists() && file !=null)
					multipartFile.transferTo(file);
				isSuccess = true;
				msg = "上传成功";
				resultMap.put("filePath", filePath);
				resultMap.put("fileName", saveName);
				resultMap.put( "certId",cerId );
				if ( cerId != null && !cerId.equals( "" ) ) {
					if (delFileName != null && !delFileName.equals( "" )) {
						energyManagementService.deleteFile(delFileName,request);
					}
//					Integer integer = energyManagementService.modifyCalData( resultMap );
				} else {
					resultMap.put( "cerId",dbSequence );
					energyManagementService.savePic(resultMap);
				}
			}
		} catch (IOException e) {
			Log.info("uploadShowCompanyImg error IOException");
			isSuccess = false;
			msg = "上传失败,发生未知异常";
		}finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e){
					Log.error(this.getClass()+" uploadShowCompanyImg inputStream close error!");
				}
			}
		}
		resultMap.put("isSuccess", isSuccess);
		resultMap.put("msg", msg);
		return resultMap;
	}
	
	/**
	 * 删除企业联系人记录
	 * @param request
	 * @return
	 */
	@RequestMapping( value = "/deleteCalById" , method = RequestMethod.POST)
	public @ResponseBody Boolean deleteCalById(HttpServletRequest request){
		String enmId = request.getParameter( "enmId" );
		String cerId = request.getParameter( "cerId" );
		String fileName = request.getParameter( "fileName" );
		boolean flag = energyManagementService.deleteCalData( Long.parseLong( enmId ),Long.parseLong( cerId ) );
		if(flag){
			energyManagementService.deleteFile(fileName,request);
		}
		return flag;
	}
	
	
	/**
	 * 项目负责人信息上传
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:53
	 */
	@RequestMapping( value = "/uploadcalculater" , method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> uploadCalculater(HttpServletRequest request){
		Map<String, Object> result = null;
		try {
			Map<String, Object> map = jacksonUtils.readJSON2Map( request.getParameter( "params" ) );
			result = energyManagementService.uploadCalculater( map,request );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	
	
}
