package com.linyang.energy.service.impl;

import com.esotericsoftware.minlog.Log;
import com.linyang.energy.mapping.energydataquery.PollutVoltCurrPowerMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.CurveBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.PollutVoltCurrPowerService;
import com.linyang.energy.utils.DataUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 9:03 2018/12/17
 * @ Description：class说明
 * @ Modified By：:dingy
 * @Version: $version$
 */
@Service
public class PollutVoltCurrPowerServiceImpl implements PollutVoltCurrPowerService {
	
	@Autowired
	private PollutVoltCurrPowerMapper pollutVoltCurrPowerMapper;
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	@Autowired
	private MeterManagerMapper meterManagerMapper;
	
	
	
	@Override
	public List<CurveBean> queryPollutCur(Map<String, Object> param) {
		return pollutVoltCurrPowerMapper.queryPollutCur( param );
	}
	
	@Override
	public Map<String, Object> queryPolType(Long ledgerId) {
		return pollutVoltCurrPowerMapper.queryPolType( ledgerId );
	}
	
	@Override
	public Map<String, Object> queryFacilRelation(Long ledgerId) {
		Map<String, Object> relationMap = null;
		Map<String, Object> polMap = pollutVoltCurrPowerMapper.queryPolType( ledgerId );
		if(polMap == null)
			return null;
		
		Map<String,Object> param = new HashMap<String,Object>( 0 );
		
		param.put( "ledgerId",ledgerId);
		
		param.put( "LEDGER_ID_POLLUTCTL",0 );
		param.put( "LEDGER_ID_POLLUT",0 );
		
		if(polMap.containsKey( "POLLUTCTL_ID" )) {
			param.put( "type", 0 );
			param.put( "LEDGER_ID_POLLUTCTL",ledgerId );
		}
		if(polMap.containsKey( "POLLUT_ID" )) {
			param.put( "type", 1 );
			param.put( "LEDGER_ID_POLLUT",ledgerId );
		}
		relationMap =  pollutVoltCurrPowerMapper.queryFacilRelation( param );
		if(null != relationMap){
			relationMap.put( "type" , 2 );	//查询所有
		}
		return relationMap!=null?relationMap:param;
	}
	
	
	/**
	 * 得到Excel，数据填充
	 * @author
	 * @param 参数 table名字sheetName，输出流output，结果集list
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void getEleExcel(String sheetName, OutputStream output , Map<String, Object> result ) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet1 = wb.createSheet("报表");
		//设置默认单元格宽度
		sheet1.setDefaultColumnWidth(15);
		sheet1.setColumnWidth((short)0,(short)14*256);
		sheet1.setColumnWidth((short)2,(short)6*256);
		HSSFCellStyle style = wb.createCellStyle();
		HSSFDataFormat df = wb.createDataFormat();
		style.setDataFormat(df.getFormat("0.00###"));//支持5位
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);


		HSSFCellStyle titlestyle = wb.createCellStyle();
		titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titlestyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		titlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titlestyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		titlestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		titlestyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		titlestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		HSSFFont font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		titlestyle.setFont(font);

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

		HSSFCellStyle greenfontstyle = wb.createCellStyle();
		greenfontstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		greenfontstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		greenfontstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		greenfontstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		greenfontstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		greenfontstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		font = wb.createFont();
		font.setColor(HSSFColor.GREEN.index);
		greenfontstyle.setFont(font);

		Long objectId = (Long)result.get("objectId");
		Integer oType = (Integer)result.get("oType");
		String ledgerName = "";
		if(oType !=null && oType == 1){
			LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(objectId);
			ledgerName = ledger.getLedgerName();
		}else if(oType !=null && oType != 1){
			MeterBean meter = this.meterManagerMapper.getMeterDataByPrimaryKey(objectId);
			ledgerName = meter.getMeterName();
		}


		String title = "" ;
		int checkType = (Integer)result.get( "checkType" ) ;
		title = "曲线电量（kWh）";
			if( checkType == CurveBean.CURVE_TYPE_ELE ){
			List<CurveBean> tmp = ( List<CurveBean> )result.get( "chartData" ) ;

			sheet1.setColumnWidth((short)15,(short)15*256);
			List<Double> maxArr = new ArrayList<Double>( 0 );
			List<Double> minArr = new ArrayList<Double>( 0 );
				
			int size = tmp.size();
			if(size % 24 != 0){
				size = (size / 24 + 1 )*24  ;
			}

			Object[] rows = new Object[size*6/24 + 3];//数据行+表头行
			for( int i = 0 ; i < rows.length ; i ++ ){
				rows[i] = sheet1.createRow( i ) ;
			}

			((HSSFRow)rows[0]).createCell(0).setCellValue( "用户：" + ledgerName ) ;
			((HSSFRow)rows[1]).createCell(0).setCellValue( "日期" ) ;
			((HSSFRow)rows[1]).createCell(3).setCellValue( title ) ;
			((HSSFRow)rows[1]).getCell(0).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).getCell(3).setCellStyle(titlestyle);

			for( int i = 3 ; i < 15 ; i ++ ){
				((HSSFRow)rows[2]).createCell( i ).setCellValue( ( i - 2 ) + ":00" ) ;
				((HSSFRow)rows[2]).getCell(i).setCellStyle(titlestyle);
			}
			((HSSFRow)rows[2]).createCell(15).setCellValue( "合计" ) ;
			((HSSFRow)rows[2]).getCell(15).setCellStyle(titlestyle);

			for (int i = 1; i < 16; i++) {
				((HSSFRow)rows[1]).createCell(i).setCellStyle(titlestyle);
			}
			//Region(行,列,哪里开始,哪里结束)
			sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 2));
			sheet1.addMergedRegion(new Region(1, (short) 3, 1, (short) 15));
			
			
			//取最大值和最小值
			double maxA = 0,maxB = 0, maxC = 0;
			double minA = Double.MAX_VALUE ,minB = Double.MAX_VALUE,minC = Double.MAX_VALUE;
			for (int i = 0; i < tmp.size() ; i++){
				for (int j = 0; j < 3; j ++){
					
					if (j==0 && null != tmp.get( i ) && null != ((CurveBean)tmp.get( i )).getA() ) {
							if (new BigDecimal(((CurveBean)tmp.get( i )).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(maxA)) > 0)
								maxA = new BigDecimal(((CurveBean)tmp.get(i)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
							if (new BigDecimal(((CurveBean)tmp.get( i )).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(minA)) < 0)
								minA = new BigDecimal(((CurveBean)tmp.get(i)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
							if ( i != 0 && (i+1) % 24 == 0 ) {
								maxArr.add( maxA );
								minArr.add( minA );
								maxA = 0 ;
								minA = Double.MAX_VALUE;
							}
					}
					if (j==1 && null != tmp.get( i ) && null != ((CurveBean)tmp.get( i )).getB() ) {
						if (new BigDecimal(((CurveBean)tmp.get( i )).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(maxB)) > 0)
							maxB = new BigDecimal(((CurveBean)tmp.get(i)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
						if (new BigDecimal(((CurveBean)tmp.get( i )).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(minB)) < 0)
							minB = new BigDecimal(((CurveBean)tmp.get(i)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
						if (  i != 0 &&  (i+1) % 24 == 0  ) {
							maxArr.add( maxB );
							minArr.add( minB );
							maxB = 0;
							minB = Double.MAX_VALUE;
						}
					}
					if ( j == 2 && null != tmp.get( i ) && null != ((CurveBean)tmp.get( i )).getC() ) {
						if (new BigDecimal(((CurveBean)tmp.get( i )).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(maxC)) > 0)
							maxC = new BigDecimal(((CurveBean)tmp.get(i)).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
						if (new BigDecimal(((CurveBean)tmp.get( i )).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(minC)) < 0)
							minC = new BigDecimal(((CurveBean)tmp.get(i)).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
						if (  i != 0 &&  (i+1) % 24 == 0  ) {
							maxArr.add( maxC );
							minArr.add( minC );
							maxC = 0;
							minC = Double.MAX_VALUE;
						}
					}
				}
			}
				
				
			//放数据,并计算出合计
			double sumA = 0 , sumB = 0 , sumC = 0;
			int countA = 0,countB = 0,countC = 0;
			int indexA = 0,indexB = 1,indexC = 2;
			int ca = 0 , cb = 0, cc = 0;		//计数器
			for (int i = 3; i < size*6/24 + 3 ; i++) {
				for (int j = 3; j < 15; j++) {
					if(countA >= size)
						countA = 0;
					if(countB >= size)
						countB = 0;
					if(countC >= size)
						countC = 0;
					((HSSFRow)rows[i]).createCell(j).setCellStyle(style);
					if ( i % 6 == 3 || i % 6 == 4 ) {
						if(countA >= tmp.size() || DataUtil.parseDouble2BigDecimal(((CurveBean)tmp.get( countA )).getA()) == null ){
							((HSSFRow)rows[i]).getCell( j ).setCellValue( "-" ) ;
						} else {
							Double tempD = new BigDecimal(((CurveBean) tmp.get(countA)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
							((HSSFRow)rows[i]).getCell( j ).setCellValue( tempD ) ;
							sumA = DataUtil.doubleAdd(sumA, tempD);
							if(indexA < maxArr.size() &&  minArr.get( indexA ) != maxArr.get( indexA )){
								if (tempD.equals(maxArr.get( indexA ))){
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
									ca++;
								}
								if (tempD.equals(minArr.get( indexA ))){
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
									ca++;
								}
								if(ca == 2 && indexC < maxArr.size()){
									ca = 0 ;
									indexA +=3;
								}
							}
						}
						countA++;
					}
					if ( i % 6 == 5 || i % 6 == 0 ) {
						if(countB >= tmp.size() || DataUtil.parseDouble2BigDecimal(((CurveBean)tmp.get( countB )).getB()) == null ){
							((HSSFRow)rows[i]).getCell( j ).setCellValue( "-" ) ;
						} else {
							Double tempD = new BigDecimal(((CurveBean) tmp.get(countB)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
							((HSSFRow)rows[i]).getCell( j ).setCellValue( tempD ) ;
							sumB = DataUtil.doubleAdd(sumB, tempD);
							if(indexB < maxArr.size() &&  minArr.get( indexB ) != maxArr.get( indexB )){
								if (tempD.equals(maxArr.get( indexB ))){
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
									cb++;
								}
								if (tempD.equals(minArr.get( indexB ))){
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
									cb++;
								}
								if(cb == 2){
									cb = 0 ;
									indexB +=3;
								}
							}
						}
						countB++;
					}
					if ( i % 6 == 1 || i % 6 == 2) {
						if(countC >= tmp.size() || DataUtil.parseDouble2BigDecimal(((CurveBean)tmp.get( countC )).getC()) == null ){
							((HSSFRow)rows[i]).getCell( j ).setCellValue( "-" ) ;
						} else {
							Double tempD = new BigDecimal(((CurveBean) tmp.get(countC)).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
							((HSSFRow)rows[i]).getCell( j ).setCellValue( tempD ) ;
							sumC = DataUtil.doubleAdd(sumC, tempD);
							if(indexC < maxArr.size() && minArr.get( indexC ) != maxArr.get( indexC )){
								if (tempD.equals(maxArr.get( indexC ))){
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
									cc++;
								}
								if (tempD.equals(minArr.get( indexC ))){
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
									cc++;
								}
								if(cc == 2){
									cc = 0 ;
									indexC +=3;
								}
							}
						}
						countC++;
					}
					
					//字段生成
					if( i % 6 == 3 && j == 3 ) {
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "总" ) ;
						((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
					}
					
					if( i % 6 == 5 && j == 3 ) {
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "污染源" ) ;
						((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
					}
					if( i % 6 == 1 && j == 3 ) {
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "治理源" ) ;
						((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
					}
					
					if( i % 6 == 3 && j == 3 ){
						String sDate = ( new SimpleDateFormat("yyyy/MM/dd")).format( ((CurveBean)tmp.get( countA )).getFreezeTime() ) ;
						((HSSFRow)rows[i]).createCell( 0 ).setCellValue( sDate ) ;
						((HSSFRow)rows[i]).getCell(0).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+2]).createCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+3]).createCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+4]).createCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+5]).createCell( 0 ).setCellStyle(style);
					}
					
					
					//放合计的数据
					
					if ( i % 6 ==  4 && j == 14) {
						((HSSFRow)rows[i-1]).createCell(j+1).setCellStyle(style);
						((HSSFRow)rows[i]).createCell(j+1).setCellStyle(style);
						((HSSFRow)rows[i-1]).getCell( j+1 ).setCellValue( sumA ) ;
						sheet1.addMergedRegion(new Region(i-1, (short) (j+1), i, (short) (j+1)));//总字段合计合并
						sumA = 0 ;
					}
					if ( i % 6 ==  0 && j == 14) {
						((HSSFRow)rows[i-1]).createCell(j+1).setCellStyle(style);
						((HSSFRow)rows[i]).createCell(j+1).setCellStyle(style);
						((HSSFRow)rows[i-1]).getCell( j+1 ).setCellValue( sumB ) ;
						sheet1.addMergedRegion(new Region(i-1, (short) (j+1), i, (short) (j+1)));//污染源字段合计合并
						sumB = 0;
					}
					if ( i % 6 ==  2 && j == 14) {
						((HSSFRow)rows[i-1]).createCell(j+1).setCellStyle(style);
						((HSSFRow)rows[i]).createCell(j+1).setCellStyle(style);
						((HSSFRow)rows[i-1]).getCell( j+1 ).setCellValue( sumC ) ;
						sheet1.addMergedRegion(new Region(i-1, (short) (j+1), i, (short) (j+1)));//治理源字段合计合并
						sumC = 0;
					}
					
					
					//字段合并
					if( i % 6 ==  4 && j == 3 ) {
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//总字段合并
					}
					if( i % 6 == 0 && j == 3) {
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//污染源字段合并
					}
					if( i % 6 == 2 && j == 3) {
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//治理源字段合并
						sheet1.addMergedRegion(new Region(i-5, (short) 0, i, (short) 0));//合并日期
					}
				}
			}

			((HSSFRow)rows[1]).createCell(3).setCellValue(title);
			((HSSFRow)rows[1]).getCell(3).setCellStyle(titlestyle);
		}
		try {
			output.flush();
			wb.write(output);
			output.close();
		} catch (IOException e) {
			Log.error(this.getClass() + ".getExcel()");
		}
	}
	
	
	@Override
	public List<Map<String, Object>> queryEventDeclar(Map<String, Object> param) {
		List<Map<String, Object>> maps = null;
		Map<String, Object> map = this.queryFacilRelation( Long.parseLong( param.get( "objectId" ).toString() ) );
		if(null != map){
			param.putAll( map );
			param.put( "eventId",1107 );
			maps = pollutVoltCurrPowerMapper.queryEventDeclar( param );
		}
		return maps;
	}
}
