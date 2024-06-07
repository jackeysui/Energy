package com.linyang.energy.controller;

import java.math.BigDecimal;import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.*;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.energy.model.EasyAdviceScoreBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WeatherUtils;
import com.linyang.util.DoubleUtils;

/**
 * 微信接口
 * 
 * @author jijialu
 * 
 */
@Controller
@RequestMapping("/easynxInterface")
public class WechatController extends BaseController {

	@Autowired
	private PhoneService phoneService;
	@Autowired
	private DataQueryService dataQueryService;
	@Autowired
	private BigDataService bigDataService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private WechatAssessment wechatAssessment;
    @Autowired
    private UserAnalysisService userAnalysisService;
	
	/**
	 * 微信首页
	 * @param request
	 * @return
	 */
	@RequestMapping("/easynxIndex")
	public @ResponseBody
	ModelAndView easynxIndex(HttpServletRequest request) {
		// 身份认证
		long accountId = getLongParam(request, "accountId", -1L);
		int accountType = getIntParams(request, "accountType", 2);
		UserBean userBean1 = phoneService.getUserByAccountId(accountId);
		long ledgerId = 0;	
		String ledgerName = userBean1.getLedgerName();
		request.setAttribute("ledgerName", ledgerName);
		request.setAttribute("accountId", accountId);

        //记录微信登陆
        int loginFlag = getIntParams(request, "loginFlag", 0);
        if(loginFlag == 1){
            this.userAnalysisService.addAccountLogin(accountId, new Date(), 4, "微信");
        }
		
		// 普通用户用电数据返回集合
		Map<String, Object> resultMap = null;
		if (userBean1 == null || userBean1.getLedgerId() == null) {
			return null;
		}else{ ledgerId=userBean1.getLedgerId(); }
		// 分平台用户1,普通用户2
		if (accountType == 2) {
			//天气
			String weather = "";
			//企业所在地
			String ledgerRegion = phoneService.getRegionById(ledgerId);
			request.setAttribute("region", ledgerRegion);
			if (null != ledgerRegion) {			
				if(ledgerRegion.contains("市")){
					ledgerRegion = ledgerRegion.replace("市", "");
				}else if(ledgerRegion.contains("县")){
					ledgerRegion = ledgerRegion.replace("县", "");
				}else if(ledgerRegion.contains("区")) {
					ledgerRegion = ledgerRegion.replace("区", "");
				}
				List<String> weatherbyCityName = WeatherUtils.getInstance().getWeatherbyCityName(ledgerRegion);
				if (weatherbyCityName.size()>0) {
					String[] weatherInfo = weatherbyCityName.get(7).split(" ");
					weather = weatherInfo[1];
				}
			}			
			request.setAttribute("weather", weather);
			
			resultMap = new HashMap<String, Object>();			
			resultMap = phoneService.getUserLastScores(accountId);
			if (resultMap != null) {
				//总分
				String score = resultMap.get("TOTAL_SCORE") == null ? "0"
						: resultMap.get("TOTAL_SCORE").toString();
				//击败用户的提示
				String tips = phoneService.getScoreTips(accountId, Integer
						.valueOf(score));
				request.setAttribute("totalScore", score);
				request.setAttribute("sectionOne", resultMap.get("SECTION_ONE"));
				request.setAttribute("sectionTwo", resultMap.get("SECTION_TWO"));
				request.setAttribute("sectionThr", resultMap.get("SECTION_THR"));
				request.setAttribute("sectionFor", resultMap.get("SECTION_FOR"));
				request.setAttribute("tips", tips);
				//底部tab显示判断
				request.setAttribute("showTab", getStrParam(request, "from", null));
				
				request.setAttribute("title", "首页分享");
				request.setAttribute("desc", "哇哦，"+ledgerName+tips);

			}
				
			return new ModelAndView("energyApp/WeChat/easynx_index");
		} else {
			String baseDate = DateUtil.getCurrentDateStr("yyyy-MM-dd");
			Date beginDate = DateUtil.convertStrToDate(baseDate + " 00:00:00");
			Date endDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");

			ledgerId = userBean1.getLedgerId();

			// 今日电量
			Double todayQ = this.dataQueryService.getCurrentTotalQ(ledgerId,beginDate, endDate);

			// 实时功率因数
			Double todayPF = phoneService.getCurrentPF(ledgerId);
			request.setAttribute("todayPF", todayPF);

			// 实时负荷
			Double newAP = phoneService.getRecentAP(ledgerId);
			request.setAttribute("newAP", newAP);

			// 报警次数
			Integer todayEvent = phoneService.getEventNumById(ledgerId,
					beginDate, endDate);
			request.setAttribute("todayEvent", todayEvent);

			// 本月电量
			Date monBaseDate = DateUtil.getCurrMonthFirstDay(beginDate);
			Date monBeginDate = DateUtil.convertStrToDate(DateUtil.convertDateToStr(monBaseDate, "yyyy-MM-dd")
					+ " 00:00:00");
			Double monthQ = this.dataQueryService.getCurrentTotalQ(ledgerId,monBeginDate, endDate);

			// 预测电量
			Double dayPredictQ = bigDataService.getDayPredictData(ledgerId);
			Double monthPredictQ = bigDataService.getMonthPredictData(ledgerId);

			//实际电量占预测电量的占比
			double dayPredictPer = 0;
			double monthPredictPer = 0;			
			if (dayPredictQ != 0 && todayQ != null && monthPredictQ != 0
					&& monthQ != null) {
				dayPredictPer = DoubleUtils.getDoubleValue(
						new BigDecimal(todayQ).multiply(new BigDecimal(100)).divide(new BigDecimal(dayPredictQ), 2, BigDecimal.ROUND_DOWN).doubleValue(), 0);
				monthPredictPer = DoubleUtils.getDoubleValue(new BigDecimal(monthQ).multiply(new BigDecimal(100))
						.divide(new BigDecimal(monthPredictQ), 2, BigDecimal.ROUND_DOWN).doubleValue(), 0);
			}
			request.setAttribute("dayPredictPer", dayPredictPer);
			request.setAttribute("monthPredictPer", monthPredictPer);
			
			//微信推送信息
			double dayPredictDesc = 0;
			double monthPredictDesc = 0;
			String dayTips = "";
			String monthTips = "";
			if (dayPredictPer > 100d) {
				dayPredictDesc = DataUtil.doubleSubtract(dayPredictPer, 100d);
				dayTips = "已经超额使用了今日预测电量的"+String.valueOf(dayPredictDesc)+"%，";
			}else {
				dayPredictDesc = dayPredictPer;
				dayTips = "已经使用了今日预测电量的"+String.valueOf(dayPredictDesc)+"%，";
			}
			
			if (monthPredictPer > 100d) {
				monthPredictDesc =  DataUtil.doubleSubtract(monthPredictPer, 100d);
				monthTips = "已经超额使用了本月预测电量的"+String.valueOf(monthPredictDesc)+"%。";
			}else {
				monthPredictDesc = monthPredictPer;
				monthTips = "已经使用了本月预测电量的"+String.valueOf(monthPredictDesc)+"%。";
			}
			
			//底部tab显示判断
			request.setAttribute("showTab", getStrParam(request, "from", null));
			
			request.setAttribute("title", "实时分享");
			request.setAttribute("desc", "哇哦，"+ledgerName+dayTips+monthTips);
			
			//记录用户足迹
	        this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_61, null, 2);
						
			return new ModelAndView("energyApp/WeChat/easynx_current_data");
		}
	}

	/**
	 * 实时能耗概况
	 * @param request
	 * @return
	 */
	@RequestMapping("/currentReport")
	public ModelAndView currentReport(HttpServletRequest request) {

		long accountId = getLongParam(request, "accountId", -1L);
		UserBean userBean = phoneService.getUserByAccountId(accountId);	
		if (userBean == null || userBean.getLedgerId() == null) {
			return null;
		}
		
		String ledgerName = userBean.getLedgerName();
		long ledgerId = userBean.getLedgerId();
		request.setAttribute("ledgerName", userBean.getLedgerName());
		request.setAttribute("accountId", accountId);

		String baseDate = DateUtil.getCurrentDateStr("yyyy-MM-dd");
		Date beginDate = DateUtil.convertStrToDate(baseDate + " 00:00:00");
		Date endDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");

		// 今日电量
		Double todayQ = this.dataQueryService.getCurrentTotalQ(ledgerId, beginDate, endDate);

		// 实时功率因数
		Double todayPF = phoneService.getCurrentPF(ledgerId);
		request.setAttribute("todayPF", todayPF);

		// 实时负荷
		Double newAP = phoneService.getRecentAP(ledgerId);
		request.setAttribute("newAP", newAP);

		// 报警次数
		Integer todayEvent = phoneService.getEventNumById(ledgerId, beginDate,
				endDate);
		request.setAttribute("todayEvent", todayEvent);
		
		// 本月电量
		Date monBaseDate = DateUtil.getCurrMonthFirstDay(beginDate);
		Date monBeginDate = DateUtil.convertStrToDate(DateUtil.convertDateToStr(monBaseDate, "yyyy-MM-dd")
				+ " 00:00:00");
		Double monthQ = this.dataQueryService.getCurrentTotalQ(ledgerId, monBeginDate, endDate);
		
		// 预测电量
		Double dayPredictQ = bigDataService.getDayPredictData(ledgerId);
		Double monthPredictQ = bigDataService.getMonthPredictData(ledgerId);
		
		//实际电量占预测电量的占比
		double dayPredictPer = 0;
		double monthPredictPer = 0;
		if (dayPredictQ != null && monthPredictQ != null && todayQ != null
				&& monthQ != null && dayPredictQ != 0d && monthPredictQ != 0d) {
			dayPredictPer = DoubleUtils.getDoubleValue(new BigDecimal(todayQ).multiply(new BigDecimal(100)).divide(new BigDecimal(dayPredictQ), 2, BigDecimal.ROUND_HALF_UP).doubleValue(), 0);
			monthPredictPer = DoubleUtils.getDoubleValue(new BigDecimal(monthQ).multiply(new BigDecimal(100))
					.divide(new BigDecimal(monthPredictQ), 2, BigDecimal.ROUND_HALF_UP).doubleValue(), 0);
		}		
		
		request.setAttribute("dayPredictPer", Math.abs(dayPredictPer));
		request.setAttribute("monthPredictPer", Math.abs(monthPredictPer));		
		
		//微信推送信息
		double dayPredictDesc = 0;
		double monthPredictDesc = 0;
		String dayTips = "";
		String monthTips = "";
		if (dayPredictPer > 100d) {
			dayPredictDesc = DataUtil.doubleSubtract(dayPredictPer, 100d);
			dayTips = "已经超额使用了今日预测电量的"+String.valueOf(dayPredictDesc)+"%，";
		}else {
			dayPredictDesc = dayPredictPer;
			dayTips = "已经使用了今日预测电量的"+String.valueOf(dayPredictDesc)+"%，";
		}
		
		if (monthPredictPer > 100d) {
			monthPredictDesc = DataUtil.doubleSubtract(monthPredictPer, 100d);
			monthTips = "已经超额使用了本月预测电量的"+String.valueOf(monthPredictDesc)+"%。";
		}else {
			monthPredictDesc = monthPredictPer;
			monthTips = "已经使用了本月预测电量的"+String.valueOf(monthPredictDesc)+"%。";
		}
		
		//底部tab显示判断
		request.setAttribute("showTab", getStrParam(request, "from", null));
		
		request.setAttribute("title", "实时分享");
		request.setAttribute("desc", "哇哦，"+ledgerName+dayTips+monthTips);

        //记录用户足迹
        this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_61, null, 2);
		return new ModelAndView("energyApp/WeChat/easynx_current_data");
	}

	/**
	 * 昨日能耗概况
	 * @param request
	 * @return
	 */
	@RequestMapping("/dayReport")
	public @ResponseBody
	ModelAndView dayReport(HttpServletRequest request) {
		// 身份认证
		long accountId = getLongParam(request, "accountId", -1L);
		UserBean userBean = phoneService.getUserByAccountId(accountId);		
		if (userBean == null || userBean.getLedgerId() == null) {
			return null;
		}
		
		String ledgerName = userBean.getLedgerName();
		long ledgerId = userBean.getLedgerId();
		request.setAttribute("ledgerName", userBean.getLedgerName());
		request.setAttribute("accountId", accountId);
		
		String baseDate = DateUtil.getYesterdayDateStr("yyyy-MM-dd");
		Date beginDate = DateUtil.convertStrToDate(baseDate + " 00:00:00");
		Date endDate = DateUtil.convertStrToDate(baseDate + " 23:59:59");

		// 昨日总电量
		Double yestodayQ = phoneService.getLedgerEnergyData(ledgerId,
				beginDate, endDate, 1);
		request.setAttribute("yestodayQ", yestodayQ);
		
		// 昨日功率因数
		Double yesterdayPF = phoneService.getLedgerPFData(ledgerId, beginDate,
				endDate, 1);
		request.setAttribute("yesterdayPF", yesterdayPF);
		
		// 昨日报警次数
		Integer yesterdayEvent = phoneService.getEventNumById(ledgerId,
				beginDate, endDate);
		request.setAttribute("yesterdayEvent", yesterdayEvent);

		// 昨日最大需量
		Double yestodayMaxMD = indexService.getLedgerMaxPwr(ledgerId, beginDate, endDate);
		request.setAttribute("yestodayMaxMD", yestodayMaxMD);
		
		Date preBeginDate = DateUtil.getDateBetween(beginDate, -1);
		Date preEndDate = DateUtil.getDateBetween(endDate, -1);

		// 前天总电量
		Double preQ = phoneService.getLedgerEnergyData(ledgerId, preBeginDate,
				preEndDate, 1);

		// 前天功率因数
		Double prePF = phoneService.getLedgerPFData(ledgerId, preBeginDate,
				preEndDate, 1);

		// 前天报警次数
		Integer preEvent = phoneService.getEventNumById(ledgerId, preBeginDate,
				preEndDate);
		

		// 前天最大需量
		Double preMaxMD = indexService.getLedgerMaxPwr(ledgerId, preBeginDate, preEndDate);
		
		//昨日电量占前天电量的占比
		double circleQ = 0;
		if (yestodayQ != null && preQ != null && preQ != 0) {
			circleQ = DataUtil.doubleDivide(DataUtil.doubleMultiply(yestodayQ, 100), preQ, 1);
		}
		request.setAttribute("circleQ", circleQ);
		
		//电量环比增长
		Double percentQ = valueCompare(preQ, yestodayQ);
		request.setAttribute("percentQ", percentQ);		
		//功率因数环比增长
		Double percentPF = valueCompare(prePF, yesterdayPF);
		request.setAttribute("percentPF", percentPF);
		//事件告警环比增长
		Double percentEvent = valueCompare(preEvent, yesterdayEvent);
		request.setAttribute("percentEvent", percentEvent);
		//最大需量环比增长
		Double percentMD = valueCompare(preMaxMD, yestodayMaxMD);		
		request.setAttribute("percentMD", percentMD);
		
		String tips = "";
		if (percentQ != null) {
			if (percentQ > 0d) {
				tips = "的昨日用电量相对于前日上升了"+percentQ.toString()+"%。";
			} else if (percentQ < 0d){
				tips = "的昨日用电量相对于前日下降了"+percentQ.toString()+"%。";
			} else {
				tips = "的昨日用电量相对于前日下降了0%。";
			}
		}
		
		//底部tab显示判断
		request.setAttribute("showTab", getStrParam(request, "from", null));
		
		request.setAttribute("title", "昨日分享");
		request.setAttribute("desc", ledgerName+tips);

        //记录用户足迹
        this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_62, null, 2);
		return new ModelAndView("energyApp/WeChat/yesterday_data");
	}

	/**
	 * 历史能耗概况
	 * @param request
	 * @return
	 */
	@RequestMapping("/historyReport")
	public @ResponseBody
	ModelAndView weekReport(HttpServletRequest request){
		// 身份认证
		long accountId = getLongParam(request, "accountId", -1L);
		int reportType = getIntParams(request, "reportType", 1);// 1为周报，2为月报
		UserBean userBean = phoneService.getUserByAccountId(accountId);
		if (userBean == null || userBean.getLedgerId() == null) {
			return null;
		}
		
		String ledgerName = userBean.getLedgerName();
		Long ledgerId = userBean.getLedgerId();
		request.setAttribute("accountId", accountId);
		request.setAttribute("reportType", reportType);
		request.setAttribute("ledgerName", userBean.getLedgerName());

		// 上周能耗查询
		String baseDate = DateUtil.getMonday(DateUtil.getAWeekAgoDateStr("yyyy-MM-dd"));

		Date beginDate = DateUtil.convertStrToDate(baseDate + " 00:00:00");
		Date endDate = DateUtil.convertStrToDate(DateUtil.getSunday(baseDate)
				+ " 23:59:59");

		// 上周总电量
		Double lastWeekQ = phoneService.getLedgerEnergyData(ledgerId,
				beginDate, endDate, 1);
		request.setAttribute("lastWeekQ", lastWeekQ);

		// 上周功率因数
		Double lastWeekRQ = phoneService.getLedgerSumRQ(ledgerId, beginDate,
				endDate);
		Double lastWeekPF = DataUtil.getPF(lastWeekQ, lastWeekRQ);
		request.setAttribute("lastWeekPF", lastWeekPF);

		// 上周报警次数
		Integer lastWeekEvent = phoneService.getEventNumById(ledgerId,
				beginDate, endDate);
		request.setAttribute("lastWeekEvent", lastWeekEvent);
		
		// 上周工作日
		Integer lastWeekWorkDays = getWorkDayNum(beginDate, endDate, ledgerId);
		request.setAttribute("lastWeekWorkDays", lastWeekWorkDays);

		// 上周最大需量
		Double lastWeekMaxMD = indexService.getLedgerMaxPwr(ledgerId, beginDate, endDate);
		request.setAttribute("lastWeekMaxMD", lastWeekMaxMD);
		
		Date preBeginDate = DateUtil.getDateBetween(beginDate, -7);
		Date preEndDate = DateUtil.getDateBetween(endDate, -7);

		// 上上周电量
		Double preWeekQ = phoneService.getLedgerEnergyData(ledgerId,
				preBeginDate, preEndDate, 1);

		// 上上周功率因数
		Double preWeekRQ = phoneService.getLedgerSumRQ(ledgerId, preBeginDate,
				preEndDate);
		Double preWeekPF = DataUtil.getPF(preWeekQ, preWeekRQ);

		// 上上周报警次数
		Integer preWeekEvent = phoneService.getEventNumById(ledgerId,
                preBeginDate, preEndDate);

		// 上上周最大需量
		Double preWeekMaxMD = indexService.getLedgerMaxPwr(ledgerId, preBeginDate, preEndDate);
		
		//上周电量占上上周电量的占比
		double weekCircleQ = 0;
		if (lastWeekQ != null && preWeekQ != null && preWeekQ != 0) {
			weekCircleQ = new BigDecimal(lastWeekQ).multiply(new BigDecimal(100))
					.divide(new BigDecimal(preWeekQ), 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		}
		request.setAttribute("weekCircleQ", weekCircleQ);
		
		//上周电量环比增长
		Double weekPercentQ = valueCompare(preWeekQ, lastWeekQ);
		request.setAttribute("weekPercentQ", weekPercentQ);
		//上周功率因数环比增长
		Double weekPercentPF = valueCompare(preWeekPF, lastWeekPF);
		request.setAttribute("weekPercentPF", weekPercentPF);
		//上周报警事件环比增长
		Double weekPercentEvent = valueCompare(preWeekEvent, lastWeekEvent);
		request.setAttribute("weekPercentEvent", weekPercentEvent);
		//上周最大需量环比增长
		Double weekPercentMD = valueCompare(preWeekMaxMD, lastWeekMaxMD);
		request.setAttribute("weekPercentMD", weekPercentMD);
		
		// 上月能耗查询--------------------------------------------------------------------------//
		baseDate = DateUtil.convertDateToStr(DateUtil
				.getPreMonthFristDay(new Date()), "yyyy-MM-dd");
		beginDate = DateUtil.convertStrToDate(baseDate + " 00:00:00");
		endDate = DateUtil.convertStrToDate(DateUtil.getMonthLastDay(baseDate)
				+ " 23:59:59");

		// 上月总电量
		Double lastMonQ = phoneService.getLedgerEnergyData(ledgerId, beginDate,
				endDate, 2);
		request.setAttribute("lastMonQ", lastMonQ);
		
		// 上月功率因数
		Double lastMonPF = phoneService.getLedgerPFData(ledgerId, beginDate,
				endDate, 2);
		request.setAttribute("lastMonPF", lastMonPF);
		
		// 上月报警次数
		Integer lastMonEvent = phoneService.getEventNumById(ledgerId,
				beginDate, endDate);
		request.setAttribute("lastMonEvent", lastMonEvent);

		// 上月最大需量
		Double lastMonMaxMD = indexService.getLedgerMaxPwr(ledgerId, beginDate, endDate);
		request.setAttribute("lastMonMaxMD", lastMonMaxMD);
		
		// 上月工作日
		Integer lastMonWorkDays = getWorkDayNum(beginDate, endDate, ledgerId);
		request.setAttribute("lastMonWorkDays", lastMonWorkDays);
		
		preBeginDate = DateUtil.getMonthDate(beginDate, -1);
		preEndDate = DateUtil.getMonthLastDay(preBeginDate);

		// 上上月电量
		Double preMonQ = phoneService.getLedgerEnergyData(ledgerId,
				preBeginDate, preEndDate, 2);

		// 上上月最大需量
		Double preMonMaxMD = indexService.getLedgerMaxPwr(ledgerId, preBeginDate, preEndDate);

		// 上上月功率因数
		Double preMonPF = phoneService.getLedgerPFData(ledgerId, preBeginDate,
				preEndDate, 2);

		// 上上月报警次数
		Integer preMonEvent = phoneService.getEventNumById(ledgerId,
				preBeginDate, preEndDate);
		
		//上月电量占上上月电量的占比
		double monCircleQ = 0;
		if (lastMonQ != null && preMonQ != null && preMonQ != 0) {
			monCircleQ = new BigDecimal(lastMonQ).multiply(new BigDecimal(100))
					.divide(new BigDecimal(preMonQ), 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		}
		request.setAttribute("monCircleQ", monCircleQ);
		
		//上月电量环比增长
		Double monPercentQ = valueCompare(preMonQ, lastMonQ);
		request.setAttribute("monPercentQ", monPercentQ);
		//上月功率因数环比增长
		Double monPercentPF = valueCompare(preMonPF, lastMonPF);
		request.setAttribute("monPercentPF", monPercentPF);
		//上月报警次数环比增长
		Double monPercentEvent = valueCompare(preMonEvent, lastMonEvent);
		request.setAttribute("monPercentEvent", monPercentEvent);
		//上月最大需量环比增长
		Double monPercentMD = valueCompare(preMonMaxMD, lastMonMaxMD);
		request.setAttribute("monPercentMD", monPercentMD);

		//底部tab显示判断
		request.setAttribute("showTab", getStrParam(request, "from", null));
		
		request.setAttribute("title", "历史分享");
		request.setAttribute("desc", ledgerName+"的上周和上月用电简报。");

        //记录用户足迹
        this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_63, null, 2);
		return new ModelAndView("energyApp/WeChat/easynx_history_data");
	}
	
	/**
	 * 小易建议
	 * @param request
	 * @return
	 */
	@RequestMapping("/easyAdvice")
	public ModelAndView easyAdvice(HttpServletRequest request) {
		Long accountId = getLongParam(request, "accountId", -1L);
		UserBean userBean = phoneService.getUserByAccountId(accountId);
		if (userBean == null || userBean.getLedgerId() == null) {
			return null;
		}

		EasyAdviceScoreBean bean1 = null;
		EasyAdviceScoreBean bean2 = null;
		EasyAdviceScoreBean bean3 = null;
		
		//查询缓存中的最低分的三项评分项
		Object obj = wechatAssessment.getAssessmentCache().get(accountId);
		List<EasyAdviceScoreBean> scoreList = null;
		if (obj != null) {
			Map<String, Object> accountCache = (Map<String, Object>) obj;
			scoreList = (List<EasyAdviceScoreBean>)accountCache.get("scoreList");		
			if (scoreList != null && scoreList.size() > 0) {
				bean1 = scoreList.get(0);
				bean2 = scoreList.get(1);
				bean3 = scoreList.get(2);
			}
		}
		if (bean1 != null && bean2 != null && bean3 != null) {
			//名称
			request.setAttribute("scoreName1", bean1.getScoreName());
			//实际值
			request.setAttribute("acVal1", bean1.getAcValStr());
			//推荐值
			request.setAttribute("stVal1", bean1.getStValStr());
			//规则
			request.setAttribute("rules1", bean1.getRules());
			request.setAttribute("scoreName2", bean2.getScoreName());
			request.setAttribute("acVal2", bean2.getAcValStr());
			request.setAttribute("stVal2", bean2.getStValStr());
			request.setAttribute("rules2", bean2.getRules());
			request.setAttribute("scoreName3", bean3.getScoreName());
			request.setAttribute("acVal3", bean3.getAcValStr());
			request.setAttribute("stVal3", bean3.getStValStr());
			request.setAttribute("rules3", bean3.getRules());
		}
		
		return new ModelAndView("energyApp/WeChat/easynx_advice");
		
	}
	
	/**
	 * 获取企业时间范围内工作日天数
	 * 
	 * @return
	 */
	public int getWorkDayNum(Date beginDate, Date endDate, long ledgerId) {

		int num = 0;
		int betweenDays = DateUtil.daysBetweenDates(endDate, beginDate);
		for (int i = 0; i <= betweenDays; i++) {
			Date judgeDate = DateUtil.getDateBetween(beginDate, i);
			if (1 == bigDataService.judgeDayType(judgeDate, ledgerId)) {
				num++;
			}
		}

		return num;
	}

	/**
	 * 计算环比增长
	 * 
	 * @param oldVal
	 * @param newVal
	 * @return
	 */
	public Double valueCompare(Double oldVal, Double newVal) {
		
		Double percent = null; double d_oldVal = 0; double d_newVal = 0;
		if (null != oldVal && null != newVal) {d_oldVal = oldVal; d_newVal = newVal;} else return null; if (0 != d_oldVal) {
			percent = new BigDecimal(d_newVal).subtract(new BigDecimal(d_oldVal)).multiply(new BigDecimal(100))
					.divide(new BigDecimal(d_oldVal), 1, BigDecimal.ROUND_HALF_DOWN).doubleValue();
			if (percent >= 100d && percent < 1000d) {
				DoubleUtils.getDoubleValue(percent, 0);
			} else if(percent >= 1000) {
				percent = 999d;
			} 		
		}
		
		return percent;

	}

	public Double valueCompare(Integer oldVal, Integer newVal) {
		if (oldVal == null || newVal == null || newVal == 0) return null;
		Double percent = null;
		int i_oldVal = oldVal; int i_newVal = newVal;
		percent = new BigDecimal(i_newVal).subtract(new BigDecimal(i_oldVal)).divide(new BigDecimal(i_oldVal), 3, BigDecimal.ROUND_HALF_DOWN)
				.multiply(new BigDecimal(100)).doubleValue();
		if (percent >= 100d && percent < 1000d) {
			DoubleUtils.getDoubleValue(percent, 0);
		} else if(percent >= 1000) {
			percent = 999d;
		} 	

		return percent;

	}

}
