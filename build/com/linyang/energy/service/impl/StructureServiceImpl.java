package com.linyang.energy.service.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.yunNan.StructureMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.service.StructureService;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 11:18 2020/5/27
 * @ Description：用能单位产品结构信息 + 非能源产品信息
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class StructureServiceImpl implements StructureService {
	
	// add or update method by catkins.
	// date 2020/5/27
	// Modify the content: 云南能效-用能单位产品结构信息接口
	private static final String PRODUCT_ADD = "/YNEnergy/companyProductStructure/add";
	
	private static final String PRODUCT_UPDATE = "/YNEnergy/companyProductStructure/update";
	
	private static final String PRODUCT_DELETE = "/YNEnergy/companyProductStructure/delete";
	//end
	
	// add or update method by catkins.
	// date 2020/5/27
	// Modify the content: 云南能效-用能单位非能源产品信息接口
	private static final String MATERIEL_ADD= "/YNEnergy/companyMaterielProduct/add";
	
	private static final String MATERIEL_UPDATE = "/YNEnergy/companyMaterielProduct/update";
	
	private static final String MATERIEL_DELETE = "/YNEnergy/companyMaterielProduct/delete";
	//end
	
	
	
	@Autowired
	private StructureMapper structureMapper;
	
	@Autowired
	private YunNanMapper yunNanMapper;
	
	
	/**
	 * 查询产品结构信息分页列表
	 * @author catkins.
	 * @param page
	 * @param queryMap
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2020/5/27 13:57
	 */
	@Override
	public List<Map<String, Object>> queryenProductPageList(Page page, Map<String, Object> queryMap) {
		List<Map<String, Object>> maps = null;
		if(page != null) {
			Map<String, Object> map = new HashMap<String, Object>( queryMap );
			map.put( Dialect.pageNameField, page );
			maps = structureMapper.queryProductPageList( map );
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
				params.put( "productId", SequenceUtils.getDBSequence() );
				result = structureMapper.saveData( params )>0?101:500;
			} else {
				params.put("changeDate",new Date());
				result = structureMapper.modifyData( params )>0?201:500;
			}
		} catch (Exception e) {
			result = 555;
		}
		return result;
	}
	
	@Override
	public Map<String, Object> queryDataById(Long productId) {
		return structureMapper.queryDataById( productId );
	}
	
	@Override
	public Boolean delStructureData(Long productId) {
		return structureMapper.delStructureData( productId ) > 0;
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
	public Map<String, Object> uploadStructure(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		
		int uploadType = 0;
		int isEnProdut = 0;
		List<String> productIds = new ArrayList<String>( 0 );
		if(params != null){
			if( params.containsKey( "uploadStatus" ) ){
				uploadType = Integer.parseInt( params.get( "uploadStatus" ).toString() );
			}
			if( params.containsKey( "productIds" ) ){
				productIds = (List<String>) params.get( "productIds" );
			}
			if( params.containsKey( "isEnProDut" ) ){
				isEnProdut = Integer.parseInt( params.get( "isEnProDut" ).toString() );
			}
		}
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.parseLong( productIds.get( 0 ).substring( productIds.get( 0 ).indexOf( "," )+1 ) ) );
		Map<String, Object> map = assemblyData( Long.parseLong( productIds.get( 0 ).substring( 0, productIds.get( 0 ).indexOf( "," ) ) ), uploadType,isEnProdut );
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.parseLong( productIds.get( 0 ).substring( productIds.get( 0 ).indexOf( "," )+1 ) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		
		ObjectNode jsonNodes = null;
		String responseMessage = "信息已存在,请勿重复操作";
		if( !map.containsKey( "responseCode" ) ){
			if (uploadType == 1) {
				if ( isEnProdut == 0 ) { // 非能源类型
					jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+MATERIEL_ADD,map,ak.get( "token" ).toString());
				} else {
					jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+PRODUCT_ADD,map,ak.get( "token" ).toString());
				}
			} else if ( uploadType == 2 ){
				if ( isEnProdut == 0 ) { // 非能源类型
					jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+MATERIEL_UPDATE,map,ak.get( "token" ).toString());
				} else {
					jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+PRODUCT_UPDATE,map,ak.get( "token" ).toString());
				}
			} else if ( uploadType == 3 ){
				if ( isEnProdut == 0 ) { // 非能源类型
					jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+MATERIEL_DELETE,map,ak.get( "token" ).toString());
				} else {
					jsonNodes = HttpYunNan.sendHttpToYunNan(WebConstant.yunNanIpPort+PRODUCT_DELETE,map,ak.get( "token" ).toString());
				}
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
					updateMap.put( "uploadStatus", "0" );
				}
				updateMap.put( "productId",Long.parseLong( productIds.get( 0 ).substring( 0, productIds.get( 0 ).indexOf( "," ) ) ) );
				structureMapper.modifyData( updateMap );
			}
		} else {
			resultMap.putAll( map );
		}
		productIds.remove( 0 );
		resultMap.put( "productIds", productIds );
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
	private Map<String,Object> assemblyData(Long enmId,Integer uploadType,int isEnProdut){
		Map<String, Object> dataMap = structureMapper.queryDataById(enmId);
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
		map.put( "productName" , dataMap.get( "PRODUCT_NAME" ) );/*产品名称*/
		map.put( "productCode" , dataMap.get( "PRODUCT_CODE" ) );/*产品编号*/
		map.put( "productUnit" , dataMap.get( "PRODUCT_UNIT" ) );/*计量单位*/
		String productType = dataMap.get( "PRODUCT_TYPE" ).toString();
		if ( isEnProdut == 0 ) {
			/*P--主产品,M--中间产品,S--原料/物料*/
			switch( Integer.valueOf( productType ) ){
				case 1:
					map.put( "productType" , "p" );/*产品类型*/
					break;
				case 2:
					map.put( "productType" , "M" );/*产品类型*/
					break;
				default:
					map.put( "productType" , "S" );/*产品类型*/
					break;
			}
			map.put( "produceName" , dataMap.get( "PRODUCT_NAME" ) );/*产品名称*/
			map.put( "produceNo" , dataMap.get( "PRODUCT_CODE" ) );/*产品编号*/
			map.put( "produceUnit" , dataMap.get( "PRODUCT_UNIT" ) );/*计量单位*/
		} else {
			map.put( "productType" , productType );/*产品类型*/
		}
		map.put( "remark" , dataMap.get( "REMARK" ) );/*描述*/
		return map;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
