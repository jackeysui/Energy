package com.linyang.energy.service.impl;

import java.io.IOException;import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.leegern.util.NumberUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esotericsoftware.minlog.Log;
import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.energy.common.CommonResource;
import com.linyang.energy.mapping.authmanager.RateBeanMapper;
import com.linyang.energy.mapping.energydataquery.ERateQueryMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.model.RateSectorBean;
import com.linyang.energy.service.ERateQueryService;import com.linyang.energy.utils.DataUtil;
import com.linyang.util.DoubleUtils;

/**
 * 
 * @author guosen
 * @date 2014-11-24
 */
@Service
public class ERateQueryServiceImpl implements ERateQueryService {

	@Autowired
	private ERateQueryMapper eRateQueryMapper;
	@Autowired
	private MeterManagerMapper meterManagerMapper;
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	@Autowired
	private RateBeanMapper rateBeanMapper;

	@Override
	public Map<String, Object> queryIndicationInfo(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean hasMeter = false; //是否有计量点
		try {
			int sourceType = (Integer) param.get("sourceType");
            Long id = (Long) param.get("id");
            int type = (Integer) param.get("type");
            List dataTypes = (List) param.get("dataTypes"); //1表示电，2表示水，3表示气，4表示热
            Long ledgerId = null;
            LedgerBean ledger = null;
            List<MeterBean> meterList = new ArrayList<MeterBean>();
            if(type == 1){
                ledgerId = id;
                meterList = this.meterManagerMapper.getMeterListByLedgerId(ledgerId);
                //表格标题名
                ledger = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
                result.put("ledgerName", ledger.getLedgerName());
            }
            else if (type == 2){
                MeterBean meter = this.meterManagerMapper.getMeterDataByPrimaryKey(id);
                ledgerId = meter.getLedgerId();
                meterList.add(meter);
                //表格标题名
                ledger = this.ledgerManagerMapper.selectByLedgerId(ledgerId);
                result.put("ledgerName", meter.getMeterName());
            }
            if(meterList != null && meterList.size() > 0){ //是否有计量点
                hasMeter = true;
            }
			
			//得到该用户的费率ID
			Long rateId = null;
            if(ledger != null){
                rateId = ledger.getRateId();
            }
			if(meterList != null && meterList.size() > 0 && dataTypes != null) {
                for (int n = 0; n < dataTypes.size(); n++) {
                    List<Map<String, Object>> totalList = new ArrayList<Map<String,Object>>();
                    Short dataType = Short.parseShort(dataTypes.get(n).toString());

                    for (MeterBean meterBean : meterList) {
                        if(!meterBean.getMeterType().equals(dataType)){
                            continue;
                        }
                        Map<String, Object> eRateMap = new HashMap<String, Object>();
                        eRateMap.put("meterName", meterBean.getMeterName());//计量点名称
                        LedgerBean ledgerBean = this.ledgerManagerMapper.selectByLedgerId(meterBean.getLedgerId());
                        String name = ledgerBean.getLedgerName();
                        eRateMap.put("ledgerName", name); //所属分户名
                        Double pt = (meterBean.getPt()==null?1:Double.valueOf( meterBean.getPt() ));
                        Double ct = (meterBean.getCt() ==null?1:Double.valueOf( meterBean.getCt() ));
                        if(dataType==2){
                        	eRateMap.put("ptct", pt);//倍率
                        }
                        else{
                        	eRateMap.put("ptct", new DecimalFormat("#.####").format(pt*ct));//倍率
                        }
                        Long meterId = meterBean.getMeterId();
                        String address =  meterBean.getAmmeterAddress();//表地址
                        eRateMap.put("address", address);
                        //计算百分比
                        int percent = 100;
                        if(type == 1){
                            List<Map<String, Object>> ledgerMeter = this.meterManagerMapper.getLedgerMeter(ledgerId, meterId);
                            int addAttr = Integer.valueOf(ledgerMeter.get(0).get("ADD_ATTR").toString());
                            percent = Integer.valueOf(ledgerMeter.get(0).get("PCT").toString()) * addAttr;
                        }

                        Map<String, Object> totalMap = null;
                        if (sourceType == 2){    //15分钟曲线
                            totalMap = curIndicationProcess((Date) param.get("beginTime"), (Date) param.get("endTime"), meterId, pt, ct);
                        }
                        else {   //日
                            totalMap = dataProcess((Date) param.get("beginTime"), (Date) param.get("endTime"), meterId, 0, "总", pt, ct,dataType);
                        }
                        if (totalMap != null){
                            totalMap.put("percent", percent);
                            eRateMap.putAll(totalMap);// 总电量示值
                        }

                        if (sourceType == 1 && rateId != null && rateId != 0 && dataType==1) {
                            List<RateSectorBean> rateSectorList = this.rateBeanMapper.getSectorData(rateId);
                            if(rateSectorList != null && rateSectorList.size() > 0) {
                                List<Map<String, Object>> rateList = new ArrayList<Map<String,Object>>();//分费率
                                for (RateSectorBean rateSectorBean : rateSectorList) {
                                    int rateNum = (int) rateSectorBean.getSectorId();
                                    String rateName = rateSectorBean.getSectorName();
                                    Map<String, Object> rateMap =
                                        dataProcess((Date)param.get("beginTime"), (Date)param.get("endTime"), meterId, rateNum, rateName, pt, ct,dataType);

                                    rateMap.put("percent", percent);
                                    rateList.add(rateMap);
                                }
                                eRateMap.put("rateList", rateList);//分费率电量示值
                            }
                        }
                        totalList.add(eRateMap);
                    }
                    result.put("total"+dataType.toString(), totalList);
                }
			}
		} catch (NumberFormatException e) {
			Log.info("queryIndicationInfo error NumberFormatException");
		}
		
		result.put("hasMeter", hasMeter);
		return result;
	}

    @Override
    public Map<String, Object> getChildQData(Map<String, Object> param){
        Map<String, Object> result = new HashMap<String, Object>();
        int sourceType = (Integer) param.get("sourceType"); //1表示日冻结，2表示15分钟曲线
        Long ledgerId = (Long) param.get("id"); //分户ID
        Date beginTime = (Date) param.get("beginTime"); //开始时间
        Date endTime = (Date) param.get("endTime"); //结束时间
        List dataTypes = (List) param.get("dataTypes"); //1表示电，2表示水，3表示气，4表示热
        List<Map<String, Object>> total = new ArrayList<Map<String, Object>>();
        if(dataTypes != null && dataTypes.size() > 0){
            if(dataTypes.size() > 1){
            //有多个类型，肯定是分户
                for (int n = 0; n < dataTypes.size(); n++) {
                    List<Map<String, Object>> resultItem = new ArrayList<Map<String, Object>>();
                    Short dataType = Short.parseShort(dataTypes.get(n).toString());
                    // 直接显示在分户下的采集点
                    List<MeterBean> childDcp = this.meterManagerMapper.getShowMeterByLedgerId(ledgerId);
                    if(childDcp != null && childDcp.size() > 0) {
                        for (MeterBean meterBean : childDcp) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            if(!meterBean.getMeterType().equals(dataType)){
                                map.put("NAME", meterBean.getMeterName());
                                map.put("VALUE", "-");
                            }else{
                                Double pt = (meterBean.getPt()==null?1:Double.valueOf( meterBean.getPt() ));
                                Double ct = (meterBean.getCt() ==null?1:Double.valueOf( meterBean.getCt() ));
                                Long meterId = meterBean.getMeterId();
                                Map<String, Object> totalMap = null;
                                if (sourceType == 2){
                                    totalMap = curIndicationProcess(beginTime, endTime, meterId, pt, ct);
                                }
                                else{
                                    totalMap = dataProcess(beginTime, endTime, meterId, 0, "总", pt, ct, dataType);
                                }
                                double energy = 0;
                                if(totalMap != null && totalMap.keySet().contains("energy")){
                                    energy = Double.valueOf(totalMap.get("energy").toString());
                                }
                                map.put("NAME", meterBean.getMeterName());
                                map.put("VALUE", NumberUtil.formatDouble(energy, NumberUtil.PATTERN_DOUBLE));
                            }

                            resultItem.add(map);
                        }
                    }
                    // 子分户
                    List<Long> childEmo = this.meterManagerMapper.getLedgerIdsByParent(ledgerId);
                    if(childEmo != null && childEmo.size() > 0){
                        for(int i = 0; i < childEmo.size(); i++){
                            Long ledId = childEmo.get(i);
                            LedgerBean led = ledgerManagerMapper.selectByLedgerId(ledId);
                            Map<String, Object> ledMap = new HashMap<String, Object>();
                            String ledName = led.getLedgerName();
                            double ledValue = 0D;
                            List<MeterBean> meterList = eRateQueryMapper.getComputeMetersWithMeterType(ledId,dataType);
                            if(meterList != null && meterList.size() > 0) {
                                boolean hasData = false;
                                for (MeterBean meterBean : meterList) {
                                    if(!meterBean.getMeterType().equals(dataType)){
                                        continue;
                                    }
                                    hasData = true;
                                    Double pt = (meterBean.getPt()==null?1:Double.valueOf( meterBean.getPt() ));
                                    Double ct = (meterBean.getCt() ==null?1:Double.valueOf( meterBean.getCt() ));
                                    Long meterId = meterBean.getMeterId();
                                    Map<String, Object> totalMap = null;
                                    if (sourceType == 2){
                                        totalMap = curIndicationProcess(beginTime, endTime, meterId, pt, ct);
                                    }
                                    else{
                                        totalMap = dataProcess(beginTime, endTime, meterId, 0, "总", pt, ct,dataType);
                                    }
                                    Double energy = 0D;
                                    if(totalMap != null && totalMap.keySet().contains("energy")){
                                        List<Map<String,Object>> mapList = meterManagerMapper.getLedgerMeter(ledId, meterId);
                                        if(mapList != null && mapList.size() > 0){ //计算百分占比
                                            Map<String,Object> map = mapList.get(0);
                                            int addAttr = Integer.valueOf(map.get("ADD_ATTR").toString());
                                            int percent = Integer.valueOf(map.get("PCT").toString());
                                            energy = new BigDecimal(Double.valueOf(totalMap.get("energy").toString())).multiply(new BigDecimal(addAttr)).multiply(new BigDecimal(percent)).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                        }
                                    }
                                    ledValue = DataUtil.doubleAdd_new(ledValue, energy,5);
                                }
                                if(hasData){
                                    ledMap.put("NAME", ledName);
                                    ledMap.put("VALUE", NumberUtil.formatDouble(ledValue, NumberUtil.PATTERN_DOUBLE));
                                }else{
                                    ledMap.put("NAME", ledName);
                                    ledMap.put("VALUE", "-");
                                }
                            }else{
                                ledMap.put("NAME", ledName);
                                ledMap.put("VALUE", "-");
                            }
                            resultItem.add(ledMap);
                        }
                    }
                    result.put(dataTypes.get(n).toString(), resultItem);
                    

                }
                //计算所有类型表总值并转换成标煤
                CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
                List<Short> meterTypeList = new ArrayList<Short>();
                List<MeterBean> allMeterList = eRateQueryMapper.getComputeMeters(ledgerId);
                for (MeterBean mBean : allMeterList) {
                    if(mBean.getMeterType() != null && mBean.getMeterType() != 0 && !meterTypeList.contains(mBean.getMeterType())){
                        meterTypeList.add(mBean.getMeterType());
                    }
                }
                for(Short meterType : meterTypeList){
                    Map<String, Object> map = new HashMap<String, Object>();
                    List<MeterBean> meterList = eRateQueryMapper.getComputeMeters(ledgerId);
                    Double ledValue = 0d;
                    if(meterList != null && meterList.size() > 0) {
                        for (MeterBean meterBean : meterList) {
                            Double pt = (meterBean.getPt()==null?1:Double.valueOf( meterBean.getPt() ));
                            Double ct = (meterBean.getCt() ==null?1:Double.valueOf( meterBean.getCt() ));
                            Long meterId = meterBean.getMeterId();
                            Map<String, Object> totalMap = dataProcess(beginTime, endTime, meterId, 0, "总", pt, ct,meterType);
                            Double energy = 0D;
                            if(totalMap != null && totalMap.keySet().contains("energy")){
                                List<Map<String,Object>> mapList = meterManagerMapper.getLedgerMeter(ledgerId, meterId);
                                if(mapList != null && mapList.size() > 0){ //计算百分占比
                                    Map<String,Object> mapTemp = mapList.get(0);
                                    int addAttr = Integer.valueOf(mapTemp.get("ADD_ATTR").toString());
                                    int percent = Integer.valueOf(mapTemp.get("PCT").toString());
                                    energy = new BigDecimal(Double.valueOf(totalMap.get("energy").toString())).multiply(new BigDecimal(addAttr)).multiply(new BigDecimal(percent)).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                }
                            }
                            ledValue = DataUtil.doubleAdd_new(ledValue, energy,5);
                        }
                    }
                    LedgerBean led = ledgerManagerMapper.selectByLedgerId(ledgerId);
                    map.put("NAME", led.getLedgerName());
                    if(meterType == 1){
                        map.put("NAME", "电");
                        ledValue = new BigDecimal(ledValue).multiply(new BigDecimal(resource.getElecUnit())).divide(new BigDecimal(1000), 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    }else if(meterType == 2){
                        map.put("NAME", "水");
                        ledValue = new BigDecimal(ledValue).multiply(new BigDecimal(resource.getWaterUnit())).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    }else if(meterType == 3){
                        map.put("NAME", "气");
                        ledValue = new BigDecimal(ledValue).multiply(new BigDecimal(resource.getGasUnit())).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    }else if(meterType == 4){
                        map.put("NAME", "热");
                        ledValue = new BigDecimal(ledValue).multiply(new BigDecimal(resource.getHotUnit())).divide(new BigDecimal(1000), 3, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    }
                    map.put("VALUE", ledValue);
                    total.add(map);
                }
                result.put("total", total);
            }
            else{

                List<Map<String, Object>> resultItem = new ArrayList<Map<String, Object>>();
                Integer dataType = Integer.parseInt(dataTypes.get(0).toString());
                // 直接显示在分户下的采集点
                List<MeterBean> childDcp = this.meterManagerMapper.getShowMeterByLedgerId(ledgerId);
                if(childDcp != null && childDcp.size() > 0) {
                    for (MeterBean meterBean : childDcp) {
                        if(meterBean.getMeterType() != dataType.shortValue()){
                            continue;
                        }
                        Map<String, Object> map = new HashMap<String, Object>();
                        Double pt = (meterBean.getPt()==null?1:Double.valueOf( meterBean.getPt() ));
                        Double ct = (meterBean.getCt() ==null?1:Double.valueOf( meterBean.getCt() ));
                        Long meterId = meterBean.getMeterId();
                        Map<String, Object> totalMap = null;
                        if (sourceType == 2){
                            totalMap = curIndicationProcess(beginTime, endTime, meterId, pt, ct);
                        }
                        else{
                            totalMap = dataProcess(beginTime, endTime, meterId, 0, "总", pt, ct, dataType);
                        }
                        double energy = 0D;
                        if(totalMap != null && totalMap.keySet().contains("energy")){
                            energy = Double.valueOf(totalMap.get("energy").toString());
                        }
                        map.put("NAME", meterBean.getMeterName());
                        map.put("VALUE", NumberUtil.formatDouble(energy, NumberUtil.PATTERN_DOUBLE));

                        resultItem.add(map);
                    }
                }
                // 子分户
                List<Long> childEmo = this.meterManagerMapper.getLedgerIdsByParent(ledgerId);
                if(childEmo != null && childEmo.size() > 0){
                    for(int i = 0; i < childEmo.size(); i++){
                        Long ledId = childEmo.get(i);
                        LedgerBean led = ledgerManagerMapper.selectByLedgerId(ledId);
                        Map<String, Object> ledMap = new HashMap<String, Object>();
                        String ledName = led.getLedgerName();
                        double ledValue = 0;
                        List<MeterBean> meterList = eRateQueryMapper.getComputeMeters(ledId);
                        if(meterList != null && meterList.size() > 0) {
                            for (MeterBean meterBean : meterList) {
                                Double pt = (meterBean.getPt()==null?1:Double.valueOf( meterBean.getPt() ));
                                Double ct = (meterBean.getCt() ==null?1:Double.valueOf( meterBean.getCt() ));
                                Long meterId = meterBean.getMeterId();
                                Map<String, Object> totalMap = null;
                                if (sourceType == 2){
                                    totalMap = curIndicationProcess(beginTime, endTime, meterId, pt, ct);
                                }
                                else{
                                    totalMap = dataProcess(beginTime, endTime, meterId, 0, "总", pt, ct,dataType);
                                }
                                Double energy = 0D;
                                if(totalMap != null && totalMap.keySet().contains("energy")){
                                    List<Map<String,Object>> mapList = meterManagerMapper.getLedgerMeter(ledId, meterId);
                                    if(mapList != null && mapList.size() > 0){ //计算百分占比
                                        Map<String,Object> map = mapList.get(0);
                                        int addAttr = Integer.valueOf(map.get("ADD_ATTR").toString());
                                        int percent = Integer.valueOf(map.get("PCT").toString());
                                        energy = new BigDecimal(Double.valueOf(totalMap.get("energy").toString())).multiply(new BigDecimal(addAttr)).multiply(new BigDecimal(percent)).divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                                    }
                                }
                                ledValue = DataUtil.doubleAdd_new(ledValue, energy,5);
                            }
                        }
                        ledMap.put("NAME", ledName);
                        ledMap.put("VALUE", NumberUtil.formatDouble(ledValue, "#.####"));

                        resultItem.add(ledMap);
                    }
                }
                result.put(dataTypes.get(0).toString(), resultItem);
            }
        }
        if(dataTypes!=null && dataTypes.size() > 0)
            result.put("size", dataTypes.size());
        return result;
    }

	/**
	 * 日电能示值-数据处理
	 */
	private Map<String, Object> dataProcess(Date beginTime, Date endTime, Long meterId, int rateNum, String rateName, Double pt, Double ct,int dataType) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rateNum", rateNum);//费率ID
        if(dataType == 1){
            Double startFaeRate_old = null;
            Double endFaeRate_old = null;
            Double startFaeRate_new = null;
            Double endFaeRate_new = null;
            if(rateNum == 0) {
                startFaeRate_old = this.eRateQueryMapper.queryTotalFaeRate(meterId, beginTime);
                endFaeRate_old = this.eRateQueryMapper.queryTotalFaeRate(meterId, endTime);

                startFaeRate_new = this.eRateQueryMapper.getViewDayETotal(meterId, beginTime);
                endFaeRate_new = this.eRateQueryMapper.getViewDayETotal(meterId, endTime);
            }else {
                startFaeRate_old = this.eRateQueryMapper.queryFaeRate(meterId, beginTime, rateNum);
                endFaeRate_old = this.eRateQueryMapper.queryFaeRate(meterId, endTime, rateNum);

                startFaeRate_new = this.eRateQueryMapper.getViewDayERate(meterId, beginTime, rateNum);
                endFaeRate_new = this.eRateQueryMapper.getViewDayERate(meterId, endTime, rateNum);
            }
            // 处理 结束示值 起始示值
            if(startFaeRate_old != null && endFaeRate_old != null && startFaeRate_old > endFaeRate_old){
                Double startTmp = startFaeRate_old;
                startFaeRate_old = endFaeRate_old;
                endFaeRate_old = startTmp;
            }
            if(startFaeRate_new != null && endFaeRate_new != null && startFaeRate_new > endFaeRate_new){
                Double startTmp2 = startFaeRate_new;
                startFaeRate_new = endFaeRate_new;
                endFaeRate_new = startTmp2;
            }

            map.put("rateName", rateName);//费率名称
            map.put("ratestart", startFaeRate_old );//起始示值
            map.put("rateend", endFaeRate_old);//结束示值
            map.put("ratestart_new", startFaeRate_new);
            map.put("rateend_new", endFaeRate_new);

            if(startFaeRate_new != null && endFaeRate_new != null) {
            	Double energy =  DataUtil.doubleMultiply_new(DataUtil.doubleSubtract(endFaeRate_new, startFaeRate_new), pt * ct,5);//Double energy = (endFaeRate_new*100 - startFaeRate_new*100) * pt * ct /100;
                map.put("energy", DoubleUtils.getDoubleValue(energy, 5));//电能量
            }
        }else if(dataType == 2){
            Double startValue = this.eRateQueryMapper.queryEWater(meterId, beginTime);
            Double endValue= this.eRateQueryMapper.queryEWater(meterId, endTime);
            map.put("rateName", "水");//费率名称
            map.put("ratestart", startValue);//起始示值
            map.put("rateend", endValue);//结束示值
            double rate = pt;
            if(startValue != null && endValue != null) {
                Double energy = DataUtil.doubleMultiply(DataUtil.doubleSubtract(endValue, startValue), rate);
                map.put("energy", DoubleUtils.getDoubleValue(energy, 2));//
            }
        }else if(dataType == 3){
            Double startValue = this.eRateQueryMapper.queryEGas(meterId, beginTime);
            Double endValue= this.eRateQueryMapper.queryEGas(meterId, endTime);
            map.put("rateName", "气");//费率名称
            map.put("ratestart", startValue);//起始示值
            map.put("rateend", endValue);//结束示值
            int rate = 1;
            if(startValue != null && endValue != null) {
                Double energy = DataUtil.doubleMultiply(DataUtil.doubleSubtract(endValue, startValue), rate);
                map.put("energy", DoubleUtils.getDoubleValue(energy, 2));//
            }
        }else if(dataType == 4){
            Double startValue = this.eRateQueryMapper.queryEHeat(meterId, beginTime);
            Double endValue= this.eRateQueryMapper.queryEHeat(meterId, endTime);
            map.put("rateName", "热");//费率名称
            map.put("ratestart", startValue);//起始示值
            map.put("rateend", endValue);//结束示值
            int rate = 1;
            if(startValue != null && endValue != null) {
                Double energy = DataUtil.doubleMultiply(DataUtil.doubleSubtract(endValue, startValue), rate);
                map.put("energy", DoubleUtils.getDoubleValue(energy, 2));//
            }
        }
		return map;
	}

    /**
     * 曲线电能示值-数据处理
     */
    private Map<String, Object> curIndicationProcess(Date beginTime, Date endTime, Long meterId, Double pt, Double ct) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rateNum", 0);// 费率ID
        map.put("rateName", "总");// 费率名称
        Double startFae_new = this.eRateQueryMapper.getViewCurETotal(meterId, beginTime);
        Double endFae_new = this.eRateQueryMapper.getViewCurETotal(meterId, endTime);
        // 判断数值大小
        if(startFae_new > endFae_new){
            startFae_new = startFae_new + endFae_new;
            endFae_new = startFae_new - endFae_new;
            startFae_new = startFae_new - endFae_new;
        }
        map.put("ratestart_new", startFae_new);
        map.put("rateend_new", endFae_new);
        if (startFae_new != null && endFae_new != null) {
            Double energy = DataUtil.doubleMultiply_new(DataUtil.doubleSubtract(endFae_new, startFae_new ), pt * ct,5);
            map.put("energy", DoubleUtils.getDoubleValue(energy, 5));// 电能量
        }
        Double ratestart = this.eRateQueryMapper.getCurETotal(meterId, beginTime);
        Double rateend = this.eRateQueryMapper.getCurETotal(meterId, endTime);
        map.put("ratestart", ratestart);// 起始示值
        map.put("rateend", rateend);// 结束示值

        return map;
    }

	@SuppressWarnings("unchecked")
	@Override
	public void optExcel(ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param) {
		//声明一个工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
        
        List dataTypes = (List) param.get("dataTypes"); //1表示电，2表示水，3表示气，4表示热
        try{
            if(dataTypes != null && dataTypes.size() > 0){
                for (int n = 0; n < dataTypes.size(); n++) {
                    Integer dataType = Integer.parseInt(dataTypes.get(n).toString());
                    //定义一个表格
                    HSSFSheet sheet = null;
                    if(dataType == 1){
                        sheet = wb.createSheet("电能示值");
                    }else if(dataType == 2){
                        sheet = wb.createSheet("水能示值");
                    }else if(dataType == 3){
                        sheet = wb.createSheet("气能示值");
                    }else {
                        sheet = wb.createSheet("热能示值");
                    }

                    sheet.setColumnWidth((short)0,(short)24*256);
                    sheet.setColumnWidth((short)1,(short)24*256);
                    sheet.setColumnWidth((short)2,(short)24*256);
                    sheet.setColumnWidth((short)3,(short)24*256);
                    sheet.setColumnWidth((short)4,(short)24*256);
                    sheet.setColumnWidth((short)5,(short)24*256);
                    sheet.setColumnWidth((short)6,(short)24*256);
                    sheet.setColumnWidth((short)7,(short)24*256);
                    sheet.setColumnWidth((short)8,(short)24*256);

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
                    HSSFCellStyle style_total =wb.createCellStyle();
                    style_total.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    style_total.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
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

                    //定义表名的字体样式
                    HSSFCellStyle trstyle =wb.createCellStyle();
                    HSSFFont trfont = wb.createFont();
                    trfont.setColor(HSSFColor.BLACK.index);
                    trfont.setFontName("黑体");
                    trfont.setFontHeightInPoints((short) 14);//设置字体大小
                    trstyle.setFont(trfont);
                    trstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    trstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                    //行号
                    int rowIndex = 0;
                    //第一行----时间选择
                    HSSFRow nameRow = sheet.createRow(rowIndex++);
                    nameRow.createCell(0).setCellValue("起始时间");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date beginTime = (Date)param.get("excelBegin");
                    Date endTime = (Date)param.get("excelEnd");
                    nameRow.createCell(1).setCellValue(sdf.format(beginTime));
                    nameRow.createCell(2).setCellValue("结束时间");
                    nameRow.createCell(3).setCellValue(sdf.format(endTime));

                    //分户总数据
                    List<Map<String, Object>> totalList = (List<Map<String, Object>>) result.get("total" + dataType);
                    rowIndex = bulidExcel(result.get("ledgerName").toString(), sheet, style_,style_total, titlestyle, trstyle, totalList, rowIndex,dataType);
                    rowIndex++;//空行
                }
            }
		
			outputStream.flush();
			wb.write(outputStream);
			outputStream.close();
        }catch( IOException e ){
        	Log.info("optExcel error IOException");
		}
	}

	/**
	 * 数据拼接
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
	@SuppressWarnings("unchecked")
	private int bulidExcel(String type, HSSFSheet sheet, HSSFCellStyle style_, HSSFCellStyle style_total,HSSFCellStyle titlestyle, HSSFCellStyle trstyle,
			List<Map<String, Object>> list, int rowIndex,int dataType) throws UnsupportedEncodingException {

        if(list != null && list.size() > 0){
            //表格标题
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 3, 4));
            HSSFRow titleRow = sheet.createRow(rowIndex++);
            HSSFCell titleCell = titleRow.createCell(3);
            titleCell.setCellStyle(trstyle);
            try{titleCell.setCellValue(URLDecoder.decode(type,"UTF-8"));} catch(UnsupportedEncodingException e){ Log.error(this.getClass()+"decode error!");}

            HSSFCell totalCell = titleRow.createCell(7);
            totalCell.setCellStyle(style_total);
            String unit = "kWh";
            //表格表头--- 用户名、计量点、表头编号、倍率、表地址，费率、起始示值、结束示值、电量
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

            cellA.setCellValue("用户名");
            cellB.setCellValue("采集点");
            cellD.setCellValue("倍率");
            cellE.setCellValue("表地址");
            cellF.setCellValue("费率");
            cellG.setCellValue("起始示值");
            cellH.setCellValue("结束示值");
            cellI.setCellValue("百分比(%)");
            if(dataType == 1){
                cellJ.setCellValue("电量(kWh)");
                unit = "kWh";
            }else if(dataType == 2){
                cellJ.setCellValue("水量(m³)");
                unit = "m³";
            }else if(dataType == 3){
                cellJ.setCellValue("气量(m³)");
                unit = "m³";
            }else if(dataType == 4){
                cellJ.setCellValue("热量(kWh)");
                unit = "kWh";
            }else{
                cellJ.setCellValue("电量(kWh)");
            }

            double totalEnergy = 0;
            //加载数据
            for( int i = 0 ; i < list.size( ) ; i ++ ){
                Map<String, Object> map = list.get(i);
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
                
                cell1A.setCellValue(checkIsNull(map.get("ledgerName")));
                cell1B.setCellValue(checkIsNull(map.get("meterName")));
                cell1D.setCellValue(checkIsNull(map.get("ptct")));
                cell1E.setCellValue(checkIsNull(map.get("address")));
                cell1F.setCellValue(checkIsNull(map.get("rateName")));
                cell1G.setCellValue(checkIsNull(map.get("ratestart")));
                cell1H.setCellValue(checkIsNull(map.get("rateend")));
                cell1I.setCellValue(checkIsNull(map.get("percent")));
                if(map.get("percent") != null && map.get("energy") != null){
                    double energy = Double.valueOf(map.get("energy").toString());
                    long percent = Long.valueOf(map.get("percent").toString());
                    energy = DoubleUtils.getDoubleValue(DataUtil.doubleDivide(DataUtil.doubleMultiply(energy, percent), 100, 5), 5);
                    totalEnergy = DataUtil.doubleAdd_new(totalEnergy, energy,5);
                    cell1J.setCellValue(energy);
                }
                else{
                    cell1J.setCellValue("");
                }

                //加载分费率数据
                if(map.get("rateList") != null){
                    List<Map<String, Object>> rateList = (List<Map<String, Object>>) map.get("rateList");
                    //下标是从0开始的。
                    //sheet.addMergedRegion(new CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1+rateList.size(), 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1+rateList.size(), 1, 1));
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1+rateList.size(), 2, 2));
                    sheet.addMergedRegion(new CellRangeAddress(rowIndex-1, rowIndex-1+rateList.size(), 3, 3));

                    for (Map<String, Object> map2 : rateList) {
                        HSSFRow rateRow = sheet.createRow(rowIndex++);
                        HSSFCell cell1rA = rateRow.createCell(0);
                        HSSFCell cell1rB = rateRow.createCell(1);
                        HSSFCell cell1rD = rateRow.createCell(2);
                        HSSFCell cell1rE = rateRow.createCell(3);
                        HSSFCell cell1rF = rateRow.createCell(4);
                        HSSFCell cell1rG = rateRow.createCell(5);
                        HSSFCell cell1rH = rateRow.createCell(6);
                        HSSFCell cell1rI = rateRow.createCell(7);
                        HSSFCell cell1rJ = rateRow.createCell(8);
                        cell1rA.setCellStyle(style_);
                        cell1rB.setCellStyle(style_);
                        cell1rD.setCellStyle(style_);
                        cell1rE.setCellStyle(style_);
                        cell1rF.setCellStyle(style_);
                        cell1rG.setCellStyle(style_);
                        cell1rH.setCellStyle(style_);
                        cell1rI.setCellStyle(style_);
                        cell1rJ.setCellStyle(style_);
                        cell1rF.setCellValue(checkIsNull(map2.get("rateName")));
                        cell1rG.setCellValue(checkIsNull(map2.get("ratestart")));
                        cell1rH.setCellValue(checkIsNull(map2.get("rateend")));
                        cell1rI.setCellValue(checkIsNull(map2.get("percent")));
                        if(map2.get("percent") != null && map2.get("energy") != null){
                            double energy = Double.valueOf(map2.get("energy").toString());
                            long percent = Long.valueOf(map2.get("percent").toString());
                            energy = DoubleUtils.getDoubleValue(DataUtil.doubleDivide(DataUtil.doubleMultiply(energy, percent), 100, 5), 5);
                            cell1rJ.setCellValue(energy);
                        }
                        else{
                            cell1rJ.setCellValue("");
                        }
                    }
                }
            }
            totalCell.setCellValue("合计："+ totalEnergy + unit);
        }
        return rowIndex;
	}
	
	/**
	 * 判断是否为空
	 * @param obj
	 * @return
	 */
	private String checkIsNull(Object obj) {
		if(obj == null) {
			return "";
		}
		return obj.toString();
	}
}
