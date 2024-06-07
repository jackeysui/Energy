package com.linyang.energy.controller.bigDataPredict;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.DateUtil;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.LoadPredictSetBean;
import com.linyang.energy.service.BigDataService;
import com.linyang.energy.service.LedgerManagerService;


/**
 * 大数据预测
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/bigData")
public class BigDataController extends BaseController{
	
	@Autowired
	private BigDataService bigDataService;
	@Autowired
	private LedgerManagerService ledgerManagerService;
    @Autowired
	private UserAnalysisService userAnalysisService;

	/**
	 * 跳转到负荷预测页面
	 * @return
	 */
	@RequestMapping("/gotoLoadPredict")
	public ModelAndView gotoLoadPredict(HttpServletRequest request){
		Date today = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
		Date tomorrow = DateUtil.getSomeDateInYear(today,1);
		try {
			request.setAttribute("tomorrow", DateUtil.convertDateToStr(tomorrow, DateUtil.DEFAULT_SHORT_PATTERN));
		} catch (ParseException e) {
			Log.error(this.getClass() + ".gotoLoadPredict()--负荷预测时间转换失败");
		}
		return  new ModelAndView(URLConstant.LOAD_PREDICT) ;
	}
	
	/**
	 * 负荷预测
	 * @param request
	 * @return
	 */
	@RequestMapping("/loadPredict")
	public @ResponseBody Map<String, Object> loadPredict(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		Long ledgerId = getLongParam(request, "ledgerId", 0);
		String baseDateStr = getStrParam(request, "baseTime", "");
		param.put("ledgerId", ledgerId);
		param.put("baseDateStr", baseDateStr);
		Map<String, Object> result = new HashMap<String, Object>();
		
		result = bigDataService.loadPredict(param);
			
		return result;
	}
	
	/**
	 * 跳转到数据预测页面
	 * @return
	 */
	@RequestMapping("/gotoDataPredict")
	public ModelAndView gotoDataPredict(HttpServletRequest request){
		Date today = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
		Date tomorrow = DateUtil.getSomeDateInYear(today,1);
		try {
			LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
			request.setAttribute("loginLedger", bean);
			request.setAttribute("tomorrow", DateUtil.convertDateToStr(tomorrow, DateUtil.DEFAULT_SHORT_PATTERN));
			request.setAttribute("minDate", "%y-%M-{%d+1}");
            request.setAttribute("predictCheck", 0);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".gotoDataPredict()--数据预测时间转换失败");
		}
		return  new ModelAndView(URLConstant.DATA_PREDICT) ;
	}
	
	/**
	 * 跳转到数据预测检查页面
	 * @return
	 */
	@RequestMapping("/gotoDataPredictCheck")
	public ModelAndView gotoDataPredictCheck(HttpServletRequest request){
		Date today = DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
		Date tomorrow = DateUtil.getSomeDateInYear(today,1);
		try {
			LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
			request.setAttribute("loginLedger", bean);
			request.setAttribute("tomorrow", DateUtil.convertDateToStr(tomorrow, DateUtil.DEFAULT_SHORT_PATTERN));
			request.setAttribute("minDate", "");
            request.setAttribute("predictCheck", 1);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".gotoDataPredict()--数据预测检查时间转换失败");
		}
		return  new ModelAndView(URLConstant.DATA_PREDICT) ;
	}
	
	/**
	 * 数据预测，支持电量预测和负荷（有功功率）预测
	 * @param request
	 * @return
	 */
	@RequestMapping("/dataPredict")
	public @ResponseBody Map<String, Object> dataPredict(HttpServletRequest request){
		Map<String, Object> param = new HashMap<String, Object>();
		Long ledgerId = getLongParam(request, "ledgerId", 0);
		String baseDateStr = getStrParam(request, "baseTime", "");
		//数据类型 1-电量，2-负荷
		Integer dataType = getIntParams(request, "dataType", 0);
		param.put("ledgerId", ledgerId);
		param.put("baseDateStr", baseDateStr);
		param.put("dataType", dataType);
		Map<String, Object> result = new HashMap<String, Object>();
		
			result = bigDataService.dataPredict(param);

            //记录用户足迹
            Integer predictCheck = getIntParams(request, "predictCheck", 0);
            if(predictCheck == 0){
                Long operItem = OperItemConstant.OPER_ITEM_55;
                if(dataType == 1){
                    operItem = OperItemConstant.OPER_ITEM_55;
                }
                else if(dataType == 2){
                    operItem = OperItemConstant.OPER_ITEM_78;
                }
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), operItem, 220L, 1);
            }
            else if(predictCheck == 1){
                Long operItem = OperItemConstant.OPER_ITEM_56;
                if(dataType == 1){
                    operItem = OperItemConstant.OPER_ITEM_56;
                }
                else if(dataType == 2){
                    operItem = OperItemConstant.OPER_ITEM_79;
                }
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), operItem, 221L, 1);
            }

		return result;
	}
	
	/**
	 * 跳转到设置页面
	 * @return
	 */
	@RequestMapping("/gotoSetPage")
	public ModelAndView gotoSetPage(HttpServletRequest request){
		return new ModelAndView(URLConstant.SET_PAGE) ;
	}
	
	/**
	 * 得到预测的设置数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/getSetInfo")
	public @ResponseBody LoadPredictSetBean getSetInfo(HttpServletRequest request){
		//try {
			LoadPredictSetBean setBean = bigDataService.getSetInfo();
			return setBean;
		//} catch (Exception e) {
		//	Log.error(this.getClass() + ".getSetInfo()--无法得到预测的设置数据");
		//}
		//return null;
	}
	
	/**
	 * 保存设置信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveSetInfo")
	public @ResponseBody boolean saveSetInfo(HttpServletRequest request){
		LoadPredictSetBean bean = new LoadPredictSetBean();
		bean.setHistoryDay(getStrParam(request, "historyDay", ""));
		bean.setTempHighLine(getStrParam(request, "tempHighLine", ""));
		bean.setTempLowLine(getStrParam(request, "tempLowLine", ""));
		bean.setProportion(getStrParam(request, "proportion", ""));
		bean.setPointNum(getStrParam(request, "pointNum", ""));
		return bigDataService.saveSetInfo(bean);
	}
}
