package com.linyang.energy.mapping.energydataquery;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.StartrateBean;

/**
 * 开机率分析mapper
 * 
 * @author gaofeng
 * 
 */
public interface StartingRateMapper {

	/**
	 * 查询开机率
	 * 
	 * @param allMin
	 * @param meterIds
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<StartrateBean> queryStartRateData(@Param("allMin") int allMin, List<String> meterIds,
			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("beginTime2") Date beginTime2,  @Param("endTime2") Date endTime2);
	
//	public List<StartrateBean> queryStartRateData(@Param("allMin") int allMin, @Param("meterIds") String meterIds,
//			@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("beginTime2") Date beginTime2,  @Param("endTime2") Date endTime2);
	
	/**
	 * 得到设备树
	 * @param ledgerId
	 * @return
	 */
	public List<LedgerTreeBean> getTree(@Param("ledgerId") long ledgerId);

}
