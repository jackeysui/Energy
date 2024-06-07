/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.linyang.energy.controller;

import com.linyang.common.web.page.Page;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.model.GroupBean;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.GroupService;
// import com.linyang.energy.service.TreeSetService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OperItemConstant;
import com.linyang.energy.utils.SequenceUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
//import net.sf.json.util.JSONUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/groupController")
public class GroupController extends BaseController {
    @Autowired
	private GroupService groupService;
 
    @Autowired
    private UserAnalysisService userAnalysisService;
    
    @RequestMapping("/gotoGroupManage")
	public ModelAndView gotoGroupManage(HttpServletRequest request) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
    	map.put("ledgerId", ledgerId);
		return new ModelAndView("energy/system/groupManage", map);
	}
    @RequestMapping("/gotoGroupConfig")
	public ModelAndView gotoGroupConfig() {
		return new ModelAndView("energy/system/groupConfig");
	}
    @RequestMapping("/checkGroupName")
	public @ResponseBody boolean checkGroupName(HttpServletRequest request) 
	{
		boolean isSuccess = true;
		String groupName = request.getParameter("groupName");
        Long groupId = getLongParam(request,"groupId", 0);
        isSuccess = groupService.checkNewGroupName(groupName, groupId);
		return isSuccess;	
	}
    @SuppressWarnings("unchecked")
    @RequestMapping("/getGroupPageList")
	public @ResponseBody Map<String, Object> getGroupPageList(HttpServletRequest request) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		Page page = null;
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
        
		if(queryMap.containsKey("queryMap"))
			mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
        
        int groupType = mapQuery.get("groupType") == null ? 1: mapQuery.get("groupType").toString().length()==0? 1:Integer.parseInt(mapQuery.get("groupType").toString());//1-客户组；2-计量点组
        String groupName = (String)mapQuery.get("groupName");
        
        List<GroupBean> list = null;
		UserBean user = this.getSessionUserInfo(request);
		if(user.getAccountId() == null){
            list = new ArrayList<GroupBean>();
		}else{
            list = groupService.getGroupPageList(page, user.getAccountId(), groupType,groupName);
        }
        map.put("queryMap",mapQuery);
        map.put("page", page);
        map.put("list", list);
	
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_135, 209l, 1);
		return map;
	}
    
    @RequestMapping("/queryGroupInfo")
	public @ResponseBody GroupBean queryGroupInfo(HttpServletRequest request)
	{
		Long groupId = Long.parseLong(request.getParameter("groupId"));
		GroupBean group = groupService.getGroupById(groupId);
		UserBean user= this.getSessionUserInfo(request);
		
		if( group.getAccountId().compareTo(user.getAccountId())==0)//如果群组的创建ID和当前用户ID相等，则说明这个组是这个用户创建的，拥有修改权
			group.setIfCurrentUserGroup(true);
		else
			group.setIfCurrentUserGroup(false);
		
		return group;
	}
    
    @RequestMapping("/addGroupInfo")
	public @ResponseBody boolean addGroupInfo(HttpServletRequest request,GroupBean group){
		//默认的用户序列
		group.setGroupId(SequenceUtils.getDBSequence());
		
		UserBean user= this.getSessionUserInfo(request);
		group.setAccountId(user.getAccountId());
		
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("add a group:" + group.getGroupName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = groupService.addGroupInfo(group);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
		
	}
    
    @RequestMapping("/updateGroupInfo")
	public @ResponseBody boolean updateGroupInfo(HttpServletRequest request,GroupBean group){	
		UserBean user= this.getSessionUserInfo(request);

		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("update a group:" + group.getGroupName()).
		append(" by ").
		append(user.getLoginName()).
		append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
//		group.setAccountId(user.getAccountId());
		
		isSuccess = groupService.updateGroupInfo(group);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
		
	}
    
    @RequestMapping("/deleteGroup")
	public @ResponseBody boolean deleteGroup(HttpServletRequest request){
        String groupIds = super.getStrParam(request, "groupIds", "");
        if(groupIds==null){return true;}
        if(groupIds.length()==0){
            return true;
        }
		boolean isSuccess = false;
		UserBean user= this.getSessionUserInfo(request);
		
		StringBuffer desc = new StringBuffer();
		desc.append("delete group:").
		append(groupIds)
        .append(" by ").
		append(user.getLoginName()).
		append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		
		try{
            String[] strArray = groupIds.split(",");
            for(String str : strArray){
                groupService.deleteGroup(Long.parseLong(str));
            }
			isSuccess = true;
		}
		catch(NumberFormatException ec){
			isSuccess =  false;
		}
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_USER_ID,CommonOperaDefine.MODULE_NAME_GROUP,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
	}
    
    @RequestMapping("/getLedgerTree")
	public @ResponseBody List<LedgerTreeBean> getLedgerTree(HttpServletRequest request)
	{
        long nodeId = getLongParam(request, "nodeId", 0);
        int nodeType = getIntParams(request, "nodeType", 1);
        String nodeName = request.getParameter("nodeName");
        
        if(nodeType == 1){
            if(nodeId != 0){
                return groupService.getCompanyTree(nodeId,nodeName);
            }else{
                UserBean user = this.getSessionUserInfo(request);
                if(user.getLedgerId() != null){
                    return groupService.getCompanyTree(user.getLedgerId(),nodeName);
                }else{
                    return null;
                }
            }
        }else{
            return groupService.getDcpTree(nodeId);
        }
	}
    
    @RequestMapping("/getGroupTree")
	public @ResponseBody List<LedgerTreeBean> getGroupTree(HttpServletRequest request)
	{
        int nodeType = getIntParams(request, "nodeType", 1);
        UserBean user = this.getSessionUserInfo(request);
		if(user.getAccountId() == null)
        {
            return null;
        }
        return groupService.getGroupTree(user.getAccountId(),nodeType);
	}
    
    @RequestMapping("/saveConfig")
	public @ResponseBody boolean saveConfig(HttpServletRequest request) throws UnsupportedEncodingException, IOException{
		boolean isSuccess = false;
        List<HashMap<String, Object>> itemList = jacksonUtils.readJSON2Genric(request.getParameter("items"),new TypeReference<List<HashMap<String, Object>>>(){});
        for (HashMap<String, Object> map : itemList) {
            Long groupId = Long.parseLong(map.get("groupId").toString());
            String ledgerIds = map.get("ledgerIds").toString();
            String[] ledgerMap = null;
            if(ledgerIds.length()>0){
                ledgerMap = ledgerIds.split(",");
            }
            isSuccess = groupService.updateGroupLedgerRelation(ledgerMap, groupId);
        }
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_136, 210l, 1);
		return isSuccess;
	}
    
    @RequestMapping("/isModifyAllow")
	public @ResponseBody boolean isModifyAllow(HttpServletRequest request){
        Long groupId = Long.parseLong(request.getParameter("groupId"));
        UserBean user = this.getSessionUserInfo(request);
		if(user.getAccountId()==1)//如果是超级管理员,直接获取分户或者计量点，不需要考虑权限
        {
            return true;
        }else{
            GroupBean gBean = groupService.getGroupById(groupId);
            if(gBean != null){
                return gBean.getAccountId().equals(user.getAccountId());
            }
            return true;
        }
	}
}
