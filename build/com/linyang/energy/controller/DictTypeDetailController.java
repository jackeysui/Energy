/**
 * 
 */
package com.linyang.energy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.energy.dto.CatergoryTreeBean;
import com.linyang.energy.model.DictTypeDetailBean;
import com.linyang.energy.service.DictTypeDetailService;
import com.linyang.energy.utils.SequenceUtils;

/** 
 * @类功能说明： 数据字典
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-11 上午11:17:41  
 * @版本：V1.0  */

@Controller
@RequestMapping("/dictTypeDetailController")
public class DictTypeDetailController extends BaseController{
	@Autowired
	private DictTypeDetailService dictTypeDetailService;
	/**
	 * 
	 * 函数功能说明 :跳转到数据字典页面
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoDictTypeDetail")
	public ModelAndView gotoDictTypeDetail() {
		return new ModelAndView("energy/system/dictTypeDetail");
	}
	/**
	 * 函数功能说明  :获取大类下面的直接子类
	 * @param cateId
	 * @return  ModelAndView
	 */
	@RequestMapping("/getDirectChildTree")
	public @ResponseBody  Map<String, Object> getDirectChildTree(long cateId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<DictTypeDetailBean> ss =  dictTypeDetailService.getDirectChildTree(cateId);
		map.put("dictTypeDetailBean",ss);
		return map;
	}
	/**
	 * 函数功能说明  :插入大类下面的直接子类
	 * @param cateId
	 * @return  boolean
	 */
	@RequestMapping("/addDetail")
	public @ResponseBody boolean addDetail(DictTypeDetailBean dictTypeDetailBean){
		dictTypeDetailBean.setDetailId(SequenceUtils.getDBSequence());
		return dictTypeDetailService.addDetail(dictTypeDetailBean);
	}
	/**
	 * 函数功能说明  :更新大类下面的直接子类
	 * @param cateId
	 * @return  boolean
	 */
	@RequestMapping("/updateDetail")
	public @ResponseBody boolean updateDetail(DictTypeDetailBean dictTypeDetailBean){
		return dictTypeDetailService.updateDetail(dictTypeDetailBean);
	}
	/**
	 * 函数功能说明  :删除子项
	 * @param cateId
	 * @return  boolean
	 */
	@RequestMapping("/deleteDetail")
	public @ResponseBody boolean deleteDetail(long detailId){
		return dictTypeDetailService.deleteDetail(detailId);
	}
	/**
	 * 函数功能说明  :校验名称
	 * @param cateId,perType
	 * @return  boolean
	 */
	@RequestMapping("/checkName")
	public @ResponseBody boolean checkName(String name,int operType){
		return dictTypeDetailService.checkName(name,operType);
	}
	/**
	 * 函数功能说明  :查询数据字典父类树
	 * @return  List<CatergoryTreeBean>
	 */
	@RequestMapping(value = "/getParentTree", method = RequestMethod.POST)
	public @ResponseBody List<CatergoryTreeBean> getParentTree(){
	
		return dictTypeDetailService.getParentTree();
	}
	/**
	 * 函数功能说明  :查询数据字典子类类树
	 * @return  List<CatergoryTreeBean>
	 */
	@RequestMapping(value = "/getChildTree", method = RequestMethod.POST)
	public @ResponseBody List<CatergoryTreeBean> getChildTree(long parentId){
	
		return dictTypeDetailService.getChildTree(parentId);
	}


}
