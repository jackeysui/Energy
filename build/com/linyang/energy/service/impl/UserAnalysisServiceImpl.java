package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.linyang.energy.mapping.authmanager.ModuleBeanMapper;
import com.linyang.energy.model.ModuleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jpush.api.utils.StringUtils;

import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.mapping.userAnalysis.AccountLoginHisMapper;
import com.linyang.energy.mapping.userAnalysis.AccountTraceMapper;
import com.linyang.energy.model.AccountLoginHisBean;
import com.linyang.energy.model.AccountTraceBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;

@Service
public class UserAnalysisServiceImpl implements UserAnalysisService {

	@Autowired
	private AccountTraceMapper accountTraceMapper;
	@Autowired
	private AccountLoginHisMapper accountLoginHisMapper;
	@Autowired
	private LedgerManagerMapper ledgerManagerMapper;
    @Autowired
	private ModuleBeanMapper moduleBeanMapper;

	@Override
	public void addAccountTrace(Long accountId, Long operItemId, Long moduleId, int operClient) {
		AccountTraceBean trace = new AccountTraceBean();
		trace.setTraceId(SequenceUtils.getDBSequence());
		trace.setAccountId(accountId);
		trace.setModuleId(moduleId);
		trace.setOperItemId(operItemId);
		trace.setOperateTime(new Date());
        trace.setOperClient(operClient);
		this.accountTraceMapper.addAccountTrace(trace);
	}

	@Override
	public void addAccountLogin(Long accountId, Date date, int type,
			String osVersion) {
		AccountLoginHisBean loginBean = new AccountLoginHisBean();
		loginBean.setAccountId(accountId);
		loginBean.setLoginDate(date);
		loginBean.setLoginSoftwareType(type);
		loginBean.setOsVersion(osVersion);
        int num = this.accountLoginHisMapper.checkAccountLoginHis(accountId, date);
        if(num == 0){
            this.accountLoginHisMapper.addAccountLoginHis(loginBean);
        }
	}

	@Override
	public List<Map<String, Object>> clickRateAnalysis(Map<String, Object> queryMap) {
		Date lastDate = DateUtil.getMonthDate(new Date(), -1);
		queryMap.put("lastDate", lastDate);
		queryMap.put("beginTime", DateUtil.convertStrToDate(queryMap.get("beginTime")+" 00:00:00"));
		queryMap.put("endTime", DateUtil.convertStrToDate(queryMap.get("endTime")+" 23:59:59"));
		Date nowDate = DateUtil.clearDate(new Date());
		queryMap.put("nowDate",nowDate);
		queryMap.put("7days", DateUtil.getDateBetween(nowDate, -7));//7天
		queryMap.put("14days", DateUtil.getDateBetween(nowDate, -14));//14天
		queryMap.put("21days", DateUtil.getDateBetween(nowDate, -21));//21天
		queryMap.put("30days", DateUtil.getDateBetween(nowDate, -30));//30天
		String ledgerIdstr = (String) queryMap.get("ledgerId");
		if(StringUtils.isNotEmpty(ledgerIdstr)){
			String[] ledgerIds = ledgerIdstr.split(",");			
			queryMap.put("list", ledgerIds);
			List<Long> ids = accountTraceMapper.getAllLedgerId(queryMap);
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            int maxLen = ids.size()/1000 + 1;
            for(int i = 0; i < maxLen; i++){
                if(i == maxLen - 1){
                    queryMap.put("list", ids.subList(1000*i, ids.size()));
                    List<Map<String, Object>> temp = this.accountTraceMapper.clickRateAnalysis(queryMap);
                    result.addAll(temp);
                }
                else {
                    queryMap.put("list", ids.subList(1000*i, 1000*i + 1000));
                    List<Map<String, Object>> temp = this.accountTraceMapper.clickRateAnalysis(queryMap);
                    result.addAll(temp);
                }
            }
            return result;
		}
        else {
			queryMap.put("ledgerId", "");
            List<Map<String, Object>> result = this.accountTraceMapper.clickRateAnalysis(queryMap);
            return result;
		}
	}

	@Override
	public List<Map<String, Object>> clickActivity(Map<String, Object> queryMap) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Date lastDate = DateUtil.getMonthDate(new Date(), -1);
		queryMap.put("lastDate", lastDate);
		queryMap.put("beginTime", DateUtil.convertStrToDate(queryMap.get("beginTime")+" 00:00:00"));
		queryMap.put("endTime", DateUtil.convertStrToDate(queryMap.get("endTime")+" 23:59:59"));
		Date nowDate = DateUtil.clearDate(new Date());
		queryMap.put("nowDate",nowDate);
		queryMap.put("7days", DateUtil.getDateBetween(nowDate, -7));//7天
		queryMap.put("14days", DateUtil.getDateBetween(nowDate, -14));//14天
		queryMap.put("21days", DateUtil.getDateBetween(nowDate, -21));//21天
		queryMap.put("30days", DateUtil.getDateBetween(nowDate, -30));//30天
		String ledgerIdstr = (String) queryMap.get("ledgerId");
		if(StringUtils.isNotEmpty(ledgerIdstr)){
			String[] ledgerIds = ledgerIdstr.split(",");
			for (String lId : ledgerIds) {
				LedgerBean ledger = ledgerManagerMapper.selectByLedgerId(Long.parseLong(lId));
				String[] lIds = new String[1];
				lIds[0] = lId;
				queryMap.put("list", lIds);
				List<Long> ids = accountTraceMapper.getAllLedgerId(queryMap);
				List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
                int maxLen = ids.size()/1000 + 1;
                for(int i = 0; i < maxLen; i++){
                    if(i == maxLen - 1){
                        queryMap.put("array", ids.subList(1000*i, ids.size()));
                        List<Map<String, Object>> temp = this.accountTraceMapper.clickActivity(queryMap);
                        maps.addAll(temp);
                    }
                    else {
                        queryMap.put("array", ids.subList(1000*i, 1000*i + 1000));
                        List<Map<String, Object>> temp = this.accountTraceMapper.clickActivity(queryMap);
                        maps.addAll(temp);
                    }
                }
				for (Map<String, Object> map : maps) {
					map.put("LEDGERNAME", ledger.getLedgerName());
					map.put("LEDGERID", ledger.getLedgerId());
				}
				list.addAll(maps);
			}
		}else {
			queryMap.put("ledgerId", -1);
		}
		return list;
	}

	@Override
	public Map<String, Object> buildExcelData(Map<String, Object> queryMap) {
		return null;
	}

	@Override
	public void optExcel(ServletOutputStream outputStream,
			Map<String, Object> result, Map<String, Object> param) {
		
	}

	@Override
	public void calAccountLevel() {
		Date beginTime = DateUtil.clearDate(DateUtil.addDateDay(new Date(), -1));
		Date endTime = DateUtil.clearDate(new Date());

		long accountId;
		int tnum;
		List<Map<String, Object>> loginNum = accountTraceMapper.getAccountLoginNum(beginTime, endTime);
		for (Map<String, Object> num : loginNum) {
			if (num != null){
			accountId = Long.valueOf(num.get("ACCOUNTID").toString());
			tnum = Integer.valueOf(num.get("TNUM").toString());
			accountTraceMapper.updateAccountLevel(accountId, tnum * 5);
			}
		}

		List<Map<String, Object>> traceNum = accountTraceMapper.getAccountTraceNum(beginTime, endTime);
		for (Map<String, Object> num : traceNum) {
			if (num != null){
			accountId = Long.valueOf(num.get("ACCOUNTID").toString());
			tnum = Integer.valueOf(num.get("TNUM").toString());
			accountTraceMapper.updateAccountLevel(accountId, tnum);
		}
		}
	}

    @Override
    public List<Map<String, Object>> getUsuallyUseModel(Long accountId){
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> moduleList = this.accountTraceMapper.getMostAccountTrace(accountId);
        if(moduleList != null && moduleList.size() > 0){
            int size = 4;
            if(moduleList.size() < size){
                size = moduleList.size();
            }
            for(int i = 0; i < size; i++){
                Map<String, Object> map = moduleList.get(i);
                Long parentId = Long.valueOf(map.get("PARENT_ID").toString());
                ModuleBean parentBean = this.moduleBeanMapper.selectByPrimaryKey(parentId);
                String parentName = parentBean.getModuleName();
                map.put("PARENT_NAME", parentName);
                result.add(map);
            }
        }
        return result;
    }

}
