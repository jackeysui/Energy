package com.linyang.energy.mapping.classAnalysis;

import com.linyang.energy.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16-8-1.
 */
public interface ClassMapper {

    /**
     * 得到下拉de企业列表
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getCompLedgerList(@Param("ledgerId")Long ledgerId);

    /**
     * 根据ledgerId得到班制列表
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getLedgerClasses(@Param("ledgerId")Long ledgerId);
    
    /**
     * 得到WorkshopRelationBean列表
     * @param workshopId
     * @return
     */
    List<WorkshopRelationBean> getWorkshopRelationById(@Param("workshopId")Long workshopId );

    /**
     * 根据班制ID获取下面的班组列表
     * @param classId
     * @return
     */
    List<Map<String, Object>> getTeamsByClassId(@Param("classId")Long classId);

    List<Map<String, Object>> getTeamsByClassIdNew(@Param("classId")Long classId);
    
    /**
     * 根据班制ID获取下面的班组列表
     * @param classId
     * @return
     */
    List<WorkingHourBean> getWorkingHoursByClassId(@Param("classId")Long classId);

    List<WorkingHourBean> getWorkingHoursByTeamId(@Param("teamId")Long teamId);

    /**
     * 根据班制ID获取teamId列表
     * @param classId
     * @return
     */
    List<Long> getTeamIdByClassId(@Param("classId")Long classId, @Param("teamName")String teamName);

    /**
     * 插入t_class_config
     * @param classConfig
     * @return
     */
    void insertClassConfig(Map<String, Object> classConfig);

    /**
     * 更新t_class_config
     * @param classConfig
     * @return
     */
    void updateClassConfig(Map<String, Object> classConfig);

    /**
     * 删除t_class_config
     * @param
     * @return
     */
    void deleteClassConfig(@Param("classId")Long classId);

    /**
     * 删除T_WORKINGHOUR_RELATION
     * @param
     * @return
     */
    void deleteWorkingHourRelation(@Param("classId")Long classId);

    /**
     * 删除t_product_output
     * @param
     * @return
     */
    void deleteProductOutput(@Param("classId")Long classId);

    void deleteProductOutputByShopId(@Param("workShopId")Long workShopId);

    /**
     * 删除T_TEAM_CONFIG
     * @param
     * @return
     */
    void deleteTeamConfig(@Param("classId")Long classId);

    /**
     * 删除T_WORKSHOP_RELATION
     * @param
     * @return
     */
    void deleteWorkShopRelation(@Param("classId")Long classId);

    void deleteWorkShopRelationByShopId(@Param("workShopId")Long workShopId);

    /**
     * 删除T_WORKSHOP_METER
     * @param
     * @return
     */
    void deleteWorkShopMeter(@Param("classId")Long classId);

    void deleteWorkShopMeterByShopId(@Param("workShopId")Long workShopId);

    /**
     * 删除T_WORKSHOP_CONFIG
     * @param
     * @return
     */
    void deleteWorkshopConfig(@Param("classId")Long classId);

    void deleteWorkshopByShopId(@Param("workShopId")Long workShopId);

    /**
     * 插入T_TEAM_CONFIG
     * @param
     * @return
     */
    void insertTeamConfig(@Param("teamId")Long teamId, @Param("classId")Long classId, @Param("teamName")String teamName);

    /**
     * 插入T_WORKINGHOUR_RELATION
     * @param
     * @return
     */
    void insertWorkHourRelation(@Param("workHourId")Long workHourId, @Param("teamId")Long teamId,
                                @Param("onDutyWeek")Integer onDutyWeek, @Param("offDutyWeek")Integer offDutyWeek,
                                @Param("onDuty")Date onDuty, @Param("offDuty")Date offDuty);
    /**
     * 根据主键得到班制信息
     * @param classId
     * @return
     */
    ClassConfigBean getClassConfigById(@Param("classId")Long classId);

    ClassConfigBean getClassConfigByTeamId(@Param("teamId")Long teamId);

    /**
     * 根据ledgerId获取生产单元配置列表
     * @param
     * @return
     */
    List<Map<String, Object>> getWorkshopPageConfig(Map<String,Object> map);

    /**
     * 根据workShopId获取生产单元配置
     * @param
     * @return
     */
    Map<String, Object> getWorkShopById(@Param("workShopId")Long workShopId);

    /**
     * 根据workShopId获取相关的EMO、DCP
     * @param
     * @return
     */
    List<Map<String, Object>> getRelationsByWorkShop(@Param("workShopId")Long workShopId);

    /**
     * 插入T_WORKSHOP_CONFIG
     * @param
     * @return
     */
    void insertWorkshopConfig(@Param("workShopId")Long workShopId,@Param("workShopName")String workShopName,@Param("ledgerId")Long ledgerId,@Param("classId")Long classId);

    /**
     * 更新T_WORKSHOP_CONFIG
     * @param
     * @return
     */
    void updateWorkshopConfig(@Param("workShopId")Long workShopId,@Param("workShopName")String workShopName,@Param("ledgerId")Long ledgerId,@Param("classId")Long classId);

    /**
     * 插入T_WORKSHOP_RELATION
     * @param
     * @return
     */
    void insertWorkshopRelation(@Param("workShopId")Long workShopId,@Param("type")Integer type,@Param("id")Long id);

    /**
     * 插入T_WORKSHOP_METER
     * @param
     * @return
     */
    void insertWorkshopMeterDcp(@Param("workShopId")Long workShopId, @Param("meterId")Long meterId);

    void insertWorkshopMeterEmo(@Param("workShopId")Long workShopId, @Param("ledgerId")Long ledgerId);

    /**
     * 根据ledgerId得到产品配置列表
     * @param param
     * @return
     */
    List<ProductConfigBean> queryProductConfigPageList(Map<String, Object> param);
    
    /**
     * 根据productId得到产品配置信息
     * @param productId
     * @return
     */
    Map<String, Object> getProductConfigById(@Param("productId")Long productId);
    
    /**
     * 先检查产品名是否重复
     * @param ledgerId
     * @param productName
     * @return
     */
    Integer getProductConfigNum(@Param("ledgerId")Long ledgerId,@Param("productName")String productName);
    
    /**
     * 先检查否存在基准产品
     * @return
     */
    Integer getStandardProduct(@Param("ledgerId")Long ledgerId);
    
    /**
     * 插入T_PRODUCT_CONFIG
     * @param
     * @return
     */
    void insertProductConfig(@Param("ledgerId")Long ledgerId,@Param("productId")Long productId,@Param("productName")String productName,@Param("productUnit")String productUnit,@Param("convertRatio")Integer convertRatio,@Param("unitConsumption")Double unitConsumption,@Param("isStandard")Integer isStandard);

    /**
     * 更新T_PRODUCT_CONFIG
     * @param
     * @return
     */
    void updateProductConfig(@Param("productId")Long productId,@Param("productName")String productName,@Param("productUnit")String productUnit,@Param("convertRatio")Integer convertRatio,@Param("unitConsumption")Double unitConsumption);
    
    /**
     * 删除前检查是否为基准产品
     * @param productId
     * @return
     */
    Integer getStandardById(@Param("productId")Long productId);
    
    /**
     * 删除T_PRODUCT_CONFIG
     * @param
     * @return
     */
    void deleteProductConfig(@Param("productId")Long productId);

    /**
     * 根据条件得到产量列表
     * @param param
     * @return
     */
    List<ProductOutputBean> queryProductOutputPageList(Map<String, Object> param);
    
    /**
     * 根据ledgerId获取产品列表
     * @param
     * @return
     */
    List<Map<String, Object>> getProductListByLedgerAll(@Param("ledgerId")Long ledgerId);

    List<Map<String, Object>> getProductListByLedger(@Param("ledgerId")Long ledgerId, @Param("classId")Long classId, @Param("shopIdList")List<Long> shopIdList);
    
    /**
     * 根据productId获取车间列表
     * @param
     * @return
     */
    List<Map<String, Object>> getWorkshopListByProductId(@Param("productId")Long productId);
    
    /**
     * 根据productId,workshopId获取班组列表
     * @param
     * @return
     */
    List<Map<String, Object>> getTeamListByProductId(@Param("productId")Long productId,@Param("workshopId")Long workshopId);
    
    /**
     * 根据ledgerId获取车间列表
     * @param
     * @return
     */
    List<Map<String, Object>> getWorkshopListByLedgerId(@Param("ledgerId")Long ledgerId);
    
    /**
     * 根据workshopId获取班组列表
     * @param
     * @return
     */
    List<Map<String, Object>> getTeamListByWorkshopId(@Param("workshopId")Long workshopId);
    
    /**
     * 根据outputId获取产量信息
     * @param outputId
     * @return
     */
    Map<String, Object> getProductOutputById(@Param("outputId")Long outputId);
    
    /**
     * 根据productId获取单位
     * @param
     * @return
     */
    String getProductUnitByProductId(@Param("productId")Long productId);
    
    /**
     * 插入T_PRODUCT_OUTPUT
     * @param
     * @return
     */
    void insertProductOutput(@Param("outputId")Long outputId,@Param("ledgerId")Long ledgerId,@Param("productId")Long productId,@Param("workshopId")Long workshopId,@Param("teamId")Long teamId,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("productOutput")Double productOutput);
    
    /**
     * 更新T_PRODUCT_OUTPUT
     * @param outputId
     * @param productId
     * @param workshopId
     * @param teamId
     * @param startTime
     * @param endTime
     * @param productOutput
     */
    void updateProductOutput(@Param("outputId")Long outputId,@Param("productId")Long productId,@Param("workshopId")Long workshopId,@Param("teamId")Long teamId,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("productOutput")Double productOutput);
    
    /**
     * 根据outputId删除T_PRODUCT_OUTPUT
     * @param
     * @return
     */
    void deleteProductOutputById(@Param("outputId")Long outputId);
    
    
    
    /**
     * 新增前，先检查班制名是否重复
     * @param
     * @return
     */
    Integer getClassNumByLedgerAndName(@Param("ledgerId")Long ledgerId,@Param("classId")Long classId,@Param("className")String className);
    
    /**
     * 根据分户ID,车间Id获得产品列表
     * @param param
     * @return
     */
    List<Map<String, Object>> getProductByWorkshopId(Map<String, Object> param);
    
    /**
     * 根据ClassId获取车间列表
     * @param classId
     * @return
     */
    List<Map<String, Object>> getWorkshopListByClassId(@Param("classId")Long classId);

    /**
     * 查询班组耗能数据分析
     * @param queryParam
     */
	Double queryClassEnergyData(Map<String, Object> queryParam);

    /**
     * 先检查车间/单位名是否重复
     * @param
     * @return
     */
    Integer getWorkshopNumBy(@Param("workShopId")Long workShopId,@Param("ledgerId")Long ledgerId,@Param("workShopName")String workShopName);

    /**
     * 查看某一时间段具体的产量情况
     * @param productParam
     * @return
     */
	Map<String,Object> getProductDetail(Map<String, Object> productParam);
	
	/**
	 * 根据主键得到产品对象
	 * @param productId
	 */
	ProductConfigBean queryProductConfigById(@Param("productId")Long productId);

	/**
	 * 查看这些车间下的所有产品
	 * @param workshopIds
	 * @return
	 */
	List<ProductConfigBean> queryProductConfigByWorkshopIds(@Param("workshopIds")List<Long> workshopIds);

	/**
	 * 得到车间所属的测量点
	 * @param workshopId
	 * @return
	 */
	List<WorkshopMeterBean> getWorkshopMetersByWorkshopId(@Param("workshopId")Long workshopId);

    /**
     * 根据ledgerId得到生产单元ID
     * @param ledgerId
     * @return
     */
    List<Long> getWorkshopsByLedgerId(@Param("ledgerId")Long ledgerId);
    
    /**
     * 获取EMO下的EMO的Id拼成的String
     * @return
     */
    List<String> getEmoStrByLedger(@Param("ledgerId")Long ledgerId);

    /**
     * 获取EMO下的DCP的Id拼成的String
     * @return
     */
    List<String> getDcpStrByLedger(@Param("ledgerId")Long ledgerId);

    /**
     * 根据teamId得到是“周”还是“天”
     * @return
     */
    Integer getCycleByTeamId(@Param("teamId")Long teamId);

    /**
     * 根据teamId得到班组时段们
     * @return
     */
    List<Map<String, Object>> getTeamTimeByTeamId(@Param("teamId")Long teamId);

    /**
     * 删除T_PRODUCT_DETAIL
     * @param
     * @return
     */
    void deleteProductDetail(@Param("outputId")Long outputId);

    /**
     * 插入T_PRODUCT_DETAIL
     * @param
     * @return
     */
    void insertProductDetail(@Param("productId")Long productId,@Param("workshopId")Long workshopId,@Param("teamId")Long teamId,
                             @Param("outputId")Long outputId,@Param("begin")Date begin,@Param("end")Date end,
                             @Param("outPut")Double outPut);

    /**
     * 根据workShopId、meterId查询workShopMeter个数
     */
    int getWorkshopMeterNumByMeterId(@Param("workShopId")Long workShopId, @Param("meterId")Long meterId);

    /**
     * 根据classId获取班组列表
     * @param
     * @return
     */
    List<Map<String, Object>> getTeamListByClassId(@Param("classId")Long classId);

    /**
     * 根据classId获取基础时段列表
     * @param
     * @return
     */
    List<Map<String, Object>> getBasetimeListByClassId(@Param("classId")Long classId);

    /**
     * 删除T_CLASS_BASE_TIME
     * @param
     * @return
     */
    void deleteClassBaseTime(@Param("classId")Long classId);

    /**
     * 删除班制休息日相关表
     * @param
     * @return
     */
    void deleteClassRestDay(@Param("classId")Long classId);
    void deleteClassCustomHoliday(@Param("classId")Long classId);
    void deleteClassWorkdayRest(@Param("classId")Long classId);

    /**
     * 插入T_CLASS_BASE_TIME
     * @param
     * @return
     */
    void insertBaseTimeConfig(@Param("timeId")Long timeId, @Param("classId")Long classId, @Param("beginDay")Integer beginDay,
                              @Param("beginTime")Date beginTime, @Param("endDay")Integer endDay, @Param("endTime")Date endTime);

    /**
     *  班制de休息日是否包含法定节假日
     */
    int includeDefaultHoliday(@Param("classId") Long classId);

    /**
     *  班制de判断是否自定义节假日
     */
    int judgeHoliday(@Param("classId") Long classId, @Param("beginTime") Date beginTime);

    /**
     * 班制de判断是否休息日
     */
    Integer judgeRestDay(@Param("classId") Long classId, @Param("weekNumber") int weekNumber);

    /**
     * 根据teamId获取"自定义班组"工作时段列表
     */
    List<Map<String, Object>> getWorkHoursByTeam(@Param("teamId")Long teamId);

    /**
     * 按条件获取时段们
     */
    List<WorkingHourBean> getTeamClassLastInterval(@Param("classId")Long classId, @Param("teamLast") Date teamLast);

    List<WorkingHourBean> getTeamClassBeforeInterval(@Param("classId")Long classId, @Param("teamLast") Date teamLast);

}
