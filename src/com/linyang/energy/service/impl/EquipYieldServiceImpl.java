package com.linyang.energy.service.impl;

import com.linyang.energy.mapping.equipYield.EquipYieldMapper;
import com.linyang.energy.mapping.phone.PhoneMapper;
import com.linyang.energy.service.EquipYieldService;
import com.linyang.energy.utils.DateUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：${date} ${time}
 * @ Description：企业产能申报
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class EquipYieldServiceImpl implements EquipYieldService {
	
	@Autowired
	private EquipYieldMapper equipYieldMapper;
	
	@Autowired
	private PhoneMapper phoneMapper;
	
	@Override
	public List<Map<String, Object>> queryTableHead(Map<String, Object> params) {
		return equipYieldMapper.queryTableHead( params );
	}
	
	/**
	 * 查询企业产能申报列表数据
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> queryEquipList(Map<String, Object> params) {
		Map<String,Object> result = new HashMap<>( 0 );
		Map<String,Object> paramMap = new HashMap<>( 0 );
		List<Long> meterIds = equipYieldMapper.getMeterIdsByLedgerId( params );
		paramMap.put( "beginDate",params.get( "beginDate" ) );
		paramMap.put( "endDate",params.get( "endDate" ) );
		paramMap.put( "ledgerId",params.get( "ledgerId" ) );
		for (Long meterId : meterIds) {
			paramMap.put( "meterId",meterId );
			List<Map<String, Object>> maps = equipYieldMapper.queryEquipList( paramMap );
			if ( maps.size() > 0 ) {
				result.put( meterId+"",maps );
			} else {
				result.put( meterId+"","" );
			}
		}
		return result;
	}
	
	/**
	 * 导出excel模板文件
	 * @param name
	 * @param outputStream
	 * @param params
	 */
	@Override
	public void getExcelTemplate(String name, ServletOutputStream outputStream, Map<String, Object> params) {
		
		HSSFWorkbook workbook = new HSSFWorkbook(  );
		
		// 设置字体
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		// 字体大小
		headfont.setFontHeightInPoints((short) 10);
		// 加粗
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		//头部样式
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		// 左右居中
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 上下居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headstyle.setLocked(true);
		// 自动换行
		headstyle.setWrapText(true);
		headstyle.setBorderLeft((short) 1);
		headstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		headstyle.setRightBorderColor(HSSFColor.BLACK.index);
		headstyle.setBottomBorderColor(HSSFColor.BLACK.index);
		headstyle.setBorderBottom((short) 1);
		headstyle.setBorderRight((short) 1);
		
		// 加粗字体样式
		HSSFFont columnHeadFont = workbook.createFont();
		columnHeadFont.setFontName("宋体");
		columnHeadFont.setFontHeightInPoints((short) 11);
		columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		// 列头的样式
		HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
		columnHeadStyle.setFont(columnHeadFont);
		columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		columnHeadStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		columnHeadStyle.setLocked(true);
		columnHeadStyle.setWrapText(true);
		columnHeadStyle.setTopBorderColor(HSSFColor.BLACK.index);
		columnHeadStyle.setBorderTop((short) 1);
		columnHeadStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		columnHeadStyle.setBorderLeft((short) 1);
		columnHeadStyle.setRightBorderColor(HSSFColor.BLACK.index);
		columnHeadStyle.setBorderRight((short) 1);
		columnHeadStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		columnHeadStyle.setBorderBottom((short) 1);
		
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 9);
		
		/*内容居左单元格样式*/
		HSSFCellStyle leftstyle = workbook.createCellStyle();
		leftstyle.setFont(font);
		leftstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		leftstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		leftstyle.setWrapText(true);
		leftstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		leftstyle.setBorderLeft((short) 1);
		leftstyle.setTopBorderColor(HSSFColor.BLACK.index);
		leftstyle.setBorderTop((short) 1);
		leftstyle.setRightBorderColor(HSSFColor.BLACK.index);
		leftstyle.setBorderRight((short) 1);
		leftstyle.setBottomBorderColor(HSSFColor.BLACK.index);
		leftstyle.setBorderBottom((short) 1);
		leftstyle.setFillForegroundColor(HSSFColor.WHITE.index);
		
		// 查询到表头的时间数据
		List<Map<String, Object>> headDatas = equipYieldMapper.queryTableHead( params );
		
		// 查询到所有终端信息
		List<Map<String, Object>> meters = equipYieldMapper.queryMeterDataByLedgerId( params );
		
		HSSFSheet sheet = workbook.createSheet("企业产能模板");
		sheet.setColumnWidth( 0,4200 );
		
		//合并行
		CellRangeAddress range = null;
		
		// 标题行
		HSSFRow row = sheet.createRow( 0 );
		
		HSSFCell hssfCell = row.createCell( 0 );
		hssfCell.setCellStyle( headstyle );
		hssfCell.setCellValue( "测量点" );
		range = new CellRangeAddress(0, 1, 0,0);
		sheet.addMergedRegion(range);
		
		//设置单元格表头(时间)
		for (int i = 0; i < headDatas.size(); i++) {
			HSSFCell cell = row.createCell( i * 4 + 1 );
			cell.setCellStyle( headstyle );
			cell.setCellValue( headDatas.get( i ).get( "FREEZE_TIME" ).toString().substring( 0,11 ) );
			HSSFCell cell1 = row.createCell( i * 4 + 2 );
			cell1.setCellStyle( headstyle );
			HSSFCell cell2 = row.createCell( i * 4 + 3 );
			cell2.setCellStyle( headstyle );
			HSSFCell cell3 = row.createCell( i * 4 + 4 );
			cell3.setCellStyle( headstyle );
			range = new CellRangeAddress(0, 0, i*4+1,i*4+4);
			sheet.addMergedRegion(range);
		}
		
		//设置单元格表头
		row = sheet.createRow( 1 );
		for (int i = 0; i < headDatas.size(); i++) {
			HSSFCell cell = row.createCell( i * 4 + 1 );
			cell.setCellStyle( headstyle );
			cell.setCellValue( "96#" );
			HSSFCell cell1 = row.createCell( i * 4 + 2 );
			cell1.setCellStyle( headstyle );
			cell1.setCellValue( "97#" );
			HSSFCell cell2 = row.createCell( i * 4 + 3 );
			cell2.setCellStyle( headstyle );
			cell2.setCellValue( "98#" );
			HSSFCell cell3 = row.createCell( i * 4 + 4 );
			cell3.setCellStyle( headstyle );
			cell3.setCellValue( "其他" );
		}
		
		//设置单元格左侧测量点列表
		for (int i = 0; i < meters.size(); i++) {
			row = sheet.createRow( i+2 );
			HSSFCell cell = row.createCell( 0 );
			cell.setCellStyle(columnHeadStyle);
			cell.setCellValue( meters.get( i ).get( "meterName" ).toString() );
		}
		
//		this.setCellStyle(0,0,);
		
		try {
			outputStream.flush();
			workbook.write(outputStream);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把模板数据插入数据库
	 * @param datas
	 * @return
	 */
	@Override
	public boolean insertExcelData(List<Object[]> datas) {
		List<Map<String, Object>> maps = assemblyData( datas );
		boolean flag = true;
		try {
			for (Map<String,Object> map:maps) {
				Integer integer = phoneMapper.declarePorduction( map );
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	/**
	 * 组装需要插入的数据
	 * @param datas
	 * @return
	 */
	private List<Map<String,Object>> assemblyData(List<Object[]> datas){
		List<Map<String,Object>> dataList = new ArrayList<>( 0 );
		Map<String,Object> map = null;
		List<String> dates = new ArrayList<>( 0 );
		
		Object[] obj = datas.get( 0 );
		for (int i = 1; i < obj.length; i+=4) {
			if (obj != null) {
				dates.add( obj[i].toString() );
			}
		}
		
		for (int i = 2; i < datas.size(); i++) {
			Object[] obj1 = datas.get( i );
			Map<String, Object> meter = equipYieldMapper.queryMeterDataByMeterName( obj1[0].toString() );
			for (int j = 0; j < dates.size(); j++) {
				map = new HashMap<>( 0 );
				Double sum = 0.0;
				map.put( "meterId",meter.get( "meterId" ) );
				map.put( "ledgerId",meter.get( "ledgerId" ) );
				map.put( "dataDate",DateUtil.convertStrToDate( dates.get( j )+"00:00:00" ) );
//				map.put( "yield96",0 );
				if ( (j*4+1) < obj1.length && obj1[j*4+1] != null ) {
					map.put( "yield96",obj1[j*4+1] );
					sum += Double.parseDouble( obj1[j*4+1].toString() );
				}
//				map.put( "yield97",0 );
				if ( (j*4+2) < obj1.length && obj1[j*4+2] != null ) {
					map.put( "yield97",obj1[j*4+2] );
					sum += Double.parseDouble( obj1[j*4+2].toString() );
				}
//				map.put( "yield98",0 );
				if ( (j*4+3) < obj1.length && obj1[j*4+3] != null ) {
					map.put( "yield98",obj1[j*4+3] );
					sum += Double.parseDouble( obj1[j*4+3].toString() );
				}
//				map.put( "yieldOther",0 );
				if ( (j*4+4) < obj1.length && obj1[j*4+4] != null ) {
					map.put( "yieldOther",obj1[j*4+4] );
					sum += Double.parseDouble( obj1[j*4+4].toString() );
				}
				map.put( "yieldTotal",sum );
				map.put( "chgTime",DateUtil.convertStrToDate( DateUtil.getCurrentDateStr() ) );
				if ( (j*4+4) < obj1.length ) {
					dataList.add( map );
				}
			}
		}
		return dataList;
	}
	
	
	/**
	 * 导出excel
	 * @param name
	 * @param outputStream
	 * @param params
	 */
	@Override
	public void getExcel(String name, ServletOutputStream outputStream, Map<String, Object> params) {
		
		HSSFWorkbook workbook = new HSSFWorkbook(  );
		
		// 设置字体
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("宋体");
		// 字体大小
		headfont.setFontHeightInPoints((short) 10);
		// 加粗
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		//头部样式
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		// 左右居中
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 上下居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		headstyle.setLocked(true);
		// 自动换行
		headstyle.setWrapText(true);
		headstyle.setBorderLeft((short) 1);
		headstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		headstyle.setRightBorderColor(HSSFColor.BLACK.index);
		headstyle.setBottomBorderColor(HSSFColor.BLACK.index);
		headstyle.setBorderBottom((short) 1);
		headstyle.setBorderRight((short) 1);
		
		// 加粗字体样式
		HSSFFont columnHeadFont = workbook.createFont();
		columnHeadFont.setFontName("宋体");
		columnHeadFont.setFontHeightInPoints((short) 11);
		columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		// 列头的样式
		HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
		columnHeadStyle.setFont(columnHeadFont);
		columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		columnHeadStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		columnHeadStyle.setLocked(true);
		columnHeadStyle.setWrapText(true);
		columnHeadStyle.setTopBorderColor(HSSFColor.BLACK.index);
		columnHeadStyle.setBorderTop((short) 1);
		columnHeadStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		columnHeadStyle.setBorderLeft((short) 1);
		columnHeadStyle.setRightBorderColor(HSSFColor.BLACK.index);
		columnHeadStyle.setBorderRight((short) 1);
		columnHeadStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		columnHeadStyle.setBorderBottom((short) 1);
		
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 9);
		
		/*内容居左单元格样式*/
		HSSFCellStyle leftstyle = workbook.createCellStyle();
		leftstyle.setFont(font);
		leftstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		leftstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		leftstyle.setWrapText(true);
		leftstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		leftstyle.setBorderLeft((short) 1);
		leftstyle.setTopBorderColor(HSSFColor.BLACK.index);
		leftstyle.setBorderTop((short) 1);
		leftstyle.setRightBorderColor(HSSFColor.BLACK.index);
		leftstyle.setBorderRight((short) 1);
		leftstyle.setBottomBorderColor(HSSFColor.BLACK.index);
		leftstyle.setBorderBottom((short) 1);
		leftstyle.setFillForegroundColor(HSSFColor.WHITE.index);
		
		// 查询到表头的时间数据
		List<Map<String, Object>> headDatas = equipYieldMapper.queryTableHead( params );
		
		// 查询到所有终端信息
		List<Map<String, Object>> meters = equipYieldMapper.queryMeterDataByLedgerId( params );
		
		HSSFSheet sheet = workbook.createSheet("企业产能模板");
		sheet.setColumnWidth( 0,4200 );
		
		//合并行
		CellRangeAddress range = null;
		
		// 标题行
		HSSFRow row = sheet.createRow( 0 );
		
		HSSFCell hssfCell = row.createCell( 0 );
		hssfCell.setCellStyle( headstyle );
		hssfCell.setCellValue( "测量点" );
		range = new CellRangeAddress(0, 1, 0,0);
		sheet.addMergedRegion(range);
		
		//设置单元格表头(时间)
		for (int i = 0; i < headDatas.size(); i++) {
			HSSFCell cell = row.createCell( i * 4 + 1 );
			cell.setCellStyle( headstyle );
			cell.setCellValue( headDatas.get( i ).get( "FREEZE_TIME" ).toString().substring( 0,11 ) );
			HSSFCell cell1 = row.createCell( i * 4 + 2 );
			cell1.setCellStyle( headstyle );
			HSSFCell cell2 = row.createCell( i * 4 + 3 );
			cell2.setCellStyle( headstyle );
			HSSFCell cell3 = row.createCell( i * 4 + 4 );
			cell3.setCellStyle( headstyle );
			range = new CellRangeAddress(0, 0, i*4+1,i*4+4);
			sheet.addMergedRegion(range);
		}
		
		//设置单元格表头
		row = sheet.createRow( 1 );
		for (int i = 0; i < headDatas.size(); i++) {
			HSSFCell cell = row.createCell( i * 4 + 1 );
			cell.setCellStyle( headstyle );
			cell.setCellValue( "96#" );
			HSSFCell cell1 = row.createCell( i * 4 + 2 );
			cell1.setCellStyle( headstyle );
			cell1.setCellValue( "97#" );
			HSSFCell cell2 = row.createCell( i * 4 + 3 );
			cell2.setCellStyle( headstyle );
			cell2.setCellValue( "98#" );
			HSSFCell cell3 = row.createCell( i * 4 + 4 );
			cell3.setCellStyle( headstyle );
			cell3.setCellValue( "其他" );
		}
		
		Map<String,Object> dataParams = new HashMap<>( 0 );
		List<Map<String, Object>> maps = new ArrayList<>( 0 );
		//设置单元格左侧测量点列表
		for (int i = 0; i < meters.size(); i++) {
			row = sheet.createRow( i+2 );
			HSSFCell cell = row.createCell( 0 );
			cell.setCellStyle(columnHeadStyle);
			cell.setCellValue( meters.get( i ).get( "meterName" ).toString() );
			
			dataParams.put( "beginDate",params.get( "beginDate" ) );
			dataParams.put( "endDate",params.get( "endDate" ) );
			dataParams.put( "meterId",meters.get( i ).get( "meterId" ) );
			dataParams.put( "ledgerId",params.get( "ledgerId" ) );
			maps = equipYieldMapper.queryEquipList( dataParams );
			for (int j = 0; j < maps.size(); j++) {
				cell = row.createCell( j*4+1 );
				cell.setCellStyle(columnHeadStyle);
				if ( maps.get( j ).containsKey( "yield96" ) ) {
					cell.setCellValue( maps.get( j ).get( "yield96" ).toString() );
				} else {
					cell.setCellValue( "" );
				}
				
				cell = row.createCell( j*4+2 );
				cell.setCellStyle(columnHeadStyle);
				if ( maps.get( j ).containsKey( "yield97" ) ) {
					cell.setCellValue( maps.get( j ).get( "yield97" ).toString() );
				} else {
					cell.setCellValue( "" );
				}
				
				cell = row.createCell( j*4+3 );
				cell.setCellStyle(columnHeadStyle);
				if ( maps.get( j ).containsKey( "yield98" ) ) {
					cell.setCellValue( maps.get( j ).get( "yield98" ).toString() );
				} else {
					cell.setCellValue( "" );
				}
				
				cell = row.createCell( j*4+4 );
				cell.setCellStyle(columnHeadStyle);
				if ( maps.get( j ).containsKey( "yieldOther" ) ) {
					cell.setCellValue( maps.get( j ).get( "yieldOther" ).toString() );
				} else {
					cell.setCellValue( "" );
				}
			}
		}
		
		try {
			outputStream.flush();
			workbook.write(outputStream);
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
