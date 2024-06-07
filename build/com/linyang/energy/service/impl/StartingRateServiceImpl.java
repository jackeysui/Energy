package com.linyang.energy.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.mapping.authmanager.MeterBeanMapper;
import com.linyang.energy.mapping.energydataquery.StartingRateMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.StartrateBean;
import com.linyang.energy.service.StartingRateService;
import com.linyang.energy.utils.DateUtil;

@Service
public class StartingRateServiceImpl implements StartingRateService {

	@Autowired
	private StartingRateMapper startingRateMapper;

	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	
	@Autowired
	private MeterBeanMapper meterBeanMapper;

	@Override
	public List<StartrateBean> queryStartRateData(Map<String, Object> param) {
        List<String> meterIdList = null;
        if(param.get("meterIds").toString() != null && param.get("meterIds").toString().length()>0){
            String[] meterIds = param.get("meterIds").toString().split(",");
            meterIdList = new ArrayList<String>();
            Collections.addAll(meterIdList, meterIds);
        }
		Date beginTime = (Date) param.get("beginTime");
		Date beginTime2 = DateUtil.getDateBetween(beginTime, -1);
		Date endTime = (Date) param.get("endTime");
		Date endTime2 = (Date) param.get("endTime2");
		
		int days = DateUtil.daysBetweenDates(endTime, beginTime);

		List<StartrateBean> result = startingRateMapper.queryStartRateData( (days+1) * 24 * 60, meterIdList, beginTime, endTime, beginTime2, endTime2);
		return result;
	}

	@Override
	public void optExcel(ServletOutputStream outputStream, List<StartrateBean> result, Map<String, Object> param) {
		// 声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();

		// 定义一个表格
		HSSFSheet sheet = wb.createSheet("开机率");
		sheet.setColumnWidth((short) 0, (short) 24 * 256);
		sheet.setColumnWidth((short) 1, (short) 24 * 256);
		sheet.setColumnWidth((short) 2, (short) 24 * 256);
		sheet.setColumnWidth((short) 3, (short) 24 * 256);
		sheet.setColumnWidth((short) 4, (short) 24 * 256);
		sheet.setColumnWidth((short) 5, (short) 24 * 256);
		sheet.setColumnWidth((short) 6, (short) 24 * 256);
		sheet.setColumnWidth((short) 7, (short) 24 * 256);
		sheet.setColumnWidth((short) 8, (short) 24 * 256);

		// 定义整体风格
		HSSFCellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		// 定义标题风格
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

		// 定义具体表格内容样式
		HSSFCellStyle style_ = wb.createCellStyle();
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

		// 定义表名的字体样式
		HSSFCellStyle trstyle = wb.createCellStyle();
		HSSFFont trfont = wb.createFont();
		trfont.setColor(HSSFColor.BLACK.index);
		trfont.setFontName("黑体");
		trfont.setFontHeightInPoints((short) 14);// 设置字体大小
		trstyle.setFont(trfont);
		trstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		trstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		try {
			// 行号
			int rowIndex = 0;
			// 第一行----时间选择
			HSSFRow nameRow = sheet.createRow(rowIndex++);
			nameRow.createCell(0).setCellValue("起始时间");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date beginTime = (Date) param.get("beginTime");
			Date endTime = (Date) param.get("endTime");
            nameRow.createCell(1).setCellValue(URLDecoder.decode(sdf.format(beginTime),"UTF-8"));
			nameRow.createCell(2).setCellValue("结束时间");
            nameRow.createCell(3).setCellValue(URLDecoder.decode(sdf.format(endTime),"UTF-8"));

			// 分户总数据
			rowIndex = bulidExcel("总", sheet, style_, titlestyle, trstyle, result, rowIndex);

			outputStream.flush();
			wb.write(outputStream);
			outputStream.close();
		} 
		catch (UnsupportedEncodingException e) {
			Log.info("optExcel error UnsupportedEncodingException");
		}
		catch(IOException e){
			Log.info("optExcel error IOException");
		}  
	}

	/**
	 * 数据拼接
	 * 
	 * @param type
	 * @param sheet
	 * @param style_
	 * @param titlestyle
	 * @param trstyle
	 * @param list
	 * @param rowIndex
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private int bulidExcel(String type, HSSFSheet sheet, HSSFCellStyle style_, HSSFCellStyle titlestyle, HSSFCellStyle trstyle,
			List<StartrateBean> list, int rowIndex) throws UnsupportedEncodingException {
		// 表格表头--- 能源管理单元、名称、总（kWh）、、尖（kWh）、峰（kWh）、平（kWh）、谷（kWh）、开机时长（h）、开机率（%）
		HSSFRow trRow = sheet.createRow(rowIndex++);

		HSSFCell cellA = trRow.createCell(0);
		HSSFCell cellB = trRow.createCell(1);
		HSSFCell cellD = trRow.createCell(2);
		HSSFCell cellE = trRow.createCell(3);
		HSSFCell cellF = trRow.createCell(4);
		HSSFCell cellG = trRow.createCell(5);
		HSSFCell cellH = trRow.createCell(6);
		HSSFCell cellI = trRow.createCell(7);
		HSSFCell cellJ = trRow.createCell(8);

		cellA.setCellStyle(titlestyle);
		cellB.setCellStyle(titlestyle);
		cellD.setCellStyle(titlestyle);
		cellE.setCellStyle(titlestyle);
		cellF.setCellStyle(titlestyle);
		cellG.setCellStyle(titlestyle);
		cellH.setCellStyle(titlestyle);
		cellI.setCellStyle(titlestyle);
		cellJ.setCellStyle(titlestyle);

		cellA.setCellValue("能源管理单元");
		cellB.setCellValue("名称");
		cellD.setCellValue("总（kWh）");
		cellE.setCellValue("尖（kWh）");
		cellF.setCellValue("峰（kWh）");
		cellG.setCellValue("平（kWh）");
		cellH.setCellValue("谷（kWh）");
		cellI.setCellValue("开机时长（h）");
		cellJ.setCellValue("开机率（%）");

		// 加载数据
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				StartrateBean map = list.get(i);
				HSSFRow row1 = sheet.createRow(rowIndex++);
				HSSFCell cell1A = row1.createCell(0);
				HSSFCell cell1B = row1.createCell(1);
				HSSFCell cell1D = row1.createCell(2);
				HSSFCell cell1E = row1.createCell(3);
				HSSFCell cell1F = row1.createCell(4);
				HSSFCell cell1G = row1.createCell(5);
				HSSFCell cell1H = row1.createCell(6);
				HSSFCell cell1I = row1.createCell(7);
				HSSFCell cell1J = row1.createCell(8);

				cell1A.setCellStyle(style_);
				cell1B.setCellStyle(style_);
				cell1D.setCellStyle(style_);
				cell1E.setCellStyle(style_);
				cell1F.setCellStyle(style_);
				cell1G.setCellStyle(style_);
				cell1H.setCellStyle(style_);
				cell1I.setCellStyle(style_);
				cell1J.setCellStyle(style_);

				cell1A.setCellValue(checkIsNull(map.getLedgerName()));
				cell1B.setCellValue(checkIsNull(map.getMeterName()));
				cell1D.setCellValue(checkIsNull(map.getTotal()));
				cell1E.setCellValue(checkIsNull(map.getRate1()));
				cell1F.setCellValue(checkIsNull(map.getRate2()));
				cell1G.setCellValue(checkIsNull(map.getRate3()));
				cell1H.setCellValue(checkIsNull(map.getRate4()));
				cell1I.setCellValue(checkIsNull(map.getStartMin()));
				cell1J.setCellValue(checkIsNull(map.getStartRate()));
			}
		}
		return rowIndex;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param obj
	 * @return
	 */
	private String checkIsNull(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	@Override
	public List<LedgerTreeBean> getTree(Long ledgerId) {
		List<LedgerTreeBean> list = startingRateMapper.getTree(ledgerId);
		for (LedgerTreeBean ledgerTreeBean : list) {
			long parentLedgerId = Long.parseLong(ledgerTreeBean.getId());
			int count = ledgerManagerMapper.countLedgerByLedgerId(parentLedgerId);
			if(count <= 0)
				count = meterBeanMapper.countMeterByLedgerId(parentLedgerId);
			if(count >0)
				ledgerTreeBean.setIsParent(true);
		}
		return list;
	}
}
