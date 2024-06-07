package com.linyang.energy.service;

import com.linyang.energy.model.IndustryBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 20-5-12.
 */
public interface YunNanService {


    /**
     * 平台接入管理”页面 初始化页面数据
     */
    public Map<String,Object> joinManageData(Long entId);

    /**
     * 获取“单位类型”下拉列表
     */
    public Map<String,Object> getBaseDataEntType(String regionId);

    /**
     *得到行业树信息
     */
    public List<IndustryBean> queryIndustryInfo(IndustryBean industryBean);

    /**
     * 保存 企业"统一社会信用代码"
     */
    public void insertUpdateCompany(Map<String, Object> companyInfo);

    /**
     * 申请工作密钥
     */
    public Map<String,Object> applySk(Map<String, Object> companyInfo);

    /**
     * 企业信息注册--申请
     */
    public Map<String,Object> applyCompany(Map<String, Object> companyInfo, String filePath);

    /**
     * 重置
     */
    public Map<String,Object> resetRegist(Long entId);

    /**
     * 获取AK--申请
     */
    public Map<String,Object> applyAK(Long entId);

    /**
     * 保存 AK
     */
    public void insertUpdatePlatAk(Map<String, Object> platAk);

    /**
     * 平台版本校验
     */
    public Map<String,Object> applyCheckVersion(Long entId);

    /**
     * 保存 平台版本校验
     */
    public void insertUpdatePlatVersion(Map<String, Object> platVersion);

    /**
     * 平台基础配置地址 查询
     */
    public Map<String,Object> applyPlatUrls(Long entId);

    /**
     * 保存 平台基础配置地址
     */
    public void insertUpdatePlatUrls(Map<String, Object> platUrl);

    /**
     * 平台基础数据下载
     */
    public Map<String,Object> downloadBaseData(Long entId, Integer itemIndex);


    /**
     * 机构能源消费”页面--初始化页面数据
     */
    public Map<String,Object> instituteEnergyData(Long entId);

    /**
     *  “机构能源消费”页面--载入历史数据
     */
    public Map<String,Object> instituteEnergyCopy(Long entId, String date);

    /**
     * “机构能源消费”页面--手工上传
     */
    public Map<String,Object> reportInstituteEnergy(Map<String, Object> resrContInfo);

    /**
     * 保存“机构能源消费”
     */
    public void insertUpdateResrContInfo(Map<String, Object> resrContInfo);


    /**
     * “机构机房消费”页面--初始化页面数据
     */
    public Map<String,Object> mechanismRoomData(Long entId);

    /**
     *  “机构机房消费”页面--载入历史数据
     */
    public Map<String,Object> mechanismRoomCopy(Long entId, String date);

    /**
     * “机构机房消费”页面--手工上传
     */
    public Map<String,Object> reportMechanismRoom(Map<String, Object> contInfo);

    /**
     * 保存“机构机房消费”
     */
    public void insertUpdateMechanismRoom(Map<String, Object> contInfo);


    /**
     * “机构采暖消费”页面--初始化页面数据
     */
    public Map<String,Object> mechanismHeatingData(Long entId);

    /**
     *  “机构采暖消费”页面--载入历史数据
     */
    public Map<String,Object> mechanismHeatingCopy(Long entId, String date);

    /**
     * “机构采暖消费”页面--手工上传
     */
    public Map<String,Object> reportMechanismHeating(Map<String, Object> contInfo);

    /**
     * 保存“机构采暖消费”
     */
    public void insertUpdateMechanismHeating(Map<String, Object> contInfo);


    /**
     * “数据填报上传”页面--初始化页面数据
     */
    public Map<String,Object> getCompanyCollectionConfig(Long entId);

    public Map<String,Object> getOneCompanyCollectionConfig(Long entId, Long dataId);

    /**
           * 数据填报上传
     */
    public Map<String,Object> reportDataFillUpload(Map<String, Object> contInfo);
    
    /**
            * 数据填报上传
    */
    public Map<String,Object> reUploadData(Map<String, Object> contInfo);

}
