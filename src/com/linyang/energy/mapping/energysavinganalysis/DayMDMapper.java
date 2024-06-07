package com.linyang.energy.mapping.energysavinganalysis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.energy.model.CurveBean;

/**
 * 日需量分析
 * @author guosen
 * @date 2014-12-23
 */
public interface DayMDMapper {
	
	/**
	 * 得到日需量数据
	 * @param params
	 * @return
	 */
	public List<CurveBean> queryDayMDData(Map<String, Object> params);

	/**
	 * 得到需量阀值
	 * @param meterId
	 * @return
	 */
	public Double getDemandThres(@Param("treeType")int treeType,@Param("meterId")Long meterId);

	/**
	 * 得到分户容量
	 * @param ledgerId
	 * @return
	 */
	public Long getLedgerVolumeByLedgerId(@Param("ledgerId")Long ledgerId);	

}
