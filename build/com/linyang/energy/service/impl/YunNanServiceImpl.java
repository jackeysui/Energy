package com.linyang.energy.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leegern.util.CollectionUtil;
import com.leegern.util.StringUtil;
import com.linyang.energy.mapping.authmanager.IndustryBeanMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.model.IndustryBean;
import com.linyang.energy.model.InnerBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.service.YunNanService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import com.linyang.energy.utils.yunNanUtil.JsonUtil;
import com.linyang.energy.utils.yunNanUtil.SM4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 20-5-12.
 */
@Service
public class YunNanServiceImpl implements YunNanService {

    @Autowired
    private YunNanMapper yunNanMapper;

    @Autowired
    private LedgerManagerMapper ledgerManagerMapper;

    @Autowired
    private MeterManagerMapper meterManagerMapper;

    @Autowired
    private IndustryBeanMapper industryBeanMapper;

    @Override
    public Map<String,Object> joinManageData(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();

        LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(entId);
        int ledgerFlag = 0;
        if(ledgerBean.getAnalyType() == 102){
            ledgerFlag = 1;
            result.put("entInfo", this.yunNanMapper.getCompanyInfoById(entId));
            result.put("platAk", this.yunNanMapper.getPlatAkById(entId));
            result.put("platVersion", this.yunNanMapper.getPlatVersionById(entId));
            result.put("platUrl", this.yunNanMapper.getPlatUrlsById(entId));
        }
        result.put("ledgerFlag", ledgerFlag);

        return result;
    }

    @Override
    public Map<String,Object> getBaseDataEntType(String regionId){
        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> entTypeList = this.yunNanMapper.getEntTypeList(regionId);
        result.put("entTypeList", entTypeList);

        return result;
    }

    @Override
    public List<IndustryBean> queryIndustryInfo(IndustryBean industryBean) {
        return industryBeanMapper.queryIndustryTypeInfo(industryBean);
    }

    @Override
    public void insertUpdateCompany(Map<String, Object> companyInfo){
        Long entId = Long.valueOf(companyInfo.get("entId").toString());

        //检查“统一社会信用代码”是否已存在
        int num = this.yunNanMapper.getCompanyNumBy(entId);
        if(num > 0){
            //修改
            this.yunNanMapper.updateCompanyInfo(companyInfo);
        }
        else {
            //新增
            this.yunNanMapper.insertCompanyInfo(companyInfo);
        }
    }

    @Override
    public Map<String,Object> applySk(Map<String, Object> companyInfo){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "";

        Long entId = Long.valueOf(companyInfo.get("entId").toString());
        String sk = companyInfo.get("sk").toString();
        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        if(null != ent && ent.keySet().contains("entCode")){
            String entCode = ent.get("entCode").toString();
            //上报到云南平台
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("enterpriseCode", entCode);
            if(StringUtil.isNotEmpty(sk)){
                params.put("secretKey", sk);
            }
            ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/enterpriseInfo/rwk", params, null);
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    isSuccess = true;
                    message = "上报云南省平台成功";
                    //入库
                    String secretKey = objectNode.get("data").get("secretKey").toString().replaceAll("\"","");
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("entId", entId);
                    temp.put("sk", secretKey);
                    this.yunNanMapper.updateCompanyInfo(temp);

                    result.put("secretKey", secretKey);
                }
                else {
                    isSuccess = false;
                    message = responseCode + objectNode.get("responseMessage").toString().replaceAll("\"","");
                }
            }
        }
        else {
            isSuccess = false;
            message = "请先保存该企业的统一社会信用代码";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public Map<String,Object> resetRegist(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = true;
        String message = "重置成功";

        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        if(null != ent && ent.keySet().contains("dataIndex") && StringUtil.isNotEmpty(ent.get("dataIndex").toString())){
            this.yunNanMapper.resetRegist(entId);
        }
        else {
            isSuccess = false;
            message = "您尚未申请，无需重置";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public Map<String,Object> applyCompany(Map<String, Object> companyInfo, String filePath){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "申请失败";

        //先保存到数据库
        insertUpdateCompany(companyInfo);

        ////企业注册申请
        //先查询“记录索引号”，看是否有申请记录,若无记录则走“/YNEnergy/enterpriseInfo/apply”，若有则走“/YNEnergy/enterpriseInfo/update”
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String url = WebConstant.yunNanIpPort;

        Long entId = Long.valueOf(companyInfo.get("entId").toString());
        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        companyInfo.put("password", ent.get("password").toString());
        companyInfo.put("entCode", ent.get("entCode").toString());
        companyInfo.put("sk", ent.get("sk").toString());
        if(ent.keySet().contains("dataIndex") && StringUtil.isNotEmpty(ent.get("dataIndex").toString())){

            params.put("dataIndex", ent.get("dataIndex").toString());
            url = url + "/YNEnergy/enterpriseInfo/update";
        }
        else {
            url = url + "/YNEnergy/enterpriseInfo/apply";
        }

        if(companyInfo.keySet().contains("entCode") && StringUtil.isNotEmpty(companyInfo.get("entCode").toString())){
            params.put("enterpriseCode", companyInfo.get("entCode").toString());
        }
        if(companyInfo.keySet().contains("isOrg") && StringUtil.isNotEmpty(companyInfo.get("isOrg").toString())){
            params.put("isorg", companyInfo.get("isOrg").toString());
        }
        params.put("password", SM4Util.yunNanSm4Encode(companyInfo.get("sk").toString(), companyInfo.get("entCode").toString(), companyInfo.get("password").toString()));
        if(companyInfo.keySet().contains("entName") && StringUtil.isNotEmpty(companyInfo.get("entName").toString())){
            params.put("cmpName", companyInfo.get("entName").toString());
        }
        if(companyInfo.keySet().contains("addr") && StringUtil.isNotEmpty(companyInfo.get("addr").toString())){
            params.put("address", companyInfo.get("addr").toString());
        }
        if(companyInfo.keySet().contains("zipCode") && StringUtil.isNotEmpty(companyInfo.get("zipCode").toString())){
            params.put("zipCode", companyInfo.get("zipCode").toString());
        }
        if(companyInfo.keySet().contains("corpName") && StringUtil.isNotEmpty(companyInfo.get("corpName").toString())){
            params.put("corporationName", companyInfo.get("corpName").toString());
        }
        if(companyInfo.keySet().contains("regionCode") && StringUtil.isNotEmpty(companyInfo.get("regionCode").toString())){
            params.put("regionCode", companyInfo.get("regionCode").toString());
        }
        if(companyInfo.keySet().contains("license") && StringUtil.isNotEmpty(companyInfo.get("license").toString())){
            String licenseBase64 = HttpYunNan.convertFileToBase64(filePath + companyInfo.get("license").toString());
            params.put("license", licenseBase64);
        }
        if(companyInfo.keySet().contains("registerAddr") && StringUtil.isNotEmpty(companyInfo.get("registerAddr").toString())){
            params.put("registerAddress", companyInfo.get("registerAddr").toString());
        }
        if(companyInfo.keySet().contains("registerDate") && StringUtil.isNotEmpty(companyInfo.get("registerDate").toString())){
            params.put("registerDate", companyInfo.get("registerDate").toString());
        }
        if(companyInfo.keySet().contains("registerPrincipal") && StringUtil.isNotEmpty(companyInfo.get("registerPrincipal").toString())){
            params.put("registerPrincipal", companyInfo.get("registerPrincipal").toString());
        }
        if(companyInfo.keySet().contains("typeCode") && StringUtil.isNotEmpty(companyInfo.get("typeCode").toString())){
            params.put("typeCode", companyInfo.get("typeCode").toString());
        }
        if(companyInfo.keySet().contains("industryCode") && StringUtil.isNotEmpty(companyInfo.get("industryCode").toString())){
            params.put("industryCode", companyInfo.get("industryCode").toString());
//            params.put("industryCode", "A01");
        }
        if(companyInfo.keySet().contains("fieldCode") && StringUtil.isNotEmpty(companyInfo.get("fieldCode").toString())){
            params.put("fieldCode", companyInfo.get("fieldCode").toString());
        }
        if(companyInfo.keySet().contains("consumeLevel") && StringUtil.isNotEmpty(companyInfo.get("consumeLevel").toString())){
            params.put("energyConsumeLevel", companyInfo.get("consumeLevel").toString());
        }
        if(companyInfo.keySet().contains("isCenter") && StringUtil.isNotEmpty(companyInfo.get("isCenter").toString())){
            params.put("center", companyInfo.get("isCenter").toString());
        }
        if(companyInfo.keySet().contains("isJgzh") && StringUtil.isNotEmpty(companyInfo.get("isJgzh").toString())){
            params.put("jgzh", companyInfo.get("isJgzh").toString());
        }
        if(companyInfo.keySet().contains("lon") && StringUtil.isNotEmpty(companyInfo.get("lon").toString())){
            params.put("latitude", companyInfo.get("lon").toString());
        }
        if(companyInfo.keySet().contains("lat") && StringUtil.isNotEmpty(companyInfo.get("lat").toString())){
            params.put("longitude", companyInfo.get("lat").toString());
        }
        if(companyInfo.keySet().contains("tel") && StringUtil.isNotEmpty(companyInfo.get("tel").toString())){
            params.put("phone", companyInfo.get("tel").toString());
        }
        if(companyInfo.keySet().contains("fax") && StringUtil.isNotEmpty(companyInfo.get("fax").toString())){
            params.put("fax", companyInfo.get("fax").toString());
        }
        if(companyInfo.keySet().contains("email") && StringUtil.isNotEmpty(companyInfo.get("email").toString())){
            params.put("email", companyInfo.get("email").toString());
        }
        if(companyInfo.keySet().contains("entUrl") && StringUtil.isNotEmpty(companyInfo.get("entUrl").toString())){
            params.put("url", companyInfo.get("entUrl").toString());
        }
        if(companyInfo.keySet().contains("prodLine") && StringUtil.isNotEmpty(companyInfo.get("prodLine").toString())){
            params.put("productionLine", companyInfo.get("prodLine").toString());
        }
        if(companyInfo.keySet().contains("prodLeading") && StringUtil.isNotEmpty(companyInfo.get("prodLeading").toString())){
            params.put("leadingProduct", companyInfo.get("prodLeading").toString());
        }
        if(companyInfo.keySet().contains("groupName") && StringUtil.isNotEmpty(companyInfo.get("groupName").toString())){
            params.put("groupName", companyInfo.get("groupName").toString());
        }
        if(companyInfo.keySet().contains("groupAddr") && StringUtil.isNotEmpty(companyInfo.get("groupAddr").toString())){
            params.put("groupAddress", companyInfo.get("groupAddr").toString());
        }
        if(companyInfo.keySet().contains("remark") && StringUtil.isNotEmpty(companyInfo.get("remark").toString())){
            params.put("remark", companyInfo.get("remark").toString());
        }
        if(companyInfo.keySet().contains("orgType") && StringUtil.isNotEmpty(companyInfo.get("orgType").toString())){
            params.put("orgType", companyInfo.get("orgType").toString());
        }
        if(companyInfo.keySet().contains("orgCode") && StringUtil.isNotEmpty(companyInfo.get("orgCode").toString())){
            params.put("orgCode", companyInfo.get("orgCode").toString());
        }

        ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(url, params, null);
        if(null != objectNode && objectNode.has("responseCode")){
            String responseCode = objectNode.get("responseCode").toString();
            if(responseCode.equals("200")){
                isSuccess = true;
                message = "申请成功";
                if(url.contains("/YNEnergy/enterpriseInfo/apply")){
                    //“记录索引号”入库
                    String dataIndex = objectNode.get("data").get("dataIndex").toString().replaceAll("\"","");
                    Map<String, Object> temp = new HashMap<String, Object>();
                    temp.put("entId", entId);
                    temp.put("dataIndex", dataIndex);
                    this.yunNanMapper.updateCompanyInfo(temp);
                }
            }
            else {
                isSuccess = false;
                message = responseCode + objectNode.get("responseMessage").toString().replaceAll("\"","");
            }
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public Map<String,Object> applyAK(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "申请失败";

        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        if(null != ent && ent.keySet().contains("entCode") && ent.keySet().contains("password") && ent.keySet().contains("sk")){
            String entCode = ent.get("entCode").toString();
            String password = SM4Util.yunNanSm4Encode(ent.get("sk").toString(), ent.get("entCode").toString(), ent.get("password").toString());
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("enterpriseCode", entCode);
            params.put("password", password);
            ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/requestAK", params, null);
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    isSuccess = true;
                    message = "申请成功";
                    //入库T_PLAT_AK
                    String akExpiresIn = objectNode.get("data").get("akExpiresIn").toString().replaceAll("\"","").substring(0,16);
                    String akValidAt = objectNode.get("data").get("akValidAt").toString().replaceAll("\"","").substring(0,16);
                    String enterpriseUploadTime = objectNode.get("data").get("enterpriseUploadTime").toString().replaceAll("\"","");
                    String platformURL = objectNode.get("data").get("platformURL").toString().replaceAll("\"","");
                    String token = objectNode.get("data").get("token").toString().replaceAll("\"","");

                    result.put("entId", entId);
                    result.put("endTime", akExpiresIn);
                    result.put("beginTime", akValidAt);
                    result.put("uploadTime", enterpriseUploadTime);
                    result.put("platUrl", platformURL);
                    result.put("token", token);

                    insertUpdatePlatAk(result);
                }
                else {
                    isSuccess = false;
                    message = responseCode + objectNode.get("responseMessage").toString().replaceAll("\"","");
                }
            }
        }
        else {
            isSuccess = false;
            message = "请按照步骤从上往下一步一步申请";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public void insertUpdatePlatAk(Map<String, Object> platAk){
        Long entId = Long.valueOf(platAk.get("entId").toString());

        //检查 是否已存在
        Map<String, Object> temp = this.yunNanMapper.getPlatAkById(entId);
        if(null != temp){
            //修改
            this.yunNanMapper.updatePlatAk(platAk);
        }
        else {
            //新增
            this.yunNanMapper.insertPlatAk(platAk);
        }
    }

    @Override
    public Map<String,Object> applyCheckVersion(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "校验失败";

        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        if( null != ent && ent.keySet().contains("entCode") && null != platAk && platAk.keySet().contains("token") ){
            String entCode = ent.get("entCode").toString();
            String ak = platAk.get("token").toString();

            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("enterpriseCode", entCode);
            ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/checkVersion", params, ak);
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    isSuccess = true;
                    message = "校验成功";
                    //入库T_PLAT_VERSION
                    String dicVersion = objectNode.get("data").get("dicVersion").toString().replaceAll("\"","");
                    String loadPlatformDataURL = objectNode.get("data").get("loadPlatformDataURL").toString().replaceAll("\"","");
                    String regVersion = objectNode.get("data").get("regVersion").toString().replaceAll("\"","");

                    result.put("entId", entId);
                    result.put("dicVersion", dicVersion);
                    result.put("regVersion", regVersion);
                    result.put("dataUrl", loadPlatformDataURL);

                    insertUpdatePlatVersion(result);
                }
                else {
                    isSuccess = false;
                    message = responseCode + objectNode.get("responseMessage").toString().replaceAll("\"","");
                }
            }
        }
        else {
            isSuccess = false;
            message = "请按照步骤从上往下一步一步申请";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public void insertUpdatePlatVersion(Map<String, Object> platVersion){
        Long entId = Long.valueOf(platVersion.get("entId").toString());

        //检查 是否已存在
        Map<String, Object> temp = this.yunNanMapper.getPlatVersionById(entId);
        if(null != temp){
            //修改
            this.yunNanMapper.updatePlatVersion(platVersion);
        }
        else {
            //新增
            this.yunNanMapper.insertPlatVersion(platVersion);
        }
    }

    @Override
    public Map<String,Object> applyPlatUrls(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "申请失败";

        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        if( null != ent && ent.keySet().contains("entCode") && null != platAk && platAk.keySet().contains("token") ){
            String entCode = ent.get("entCode").toString();
            String ak = platAk.get("token").toString();

            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("enterpriseCode", entCode);
            ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/platformAccessURL", params, ak);
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    isSuccess = true;
                    message = "申请成功";
                    //入库T_PLAT_BASEURL
                    String enterpriseDataDownloadURL = objectNode.get("data").get("enterpriseDataDownloadURL").toString().replaceAll("\"","");
                    String enterpriseDataURL = objectNode.get("data").get("enterpriseDataURL").toString().replaceAll("\"","");
                    String enterpriseInfoDownloadURL = objectNode.get("data").get("enterpriseInfoDownloadURL").toString().replaceAll("\"","");
                    String enterpriseInfoURL = objectNode.get("data").get("enterpriseInfoURL").toString().replaceAll("\"","");
                    String loadBaseDataURL = objectNode.get("data").get("loadBaseDataURL").toString().replaceAll("\"","");
                    String organizationDataURL = objectNode.get("data").get("organizationDataURL").toString().replaceAll("\"","");

                    result.put("entId", entId);
                    result.put("dataDownUrl", enterpriseDataDownloadURL);
                    result.put("dataUrl", enterpriseDataURL);
                    result.put("infoDownUrl", enterpriseInfoDownloadURL);
                    result.put("infoUrl", enterpriseInfoURL);
                    result.put("baseDataUrl", loadBaseDataURL);
                    result.put("originDataUrl", organizationDataURL);

                    insertUpdatePlatUrls(result);
                }
                else {
                    isSuccess = false;
                    message = responseCode + objectNode.get("responseMessage").toString();
                }
            }
        }
        else {
            isSuccess = false;
            message = "请按照步骤从上往下一步一步申请";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public void insertUpdatePlatUrls(Map<String, Object> platUrl){
        Long entId = Long.valueOf(platUrl.get("entId").toString());

        //检查 是否已存在
        Map<String, Object> temp = this.yunNanMapper.getPlatUrlsById(entId);
        if(null != temp){
            //修改
            this.yunNanMapper.updatePlatUrls(platUrl);
        }
        else {
            //新增
            this.yunNanMapper.insertPlatUrls(platUrl);
        }
    }

    @Override
    public Map<String,Object> downloadBaseData(Long entId, Integer itemIndex){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "下载失败";

        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);

        Map<String, Object> params = new LinkedHashMap<String, Object>();
        if( null != ent && ent.keySet().contains("entCode") ){
            String entCode = ent.get("entCode").toString();
            params.put("enterpriseCode", entCode);
            params.put("itemIndex", itemIndex.toString());

            if(itemIndex == 1 || itemIndex == 2 || itemIndex == 16){   //无需AK
                ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/dataDownload/public", params, null);
                if(null != objectNode && objectNode.has("responseCode")){
                    String responseCode = objectNode.get("responseCode").toString();
                    if(responseCode.equals("200")){
                        isSuccess = true;
                        message = "下载成功";
                        //入库
                        JsonNode data = objectNode.get("data");
                        if(itemIndex == 16){ //企业类型编码(T_ENT_TYPE表)
                            if(data.has("EnterpriseTypeCodes")){
                                JsonNode tempNode = data.get("EnterpriseTypeCodes");
                                if(null != tempNode && tempNode.size() > 0){
                                    //删除旧表数据
                                    this.yunNanMapper.deleteTableEntType();
                                    for(int i = 0; i < tempNode.size(); i++){
                                        JsonNode temp = tempNode.get(i);
                                        String code = temp.get("code").toString().replaceAll("\"","");
                                        String name = temp.get("name").toString().replaceAll("\"","");
                                        //新增数据
                                        this.yunNanMapper.insertEntType(code, name, "530000");
                                    }
                                }
                            }
                        }
                        if(itemIndex == 1){  //行政区划代码（云南）
                            if(data.has("RegionCodes")){
                                JsonNode tempNode = data.get("RegionCodes");
                                if(null != tempNode && tempNode.size() > 0){
                                    //删除旧表数据
                                    this.yunNanMapper.deleteRegionType();
                                    //新增数据
                                    for(int i = 0; i < tempNode.size(); i++){
                                        JsonNode temp = tempNode.get(i);
                                        String code = temp.get("code").toString().replaceAll("\"","");
                                        String name = temp.get("name").toString().replaceAll("\"","");
                                        if(code.length() == 6){
                                            if(code.equals("530000")){
                                                this.yunNanMapper.insetRegionType(code, name, 0, null);
                                            }
                                            else if(code.substring(0,2).equals("53") && code.substring(4,6).equals("00")){
                                                this.yunNanMapper.insetRegionType(code, name, 1, "530000");
                                            }
                                            else if(code.substring(0,2).equals("53")){
                                                String parentCode = code.substring(0,4) + "00";
                                                this.yunNanMapper.insetRegionType(code, name, 2, parentCode);
                                            }
                                        }
                                    }

                                }
                            }
                        }
                        if(itemIndex == 2){  //行业编码(T_INDUSTRY_TYPE表)
                            if(data.has("IndustryCodes")){
                                JsonNode tempNode = data.get("IndustryCodes");
                                if(null != tempNode && tempNode.size() > 0){
                                    //删除旧表数据
                                    this.yunNanMapper.deleteIndustryType();
                                    //新增数据
                                    Map<String, String> keyMap = new HashMap<>();
                                    for(int i = 0; i < tempNode.size(); i++){
                                        JsonNode temp = tempNode.get(i);
                                        String code = temp.get("code").toString().replaceAll("\"","");
                                        String name = temp.get("name").toString().replaceAll("\"","");
                                        keyMap.put(code, name);
                                    }
                                    for(int i = 0; i < tempNode.size(); i++){
                                        JsonNode temp = tempNode.get(i);
                                        String code = temp.get("code").toString().replaceAll("\"","");
                                        String name = temp.get("name").toString().replaceAll("\"","");
                                        if(code.length() == 1){
                                            this.yunNanMapper.insetIndustryType(code, name, 0, null);
                                        }
                                        else if(code.length() == 3){
                                            String parent = code.substring(0, 1);
                                            this.yunNanMapper.insetIndustryType(code, name, 1, parent);
                                        }
                                        else if(code.length() == 4){
                                            String parent = code.substring(0, 3);
                                            if(!keyMap.keySet().contains(parent)){
                                                parent = code.substring(0, 1);
                                            }
                                            this.yunNanMapper.insetIndustryType(code, name, 1, parent);
                                        }
                                        else if(code.length() == 5){
                                            String parent = code.substring(0, 4);
                                            if(!keyMap.keySet().contains(parent)){
                                                parent = code.substring(0, 3);
                                                if(!keyMap.keySet().contains(parent)){
                                                    parent = code.substring(0, 1);
                                                }
                                            }
                                            this.yunNanMapper.insetIndustryType(code, name, 1, parent);
                                        }
                                    }

                                }
                            }
                        }
                    }
                    else {
                        isSuccess = false;
                        message = responseCode + objectNode.get("responseMessage").toString();
                    }
                }

            }
            else {      //需要AK
                Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
//                Map<String, Object> platUrl = this.yunNanMapper.getPlatUrlsById(entId);
                if( null != platAk && platAk.keySet().contains("token") ){
                    String ak = platAk.get("token").toString();
//                    String dataUrl = platUrl.get("baseDataUrl").toString();

                    ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/dataDownload", params, ak);
                    if(null != objectNode && objectNode.has("responseCode")){
                        String responseCode = objectNode.get("responseCode").toString();
                        if(responseCode.equals("200")){
                            isSuccess = true;
                            message = "下载成功";

                            //入库
                            JsonNode data = objectNode.get("data");
                            if(itemIndex == 3){ //生产工序编码(T_PROD_PCODE表)
                                if(data.has("ProductionProcessCodes")){
                                    JsonNode tempNode = data.get("ProductionProcessCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                    }
                                }
                            }
                            if(itemIndex == 4){ //工序单元编码(T_PROD_PUNITCODE)
                                if(data.has("processUnitCodes")){
                                    JsonNode tempNode = data.get("processUnitCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                    }
                                }
                            }
                            if(itemIndex == 5){ //用能设备编码(T_ENEQP)
                                if(data.has("EnergyDeviceCodes")){
                                    JsonNode tempNode = data.get("EnergyDeviceCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                    }
                                }
                            }
                            if(itemIndex == 6){ //数据采集来源类型编码(T_DATA_SRC)
                                if(data.has("DataCollectOriginCodes")){
                                    JsonNode tempNode = data.get("DataCollectOriginCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableDataSrc();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");
                                            String name = temp.get("name").toString().replaceAll("\"","");
                                            //新增数据
                                            this.yunNanMapper.insertDataSrc(code, name, "530000");
                                        }
                                    }
                                }
                            }
                            if(itemIndex == 7){ //采集数据类型编码(T_DATA_TYPE)
                                if(data.has("CollectDataTypeCodes")){
                                    JsonNode tempNode = data.get("CollectDataTypeCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableDataType();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");
                                            String name = temp.get("name").toString().replaceAll("\"","");
                                            //新增数据
                                            this.yunNanMapper.insertDataType(code, name, "530000");
                                        }
                                    }
                                }
                            }
                            if(itemIndex == 8){ //能源品种编码计量单位及精度
                                if(data.has("EnergyTypeUnitCollectCodes")){
                                    JsonNode tempNode = data.get("EnergyTypeUnitCollectCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableEnType();
                                        this.yunNanMapper.deleteTableEnConvert();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");  //能源品种编码
                                            String name = temp.get("name").toString().replaceAll("\"","");  //能源品种名称
                                            String collectRatio = temp.get("collectRatio").toString().replaceAll("\"","");  //折标系数
                                            String collectUnit = temp.get("collectUnit").toString().replaceAll("\"","");  //折标系数单位
                                            String qualityUnit = temp.get("qualityUnit").toString().replaceAll("\"","");  //计量单位
                                            String qualityPrecision = temp.get("qualityPrecision").toString().replaceAll("\"","");  //精度

                                            String complexUnit = temp.get("complexUnit").toString().replaceAll("\"","");  //计量单位
                                            String complexPrecision = temp.get("complexPrecision").toString().replaceAll("\"","");  //精度
                                            //新增数据
                                            if(code.equals("0000")){
                                                this.yunNanMapper.insertEnType(code,name,complexUnit,complexPrecision,"530000");
                                            }
                                            else {
                                                this.yunNanMapper.insertEnType(code,name,qualityUnit,qualityPrecision,"530000");
                                            }
                                            this.yunNanMapper.insertEnConvert(code,name,collectRatio,collectUnit,"530000");
                                        }
                                    }
                                }
                            }
                            if(itemIndex == 9){ //耗能工质编码计量单位及精度
                                if(data.has("EnergySubstanceCodes")){
                                    JsonNode tempNode = data.get("EnergySubstanceCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableWmed();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");
                                            String name = temp.get("name").toString().replaceAll("\"","");
                                            String qualityUnit = temp.get("qualityUnit").toString().replaceAll("\"","");
                                            String qualityPrecision = temp.get("qualityPrecision").toString().replaceAll("\"","");
                                            String collectUnit = temp.get("collectUnit").toString().replaceAll("\"","");
                                            String collectPrecision = temp.get("collectPrecision").toString().replaceAll("\"","");
                                            //新增数据
                                            this.yunNanMapper.insertTableWmed(code, name,qualityUnit,qualityPrecision,collectUnit,collectPrecision, "530000");
                                        }
                                    }
                                }
                            }
                            if(itemIndex == 10){ //非能源类产品及计量单位
                                if(data.has("UnenergyProductCodes")){
                                    JsonNode tempNode = data.get("UnenergyProductCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                    }
                                }
                            }
                            if(itemIndex == 11){ //能效指标编码及计量单位
                                if(data.has("EnergyEfficiencyCodes")){
                                    JsonNode tempNode = data.get("EnergyEfficiencyCodes");
                                    if(null != tempNode && tempNode.size() > 0){
                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableEei();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");
                                            String name = temp.get("name").toString().replaceAll("\"","");
                                            String industry = temp.get("industry").toString().replaceAll("\"","");
                                            String targetType = temp.get("targetType").toString().replaceAll("\"","");
                                            String unitCh = temp.get("unitCh").toString().replaceAll("\"","");
                                            String unitEn = temp.get("unitEn").toString().replaceAll("\"","");
                                            //新增数据
                                            this.yunNanMapper.insertTableEei(code, name,industry,targetType,unitCh,unitEn, "530000");
                                        }
                                    }
                                }
                            }
                            if(itemIndex == 12){ //经营指标编码及计量单位
                                if(data.has("BusinessTargetCodes")){
                                    JsonNode tempNode = data.get("BusinessTargetCodes");
                                    if(null != tempNode && tempNode.size() > 0){
                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableKpi();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");
                                            String name = temp.get("name").toString().replaceAll("\"","");
                                            String unit = temp.get("unit").toString().replaceAll("\"","");
                                            String precisions = temp.get("precisions").toString().replaceAll("\"","");
                                            //新增数据
                                            this.yunNanMapper.insertTableKpi(code, name, unit, precisions, "530000");
                                        }
                                    }
                                }
                            }
                            if(itemIndex == 13){ //其他数据编码
                                if(data.has("OtherDataCodes")){
                                    JsonNode tempNode = data.get("OtherDataCodes");
                                    if(null != tempNode && tempNode.size() > 0){
                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableDataCodeOther();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");
                                            String name = temp.get("name").toString().replaceAll("\"","");
                                            //新增数据
                                            this.yunNanMapper.insertDataCodeOther(code, name, "530000");
                                        }
                                    }
                                }
                            }
                            if(itemIndex == 14){ //能源用途编码
                                if(data.has("EnergyUseCodes")){
                                    JsonNode tempNode = data.get("EnergyUseCodes");
                                    if(null != tempNode && tempNode.size() > 0){
                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableDataCodeEnUse();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");
                                            String name = temp.get("name").toString().replaceAll("\"","");
                                            //新增数据
                                            this.yunNanMapper.insertDataCodeEnUse(code, name, "530000");
                                        }
                                    }
                                }
                            }
                            if(itemIndex == 15){ //计量器具编码
                                if(data.has("GaugeTypeCodes")){
                                    JsonNode tempNode = data.get("GaugeTypeCodes");
                                    if(null != tempNode && tempNode.size() > 0){

                                    }
                                }
                            }
                            if(itemIndex == 17){ //重点用能设备分类编码
                                if(data.has("DeviceTypeCodes")){
                                    JsonNode tempNode = data.get("DeviceTypeCodes");
                                    if(null != tempNode && tempNode.size() > 0){
                                        //删除旧表数据
                                        this.yunNanMapper.deleteTableEneqpType();
                                        for(int i = 0; i < tempNode.size(); i++){
                                            JsonNode temp = tempNode.get(i);
                                            String code = temp.get("code").toString().replaceAll("\"","");
                                            String name = temp.get("name").toString().replaceAll("\"","");
                                            //新增数据
                                            this.yunNanMapper.insertEneqpType(code, name, "530000");
                                        }
                                    }
                                }
                            }

                        }
                        else {
                            isSuccess = false;
                            message = responseCode + objectNode.get("responseMessage").toString();
                        }
                    }
                }
                else {
                    isSuccess = false;
                    message = "请按照步骤从上往下一步一步申请";
                }

            }
        }
        else {
            isSuccess = false;
            message = "请按照步骤从上往下一步一步申请";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public Map<String,Object> instituteEnergyData(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();

        LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(entId);
        int ledgerFlag = 0;
        if(ledgerBean.getAnalyType() == 102){
            ledgerFlag = 1;
            result.put("dateList", this.yunNanMapper.getInstituteEnergyDates(entId));
        }
        result.put("ledgerFlag", ledgerFlag);

        return result;
    }

    @Override
    public Map<String,Object> instituteEnergyCopy(Long entId, String date){
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("resrContInfo", this.yunNanMapper.getResrContInfoBy(entId, date));

        return result;
    }

    @Override
    public Map<String,Object> reportInstituteEnergy(Map<String, Object> resrContInfo){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "申请失败";

        ////手工上传
        Long entId = Long.valueOf(resrContInfo.get("entId").toString());
        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        Map<String, Object> platVersion = this.yunNanMapper.getPlatVersionById(entId);
        if(null != ent && ent.keySet().contains("entCode") && null != platAk && platAk.keySet().contains("token")
                && null != platVersion  && platVersion.keySet().contains("dicVersion") && platVersion.keySet().contains("regVersion")){
            String entCode = ent.get("entCode").toString();
            String ak = platAk.get("token").toString();
            String dicVersion = platVersion.get("dicVersion").toString();
            String regVersion = platVersion.get("regVersion").toString();

            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("enterpriseCode", entCode);
            params.put("regVersion", regVersion);
            params.put("dicVersion", dicVersion);
            params.put("reportDate", resrContInfo.get("reportDate").toString());
            params.put("charger", resrContInfo.get("charger").toString());
            params.put("statisticians", resrContInfo.get("statistician").toString());
            params.put("preparer", resrContInfo.get("preparer").toString());
            params.put("preparDate", resrContInfo.get("preparDate").toString());
            params.put("f009", resrContInfo.get("useArea").toString());
            params.put("f101", resrContInfo.get("floorage").toString());
            params.put("f102", resrContInfo.get("energyPeople").toString());
            params.put("f1021", resrContInfo.get("staff").toString());
            params.put("f103", resrContInfo.get("vehicleNo").toString());
            params.put("f1031", resrContInfo.get("gascarNo").toString());
            params.put("f1032", resrContInfo.get("dieselcarNo").toString());
            params.put("f1033", resrContInfo.get("energycarNo").toString());
            params.put("f104", resrContInfo.get("lpgCont").toString());
            params.put("f1041", resrContInfo.get("lpgCost").toString());
            params.put("f105", resrContInfo.get("postNo").toString());
            params.put("f110", resrContInfo.get("q").toString());
            params.put("f111", resrContInfo.get("qValue").toString());
            params.put("f120", resrContInfo.get("w").toString());
            params.put("f121", resrContInfo.get("wValue").toString());
            params.put("f130", resrContInfo.get("c").toString());
            params.put("f131", resrContInfo.get("cValue").toString());
            params.put("f140", resrContInfo.get("g").toString());
            params.put("f141", resrContInfo.get("gValue").toString());
            params.put("f150", resrContInfo.get("petrol").toString());
            params.put("f151", resrContInfo.get("petrolValue").toString());
            params.put("f1501", resrContInfo.get("gascarFuel").toString());
            params.put("f1502", resrContInfo.get("gascarFuelOther").toString());
            params.put("f1511", resrContInfo.get("gascarCost").toString());
            params.put("f1512", resrContInfo.get("gascarOtherCost").toString());
            params.put("f160", resrContInfo.get("diesel").toString());
            params.put("f161", resrContInfo.get("dieselValue").toString());
            params.put("f1601", resrContInfo.get("dieselcarFuel").toString());
            params.put("f1602", resrContInfo.get("dieselcarFuelOther").toString());
            params.put("f1611", resrContInfo.get("dieselcarCost").toString());
            params.put("f1612", resrContInfo.get("dieselcarOtherCost").toString());
            params.put("f170", resrContInfo.get("h").toString());
            params.put("f171", resrContInfo.get("hValue").toString());
            params.put("f180", resrContInfo.get("other").toString());
            params.put("f181", resrContInfo.get("otherValue").toString());
            params.put("f190", resrContInfo.get("collectorArea").toString());
            params.put("f191", resrContInfo.get("solarCapacity").toString());
            params.put("f192", resrContInfo.get("geothermCapacity").toString());

            ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/arganEnergy/addEnergy", params, ak);
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    isSuccess = true;
                    message = "申请成功";
                    //入库T_RESR_CONT_INFO
                    insertUpdateResrContInfo(resrContInfo);
                }
                else {
                    isSuccess = false;
                    message = responseCode + objectNode.get("responseMessage").toString();
                }
            }
        }
        else {
            isSuccess = false;
            message = "该企业需要先获取AK，并完成平台版本校验";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public void insertUpdateResrContInfo(Map<String, Object> resrContInfo){
        Long entId = Long.valueOf(resrContInfo.get("entId").toString());
        String reportDate = resrContInfo.get("reportDate").toString();

        //检查 是否已存在
        int num = this.yunNanMapper.getResrContInfoNumBy(entId, reportDate);
        if(num > 0){
            //修改
            this.yunNanMapper.updateResrContInfo(resrContInfo);
        }
        else {
            //新增
            this.yunNanMapper.insertResrContInfo(resrContInfo);
        }
    }

    @Override
    public Map<String,Object> mechanismRoomData(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();

        LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(entId);
        int ledgerFlag = 0;
        if(ledgerBean.getAnalyType() == 102){
            ledgerFlag = 1;
            result.put("dateList", this.yunNanMapper.getMechanismRoomDataDates(entId));
        }
        result.put("ledgerFlag", ledgerFlag);

        return result;
    }

    @Override
    public Map<String,Object> mechanismRoomCopy(Long entId, String date){
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("contInfo", this.yunNanMapper.getMechanismRoomBy(entId, date));

        return result;
    }

    @Override
    public Map<String,Object> reportMechanismRoom(Map<String, Object> contInfo){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "申请失败";

        ////手工上传
        Long entId = Long.valueOf(contInfo.get("entId").toString());
        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        Map<String, Object> platVersion = this.yunNanMapper.getPlatVersionById(entId);
        if(null != ent && ent.keySet().contains("entCode") && null != platAk && platAk.keySet().contains("token")
                && null != platVersion  && platVersion.keySet().contains("dicVersion") && platVersion.keySet().contains("regVersion")){
            String entCode = ent.get("entCode").toString();
            String ak = platAk.get("token").toString();
            String dicVersion = platVersion.get("dicVersion").toString();
            String regVersion = platVersion.get("regVersion").toString();

            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("enterpriseCode", entCode);
            params.put("regVersion", regVersion);
            params.put("dicVersion", dicVersion);
            params.put("idcName", contInfo.get("centerName").toString());
            params.put("reportDate", contInfo.get("reportDate").toString());
            params.put("charger", contInfo.get("charger").toString());
            params.put("statisticians", contInfo.get("statistician").toString());
            params.put("preparer", contInfo.get("preparer").toString());
            params.put("preparDate", contInfo.get("preparDate").toString());
            params.put("f201", contInfo.get("floorage").toString());
            params.put("f210", contInfo.get("rittalNo").toString());
            params.put("f2101", contInfo.get("reserveRittalNo").toString());
            params.put("f220", contInfo.get("totalP").toString());
            params.put("f2201", contInfo.get("itP").toString());
            params.put("f2202", contInfo.get("acP").toString());
            params.put("f2203", contInfo.get("accessP").toString());
            params.put("f230", contInfo.get("upsCapacity").toString());
            params.put("f240", contInfo.get("totalQ").toString());
            params.put("f2401", contInfo.get("itQ").toString());
            params.put("f2402", contInfo.get("acQ").toString());
            params.put("f2403", contInfo.get("accessQ").toString());
            params.put("f250", contInfo.get("otherQ").toString());

            ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/arganEnergy/addIDC", params, ak);
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    isSuccess = true;
                    message = "申请成功";
                    //入库T_DCRESR_CONT_INFO
                    insertUpdateMechanismRoom(contInfo);
                }
                else {
                    isSuccess = false;
                    message = responseCode + objectNode.get("responseMessage").toString();
                }
            }
        }
        else {
            isSuccess = false;
            message = "该企业需要先获取AK，并完成平台版本校验";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public void insertUpdateMechanismRoom(Map<String, Object> contInfo){
        Long entId = Long.valueOf(contInfo.get("entId").toString());
        String reportDate = contInfo.get("reportDate").toString();

        //检查 是否已存在
        int num = this.yunNanMapper.getMechanismRoomNumBy(entId, reportDate);
        if(num > 0){
            //修改
            this.yunNanMapper.updateMechanismRoom(contInfo);
        }
        else {
            //新增
            this.yunNanMapper.insertMechanismRoom(contInfo);
        }
    }

    @Override
    public Map<String,Object> mechanismHeatingData(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();

        LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(entId);
        int ledgerFlag = 0;
        if(ledgerBean.getAnalyType() == 102){
            ledgerFlag = 1;
            result.put("dateList", this.yunNanMapper.getMechanismHeatingDataDates(entId));
        }
        result.put("ledgerFlag", ledgerFlag);

        return result;
    }

    @Override
    public Map<String,Object> mechanismHeatingCopy(Long entId, String date){
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("contInfo", this.yunNanMapper.getMechanismHeatingBy(entId, date));

        return result;
    }

    @Override
    public Map<String,Object> reportMechanismHeating(Map<String, Object> contInfo){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = false;
        String message = "申请失败";

        ////手工上传
        Long entId = Long.valueOf(contInfo.get("entId").toString());
        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        Map<String, Object> platVersion = this.yunNanMapper.getPlatVersionById(entId);
        if(null != ent && ent.keySet().contains("entCode") && null != platAk && platAk.keySet().contains("token")
                && null != platVersion  && platVersion.keySet().contains("dicVersion") && platVersion.keySet().contains("regVersion")){
            String entCode = ent.get("entCode").toString();
            String ak = platAk.get("token").toString();
            String dicVersion = platVersion.get("dicVersion").toString();
            String regVersion = platVersion.get("regVersion").toString();

            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("enterpriseCode", entCode);
            params.put("regVersion", regVersion);
            params.put("dicVersion", dicVersion);
            params.put("reportDate", contInfo.get("reportDate").toString());
            params.put("charger", contInfo.get("charger").toString());
            params.put("statisticians", contInfo.get("statistician").toString());
            params.put("preparer", contInfo.get("preparer").toString());
            params.put("preparDate", contInfo.get("preparDate").toString());
            params.put("f301", contInfo.get("heatArea").toString());
            params.put("f3011", contInfo.get("ihArea").toString());
            params.put("f3012", contInfo.get("chAreaA").toString());
            params.put("f3013", contInfo.get("chAreaH").toString());
            params.put("f302", contInfo.get("heatDay").toString());
            params.put("f303", contInfo.get("boilerP").toString());
            params.put("f304", contInfo.get("steamP").toString());
            params.put("f310", contInfo.get("w").toString());
            params.put("f311", contInfo.get("wValue").toString());
            params.put("f320", contInfo.get("q").toString());
            params.put("f321", contInfo.get("qValue").toString());
            params.put("f330", contInfo.get("c").toString());
            params.put("f331", contInfo.get("cValue").toString());
            params.put("f340", contInfo.get("g").toString());
            params.put("f341", contInfo.get("gValue").toString());
            params.put("f350", contInfo.get("diesel").toString());
            params.put("f351", contInfo.get("dieselValue").toString());
            params.put("f360", contInfo.get("h").toString());
            params.put("f361", contInfo.get("hValue").toString());
            params.put("f370", contInfo.get("other").toString());
            params.put("f371", contInfo.get("otherValue").toString());

            ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/arganEnergy/addWarm", params, ak);
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    isSuccess = true;
                    message = "申请成功";
                    //入库T_HEATRESR_CONT_INFO
                    insertUpdateMechanismHeating(contInfo);
                }
                else {
                    isSuccess = false;
                    message = responseCode + objectNode.get("responseMessage").toString();
                }
            }
        }
        else {
            isSuccess = false;
            message = "该企业需要先获取AK，并完成平台版本校验";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }

    @Override
    public void insertUpdateMechanismHeating(Map<String, Object> contInfo){
        Long entId = Long.valueOf(contInfo.get("entId").toString());
        String reportDate = contInfo.get("reportDate").toString();

        //检查 是否已存在
        int num = this.yunNanMapper.getMechanismHeatingNumBy(entId, reportDate);
        if(num > 0){
            //修改
            this.yunNanMapper.updateMechanismHeating(contInfo);
        }
        else {
            //新增
            this.yunNanMapper.insertMechanismHeating(contInfo);
        }
    }

    @Override
    public Map<String,Object> getCompanyCollectionConfig(Long entId){
        Map<String, Object> result = new HashMap<String, Object>();

        LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId(entId);
        int ledgerFlag = 0;
        if(ledgerBean!=null&&ledgerBean.getAnalyType() == 102){
            ledgerFlag = 1;
            result.put("dataList", this.yunNanMapper.getCompanyCollectionConfig(entId));
        }
        result.put("ledgerFlag", ledgerFlag);

        return result;
    }

    @Override
    public Map<String,Object> getOneCompanyCollectionConfig(Long entId, Long dataId){

        Map<String, Object> dataConfig = this.yunNanMapper.getOneCollectionConfig(entId, dataId, "530000");
        if(null != dataConfig && dataConfig.keySet().contains("relateType") && dataConfig.keySet().contains("relateId")){
            Long relateId = Long.valueOf(dataConfig.get("relateId").toString());
            Integer relateType = Integer.valueOf(dataConfig.get("relateType").toString());
            if(relateType == 1){ //能管对象
                LedgerBean ledger = this.ledgerManagerMapper.selectByLedgerId(relateId);
                String name = "";
                if( null != ledger && StringUtil.isNotEmpty(ledger.getLedgerName()) ){
                    name = ledger.getLedgerName();
                }
                dataConfig.put("relateName", name + "（能管对象）");
            }
            else if(relateType == 2){ //采集点
                MeterBean meter = this.meterManagerMapper.getMeterDataByPrimaryKey(relateId);
                String name = "";
                if( null != meter && StringUtil.isNotEmpty(meter.getMeterName()) ){
                    name = meter.getMeterName();
                }
                dataConfig.put("relateName", name + "（采集点）");
            }
        }

        return dataConfig;
    }

    @Override
    public Map<String,Object> reportDataFillUpload(Map<String, Object> contInfo){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = true;
        String message = "上报完成";

        Long entId = Long.valueOf(contInfo.get("entId").toString());
        Long dataId = Long.valueOf(contInfo.get("dataId").toString());
        String valid = contInfo.get("valid").toString();
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) contInfo.get("dataList");

        Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
        Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        Map<String, Object> platVersion = this.yunNanMapper.getPlatVersionById(entId);
        if(null != ent && ent.keySet().contains("entCode") && null != platAk && platAk.keySet().contains("token")
                && null != platVersion  && platVersion.keySet().contains("dicVersion") && platVersion.keySet().contains("regVersion")){

            String entCode = ent.get("entCode").toString();
            String ak = platAk.get("token").toString();
            String dicVersion = platVersion.get("dicVersion").toString();
            String regVersion = platVersion.get("regVersion").toString();

            Map<String,Object> dataConfig = getOneCompanyCollectionConfig(entId, dataId);
            if(!dataConfig.keySet().contains("convertRatio") || dataConfig.get("convertRatio") == null){
                dataConfig.put("convertRatio", 1);
            }
            if(null != dataConfig && dataConfig.keySet().contains("dataCode") && dataConfig.keySet().contains("convertRatio")
                    && dataConfig.keySet().contains("scope") && dataConfig.keySet().contains("inputType") && dataConfig.keySet().contains("frequce")){

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("enterpriseCode", entCode);
                params.put("regVersion", regVersion);
                params.put("dicVersion", dicVersion);
                params.put("dataCode", dataConfig.get("dataCode").toString());
                params.put("convertRation", dataConfig.get("convertRatio").toString());
                params.put("valid", valid);
                params.put("scope", dataConfig.get("scope").toString());
                params.put("inputType", dataConfig.get("inputType").toString());
                params.put("statType", dataConfig.get("frequce").toString());
                params.put("uploadDate", DateUtil.convertDateToStr(new Date(), DateUtil.DEFAULT_PATTERN));
                for(Map<String, Object> temp: dataList){
                    params.put("dataValue", temp.get("data").toString());

                    String statDate = "";
                    if(dataConfig.get("frequce").toString().equals("0")){   //实时
                        Date date1 = DateUtil.convertStrToDate(temp.get("time").toString(), DateUtil.MOUDLE_PATTERN);
                        statDate = DateUtil.convertDateToStr(date1, DateUtil.DEFAULT_PATTERN);
                    }
                    else if(dataConfig.get("frequce").toString().equals("1")){ //日
                        Date date1 = DateUtil.convertStrToDate(temp.get("time").toString(), DateUtil.SHORT_PATTERN);
                        statDate = DateUtil.convertDateToStr(date1, DateUtil.DEFAULT_PATTERN);
                    }
                    else if(dataConfig.get("frequce").toString().equals("2")){ //月
                        Date date1 = DateUtil.convertStrToDate(temp.get("time").toString(), DateUtil.MONTH_PATTERN);
                        statDate = DateUtil.convertDateToStr(date1, DateUtil.DEFAULT_PATTERN);
                    }
                    else if(dataConfig.get("frequce").toString().equals("3")){ //年
                        Date date1 = DateUtil.convertStrToDate(temp.get("time").toString(), DateUtil.YEAR_PATTERN);
                        statDate = DateUtil.convertDateToStr(date1, DateUtil.DEFAULT_PATTERN);
                    }
                    params.put("statDate", statDate);

                    ObjectNode objectNode = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort + "/YNEnergy/dataEnterpriseEnergy", params, ak);
                    if(null != objectNode && objectNode.has("responseCode")){
                        String responseCode = objectNode.get("responseCode").toString();
                        if(!responseCode.equals("200")){
                            isSuccess = false;
                            message = responseCode + objectNode.get("responseMessage").toString();
                        }

                    }
                }

            }
            else {
                isSuccess = false;
                message = "该采集数据项配置有误!";
            }

        }
        else {
            isSuccess = false;
            message = "该企业需要先获取AK，并完成平台版本校验!";
        }

        result.put("isSuccess", isSuccess);
        result.put("message", message);
        return result;
    }
    
    @Override
    public Map<String,Object> reUploadData(Map<String, Object> contInfo){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isSuccess = true;
        String message = "上报完成";
        //long entId=Long.valueOf(contInfo.get("entid").toString());
        //String valid = contInfo.get("valid").toString();
        InnerBean innerBean=new InnerBean();
        innerBean.setDataCfgItemIDs(contInfo.get("dataids").toString());
        innerBean.setDataType(Integer.parseInt(contInfo.get("frequce").toString()));
        innerBean.setStartTimeMS(DateUtil.convertStrToDate(contInfo.get("begindate").toString(),DateUtil.SHORT_PATTERN).getTime());
        innerBean.setEndTimeMS(DateUtil.convertStrToDate(contInfo.get("enddate").toString(),DateUtil.SHORT_PATTERN).getTime());
        //System.out.println(JsonUtil.jsonObj2Sting(innerBean));
        String objectNode=HttpYunNan.sendHttpToInnerYunNan(WebConstant.ynnerYunNanIpPort, innerBean);     
		if(!objectNode.equals("OK")) {
			isSuccess=false;
			message="上传失败";
		}      
        
		result.put("flag", isSuccess);
        result.put("message", message);
        
        return result;
    }

}
