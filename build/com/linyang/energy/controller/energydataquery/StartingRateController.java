package com.linyang.energy.controller.energydataquery;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.StringUtil;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.StartrateBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.StartingRateService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;

/**
 * 开机率分析
 * 
 * @author gaofeng
 * 
 */
@Controller
@RequestMapping("/startingrate")
public class StartingRateController extends BaseController {

	@Autowired
	private StartingRateService startingRateService;

	@RequestMapping("/showStartingRatePage")
	public ModelAndView showVoltageCurrentPage(HttpServletRequest request) {
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		request.setAttribute("beginTime", DateUtils.getBeforeAfterDate(currentTime, -30));
		request.setAttribute("endTime", DateUtils.getBeforeAfterDate(currentTime, -1));
		return new ModelAndView("/energy/dataquery/starting_rate");
	}

	/**
	 * 查询开机率数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryStartRateData")
	public @ResponseBody List<StartrateBean> queryStartRateData(HttpServletRequest request) {
		List<StartrateBean> result = new ArrayList<StartrateBean>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> param = this.populateParam(json);
			result = startingRateService.queryStartRateData(param);

		} catch (ParseException e) {
			Log.info("queryStartRateData error ParseException");
		}
		return result;
	}

	/**
	 * 封装查询参数
	 * 
	 * @param json
	 * @return
	 * @throws ParseException
	 */
	private Map<String, Object> populateParam(JSONObject json) throws ParseException {
		Map<String, Object> params = new HashMap<String, Object>();
		// 起止时间
		if (!StringUtil.isEmpty(json.get("beginTime")) && !StringUtil.isEmpty(json.get("endTime"))) {
			params.put("beginTime", DateUtil.convertStrToDate(json.getString("beginTime"),DateUtil.DEFAULT_PATTERN));
			params.put("endTime", DateUtil.convertStrToDate(json.getString("endTime"),DateUtil.DEFAULT_PATTERN));
			params.put("endTime2", DateUtil.convertStrToDate(json.getString("endTime2"),DateUtil.DEFAULT_PATTERN));
		}
		// 电表Id
		if (json.has("meterIds")) {
			params.put("meterIds", json.getString("meterIds"));
		}
		
		return params;
	}

	/**
	 * 导出到Excel
	 */
	@RequestMapping(value = "/optExcel")
	public @ResponseBody void optExcel(HttpServletRequest request, HttpServletResponse response) {
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> param = populateParam(json);
			
			List<StartrateBean> result = startingRateService.queryStartRateData(param);

			String title = "开机率";
			byte[] bs = title.getBytes();
			title = new String(bs, "ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + title + ".xls"); // 指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			startingRateService.optExcel(response.getOutputStream(), result, param);

		} catch (ParseException e) {
			Log.info("optExcel error IOException");

		}
		catch (UnsupportedEncodingException e) {
			Log.info("optExcel error UnsupportedEncodingException");
		}
		catch (IOException e) {
			Log.info("optExcel error IOException");
		}
	}
	
	private List<LedgerTreeBean> addIconPath(List<LedgerTreeBean> list,HttpServletRequest request){
		if(CommonMethod.isCollectionNotEmpty(list)){
			for (LedgerTreeBean ledgerTreeBean : list) {
				if(CommonMethod.isNotEmpty(ledgerTreeBean.getIcon()))
					ledgerTreeBean.setIcon(request.getContextPath()+ledgerTreeBean.getIcon());
			}
		}
		return list;
	}
	
	/**
	 * 得到一个用户的父类分户树
	 * @return
	 */
	@RequestMapping(value = "/getTree", method = RequestMethod.POST)
	public @ResponseBody List<LedgerTreeBean> getUserTree(HttpServletRequest request) {
		UserBean userBean = super.getSessionUserInfo(request);
		if (userBean != null) {
			if (userBean.getLedgerId() != null && userBean.getLedgerId() != 0) {
				Long ledgerId = userBean.getLedgerId();
				return addIconPath(startingRateService.getTree(ledgerId), request);
			}
		}
		return new ArrayList<LedgerTreeBean>();
	}
	
}
