package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.yunNan.CollectConfigManageMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.service.CollectConfigManageService;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import com.linyang.energy.utils.yunNanUtil.JsonUtil;

/**
 * @类功能说明：采集配置管理
 * @公司名称：江苏林洋电子有限公司
 * @作者：james
 * @创建时间：2020-05-14 下午03:45:47  
 * @版本：V1.0
 */
@Service
public class CollectConfigManageServiceImpl implements CollectConfigManageService {

	@Autowired
	private CollectConfigManageMapper collectConfigManageMapper;
    @Autowired
    private YunNanMapper yunNanMapper;

	@Override
	public List<Map<String, Object>> getProdProcPageList(Page page, Map<String, Object> queryCondition) {
		queryCondition.put(Dialect.pageNameField, page);
		return collectConfigManageMapper.getProdProcPageData(queryCondition);
	}
	
	@Override
	public List<Map<String, Object>> getAllProdProcList(Map<String, Object> queryCondition) {
		return collectConfigManageMapper.getAllProdProcData(queryCondition);
	}

	@Override
	public Map<String, Object> getProdProcById(long procId) {
		return collectConfigManageMapper.getProcById(procId);
	}
	
	 /**  
   	 * 函数功能说明  :获取依赖工序的子元素数量
   	 */
	@Override
	public int getProcSonCounts(long procId) {
		return collectConfigManageMapper.getIfCanDeleteProc(procId);
	}

	@Override
	public int addProdProc(Map<String, Object> proc) {		
		return collectConfigManageMapper.addProdProc(proc);
	}

	@Override
	public int updateProdProc(Map<String, Object> proc) {
		return collectConfigManageMapper.updateProdProc(proc);
	}

	@Override
	public int deleteProdProc(long procId) {
		return collectConfigManageMapper.deleteProdProc(procId);
	}
	
	@Override
	public Map<String, String> uploadProdProc(String procId, String typeId) {
		Map<String, String> result=new HashMap<String,String>();
		Map<String,Object> proc = collectConfigManageMapper.getProcById(Long.parseLong(procId));
		if(proc!=null) {
			ObjectNode objectNode = operateProdProc(proc,typeId);
			System.out.println(JsonUtil.jsonObj2Sting(objectNode));
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    Map<String,Object> updateInfo=new HashMap<String,Object>();
                    updateInfo.put("procId", procId);
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
                    collectConfigManageMapper.updateProdProcStatus(updateInfo);
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
	
	private ObjectNode  operateProdProc(Map<String,Object> proc,String typeId)
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
        String url=WebConstant.yunNanIpPort;//test
		if(typeId.equals("0")) {//增加
			url += "/YNEnergy/processConfigure/add";
			params.put("regVersion",regVersion); /*平台服务版本*/
			params.put("dicVersion", dicVersion); /*基础数据版本*/
			params.put("enterpriseCode", entCode); 
			params.put("processCode", proc.get("PROCESS_CODE"));
			params.put("processName", proc.get("PROCESS_NAME"));
			//params.put("remark", "");
		}
		else if(typeId.equals("1")) {//修改
			url += "/YNEnergy/processConfigure/update";		
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("regVersion", regVersion);
			params.put("dicVersion", dicVersion);   
			params.put("enterpriseCode", entCode); 
			params.put("processName", proc.get("PROCESS_NAME"));
			params.put("processCode", proc.get("PROCESS_CODE"));
			//params.put("remark", "");
		}
		else {//删除
			url += "/YNEnergy/processConfigure/delete";
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("enterpriseCode", entCode); 
		}
		System.out.println(JsonUtil.jsonObj2Sting(params));
		return HttpYunNan.sendHttpToYunNan(url, params, ak); 
	}	

	@Override
	public List<Map<String, Object>> getProdPCode(long entId) {
		return collectConfigManageMapper.getProdPCode(entId);
	}

	@Override
	public List<Map<String, Object>> getProdPUnitCode(long entId) {
		return collectConfigManageMapper.getProdPUnitCode(entId);
	}

	@Override
	public List<Map<String, Object>> getProcUnitPageList(Page page, Map<String, Object> queryCondition) {
		queryCondition.put(Dialect.pageNameField, page);
		return collectConfigManageMapper.getProcUnitPageData(queryCondition);
	}

	@Override
	public Map<String, Object> getProcUnitById(long procId) {
		return collectConfigManageMapper.getProcUnitById(procId);
	}
	
	 /**  
   	 * 函数功能说明  :获取依赖工序单元的子元素数量
   	 */
	@Override
	public int getPUnitSonCounts(long unitId) {
		return collectConfigManageMapper.getPUnitSonCounts(unitId);
	}

	@Override
	public int addProcUnit(Map<String, Object> proc) {
		return collectConfigManageMapper.addProcUnit(proc);
	}

	@Override
	public int updateProcUnit(Map<String, Object> proc) {
		return collectConfigManageMapper.updateProcUnit(proc);
	}

	@Override
	public int deleteProcUnit(long procId) {
		return collectConfigManageMapper.deleteProcUnit(procId);
	}
	
	@Override
	public Map<String, String> uploadProcUnit(String unitId, String typeId) {
		Map<String, String> result=new HashMap<String,String>();
		Map<String,Object> proc = collectConfigManageMapper.getProcUnitById(Long.parseLong(unitId));
		if(proc!=null) {
			ObjectNode objectNode = operateProcUnit(proc,typeId);
			System.out.println(JsonUtil.jsonObj2Sting(objectNode));
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    Map<String,Object> updateInfo=new HashMap<String,Object>();
                    updateInfo.put("unitId", unitId);
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
                    collectConfigManageMapper.updateProcUnitStatus(updateInfo);
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
	
	private ObjectNode  operateProcUnit(Map<String,Object> proc,String typeId)
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
        String url=WebConstant.yunNanIpPort;//test
		if(typeId.equals("0")) {//增加
			url +=  "/YNEnergy/unitConfigure/add";
			params.put("regVersion", regVersion); /*平台服务版本*/
			params.put("dicVersion", dicVersion); /*基础数据版本*/
			params.put("enterpriseCode", entCode); 
			params.put("unitCode", proc.get("UNIT_CODE"));
			params.put("unitName", proc.get("UNIT_NAME"));
			params.put("processCode", proc.get("PROCESS_CODE"));
			if(proc.get("COMM_DATE")!=null) {
				params.put("commDate", proc.get("COMM_DATE"));
			}
			if(proc.get("DESIGNED_CAPC")!=null) {
				params.put("designedCapacity", proc.get("DESIGNED_CAPC"));
			}
			//params.put("remark", "");
		}
		else if(typeId.equals("1")) {//修改
			url += "/YNEnergy/unitConfigure/update";		
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("regVersion", regVersion);
			params.put("dicVersion", dicVersion);   
			params.put("enterpriseCode", entCode); 
			params.put("unitCode", proc.get("UNIT_CODE"));
			params.put("unitName", proc.get("UNIT_NAME"));
			params.put("processCode", proc.get("PROCESS_CODE"));
			if(proc.get("COMM_DATE")!=null) {
				params.put("commDate", proc.get("COMM_DATE"));
			}
			if(proc.get("DESIGNED_CAPC")!=null) {
				params.put("designedCapacity", proc.get("DESIGNED_CAPC"));
			}
			//params.put("remark", "");
		}
		else {//删除
			url += "/YNEnergy/unitConfigure/delete";
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("enterpriseCode", entCode); 
		}
		 System.out.println(JsonUtil.jsonObj2Sting(params));
		return HttpYunNan.sendHttpToYunNan(url, params, ak); 
	}	

	@Override
	public List<Map<String, Object>> getConDevicePageList(Page page, Map<String, Object> queryCondition) {
		queryCondition.put(Dialect.pageNameField, page);
		return collectConfigManageMapper.getConDeivcePageData(queryCondition);
	}

	@Override
	public Map<String, Object> getConDeviceById(long procId) {
		return collectConfigManageMapper.getConDeivceById(procId);
	}
	
	/**  
   	 * 函数功能说明  :获取依赖工序的子元素数量0
   	*/
	@Override
	public int getDeviceSonCounts(long deviceId) {
		return collectConfigManageMapper.getDeviceSonCounts(deviceId);
	}

	@Override
	public int addConDevice(Map<String, Object> proc) {
		return collectConfigManageMapper.addConDeivce(proc);
	}

	@Override
	public int updateConDevice(Map<String, Object> proc) {
		return collectConfigManageMapper.updateConDeivce(proc);
	}

	@Override
	public int deleteConDevice(long procId) {
		return collectConfigManageMapper.deleteConDeivce(procId);
	}
	
	@Override
	public Map<String, String> uploadConsumeDevice(String procId, String typeId) {
		Map<String, String> result=new HashMap<String,String>();
		Map<String,Object> proc = collectConfigManageMapper.getConDeivceById(Long.parseLong(procId));
		if(proc!=null) {
			ObjectNode objectNode = operateConsumeDevice(proc,typeId);
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                System.out.println(JsonUtil.jsonObj2Sting(responseCode));
                if(responseCode.equals("200")){
                    Map<String,Object> updateInfo=new HashMap<String,Object>();
                    updateInfo.put("deviceId", procId);
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
                    collectConfigManageMapper.updateConDeivceStatus(updateInfo);
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
	
	private ObjectNode  operateConsumeDevice(Map<String,Object> proc,String typeId)
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
        String url=WebConstant.yunNanIpPort;//test
		if(typeId.equals("0")) {//增加
			url += "/YNEnergy/deviceConfigure/add";
			params.put("regVersion", regVersion); /*平台服务版本*/
			params.put("dicVersion", dicVersion); /*基础数据版本*/
			params.put("enterpriseCode", entCode); /*统一社会信用代码  */
			params.put("deviceNo", proc.get("DEVICE_TYPE").toString()+proc.get("DEVICE_NO").toString());
			params.put("deviceName", proc.get("DEVICE_NAME"));
			if(proc.get("MODEL")!=null) {
				params.put("model", proc.get("MODEL"));
			}
			params.put("location", proc.get("LOCATION"));
			if(proc.get("DEPT")!=null) {
				params.put("dept", proc.get("DEPT"));
			}
			params.put("deviceType", proc.get("DEVICE_TYPE"));
			if(proc.get("USING_DATE")!=null) {
				params.put("usingDate", proc.get("USING_DATE"));
			}
			params.put("currentState", "正常");
			if(proc.get("MANUFACTURER")!=null) {
				params.put("manufacturer", proc.get("MANUFACTURER"));
			}
			if(proc.get("ENERGY_CODE")!=null) {
				params.put("energyCode", proc.get("ENERGY_CODE"));
			}
			if(proc.get("ENERGY_VALUE")!=null) {
				params.put("energyValue", proc.get("ENERGY_VALUE")+"瓦"); 
			}
			params.put("processUnitCode", proc.get("PROCESS_CODE").toString()+proc.get("UNIT_CODE").toString()); 
			//params.put("remark", ""); 
		}
		else if(typeId.equals("1")) {//修改
			url +=  "/YNEnergy/deviceConfigure/update";		
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("regVersion", regVersion);
			params.put("dicVersion", dicVersion);   
			params.put("enterpriseCode", entCode); 
			params.put("deviceNo", proc.get("DEVICE_TYPE").toString()+proc.get("DEVICE_NO").toString());
			params.put("deviceName", proc.get("DEVICE_NAME"));
			if(proc.get("MODEL")!=null) {
				params.put("model", proc.get("MODEL"));
			}
			params.put("location", proc.get("LOCATION"));
			if(proc.get("DEPT")!=null) {
				params.put("dept", proc.get("DEPT"));
			}
			params.put("deviceType", proc.get("DEVICE_TYPE"));
			if(proc.get("USING_DATE")!=null) {
				params.put("usingDate", proc.get("USING_DATE"));
			}
			params.put("currentState", "正常");
			if(proc.get("MANUFACTURER")!=null) {
				params.put("manufacturer", proc.get("MANUFACTURER"));
			}
			if(proc.get("ENERGY_CODE")!=null) {
				params.put("energyCode", proc.get("ENERGY_CODE"));
			}
			if(proc.get("ENERGY_VALUE")!=null) {
				params.put("energyValue", proc.get("ENERGY_VALUE")+"瓦"); 
			}
			params.put("processUnitCode", proc.get("PROCESS_CODE").toString()+proc.get("UNIT_CODE").toString()); 
			//params.put("remark", ""); 
		}
		else {//删除
			url +=  "/YNEnergy/deviceConfigure/delete";
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("enterpriseCode", entCode); /*统一社会信用代码  */
		}
		System.out.println(JsonUtil.jsonObj2Sting(params));
		return HttpYunNan.sendHttpToYunNan(url, params, ak); 
	}	

	@Override
	public List<Map<String, Object>> getDataConfigPageList(Page page, Map<String, Object> queryCondition) {
		queryCondition.put(Dialect.pageNameField, page);
		return collectConfigManageMapper.getDataConfigPageData(queryCondition);
	}
	
	@Override
	public List<Map<String, Object>> getDataConfigList(Map<String, Object> queryCondition) {
		return collectConfigManageMapper.getDataConfigData(queryCondition);
	}

	@Override
	public Map<String, Object> getDataConfigById(long procId) {	
		return collectConfigManageMapper.getDataConfigById(procId);
	}
	
	@Override
	public List<Map<String, Object>> getMpedListByLedgerID(long entId) {
		return collectConfigManageMapper.getMpedListByLedgerID(entId);
	}

	@Override
	public int addDataConfig(Map<String, Object> proc) {	
		return collectConfigManageMapper.addDataConfig(proc);
	}

	@Override
	public int updateDataConfig(Map<String, Object> proc) {
		return collectConfigManageMapper.updateDataConfig(proc);
	}

	@Override
	public int deleteDataConfig(long procId) {
		return collectConfigManageMapper.deleteDataConfig(procId);
	}
	
	
	@Override
	public Map<String, String> uploadDataConfig(String dataId, String typeId) {
		Map<String, String> result=new HashMap<String,String>();
		Map<String,Object> proc = collectConfigManageMapper.getDataConfigById(Long.parseLong(dataId));
		if(proc!=null) {
			ObjectNode objectNode = operateDataConfig(proc,typeId);
			System.out.println(JsonUtil.jsonObj2Sting(objectNode));
            if(null != objectNode && objectNode.has("responseCode")){
                String responseCode = objectNode.get("responseCode").toString();
                if(responseCode.equals("200")){
                    Map<String,Object> updateInfo=new HashMap<String,Object>();
                    updateInfo.put("dataId", dataId);
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
                    collectConfigManageMapper.updateDataConfigStatus(updateInfo);
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
	
	private ObjectNode  operateDataConfig(Map<String,Object> proc,String typeId)
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
        String url=WebConstant.yunNanIpPort;//test
		if(typeId.equals("0")) {   //增加
			url += "/YNEnergy/dataCollectConfigure/add";
			params.put("regVersion", regVersion); /*平台服务版本*/
			params.put("dicVersion", dicVersion); /*基础数据版本*/
			params.put("enterpriseCode", entCode); /*统一社会信用代码  */
			params.put("collectItemName", proc.get("DATA_NAME"));
			params.put("dataCode", proc.get("DATA_CODE"));
			params.put("processCode", proc.get("PROCESS_CODE"));
			params.put("processUnitCode", proc.get("UNIT_CODE"));
			params.put("equipmentCode", proc.get("DEVICE_TYPE"));//设备类型
			params.put("equipmentUnitCode", proc.get("DEVICE_TYPE").toString()+proc.get("DEVICE_NO").toString());
			params.put("energyClassCode", proc.get("DTYPE_ID"));
			params.put("energyTypeCode", proc.get("EN_TYPE_ID"));
			params.put("dataUsageCode", proc.get("DATAUSAGE_ID"));
			params.put("inputType", proc.get("INPUT_TYPE"));
			if(proc.get("DATA_MAX")!=null) {
				params.put("dataValueMax", proc.get("DATA_MAX"));
			}
			if(proc.get("DATA_MIN")!=null) {
				params.put("dataValueMin", proc.get("DATA_MIN"));
			}
			params.put("statType", proc.get("FREQUCE"));
			params.put("collectSystemName", "林洋能效管理系统");
			if(proc.get("SCOPE")!=null) {
				params.put("scope", proc.get("SCOPE"));
			}
			//params.put("remark", "");
		}
		else if(typeId.equals("1")) {//修改
			url +=  "/YNEnergy/dataCollectConfigure/update";		
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("regVersion", regVersion);
			params.put("dicVersion", dicVersion);   
			params.put("enterpriseCode", entCode); 
			params.put("collectItemName", proc.get("DATA_NAME"));
			params.put("dataCode", proc.get("DATA_CODE"));
			params.put("processCode", proc.get("PROCESS_CODE"));
			params.put("processUnitCode", proc.get("UNIT_CODE"));
			params.put("equipmentCode", proc.get("DEVICE_TYPE"));//设备类型
			params.put("equipmentUnitCode", proc.get("DEVICE_TYPE").toString()+proc.get("DEVICE_NO").toString());
			params.put("energyClassCode", proc.get("DTYPE_ID"));
			params.put("energyTypeCode", proc.get("EN_TYPE_ID"));
			params.put("dataUsageCode", proc.get("DATAUSAGE_ID"));
			params.put("inputType", proc.get("INPUT_TYPE"));
			if(proc.get("DATA_MAX")!=null) {
				params.put("dataValueMax", proc.get("DATA_MAX"));
			}
			if(proc.get("DATA_MIN")!=null) {
				params.put("dataValueMin", proc.get("DATA_MIN"));
			}
			params.put("statType", proc.get("FREQUCE"));
			//params.put("collectSystemName", "");
			if(proc.get("SCOPE")!=null) {
				params.put("scope", proc.get("SCOPE"));
			}
			//params.put("remark", "");
		}
		else {//删除
			url +=  "/YNEnergy/dataCollectConfigure/delete";
			params.put("dataIndex", proc.get("DATA_INDEX"));
			params.put("enterpriseCode", entCode);
		}
		System.out.println(JsonUtil.jsonObj2Sting(params));
		return HttpYunNan.sendHttpToYunNan(url, params, ak); 
	}	
	

	@Override
	public List<Map<String, Object>> getEqpType() {
		return collectConfigManageMapper.getEqpType();
	}

	@Override
	public List<Map<String, Object>> getEnType() {
		return collectConfigManageMapper.getEnType();
	}

	@Override
	public List<Map<String, Object>> getAllProcUnitList(Map<String, Object> queryCondition) {
		return collectConfigManageMapper.getAllProcUnitData(queryCondition);
	}

	@Override
	public List<Map<String, Object>> getAllDeviceList(Map<String, Object> queryCondition) {
		return collectConfigManageMapper.getAllDeviceData(queryCondition);
	}

	@Override
	public List<Map<String, Object>> getDataType() {
		return collectConfigManageMapper.getDataType();
	}

	@Override
	public List<Map<String, Object>> getDataUsage() {
		return collectConfigManageMapper.getDataUsage();
	}

	@Override
	public List<Map<String, Object>> getDataSrc() {
		return collectConfigManageMapper.getDataSrc();
	}

	@Override
	public List<Map<String, Object>> getEnTypeList(String dTypeId) {
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		switch(dTypeId) {
			case "01":
				result=collectConfigManageMapper.getEnType();
				break;
			case "02":
				result=collectConfigManageMapper.getEnType();
				break;
			case "03":
				result=collectConfigManageMapper.getWPED();
				break;
			case "04":
				result=collectConfigManageMapper.getNonenprod();
				break;
			case "06":
				//result=collectConfigManageMapper.getEnType();
				break;
			case "07":
				//result=collectConfigManageMapper.getEnType();
				break;
			case "08":
				result=collectConfigManageMapper.getEEI();
				break;
			case "09":
				//result=collectConfigManageMapper.getEnType();
				break;
			case "10":
				result=collectConfigManageMapper.getOtherDataCode();
				break;
		}
		return result;
	}
}
