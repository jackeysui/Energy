package com.linyang.energy.mapping.merchants;

import com.linyang.energy.model.MerchantBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 10:44 2019/7/15
 * @ Description：class说明
 * @ Modified By：:dingy
 * @Version: $version$
 */
public interface MerchantsMapper {
	
	/**
	 * 获取商户的最下一层节点列表
	 * @param ledgerId
	 * @return
	 */
	List<MerchantBean> getMerchants(@Param( "ledgerId" ) long ledgerId,@Param( "userNo" )String userNo,@Param( "ledgerName" )String ledgerName);
	
	/**
	 * 查询能管对象下拥有费率的最底层能管对象的数量,用于判断是否有没有配置费率的能管对象
	 * @param ledgerId
	 * @return
	 */
	Integer getMerchantsNum(@Param( "ledgerId" ) long ledgerId,@Param( "userNo" )String userNo,@Param( "dataType" ) int dataType,@Param( "ledgerName" )String ledgerName);
	
	/**
	 * 查询电能 起始值,结束值,电量(用于展示总示值,电量)
	 * @param beginTime
	 * @param endTime
	 * @param rateNum
	 * @param meterIds
	 * @return
	 */
	Map<String,Object> queryTotalValue(@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime,@Param( "meterIds" )List<Long> meterIds);
	
	/**
	 * 查询电能 起始值,结束值,电量(用于展示分示值,电量)
	 * @param beginTime
	 * @param endTime
	 * @param rateNum
	 * @param meterIds
	 * @return
	 */
	Map<String,Object> queryPointValue(@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime,@Param( "rateNum" )long rateNum,@Param( "meterIds" )List<Long> meterIds);
	
	/**
	 * 查询水 起始值,结束值,水量
	 * @param beginTime
	 * @param endTime
	 * @param meterIds
	 * @return
	 */
	Map<String,Object> queryWaterValue(@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime,@Param( "meterIds" )List<Long> meterIds);
	
	/**
	 * 查询气 起始值,结束值,气量
	 * @param beginTime
	 * @param endTime
	 * @param meterIds
	 * @return
	 */
	Map<String,Object> queryHeatValue(@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime,@Param( "meterIds" )List<Long> meterIds);
	
	
	
	
	// add or update method by catkins
	// date ]
	// Modify the content: 数据相关查询
	/**
	 * 查询正向有功分费率电能示值(用于显示示值)
	 * @param meterId
	 * @param date
	 * @param rate
	 * @return
	 */
	Map<String,Object> queryFaeRate(@Param("meterId")Long meterId,  @Param("date")Date date, @Param("rate")int rate,@Param( "instead" )int instead);
	
	/**
	 * 查询正向有功电能示值(用于计算电量)
	 * @param meterId
	 * @param date
	 * @param rate
	 * @return
	 */
	Double getViewDayERate(@Param("meterId")Long meterId,  @Param("date")Date date, @Param("rate")int rate,@Param( "instead" )int instead);
	
	/**
	 * 查询正向有功总电能示值(用于显示示值)
	 * @param meterId
	 * @param date
	 * @return
	 */
	Map<String,Object> queryTotalFaeRate(@Param("meterId")Long meterId,  @Param("date")Date date,@Param( "instead" )int instead);
	
	/**
	 * 查询正向有功总电能示值(用于计算电量)
	 * @param meterId
	 * @param date
	 * @return
	 */
	Double getViewDayETotal(@Param("meterId")Long meterId,  @Param("date")Date date,@Param( "instead" )int instead);
	
	/**
	 * 查询水能示值
	 * @param meterId
	 * @param date
	 * @return
	 */
	Map<String,Object> queryEWater(@Param("meterId")Long meterId,  @Param("date")Date date,@Param( "instead" )int instead);
	
	/**
	 * 查询气能示值
	 * @param meterId
	 * @param date
	 * @return
	 */
	Map<String,Object> queryEGas(@Param("meterId")Long meterId,  @Param("date")Date date,@Param( "instead" )int instead);
	
	/**
	 * 查询热能示值
	 * @param meterId
	 * @param date
	 * @return
	 */
	Map<String,Object> queryEHeat(@Param("meterId")Long meterId,  @Param("date")Date date,@Param( "instead" )int instead);
	
	/**
	 * 得到父节点层级
	 * @param ledgerId
	 * @return
	 */
	Integer queryLevel(@Param( "ledgerId" ) Long ledgerId,@Param( "parLedgerId" ) Long parLedgerId);
	
	/**
	 * 得到父节点名称字符串(多个),以'>'符号拼接
	 * @param parLedgerId	点选节点的能管对象id
	 * @param ledgerId		最底层能管对象id
	 * @return
	 */
	String queryParName(@Param( "parLedgerId" ) Long parLedgerId,@Param( "ledgerId" )Long ledgerId);
	
	/**
	 * 查询缴费状态
	 * @param ledgerId	能管对象id
	 * @param beginTime	开始时间
	 * @param endTime	结束时间
	 * @return
	 */
	Map<String,Object> queryPayCost(@Param( "ledgerId" )long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 存储缴费状态
	 * @param ledgerId 能管对象id
	 * @param dateTime 结算时间(获取服务器时间)
	 * @return
	 */
	int doPayCost(@Param( "ledgerId" )long ledgerId,@Param( "dateTime" )Date dateTime);
	
	Map<String,Object> queryParentRateId (@Param( "ledgerId" )long ledgerId);
	//end
	
	/**
	 * 查询能管对象结算日相关信息
	 * @author catkins.
	 * @param ledgerId
	 * @return java.util.Map<java.lang.String,java.lang.Integer>
	 * @exception
	 * @date 2019/11/6 16:48
	 */
	public Map<String,Object> queryCalcData(Long ledgerId);
	
}
