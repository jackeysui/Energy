package com.linyang.energy.service;

import java.util.List;
import java.util.Map;

import com.linyang.energy.model.LedgerRelationBean;
import com.linyang.energy.model.LineLossTreeBean;

public interface TreeSetService {

	/**
	 * 得到未配置的DCP
	 * @param ledgerId
	 * @return
	 */
	List<LineLossTreeBean> getUnSetDCP(long ledgerId);

	/**
	 * 得到已配置的DCP
	 * @param ledgerId
	 * @return
	 */
	List<LineLossTreeBean> getSetDCP(long ledgerId, long type,String meterName);

	/**
	 * 得到EMO管理树
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getEMOTree(long ledgerId,String meterName);

	/**
	 * 得到EMO总加组模型配置
	 * @param ledgerId
	 * @return
	 */
	List<LedgerRelationBean> getEMOModel1(long ledgerId);
	
	/**
	 * 得到EMO表计模型配置
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getEMOModel2(long ledgerId);

	
	/**
	 * 保存
	 * @param ledgerId 
	 * @param param
	 * @return
	 */
	boolean save(Long ledgerId, List<Map<String, Object>> ledgerRelationBeans, List<Map<String, Object>> ledgerShowBeans);

	/**
	 * 得到企业列表
	 * @param ledgerId
	 * @return
	 */
	List<Map<String, Object>> getCompanyList(Long ledgerId, Integer analyType);

    /**
     * 得到管辖区域内未设置经纬度的企业列表
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getNoPositionLedgerList(Long ledgerId);

    /**
     * 得到首页地图 某种搜索模式 下的自动完成结果
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getSearchModelDataList(Long ledgerId, Integer searchModel);

    /**
     * 得到企业和平台运营商
     * @param ledgerId
     * @return
     */
	List<Map<String, Object>> getEMOList(Long ledgerId);
	
	/**
	 * 根据id得到未配置的DCP
	 * @param ledgerId
	 * @return
	 */
	List<LineLossTreeBean> getUnSetDCPByName(String meterName,Long ledgerId);

}
