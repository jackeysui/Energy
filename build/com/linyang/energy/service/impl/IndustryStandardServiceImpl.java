package com.linyang.energy.service.impl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.math.BigDecimal;import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leegern.util.NumberUtil;
import com.linyang.energy.mapping.industryStandard.IndustryStandardMapper;
import com.linyang.energy.service.IndustryStandardService;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.DoubleUtils;

/** 
* @Description
* @author Jijialu
* @date 2017年4月19日 下午6:44:16 
*/

@Service
public class IndustryStandardServiceImpl implements IndustryStandardService{

	@Autowired IndustryStandardMapper industryStandardMapper;
	
	@Override
	public List<Map<String, Object>> getIndustryStandardIdList() {
		return industryStandardMapper.getIndustryStandardIdList();
	}

	@Override
	public List<Map<String, Object>> getIndustryStandardPageList(Map<String, Object> params) {
		return industryStandardMapper.getIndustryStandardPageList(params);
	}

	@Override
	public Map<String, Object> getIndustryStandardById(Long standardId) {
		return industryStandardMapper.getIndustryStandardById(standardId);
	}
	
	@Override
	public Map<String, Object> insertStandard(Map<String, Object> standardInfo) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Long standardId = SequenceUtils.getDBSequence();
        String standardName = standardInfo.get("standardName").toString();       
        Double standardData = Double.valueOf(standardInfo.get("standardData").toString());
        String unitType = standardInfo.get("unitType").toString();
        Double aboveStandardData = Double.valueOf(standardInfo.get("aboveStandardData").toString());
        Double belowStandardData = Double.valueOf(standardInfo.get("belowStandardData").toString());
        
        //先检查名称是否重复
        int nameNum = this.industryStandardMapper.getStandardNum(standardName);
        if(nameNum > 0){
            result.put("message", "指标名重复");
            return result;
        } 
        
        this.industryStandardMapper.insertStandard(standardId, standardName, standardData, unitType, aboveStandardData, belowStandardData);   
        result.put("message", "");
        
        return result;
	}

	@Override
	public void updateStandard(Map<String, Object> standardInfo) {
		Long standardId = Long.valueOf(standardInfo.get("standardId").toString());
        String standardName = standardInfo.get("standardName").toString();       
        //标准数据
        Double standardData = Double.valueOf(standardInfo.get("standardData").toString());
        //单位
        String unitType = standardInfo.get("unitType").toString();
        //优秀评价值
        Double aboveStandardData = Double.valueOf(standardInfo.get("aboveStandardData").toString());
        //差 评价值
        Double belowStandardData = Double.valueOf(standardInfo.get("belowStandardData").toString());
        
        this.industryStandardMapper.updateStandard(standardId, standardName, standardData, unitType, aboveStandardData, belowStandardData);
	}

	@Override
	public void deleteStandard(Long standardId) {
		this.industryStandardMapper.deleteStandard(standardId);
	}

	@Override
	public List<Map<String, Object>> getLedgerStandardIdList(Long ledgerId) {
		return industryStandardMapper.getLedgerStandardIdList(ledgerId);
	}

	@Override
	public void insertOrUpdateLedgerStandard(Map<String, Object> standardInfo) {
		Long ledgerId = Long.valueOf(standardInfo.get("ledgerId").toString());
		Long standardId = Long.valueOf(standardInfo.get("standardId").toString());
		Double ledgerData = Double.valueOf(standardInfo.get("ledgerData").toString());
		Double unitConsumption = Double.valueOf(standardInfo.get("unitConsumption").toString());
		Double standardDiff = Double.valueOf(standardInfo.get("standardDiff").toString());
		Date startTime = DateUtil.convertStrToDate(standardInfo.get("beginDate").toString(), DateUtil.SHORT_PATTERN);
		Date endTime = DateUtil.convertStrToDate(standardInfo.get("endDate").toString(), DateUtil.SHORT_PATTERN);
		
		//判断记录是否存在，不存在插入一条记录，存在则更新已有记录
		int num = this.industryStandardMapper.getLedgerStandardNum(ledgerId, standardId);
		if (num > 0) {
			this.industryStandardMapper.updateLedgerStandard(ledgerId, standardId, ledgerData, unitConsumption, standardDiff, startTime, endTime);
		}else {
			this.industryStandardMapper.insertLedgerStandard(ledgerId, standardId, ledgerData, unitConsumption, standardDiff, startTime, endTime);
		}
	}

	@Override
	public Map<String, Object> getLedgerStandardInfo(Long ledgerId, Long standardId) {
		if (standardId == 0){
			//未传入standardId则默认选取企业某指标
			Map<String, Object> map = null;
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			list = this.industryStandardMapper.getLedgerStandardIdList(ledgerId);
			if (list.size() > 0) {
				map = new HashMap<String, Object>();
				map = list.get(0);
				standardId = Long.valueOf(map.get("STANDARD_ID").toString());
			}
		}
		Map<String, Object> result = this.industryStandardMapper.getLedgerStandardInfo(ledgerId, standardId);
		if (result != null) {
			//评价
			String assess = "";
			int diffFlag = 1;//与国标相比较，0为上升，1为下降
			Double unitConsumption = Double.valueOf(result.get("UNIT_CONSUMPTION").toString());
			Double standardData = Double.valueOf(result.get("STANDARD_DATA").toString());
			String unit = result.get("UNIT_TYPE").toString();
			Double aboveStandard = Double.valueOf(result.get("ABOVE_STANDARD_DATA").toString());
			Double belowStandard = Double.valueOf(result.get("BELOW_STANDARD_DATA").toString());
			String startTime = DateUtil.convertDateToStr(DateUtil.convertStrToDate(result.get("START_TIME").toString()), DateUtil.SHORT_PATTERN);
			String endTime = DateUtil.convertDateToStr(DateUtil.convertStrToDate(result.get("END_TIME").toString()), DateUtil.SHORT_PATTERN);
			/**
			 * 评价单耗分为3个区间
			 * 1.小于等于优秀值为优秀
			 * 2.大于等于差值为差
			 * 3.在优秀值与差值之间为标准
			 */
			if (unitConsumption <= belowStandard) {
				assess = "单耗处于优秀水平，请继续保持！";
			}else if (unitConsumption >= aboveStandard) {
				assess = "单耗处于较差的水平，请努力改善！";
			}else {
				assess = "单耗处于标准水平，请继续保持！";
			}
			result.put("ASSESS", assess);
			//计算国标差值，判断正负
			Double diff = DataUtil.doubleSubtract(unitConsumption, standardData);
			if (diff > 0) {
				diffFlag = 0;
			}else {
				diffFlag = 1;
			}
			//计算相同指标下击败用户数及排名,如果该指标平台只有一条数据则排名为1%,击败99%用户
			Integer standardSum = this.industryStandardMapper.getStandardSumById(standardId);
			Integer defeatNum = this.industryStandardMapper.getStandardDefeat(standardId, unitConsumption);
			Double defeatPercent = DoubleUtils.getDoubleValue(defeatNum/standardSum*100, 2);
			Double rank = DataUtil.doubleSubtract(100, defeatPercent);
			if (defeatPercent.equals(100d)) {
				defeatPercent = 99D;
				rank = 1D;
			}		
			Double standardPercent = new BigDecimal(unitConsumption).multiply(new BigDecimal(100)).divide(new BigDecimal(standardData), 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
			String unitCon = unitConsumption.toString()+unit;
			String standardDataStr = standardData.toString()+unit;
			result.put("UNIT_CON", unitCon);
			result.put("STANDARD_DATA", standardDataStr);
			result.put("START_TIME", startTime);
			result.put("END_TIME", endTime);
			result.put("STANDARD_PERCENT", standardPercent);
			result.put("DEFEAT_PERCENT", defeatPercent);
			result.put("RANK", rank);
			result.put("DIFF_FLAG", diffFlag);
		}
		
		return result;
	}

	@Override
	public Double queryLedgerQSum(Long ledgerId, Date beginDate, Date endDate) {
		//获取分户日电量之和，保留两位小数
		Double result = this.industryStandardMapper.queryLedgerQSum(ledgerId, beginDate, endDate);
		if (result != null) {
			String resultStr =  NumberUtil.formatDouble(result, NumberUtil.PATTERN_DOUBLE);
			return Double.valueOf(resultStr);
		}
		return result;
	}

}
