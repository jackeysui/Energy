package com.linyang.energy.controller;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.Item;
import com.linyang.energy.model.*;
import com.linyang.energy.service.*;
import com.linyang.energy.utils.CSRFTokenManager;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.CommonMethod;
import com.linyang.util.DoubleUtils;

import net.sf.json.JSONObject;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.utils.MapMapping;

@Controller
@RequestMapping("/index")
public class IndexController extends BaseController {
	@Autowired
	private IndexService indexService;
	@Autowired
	private FrameService frameService;
	@Autowired
	private LedgerManagerService ledgerManagerService;
	@Autowired
	private PhoneService phoneService;
	@Autowired
	private EleAssessment eleAssessmentBean;
	@Autowired
	private DataQueryService dataQueryService;
	@Autowired
	private UserAnalysisService userAnalysisService;
	@Autowired
	private EventQueryService eventQueryService;
	@Autowired
	private VoltCurrPowerService voltCurrPowerService;
	@Autowired
	private MeterManagerService meterManagerService;
	@Autowired
	private UserService userService;
	@Autowired
	private SuggestService suggestService;

	/**
	 * 跳转到首页
	 * @param request
     * @return
     */
	@RequestMapping("/showIndexPage")
	public ModelAndView showIndexPage(HttpServletRequest request) {
		Map<String, Object> map = frameService.getUserModules(super.getSessionUserInfo(request).getAccountId(),
				super.getSessionRoleType(request));
		map.put("mapData", MapMapping.getMapMapping());
		map.put("csrf", CSRFTokenManager.getTokenForSession(request.getSession()));
		Long accountId = super.getSessionUserInfo(request).getAccountId();
		Long ledgerId = 0L;
		if (super.getSessionUserInfo(request).getLedgerId() == 0L) {
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = super.getSessionUserInfo(request).getLedgerId();
		}

		LedgerBean ledger = new LedgerBean();
		if (ledgerManagerService.selectByLedgerId(ledgerId) != null) {
			ledger = ledgerManagerService.selectByLedgerId(ledgerId);
		}
		map.put("ledgerId", ledger.getLedgerId());
		map.put("analyType", ledger.getAnalyType());
		map.put("energyType", ledger.getEnergyType());
		map.put("isGuestUser", (Boolean) request.getSession().getAttribute("isGuestUser"));
		if (ledger.getAnalyType() != 102) {
			List<LedgerBean> ledgerList = ledgerManagerService.getAllSubLedgersByLedgerId(ledgerId);
			if (ledgerList != null && ledgerList.size() > 0) {
				map.put("energyType", 1);
				for (LedgerBean lb : ledgerList) {
					if (lb.getEnergyType() == 2) {// 综合能源
						map.put("energyType", 2);
						break;
					}
				}
			}
		}

		int hasCheck = 0;
		if (ledger.getAnalyType() == 102 && super.getSessionUserInfo(request).getLedgerId() != 0) {
			Integer score = this.phoneService.getLastAssessScore(super.getSessionUserInfo(request).getAccountId());
			String value = "";
			if (score != null) {
				if (score >= 100) {
					value = "满";
                } else {
					value = score.toString();
				}
			}
			map.put("score", value);
			hasCheck = 1;
		}
		map.put("hasCheck", hasCheck);
		Integer roleType = super.getSessionRoleType(request);
		map.put("roleType", roleType);
		// 电工模式下，获取常用功能
		if (roleType == 1) {
			List<Map<String, Object>> moduleList = this.userAnalysisService
					.getUsuallyUseModel(super.getSessionUserInfo(request).getAccountId());
			map.put("moduleList", moduleList);
		}
		return new ModelAndView("energy/index/index", map);
	}

	/**
	 * 用户等级帮助页面
	 */
	@RequestMapping("/levelPage")
	public ModelAndView levelPage(HttpServletRequest request) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Long accountId = super.getSessionUserInfo(request).getAccountId();
		map.putAll(this.phoneService.getLevelHelp(accountId));
		return new ModelAndView("energy/index/levelPage", map);
	}

	/**
	 * 查看服务器是否启动
	 */
	@RequestMapping(value = "/checkServerStart", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkServerStart(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		return result;
	}

	/**
	 * 显示轮显主页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/show")
	public ModelAndView showIndexDemoPage(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		LedgerBean ledger = ledgerManagerService.selectByLedgerId(ledgerId);
		map.put("ledgerName", ledger.getLedgerName());
		map.put("analyType", ledger.getAnalyType());
		return new ModelAndView("energy/linyangdemo/index", map);
	}

	/**
	 * 轮显---主页面根据“轮显配置”轮流显示相关页面数据
	 */
	@RequestMapping(value = "/getShowLedger", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getShowLedger(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user != null) {
			if (user.getLedgerId() == 0L) {
				Long accountId = user.getAccountId();
				// 权限为群组分配ledgerId
				ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
			} else {
				ledgerId = user.getLedgerId();
			}
		} else {
			ledgerId = getLongParam(request, "ledgerId", 0);
			String username = getStrParam(request, "username", "");
			String roleType = getStrParam(request, "roleType", "");
			UserBean userBean = userService.getUserByUserName(username);
			frameService.saveSession(loginSessionKey, userBean, roleType, request, username);
		}
		Map<String, Object> result = this.indexService.getShowLedgerUrls(ledgerId);
		return result;
	}

	/**
	 * 轮显---实时能耗检测
	 */
	@RequestMapping("/realTimeCheck")
	public ModelAndView realTimeCheck(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		long ledgerId = getLongParam(request, "ledgerId", -1);
		map.put("ledgerId", ledgerId);
		if (ledgerId != -1) {
			LedgerBean ledger = ledgerManagerService.selectByLedgerId(ledgerId);
			map.put("ledgerName", ledger.getLedgerName());
		}
		return new ModelAndView("energy/linyangdemo/realTimeCheck", map);
	}

	/**
	 * 轮显---实时能耗检测数据
	 */
	@RequestMapping(value = "/realEnergyCheck", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> realEnergyCheck(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();

		long ledgerId = getLongParam(request, "ledgerId", -1L);
		if (ledgerId != -1L) {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("tId", ledgerId);
			queryMap.put("type", 1); // 表示分户
			queryMap.put("timeType", 1); // 表示"日"
			queryMap.put("dataType", 1); // 复费率电量
			LedgerBean ledgerBean = this.ledgerManagerService.selectByLedgerId(ledgerId);
			queryMap.put("rateId", ledgerBean.getRateId());
			Date temp = com.linyang.energy.utils.DateUtil.getDateBetween(new Date(), -1);
			Date endTime = com.linyang.energy.utils.DateUtil.setDateEnd(temp);
			Date temp2 = com.linyang.energy.utils.DateUtil.clearDate(new Date());
			Date beginTime = com.linyang.energy.utils.DateUtil.getDateBetween(temp2, -11);
			Long beginDate = beginTime.getTime() / 1000;
			Long endDate = endTime.getTime() / 1000;
			queryMap.put("beginDate", beginDate); // 起始时间
			queryMap.put("endDate", endDate); // 结束时间
			/// 近11天的用电量柱状图数据
			List<ChartItemWithTime> list = dataQueryService.getElectricityChartData(queryMap);
			map.put("list", list);
			int listSize = 0;
			if (CommonMethod.isCollectionNotEmpty(list)) {
				listSize = list.size();
				map.put("legend", list.get(0).getMap().keySet());
				List<Item> pieDatas = new ArrayList<Item>();
				// 饼图
				Item item = null;
				for (ChartItemWithTime chartItemWithTime : list) {
					item = new Item(chartItemWithTime.getName());
					Collection<Object> values = chartItemWithTime.getMap().values();
					BigDecimal totalValues = BigDecimal.ZERO;
					for (Object object : values) {
						if (DoubleUtils.isDoubleType(object.toString()))
							totalValues = totalValues.add(new BigDecimal(Double.valueOf(object.toString())));
					}
					item.setValue(DoubleUtils.getDoubleValue(totalValues.doubleValue(), 2));
					pieDatas.add(item);
				}
				map.put("pieDatas", pieDatas);
			}
			map.put("listSize", listSize);
			Date date = WebConstant.getChartBaseDate();
			/// 实时用电量
			List<Map<String, Object>> list1 = this.phoneService.getEnergyData(ledgerId, 1, date, 1);
			map.put("list1", list1);
			/// 实时功率因数
			List<Map<String, Object>> list2 = this.phoneService.getPFData(ledgerId, 1, date, 1,0);
			map.put("list2", list2);
			/// 实时功率
			List<Map<String, Object>> list3 = this.dataQueryService.getPowData(ledgerId, date);
			map.put("list3", list3);
			/// 实时谐波数据
			Map<String, Object> harData = this.dataQueryService.getHarData(ledgerId);
			map.putAll(harData);
		}

		return map;
	}

	/**
	 * 轮显--- 保存轮显分户配置
	 */
	@RequestMapping(value = "/saveLyConfig", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveLyConfig(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String data = getStrParam(request, "data", "");
		this.indexService.saveLyConfig(data);
		return map;
	}

	/**
	 * 用电评估弹出页面
	 */
	@RequestMapping("/eleAssessment")
	public ModelAndView eleAssessment(HttpServletRequest request) {
		return new ModelAndView("energy/index/eleAssessment");
	}

	/**
	 * 用电评估弹出页面数据
	 */
	@RequestMapping(value = "/getEleAssessment", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEleAssessment(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		UserBean userBean = super.getSessionUserInfo(request);
		if (userBean != null) {
			map.putAll(this.phoneService.getLastAssessment(userBean.getAccountId()));
		}
		return map;
	}

	/**
	 * 点击立即体检
	 **/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/beginBodyCheck", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> beginBodyCheck(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserBean userBean = super.getSessionUserInfo(request);
		if (userBean != null) {
			Long accountId = userBean.getAccountId();
			Object obj = this.eleAssessmentBean.getAssessmentCache().get(accountId);
			if (obj != null) {
				Map<String, Object> accountCache = (Map<String, Object>) obj;
				int complete = Integer.valueOf(accountCache.get("complete").toString());
				if (0 != complete) {

					this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_42, null, 1); // 记录用户使用记录

					this.eleAssessmentBean.assessment(accountId);
				}
			} else {
				this.userAnalysisService.addAccountTrace(accountId, OperItemConstant.OPER_ITEM_42, null, 1); // 记录用户使用记录

				this.eleAssessmentBean.assessment(accountId);
			}
		}
		return result;
	}

	/**
	 * 点击立即体检后，不断查询缓存中该accountId对应的结果
	 **/
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAssessmentCache", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAssessmentCache(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserBean userBean = super.getSessionUserInfo(request);
		if (userBean != null) {
			Long accountId = userBean.getAccountId();
			Object obj = this.eleAssessmentBean.getAssessmentCache().get(accountId);
			if (obj != null) {
				Map<String, Object> accountCache = (Map<String, Object>) obj;
				result.putAll(accountCache);
				int complete = Integer.valueOf(accountCache.get("complete").toString());
				if (complete == 1) {
					// 计算击败用户百分比
					if (accountCache.get("score") != null) {
						int score = Integer.valueOf(accountCache.get("score").toString());
						int percent = this.phoneService.getBeatUserPercent(accountId, score);
						result.put("beatPercent", percent);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 自动登录页面
	 * 
	 * @return
	 */
	@RequestMapping("/autoLogin")
	public ModelAndView autoLogin(HttpServletRequest request) {
		return new ModelAndView("energy/index/auto_login");
	}

	@RequestMapping(value = "/showCompanyPage", method = RequestMethod.GET)
	public ModelAndView showCompanyPage(String cityName, long ledgerId) {
		String str_cityName = cityName;
		if (str_cityName == null || str_cityName.trim().length() == 0)
			str_cityName = "";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cityName", str_cityName);
		map.put("ledgerId", ledgerId);
		return new ModelAndView("energy/index/company", map);
	}

	/**
	 * 查询系统事件
	 * 
	 * @return
	 */
	@RequestMapping(value = "/querySysEventInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> querySysEventInfo(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Map<String, Object> param = super.jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
			Page page = new Page((Integer) param.get("pageNo"), (Integer) param.get("pageSize"));
			List<EventBean> list = indexService.querySysEventInfo(param, page);
			map.put("page", page);
			map.put("dataInfo", list);
		} catch (IOException e) {
			Log.error(this.getClass() + ".querySysEventInfo()--无法查询系统事件");
		}
		return map;
	}

	/**
	 * 查询计量点统计信息
	 * 
	 * @param meterStatus
	 *            计量点状态 0:停止;1:正常
	 * @return 计量点类型 1:电; 2:水; 3:气; 4:热
	 */
	@RequestMapping(value = "/queryMeterCountInfo", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> queryMeterCountInfo(int meterStatus) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("meterStatus", meterStatus);
		return indexService.queryMeterCountInfo(param);
	}

	/**
	 * 查询实时曲线统计数据(电水汽)当前用户所有计量点
	 * 
	 * @param curveType
	 *            曲线类型(1:电; 2:水; 3:气)
	 * @return
	 */
	@RequestMapping(value = "/queryRealCurveInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryRealCurveInfo(HttpServletRequest request, int curveType) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ledgerId", super.getSessionUserInfo(request).getLedgerId()); // 所属分户Id
		param.put("beginTime", DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN)); // 开始时间
		param.put("endTime", DateUtil.getCurrentDate(DateUtil.DEFAULT_FULL_PATTERN)); // 截止时间
		param.put("curveType", curveType);
		List<RealCurveBean> resultList = null;
		try {
			// 查询实时曲线统计数据(电水汽)
			resultList = indexService.queryRealCurveInfo(param);
		} catch (ParseException e) {
			Log.error(this.getClass() + ".queryRealCurveInfo()--无法查询实时曲线统计数据(电水汽)当前用户所有计量点");
		}
		result.put("curveStat", resultList);
		// 查询实时曲线统计数据
		// a. 本日峰值
		Map<String, Object> resultMap = indexService.getRealCurveMaxData(param);
		result.put("todayPeak", resultMap != null ? resultMap.get("DATA_VALUE") : null);
		// b. 本月峰值
		param.put("beginTime", DateUtil.getCurrMonthFirstDay()); // 当前月一号
		Map<String, Object> resultMap2 = indexService.getRealCurveMaxData(param);
		result.put("monthPeak", resultMap2 != null ? resultMap2.get("DATA_VALUE") : null);
		// c. 本年峰值
		param.put("beginTime", DateUtil.getCurrYearFirstDay()); // 本年一月一号
		Map<String, Object> resultMap3 = indexService.getRealCurveMaxData(param);
		result.put("yearPeak", resultMap3 != null ? resultMap3.get("DATA_VALUE") : null);
		return result;
	}

	/**
	 * 查询电费信息
	 * 
	 * @return 计量点类型 1:电; 2:水; 3:气; 4:热
	 */
	@RequestMapping(value = "/queryElecBillInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryElecBillInfo(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 获取当前登录人所属分户
		UserBean user = super.getSessionUserInfo(request);
		if (null != user) {
			Long ledgerId = 0L;
			if (user.getLedgerId() == 0L) {
				Long accountId = user.getAccountId();
				// 权限为群组分配ledgerId
				ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
			} else {
				ledgerId = user.getLedgerId();
			}
			param.put("ledgerId", ledgerId); // 分户Id
			param.put("beginTime", DateUtil.getCurrMonthFirstDay()); // 当前月一号
			param.put("endTime", DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN)); // 当天
			param.put("lastBeginTime", DateUtil.getLastMonthDate(DateUtil.getCurrMonthFirstDay())); // 上一个月一号
			param.put("lastEndTime",
					DateUtil.getLastMonthDate(DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN))); // 上个月同期
			return indexService.queryElecBillInfo(param);
		}
		return null;
	}

	/**
	 * 查询能耗排名(计量点类型 0:综合, 1:电, 2:水, 3:气, 4:热)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryUseEnergyRanking", method = RequestMethod.POST)
	public @ResponseBody List<Map<String, Object>> queryUseEnergyRanking(HttpServletRequest request, int meterType) {
		// 获取当前登录人所属分户
		UserBean user = super.getSessionUserInfo(request);
		List<Map<String, Object>> list = null;
		if (null != user) {
			Map<String, Object> param = new HashMap<String, Object>();
			Long ledgerId = 0L;
			if (user.getLedgerId() == 0L) {
				Long accountId = user.getAccountId();
				// 权限为群组分配ledgerId
				ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
			} else {
				ledgerId = user.getLedgerId();
			}
			param.put("ledgerId", ledgerId); // 分户Id
			param.put("meterType", meterType); // 计量点类型
			param.put("beginTime", DateUtil.getCurrMonthFirstDay()); // 当前月一号
			param.put("endTime", DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN)); // 当天
			list = indexService.queryUseEnergyRanking(param);
		}
		return list;
	}

	/**
	 * 查询本月用能分布
	 * 
	 * @return 按分户统计totalByLedger, 按计量点类型统计totalByType
	 */
	@RequestMapping(value = "/queryCurrMonthEnergyDist", method = RequestMethod.POST)
	public @ResponseBody Map<String, List<Map<String, Object>>> queryCurrMonthEnergyDist(HttpServletRequest request) {
		// 获取当前登录人所属分户
		UserBean user = super.getSessionUserInfo(request);
		Map<String, List<Map<String, Object>>> map = null;
		if (null != user) {
			Map<String, Object> param = new HashMap<String, Object>();
			Long ledgerId = 0L;
			if (user.getLedgerId() == 0L) {
				Long accountId = user.getAccountId();
				// 权限为群组分配ledgerId
				ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
			} else {
				ledgerId = user.getLedgerId();
			}
			param.put("ledgerId", ledgerId);
			param.put("beginTime", DateUtil.getCurrMonthFirstDay()); // 当前月一号
			param.put("endTime", DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN)); // 当天
			map = indexService.queryCurrMonthEnergyDist(param);
		}
		return map;
	}

	/**
	 * 获取企业描述
	 * 
	 * @return remark:企业描述
	 */
	@RequestMapping(value = "/getEnterpriseDesc", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> getEnterpriseDesc(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Map<String, String> result = null;
		if (null != user) {
			// 获取企业描述
			Long ledgerId = 0L;
			if (user.getLedgerId() == 0L) {
				Long accountId = user.getAccountId();
				// 权限为群组分配ledgerId
				ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
			} else {
				ledgerId = user.getLedgerId();
			}
			result = indexService.getEnterpriseDesc(ledgerId);
		}
		return result;
	}

	/**
	 * 获取第一个图的数据--CO2排放，标准煤
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChart1Data")
	public @ResponseBody Map<String, Object> getChart1Data(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.queryChart1Data(ledgerId);
	}

	/**
	 * 获取第二个图的数据---耗电量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChart2Data")
	public @ResponseBody List<Map<String, Object>> getChart2Data(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.queryChart2Data(ledgerId);
	}

	@RequestMapping(value = "/getChart2DataNew")
	public @ResponseBody Map<String, Object> getChart2DataNew(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.queryChart2DataNew(ledgerId);
	}

	/**
	 * 获取第四个图的数据----最大需量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChart4Data")
	public @ResponseBody Map<String, Object> getChart4Data(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.queryChart4Data(ledgerId);
	}

	/**
	 * 获取第五个图的数据---7天日电量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChart5Data")
	public @ResponseBody Map<String, Object> getChart5Data(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.queryChart5Data(ledgerId);
	}

	/**
	 * 获取第六个图的数据----实时功率
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChart6Data")
	public @ResponseBody List<Map<String, Object>> getChart6Data(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		List<Map<String, Object>> list = indexService.queryChart6Data(ledgerId);
		return list;
	}

	/**
	 * 获取第七个图的数据----30天日电量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChart7Data")
	public @ResponseBody Map<String, Object> getChart7Data(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.queryChart7Data(ledgerId);
	}

	@RequestMapping(value = "/getChart7DataNew")
	public @ResponseBody Map<String, Object> getChart7DataNew(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.getChart7DataNew(ledgerId);
	}

	/**
	 * 获取第八个图的数据----最大负荷
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChart8Data")
	public @ResponseBody Map<String, Object> getChart8Data(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		Map<String, Object> result = indexService.queryChart8Data(ledgerId);
		return result;
	}

	/**
	 * 获取第三个图的数据----电费
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getChart3Data")
	public @ResponseBody Map<String, Object> getChart3Data(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.queryChart3Data(ledgerId);
	}

	@RequestMapping(value = "/getChart3DataNew")
	public @ResponseBody Map<String, Object> getChart3DataNew(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		Long ledgerId = 0L;
		if (user.getLedgerId() == 0L) {
			Long accountId = user.getAccountId();
			// 权限为群组分配ledgerId
			ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
		} else {
			ledgerId = user.getLedgerId();
		}
		return indexService.queryChart3DataNew(ledgerId);
	}

	@RequestMapping(value = "/getChart3DataPartner")
	public @ResponseBody Map<String, Object> getChart3DataPartner(HttpServletRequest request) {
		UserBean user = super.getSessionUserInfo(request);
		long accountId = user.getAccountId();
		long ledgerId = 0;
		if (accountId == 1) {
			ledgerId = -100L;
		} else {
			if (user.getLedgerId() == 0L) {
				// 权限为群组分配ledgerId
				ledgerId = ledgerManagerService.getLedgerIfNull(accountId);
			} else {
				ledgerId = user.getLedgerId();
			}
		}
		return indexService.getChart3DataPartner(ledgerId);
	}

	@RequestMapping("/showTotalDataPage")
	public ModelAndView showTotalDataPage(HttpServletRequest request) {
		Map<String, Object> map = frameService.getUserModules(super.getSessionUserInfo(request).getAccountId(),
				super.getSessionRoleType(request));
		return new ModelAndView("energy/linyangdemo/total_data", map);
	}

	/**
	 * 各厂区用电量 （本月累计）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getTotal1Data")
	public @ResponseBody List<Map<String, Object>> getTotal1Data(HttpServletRequest request) {
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ledgerId", ledgerId);
		param.putAll(getDemoDate());
		List<Map<String, Object>> list = indexService.getTotal1Data(param);
		return list;
	}

	/**
	 * 各厂区最大需量 （本月）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getTotal2Data")
	public @ResponseBody List<Map<String, Object>> getTotal2Data(HttpServletRequest request) {
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ledgerId", ledgerId);
		param.putAll(getDemoDate());
		return indexService.getTotal2Data(param);
	}

	/**
	 * 每日用电量和光伏发电对比（本月累计）---或者是和上月电量对比
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getTotal3Data")
	public @ResponseBody Map<String, Object> getTotal3Data(HttpServletRequest request) {
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ledgerId", ledgerId);
		param.putAll(getDemoDate());
		return indexService.getTotal3Data(param);
	}

	/**
	 * 光伏发电占比（本月累计）---或者是和上月电量对比。
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getTotal4Data")
	public @ResponseBody Map<String, Object> getTotal4Data(HttpServletRequest request) {
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ledgerId", ledgerId);
		param.putAll(getDemoDate());
		return indexService.getTotal4Data(param);
	}

	/**
	 * 获取大屏显示页面的第一天和最后一天
	 * 
	 * @return
	 */
	private Map<String, Object> getDemoDate() {
		Date startDate = null;
		Date endDate = null;
		Map<String, Object> map = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth == 1) {// 每个月的第一天，取上一个月的数据
			startDate = DateUtil.getLastMonthFirstDay();
			endDate = DateUtil.getLastDayOfMonth(DateUtil.getLastMonthFirstDay());
		} else {
			startDate = DateUtil.getCurrMonthFirstDay();
			endDate = DateUtil.getLastDayOfMonth(new Date());
		}
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		// 获取上个月的同期天数
		map.put("startDate2", DateUtil.getLastMonthDate(startDate));
		map.put("endDate2", DateUtil.getLastMonthDate(endDate));
		return map;
	}

	/**
	 * 用户提交建议
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addSug", method = RequestMethod.POST)
	public @ResponseBody boolean addSug(HttpServletRequest request) {
		UserBean sessionuser = null;
		if (request.getSession() != null) {
			sessionuser = ((UserBean) request.getSession().getAttribute(loginSessionKey));
		}
		boolean isSuccess = false;
		if (sessionuser == null) {
			isSuccess = false;
		} else {

			UserBean user = super.getSessionUserInfo(request);

			Object replyBean = suggestService.interpositionRecordForWeb(user.getAccountId(), getStrParam(request, "sugmsg", ""), null,
					getStrParam(request, "contactway", ""));
			if(replyBean!=null)
				isSuccess = true;

			// 记录日志
			StringBuilder sb = new StringBuilder();
			String sugmsg = getStrParam(request, "sugmsg", "");
			if (sugmsg == null) {
				return isSuccess;
			}
			if (sugmsg.length() > 7) {
				sugmsg = sugmsg.substring(6) + "...";
			}
			sb.append("add a user feedback:").append(sugmsg).append(" by ")
					.append(super.getSessionUserInfo(request).getLoginName()).append("  ")
					.append(com.linyang.energy.utils.DateUtil.convertDateToStr(new Date(),
							com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN));
			int rst = CommonOperaDefine.OPRATOR_FAIL;
			if (isSuccess == true) {
				rst = CommonOperaDefine.OPRATOR_SUCCESS;
			}
			super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_ID_USER_FEEDBACK,
					CommonOperaDefine.MODULE_NAME_USER_FEEDBACK, CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE, rst,
					sb.toString()), request);
		}
		return isSuccess;
	}

	/**
	 * 联系我们界面
	 */
	@RequestMapping("/contactShow")
	public ModelAndView contact(HttpServletRequest request) {
		SuggestBean suggest = new SuggestBean();
		UserBean user = super.getSessionUserInfo(request);
		Map<String, Object> map = new HashMap<String, Object>();

		suggest.setAccountId(user.getAccountId());
		List<SuggestBean> sugList = this.indexService.getSugInfo(suggest);
		map.put("sugList", sugList);

		return new ModelAndView("energy/index/contact", map);
	}

	/**
	 * 事件列表
	 */
	@RequestMapping(value = "/indexEventList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> indexEventList(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		// 分页信息，固定显示为8条
		Page page = super.getCurrentPage("1", "8");

		// 设置开始和结束时间
		GregorianCalendar beginTime = new GregorianCalendar();
		beginTime.setTime(new Date());
		beginTime.set(Calendar.HOUR_OF_DAY, 00);
		beginTime.set(Calendar.MINUTE, 00);
		beginTime.set(Calendar.SECOND, 00);
		beginTime.set(Calendar.MILLISECOND, 00);

		GregorianCalendar endTime = new GregorianCalendar();
		endTime.setTime(new Date());
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);

		List<EventBean> result = null;

		param.put("ledgerId", ledgerId);
		param.put("page", page);
		param.put("beginTime", beginTime.getTime());
		param.put("endTime", endTime.getTime());
		result = eventQueryService.queryEventPageList3(param);
		if (result != null && result.size() > 0) {
			resultMap.put("dataInfo", result);
		}

		return resultMap;
	}

	/**
	 * 本月能耗总览
	 */
	@RequestMapping(value = "/monthEnergyView", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> monthEnergyView(HttpServletRequest request) {
		Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
		return this.indexService.indexEnergyAll(ledgerId);
	}

	/**
	 * 查询电工首页图表数据
	 */
	@RequestMapping(value = "/getMyChartData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMyChartData(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<CurveBean> chartData = null; // 曲线数据
		boolean hasData = false;
		try {
			// 设置开始和结束时间
			GregorianCalendar beginTime = new GregorianCalendar();
			beginTime.setTime(new Date());
			beginTime.set(Calendar.HOUR_OF_DAY, 00);
			beginTime.set(Calendar.MINUTE, 00);
			beginTime.set(Calendar.SECOND, 00);
			beginTime.set(Calendar.MILLISECOND, 00);

			// GregorianCalendar endTime = new GregorianCalendar();
			// endTime.setTime(new Date());
			// endTime.set(Calendar.HOUR_OF_DAY, 23);
			// endTime.set(Calendar.MINUTE, 59);
			// endTime.set(Calendar.SECOND, 59);
			Date endTime = DateUtil.getSomeDateInYear(beginTime.getTime(), 1);

			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			// 对于 有光伏的企业，"无功"换成"光伏"
			String hasLight = "1";
			LedgerBean ledgerBean = this.ledgerManagerService
					.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
			if (json.getInt("curveType") == 4 && ledgerBean != null && ledgerBean.getAnalyType() == 102) {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ledgerId", ledgerBean.getLedgerId());
				params.put("beginTime", beginTime.getTime());
				params.put("endTime", endTime);
				chartData = voltCurrPowerService.getLightPower(params);
				result.put("chartData", chartData);
			}
			// 如果没有光伏
			if (chartData == null || chartData.size() == 0) {
				hasLight = "0";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("objectType", json.getInt("objectType"));
				params.put("objectId", super.getSessionUserInfo(request).getLedgerId());
				params.put("curveType", json.getInt("curveType"));
				params.put("isLedger", json.getBoolean("isLedger"));
				params.put("dataType", 0);
				params.put("beginTime", beginTime.getTime());
				params.put("endTime", endTime);
				//add by dy   修改了曲线部分的查询机制,增加一个变量查询1小时间隔的曲线,删除会出错
				params.put( "frequency" , 3 );

				chartData = voltCurrPowerService.queryVoltCurrPowerInfo(params);
				result.putAll(handleData(chartData, params));
			}

			result.put("hasLight", hasLight);
		} catch (ParseException e) {
			Log.info("saveSubscriptionInfo error ParseException");
		}
		if (chartData != null && chartData.size() > 0) {
			hasData = true;
		}
		result.put("hasData", hasData);
		return result;
	}

	/**
	 * 处理返回的数据
	 * 
	 * @param chartData
	 * @param
	 * @param params
	 * @return
	 */
	private Map<String, Object> handleData(List<CurveBean> chartData, Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int curveType = (Integer) params.get("curveType");
		// 电压电流情况下要判断接线方式
		if (curveType == CurveBean.CURVE_TYPE_V || curveType == CurveBean.CURVE_TYPE_I) {
			Integer oType = (Integer) params.get("objectType");
			Integer commMode = 0;
			if (oType != null && oType == 1) {// EMO
				commMode = this.meterManagerService.getCommModeByLedgerId((Long) params.get("objectId"));
			} else {// DCP
				commMode = this.meterManagerService.getCommModeByMeterId((Long) params.get("objectId"));
			}
			result.put("commMode", commMode);// 接线方式
		}
		// 查询电压、电流、有功功率、无功功率、功率因数水平线
		if (curveType != CurveBean.CURVE_TYPE_ELE && curveType != CurveBean.CURVE_TYPE_RP) {// 排除无功功率和电量
			Map<String, Object> lineData = voltCurrPowerService.queryStraightLine(params);
			result.put("lineData", lineData);
		}
		result.put("chartData", chartData);
		return result;
	}

	/**
	 * 显示更新日志
	 * 
	 * @return
	 */
	@RequestMapping("/updateHelp")
	public ModelAndView updateHelp(HttpServletRequest request) {
		return new ModelAndView("energy/index/upgrade_information");
	}

	/**
	 * 显示大屏设置界面
	 * 
	 * @return
	 */
	@RequestMapping("/demoSet")
	public ModelAndView demoSet(HttpServletRequest request) {
		return new ModelAndView("energy/linyangdemo/config_set");
	}

	/**
	 * 得到企业下面的部门
	 * 
	 * @return
	 */
	@RequestMapping("/getCompanyRelation")
	public @ResponseBody Map<String, Object> getCompanyRelation(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long ledgerId = getLongParam(request, "ledgerId", 0);
		List<Map<String, Object>> listLedger = indexService.getChildLedger(ledgerId);
		List<Long> list = indexService.getCompanyRelation(ledgerId);
		result.put("list1", listLedger);
		result.put("list2", list);
		return result;
	}

	/**
	 * 得到企业下面的部门
	 * 
	 * @return
	 */
	@RequestMapping("/showScreenSet")
	public @ResponseBody Map<String, Object> showScreenSet(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Long ledgerId = getLongParam(request, "ledgerId", 0);
		CompanyDisplaySet set = this.indexService.showScreenSet(ledgerId);
		if (set != null) {
			result.put("set", set);
		}
		return result;
	}

	/**
	 * 得到企业下面的部门
	 * 
	 * @return
	 */
	@RequestMapping("/saveScreen")
	public @ResponseBody Map<String, Object> saveScreen(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Long> departAry = jacksonUtils.readJSON2Genric(request.getParameter("departAry"),
					new TypeReference<List<Long>>() {
					});
			Long ledgerId = getLongParam(request, "ledgerId", 0);
			Integer menu = getIntParams(request, "menu", 0);
			Integer gatherShow = getIntParams(request, "gatherShow", 0);
			Integer gather = getIntParams(request, "gather", 0);
			Integer depart = getIntParams(request, "depart", 0);
			this.indexService.saveScreen(ledgerId, departAry, menu, gatherShow, gather, depart);
			
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_151, 121l, 1);
		} catch (IOException e) {
			Log.error(this.getClass() + ".saveScreen()--无法得到企业下面的部门");
		}
		return result;
	}

	/**
	 * 跳转到首页
	 * 
	 * @return
	 */
	@RequestMapping("/showCompanyListPage")
	public ModelAndView showCompanyListPage(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Integer roleType = super.getSessionRoleType(request);
		map.put("roleType", roleType);
		return new ModelAndView("energy/index/companyList", map);
	}

	@RequestMapping("/getCompanyList")
	public @ResponseBody Map<String, Object> getCompanyList(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", false);
		Map<String, Object> requstParams = new HashMap<String, Object>();
		requstParams.put("accountId", super.getSessionUserInfo(request).getAccountId());
		requstParams.put("companyName", getStrParam(request, "companyName", "")); // 企业名称
		requstParams.put("regionId_0", getStrParam(request, "regionId_0", "")); // 省,
																				// region_level
																				// 0
		requstParams.put("regionId_1", getStrParam(request, "regionId_1", "")); // 市,
																				// region_level
																				// 1
		requstParams.put("regionId_2", getStrParam(request, "regionId_2", "")); // 县,
																				// region_level
																				// 2
		requstParams.put("operator", getStrParam(request, "operator", "")); // 运营商

		// 分页信息
		Page page = super.getCurrentPage(getStrParam(request, "pageNo", "1"), getStrParam(request, "pageSize", "20"));
		requstParams.put("page", page);

		List<LedgerBean> companyList = indexService.getCompanyPageList(requstParams);
		if (CommonMethod.isCollectionNotEmpty(companyList)) {
			result.put("result", true);
			result.put("companyList", companyList);
			result.put("page", page);
		}
		return result;
	}

	@RequestMapping("/getSubRegions")
	public @ResponseBody Map<String, Object> getSubRegions(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", false);

		int level = getIntParams(request, "level", 0);
		String parentId = getStrParam(request, "parentId", "0");

		List<RegionBean> regions = indexService.getSubRegions(level, parentId);
		if (CommonMethod.isCollectionNotEmpty(regions)) {
			result.put("result", true);
			result.put("regions", regions);
		}

		return result;
	}
}