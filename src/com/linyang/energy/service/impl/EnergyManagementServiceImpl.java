package com.linyang.energy.service.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.yunNan.EnergyManagementMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.service.EnergyManagementService;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:43 2020/5/21
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class EnergyManagementServiceImpl implements EnergyManagementService {
	
	@Autowired
	private EnergyManagementMapper energyManagementMapper;
	
	@Autowired
	private YunNanMapper yunNanMapper;
	
	// add or update method by catkins.
	// date 2020/5/22
	// Modify the content: 企业联系人上传url
	private static final String EN_MANAGEMENT_ADD_URL = "/YNEnergy/companyContacter/add";
	
	private static final String EN_MANAGEMENT_MODIFY_URL = "/YNEnergy/companyContacter/update";
	
	private static final String EN_MANAGEMENT_DELETE_URL = "/YNEnergy/companyContacter/delete";
	//end
	
	// add or update method by catkins.
	// date 2020/5/22
	// Modify the content:  管理人员上传url
	private static final String COMPANY_ADD_URL = "/YNEnergy/companyEnergyManager/add";
	
	private static final String COMPANY_MODIFY_URL = "/YNEnergy/companyEnergyManager/update";
	
	private static final String COMPANY_DELETE_URL = "/YNEnergy/companyEnergyManager/delete";
	//end

	// add or update method by catkins.
	// date 2020/5/22
	// Modify the content:  项目负责人上传url
	private static final String MONITOR_ADD_URL = "/YNEnergy/companyEnergyMonitor/add";
	
	private static final String MONITOR_MODIFY_URL = "/YNEnergy/companyEnergyMonitor/update";
	
	private static final String MONITOR_DELETE_URL = "/YNEnergy/companyEnergyMonitor/delete";
	//end
	
	// add or update method by catkins.
	// date 2020/5/22
	// Modify the content:  计量人员上传url
	private static final String CALCULATER_ADD_URL = "/YNEnergy/companyCalculater/add";
	
	private static final String CALCULATER_MODIFY_URL = "/YNEnergy/companyCalculater/update";
	
	private static final String CALCULATER_DELETE_URL = "/YNEnergy/companyCalculater/delete";
	//end
	
	
	/**
	 * 查询计量器管理列表数据
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/14 14:32
	 */
	@Override
	public List<Map<String, Object>> queryenManagementPageList(Page page, Map<String, Object> queryMap) {
		List<Map<String, Object>> maps = null;
		if(page != null) {
			Map<String, Object> map = new HashMap<String, Object>( queryMap );
			map.put( Dialect.pageNameField, page );
			maps = energyManagementMapper.queryenManagementPageList( map );
		}
		return maps;
	}
	
	/**
	 * 新增和修改方法
	 * @author catkins.
	 * @param params
	 * @return java.lang.Integer
	 * @exception
	 * @date 2020/5/20 16:35
	 */
	@Override
	public Integer saveAndModify(Map<String, Object> params) {
		if( params == null || params.size() <= 0 ){
			return null;
		}
		Integer result = -1;
		try {
			if(params.containsKey( "operationType" ) && params.get( "operationType" ).toString().equals( "1" )){
				params.put( "enmId", SequenceUtils.getDBSequence() );
				result = energyManagementMapper.saveData( params )>0?101:500;
			} else {
				params.put("changeDate",new Date());
				result = energyManagementMapper.modifyData( params )>0?201:500;
			}
		} catch (Exception e) {
			result = 555;
		}
		return result;
	}
	
	@Override
	public Map<String, Object> queryDataById(Long enmId) {
		return energyManagementMapper.queryDataById( enmId );
	}
	
	@Override
	public boolean deleteRecordData(Long enmId) {
		return energyManagementMapper.deleteRecordData( enmId ) > 0;
	}
	
	/**
	 * 企业联系人上传
	 * @author catkins.
	 * @param params
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:15
	 */
	@Override
	public Map<String, Object> uploadData(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		
		int uploadType = 0;
		List<String> enmIds = new ArrayList<String>( 0 );
		if(params != null){
			if( params.containsKey( "uploadStatus" ) ){
				uploadType = Integer.parseInt( params.get( "uploadStatus" ).toString() );
			}
			if( params.containsKey( "enmIds" ) ){
				enmIds = (List<String>) params.get( "enmIds" );
			}
		}
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.parseLong( enmIds.get( 0 ).substring( enmIds.get( 0 ).indexOf( "," )+1 ) ) );
		
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.parseLong( enmIds.get( 0 ).substring( enmIds.get( 0 ).indexOf( "," )+1 ) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		
		Map<String, Object> map = assemblyData( Long.parseLong( enmIds.get( 0 ).substring( 0,enmIds.get( 0 ).indexOf( "," ) ) ), uploadType );
		ObjectNode jsonNodes = null;
		String responseMessage = "信息已存在,请勿重复操作";
		if( !map.containsKey( "responseCode" ) ){
			if (uploadType == 1) {
//				JsonNodeFactory factory = new JsonNodeFactory(false);
//				jsonNodes = factory.objectNode();
//				ObjectNode jsonNodes1 = factory.objectNode();
//				jsonNodes1.put( "dataIndex","B74174A8B74174A8" );
//				jsonNodes.put( "data", jsonNodes1 );
//				jsonNodes.put( "responseCode", 200 );
//				jsonNodes.put( "responseMessage", "OK" );
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+EN_MANAGEMENT_ADD_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 2 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+EN_MANAGEMENT_MODIFY_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 3 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+EN_MANAGEMENT_DELETE_URL,map,ak.get( "token" ).toString());
			}
			String responseCode = jsonNodes.get( "responseCode" ).toString();
			resultMap.put( "responseCode", responseCode);
			responseMessage = jsonNodes.get( "responseMessage" ).toString();
			resultMap.put( "responseMessage", responseMessage.substring( 1,responseMessage.length()-1 ));
			if ( responseCode.equals( "200" ) && uploadType != 2 ) {
				Map<String,Object> updateMap = new HashMap<String,Object>( 0 );
				if ( uploadType == 1 ) {
					String dataIndex = jsonNodes.get( "data" ).get( "dataIndex" ).toString();
					dataIndex = dataIndex.substring( 1,dataIndex.length()-1 );
					updateMap.put( "dataIndex",dataIndex );
					updateMap.put( "uploadStatus","1" );
				} else if( uploadType == 3 ){
					updateMap.put( "dataIndex","" );
					updateMap.put( "uploadStatus","0" );
				}
				updateMap.put( "enmId",Long.parseLong( enmIds.get( 0 ).substring( 0,enmIds.get( 0 ).indexOf( "," ) ) ) );
				energyManagementMapper.modifyData( updateMap );
			}
		} else {
			resultMap.putAll( map );
		}
		enmIds.remove( 0 );
		resultMap.put( "enmIds", enmIds );
		resultMap.put( "uploadStatus", uploadType );
		resultMap.put( "responseMessage",responseMessage );
		return resultMap;
	}
	
	/**
	 * 企业联系人组装上传参数
	 * @author catkins.
	 * @param enmId
	 * @param uploadType
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:16
	 */
	private Map<String,Object> assemblyData(Long enmId,Integer uploadType){
		Map<String, Object> dataMap = energyManagementMapper.queryDataById(enmId);
		if( dataMap == null || dataMap.size() <= 0 ){
			dataMap = new HashMap<String, Object>( 0 );
			dataMap.put( "responseCode", "500");
			dataMap.put( "responseMessage", "500");
			return dataMap;
		}
		if( !dataMap.get( "UPLOAD_STATUS" ).toString().equals( "1" ) && (uploadType == 2 || uploadType == 3)){
			dataMap.put( "responseCode", "505");
			dataMap.put( "responseMessage", "505");
			return dataMap;
		}
		if( dataMap.get( "UPLOAD_STATUS" ).toString().equals( "1" ) && uploadType == 1 ){
			dataMap.put( "responseCode", "506");
			dataMap.put( "responseMessage", "506");
			return dataMap;
		}
		Map<String,Object> map = new HashMap<String,Object>( 0 );
		//如果是删除方法,报文不同,直接return
		if( uploadType == 3 ){
			map.put( "dataIndex",dataMap.get( "DATA_INDEX" ) );
			map.put( "enterpriseCode",dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
			return map;
		}
		if ( uploadType == 2 ) {
			map.put( "dataIndex",dataMap.get( "DATA_INDEX" ) );
		}
		
		Map<String, Object> version = yunNanMapper.getPlatVersionById( Long.parseLong( dataMap.get( "ENT_ID" ).toString() ) );
		map.put( "regVersion",version.get( "regVersion" ) );/*平台服务版本*/
		map.put( "dicVersion",version.get( "dicVersion" ) );/*基础数据版本*/
		map.put( "enterpriseCode" , dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
		map.put( "contacterName" , dataMap.get( "ENM_NAME" ) );/*姓名*/
		map.put( "phone" , dataMap.get( "TEL" ) );/*电话*/
		map.put( "dept" , dataMap.get( "DEPT" ) );/*办公室*/
		map.put( "job" , dataMap.get( "JOB" ) );/*职位、或者岗位*/
		map.put( "fax" , dataMap.get( "FAX" ) );/*传真*/
		map.put( "email" , dataMap.get( "EMAIL" ) );/*传真*/
		map.put( "remark" , "" );/*备注*/
		return map;
	}
	
	
	/**
	 * 管理人员信息上传
	 * @author catkins.
	 * @param map
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:55
	 */
	@Override
	public Map<String, Object> uploadCompany(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		
		int uploadType = 0;
		List<String> enmIds = new ArrayList<String>( 0 );
		if(params != null){
			if( params.containsKey( "uploadStatus" ) ){
				uploadType = Integer.parseInt( params.get( "uploadStatus" ).toString() );
			}
			if( params.containsKey( "enmIds" ) ){
				enmIds = (List<String>) params.get( "enmIds" );
			}
		}
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.parseLong( enmIds.get( 0 ).substring( enmIds.get( 0 ).indexOf( "," )+1 ) ) );
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.parseLong( enmIds.get( 0 ).substring( enmIds.get( 0 ).indexOf( "," )+1 ) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		Map<String, Object> map = assemblyCompany( Long.parseLong( enmIds.get( 0 ).substring( 0,enmIds.get( 0 ).indexOf( "," ) ) ), uploadType );
		ObjectNode jsonNodes = null;
		String responseMessage = "信息已存在,请勿重复操作";
		if( !map.containsKey( "responseCode" ) ){
			if (uploadType == 1) {
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+COMPANY_ADD_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 2 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+COMPANY_MODIFY_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 3 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+COMPANY_DELETE_URL,map,ak.get( "token" ).toString());
			}
			String responseCode = jsonNodes.get( "responseCode" ).toString();
			resultMap.put( "responseCode", responseCode);
			responseMessage = jsonNodes.get( "responseMessage" ).toString();
			resultMap.put( "responseMessage", responseMessage.substring( 1,responseMessage.length()-1 ));
			System.out.println(jsonNodes.toString());
			if ( responseCode.equals( "200" ) && uploadType != 2) {
				Map<String,Object> updateMap = new HashMap<String,Object>( 0 );
				if ( uploadType == 1 ) {
					String dataIndex = jsonNodes.get( "data" ).get( "dataIndex" ).toString();
					dataIndex = dataIndex.substring( 1,dataIndex.length()-1 );
					updateMap.put( "dataIndex",dataIndex );
					updateMap.put( "uploadStatus","1" );
				} else if( uploadType == 3 ){
					updateMap.put( "dataIndex","" );
					updateMap.put( "uploadStatus","0" );
				}
				updateMap.put( "enmId",Long.parseLong( enmIds.get( 0 ).substring( 0,enmIds.get( 0 ).indexOf( "," ) ) ) );
				energyManagementMapper.modifyData( updateMap );
			}
		} else {
			resultMap.putAll( map );
		}
		enmIds.remove( 0 );
		resultMap.put( "enmIds", enmIds );
		resultMap.put( "uploadStatus", uploadType );
		resultMap.put( "responseMessage",responseMessage );
		return resultMap;
	}
	
	
	
	private Map<String,Object> assemblyCompany(Long enmId,Integer uploadType){
		Map<String, Object> dataMap = energyManagementMapper.queryDataById(enmId);
		if( dataMap == null || dataMap.size() <= 0 ){
			dataMap = new HashMap<String, Object>( 0 );
			dataMap.put( "responseCode", "500");
			dataMap.put( "responseMessage", "500");
			return dataMap;
		}
		if( !dataMap.get( "UPLOAD_STATUS" ).toString().equals( "1" ) && (uploadType == 2 || uploadType == 3)){
			dataMap.put( "responseCode", "505");
			dataMap.put( "responseMessage", "505");
			return dataMap;
		}
		if( dataMap.get( "UPLOAD_STATUS" ).toString().equals( "1" ) && uploadType == 1 ){
			dataMap.put( "responseCode", "506");
			dataMap.put( "responseMessage", "506");
			return dataMap;
		}
		Map<String,Object> map = new HashMap<String,Object>( 0 );
		//如果是删除方法,报文不同,直接return
		if( uploadType == 3 ){
			map.put( "dataIndex",dataMap.get( "DATA_INDEX" ) );
			map.put( "enterpriseCode",dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
			return map;
		}
		if ( uploadType == 2 ) {
			map.put( "dataIndex",dataMap.get( "DATA_INDEX" ) );
		}
		Map<String, Object> version = yunNanMapper.getPlatVersionById( Long.parseLong( dataMap.get( "ENT_ID" ).toString() ) );
		map.put( "regVersion",version.get( "regVersion" ) );/*平台服务版本*/
		map.put( "dicVersion",version.get( "dicVersion" ) );/*基础数据版本*/
		map.put( "enterpriseCode" , dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
		map.put( "manager" , dataMap.get( "ENM_NAME" ) );/*姓名*/
		map.put( "phone" , dataMap.get( "TEL" ) );/*电话*/
		map.put( "dept" , dataMap.get( "DEPT" ) );/*办公室*/
		map.put( "job" , dataMap.get( "JOB" ) );/*职位、或者岗位*/
		map.put( "trainInfo" , dataMap.get( "TRAIN_INFO" ) );/*培训或者取证情况描述*/
		map.put( "remark" , "" );/*备注*/
		return map;
	}
	
	/**
	 * 项目负责人信息上传
	 * @author catkins.
	 * @param map
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:55
	 */
	@Override
	public Map<String, Object> uploadMonitor(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		
		int uploadType = 0;
		List<String> enmIds = new ArrayList<String>( 0 );
		if(params != null){
			if( params.containsKey( "uploadStatus" ) ){
				uploadType = Integer.parseInt( params.get( "uploadStatus" ).toString() );
			}
			if( params.containsKey( "enmIds" ) ){
				enmIds = (List<String>) params.get( "enmIds" );
			}
		}
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.parseLong( enmIds.get( 0 ).substring( enmIds.get( 0 ).indexOf( "," )+1 ) ) );
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.parseLong( enmIds.get( 0 ).substring( enmIds.get( 0 ).indexOf( "," )+1 ) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		Map<String, Object> map = assemblyMonitor( Long.parseLong( enmIds.get( 0 ).substring( 0,enmIds.get( 0 ).indexOf( "," ) ) ), uploadType );
		ObjectNode jsonNodes = null;
		String responseMessage = "信息已存在,请勿重复操作";
		if( !map.containsKey( "responseCode" ) ){
			if (uploadType == 1) {
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+MONITOR_ADD_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 2 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+MONITOR_MODIFY_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 3 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+MONITOR_DELETE_URL,map,ak.get( "token" ).toString());
			}
			String responseCode = jsonNodes.get( "responseCode" ).toString();
			resultMap.put( "responseCode", responseCode);
			responseMessage = jsonNodes.get( "responseMessage" ).toString();
			resultMap.put( "responseMessage", responseMessage.substring( 1,responseMessage.length()-1 ));
			if ( responseCode.equals( "200" ) && uploadType != 2) {
				Map<String,Object> updateMap = new HashMap<String,Object>( 0 );
				if ( uploadType == 1 ) {
					String dataIndex = jsonNodes.get( "data" ).get( "dataIndex" ).toString();
					dataIndex = dataIndex.substring( 1,dataIndex.length()-1 );
					updateMap.put( "dataIndex",dataIndex );
					updateMap.put( "uploadStatus","1" );
				} else if( uploadType == 3 ){
					updateMap.put( "dataIndex","" );
					updateMap.put( "uploadStatus","0" );
				}
				updateMap.put( "enmId",Long.parseLong( enmIds.get( 0 ).substring( 0,enmIds.get( 0 ).indexOf( "," ) ) ) );
				energyManagementMapper.modifyData( updateMap );
			}
		} else {
			resultMap.putAll( map );
		}
		enmIds.remove( 0 );
		resultMap.put( "enmIds", enmIds );
		resultMap.put( "uploadStatus", uploadType );
		resultMap.put( "responseMessage",responseMessage );
		return resultMap;
	}
	
	private Map<String,Object> assemblyMonitor(Long enmId,Integer uploadType){
		Map<String, Object> dataMap = energyManagementMapper.queryDataById(enmId);
		if( dataMap == null || dataMap.size() <= 0 ){
			dataMap = new HashMap<String, Object>( 0 );
			dataMap.put( "responseCode", "500");
			dataMap.put( "responseMessage", "500");
			return dataMap;
		}
		if( !dataMap.get( "UPLOAD_STATUS" ).toString().equals( "1" ) && (uploadType == 2 || uploadType == 3)){
			dataMap.put( "responseCode", "505");
			dataMap.put( "responseMessage", "505");
			return dataMap;
		}
		if( dataMap.get( "UPLOAD_STATUS" ).toString().equals( "1" ) && uploadType == 1 ){
			dataMap.put( "responseCode", "506");
			dataMap.put( "responseMessage", "506");
			return dataMap;
		}
		Map<String,Object> map = new HashMap<String,Object>( 0 );
		//如果是删除方法,报文不同,直接return
		if( uploadType == 3 ){
			map.put( "dataIndex",dataMap.get( "DATA_INDEX" ) );
			map.put( "enterpriseCode",dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
			return map;
		}
		if ( uploadType == 2 ) {
			map.put( "dataIndex",dataMap.get( "DATA_INDEX" ) );
		}
		
		Map<String, Object> version = yunNanMapper.getPlatVersionById( Long.parseLong( dataMap.get( "ENT_ID" ).toString() ) );
		map.put( "regVersion",version.get( "regVersion" ) );/*平台服务版本*/
		map.put( "dicVersion",version.get( "dicVersion" ) );/*基础数据版本*/
		map.put( "enterpriseCode" , dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
		map.put( "monitor" , dataMap.get( "ENM_NAME" ) );/*姓名*/
		map.put( "phone" , dataMap.get( "TEL" ) );/*电话*/
		map.put( "dept" , dataMap.get( "DEPT" ) );/*办公室*/
		map.put( "job" , dataMap.get( "JOB" ) );/*职位、或者岗位*/
		map.put( "remark" , "" );/*备注*/
		return map;
	}
	
	
	@Override
	public Integer savePic(Map<String, Object> params) {
		return energyManagementMapper.saveCalData(params);
	}
	
	@Override
	public Integer modifyCalData(Map<String, Object> map) {
		return energyManagementMapper.modifyCalData( map );
	}
	
	/**
	 * 新增和修改方法(计量人员配置)
	 * @author catkins.
	 * @param params
	 * @return java.lang.Integer
	 * @exception
	 * @date 2020/5/20 16:35
	 */
	@Override
	public Integer saveAndModifyCal(Map<String, Object> params) {
		if( params == null || params.size() <= 0 ){
			return null;
		}
		Integer result = -1;
		try {
			Integer line = energyManagementMapper.modifyCalData( params );
			if (line > 0) {
				if(params.containsKey( "operationType" ) && params.get( "operationType" ).toString().equals( "1" )){
					params.put( "enmId", SequenceUtils.getDBSequence() );
					result = energyManagementMapper.saveData( params )>0?101:500;
				} else {
					params.put("changeDate",new Date());
					result = energyManagementMapper.modifyData( params )>0?201:500;
				}
			}
		} catch (Exception e) {
			result = 555;
		}
		return result;
	}
	
	@Override
	public Boolean deleteCalData(Long enmId,Long cerId) {
		boolean cerFlag = energyManagementMapper.deleteCalData( cerId )>0;
		boolean recordFlag = false;
		if ( cerFlag ) {
			recordFlag = energyManagementMapper.deleteRecordData( enmId ) > 0;
		}
		return cerFlag && recordFlag;
	}
	
	/**
	 * 删除单个文件
	 * @param fileName 要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String fileName, HttpServletRequest request) {
		String filePath = System.getProperty( "user.dir" ).substring( 0,System.getProperty( "user.dir" ).lastIndexOf( "\\" ) ) + File.separator + "webapps"
				+ request.getContextPath() + File.separator + "upload" + File.separator + "yunNan" + File.separator + "calculater_chart" + File.separator; // 文件存储文件夹路径
		File file = new File( filePath + fileName );
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				System.out.println("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			System.out.println("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}
	
	
	public String readThirdCompanyImg(String fileName,HttpServletRequest request){
		String filePath = System.getProperty( "user.dir" ).substring( 0,System.getProperty( "user.dir" ).lastIndexOf( "\\" ) ) + File.separator + "webapps"
				+ request.getContextPath() + File.separator + "upload" + File.separator + "yunNan" + File.separator + "calculater_chart" + File.separator; // 文件存储文件夹路径
		File file = new File( filePath + fileName );
		if(file == null)
			return "";
		String imgType = fileName.substring(fileName.lastIndexOf(".") + 1);
		byte[] buffer = new byte[0];
		try {
			FileInputStream inputFile = new FileInputStream(file);
			buffer = new byte[(int)file.length()];
			inputFile.read(buffer);
			inputFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "data:image/" + imgType + ";base64," + new BASE64Encoder().encode(buffer).replaceAll("\r\n","");
	}
	
	
	/**
	 * 项目负责人信息上传
	 * @author catkins.
	 * @param map
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/22 13:55
	 */
	@Override
	public Map<String, Object> uploadCalculater(Map<String, Object> params, HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		
		int uploadType = 0;
		List<String> enmIds = new ArrayList<String>( 0 );
		if(params != null){
			if( params.containsKey( "uploadStatus" ) ){
				uploadType = Integer.parseInt( params.get( "uploadStatus" ).toString() );
			}
			if( params.containsKey( "enmIds" ) ){
				enmIds = (List<String>) params.get( "enmIds" );
			}
		}
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.parseLong( enmIds.get( 0 ).substring( enmIds.get( 0 ).indexOf( "," )+1 ) ) );
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.parseLong( enmIds.get( 0 ).substring( enmIds.get( 0 ).indexOf( "," )+1 ) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		
		Map<String, Object> map = assemblyCalculater( Long.parseLong(  enmIds.get( 0 ).substring( 0,enmIds.get( 0 ).indexOf( "," ) )), uploadType,request );
		ObjectNode jsonNodes = null;
		String responseMessage = "信息已存在,请勿重复操作";
		if( !map.containsKey( "responseCode" ) ){
			if (uploadType == 1) {
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+CALCULATER_ADD_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 2 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+CALCULATER_MODIFY_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 3 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+CALCULATER_DELETE_URL,map,ak.get( "token" ).toString());
			}
			String responseCode = jsonNodes.get( "responseCode" ).toString();
			resultMap.put( "responseCode", responseCode);
			responseMessage = jsonNodes.get( "responseMessage" ).toString();
			resultMap.put( "responseMessage", responseMessage.substring( 1,responseMessage.length()-1 ));
			if ( responseCode.equals( "200" ) && uploadType != 2) {
				Map<String,Object> updateMap = new HashMap<String,Object>( 0 );
				if ( uploadType == 1 ) {
					String dataIndex = jsonNodes.get( "data" ).get( "dataIndex" ).toString();
					dataIndex = dataIndex.substring( 1,dataIndex.length()-1 );
					updateMap.put( "dataIndex",dataIndex );
					updateMap.put( "uploadStatus","1" );
				} else if( uploadType == 3 ){
					updateMap.put( "dataIndex","" );
					updateMap.put( "uploadStatus","0" );
				}
				updateMap.put( "enmId",Long.parseLong( enmIds.get( 0 ).substring( 0,enmIds.get( 0 ).indexOf( "," ) ) ) );
				energyManagementMapper.modifyData( updateMap );
			}
		} else {
			resultMap.putAll( map );
		}
		enmIds.remove( 0 );
		resultMap.put( "enmIds", enmIds );
		resultMap.put( "uploadStatus", uploadType );
		resultMap.put( "responseMessage",responseMessage );
		return resultMap;
	}
	
	private Map<String,Object> assemblyCalculater(Long enmId,Integer uploadType, HttpServletRequest request){
		Map<String, Object> dataMap = energyManagementMapper.queryDataById(enmId);
		if( dataMap == null || dataMap.size() <= 0 ){
			dataMap = new HashMap<String, Object>( 0 );
			dataMap.put( "responseCode", "500");
			dataMap.put( "responseMessage", "500");
			return dataMap;
		}
		if( !dataMap.get( "UPLOAD_STATUS" ).toString().equals( "1" ) && (uploadType == 2 || uploadType == 3)){
			dataMap.put( "responseCode", "505");
			dataMap.put( "responseMessage", "505");
			return dataMap;
		}
		if( dataMap.get( "UPLOAD_STATUS" ).toString().equals( "1" ) && uploadType == 1 ){
			dataMap.put( "responseCode", "506");
			dataMap.put( "responseMessage", "506");
			return dataMap;
		}
		Map<String,Object> map = new HashMap<String,Object>( 0 );
		//如果是删除方法,报文不同,直接return
		if( uploadType == 3 ){
			map.put( "dataIndex",dataMap.get( "DATA_INDEX" ) );
			map.put( "enterpriseCode",dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
			return map;
		}
		if ( uploadType == 2 ) {
			map.put( "dataIndex",dataMap.get( "DATA_INDEX" ) );
		}
		
		Map<String, Object> version = yunNanMapper.getPlatVersionById( Long.parseLong( dataMap.get( "ENT_ID" ).toString() ) );
		map.put( "regVersion",version.get( "regVersion" ) );/*平台服务版本*/
		map.put( "dicVersion",version.get( "dicVersion" ) );/*基础数据版本*/
		map.put( "enterpriseCode" , dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
		map.put( "calculaterName" , dataMap.get( "ENM_NAME" ) );/*姓名*/
		map.put( "phone" , dataMap.get( "TEL" ) );/*电话*/
		map.put( "dept" , dataMap.get( "DEPT" ) );/*办公室*/
		map.put( "job" , dataMap.get( "JOB" ) );/*职位、或者岗位*/
		map.put( "technicalPost" , dataMap.get( "TECH_POST" ) );/*专业技术职称*/
		/*
		使用Base64编码 文件小于400K
		*/
		map.put( "certificate" ,  this.readThirdCompanyImg(dataMap.get( "CERT_URL" ).toString(),request) );/*专业技术证书*/
		map.put( "certificateName" , dataMap.get( "CERT_NAME" ) );/*名称*/
		map.put( "certificateNo" , dataMap.get( "CERT_NO" ) );/*编号*/
		map.put( "remark" , "" );/*备注*/
		return map;
	}
	
}
