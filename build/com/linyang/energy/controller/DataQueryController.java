package com.linyang.energy.controller;

import com.linyang.energy.common.URLConstant;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.Item;
import com.linyang.energy.service.DataQueryService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.service.VoltCurrPowerService;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;
import com.linyang.util.DoubleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;



@Controller
@RequestMapping("/dataQuery")
public class DataQueryController extends BaseController{

	@Autowired
	private DataQueryService dataQueryService;
	
	@Autowired
	private UserAnalysisService	userAnalysisService;

	@Autowired
	private VoltCurrPowerService voltCurrPowerService;
	
	/**
	 * 电量查询页面
	 * @return
	 */
	@RequestMapping("/showElePage")
	public ModelAndView showElectricityPage(HttpServletRequest request){
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(com.linyang.energy.utils.DateUtil.addDateDay(
				WebConstant.getChartBaseDate(), -1), com.linyang.energy.utils.DateUtil.SHORT_PATTERN);// DateUtils.getCurrentTime(DateUtils.FORMAT_SHORT);
		String beforeAfterDate = DateUtils.getBeforeAfterDate(currentTime, -10);
		//开始时间
		request.setAttribute("beginDate", beforeAfterDate);
		//结束时间
		request.setAttribute("endDate", currentTime);
		return new ModelAndView("/energy/dataquery/query_electricity");
	}

	/**
	 * 电压电流查询
	 * @return
	 */
	@RequestMapping("/showVcPage")
	public ModelAndView showVoltageCurrentPage(HttpServletRequest request){
		Map<String, String> param = new HashMap<String, String>();
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		String beforeAfterDate = DateUtils.getBeforeAfterDate(currentTime, -1);
		Long accountId = super.getSessionUserInfo(request).getAccountId();
		//获取用户y轴最小值设置
		param.put("yMinValue", voltCurrPowerService.getUserYMin(accountId).toString());
		param.put("beginTime", beforeAfterDate);//DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN));
		param.put("endTime",   beforeAfterDate);//DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN));
		
		param.put("menuType", "1");//查询选项类型
		return new ModelAndView("/energy/dataquery/query_voltage_current", "params", param);
	}
	
	/**
	 * 扩展曲线查询
	 * @return
	 */
	@RequestMapping("/showVcPage2")
	public ModelAndView showVoltageCurrentPage2(HttpServletRequest request){
		Map<String, String> param = new HashMap<String, String>();
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		String beforeAfterDate = DateUtils.getBeforeAfterDate(currentTime, -1);
		Long accountId = super.getSessionUserInfo(request).getAccountId();
		//获取用户y轴最小值设置
		param.put("yMinValue", voltCurrPowerService.getUserYMin(accountId).toString());
		param.put("beginTime", beforeAfterDate);//DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN));
		param.put("endTime",   beforeAfterDate);//DateUtil.getYesterdayDateStr(DateUtil.DEFAULT_SHORT_PATTERN));
		
		param.put("menuType", "2");//查询选项类型
		return new ModelAndView("/energy/dataquery/query_voltage_current", "params", param);
	}
	
	/**
	 * 指标关联
	 * @return
	 */
	@RequestMapping("/showConnectAnalysisPage")
	public ModelAndView showConnectAnalysisPage(){
		Map<String, String> param = new HashMap<String, String>();
		String currentTime = DateUtil.convertDateToStr(WebConstant.getChartBaseDate(),DateUtil.SHORT_PATTERN);
		String beforeAfterDate = DateUtils.getBeforeAfterDate(currentTime, -1);
		param.put("beginTime", beforeAfterDate);
		param.put("endTime",   beforeAfterDate);
		return new ModelAndView(URLConstant.URL_CONNECT_ANALYSIS, "params", param);
	}
	
	/**
	 * 热工消耗查询
	 * @return
	 */
	@RequestMapping("/showTcPage")
	public ModelAndView showThermalConsumptionPage(){
		return new ModelAndView("/energy/dataquery/query_thermal_consumption");
	}
	
	/**
	 * 电量示值查询
	 * @return
	 */
	@RequestMapping("/showIndicationPage")
	public ModelAndView showIndicationPage(){
		Map<String, String> param = new HashMap<String, String>();
		Date d = com.linyang.energy.utils.DateUtil.addDateDay(com.linyang.energy.utils.DateUtil.clearDate(WebConstant
				.getChartBaseDate()), -1);
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(d,
				com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		String beforeAfterDate = com.linyang.energy.utils.DateUtil.convertDateToStr(com.linyang.energy.utils.DateUtil.addDateDay(
				d, -7), com.linyang.energy.utils.DateUtil.SHORT_PATTERN);
		param.put("beginTime", beforeAfterDate);
		param.put("endTime", currentTime);
		return new ModelAndView("/energy/dataquery/query_indication", "params", param);
	}
    
    /**
	 * 班组电量考核
	 * @return
	 */
	@RequestMapping("/showStagePage")
	public ModelAndView showGroupElectricityPage(){
		Map<String, String> param = new HashMap<String, String>();
		Date d = com.linyang.energy.utils.DateUtil.addDateDay(com.linyang.energy.utils.DateUtil.clearDate(WebConstant
				.getChartBaseDate()), -1);
		String currentTime = com.linyang.energy.utils.DateUtil.convertDateToStr(d,
				com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);
		String beforeAfterDate = com.linyang.energy.utils.DateUtil.convertDateToStr(com.linyang.energy.utils.DateUtil.addDateDay(
				d, -7), com.linyang.energy.utils.DateUtil.MOUDLE_PATTERN);
		param.put("beginTime", beforeAfterDate);
		param.put("endTime", currentTime);
		return new ModelAndView("/energy/dataquery/query_group_electricity", "params", param);
	}

	/**
	 * 得到热工消耗图表
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/eleChart", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getElectricityChartData(HttpServletRequest request) throws IOException{
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		String dataType = (String) queryMap.get("dataType");
		List<ChartItemWithTime> list = dataQueryService.getElectricityChartData(queryMap);
		map.put("list", list);
        int unitFlag = 0; //单位标识(0表示默认；1表示乘1000；2表示再乘1000)
		int listSize = 0;
		if(CommonMethod.isCollectionNotEmpty(list)){
			listSize = list.size();
			map.put("legend", list.get(0).getMap().keySet());
			List<Item> pieDatas = new ArrayList<Item>();
			//饼图
            double chartMax = 0D;
			Item item = null;
			for (ChartItemWithTime chartItemWithTime : list) {
				item = new Item(chartItemWithTime.getName());
				Collection<Object> values = chartItemWithTime.getMap().values();
				double totalValues = 0;
				for (Object object : values) {
					if(DoubleUtils.isDoubleType(object.toString())){
                        totalValues = DataUtil.doubleAdd(totalValues, Double.valueOf(object.toString()));
                        if( Double.valueOf(object.toString()) > chartMax ){    //图中数据的最大值（用于单位标识）
                            chartMax = Double.valueOf(object.toString());
                        }
                    }
				}
				item.setValue(DoubleUtils.getDoubleValue(totalValues, 5));
				pieDatas.add(item);
			}
			map.put("pieDatas",pieDatas);

            if(chartMax > 10000000){
                unitFlag = 2;
            }
            else if(chartMax > 10000){
                unitFlag = 1;
            }
		}
        map.put("unitFlag", unitFlag);
		map.put("listSize", listSize);

        //记录用户足迹
        Object exportExcel = request.getAttribute("exportExcel");
        if(exportExcel == null){
            Long operItemId = OperItemConstant.OPER_ITEM_1;//复费率电量
            if(dataType.equals("0")){//电量
                operItemId = OperItemConstant.OPER_ITEM_3;
            }else if(dataType.equals("2")){//水量
                operItemId = OperItemConstant.OPER_ITEM_43;
            }else if(dataType.equals("3")){//气量
                operItemId = OperItemConstant.OPER_ITEM_45;
            }else if(dataType.equals("4")){//热量
                operItemId = OperItemConstant.OPER_ITEM_47;
            }
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,12L, 1);
        }

		return map;
	}


	/**
	 * 得到热工消耗图表
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/tcChart", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> getThermalConsumptionChartData(HttpServletRequest request) throws IOException{
		Map<String,Object> map = new HashMap<String, Object>();
		List<ChartItemWithTime> list = dataQueryService.getThermalConsumptionChartData(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
		map.put("list", list);
		if(CommonMethod.isCollectionNotEmpty(list))
			map.put("legend", list.get(0).getMap().keySet());
		return map;
	}

	/**
	 * 导出到Excel
	 * @author 周礼
	 * @param 参数 request(页面请求),response(页面响应,返回excel)
	 */

	@RequestMapping(value = "/eleExcel")
	public @ResponseBody void getEleExcel(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String filename = "能管对象电量";
        String pageInfo = request.getParameter("pageInfo");
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(pageInfo);
		if (queryMap.get("type").toString().equals("2")){
			filename = "采集点电量";
            if(("2").equals(queryMap.get("dataType").toString())){
                filename = "采集点水量";
            }else if(("3").equals(queryMap.get("dataType").toString())){
                filename = "采集点气量";
            }else if(("4").equals(queryMap.get("dataType").toString())){
                filename = "采集点热量";
            }
        }else if(queryMap.get("type").toString().equals("1")){
            if(("2").equals(queryMap.get("dataType").toString())){
                filename = "能管对象水量";
            }else if(("3").equals(queryMap.get("dataType").toString())){
                filename = "能管对象气量";
            }else if(("4").equals(queryMap.get("dataType").toString())){
                filename = "能管对象热量";
            }
        }

		response.setHeader("Content-Disposition",
		"attachment;filename="+new String(filename.getBytes(), "ISO-8859-1")+".xls");	//指定下载的文件名
		response.setContentType("application/vnd.ms-excel");
		//得到数据：数据库查询的结果集map，调用上面getElectricityChartData方法
        request.setAttribute("exportExcel", 1);
		Map<String, Object> map = getElectricityChartData(request);
		//得到页面请求信息pageInfo，作为参数传进getEleExcel方法
		dataQueryService.getEleExcel(filename, response.getOutputStream(),map,queryMap);//"Cache.xls"

		String dataType = (String) queryMap.get("dataType");
		Long operItemId = OperItemConstant.OPER_ITEM_2;//付费率电量
        if(dataType.equals("0")){//电量
			operItemId = OperItemConstant.OPER_ITEM_4;
		}else if(dataType.equals("2")){//水量
			operItemId = OperItemConstant.OPER_ITEM_44;
		}else if(dataType.equals("3")){//气量
			operItemId = OperItemConstant.OPER_ITEM_46;
		}else if(dataType.equals("4")){//热量
			operItemId = OperItemConstant.OPER_ITEM_48;
		}
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,12L, 1);
	}

}
