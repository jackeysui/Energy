package com.linyang.energy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List; import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.linyang.common.web.common.Log;

/**
 * @Description Excel读、写辅助类
 * @author Leegern
 * @date 2013-8-1 下午08:44:54
 */
public class ExcelUtil {

	/**
	 * Excel 2003
	 */
	private final static String XLS = "xls";
	/**
	 * Excel 2007
	 */
	private final static String XLSX = "xlsx";
	
	
	/**
	 * 将HSSFWorkbook写入Excel文件
	 * @param 	wb		HSSFWorkbook
	 * @param 	absPath	写入文件的相对路径
	 * @param 	wbName	文件名
	 */
	public static void writeWorkbook(HSSFWorkbook wb, String fileName){
		FileOutputStream fos = null;
		try {
			fos=new FileOutputStream(fileName);
			wb.write(fos);
		} catch (FileNotFoundException e) {
			Log.error("ExcelUtil.writeWorkbook -- 文件未找到!");
		} catch (IOException e) {
			Log.error("ExcelUtil.writeWorkbook -- IO出错!");
		} finally{
			try {
				if(fos!=null){
					fos.close();
				}
			} catch (IOException e) {
				Log.error("ExcelUtil.writeWorkbook -- 关闭流失败!");
			}
		}
	}
	
	/**
	 * 创建HSSFWorkbook对象
	 * @return
	 */
	public static HSSFWorkbook createWorkbook(){
		
		return new HSSFWorkbook();
	}
	
	/**
	 * 创建HSSFSheet对象
	 * @param 	wb	        HSSFWorkbook对象
	 * @param 	sheetName	sheet名称
	 * @return	HSSFSheet
	 */
	public static HSSFSheet createSheet(HSSFWorkbook wb, String sheetName){
		HSSFSheet sheet = wb.createSheet(sheetName);
		sheet.setDefaultColumnWidth(62);
		sheet.setGridsPrinted(false);
		sheet.setDisplayGridlines(false);
		return sheet;
	}
	/**
	 * 创建HSSFRow
	 * @param 	sheet	HSSFSheet
	 * @param 	rowNum	int
	 * @param 	height	int
	 * @return	HSSFRow
	 */
	public static HSSFRow createRow(HSSFSheet sheet,int rowNum,int height){
		HSSFRow row=sheet.createRow(rowNum);
		row.setHeight((short)height);
		return row;
	}
	
	/**
	 * 创建表头CellStyle样式
	 * @param 	wb	  HSSFWorkbook对象
	 * @param   cs    cellStyle实例	
	 * @return
	 */
	public static CellStyle createHeadCellStyle(HSSFWorkbook wb, CellStyle cs){
		cs.setBorderBottom(CellStyle.BORDER_THIN); // 设置border
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);  // 设置水平对齐
		Font f = wb.createFont();                 // 设置字体
		f.setBoldweight(Font.BOLDWEIGHT_BOLD);
		cs.setFont(f);
		return cs;
	}
	
	/**
	 * 创建表格内容CellStyle样式
	 * @param cs  cellStyle实例
	 * @return
	 */
	public static CellStyle createBodyCellStyle(CellStyle cs){
		cs.setBorderBottom(CellStyle.BORDER_THIN);  // 设置border
		cs.setBorderLeft(CellStyle.BORDER_THIN);
		cs.setBorderRight(CellStyle.BORDER_THIN);
		cs.setBorderTop(CellStyle.BORDER_THIN);
		cs.setAlignment(CellStyle.ALIGN_CENTER);   // 设置水平对齐
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 设置垂直对齐
		cs.setWrapText(false);               // 设置是否自动换行
		return cs;
	}
	
	/**
	 * 创建带边框的CellStyle样式
	 * @param 	cs				cellStyle实例
	 * @param 	backgroundColor	背景色	
	 * @param 	foregroundColor	前置色
	 * @param	font			字体
	 * @return	CellStyle
	 */
	public static CellStyle createBorderCellStyle(CellStyle cs, short backgroundColor, short foregroundColor,short halign,Font font){
		cs.setAlignment(halign);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs.setFillBackgroundColor(backgroundColor);
		cs.setFillForegroundColor(foregroundColor);
		cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cs.setFont(font);
		cs.setBorderLeft(CellStyle.BORDER_DASHED);
		cs.setBorderRight(CellStyle.BORDER_DASHED);
		cs.setBorderTop(CellStyle.BORDER_DASHED);
		cs.setBorderBottom(CellStyle.BORDER_DASHED);  
		return cs;
	}
	
	/**
	 * 创建CELL
	 * @param 	row		HSSFRow	
	 * @param 	cellNum	int
	 * @param 	style	HSSFStyle
	 * @return	HSSFCell
	 */
	public static HSSFCell createCell(HSSFRow row, int cellNum, CellStyle style){
		HSSFCell cell=row.createCell(cellNum);
		cell.setCellStyle(style);
		return cell;
	}
	
	/**
	 * 合并单元格
	 * @param 	sheet		HSSFSheet
	 * @param 	firstRow	int
	 * @param 	lastRow		int
	 * @param 	firstColumn	int
	 * @param 	lastColumn	int
	 * @return	int			合并区域号码
	 */
	public static int mergeCell(HSSFSheet sheet,int firstRow,int lastRow,int firstColumn,int lastColumn){
		return sheet.addMergedRegion(new CellRangeAddress(firstRow,lastRow,firstColumn,lastColumn));	
	}
	
	/**
	 * 创建字体
	 * @param 	wb			HSSFWorkbook	
	 * @param 	boldweight	short
	 * @param 	color		short
	 * @return	Font	
	 */
	public static Font createFont(HSSFWorkbook wb,short boldweight,short color,short size){
		Font font=wb.createFont();
		font.setBoldweight(boldweight);
		font.setColor(color);
		font.setFontHeightInPoints(size);
		return font;
	}
	
	/**
	 * 设置合并单元格的边框样式
	 * @param	sheet	HSSFSheet	
	 * @param 	ca		CellRangAddress
	 * @param 	style	CellStyle
	 */
	public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress ca,CellStyle style) {  
	    for (int i = ca.getFirstRow(); i <= ca.getLastRow(); i++) {  
	        HSSFRow row = HSSFCellUtil.getRow(i, sheet);  
	        for (int j = ca.getFirstColumn(); j <= ca.getLastColumn(); j++) {  
	            HSSFCell cell = HSSFCellUtil.getCell(row, j);  
	            cell.setCellStyle(style);  
	        }  
	    }  
	}  
	
	/**
	 * 设置列属性为下拉列表
	 * @param sheet     HSSFSheet对象 
	 * @param items     下拉列表值
	 * @param firstRow  起始行数
	 * @param lastRow   截止行数
	 * @param firstCol  起始列数
	 * @param lastCol   截止列数
	 */
	public static void setSelectedItem(HSSFSheet sheet, String[] items, int firstRow, int lastRow, int firstCol, int lastCol){
		//生成下拉列表  
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);  
        //生成下拉框内容  
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(items);  
        //绑定下拉框和作用区域  
        HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);  
        //对sheet页生效  
        sheet.addValidationData(data_validation);  
	}
	
	/**
	 * 导入Excel指定的Sheet数据
	 * @param file      Excel文件
	 * @param sheetNum  Sheet序号(从0开始)
	 * @return 
	 */
	public static List<Object[]> importExcel(File file, int sheetNum) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        try {return importExcel(fileInputStream, FilenameUtils.getExtension(file.getName()), sheetNum);} catch(IOException e){Log.error("ExcelUtil importExcel IO error!");return null;} 
        finally{CloseHander(fileInputStream);}
	}
	
	private static void CloseHander(FileInputStream fileInputStream){
		try
		{if(fileInputStream != null){fileInputStream.close();}}
		catch(IOException ex){Log.error("Close Hander"+ex.getMessage());}
	}

	/**
	 * 导入Excel指定的Sheet数据
	 * @param is            文件输入流
	 * @param extensionName 文件后缀名
	 * @param sheetNum      Sheet序号(从0开始)
	 * @return
	 * @throws IOException
	 */
	public static List<Object[]> importExcel(InputStream is, String extensionName, int sheetNum) throws IOException {
		Workbook workbook = null;
		//判断2003还是2007
		if (extensionName.toLowerCase(Locale.ENGLISH).equals(XLS)) {
			workbook = new HSSFWorkbook(is);
		} else if (extensionName.toLowerCase(Locale.ENGLISH).equals(XLSX)) {
			workbook = new XSSFWorkbook(is);
		}
		return importExcelToList(workbook, sheetNum);
	}

	/**
	 * 由指定的Sheet导出至List
	 * @param workbook 
	 * @param sheetNum
	 * @return
	 * @throws IOException
	 */
	private static List<Object[]> importExcelToList(Workbook workbook, int sheetNum) {
		List<Object[]> list = new ArrayList<Object[]>();
		Sheet sheet = workbook.getSheetAt(sheetNum);
		// 公式解析结果
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		int minRowIx = sheet.getFirstRowNum();
		int maxRowIx = sheet.getLastRowNum();
		for (int rowIx = minRowIx; rowIx <= maxRowIx; rowIx++) {
			Row row = sheet.getRow(rowIx);
			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			Object[] objs = new Object[maxColIx];
			for (short colIx = minColIx; colIx <= maxColIx; colIx++) {
				Object obj = null;
				Cell cell = row.getCell(new Integer(colIx));
				CellValue cellValue = evaluator.evaluate(cell);
				if (null == cellValue ) {
					continue;
				}
				// 公式解析后，只存在Boolean、Numeric和String三种数据类型，此外就是Error了
				switch (cellValue.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					obj = cellValue.getBooleanValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					// 这里的日期类型会被转换为数字类型，需要判别后区分处理
					if (DateUtil.isCellDateFormatted(cell)) {
						obj = cell.getDateCellValue();
					} else {
						// 格式化Double
						DecimalFormat df = new DecimalFormat("0");  
						obj = df.format(cellValue.getNumberValue());
					}
					break;
				case Cell.CELL_TYPE_STRING:
					obj = cellValue.getStringValue();
					break;
				case Cell.CELL_TYPE_FORMULA:
					break;
				case Cell.CELL_TYPE_BLANK:
					break;
				case Cell.CELL_TYPE_ERROR:
					break;
				default:
					break;
				}
				objs[colIx] = obj;
			}
			//排除全是空的行
			boolean flag = false;
			for (int i=0; i < objs.length; i++) {
				if (null != objs[i]) {
					flag = true;
					break;
				}
			}
			if (flag) {
				list.add(objs);
			}
		}
		return list;
	}
	
	/**
	 * 设置所有列为自动调整列宽
	 * 
	 * @param sheet
	 * @param colNum
	 */
	public static void setAutoSizeColumn(HSSFSheet sheet, int colNum) {
		for (int i = 0; i < colNum; i++)
			sheet.autoSizeColumn(i);
	}
	
	/**
	 * 设置所有列的列宽
	 * 
	 * @param sheet
	 * @param colNum
	 */
	public static void setColumnWidth(HSSFSheet sheet, int colNum, int width) {
		for (int i = 0; i < colNum; i++)
			sheet.setColumnWidth(i, width);
	}
	
	/**
	 * 定义数据样式
	 * @param wb
	 * @return
	 */
	public static HSSFCellStyle setTdStyle(HSSFWorkbook wb){
		//定义具体表格内容样式
	    HSSFCellStyle style_ =wb.createCellStyle();
	    HSSFDataFormat df = wb.createDataFormat();
	    style_.setDataFormat(df.getFormat("0.00###"));//支持5位
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
	    return style_;
	}
	
	/**  
     * 生成单元格
     * @param sheet  
     * @param region  
     * @param cs  
     */    
	public static void createCell(int start, int end, HSSFRow row,    HSSFCellStyle style) {    
        for(int i=start;i<=end;i++){       
            HSSFCell cell = row.createCell(i);       
            cell.setCellValue("");       
            cell.setCellStyle(style);       
        }    
    }  
	
}