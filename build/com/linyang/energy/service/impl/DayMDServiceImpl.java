package com.linyang.energy.service.impl;

import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.mapping.authmanager.MeterBeanMapper;
import com.linyang.energy.mapping.demanddeclare.DemandDeclareMapper;
import com.linyang.energy.mapping.energysavinganalysis.CostMapper;
import com.linyang.energy.mapping.energysavinganalysis.DayMDMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.CurveBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.DayMDService;
import com.linyang.energy.utils.DataUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 日需量分析
 * @author guosen
 * @date 2014-12-23
 */
@Service
public class DayMDServiceImpl implements DayMDService {

	@Autowired
	private DayMDMapper dayMDMapper;
	@Autowired
	private DemandDeclareMapper demandDeclareMapper;
	@Autowired
	private MeterBeanMapper meterBeanMapper;
	@Autowired
	private CostMapper costMapper;
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;

	@Override
	public List<CurveBean> getDayMDData(Map<String, Object> params) {
		int treeType = (Integer) params.get("treeType");
		Long meterId = (Long)params.get("meterId");

		List<CurveBean> list = this.dayMDMapper.queryDayMDData(params);
		if(treeType == 1){
			List<Long> pointIds = costMapper.getAllottedPointIdsByLedgerId(meterId);
			if(pointIds.size() == 1){//分户下只配置一个测量点，取该测量点的信息
				Map<String, Object> tempLastMap = new HashMap<String, Object>();
				tempLastMap.put("meterId",pointIds.get(0));
				tempLastMap.put("beginTime",params.get("beginTime"));
				tempLastMap.put("endTime",params.get("endTime"));
				tempLastMap.put("treeType",2);
				list = dayMDMapper.queryDayMDData(tempLastMap);
			}
		}

		List<CurveBean> chartData = new ArrayList<CurveBean>();
		if(list != null && list.size()>0){//加入申报需量
			Double demandThres = dayMDMapper.getDemandThres(treeType,meterId);//得到该计量点所在分户所绑定的费率需量阀值

			double thres = 0;
			if(demandThres != null){
				thres = DataUtil.doubleDivide(demandThres, 100);
			}
			Map<Date,Double> map = new HashMap<Date, Double>();
			Date freezeTimeTemp = new Date();
			for (CurveBean curveBean : list) {

				Date freezeTime = curveBean.getFreezeTime();
				if(freezeTimeTemp.equals(freezeTime)){
					continue;
				}
				freezeTimeTemp = freezeTime;
				double max = curveBean.getMax();
				Date beginTime = DateUtil.getMonthFirstDay(freezeTime);
				beginTime = DateUtil.clearDate(beginTime);
				if(!map.containsKey(beginTime)){//是否查询过申报量
					Double declareValue = null;

					Map<String, Object> declare = null;
					if (this.demandDeclareMapper.getDeclareValue(meterId, treeType, beginTime) != null) {
						declare = this.demandDeclareMapper.getDeclareValue(meterId, treeType, beginTime);
					}
					if(declare != null){
						declareValue = Double.parseDouble(declare.get("DECLARE_VALUE").toString());
						String type = declare.get("DECLARE_TYPE").toString();
						if(type.equals("1")){//变压器容量申报类型;1,容量;2,需量
							declareValue = DataUtil.doubleMultiply(declareValue, 0.75);
						}
					}

					map.put(beginTime, declareValue);
				}
				Double md = map.get(beginTime);
				if(md != null && max > BigDecimal.ONE.add(new BigDecimal(thres)).multiply(new BigDecimal(md)).doubleValue() ){//最大需量超过需量申报值*（1+超罚限值）的用红色显示
					curveBean.setHasOver(true);
				}
				curveBean.setMd(md);
				chartData.add(curveBean);
			}
			return chartData;
		}
		return list;
	}

//	private void getLogicLedgerBean(long ledgerId){
//		List<LedgerTreeBean> ledgerTreeBeanList = ledgerManagerMapper.getChildLedgerTree(ledgerId);
//		boolean flag = false;
//		for (LedgerTreeBean ledgerTreeBean : ledgerTreeBeanList) {
//			if (ledgerTreeBean.getTreeNodeType() == 2) {
//				ledgerIdlist.add(ledgerId);
//				flag = true;
//				break;
//			}
//		}
//		if (!flag) {
//			for (LedgerTreeBean ledgerTreeBean : ledgerTreeBeanList) {
//				this.getLogicLedgerBean(Long.parseLong(ledgerTreeBean.getId()));
//			}
//		}
//	}

	@Override
	public void exportExcel(ServletOutputStream outputStream, List<CurveBean> list,int objType) {
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();

		//定义一个表格
		HSSFSheet sheet = wb.createSheet("日需量分析");
		sheet.setColumnWidth((short)0,(short)24*256);
		sheet.setColumnWidth((short)1,(short)24*256);
		sheet.setColumnWidth((short)2,(short)24*256);
		sheet.setColumnWidth((short)3,(short)24*256);

		//定义整体风格
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		//定义标题风格
		HSSFCellStyle titlestyle = wb.createCellStyle();
		titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titlestyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		titlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
		titlestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		HSSFFont font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		titlestyle.setFont(font);

		//定义具体表格内容样式
		HSSFCellStyle style_ =wb.createCellStyle();
		style_.setRightBorderColor(HSSFColor.BLACK.index);
		style_.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style_.setLeftBorderColor(HSSFColor.BLACK.index);
		style_.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style_.setTopBorderColor(HSSFColor.BLACK.index);
		style_.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style_.setBottomBorderColor(HSSFColor.BLACK.index);
		style_.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style_.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style_.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

		//定义红色字体样式
		HSSFCellStyle redfontstyle = wb.createCellStyle();
		redfontstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		redfontstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		redfontstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		redfontstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		redfontstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		redfontstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		font = wb.createFont();
		font.setColor(HSSFColor.RED.index);
		redfontstyle.setFont(font);


		try{
			HSSFRow trRow = sheet.createRow(0);
			HSSFCell cellA = trRow.createCell(0);
			HSSFCell cellB = trRow.createCell(1);
			HSSFCell cellC = trRow.createCell(2);
			HSSFCell cellD = trRow.createCell(3);
			cellA.setCellStyle(titlestyle);
			cellB.setCellStyle(titlestyle);
			cellC.setCellStyle(titlestyle);
			cellD.setCellStyle(titlestyle);
			cellA.setCellValue("变压器");
			cellB.setCellValue("日期");
			cellC.setCellValue("最大需量（kW）");
			cellD.setCellValue("发生时间");

			int rowIndex = 1;
			String meterName = "";
			for (int i = 0; i < list.size(); i++) {
				CurveBean curveBean = list.get(i);
				if(i == 0){
					if(objType == 1){
						LedgerBean lBean = ledgerManagerMapper.selectByLedgerId(curveBean.getMeterId());
						meterName = lBean.getLedgerName();
					}else{
						MeterBean meterBean = this.meterBeanMapper.selectByPrimaryKey(curveBean.getMeterId());
						meterName = meterBean.getMeterName();
					}
				}
				HSSFRow tdrow = sheet.createRow(rowIndex++);
				HSSFCell cell1A = tdrow.createCell(0);
				HSSFCell cell1B = tdrow.createCell(1);
				HSSFCell cell1C = tdrow.createCell(2);
				HSSFCell cell1D = tdrow.createCell(3);

				cell1A.setCellStyle(style_);
				cell1B.setCellStyle(style_);
				if(curveBean.isHasOver()){//最大需量超过需量申报值*（1+超罚限值）的用红色显示
					cell1C.setCellStyle(redfontstyle);
				}else {
					cell1C.setCellStyle(style_);
				}
				cell1D.setCellStyle(style_);

				cell1A.setCellValue(meterName);
				cell1B.setCellValue(DateUtil.convertDateToStr(curveBean.getFreezeTime(), DateUtil.DEFAULT_SHORT_PATTERN));
				cell1C.setCellValue(curveBean.getMax());
				cell1D.setCellValue(DateUtil.convertDateToStr(curveBean.getMaxTime(), "MM-dd HH:mm"));
			}

			outputStream.flush();
			wb.write(outputStream);
			outputStream.close();
		}
		catch(ParseException e ){
			Log.info("exportExcel error ParseException");
		}
		catch(IOException e ){
			Log.info("exportExcel error IOException");
		}

	}

	@Override
	public Long getLedgerVolumeByLedgerId(long ledgerId) {
		return dayMDMapper.getLedgerVolumeByLedgerId(ledgerId);
	}
}
