package com.linyang.energy.service.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.yunNan.InstitutionsMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.service.InstitutionsService;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 11:25 2020/5/19
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class InstitutionsServcieImpl implements InstitutionsService {
	
	@Autowired
	private InstitutionsMapper institutionsMapper;
	
	@Autowired
	private YunNanMapper yunNanMapper;
	
	
	private static final String ADD_URL = "/YNEnergy/gaugeVerifyConfigure/add";
	
	private static final String MODIFY_URL = "/YNEnergy/gaugeVerifyConfigure/update";
	
	private static final String DELETE_URL = "/YNEnergy/gaugeVerifyConfigure/delete";
	
	/**
	 * 查询计量器具信息
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/19 14:37
	 */
	@Override
	public List<Map<String, Object>> queryMsiData() {
		return institutionsMapper.queryMsiData();
	}
	
	/**
	 * 查询检测单位信息
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/19 14:37
	 */
	@Override
	public List<Map<String, Object>> queryAoData() {
		return institutionsMapper.queryAoData();
	}
	
	/**
	 * 查询检定校准分页信息
	 * @author catkins.
	 * @param page
	 * @param params
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/19 14:38
	 */
	@Override
	public List<Map<String, Object>> queryInstitutionsPageList(Page page, Map<String, Object> params) {
		List<Map<String, Object>> maps = null;
		if(page != null) {
			Map<String, Object> map = new HashMap<String, Object>( params );
			map.put( Dialect.pageNameField, page );
			maps = institutionsMapper.queryInstitutionsPageList( map );
		}
		return maps;
	}
	
	/**
	 * 新增和修改方法
	 * @author catkins.
	 * @param params
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/19 14:39
	 */
	@Override
	public Integer saveAndModify(Map<String, Object> params) {
		if( params == null || params.size() <= 0 ){
			return null;
		}
		Integer result = -1;
		try {
			if(params.containsKey( "operationType" ) && params.get( "operationType" ).toString().equals( "1" )){
				/**
				 * 这里新增数据的时候同时需要更新一下计量器具信息里的检验单位字段信息
				 * 字段信息为该计量器具最新一次的校准单位
				 */
				institutionsMapper.modifyMeasuringAo(params);
				result = institutionsMapper.saveData( params )>0?101:500;
			} else {
				params.put("changeDate",new Date());
				result = institutionsMapper.modifyData( params )>0?201:500;
			}
		} catch (Exception e) {
			result = 555;
		}
		return result;
	}
	
	@Override
	public Map<String, Object> queryDataById(Long msiId, Long aoId) {
		return institutionsMapper.queryDataById( msiId,aoId );
	}
	
	
	@Override
	public Boolean deleteInsData(Long msiId, Long aoId) {
		return institutionsMapper.deleteInsData( msiId,aoId )>0;
	}
	
	/**
	 * 上传数据到云南的方法
	 * @author catkins.
	 * @param params
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/20 13:34
	 */
	@Override
	public Map<String, Object> uploadData(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		
		int uploadType = 0;
		List<String> msiIds = new ArrayList<String>( 0 );
		if(params != null){
			if( params.containsKey( "uploadStatus" ) ){
				uploadType = Integer.parseInt( params.get( "uploadStatus" ).toString() );
			}
			if( params.containsKey( "msiIds" ) ){
				msiIds = (List<String>) params.get( "msiIds" );
			}
		}
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.parseLong( msiIds.get( 0 ).substring( msiIds.get( 0 ).lastIndexOf( "," )+1 ) ) );
		long msiId = Long.valueOf( msiIds.get( 0 ).substring( 0, msiIds.get( 0 ).indexOf( "," ) ) );
		long aoId = Long.valueOf( msiIds.get( 0 ).substring(  msiIds.get( 0 ).indexOf( "," )+1 ,msiIds.get( 0 ).lastIndexOf(",")) );
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.parseLong( msiIds.get( 0 ).substring( msiIds.get( 0 ).lastIndexOf( "," )+1 ) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		
		Map<String, Object> map = assemblyData( msiId,aoId, uploadType );
		ObjectNode jsonNodes = null;
		String responseMessage = "信息已存在,请勿重复操作";
		if( !map.containsKey( "responseCode" ) ){
			if (uploadType == 1) {
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+ADD_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 2 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+MODIFY_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 3 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+DELETE_URL,map,ak.get( "token" ).toString());
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
					updateMap.put( "uploadStatus",1 );
				} else if( uploadType == 3 ){
					updateMap.put( "dataIndex","" );
					updateMap.put( "uploadStatus",0 );
				}
				updateMap.put( "msiId",msiId );
				updateMap.put( "aoId",aoId );
				institutionsMapper.updateDataIndexById( updateMap );
			}
		} else {
			resultMap.putAll( map );
		}
		msiIds.remove( 0 );
		resultMap.put( "msiIds", msiIds );
		resultMap.put( "uploadStatus", uploadType );
		resultMap.put( "responseMessage",responseMessage );
		return resultMap;
	}
	
	/**
	 *	上传参数组装
	 * @param msiId
	 * @param aoId
	 * @param uploadType	上传类型
	 * @return
	 */
	private Map<String,Object> assemblyData(Long msiId,Long aoId,int uploadType){
		Map<String, Object> dataMap = institutionsMapper.queryUploadData(msiId,aoId);
		if( dataMap == null || dataMap.size() <= 0 ){
			dataMap = new HashMap<String, Object>( 0 );
			dataMap.put( "responseCode", "500");
			dataMap.put( "responseMessage", "500");
			return dataMap;
		}
		if( !dataMap.get( "UPLOADSTATUS" ).toString().equals( "1" ) && (uploadType == 2 || uploadType == 3)){
			dataMap.put( "responseCode", "505");
			dataMap.put( "responseMessage", "505");
			return dataMap;
		}
		if( dataMap.get( "UPLOADSTATUS" ).toString().equals( "1" ) && uploadType == 1 ){
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
		map.put( "serNo",dataMap.get( "SERNO" ) );/*计量器出产序号*/
		map.put( "manageNo", dataMap.get( "MSI_TYPE" ).toString()+dataMap.get( "MSI_LEVEL" ).toString()+dataMap.get( "MANAGE_NO" ).toString() ); /*计量器管理编号(计量器具类型+器具等级+管理编号)*/
		map.put( "barCode",dataMap.get( "BARCODE" ) );/*计量器二维码*/
		map.put( "verifyOrg",dataMap.get( "AO_NAME" ) );/*校验机构*/
		map.put( "verifyTime",dataMap.get( "RECORD_DATE" ) ); /*校验时间*/
		map.put( "remark" ,"" );/*备注*/
		return map;
	}
	
	
	
}
