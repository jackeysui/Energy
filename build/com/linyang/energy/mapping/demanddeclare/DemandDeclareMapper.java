package com.linyang.energy.mapping.demanddeclare;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.DemandRecBean;

/**
 * @author guosen
 * @date 2014-12-22
 * @Modified By DINGYANG
 * @Description 需量申报
 * @Version 6254
 */
public interface DemandDeclareMapper {
	
	/**
	 * 保存BO
	 * @param demandRecBean
	 */
	public void insert(DemandRecBean demandRecBean);

	/**
	 * 根据主键得到BO对象
	 * @param recId
	 * @return
	 */
	public DemandRecBean getBeanById(Long recId);

	/**
	 * 更新
	 * @param demandRecBean
	 */
	public void update(DemandRecBean demandRecBean);

	/**
	 * 得到申报记录列表
	 * @param mapQuery
	 * @return
	 */
	public List<Map<String, Object>> getDecalrePageData(Map<String, Object> mapQuery);

	/**
	 * 删除
	 * @param declareTime
	 */
	public int deleteBoByDeclareTime(Date declareTime);
	
	/**
	 * 删除
	 * @param demandRecBean
	 */
	public void deleteBoByTimeAndMeterId(DemandRecBean demandRecBean);

	/**
	 * 根据申报月份和专变ID判断是否重复
	 * @param demandRecBean
	 * @return
	 */
	public Integer getBeanByTimeAndMeterId(DemandRecBean demandRecBean);

	/**
	 * 得到申报值
	 * @param meterId
	 * @param beginTime
	 * @return
	 */
	public Map<String, Object> getDeclareValue(@Param("meterId") Long meterId,@Param("treeType") Integer treeType, @Param("beginTime") Date beginTime);

	/**
	 * 得到emo申报列表
	 * @param meterId
	 * @param beginTime
	 * @return
	 */
	public Map<String, Object> getEmoDecalreData(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime);
	
	/**
	 * 得到emo申报记录列表
	 * @param mapQuery
	 * @return
	 */
	public List<Map<String, Object>> getEmoDecalrePageData(Map<String, Object> mapQuery);
    
    /**
	 * 得到emo申报类型
     * @param map（ledgerId，date）
	 * @return
	 */
    public Integer getEmoDecalreType(Map<String, Object> map);
    
    /**
     * 获取采集点数量
     * @param ledgerId
     * @return
     */
    Integer getPointNum(long ledgerId);

    /**
     * 根据ledgerid查询meterid
     * @param ledgerId
     * @return
     */
    Long getMeterIdByLedgerId(long ledgerId);
    
    
    /**
     * 根据meterId查询上月(根据数据库服务器时间)日冻结最大值
     * @param meterId
     * @return
     */
    Integer getmaxFaqValue(long meterId);
    
    /**
     * 根据时间和id查询企业申报值
     * @param page
     * @param mapQuery
     * @return
     */
	Map<String, Object> getEmoDecalreByIdAndBeginTime(Map<String, Object> map);
	
	/**
	 * 获取上个月的需量记录,如果没有就查询最后一次申报记录
	 * @param
	 * @return
	 */
	Map<String, Object> getLsatDeclare(@Param("ledgerId") Long ledgerId, @Param("beginTime") Date beginTime,@Param("flag")Integer flag);

	/**
	 * 获取每个月最后一条申报记录
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getEveryLsatDeclare(Map<String, Object> map);
}
