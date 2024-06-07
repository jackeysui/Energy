package com.linyang.energy.controller.energydataquery;

import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.EventBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.EventQueryService;
import com.linyang.energy.service.MessageService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.WebConstant;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * @Description 事件查询controller
 * @author Leegern
 * @date Jan 21, 2014 10:29:29 AM
 */
@Controller
@RequestMapping("/eventquery")
public class EventQueryController extends BaseController {
	@Autowired
	private EventQueryService eventQueryService;
	@Autowired
	private UserAnalysisService userAnalysisService;
    @Autowired
	private MessageService messageService;

	/**
	 * 进入事件查询主页
	 * @return
	 */
	@RequestMapping(value = "/gotoEventQueryMain")
	public ModelAndView gotoEventQueryMain() {
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("beginTime", com.linyang.energy.utils.DateUtil.convertDateToStr(com.linyang.energy.utils.DateUtil
				.getYearFirstDate(WebConstant.getChartBaseDate()), com.linyang.energy.utils.DateUtil.SHORT_PATTERN)); // DateUtil.getAWeekAgoDateStr(DateUtil.DEFAULT_SHORT_PATTERN));
		param.put("endTime",  currentTime);//DateUtil.getCurrentDateStr(DateUtil.DEFAULT_SHORT_PATTERN));
        param.put("eventTypeList", this.messageService.getAllEventTypeList());
		return new ModelAndView(URLConstant.URL_EVENT_QUERY, "params", param);
	}
	
	/**
	 * 查询事件列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryEventList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryEventList(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
		// 分页信息
		Page page = super.getCurrentPage(json.getString("pageNo"), json.getString("pageSize"));
		List<EventBean> result = null;
		try {
			if (json.has("beginTime")) {
				param.put("beginTime", DateUtil.convertStrToDate(json.getString("beginTime") + " 00:00:00"));
			}
			if (json.has("endTime")) {
				param.put("endTime", DateUtil.convertStrToDate(json.getString("endTime") + " 23:59:59"));
			}
			param.put("page", page);
			
			long ledgerId = -1L;
			UserBean user = getSessionUserInfo(request);		
			if(user.getLedgerId() == null){
				ledgerId = 0;
			}else{
				ledgerId = user.getLedgerId();
			}
            List<String> ledgerIdList = null;                        //ledgerIds转集合
            if(json.getString("ledgerIds") != null && json.getString("ledgerIds").length()>0){
                ledgerIdList = new ArrayList<String>();
                String[] ledgerIds = json.getString("ledgerIds").split(",");
                Collections.addAll(ledgerIdList, ledgerIds);
            }
			param.put("ledgerIds",ledgerIdList);
            List<String> meterIdList = null;                          //meterIds转集合
            if(json.getString("meterIds") != null && json.getString("meterIds").length()>0){
                meterIdList = new ArrayList<String>();
                String[] meterIds = json.getString("meterIds").split(",");
                Collections.addAll(meterIdList, meterIds);
            }
			param.put("meterIds", meterIdList);
			param.put("eventId",json.getString("eventId"));
			param.put("ledgerId", ledgerId);
			param.put("accountId", user.getAccountId());
            param.put("sortName",json.getString("sortName"));
            param.put("sortOrder",json.getString("sortOrder"));
			
			result = eventQueryService.queryEventPageList2(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryEventList()--无法查询事件列表");
		}
		resultMap.put("page", page);
		if(result != null && result.size() > 0){
			resultMap.put("dataInfo", result);
		}
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),OperItemConstant.OPER_ITEM_17,31L, 1);
		return resultMap; 
	}
	
	
	//TODO：
	/**
	 * 事件报警
	 * @return
	 */
	@RequestMapping(value = "/gotoEventWarning")
	public @ResponseBody Map<String, Object> gotoEventWarning(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
	
			Map<String, Object> param = new HashMap<String, Object>();
			Page page = super.getCurrentPage(1+"", 30+"");
			param.put("page", page);
			long ledgerId = -1L;
			UserBean user = getSessionUserInfo(request);
			if (user.getAccountId() != 1) {
				if (user.getLedgerId() == null)
					ledgerId = 0;
				else
					ledgerId = user.getLedgerId();
			}
			param.put("ledgerId", ledgerId);
			param.put("accountId", user.getAccountId());
			
			//告警查询时间
            Date endTime = com.linyang.energy.utils.DateUtil.getPreNDate(new Date(), WebConstant.eventCalHour);
            endTime = com.linyang.energy.utils.DateUtil.clearMinute(endTime);
            Date beginTime = com.linyang.energy.utils.DateUtil.getPreNDate(endTime, WebConstant.eventIntervalHour);
			param.put("beginTime", beginTime);
			List<EventBean> eventList = this.eventQueryService.queryEventWarningList(param);
			result.put("list", eventList);
			result.put("warningInterval", WebConstant.eventIntervalHour);//告警间隔时间
			result.put("warningNum", WebConstant.getWarningNum());//告警条数

		return result;
	}
}
