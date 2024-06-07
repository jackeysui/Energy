package com.linyang.energy.service.impl;

import java.util.*;

import com.leegern.util.CollectionUtil;
import com.leegern.util.NumberUtil;
import com.linyang.energy.mapping.IndexMapper;
import com.linyang.energy.mapping.energydataquery.DataQueryMapper;
import com.linyang.energy.mapping.phone.PhoneMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.*;
import com.linyang.energy.service.ClassService;
import com.linyang.energy.utils.DataUtil;import com.linyang.energy.utils.DateUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;

@Service
public class LedgerManagerServiceImpl implements LedgerManagerService {
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
    @Autowired
    private MeterManagerMapper meterManagerMapper;
    @Autowired
    private IndexMapper indexMapper;
    @Autowired
    private DataQueryMapper dataQueryMapper;
    @Autowired
    private PhoneMapper phoneMapper;
    @Autowired
    private ClassService classService;

	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#deleteByLedgerId(java.lang.Long)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteByLedgerId(Long ledgerId) {
		int flag = 0;
		LedgerBean bean = this.selectByLedgerId(ledgerId);
		if (null == bean || bean.getLedgerId() == 0) {
			flag = 1;
		}
		else {
			// 根据分户Id删除分户信息
            List<LedgerBean> pLedgers = ledgerManagerMapper.getAllParentLedgersByLedgerId(ledgerId);
			flag = ledgerManagerMapper.deleteByLedgerId(ledgerId);
			if (flag > 0) {
                // 更新企业个数
                if(bean.getAnalyType() == 102){
                    updateLedgerCompanyCount(pLedgers);
                }
				// 更新其他节点左边距的值
				LedgerBean ledger = new LedgerBean();
				ledger.setLedgerLft(bean.getLedgerRgt());
				ledgerManagerMapper.updateLftRgtForDel(ledger);
				
				// 更新其他节点右边距的值
				ledger.setLedgerLft(0);
				ledger.setLedgerRgt(bean.getLedgerRgt());
				ledgerManagerMapper.updateLftRgtForDel(ledger);
			}
		}
		return flag;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#getLedgerList(com.linyang.energy.model.LedgerBean)
	 */
	@Override
	public List<LedgerBean> getLedgerList(Page page, LedgerBean ledger, String parentName) {
        List<LedgerBean> ledgerList = ledgerManagerMapper.getLedgerPageList(page, ledger, parentName);
        for(int i = 0; i < ledgerList.size(); i++){
            String company = "-";
            LedgerBean ledgerBean = ledgerList.get(i);
            Integer analyType = ledgerBean.getAnalyType();
            if(analyType!= null && analyType == 102){
                company = ledgerBean.getLedgerName();
                ledgerBean.setCompany(company);
                continue;
            }
            Long ledgerId = ledgerBean.getLedgerId();
            Map<String, Object> map = this.ledgerManagerMapper.getParentLedgerCompany(ledgerId);
            while (map != null && map.size() > 0){
                if(map.keySet().contains("ANALY_TYPE")){
                    int type = Integer.valueOf(map.get("ANALY_TYPE").toString());
                    if(type == 102){
                        company = map.get("LEDGER_NAME").toString();
                        break;
                    }
                }
                map = this.ledgerManagerMapper.getParentLedgerCompany(Long.valueOf(map.get("LEDGER_ID").toString()));
            }
            ledgerBean.setCompany(company);
        }
		return ledgerList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#queryRateInfo(java.util.Date)
	 */
	@Override
	public List<RateBean> queryRateInfo(Date currentDate) {
		
		return ledgerManagerMapper.queryRateInfo(currentDate);
	}

    @Override
    public List<Map<String, Object>> queryOtherRateInfo(int rateType){
        return ledgerManagerMapper.queryOtherRateInfo(rateType);
    }
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#queryAnalyTypes()
	 */
	@Override
	public List<CateBean> queryAnalyTypes() {
		
		return ledgerManagerMapper.queryAnalyTypes();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#insertBySelective(com.linyang.energy.model.LedgerBean)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertBySelective(LedgerBean ledger) {
		
		return this.addLedgerInfo(ledger);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#selectByLedgerId(java.lang.Long)
	 */
	@Override
	public LedgerBean selectByLedgerId(Long ledgerId) {
		
	return ledgerManagerMapper.selectByLedgerId(ledgerId);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#findOneLedger(com.linyang.energy.model.LedgerBean)
	 */
	@Override
	public LedgerBean findOneLedger(LedgerBean ledger) {
		List<LedgerBean> list = ledgerManagerMapper.getLedgerList(ledger); 
		if (null != list && ! list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#updateBySelective(com.linyang.energy.model.LedgerBean)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateBySelective(LedgerBean ledger) {
		int result = 0;
		LedgerBean origBean = null;if(null == ledger) return result; origBean = this.selectByLedgerId(ledger.getLedgerId());
		if (null != origBean) {
			// 判断原始阀值的设置值是否与目前的相同
			if (! ledger.getThresholdValue().equals(origBean.getThresholdValue())) {
				if (ledger.getThresholdValue().length()>0 || origBean.getThresholdValue().length()>0 ) {
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("ledgerId",      ledger.getLedgerId());       // 分户Id
					param.put("thresholdId",   ledger.getThresholdId());    // 阀值Id
					param.put("thresholdValue",ledger.getThresholdValue()); // 阀值设置值
					// 合并分户阀值关联关系
					this.modifyLedgerThreshold(param, false);
				}
			}
		}
		
		if (null != origBean && !origBean.getParentLedgerId().equals(ledger.getParentLedgerId())) {// 父分户变动，更新左右边界
			LedgerBean currPLedger = this.selectByLedgerId(ledger.getParentLedgerId());
			int span = origBean.getLedgerRgt() - origBean.getLedgerLft() + 1; //移动跨度

			//移动的子分户列表
			List<LedgerBean> childLedgers = ledgerManagerMapper.getLedgerListByParent(origBean.getLedgerLft(), origBean
					.getLedgerRgt());

			int offset = 0;
			int depth = 0;
			if (ledger.getParentLedgerId() == null || ledger.getParentLedgerId() == 0) { //移动到根节点
				ledgerManagerMapper.updateLedgerLft(0 - span, origBean.getLedgerLft());
				ledgerManagerMapper.updateLedgerRgt(0 - span, origBean.getLedgerLft());

				// 获取最大边距值
				int maxBorder = ledgerManagerMapper.getMaxBorder();
				offset = maxBorder - origBean.getLedgerLft() + 1;
				depth = 1 - origBean.getDepth();
				
				for (LedgerBean l : childLedgers) {
					l.setLedgerLft(l.getLedgerLft() + offset);
					l.setLedgerRgt(l.getLedgerRgt() + offset);
					l.setDepth(l.getDepth() + depth);
					ledgerManagerMapper.updateLftRgt(l);
				}
			} else {
				depth = currPLedger.getDepth() - origBean.getDepth() + 1; //移动深度
				int preRL = 0;
				if (currPLedger.getLedgerLft().equals(currPLedger.getLedgerRgt() - 1)) {// 表示在叶子节点下增加节点
					offset = currPLedger.getLedgerLft() - origBean.getLedgerLft() + 1;
					preRL = currPLedger.getLedgerLft();
				} else {
					// 查询此节点的前一个节点
					LedgerBean lb = ledgerManagerMapper.findPreviousLedger(currPLedger.getLedgerId());
					offset = lb.getLedgerRgt() - origBean.getLedgerLft() + 1;
					preRL = lb.getLedgerRgt();
				}
				
				result = ledgerManagerMapper.updateBySelective(ledger);
				
				//移出空档
				ledgerManagerMapper.updateLedgerLft(span, preRL);
				ledgerManagerMapper.updateLedgerRgt(span, preRL);

				//更新所有子节点
				for (LedgerBean l : childLedgers) {
					l.setLedgerLft(l.getLedgerLft() + offset);
					l.setLedgerRgt(l.getLedgerRgt() + offset);
					l.setDepth(l.getDepth() + depth);
					ledgerManagerMapper.updateLftRgt(l);
				}
				
				if (offset > 0) { //从小移到大
					ledgerManagerMapper.updateLedgerLft(0 - span, origBean.getLedgerLft());
					ledgerManagerMapper.updateLedgerRgt(0 - span, origBean.getLedgerLft());
				} else { //从大移到小
					ledgerManagerMapper.updateLedgerLft(0 - span, origBean.getLedgerRgt());
					ledgerManagerMapper.updateLedgerRgt(0 - span, origBean.getLedgerRgt());
				}
			}
		}
		// 更新新、老分户以及祖先分户采集个数
		if(origBean != null){
			int ledgerNum = origBean.getCollmeterNumber();
			List<LedgerBean> ledgerOldList = ledgerManagerMapper.queryRecursiveLedgerById(origBean.getParentLedgerId());
			for (LedgerBean ledgerBean : ledgerOldList) {
				ledgerBean.setCollmeterNumber(ledgerBean.getCollmeterNumber() - ledgerNum);
				ledgerManagerMapper.updateBySelective(ledgerBean);
			}
			List<LedgerBean> ledgerNewList = ledgerManagerMapper.queryRecursiveLedgerById(ledger.getParentLedgerId());
			for (LedgerBean ledgerBean : ledgerNewList) {
				ledgerBean.setCollmeterNumber(ledgerBean.getCollmeterNumber() + ledgerNum);
				ledgerManagerMapper.updateBySelective(ledgerBean);
			}
		}
		// 更新分户本身信息
		result = ledgerManagerMapper.updateBySelective(ledger);
        ledgerManagerMapper.updateRelationLedgerName(ledger);

        // 更新企业个数
        if(result > 0){
            if(origBean != null){
                if((ledger.getAnalyType() == 102 && origBean.getAnalyType() != 102)||(ledger.getAnalyType() != 102 && origBean.getAnalyType() == 102)){
                    List<LedgerBean> pLedgers = ledgerManagerMapper.getAllParentLedgersByLedgerId(ledger.getLedgerId());
                    updateLedgerCompanyCount(pLedgers);
                }
                if(!origBean.getParentLedgerId().equals(ledger.getParentLedgerId())){
                    List<LedgerBean> pLedgers = ledgerManagerMapper.getAllParentLedgersByLedgerId(ledger.getLedgerId());
                    updateLedgerCompanyCount(pLedgers);
                    
                    if(origBean.getParentLedgerId() != null){
                        List<LedgerBean> opLedgers = ledgerManagerMapper.getAllParentLedgersByLedgerId(origBean.getParentLedgerId());
                        LedgerBean origParBean = this.selectByLedgerId(origBean.getParentLedgerId());
                        if(origParBean != null){
                            opLedgers.add(origParBean);
                        }
                        updateLedgerCompanyCount(opLedgers);
                    }
                }
            }
        }
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#getLedgerRelatedByLedgerId(java.lang.Long)
	 */
	@Override
	public int getLedgerRelatedByLedgerId(Long ledgerId) {
		
		return ledgerManagerMapper.getLedgerRelatedByLedgerId(ledgerId);
	}
	
	/**
	 * 添加分户信息
	 * @param bean
	 * @return
	 */
	private int addLedgerInfo(LedgerBean bean){
		// 表示添加的是根节点
		if (bean.getParentLedgerId() == null || bean.getParentLedgerId() == 0) {
			// 获取最大边距值
			int maxBorder = ledgerManagerMapper.getMaxBorder();
			// 设置新节点的左右边距
			bean.setLedgerLft(maxBorder + 1);
			bean.setLedgerRgt(maxBorder + 2);
			// 设置深度为1
			bean.setDepth(1);
			// 设置父节点为0
			bean.setParentLedgerId(0L);
		}
		// 表示添加子节点
		else {
			// 查询父节点信息
			LedgerBean pLedger = this.selectByLedgerId(bean.getParentLedgerId());
			//如果父分户为VIP，则该分户也为VIP-暂时屏蔽-guosen-2014-12-16
//			if(pLedger.getLedgerType() == LedgerBean.LEDGER_TYPE_VIP){
//				bean.setLedgerType(LedgerBean.LEDGER_TYPE_VIP);
//			}
			
			// 更新参数
			LedgerBean ledger = new LedgerBean();
			int myLft = 0;
			int myRft = 0;
			int lft = 1;
			int rft = 2;
			// 表示在叶子节点下增加节点
			if (pLedger.getLedgerLft().equals(pLedger.getLedgerRgt() - 1)) {
				lft = pLedger.getLedgerLft();
				rft = pLedger.getLedgerLft();
				myLft = pLedger.getLedgerLft() + 1;  // 父节点的左边距  + 1
				myRft = pLedger.getLedgerLft() + 2;  // 父节点的左边距  + 2
			}
			// 表示添加的是子节点 
			else {
				// 查询此节点的前一个节点
				LedgerBean lb = ledgerManagerMapper.findPreviousLedger(pLedger.getLedgerId());
				if (lb != null) {
					lft = lb.getLedgerRgt();
					rft = lb.getLedgerRgt();
				}
				myLft = lft + 1;  // 兄弟节点的右边距  + 1
				myRft = rft + 2;  // 兄弟节点的右边距  + 2
			}
			ledger.setLedgerLft(lft);
			// 更新其他节点左边距的值
			ledgerManagerMapper.updateLftRgtForAdd(ledger);
			
			// 更新其他节点右边距的值
			ledger.setLedgerLft(0);
			ledger.setLedgerRgt(rft);
			ledgerManagerMapper.updateLftRgtForAdd(ledger);
			
			// 设置新节点的左右边距
			bean.setLedgerLft(myLft);
			bean.setLedgerRgt(myRft);
			
			// 设置深度
			bean.setDepth(pLedger.getDepth() + 1);
		}
		// 生成主键
		if (bean.getLedgerId() == null) {
			bean.setLedgerId(SequenceUtils.getDBSequence());
		}		
		// 添加新节点
		int flag = ledgerManagerMapper.insertBySelective(bean);
		if (flag > 0) {
            if(bean.getAnalyType() == 102){
                List<LedgerBean> pLedgers = ledgerManagerMapper.getAllParentLedgersByLedgerId(bean.getLedgerId());
                updateLedgerCompanyCount(pLedgers);
            }
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("ledgerId",      bean.getLedgerId());       // 分户Id
			param.put("thresholdId",   bean.getThresholdId());    // 阀值Id
			param.put("thresholdValue",bean.getThresholdValue()); // 阀值设置值
			
			// 合并分户阀值关联关系
			this.modifyLedgerThreshold(param, true);
		}
		return flag; 
	}
	
	/**
	 * 合并分户阀值关联关系
	 * @param param   参数
	 * @param addFlag 是否是添加标识
	 * @return
	 */
	private int modifyLedgerThreshold(Map<String, Object> param, boolean addFlag) {
		if (! addFlag) {
			// 删除分户阀值关联关系 
			ledgerManagerMapper.deleteLedgerThreshold((Long)param.get("ledgerId"));
		}
		// 添加分户阀值关联关系 
		return ledgerManagerMapper.addLedgerThreshold(param);
	}

	@Override
	public List<LedgerAllMeterBean> getLedgerAllMeter() {
		return ledgerManagerMapper.getLedgerAllMeter();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertLedgerMeter(Map<Long, Map<Integer, List<LedgerMeterBean>>> meters, int type) {
		if (meters == null || meters.size() == 0)
			return;

		if (type == 1){
            ledgerManagerMapper.delLedgerMeter(null);// 先删除后插入
        }
		else {
            ledgerManagerMapper.delDeviceMeter();
        }

		Map<String, Long> param = new HashMap<String, Long>();
		for (Long lId : meters.keySet()) {
			param.put("ledgerId", lId);
			Map<Integer, List<LedgerMeterBean>> me = meters.get(lId);
			for (List<LedgerMeterBean> mi : me.values()) {
				for (LedgerMeterBean m : mi) {
					param.put("meterId", m.getMeterId());
					if (type == 1){
                        long addAttr = getAddAttr(lId, m.getMeterId());
                        param.put("addAttr", addAttr);
                        ledgerManagerMapper.insertLedgerMeter(param);
                    }
					else {
                        ledgerManagerMapper.insertDeviceMeter(param);
                    }
				}
			}
		}
	}

	@Override
	public List<LedgerAllMeterBean> getDeviceAllMeter() {
		return ledgerManagerMapper.getDeviceAllMeter();
	}

	@Override
	public List<LedgerDeviceAllMeterBean> getLedgerDeviceMeter() {
		return ledgerManagerMapper.getLedgerDeviceMeter();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertLedgerDeviceMeter(Map<Long, Map<Long, Map<Integer, List<LedgerMeterBean>>>> result) {
		if (result == null || result.size() == 0)
			return;

		ledgerManagerMapper.delLedgerDeviceMeter();// 先删除后插入

		Map<String, Long> param = new HashMap<String, Long>();
		for (long ledgerId : result.keySet()) {
			param.put("ledgerId", ledgerId);
			for (Long lId : result.get(ledgerId).keySet()) {
				param.put("typeId", lId);
				Map<Integer, List<LedgerMeterBean>> me = result.get(ledgerId).get(lId);
				for (List<LedgerMeterBean> mi : me.values()) {
					for (LedgerMeterBean m : mi) {
						param.put("meterId", m.getMeterId());
                        long addAttr = getAddAttr(ledgerId, m.getMeterId());
                        param.put("addAttr", addAttr);
                        ledgerManagerMapper.insertLedgerDeviceMeter(param);
					}
				}
			}
		}
	}

    /** 按层级负负得正的算法得到累加属性 */
    private long getAddAttr(long ledgerId, long meterId){
        MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(meterId);
        Long attributeId = meterBean.getAttributeId();
        long result = 0;
        if(attributeId == 1){
            result = 1;
        }
        else if(attributeId == 2){
            result = -1;
        }
        else {
             return result;
        }
        Map<String, Object> map = ledgerManagerMapper.getParentAddAttr(meterId, 2);
        while (map != null && map.size() > 0 && Long.valueOf(map.get("ID").toString()) != ledgerId){
            int addAttr = Integer.valueOf(map.get("ADD_ATTR").toString());
            if(addAttr == 2){
                result = 0 - result;
            }
            else if(addAttr == 3){
                result = 0;
                break;
            }
            map = ledgerManagerMapper.getParentAddAttr(Long.valueOf(map.get("ID").toString()), 1);
        }
        return result;
    }

	@Override
	public boolean checkLedgerName(Map<String, Object> map) {
		boolean flag = false;
		if (CommonMethod.isMapNotEmpty(map)) {
			if(ledgerManagerMapper.checkLedgerName(map)==0) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	@Override
	public List<LedgerTreeBean> getSubLedgerTree(long parentLedgerId) {
		return ledgerManagerMapper.getSubLedgerTree(parentLedgerId);
	}

	@Override
	public List<LedgerBean> getLedgerListByParam(Map<String, Object> param) {
		if(param.get("page") != null)
			return ledgerManagerMapper.getLedgerInfoPageList(param);
		else 
			return ledgerManagerMapper.getLedgerInfoList(param);
	}

	@Override
	public List<LedgerBean> getUserLedger(long accountId) {
		return ledgerManagerMapper.getUserLedger(accountId);
	}
	
	//add by qwt 2015-6-4
	//add for 添加获取所有分户函数
	@Override
	public List<LedgerBean> getAllLedger() {
		return ledgerManagerMapper.getAllLedger();
	}
	//end

	@Override
	public boolean checkLineloss(Map<String, Object> map) {
		boolean isOK = true;
		Long ledgerId = (Long)map.get("ledgerId");
		Integer count = 0;
		if(ledgerId == 0){
			count = ledgerManagerMapper.checkLinelossByParentId(map);
		}else {
			count = ledgerManagerMapper.checkLineloss(map);
		}
		if(count != null && count > 0){
			isOK = false;
		}
		return isOK;
	}

	@Override
	public LedgerBean getLedgerDataById(Long ledgerId) {
		return ledgerManagerMapper.selectByLedgerId(ledgerId);
	}

    @Override
    public void updateLedgerMeter(){
        Map<Long, List<LedgerRelationBean>> allRelation = new HashMap<Long, List<LedgerRelationBean>>(); //key为ledgerId
        List<Long> ledgerIds = this.ledgerManagerMapper.getAllCompLedgerIds();
        if(ledgerIds != null && ledgerIds.size() > 0){
            for(int i = 0; i < ledgerIds.size(); i++){
                Long ledgerId = ledgerIds.get(i);
                List<LedgerRelationBean> temp = this.ledgerManagerMapper.getLedgerMeterRelation(ledgerId, 2);
                allRelation.put(ledgerId, temp);
            }

            for(int i = 0; i < ledgerIds.size(); i++){
                Long childId = ledgerIds.get(i);
                List<LedgerRelationBean> parentRelations = this.ledgerManagerMapper.getParentRelation(childId);
                if(parentRelations != null && parentRelations.size() > 0){
                    LedgerRelationBean parentRel = parentRelations.get(0);
                    Long parentId = parentRel.getLedgerId();
                    int attr = parentRel.getAttr();
                    int pct = parentRel.getPct();
                    List<LedgerRelationBean> append = allRelation.get(childId);
                    for(int j = 0; j < append.size(); j++){
                        LedgerRelationBean append_one = append.get(j);
                        LedgerRelationBean add_one = new LedgerRelationBean();
                        add_one.setType(2);
                        add_one.setId(append_one.getId());
                        int a = attr * append_one.getAttr();
                        add_one.setAttr(a);
                        int p = append_one.getPct() * pct/100;
                        add_one.setPct(p);
                        if(allRelation.keySet().contains(parentId)){  //添加到父节点中
                            allRelation.get(parentId).add(add_one);
                        }
                    }

                }
            }
        }
        // 先删除后插入
        ledgerManagerMapper.delLedgerMeter(null);
        if(!allRelation.isEmpty()){
            Map<String, Object> param = new HashMap<String, Object>();
            for (Long ledgerId : allRelation.keySet()) {
                param.put("ledgerId", ledgerId);
                List<LedgerRelationBean> list = allRelation.get(ledgerId);
                for(int i = 0; i < list.size(); i++){
                    LedgerRelationBean relation = list.get(i);
                    param.put("meterId", relation.getId());
                    param.put("addAttr", relation.getAttr());
                    param.put("percent", relation.getPct());
                    int num = ledgerManagerMapper.getOneRelationcount(ledgerId, relation.getId());
                    if(num == 0){
                        ledgerManagerMapper.insertLedgerMeterNew(param);
                    }
                    else{
                        ledgerManagerMapper.updateLedgerMeterNew(param);
                    }
                }
            }
        }

        //更新计算模型时，刷新T_WORKSHOP_METER
        this.classService.refreshWorkshopMeterAll(-1L);
    }

    @Override
    public void updateOneLedger(Long ledgerId){
        updateOneLedgerModel(ledgerId);
        Long parentId = null;
        List<LedgerRelationBean> parentRelations = this.ledgerManagerMapper.getParentRelation(ledgerId);
        if(parentRelations != null && parentRelations.size() > 0){
            parentId = parentRelations.get(0).getLedgerId();
        }
        while(parentId != null && parentId > 0){
            updateOneLedgerModel(parentId);

            List<LedgerRelationBean> temp = this.ledgerManagerMapper.getParentRelation(parentId);
            if(temp != null && temp.size() > 0){
                parentId = temp.get(0).getLedgerId();
            }
            else{
                parentId = null;
            }
        }

        //更新计算模型时，刷新T_WORKSHOP_METER
        this.classService.refreshWorkshopMeterAll(ledgerId);
    }

    @Override
    public void copyOldModel(){
        List<LedgerBean> ledgerBeans = this.ledgerManagerMapper.getAllLedger();
        if(ledgerBeans != null && ledgerBeans.size() > 0){
            this.ledgerManagerMapper.deleteLedgerRelation(); //先删后插
            for(int i = 0; i < ledgerBeans.size(); i++){
                LedgerBean ledger = ledgerBeans.get(i);
                List<LedgerRelationBean> totalList = this.ledgerManagerMapper.getPointRelations(ledger.getLedgerId(), 1);
                if(totalList != null && totalList.size() > 0){
                    insertLedgerRelation(totalList);  //插
                }
                else {
                    List<LedgerRelationBean> partList = this.ledgerManagerMapper.getPointRelations(ledger.getLedgerId(), 2);
                    insertLedgerRelation(partList);  //插
                    List<LedgerRelationBean> ledgerList = this.ledgerManagerMapper.getLedgerRelations(ledger.getLedgerId());
                    insertLedgerRelation(ledgerList);  //插
                }
            }
        }

    }

    private void insertLedgerRelation(List<LedgerRelationBean> relations){
        if(relations != null && relations.size() > 0){
            for(int j = 0; j < relations.size(); j++){
                LedgerRelationBean relation = relations.get(j);
                relation.setRecId(SequenceUtils.getDBSequence());
                ledgerManagerMapper.addLedgerRelation(relation);  //插
            }
        }
    }

    private void updateOneLedgerModel(Long ledgerId){
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        List<LedgerRelationBean> relations = this.ledgerManagerMapper.getLedgerMeterRelation(ledgerId, null);
        if(relations != null && relations.size() > 0){
            for(int i = 0; i < relations.size(); i++){
                LedgerRelationBean relationBean = relations.get(i);
                Integer relationType = relationBean.getType();
                Long relationId = relationBean.getId();
                Integer attrType = relationBean.getAttr();
                Integer percent = relationBean.getPct();
                if(relationType == 2){
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("METER_ID", relationId);
                    temp.put("ADD_ATTR", attrType);
                    temp.put("PCT", percent);
                    result.add(temp);
                }
                else if(relationType == 1){
                    List<Map<String, Object>> listAdd = this.ledgerManagerMapper.getChildLedgerMeter(attrType, percent, relationId);
                    if(listAdd != null && listAdd.size() > 0){
                        result.addAll(listAdd);
                    }
                }
            }
        }

        // 先删除后插入
        ledgerManagerMapper.delLedgerMeter(ledgerId);
        if(result.size() > 0){
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("ledgerId", ledgerId);
            for(int i = 0; i < result.size(); i++){
                Map<String, Object> temp = result.get(i);
                param.put("meterId", temp.get("METER_ID"));
                param.put("addAttr", temp.get("ADD_ATTR"));
                param.put("percent", temp.get("PCT"));
                int num = ledgerManagerMapper.getOneRelationcount(ledgerId, Long.valueOf(temp.get("METER_ID").toString()));
                if(num == 0){
                    ledgerManagerMapper.insertLedgerMeterNew(param);
                }
                else{
                    ledgerManagerMapper.updateLedgerMeterNew(param);
                }
            }
        }

    }

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#saveThirdCompanySet(com.linyang.energy.model.LedgerBean)
	 */
	@Override
	public boolean saveThirdCompanySet(LedgerBean bean) {
		return ledgerManagerMapper.updateBySelective(bean)>0;
	}

	/* (non-Javadoc)
	 * @see com.linyang.energy.service.LedgerManagerService#getLogoConfig(java.lang.Long,java.lang.String)
	 */
	@Override
	public Map<String, Object> getLogoConfig(Long ledgerId,String loginPath,int logoType) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		int logoIconType = 1; // 1自定义 ; 2中性
		String logoColor = null, logoIcon = null;
		// 向上寻址
		while(true){
			LedgerBean ledgerBean = this.getLedgerDataById(ledgerId);
			if(ledgerBean == null)
				break;
			if(StringUtils.isBlank(logoColor))
				logoColor = ledgerBean.getLogoColor();
			logoIcon = ledgerBean.getLogoIcon();
			if(StringUtils.isBlank(logoIcon)){
				ledgerId = ledgerBean.getParentLedgerId();
				continue;
			} else {
				break;
			}
		}
		// 默认值
		if (StringUtils.isBlank(logoColor))
			logoColor = "1487e6";
		if (StringUtils.isBlank(logoIcon)) {
			if("neuter_login".equals(loginPath)) { // 中性
				if(logoType==1) { // 大屏
					logoIcon = "../energy/images/frame/neuter_demo_logo.png";
				} else {
					logoIcon = "../energy/images/frame/neuter_header_logo2.png";
				} 
			}
            else if("heat_login".equals(loginPath)) { // 热泵
                if(logoType==1) { // 大屏
                    logoIcon = "../energy/images/frame/heat_demo_logo.png";
                } else {
                    logoIcon = "../energy/images/frame/heat_header_logo2.png";
                }
            }
            else if("dlfl_login".equals(loginPath)) { // 芃霖
                if(logoType==1) { // 大屏
                    logoIcon = "../energy/images/frame/dlfl_demo_logo.png";
                } else {
                    logoIcon = "../energy/images/frame/dlfl_header_logo2.png";
                }
            }
            else if("zdny_login".equals(loginPath)) { // 中达能源互联网
                if(logoType==1) { // 大屏
                    logoIcon = "../energy/images/frame/zdny_demo_logo.png";
                } else {
                    logoIcon = "../energy/images/frame/zdny_header_logo2.png";
                }
            }
            else {
				if(logoType==1) {
					logoIcon = "../energy/images/frame/demo_logo.png";
				} else {
					logoIcon = "../energy/images/frame/header_logo2.png";
				} 
			}
			logoIconType = 2;
		}
		resultMap.put("logoColor", logoColor);
		resultMap.put("logoIcon", logoIcon);
		resultMap.put("logoIconType", logoIconType);
		return resultMap;
	}

    private void updateLedgerCompanyCount(List<LedgerBean> ledgers){
        for(LedgerBean l : ledgers){
            String name = l.getLedgerName();
            int count = 1;
            int si = name.lastIndexOf("(");
            int siz = name.lastIndexOf("（");
            int ei = name.lastIndexOf(")");
            int eiz = name.lastIndexOf("）");
            if(si != -1 && ei != -1 && StringUtils.isNumeric(name.substring(si+1,ei))){
                count = ledgerManagerMapper.countCompanyLedgerByLedgerId(l.getLedgerId());
                l.setLedgerName(name.substring(0,si+1)+ count +")");
//                        if(count >0 ){
//                        }else{
//                            l.setLedgerName(name.substring(0,si));
//                        }
            }else if(siz != -1 && eiz != -1 && StringUtils.isNumeric(name.substring(siz+1,eiz))){
                count = ledgerManagerMapper.countCompanyLedgerByLedgerId(l.getLedgerId());
                l.setLedgerName(name.substring(0,siz+1)+ count +"）");
            }else{
                count = ledgerManagerMapper.countCompanyLedgerByLedgerId(l.getLedgerId());
                l.setLedgerName(name + "(" + count +")");
//                        if(count >0 ){
//                        }else{
//                            l.setLedgerName(name);
//                        }
            }
            ledgerManagerMapper.updateBySelective(l);
        }
    }

    @Override
    public List<Short> getLedgerCalcDCPType(long ledgerId) {
        List<Short> meterTypeList = new ArrayList<Short>();
        List<MeterBean> meterList = meterManagerMapper.getCalcMeterByLedgerId(ledgerId);
        for (MeterBean meterBean : meterList) {
            if(meterBean.getMeterType() != null && meterBean.getMeterType() != 0 && !meterTypeList.contains(meterBean.getMeterType())){
                meterTypeList.add(meterBean.getMeterType());
            }
        }
        Collections.sort(meterTypeList);
        return meterTypeList;
    }

    @Override
    public void saveLedgerPosition(LedgerBean bean) {
        ledgerManagerMapper.updateBySelective(bean);
    }

    @Override
    public void deleteLedgerPosition(Long ledgerId) {
        ledgerManagerMapper.deleteLedgerPosition(ledgerId);
    }

    @Override
    public List<LedgerBean> getMapShowLedgerList(Long ledgerId, Integer searchModel, String selectIdStr, String keyWord){
        List<LedgerBean> list = new ArrayList<LedgerBean>();
        if(keyWord.length()==0){
            //搜索字为空，默认显示所有企业
            list = ledgerManagerMapper.getMapShowLedgerList2(ledgerId, "");
        }
        else{
            if(searchModel == 1 || searchModel == 2){ //选择省、市
                list = ledgerManagerMapper.getMapShowLedgerList1(ledgerId, searchModel, selectIdStr);
            }
            else if(searchModel == 3){  //选择平台运营商
                Long selectId = Long.valueOf(selectIdStr);
                list = ledgerManagerMapper.getMapShowLedgerList2(selectId, "");
            }
            else if(searchModel == 4){  //选择企业
                list = ledgerManagerMapper.getMapShowLedgerList2(ledgerId, keyWord);
            }
        }
        return list;
    }

    @Override
    public Map<String,Object> getLedgerUseData(LedgerBean ledger){
        Map<String, Object> map = new HashMap<String, Object>();

        Date now = new Date();
        Date beginTime = DateUtil.getPreMonthFristDay(now);
        Date endTime = DateUtil.getPreMonthLastDay(now);
        //电
        String ele_unit = "kWh";
        double ele = indexMapper.getLedgerOneUseByStat(ledger, 1, beginTime, endTime);
        ele = NumberUtil.formatDouble(ele, NumberUtil.PATTERN_FLOAT);
        if(ele > 100000){
            ele = DataUtil.doubleDivide(ele, 1000, 1);
            ele_unit = "MWh";
        }
        map.put("ele", ele);
        map.put("eleUnit", ele_unit);
        //水
        String water_unit = "m³";
        double water = indexMapper.getLedgerOneUseByStat(ledger, 2, beginTime, endTime);
        water = NumberUtil.formatDouble(water, NumberUtil.PATTERN_FLOAT);
        if(water > 100000){
            water = DataUtil.doubleDivide(water, 1000, 1);
            water_unit = "km³";
        }
        map.put("water", water);
        map.put("waterUnit", water_unit);
        //气
        String gas_unit = "m³";
        double gas = indexMapper.getLedgerOneUseByStat(ledger, 3, beginTime, endTime);
        gas = NumberUtil.formatDouble(gas, NumberUtil.PATTERN_FLOAT);
        if(gas > 100000){
            gas = DataUtil.doubleDivide(gas, 1000, 1);
            gas_unit = "km³";
        }
        map.put("gas", gas);
        map.put("gasUnit", gas_unit);
        //热
        String hot_unit = "kWh";
        double hot = indexMapper.getLedgerOneUseByStat(ledger, 4, beginTime, endTime);
        hot = NumberUtil.formatDouble(hot, NumberUtil.PATTERN_FLOAT);
        if(hot > 100000){
            hot = DataUtil.doubleDivide(hot, 1000, 1);
            hot_unit = "MWh";
        }
        map.put("hot", hot);
        map.put("hotUnit", hot_unit);

        return map;
    }

    @Override
    public Map<String,Object> getLedgerMessageData(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();

        //平台企业
        long companyCount = ledgerManagerMapper.countCompanyLedgerByLedgerId(ledgerId);
        map.put("companyCount",companyCount);
        //监测点
        long pointCount = ledgerManagerMapper.countMeterByLedgerId(ledgerId);
        map.put("pointCount",pointCount);
        //运营商
        long partnerCount = ledgerManagerMapper.countPartnerByLedgerId(ledgerId);
        map.put("partnerCount",partnerCount);

        return map;
    }

    @Override
    public Map<String,Object> getLedgerPowerData(Long ledgerId){
        Map<String, Object> map = new HashMap<String, Object>();

        //实时功率
        double power = 0;
        String power_unit = "kW";
        Date nowDate = new Date();
        List<Date> list = this.dataQueryMapper.getgetMaxDateListCurAp(ledgerId, DateUtil.clearDate(nowDate), nowDate);
        if(CollectionUtils.isNotEmpty(list)){
            Date time = DateUtil.getDateMinusMinute(list.get(0), 15);
            power = this.dataQueryMapper.getCurPowData(ledgerId, time);
            power = NumberUtil.formatDouble(power, NumberUtil.PATTERN_FLOAT);
        }

        if(power > 100000){
            power = NumberUtil.formatDouble(DataUtil.doubleDivide(power, 1000).doubleValue(), NumberUtil.PATTERN_FLOAT);
            power_unit = "MW";
        }
        map.put("power", power);
        map.put("powerUnit", power_unit);

        return map;
    }

    @Override
    public List<LedgerBean> getAllSubLedgersByLedgerId(Long ledgerId) {
        return ledgerManagerMapper.getAllSubLedgersByLedgerId(ledgerId);
    }

	@Override
	public boolean saveCompanyLink(Long ledgerId, String urlStr) {
		
			LedgerBean ledger = new LedgerBean();
			ledger.setLedgerId(ledgerId);
			ledger.setUrl(urlStr);
			ledgerManagerMapper.updateBySelective(ledger);
			return true;
		
	}

	/**
	 * 判断是否设置了第三方链接
	 */
	@Override
	public Map<String, Object> checkThirdLink(Long ledgerId) {
		Map<String, Object> map = new HashMap<String, Object>();
		LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(ledgerId);
		boolean hasUrl = false;
		String url = ledger.getUrl();
		if(url != null && url.length() > 0){
			hasUrl = true;
		}
		map.put("hasUrl", hasUrl);
		map.put("url", url);
		return map;
	}

	@Override
	public List<Map<String, Object>> getLedgersByLedgerId(Long ledgerId) {
		return ledgerManagerMapper.getLedgersByLedgerId(ledgerId);
	}

    @Override
    public List<Map<String, Object>> getLedgersForGroup(Long accountId) {
        return ledgerManagerMapper.getLedgersForGroup(accountId);
    }

    @Override
    public List<Map<String, Object>> getLedgersForGroup2(Long accountId) {
        return ledgerManagerMapper.getLedgersForGroup2(accountId);
    }

	@Override
	public List<Map<String, Object>> getMetersByLedgerId(Long ledgerId) {
		return ledgerManagerMapper.getMetersByLedgerId(ledgerId);
	}

    @Override
    public List<Map<String, Object>> getMetersForGroup(Long accountId) {
        return ledgerManagerMapper.getMetersForGroup(accountId);
    }

	@Override
	public Long getLedgerByMeterId(Long meterId) {
		List<Long> list = ledgerManagerMapper.getLedgerByMeterId(meterId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

    @Override
    public int getIsLedgerFlag(Long ledgerId){
        int ledgerFlag = 0;
        List<Long> pCompanys = this.ledgerManagerMapper.getParentCompanys(ledgerId);
        if (pCompanys != null && pCompanys.size() > 0) {
            ledgerFlag = 1;
        }
        return ledgerFlag;
    }

	@Override
	public Long getLedgerIfNull(Long accountId) {
		return ledgerManagerMapper.getLedgerIfNull(accountId);
	}

	 /**
		 * 根据测量点id查询能管对象id集合、所在结构树根id
		 * @param MeterId ledgerId
		 * @return
		 */
	@Override
	public Map<String, Object> getLedgerInfoByMeterId(Long meterId,Long ledgerId) {
		List<Long> ledgerIds = null;
		List<Long> meterIds = null;
		List<Long> lgIds = null;
		Long rootId = null;
		Long companyId = null;
		Long pitchId = null;//在管理者下面需要选中的id
		boolean hasTree = true;
		boolean isShow = true;
		Map<String,Object> map = new HashMap<String,Object>();
		rootId = ledgerManagerMapper.getRootLedgerIdByMeterId(meterId);
		lgIds = ledgerManagerMapper.getLegerIdByMeterId(meterId);//其次从T_LEDGER_SHOW查询数据是否存在
		if(lgIds == null || lgIds.size() == 0){
			lgIds = ledgerManagerMapper.getLegerIdByMeterId1(meterId);//首先从T_LEDGER_RELATION查询数据是否存在
			isShow = false;
		}
		if( null != lgIds && lgIds.size() > 0){
			if(isShow){
				pitchId = meterId;
			}else{
				if(ledgerId != null && ledgerId != 0){
					if(lgIds.contains(ledgerId)){
						pitchId = ledgerId;
					}
				}else{
					pitchId = lgIds.get(0);
				}
			}
			
		}
		if(lgIds == null || lgIds.size() == 0){
			meterIds = ledgerManagerMapper.getMeterIdsByMeterId(meterId);//第四步 从T_LINELOSS_METER_INFO查询数据是否存在
		}
		if((lgIds == null || lgIds.size() == 0) && null !=meterIds && meterIds.size()>0 ){
			
			lgIds = ledgerManagerMapper.getLedgerIdByMeterIds(meterIds);//第六步 从T_LINELOSS_METER_INFO获得id集合，再从T_LEDGER_SHOW查询数据是否存在
		}
		if((lgIds == null || lgIds.size() == 0) && null !=meterIds && meterIds.size()>0){
			lgIds = ledgerManagerMapper.getLedgerIdByMeterIds1(meterIds);//第五步第六步 从T_LINELOSS_METER_INFO获得id集合，再从T_LEDGER_RELATION查询数据是否存在
		}
		
		if(null != lgIds && lgIds.size() > 0){
			if(lgIds.contains(ledgerId)){//能管对象id集合中是否包含ledgerId
				ledgerIds = ledgerManagerMapper.getLedgerIdsByLedgerId(ledgerId);
				if(isShow){
					pitchId = meterId;
				}else{
					pitchId = ledgerId;
				}
			}else{
				ledgerIds = ledgerManagerMapper.getLedgerIdsByLedgerId(lgIds.get(0));
			}
			if(null == ledgerIds || ledgerIds.size()==0){
				if(ledgerId != null && ledgerId != 0){
					pitchId = ledgerId;
					if(null != ledgerId)ledgerIds = ledgerManagerMapper.getLedgerIdsByLedgerId(ledgerId);
				}else{
					companyId = ledgerManagerMapper.getCompanyIdByMeterId(meterId);
					pitchId = companyId;
					if(null != companyId)ledgerIds = ledgerManagerMapper.getLedgerIdsByLedgerId(companyId);
				}
				
			}
			if(null == pitchId){
				pitchId = lgIds.get(0);
			}
		}else{
			if(ledgerId != null && ledgerId != 0){
				pitchId = ledgerId;
				if(null != ledgerId)ledgerIds = ledgerManagerMapper.getLedgerIdsByLedgerId(ledgerId);
			}else{
				companyId = ledgerManagerMapper.getCompanyIdByMeterId(meterId);
				pitchId = companyId;
				if(null != companyId)ledgerIds = ledgerManagerMapper.getLedgerIdsByLedgerId(companyId);
			}
		}
		
		if(null == ledgerIds || ledgerIds.size()==0){
			hasTree = false;
		}
		map.put("pitchId", pitchId);
		map.put("hasTree", hasTree);
		map.put("ledgerIds", ledgerIds);
		map.put("rootId", rootId);
		return map;
	}

    @Override
    public Map<String, Object> getLedgerHeatType(Long ledgerId){
        Map<String,Object> map = new HashMap<String,Object>();
        //查询热泵类型
        int isHeat = 0;
        LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(ledgerId);
        if(null != ledgerBean
           && null != ledgerBean.getAnalyType() && ledgerBean.getAnalyType() == 106
           && null != ledgerBean.getEqpTypeId() && ledgerBean.getEqpTypeId() == 1){
            isHeat = 1;
        }
        map.put("isHeat", isHeat);
        //查询热泵数据
        if(isHeat == 1){
            List<Long> meterIds = ledgerManagerMapper.getMeterIdsByLedgerId(ledgerId);
            if(CollectionUtils.isNotEmpty(meterIds)){
                Long meterId = meterIds.get(0);
                Date endTime = new Date();
                Date beginTime = DateUtil.getNextMinuteDate(endTime, -15);
                //机组运行
                Integer status1 = 0;
                List<Integer> oneList = ledgerManagerMapper.getHeatStatusBy(meterId, 1, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(oneList)){
                    status1 = oneList.get(0);
                }
                map.put("status1", status1);
                //机组制热
                Integer status2 = 0;
                List<Integer> twoList = ledgerManagerMapper.getHeatStatusBy(meterId, 2, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(twoList)){
                    status2 = twoList.get(0);
                }
                map.put("status2", status2);
                //机组除霜
                Integer status3 = 0;
                List<Integer> threeList = ledgerManagerMapper.getHeatStatusBy(meterId, 3, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(threeList)){
                    status3 = threeList.get(0);
                }
                map.put("status3", status3);
                //机组防冻
                Integer status4 = 0;
                List<Integer> fourList = ledgerManagerMapper.getHeatStatusBy(meterId, 4, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(fourList)){
                    status4 = fourList.get(0);
                }
                map.put("status4", status4);
                //机组故障
                Integer status5 = 0;
                List<Integer> fiveList = ledgerManagerMapper.getHeatStatusBy(meterId, 5, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(fiveList)){
                    status5 = fiveList.get(0);
                }
                map.put("status5", status5);
                //室温
                List<Double> roomTempList = ledgerManagerMapper.getHeatRoomTempBy(meterId, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(roomTempList)){
                    Double roomTemp = roomTempList.get(0);
                    map.put("roomTemp", NumberUtil.formatDouble(roomTemp, "0.#"));
                }
                //水温(出水温度)
                List<Double> waterTempList = ledgerManagerMapper.getHeatWaterTempBy(meterId, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(waterTempList)){
                    Double waterTemp = waterTempList.get(0);
                    map.put("waterTemp", NumberUtil.formatDouble(waterTemp, "0.#"));
                }
                //Ub
                List<Double> ubList = ledgerManagerMapper.getHeatUbBy(meterId, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(ubList)){
                    Double ub = ubList.get(0);
                    map.put("ub", NumberUtil.formatDouble(ub, "0.#"));
                }
                //Ib
                List<Double> ibList = ledgerManagerMapper.getHeatIbBy(meterId, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(ibList)){
                    Double ib = ibList.get(0);
                    map.put("ib", NumberUtil.formatDouble(ib, "0.#"));
                }
                //qVal
                List<Map<String, Object>> qVal2List = ledgerManagerMapper.getHeatQVal2(meterId, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(qVal2List)){
                    Map<String, Object> temp = qVal2List.get(0);
                    Double qVal2 = Double.valueOf(temp.get("VAL").toString());
                    String time2 = temp.get("DATE_TIME").toString();
                    Date date2 = DateUtil.convertStrToDate(time2, DateUtil.MOUDLE_PATTERN);
                    Date date1 = DateUtil.getNextMinuteDate(date2, -60);
                    String time1 = DateUtil.convertDateToStr(date1, DateUtil.MOUDLE_PATTERN);
                    List<Double> qVal1List = ledgerManagerMapper.getHeatQVal1(meterId, time1);
                    if(CollectionUtils.isNotEmpty(qVal1List)){
                        Double qVal1 = qVal1List.get(0);
                        Double qVal = qVal2 - qVal1;
                        map.put("qVal", NumberUtil.formatDouble(qVal, "0.#"));
                    }
                }
                //hotVal
                List<Map<String, Object>> hotVal2List = ledgerManagerMapper.getHeatHotVal2(meterId, beginTime, endTime);
                if(CollectionUtils.isNotEmpty(hotVal2List)){
                    Map<String, Object> temp = hotVal2List.get(0);
                    Double hotVal2 = Double.valueOf(temp.get("VAL").toString());
                    String time2 = temp.get("DATE_TIME").toString();
                    Date date2 = DateUtil.convertStrToDate(time2, DateUtil.MOUDLE_PATTERN);
                    Date date1 = DateUtil.getNextMinuteDate(date2, -60);
                    String time1 = DateUtil.convertDateToStr(date1, DateUtil.MOUDLE_PATTERN);
                    List<Double> hotVal1List = ledgerManagerMapper.getHeatHotVal1(meterId, time1);
                    if(CollectionUtils.isNotEmpty(hotVal1List)){
                        Double hotVal1 = hotVal1List.get(0);
                        Double hotVal = hotVal2 - hotVal1;
                        map.put("hotVal", NumberUtil.formatDouble(hotVal, "0.#"));
                    }
                }
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> getEnergyMapData(Long ledgerId){
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("ledgerStyle", this.ledgerManagerMapper.getLedgerFontstyleById(ledgerId));
        result.put("ledgerRelates", this.ledgerManagerMapper.getLedgerRelatesById(ledgerId));

        return result;
    }

    @Override
    public void saveLedgerBackImg(Long ledgerId, String fileName){
        long count = this.ledgerManagerMapper.getLedgerFontstyleNumBy(ledgerId);
        if(count > 0){
            ledgerManagerMapper.updateLedgerBackImg(ledgerId, fileName);
        }
        else {
            ledgerManagerMapper.addLedgerBackImg(ledgerId, fileName);
        }
    }

    @Override
    public void saveLedgerStyle(Long ledgerId, Integer displayForm, Integer fontSize, String fontWeight, String fontColor, Double bubble, String backColor, String dataColor){
        long count = this.ledgerManagerMapper.getLedgerFontstyleNumBy(ledgerId);
        if(count > 0){
            ledgerManagerMapper.updateLedgerStyle(ledgerId, displayForm, fontSize, fontWeight, fontColor, bubble, backColor, dataColor);
        }
        else {
            ledgerManagerMapper.addLedgerStyle(ledgerId, displayForm, fontSize, fontWeight, fontColor, bubble, backColor, dataColor);
        }
    }

    @Override
    public String saveLedgerRelate(Long ledgerId, Long oldObjectId, Integer oldObjectType, Long newObjectId, Integer newObjectType, String dataType, Double x, Double y, Integer newPosition){
        String returnStr = "";
        Map<String, Object> fontStyle = this.ledgerManagerMapper.getLedgerFontstyleById(ledgerId);
        if(fontStyle != null && fontStyle.keySet().contains("displayForm") && fontStyle.get("displayForm").toString().equals("2") && dataType.contains(",")){
            returnStr = "液晶样式只能配置一个数据项展示";
            return returnStr;
        }

        if(oldObjectId > 0 && oldObjectType > 0){
            ledgerManagerMapper.updateLedgerRelate(ledgerId, oldObjectId, oldObjectType, newObjectId, newObjectType, dataType, x, y, newPosition);
        }
        else {
            ledgerManagerMapper.addLedgerRelate(ledgerId, newObjectId, newObjectType, dataType, x, y, newPosition);
        }
        return returnStr;
    }

    @Override
    public void removeLedgerRelate(Long ledgerId, Long objectId, Integer objectType){
        ledgerManagerMapper.removeLedgerRelate(ledgerId, objectId, objectType);
    }

    @Override
    public Map<String, Object> getOnePointDataInImg(Long objectId, Integer objectType, Long ledgerId){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        //计算日时间
        String yesterday = DateUtil.convertDateToStr(DateUtil.getSomeNextDayDate(new Date(), -1), DateUtil.SHORT_PATTERN);
        //计算实时时间点
        Date nowDate = new Date();
        String minutes = DateUtil.convertDateToStr(nowDate, DateUtil.MINUTE_PATTERN);
        int minuteNum = Integer.valueOf(minutes);
        minuteNum = minuteNum % 15;
        Date curTime = DateUtil.getNextMinuteDate(DateUtil.clearSecond(nowDate), -(minuteNum+15));

        Map<String,Object> fontStyle = this.ledgerManagerMapper.getLedgerMeterFontstyleBy(ledgerId, objectId, objectType);
        String dataType = "";
        if(fontStyle != null && fontStyle.containsKey("dataType")){
            dataType = fontStyle.get("dataType").toString();
        }
        String[] types = dataType.split(",");
        for(String type : types){
            Integer temp = Integer.valueOf(type);

            if(temp == 1 || temp == 2 || temp == 3){    //日:电量、水量、气量
                Double data = this.ledgerManagerMapper.getDayDataInImg(objectId, objectType, temp, yesterday);
                if(data == null){
                    resultMap.put("data" + type, "--");
                }
                else {
                    resultMap.put("data" + type, NumberUtil.formatDouble(data, NumberUtil.PATTERN_INTEGER));
                }
            }
            else if(temp == 5){  //实时:功率
                Double data = this.ledgerManagerMapper.getCurApInImg(objectId, objectType, curTime);
                if(data == null){
                    resultMap.put("data" + type, "--");
                }
                else {
                    resultMap.put("data" + type, NumberUtil.formatDouble(data, NumberUtil.PATTERN_INTEGER));
                }
            }

            if((temp == 6 || temp == 7 || temp == 8) && objectType == 2){  //实时:电压、电流、示值
                if(temp == 6){
                    Map<String, Object> param = new HashMap<String, Object>();
                    // 得到电源接线方式1,三相三线;2,三相四线;3:单相表
                    Integer commMode = this.phoneMapper.queryCommMode(objectId);
                    param.put("commMode", commMode);
                    param.put("objectId", objectId);
                    param.put("curTime", curTime);
                    resultMap.put("data" + type,  ledgerManagerMapper.getCurVInImg(param));
                }
                if(temp == 7){
                    Map<String, Object> param = new HashMap<String, Object>();
                    // 得到电源接线方式1,三相三线;2,三相四线;3:单相表
                    Integer commMode = this.phoneMapper.queryCommMode(objectId);
                    param.put("commMode", commMode);
                    param.put("objectId", objectId);
                    param.put("curTime", curTime);
                    resultMap.put("data" + type,  ledgerManagerMapper.getCurIInImg(param));
                }
                if(temp == 8){
                    Double data = this.ledgerManagerMapper.getCurEInImg(objectId, curTime);
                    if(data == null){
                        resultMap.put("data" + type, "--");
                    }
                    else {
                        resultMap.put("data" + type, NumberUtil.formatDouble(data, NumberUtil.PATTERN_INTEGER));
                    }
                }
            }
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> getObjectNeedData(Long ledgerId, Long objectId, Integer objectType){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        String objectName = "";
        String dataType = "";
        Integer position = 1;
        if(ledgerId > 0 && objectId > 0 && objectType > 0){
            if(objectType == 1){
                LedgerBean ledgerBean = this.ledgerManagerMapper.selectByLedgerId(objectId);
                objectName = ledgerBean.getLedgerName();
            }
            else if(objectType == 2){
                MeterBean meterBean = this.meterManagerMapper.getMeterDataByPrimaryKey(objectId);
                objectName = meterBean.getMeterName();
            }


            Map<String,Object> fontStyle = this.ledgerManagerMapper.getLedgerMeterFontstyleBy(ledgerId, objectId, objectType);
            if(fontStyle != null && fontStyle.keySet().contains("dataType")){
                dataType = fontStyle.get("dataType").toString();
                position = Integer.valueOf(fontStyle.get("position").toString());
            }
        }

        resultMap.put("objectName", objectName);
        resultMap.put("dataType", dataType);
        resultMap.put("position", position);
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> getRelateLedgerMeterList(Long ledgerId, Integer searchModel, Long objectId, Integer objectType, String objectName){
        List<Map<String, Object>> list = new ArrayList<>();
        if(searchModel == objectType){
            Map<String, Object> temp = new HashMap<>();
            temp.put("ID", objectId);
            temp.put("NAME", objectName);
            list.add(temp);
        }
        //查询该ledgerId是否有企业类型的父节点
        if(searchModel == 2){
            List<Long> pIds = this.ledgerManagerMapper.getParentCompLedger(ledgerId);
            if(CollectionUtil.isNotEmpty(pIds)){
                ledgerId = pIds.get(0);
            }
        }

        list.addAll(this.ledgerManagerMapper.getRelateLedgerMeterList(ledgerId, searchModel));

        return list;
    }

    @Override
    public Long getIfCanChangeYj(Long ledgerId){
        Long count = this.ledgerManagerMapper.getDataTypeCount(ledgerId);
        return count;
    }

    @Override
    public List<Map<String, Object>> getDataMaintainPage(Map<String, Object> queryMap){
        List<Map<String,Object>> list = ledgerManagerMapper.getDataMaintainPage(queryMap);
        return list == null ? new ArrayList<Map<String,Object>>() : list;
    }

    @Override
    public List<Map<String, Object>> getCompLedgerList(Long ledgerId){
        return ledgerManagerMapper.getCompLedgerList(ledgerId);
    }

    @Override
    public Map<String, Object> getDataMaintain(Long ledgerId){
        Map<String,Object> map = ledgerManagerMapper.getDataMaintain(ledgerId);
        return map == null ? new HashMap<String,Object>() : map;
    }

    @Override
    public void deleteMaintain(Long ledgerId){
        this.ledgerManagerMapper.deleteMaintain(ledgerId);
    }

    @Override
    public void saveDataMaintain(Map<String, Object> info){
        Long ledgerId = Long.valueOf(info.get("ledgerId").toString());
        this.ledgerManagerMapper.deleteMaintain(ledgerId);
        this.ledgerManagerMapper.insertDataMaintain(info);
    }
	
	
	@Override
	public Integer deleteFacil(LedgerBean ledgerBean) {
		return ledgerManagerMapper.deleteFacil( ledgerBean );
	}
	
	@Override
	public List<Map<String, Object>> selectParent(Long ledgerId) {
		return ledgerManagerMapper.selectParent( ledgerId );
	}
	
	@Override
	public List<Map<String, Object>> queryPollut(Long ledgerId) {
		return ledgerManagerMapper.queryPollut( ledgerId );
	}
	
	@Override
	public List<Map<String, Object>> queryPollutctl(Long ledgerId) {
		return ledgerManagerMapper.queryPollutctl( ledgerId );
	}
	
	@Override
	public boolean checkdeviceCode(Map<String, Object> map) {
		boolean flag = true;
		if (CommonMethod.isMapNotEmpty(map)) {
			if(ledgerManagerMapper.checkdeviceCode(map) == 0) {//false表示没有相同的
				flag = false;
			} else {		//true表示存在相同的
				flag = true;
			}
		}
		return flag;
	}
}
