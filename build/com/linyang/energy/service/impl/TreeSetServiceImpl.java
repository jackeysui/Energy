package com.linyang.energy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.mapping.authmanager.MeterBeanMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.LedgerRelationBean;
import com.linyang.energy.model.LineLossTreeBean;
import com.linyang.energy.service.TreeSetService;
import com.linyang.energy.utils.SequenceUtils;

@Service
public class TreeSetServiceImpl implements TreeSetService{

	@Autowired
	private MeterBeanMapper meterBeanMapper;
	
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	
	@Override
	public List<LineLossTreeBean> getUnSetDCP(long ledgerId) {
		return meterBeanMapper.getUnSetDCP(ledgerId);
	}

	@Override
	public List<LineLossTreeBean> getSetDCP(long ledgerId, long type,String meterName) {
		if(type == 1){//EMO配置
			LedgerBean bean = ledgerManagerMapper.selectByLedgerId(ledgerId);
			if(bean != null && bean.getAnalyType() != 102){//非企业类型,要找出上级企业的ID
				Long parentId = ledgerManagerMapper.getCompanyId(ledgerId);
				if(parentId != null){
					ledgerId = parentId;
				}
			};
		}
		return meterBeanMapper.getSetDCP(ledgerId,meterName);
	}

	@Override
	public List<Map<String, Object>> getEMOTree(long ledgerId,String meterName) {
		return meterBeanMapper.getEMOTree(ledgerId,meterName);
	}

	@Override
	public List<LedgerRelationBean> getEMOModel1(long ledgerId) {
		return meterBeanMapper.getEMOModel1(ledgerId);
	}

	@Override
	public List<Map<String, Object>> getEMOModel2(long ledgerId) {
		return meterBeanMapper.getEMOModel2(ledgerId);
	}

	@Override
	public boolean save(Long ledgerId, List<Map<String, Object>> ledgerRelationBeans,
			List<Map<String, Object>> ledgerShowBeans) {
		meterBeanMapper.deleteLedgerRelation(ledgerId);
		meterBeanMapper.deleteLedgerShow(ledgerId);
		
		for (Map<String, Object> map : ledgerRelationBeans) {
			map.put("recId", SequenceUtils.getDBSequence());
			meterBeanMapper.addLedgerRelation(map);
		}
		
		for (Map<String, Object> map : ledgerShowBeans) {
			meterBeanMapper.addLedgerShow(map);
		}
		return true;
	}

	@Override
	public List<Map<String, Object>> getCompanyList(Long ledgerId, Integer analyType) {
		List<Map<String, Object>> list = ledgerManagerMapper.getCompanyList(ledgerId, analyType);
		if(list != null && list.size() > 0){
			return list;
		}
		return ledgerManagerMapper.getParentCompany(ledgerId, analyType);
	}

    @Override
    public List<Map<String, Object>> getNoPositionLedgerList(Long ledgerId) {
        List<Map<String, Object>> list = ledgerManagerMapper.getNoPositionLedgerList(ledgerId);
        return list;
    }

    @Override
    public List<Map<String, Object>> getSearchModelDataList(Long ledgerId, Integer searchModel){
        List<Map<String, Object>> list = ledgerManagerMapper.getSearchModelDataList(ledgerId, searchModel);
        return list;
    }

	@Override
	public List<Map<String, Object>> getEMOList(Long ledgerId) {
		List<Map<String, Object>> list = ledgerManagerMapper.getEMOList(ledgerId);
		if(list != null && list.size() > 0){
			return list;
		}
		return ledgerManagerMapper.getParentEMO(ledgerId);
	}
	
	@Override
	public List<LineLossTreeBean> getUnSetDCPByName(String meterName,Long ledgerId) {
		return meterBeanMapper.getUnSetDCPByName( meterName ,ledgerId);
	}
}
