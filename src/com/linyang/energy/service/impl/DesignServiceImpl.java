package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.mapping.authmanager.DesignBeanMapper;
import com.linyang.energy.model.DesignBean;
import com.linyang.energy.model.PowerInputBean;
import com.linyang.energy.service.DesignService;
import com.linyang.util.CommonMethod;

@Service
public class DesignServiceImpl implements DesignService {
	@Autowired
	private DesignBeanMapper designMapper;

	@Override
	public Map<String,Object> getPlanListData() {
		List<DesignBean> designList = designMapper.getDesignList();
		List<PowerInputBean> powerList = designMapper.getPowerList();
		Map<String,Object> map = new HashMap<String,Object> ();
		map.put("designList", designList);
		map.put("powerList", powerList);
		return map;
	}

	@Override
	public boolean deletePlanData(Long planId) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(planId)){
			designMapper.deletePlan(planId);
			designMapper.deletePower(planId);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean addPlanData(DesignBean design,List<Map<String, Object>> powerList){
		boolean flag = false;
		if(design!=null) {
			designMapper.insertPlanData(design);
			if(CommonMethod.isCollectionNotEmpty(powerList)) {
				long planId = design.getPlanId();
				for(Map<String, Object> p : powerList) {
					PowerInputBean power = new PowerInputBean();
					power.setPlanId(planId);
					power.setTypeId(Long.valueOf(p.get("typeId").toString()));
					power.setUnitId(Long.valueOf(p.get("unitId").toString()));
					power.setTypeValue(Double.valueOf(p.get("typeValue").toString()));
					designMapper.insertPowerInfo(power);
				}
			}
			flag=true;
		}
		return flag;
	}

	@Override
	public DesignBean getPlanInfo(Long planId) {
		DesignBean plan = designMapper.getPlanInfo(planId);
		return plan == null?new DesignBean():plan;
	}

	@Override
	public List<PowerInputBean> getPowerDataById(Long planId) {
		List<PowerInputBean> list = designMapper.getPowerDataById(planId);
		return list==null?new ArrayList<PowerInputBean> ():list;
	}

	@Override
	public boolean updatePlanData(DesignBean design,List<Map<String, Object>> powerList) {
		boolean flag = false;
		if(design!=null) {
			designMapper.updatePlanInfo(design);
			if(CommonMethod.isCollectionNotEmpty(powerList)) {
				long planId = design.getPlanId();
				List<PowerInputBean> list = designMapper.getPowerDataById(planId);
				Map<Long, Object> powerMap = new HashMap<Long, Object> ();
				if(CommonMethod.isCollectionNotEmpty(list)) {
					for(PowerInputBean power : list) {
						powerMap.put(power.getTypeId(), power);
					}
				}
				for(Map<String, Object> p : powerList) {
					PowerInputBean power = new PowerInputBean();
					power.setPlanId(planId);
					long gradeId = Long.valueOf(p.get("gradeId").toString());
					long typeId = Long.valueOf(p.get("typeId").toString());
					powerMap.remove(typeId);
					power.setTypeId(typeId);
					power.setUnitId(Long.valueOf(p.get("unitId").toString()));
					power.setTypeValue(Double.valueOf(p.get("typeValue").toString()));
					if(gradeId==-100||gradeId!=typeId) {
						designMapper.insertPowerInfo(power);
					} else {
						designMapper.updatePowerInfo(power);
					}
				}
				Map<String, Object> param = new HashMap<String, Object> ();
				if(CommonMethod.isMapNotEmpty(powerMap)) {
					for(Long key : powerMap.keySet()) {
						param.put("planId", design.getPlanId());
						param.put("typeId", key);
						designMapper.deletePowerDetail(param);
					}
				}
			}
			flag=true;
		}
		return flag;
	}

	@Override
	public List<Map<Long, String>> getPowerData(Long productId) {
		List<Map<Long, String>> list = designMapper.getPowerData(productId);
		return (list==null?new ArrayList<Map<Long, String>> (): list);
	}

	@Override
	public List<Map<Long, String>> getUnitData() {
		List<Map<Long, String>> list = designMapper.getUnitData();
		return list==null?new ArrayList<Map<Long, String>> (): list;
	}

	@Override
	public List<Map<Long, String>> getAssembleData() {
		List<Map<Long, String>> list = designMapper.getAssembleData();
		return list==null?new ArrayList<Map<Long, String>> (): list;
	}

	@Override
	public List<Map<Long, String>> getProductInfo() {
		List<Map<Long, String>> list = designMapper.getProductData();
		return list==null?new ArrayList<Map<Long, String>> (): list;
	}

	@Override
	public Map<String,Object> searchDesign(Map<String, Object> map) {
		List<DesignBean> designList = designMapper.searchDesign(map);
		List<PowerInputBean> powerList = designMapper.getPowerList();
		Map<String,Object> dataMap = new HashMap<String,Object> ();
		dataMap.put("designList", designList);
		dataMap.put("powerList", powerList);
		return dataMap;
	}
	
}
