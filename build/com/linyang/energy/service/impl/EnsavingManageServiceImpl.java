package com.linyang.energy.service.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.mapping.yunNan.EnsavingManageMapper;
import com.linyang.energy.mapping.yunNan.YunNanMapper;
import com.linyang.energy.service.EnsavingManageService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.energy.utils.WebConstant;
import com.linyang.energy.utils.yunNanUtil.HttpYunNan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:57 2020/5/25
 * @ Description：class说明
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Service
public class EnsavingManageServiceImpl implements EnsavingManageService {
	
	// add or update method by catkins.
	// date 2020/5/26
	// Modify the content: 用能单位节能项目情况信息接口地址
	private static final String ADD_URL = "/YNEnergy/companyEnergyConservation/add";
	
	private static final String MODIFY_URL = "/YNEnergy/companyEnergyConservation/update";
	
	private static final String DELETE_URL = "/YNEnergy/companyEnergyConservation/delete";
	//end
	
	@Autowired
	private EnsavingManageMapper ensavingManageMapper;
	
	@Autowired
	private YunNanMapper yunNanMapper;
	
	@Override
	public List<Map<String, Object>> queryEnsavingPageList(Page page, Map<String, Object> queryMap) {
		List<Map<String, Object>> maps = null;
		if(page != null) {
			Map<String, Object> map = new HashMap<String, Object>( queryMap );
			map.put( Dialect.pageNameField, page );
			maps = ensavingManageMapper.queryEnsavingPageList( map );
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
				params.put("ensId",SequenceUtils.getDBSequence());
				result = ensavingManageMapper.saveData( params )>0?101:500;
			} else {
				params.put("changeDate",new Date());
				result = ensavingManageMapper.modifyData( params )>0?201:500;
			}
		} catch (Exception e) {
			result = 555;
		}
		return result;
	}
	
	/**
	 * 方法实现说明
	 * @author catkins.
	 * @param ensId
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2020/5/26 15:31
	 */
	@Override
	public Map<String, Object> queryDataById(Long ensId) {
		return ensavingManageMapper.queryDataById( ensId );
	}
	
	@Override
	public Boolean deleteDataById(Long ensId) {
		return ensavingManageMapper.deleteDataById(ensId)>0;
	}
	
	
	@Override
	public Map<String, Object> uploadData(Map<String, Object> params) {
		Map<String,Object> resultMap = new HashMap<String,Object>( 0 );
		
		int uploadType = 0;
		List<String> ensIds = new ArrayList<String>( 0 );
		if(params != null){
			if( params.containsKey( "uploadStatus" ) ){
				uploadType = Integer.parseInt( params.get( "uploadStatus" ).toString() );
			}
			if( params.containsKey( "ensIds" ) ){
				ensIds = (List<String>) params.get( "ensIds" );
			}
		}
		Map<String, Object> ak = yunNanMapper.getPlatAkById( Long.parseLong( ensIds.get( 0 ).substring( ensIds.get( 0 ).indexOf( "," )+1 ) ) );
		
		Map<String, Object> platUrls = yunNanMapper.getPlatUrlsById( Long.parseLong( ensIds.get( 0 ).substring( ensIds.get( 0 ).indexOf( "," )+1 ) ) );
		String infoUrl = "";
		if(platUrls != null && platUrls.containsKey( "infoUrl" ))
			infoUrl = platUrls.get( "infoUrl" ).toString();/*用能单位基础信息配置交换地址*/
//		platUrls.get( "dataUrl" );/*用能单位能耗资源数据交换地址*/
		String responseMessage = "信息已存在,请勿重复操作";
		Map<String, Object> map = assemblyData( Long.parseLong( ensIds.get( 0 ).substring( 0 , ensIds.get( 0 ).indexOf( "," ) ) ), uploadType );
		ObjectNode jsonNodes = null;
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
			if ( responseCode.equals( "200" ) && uploadType != 2) {
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
				updateMap.put( "oldMsiId",Long.parseLong( ensIds.get( 0 ).substring( 0 , ensIds.get( 0 ).indexOf( "," ) ) ) );
				ensavingManageMapper.updateDataIndexById( updateMap );
			}
		} else {
			resultMap.putAll( map );
		}
		if(ensIds.size() >= 1)
			ensIds.remove( 0 );
		resultMap.put( "ensIds", ensIds );
		resultMap.put( "uploadStatus", uploadType );
		resultMap.put( "responseMessage",responseMessage );
		return resultMap;
	}
	
	private Map<String,Object> assemblyData(Long ensId,Integer uploadType){
		Map<String, Object> dataMap = ensavingManageMapper.queryDataById( ensId );
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
		Date beginTime = DateUtil.convertStrToDate(dataMap.get( "BEGIN_TIME" ).toString());
		Date endTime = DateUtil.convertStrToDate(dataMap.get( "END_TIME" ).toString());
		map.put( "regVersion",version.get( "regVersion" ) );/*平台服务版本*/
		map.put( "dicVersion",version.get( "dicVersion" ) );/*基础数据版本*/
		map.put( "enterpriseCode" , dataMap.get( "ENT_CODE" ) );/*统一社会信用代码 */
		map.put( "projectType",dataMap.get( "ENS_TYPE" ) );/*项目类型*/
		map.put( "projectName",dataMap.get( "ENS_NAME" ) );/*项目名称*/
		map.put( "improveMeasure",dataMap.get( "IMP_MEAS" ) );/*改造措施*/
		map.put( "investmentAmount",dataMap.get( "AMOUNT" ) );/*投资金额 单位万*/
		map.put( "projectTimeline",DateUtil.convertDateToStr(beginTime,"yyyy年MM月启动")+DateUtil.convertDateToStr(endTime,"预计yyyy年MM月完成") );/*时间安排*/  //"2019年12月启动，预计2020年2月完成"
		map.put( "energySavingAmount",dataMap.get( "ENS_AMOUNT" ) );/*预期节能量 单位：tce/年*/
		map.put( "remark",dataMap.get( "REMARK" ) );/*备注*/
		return map;
	}
	
	
	
}
