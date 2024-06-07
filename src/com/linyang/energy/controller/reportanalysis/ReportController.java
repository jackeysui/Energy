package com.linyang.energy.controller.reportanalysis;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linyang.common.web.page.Page;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.ReportService;
import com.linyang.energy.service.TreeSetService;

/**
 * 
 * 报表分析控制器
 * @author guosen
 *
 */
@Controller
@RequestMapping("/report")
public class ReportController extends BaseController{

	@Autowired
	private ReportService reportService;
	@Autowired
	private TreeSetService treeSetService;
    @Autowired
	private UserAnalysisService userAnalysisService;

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	/**
	 * 进入报表分析页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/index")
	public ModelAndView goToReportPage(HttpServletRequest request) {
		//日报默认昨天
		Date baseTime = DateUtil.getSomeDateInYear(new Date(), -1);
		try {
			request.setAttribute("baseTime", DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN));
			UserBean userBean = this.getSessionUserInfo(request);
			Map<String, Object> typeMap = reportService.getMeterTypeMap(userBean.getLedgerId());
			request.setAttribute("meterType", typeMap);
		} catch (ParseException e) {
			Log.error("ReportController.goToReportPage()-----日期转换出错");
		}
		return new ModelAndView("/energy/report/report_analysis");
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/search")
	public @ResponseBody Map<String, Object> search(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
            //记录用户足迹
            if(json.has("dataType")){
                Long operItem = OperItemConstant.OPER_ITEM_49;
                if(json.getInt("dataType") == 1 || json.getInt("dataType") == 7 || json.getInt("dataType") == 12){
                    operItem = OperItemConstant.OPER_ITEM_49;
                }
                else if(json.getInt("dataType") == 2){
                    operItem = OperItemConstant.OPER_ITEM_68;
                }
                else if(json.getInt("dataType") == 3){
                    operItem = OperItemConstant.OPER_ITEM_70;
                }
                else if(json.getInt("dataType") == 4){
                    operItem = OperItemConstant.OPER_ITEM_72;
                }
                else if(json.getInt("dataType") == 5){
                    operItem = OperItemConstant.OPER_ITEM_74;
                }
                else if(json.getInt("dataType") == 6){
                    operItem = OperItemConstant.OPER_ITEM_76;
                }
                else if(json.getInt("dataType") == 8 || json.getInt("dataType") == 13){
                    operItem = OperItemConstant.OPER_ITEM_83;
                }
                else if(json.getInt("dataType") == 9 || json.getInt("dataType") == 14){
                    operItem = OperItemConstant.OPER_ITEM_85;
                }
                else if(json.getInt("dataType") == 10 || json.getInt("dataType") == 15){
                    operItem = OperItemConstant.OPER_ITEM_87;
                }
                else if(json.getInt("dataType") == 11 || json.getInt("dataType") == 16){
                    operItem = OperItemConstant.OPER_ITEM_89;
                }
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), operItem, 207L, 1);
            }
            //查询
			Map<String, Object> param = this.populateParam(json);
			result = reportService.search(param);
		} catch (ParseException e) {
			Log.info("saveSubscriptionInfo error IOException");
		}
		return result;
	}
	
	/**
	 * 导出EXCEL
	 * @return
	 */
	@RequestMapping(value = "/reportExcel", method = RequestMethod.POST)
	public @ResponseBody void reportExcel(HttpServletRequest request , HttpServletResponse response ) {
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
            //记录用户足迹
            if(json.has("dataType")){
                Long operItem = OperItemConstant.OPER_ITEM_50;
                if(json.getInt("dataType") == 1 || json.getInt("dataType") == 7 || json.getInt("dataType") == 12){
                    operItem = OperItemConstant.OPER_ITEM_50;
                }
                else if(json.getInt("dataType") == 2){
                    operItem = OperItemConstant.OPER_ITEM_69;
                }
                else if(json.getInt("dataType") == 3){
                    operItem = OperItemConstant.OPER_ITEM_71;
                }
                else if(json.getInt("dataType") == 4){
                    operItem = OperItemConstant.OPER_ITEM_73;
                }
                else if(json.getInt("dataType") == 5){
                    operItem = OperItemConstant.OPER_ITEM_75;
                }
                else if(json.getInt("dataType") == 6){
                    operItem = OperItemConstant.OPER_ITEM_77;
                }
                else if(json.getInt("dataType") == 8 || json.getInt("dataType") == 13){
                    operItem = OperItemConstant.OPER_ITEM_84;
                }
                else if(json.getInt("dataType") == 9 || json.getInt("dataType") == 14){
                    operItem = OperItemConstant.OPER_ITEM_86;
                }
                else if(json.getInt("dataType") == 10 || json.getInt("dataType") == 15){
                    operItem = OperItemConstant.OPER_ITEM_88;
                }
                else if(json.getInt("dataType") == 11 || json.getInt("dataType") == 16){
                    operItem = OperItemConstant.OPER_ITEM_90;
                }
                this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), operItem, 207L, 1);
            }
            //查询
			Map<String, Object> param = this.populateParam(json);
			Map<String, Object> result = reportService.search(param);
			String title = this.createTitle(param);
			byte[] bs = title.getBytes();
			response.setHeader("Content-Disposition", "attachment;filename="+new String(bs, "ISO-8859-1")+".xls");	//指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			reportService.reportExcel("Cache.xls", response.getOutputStream(), result , param, title);
		} catch (ParseException e) {
			Log.info("reportExcel error ParseException");
		}
		catch (IOException e) {
			Log.info("reportExcel error IOException");
		}
	}
	
	/**
	 * 生成报表名称
	 * @param param
	 * @return
	 */
	private String createTitle(Map<String, Object> param) {
		int dataType = (Integer) param.get("dataType");
		String baseTime = (String) param.get("baseTime");
		String title = "";
		switch (dataType) {
			case 1:
				//日报-正向有功电量，正向无功电量
				title = baseTime + "电量日报";
				break;
			case 2:
				//日报-有功功率
				title = baseTime + "有功功率日报";
				break;
			case 3:
				//日报-无功功率
				title = baseTime + "无功功率日报";
				break;
			case 4:
				//日报-功率因数
				title = baseTime + "功率因数日报";
				break;
			case 5:
				//日报-电流
				title = baseTime + "电流日报";
				break;
			case 6:
				//日报-电压
				title = baseTime + "电压日报";
				break;
			case 7:
				//月报-电
				title = baseTime + "电量月报";
				break;
			case 8:
				//月报-水
				title = baseTime + "水量月报";
				break;
			case 9:
				//月报-气
				title = baseTime + "气量月报";
				break;
			case 10:
				//月报-热
				title = baseTime + "热量月报";
				break;
			case 11:
				//月报-综合能源
				title = baseTime + "综合能源月报";
				break;
			case 12:
				//年报-电
				title = baseTime + "电量年报";
				break;
			case 13:
				//年报-水
				title = baseTime + "水量年报";
				break;
			case 14:
				//年报-气
				title = baseTime + "气量年报";
				break;
			case 15:
				//年报-热
				title = baseTime + "热量年报";
				break;
			case 16:
				//年报-综合能源
				title = baseTime + "综合能源年报";
				break;
		}
		return title;
	}

	/**
	 * 封装查询参数
	 * @param json
	 * @return
	 * @throws ParseException 
	 */
	private Map<String, Object> populateParam(JSONObject json) throws ParseException {
		Map<String, Object> params = new HashMap<String, Object>();
		int reportType = 0;
		int dataType = 0;
		Set<String> meterTempSet = new HashSet<>( 0 );
		List<String> meterTemp = new ArrayList<String>();
		boolean includeChild = false;
		boolean isEle = false;
		//报表类型
		if (json.has("reportType")) {
			reportType = json.getInt("reportType");
			params.put("reportType", reportType);
		}
		//是否显示基本信息
		if (json.has("display")) {
			int display = json.getInt("display");
			params.put("display", display);
		}
		//数据类型
		if (json.has("dataType")) {
			dataType = json.getInt("dataType");
			params.put("dataType", dataType);
		}
		//基本时间
		String baseTimeStr = DateUtil.getCurrentDateStr(DateUtil.DEFAULT_SHORT_PATTERN);
		if (json.has("baseTime")) {
			baseTimeStr = json.getString("baseTime");
			params.put("baseTime", baseTimeStr);
		}
		//开始时间
		String beginTimeStr = "";
		if (json.has("beginTime")) {
			beginTimeStr = json.getString("beginTime");
			params.put("beginTime", DateUtil.convertStrToDate(beginTimeStr, DateUtil.DEFAULT_SHORT_PATTERN));
		}
		//结束时间
		String endTimeStr = "";
		if (json.has("endTime")) {
			endTimeStr = json.getString("endTime");
			params.put("endTime", DateUtil.convertStrToDate(endTimeStr, DateUtil.DEFAULT_SHORT_PATTERN));
		}
		//是否包含子节点
		if (json.has("includeChild")) {
			includeChild = json.getBoolean("includeChild");
		}
		if (json.has("isEle")){
			isEle = json.getBoolean("isEle");
		}
		
		
		//能管对象ID
		if (json.has("ledgerId")) {
			String ledgerId = json.getString("ledgerId");
			if(ledgerId.length()>0){
				if (includeChild && !isEle) {
					//获取未选中项
					String[] noLedgerAry = null;
					String noLedger = json.getString("noLedger");
					if (noLedger.length()>0) {
						noLedgerAry = noLedger.split(",");
						Map<String, Object> map1 = new HashMap<String, Object>();
						map1.put("ledgerIdAry", noLedgerAry);
						List<String> result1 = reportService.getChildLedger(map1);
						if (result1 != null && result1.size() > 0) {
							noLedgerAry = result1.toArray(new String[result1.size()]);
						}
					}
					
					//查询选中的ledger及下属ledger
					String[] ledgerIdAry = ledgerId.split(",");
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("ledgerIdAry", ledgerIdAry);
					map2.put("noLedgerAry", noLedgerAry);
					List<String> result2 = reportService.getChildLedger(map2);
					if (result2 != null && result2.size() > 0) {
						ledgerIdAry = result2.toArray(new String[result2.size()]);
						map2.put("ledgerIdAry", ledgerIdAry);
					}				
					//非拓扑树状态下查询下属meter
					meterTemp = reportService.getChildMeter(map2);
					//电流电压只针对测量点
					if(dataType != 5 && dataType != 6){
						params.put("ledgerIdAry", ledgerIdAry);
					}			
				} else if(includeChild && isEle){
					//拓扑树状态下选中ledger
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ledgerIdAry", ledgerId.split(","));
					Map<String, Object> map4 = new HashMap<String, Object>();
					meterTemp = reportService.getEleChildMeterNew(map);
					map4.put("meterIdAry", meterTemp);
					meterTemp.addAll( reportService.getEleChildMeterByM(map4) );
				} else{
					params.put("ledgerIdAry", ledgerId.split(","));
				}
			}
		}
		
		//测量点ID
		if (json.has("meterId") && meterTemp.size() == 0) {
			String meterId = json.getString("meterId");
			String noMeter = json.getString("noMeter");
			if(meterId.length()>0){
				String[] meterIdAry = meterId.split(",");
				for (int i = 0; i < meterIdAry.length; i++) {
					meterTemp.add(meterIdAry[i]);
				}
				if (includeChild) {
					Map<String, Object> map3 = new HashMap<String, Object>();
					String[] noMeterAry = null;
					if (noMeter.length()>0) {
						noMeterAry = noMeter.split(",");
					}
					if (isEle) {
						if (noMeterAry != null) {
							Map<String, Object> map4 = new HashMap<String, Object>();
							map4.put("meterIdAry", noMeterAry);
							List<String> result3 = reportService.getEleChildMeterByM(map4);
							if (result3 != null && result3.size() > 0) {
								noMeterAry = result3.toArray(new String[result3.size()]);
							}
						}
						map3.put("meterIdAry", meterIdAry);
						map3.put("noMeterAry", noMeterAry);
						List<String> result4 = reportService.getEleChildMeterByM(map3);
						meterTemp.addAll(result4);
					}else {
						//移除未勾选项meter
						if(noMeterAry != null){
							for (int i = 0; i < noMeterAry.length; i++) { 
								Iterator iter = meterTemp.iterator();  
								while (iter.hasNext()) { 
									if (iter.next().toString().equals(noMeterAry[i])) { 
										iter.remove(); 							 
									} 
								}
							}
						}
					}
				}				
			}
		}
		
		if (meterTemp != null && meterTemp.size() > 0) {
			
			meterTempSet.addAll( meterTemp );
			//综合能源只针对能管对象
			if (dataType != 11 && dataType != 16) {
//				params.put("meterIdAry",  meterTemp.toArray(new String[meterTemp.size()]));
				params.put("meterIdAry", new ArrayList<>( meterTempSet ));
			}
		}

		if(reportType == 0){
			//日报
			try {
				params.put("beginDate", DateUtil.convertStrToDate(baseTimeStr + " 00:00:00"));
				Date endTime = null;
				if(dataType == 1){//日电量
					endTime =  DateUtil.getSomeDateInYear(DateUtil.convertStrToDate(baseTimeStr + " 00:00:00"), 1);
				}else {
					endTime =  DateUtil.convertStrToDate(baseTimeStr + " 23:59:59");
				}
					
				params.put("endDate", endTime);
			} catch (ParseException e) {
				Log.error("日期格式错误");
			}
			
		}else if(reportType == 1){
			//月报
			try {
				Date endDate = DateUtil.convertStrToDate(baseTimeStr + " 00:00:00");
				params.put("endDate", endDate);
				//如果选择日期是这个月的最后一天，则开始时间取这个月的第一天
				if(endDate.equals(DateUtil.getLastDayOfMonth(endDate))){
					params.put("beginDate", DateUtil.getMonthFirstDay(endDate) );
				}else {
					params.put("beginDate", DateUtil.getSomeDateInYear(DateUtil.getLastMonthDate(endDate), 1) );
				}
				
			} catch (ParseException e) {
				Log.error("日期格式错误");
			}
		}else if(reportType == 2){
			//年报
			try {
				//得到这个月月初，然后往前推12个月
				Date baseTime = DateUtil.convertStrToDate(baseTimeStr + " 00:00:00");
				//上月的第一天
				Date endDate = DateUtil.getMonthFirstDay(DateUtil.getLastMonthDate(baseTime));
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.MONTH, -11);
				Date beginDate = cal.getTime();
				params.put("endDate", endDate);
				params.put("beginDate", beginDate);
			} catch (ParseException e) {
				Log.error("日期格式错误");
			}
		}
		return params;
	}
	
	/**
	 * 进入报表分析页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gotoMonthElePage")
	public ModelAndView gotoMonthElePage(HttpServletRequest request) {
		//日报默认昨天
		Date baseTime = DateUtil.getSomeDateInYear(new Date(), -1);
		try {
			request.setAttribute("endTime", DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN));
			request.setAttribute("beginTime", DateUtil.convertDateToStr(DateUtil.getLastMonthDate(baseTime), DateUtil.DEFAULT_SHORT_PATTERN));
			UserBean userBean = this.getSessionUserInfo(request);
			Map<String, Object> typeMap = reportService.getMeterTypeMap(userBean.getLedgerId());
			request.setAttribute("meterType", typeMap);
			Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
			List<Map<String, Object>> dataList = treeSetService.getCompanyList(ledgerId, 103);//商户
			request.setAttribute("dataList", dataList);
		} catch (ParseException e) {
			Log.error("ReportController.gotoMonthElePage()-----日期转换出错");
		}
		return new ModelAndView("/energy/report/month_ele");
	}	
	
	/**
	 * 查询商户月电费数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/searchMonthEle")
	public @ResponseBody Map<String, Object> searchMonthEle(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> param = this.populateParam(json);
			result = reportService.searchMonthEle(param);
            //记录用户足迹
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_51, 223L, 1);
		} catch (ParseException e) {
			Log.info("searchMonthEle error ParseException");
		}
		return result;
	}
	
	/**
	 * 导出EXCEL
	 * @return
	 */
	@RequestMapping(value = "/reportMonthExcel")
	public @ResponseBody void reportMonthExcel(HttpServletRequest request , HttpServletResponse response ) {
		try {
			JSONObject json = JSONObject.fromObject(request.getParameter("paramInfo"));
			Map<String, Object> param=null;
			if(json!=null && json.toString().length()>0){
				param = this.populateParam(json);
			}
			Map<String, Object> result = null;
			if(param!=null && param.toString().length()>0){
				result = reportService.searchMonthEle(param);
			}
			String title = "月电费报表";
			byte[] bs = title.getBytes();
			response.setHeader("Content-Disposition", "attachment;filename="+new String(bs, "ISO-8859-1")+".xls");	//指定下载的文件名
			response.setContentType("application/vnd.ms-excel");
			reportService.reportMonthExcel("Cache.xls", response.getOutputStream(), result , param, title);
            //记录用户足迹
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_52, 223L, 1);
		} catch (IOException e) {
			Log.info("reportMonthExcel error IOException");
		} catch (ParseException e) {
			Log.info("reportMonthExcel error ParseException");
		} //catch (NullPointerException e) {
			//Log.info("reportMonthExcel error NullPointerException");
		//}
	}

    /**
     * 进入"负荷分析报表"页面
     */
    @RequestMapping(value = "/fhAnalysis")
    public ModelAndView fhAnalysis(HttpServletRequest request) {
        //日报默认昨天
        Date baseTime = DateUtil.getSomeDateInYear(new Date(), -1);
        try {
            request.setAttribute("endTime", DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN));
            request.setAttribute("beginTime", DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN));
        }
        catch (ParseException e) {
            Log.error(this.getClass() + ".fhAnalysis()");
        }
        return new ModelAndView("/energy/report/fh_analysis");
    }

    /**
     * 负荷分析报表 --- 加载数据
     */
    @RequestMapping("/fhAnalysisData")
    public @ResponseBody Map<String,Object> fhAnalysisData(HttpServletRequest request){
        Map<String, Object> resultMap = null;

        Map<String, Object> paramInfo = null;
        try {
            paramInfo = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
            Page page = super.getCurrentPage(paramInfo.get("pageNo").toString(), paramInfo.get("pageSize").toString());
            paramInfo.put("page", page);

            resultMap = this.reportService.fhAnalysisDataPage(paramInfo);
            resultMap.put("page", page);
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".fhAnalysisData()");
        }

        return resultMap;
    }

    /**
     * 负荷分析报表 --- 导出EXCEL
     */
    @RequestMapping(value = "/fhAnalysisExcel")
    public @ResponseBody void fhAnalysisExcel(HttpServletRequest request , HttpServletResponse response){
        try {
            Map<String, Object> paramInfo = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

            String title = "负荷分析报表";
            byte[] bs = title.getBytes();
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(bs, "ISO-8859-1") + ".xls");	//指定下载的文件名
            response.setContentType("application/vnd.ms-excel");
            reportService.fhAnalysisExcel(response.getOutputStream(), paramInfo, title);
        }
        catch (IOException e) {
            Log.info("fhAnalysisExcel error IOException");
        }
    }

    /**
     * 进入"电耗分析"页面
     */
    @RequestMapping(value = "/energyAnalysis")
    public ModelAndView energyAnalysis(HttpServletRequest request){
        //日报默认昨天
        Date baseTime = DateUtil.getSomeDateInYear(new Date(), -1);
        try {
            request.setAttribute("endTime", DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN));
            request.setAttribute("beginTime", DateUtil.convertDateToStr(baseTime, DateUtil.DEFAULT_SHORT_PATTERN));
        }
        catch (ParseException e) {
            Log.error(this.getClass() + ".energyAnalysis()");
        }
        return new ModelAndView("/energy/report/ele_analysis");
    }

    /**
     * 电耗分析 --- 加载数据
     */
    @RequestMapping("/energyAnalysisData")
    public @ResponseBody Map<String,Object> energyAnalysisData(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object> paramInfo = null;
        try {
            paramInfo = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

            resultMap.put("list", this.reportService.energyAnalysisData(paramInfo));
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".energyAnalysisData()");
        }

        return resultMap;
    }

    /**
     * 电耗分析 --- 导出EXCEL
     */
    @RequestMapping(value = "/energyAnalysisExcel")
    public @ResponseBody void energyAnalysisExcel(HttpServletRequest request , HttpServletResponse response){
        try {
            Map<String, Object> paramInfo = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

            String title = "电耗分析报表";
            byte[] bs = title.getBytes();
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(bs, "ISO-8859-1") + ".xls");	//指定下载的文件名
            response.setContentType("application/vnd.ms-excel");
            reportService.energyAnalysisExcel(response.getOutputStream(), paramInfo, title);
        }
        catch (IOException e) {
            Log.info("fhAnalysisExcel error IOException");
        }
    }


    /**
     * 进入"电耗明细"页面
     */
    @RequestMapping(value = "/energyDetail")
    public ModelAndView energyDetail(HttpServletRequest request){
        //日报默认昨天
        Date endDate = DateUtil.getSomeDateInYear(new Date(), -1);
        Date beginDate = DateUtil.getSomeDateInYear(endDate, -29);
        try {
            request.setAttribute("endTime", DateUtil.convertDateToStr(endDate, DateUtil.DEFAULT_SHORT_PATTERN));
            request.setAttribute("beginTime", DateUtil.convertDateToStr(beginDate, DateUtil.DEFAULT_SHORT_PATTERN));
        }
        catch (ParseException e) {
            Log.error(this.getClass() + ".energyDetail()");
        }
        return new ModelAndView("/energy/report/ele_detail");
    }

    /**
     * 电耗明细 --- 加载数据
     */
    @RequestMapping("/energyDetailData")
    public @ResponseBody Map<String,Object> energyDetailData(HttpServletRequest request){
        Map<String, Object> resultMap = null;

        Map<String, Object> paramInfo = null;
        try {
            paramInfo = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));
            Page page = super.getCurrentPage(paramInfo.get("pageNo").toString(), paramInfo.get("pageSize").toString());
            paramInfo.put("page", page);

            resultMap = this.reportService.energyDetailDataPage(paramInfo);
            resultMap.put("page", page);
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".energyDetailData()");
        }

        return resultMap;
    }

    /**
     * 电耗明细 --- 导出EXCEL
     */
    @RequestMapping(value = "/energyDetailExcel")
    public @ResponseBody void energyDetailExcel(HttpServletRequest request , HttpServletResponse response){
        try {
            Map<String, Object> paramInfo = jacksonUtils.readJSON2Map(request.getParameter("paramInfo"));

            String title = "电耗明细报表";
            byte[] bs = title.getBytes();
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(bs, "ISO-8859-1") + ".xls");	//指定下载的文件名
            response.setContentType("application/vnd.ms-excel");
            reportService.energyDetailExcel(response.getOutputStream(), paramInfo, title);
        }
        catch (IOException e) {
            Log.info("energyDetailExcel error IOException");
        }
    }
	
}
