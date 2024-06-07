package com.linyang.energy.controller;

import com.linyang.energy.model.DistributionBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.IndustryQueryService;
import com.linyang.energy.service.LedgerManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 10:23 2018/12/11
 * @ Description：产污设施分布统计
 * @ Modified By：:dingy
 * @Version: $version$
 */
@Controller
@RequestMapping("industryQuery")
public class PollutIndustryQueryController extends BaseController {
	
	@Autowired
	private IndustryQueryService industryQueryService;
	
	@Autowired
	private LedgerManagerService ledgerManagerService;
	
	public static final Integer ANALYTYPE_102 = 102;
	
	public static final Integer ANALYTYPE_105 = 105;
	
	public static final Integer ANALYTYPE_104 = 104;
	
	/**
	 * @return
	 */
	@RequestMapping("/showIndustry")
	public ModelAndView showIndustryPage(HttpServletRequest request){
		Map<String, String> param = new HashMap<String, String>();
		Long accountId = super.getSessionUserInfo(request).getAccountId();
		return new ModelAndView("/energy/dataquery/query_Industry");
	}
	
	
	/**
	 * 查询机构分布报表数据
	 * @return
	 */
	@RequestMapping(value = "/queryInstitutionInfo", method = RequestMethod.POST)
	public @ResponseBody List<DistributionBean> queryInstitutionInfo(HttpServletRequest request){
		List<DistributionBean> list = null;
		boolean flag = true;
		LedgerBean ledgerBean = new LedgerBean();
		
		try {
			Long ledgerId = getLongParam(request, "ledgerId", -1);
			ledgerBean.setLedgerId( ledgerId );
			//101-建筑楼宇,102-企事业单位,104-区域,105-平台运营商,106-设备,107-部门,108-云终端
			if (ledgerId != 1) {
				ledgerBean = ledgerManagerService.getLedgerDataById(ledgerBean.getLedgerId());
				do {
					if (ledgerBean != null && ledgerBean.getAnalyType() != ANALYTYPE_102 && ledgerBean.getAnalyType() != ANALYTYPE_104 && ledgerBean.getAnalyType() != ANALYTYPE_105)
						ledgerBean = ledgerManagerService.getLedgerDataById(ledgerBean.getParentLedgerId());
					if (ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_102)
						flag = false;
					if (ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_105)
						flag = false;
					if (ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_104)
						flag = false;
				} while (flag);
			}
			
			list = industryQueryService.queryAllEnterprise( ledgerBean.getLedgerId() );
		} catch (Exception e) {
			e.printStackTrace(  );
		}
		return this.groupList( list, 12 );
	}
	
	/**
	 * 查询行业分布报表数据
	 * @return
	 */
	@RequestMapping(value = "/queryIndustryInfo", method = RequestMethod.POST)
	public @ResponseBody List<DistributionBean> queryIndustryInfo(HttpServletRequest request){
		List<DistributionBean> list = null;
		boolean flag = true;
		LedgerBean ledgerBean = new LedgerBean();
		try {
			Long ledgerId = getLongParam(request, "ledgerId", -1);
			ledgerBean.setLedgerId( ledgerId );
			//101-建筑楼宇,102-企事业单位,104-区域,105-平台运营商,106-设备,107-部门,108-云终端
			if (ledgerId != 1) {
				ledgerBean = ledgerManagerService.getLedgerDataById(ledgerBean.getLedgerId());
				do {
					if (ledgerBean != null && ledgerBean.getAnalyType() != ANALYTYPE_102 && ledgerBean.getAnalyType() != ANALYTYPE_104 && ledgerBean.getAnalyType() != ANALYTYPE_105)
						ledgerBean = ledgerManagerService.getLedgerDataById(ledgerBean.getParentLedgerId());
					if (ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_102)
						flag = false;
					if (ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_105)
						flag = false;
					if (ledgerBean != null && ledgerBean.getAnalyType() == ANALYTYPE_104)
						flag = false;
				} while (flag);
			}
			
			list = industryQueryService.queryAllLedger( ledgerBean.getLedgerId() );
		} catch (Exception e) {
			e.printStackTrace(  );
		}
		return list;
	}
	
	
	public List<DistributionBean> groupList(List<DistributionBean> list,int count){
		List<DistributionBean> newList = new ArrayList<DistributionBean>( 0 );
		long pollutCount = 0;
		long pollutctlCount = 0;
		long ledgerCount = 0;
		for (int i = 0 ; i < list.size() ; i++) {
			if( i > count - 1 ){
				pollutCount += list.get( i ).getPollutCount();
				pollutctlCount += list.get( i ).getPollutctlCount();
				ledgerCount += list.get( i ).getLedgerCount();
			} else {
				newList.add( list.get( i ) );
			}
		}
		if (list.size() > count) {
			newList.add( new DistributionBean(null ,"其它",pollutCount,pollutctlCount ,ledgerCount )  );
		}
		return newList;
	}
	
	
	
}
