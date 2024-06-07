package com.linyang.energy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.yunNan.CollectConfigManageMapper;
import com.linyang.energy.mapping.yunNan.EntBaseFileMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.service.CollectConfigManageService;
import com.linyang.energy.service.EntBaseFileService;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import com.linyang.energy.utils.yunNanUtil.JsonUtil;

/**
 * @类功能说明：企业基本档案
 * @公司名称：江苏林洋电子有限公司
 * @作者：james
 * @创建时间：2020-05-25 下午03:45:47  
 * @版本：V1.0
 */
@Service
public class EntBaseFileServiceImpl implements EntBaseFileService {
	@Autowired
    private YunNanMapper yunNanMapper;
	
	@Autowired
	private EntBaseFileMapper entBaseFileMapper;

	@Override
	public Map<String, Object> getAoInfo(long entId) {		
		return entBaseFileMapper.getEmInfoById(entId);
	}

	@Override
	public int saveEmInfo(Map<String, Object> aoInfo) {
		return entBaseFileMapper.mergeEmInfo(aoInfo);
	}
	
	@Override
	public Map<String, String> uploadEnergyManage(String entId, String typeId) {
		Map<String, String> result=new HashMap<String,String>();
		Map<String,Object> proc = entBaseFileMapper.getEmInfoById(Long.parseLong(entId));
		if(proc!=null) {
			ObjectNode objectNode = operateEnergyManage(proc,typeId);
			System.out.println(JsonUtil.jsonObj2Sting(objectNode));
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    Map<String,Object> updateInfo=new HashMap<String,Object>();
                    updateInfo.put("entId", entId);
                    if(typeId.equals("0")) {//增加
                    	updateInfo.put("uploadStatus", "1");
                    	result.put("status", "1");	
                    	updateInfo.put("dataIndex", objectNode.get("data").get("dataIndex").asText());
            		}
            		else if(typeId.equals("1")) {//修改
            			updateInfo.put("uploadStatus", "2");
            			result.put("status", "2");
            		}
            		else {//删除
            			updateInfo.put("uploadStatus", "3");
            			updateInfo.put("dataIndex", "");
            			result.put("status", "3");
            		}
                    entBaseFileMapper.updateEnergyManageStatus(updateInfo);
                    result.put("flag", "true");
                }
                else {
                	result.put("flag", "false");
                }
                result.put("message", objectNode.get("responseMessage").asText());
            }
		}
		
		return result;
	}
	
	private ObjectNode  operateEnergyManage(Map<String,Object> proc,String typeId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		
		long entId=Long.valueOf(proc.get("ENT_ID").toString());
		Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
		Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        Map<String, Object> platVersion = this.yunNanMapper.getPlatVersionById(entId);
        Map<String, Object> platurl = this.yunNanMapper.getPlatUrlsById(entId);
        ObjectNode result=null;
        if(platAk==null||platVersion==null||ent==null||platurl==null) {
        	return result;
        }
        
        String entCode = ent.get("entCode").toString();
        String ak = platAk.get("token").toString();
        String dicVersion = platVersion.get("dicVersion").toString();
        String regVersion = platVersion.get("regVersion").toString();
		//String url=platurl.get("infoUrl").toString();  //用能单位基础信息配置交换地址
        String url=WebConstant.yunNanIpPort;  //test
		if(typeId.equals("0")) {  //增加
			url += "/YNEnergy/companyEnergySys/add";
			params.put("regVersion", regVersion); /*平台服务版本*/
			params.put("dicVersion", dicVersion); /*基础数据版本*/
			params.put("enterpriseCode", entCode); 
			params.put("energyOffice", proc.get("EM_NAME"));
			params.put("energyOfficial", proc.get("OFFICIAL_NAME"));
			params.put("energyOfficialPosition", proc.get("OFFICIAL_POSITION"));
			params.put("phone", proc.get("TEL"));
			if(proc.get("FAX")!=null) { //可空字段
				params.put("fax", proc.get("FAX"));
			}
		}
		else if(typeId.equals("1")) { //修改
			url +=  "/YNEnergy/companyEnergySys/update";	
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("regVersion", regVersion);
			params.put("dicVersion", dicVersion);   
			params.put("enterpriseCode", entCode); 
			params.put("energyOffice", proc.get("EM_NAME"));
			params.put("energyOfficial", proc.get("OFFICIAL_NAME"));
			params.put("energyOfficialPosition", proc.get("OFFICIAL_POSITION"));
			params.put("phone", proc.get("TEL"));
			if(proc.get("FAX")!=null) { //可空字段
				params.put("fax", proc.get("FAX"));
			}
		}
		else {  //删除
			url +=  "/YNEnergy/companyEnergySys/delete";
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("enterpriseCode", entCode); 
		}
		//System.out.println(JsonUtil.jsonObj2Sting(params));
		return HttpYunNan.sendHttpToYunNan(url, params, ak); 
	}	

	@Override
	public Map<String, Object> getEntIdentInfo(long entId) {
		return entBaseFileMapper.getEntIdentInfoById(entId);
	}

	@Override
	public int saveEntIdentInfo(Map<String, Object> aoInfo) {
		return entBaseFileMapper.mergeEntIdentInfo(aoInfo);
	}
	
	@Override
	public Map<String, String> uploadEntIdent(String entId, String typeId) {
		Map<String, String> result=new HashMap<String,String>();
		Map<String,Object> proc = entBaseFileMapper.getEntIdentInfoById(Long.parseLong(entId));
		if(proc!=null) {
			ObjectNode objectNode = operateEntIdent(proc,typeId);
			System.out.println(JsonUtil.jsonObj2Sting(objectNode));
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    Map<String,Object> updateInfo=new HashMap<String,Object>();
                    updateInfo.put("entId", entId);
                    if(typeId.equals("0")) {//增加
                    	updateInfo.put("uploadStatus", "1");
                    	result.put("status", "1");
                    	updateInfo.put("dataIndex", objectNode.get("data").get("dataIndex").asText());
            		}
            		else if(typeId.equals("1")) {//修改
            			updateInfo.put("uploadStatus", "2");
            			result.put("status", "2");
            		}
            		else {//删除
            			updateInfo.put("uploadStatus", "3");
            			updateInfo.put("dataIndex", "");
            			result.put("status", "3");
            		}
                    
                    entBaseFileMapper.updateEntIdentStatus(updateInfo);
                    result.put("flag", "true");
                }
                else {
                	result.put("flag", "false");
                }
                result.put("message", objectNode.get("responseMessage").asText());
            }
		}
		
		return result;
	}
	
	private ObjectNode  operateEntIdent(Map<String,Object> proc,String typeId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		
		long entId=Long.valueOf(proc.get("ENT_ID").toString());
		Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
		Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        Map<String, Object> platVersion = this.yunNanMapper.getPlatVersionById(entId);
        Map<String, Object> platurl = this.yunNanMapper.getPlatUrlsById(entId);
        ObjectNode result=null;
        if(platAk==null||platVersion==null||ent==null||platurl==null) {
        	return result;
        }
        String entCode = ent.get("entCode").toString();
        String ak = platAk.get("token").toString();
        String dicVersion = platVersion.get("dicVersion").toString();
        String regVersion = platVersion.get("regVersion").toString();
		//String url=platurl.get("infoUrl").toString();  //用能单位基础信息配置交换地址
		 String url=WebConstant.yunNanIpPort;  //test
		if(typeId.equals("0")) {//增加
			url += "/YNEnergy/companyEnergyCertification/add";
			params.put("regVersion", regVersion); /*平台服务版本*/
			params.put("dicVersion", dicVersion); /*基础数据版本*/
			params.put("enterpriseCode", entCode); 
			params.put("energyPass", proc.get("STATUS"));
			if(proc.get("PASS_NAME")!=null) {
				params.put("director", proc.get("PASS_NAME"));
			}
			if(proc.get("TEL")!=null) {
				params.put("phone", proc.get("TEL"));
			}
			if(proc.get("PASS_DATE")!=null) {
				params.put("passDate", proc.get("PASS_DATE"));
			}
			params.put("passOrg", proc.get("PASS_ORG"));
		}
		else if(typeId.equals("1")) {//修改
			url += "/YNEnergy/companyEnergyCertification/update";		
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("regVersion", regVersion);
			params.put("dicVersion", dicVersion);   
			params.put("enterpriseCode", entCode); 
			params.put("energyPass", proc.get("STATUS"));
			if(proc.get("PASS_NAME")!=null) {
				params.put("director", proc.get("PASS_NAME"));
			}
			if(proc.get("TEL")!=null) {
				params.put("phone", proc.get("TEL"));
			}
			if(proc.get("PASS_DATE")!=null) {
				params.put("passDate", proc.get("PASS_DATE"));
			}
			params.put("passOrg", proc.get("PASS_ORG"));
		}
		else {//删除
			url += "/YNEnergy/companyEnergyCertification/delete";
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("enterpriseCode", entCode); 
		}
		System.out.println(JsonUtil.jsonObj2Sting(params));
		return HttpYunNan.sendHttpToYunNan(url, params, ak); 
	}	


	@Override
	public Map<String, Object> getWaterPowerGasInfo(long entId,int accType) {	
		return entBaseFileMapper.getWaterPowerGasInfoById(entId,accType);
	}

	@Override
	public int saveWaterPowerGasInfo(Map<String, Object> aoInfo) {
		return entBaseFileMapper.mergeWaterPowerGasInfo(aoInfo);
	}
	
	@Override
	public Map<String, String> uploadWaterPowerGas(String entId,String accType, String typeId) {
		Map<String, String> result=new HashMap<String,String>();
		Map<String,Object> proc = entBaseFileMapper.getWaterPowerGasInfoById(Long.parseLong(entId),Integer.parseInt(typeId));
		if(proc!=null) {
			ObjectNode objectNode = operateWaterPowerGas(proc,typeId);
			System.out.println(JsonUtil.jsonObj2Sting(objectNode));
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    Map<String,Object> updateInfo=new HashMap<String,Object>();
                    updateInfo.put("entId", entId);
                    if(typeId.equals("0")) {//增加
                    	updateInfo.put("uploadStatus", "1");
                    	result.put("status", "1");
                    	updateInfo.put("dataIndex", objectNode.get("data").get("dataIndex").asText());
            		}
            		else if(typeId.equals("1")) {//修改
            			updateInfo.put("uploadStatus", "2");
            			result.put("status", "2");
            		}
            		else {//删除
            			updateInfo.put("uploadStatus", "3");
            			updateInfo.put("dataIndex", "");
            			result.put("status", "3");
            		}
                    entBaseFileMapper.updateWPGStatus(updateInfo);
                    result.put("flag", "true");
                }
                else {
                	result.put("flag", "false");
                }
                result.put("message", objectNode.get("responseMessage").asText());
            }
		}
		
		return result;
	}
	
	private ObjectNode  operateWaterPowerGas(Map<String,Object> proc,String typeId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		
		long entId=Long.valueOf(proc.get("ENT_ID").toString());
		Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
		Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        Map<String, Object> platVersion = this.yunNanMapper.getPlatVersionById(entId);
        Map<String, Object> platurl = this.yunNanMapper.getPlatUrlsById(entId);
        ObjectNode result=null;
        if(platAk==null||platVersion==null||ent==null||platurl==null) {
        	return result;
        }
        String entCode = ent.get("entCode").toString();
        String ak = platAk.get("token").toString();
        String dicVersion = platVersion.get("dicVersion").toString();
        String regVersion = platVersion.get("regVersion").toString();
		//String url=platurl.get("infoUrl").toString();  //用能单位基础信息配置交换地址
		 String url=WebConstant.yunNanIpPort;  //test
		if(typeId.equals("0")) {//增加
			url += "/YNEnergy/companyEnergyAccount/add";
			params.put("regVersion", regVersion); /*平台服务版本*/
			params.put("dicVersion", dicVersion); /*基础数据版本*/
			params.put("enterpriseCode", entCode); 
			params.put("accountType", proc.get("ACCOUNT_TYPE"));
			params.put("accountNo", proc.get("ACCOUNT_NO"));
			params.put("accountName", proc.get("ACCOUNT_NAME"));
			params.put("provider", proc.get("PROVIDER"));
		}
		else if(typeId.equals("1")) {//修改
			url += "/YNEnergy/companyEnergyAccount/update";		
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("regVersion", regVersion);
			params.put("dicVersion", dicVersion);   
			params.put("enterpriseCode", entCode); 
			params.put("accountType", proc.get("ACCOUNT_TYPE"));
			params.put("accountNo", proc.get("ACCOUNT_NO"));
			params.put("accountName", proc.get("ACCOUNT_NAME"));
			params.put("provider", proc.get("PROVIDER"));
		}
		else {//删除
			url += "/YNEnergy/companyEnergyAccount/delete";
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("enterpriseCode", entCode); 
		}
		System.out.println(JsonUtil.jsonObj2Sting(params));
		return HttpYunNan.sendHttpToYunNan(url, params, ak); 
	}	

	@Override
	public List<Map<String, Object>> getRunYearPageList(Page page, Map<String, Object> queryCondition) {
		queryCondition.put(Dialect.pageNameField, page);
		return entBaseFileMapper.getRunYearPageData(queryCondition);
	}

	@Override
	public Map<String, Object> getRunYearInfo(Map<String, Object> queryCondition) {
		return entBaseFileMapper.getRunYearInfo(queryCondition);
	}

	@Override
	public int saveRunYearInfo(Map<String, Object> proc) {
		return entBaseFileMapper.mergeRunYearInfo(proc);
	}

	@Override
	public int deleteRunYearInfo(Map<String, Object> queryCondition) {
		return entBaseFileMapper.deleteRunYearInfo(queryCondition);
	}

	@Override
	public Map<String, String> uploadRunYearInfo(String entId, String year, String typeId) {
		Map<String, String> result=new HashMap<String,String>();
		Map<String, Object> queryCondition=new HashMap<String,Object>();
		queryCondition.put("entId", entId);
		queryCondition.put("cntYear", year);
		Map<String,Object> proc = entBaseFileMapper.getRunYearInfo(queryCondition);
		if(proc!=null) {
			ObjectNode objectNode = operateRunYearInfo(proc,typeId);
			System.out.println(JsonUtil.jsonObj2Sting(objectNode));
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    Map<String,Object> updateInfo=new HashMap<String,Object>();
                    updateInfo.put("entId", entId);
                    updateInfo.put("cntYear", year);
                    if(typeId.equals("0")) {//增加
                    	updateInfo.put("uploadStatus", "1");
                    	result.put("status", "1");
                    	updateInfo.put("dataIndex", objectNode.get("data").get("dataIndex").asText());
            		}
            		else if(typeId.equals("1")) {//修改
            			updateInfo.put("uploadStatus", "2");
            			result.put("status", "2");
            		}
            		else {//删除
            			updateInfo.put("uploadStatus", "3");
            			updateInfo.put("dataIndex", "");
            			result.put("status", "3");
            		}
                    
                    entBaseFileMapper.updateRunYearInfoStatus(updateInfo);
                    result.put("flag", "true");
                }
                else {
                	result.put("flag", "false");
                }
                result.put("message", objectNode.get("responseMessage").asText());
            }
		}
		
		return result;
	}
	
	private ObjectNode  operateRunYearInfo(Map<String,Object> proc,String typeId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		
		long entId=Long.valueOf(proc.get("ENT_ID").toString());
		Map<String, Object> ent = this.yunNanMapper.getCompanyInfoById(entId);
		Map<String, Object> platAk = this.yunNanMapper.getPlatAkById(entId);
        Map<String, Object> platVersion = this.yunNanMapper.getPlatVersionById(entId);
        Map<String, Object> platurl = this.yunNanMapper.getPlatUrlsById(entId);
        ObjectNode result=null;
        if(platAk==null||platVersion==null||ent==null||platurl==null) {
        	return result;
        }
        String entCode = ent.get("entCode").toString();
        String ak = platAk.get("token").toString();
        String dicVersion = platVersion.get("dicVersion").toString();
        String regVersion = platVersion.get("regVersion").toString();
		//String url=platurl.get("infoUrl").toString();  //用能单位基础信息配置交换地址
		String url=WebConstant.yunNanIpPort;  //test
		if(typeId.equals("0")) {//增加
			url += "/YNEnergy/companyOutputValue/add";
			params.put("regVersion", regVersion); /*平台服务版本*/
			params.put("dicVersion", dicVersion); /*基础数据版本*/
			params.put("enterpriseCode", entCode); 
			params.put("countYear", proc.get("CNT_YEAR"));
			params.put("outputValue", proc.get("OUTPUT_VALUE"));
			params.put("addedValue", proc.get("ADDED_VALUE"));
			params.put("salesIncome", proc.get("INCOME"));
		}
		else if(typeId.equals("1")) {//修改
			url += "/YNEnergy/companyOutputValue/update";		
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("regVersion", regVersion);
			params.put("dicVersion", dicVersion);   
			params.put("enterpriseCode", entCode); 
			params.put("countYear", proc.get("CNT_YEAR"));
			params.put("outputValue", proc.get("OUTPUT_VALUE"));
			params.put("addedValue", proc.get("ADDED_VALUE"));
			params.put("salesIncome", proc.get("INCOME"));
		}
		else {//删除
			url += "/YNEnergy/companyOutputValue/delete";
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("enterpriseCode", entCode); 
		}
		System.out.println(JsonUtil.jsonObj2Sting(params));
		return HttpYunNan.sendHttpToYunNan(url, params, ak); 
	}	
}
