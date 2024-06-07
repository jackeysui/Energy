package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.authmanager.RateBeanMapper;
import com.linyang.energy.model.RateBean;
import com.linyang.energy.model.RateSectorBean;
import com.linyang.energy.model.RateSectorContentBean;
import com.linyang.energy.model.RoleBean;
import com.linyang.energy.service.RateService;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;

@Service
public class RateServiceImpl implements RateService {
	@Autowired
	private RateBeanMapper rateMapper;

	@Override
	public List<Map<String, Object>> getRatePageData(Page page,
			Map<String, Object> queryMa) {
		queryMa.put(Dialect.pageNameField, page);
		List<Map<String,Object>> list = rateMapper.getRatePageData(queryMa);
		return list == null ? new ArrayList<Map<String,Object>>() : list;
	}

	@Override
	public boolean deleteRateData(Long rateId) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(rateId)){
			rateMapper.deleteRateDetail(rateId);//根据Id删除费率配置信息 
			rateMapper.deleteRate(rateId);//根据Id费率信息
			rateMapper.deleteRateSectorContent(rateId);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean getRateLedgerLink(Long rateId) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(rateId)){
			//检查是否有流水线和排班关联
			if(rateMapper.getRateLedgerLink(rateId)==0) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public boolean checkRateName(String rateName, Long rateId) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(rateName)){
			//检查是否有费率名称重复
			if(rateMapper.checkRateName(rateName, rateId)==0) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public boolean addRateData(RateBean rate,List<Map<String,Object>> sectorList, List<Map<String,Object>> sectorContentList){
		boolean flag = false;
		if(rate!=null) {
			Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("rateName", rate.getRateName());
            queryMap.put("rateNumber", rate.getRateNumber());
            List<RateBean> rateList = rateMapper.getFilteredRate(queryMap);
            if(rateList.size()>0)
            {
            	flag = false;
            }
            else
            {
            	rateMapper.insertRateData(rate);
            	addSectorInfo(rate.getRateId(), sectorList, sectorContentList);
            	flag=true;
            }
		}
		return flag;
	}

	@Override
	public List<RateSectorBean> getSectorData(Long rateId) {
		List<RateSectorBean> list = rateMapper.getSectorData(rateId);
		return list==null?new ArrayList<RateSectorBean>():list;
	}

	@Override
	public RateBean getRateData(Long rateId) {
		RateBean rate = rateMapper.getRateData(rateId);
		return rate==null? new RateBean():rate;
	}

	@Override
	public boolean updateRateData(RateBean rate,List<Map<String,Object>> sectorList, 
			List<Map<String,Object>> sectorContentList) {
		boolean flag = false;
		if(rate!=null) {
			Long rateId = rate.getRateId();
			rateMapper.updateRateInfo(rate);
			rateMapper.deleteRateDetail(rateId);
			rateMapper.deleteRateSectorContent(rateId);
			addSectorInfo(rateId, sectorList, sectorContentList);
			flag=true;
		}
		return flag;
	}

    @Override
    public void updateOtherRateData(int rateType, Long rateId, String rateName, String rateRemark, Double rateValue){
        if(rateId > 0){
            rateMapper.updateOtherRate(rateType, rateId, rateName, rateRemark);

            rateMapper.deleteRateDetail(rateId);
            
            RateSectorBean rateSectorBean = new RateSectorBean();
            rateSectorBean.setSectorId(0L);
            rateSectorBean.setRateValue(rateValue);
            rateSectorBean.setRateId(rateId);
            rateSectorBean.setSectorName(rateName);
            rateMapper.insertRateDetail(rateSectorBean);
        }
        else {
        	Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("rateName", rateName);
            queryMap.put("rateNumber", 1);
            List<RateBean> rateList = rateMapper.getFilteredRate(queryMap);
            if(rateList.size()==0)
            {
            	 rateId = SequenceUtils.getDBSequence();
                 rateMapper.insertOtherRate(rateType, rateId, rateName, rateRemark);
                 RateSectorBean rateSectorBean = new RateSectorBean();
                 rateSectorBean.setSectorId(0L);
                 rateSectorBean.setRateValue(rateValue);
                 rateSectorBean.setRateId(rateId);
                 rateSectorBean.setSectorName(rateName);
                 rateMapper.insertRateDetail(rateSectorBean);
            }
        }
       
    }

    @Override
    public List<Map<String,Object>> getWaterGasHotByRateId(Long rateId){
        return rateMapper.getWaterGasHotByRateId(rateId);
    }

	private void addSectorInfo(Long rateId, List<Map<String, Object>> sectorList,
			List<Map<String, Object>> sectorContentList) {
		if(CommonMethod.isCollectionNotEmpty(sectorList)) {
			for(Map<String, Object> bean : sectorList) {
				RateSectorBean sector = new RateSectorBean();
				sector.setSectorId((Integer)bean.get("sectorId"));
				sector.setSectorName(bean.get("sectorName").toString());
				sector.setRateValue(Double.valueOf(bean.get("sectorPrice").toString()));
				sector.setRateId(rateId);
				rateMapper.insertRateDetail(sector);
			}
		}
		if(CommonMethod.isCollectionNotEmpty(sectorContentList)) {
			for(Map<String, Object> bean : sectorContentList) {
				RateSectorContentBean sectorContent = new RateSectorContentBean();
				sectorContent.setContentId(SequenceUtils.getDBSequence());
				sectorContent.setRateId(rateId);
				sectorContent.setSectorId((Integer)bean.get("sectorId"));
				sectorContent.setBeginTime(bean.get("startTime").toString());
				sectorContent.setEndTime(bean.get("endTime").toString());
				rateMapper.insertSectorContent(sectorContent);
			}
		}
	}

	@Override
	public List<RateSectorContentBean> getSectorContentData(Long rateId) {
		List<RateSectorContentBean> list = rateMapper.getSectorContentData(rateId);
		return list==null?new ArrayList<RateSectorContentBean>():list;
	}

}
