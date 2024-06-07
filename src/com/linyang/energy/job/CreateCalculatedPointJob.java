package com.linyang.energy.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.linyang.energy.model.LedgerAllMeterBean;
import com.linyang.energy.model.LedgerDeviceAllMeterBean;
import com.linyang.energy.model.LedgerMeterBean;
import com.linyang.energy.service.LedgerManagerService;

/**
 * 计算各个分户、分项下有效的计算计量点
 *
 */
public class CreateCalculatedPointJob {
	@Autowired
	private LedgerManagerService ledgerManagerService;


    /**
     * 全局刷新 (在用)
     */
    public void processLedgerRelation() {
        this.ledgerManagerService.updateLedgerMeter();
    }



	/**
	 * 处理分户或分项有效的计算计量点 (已废弃)
	 * 
	 * @param type
	 *            1、分户；2、分项
	 */
	public void processAllLedgerMeter(int type) {
		List<LedgerAllMeterBean> ledgers;
		if (type == 1)
			ledgers = ledgerManagerService.getLedgerAllMeter();
		else
			ledgers = ledgerManagerService.getDeviceAllMeter();

		Map<Long, Map<Integer, List<LedgerMeterBean>>> result = processLedgerMeter(ledgers, type);
		ledgerManagerService.insertLedgerMeter(result, type);
	}

	/**
	 * 处理分户或分项有效的计算计量点 (已废弃)
	 * 
	 * @param ledgers
	 * @param type
	 *            1、分户；2、分项
	 */
	public Map<Long, Map<Integer, List<LedgerMeterBean>>> processLedgerMeter(List<LedgerAllMeterBean> ledgers, int type) {
		Map<Long, Map<Integer, List<LedgerMeterBean>>> result = new HashMap<Long, Map<Integer, List<LedgerMeterBean>>>();
		Map<Integer, List<LedgerMeterBean>> meters;
		Map<Integer, List<LedgerMeterBean>> parentMeters;
		List<LedgerMeterBean> directMeters;
		long preLedgerId = 0;
		int index = 0;

		for (LedgerAllMeterBean lm : ledgers) {
			meters = result.get(lm.getLedgerId());
			if (meters == null){
                meters = new HashMap<Integer, List<LedgerMeterBean>>();
            }
			if (lm.getParentLedgerId() != 0) {
				parentMeters = result.get(lm.getParentLedgerId());
				if (parentMeters == null) {
                    parentMeters = new HashMap<Integer, List<LedgerMeterBean>>();
                }
			}
            else {
                parentMeters = null;
            }

			if (lm.getMeters() != null && lm.getMeters().size() > 0) {
				index = 0;      boolean hasNoDirectTotal = false;//该分户下没有直接总表，要将直接分表计算在内
				sortLedgerMeterByType(lm.getMeters(), type);
				for (LedgerMeterBean meter : lm.getMeters()) {
					directMeters = meters.get(meter.getMeterType());
					if (directMeters == null){
                        directMeters = new ArrayList<LedgerMeterBean>();
                    }

                    if (isTotal(meter.getMeterAttr(), type)) {//有直接总表
                        if (directMeters.size() > 0 && index == 0 && preLedgerId != lm.getLedgerId()) {
                            directMeters.clear();
                        }
                        directMeters.add(meter);
                    } else {
                        if (index == 0) {
                            directMeters.add(meter);
                            hasNoDirectTotal = true;
                        } else {
                            if (hasNoDirectTotal) {//直接表全是分，没有直接总表
                                directMeters.add(meter);
                            }
                        }
                    }

					meters.put(meter.getMeterType(), directMeters);
					index ++;
				}
			}
			result.put(lm.getLedgerId(), meters);
			if (lm.getParentLedgerId() != 0) {// 如果有父对象，同时加入父对象中
				for (Integer mid : meters.keySet()) {
                    if(parentMeters != null && parentMeters.size() >mid){
                        if (parentMeters.get(mid) == null) {
                            List<LedgerMeterBean> dm = new ArrayList<LedgerMeterBean>();  dm.addAll(meters.get(mid));  parentMeters.put(mid, dm);
                        }
                        else {
                            parentMeters.get(mid).addAll(meters.get(mid));
                        }
                    }
				}
				result.put(lm.getParentLedgerId(), parentMeters);
			}
			preLedgerId = lm.getLedgerId();
		}

		return result;
	}

	/**
	 * 处理分户分项有效的计算计量点 (已废弃)
	 */
	public void processLedgerDeviceMeter() {
		Map<Long, Map<Long, Map<Integer, List<LedgerMeterBean>>>> result = new HashMap<Long, Map<Long, Map<Integer, List<LedgerMeterBean>>>>();

		List<LedgerDeviceAllMeterBean> ledgers = ledgerManagerService.getLedgerDeviceMeter();
		for (LedgerDeviceAllMeterBean ledger : ledgers) {// 按分户分别单独处理
			result.put(ledger.getBigLedgerId(), processLedgerMeter(ledger.getAllDevices(), 2));
		}
		ledgerManagerService.insertLedgerDeviceMeter(result);
	}

	/**
	 * 是否总表  (已废弃)
	 * 
	 * @param lType
	 * @param type
	 * @return
	 */
	private boolean isTotal(int lType, int type) {
		if (type == 1)
			return isLedgerTotal(lType);
		else
			return isDeviceTotal(lType);
	}

	/**
	 * 是否分户总表  (已废弃)
	 * 
	 * @param type
	 * @return
	 */
	private boolean isLedgerTotal(int type) {
		if (type == 1 || type == 4)
			return true;
		return false;
	}

	/**
	 * 是否分项总表  (已废弃)
	 * 
	 * @param type
	 * @return
	 */
	private boolean isDeviceTotal(int type) {
		if (type == 2 || type == 4)
			return true;
		return false;
	}
	
	/**
	 * 将总表放在队列的最前面，方便处理  (已废弃)
	 * 
	 * @param meters
	 */
	private void sortLedgerMeterByType(List<LedgerMeterBean> meters, int type) {
		if (meters.size() == 1)
			return;

		List<LedgerMeterBean> l = new ArrayList<LedgerMeterBean>();
		ListIterator<LedgerMeterBean> it = meters.listIterator();
		while (it.hasNext()) {
			LedgerMeterBean m = it.next();
			if (isTotal(m.getMeterAttr(), type)) {
				l.add(m);
				it.remove();
			}
		}
		if (l.size() > 0) {
			meters.addAll(0, l);
		}
	}

}
