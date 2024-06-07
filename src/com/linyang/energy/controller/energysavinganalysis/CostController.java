package com.linyang.energy.controller.energysavinganalysis;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.MeterManagerService;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.energy.controller.BaseController;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.Item;
import com.linyang.energy.model.MonthMDFeeBean;
import com.linyang.energy.service.CostService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.ExcelUtil;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.CommonMethod;
import com.linyang.util.DoubleUtils;

/**
 *电费计算
 * 
 * @author:gaofeng
 * @date:2014.12.17
 */
@Controller
@RequestMapping("/cost")
public class CostController extends BaseController {
	@Autowired
	private CostService costService;
	@Autowired
	private UserAnalysisService userAnalysisService;
    @Autowired
    private MeterManagerService meterManagerService;
    @Autowired
    private LedgerManagerService ledgerManagerService;
	
	/**
	 * 电费分析页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/showCostAnaPage", method = RequestMethod.GET)
	public ModelAndView showCostAnaPage() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lastMonth", DateUtil.getLastMonth(WebConstant.getChartBaseDate()));
		return new ModelAndView("/energy/scheduleanalysis/electricity-cost_analysis", map);
	}
	
	/**
	 * 月MD分析页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/showMonMdAnaPage", method = RequestMethod.GET)
	public ModelAndView showMonMdAnaPage() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lastMonth", DateUtil.getCurrentDateStr(DateUtil.MONTH_PATTERN));
		map.put("currYear", DateUtil.getCurrentYear());
		return new ModelAndView("/energy/scheduleanalysis/monthmd_analysis", map);
	}

	/**
	 * 电费计算电量图数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/eleChart", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getElectricityChartData(HttpServletRequest request) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
        Integer objType = Integer.valueOf(queryMap.get("objType").toString());
        Long objId = Long.valueOf(queryMap.get("objId").toString());		//点能管对象是ledgerId  如果点测量点就是meterId
       
        
        Integer queryType = Integer.valueOf(queryMap.get("queryType").toString()); //queryType:1表示电能耗，0表示总能耗

		List<ChartItemWithTime> list = costService.getDayEmoDcpChartData(queryMap);
        result.put("list", list);
        int unitFlag = 0;   //单位标识(0表示默认；1表示乘1000；2表示再乘1000)
        if (CommonMethod.isCollectionNotEmpty(list)) {
            result.put("legend", list.get(0).getMap().keySet());
            //图中数据的最大值（用于单位标识）
            double chartMax = 0D;
            
            for (ChartItemWithTime chartItemWithTime : list) {
            	
                Collection<Object> values = chartItemWithTime.getMap().values();
                for (Object object : values) {
                    if(DoubleUtils.isDoubleType(object.toString())){
                        if( Double.valueOf(object.toString()) > chartMax ){
                            chartMax = Double.valueOf(object.toString());
                        }
                    }
                }
            }

            if(queryType==1){
                if(chartMax > 10000000){
                    unitFlag = 2;
                }
                else if(chartMax > 10000){
                    unitFlag = 1;
                }
            }
            else if(queryType==0 && chartMax>100000){
                unitFlag = 1;
            }
        }
        result.put("unitFlag", unitFlag);

        boolean isOnlyElecMeter = false;
        boolean adjustbt_show = true;
        if(objType == 2){
            MeterBean meterBean = this.meterManagerService.getMeterDataById(objId);
            Short meterType= meterBean.getMeterType();
            if(meterType != null && meterType == 1){
                isOnlyElecMeter = true;
            }
            queryType=-1;
        }else if(objType == 1){
            List<Short> typeList = ledgerManagerService.getLedgerCalcDCPType(objId);
            if(typeList != null && typeList.size() == 1 && typeList.get(0) == 1){
                isOnlyElecMeter = true;
            }
            LedgerBean ledgerBean = ledgerManagerService.selectByLedgerId(objId);
            Integer analyType = ledgerBean.getAnalyType();
            if(analyType != null && (analyType == 104 || analyType == 105)){              	
            	adjustbt_show = false;
            }
        }
        result.put("adjustbt_show", adjustbt_show);
        result.put("isOnlyElecMeter", isOnlyElecMeter || queryType==1);

        if(isOnlyElecMeter){
            boolean p2_show = true;
            if(objType == 2){
                MeterBean meterBean = this.meterManagerService.getMeterDataById(objId);
                Integer volType= meterBean.getVolType();
                if(volType != null && volType == 1){
                    p2_show = false;
                }
            }
            else if(objType == 1){
                LedgerBean ledgerBean = ledgerManagerService.selectByLedgerId(objId);
                Integer analyType = ledgerBean.getAnalyType();
                if(analyType != null && analyType == 102){
                    p2_show = false;
                }
            }
            result.put("p2_show", p2_show);
        }

		//记录用户足迹
        Long operItemId = OperItemConstant.OPER_ITEM_28;
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,42L,1);
		
		return result;
	}
	
	/**
	 * 取电费数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/eleFee", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEleFeeData(HttpServletRequest request) throws IOException {
		return costService.calEmoDcpEleFee(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
	}

    /**
     * 深度分析 -- 记录用户使用记录
     */
    @RequestMapping(value = "/recordCostAnalyze")
    public @ResponseBody void recordCostAnalyze(HttpServletRequest request,HttpServletResponse response){
        //记录用户足迹
        this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),OperItemConstant.OPER_ITEM_29,42L,1);
    }

	/**
	 * 月MD分析图数据
	 *
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/monMdChart", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMonMdChartData(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ChartItemWithTime> list = costService.getMonMaxDemandChartData(jacksonUtils.readJSON2Map(request
				.getParameter("pageInfo")));
		map.put("list", list);
		int listSize = 0;
		if (CommonMethod.isCollectionNotEmpty(list)) {
			listSize = list.size();
			map.put("legend", list.get(0).getMap().keySet());
		}
		map.put("listSize", listSize);
		Long operItemId = OperItemConstant.OPER_ITEM_32;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,43L,1);
		return map;
	}
	
	/**
	 * 取月需量电费数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mdFee", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMdFeeData(HttpServletRequest request) throws IOException {
		return costService.getMonMDFeeData(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
	}
	
	/**
	 * 取月需量分析数据
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mdAnalysis", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMdAnalysis(HttpServletRequest request) throws IOException {
		long pointId = Long.parseLong(request.getParameter("pointId"));
		int treeType = Integer.parseInt(request.getParameter("treeType"));
		Long operItemId = OperItemConstant.OPER_ITEM_34;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,43L,1);
		return costService.nearYearMonMaxAnalysis(pointId,treeType);
	}
	
	/**
	 * 月MD分析数据导出到excel
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportMonMdData")
	public @ResponseBody void exportMonMdData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Long operItemId = OperItemConstant.OPER_ITEM_33;
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(),operItemId,43L,1);
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pointId", request.getParameter("pointId"));
		queryMap.put("timeType", request.getParameter("timeType"));
		queryMap.put("beginTime", request.getParameter("beginTime"));
		queryMap.put("endTime", request.getParameter("endTime"));
		queryMap.put("treeType", request.getParameter("treeType"));
		Map<String, Object> data = costService.getMonMDFeeData(queryMap);// 取数据

		String fileName = "monMd";
		String sheetName = "sheet";
		HSSFWorkbook wb = ExcelUtil.createWorkbook(); // 创建HSSFWorkbook对象
		HSSFSheet sheet = ExcelUtil.createSheet(wb, sheetName); // 创建HSSFSheet对象

		int rowIndex = 0;
		int cellNum = 13;// 列数
		CellStyle bodycs = wb.createCellStyle(); // 内容样式实例
		// 创建一个DataFormat对象
		HSSFDataFormat format = wb.createDataFormat();
		// 设置单元格格式是指文本型
		bodycs.setDataFormat(format.getFormat("@"));

		Row r = ExcelUtil.createRow(sheet, rowIndex, 360);
		for (int i = 1; i <= 12; i++)
			createCell(sheet, r, i, "", bodycs);
		if((Boolean) data.get("showCommonOnly")){
			createCell(sheet, r, 0, "名称", bodycs);
			if (data.get("name") != null)
				createCell(sheet, r, 1, data.get("name").toString(), bodycs);
			createCell(sheet, r, 2, "基本电费模式", bodycs);
			if (data.get("declareType") != null) {
				int dc = (Integer) data.get("declareType");
				if (dc == 1)
					createCell(sheet, r, 3, "变压器容量".toString(), bodycs);
				else if (dc == 2)
					createCell(sheet, r, 3, "需量申报".toString(), bodycs);
				else if (dc == 3)
					createCell(sheet, r, 3, "需量申报/变压器容量".toString(), bodycs);
			}
			Map<Integer, List<MonthMDFeeBean>> mdFee = (Map<Integer, List<MonthMDFeeBean>>) data.get("mdFee");
			if (mdFee != null && mdFee.size() > 0) {
				for (Integer key : mdFee.keySet()) {
					rowIndex++;
					r = ExcelUtil.createRow(sheet, rowIndex, 360);
					createCell(sheet, r, 0, key.toString(), bodycs);
					for (int i = 1; i <= 12; i++)
						createCell(sheet, r, i, i + "月", bodycs);
					
					List<MonthMDFeeBean> fee = mdFee.get(key);
					if (fee != null && fee.size() > 0) {
						rowIndex++;
						r = ExcelUtil.createRow(sheet, rowIndex, 360);
						createCell(sheet, r, 0, "月需量", bodycs);
						for (int i = 1; i <= 12; i++)
							createCell(sheet, r, i, "", bodycs);
						for (MonthMDFeeBean f : fee)
                            if(f.getMonthMD() != null)
                                createCell(sheet, r, f.getMonth(), Double.toString(f.getMonthMD()), bodycs);
                            else
                                createCell(sheet, r, f.getMonth(), "", bodycs);
					}
				}
					
			}
		}else{
			createCell(sheet, r, 0, "容量电价", bodycs);
			if (data.get("volPrice") != null)
				createCell(sheet, r, 1, data.get("volPrice").toString(), bodycs);
			createCell(sheet, r, 2, "需量电价", bodycs);
			if (data.get("demandPrice") != null)
				createCell(sheet, r, 3, data.get("demandPrice").toString(), bodycs);
			createCell(sheet, r, 4, "超罚限值", bodycs);
			if (data.get("demandThres") != null)
				createCell(sheet, r, 5, data.get("demandThres").toString() + "%", bodycs);
			createCell(sheet, r, 6, "基本电费模式", bodycs);
			if (data.get("declareType") != null) {
				int dc = (Integer) data.get("declareType");
				if (dc == 1)
					createCell(sheet, r, 7, "变压器容量".toString(), bodycs);
				else if (dc == 2)
					createCell(sheet, r, 7, "需量申报".toString(), bodycs);
				else if (dc == 3)
					createCell(sheet, r, 7, "需量申报/变压器容量".toString(), bodycs);
			}
			
			rowIndex++;
			r = ExcelUtil.createRow(sheet, rowIndex, 360);
			createCell(sheet, r, 0, "名称", bodycs);
			for (int i = 1; i <= 12; i++)
				createCell(sheet, r, i, "", bodycs);
			if (data.get("name") != null)
				createCell(sheet, r, 1, data.get("name").toString(), bodycs);
			createCell(sheet, r, 2, "容量（kVA）", bodycs);
			if (data.get("volume") != null) {
				createCell(sheet, r, 3, data.get("volume").toString(), bodycs);
				createCell(sheet, r, 5, Double.toString(new BigDecimal((Long) data.get("volume")).multiply(new BigDecimal(0.4)).doubleValue()), bodycs);
			}
			createCell(sheet, r, 4, "需量申报下限（kW）", bodycs);
			
			Map<Integer, List<MonthMDFeeBean>> mdFee = (Map<Integer, List<MonthMDFeeBean>>) data.get("mdFee");
			if (mdFee != null && mdFee.size() > 0) {
				for (Integer key : mdFee.keySet()) {
					rowIndex++;
					r = ExcelUtil.createRow(sheet, rowIndex, 360);
					createCell(sheet, r, 0, key.toString(), bodycs);
					for (int i = 1; i <= 12; i++)
						createCell(sheet, r, i, i + "月", bodycs);
					
					List<MonthMDFeeBean> fee = mdFee.get(key);
					if (fee != null && fee.size() > 0) {
						rowIndex++;
						r = ExcelUtil.createRow(sheet, rowIndex, 360);
						createCell(sheet, r, 0, "申报需量", bodycs);
						for (int i = 1; i <= 12; i++)
							createCell(sheet, r, i, "", bodycs);
						for (MonthMDFeeBean f : fee)
                            if(f.getDeclareMD() != null)
                                createCell(sheet, r, f.getMonth(), Double.toString(f.getDeclareMD()), bodycs);
                            else
                                createCell(sheet, r, f.getMonth(), "", bodycs);
						
						rowIndex++;
						r = ExcelUtil.createRow(sheet, rowIndex, 360);
						createCell(sheet, r, 0, "月需量", bodycs);
						for (int i = 1; i <= 12; i++)
							createCell(sheet, r, i, "", bodycs);
						for (MonthMDFeeBean f : fee)
							if(f.getMonthMD() != null)
                                createCell(sheet, r, f.getMonth(), Double.toString(f.getMonthMD()), bodycs);
                            else
                                createCell(sheet, r, f.getMonth(), "", bodycs);
						
						rowIndex++;
						r = ExcelUtil.createRow(sheet, rowIndex, 360);
						createCell(sheet, r, 0, "需量偏差值", bodycs);
						for (int i = 1; i <= 12; i++)
							createCell(sheet, r, i, "", bodycs);
						for (MonthMDFeeBean f : fee)
                            if(f.getMdDeviation() != null)
                                createCell(sheet, r, f.getMonth(), Double.toString(f.getMdDeviation()), bodycs);
                            else
                                createCell(sheet, r, f.getMonth(), "", bodycs);
						
						rowIndex++;
						r = ExcelUtil.createRow(sheet, rowIndex, 360);
						createCell(sheet, r, 0, "需量偏差率", bodycs);
						for (int i = 1; i <= 12; i++)
							createCell(sheet, r, i, "", bodycs);
						for (MonthMDFeeBean f : fee)
                            if(f.getMdDeviationRate() != null)
                                createCell(sheet, r, f.getMonth(), Double.toString(f.getMdDeviationRate()) + "%", bodycs);
                            else
                                createCell(sheet, r, f.getMonth(), "", bodycs);
						
						rowIndex++;
						r = ExcelUtil.createRow(sheet, rowIndex, 360);
						createCell(sheet, r, 0, "需量电费", bodycs);
						for (int i = 1; i <= 12; i++)
							createCell(sheet, r, i, "", bodycs);
						for (MonthMDFeeBean f : fee)
                            if(f.getMdFee() != null)
                                createCell(sheet, r, f.getMonth(), Double.toString(f.getMdFee()), bodycs);
                            else
                                createCell(sheet, r, f.getMonth(), "", bodycs);
					}
				}
			}
		}

		ExcelUtil.setColumnWidth(sheet, cellNum, 4500);

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
		OutputStream out = response.getOutputStream();
		wb.write(out);
	}
	
	private void createCell(HSSFSheet sheet, Row r, int cellNo, String value, CellStyle bodycs) {
		Cell c = r.createCell(cellNo);
		// 设置单元格值
		c.setCellValue(value);

		// 设置内容单元格样式
		c.setCellStyle(ExcelUtil.createBodyCellStyle(bodycs));
	}
	
	/**
	 * 取上个月电费
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/preMonthEleFee", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getPreMonthEleFee(HttpServletRequest request) throws IOException {
		return costService.calEmoDcpPreMonthEleFee(jacksonUtils.readJSON2Map(request.getParameter("pageInfo")));
	}

    /**
     * 移峰填谷 -- 记录用户使用记录
     */
    @RequestMapping(value = "/addUserRecord")
    public @ResponseBody void addUserRecord(HttpServletRequest request,HttpServletResponse response) {
        //记录用户足迹
        this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_65, 42L, 1);
    }

}
