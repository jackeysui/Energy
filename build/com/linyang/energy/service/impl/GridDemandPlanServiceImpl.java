package com.linyang.energy.service.impl;

import com.leegern.util.DateUtil;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.demand.GridDemandPlanMapper;
import com.linyang.energy.mapping.demand.LedgerDetailsMapper;
import com.linyang.energy.model.EntPeakAbility;
import com.linyang.energy.model.GridDemandPlanBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.GridDemandPlanService;
import com.linyang.util.CommonMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Administrator on 16-9-7.
 */
@Service
public class GridDemandPlanServiceImpl implements GridDemandPlanService{

    @Autowired
    private GridDemandPlanMapper gridDemandPlanMapper;
    
    @Autowired
    private LedgerDetailsMapper ledgerDetailsMapper;

	@Override
	public List<GridDemandPlanBean> getGridDemandPlanListPageData(Map<String, Object> paraMap) {
		 List<GridDemandPlanBean> list=gridDemandPlanMapper.getGridDemandPlanListPageData(paraMap);
		 
		 return list == null?new ArrayList<GridDemandPlanBean>():list;
	}

	@Override
	public GridDemandPlanBean getGridDemandPlan(Long planId) {
		return gridDemandPlanMapper.getGridDemandPlan(planId);
	}

	@Override
	public int insertGridDemand(GridDemandPlanBean gridDemandPlanBean) {
		return gridDemandPlanMapper.insertGridDemand(gridDemandPlanBean);
	}

	@Override
	public Integer getDemandNumByName(Long planId, String planName) {
		return gridDemandPlanMapper.getDemandNumByName(planId,planName);
	}

	@Override
	public int updateGridDemand(GridDemandPlanBean gridDemandPlanBean) {
		return gridDemandPlanMapper.updateGridDemand(gridDemandPlanBean);
	}

	@Override
	public int deleteGridDemand(Long planId) {
		GridDemandPlanBean gridDemandPlanBean=gridDemandPlanMapper.getGridDemandPlan(planId);
		if(gridDemandPlanBean.getPlanStatus()==2) {
			return 2; //已生效不能删除
		}
		
		Date current=DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
		try {
			Date start=DateUtil.convertStrToDate(gridDemandPlanBean.getStartDate(),DateUtil.DEFAULT_SHORT_PATTERN);
			if(current.getTime()>start.getTime()) {
				return 3; //当前日期大于开始日期不能删除
			}
			
			gridDemandPlanMapper.deletePlanLoadConfig(planId, 0l);
			gridDemandPlanMapper.deletePlanLedgerConfig(planId, 0l);
			gridDemandPlanMapper.deleteGridDemand(planId);
			return 1;  //删除成功
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
			return 0;
		}
	}

	@Override
	public GridDemandPlanBean getPlanLedgerConfig(Long planId, Long ledgerId) {
		GridDemandPlanBean gridDemandPlanBean=gridDemandPlanMapper.getPlanLedgerConfig(planId,ledgerId);
		if(gridDemandPlanBean!=null) {
			gridDemandPlanBean.getPlanLoads().addAll(gridDemandPlanMapper.getPlanLedgerLoad(planId, ledgerId));
			gridDemandPlanBean.getPlanLoads().addAll(gridDemandPlanMapper.getPlanMeterLoad(planId, ledgerId));
		}
		return gridDemandPlanBean;
	}

	@Override
	public int mergePlanLedgerConfig(GridDemandPlanBean gridDemandPlanBean,int type) {
		int result=0;
		if(type==0) {	//新增
			result= gridDemandPlanMapper.insertPlanLedgerConfig(gridDemandPlanBean);
		}
		else {   //修改
			gridDemandPlanMapper.deletePlanLoadConfig(gridDemandPlanBean.getPlanId(), gridDemandPlanBean.getLedgerId());
			result=  gridDemandPlanMapper.updatePlanLedgerConfig(gridDemandPlanBean);
		}
		
		for(Map<String, Object> map:gridDemandPlanBean.getPlanLoads()) {
			long loadId=Long.valueOf(map.get("loadId").toString());
			int loadtype=Integer.valueOf(map.get("type").toString());
			gridDemandPlanMapper.insertPlanLoadConfig(gridDemandPlanBean.getPlanId(), gridDemandPlanBean.getLedgerId(), loadId, loadtype);
		}
		
		return result;
	}

	@Override
	public void deletePlanLedgerConfig(Long planId, Long ledgerId) {
		gridDemandPlanMapper.deletePlanLoadConfig(planId, ledgerId);
		gridDemandPlanMapper.deletePlanLedgerConfig(planId, ledgerId);
	}

	@Override
	public int updatePlanStatus(Long planId, int planStatus) {
		return gridDemandPlanMapper.updatePlanStatus(planId, planStatus);
	}

	@Override
	public double estimagePitchPeak(GridDemandPlanBean gridDemandPlanBean) {
		double total=0d;
		
		Date today=DateUtil.getCurrentDate(DateUtil.DEFAULT_SHORT_PATTERN);
		try {
			String todayDateStr=DateUtil.convertDateToStr(today, DateUtil.DEFAULT_SHORT_PATTERN);
			String beginDate=com.linyang.energy.utils.DateUtil.addDay(todayDateStr,-7);			
			for(int i = 0 ; i < 7 ; i++ ){
			   for(Map<String, Object> map:gridDemandPlanBean.getPlanLoads()) {
				   Double beforeSearch = null;
				   String tempdate=com.linyang.energy.utils.DateUtil.addDay(beginDate,i);
				   System.out.println(tempdate+" "+gridDemandPlanBean.getStartTime());
				   System.out.println(tempdate+" "+gridDemandPlanBean.getStartTime());
				   if(map.get("type").toString().equals("1")) { //能管对象
					   beforeSearch = ledgerDetailsMapper.queryOneLedgerFaq(Long.valueOf(map.get("loadId").toString()),
							   tempdate+" "+gridDemandPlanBean.getStartTime(),
							   tempdate+" "+gridDemandPlanBean.getEndTime());
						if(beforeSearch != null)
							total += beforeSearch;
				   }
				   else {
					   beforeSearch = ledgerDetailsMapper.queryOneMeterFaq(Long.valueOf(map.get("loadId").toString()),
							   tempdate+" "+gridDemandPlanBean.getStartTime(),
							   tempdate+" "+gridDemandPlanBean.getEndTime());
						if(beforeSearch != null)
							total += beforeSearch;
				   }
			   }
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		double data=total/7;
		BigDecimal   b   =   new   BigDecimal(data);   
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public int getPlanLedgerCount(Long planId) {
		return gridDemandPlanMapper.getPlanLedgerCount(planId);
	}

	@Override
	public int saveLedgerPeakloadConfig(EntPeakAbility entPeakAbility) {
		return gridDemandPlanMapper.saveLedgerPeakloadConfig(entPeakAbility);
	}

	@Override
	public List<EntPeakAbility> getLedgerPeakConfigPageData(Page page, Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		paraMap.put(Dialect.pageNameField, page);
		return gridDemandPlanMapper.getLedgerPeakConfigPageData(paraMap);
	}

	@Override
	public EntPeakAbility getLedgerPeakConfig(Long ledgerId) {
		// TODO Auto-generated method stub
		return gridDemandPlanMapper.getLedgerPeakConfig(ledgerId);
	}

	@Override
	public int deleteLedgerPeakConfig(Long ledgerId) {
		// TODO Auto-generated method stub
		return gridDemandPlanMapper.deleteLedgerPeakConfig( ledgerId );
	}
	
	@Override
	public List<LedgerBean> getLedgerList(Long ledgerId) {
		return gridDemandPlanMapper.getLedgerList( ledgerId );
	}
}
