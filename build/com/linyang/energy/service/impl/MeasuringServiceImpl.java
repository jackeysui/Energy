package com.linyang.energy.service.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.yunNan.MeasuringMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.service.MeasuringService;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 13:34 2020/5/14
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class MeasuringServiceImpl implements MeasuringService {
	
	@Autowired
	private MeasuringMapper measuringMapper;
	
	@Autowired
	private YunNanMapper yunNanMapper;
	
	// add or update method by catkins.
	// date 2020/5/19
	// Modify the content: 云南计量器具配置,上传(新增,修改,删除)url
	private static final String ADD_URL = "/YNEnergy/gaugeConfigure/add";
	
	private static final String UPDATE_URL = "/YNEnergy/gaugeConfigure/update";
	
	private static final String DELETE_URL = "/YNEnergy/gaugeConfigure/delete";
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
	public List<Map<String, Object>> queryMeasuringPageList(Page page, Map<String, Object> queryMap) {
		List<Map<String, Object>> maps = null;
		if(page != null) {
			Map<String, Object> map = new HashMap<String, Object>( queryMap );
			map.put( Dialect.pageNameField, page );
			maps = measuringMapper.queryMeasuringPageList( map );
		}
		return maps;
	}
	
	/**
	 * 修改和新增计量器具方法
	 * @author catkins.
	 * @param params
	 * @return java.lang.Integer
	 * @exception
	 * @date 2020/5/15 10:47
	 */
	@Override
	public Integer saveAndModify(Map<String, Object> params) {
		if ( params != null ) {
			if(params.containsKey( "operationType" ) && params.get( "operationType" ).toString().equals( "1" )){
				params.put("msiId",SequenceUtils.getDBSequence());
				params.put("installDate",new Date());
				params.put( "uploadStatus", 0 );
				/**
				 * 新增的时候需要添加一下T_ALIGN_RECORD的数据
				 */
				measuringMapper.saveRecordData(params);
				return measuringMapper.saveMeasuring( params )>0?101:500;
			} else {
				if( params.containsKey( "isChange" ) && params.get( "isChange" ).toString().equals( "1" ) )
					params.put( "changeDate",new Date() );
				return measuringMapper.modifyMeasuring( params )>0?201:500;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * 根据msiId查询相关数据
	 * @author catkins.
	 * @param msiId
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/15 17:11
	 */
	@Override
	public Map<String, Object> queryDataById(Long msiId) {
		return measuringMapper.queryDataById(msiId);
	}
	
	/**
	 * 查询检测机构模糊查询框数据
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/15 17:14
	 */
	@Override
	public List<Map<String,Object>> queryAlignOrg() {
		return measuringMapper.queryAlignOrg();
	}
	
	/**
	 * 查询计量器具类型查询框数据
	 * @author catkins.
	 * @param
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/18 9:56
	 */
	@Override
	public List<Map<String, Object>> queryMsiType() {
		return measuringMapper.queryMsiType();
	}
	
	/**
	 * 根据计量器具id删除数据
	 * @author catkins.
	 * @param msiId
	 * @return boolean
	 * @exception
	 * @date 2020/5/18 9:57
	 */
	@Override
	public boolean deleteMsiDataById(Long msiId) {
		return measuringMapper.deleteMsiDataById(msiId)>0;
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
		
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.valueOf( msiIds.get( 0 ).substring(msiIds.get( 0 ).indexOf( "," )+1) ) );
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.valueOf( msiIds.get( 0 ).substring(msiIds.get( 0 ).indexOf( "," )+1) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		
		Map<String, Object> map = assemblyData( Long.valueOf( msiIds.get( 0 ).substring( 0, msiIds.get( 0 ).indexOf( "," )) ), uploadType );
		ObjectNode jsonNodes = null;
		String responseMessage = "信息已存在,请勿重复操作";
		if( !map.containsKey( "responseCode" ) ){
			if (uploadType == 1) {
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+ADD_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 2 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+UPDATE_URL,map,ak.get( "token" ).toString());
			} else if ( uploadType == 3 ){
				jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+DELETE_URL,map,ak.get( "token" ).toString());
			}
			String responseCode = jsonNodes.get( "responseCode" ).toString();
			resultMap.put( "responseCode", responseCode);
			responseMessage = jsonNodes.get( "responseMessage" ).toString();
			resultMap.put( "responseMessage", responseMessage.substring( 1,responseMessage.length()-1 ));
			System.out.println(jsonNodes.toString());
			if ( responseCode.equals( "200" ) && uploadType != 2 ) {
				String dataIndex = "";
				if ( uploadType == 1 ) {
					dataIndex = jsonNodes.get( "data" ).get( "dataIndex" ).toString();
					dataIndex = dataIndex.substring( 1,dataIndex.length()-1 );
					measuringMapper.updateDataIndexById( Long.valueOf( msiIds.get( 0 ).substring( 0, msiIds.get( 0 ).indexOf( "," )) ),dataIndex,1 );
					measuringMapper.updateUsageStatus(Long.parseLong( map.get( "dataCode" ).toString() ));
				} else if( uploadType == 3 ){
					measuringMapper.updateDataIndexById( Long.valueOf( msiIds.get( 0 ).substring( 0, msiIds.get( 0 ).indexOf( "," )) ),dataIndex,0 );
				}
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
	 * 组装上传参数
	 * @return
	 */
	private Map<String,Object> assemblyData(Long msiId,int uploadType){
		Map<String, Object> dataMap = measuringMapper.queryUploadData( msiId );
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
		
		Map<String, Object> version = yunNanMapper.getPlatVersionById( Long.parseLong( dataMap.get( "ENT_ID" ).toString() ) );
		
		switch(uploadType){
			case 2:
				map.put( "dataIndex" , dataMap.get( "DATA_INDEX" ) );
			case 1:
				map.put( "regVersion" , version.get( "regVersion" ) );/*平台服务版本*/
				map.put( "dicVersion" , version.get( "dicVersion" ) );/*基础数据版本*/
				map.put( "enterpriseCode" , dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
				map.put( "meterName",dataMap.get( "MSINAME" ) );/*计量器具名称*/
				break;
			case 3://3是删除报文不同,直接return掉
				map.put( "dataIndex" , dataMap.get( "DATA_INDEX" ) );
				map.put( "enterpriseCode" , dataMap.get( "ENT_CODE" ) ); /*统一社会信用代码 */
				return map;
		}
		map.put( "meterType", "" );
		if ( dataMap.containsKey( "METERTYPE" ) ) {
			map.put( "meterType",dataMap.get( "METERTYPE" ) );/*计量器具类型*/
		}
		map.put( "meterLevel","" );
		if ( dataMap.containsKey( "METERLEVEL" ) ) {
			map.put( "meterLevel",dataMap.get( "METERLEVEL" ) );/*器具等级*/
		}
		
		map.put( "params","" );
		if ( dataMap.containsKey( "PARAMS" ) ) {
			map.put( "params",dataMap.get( "PARAMS" ) );/*计量相关参数*/
		}
		
		map.put( "dataCode","" );
		if ( dataMap.containsKey( "DATACODE" ) ) {
			map.put( "dataCode",dataMap.get( "DATACODE" ) );/*所属上报数据组合编码*/
		}
		map.put( "reportArithmetic","" );
		if ( dataMap.containsKey( "REPORTARITHMETIC" ) ) {
			map.put( "reportArithmetic",dataMap.get( "REPORTARITHMETIC" ) );/*与上报数据组合编码算术关系*/
		}
		map.put( "reportRatio","" );
		if ( dataMap.containsKey( "REPORTRATIO" ) ) {
			map.put( "reportRatio",dataMap.get( "REPORTRATIO" ) );/*与上报数据组合编码算术系数*/
		}
		map.put( "manufacturer","" );
		if ( dataMap.containsKey( "MANUFACTURER" ) ) {
			map.put( "manufacturer",dataMap.get( "MANUFACTURER" ) );/*生产厂家*/
		}
		map.put( "model","" );
		if ( dataMap.containsKey( "MODEL" ) ) {
			map.put( "model",dataMap.get( "MODEL" ) );/*规格型号*/
		}
		map.put( "precisionLevel","" );
		if ( dataMap.containsKey( "PRECISIONLEVEL" ) ) {
			map.put( "precisionLevel",dataMap.get( "PRECISIONLEVEL" ) );/*精确度等级*/
		}
		map.put( "ranges","" );
		if ( dataMap.containsKey( "RANGES" ) ) {
			map.put( "ranges",dataMap.get( "RANGES" ) );/*测量范围*/
		}
		map.put( "manageNo","" );
		if ( dataMap.containsKey( "MANAGENO" ) ) {
			map.put( "manageNo",dataMap.get( "MANAGENO" ) );/*管理编号*/
		}
		map.put( "alignState","" );
		if ( dataMap.containsKey( "ALIGNSTATE" ) ) {
			map.put( "alignState",dataMap.get( "ALIGNSTATE" ) );/*检定校准状态*/
		}
		map.put( "alignCycle","" );
		if ( dataMap.containsKey( "ALIGNCYCLE" ) ) {
			map.put( "alignCycle",dataMap.get( "ALIGNCYCLE" ) );/*检定校准周期 单位：月*/
		}
		map.put( "lastAlignDate","" );
		if ( dataMap.containsKey( "LASTALIGNDATE" ) ) {
			map.put( "lastAlignDate",dataMap.get( "LASTALIGNDATE" ) );/*最近一次检定校准时间*/
		}
		map.put( "nextAlignDate","" );
		if ( dataMap.containsKey( "NEXTALIGNDATE" ) ) {
			map.put( "nextAlignDate",dataMap.get( "NEXTALIGNDATE" ) );/*下次一次检定校准时间*/
		}
		map.put( "alignOrg","" );
		if ( dataMap.containsKey( "ALIGNORG" )) {
			map.put( "alignOrg",dataMap.get( "ALIGNORG" ) );/*检测机构*/
		}
		map.put( "unAlignReason","" );
		if ( dataMap.containsKey( "UNALIGNREASON" ) ) {
			map.put( "unAlignReason",dataMap.get( "UNALIGNREASON" ) );/*未检定/校准原因*/
		}
		map.put( "location","" );
		if ( dataMap.containsKey( "LOCATION" ) ) {
			map.put( "location",dataMap.get( "LOCATION" ) );/*安装地点*/
		}
		map.put( "installer","" );
		if ( dataMap.containsKey( "INSTALLER" ) ) {
			map.put( "installer",dataMap.get( "INSTALLER" ) );/*安装方*/
		}
		map.put( "installTime","" );
		if ( dataMap.containsKey( "INSTALLTIME" ) ) {
			map.put( "installTime",dataMap.get( "INSTALLTIME" ) );/*安装时间*/
		}
		map.put( "linkSys","" );
		if ( dataMap.containsKey( "LINKSYS" ) ) {
			map.put( "linkSys",dataMap.get( "LINKSYS" ) );/*接入系统*/
		}
		map.put( "currentState","" );
		if ( dataMap.containsKey( "CURRENTSTATE" ) ) {
			map.put( "currentState",dataMap.get( "CURRENTSTATE" ) );/*当前状态*/
		}
		map.put( "stateChangeTime","" );
		if ( dataMap.containsKey( "STATECHANGETIME" ) ) {
			map.put( "stateChangeTime",dataMap.get( "STATECHANGETIME" ) );/*状态发生时间*/
		}
		map.put( "serNo","" );
		if ( dataMap.containsKey( "SERNO" ) ) {
			map.put( "serNo",dataMap.get( "SERNO" ) );/*设备出产序列号*/
		}
		map.put( "barCode","" );
		if ( dataMap.containsKey( "BARCODE" ) ) {
			map.put( "barCode",dataMap.get( "BARCODE" ) );/*计量器具二维码*/
		}
		map.put( "remark","" );
		if ( dataMap.containsKey( "REMARK" ) ) {
			map.put( "remark",dataMap.get( "REMARK" ) );/*备注*/
		}
		return map;
	}
	
	
	@Override
	public List<Map<String, Object>> queryDataCode() {
		return measuringMapper.queryDataCode();
	}
	
	@Override
	public List<Map<String, Object>> queryEnType() {
		return measuringMapper.queryEnType();
	}
	
	
	@Override
	public List<Map<String, Object>> queryLedgerByRegion(Long ledgerId) {
		return measuringMapper.queryLedgerByRegion(ledgerId);
	}
}
