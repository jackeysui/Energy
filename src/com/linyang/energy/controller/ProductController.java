package com.linyang.energy.controller;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.utils.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.linyang.energy.common.URLConstant;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.ProductsBean;
import com.linyang.energy.service.ProductService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.SequenceUtils;

/**
 * 
 * @类功能说明： 产品管理
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2014-1-21 上午11:11:10  
 * @版本：V1.0
 */
@Controller
@RequestMapping("/productsManager")
public class ProductController extends BaseController{
	/**
	 * 注入产品管理Service
	 */
	@Autowired
	private ProductService productService;
	
	/**
	 * 进入产品管理页面
	 * @return
	 */
	@RequestMapping(value = "/gotoProductMain")
	public String gotoProductMain(){	
		return URLConstant.URL_PRODUCT_LIST;
	}
	
	/**
	 * 
	 * 函数功能说明  :初始化页面的能源类型和单位
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/setSelectData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> setSelectData(HttpServletRequest request) {
		Map<String, Object> quaryMap = new HashMap<String,Object> (); 
		List<Map<Long, String>> powerDatas = productService.getPowerData();
		List<Map<Long, String>> unitDatas = productService.getUnitData();
		quaryMap.put("powerDatas", powerDatas);
		quaryMap.put("unitDatas", unitDatas);
		return quaryMap;
	}
	
	
	
	/**
	 * 
	 * 函数功能说明  :得到列表
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/queryProductList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryProductList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<ProductsBean> dataList = productService.getProductListData();
		result.put("list",dataList);
		return result;
	}
	
	@RequestMapping(value = "/checkProductLink", method = RequestMethod.POST)
	public @ResponseBody boolean checkProductLink(HttpServletRequest request,Long productId) {
		boolean flag = false;
		flag = productService.getProductLink(productId);
		return flag;
	}
	
	/**
	 * 
	 * 函数功能说明  :根据费率Id删除产品
	 * @param scheduleId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/deleteProductData", method = RequestMethod.POST)
	public @ResponseBody boolean deleteProductData(HttpServletRequest request,Long productId) {
		ProductsBean product = productService.getProductData(productId);
		boolean flag = false;
		File file = new File(request.getSession().getServletContext().getRealPath("/upload")
				+"/"+product.getPicName());
		flag = productService.deleteProductData(productId,product.getPicId());
		int result = CommonOperaDefine.OPRATOR_FAIL;
		StringBuffer desc = new StringBuffer();
		desc.append("delete a product, ")
	    .append(product.getProductName().toString());
		// 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()&&flag) {  
	        if(file.delete()){ 
	        	result =  CommonOperaDefine.OPRATOR_SUCCESS;
	        }
	    }  
	    super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_PRODUCT_ID,CommonOperaDefine.MODULE_NAME_PRODUCT,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return flag;
	}
	
	/**
	 * 
	 * 函数功能说明  :根据ID得到产品相关信息
	 * @param request
	 * @param productId
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getProductData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getProductData(HttpServletRequest request,long productId) {
		Map<String, Object> result = new HashMap<String, Object>();
		ProductsBean product = productService.getProductData(productId);
		List<Map<String, Object>> dataList = productService.getPowerData(productId);
		result.put("list",dataList);
		result.put("product", product);
		return result;
	}

	/**
	 * 
	 * 函数功能说明  :上传图片
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException      
	 * @return  Map<String,String>     
	 * @throws
	 */
	@RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
	public @ResponseBody Map<String,String> addImage(HttpServletRequest request) throws IllegalStateException, IOException {
		String msg = "获取文件失败";
		Map<String,String> result = new HashMap<String,String> (); result.put("msg", msg);result.put("fileName", "");
	    //转型为MultipartHttpRequest(重点的所在)   
		MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;   
	    //  获得图片（根据前台的name名称得到上传的文件）   
		MultipartFile imgFile = multipartRequest.getFile("filePath"); if (imgFile == null) return result;
		String fileName = imgFile.getOriginalFilename(); if (fileName == null || fileName.trim().length()==0) return result;
		if(imgFile.getSize()>10*1024*1024) {
			msg = "图片太大";
			result.put("fileName", "");
		} else if(!(imgFile.getOriginalFilename() ==null || imgFile.getOriginalFilename().length()==0)) {
			String saveName =  System.currentTimeMillis()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
			File file = null;
            if(!DataUtil.checkFilePath(saveName)){ file = new File(request.getSession().getServletContext().getRealPath("/upload") + "/" + saveName); }
			// 文件路径是否存在
			if (file != null && null != file.getParentFile() && !file.exists()) {
				if(!file.getParentFile().mkdirs()){
					msg = "文件创建失败";
				}
			}
			imgFile.transferTo(file); //保存文件
			msg = "上传成功";
			result.put("fileName", saveName);
		} 
		result.put("msg", msg);
		return result;
	} 

	
	/**
	 * 
	 * 函数功能说明  :检查是否有相同的产品名称 
	 * @param scheduleName
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/checkProductName", method = RequestMethod.POST)
	public @ResponseBody boolean checkProductName(String productName) {
		return productService.checkProductName(productName);
	}
	
	/**
	 * 
	 * 函数功能说明  :保存信息
	 * @param request
	 * @return
	 * @throws IOException      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/saveProductInfo")
	public @ResponseBody boolean saveProductInfo (HttpServletRequest request) {
		String s=request.getParameter("powerIds");
		ProductsBean product = new ProductsBean();
		product.setPicName(request.getParameter("picName"));
		product.setPicUrl(request.getParameter("picName"));
		String unit=request.getParameter("unitConsumer");
		if(null!=unit&&unit.length()<=20&&unit.length()>0){
		product.setUnitConsumer(Double.valueOf(unit));}
		product.setMeasureUnitId(Long.valueOf(request.getParameter("measureUnitId")));
		product.setProductName(request.getParameter("productName"));
		String optType = request.getParameter("optType");
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		int logType=0;
		if("0".equals(optType)) { //新增
			//默认的费率序列
			product.setProductId(SequenceUtils.getDBSequence());
			product.setPicId(SequenceUtils.getDBSequence());
			desc.append("add a product, ")
		    .append(product.getProductName().toString());
			isSuccess = productService.addProductData(product,s);
			logType=CommonOperaDefine.LOG_TYPE_INSERT;
		} else {
			product.setProductId(Long.valueOf(request.getParameter("productId")));
			product.setPicId(Long.valueOf(request.getParameter("picId")));
			desc.append("update a product, ")
		    .append(product.getProductName().toString());
			isSuccess = productService.updateProductData(product,s);
			logType=CommonOperaDefine.LOG_TYPE_UPDATE;
		}
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(logType,CommonOperaDefine.MODULE_NAME_PRODUCT_ID,CommonOperaDefine.MODULE_NAME_PRODUCT,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;		
	}
}
