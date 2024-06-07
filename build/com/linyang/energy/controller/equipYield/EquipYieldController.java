package com.linyang.energy.controller.equipYield;

import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.EquipYieldService;
import com.linyang.energy.utils.ExcelUtil;
import com.linyang.energy.utils.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：${date} ${time}
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/equipYield")
public class EquipYieldController extends BaseController {
	
	@Autowired
	private EquipYieldService equipYieldService;
	
	/**
	 * 跳转到企业产能申报
	 *
	 * @param
	 * @return
	 * @throws
	 * @author catkins.
	 * @date 2021/7/20 11:17
	 */
	@RequestMapping("/gotoEquipPage")
	public ModelAndView gotoEquipPage(HttpServletRequest request) {
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr( WebConstant.getChartBaseDate(), com.linyang.energy.utils.DateUtil.SHORT_PATTERN );
		Map<String, Object> param = new HashMap<String, Object>();
		param.put( "beginTime", com.linyang.energy.utils.DateUtil.convertDateToStr( com.linyang.energy.utils.DateUtil.getCurrMonthFirstDay( WebConstant.getChartBaseDate() ), com.linyang.energy.utils.DateUtil.SHORT_PATTERN ) );
		param.put( "endTime", currentTime );
		return new ModelAndView( "energy/equipYield/equip_yield", "params", param );
	}
	
	/**
	 * 查询表格头
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryTableHead", method = RequestMethod.POST)
	public @ResponseBody
	List<Map<String, Object>> queryTableHead(HttpServletRequest request) {
		Map<String, Object> pageInfo = new HashMap<>( 0 );
		try {
			pageInfo = jacksonUtils.readJSON2Map( request.getParameter( "pageInfo" ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return equipYieldService.queryTableHead( pageInfo );
	}
	
	/**
	 * 查询列表数据
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryDataList", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> queryDataList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>( 0 );
		Map<String, Object> pageInfo = new HashMap<>( 0 );
		try {
			pageInfo = jacksonUtils.readJSON2Map( request.getParameter( "pageInfo" ) );
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = equipYieldService.queryEquipList( pageInfo );
		return result;
	}
	
	
	/**
	 * 导出Excel模板文件
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getExcelTemplate")
	public @ResponseBody
	void getExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> pageInfo = jacksonUtils.readJSON2Map( request.getParameter( "pageInfo" ) );
			String title = "企业产能申报模板";
			byte[] bs = title.getBytes();
			title = new String( bs, "ISO-8859-1" );
			response.setHeader( "Content-Disposition", "attachment;filename=" + title + ".xls" );    //指定下载的文件名
			response.setContentType( "application/vnd.ms-excel" );
			equipYieldService.getExcelTemplate( "Cache.xls", response.getOutputStream(), pageInfo );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传模板文件
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/uploadTemplate")
	public @ResponseBody boolean uploadTemplate(HttpServletRequest request, HttpServletResponse response) {
		boolean flag = false;
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile( "filePath" );
		// 文件名
		String fileName = "";
		if (multipartFile.getOriginalFilename().length() > 0 && multipartFile.getOriginalFilename() != null) {
			fileName = multipartFile.getOriginalFilename();
		}
		// 文件保存路径
		String filePath = System.getProperty( "user.dir" ).substring( 0, System.getProperty( "user.dir" ).lastIndexOf( "\\" ) ) +
				File.separator + "webapps" + request.getContextPath() + File.separator + "upload" + File.separator + "yunjian" +
				File.separator + "equipTemplate" + File.separator; // 文件存储文件夹路径
		
		// 文件全路径
		String fullPath = filePath + fileName;
		File file = new File( fullPath );
		try {
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
			
			// 得到Excel的数据
			List<Object[]> datas = ExcelUtil.importExcel( file, 0 );
			if (!datas.isEmpty()) {
				flag = equipYieldService.insertExcelData( datas );
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			removeFile(file);
		}
		return flag;
	}
	
	/**
	 * 删除文件
	 * @param file 文件对象
	 * @return 成功或失败
	 */
	private boolean removeFile(File file){
		boolean flag = false;
		if (file.exists()) {
			// 删除文件
			flag = file.delete();
		}
		return flag;
	}
	
	
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getExcel")
	public @ResponseBody void getExcel(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> pageInfo = jacksonUtils.readJSON2Map( request.getParameter( "pageInfo" ) );
			String title = "企业产能申报";
			byte[] bs = title.getBytes();
			title = new String( bs, "ISO-8859-1" );
			response.setHeader( "Content-Disposition", "attachment;filename=" + title + ".xls" );    //指定下载的文件名
			response.setContentType( "application/vnd.ms-excel" );
			equipYieldService.getExcel( "Cache.xls", response.getOutputStream(), pageInfo );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
