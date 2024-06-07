package com.linyang.energy.controller.demand;

import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.service.LedgerDetailsService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.ListSortUtil;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 9:59 2019/9/7
 * @ Description：企业响应明细
 * @ Modified By：:catkins.
 * @Version: $version$
 */
@Controller
@RequestMapping("/details")
public class LedgerDetailsController extends BaseController {
	
	@Autowired
	private LedgerDetailsService ledgerDetailsService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
	 * 跳转到企业响应明细页面
	 * @author catkins.
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @exception
	 * @date 2019/9/7 10:04
	 */
	@RequestMapping(value="/ledgerDetails")
	public ModelAndView gotoLedgerDetailsPage(HttpServletRequest request){
		List<Map<String, Object>> maps = ledgerDetailsService.queryPlanList();
		return new ModelAndView( "/energy/demand/ledger_details" ,"plans",maps);
	}
	
	/**
	 * 获取方案的详细信息
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/7 10:06
	 */
	@RequestMapping(value="/planDetails",method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryPlanDetails(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>( 0 );
		String planId = request.getParameter( "planId" );
		if (null != planId && !planId.equals( "" )){
			resultMap = ledgerDetailsService.queryPlanDetailsById( Long.parseLong( planId ) );
		}
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_115, 137l, 1);
		return resultMap;
	}
	
	/**
	 * 获取方案下企业列表
	 * @author catkins.
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/8 14:37
	 */
	@RequestMapping(value = "/ledgerListByPlan",method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryLedgerListPages(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>( 0 );
		List<Map<String, Object>> ledgerDatas = null;
		Page page = null;
		// 得到当前分页
		try {
			Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
			if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
				page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
			else
				page = new Page();
			ledgerDatas = ledgerDetailsService.queryLedgerListPageData( page, queryMap );
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put( "page", page);
		result.put( "list",ledgerDatas );
		return result;
	}
	
	/**
	 * 得到企业响应明细页面的 需求响应概况和图表数据
	 * @author catkins.
	 * @param
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/9/7 11:35
	 */
	@RequestMapping(value = "/queryChartAndTable",method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> queryTableAndChartDatas(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>( 0 );
		Map<String, Object> overviewMap = null; // 用于存放需求响应概况数据
		List<Map<String,Object>> chartList = null; // 用于存放需求响应方案图表数据
		try {
			//拿到当前管理者账号企业id,用于查询下属所有企业列表用
			long ledgerId = super.getSessionUserInfo(request).getLedgerId();
			//拿到页面的请求参数
			Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));

			queryMap.put( "ledgerId",ledgerId );
			//拿到需求响应概况信息
			overviewMap = ledgerDetailsService.queryOverview( queryMap );
			//拿到需求响应方案图表信息
			chartList = ledgerDetailsService.queryChartDatas( queryMap );
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("overview",overviewMap);
		result.put( "chartList",chartList );
		return result;
	}
	
	/**
	 * 给页面数据进行排序,按照页面选择的方式进行排序
	 * @author catkins.
	 * @param request
	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 * @exception
	 * @date 2019/9/9 15:26
	 */
	@RequestMapping(value = "sortData",method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> sortData(HttpServletRequest request) {
		List<Map<String,Object>> chartList = null; // 用于存放需求响应方案图表数据
		try {
			//拿到当前管理者账号企业id,用于查询下属所有企业列表用
			long ledgerId = super.getSessionUserInfo(request).getLedgerId();
			//拿到页面的请求参数
			Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
			queryMap.put( "ledgerId",ledgerId );
			//拿到需求响应方案图表信息
			chartList = ledgerDetailsService.queryChartDatas( queryMap );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chartList;
	}

}
