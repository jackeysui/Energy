package com.linyang.energy.controller.timeManager;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.minlog.Log;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.dto.HolidayBean;
import com.linyang.energy.model.LedgerRestDayBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.TimeService;
import com.linyang.energy.service.UserService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OfficialHolidayUtils;

/**
 * @Description 休息时间Controller
 * @author xs
 * @since 2016年3月24日08:32:15
 */
@Controller
@RequestMapping("/restTime")
public class RestTimeController extends BaseController {
	
	@Autowired
	private UserService userService;
    @Autowired
	private TimeService timeService;
    @Autowired
    private UserAnalysisService userAnalysisService;
	
	@RequestMapping(value = "/index")
	public ModelAndView goToRestTimePage(HttpServletRequest request) {
		return new ModelAndView(URLConstant.REST_TIME);
	}

	@RequestMapping(value = "/getOfficialHolidays" ,method=RequestMethod.POST)
	public @ResponseBody List<HolidayBean> getOfficialHolidays(HttpServletRequest request) {
		return OfficialHolidayUtils.readOfficialHolidayList();
	}
	
	@RequestMapping(value = "/saveRestTimeSetting" ,method=RequestMethod.POST)
	public @ResponseBody boolean saveRestTimeSetting(HttpServletRequest request) {
		Map<String, Object> settingInfo = new HashMap<String, Object>();
		try {
			settingInfo = jacksonUtils.readJSON2Map(request.getParameter("restTimeSettingInfo"));
		} catch (IOException e) {
			Log.error("RestTimeController -- saveRestTimeSetting get paramater error");
		}
        String leadgerIdStr = request.getParameter("ledgerId");
        long ledgerId = 0l;
        if(leadgerIdStr == null || leadgerIdStr.length()==0){
            Long accountId = super.getSessionUserInfo(request).getAccountId();
            List<UserBean> uList = userService.getCompanyLedgerDataByAccountId(accountId);
            if(uList != null && uList.size() >0){
                ledgerId =  uList.get(0).getLedgerId();
            }else{
                return false;
            }
        }else{
            ledgerId = Long.parseLong(leadgerIdStr);
        }
		
		boolean isSuccess = timeService.saveRestTimeSetting(ledgerId,settingInfo);
                
		StringBuffer desc = new StringBuffer();
		desc.append("save rest time setting--ledgerId:" + ledgerId)
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_ID_REST_TIME,CommonOperaDefine.MODULE_NAME_REST_TIME,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_150, 120l, 1);
		return isSuccess;
	}
    
    @RequestMapping(value = "/getRestTimeSetting" ,method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> getRestTimeSetting(HttpServletRequest request) {
        String leadgerIdStr = request.getParameter("ledgerId");
        long ledgerId = 0l;
        if(leadgerIdStr == null || leadgerIdStr.length()==0){
            Long accountId = super.getSessionUserInfo(request).getAccountId();
            List<UserBean> uList = userService.getCompanyLedgerDataByAccountId(accountId);
            if(uList != null && uList.size() >0){
                ledgerId =  uList.get(0).getLedgerId();
            }else{
                return new HashMap<String, Object>();
            }
        }else{
            ledgerId = Long.parseLong(leadgerIdStr);
        }
		return timeService.getRestTimeSetting(ledgerId);
	}
    
    /**
	 * 得到企业分户数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getCompanyLedgerDataByLedgerId")
	public @ResponseBody UserBean getCompanyLedgerDataByLedgerId(HttpServletRequest request) {
		String leadgerIdStr = request.getParameter("ledgerId");
        if(leadgerIdStr == null || leadgerIdStr.length()==0){
            Long accountId = super.getSessionUserInfo(request).getAccountId();
            List<UserBean> uList = userService.getCompanyLedgerDataByAccountId(accountId);
            if(uList != null && uList.size() >0){
                return uList.get(0);
            }else{
                return null;
            }
        }else{
            UserBean uBean = userService.getCompanyLedgerDataByLedgerId(Long.parseLong(leadgerIdStr));
            return uBean;
        }
	}


    /*************************************************************************************
     * 班制休息日配置弹出页
     * @param request
     * @return
     */
    @RequestMapping(value = "/getClassRestPage")
    public ModelAndView getClassRestPage(HttpServletRequest request) {
        return new ModelAndView(URLConstant.URL_CLASS_REST);
    }

    @RequestMapping(value = "/getClassRestSetting" ,method=RequestMethod.POST)
    public @ResponseBody Map<String, Object> getClassRestSetting(HttpServletRequest request) {
        Long classId = super.getLongParam(request, "classId", -1L);
        return timeService.getClassRestSetting(classId);
    }

    @RequestMapping(value = "/saveClassRestTimeSetting" ,method=RequestMethod.POST)
    public @ResponseBody boolean saveClassRestTimeSetting(HttpServletRequest request) {
        Map<String, Object> settingInfo = new HashMap<String, Object>();
        try {
            settingInfo = jacksonUtils.readJSON2Map(request.getParameter("restTimeSettingInfo"));
        } catch (IOException e) {
        	Log.error(this.getClass() + ".saveClassRestTimeSetting()--无法保存班组休息时间设置");
        }
        Long classId = super.getLongParam(request, "classId", -1L);
        boolean isSuccess = timeService.saveClassRestSetting(classId,settingInfo);

        StringBuffer desc = new StringBuffer();
        desc.append("save rest time setting--classId:" + classId)
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        int result = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess){
            result =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_ID_EVENT_EXCEED_SET_MODEL,CommonOperaDefine.MODULE_NAME_EVENT_EXCEED_SET_MODEL,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
        return isSuccess;
    }

}
