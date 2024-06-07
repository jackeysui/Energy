package com.linyang.energy.service.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.yunNan.ReplaceRecordMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.service.ReplaceRecordService;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:53 2020/5/20
 * @ Description：计量器具更换记录管理
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class ReplaceRecordServiceImpl implements ReplaceRecordService {
	
	@Autowired
	private ReplaceRecordMapper replaceRecordMapper;
	
	@Autowired
	private YunNanMapper yunNanMapper;
	
	private static final String ADD_URL = "/YNEnergy/gaugeReplaceConfigure/add";
	
	private static final String MODIFY_URL = "/YNEnergy/gaugeReplaceConfigure/update";
	
	private static final String DELETE_URL = "/YNEnergy/gaugeReplaceConfigure/delete";
	
	/**
	 * 查询计量器具更换分页列表
	 * @author catkins.
	 * @param page
	 * @param params
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/20 15:53
	 */
	@Override
	public List<Map<String, Object>> queryRecordPageList(Page page, Map<String, Object> params) {
		List<Map<String, Object>> maps = null;
		if(page != null) {
			Map<String, Object> map = new HashMap<String, Object>( params );
			map.put( Dialect.pageNameField, page );
			maps = replaceRecordMapper.queryRecordPageList( map );
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
				result = replaceRecordMapper.saveData( params )>0?101:500;
			} else {
				params.put("changeDate",new Date());
				result = replaceRecordMapper.modifyData( params )>0?201:500;
			}
		} catch (Exception e) {
			result = 555;
		}
		return result;
	}
	
	@Override
	public Map<String, Object> queryDataById(Long oldMsiId) {
		return replaceRecordMapper.queryDataById( oldMsiId );
	}
	
	@Override
	public boolean deleteRecordData(Long oldMsiId) {
		return replaceRecordMapper.deleteRecordData( oldMsiId ) > 0;
	}
	
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
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.parseLong( msiIds.get( 0 ).substring( msiIds.get( 0 ).indexOf( "," )+1 ) ) );
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.parseLong( msiIds.get( 0 ).substring( msiIds.get( 0 ).indexOf( "," )+1 ) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		
		Map<String, Object> map = assemblyData( Long.parseLong( msiIds.get( 0 ).substring( 0,msiIds.get( 0 ).indexOf( "," ) ) ), uploadType );
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
				updateMap.put( "oldMsiId",Long.parseLong( msiIds.get( 0 ).substring( 0,msiIds.get( 0 ).indexOf( "," ) ) ) );
				replaceRecordMapper.updateDataIndexById( updateMap );
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
	
	private Map<String,Object> assemblyData(Long msiId,Integer uploadType){
		Map<String, Object> dataMap = replaceRecordMapper.queryUploadData(msiId);
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
			map.put( "dataIndex",dataMap.get( "DATAINDEX" ) );
			map.put( "enterpriseCode",dataMap.get( "ENTERPRISECODE" ) );/*统一社会信用代码 */
			return map;
		}
		if ( uploadType == 2 ) {
			map.put( "dataIndex",dataMap.get( "DATAINDEX" ) );
		}
		Map<String, Object> version = yunNanMapper.getPlatVersionById( Long.parseLong( dataMap.get( "ENTID" ).toString() ) );
		map.put( "regVersion",version.get( "regVersion" ) );/*平台服务版本*/
		map.put( "dicVersion",version.get( "dicVersion" ) );/*基础数据版本*/
		map.put( "enterpriseCode" , dataMap.get( "ENTERPRISECODE" ) );/*统一社会信用代码 */
		map.put( "replacedSerno",dataMap.get( "REPLACEDSERNO" ) );/*被替换计量器出产序号*/
		map.put( "replacedManageNo", dataMap.get( "REPLACEMSITYPE" ).toString() + dataMap.get( "REPLACEMSILEVEL" ) + dataMap.get( "REPLACEDMANAGENO" )  ); /*被替换计量器管理编号*/
		map.put( "replacedBarCode",dataMap.get( "REPLACEDBARCODE" ) );/*被替换计量器二维码*/
		map.put( "installSerNo",dataMap.get( "INSTALLSERNO" ) );/*新装计量器出产序号*/
		/*
		计量器具类型+器具等级+管理编号
		*/
		map.put( "installManageNo", dataMap.get( "INSTALLMSITYPE" ).toString() + dataMap.get( "INSTALLMSILEVEL" ) + dataMap.get( "INSTALLMANAGENO" ) ); /*新装计量器管理编号*/
		map.put( "installBarCode" , dataMap.get( "INSTALLBARCODE" ) );/*新装计量器二维码*/
		map.put( "installTime" , dataMap.get( "INSTALLTIME" ) );/*新装计量器时间*/
		map.put( "remark" , dataMap.get( "REMARK" ) );/*备注*/
		return map;
	}
	
}
