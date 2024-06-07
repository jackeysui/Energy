package com.linyang.energy.service.impl;

import java.text.DecimalFormat;
import java.util.*;

import com.leegern.util.NumberUtil;
import com.linyang.energy.service.ClassService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.mapping.classAnalysis.ClassMapper;
import com.linyang.energy.model.ProductConfigBean;
import com.linyang.energy.model.ProductOutputBean;
import com.linyang.energy.model.WorkingHourBean;
import com.linyang.energy.service.ClassProductService;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;

@Service
public class ClassProductServiceImpl implements ClassProductService{

	@Autowired
    private ClassMapper classMapper;

    @Autowired
    private ClassService classService;
	
	@Override
	public List<ProductConfigBean> queryProductConfigPageList(Map<String, Object> param) {
		List<ProductConfigBean> list = this.classMapper.queryProductConfigPageList(param);
		return list;
	}

	@Override
	public Map<String, Object> getProductConfigById(Long productId) {
		Map<String, Object> config = this.classMapper.getProductConfigById(productId);
		return config;
	}
	
	@Override
	public Map<String, Object> insertProductConfig(Map<String, Object> productConfigInfo) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Long ledgerId = Long.valueOf(productConfigInfo.get("ledgerId").toString());
        String productName = productConfigInfo.get("productName").toString();
        String productUnit = productConfigInfo.get("productUnit").toString();
        Integer convertRatio = Integer.valueOf(productConfigInfo.get("convertRatio").toString());
        Double unitConsumption = Double.valueOf(productConfigInfo.get("unitConsumption").toString());
        Integer isStandard = Integer.valueOf(productConfigInfo.get("isStandard").toString());
        Long productId = SequenceUtils.getDBSequence();
        
        //先检查名称是否重复
        int nameNum = this.classMapper.getProductConfigNum(ledgerId, productName);
        if(nameNum > 0){
            result.put("message", "产品名重复");
            return result;
        }
        
        //检查是否存在基准产品
        int standardNum = this.classMapper.getStandardProduct(ledgerId);
        if (standardNum > 0) {
        	if (isStandard == 0) {
        		result.put("message", "基准产品已存在");
				return result;
			}
        	//插入T_PRODUCT_CONFIG
        	this.classMapper.insertProductConfig(ledgerId, productId, productName, productUnit, convertRatio, unitConsumption,isStandard);
		}else {
			if (isStandard == 0) {
				//插入T_PRODUCT_CONFIG
	        	this.classMapper.insertProductConfig(ledgerId, productId, productName, productUnit, convertRatio, unitConsumption,isStandard);
			}else {
				result.put("message", "请先录入基准产品");
				return result;
			}
		}
        
        result.put("message", "");
        return result;
	}

	@Override
	public void updateProductConfig(Map<String, Object> productConfigInfo) {
		Long productId = Long.valueOf(productConfigInfo.get("productId").toString());
		String productName = productConfigInfo.get("productName").toString();
	    String productUnit = productConfigInfo.get("productUnit").toString();
	    Integer convertRatio = Integer.valueOf(productConfigInfo.get("convertRatio").toString());
	    Double unitConsumption = Double.valueOf(productConfigInfo.get("unitConsumption").toString());
        //修改T_PRODUCT_CONFIG
        this.classMapper.updateProductConfig(productId, productName, productUnit, convertRatio, unitConsumption);
	}

	@Override
	public Map<String, Object> deleteProductConfig(Long productId) {
		Map<String, Object> result = new HashMap<String, Object>();
		int isStandard = this.classMapper.getStandardById(productId);
		
		if (isStandard == 1) {
			//删除T_PRODUCT_CONFIG
			this.classMapper.deleteProductConfig(productId);
		}else {
			result.put("message", "基准产品无法删除");
			return result;
		}
		
		result.put("message", "");
		return result;
	}

	
	@Override
	public List<ProductOutputBean> queryProductOutputPageList(
			Map<String, Object> param) {
		List<ProductOutputBean> list = this.classMapper.queryProductOutputPageList(param);
		return list;
	}

    @Override
    public List<Map<String, Object>> getProductListByLedgerAll(Long ledgerId) {
        List<Map<String, Object>> list = this.classMapper.getProductListByLedgerAll(ledgerId);
        return list;
    }
	
	@Override
	public List<Map<String, Object>> getProductListByLedger(Long ledgerId, Long classId, String workshopIds) {
        List<Long> shopIdList = null;
        if(workshopIds.length()>0){
            shopIdList = new ArrayList<Long>();
            String[] shopIds = workshopIds.split(",");
            for(int i = 0; i < shopIds.length; i++){
                shopIdList.add(Long.valueOf(shopIds[i]));
            }
        }
		List<Map<String, Object>> list = this.classMapper.getProductListByLedger(ledgerId, classId, shopIdList);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getWorkshopListByProductId(Long productId) {
		List<Map<String, Object>> list = this.classMapper.getWorkshopListByProductId(productId);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getTeamListByProductId(Long productId,Long workshopId) {
		List<Map<String, Object>> list = this.classMapper.getTeamListByProductId(productId, workshopId);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getWorkshopListByLedgerId(Long ledgerId) {
		List<Map<String, Object>> list = this.classMapper.getWorkshopListByLedgerId(ledgerId);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getTeamListByWorkshopId(Long workshopId) {
		List<Map<String, Object>> list = this.classMapper.getTeamListByWorkshopId(workshopId);
		return list;
	}
	
	@Override
	public String getProductUnitByProductId(Long productId) {
		String unit = this.classMapper.getProductUnitByProductId(productId);
		return unit;
	}
	
	@Override
	public Map<String, Object> insertOrUpdateProductOutput(Map<String, Object> productOutputInfo) {
		Map<String,Object> result = new HashMap<String, Object>();
		
		Long outputId = Long.valueOf(productOutputInfo.get("outputId").toString());
		Long ledgerId = Long.valueOf(productOutputInfo.get("ledgerId").toString());
		Long productId = Long.valueOf(productOutputInfo.get("productId").toString());
		Long workshopId = Long.valueOf(productOutputInfo.get("workshopId").toString());
		Long teamId = Long.valueOf(productOutputInfo.get("teamId").toString());
		Date startTime = DateUtil.convertStrToDate(productOutputInfo.get("startTime").toString(), DateUtil.MOUDLE_PATTERN);
		Date endTime = DateUtil.convertStrToDate(productOutputInfo.get("endTime").toString(), DateUtil.MOUDLE_PATTERN);

        double productOutput = Double.valueOf(productOutputInfo.get("productOutput").toString());
        productOutput = NumberUtil.formatDouble(productOutput, NumberUtil.PATTERN_DOUBLE);

        //判断录入时间是否超过一个月
        int daysBetween = DateUtil.daysBetweenDates(endTime, startTime);
        if (daysBetween <= 31) {
        	result.put("message", "");
        	//新增
    		if (outputId == 0) {
    			outputId = SequenceUtils.getDBSequence();	
    			this.classMapper.insertProductOutput(outputId,ledgerId,productId,workshopId,teamId,startTime,endTime,productOutput);
    		}else {
    			//修改
    			this.classMapper.updateProductOutput(outputId, productId, workshopId, teamId, startTime, endTime, productOutput);
    		}

            //产量分时段录入 T_PRODUCT_DETAIL
            this.classMapper.deleteProductDetail(outputId);  //先删除
            Map<String, Object> reMap = this.classService.getProductSplitTime(teamId, startTime, endTime);
            List<Map<String, Date>> list = (List<Map<String, Date>>) reMap.get("list");
            Double minTotal = Double.valueOf(reMap.get("minTotal").toString());
            if(list != null && list.size() > 0 && minTotal > 0){  //再插入
                for(int i = 0; i < list.size(); i++){
                    Map<String, Date> map = list.get(i);
                    Date begin = map.get("begin");
                    Date end = map.get("end");
                    int min = DateUtil.getMinBetweenTime(begin, end);
                    Double outPut = DataUtil.doubleDivide(DataUtil.doubleMultiply(min, productOutput), minTotal, 2);
                    this.classMapper.insertProductDetail(productId,workshopId,teamId,outputId,begin,end,outPut);
                }
            }    
            
		}else {
			result.put("message", "生产时间段不能超过一个月！");
		}
        
        return result;
	}
	
	@Override
	public void deleteProductOutputById(Long outputId) {
        //删除 T_PRODUCT_OUTPUT
		this.classMapper.deleteProductOutputById(outputId);
		
		//删除T_PRODUCT_DETAIL
		this.classMapper.deleteProductDetail(outputId);

	}

	@Override
	public Map<String, Object> getProductOutputById(Long outputId) {
		Map<String, Object> output = this.classMapper.getProductOutputById(outputId);
		return output;
	}


}
