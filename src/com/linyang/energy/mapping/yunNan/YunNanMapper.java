package com.linyang.energy.mapping.yunNan;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 20-5-12.
 */
public interface YunNanMapper {

    /**
     * 检查“统一社会信用代码”是否已存在
     */
    public Integer getCompanyNumBy(@Param("entId")Long entId);

    /**
     * 插入 T_ENT_INFO
     */
    public void insertCompanyInfo(Map<String, Object> companyInfo);

    /**
     * 更新 T_ENT_INFO
     */
    public void updateCompanyInfo(Map<String, Object> companyInfo);

    /**
     * 用主键获取 T_ENT_INFO
     */
    public Map<String, Object> getCompanyInfoById(@Param("entId")Long entId);

    /**
     * 重置
     */
    public void resetRegist(@Param("entId")Long entId);

    /**
     * 插入 T_PLAT_AK
     */
    public void insertPlatAk(Map<String, Object> platAk);

    /**
     * 更新 T_PLAT_AK
     */
    public void updatePlatAk(Map<String, Object> platAk);

    /**
     * 用主键获取 T_PLAT_AK
     */
    public Map<String, Object> getPlatAkById(@Param("entId")Long entId);

    /**
     * 插入 T_PLAT_VERSION
     */
    public void insertPlatVersion(Map<String, Object> platVersion);

    /**
     * 更新 T_PLAT_VERSION
     */
    public void updatePlatVersion(Map<String, Object> platVersion);

    /**
     * 用主键获取 T_PLAT_VERSION
     */
    public Map<String, Object> getPlatVersionById(@Param("entId")Long entId);

    /**
     * 插入 T_PLAT_BASEURL
     */
    public void insertPlatUrls(Map<String, Object> platUrl);

    /**
     * 更新 T_PLAT_BASEURL
     */
    public void updatePlatUrls(Map<String, Object> platUrl);

    /**
     * 用主键获取 T_PLAT_BASEURL
     */
    public Map<String, Object> getPlatUrlsById(@Param("entId")Long entId);

    /**
     * 获取“单位类型”下拉列表
     */
    public List<Map<String, Object>> getEntTypeList(@Param("regionId")String regionId);


    /**
     * ------------------------------------基础数据下载 begin ------------------------------------------------------------------
     */

    /** 企业类型编码 */
    public void deleteTableEntType();
    public void insertEntType(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /**
     * 清除T_PROD_PCODE表
     */
    public void deleteTableProdpCode();
    /**
     * 插入T_PROD_PCODE表
     */
    public void insertProdpCode(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /**
     * 清除T_PROD_PUNITCODE表
     */
    public void deleteTableProdpUnitCode();
    /**
     * 插入T_PROD_PUNITCODE表
     */
    public void insertProdpUnitCode(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /**
     * 清除T_ENEQP表
     */
    public void deleteTableEntEquipment();
    /**
     * 插入T_ENEQP表
     */
    public void insertEntEquipment(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /** 数据采集来源类型编码 */
    public void deleteTableDataSrc();
    public void insertDataSrc(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /** 采集数据类型编码 */
    public void deleteTableDataType();
    public void insertDataType(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /** 能源品种编码计量单位及精度 */
    public void deleteTableEnType();
    public void deleteTableEnConvert();
    public void insertEnType(@Param("code")String code, @Param("name")String name, @Param("unit")String unit, @Param("precision")String precision, @Param("region")String region);
    public void insertEnConvert(@Param("code")String code, @Param("name")String name, @Param("ratio")String ratio, @Param("ratioUnit")String ratioUnit, @Param("region")String region);

    /** 行政区域编码（云南） */
    public void deleteRegionType();
    public void insetRegionType(@Param("code")String code, @Param("name")String name, @Param("level")Integer level, @Param("parent")String parent);

    /** 行业编码 */
    public void deleteIndustryType();
    public void insetIndustryType(@Param("code")String code, @Param("name")String name, @Param("level")Integer level, @Param("parent")String parent);

    /** 耗能工质编码计量单位及精度 */
    public void deleteTableWmed();
    public void insertTableWmed(@Param("code")String code, @Param("name")String name, @Param("qualityUnit")String qualityUnit, @Param("qualityPrecision")String qualityPrecision,
                                @Param("collectUnit")String collectUnit, @Param("collectPrecision")String collectPrecision, @Param("region")String region);

    /** 能效指标编码及计量单位 */
    public void deleteTableEei();
    public void insertTableEei(@Param("code")String code, @Param("name")String name, @Param("industry")String industry, @Param("targetType")String targetType,
                                @Param("unitCh")String unitCh, @Param("unitEn")String unitEn, @Param("region")String region);

    /** 经营指标编码及计量单位 */
    public void deleteTableKpi();
    public void insertTableKpi(@Param("code")String code, @Param("name")String name, @Param("unit")String unit, @Param("precisions")String precisions, @Param("region")String region);

    /** 其他数据编码 */
    public void deleteTableDataCodeOther();
    public void insertDataCodeOther(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /** 能源用途编码 */
    public void deleteTableDataCodeEnUse();
    public void insertDataCodeEnUse(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /** 重点用能设备分类编码 */
    public void deleteTableEneqpType();
    public void insertEneqpType(@Param("code")String code, @Param("name")String name, @Param("region")String region);

    /**
     * ------------------------------------基础数据下载 end ------------------------------------------------------------------
     */


    /**
     * 根据企业ID获取企业已经上报的“机构能源消费”列表
     */
    public List<String> getInstituteEnergyDates(@Param("entId")Long entId);

    public Map<String, Object> getResrContInfoBy(@Param("entId")Long entId, @Param("date")String date);

    /**
     * 检查“机构能源消费”是否已存在
     */
    public Integer getResrContInfoNumBy(@Param("entId")Long entId, @Param("reportDate")String reportDate);

    /**
     * 插入 “机构能源消费”
     */
    public void insertResrContInfo(Map<String, Object> resrContInfo);

    /**
     * 更新 “机构能源消费”
     */
    public void updateResrContInfo(Map<String, Object> resrContInfo);


    /**
     * 根据企业ID获取企业已经上报的“机构机房消费”列表
     */
    public List<String> getMechanismRoomDataDates(@Param("entId")Long entId);

    public Map<String, Object> getMechanismRoomBy(@Param("entId")Long entId, @Param("date")String date);

    /**
     * 检查“机构机房消费”是否已存在
     */
    public Integer getMechanismRoomNumBy(@Param("entId")Long entId, @Param("reportDate")String reportDate);

    /**
     * 插入 “机构机房消费”
     */
    public void insertMechanismRoom(Map<String, Object> contInfo);

    /**
     * 更新 “机构机房消费”
     */
    public void updateMechanismRoom(Map<String, Object> contInfo);


    /**
     * 根据企业ID获取企业已经上报的“机构采暖消费”列表
     */
    public List<String> getMechanismHeatingDataDates(@Param("entId")Long entId);

    public Map<String, Object> getMechanismHeatingBy(@Param("entId")Long entId, @Param("date")String date);

    /**
     * 检查“机构采暖消费”是否已存在
     */
    public Integer getMechanismHeatingNumBy(@Param("entId")Long entId, @Param("reportDate")String reportDate);

    /**
     * 插入 “机构采暖消费”
     */
    public void insertMechanismHeating(Map<String, Object> contInfo);

    /**
     * 更新 “机构采暖消费”
     */
    public void updateMechanismHeating(Map<String, Object> contInfo);


    /**
     * 根据企业ID获取企业的“采集项配置”列表
     */
    public List<Map<String, Object>> getCompanyCollectionConfig(@Param("entId")Long entId);

    public Map<String, Object> getOneCollectionConfig(@Param("entId")Long entId, @Param("dataId")Long dataId, @Param("region")String region);

}
