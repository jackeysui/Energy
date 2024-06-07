package com.linyang.energy.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;


import org.apache.poi.hssf.usermodel.HSSFCell;     
import org.apache.poi.hssf.usermodel.HSSFCellStyle;         
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;     
import org.apache.poi.hssf.usermodel.HSSFSheet;     
import org.apache.poi.hssf.usermodel.HSSFWorkbook;     
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.leegern.util.DateUtil;
import com.leegern.util.StringUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.ChartItemWithTime;
import com.linyang.energy.dto.TimeEnum;
import com.linyang.energy.mapping.energydataquery.DayHarMapper;
import com.linyang.energy.service.DayHarService;
import com.linyang.energy.utils.ExcelUtil;

/**
  @description: 日谐波数据查询业务逻辑层接口实现类
  @version:南投项目
  @author:周礼
  @date:2014.08.28	13:36:56
 */
@Service
public class DayHarServiceImpl implements DayHarService {
	@Autowired
	private DayHarMapper DayHarMapper;

	//查询日谐波柱状图数据，返回list
	public List<Map<String,Object>> getDayHarChartDatas(Map<String,Object> queryMap){
		Date endTime = ( Date )queryMap.get( "Time" ) ;
		Calendar cal_ = new GregorianCalendar( ) ;
		cal_.setTime( endTime ) ;
		cal_.add( Calendar.DAY_OF_MONTH , 1 ) ;	
		cal_.add( Calendar.SECOND , -1 ) ;	
		endTime =  cal_.getTime( ) ;
		queryMap.put( "endTime" , endTime ) ;
		List<Map<String,Object>> list = DayHarMapper.getDayHarChartDatas(queryMap);
		return fixData(list) ;
	}
	
	//查询畸变柱状图数据，返回list
	public List<Map<String,Object>> getDayDisChartDatas(Map<String,Object> queryMap){
		List<Map<String,Object>> list =  null; if (DayHarMapper.getDayDisChartDatas(queryMap)!=null){list=DayHarMapper.getDayDisChartDatas(queryMap);} 
		if (list == null ||list.size() == 0) {
			Map<String,Object> map = new HashMap<String,Object>( ) ;
			map.put( "A_MAX" , 0.00001 ) ;
			map.put( "B_MAX" , 0.00001 ) ;
			map.put( "C_MAX" , 0.00001 ) ;
		    if(null != list)list.add(map);
		}
		return list;
	}

	//查询日谐波报表数据，返回list
	public List<Map<String,Object>> getDayHarReportDatas(Map<String,Object> queryMap){
		List<Map<String,Object>> list = DayHarMapper.getDayHarReportDatas(queryMap);
		list = fixData( list ) ;
		return list;
	}
	
	//为了方便制表
	//谐波次数为空的位置
	//同样插入一个谐波次数的map
	//但其A,B,C的max全为0.00001  updated by james 20141127
	private List<Map<String,Object>> fixData( List<Map<String,Object>> list )
	{
		List<Map<String,Object>> fixedList = new ArrayList<Map<String,Object>>( ) ;
		for(int i=1;i<=10;i++)
		{
			int num=2*i+1;
            if(i == 10){
                num = 18;//21次取18次谐波数据
            }
			Map<String,Object> item=FindSpecialNumData(list,num);
			if(item!=null)
			{
				fixedList.add(item);
			}
			else
			{
				Map<String,Object> emptyCount = new HashMap<String,Object>( ) ;
				emptyCount.put( "A_MAX" , 0.00001 ) ;
				emptyCount.put( "B_MAX" , 0.00001 ) ;
				emptyCount.put( "C_MAX" , 0.00001 ) ;
				emptyCount.put( "A_MAXTIME" , null ) ;
				emptyCount.put( "B_MAXTIME" , null ) ;
				emptyCount.put( "C_MAXTIME" , null ) ;
				emptyCount.put( "NUM" , num ) ;
				fixedList.add( emptyCount ) ;
			}
			
		}
		
		return fixedList ;
	}
	
	private Map<String,Object> FindSpecialNumData(List<Map<String,Object>> list,int num)
	{
		Map<String,Object> result=null;
		for(int i=0;i<list.size();i++)
		{
			if(((java.math.BigDecimal)list.get( i ).get( "NUM" )).intValue( ) == num)
			{
				result=list.get( i );
			}
		}	
		
		return result;
	}

	//查询日谐波畸变报表数据，返回list
	public List<Map<String,Object>> getDayDisReportDatas(Map<String,Object> queryMap){
		List<Map<String,Object>> list = DayHarMapper.getDayDisReportDatas(queryMap);
		
			Map<String,Object> map = new HashMap<String,Object>( ) ;
			map.put( "A_MAX" , 0.00001 ) ;
			map.put( "B_MAX" , 0.00001 ) ;
			map.put( "C_MAX" , 0.00001 ) ;
			map.put( "A_MAXTIME" , null ) ;
			map.put( "B_MAXTIME" , null ) ;
			map.put( "C_MAXTIME" , null ) ;
			if(list!=null)
				list.add(map);
		
		return list;
	}

	//导出到Excel
	public void getHarExcel(String sheetName, OutputStream output,Map<String,Object> map,Map<String,Object> queryMap){
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		//定义一个表格
		HSSFSheet sheet = wb.createSheet("日谐波数据");
		sheet.setColumnWidth((short)1,(short)14*256);
		sheet.setColumnWidth((short)3,(short)14*256);
		sheet.setColumnWidth((short)5,(short)14*256);
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

		
		try
		{
			int curveType = Integer.parseInt( ( (String)queryMap.get("curveType") ) );
			String A = "";
			String B = "";
			String C = "";
			switch( curveType ) {
			case 1:
				A = "A相谐波电流";
				B = "B相谐波电流";
				C = "C相谐波电流";
				break;
			case 2:
				A = "A相谐波电压含有率";
				B = "B相谐波电压含有率";
				C = "C相谐波电压含有率";
				break;
			}
			//增加用户名
			String userName = queryMap.get("userName").toString();
			String dateName = queryMap.get("dateName").toString();
			HSSFRow nameRow = sheet.createRow(0);
			nameRow.createCell(0).setCellValue(URLDecoder.decode(userName,"UTF-8"));
			nameRow.createCell(5).setCellValue(URLDecoder.decode(dateName,"UTF-8"));
			sheet.addMergedRegion(new Region(0, (short) 5, 0, (short) 6));//日期
			
			//生成表头
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell cellA = row1.createCell(0);
			HSSFCell cellB = row1.createCell(1);
			HSSFCell cellC = row1.createCell(2);
			HSSFCell cellD = row1.createCell(3);
			HSSFCell cellE = row1.createCell(4);
			HSSFCell cellF = row1.createCell(5);
			HSSFCell cellG = row1.createCell(6);
	
			HSSFRow row2 = sheet.createRow(2);
			HSSFCell cella = row2.createCell(0);
			HSSFCell cellb = row2.createCell(1);
			HSSFCell cellc = row2.createCell(2);
			HSSFCell celld = row2.createCell(3);
			HSSFCell celle = row2.createCell(4);
			HSSFCell cellf = row2.createCell(5);
			HSSFCell cellg = row2.createCell(6);
	
	
			cellA.setCellStyle(titlestyle);
			cellB.setCellStyle(titlestyle);	
			cellC.setCellStyle(titlestyle);
			cellD.setCellStyle(titlestyle);
			cellE.setCellStyle(titlestyle);	
			cellF.setCellStyle(titlestyle);
			cellG.setCellStyle(titlestyle);
			cella.setCellStyle(titlestyle);	
			cellb.setCellStyle(titlestyle);
			cellc.setCellStyle(titlestyle);
			celld.setCellStyle(titlestyle);	
			celle.setCellStyle(titlestyle);
			cellf.setCellStyle(titlestyle);	
			cellg.setCellStyle(titlestyle);
	
			cellA.setCellValue("谐波次数");
			cellB.setCellValue(A);
			cellC.setCellValue("");
			cellD.setCellValue(B);
			cellE.setCellValue("");
			cellF.setCellValue(C);
			cellG.setCellValue("");
			
			cella.setCellValue("");
			if( curveType == 1 )
			{
				cellb.setCellValue("日最大值(A)");
				cellc.setCellValue("发生时间");
				celld.setCellValue("日最大值(A)");
				celle.setCellValue("发生时间");
				cellf.setCellValue("日最大值(A)");
				cellg.setCellValue("发生时间");
			}
			else if( curveType ==2 )
			{
				cellb.setCellValue("日最大值(%)");
				cellc.setCellValue("发生时间");
				celld.setCellValue("日最大值(%)");
				celle.setCellValue("发生时间");
				cellf.setCellValue("日最大值(%)");
				cellg.setCellValue("发生时间");
			}
			sheet.addMergedRegion(new Region(1, (short) 0, 2, (short) 0));//谐波次数
			sheet.addMergedRegion(new Region(1, (short) 1, 1, (short) 2));//A项谐波电流
			sheet.addMergedRegion(new Region(1, (short) 3, 1, (short) 4));//B项谐波电流
			sheet.addMergedRegion(new Region(1, (short) 5, 1, (short) 6));//C项谐波电流
			//表头结束
			
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
			
			//日谐波数据Har
			List<Map<String, Object>> Har = (List<Map<String, Object>>)map.get("Har");
			//日谐波畸变数据Har
			List<Map<String, Object>> Dis = (List<Map<String, Object>>)map.get("Dis");
			
			
			
			int lastRow = 0 ;
			
			for( int i = 0 ; i < Har.size( ) ; i ++ )
			{
				HSSFRow row = sheet.createRow(i+3);
				HSSFCell cell1A = row.createCell(0);
				HSSFCell cell1B = row.createCell(1);
				HSSFCell cell1C = row.createCell(2);
				HSSFCell cell1D = row.createCell(3);
				HSSFCell cell1E = row.createCell(4);
				HSSFCell cell1F = row.createCell(5);
				HSSFCell cell1G = row.createCell(6);
				
				cell1A.setCellStyle(style_);
				cell1B.setCellStyle(style_);
				cell1C.setCellStyle(style_);
				cell1D.setCellStyle(style_);
				cell1E.setCellStyle(style_);
				cell1F.setCellStyle(style_);
				cell1G.setCellStyle(style_);
				
                int num = Integer.parseInt(Har.get( i ).get( "NUM" ).toString());
                if(num == 18){
                    num = 21;
                }
				if( new BigDecimal(Double.parseDouble( ( Har.get( i ).get( "A_MAX" ) ).toString() )).compareTo(new BigDecimal(0.00001)) ==  0)
				{
					cell1A.setCellValue( num + "次" ) ;
					cell1B.setCellValue( "" ) ;
					cell1C.setCellValue( "" ) ;
					cell1D.setCellValue( "" ) ;
					cell1E.setCellValue( "" ) ;
					cell1F.setCellValue( "" ) ;
					cell1G.setCellValue( "" ) ;
					
				}
				else
				{
					cell1A.setCellValue( num + "次" ) ;
					cell1B.setCellValue( ((BigDecimal)Har.get( i ).get( "A_MAX" ))==null?"":Har.get( i ).get( "A_MAX" ).toString() ) ;
					cell1C.setCellValue( Har.get( i ).get( "A_MAXTIME" )==null?"":Har.get( i ).get( "A_MAXTIME" ).toString() ) ;
					cell1D.setCellValue( ((BigDecimal)Har.get( i ).get( "B_MAX" ))==null?"":Har.get( i ).get( "B_MAX" ).toString() ) ;
					cell1E.setCellValue( Har.get( i ).get( "B_MAXTIME" )==null?"":Har.get( i ).get( "B_MAXTIME" ).toString()  ) ;
					cell1F.setCellValue( ((BigDecimal)Har.get( i ).get( "C_MAX" ))==null?"":Har.get( i ).get( "C_MAX" ).toString() ) ;
					cell1G.setCellValue( Har.get( i ).get( "C_MAXTIME" )==null?"":Har.get( i ).get( "C_MAXTIME" ).toString() ) ;
				}
				lastRow = i ;
			}
			lastRow ++ ;
			
			HSSFRow totalRow = sheet.createRow(lastRow+3);
			HSSFCell cell2A = totalRow.createCell(0);
			HSSFCell cell2B = totalRow.createCell(1);
			HSSFCell cell2C = totalRow.createCell(2);
			HSSFCell cell2D = totalRow.createCell(3);
			HSSFCell cell2E = totalRow.createCell(4);
			HSSFCell cell2F = totalRow.createCell(5);
			HSSFCell cell2G = totalRow.createCell(6);
			cell2A.setCellStyle(style_);
			cell2B.setCellStyle(style_);
			cell2C.setCellStyle(style_);
			cell2D.setCellStyle(style_);
			cell2E.setCellStyle(style_);
			cell2F.setCellStyle(style_);
			cell2G.setCellStyle(style_);
			
			if( Dis.size( ) == 0 )
			{
				cell2A.setCellValue( "总畸变" ) ;
				cell2B.setCellValue( "" ) ;
				cell2C.setCellValue( "" ) ;
				cell2D.setCellValue( "" ) ;
				cell2E.setCellValue( "" ) ;
				cell2F.setCellValue( "" ) ;
				cell2G.setCellValue( "" ) ;
			}
			else
			{
				cell2A.setCellValue( "总畸变" ) ;
				cell2B.setCellValue( Dis.get( 0 ).get( "A_MAX" ) + "" ) ;
				cell2C.setCellValue( Dis.get( 0 ).get( "A_MAXTIME" )==null?"":Dis.get( 0 ).get( "A_MAXTIME" ).toString() ) ;
				cell2D.setCellValue( Dis.get( 0 ).get( "B_MAX" ) + "" ) ;
				cell2E.setCellValue( Dis.get( 0 ).get( "B_MAXTIME" )==null?"":Dis.get( 0 ).get( "B_MAXTIME" ).toString() ) ;
				cell2F.setCellValue( Dis.get( 0 ).get( "C_MAX" ) + "" ) ;
				cell2G.setCellValue( Dis.get( 0 ).get( "C_MAXTIME" )==null?"":Dis.get( 0 ).get( "C_MAXTIME" ).toString()) ;
			}
			

			
		}
		catch(UnsupportedEncodingException e)
		{
			Log.info("getHarExcel error NumberFormatException");
		}
		try {
			output.flush();
			wb.write(output);
			output.close();
		} catch (IOException e) {
			/*System.out.println("Output is closed ");*/ Log.info("Output is closed");
		}
	}
	
	/**
	 * 选择EMO，得到总表个数
	 */
	@Override
	public int getNum(Map<String, Object> params) {
		int num = DayHarMapper.getNum(params);
		return num;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCurChartData(Map<String, Object> param) throws ParseException {
		Map<String,Object> result = new HashMap<String, Object>();
		boolean hasData = false;
		List<Map<String,Object>> list = DayHarMapper.getCurChartData(param);
		//有数据
		if(list != null && list.size() > 0){
			hasData = true;
			
			Date beginDate = (Date) param.get("Time");
			Date endDate = (Date) param.get("endTime");
			
			//图表初始化，填充初始值
			ChartItemWithTime chartItem = new ChartItemWithTime(TimeEnum.DAYMINUTE, "", beginDate.getTime()/1000, endDate.getTime()/1000, null);
			SortedMap<String, Object> chartMap = chartItem.getMap();
			
			if(list != null && list.size() > 0){
				for (Map<String, Object> map : list) {
					Date freezeTime = (Date) map.get("FREEZE_TIME");
					String freezeStr = DateUtil.convertDateToStr(freezeTime, "HH:mm");
					Map<String, Object> item;
					if(chartMap.get(freezeStr) == null){
						item = new HashMap<String, Object>();
						item.put("har", '-');
						item.put("fund", '-');
					}else {
						item = (Map<String, Object>) chartMap.get(freezeStr);
					}
					if(map.get("T").toString().equals("HAR")){
						item.put("har", map.get("VAL"));
					}else if(map.get("T").toString().equals("FUND")){
						item.put("fund", map.get("VAL"));
					}
					chartMap.put(freezeStr, item);
				}
			}
			List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>();
			Iterator<Entry<String, Object>> it = chartMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				Map<String, Object> entryMap = new HashMap<String, Object>();
				entryMap.put("time", entry.getKey());
				Map<String, Object> item = (Map<String, Object>) entry.getValue();
				if(item == null){
					entryMap.put("har", "-");
					entryMap.put("fund", "-");
				}else {

                    if(!item.get("har").toString().equals("-")){
                        BigDecimal har =  (BigDecimal) item.get("har");
                        BigDecimal nHar = har.setScale(2, RoundingMode.DOWN);
                        entryMap.put("har",nHar.doubleValue());
                    }
                    else{
                        entryMap.put("har","-");
                    }
                    if(!item.get("fund").toString().equals("-")){
                        BigDecimal fund =  (BigDecimal) item.get("fund");
                        BigDecimal nFund = fund.setScale(2, RoundingMode.DOWN);
                        entryMap.put("fund", nFund.doubleValue());
                    }
                    else{
                        entryMap.put("fund","-");
                    }
				}

				
				list2.add(entryMap);
			}
			result.put("list", list2);
		}
		result.put("hasData", hasData);
		return result;
	}

	/**
	 * 导出曲线谐波有功功率，曲线基波有功功率
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void exportExcel(String string, ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param, String title) throws UnsupportedEncodingException {
		List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
		
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet=null;
		try{
		sheet = wb.createSheet(URLDecoder.decode(title,"UTF-8"));}
		catch(UnsupportedEncodingException ex){Log.error("EncodingException"+ex.getMessage());}
		int colCount = 97;

		//设置列宽
		for (int i = 0; i < 97; i++) {
			int width = 256;
			if(i == 0 || i== 1){
				width = 256 * 2;
			}
			sheet.setColumnWidth(i,12*width);
		}
		
		//数据总数量
		int dataSize = list.size();
		
		//总行数.列表头，谐波总，ABC;基波总,ABC;
		int dataRow = 3;
		
		HSSFRow[] rows = new HSSFRow[dataRow];
		//定义表头风格
		HSSFCellStyle thStyle = ExcelUtil.setTdStyle(wb);
	
		//第1行
		rows[0] = sheet.createRow(0);
		ExcelUtil.createCell(0, colCount, rows[0], thStyle);
		//合并单元格-----采集点/能管单元
		rows[0].getCell(0).setCellValue("采集点");
		rows[0].getCell(1).setCellValue("数据项");
		
		
		//时间 从0:00到23:45分
		for (int i = 0; i < 96; i++) {
			String timeStr = "";
			if( i % 4 == 0){
				timeStr = ":00";
			}else if(i % 4 == 1){
				timeStr = ":15";
			}else if(i % 4 == 2){
				timeStr = ":30";
			}else if(i % 4 == 3){
				timeStr = ":45";
			}
			rows[0].getCell(i+2).setCellValue( i/4 +""+ timeStr );
		}
		
		//定义数据样式
		HSSFCellStyle tdStyle = ExcelUtil.setTdStyle(wb);
		
		//从第2行开始
		int j = 1;
		for (int i = 0; i < dataSize; i++) {
			Map<String, Object> map = list.get(i);
			//一天里的第一个数据
			if(i%96 == 0){
				for(int m=0; m<2;m++){
					rows[j+m] = sheet.createRow(j+m);
					ExcelUtil.createCell(0, colCount, rows[j+m], tdStyle);
				}
				sheet.addMergedRegion(new CellRangeAddress(j, j+1, 0, 0));//合并单元格---具体的对象
		        rows[j].getCell(0).setCellValue(param.get("meterName").toString());
		        rows[j].getCell(1).setCellValue("谐波总有功功率");
//		        rows[j+1].getCell(1).setCellValue("谐波A相有功功率");
//		        rows[j+2].getCell(1).setCellValue("谐波B相有功功率");
//		        rows[j+3].getCell(1).setCellValue("谐波C相有功功率");
		        rows[j+1].getCell(1).setCellValue("基波总有功功率");
//		        rows[j+5].getCell(1).setCellValue("基波A相有功功率");
//		        rows[j+6].getCell(1).setCellValue("基波B相有功功率");
//		        rows[j+7].getCell(1).setCellValue("基波C相有功功率");
			}
			
			rows[j].getCell(i%96+2).setCellValue(map.get("har").toString()); 
//			rows[j+1].getCell(i%96+2).setCellValue(map.get("hara").toString());
//			rows[j+2].getCell(i%96+2).setCellValue(map.get("harb").toString());
//			rows[j+3].getCell(i%96+2).setCellValue(map.get("harc").toString());
			rows[j+1].getCell(i%96+2).setCellValue(map.get("fund").toString());
//			rows[j+5].getCell(i%96+2).setCellValue(map.get("funda").toString());
//			rows[j+6].getCell(i%96+2).setCellValue(map.get("fundb").toString());
//			rows[j+7].getCell(i%96+2).setCellValue(map.get("fundc").toString());
		}
		try {
			outputStream.flush();
			wb.write(outputStream);
			outputStream.close();
		} catch (IOException e) {
			Log.error(this.getClass() + ".exportExcel()--无法导出为excel");
		}
	}

	
	/**
	 * 得到谐波电流电压曲线数据
	 * @throws ParseException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getHarIVData(Map<String, Object> param) throws ParseException {
		Map<String,Object> result = new HashMap<String, Object>();
		boolean hasData = false;
		//谐波次数
		Integer curTimes = (Integer) param.get("curTimes");
		
		List<Map<String, Object>> list = this.DayHarMapper.getHarIVData(param);
		//有数据
		if(list != null && list.size() > 0){

			hasData = true;
			//谐波次数
			Object  harTimes = list.get(0).get("HAR_VA_RATE_22_51");
			if(harTimes == null){
				//选择的谐波次数比实际的高
				result.put("error", 1);
			}
			
			Date beginDate = (Date) param.get("Time");
			Date endDate = (Date) param.get("endTime");
			
			ChartItemWithTime chartItem = new ChartItemWithTime(TimeEnum.DAYMINUTE, "", beginDate.getTime()/1000, endDate.getTime()/1000, null);
			SortedMap<String, Object> chartMap = chartItem.getMap();
			
			//存放最大值的list
			List<Map<String, Double>> maxList = new ArrayList<Map<String,Double>>();
			if(curTimes != null){
				initList(maxList,21);
			}else {
				initList(maxList,51);
			}
			
			
			if(list != null && list.size() > 0){
				for (Map<String, Object> map : list) {
					//1-获取曲线数据
					Date freezeTime = (Date) map.get("FREEZE_TIME");
					String freezeStr = DateUtil.convertDateToStr(freezeTime, "HH:mm");
					Map<String, Object> item;
					if(chartMap.get(freezeStr) == null){
						item = new HashMap<String, Object>();
						item.put("VA", '-');
						item.put("VB", '-');
						item.put("VC", '-');
					}else {
						item = (Map<String, Object>) chartMap.get(freezeStr);
					}

					//A相数据
					BigDecimal va = (BigDecimal) map.get("HAR_VA_RATE");
					String harVa = (String) map.get("HAR_VA_RATE_02_21");
					String harVa51 = (String) map.get("HAR_VA_RATE_22_51"); 
					
					addMaxList(harTimes, maxList, va, harVa, harVa51,"A");
					
					
					
					//B相数据
					BigDecimal vb = (BigDecimal) map.get("HAR_VB_RATE");
					String harVb = (String) map.get("HAR_VB_RATE_02_21");
					String harVb51 = (String) map.get("HAR_VB_RATE_22_51");
					
					addMaxList(harTimes, maxList, vb, harVb, harVb51,"B");
					
					//C相数据
					BigDecimal vc = (BigDecimal) map.get("HAR_VC_RATE");
					String harVc = (String) map.get("HAR_VC_RATE_02_21");
					String harVc51 = (String) map.get("HAR_VC_RATE_22_51");
					
					addMaxList(harTimes, maxList, vc, harVc, harVc51,"C");
					
					//总
					if(curTimes == 0){
						item.put("VA", va);
						item.put("VB", vb);
						item.put("VC", vc);
					}
					//2-21次
					else if(curTimes > 0 && curTimes < 22){
						item.put("VA", harVa.split(",")[curTimes-2]);
						item.put("VB", harVb.split(",")[curTimes-2]);
						item.put("VC", harVc.split(",")[curTimes-2]);
					}
					//22-51次
					else if(curTimes > 21){
						item.put("VA", harVa51.split(",")[curTimes-23]);
						item.put("VB", harVb51.split(",")[curTimes-23]);
						item.put("VC", harVc51.split(",")[curTimes-23]);
					}
					chartMap.put(freezeStr, item);
				}
			}
			
			List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>();
			Iterator<Entry<String, Object>> it = chartMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				Map<String, Object> entryMap = new HashMap<String, Object>();
				entryMap.put("time", entry.getKey());
				Map<String, Object> item = (Map<String, Object>) entry.getValue();

				
				
				
				if(null !=item ){
					entryMap.put("VA", item.get("VA"));
					entryMap.put("VB", item.get("VB"));
					entryMap.put("VC", item.get("VC"));
					list2.add(entryMap);
				}
			
			}
			List<Double> vList = new ArrayList<Double>();
			List<String> xList = new ArrayList<String>();
			//找出最大值，并且知道是那一相
			for (Map<String, Double> map2 : maxList) {
				Iterator<Entry<String, Double>> ite = map2.entrySet().iterator();
				double maxV = 0;
				String maxX = "A";
				while (ite.hasNext()) {
					Entry<String, Double> e = ite.next();
					if(e.getValue() > maxV){
						maxV = e.getValue();
						maxX = e.getKey();
					}
				}
				vList.add(maxV);
				xList.add(maxX);
			}
			
			int[] index = new int[3];
			
			for (int i = 0; i < 3; ++i) {
				if(!indexOf(index, i) && i != 0){
					index[i] = i+1;
				}else {
					index[i] = i;
				}
				
				for (int j = 0; j < vList.size(); ++j) {
					if (vList.get(index[i]) < vList.get(j) && indexOf(index, j)) {
						index[i] = j;
					}
				}
			}
			
			result.put("times", index);
			double[] maxValue = new double[3];
			maxValue[0] = vList.get(index[0]);
			maxValue[1] = vList.get(index[1]);
			maxValue[2] = vList.get(index[2]);
			result.put("maxValue", maxValue);
			String[] maxX = new String[3];
			maxX[0] = xList.get(index[0]);
			maxX[1] = xList.get(index[1]);
			maxX[2] = xList.get(index[2]);
			result.put("maxX", maxX);
			//没有错误
			result.put("error", 0);
			result.put("list", list2);
		}
		result.put("hasData", hasData);
		return result;
	}
	
	public boolean indexOf(int[] num, int j) {
		for (int i = 0; i < num.length; ++i) {
			if (num[i] == j) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containItem(int[] num, int j) {
		for (int i = 0; i < num.length; ++i) {
			if (num[i] == j) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 给list赋值
	 * @param harTimes
	 * @param maxList
	 * @param va
	 * @param harVa
	 * @param harVa51
	 * @param x
	 */
	private void addMaxList(Object harTimes, List<Map<String, Double>> maxList,
			BigDecimal va, String harVa, String harVa51, String x) {
		double aa = maxList.get(0).get(x);
		if(va.doubleValue() > aa){
			maxList.get(0).put(x, va.doubleValue());
		}
		if(harVa != null ){
			String[] harVaAry = harVa.split(",");
			for (int i=0;i<harVaAry.length;i++) {
				String val = harVaAry[i];
				if(StringUtil.isNotEmpty(val.trim())){
					double a2 = Double.parseDouble(val.trim());
					double aa2 = maxList.get(i+1).get(x);
					if(a2 > aa2){
						maxList.get(i+1).put(x, a2);
					}
				}
			}
		}
		if(harTimes != null){
			String[] harVaAry51 = harVa51.split(",");
			for (int i=0;i<harVaAry51.length;i++) {
				String val = harVaAry51[i];
				if(StringUtil.isNotEmpty(val.trim())){
					double a2 = Double.parseDouble(val.trim());
					double aa2 = maxList.get(i+1).get(x);
					if(a2 > aa2){
						maxList.get(i+1).put(x, a2);
					}
				}
			}
		}
	}

	/**
	 * list初始化
	 * @param maxList
	 * @param size
	 */
	private void initList(List<Map<String, Double>> maxList,int size) {
		for(int i =0;i<size;i++){
			Map<String,Double> map = new HashMap<String, Double>();
			map.put("A", 0d);
			map.put("B", 0d);
			map.put("C", 0d);
			maxList.add(map);
		}
	}
}
