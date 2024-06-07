package com.linyang.energy.service.impl;

import com.linyang.common.web.common.Log;
import com.linyang.energy.mapping.authmanager.RateBeanMapper;
import com.linyang.energy.mapping.energydataquery.ERateQueryMapper;
import com.linyang.energy.mapping.merchants.MerchantsMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.MerchantBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.model.RateSectorBean;
import com.linyang.energy.service.MerchantsService;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.util.DoubleUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static com.linyang.energy.utils.DateUtil.getPreMonthLastDay;
import static com.linyang.energy.utils.DateUtil.getPreMonthLastDay2;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 10:43 2019/7/15
 * @ Description：class说明
 * @ Modified By：:dingy
 * @Version: $version$
 */
@Service
public class MerchantsServiceImpl implements MerchantsService {
	
	@Autowired
	private MerchantsMapper merchantsMapper;
	@Autowired
	private ERateQueryMapper eRateQueryMapper;
	@Autowired
	private RateBeanMapper rateBeanMapper;
	
	/**
	 * 数据保留位数
	 */
	private final String KEEP_FEW = "0.00";
	
	/**
	 * 获取商户数据
	 * @author catkins
	 * @param beginTime
	 * @param endTime
	 * @param ledgerId
	 * @param dataType
	 * @return java.util.List<com.linyang.energy.model.MerchantBean>
	 * @exception
	 * @date 2019/7/16 11:01
	 */
	@Override
	public Map<String,Object> getMerchants(Date beginTime, Date endTime,long ledgerId,int dataType,int instead,Date dateTime,long parLedgerId,String userNo,int payStatus,String ledgerName) {
		
		Map<String,Object> result = new HashMap<>( 0 );
		
		Set<List<RateSectorBean>> rateSet = new HashSet<>( 0 );
		
		//犹豫不同能管对象类型会相互干扰,故重新放一个新集合里
		List<MerchantBean> resultBean = new ArrayList<>( 0 );
		
		
		//得到商户下所有最下级能管对象
		List<MerchantBean> merchants = merchantsMapper.getMerchants( ledgerId,userNo,ledgerName );
		
		//得到能管对象下该类型是否全都有费率
		//Integer merchantsNum = merchantsMapper.getMerchantsNum( ledgerId, userNo, dataType ,ledgerName );
		
		//两个不相等的话,相当于这个能管对象下还有没设置费率的能管对象(排除电)
//		if ( dataType != 1 ) {
//			if(merchants.size() != 1 && merchants.size() != merchantsNum){
//				result.put( "merFee", "该能管对象下有对象没有设置费率" );
//			} else if(merchants.size() == 1 && merchants.size() != merchantsNum) {
//				result.put( "merFee", "该能管对象没有设置费率" );
//			}
//		}
		
		for (MerchantBean bean:merchants) {
			
			//获取所有能管对象下的采集点
			List<MeterBean> childDcp = eRateQueryMapper.getComputeMetersWithMeterType(bean.getLedgerId(),Short.parseShort( String.valueOf( dataType ) ));
			bean.setSize( childDcp.size() );
			if( childDcp.isEmpty() ) {
				continue;
			}
			
			//可能出现继承父费率情况
			if( dataType == 1 && bean.getRateId() == 0l ){// 除了电费率,其它可能为空
				this.queryParentRateId(1,bean,dataType);
				if( (bean.getRateId() == 0l || bean.getRateId() == null) && bean.getInherit() == 0 ){
					result.put( "merFee", "该能管对象没有设置费率" );
				}
				if( (bean.getRateId() == 0l || bean.getRateId() == null) && bean.getInherit() == 0 ){
					result.put( "merFee", "该能管对象下有对象没有设置费率" );
				}
			} else if ( dataType == 2 && bean.getwRateId() == null ) {
				this.queryParentRateId(1,bean,dataType);
				if( bean.getwRateId() == null && bean.getInherit() == 0 ){
					result.put( "merFee", "该能管对象没有设置费率" );
				}
				if( bean.getwRateId() == null && bean.getInherit() == 1 ){
					result.put( "merFee", "该能管对象下有对象没有设置费率" );
				}
			} else if ( dataType == 3 && bean.getgRateId() == null ){
				this.queryParentRateId(1,bean,dataType);
				if( bean.getgRateId() == null && bean.getInherit() == 0 ){
					result.put( "merFee", "该能管对象没有设置费率" );
				}
				if( bean.getgRateId() == null && bean.getInherit() == 1 ){
					result.put( "merFee", "该能管对象下有对象没有设置费率" );
				}
			} else if ( dataType == 4 && bean.gethRateId() == null ){
				this.queryParentRateId(1,bean,dataType);
				if( bean.gethRateId() == null && bean.getInherit() == 0 ){
					result.put( "merFee", "该能管对象没有设置费率" );
				}
				if( bean.gethRateId() == null && bean.getInherit() == 0 ){
					result.put( "merFee", "该能管对象下有对象没有设置费率" );
				}
			}
			
			List<RateSectorBean> rateSectorList = null;
			if (bean.getRateId() != null && bean.getRateId() != 0 && dataType == 1 ) {	//电
				rateSectorList = this.rateBeanMapper.getSectorData( bean.getRateId() );
				rateSet.add( rateSectorList );
			} else if (bean.getwRateId() != null && bean.getwRateId() != 0 && dataType == 2 ) { // 水
				rateSectorList = this.rateBeanMapper.getSectorData( bean.getwRateId() );
				rateSet.add( rateSectorList );
			} else if (bean.getgRateId() != null && bean.getgRateId() != 0 && dataType == 3 ) {	// 气
				rateSectorList = this.rateBeanMapper.getSectorData( bean.getgRateId() );
				rateSet.add( rateSectorList );
			} else if (bean.gethRateId() != null && bean.gethRateId() != 0 && dataType == 4 ) {	//热(暂未使用)
				rateSectorList = this.rateBeanMapper.getSectorData( bean.gethRateId() );
				rateSet.add( rateSectorList );
			}
			
			List<Map<String, Object>> rateList = new ArrayList<>();//分费率
			for (int i = 0 ; i < childDcp.size() ; i++) {
				Double pt = childDcp.get( i ).getPt();
				Double ct = childDcp.get( i ).getCt();
				
				//插入倍率
				Map<String, Object> rateMap = null;
				if (dataType == 1) {
					rateMap = this.dataProcess( beginTime, endTime, childDcp.get( i ).getMeterId(), 0, "总", pt, ct, dataType,0 ,instead);
				} else if (rateSectorList != null && rateSectorList.size() > 0 && dataType != 1 ) {
					for (RateSectorBean rateSectorBean : rateSectorList) {
						rateMap = new HashMap<>( 0 );
						int rateNum = (int) rateSectorBean.getSectorId();
						String rateName = rateSectorBean.getSectorName();
						double rateValue = rateSectorBean.getRateValue();
						rateMap =  dataProcess(beginTime, endTime, childDcp.get( i ).getMeterId(), rateNum, rateName, pt, ct,dataType,rateValue,instead) ;
					}
				}
				
				if(rateMap == null)
					continue;
				
				
				if ( dataType == 1 ) {
					rateMap.put( "ratio" , pt*ct );
				} else if ( dataType == 2 ) {
					rateMap.put( "ratio" , pt );
				} else {
					rateMap.put( "ratio" , 1 );
				}
				
				//插入测量点名称
				rateMap.put( "meterName" , childDcp.get( i ).getMeterName() );
				
				rateList.add(rateMap);
				
				if(rateSectorList != null && rateSectorList.size() > 0 && dataType == 1 ) {
					for (RateSectorBean rateSectorBean : rateSectorList) {
						rateMap = new HashMap<>( 0 );
						int rateNum = (int) rateSectorBean.getSectorId();
						String rateName = rateSectorBean.getSectorName();
						double rateValue = rateSectorBean.getRateValue();
						rateMap =  dataProcess(beginTime, endTime, childDcp.get( i ).getMeterId(), rateNum, rateName, pt, ct,dataType,rateValue,instead) ;
						rateList.add(rateMap);
					}
				}
			}
			//查询缴费状态
			Map<String, Object> payCostMap = merchantsMapper.queryPayCost( bean.getLedgerId(), beginTime, endTime );
			
			//把查询到的数据放进bean
			if (payCostMap != null && payCostMap.containsKey( "PAY_STATUS" )) {
				bean.setValues(  beginTime, endTime,Integer.parseInt( payCostMap.get( "PAY_STATUS" ).toString() ),rateList ,endTime.before( new Date() )?1:-1 );
			} else {
				bean.setValues(  beginTime, endTime,0,rateList ,endTime.before( new Date() )?1:-1 );
			}
			//查询父节点名称
			String parName = this.merchantsMapper.queryParName( parLedgerId, bean.getLedgerId() );
			//修改bean内容
			this.changeBean( bean , parName ,ledgerId,parLedgerId);
			// 修改完内容的bean存放进新集合里
			resultBean.add( bean );
		}
		result.put( "rateSet" , rateSet );
		result.put( "datas", resultBean );
		return result;
	}
	
	/**
	 * 日电能示值,水量,气量,热量-数据处理
	 * @author catkins
	 * @param beginTime		开始时间
	 * @param endTime		结束时间
	 * @param meterId		采集点id
	 * @param rateNum		费率号
	 * @param rateName		费率名称
	 * @param pt			倍率(水用pt)
	 * @param ct			倍率(电用pt*ct)
	 * @param dataType		数据类型(1.电,2.水,3.气,4.热)
	 * @param rateValue		费率价格
	 * @param instead		替换数据选项(1.之前最近,2.之后最近)
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/7/22 9:35
	 */
	private Map<String, Object> dataProcess(Date beginTime, Date endTime, Long meterId, int rateNum, String rateName, Double pt, Double ct,int dataType,double rateValue,int instead) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rateNum", rateNum);//费率ID
		if(dataType == 1){
			Map<String,Object> startFaeRate_old = null;
			Map<String,Object> endFaeRate_old = null;
			Double startFaeRate_new = null;
			Double endFaeRate_new = null;
			if(rateNum == 0) {
				startFaeRate_old = this.merchantsMapper.queryTotalFaeRate(meterId, getPreMonthLastDay2(beginTime),instead);
				endFaeRate_old = this.merchantsMapper.queryTotalFaeRate(meterId, endTime,instead);
				
				startFaeRate_new = this.merchantsMapper.getViewDayETotal(meterId, getPreMonthLastDay2(beginTime),instead);
				endFaeRate_new = this.merchantsMapper.getViewDayETotal(meterId, endTime,instead);
			}else {
				startFaeRate_old = this.merchantsMapper.queryFaeRate(meterId, getPreMonthLastDay2(beginTime), rateNum,instead);
				endFaeRate_old = this.merchantsMapper.queryFaeRate(meterId, endTime, rateNum,instead);
				
				startFaeRate_new = this.merchantsMapper.getViewDayERate(meterId, getPreMonthLastDay2(beginTime), rateNum,instead);
				endFaeRate_new = this.merchantsMapper.getViewDayERate(meterId, endTime, rateNum,instead);
			}
			
			map.put("rateName", rateName);//费率名称
			
			//起始示值+是否是替代数据的标志
			if( startFaeRate_old != null && startFaeRate_old.get("FEEVALUE") != null) {
				map.put( "ratestart", startFaeRate_old.get( "FEEVALUE" ).toString() + "_" + startFaeRate_old.get( "FLAG" ).toString() );
			} else {
				map.put( "ratestart", "-" );
			}
			//结束示值+是否是替代数据的标志
			if ( endFaeRate_old != null && endFaeRate_old.get("FEEVALUE") != null) {
				map.put("rateend", endFaeRate_old.get("FEEVALUE")+"_"+endFaeRate_old.get( "FLAG" ).toString());
			} else {
				map.put( "rateend", "-" );
			}
			
			if(startFaeRate_new != null){
				map.put("ratestart_new", startFaeRate_new);
			} else {
				map.put("ratestart_new", "-");
			}
			
			if(startFaeRate_new != null){
				map.put("rateend_new", endFaeRate_new);
			} else {
				map.put("rateend_new", "-");
			}
			
			map.put("energy", 0);//电能量
			map.put( "totalFee" , 0 );
			
//			if(startFaeRate_new != null && endFaeRate_new != null) {
			if(startFaeRate_old != null && startFaeRate_old.get("FEEVALUE") != null &&
					endFaeRate_old != null && endFaeRate_old.get("FEEVALUE") != null) {
				Double energy =  DataUtil.doubleMultiply(DataUtil.doubleSubtract(Double.parseDouble( endFaeRate_old.get("FEEVALUE").toString() ),
						Double.parseDouble( startFaeRate_old.get("FEEVALUE").toString() )), pt * ct);//Double energy = (endFaeRate_new*100 - startFaeRate_new*100) * pt * ct /100;
				if (rateNum == 0) {
					map.put("energy", keepDigits(String.valueOf( energy ),KEEP_FEW));//电能量
				}
				map.put( "totalFee" ,  keepDigits(String.valueOf( energy*rateValue ),KEEP_FEW) );
			}
		}else if(dataType == 2){
			Map<String,Object> startValue = this.merchantsMapper.queryEWater(meterId, getPreMonthLastDay2(beginTime),instead);
			Map<String,Object> endValue= this.merchantsMapper.queryEWater(meterId, endTime,instead);
			map.put("rateName", "水");//费率名称
			if ( startValue != null && startValue.get("WFLOW_VALUE") != null) {
				map.put( "ratestart", startValue.get( "WFLOW_VALUE" ).toString() + "_" + startValue.get( "FLAG" ).toString() );//起始示值+是否是替代数据的标志
			} else {
				map.put( "ratestart", "-");
			}
			if ( endValue != null && endValue.get("WFLOW_VALUE") != null) {
				map.put( "rateend", endValue.get( "WFLOW_VALUE" ).toString() + "_" + endValue.get( "FLAG" ).toString() );//结束示值+是否是替代数据的标志
			} else {
				map.put( "rateend", "-");
			}
			double rate = pt;
			map.put("energy", 0 );//电能量
			map.put( "totalFee" , 0 );
			if(startValue != null && startValue.get("WFLOW_VALUE") != null
					&& endValue != null && endValue.get("WFLOW_VALUE") != null) {
				Double energy = DataUtil.doubleMultiply(DataUtil.doubleSubtract(Double.parseDouble( endValue.get( "WFLOW_VALUE" ).toString() ),
						Double.parseDouble( startValue.get( "WFLOW_VALUE" ).toString() )), rate);
				map.put("energy", keepDigits(String.valueOf( energy ),KEEP_FEW));//电能量
				map.put( "totalFee" ,  keepDigits(String.valueOf( energy*rateValue ),KEEP_FEW) );
			}
		}else if(dataType == 3){
			Map<String,Object> startValue = this.merchantsMapper.queryEGas(meterId, getPreMonthLastDay2(beginTime),instead);
			Map<String,Object> endValue= this.merchantsMapper.queryEGas(meterId, endTime,instead);
			map.put("rateName", "气");//费率名称
			if ( startValue != null && startValue.get("GFLOW_VALUE") != null) {
				map.put( "ratestart", startValue.get( "GFLOW_VALUE" ).toString() + "_" + startValue.get( "FLAG" ).toString() );//起始示值+是否是替代数据的标志
			} else {
				map.put( "ratestart", "-");
			}
			if ( endValue != null && endValue.get("GFLOW_VALUE") != null) {
				map.put( "rateend", endValue.get( "GFLOW_VALUE" ).toString() + "_" + endValue.get( "FLAG" ).toString() );//结束示值+是否是替代数据的标志
			} else {
				map.put( "rateend", "-");
			}
			int rate = 1;
			map.put("energy", 0);//电能量
			map.put( "totalFee" , 0 );
			if(startValue != null && startValue.get("GFLOW_VALUE") != null
					&& endValue != null && endValue.get("GFLOW_VALUE") != null) {
				Double energy = DataUtil.doubleMultiply(DataUtil.doubleSubtract(Double.parseDouble( endValue.get( "GFLOW_VALUE" ).toString() ), Double.parseDouble( startValue.get( "GFLOW_VALUE" ).toString() )), rate);
				map.put("energy", keepDigits(String.valueOf( energy ),KEEP_FEW));//电能量
				map.put( "totalFee" ,  keepDigits(String.valueOf( energy*rateValue ),KEEP_FEW) );
			}
		} else if(dataType == 4){		//热暂时没有
			Map<String,Object> startValue = this.merchantsMapper.queryEHeat(meterId, getPreMonthLastDay2(beginTime),instead);
			Map<String,Object> endValue= this.merchantsMapper.queryEHeat(meterId, endTime,instead);
			map.put("rateName", "热");//费率名称
			if ( startValue != null && startValue.get("HEAT_VALUE") != null) {
				map.put( "ratestart", startValue.get( "HEAT_VALUE" ).toString() + "_" + startValue.get( "FLAG" ).toString() );//起始示值
			} else {
				map.put( "ratestart", "-");
			}
			if ( endValue != null && endValue.get("HEAT_VALUE") != null) {
				map.put( "rateend", endValue.get( "HEAT_VALUE" ).toString() + "_" + endValue.get( "FLAG" ).toString() );//结束示值
			} else {
				map.put( "rateend", "-");
			}
			
			int rate = 1;
			map.put("energy", 0);//电能量
			map.put( "totalFee" , 0 );
			if(startValue != null && startValue.get("HEAT_VALUE") != null
					&& endValue != null && endValue.get("HEAT_VALUE") != null) {
				Double energy = DataUtil.doubleMultiply(DataUtil.doubleSubtract(Double.parseDouble( endValue.get( "HEAT_VALUE" ).toString() ), Double.parseDouble( startValue.get( "HEAT_VALUE" ).toString() )), rate);
				map.put("energy", keepDigits(String.valueOf( energy ),KEEP_FEW));//电能量
				map.put( "totalFee" ,  keepDigits(String.valueOf( energy*rateValue ),KEEP_FEW) );
			}
		}
		return map;
	}
	
	/**
	 * 获得父节点层级
	 * @param ledgerId
	 * @return
	 */
	@Override
	public Integer queryLevel(long ledgerId,long parLedgerId) {
		return this.merchantsMapper.queryLevel( ledgerId,parLedgerId );
	}
	
	/**
	 * 存储缴费状态
	 * @param ledgerId 能管对象id
	 * @param dateTime 结算时间(获取服务器时间)
	 * @return
	 */
	@Override
	public int doPayCost(long ledgerId, Date dateTime) {
		return merchantsMapper.doPayCost( ledgerId,dateTime );
	}
	
	/**
	 * 查询出能管对象父节点名称,并改变bean的内容
	 * @author catkins
	 * @param bean
	 * @param parName
	 * @return void
	 * @exception
	 * @date 2019/7/18 15:29
	 */
	private void changeBean(MerchantBean bean,String parName,long ledgerId,long parLedgerId){
		Integer level = this.queryLevel( ledgerId,parLedgerId );
		parName = parName.substring( 1,parName.length() );
		String[] split = parName.split( ">" );
		int length = split.length;
		if(length == level){
			if (length > 1) {
				bean.setFirstName( split[1] );
			} else {
				bean.setFirstName( "-" );
			}
			if (length > 2) {
				bean.setSecondName( split[2] );
			} else {
				bean.setSecondName( "-" );
			}
			if ( length > 3 ) {
				bean.setThirdName( split[3] );
			} else {
				bean.setThirdName( "-" );
			}
		} else if (length+1 == level){
			bean.setFirstName( "-" );
			if (length > 1) {
				bean.setSecondName( split[1] );
			} else {
				bean.setSecondName( "-" );
			}
			if ( length > 2 ) {
				bean.setThirdName( split[2] );
			} else {
				bean.setThirdName( "-" );
			}
		} else if (length+2 == level){
			bean.setFirstName( "-" );
			bean.setSecondName( "-" );
			if (length > 1) {
				bean.setThirdName( split[1] );
			} else {
				bean.setThirdName( "-" );
			}
		}
	}
	
	/**
	 * 保留两位小数
	 * @author catkins
	 * @param dataNumber
	 * @return java.lang.String
	 * @exception
	 * @date 2019/7/29 13:33
	 */
	private String keepDigits(String dataNumber,String pattern) {
		double num = Double.parseDouble( dataNumber );
		DecimalFormat df = null;
		if(pattern == null || pattern.equals( "" ))
			df = new DecimalFormat("#0.0000");
		else
			df = new DecimalFormat(pattern);
		return df.format( num );
	}
	
	/**
	 * 导出excel
	 * @author catkins
	 * @param name
	 * @param outputStream
	 * @param result
	 * @param title
	 * @return void
	 * @exception
	 * @date 2019/7/22 18:58
	 */
	@Override
	public void getExcel(String name, ServletOutputStream outputStream, Map<String, Object> result, int dataType,String beginTime,String endTime) {
		//先拿到重新整合之后的数据
		Map<String, Object> restData = this.restData( result, dataType );
		//拿到数据的所有key
		List<String> keySet = new ArrayList<>( restData.keySet() );
		HSSFWorkbook workbook = new HSSFWorkbook(  );
		for (int i = 0 ; i < keySet.size() ; i++) {
			this. createExcelSheet(workbook,"商户报表"+(i+1) ,(Map<String,Object>)restData.get( keySet.get( i ) ),dataType,beginTime,endTime);
		}
		try {
			outputStream.flush();
			workbook.write(outputStream);
			outputStream.close();
		} catch (IOException e) {
			Log.error(this.getClass() + ".getExcel()--无法导出商户报表数据");
		}
	}
	
	/**
	 * 处理导出数据
	 * @author catkins
	 * @param result
	 * @param dataType
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/7/23 9:05
	 */
	private Map<String,Object> restData(Map<String, Object> result,int dataType){
		
		Map<String,Object> rateMap = new HashMap<>( 0 );
		
		Map<String,Object> map = null;
		
		//费率set集合
		Set<List<RateSectorBean>> rateSet = (Set<List<RateSectorBean>>)result.get( "rateSet" );
		//费率set转成list
		List<List<RateSectorBean>> rateList = new ArrayList<>( rateSet );
		
		//数据集合
		List<MerchantBean> dataList = (List<MerchantBean>)result.get( "datas" );
		
		//新数据集合
		List<MerchantBean> dataList_new = null;
		
		// 把费率集合和数据集合按照费率号分类整合起来
		for (List<RateSectorBean> rateBeanList:rateList) {
			dataList_new = new ArrayList<>( 0 );
			
			map = new HashMap<>( 0 );
			
			for (MerchantBean merchantBean:dataList) {
				if (dataType == 1) {
					if( merchantBean.getRateId() != null && rateBeanList.get( 0 ).getRateId() == merchantBean.getRateId() ){
						dataList_new.add( merchantBean );
					}
				} else if (dataType == 2){
					if( merchantBean.getwRateId() != null && rateBeanList.get( 0 ).getRateId() == merchantBean.getwRateId() ){
						dataList_new.add( merchantBean );
					}
				} else if (dataType == 3){
					if( merchantBean.getgRateId() != null && rateBeanList.get( 0 ).getRateId() == merchantBean.getgRateId() ){
						dataList_new.add( merchantBean );
					}
				} else if (dataType == 4){
					if( merchantBean.gethRateId() != null && rateBeanList.get( 0 ).getRateId() == merchantBean.gethRateId() ){
						dataList_new.add( merchantBean );
					}
				}
			}
			
			map.put( "rateBeanList",rateBeanList );
			
			map.put( "dataList",dataList_new );
			
			rateMap.put( String.valueOf( rateBeanList.get( 0 ).getRateId() ),map );
		}
		
		return rateMap;
	}
	
	/**
	 *	生成单个工作表
	 * @param workbook
	 * @param sheetName
	 * @param dataMap
	 * @param dataType
	 * @param beginTime
	 * @param endTime
	 */
	public void createExcelSheet(HSSFWorkbook workbook, String sheetName, Map<String, Object> dataMap, int dataType, String beginTime, String endTime){
		
		// add or update method by catkins
		// date 2019/7/24
		// Modify the content: excel坐标的最大值(用于后面调用补样式的方法)
		int rowMax = 0;
		int cellMax = 0;
		//end
		
		try {
			
			// 首先拿到费率集合
			List<RateSectorBean> rateList = (List<RateSectorBean>)dataMap.get( "rateBeanList" );
			
			// 然后拿到数据集合
			List<MerchantBean> merList = (List<MerchantBean>)dataMap.get( "dataList" );
			
			HSSFSheet sheet = workbook.createSheet(sheetName);
			
			int columnSize = 0;
			if (dataType == 1) {
				columnSize = 10+2+rateList.size()*2;
			} else {
				columnSize = 10+rateList.size()*2;
			}
			
			//设置列宽
			for(int i = 0;i< columnSize;i++ ){
				sheet.setColumnWidth(i, 3600);
			}
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
			/**内容居中单元格样式*/
			HSSFCellStyle centerstyle = workbook.createCellStyle();
			centerstyle.setFont(font);
			centerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			centerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			centerstyle.setWrapText(true);
			centerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
			centerstyle.setBorderLeft((short) 1);
			centerstyle.setTopBorderColor(HSSFColor.BLACK.index);
			centerstyle.setBorderTop((short) 1);
			centerstyle.setRightBorderColor(HSSFColor.BLACK.index);
			centerstyle.setBorderRight((short) 1);
			centerstyle.setBottomBorderColor(HSSFColor.BLACK.index);
			centerstyle.setBorderBottom((short) 1);
			centerstyle.setFillForegroundColor(HSSFColor.WHITE.index);
			/**内容居左单元格样式*/
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
			/**跨行跨列无边框线样式*/
			HSSFCellStyle rowColstyle = workbook.createCellStyle();
			rowColstyle.setFont(font);
			rowColstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			rowColstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			rowColstyle.setWrapText(true);
			rowColstyle.setLeftBorderColor(HSSFColor.WHITE.index);
			rowColstyle.setBorderLeft((short) 1);
			rowColstyle.setTopBorderColor(HSSFColor.WHITE.index);
			rowColstyle.setBorderTop((short) 1);
			rowColstyle.setRightBorderColor(HSSFColor.BLACK.index);
			rowColstyle.setBorderRight((short) 1);
			rowColstyle.setBottomBorderColor(HSSFColor.WHITE.index);
			rowColstyle.setBorderBottom((short) 1);
			rowColstyle.setFillForegroundColor(HSSFColor.WHITE.index);
			/**字体红色无边框居左*/
			HSSFFont redFont = workbook.createFont();
			redFont.setFontName("宋体");
			redFont.setFontHeightInPoints((short) 9);
			redFont.setColor(HSSFColor.RED.index);
			HSSFCellStyle redStyle = workbook.createCellStyle();
			redStyle.setFont(redFont);
			redStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			redStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			redStyle.setWrapText(true);
			redStyle.setLeftBorderColor(HSSFColor.WHITE.index);
			redStyle.setBorderLeft((short) 1);
			redStyle.setTopBorderColor(HSSFColor.WHITE.index);
			redStyle.setBorderTop((short) 1);
			redStyle.setRightBorderColor(HSSFColor.BLACK.index);
			redStyle.setBorderRight((short) 1);
			redStyle.setBottomBorderColor(HSSFColor.WHITE.index);
			redStyle.setBorderBottom((short) 1);
			redStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			/**字体红色有边框居中*/
			HSSFCellStyle redCenterStyle = workbook.createCellStyle();
			redCenterStyle.setFont(redFont);
			redCenterStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			redCenterStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			redCenterStyle.setWrapText(true);
			redCenterStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			redCenterStyle.setBorderLeft((short) 1);
			redCenterStyle.setTopBorderColor(HSSFColor.BLACK.index);
			redCenterStyle.setBorderTop((short) 1);
			redCenterStyle.setRightBorderColor(HSSFColor.BLACK.index);
			redCenterStyle.setBorderRight((short) 1);
			redCenterStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			redCenterStyle.setBorderBottom((short) 1);
			redCenterStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			/**字体红色加粗有边框居中*/
			HSSFFont redBoldFont = workbook.createFont();
			redBoldFont.setFontName("宋体");
			redBoldFont.setFontHeightInPoints((short) 9);
			redBoldFont.setColor(HSSFColor.RED.index);
			redBoldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle redBoldStyle = workbook.createCellStyle();
			redBoldStyle.setFont(redBoldFont);
			redBoldStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			redBoldStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			redBoldStyle.setWrapText(true);
			redBoldStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			redBoldStyle.setBorderLeft((short) 1);
			redBoldStyle.setTopBorderColor(HSSFColor.BLACK.index);
			redBoldStyle.setBorderTop((short) 1);
			redBoldStyle.setRightBorderColor(HSSFColor.BLACK.index);
			redBoldStyle.setBorderRight((short) 1);
			redBoldStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			redBoldStyle.setBorderBottom((short) 1);
			redBoldStyle.setFillForegroundColor(HSSFColor.WHITE.index);
			
			/**创建标题*/
			HSSFRow row0 = sheet.createRow(0);
			for (int i = 0; i < columnSize; i++) {
				row0.createCell(i).setCellStyle(columnHeadStyle);
			}
			
			HSSFCell cell = row0.createCell(0);
			
			StringBuilder rateStr = new StringBuilder(  );
			rateStr.append( "费率价格 : " );
			for (RateSectorBean rateBean:rateList) {
				rateStr.append( rateBean.getSectorName() ).append( " : " ).append( rateBean.getRateValue() ).append( " , " );
			}
			cell.setCellValue(new HSSFRichTextString(rateStr.toString()));
			cell.setCellStyle(rowColstyle);
			
			//合并行
			CellRangeAddress range = new CellRangeAddress(0, 0, 0,columnSize-1);
			sheet.addMergedRegion(range);
			
			//列号
			int collNum = 0;
			
			
			/**创建第二行*/
			HSSFRow row1 = sheet.createRow(1);
			
			/**创建第三行*/
			HSSFRow row2 = sheet.createRow(2);
			
			
			cell = row1.createCell(collNum);
			cell.setCellValue(new HSSFRichTextString("上三级对象"));
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			cell.setCellStyle(columnHeadStyle);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "上二级对象" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "上一级对象" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "对象名称" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "户号" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "采集点" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "倍率" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "开始示值("+beginTime+")" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 1, collNum,collNum+rateList.size());
			sheet.addMergedRegion(range);
			
			//第三行
			if (dataType == 1) {
				cell = row2.createCell( collNum );
				cell.setCellValue( "总" );
				cell.setCellStyle(centerstyle);
				for (int i = 0,t = collNum+1 ; i < rateList.size() ; i++,t++){
					cell = row2.createCell( t );
					cell.setCellValue( rateList.get( i ).getSectorName() );
					cell.setCellStyle(centerstyle);
				}
				collNum+=1+rateList.size();
			} else {
				cell = row2.createCell( collNum );
				cell.setCellValue( rateList.get( 0 ).getSectorName() );
				cell.setCellStyle(centerstyle);
				collNum++;
			}
			
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "结束示值("+endTime+")" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 1, collNum,collNum+rateList.size());
			sheet.addMergedRegion(range);
			
			
			if (dataType == 1) {
				cell = row2.createCell( collNum );
				cell.setCellValue( "总" );
				cell.setCellStyle(centerstyle);
				for (int i = 0,t = collNum+1 ; i < rateList.size() ; i++,t++){
					cell = row2.createCell( t );
					cell.setCellValue( rateList.get( i ).getSectorName() );
					cell.setCellStyle(centerstyle);
				}
				collNum+=1+rateList.size();
			} else {
				cell = row2.createCell( collNum );
				cell.setCellValue( rateList.get( 0 ).getSectorName() );
				cell.setCellStyle(centerstyle);
				collNum++;
			}
			
			cell = row1.createCell( collNum );
			
			if ( dataType == 1 ) {
				cell.setCellValue( new HSSFRichTextString( "能耗(Kwh)" ) );
			} else if ( dataType == 2 ) {
				cell.setCellValue( new HSSFRichTextString( "水量(m³)" ) );
			} else if ( dataType == 3 ) {
				cell.setCellValue( new HSSFRichTextString( "气量(m³)" ) );
			} else if ( dataType == 4 ) {
				cell.setCellValue( new HSSFRichTextString( "热量(m³)" ) );
			}
			
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "总价" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			collNum++;
			
			cell = row1.createCell( collNum );
			cell.setCellValue( new HSSFRichTextString( "缴费状态" ) );
			cell.setCellStyle(columnHeadStyle);
			//合并行
			range = new CellRangeAddress(1, 2, collNum,collNum);
			sheet.addMergedRegion(range);
			
			//数据从第四行开始
			int rowNum = 3;
			
			//填写数据
			for (MerchantBean bean:merList) {
				//数据从第1列开始
				int cellNum = 0;
				
				//计算总价格
				double totalFee = 0;
				
				//计算总能耗
				double totalEnergy = 0;
				
				int lastRowNum4Point = 0;
				
				// 得到每个测量点费率数量
				int count = bean.getRateList().size()/bean.getSize();
				
				
				//拿到每个对象里测量点集合之后方便计算需要合并几行
				List<Map<String,Object>> rates = bean.getRateList();
				int ratesSize = bean.getSize();
				
				
				/**创建数据行*/
				HSSFRow row3 = sheet.createRow(rowNum);
				
				//填入上三级对象名称
				cell = row3.createCell( cellNum );
				cell.setCellValue( bean.getThirdName() );
				cell.setCellStyle( centerstyle );
				if (ratesSize != 1) {
					//合并行
					range = new CellRangeAddress(rowNum, rowNum+ratesSize-1, 0,0);
					sheet.addMergedRegion(range);
				}
				cellNum++;
				
				// 填入上两级对象名称
				cell = row3.createCell( cellNum );
				cell.setCellValue( bean.getSecondName() );
				cell.setCellStyle( centerstyle );
				if (ratesSize != 1) {
					//合并行
					range = new CellRangeAddress(rowNum, rowNum+ratesSize-1, 1,1);
					sheet.addMergedRegion(range);
				}
				cellNum++;
				
				
				// 填入上一级对象名称
				cell = row3.createCell( cellNum );
				cell.setCellValue( bean.getFirstName() );
				cell.setCellStyle( centerstyle );
				if (ratesSize != 1) {
					//合并行
					range = new CellRangeAddress(rowNum, rowNum+ratesSize-1, 2,2);
					sheet.addMergedRegion(range);
				}
				cellNum++;
				
				// 填入对象名称
				cell = row3.createCell( cellNum );
				cell.setCellValue( bean.getLedgerName() );
				cell.setCellStyle( centerstyle );
				if (ratesSize != 1) {
					//合并行
					range = new CellRangeAddress(rowNum, rowNum+ratesSize-1, 3,3);
					sheet.addMergedRegion(range);
				}
				cellNum++;
				
				// 填入 户号
				cell = row3.createCell( cellNum );
				cell.setCellValue( bean.getUserNo()==null?"-":bean.getUserNo() );
				cell.setCellStyle( centerstyle );
				if (ratesSize != 1) {
					//合并行
					range = new CellRangeAddress(rowNum, rowNum+ratesSize-1, 4,4);
					sheet.addMergedRegion(range);
				}
				cellNum++;
				
				//测量点从第几列开始(原始值)
					int cellNum_old = cellNum;
					
					// 填入 采集点名称
					cell = row3.createCell( cellNum );
					cell.setCellValue( rates.get( 0 ).get( "meterName" ).toString() );
					cell.setCellStyle( centerstyle );
					cellNum++;
				
					// 填入 倍率
					cell = row3.createCell( cellNum );
					cell.setCellValue( rates.get( 0 ).get( "ratio" ).toString() );
					cell.setCellStyle( centerstyle );
					cellNum++;
					String rateStart = null;
					
					
					// 填入,尖峰平谷等 开始示值
					for (int i = 0;i<count ; i++) {
						cell = row3.createCell( cellNum );
						//判断是否是替代数据
						rateStart = rates.get( i ).get( "ratestart" ).toString();
						if(rateStart.indexOf( "_" ) > 0 && rateStart.substring( rateStart.indexOf( "_" )+ 1,rateStart.length()).equals( "0" )){
							//进入这里的是替代数据
							cell.setCellValue( rateStart.substring( 0,rateStart.indexOf( "_" ) ) );
							cell.setCellStyle( redCenterStyle );
						} else {
							// 进入这里的是正常数据
							cell.setCellValue( rateStart.substring( 0,rateStart.indexOf( "_" ) ) );
							cell.setCellStyle( centerstyle );
						}
						cellNum++;
						
					}
					String rateend = null;
					
					
					// 填入,尖峰平谷等 结束示值
					for (int i = 0;i<count ; i++) {
						
						cell = row3.createCell( cellNum );
						//判断是否是替代数据
						rateend = rates.get( i ).get( "rateend" ).toString();
						if(rateend.indexOf( "_" ) > 0 && rateend.substring( rateend.indexOf( "_" )+ 1,rateend.length()).equals( "0" )){
							//进入这里的是替代数据
							cell.setCellValue( rateend.substring( 0,rateend.indexOf( "_" ) ) );
							cell.setCellStyle( redCenterStyle );
						} else {
							// 进入这里的是正常数据
							cell.setCellValue( rateend.substring( 0,rateend.indexOf( "_" ) ) );
							cell.setCellStyle( centerstyle );
						}
						cellNum++;
						//累计一下总金额
						totalFee += Double.parseDouble( rates.get(i).get( "totalFee" ).toString() );
					}
				
					totalEnergy += Double.parseDouble( rates.get( 0 ).get( "energy" ).toString() );
					
					// 填入 能耗
//					int totalRowNum = cellNum;
				
					cell = row3.createCell( cellNum );
					cell.setCellValue( totalEnergy );
					cell.setCellStyle( centerstyle );
					cellNum++;
					
					
					// 填入 总价
					cell = row3.createCell( cellNum );
					cell.setCellValue( totalFee );
					cell.setCellStyle( centerstyle );
					cellNum++;
					
					// 填入 缴费状态
					cell = row3.createCell( cellNum );
					if ( bean.getStatus() == 0 ) {
						cell.setCellValue( "未缴费" );
					} else {
						cell.setCellValue( "已缴费" );
					}
					if (ratesSize != 1) {
						//合并行
						range = new CellRangeAddress(rowNum, rowNum+ratesSize-1, cellNum,cellNum);
						lastRowNum4Point = rowNum+ratesSize-1;
						sheet.addMergedRegion(range);
					}
					cell.setCellStyle( centerstyle );
					
					rowNum++;
					
					
					if(bean.getSize() > 1){
						for (int j = 1; j < bean.getSize();j++) {
							//测量点从第几列开始
							int meterNum = cellNum_old;
							
							/**创建数据行*/
							HSSFRow row4 = sheet.createRow(rowNum);
							
							// 填入 采集点名称
							cell = row4.createCell( meterNum );
							cell.setCellValue( rates.get( count*j ).get( "meterName" ).toString() );
							cell.setCellStyle( centerstyle );
							meterNum++;
							
							// 填入 倍率
							cell = row4.createCell( meterNum );
							cell.setCellValue( rates.get( count*j ).get( "ratio" ).toString() );
							cell.setCellStyle( centerstyle );
							meterNum++;
							
							// 填入,尖峰平谷等 开始示值
							for (int k = count * j; k < count * (j + 1); k++) {
								cell = row4.createCell( meterNum );
								//判断是否是替代数据
								rateStart = rates.get( k ).get( "ratestart" ).toString();
								if(rateStart.indexOf( "_" ) > 0 && rateStart.substring( rateStart.indexOf( "_" )+ 1,rateStart.length()).equals( "0" )){
									//进入这里的是替代数据
									if (!rateStart.equals( "-" )) {
										cell.setCellValue( rateStart.substring( 0,rateStart.indexOf( "_" ) ) );
									} else {
										cell.setCellValue( rateStart );
									}
									cell.setCellStyle( redCenterStyle );
								} else {
									// 进入这里的是正常数据
									if (!rateStart.equals( "-" )) {
										cell.setCellValue( rateStart.substring( 0,rateStart.indexOf( "_" ) ) );
									} else {
										cell.setCellValue( rateStart );
									}
									cell.setCellStyle( centerstyle );
								}
								meterNum++;
								
							}
							
							
							// 填入,尖峰平谷等 结束示值
							for (int k = count * j; k < count * (j + 1); k++) {
								
								cell = row4.createCell( meterNum );
								//判断是否是替代数据
								rateend = rates.get( k ).get( "rateend" ).toString();
								if(rateend.indexOf( "_" ) > 0 && rateend.substring( rateend.indexOf( "_" )+ 1,rateend.length()).equals( "0" )){
									//进入这里的是替代数据
									if (!rateend.equals( "-" )) {
										cell.setCellValue( rateend.substring( 0,rateend.indexOf( "_" ) ) );
									} else {
										cell.setCellValue( rateend );
									}
									cell.setCellStyle( redCenterStyle );
								} else {
									// 进入这里的是正常数据
									if (!rateend.equals( "-" )) {
										cell.setCellValue( rateend.substring( 0,rateend.indexOf( "_" ) ) );
									} else {
										cell.setCellValue( rateend );
									}
									cell.setCellStyle( centerstyle );
								}
								meterNum++;
								//累计一下总金额
								totalFee += Double.parseDouble( rates.get(k).get( "totalFee" ).toString() );
								totalEnergy += Double.parseDouble( rates.get( k ).get( "energy" ).toString() );
							}
							
							// 填入 能耗
							cell = row4.createCell( meterNum );
							cell.setCellStyle( centerstyle );
							if (ratesSize != 1) {
								//合并行
								range = new CellRangeAddress(rowNum-1, lastRowNum4Point, meterNum,meterNum);
								sheet.addMergedRegion(range);
							}
//							cell.setCellValue( totalEnergy );
							row3.getCell( meterNum ).setCellValue( totalEnergy );
							meterNum++;
							
							// 填入 总价
							cell = row4.createCell( meterNum );
							cell.setCellStyle( centerstyle );
							if (ratesSize != 1) {
								//合并行
								range = new CellRangeAddress(rowNum-1, lastRowNum4Point, meterNum,meterNum);
								sheet.addMergedRegion(range);
							}
//							cell.setCellValue( totalFee );
							row3.getCell( meterNum ).setCellValue( totalFee );
							meterNum++;
							
							rowNum++;
						}
					}
					rowMax = rowNum;	//10
					cellMax = cellNum;	//20
			}
			
			this.setCellStyle(0,0,rowMax,cellMax,centerstyle,sheet);
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 合并单元格之后会出现样式缺失,用此方法补全所有单元格样式
	 * @author catkins
	 * @param rowIndex		单元格开始行坐标
	 * @param columnIndex	单元格开始列坐标
	 * @param toRowIndex	单元格结束行坐标
	 * @param toColumnIndex	单元格结束列坐标
	 * @param cellStyle		需要添加的样式
	 * @param sheet			可能是多sheet结构的excel所以需要传入sheet
	 * @return void
	 * @exception
	 * @date 2019/7/24 13:51
	 */
	private void setCellStyle(int rowIndex, int columnIndex, int toRowIndex, int toColumnIndex, HSSFCellStyle cellStyle,HSSFSheet sheet) {
		//两层循环遍历所有单元格
		for (int i = rowIndex; i <= toRowIndex; i++) {
			for (int j = columnIndex; j <= (toColumnIndex + columnIndex); j++) {
				//得到当前遍历到的单元格
				HSSFRow row = sheet.getRow(i);
				HSSFCell cell = null;
				if (null != row) {	//如果当前单元格是空的,则创建单元格,并添加样式
					cell = row.getCell(j);
					if (null == cell) {
						cell = row.createCell(j);
						cell.setCellStyle(cellStyle);
					}
				}
			}
		}
	}
	
	/**
	 * 查询上层节点的费率号
	 * @author catkins
	 * @param parentLedgerId 父节点id
	 * @param parLedgerId	 权限的最大节点
	 * @param bean			 商户bean
	 * @return void
	 * @exception
	 * @date 2019/8/2 17:11
	 */
	private void queryParentRateId(long parLedgerId,MerchantBean bean,int dataType){
		
		Map<String, Object> map = merchantsMapper.queryParentRateId( bean.getLedgerId() );
		if ( dataType == 1 && !map.get( "RATE_ID" ).toString().equals( "0" ) ) {
			bean.setRateId( Long.parseLong( map.get( "RATE_ID" ).toString() ) );
			bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
			return;
		} else if ( dataType == 2 && map.get( "RATE_W_ID" ) != null && !map.get( "RATE_W_ID" ).toString().equals( "0" ) ) {
			bean.setwRateId( Long.parseLong( map.get( "RATE_W_ID" ).toString() ) );
			bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
			return;
		} else if ( dataType == 3 && map.get( "RATE_G_ID" ) != null && !map.get( "RATE_G_ID" ).toString().equals( "0" ) ) {
			bean.setgRateId( Long.parseLong( map.get( "RATE_G_ID" ).toString() ) );
			bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
			return;
		} else if ( dataType == 4 && map.get( "RATE_H_ID" ) != null && !map.get( "RATE_H_ID" ).toString().equals( "0" ) ) {
			bean.sethRateId( Long.parseLong( map.get( "RATE_H_ID" ).toString() ) );
			bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
			return;
		}
		
		while (true) {
			
			
			if ( !map.isEmpty() ) {
				if( dataType == 1 && map.containsKey( "RATE_ID" ) ){
					//判断是否存在费率号,如果结果是0,则表示不存在,继续向父节点查找
					if ( !map.get( "RATE_ID" ).toString().equals( "0" ) && !map.get( "INHERIT" ).toString().equals( "0" ) ) {
						bean.setRateId( Long.parseLong( map.get( "RATE_ID" ).toString() ) );
						bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
						return;
					}
				} else if ( dataType == 2 && map.containsKey( "RATE_W_ID" ) ){
					//判断是否存在费率号,如果结果是0,则表示不存在,继续向父节点查找
					if ( !map.get( "RATE_W_ID" ).toString().equals( "0" ) && map.get( "RATE_W_ID" ) != null && !map.get( "INHERIT" ).toString().equals( "0" ) ) {
						bean.setwRateId( Long.parseLong( map.get( "RATE_W_ID" ).toString() ) );
						bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
						return;
					}
				} else if ( dataType == 3 && map.containsKey( "RATE_G_ID" ) ){
					if ( !map.get( "RATE_G_ID" ).toString().equals( "0" ) && map.get( "RATE_G_ID" ) != null && !map.get( "INHERIT" ).toString().equals( "0" ) ) {
						bean.setgRateId( Long.parseLong( map.get( "RATE_G_ID" ).toString() ) );
						bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
						return;
					}
				} else if ( dataType == 4 && map.containsKey( "RATE_H_ID" ) ){
					if ( !map.get( "RATE_H_ID" ).toString().equals( "0" ) && map.get( "RATE_H_ID" ) != null && !map.get( "INHERIT" ).toString().equals( "0" ) ) {
						bean.sethRateId( Long.parseLong( map.get( "RATE_H_ID" ).toString() ) );
						bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
						return;
					}
				}
			}
			
			
			if(!map.isEmpty() && parLedgerId == Long.parseLong( map.get( "PARENT_LEDGER_ID" ).toString() )){//如果查询到了根节点还是空或者0表示没有设置费率
				map = merchantsMapper.queryParentRateId(parLedgerId);
				//判断是否存在费率号,如果结果是0,则表示不存在,继续向父节点查找
				if ( null != map ) {
					if ( dataType == 1 && map.containsKey( "RATE_ID" ) && !map.get( "RATE_ID" ).toString().equals( "0" )
							&& !map.get( "INHERIT" ).toString().equals( "0" ) ) {
						bean.setRateId( Long.parseLong( map.get( "RATE_ID" ).toString() ) );
						bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
						return;
					} else if ( dataType == 2 && map.containsKey( "RATE_W_ID" ) && !map.get( "RATE_W_ID" ).toString().equals( "0" )
							&& map.get( "RATE_W_ID" ) != null && !map.get( "INHERIT" ).toString().equals( "0" ) ) {
						bean.setwRateId( Long.parseLong( map.get( "RATE_W_ID" ).toString() ) );
						bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
						return;
					} else if ( dataType == 3 && map.containsKey( "RATE_G_ID" ) && !map.get( "RATE_G_ID" ).toString().equals( "0" )
							&& map.get( "RATE_G_ID" ) != null && !map.get( "INHERIT" ).toString().equals( "0" ) ) {
						bean.setgRateId( Long.parseLong( map.get( "RATE_G_ID" ).toString() ) );
						bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
						return;
					} else if ( dataType == 4 && map.containsKey( "RATE_H_ID" ) && !map.get( "RATE_H_ID" ).toString().equals( "0" )
							&& map.get( "RATE_H_ID" ) != null && !map.get( "INHERIT" ).toString().equals( "0" ) ) {
						bean.sethRateId( Long.parseLong( map.get( "RATE_H_ID" ).toString() ) );
						bean.setInherit( Integer.parseInt( map.get( "INHERIT" ).toString() ) );
						return;
					}
				}
				return;
			}
			
			map = merchantsMapper.queryParentRateId( Long.parseLong( map.get( "PARENT_LEDGER_ID" ).toString() ) );
		}
	}
	
	/**
	 * 查询能管对象结算日相关信息
	 * @author catkins.
	 * @param ledgerId
	 * @param flag		是否是递归的标识
	 * @return java.util.Map<java.lang.String,java.lang.Integer>
	 * @exception
	 * @date 2019/11/6 16:48
	 */
	@Override
	public Map<String, Object> queryCalcData(Long ledgerId,int flag) {
		Map<String, Object> calcMap = merchantsMapper.queryCalcData( ledgerId );
		if(!calcMap.isEmpty() && calcMap.containsKey( "QCALCDATE" ) &&
				(flag == 0 || ( calcMap.containsKey( "QCALCINHERIT" ) && !calcMap.get( "QCALCINHERIT" ).toString().equals( "0" ) ) )){
			return calcMap;
		} else if ( !calcMap.containsKey( "QCALCDATE" ) && calcMap.containsKey( "PARENTLEDGERID" ) && ledgerId != 1l){
			//如果没有则进行递归查询
			return this.queryCalcData( Long.parseLong( calcMap.get( "PARENTLEDGERID" ).toString() ) , 1 );
		} else {
			calcMap.put( "QCALCDATE", 5 );
		}
		return calcMap;
	}
	
}
