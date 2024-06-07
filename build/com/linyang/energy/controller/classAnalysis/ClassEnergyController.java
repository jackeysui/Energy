package com.linyang.energy.controller.classAnalysis;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.minlog.Log;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.ClassEnergyService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.utils.DateUtil;

/**
 * 班组能耗分析控制器
 * @author guosen
 * @date 2016-8-23
 *
 */
@Controller
@RequestMapping("/classEnergy")
public class ClassEnergyController extends BaseController{
	
	@Autowired
	private ClassEnergyService classEnergyService;
	@Autowired
	private LedgerManagerService ledgerManagerService;
    @Autowired
	private UserAnalysisService userAnalysisService;

	/**
	 * 跳转到班组能耗分析页面
	 * @return
	 */
	@RequestMapping("/gotoClassEnergyPage")
	public ModelAndView gotoClassEnergyPage(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
		result.put("loginLedger", bean);
		return new ModelAndView("/energy/classmanager/class_energy_analysis", result);
	}
	
	
	/**
	 * 查询数据
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> search(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		
		Map<String, Object> param = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		this.populateParam(json, param);
		result = this.classEnergyService.search(param);
        //记录用户足迹
        if(json.has("analysisType")){
            Long operItem = OperItemConstant.OPER_ITEM_64;
            if(json.getInt("analysisType") == 0){
                operItem = OperItemConstant.OPER_ITEM_64;
            }

            else if(json.getInt("analysisType") == 1){
                operItem = OperItemConstant.OPER_ITEM_80;
            }
            else if(json.getInt("analysisType") == 2){
                operItem = OperItemConstant.OPER_ITEM_81;
            }
            else if(json.getInt("analysisType") == 3){
                operItem = OperItemConstant.OPER_ITEM_82;
            }
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), operItem, 128L, 1);
        }
		
		return result;
	}
	
	/**
	 * 根据班制ID得到车间/单位列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getWorkshopListByClassId", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getWorkshopListByClassId(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		
			long classId = getLongParam(request, "classId", 0);
			if(classId > 0){
				result = this.classEnergyService.getWorkshopListByClassId(classId);
			}

		return result;
	}

	/**
	 * 处理数据
	 * @param json
	 * @param param
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private void populateParam(JSONObject json, Map<String, Object> param) {
		// 分户Id
		if(json.has("ledgerId")){
			param.put("ledgerId", json.getLong("ledgerId"));
		}
		//分析类型
		if(json.has("analysisType")){
			param.put("analysisType", json.getInt("analysisType"));
		}
		//班制ID
		if(json.has("classId")){
			param.put("classId", json.getLong("classId"));
		}
		//车间ID
		if(json.has("workshopIds")){
			JSONArray ary = json.getJSONArray("workshopIds");
			List<Long> workshopIds = JSONArray.toList(ary,Long.class);
			param.put("workshopIds", workshopIds);
		}
		//能源类型
		if(json.has("energyType")){
			param.put("energyType", json.getInt("energyType"));
		}
		//时间类型
		if(json.has("dateType")){
			int dateType = json.getInt("dateType");
			param.put("dateType", dateType);
			//上月
			if(dateType == 1){
				//开始时间 ： 00:00:00
				param.put("beginTime",DateUtil.getPreMonthFristDay(new Date()));
				//结束时间 ： 23:59:59
				param.put("endTime", DateUtil.getPreMonthLastDay(new Date()));
			}
			//自定义类型有开始时间和结束时间
			if(dateType == 2){
				param.put("beginTime", DateUtil.convertStrToDate(json.getString("beginTime"), DateUtil.MOUDLE_PATTERN));
				param.put("endTime", DateUtil.convertStrToDate(json.getString("endTime"), DateUtil.MOUDLE_PATTERN));
			}
		}
		//产品ID
		if(json.has("productId")){
			param.put("productId", json.getLong("productId"));
		}
		
		//展示类型选择
//		if(json.has("etype")){
//			param.put("etype", json.getInt("etype"));
//		}
		
	}
	

}
