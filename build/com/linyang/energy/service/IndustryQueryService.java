package com.linyang.energy.service;

import com.linyang.energy.model.DistributionBean;

import java.util.List;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 9:38 2018/12/20
 * @ Description：产污设施分布统计
 * @ Modified By：:dingy
 * @Version: 1.0
 */
public interface IndustryQueryService {
	
	/** 查询所选节点下一级所有企业单位 (选择节点不是企业单位则去掉102条件) */
	List<DistributionBean> queryAllEnterprise(long ledgerId);
	
	/** 查询企业下治污源数量 */
	long queryCountByPollutctl(long ledgerId);
	
	/** 查询企业下产污源数量 */
	long queryCountByPollut(long ledgerId);
	
	/** 查询区域等高级别能管对象下企业数量 */
	long queryCountForParent(long ledgerId);
	
	/** 查询所选节点下所有企业(按照行业) */
	List<DistributionBean> queryAllLedger(long ledgerId);
	
	/** 查询企业下治污源数量(行业分组专用) */
	long queryCountByPollutctl_2(String ledgerIds);
	
	/** 查询企业下产污源数量(行业分组专用) */
	long queryCountByPollut_2(String ledgerIds);
	
}
