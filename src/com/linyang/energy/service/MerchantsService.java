package com.linyang.energy.service;


import javax.servlet.ServletOutputStream;
import java.util.Date;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 10:43 2019/7/15
 * @ Description：class说明
 * @ Modified By：:dingy
 * @Version: $version$
 */
public interface MerchantsService {
	
	/**
	 * 获取商户信息
	 * @param beginTime		开始时间
	 * @param endTime		结束时间
	 * @param ledgerId		企业id
	 * @param dataType		数据类型
	 * @param instead		替换数据类型
	 * @param dateTime		原始选项时间
	 * @param parLedgerId	账号权限的企业id
	 * @param userNo		用户号
	 * @param payStatus		缴费状态
	 * @param payStatus		企业名称
	 * @return
	 */
	Map<String,Object> getMerchants(Date beginTime, Date endTime, long ledgerId, int dataType,
									int instead,Date dateTime,long parLedgerId,String userNo,int payStatus,String ledgerName);
	
	/**
	 * 获得父节点层级
	 * @param ledgerId
	 * @return
	 */
	Integer queryLevel(long ledgerId,long parLedgerId);
	
	/**
	 * 存储缴费状态
	 * @param ledgerId 能管对象id
	 * @param dateTime 结算时间(获取服务器时间)
	 * @return
	 */
	int doPayCost(long ledgerId,Date dateTime);
	
	/**
	 * 导出excel
	 * @param name				文件名
	 * @param outputStream
	 * @param result			结果集
	 * @param title				sheet名称
	 * @param beginTime		 	页面选择得到的开始时间(用于excel展示)
	 * @param endTime			页面选择得到的结束时间(用于excel展示)
	 */
	public void getExcel(String name,ServletOutputStream outputStream,Map<String, Object> result,int dataType,String beginTime,String endTime);
	
	

	/**
	 * 查询能管对象结算日相关信息
	 * @author catkins.
	 * @param ledgerId
	 * @param flag
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/11/6 16:48
	 */
	public Map<String,Object> queryCalcData(Long ledgerId,int flag);
	
}
