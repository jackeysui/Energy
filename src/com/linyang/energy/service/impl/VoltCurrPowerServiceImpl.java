package com.linyang.energy.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import com.linyang.energy.mapping.phone.PhoneMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esotericsoftware.minlog.Log;
import com.leegern.util.CollectionUtil;
import com.linyang.energy.mapping.energydataquery.VoltCurrPowerMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.CurveBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.VoltCurrPowerService;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.WebServiceUtil;
import com.linyang.ws.service.DataCollectionWebService;
import com.linyang.ws.wsimport.DataBean;

/**
 * 曲线查询Service实现
 * @author Leegern
 * @date Dec 4, 2013 5:16:54 PM
 */
@Service
public class VoltCurrPowerServiceImpl implements VoltCurrPowerService {
	@Autowired
	private VoltCurrPowerMapper voltCurrPowerMapper;
	@Autowired
	private MeterManagerMapper meterManagerMapper;
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	@Autowired
	private PhoneMapper phoneMapper;
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.VoltCurrPowerService#queryVoltCurrPowerInfo(java.util.Map)
	 */
	@Override
	public List<CurveBean> queryVoltCurrPowerInfo(Map<String, Object> params) throws ParseException {
		List<CurveBean> list = new ArrayList<CurveBean>();
		Integer oType = (Integer)params.get("objectType");
		if(oType != null && oType == 1){//EMO
			Integer curveType = (Integer)params.get("curveType");
			if(curveType != null && curveType == 5){
				List<MeterBean> meters = this.meterManagerMapper.getMeterListByLedgerId((Long)params.get("objectId"));
				//当该EMO只有一个测量点时，则该EMO功率因数不需要通过公式去计算，直接取该测量点的功率因数
				if(meters != null && meters.size() == 1){
					Long meterId = meters.get(0).getMeterId();
					params.put("objectType", 2);
					params.put("objectId", meterId);
					list = voltCurrPowerMapper.queryVoltCurrPowerInfo(params);
					//list = getVIPData(params, list);
                    return list;
				}
			}
			
			list = voltCurrPowerMapper.queryCurveData(params);
			if(curveType != null && curveType == 5){
				if(list != null && list.size() > 0){
					for (CurveBean curveBean : list) {
						if (curveBean.getApD() != null && curveBean.getRpD() != null) {
							double pf = DataUtil.getPF(curveBean.getApD(), curveBean.getRpD(), 3);
							curveBean.setD(pf);
						}
						if (curveBean.getApA() != null && curveBean.getRpA() != null) {
							double pfA = DataUtil.getPF(curveBean.getApA(), curveBean.getRpA(), 3);
							curveBean.setA(pfA);
						}
						if (curveBean.getApB() != null && curveBean.getRpB() != null) {
							double pfB = DataUtil.getPF(curveBean.getApB(), curveBean.getRpB(), 3);
							curveBean.setB(pfB);
						}
						if (curveBean.getApC() != null && curveBean.getRpC() != null) {
							double pfC = DataUtil.getPF(curveBean.getApC(), curveBean.getRpC(), 3);
							curveBean.setC(pfC);
						}	
					}
				}
			}
		}
        else {//DCP
			list = voltCurrPowerMapper.queryVoltCurrPowerInfo(params);
			//list = getVIPData(params, list);
		}
		return list;
	}

    @Override
    public CurveBean getLastNoblanceData(Map<String, Object> params) {
        List<CurveBean> list = this.voltCurrPowerMapper.getNoblanceDataList(params);
        if(CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public List<CurveBean> getLightPower(Map<String, Object> params) throws ParseException {
        List<CurveBean> list = voltCurrPowerMapper.getLightPower(params);
        return list;
    }
	
	/**
	 * 查询电压电流功率图表,整点数据，用于报表
	 * @author 周礼
	 * @start_time 2014.08.19 15:20:22
	 * @param params 查询参数
	 * @return
	 */
	public List<CurveBean> queryVoltCurrPowerReportInfo(Map<String, Object> params) throws ParseException {
		List<CurveBean> list = new ArrayList<CurveBean>();
		Integer oType = (Integer)params.get("objectType");
		if(oType != null && oType == 1){//EMO
			Integer curveType = (Integer)params.get("curveType");
			if(curveType != null && curveType == 5){
				List<MeterBean> meters = this.meterManagerMapper.getMeterListByLedgerId((Long)params.get("objectId"));
				//当该EMO只有一个测量点时，则该EMO功率因数不需要通过公式去计算，直接取该测量点的功率因数
				if(meters != null && meters.size() == 1){
					Long meterId = meters.get(0).getMeterId();
					params.put("objectType", 2);
					params.put("objectId", meterId);
					list = voltCurrPowerMapper.queryVoltCurrPowerReportInfo(params);
					//list = getVIPData(params, list);
                    return list;
				}
			}
			
			list = voltCurrPowerMapper.queryCurveReportData(params);
			if(curveType != null && curveType == 5){
				if(list != null && list.size() > 0){
					for (CurveBean curveBean : list) {
						if (curveBean.getApD() != null && curveBean.getRpD() != null) {
							double pf = DataUtil.getPF(curveBean.getApD(), curveBean.getRpD(), 3);
							curveBean.setD(pf);
						}
						if (curveBean.getApA() != null && curveBean.getRpA() != null) {
							double pfA = DataUtil.getPF(curveBean.getApA(), curveBean.getRpA(), 3);
							curveBean.setA(pfA);
						}
						if (curveBean.getApB() != null && curveBean.getRpB() != null) {
							double pfB = DataUtil.getPF(curveBean.getApB(), curveBean.getRpB(), 3);
							curveBean.setB(pfB);
						}
						if (curveBean.getApC() != null && curveBean.getRpC() != null) {
							double pfC = DataUtil.getPF(curveBean.getApC(), curveBean.getRpC(), 3);
							curveBean.setC(pfC);
						}				
					}
				}
			}	
		}
        else {//DCP
			list = voltCurrPowerMapper.queryVoltCurrPowerReportInfo(params);
			//list = getVIPData(params, list);
		}
		params.put("dataType", 1);//只记录一个小时一个点
		list = fixList( list );
		return list;
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


	






	






















	



















	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.VoltCurrPowerService#queryVoltCurrPowerList(java.util.Map)
	 */
	@Override
	public List<CurveBean> queryVoltCurrPowerList(Map<String, Object> params) {
		return voltCurrPowerMapper.queryVoltCurrPowerList(params);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.VoltCurrPowerService#queryStraightLine(java.util.Map)
	 */
	@Override
	public Map<String, Object> queryStraightLine(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = null;
		int curveType = (Integer)param.get("curveType");
		String thresholdValue = "";
		String voltValue = "";
		if (curveType == CurveBean.CURVE_TYPE_V) {
			// 额定电压
			param.put("curveType", 1);
			list = voltCurrPowerMapper.queryStraightLine(param);
			if (CollectionUtil.isNotEmpty(list)) {
				voltValue = list.get(0).get("THRESHOLD_VALUE").toString();
			}
			// 电压下线
			param.put("curveType", 2);
			list = voltCurrPowerMapper.queryStraightLine(param);
			if (CollectionUtil.isNotEmpty(list)) {
				thresholdValue = list.get(0).get("THRESHOLD_VALUE").toString();
			}
		}
		else if (curveType == CurveBean.CURVE_TYPE_I) {
			// 额定电流
			param.put("curveType", 3);
			list = voltCurrPowerMapper.queryStraightLine(param);
			if (CollectionUtil.isNotEmpty(list)) {
				thresholdValue = list.get(0).get("THRESHOLD_VALUE").toString();
			}
		}
		else if (curveType == CurveBean.CURVE_TYPE_AP) {
			// 有功功率
			param.put("curveType", 4);
			list = voltCurrPowerMapper.queryStraightLine(param);
			if (CollectionUtil.isNotEmpty(list)) {
				thresholdValue = list.get(0).get("THRESHOLD_VALUE").toString();
			}
		}
		else if (curveType == CurveBean.CURVE_TYPE_PF) {
			// 功率因素
			list = voltCurrPowerMapper.queryStandPF(param);
			if (CollectionUtil.isNotEmpty(list)) {
				thresholdValue = list.get(0).get("THRESHOLD_VALUE").toString();
			}
		}
		result.put("VOLT_VALUE", voltValue);
		result.put("THRESHOLD_VALUE", thresholdValue);
		return result;
	}

	/**
	 * 
	 * 函数功能说明  :
	 * 		当整点的数据为空时
	 * 		插入一个CurveBean
	 * 		时间为该位置本应该的整点
	 *      其他数据为空
	 * @param list      
	 * @return  List<CurveBean>
	 * @author 戚云琥
	 * @start_time 2014.08.20 09:17
	 */
	private List<CurveBean> fixList( List<CurveBean> list_ ){
		List<CurveBean> list = new ArrayList<CurveBean>( ) ;
		for( int i = 0 ; i < list_.size( ) ; i ++ ) {
			list.add( list_.get( i ) ) ;
		} 
		int curdd = 0 ;
		int tmpCurdd = 0 ;
		String sDate = "" ;
		String tmpSDate = "" ;
		List<CurveBean> tmpList = new ArrayList<CurveBean>( ) ;
		List<CurveBean> fixedList = new ArrayList<CurveBean>( ) ;
		while( list.size() > 0 ){
			sDate = ( new SimpleDateFormat("yyyyMMddHHmmss")).format( list.get(0).getFreezeTime() ) ;
			curdd = getday(sDate);

			tmpSDate = ( new SimpleDateFormat("yyyyMMddHHmmss")).format( list.get(0).getFreezeTime() ) ;
			tmpCurdd = getday(tmpSDate);
			while( list.size( ) > 0 && tmpCurdd == curdd ){
				tmpList.add( list.remove( 0 ) ) ;
				if( list.size( ) > 0 ){
					tmpSDate = ( new SimpleDateFormat("yyyyMMddHHmmss")).format( list.get(0).getFreezeTime() ) ;
					tmpCurdd = getday(tmpSDate);
				}
			}
			tmpList = fixDailyList( tmpList ) ;
			while( tmpList.size( ) > 0 ){
				fixedList.add( tmpList.remove( 0 ) ) ;
			}
		}
		return fixedList ;
	}

	/**
	 * 截取日期中的天
	 * @param sDate
	 * @return
	 */
	private int getday(String sDate) {
		int day = 0;
		if(sDate != null && sDate.length() > 10) {
			day = Integer.parseInt( sDate.substring( 6 , 8 ) ) ;	
		}
		return day;
	}
	
	/**
	 * 截取日期中的小时
	 * @param sDate
	 * @return
	 */
	private int getHour(String sDate) {
		int hour = 0;
		if(sDate != null && sDate.length() > 10) {
			hour = Integer.parseInt( sDate.substring( 8 , 10 ) ) ;
		}
		return hour;
	}
	
	private List<CurveBean> fixDailyList( List<CurveBean> list_ ){
		List<CurveBean> list = new ArrayList<CurveBean>( ) ;
		for( int i = 0 ; i < list_.size( ) ; i ++ ) list.add( list_.get( i ) ) ;
		List<CurveBean> fixedList = new ArrayList<CurveBean>( ) ;
		int lastHH = 0 ;
		String sDate = "" ;
		int curHH = 0 ;

		sDate = ( new SimpleDateFormat("yyyyMMddHHmmss")).format( list.get(0).getFreezeTime() ) ;
		curHH = getHour(sDate);
		if( curHH != 0 ){
			CurveBean fakeBean = new CurveBean( ) ;
			Calendar cal_ = new GregorianCalendar( ) ;
			cal_.setTime( list.get( 0 ).getFreezeTime() ) ;
			cal_.add( Calendar.HOUR , -1 * curHH ) ;
			fakeBean.setA( null ) ;
			fakeBean.setB( null ) ;
			fakeBean.setC( null ) ;
			fakeBean.setD( null ) ;		
			fakeBean.setFreezeTime( cal_.getTime( ) ) ;
			list.add( 0 , fakeBean ) ;
		}

		sDate = ( new SimpleDateFormat("yyyyMMddHHmmss")).format( list.get( list.size( ) - 1 ).getFreezeTime() ) ;
		curHH = getHour(sDate);
		if( curHH != 23 ){
			CurveBean fakeBean = new CurveBean( ) ;
			Calendar cal_ = new GregorianCalendar( ) ;
			cal_.setTime( list.get( list.size( ) - 1 ).getFreezeTime() ) ;
			cal_.add( Calendar.HOUR , ( 23 - curHH ) ) ;
			fakeBean.setA( null ) ;
			fakeBean.setB( null ) ;
			fakeBean.setC( null ) ;
			fakeBean.setD( null ) ;		
			fakeBean.setFreezeTime( cal_.getTime( ) ) ;
			list.add( fakeBean ) ;
		}
		for( int i = 0 ; i < list.size( ) ; i ++ ){
			sDate = ( new SimpleDateFormat("yyyyMMddHHmmss")).format( list.get(i).getFreezeTime() ) ;
			curHH = getHour(sDate);
			if( curHH == 0 ){
				fixedList.add( list.get( i ) ) ;
				lastHH = curHH ;
			}
			else{
				for( ; ; ){
					if( curHH == lastHH + 1 ||  curHH < lastHH){
						fixedList.add( list.get( i ) ) ;
						lastHH = curHH ;
						break ;
					}
					else{
						CurveBean tmpBean = new CurveBean() ;
						
						Calendar cal = new GregorianCalendar( ) ;
						cal.setTime( fixedList.get( lastHH ).getFreezeTime() ) ;
						cal.add( Calendar.HOUR , 1 ) ;
						tmpBean.setA( null ) ;
						tmpBean.setB( null ) ;
						tmpBean.setC( null ) ;
						tmpBean.setD( null ) ;		
						tmpBean.setFreezeTime( cal.getTime( ) ) ;
						fixedList.add( tmpBean ) ;
						lastHH ++ ;
					}
				}
			}
		}
		return fixedList ;
	}
	/**
	 * 得到Excel，数据填充
	 * @author 周礼
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
//	    style.setDataFormat(df.getFormat("0.00"));//支持2位
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
        int showEleCD = (Integer)result.get( "showEleCD" ) ;
		title = this.getTitleByCurveType(checkType);
		if( checkType == CurveBean.CURVE_TYPE_V ){
		    //接线方式
			Integer commMode = 0;
			if(oType != null && oType == 1){//EMO
				List<Integer> commModes = this.meterManagerMapper.getCommModeByLedgerId(objectId);
				if(commModes != null && commModes.size() > 1){
					 commMode = 2;
				}else if (commModes != null && commModes.size() == 1){
					commMode = commModes.get(0);
				}
			}else {//DCP
				commMode = this.meterManagerMapper.getCommModeByMeterId(objectId);
			}
            List<CurveBean> tmp = ( List<CurveBean> )result.get( "chartData" ) ;

			sheet1.setColumnWidth((short)3,(short)10*256);
			//定义最值变量
			double max = 0 , min = Double.MAX_VALUE;
			double[] maxArr = new double[tmp.size()*6/24];
			double[] minArr = new double[tmp.size()*6/24];
			
			Object[] rows = new Object[tmp.size()*6/24 + 10];
			for( int i = 0 ; i < rows.length ; i ++ ){
				rows[i] = sheet1.createRow( i ) ;
			}
			((HSSFRow)rows[0]).createCell(0).setCellValue( "用户：" + ledgerName ) ;			
			((HSSFRow)rows[1]).createCell(0).setCellValue( "日期" ) ;
			((HSSFRow)rows[1]).createCell(3).setCellValue( title ) ;
			((HSSFRow)rows[1]).getCell(0).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).getCell(3).setCellStyle(titlestyle);
			for( int i = 3 ; i < 15 ; i ++ ){
				((HSSFRow)rows[2]).createCell( i ).setCellValue( ( i - 3 ) + ":00" ) ;
				((HSSFRow)rows[2]).getCell( i ).setCellStyle(titlestyle);
			}
			((HSSFRow)rows[2]).getCell( 3 ).setCellValue( "0:00(12)" ) ;
			((HSSFRow)rows[2]).getCell(3).setCellStyle(titlestyle);

			for (int i = 1; i < 15; i++) {
				((HSSFRow)rows[1]).createCell(i).setCellStyle(titlestyle);
			}
			sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 2));
			sheet1.addMergedRegion(new Region(1, (short) 3, 1, (short) 14));
			
			//取最大值和最小值
			outer :
				for( int i = 3 , countA = 0 , countB = 0 , countC = 0 ; i < rows.length ; i ++ ){
					for( int j = 3 ; j < 15 ; j ++ ){
						if( i % 6 == 3 || i % 6 == 4 ){
							if( countA < tmp.size( ) && (( CurveBean )tmp.get( countA )).getA() != null){
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()) ) > max)
									max = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()));
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()) ) < min)
									min = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()));
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = 0 ;
                                min = Double.MAX_VALUE;
                            }
							countA ++ ;
						}
						else if( i % 6 == 5 || i % 6 == 0 ){
							if( countB < tmp.size( ) && (( CurveBean )tmp.get( countB )).getB() != null){
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()) ) > max)
									max = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()));
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()) ) < min)
									min = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()));
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = 0 ;
                                min = Double.MAX_VALUE;
                            }
							countB ++ ;
						}
						else if( i % 6 == 1 || i % 6 == 2 ){
							if( countC < tmp.size( ) && (( CurveBean )tmp.get( countC )).getC() != null){
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()) ) > max)
									max = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()));
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()) ) < min)
									min = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()));
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = 0 ;
                                min = Double.MAX_VALUE;
                            }
							countC ++ ;
							if( countC >= tmp.size( ) && j == 14 ) break outer ;
						}
					}
				}
			
			//开始写表格内容
			outer :
				for( int i = 3 , countA = 0 , countB = 0 , countC = 0 ; i < rows.length ; i ++ ){
					for( int j = 3 ; j < 15 ; j ++ ){
						((HSSFRow)rows[i]).createCell(j).setCellStyle(style);
						if( i % 6 == 3 || i % 6 == 4 ){
							if( countA >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal(((CurveBean)tmp.get( countA )).getA()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								double value = (( CurveBean )tmp.get( countA )).getA() ;
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.2f" , value) ) ;
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA())).equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA())).equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countA ++ ;
						}
						else if( i % 6 == 5 || i % 6 == 0 ){
							if( countB >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if( DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countB )).getB()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								double value = (( CurveBean )tmp.get( countB )).getB() ;
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.2f" ,value) ) ;
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB())).equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB())).equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countB ++ ;
						}
						else if( i % 6 == 1 || i % 6 == 2 ){
							if( countC >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if( DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countC )).getC()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{	
								double value = (( CurveBean )tmp.get( countC )).getC() ;
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.2f" , value) ) ;
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC())).equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC())).equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countC ++ ;
							if( countC >= tmp.size( ) && j == 14 ) break outer ;
						}
					if( i % 6 == 3 && j == 3 ){
						String sDate = ( new SimpleDateFormat("yyyy/MM/dd")).format( (( CurveBean )tmp.get( countA-1 )).getFreezeTime() ) ;
						((HSSFRow)rows[i]).createCell( 0 ).setCellValue( sDate ) ;
						((HSSFRow)rows[i]).getCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+2]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+3]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+4]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+5]).createCell( 0 ).setCellStyle( style ) ;
					}
					
					if( i % 6 == 3 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "A相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 6 == 5 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "B相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 6 == 1 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "C相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					
					if( i % 6 == 4 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 6 == 0 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 6 == 2 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-5, (short) 0, i, (short) 0));
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
				}
			}	
		}
		else if( checkType == CurveBean.CURVE_TYPE_I || checkType == CurveBean.CURVE_TYPE_V_DU || checkType == CurveBean.CURVE_TYPE_I_DU){
            List<CurveBean> tmp = ( List<CurveBean> )result.get( "chartData" ) ;

			sheet1.setColumnWidth((short)3,(short)10*256);
			//定义最值变量
			double max = 0 , min = Double.MAX_VALUE;
			double[] maxArr = new double[tmp.size()*6/24];
			double[] minArr = new double[tmp.size()*6/24];
			
			Object[] rows = new Object[tmp.size()*6/24 + 10];
			for( int i = 0 ; i < rows.length; i ++ ){
				rows[i] = sheet1.createRow( i ) ;
			}
			((HSSFRow)rows[0]).createCell(0).setCellValue( "用户：" + ledgerName ) ;
			
			((HSSFRow)rows[1]).createCell(0).setCellValue( "日期" ) ;
			((HSSFRow)rows[1]).createCell(3).setCellValue( title ) ;
			((HSSFRow)rows[1]).getCell(0).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).getCell(3).setCellStyle(titlestyle);
			
			for( int i = 3 ; i < 15 ; i ++ ){
				((HSSFRow)rows[2]).createCell( i ).setCellValue( ( i - 3 ) + ":00" ) ;
				((HSSFRow)rows[2]).getCell( i ).setCellStyle(titlestyle);
			}
			((HSSFRow)rows[2]).getCell( 3 ).setCellValue( "0:00(12)" ) ;
			((HSSFRow)rows[2]).getCell(3).setCellStyle(titlestyle);
			
			((HSSFRow)rows[1]).createCell( 1 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 2 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 4 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 5 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 6 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 7 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 8 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 9 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 10 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 11 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 12 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 13 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 14 ).setCellStyle(titlestyle);

			sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 2));
			sheet1.addMergedRegion(new Region(1, (short) 3, 1, (short) 14));

			//取最大值和最小值
			outer :
				for( int i = 3 , countA = 0 , countB = 0 , countC = 0 ; i < rows.length ; i ++ ){
					for( int j = 3 ; j < 15 ; j ++ ){
						if( i % 6 == 3 || i % 6 == 4 ){
							if( countA < tmp.size( ) && (( CurveBean )tmp.get( countA )).getA() != null){
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()) ) > max)
									max = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()));
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()) ) < min)
									min = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()));
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = 0 ;
                                min = Double.MAX_VALUE;
                            }
							countA ++ ;
						}
						else if( i % 6 == 5 || i % 6 == 0 ){
							if( countB < tmp.size( ) && (( CurveBean )tmp.get( countB )).getB() != null){
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()) ) > max)
									max = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()));
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()) ) < min)
									min = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()));
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = 0 ;
                                min = Double.MAX_VALUE;
                            }
							countB ++ ;
						}
						else if( i % 6 == 1 || i % 6 == 2 ){
							if( countC < tmp.size( ) && (( CurveBean )tmp.get( countC )).getC() != null){
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()) ) > max)
									max = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()));
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()) ) < min)
									min = Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()));
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = 0 ;
                                min = Double.MAX_VALUE;
                            }
							countC ++ ;
							if( countC >= tmp.size( ) && j == 14 ) break outer ;
						}
					}
				}
			
			//开始写表格内容
			outer :
				for( int i = 3 , countA = 0 , countB = 0 , countC = 0 ; i < rows.length ; i ++ ){
					for( int j = 3 ; j < 15 ; j ++ ){
						((HSSFRow)rows[i]).createCell(j).setCellStyle(style);
						if( i % 6 == 3 || i % 6 == 4 ){
							if( countA >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if( DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countA )).getA()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()  ) ) ;
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA())).equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA())).equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countA ++ ;
						}
						else if( i % 6 == 5 || i % 6 == 0 ){
							if( countB >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if( DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countB )).getB()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()  ) ) ;
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB())).equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB())).equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countB ++ ;
						}
						else if( i % 6 == 1 || i % 6 == 2 ){
							if( countC >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if( DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countC )).getC()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{	
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC()  ) ) ;
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC())).equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (Double.valueOf(String.format("%.2f" , (( CurveBean )tmp.get( countC )).getC())).equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countC ++ ;
							if( countC >= tmp.size( ) && j == 14 ) break outer ;
						}
					if( i % 6 == 3 && j == 3 ){
						String sDate = ( new SimpleDateFormat("yyyy/MM/dd")).format( (( CurveBean )tmp.get( countA-1 )).getFreezeTime() ) ;
						((HSSFRow)rows[i]).createCell( 0 ).setCellValue( sDate ) ;
						((HSSFRow)rows[i]).getCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+2]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+3]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+4]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+5]).createCell( 0 ).setCellStyle( style ) ;
					}
					
					if( i % 6 == 3 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "A相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 6 == 5 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "B相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 6 == 1 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "C相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					
					if( i % 6 == 4 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 6 == 0 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 6 == 2 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-5, (short) 0, i, (short) 0));
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
				}
			}
		}
		else if( checkType == CurveBean.CURVE_TYPE_ELE ){
            List<CurveBean> tmp = ( List<CurveBean> )result.get( "chartData" ) ;

			sheet1.setColumnWidth((short)15,(short)15*256);
			double max = 0;
			double min = Double.MAX_VALUE;
			double[] maxArr = new double[tmp.size()*4/24];
			double[] minArr = new double[tmp.size()*4/24];
			
			List<Double> maxList = new ArrayList<Double>( 0 );
			List<Double> minList = new ArrayList<Double>( 0 );
			
			Object[] rows = new Object[tmp.size()*4/24 + 10];//数据行+表头行
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
			sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 2));
			sheet1.addMergedRegion(new Region(1, (short) 3, 1, (short) 15));
			
            boolean cdExist = false;
            if(showEleCD == 1 || showEleCD == 2){
                cdExist = showEleCD == 1? true:false;
            }else if(showEleCD == 0){
                for(CurveBean cb :tmp){
                    if(cb.getC() != null && cb.getC() != 0){
                        cdExist = true;
                        break;
                    }
                    if(cb.getD() != null && cb.getD() != 0){
                        cdExist = true;
                        break;
                    }
                }
            }
            if(cdExist){
                //取最大值和最小值
                outer :
                    for( int i = 3 , countA = 0 , countB = 0, countC = 0 , countD = 0 ; i < rows.length ; i ++ ){
                        for( int j = 3 ; j < 15 ; j ++ ){
                            if( i % 8 == 3 || i % 8 == 4 ){
                                if( countA < tmp.size() && (( CurveBean )tmp.get( countA )).getA() != null){
                                    if (new BigDecimal(((CurveBean)tmp.get( countA )).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(max)) > 0)
                                    	max = new BigDecimal(((CurveBean)tmp.get(countA)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();//if (( (( CurveBean )tmp.get( countA )).getA()*10 + 0.5 )/10.0 > max)  max = ( (( CurveBean )tmp.get( countA )).getA()*10 + 0.5 )/10.0;
                                    if (new BigDecimal(((CurveBean)tmp.get( countA )).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(min)) < 0)
                                    	min = new BigDecimal(((CurveBean)tmp.get(countA)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();//if (( (( CurveBean )tmp.get( countA )).getA()*10 + 0.5 )/10.0 < min)  min = ( (( CurveBean )tmp.get( countA )).getA()*10 + 0.5 )/10.0;
                                }
                                if ( j == 14 && i % 2 == 0){
									maxList.add( max );
									minList.add( min );
                                    max = 0 ;
                                    min = Double.MAX_VALUE;
                                }
                                countA ++ ;
                            }else if( i % 8 == 5 || i % 8 == 6 ){
                                if( countB < tmp.size() && (( CurveBean )tmp.get( countB )).getB() != null){
                                	if (new BigDecimal(((CurveBean)tmp.get( countB )).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(max)) > 0)
                                		max = new BigDecimal(((CurveBean)tmp.get(countB)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();//if (( (( CurveBean )tmp.get( countB )).getB()*10 + 0.5 )/10.0 > max)  max = ( (( CurveBean )tmp.get( countB )).getB()*10 + 0.5 )/10.0;
                                    if (new BigDecimal(((CurveBean)tmp.get( countB )).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(min)) < 0)
                                    	min = new BigDecimal(((CurveBean)tmp.get(countB)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();//if (( (( CurveBean )tmp.get( countB )).getB()*10 + 0.5 )/10.0 < min)  min = ( (( CurveBean )tmp.get( countB )).getB()*10 + 0.5 )/10.0;
                                }
                                if ( j == 14 && i % 2 == 0){
									maxList.add( max );
									minList.add( min );
                                    max = 0 ;
                                    min = Double.MAX_VALUE;
                                }
                                countB ++ ;
                            }else if( i % 8 == 7 || i % 8 == 0 ){
                                if( countC < tmp.size( ) && (( CurveBean )tmp.get( countC )).getC() != null){
                                	if (new BigDecimal(((CurveBean)tmp.get( countC )).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(max)) > 0)
                                		max = new BigDecimal(((CurveBean)tmp.get(countC)).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                	if (new BigDecimal(((CurveBean)tmp.get( countC )).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(min)) < 0)
                                		min = new BigDecimal(((CurveBean)tmp.get(countC)).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();//if (( (( CurveBean )tmp.get( countC )).getC()*10 + 0.5 )/10.0 < min)  min = ( (( CurveBean )tmp.get( countC )).getC()*10 + 0.5 )/10.0;
                                }
                                if ( j == 14 && i % 2 == 0){
									maxList.add( max );
									minList.add( min );
                                    max = 0 ;
                                    min = Double.MAX_VALUE;
                                }
                                countC ++ ;
                            }else if( i % 8 == 1 || i % 8 == 2 ){
                                if( countD < tmp.size( ) && (( CurveBean )tmp.get( countD )).getD() != null){
                                	if (new BigDecimal(((CurveBean)tmp.get( countD )).getD()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(max)) > 0)
                                		max = new BigDecimal(((CurveBean)tmp.get(countD)).getD()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    if (new BigDecimal(((CurveBean)tmp.get( countD )).getD()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(min)) < 0)
                                    	min = new BigDecimal(((CurveBean)tmp.get(countD)).getD()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                }
                                if ( j == 14 && i % 2 == 0){
									maxList.add( max );
									minList.add( min );
                                    max = 0 ;
                                    min = Double.MAX_VALUE;
                                }
                                countD ++ ;
                            }
                            if( countD >= tmp.size( ) && j == 14 ) break outer ;
                        }
                    }

                double sumA = 0 , sumB = 0 , sumC = 0 , sumD = 0;
                boolean sumAIsNull = true, sumBIsNull = true , sumCIsNull = true , sumDIsNull = true; 
                outer :
                    for( int i = 3 , countA = 0 , countB = 0 , countC = 0 , countD = 0 ,indexA = 0,indexB = 1,indexC = 2,indexD = 3,ca = 0 , cb = 0, cc = 0,cd = 0; i < rows.length ; i ++ ){
                        for( int j = 3 ; j < 16 ; j ++ ){
                            ((HSSFRow)rows[i]).createCell(j).setCellStyle(style);
                            if( j != 15 ){//设置数据
                                if( i % 8 == 3 || i % 8 == 4 ){
                                    if( countA >= tmp.size( ) ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else if( DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countA )).getA()) == null ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else{
                                        sumAIsNull = false;
                                        Double tempD = new BigDecimal(((CurveBean) tmp.get(countA)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue(tempD);
										if (checkType == 6) {
											sumA = DataUtil.doubleAdd_new(sumA, tempD,5);
										} else {
											sumA = DataUtil.doubleAdd(sumA, tempD);
										}
	
	
										//dingy
										if(indexA < maxList.size() &&  minList.get( indexA ) != maxList.get( indexA )){
											if (tempD.equals(maxList.get( indexA ))){
												((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
												ca++;
											}
											if (tempD.equals(minList.get( indexA ))){
												((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
												ca++;
											}
											if(ca == 2 && indexD < maxList.size()){
												ca = 0 ;
												indexA +=4;
											}
										}
										
//                                        if(minArr[i/2 + i%2] != maxArr[i/2 + i%2]){
//                                            if (tempD.equals(maxArr[i/2 + i%2])){
//                                                ((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
//                                            }
//                                            if (tempD.equals(minArr[i/2 + i%2])){
//                                                ((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
//                                            }
//                                        }
                                    }
                                    countA ++ ;
                                }else if( i % 8 == 5 || i % 8 == 6 ){
                                    if( countB >= tmp.size( ) ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countB )).getB()) == null ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else{
                                        sumBIsNull = false;
                                        Double tempD = new BigDecimal(((CurveBean) tmp.get(countB)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue(tempD);
										if (checkType == 6) {
											sumB = DataUtil.doubleAdd_new(sumB, tempD,5);
										} else {
											sumB = DataUtil.doubleAdd(sumB, tempD);
										}
	
										//dingy
										if(indexB < maxList.size() &&  minList.get( indexB ) != maxList.get( indexB )){
											if (tempD.equals(maxList.get( indexB ))){
												((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
												cb++;
											}
											if (tempD.equals(minList.get( indexB ))){
												((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
												cb++;
											}
											if(cb == 2){
												cb = 0 ;
												indexB +=4;
											}
										}
          
//                                        if(minArr[i/2 + i%2] != maxArr[i/2 + i%2]){
//                                            if (tempD.equals(maxArr[i/2 + i%2])){
//                                                ((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
//                                            }
//                                            if (tempD.equals(minArr[i/2 + i%2])){
//                                                ((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
//                                            }
//                                        }
                                    }
                                    countB ++ ;
                                }else if( i % 8 == 7 || i % 8 == 0 ){
                                    if( countC >= tmp.size( ) ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countC )).getC()) == null ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else{
                                        sumCIsNull = false;
                                        Double tempD = new BigDecimal(((CurveBean) tmp.get(countC)).getC()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue(tempD);
										if (checkType == 6) {
											sumC = DataUtil.doubleAdd_new(sumC, tempD,5);
										} else {
											sumC = DataUtil.doubleAdd(sumC, tempD);
										}
	
										
	
										//dingy
										if(indexC < maxList.size() &&  minList.get( indexC ) != maxList.get( indexC )){
											if (tempD.equals(maxList.get( indexC ))){
												((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
												cc++;
											}
											if (tempD.equals(minList.get( indexC ))){
												((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
												cc++;
											}
											if(cc == 2){
												cc = 0 ;
												indexC +=4;
											}
										}
          
//                                        if(minArr[i/2 + i%2] != maxArr[i/2 + i%2]){
//                                            if (tempD.equals(maxArr[i/2 + i%2])) {
//                                                ((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
//                                            }
//                                            if (tempD.equals(minArr[i/2 + i%2])) {
//                                                ((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
//                                            }
//                                        }
                                    }
                                    countC ++ ;
                                }else if( i % 8 == 1 || i % 8 == 2 ){
                                    if( countD >= tmp.size( ) ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countD )).getD()) == null ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else{
                                        sumDIsNull = false;
                                        Double tempD = new BigDecimal(((CurveBean) tmp.get(countD)).getD()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue(tempD);
										if (checkType == 6) {
											sumD = DataUtil.doubleAdd_new(sumD, tempD,5);
										} else {
											sumD = DataUtil.doubleAdd(sumD, tempD);
										}
          
	
										//dingy
										if(indexD < maxList.size() &&  minList.get( indexD ) != maxList.get( indexD )){
											if (tempD.equals(maxList.get( indexD ))){
												((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
												cd++;
											}
											if (tempD.equals(minList.get( indexD ))){
												((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
												cd++;
											}
											if(cd == 2){
												cd = 0 ;
												indexD +=4;
											}
										}
                                        
//                                        if(minArr[i/2 + i%2] != maxArr[i/2 + i%2]){
//                                            if (tempD.equals(maxArr[i/2 + i%2])) {
//                                                ((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
//                                            }
//                                            if (tempD.equals(minArr[i/2 + i%2])) {
//                                                ((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
//                                            }
//                                        }
                                    }
                                    countD ++ ;
                                }
                            }
                            else{
                                if( i%8 == 4 ){ 
                                    if(sumAIsNull){
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue("");
                                    }else{
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( sumA ) ;
                                    }
                                    sumA = 0 ;
                                    sumAIsNull = true;
                                    sheet1.addMergedRegion(new Region(i-1, (short) j, i, (short) j));//正向有功合计行合并
                                }
                                if( i%8 == 6 ){
                                    if(sumBIsNull){
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue("");
                                    }else{
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( sumB ) ;
                                    }
                                    sumB = 0 ;
                                    sumBIsNull = true;
                                    sheet1.addMergedRegion(new Region(i-1, (short) j, i, (short) j));//正向无功合计行合并
                                }
                                if( i%8 == 0 ){
                                    if(sumCIsNull){
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( "" ) ;
                                    }else{
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( sumC ) ;
                                    }
                                    sumC = 0 ;
                                    sumCIsNull = true;
                                    sheet1.addMergedRegion(new Region(i-1, (short) j, i, (short) j));//反向有功合计行合并
                                }
                                if( i%8 == 2 ){
                                    if(sumDIsNull){
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( "" ) ;
                                    }else{
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( sumD ) ;
                                    }
                                    sumD = 0 ;
                                    sumDIsNull = true;
                                    sheet1.addMergedRegion(new Region(i-1, (short) j, i, (short) j));//反向无功合计行合并
                                }
                            }
                            if( countD >= tmp.size() && j == 15 ) break outer ;
                            if( i % 8 == 3 && j == 3 ){
                                String sDate = ( new SimpleDateFormat("yyyy/MM/dd")).format( (( CurveBean )tmp.get( countA-1)).getFreezeTime() ) ;
                                ((HSSFRow)rows[i]).createCell( 0 ).setCellValue( sDate ) ;
                                ((HSSFRow)rows[i]).getCell(0).setCellStyle(style);
                                ((HSSFRow)rows[i+1]).createCell( 0 ).setCellStyle(style);
                                ((HSSFRow)rows[i+2]).createCell( 0 ).setCellStyle(style);
                                ((HSSFRow)rows[i+3]).createCell( 0 ).setCellStyle(style);
                                ((HSSFRow)rows[i+4]).createCell( 0 ).setCellStyle(style);
                                ((HSSFRow)rows[i+5]).createCell( 0 ).setCellStyle(style);
                                ((HSSFRow)rows[i+6]).createCell( 0 ).setCellStyle(style);
                                ((HSSFRow)rows[i+7]).createCell( 0 ).setCellStyle(style);
                            }

                            if( i % 8 == 3 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "正向有功" ) ;
                                ((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                ((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
                            }
                            else if( i % 8 == 5 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "正向无功" ) ;
                                ((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                ((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
                            }
                            else if( i % 8 == 7 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "反向有功" ) ;
                                ((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                ((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
                            }
                            else if( i % 8 == 1 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "反向无功" ) ;
                                ((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                ((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
                            }

                            if( i % 8 == 4 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//正向有功字段合并
                            }
                            else if( i % 8 == 6 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//正向无功字段合并
                            }
                            else if( i % 8 == 0 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//反向有功字段合并
                            }
                            else if( i % 8 == 2 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//反向无功字段合并
                                sheet1.addMergedRegion(new Region(i-7, (short) 0, i, (short) 0));//合并日期
                            }
                        }
                    }
            }else{
                //取最大值和最小值
                outer :
                    for( int i = 3 , countA = 0 , countB = 0 ; i < rows.length ; i ++ ){
                        for( int j = 3 ; j < 15 ; j ++ ){
                            if( i % 4 == 3 || i % 4 == 0 ){
                                if( countA < tmp.size( ) && (( CurveBean )tmp.get( countA )).getA() != null){
                                	if (new BigDecimal(((CurveBean)tmp.get( countA )).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(max)) > 0)
                                		max = new BigDecimal(((CurveBean)tmp.get(countA)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                	if (new BigDecimal(((CurveBean)tmp.get( countA )).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(min)) < 0)
                                		min = new BigDecimal(((CurveBean)tmp.get(countA)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                }
                                if ( j == 14 && i % 2 == 0){
                                    maxArr[i/2] = max;
                                    minArr[i/2] = min;
                                    max = 0 ;
                                    min = Double.MAX_VALUE;
                                }
                                countA ++ ;
                            }
                            else if( i % 4 == 1 || i % 4 == 2 ){
                                if( countB < tmp.size() && (( CurveBean )tmp.get( countB )).getB() != null){
                                	if (new BigDecimal(((CurveBean)tmp.get( countB )).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(max)) > 0)
                                		max = new BigDecimal(((CurveBean)tmp.get(countB)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                	if (new BigDecimal(((CurveBean)tmp.get( countB )).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN).compareTo(new BigDecimal(min)) < 0)
                                		min = new BigDecimal(((CurveBean)tmp.get(countB)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                }
                                if ( j == 14 && i % 2 == 0){
                                    maxArr[i/2] = max;
                                    minArr[i/2] = min;
                                    max = 0 ;
                                    min = Double.MAX_VALUE;
                                }
                                countB ++ ;
                            }
                            if( countB >= tmp.size( ) && j == 14 ) break outer ;
                        }
                    }
                
                double sumA = 0 , sumB = 0;
                boolean sumAIsNull = true, sumBIsNull = true;
                outer :
                    for( int i = 3 , countA = 0 , countB = 0 ; i < rows.length ; i ++ ){
                        for( int j = 3 ; j < 16 ; j ++ ){
                            ((HSSFRow)rows[i]).createCell(j).setCellStyle(style);
                            if( j != 15 ){//设置数据
                                if( i % 4 == 3 || i % 4 == 0 ){
                                    if( countA >= tmp.size( ) ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countA )).getA()) == null ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else{
                                        sumAIsNull = false;
                                        Double tempD = new BigDecimal(((CurveBean) tmp.get(countA)).getA()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue(tempD);
										if (checkType == 6) {
											sumA = DataUtil.doubleAdd_new(sumA, tempD,5);
										} else {
											sumA = DataUtil.doubleAdd(sumA, tempD);
										}
	
										if (tempD.equals(maxArr[i/2 + i%2])){
                                            ((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
                                        }
                                        if (tempD.equals(minArr[i/2 + i%2])){
                                            ((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
                                        }
                                    }
                                    countA ++ ;
                                }
                                else if( i % 4 == 1 || i % 4 == 2 ){
                                    if( countB >= tmp.size( ) ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countB )).getB()) == null ){
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue( "" ) ;
                                    }
                                    else{
                                        sumBIsNull = false;
                                        Double tempD = new BigDecimal(((CurveBean) tmp.get(countB)).getB()).multiply(BigDecimal.TEN).add(new BigDecimal(0)).divide(BigDecimal.TEN, 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        ((HSSFRow)rows[i]).getCell( j ).setCellValue(tempD);
										if (checkType == 6) {
											sumB = DataUtil.doubleAdd_new(sumB, tempD,5);
										} else {
											sumB = DataUtil.doubleAdd(sumB, tempD);
										}
                                        if (tempD.equals(maxArr[i/2 + i%2])) {
                                            ((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
                                        }
                                        if (tempD.equals(minArr[i/2 + i%2])) {
                                            ((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
                                        }
                                    }
                                    countB ++ ;
                                }
                            }
                            else{
                                if( i%4 == 0 ){
                                    if(sumAIsNull){
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( "" ) ;
                                    }else{
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( sumA ) ;
                                    }
                                    sumA = 0 ;
                                    sumAIsNull = true;
                                    sheet1.addMergedRegion(new Region(i-1, (short) j, i, (short) j));//有功合计行合并
                                }
                                if( i%4 == 2 ){
                                    if(sumBIsNull){
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( "" ) ;
                                    }else{
                                        ((HSSFRow)rows[i-1]).getCell( j ).setCellValue( sumB ) ;
                                    }
                                    sumB = 0 ;
                                    sumBIsNull = true;
                                    sheet1.addMergedRegion(new Region(i-1, (short) j, i, (short) j));//无功合计行合并
                                }
                            }
                            if( countB >= tmp.size() && j == 15 ) break outer ;
                            if( i % 4 == 3 && j == 3 ){
                                String sDate = ( new SimpleDateFormat("yyyy/MM/dd")).format( (( CurveBean )tmp.get( countA-1)).getFreezeTime() ) ;
                                ((HSSFRow)rows[i]).createCell( 0 ).setCellValue( sDate ) ;
                                ((HSSFRow)rows[i]).getCell(0).setCellStyle(style);
                                ((HSSFRow)rows[i+1]).createCell( 0 ).setCellStyle(style);
                                ((HSSFRow)rows[i+2]).createCell( 0 ).setCellStyle(style);
                                ((HSSFRow)rows[i+3]).createCell( 0 ).setCellStyle(style);
                            }

                            if( i % 4 == 3 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "正向有功" ) ;
                                ((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                ((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
                            }
                            else if( i % 4 == 1 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "正向无功" ) ;
                                ((HSSFRow)rows[i]).getCell(1).setCellStyle(style);
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                ((HSSFRow)rows[i+1]).createCell(1).setCellStyle(style);
                            }

                            if( i % 4 == 0 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//有功字段合并
                            }
                            else if( i % 4 == 2 && j == 3 ){
                                ((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
                                ((HSSFRow)rows[i]).getCell(2).setCellStyle(style);
                                sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));//无功字段合并
                                sheet1.addMergedRegion(new Region(i-3, (short) 0, i, (short) 0));//合并日期
                            }
                        }
                    }
            }
			((HSSFRow)rows[1]).createCell(3).setCellValue(title);
			((HSSFRow)rows[1]).getCell(3).setCellStyle(titlestyle);
		}
		if( checkType == CurveBean.CURVE_TYPE_PF ){
            List<CurveBean> tmp = ( List<CurveBean> )result.get( "chartData" ) ;

            sheet1.setColumnWidth((short)3,(short)10*256);
			Object[] rows = new Object[tmp.size()*8/24 + 10];
			for( int i = 0 ; i < rows.length ; i ++ ){
				rows[i] = sheet1.createRow( i ) ;
			}
			((HSSFRow)rows[0]).createCell(0).setCellValue( "用户：" + ledgerName ) ;
			
			((HSSFRow)rows[1]).createCell(0).setCellValue( "日期" ) ;
			((HSSFRow)rows[1]).createCell(3).setCellValue( title ) ;
			((HSSFRow)rows[1]).getCell(0).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).getCell(3).setCellStyle(titlestyle);
			
			for( int i = 3 ; i < 15 ; i ++ ){
				((HSSFRow)rows[2]).createCell( i ).setCellValue( ( i - 3 ) + ":00" ) ;
				((HSSFRow)rows[2]).getCell( i ).setCellStyle(titlestyle);
			}
			((HSSFRow)rows[2]).getCell( 3 ).setCellValue( "0:00(12)" ) ;
			((HSSFRow)rows[2]).getCell(3).setCellStyle(titlestyle);
			
			((HSSFRow)rows[1]).createCell( 1 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 2 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 4 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 5 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 6 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 7 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 8 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 9 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 10 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 11 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 12 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 13 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 14 ).setCellStyle(titlestyle);

			sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 2));
			sheet1.addMergedRegion(new Region(1, (short) 3, 1, (short) 14));

			//开始写表格内容
			outer :
				for( int i = 3 , countA = 0 , countB = 0 , countC = 0 , countD = 0; i < rows.length ; i ++ ){
					for( int j = 3 ; j < 15 ; j ++ ){
						((HSSFRow)rows[i]).createCell(j).setCellStyle(style);
						if( i % 8 == 3 || i % 8 == 4 ){
							if( countD >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countD )).getD()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{	
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.3f" , (( CurveBean )tmp.get( countD )).getD()  ) ) ;
								if (Double.valueOf(String.format("%.3f", (( CurveBean )tmp.get( countD )).getD()) ) < 0.9  )
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
							}
							countD ++ ;
						}
						else if( i % 8 == 5 || i % 8 == 6 ){
							if( countA >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countA )).getA()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.3f" , (( CurveBean )tmp.get( countA )).getA()  ) ) ;
								if (Double.valueOf(String.format("%.3f", (( CurveBean )tmp.get( countA )).getA()) ) < 0.9  )
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
							}
							countA ++ ;							
						}
						else if( i % 8 == 7 || i % 8 == 0 ){
							if( countB >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countB )).getB()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.3f" , (( CurveBean )tmp.get( countB )).getB()  ) ) ;
								if (Double.valueOf(String.format("%.3f", (( CurveBean )tmp.get( countB )).getB()) ) < 0.9  )
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
							}
							countB ++ ;						
						}
						else if ( i % 8 == 1 || i % 8 == 2) {
							if( countC >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countC )).getC()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{	
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.3f" , (( CurveBean )tmp.get( countC )).getC()  ) ) ;
								if (Double.valueOf(String.format("%.3f", (( CurveBean )tmp.get( countC )).getC()) ) < 0.9  )
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
							}
							countC ++ ;						
							if( countC >= tmp.size( ) && j == 14 ) break outer ;
						}
					if( i % 8 == 3 && j == 3 ){
						String sDate = ( new SimpleDateFormat("yyyy/MM/dd")).format( (( CurveBean )tmp.get( countD-1 )).getFreezeTime() ) ;
						((HSSFRow)rows[i]).createCell( 0 ).setCellValue( sDate ) ;
						((HSSFRow)rows[i]).getCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+2]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+3]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+4]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+5]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+6]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+7]).createCell( 0 ).setCellStyle( style ) ;
					}
					
					if( i % 8 == 3 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "总" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 8 == 5 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "A相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 8 == 7 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "B相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 8 == 1 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "C相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					
					if( i % 8 == 4 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					if( i % 8 == 6 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 8 == 0 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 8 == 2 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-7, (short) 0, i, (short) 0));
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
				}
			}	
		}
		if( checkType == CurveBean.CURVE_TYPE_AP || checkType == CurveBean.CURVE_TYPE_RP){
            List<CurveBean> tmp = ( List<CurveBean> )result.get( "chartData" ) ;

			sheet1.setColumnWidth((short)3,(short)10*256);
			//定义最值变量
			double max = new BigDecimal(Double.MAX_VALUE).negate().doubleValue() , min = Double.MAX_VALUE;
			double[] maxArr = new double[tmp.size()*8/24];
			double[] minArr = new double[tmp.size()*8/24];
			
			Object[] rows = new Object[tmp.size()*8/24 + 10];
			for( int i = 0 ; i < rows.length ; i ++ ){
				rows[i] = sheet1.createRow( i ) ;
			}
			((HSSFRow)rows[0]).createCell(0).setCellValue( "用户：" + ledgerName ) ;
			
			((HSSFRow)rows[1]).createCell(0).setCellValue( "日期" ) ;
			((HSSFRow)rows[1]).createCell(3).setCellValue( title ) ;
			((HSSFRow)rows[1]).getCell(0).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).getCell(3).setCellStyle(titlestyle);
			
			for( int i = 3 ; i < 15 ; i ++ ){
				((HSSFRow)rows[2]).createCell( i ).setCellValue( ( i - 3 ) + ":00" ) ;
				((HSSFRow)rows[2]).getCell( i ).setCellStyle(titlestyle);
			}
			((HSSFRow)rows[2]).getCell( 3 ).setCellValue( "0:00(12)" ) ;
			((HSSFRow)rows[2]).getCell(3).setCellStyle(titlestyle);
			
			((HSSFRow)rows[1]).createCell( 1 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 2 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 4 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 5 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 6 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 7 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 8 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 9 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 10 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 11 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 12 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 13 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 14 ).setCellStyle(titlestyle);

			sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 2));
			sheet1.addMergedRegion(new Region(1, (short) 3, 1, (short) 14));
			
			//取最大值和最小值
			outer :
				for( int i = 3 , countA = 0 , countB = 0 , countC = 0 , countD = 0; i < rows.length ; i ++ ){
					for( int j = 3 ; j < 15 ; j ++ ){
						if( i % 8 == 3 || i % 8 == 4 ){
							if( countD < tmp.size( ) && (( CurveBean )tmp.get( countD )).getD() != null){
								if ((( CurveBean )tmp.get( countD )).getD() > max)
									max = DataUtil.retained((( CurveBean )tmp.get( countD )).getD(),2);
								if ((( CurveBean )tmp.get( countD )).getD() < min)
									min = DataUtil.retained((( CurveBean )tmp.get( countD )).getD(),2);
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = new BigDecimal(Double.MAX_VALUE).negate().doubleValue();
                                min = Double.MAX_VALUE;
                            }
							countD ++ ;
						}
						else if( i % 8 == 5 || i % 8 == 6 ){
							if( countA < tmp.size( ) && (( CurveBean )tmp.get( countA )).getA() != null){
								if ( (( CurveBean )tmp.get( countA )).getA() > max)
									max = DataUtil.retained((( CurveBean )tmp.get( countA )).getA(),2);
								if ( (( CurveBean )tmp.get( countA )).getA() < min)
									min = DataUtil.retained((( CurveBean )tmp.get( countA )).getA(),2);
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = new BigDecimal(Double.MAX_VALUE).negate().doubleValue();
                                min = Double.MAX_VALUE;
                            }
							countA ++ ;
						}
						else if( i % 8 == 7 || i % 8 == 0 ){
							if( countB < tmp.size( ) && (( CurveBean )tmp.get( countB )).getB() != null){
								if ((( CurveBean )tmp.get( countB )).getB() > max)
									max = DataUtil.retained((( CurveBean )tmp.get( countB )).getB(),2);
								if ((( CurveBean )tmp.get( countB )).getB() < min)
									min = DataUtil.retained((( CurveBean )tmp.get( countB )).getB(),2);
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = new BigDecimal(Double.MAX_VALUE).negate().doubleValue();
                                min = Double.MAX_VALUE;
                            }
							countB ++ ;							
						}
						else if( i % 8 == 1 || i % 8 == 2 ){
							if( countC < tmp.size( ) && (( CurveBean )tmp.get( countC )).getC() != null){
								if ((( CurveBean )tmp.get( countC )).getC() > max)
									max = DataUtil.retained((( CurveBean )tmp.get( countC )).getC(),2);
								if ((( CurveBean )tmp.get( countC )).getC() < min)
									min = DataUtil.retained((( CurveBean )tmp.get( countC )).getC(),2);
							}
                            if ( j == 14 && i % 2 == 0){
                                maxArr[i/2] = max;
                                minArr[i/2] = min;
                                max = new BigDecimal(Double.MAX_VALUE).negate().doubleValue();
                                min = Double.MAX_VALUE;
                            }
							countC ++ ;				
							if( countC >= tmp.size( ) && j == 14 ) break outer ;
						}
					}
				}
			
			//开始写表格内容
			outer :
				for( int i = 3 , countA = 0 , countB = 0 , countC = 0 , countD = 0; i < rows.length ; i ++ ){
					for( int j = 3 ; j < 15 ; j ++ ){
						((HSSFRow)rows[i]).createCell(j).setCellStyle(style);
						if( i % 8 == 3 || i % 8 == 4 ){
							if( countD >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countD )).getD()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{	
								((HSSFRow)rows[i]).getCell( j ).setCellValue( DataUtil.retained((( CurveBean )tmp.get( countD )).getD(),2) ) ;
								if (((CurveBean) tmp.get(countD)).getD().equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (((CurveBean) tmp.get(countD)).getD().equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countD ++ ;
						}
						else if( i % 8 == 5 || i % 8 == 6 ){
							if( countA >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countA )).getA()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								((HSSFRow)rows[i]).getCell( j ).setCellValue( DataUtil.retained((( CurveBean )tmp.get( countA )).getA(),2) ) ;
								if (((CurveBean) tmp.get(countA)).getA().equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (((CurveBean) tmp.get(countA)).getA().equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countA ++ ;						
						}
						else if( i % 8 == 7 || i % 8 == 0 ){
							if( countB >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countB )).getB()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								((HSSFRow)rows[i]).getCell( j ).setCellValue( DataUtil.retained((( CurveBean )tmp.get( countB )).getB(),2) ) ;
								if (((CurveBean) tmp.get(countB)).getB().equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (((CurveBean) tmp.get(countB)).getB().equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countB ++ ;							
						}
						else if ( i % 8 == 1 || i % 8 == 2) {
							if( countC >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countC )).getC()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{	
								((HSSFRow)rows[i]).getCell( j ).setCellValue( DataUtil.retained((( CurveBean )tmp.get( countC )).getC(),2) ) ;
								if (((CurveBean) tmp.get(countC)).getC().equals(maxArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
								if (((CurveBean) tmp.get(countC)).getC().equals(minArr[i/2 + i%2]))
									((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
							}
							countC ++ ;							
							if( countC >= tmp.size( ) && j == 14 ) break outer ;
						}
					if( i % 8 == 3 && j == 3 ){
						String sDate = ( new SimpleDateFormat("yyyy/MM/dd")).format( (( CurveBean )tmp.get( countD-1 )).getFreezeTime() ) ;
						((HSSFRow)rows[i]).createCell( 0 ).setCellValue( sDate ) ;
						((HSSFRow)rows[i]).getCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+2]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+3]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+4]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+5]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+6]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+7]).createCell( 0 ).setCellStyle( style ) ;
					}
					
					if( i % 8 == 3 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "总" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 8 == 5 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "A相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 8 == 7 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "B相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 8 == 1 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "C相" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					
					if( i % 8 == 4 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					if( i % 8 == 6 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 8 == 0 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 8 == 2 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-7, (short) 0, i, (short) 0));
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
				}
			}	
		}
		else if( checkType == CurveBean.CURVE_TYPE_D ){
            List<CurveBean> tmp = ( List<CurveBean> )result.get( "chartData" ) ;

			sheet1.setColumnWidth((short)3,(short)10*256);
			Object[] rows = new Object[tmp.size()*4/24 + 10];
			for( int i = 0 ; i < rows.length; i ++ ){
				rows[i] = sheet1.createRow( i ) ;
			}
			((HSSFRow)rows[0]).createCell(0).setCellValue( "用户：" + ledgerName ) ;
			
			((HSSFRow)rows[1]).createCell(0).setCellValue( "日期" ) ;
			((HSSFRow)rows[1]).createCell(3).setCellValue( title ) ;
			((HSSFRow)rows[1]).getCell(0).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).getCell(3).setCellStyle(titlestyle);
			
			for( int i = 3 ; i < 15 ; i ++ ){
				((HSSFRow)rows[2]).createCell( i ).setCellValue( ( i - 3 ) + ":00" ) ;
				((HSSFRow)rows[2]).getCell( i ).setCellStyle(titlestyle);
			}
			((HSSFRow)rows[2]).getCell( 3 ).setCellValue( "0:00(12)" ) ;
			((HSSFRow)rows[2]).getCell(3).setCellStyle(titlestyle);
			
			((HSSFRow)rows[1]).createCell( 1 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 2 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 4 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 5 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 6 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 7 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 8 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 9 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 10 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 11 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 12 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 13 ).setCellStyle(titlestyle);
			((HSSFRow)rows[1]).createCell( 14 ).setCellStyle(titlestyle);

			sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 2));
			sheet1.addMergedRegion(new Region(1, (short) 3, 1, (short) 14));

			//开始写表格内容
			outer :
				for( int i = 3 , countA = 0 , countB = 0 ; i < rows.length ; i ++ ){
					for( int j = 3 ; j < 15 ; j ++ ){
						((HSSFRow)rows[i]).createCell(j).setCellStyle(style);
						if( i % 4 == 3 || i % 4 == 0 ){
							if( countA >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countA )).getA()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.2f" , (( CurveBean )tmp.get( countA )).getA()  ) ) ;
							}
							countA ++ ;
						}
						else if( i % 4 == 1 || i % 4 == 2 ){
							if( countB >= tmp.size( ) ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							} 
							else if(DataUtil.parseDouble2BigDecimal((( CurveBean )tmp.get( countB )).getB()) == null ){
								((HSSFRow)rows[i]).getCell( j ).setCellValue( "--" ) ;
							}
							else{
								((HSSFRow)rows[i]).getCell( j ).setCellValue( String.format("%.2f" , (( CurveBean )tmp.get( countB )).getB()  ) ) ;
							}
							countB ++ ;
							if( countB >= tmp.size() && j == 14) break outer;
						}
					if( i % 4 == 3 && j == 3 ){
						String sDate = ( new SimpleDateFormat("yyyy/MM/dd")).format( (( CurveBean )tmp.get( countA-1 )).getFreezeTime() ) ;
						((HSSFRow)rows[i]).createCell( 0 ).setCellValue( sDate ) ;
						((HSSFRow)rows[i]).getCell( 0 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+2]).createCell( 0 ).setCellStyle( style ) ;
						((HSSFRow)rows[i+3]).createCell( 0 ).setCellStyle( style ) ;
					}
					
					if( i % 4 == 3 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "有功需量" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					else if( i % 4 == 1 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 1 ).setCellValue( "无功需量" ) ;
						((HSSFRow)rows[i]).getCell( 1 ).setCellStyle(style);
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "AM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						((HSSFRow)rows[i+1]).createCell( 1 ).setCellStyle( style ) ;
					}
					
					else if( i % 4 == 0 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
					else if( i % 4 == 2 && j == 3 ){
						((HSSFRow)rows[i]).createCell( 2 ).setCellValue( "PM" ) ;
						((HSSFRow)rows[i]).getCell( 2 ).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i-3, (short) 0, i, (short) 0));
						sheet1.addMergedRegion(new Region(i-1, (short) 1, i, (short) 1));
					}
				}
			}
		}
		else if (checkType == CurveBean.CURVE_TYPE_FREQ) {
            List<CurveBean> tmp = (List<CurveBean>) result.get("chartData");

			sheet1.setColumnWidth((short) 2, (short) 10 * 256);
			Object[] rows = new Object[tmp.size()*2/24 + 10];
			for (int i = 0; i < rows.length; i++) {
				rows[i] = sheet1.createRow(i);
			}

			((HSSFRow) rows[0]).createCell(0).setCellValue("用户：" + ledgerName);
			((HSSFRow) rows[1]).createCell(0).setCellValue("日期");
			((HSSFRow) rows[1]).createCell(2).setCellValue(title);
			((HSSFRow) rows[1]).getCell(0).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).getCell(2).setCellStyle(titlestyle);

			for (int i = 2; i < 14; i++) {
				((HSSFRow) rows[2]).createCell(i).setCellValue((i - 2) + ":00");
				((HSSFRow) rows[2]).getCell(i).setCellStyle(titlestyle);
			}
			((HSSFRow) rows[2]).getCell(2).setCellValue("0:00(12)");
			((HSSFRow) rows[2]).getCell(3).setCellStyle(titlestyle);

			((HSSFRow) rows[1]).createCell(1).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(3).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(4).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(5).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(6).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(7).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(8).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(9).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(10).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(11).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(12).setCellStyle(titlestyle);
			((HSSFRow) rows[1]).createCell(13).setCellStyle(titlestyle);

			sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 1));
			sheet1.addMergedRegion(new Region(1, (short) 2, 1, (short) 13));

			outer: for (int i = 3, count = 0; i < rows.length; i++) {
				for (int j = 2; j < 14; j++) {
					((HSSFRow) rows[i]).createCell(j).setCellStyle(style);
					// 设置数据
					if (count >= tmp.size()) {
						((HSSFRow) rows[i]).getCell(j).setCellValue("--");
					} else {
						if (DataUtil.parseDouble2BigDecimal(((CurveBean) tmp.get(count)).getD()) == null) {
							((HSSFRow) rows[i]).getCell(j).setCellValue("--");
						} else {
							((HSSFRow) rows[i]).getCell(j).setCellValue(((CurveBean) tmp.get(count)).getD());
						}
						count++;
					}
					// 设置行首日期
					if (i > 2 && i % 2 == 1 && j == 2) {
						String sDate = (new SimpleDateFormat("yyyy/MM/dd"))
								.format(((CurveBean) tmp.get(count - 1)).getFreezeTime());
						((HSSFRow) rows[i]).createCell(0).setCellValue(sDate);
						((HSSFRow) rows[i]).createCell(1).setCellValue("AM");
						((HSSFRow) rows[i]).getCell(0).setCellStyle(style);
						((HSSFRow) rows[i]).getCell(1).setCellStyle(style);

						((HSSFRow) rows[i + 1]).createCell(0).setCellStyle(style);
					}
					if (i > 2 && i % 2 == 0 && j == 2) {
						((HSSFRow) rows[i]).createCell(1).setCellValue("PM");
						((HSSFRow) rows[i]).getCell(1).setCellStyle(style);
						sheet1.addMergedRegion(new Region(i - 1, (short) 0, i, (short) 0));
					}
					if (count >= tmp.size() && j == 13)
						break outer;
				}
			}
		}
        if( checkType == CurveBean.CURVE_TYPE_WATER ){
            List<CurveBean> tmp = (List<CurveBean>) result.get("chartData");

            //定义最值变量
            double max = new BigDecimal(Double.MAX_VALUE).negate().doubleValue() , min = Double.MAX_VALUE;
            double[] maxArr = new double[tmp.size()*2/24];
            double[] minArr = new double[tmp.size()*2/24];

            sheet1.setColumnWidth((short) 2, (short) 10 * 256);
            Object[] rows = new Object[tmp.size()*2/24 + 10];
            for (int i = 0; i < rows.length; i++) {
                rows[i] = sheet1.createRow(i);
            }

            ((HSSFRow) rows[0]).createCell(0).setCellValue("用户：" + ledgerName);
            ((HSSFRow) rows[1]).createCell(0).setCellValue("日期");
            ((HSSFRow) rows[1]).createCell(2).setCellValue(title);
            ((HSSFRow) rows[1]).getCell(0).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).getCell(2).setCellStyle(titlestyle);

            for (int i = 2; i < 14; i++) {
                ((HSSFRow) rows[2]).createCell(i).setCellValue((i - 2) + ":00");
                ((HSSFRow) rows[2]).getCell(i).setCellStyle(titlestyle);
            }
            ((HSSFRow) rows[2]).getCell(2).setCellValue("0:00(12)");
            ((HSSFRow) rows[2]).getCell(3).setCellStyle(titlestyle);

            ((HSSFRow) rows[1]).createCell(1).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(3).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(4).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(5).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(6).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(7).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(8).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(9).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(10).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(11).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(12).setCellStyle(titlestyle);
            ((HSSFRow) rows[1]).createCell(13).setCellStyle(titlestyle);

            sheet1.addMergedRegion(new Region(0, (short) 0, 0, (short) 1));
            sheet1.addMergedRegion(new Region(1, (short) 0, 2, (short) 1));
            sheet1.addMergedRegion(new Region(1, (short) 2, 1, (short) 13));

            //取最大值和最小值
            outer :
            for( int i = 3 , countD = 0; i < rows.length; i ++ ){
                for( int j = 3 ; j < 15 ; j ++ ){
                    if( countD < tmp.size( ) && (( CurveBean )tmp.get( countD )).getD() != null){
                        if ((( CurveBean )tmp.get( countD )).getD() > max)
                        	max = (( CurveBean )tmp.get( countD )).getD();
                        if ((( CurveBean )tmp.get( countD )).getD() < min)
                        	min = (( CurveBean )tmp.get( countD )).getD();
                    }
                    if ( j == 14 && i % 2 == 0){
                        maxArr[i/2] = max;
                        minArr[i/2] = min;
                        max = new BigDecimal(Double.MAX_VALUE).negate().doubleValue();
                        min = Double.MAX_VALUE;
                    }
                    countD ++ ;
                    if( countD >= tmp.size( ) && j == 14 ) break outer ;
                }
            }

            //写excel
            outer: for (int i = 3, count = 0; i < rows.length; i++) {
                for (int j = 2; j < 14; j++) {
                    ((HSSFRow) rows[i]).createCell(j).setCellStyle(style);
                    // 设置数据
                    if (count >= tmp.size()) {
                        ((HSSFRow) rows[i]).getCell(j).setCellValue("--");
                    } else {
                        if (DataUtil.parseDouble2BigDecimal(((CurveBean) tmp.get(count)).getD()) == null) {
                            ((HSSFRow) rows[i]).getCell(j).setCellValue("--");
                        } else {
                            ((HSSFRow) rows[i]).getCell( j ).setCellValue( (( CurveBean )tmp.get( count )).getD() ) ;
                            if (((CurveBean) tmp.get(count)).getD().equals(maxArr[i/2 + i%2]))
                            	((HSSFRow)rows[i]).getCell( j ).setCellStyle(redfontstyle);
                            if (((CurveBean) tmp.get(count)).getD().equals(minArr[i/2 + i%2]))
                            	((HSSFRow)rows[i]).getCell( j ).setCellStyle(greenfontstyle);
                        }
                        count++;
                    }
                    // 设置行首日期
                    if (i > 2 && i % 2 == 1 && j == 2) {
                        String sDate = (new SimpleDateFormat("yyyy/MM/dd"))
                                .format(((CurveBean) tmp.get(count - 1)).getFreezeTime());
                        ((HSSFRow) rows[i]).createCell(0).setCellValue(sDate);
                        ((HSSFRow) rows[i]).createCell(1).setCellValue("AM");
                        ((HSSFRow) rows[i]).getCell(0).setCellStyle(style);
                        ((HSSFRow) rows[i]).getCell(1).setCellStyle(style);

                        ((HSSFRow) rows[i + 1]).createCell(0).setCellStyle(style);
                    }
                    if (i > 2 && i % 2 == 0 && j == 2) {
                        ((HSSFRow) rows[i]).createCell(1).setCellValue("PM");
                        ((HSSFRow) rows[i]).getCell(1).setCellStyle(style);
                        sheet1.addMergedRegion(new Region(i - 1, (short) 0, i, (short) 0));
                    }
                    if (count >= tmp.size() && j == 13)
                        break outer;
                }
            }
        }
		
		try {
			output.flush();
			wb.write(output);
			output.close();
		} catch (IOException e) {
            Log.error(this.getClass() + ".getEleExcel()");
		}
	}

	@Override
	public String getTitleByCurveType(int curveType) {
		String title = "";
		switch( curveType ) {
			case 1:
				title = "电压（V）";
				break;
			case 2:
				title = "电流（A）";
				break;
			case 3:
				title = "有功功率（kW）";
				break;
			case 4:
				title = "无功功率（kVar）";
				break;
			case 5:
				title = "功率因数";
				break;
			case 6:
				title = "曲线电量（kWh）";
				break;
			case 7:
				title = "曲线需量（kW）";
				break;
            case 8:
                title = "电压相位角（度）";
                break;
            case 9:
                title = "电流相位角（度）";
                break;
            case 10:
            	title = "电网频率（Hz）";
            	break;
            case 11:
                title = "水量（m³）";
                break;
		}
		return title;
	}

	@Override
	public List<Map<String, Object>> getVolMeterIds(Map<String, Object> params) {
		List<Map<String, Object>> volMeters = voltCurrPowerMapper.getVolMeters(params);
		if(volMeters != null && volMeters.size()>1){
			return volMeters;
		}
		return null;
	}

	@Override
	public Integer getUserYMin(Long accountId) {
		return voltCurrPowerMapper.getUserYMin(accountId);
	}

	@Override
	public void setUserYMin(Long accountId, Integer yMin) {
		voltCurrPowerMapper.setUserYMin(accountId, yMin);
	}
	
	@Override
	public Integer queryCommMode(Long objId) {
		// 得到电源接线方式1,三相三线;2,三相四线;3:单相表
		Integer commMode = phoneMapper.queryCommMode(objId);
		return commMode;
	}
}
