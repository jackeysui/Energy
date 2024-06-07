package com.linyang.energy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.PositionBean;
import com.linyang.energy.service.FrameService;
import com.linyang.energy.service.IndexChartService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.MapMapping;
import com.linyang.util.CommonMethod;


@Controller
@RequestMapping("/indexChart")
public class IndexChartController extends BaseController{
	
	/**
	 * 默认图片的名称
	 */
	private static final String defaultImg = "default.jpg";
	private static final int defaultImgPicId = 0;
	@Autowired
	private IndexChartService indexChartService;
	
	@Autowired
	private FrameService frameService;
	
	
	
	@RequestMapping("/showCompanyPage")
	public String showCompanyTempPage(long ledgerId,final RedirectAttributes attr){
		 attr.addFlashAttribute("ledgerId",ledgerId);
	     return "redirect:/indexChart/showEnterprisePage.htm";
	}
	
	/**
	 * 分户信息页面  (没用)
	 * @param
	 * @return
	 */
	@RequestMapping(value="/showEnterprisePage", method=RequestMethod.GET)
	public ModelAndView showCompanyPage(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			map.putAll(RequestContextUtils.getInputFlashMap(request));
			Map<String,Object> legerPicMap = indexChartService.getLegerPic(Long.valueOf(map.get("ledgerId").toString()));
			map.put("ledgerPic",legerPicMap.get("PIC_URL")==null?defaultImg:legerPicMap.get("PIC_URL"));
			map.put("picStr",legerPicMap.get("PIC_ID")==null?defaultImgPicId:legerPicMap.get("PIC_ID"));
			return new ModelAndView("energy/index/company",map);
		}catch (NumberFormatException e) {
			map = frameService.getUserModules(super.getSessionUserInfo(request).getAccountId(), super.getSessionRoleType(request));
			map.put("mapData",MapMapping.getMapMapping());
			return new ModelAndView("energy/index/index",map);
		}
	}
	
	@RequestMapping("/showMetersPage")
	public String showCompanyTempPage(long legerId,long plId,final RedirectAttributes attr){
		 attr.addFlashAttribute("ledgerId",legerId);
		 attr.addFlashAttribute("plId",plId);
	     return "redirect:/indexChart/showFloorsPage.htm";
	}
	
	/**
	 * 计量点信息页面   (没用)
	 * @param
	 * @return
	 */
	@RequestMapping(value="/showFloorsPage", method=RequestMethod.GET)
	public ModelAndView showMetersPage(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			map.putAll(RequestContextUtils.getInputFlashMap(request));
			List<Map<String, Object>> ledgerFloors = indexChartService.getLedgerFloors(Long.valueOf(map.get("ledgerId").toString()));
			map.put("ledgerFloors",ledgerFloors);
			String firstPic = defaultImg;
			//默认是0
			map.put("picStr",defaultImgPicId);
			if(CommonMethod.isNotEmpty(ledgerFloors)){
				Map<String, Object> picMap = ledgerFloors.get(0);
				firstPic = picMap.get("PIC_URL").toString();
				if(picMap.get("PIC_ID") != null)
					map.put("picStr",picMap.get("PIC_ID"));
			}
			map.put("firstPic",firstPic);
			return new ModelAndView("energy/index/leger_meter",map);
		}catch (NumberFormatException e) {
			map = frameService.getUserModules(super.getSessionUserInfo(request).getAccountId(), super.getSessionRoleType(request));
			map.put("mapData",MapMapping.getMapMapping());
			return new ModelAndView("energy/index/index",map);
		}
	}
	/**
	 * 得到分户或计量点位置图片信息
	 * @param legerId
	 * @return
	 */
	@RequestMapping(value="/showPicInfo", method=RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getPicInfo(long legerId){
		return indexChartService.getPicInfo(legerId);
	}
	
	/**
	 * 删除点信息，或者更新点信息
	 * @param positionBean
	 * @return
	 */
	@RequestMapping(value="/updatePointPosition", method=RequestMethod.POST)
	public @ResponseBody Map<String,Object> updatePointPosition(HttpServletRequest request,PositionBean positionBean){
		Map<String,Object> result = new HashMap<String, Object>();
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		isSuccess = indexChartService.updatePointPosition(positionBean);
		int resultOpt = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			resultOpt =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		if(positionBean.getPointX() != null && positionBean.getPointY() != null) {
			desc.append("add a point,id is ")
		    .append(positionBean.getId());
			super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_DEPT_ID,CommonOperaDefine.MODULE_NAME_DEPT,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,resultOpt,desc.toString()),request);
		} else {
			desc.append("delete a point,id is ")
		    .append(positionBean.getId());
			super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_DEPT_ID,CommonOperaDefine.MODULE_NAME_DEPT,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,resultOpt,desc.toString()),request);
		}
		result.put("isSuccess", isSuccess);
		if(positionBean.getPointX() != null && positionBean.getPointY() != null && isSuccess){
			result.putAll(indexChartService.getPositionCallBack(positionBean));
		}
		return result;
		
	}
	/**
	 * 点是否已经绑定过了
	 * @param positionBean
	 * @return
	 */
	@RequestMapping(value="/isBunded", method=RequestMethod.POST)
	public @ResponseBody boolean isBunded(PositionBean positionBean){
		return indexChartService.isBunded(positionBean);
	}
	
	/**
	 * 统计
	 * @param legerId
	 * @param topN
	 * @return
	 */
	@RequestMapping(value="/getStat", method=RequestMethod.POST)
	public  @ResponseBody Map<String,Object> getStat(long legerId,int topN){
		return indexChartService.getStat(legerId,topN);
	}
	
	/**
	 * 得到楼层计量点信息
	 * @param legerId
	 * @param strucId
	 * @return
	 */
	@RequestMapping(value="/getFloolMeters", method=RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getFloolMeters(long legerId,long strucId){
		return indexChartService.getFloolMeters(legerId, strucId);
	}

}
