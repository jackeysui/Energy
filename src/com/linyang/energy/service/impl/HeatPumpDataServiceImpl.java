package com.linyang.energy.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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
import com.linyang.energy.mapping.heatpump.HeatPumpDataMapper;
import com.linyang.energy.model.HeatPumpDataBean;
import com.linyang.energy.service.HeatPumpDataService;

@Service
public class HeatPumpDataServiceImpl implements HeatPumpDataService {

	@Autowired
	private HeatPumpDataMapper heatPumpDataMapper;

	@Override
	public Map<String, Object> getHeatPumpData(Map<String, Object> queryMap) {
		Map<String, Object> result=new HashMap<String,Object>();
		queryMap.put("beginDate", queryMap.get("beginTime").toString().subSequence(0, 10));
		queryMap.put("endDate", queryMap.get("endTime").toString().subSequence(0, 10));
		List<Map<String, Object>> elecSource=heatPumpDataMapper.getElecPowerData(queryMap);
		List<Map<String, Object>> heatSource1=heatPumpDataMapper.get1MinHeatPumpData(queryMap);
		List<Map<String, Object>> heatSource5=heatPumpDataMapper.get5MinHeatPumpData(queryMap);
		
		String heatData=queryMap.get("heatData").toString();
		List<HeatPumpDataBean> elecList1=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> elecList2=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> elecList3=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> elecList4=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> elecList5=new ArrayList<HeatPumpDataBean>();
		if(heatData.indexOf("a")>=0) //电压
		{
			HeatPumpDataBean tempA=new HeatPumpDataBean();
			HeatPumpDataBean tempB=new HeatPumpDataBean();
			HeatPumpDataBean tempC=new HeatPumpDataBean();
			tempA.setName("A相电压");
			tempB.setName("B相电压");
			tempC.setName("C相电压");
			List<String> axData=new ArrayList<String>();
			List<Double> ayData=new ArrayList<Double>();
			List<String> bxData=new ArrayList<String>();
			List<Double> byData=new ArrayList<Double>();
			List<String> cxData=new ArrayList<String>();
			List<Double> cyData=new ArrayList<Double>();
			boolean aflag=false;
			boolean bflag=false;
			boolean cflag=false;
			for(Map<String, Object> item:elecSource)
			{
				axData.add(item.get("FTIME").toString());
				bxData.add(item.get("FTIME").toString());
				cxData.add(item.get("FTIME").toString());
				Object va=item.get("VA");
				Object vb=item.get("VB");
				Object vc=item.get("VC");
				if(va!=null&&va.toString().length()>0)
				{
					aflag=true;
					ayData.add(Double.parseDouble(va.toString()));
				}
				else
				{
					ayData.add(0d);
				}
				
				if(vb!=null&&vb.toString().length()>0)
				{
					bflag=true;
					byData.add(Double.parseDouble(vb.toString()));
				}
				else
				{
					byData.add(0d);
				}
				
				if(vc!=null&&vc.toString().length()>0)
				{
					cflag=true;
					cyData.add(Double.parseDouble(vc.toString()));
				}
				else
				{
					cyData.add(0d);
				}
			}
			tempA.setxData(axData);
			tempA.setyData(ayData);
			tempB.setxData(bxData);
			tempB.setyData(byData);
			tempC.setxData(cxData);
			tempC.setyData(cyData);
			if(aflag){
				elecList1.add(tempA);
			}
			if(bflag){
				elecList1.add(tempB);
			}
			if(cflag){
				elecList1.add(tempC);
			}
		}
		
		result.put("elecList1", elecList1);
		
		if(heatData.indexOf("b")>=0) //电流
		{
			HeatPumpDataBean tempA=new HeatPumpDataBean();
			HeatPumpDataBean tempB=new HeatPumpDataBean();
			HeatPumpDataBean tempC=new HeatPumpDataBean();
			tempA.setName("A相电流");
			tempB.setName("B相电流");
			tempC.setName("C相电流");
			List<String> axData=new ArrayList<String>();
			List<Double> ayData=new ArrayList<Double>();
			List<String> bxData=new ArrayList<String>();
			List<Double> byData=new ArrayList<Double>();
			List<String> cxData=new ArrayList<String>();
			List<Double> cyData=new ArrayList<Double>();
			boolean aflag=false;
			boolean bflag=false;
			boolean cflag=false;
			for(Map<String, Object> item:elecSource)
			{
				axData.add(item.get("FTIME").toString());
				bxData.add(item.get("FTIME").toString());
				cxData.add(item.get("FTIME").toString());
				Object ia=item.get("IA");
				Object ib=item.get("IB");
				Object ic=item.get("IC");
				if(ia!=null&&ia.toString().length()>0)
				{
					aflag=true;
					ayData.add(Double.parseDouble(ia.toString()));
				}
				else
				{
					ayData.add(0d);
				}
				
				if(ib!=null&&ib.toString().length()>0)
				{
					bflag=true;
					byData.add(Double.parseDouble(ib.toString()));
				}
				else
				{
					byData.add(0d);
				}
				
				if(ic!=null&&ic.toString().length()>0)
				{
					cflag=true;
					cyData.add(Double.parseDouble(ic.toString()));
				}
				else
				{
					cyData.add(0d);
				}
			}
			tempA.setxData(axData);
			tempA.setyData(ayData);
			tempB.setxData(bxData);
			tempB.setyData(byData);
			tempC.setxData(cxData);
			tempC.setyData(cyData);
			if(aflag){
				elecList2.add(tempA);	
			}
			if(bflag){
				elecList2.add(tempB);
			}
			if(cflag){		
				elecList2.add(tempC);
			}
		}
		
		result.put("elecList2", elecList2);
		
		if(heatData.indexOf("c")>=0) 
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			HeatPumpDataBean tempA=new HeatPumpDataBean();
			HeatPumpDataBean tempB=new HeatPumpDataBean();
			HeatPumpDataBean tempC=new HeatPumpDataBean();
			temp.setName("功率因数");
			tempA.setName("A相功率因数");
			tempB.setName("B相功率因数");
			tempC.setName("C相功率因数");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			List<String> axData=new ArrayList<String>();
			List<Double> ayData=new ArrayList<Double>();
			List<String> bxData=new ArrayList<String>();
			List<Double> byData=new ArrayList<Double>();
			List<String> cxData=new ArrayList<String>();
			List<Double> cyData=new ArrayList<Double>();
			boolean flag=false;
			boolean aflag=false;
			boolean bflag=false;
			boolean cflag=false;
			for(Map<String, Object> item:elecSource)
			{
				xData.add(item.get("FTIME").toString());
				axData.add(item.get("FTIME").toString());
				bxData.add(item.get("FTIME").toString());
				cxData.add(item.get("FTIME").toString());
				Object pf=item.get("PF");
				Object apf=item.get("PF_A");
				Object bpf=item.get("PF_B");
				Object cpf=item.get("PF_C");
				if(pf!=null&&pf.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(pf.toString()));
				}
				else
				{
					yData.add(0d);
				}
				
				if(apf!=null&&apf.toString().length()>0)
				{
					aflag=true;
					ayData.add(Double.parseDouble(apf.toString()));
				}
				else
				{
					ayData.add(0d);
				}
				
				if(bpf!=null&&bpf.toString().length()>0)
				{
					bflag=true;
					byData.add(Double.parseDouble(bpf.toString()));
				}
				else
				{
					byData.add(0d);
				}
				
				if(cpf!=null&&cpf.toString().length()>0)
				{
					cflag=true;
					cyData.add(Double.parseDouble(cpf.toString()));
				}
				else
				{
					cyData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			tempA.setxData(axData);
			tempA.setyData(ayData);
			tempB.setxData(bxData);
			tempB.setyData(byData);
			tempC.setxData(cxData);
			tempC.setyData(cyData);
			if(flag){
				elecList3.add(temp);
			}
			if(aflag){
				elecList3.add(tempA);
			}
			if(bflag){
				elecList3.add(tempB);
			}
			if(cflag){
				elecList3.add(tempC);
			}
		}
		
		result.put("elecList3", elecList3);
		
		if(heatData.indexOf("d")>=0) //有功功率
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			HeatPumpDataBean tempA=new HeatPumpDataBean();
			HeatPumpDataBean tempB=new HeatPumpDataBean();
			HeatPumpDataBean tempC=new HeatPumpDataBean();
			temp.setName("有功功率");
			tempA.setName("A相有功功率");
			tempB.setName("B相有功功率");
			tempC.setName("C相有功功率");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			List<String> axData=new ArrayList<String>();
			List<Double> ayData=new ArrayList<Double>();
			List<String> bxData=new ArrayList<String>();
			List<Double> byData=new ArrayList<Double>();
			List<String> cxData=new ArrayList<String>();
			List<Double> cyData=new ArrayList<Double>();
			boolean flag=false;
			boolean aflag=false;
			boolean bflag=false;
			boolean cflag=false;
			for(Map<String, Object> item:elecSource)
			{
				xData.add(item.get("FTIME").toString());
				axData.add(item.get("FTIME").toString());
				bxData.add(item.get("FTIME").toString());
				cxData.add(item.get("FTIME").toString());
				Object ap=item.get("AP");
				Object aap=item.get("AP_A");
				Object bap=item.get("AP_B");
				Object cap=item.get("AP_C");
				if(ap!=null&&ap.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(ap.toString()));
				}
				else
				{
					yData.add(0d);
				}
				
				if(aap!=null&&aap.toString().length()>0)
				{
					aflag=true;
					ayData.add(Double.parseDouble(aap.toString()));
				}
				else
				{
					ayData.add(0d);
				}
				
				if(bap!=null&&bap.toString().length()>0)
				{
					bflag=true;
					byData.add(Double.parseDouble(bap.toString()));
				}
				else
				{
					byData.add(0d);
				}
				
				if(cap!=null&&cap.toString().length()>0)
				{
					cflag=true;
					cyData.add(Double.parseDouble(cap.toString()));
				}
				else
				{
					cyData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			tempA.setxData(axData);
			tempA.setyData(ayData);
			tempB.setxData(bxData);
			tempB.setyData(byData);
			tempC.setxData(cxData);
			tempC.setyData(cyData);
			if(flag){
				elecList4.add(temp);
			}
			if(aflag){
				elecList4.add(tempA);
			}
			if(bflag){
				elecList4.add(tempB);
			}
			if(cflag){
				elecList4.add(tempC);
			}
		}
		
		if(heatData.indexOf("e")>=0) //无功功率
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			HeatPumpDataBean atemp=new HeatPumpDataBean();
			HeatPumpDataBean btemp=new HeatPumpDataBean();
			HeatPumpDataBean ctemp=new HeatPumpDataBean();
			temp.setName("无功功率");
			atemp.setName("A相无功功率");
			btemp.setName("B相无功功率");
			ctemp.setName("C相无功功率");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			List<String> axData=new ArrayList<String>();
			List<Double> ayData=new ArrayList<Double>();
			List<String> bxData=new ArrayList<String>();
			List<Double> byData=new ArrayList<Double>();
			List<String> cxData=new ArrayList<String>();
			List<Double> cyData=new ArrayList<Double>();
			boolean flag=false;
			boolean aflag=false;
			boolean bflag=false;
			boolean cflag=false;
			for(Map<String, Object> item:elecSource)
			{
				xData.add(item.get("FTIME").toString());
				axData.add(item.get("FTIME").toString());
				bxData.add(item.get("FTIME").toString());
				cxData.add(item.get("FTIME").toString());
				Object rp=item.get("RP");
				Object arp=item.get("RP_A");
				Object brp=item.get("RP_B");
				Object crp=item.get("RP_C");
				if(rp!=null&&rp.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(rp.toString()));
				}
				else
				{
					yData.add(0d);
				}
				
				if(arp!=null&&arp.toString().length()>0)
				{
					aflag=true;
					ayData.add(Double.parseDouble(arp.toString()));
				}
				else
				{
					ayData.add(0d);
				}
				
				if(brp!=null&&brp.toString().length()>0)
				{
					bflag=true;
					byData.add(Double.parseDouble(brp.toString()));
				}
				else
				{
					byData.add(0d);
				}
				
				if(crp!=null&&crp.toString().length()>0)
				{
					cflag=true;
					cyData.add(Double.parseDouble(crp.toString()));
				}
				else
				{
					cyData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			atemp.setxData(axData);
			atemp.setyData(ayData);
			btemp.setxData(bxData);
			btemp.setyData(byData);
			ctemp.setxData(cxData);
			ctemp.setyData(cyData);
			if(flag){
				elecList4.add(temp);
			}
			if(aflag){
				elecList4.add(atemp);
			}
			if(bflag){
				elecList4.add(btemp);
			}
			if(cflag){
				elecList4.add(ctemp);
			}
		}
		
		result.put("elecList4", elecList4);
		
		if(heatData.indexOf("f")>=0) //5分钟电量
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("5分钟电量");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			NumberFormat nf = NumberFormat.getNumberInstance();
	        // 保留两位小数
	        nf.setMaximumFractionDigits(2); 		        
	        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
	        nf.setRoundingMode(RoundingMode.UP);
			for(int i=0;i<elecSource.size()-1;i++)
			{
				Map<String,Object> item=elecSource.get(i);
				xData.add(item.get("FTIME").toString());
				Object rp=item.get("FAE_VALUE");
				Object rp1=elecSource.get(i+1).get("FAE_VALUE");
				if(rp!=null&&rp.toString().length()>0&&rp1!=null&&rp1.toString().length()>0)
				{
					flag=true;					
					yData.add(Double.parseDouble(nf.format(Double.parseDouble(rp1.toString())-Double.parseDouble(rp.toString()))));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				elecList5.add(temp);
			}
		}
		//电力数据（第一张图表）
		result.put("elecList5", elecList5);
		
		
		List<HeatPumpDataBean> heatList1=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> heatList2=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> heatList3=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> heatList4=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> heatList5=new ArrayList<HeatPumpDataBean>();
		if(heatData.indexOf("g")>=0) //室外温度
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("室外温度");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object t=item.get("OUTDOOR_T");
				if(t!=null&&t.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(t.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				heatList3.add(temp);
			}
		}
		
		if(heatData.indexOf("h")>=0) //进水温度
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("进水温度");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource1)
			{
				xData.add(item.get("FTIME").toString());
				Object t=item.get("H_INLET_WATER_T");
				if(t!=null&&t.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(t.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				heatList1.add(temp);
			}
		}
		
		if(heatData.indexOf("i")>=0) //进水温度
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("出水温度");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource1)
			{
				xData.add(item.get("FTIME").toString());
				Object t=item.get("H_OUTLET_WATER_T");
				if(t!=null&&t.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(t.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				heatList1.add(temp);
			}
		}
		
		if(heatData.indexOf("j")>=0) //流量
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("流量");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource1)
			{
				xData.add(item.get("FTIME").toString());
				Object t=item.get("FLOW");
				if(t!=null&&t.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(t.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				heatList2.add(temp);
			}
		}
		
		if(heatData.indexOf("k")>=0) //热量
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("热量");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object t=item.get("CUMU_HEAT");
				if(t!=null&&t.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(t.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				heatList4.add(temp);
			}
		}
		
		if(heatData.indexOf("l")>=0) //冷量
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("冷量");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object t=item.get("CUMU_COLD");
				if(t!=null&&t.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(t.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				heatList4.add(temp);
			}
		}
		
		if(heatData.indexOf("m")>=0) //功率
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("功率");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object t=item.get("HEAT_P");
				if(t!=null&&t.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(t.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				heatList5.add(temp);
			}
		}
		
		result.put("heatList1", heatList1);
		result.put("heatList2", heatList2);
		result.put("heatList3", heatList3);
		result.put("heatList4", heatList4);
		result.put("heatList5", heatList5);		
		
		List<HeatPumpDataBean> statusList=new ArrayList<HeatPumpDataBean>();
		if(heatData.indexOf("n")>=0) //机组状态
		{
			HeatPumpDataBean temp1=new HeatPumpDataBean();
			HeatPumpDataBean temp2=new HeatPumpDataBean();
			HeatPumpDataBean temp3=new HeatPumpDataBean();
			HeatPumpDataBean temp4=new HeatPumpDataBean();
			HeatPumpDataBean temp5=new HeatPumpDataBean();
			//机组模式类型:1-启动,2-模式,3-除霜,4-防冻,5-故障
			temp1.setName("启动状态");
			temp2.setName("模式状态");
			temp3.setName("除霜状态");
			temp4.setName("防冻状态");
			temp5.setName("故障状态");
			List<String> xData1=new ArrayList<String>();
			List<Double> yData1=new ArrayList<Double>();
			List<String> xData2=new ArrayList<String>();
			List<Double> yData2=new ArrayList<Double>();
			List<String> xData3=new ArrayList<String>();
			List<Double> yData3=new ArrayList<Double>();
			List<String> xData4=new ArrayList<String>();
			List<Double> yData4=new ArrayList<Double>();
			List<String> xData5=new ArrayList<String>();
			List<Double> yData5=new ArrayList<Double>();
			boolean flag1=false;
			boolean flag2=false;
			boolean flag3=false;
			boolean flag4=false;
			boolean flag5=false;
			for(Map<String, Object> item:heatSource1)
			{
				xData1.add(item.get("FTIME").toString());
				xData2.add(item.get("FTIME").toString());
				xData3.add(item.get("FTIME").toString());
				xData4.add(item.get("FTIME").toString());
				xData5.add(item.get("FTIME").toString());
				Object v1=item.get("STATUS_OPEN");
				Object v2=item.get("STATUS_MODEL");
				Object v3=item.get("STATUS_FOG");
				Object v4=item.get("STATUS_FREEZN");
				Object v5=item.get("STATUS_ERROR");
				if(v1!=null&&v1.toString().length()>0)
				{
					flag1=true;
					yData1.add(Double.parseDouble(v1.toString()));
				}
				else
				{
					yData1.add(0d);
				}
				
				if(v2!=null&&v2.toString().length()>0)
				{
					flag2=true;
					yData2.add(Double.parseDouble(v2.toString()));
				}
				else
				{
					yData2.add(0d);
				}
				
				if(v3!=null&&v3.toString().length()>0)
				{
					flag3=true;
					yData3.add(Double.parseDouble(v3.toString()));
				}
				else
				{
					yData3.add(0d);
				}
				
				if(v4!=null&&v4.toString().length()>0)
				{
					flag4=true;
					yData4.add(Double.parseDouble(v4.toString()));
				}
				else
				{
					yData4.add(0d);
				}
				
				if(v5!=null&&v5.toString().length()>0)
				{
					flag5=true;
					yData5.add(Double.parseDouble(v5.toString()));
				}
				else
				{
					yData5.add(0d);
				}
			}
			temp1.setxData(xData1);
			temp1.setyData(yData1);
			temp2.setxData(xData2);
			temp2.setyData(yData2);
			temp3.setxData(xData3);
			temp3.setyData(yData3);
			temp4.setxData(xData4);
			temp4.setyData(yData4);
			temp5.setxData(xData5);
			temp5.setyData(yData5);
			
			
			if(flag1){
				statusList.add(temp1);
			}
			
			if(flag2){
				statusList.add(temp2);
			}
			
			if(flag3){
				statusList.add(temp3);
			}
			
			if(flag4){
				statusList.add(temp4);
			}
			
			if(flag5){
				statusList.add(temp5);
			}
		}
		
		if(heatData.indexOf("o")>=0) //热泵启/停
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("热泵启/停");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource1)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("HEATPUMP_STATUS");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				statusList.add(temp);
			}
		}
		
		//热力数据（第三张图表）
		result.put("statusList", statusList);
		
		List<HeatPumpDataBean> tempList1=new ArrayList<HeatPumpDataBean>();
		List<HeatPumpDataBean> tempList2=new ArrayList<HeatPumpDataBean>();
		
		if(heatData.indexOf("p")>=0) //1#单元开度
		{
			HeatPumpDataBean temp1=new HeatPumpDataBean();
			HeatPumpDataBean temp2=new HeatPumpDataBean();
			temp1.setName("1#单元开度1");
			temp2.setName("1#单元开度2");
			List<String> xData1=new ArrayList<String>();
			List<Double> yData1=new ArrayList<Double>();
			List<String> xData2=new ArrayList<String>();
			List<Double> yData2=new ArrayList<Double>();
			boolean flag1=false;
			boolean flag2=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData1.add(item.get("FTIME").toString());
				xData2.add(item.get("FTIME").toString());
				Object v1=item.get("OPENING_1");
				Object v2=item.get("OPENING_2");
				if(v1!=null&&v1.toString().length()>0)
				{
					flag1=true;
					yData1.add(Double.parseDouble(v1.toString()));
				}
				else
				{
					yData1.add(0d);
				}
				
				if(v2!=null&&v2.toString().length()>0)
				{
					flag2=true;
					yData2.add(Double.parseDouble(v2.toString()));
				}
				else
				{
					yData2.add(0d);
				}
			}
			temp1.setxData(xData1);
			temp1.setyData(yData1);
			temp2.setxData(xData2);
			temp2.setyData(yData2);
			if(flag2){
				tempList2.add(temp1);
			}
			if(flag2){
				tempList2.add(temp2);
			}
		}
		
		//1单元数据
		if(heatData.indexOf("q")>=0) //出水
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("出水");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("OUTLET_WATER_T");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		if(heatData.indexOf("r")>=0) //外盘1
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("外盘1");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("EXTR_T_1");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		if(heatData.indexOf("s")>=0) //外盘2
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("外盘2");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("EXTR_T_2");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		if(heatData.indexOf("t")>=0) //吸气1
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("吸气1");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("SUCTION_T_1");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		if(heatData.indexOf("u")>=0) //吸气2
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("吸气2");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("SUCTION_T_2");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		if(heatData.indexOf("v")>=0) //阀后1
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("阀后1");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("OUTLET_T_1");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		if(heatData.indexOf("w")>=0) //阀后2
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("阀后2");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("OUTLET_T_2");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		if(heatData.indexOf("x")>=0) //排气1
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("排气1");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("EXH_T_1");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		if(heatData.indexOf("y")>=0) //排气2
		{
			HeatPumpDataBean temp=new HeatPumpDataBean();
			temp.setName("排气2");
			List<String> xData=new ArrayList<String>();
			List<Double> yData=new ArrayList<Double>();
			boolean flag=false;
			for(Map<String, Object> item:heatSource5)
			{
				xData.add(item.get("FTIME").toString());
				Object v=item.get("EXH_T_2");
				if(v!=null&&v.toString().length()>0)
				{
					flag=true;
					yData.add(Double.parseDouble(v.toString()));
				}
				else
				{
					yData.add(0d);
				}
			}
			temp.setxData(xData);
			temp.setyData(yData);
			if(flag){
				tempList1.add(temp);
			}
		}
		
		//热力数据（第四张图表）
		result.put("tempList1", tempList1);
		result.put("tempList2", tempList2);
		return result;
	} 
	
	/**
	 * 得到Excel，数据填充
	 * 
	 * @author 周礼
	 * @param 参数
	 *            table名字sheetName，输出流output，结果集map，页面请求的信息queryMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getEleExcel(String sheetName, OutputStream output, Map<String, Object> queryMap)
			throws UnsupportedEncodingException {
		queryMap.put("beginDate", queryMap.get("beginTime").toString().subSequence(0, 10));
		queryMap.put("endDate", queryMap.get("endTime").toString().subSequence(0, 10));
		List<Map<String, Object>> elecSource=heatPumpDataMapper.getElecPowerData(queryMap);
		List<Map<String, Object>> heatSource1=heatPumpDataMapper.get1MinHeatPumpData(queryMap);
		List<Map<String, Object>> heatSource5=heatPumpDataMapper.get5MinHeatPumpData(queryMap);
				
		String heatData=queryMap.get("heatData").toString();
		
		// 声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		//公用样式
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

		// 生成并设置另一个表格内容样式
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

		// 生成另一个字体
		HSSFFont font_ = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style_.setFont(font_);

		// 生成第一个表格："电力数据"。
		HSSFSheet sheet1 =wb.createSheet("电力数据");		
		// 设置默认宽度为15字节
		sheet1.setDefaultColumnWidth(15);
		int sheet1rowcount=0;
		HSSFRow headRow1 = sheet1.createRow(sheet1rowcount++);
		int columncount=0;
		HSSFCell cellt = headRow1.createCell(columncount++);
		cellt.setCellStyle(titlestyle);
		cellt.setCellValue("时间");
		if(heatData.indexOf("a")>=0)
		{
			HSSFCell cellVA = headRow1.createCell(columncount++);
			cellVA.setCellStyle(titlestyle);
			cellVA.setCellValue("A相电压");
			HSSFCell cellVB = headRow1.createCell(columncount++);
			cellVB.setCellStyle(titlestyle);
			cellVB.setCellValue("B相电压");
			HSSFCell cellVC = headRow1.createCell(columncount++);
			cellVC.setCellStyle(titlestyle);
			cellVC.setCellValue("C相电压");
		}
		
		if(heatData.indexOf("b")>=0)
		{
			HSSFCell acellI = headRow1.createCell(columncount++);
			acellI.setCellStyle(titlestyle);
			acellI.setCellValue("A相电流");
			HSSFCell bcellI = headRow1.createCell(columncount++);
			bcellI.setCellStyle(titlestyle);
			bcellI.setCellValue("B相电流");
			HSSFCell ccellI = headRow1.createCell(columncount++);
			ccellI.setCellStyle(titlestyle);
			ccellI.setCellValue("C相电流");
		}
		
		if(heatData.indexOf("c")>=0)
		{
			HSSFCell cellPF = headRow1.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("功率因数");
			HSSFCell acellPF = headRow1.createCell(columncount++);
			acellPF.setCellStyle(titlestyle);
			acellPF.setCellValue("A相功率因数");
			HSSFCell bcellPF = headRow1.createCell(columncount++);
			bcellPF.setCellStyle(titlestyle);
			bcellPF.setCellValue("B相功率因数");
			HSSFCell ccellPF = headRow1.createCell(columncount++);
			ccellPF.setCellStyle(titlestyle);
			ccellPF.setCellValue("C相功率因数");
		}
		
		if(heatData.indexOf("d")>=0)
		{
			HSSFCell cellPF = headRow1.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("有功功率");
			HSSFCell acellPF = headRow1.createCell(columncount++);
			acellPF.setCellStyle(titlestyle);
			acellPF.setCellValue("A相有功功率");
			HSSFCell bcellPF = headRow1.createCell(columncount++);
			bcellPF.setCellStyle(titlestyle);
			bcellPF.setCellValue("B相有功功率");
			HSSFCell ccellPF = headRow1.createCell(columncount++);
			ccellPF.setCellStyle(titlestyle);
			ccellPF.setCellValue("C相有功功率");
		}
		
		if(heatData.indexOf("e")>=0)
		{
			HSSFCell cellPF = headRow1.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("无功功率");
			HSSFCell acellPF = headRow1.createCell(columncount++);
			acellPF.setCellStyle(titlestyle);
			acellPF.setCellValue("A相无功功率");
			HSSFCell bcellPF = headRow1.createCell(columncount++);
			bcellPF.setCellStyle(titlestyle);
			bcellPF.setCellValue("B相无功功率");
			HSSFCell ccellPF = headRow1.createCell(columncount++);
			ccellPF.setCellStyle(titlestyle);
			ccellPF.setCellValue("C相无功功率");
		}
		
		if(heatData.indexOf("f")>=0)
		{
			HSSFCell cellPF = headRow1.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("5分钟电量");
		}

		for(int i=0;i<elecSource.size();i++)
		{
			Map<String, Object> current=elecSource.get(i);
			HSSFRow datarow = sheet1.createRow(sheet1rowcount++);
			columncount=0; //临时变量归零
			HSSFCell celltemp = datarow.createCell(columncount++);
			celltemp.setCellStyle(style_);
			celltemp.setCellValue(current.get("FTIME").toString());
			
			if(heatData.indexOf("a")>=0)
			{
				HSSFCell acellV = datarow.createCell(columncount++);
				HSSFCell bcellV = datarow.createCell(columncount++);
				HSSFCell ccellV = datarow.createCell(columncount++);
				acellV.setCellStyle(style_);
				bcellV.setCellStyle(style_);
				ccellV.setCellStyle(style_);
				Object av=current.get("VA");
				Object bv=current.get("VB");
				Object cv=current.get("VC");
				if(av!=null&&av.toString().length()>0)
				{
					acellV.setCellValue(av.toString());
				}
				else
				{
					acellV.setCellValue("0");
				}
				if(bv!=null&&bv.toString().length()>0)
				{
					bcellV.setCellValue(bv.toString());
				}
				else
				{
					bcellV.setCellValue("0");
				}
				if(cv!=null&&cv.toString().length()>0)
				{
					ccellV.setCellValue(cv.toString());
				}
				else
				{
					ccellV.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("b")>=0)
			{
				HSSFCell acellI = datarow.createCell(columncount++);
				HSSFCell bcellI = datarow.createCell(columncount++);
				HSSFCell ccellI = datarow.createCell(columncount++);
				acellI.setCellStyle(style_);
				bcellI.setCellStyle(style_);
				ccellI.setCellStyle(style_);
				Object aI=current.get("IA");
				Object bI=current.get("IB");
				Object cI=current.get("IC");
				if(aI!=null&&aI.toString().length()>0)
				{
					acellI.setCellValue(aI.toString());
				}
				else
				{
					acellI.setCellValue("0");
				}
				if(bI!=null&&bI.toString().length()>0)
				{
					bcellI.setCellValue(bI.toString());
				}
				else
				{
					bcellI.setCellValue("0");
				}
				if(cI!=null&&cI.toString().length()>0)
				{
					ccellI.setCellValue(cI.toString());
				}
				else
				{
					ccellI.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("c")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				HSSFCell acellPF = datarow.createCell(columncount++);
				HSSFCell bcellPF = datarow.createCell(columncount++);
				HSSFCell ccellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				acellPF.setCellStyle(style_);
				bcellPF.setCellStyle(style_);
				ccellPF.setCellStyle(style_);
				Object pf=current.get("PF");
				Object apf=current.get("PF_A");
				Object bpf=current.get("PF_B");
				Object cpf=current.get("PF_C");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
				if(apf!=null&&apf.toString().length()>0)
				{
					acellPF.setCellValue(apf.toString());
				}
				else
				{
					acellPF.setCellValue("0");
				}
				if(bpf!=null&&bpf.toString().length()>0)
				{
					bcellPF.setCellValue(bpf.toString());
				}
				else
				{
					bcellPF.setCellValue("0");
				}
				if(cpf!=null&&cpf.toString().length()>0)
				{
					ccellPF.setCellValue(cpf.toString());
				}
				else
				{
					ccellPF.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("d")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				HSSFCell acellPF = datarow.createCell(columncount++);
				HSSFCell bcellPF = datarow.createCell(columncount++);
				HSSFCell ccellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				acellPF.setCellStyle(style_);
				bcellPF.setCellStyle(style_);
				ccellPF.setCellStyle(style_);
				Object ap=current.get("AP");
				Object aap=current.get("AP_A");
				Object bap=current.get("AP_B");
				Object cap=current.get("AP_C");
				if(ap!=null&&ap.toString().length()>0)
				{
					cellPF.setCellValue(ap.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
				
				if(aap!=null&&aap.toString().length()>0)
				{
					acellPF.setCellValue(aap.toString());
				}
				else
				{
					acellPF.setCellValue("0");
				}
				if(bap!=null&&bap.toString().length()>0)
				{
					bcellPF.setCellValue(bap.toString());
				}
				else
				{
					bcellPF.setCellValue("0");
				}
				if(cap!=null&&cap.toString().length()>0)
				{
					ccellPF.setCellValue(cap.toString());
				}
				else
				{
					ccellPF.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("e")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				HSSFCell acellPF = datarow.createCell(columncount++);
				HSSFCell bcellPF = datarow.createCell(columncount++);
				HSSFCell ccellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				acellPF.setCellStyle(style_);
				bcellPF.setCellStyle(style_);
				ccellPF.setCellStyle(style_);
				Object rp=current.get("RP");
				Object arp=current.get("RP_A");
				Object brp=current.get("RP_B");
				Object crp=current.get("RP_C");
				if(rp!=null&&rp.toString().length()>0)
				{
					cellPF.setCellValue(rp.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
				
				if(arp!=null&&arp.toString().length()>0)
				{
					acellPF.setCellValue(arp.toString());
				}
				else
				{
					acellPF.setCellValue("0");
				}
				if(brp!=null&&brp.toString().length()>0)
				{
					bcellPF.setCellValue(brp.toString());
				}
				else
				{
					bcellPF.setCellValue("0");
				}
				if(crp!=null&&crp.toString().length()>0)
				{
					ccellPF.setCellValue(crp.toString());
				}
				else
				{
					ccellPF.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("f")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				if(i<elecSource.size()-1)
				{
					Object rp=current.get("FAE_VALUE");
					Object rp1=elecSource.get(i+1).get("FAE_VALUE");
					if(rp!=null&&rp.toString().length()>0&&rp1!=null&&rp1.toString().length()>0)
					{
						cellPF.setCellValue(Double.parseDouble(rp1.toString())-Double.parseDouble(rp.toString()));
					}
					else
					{
						cellPF.setCellValue("0");
					}
				}
				else
				{
					cellPF.setCellValue("0");
				}
			}
		}
		//电力数据结束
		
		//1分钟人力数据
		HSSFSheet sheet21 =wb.createSheet("瞬时热力数据");		
		// 设置默认宽度为15字节
		sheet21.setDefaultColumnWidth(15);
		sheet1rowcount=0;
		HSSFRow headRow21 = sheet21.createRow(sheet1rowcount++);
		columncount=0;
		HSSFCell cellt21 = headRow21.createCell(columncount++);
		cellt21.setCellStyle(titlestyle);
		cellt21.setCellValue("时间");
		
		if(heatData.indexOf("h")>=0)
		{
			HSSFCell cellI = headRow21.createCell(columncount++);
			cellI.setCellStyle(titlestyle);
			cellI.setCellValue("进水温度");
		}
		
		if(heatData.indexOf("i")>=0)
		{
			HSSFCell cellPF = headRow21.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("出水温度");
		}
		
		if(heatData.indexOf("j")>=0)
		{
			HSSFCell cellPF = headRow21.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("流量");
		}
				
		for(int i=0;i<heatSource1.size();i++)
		{
			Map<String, Object> current=heatSource1.get(i);
			HSSFRow datarow = sheet21.createRow(sheet1rowcount++);
			columncount=0; //临时变量归零
			HSSFCell celltemp = datarow.createCell(columncount++);
			celltemp.setCellStyle(style_);
			celltemp.setCellValue(current.get("FTIME").toString());
						
			if(heatData.indexOf("h")>=0)
			{
				HSSFCell cellI = datarow.createCell(columncount++);
				cellI.setCellStyle(style_);
				Object I=current.get("H_INLET_WATER_T");
				if(I!=null&&I.toString().length()>0)
				{
					cellI.setCellValue(I.toString());
				}
				else
				{
					cellI.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("i")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object pf=current.get("H_OUTLET_WATER_T");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("j")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object ap=current.get("FLOW");
				if(ap!=null&&ap.toString().length()>0)
				{
					cellPF.setCellValue(ap.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			}			
		}
		//热力数据结束
		
		
		// 5分钟热力数据"。
		HSSFSheet sheet25 =wb.createSheet("热力数据");		
		// 设置默认宽度为15字节
		sheet25.setDefaultColumnWidth(15);
		sheet1rowcount=0;
		HSSFRow headRow25 = sheet25.createRow(sheet1rowcount++);
		columncount=0;
		HSSFCell cellt2 = headRow25.createCell(columncount++);
		cellt2.setCellStyle(titlestyle);
		cellt2.setCellValue("时间");
		if(heatData.indexOf("g")>=0)
		{
			HSSFCell cellV = headRow25.createCell(columncount++);
			cellV.setCellStyle(titlestyle);
			cellV.setCellValue("室外温度");
		}
				
		if(heatData.indexOf("k")>=0)
		{
			HSSFCell cellPF = headRow25.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("热量");
		}
		
		if(heatData.indexOf("l")>=0)
		{
			HSSFCell cellPF = headRow25.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("冷量");
		}
		
		if(heatData.indexOf("m")>=0)
		{
			HSSFCell cellPF = headRow25.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("功率");
		}

		for(int i=0;i<heatSource5.size();i++)
		{
			Map<String, Object> current=heatSource5.get(i);
			HSSFRow datarow = sheet25.createRow(sheet1rowcount++);
			columncount=0; //临时变量归零
			HSSFCell celltemp = datarow.createCell(columncount++);
			celltemp.setCellStyle(style_);
			celltemp.setCellValue(current.get("FTIME").toString());
			
			if(heatData.indexOf("g")>=0)
			{
				HSSFCell cellV = datarow.createCell(columncount++);
				cellV.setCellStyle(titlestyle);
				Object v=current.get("OUTDOOR_T"); //室外温度
				if(v!=null&&v.toString().length()>0)
				{
					cellV.setCellValue(v.toString());
				}
				else
				{
					cellV.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("k")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object rp=current.get("CUMU_HEAT");
				if(rp!=null&&rp.toString().length()>0)
				{
					cellPF.setCellValue(rp.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("l")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object rp=current.get("CUMU_COLD");
				if(rp!=null&&rp.toString().length()>0)
				{
					cellPF.setCellValue(rp.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("m")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object rp=current.get("HEAT_P");
				if(rp!=null&&rp.toString().length()>0)
				{
					cellPF.setCellValue(rp.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			}
		}
		//热力数据结束
		
		// 生成第三个表格："状态数据"。
		HSSFSheet sheet3 =wb.createSheet("状态数据");		
		// 设置默认宽度为15字节
		sheet3.setDefaultColumnWidth(15);
		sheet1rowcount=0;
		HSSFRow headRow3 = sheet3.createRow(sheet1rowcount++);
		columncount=0;
		HSSFCell cellt3 = headRow3.createCell(columncount++);
		cellt3.setCellStyle(titlestyle);
		cellt3.setCellValue("时间");
		if(heatData.indexOf("n")>=0)
		{
			//机组模式类型:1-启动,2-模式,3-除霜,4-防冻,5-故障
			HSSFCell STATUS_OPEN = headRow3.createCell(columncount++);
			STATUS_OPEN.setCellStyle(titlestyle);
			STATUS_OPEN.setCellValue("启动状态");
			HSSFCell STATUS_MODEL = headRow3.createCell(columncount++);
			STATUS_MODEL.setCellStyle(titlestyle);
			STATUS_MODEL.setCellValue("模式状态");
			HSSFCell STATUS_FOG = headRow3.createCell(columncount++);
			STATUS_FOG.setCellStyle(titlestyle);
			STATUS_FOG.setCellValue("除霜状态");
			HSSFCell STATUS_FREEZN = headRow3.createCell(columncount++);
			STATUS_FREEZN.setCellStyle(titlestyle);
			STATUS_FREEZN.setCellValue("防冻状态");
			HSSFCell STATUS_ERROR = headRow3.createCell(columncount++);
			STATUS_ERROR.setCellStyle(titlestyle);
			STATUS_ERROR.setCellValue("故障状态");
		}
		
		if(heatData.indexOf("o")>=0)
		{
			HSSFCell cellI = headRow3.createCell(columncount++);
			cellI.setCellStyle(titlestyle);
			cellI.setCellValue("热泵启/停");
		}
		

		for(int i=0;i<heatSource1.size();i++)
		{
			Map<String, Object> current=heatSource1.get(i);
			HSSFRow datarow = sheet3.createRow(sheet1rowcount++);
			columncount=0; //临时变量归零
			HSSFCell celltemp = datarow.createCell(columncount++);
			celltemp.setCellStyle(style_);
			celltemp.setCellValue(current.get("FTIME").toString());
			
			if(heatData.indexOf("n")>=0)
			{
				HSSFCell cellV1 = datarow.createCell(columncount++);
				cellV1.setCellStyle(style_);
				Object v1=current.get("STATUS_OPEN"); 
				if(v1!=null&&v1.toString().length()>0)
				{
					cellV1.setCellValue(v1.toString());
				}
				else
				{
					cellV1.setCellValue("0");
				}
				
				HSSFCell cellV2 = datarow.createCell(columncount++);
				cellV2.setCellStyle(style_);
				Object v2=current.get("STATUS_MODEL"); 
				if(v2!=null&&v2.toString().length()>0)
				{
					cellV2.setCellValue(v2.toString());
				}
				else
				{
					cellV2.setCellValue("0");
				}
				
				
				HSSFCell cellV3 = datarow.createCell(columncount++);
				cellV3.setCellStyle(style_);
				Object v3=current.get("STATUS_FOG"); 
				if(v3!=null&&v3.toString().length()>0)
				{
					cellV3.setCellValue(v3.toString());
				}
				else
				{
					cellV3.setCellValue("0");
				}
				
				
				HSSFCell cellV4 = datarow.createCell(columncount++);
				cellV4.setCellStyle(style_);
				Object v4=current.get("STATUS_FREEZN"); 
				if(v4!=null&&v4.toString().length()>0)
				{
					cellV4.setCellValue(v4.toString());
				}
				else
				{
					cellV4.setCellValue("0");
				}
				
				
				HSSFCell cellV5 = datarow.createCell(columncount++);
				cellV5.setCellStyle(style_);
				Object v5=current.get("STATUS_ERROR"); 
				if(v5!=null&&v5.toString().length()>0)
				{
					cellV5.setCellValue(v5.toString());
				}
				else
				{
					cellV5.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("o")>=0)
			{
				HSSFCell cellI = datarow.createCell(columncount++);
				cellI.setCellStyle(style_);
				Object I=current.get("HEATPUMP_STATUS");
				if(I!=null&&I.toString().length()>0)
				{
					cellI.setCellValue(I.toString());
				}
				else
				{
					cellI.setCellValue("0");
				}
			}
		}
		//状态数据结束
		
		// 生成第四个表格："1#单元温度"。
		HSSFSheet sheet4 =wb.createSheet("1#单元温度");		
		// 设置默认宽度为15字节
		sheet4.setDefaultColumnWidth(15);
		sheet1rowcount=0;
		HSSFRow headRow4 = sheet4.createRow(sheet1rowcount++);
		columncount=0;
		HSSFCell cellt4 = headRow4.createCell(columncount++);
		cellt4.setCellStyle(titlestyle);
		cellt4.setCellValue("时间");
		if(heatData.indexOf("q")>=0)
		{
			HSSFCell cellV = headRow4.createCell(columncount++);
			cellV.setCellStyle(titlestyle);
			cellV.setCellValue("出水");
		}
		
		if(heatData.indexOf("r")>=0)
		{
			HSSFCell cellI = headRow4.createCell(columncount++);
			cellI.setCellStyle(titlestyle);
			cellI.setCellValue("外盘1");
		}
		
		if(heatData.indexOf("s")>=0)
		{
			HSSFCell cellPF = headRow4.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("外盘2");
		}
		
		if(heatData.indexOf("t")>=0)
		{
			HSSFCell cellPF = headRow4.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("吸气1");
		}
		
		if(heatData.indexOf("u")>=0)
		{
			HSSFCell cellPF = headRow4.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("吸气2");
		}
		
		if(heatData.indexOf("v")>=0)
		{
			HSSFCell cellPF = headRow4.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("阀后1");
		}
		
		if(heatData.indexOf("w")>=0)
		{
			HSSFCell cellPF = headRow4.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("阀后2");
		}
		
		if(heatData.indexOf("x")>=0)
		{
			HSSFCell cellPF = headRow4.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("排气1");
		}
		
		if(heatData.indexOf("y")>=0)
		{
			HSSFCell cellPF = headRow4.createCell(columncount++);
			cellPF.setCellStyle(titlestyle);
			cellPF.setCellValue("排气2");
		}
		
		if(heatData.indexOf("p")>=0)
		{
			HSSFCell cellPF1 = headRow4.createCell(columncount++);
			cellPF1.setCellStyle(titlestyle);
			cellPF1.setCellValue("1#单元开度1");
			HSSFCell cellPF2 = headRow4.createCell(columncount++);
			cellPF2.setCellStyle(titlestyle);
			cellPF2.setCellValue("1#单元开度2");
		}		
		
		for(int i=0;i<heatSource5.size();i++)
		{
			Map<String, Object> current=heatSource5.get(i);
			HSSFRow datarow = sheet4.createRow(sheet1rowcount++);
			columncount=0; //临时变量归零
			HSSFCell celltemp = datarow.createCell(columncount++);
			celltemp.setCellStyle(style_);
			celltemp.setCellValue(current.get("FTIME").toString());
			
			if(heatData.indexOf("q")>=0)
			{
				HSSFCell cellV = datarow.createCell(columncount++);
				cellV.setCellStyle(style_);
				Object v=current.get("OUTLET_WATER_T"); //室外温度
				if(v!=null&&v.toString().length()>0)
				{
					cellV.setCellValue(v.toString());
				}
				else
				{
					cellV.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("r")>=0)
			{
				HSSFCell cellI = datarow.createCell(columncount++);
				cellI.setCellStyle(style_);
				Object I=current.get("EXTR_T_1");
				if(I!=null&&I.toString().length()>0)
				{
					cellI.setCellValue(I.toString());
				}
				else
				{
					cellI.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("s")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object pf=current.get("EXTR_T_2");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("t")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object pf=current.get("SUCTION_T_1");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			}
			
			if(heatData.indexOf("u")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object pf=current.get("SUCTION_T_2");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			} 
			
			if(heatData.indexOf("v")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object pf=current.get("OUTLET_T_1");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			} 
			
			if(heatData.indexOf("w")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object pf=current.get("OUTLET_T_2");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			} 
			
			if(heatData.indexOf("x")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object pf=current.get("EXH_T_1");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			} 
			
			if(heatData.indexOf("y")>=0)
			{
				HSSFCell cellPF = datarow.createCell(columncount++);
				cellPF.setCellStyle(style_);
				Object pf=current.get("EXH_T_2");
				if(pf!=null&&pf.toString().length()>0)
				{
					cellPF.setCellValue(pf.toString());
				}
				else
				{
					cellPF.setCellValue("0");
				}
			} 
			
			if(heatData.indexOf("p")>=0)
			{
				HSSFCell cellPF1 = datarow.createCell(columncount++);
				cellPF1.setCellStyle(style_);
				HSSFCell cellPF2 = datarow.createCell(columncount++);
				cellPF2.setCellStyle(style_);
				Object pf1=current.get("OPENING_1");
				Object pf2=current.get("OPENING_2");
				if(pf1!=null&&pf1.toString().length()>0)
				{
					cellPF1.setCellValue(pf1.toString());
				}
				else
				{
					cellPF1.setCellValue("0");
				}
				
				if(pf2!=null&&pf2.toString().length()>0)
				{
					cellPF2.setCellValue(pf2.toString());
				}
				else
				{
					cellPF2.setCellValue("0");
				}
			}
		}
		//状态数据结束
		
		try {
			output.flush();
			wb.write(output);
			output.close();
		} catch (IOException e) {
			Log.info("getEleExcel error IOException");
		}
	}
}
