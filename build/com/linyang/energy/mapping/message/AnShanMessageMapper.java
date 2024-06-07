package com.linyang.energy.mapping.message;/*
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  - /// |    |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.'  >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 *
 *
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 *            佛祖保佑     永不宕机     永无BUG
 */

import com.linyang.energy.model.ServiceReportBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 15:18 2020/11/27
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface AnShanMessageMapper {
	
	/**
	 * 查询上月是否生成了报告
	 * @param reportModule
	 * @return
	 */
	int serviceReportIsCreate( @Param("reportModule") Long reportModule );
	
	/**
	 * 保存报告
	 * @param reportId
	 * @param ledgerId
	 * @param reportTime
	 * @param reportModule
	 * @param content
	 * @param createTime
	 * @param status
	 * @return
	 */
	int insertReportInfo(@Param("reportId") long reportId, @Param("ledgerId") long ledgerId,
								@Param("reportTime") Date reportTime, @Param("reportModule") long reportModule, @Param("content") String content, @Param("date")Date createTime, @Param("status") int status);
	
	/**
	 * 查询服务报告
	 * @param param
	 * @return
	 */
	List<ServiceReportBean> searchSerivceReportPageList(Map<String, Object> param);
	
	/**
	 * 根据ID得到服务报告
	 * @param reportId
	 * @return
	 */
	ServiceReportBean queryReportInfoById(@Param( "reportId" )long reportId);
	
	/**
	 * 查询企业下的所有测量点
	 * @param ledgerId
	 * @return
	 */
	List<Map<String,Object>> queryLedgerMeter(@Param("ledgerId")long ledgerId);
	
	/**
	 * 根据测量点ID查询时间段内的负载率曲线
	 * @param meterId
	 * @return
	 */
	List<Map<String,Object>> queryLFbyMeterId(@Param( "meterId" )long meterId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 得到最小负载率单元
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String,Object>> getMinLoadByLedgerId(@Param("ledgerId")Long ledgerId, @Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	/**
	 * 查询日电流,电压谐波
	 * @param curveType	1电流谐波  2电压谐波
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String,Object>> getDayHarChartDatas(@Param( "curveType" )int curveType , @Param( "meterId" )long meterId,@Param("beginTime")Date beginTime, @Param("endTime")Date endTime);
	
	
	/**
	 * 查询负载率分析曲线
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String,Object>> getLoadData(@Param( "meterId" )long meterId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 查询负载率时间曲线
	 * @param meterId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String,Object>> getLoadTimeData(@Param( "meterId" )long meterId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	/**
	 * 根据meterId查询meterName
	 * @param meterId
	 * @return
	 */
	String getMeterName(@Param( "meterId" )long meterId);
	
	/**
	 * 查询企业总有功功率
	 * @param ledgerId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	List<Map<String,Object>> queryPowerDatas(@Param( "ledgerId" )long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	List<Map<String,Object>> queryPowerLineDatas(@Param( "ledgerId" )long ledgerId,@Param( "beginTime" )Date beginTime,@Param( "endTime" )Date endTime);
	
	
	List<Map<String,Object>> queryEnergyData4Parent(@Param( "ledgerId" )Long ledgerId,@Param( "beginTime" ) Date beginTime,@Param( "endTime" ) Date endTime,@Param( "showType" )Integer showType);
}
