package com.linyang.energy.mapping.pullSwitch;

import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:27 2019/11/27
 * @ Description：拉合闸
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface PullSwitchMapper{
	
	/**
	 * 根据表计Id查询表计信息
	 * @return
	 */
	Map<String,Object> queryMeterById(Long meterId);
	
	/**
	 * 根据表地址获取表计信息
	 * @param addr
	 * @return
	 */
	Map<String,Object> queryMeterByAddr(String addr);
	
	/**
	 * 根据子分户id查询企业信息
	 * @param ledgerId
	 * @return
	 */
	Map<String,Object> queryLedger(Long ledgerId);
	
	/**
	 * 根据meterId查询测量点信息(采集点不存在该终端时使用)
	 * @param meterId
	 * @return
	 */
	Map<String,Object> queryMeterDataById(Long meterId);

}
