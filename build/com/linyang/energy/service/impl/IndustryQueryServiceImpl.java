package com.linyang.energy.service.impl;

import com.linyang.energy.mapping.IndustryPollut.IndustryQueryMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.DistributionBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.IndustryQueryService;
import com.linyang.energy.utils.MapKeyComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 9:40 2018/12/20
 * @ Description：产污设施分布统计
 * @ Modified By：:dingy
 * @Version: $version$
 */
@Service
public class IndustryQueryServiceImpl implements IndustryQueryService {
	
	@Autowired
	private IndustryQueryMapper industryQueryMapper;
	
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
	
	/**
	 * 查询所选节点下一级所有企业单位 (选择节点不是企业单位则去掉102条件)
	 * 分户分析类型:101-建筑楼宇,102-企事业单位,104-区域,105-平台运营商,106-设备,107-部门,108-云终端
	 * @author dingy
	 * @param ledgerId	页面传过来的ledgerId
	 * @return java.util.List<DistributionBean>
	 *     返回的并不是查询出来的这些企业的信息,而是根据需求组装出来的新的数据,如: 企业名称,企业数量,产污数量,治污数量,合计
	 * @exception
	 * @date 2018/12/20 9:49
	 */
	@Override
	public List<DistributionBean> queryAllEnterprise(long ledgerId) {
		List<DistributionBean> list = new ArrayList<DistributionBean>( 0 );
		
		//获取企业信息
		LedgerBean ledgerBean = ledgerManagerMapper.selectByLedgerId( ledgerId );
		
		List<Map<String, Object>> maps = industryQueryMapper.queryAllEnterprise( ledgerId, ledgerBean.getAnalyType() );
		
		long pollutCount = 0;
		long pollutctlCount = 0;
		
		if (maps.size() > 0 && null != maps.get( 0 ) && maps.get( 0 ).size() > 0) {
			for (Map<String,Object> map : maps) {
				
				String id = map.get( "LEDGER_ID" ).toString();
				String name = map.get( "LEDGER_NAME" ).toString();
				pollutCount = this.queryCountByPollut( Long.parseLong( id ) );
				pollutctlCount = this.queryCountByPollutctl( Long.parseLong( id ) );
				
				long ledgerCount = this.queryCountForParent( Long.parseLong( id ) );
				
				//组装一个新的数据返回
				list.add( new DistributionBean(id ,name,pollutCount,pollutctlCount ,ledgerCount ) );
			}
		}
		
		//对组装好数据进行排序
		MapKeyComparator.ListSort( list );
		return list;
	}
	
	/**
	 * 查询企业下治污源数量
	 * @author dingy
	 * @param ledgerId
	 * @return long
	 * @exception
	 * @date 2018/12/20 10:02
	 */
	@Override
	public long queryCountByPollutctl(long ledgerId) {
		return industryQueryMapper.queryCountByPollutctl( ledgerId );
	}
	
	/**
	 * 查询企业下产污源数量
	 * @author dingy
	 * @param ledgerId
	 * @return long
	 * @exception
	 * @date 2018/12/20 10:02
	 */
	@Override
	public long queryCountByPollut(long ledgerId) {
		return  industryQueryMapper.queryCountByPollut( ledgerId );
	}
	
	/**
	 * 查询区域等高级别能管对象下企业数量
	 * @author dingy
	 * @param ledgerId
	 * @return long
	 * @exception
	 * @date 2018/12/20 17:16
	 */
	@Override
	public long queryCountForParent(long ledgerId) {
		return industryQueryMapper.queryCountForParent( ledgerId );
	}
	
	/**
	 * 查询所选节点下所有企业(按照行业)
	 * 返回结果并不是所有企业信息
	 * @author dingy
	 * @param ledgerId
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2018/12/21 11:11
	 */
	@Override
	public List<DistributionBean> queryAllLedger(long ledgerId) {
		List<DistributionBean> list = new ArrayList<DistributionBean>( 0 );
		
		long pollutCount = 0;
		long pollutctlCount = 0;
		
		//查询到的企业信息(LEDGER_ID,INDUSTRY_NAME)
		List<Map<String, Object>> ledegrMaps = industryQueryMapper.queryAllLedger( ledgerId );
		if (ledegrMaps.size() > 0 && null != ledegrMaps.get( 0 ) && ledegrMaps.get( 0 ).size() > 0) {
			//分组后返回的新map
			List<Map<String, String>> maps = this.dataGroup( ledegrMaps );
			
			for (Map<String,String> map:maps) {
				for (String key:map.keySet()) {
					
					String[] arr = map.get( key ).split( "," );
					if(arr.length > 600) {
						List<String> list1 = this.getList( arr, 600 );
						for (String strs:list1) {
							pollutCount += this.queryCountByPollut_2( strs );
							pollutctlCount += this.queryCountByPollutctl_2( strs );
						}
					}else{
						pollutCount = this.queryCountByPollut_2( map.get( key ) );
						pollutctlCount = this.queryCountByPollutctl_2( map.get( key ) );
					}
					
					//组装一个新的数据返回
					list.add( new DistributionBean(key,pollutCount,pollutctlCount , arr.length ) );
				}
			}
		}
		
		//对组装好数据进行排序
		MapKeyComparator.ListSort( list );
		return list;
	}
	
	/**
	 * 查询企业下治污源数量(行业分组专用)
	 * @author dingy
	 * @param ledgerId
	 * @return long
	 * @exception
	 * @date 2018/12/21 11:22
	 */
	@Override
	public long queryCountByPollutctl_2(String ledgerIds) {
		return industryQueryMapper.queryCountByPollutctl_2(ledgerIds);
	}
	
	/**
	 * 查询企业下产污源数量(行业分组专用)
	 * @author dingy
	 * @param ledgerId
	 * @return long
	 * @exception
	 * @date 2018/12/21 11:22
	 */
	@Override
	public long queryCountByPollut_2(String ledgerIds) {
		return industryQueryMapper.queryCountByPollut_2(ledgerIds);
	}
	
	/**
	 * 所需再查询参数的分组
	 * @author dingy
	 * @param maps
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2018/12/21 11:14
	 */
	private List<Map<String,String>> dataGroup(List<Map<String, Object>> maps){
		List<Map<String,String>> newList = new ArrayList<Map<String,String>>( 0 );
		Map<String,String> newMap = new HashMap<String,String>( 0 );
		for (Map<String,Object> map1:maps) {
				if (null != map1 && map1.containsKey( "LEDGER_ID" ) && map1.containsKey( "INDUSTRY_NAME" ) ) {
					if( null != newMap && newMap.containsKey( map1.get( "INDUSTRY_NAME" ) )){	//已经有了
						newMap.put( map1.get( "INDUSTRY_NAME" ).toString() ,newMap.get( map1.get( "INDUSTRY_NAME" ) )+","+map1.get( "LEDGER_ID" ) );
					} else {//还没有
						newMap.put( map1.get( "INDUSTRY_NAME" ).toString(),map1.get( "LEDGER_ID" ).toString());
					}
				}
		}
		newList.add( newMap );
		return newList;
	}
	
	
	/**
	 * 把超过sql语句最长1000个的字符串切成3个逗号分割的字符串集合
	 * @author dingy
	 * @param arr	把原始字符串切成数组
	 * @param size 	需要切成每个长度是多少的集合
	 * @return java.util.List<java.lang.String>
	 * @exception
	 * @date 2018/12/21 14:03
	 */
	private List<String> getList(String[] arr,int size){
		int arrSize = arr.length%size==0?arr.length/size:arr.length/size+1;
		List<String>  sub = new ArrayList<String>();
		StringBuilder sb = null;
		for(int i=0;i<arrSize;i++) {
			sb = new StringBuilder(  );
			for(int j=i*size;j<=size*(i+1)-1;j++) {
				if(j<=arr.length-1) {
					if (j<=arr.length-1) {
						sb.append( arr[j]+"," );
					}
				}
			}
			sub.add(sb.toString().substring( 0, sb.toString().length()-1));
		}
		return sub;
	}
	
	
	
	
	
	
	
	
}
