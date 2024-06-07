/**
 * 
 */
package com.linyang.energy.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.page.Page;
import com.linyang.energy.model.ModuleBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.RoleBean;
import com.linyang.energy.service.RoleService;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;
import com.linyang.util.DateUtils;

/** 
 * @类功能说明： 角色管理的控制类
 * @公司名称：江苏林洋电子有限公司
 * @作者：吴雄雄  
 * @创建时间：2013-12-5 下午02:58:50  
 * @版本：V1.0  */
@Controller
@RequestMapping("/roleController")
public class RoleController extends BaseController{
	@Autowired
	private RoleService roleService;

	/**
	 * 
	 * 函数功能说明 :跳转到角色列表页面
	 * @return ModelAndView
	 */
	@RequestMapping("/gotoRoleManage")
	public ModelAndView gotoRoleManage() {
		Map<String, Object> map = new HashMap<String, Object>();
		String currentTime = DateUtils.getCurrentTime(DateUtils.FORMAT_LONG);
		map.put("currentTime", currentTime);
		return new ModelAndView("energy/system/roleManage",map);
	}
	/**
	 * 函数功能说明  :获取用户的菜单
	 * @return  List<ModuleBean>
	 */
	@RequestMapping("/getModule")
	public @ResponseBody List<ModuleBean>getModule(HttpServletRequest request){
		return roleService.getModule(super.getSessionUserInfo(request).getAccountId());
	}
	/**
	 * 函数功能说明  :列表页面Iframe进入子页面
	 * @param roleId 角色ID
	 * @return  ModelAndView
	 */
	@RequestMapping("/gotoRoleAddUpdate")
	public ModelAndView gotoRoleAddUpdate(HttpServletRequest request,String roleId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(CommonMethod.isNotEmpty(roleId)){
			map.put("type",1);//修改
			RoleBean role = roleService.getRoleByRoleId(Long.valueOf(roleId));
			map.put("role",role );
			map.put("status", role.getRoleStatus());
			Map<String, String> map2 = DictionaryDataFactory.getDictionaryData(request).get("system_status");
			Map<Long, String> mapList = new HashMap<Long, String>();
			for (String key : map2.keySet()) {
				mapList.put(Long.parseLong(key), map2.get(key));
			}
			map.put("statusMap",mapList);
		}else{
			map.put("type",0);//添加
			//List<ModuleBean> modeBean = roleService.getModule(super.getSessionUserInfo().getAccountId());
			//map.put("modeBean",modeBean);
		}
		
		return new ModelAndView("energy/system/role_add_update",map);
	}
	/**
	 * 函数功能说明  :取角色权限id
	 * @param roleId
	 * @return  List<ModuleBean>
	 */
	@RequestMapping("/getRoleModule")
	public @ResponseBody List<ModuleBean> getRoleModule(long roleId){
		return roleService.getModuleByRoleId(roleId);
	}
	/**
	 * 
	 * 函数功能说明  :查询角色的列表
	 * @param pageInfo 前台参数集合
	 * @return  Map<String,Object>
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getRolePageList")
	public @ResponseBody Map<String, Object> getRolePageList(HttpServletRequest request) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		Page page = null;
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null)
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
		if(queryMap.containsKey("queryMap"))
			mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
		List<Map<String,Object>> list = roleService.getRolePageList(page,mapQuery);
		Map<String, String> map2 = DictionaryDataFactory.getDictionaryData(request).get("system_status");
		Map<String, String> map3 = DictionaryDataFactory.getDictionaryData(request).get("role_type");
		for (Map<String,Object> mm : list) {
			mm.put("ROLE_STATUS", map2.get(mm.get("ROLESTATUS").toString()));
		    mm.put("ROLETYPE", map3.get(mm.get("ROLETYPE").toString()));
		}
		    map.put("queryMap",mapQuery);
			map.put("page", page);
			map.put("list", list);
		return map;
	}
	/**
     * 
     * 函数功能说明  :新增角色
     * @param role  角色参数信息
     * @return  boolean     
     */
	@RequestMapping("/addRoleInfo")
	public @ResponseBody boolean addRoleInfo (HttpServletRequest request,Long[] moduleIds){
		RoleBean role = new RoleBean();
		//默认的角色序列
		role.setRoleId(SequenceUtils.getDBSequence());
		role.setCreateUserid(super.getSessionUserInfo(request).getAccountId());
		role.setRoleName(request.getParameter("roleName"));
		role.setRoleStatus(1);
		role.setRoleDesc(request.getParameter("roleDesc"));
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("add a role:" + role.getRoleName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		isSuccess = roleService.addRoleInfo(role,Arrays.asList(moduleIds));
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_ROLE_ID,CommonOperaDefine.MODULE_NAME_ROLE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;	
		
	}
	/**
     * 
     * 函数功能说明  :修改角色信息
     * @param role  角色参数信息
     * @return  boolean     
     */ 
	@RequestMapping("/updateRoleInfo")
	public @ResponseBody boolean updateRoleInfo (HttpServletRequest request,RoleBean role,Long[] moduleIds){
	    role.setModifyUserid(super.getSessionUserInfo(request).getAccountId());
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("update a role:" + role.getRoleName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		isSuccess = roleService.updateRoleInfo(role,Arrays.asList(moduleIds));
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_ROLE_ID,CommonOperaDefine.MODULE_NAME_ROLE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
			
	}
	/**
	 * 
	 * 函数功能说明  :删除角色
	 * @param role 角色参数信息
	 * @return  boolean     
	 */
	@RequestMapping("/deleteRole")
	public @ResponseBody boolean deleteRole (HttpServletRequest request){
		String str = request.getParameter("roleId");
		if(str==null){return false;}
		long roleId = Long.valueOf(str.substring(0, str.length()-1));
        RoleBean roleBean = this.roleService.getRoleByRoleId(roleId);
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("delete a role:" + roleBean.getRoleName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		isSuccess = roleService.deleteRole(roleId);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_ROLE_ID,CommonOperaDefine.MODULE_NAME_ROLE,CommonOperaDefine.OPRATOR_OBJECT_TYPE_MODULE,result,desc.toString()),request);
		return isSuccess;
	}
	
	/**
	 * 函数功能说明  :检查角色名称是否重复
	 * @param role 角色参数信息
	 * @param operType 操作类型 1：表示新增  2：表示编辑
	 * @return  boolean
	 */
	@RequestMapping("/checkRoleName")
	public @ResponseBody boolean checkRoleName (String roleName,int operType){
	     return roleService.checkRoleName(roleName,operType);	
				
	}
	
	/**
	 * 
	 * 函数功能说明  :检查角色是否和用户关联
	 * @param roleId
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/checkRoleLink")
	public @ResponseBody boolean checkRoleLink (long roleId){
	     return roleService.isUseRole(roleId);	
				
	}
	
	/**
	 * 
	 * 函数功能说明  :检查角色是否被用户使用
	 * @param role 角色参数信息
	 * @return  boolean     
	 */
	@RequestMapping("/isUseRole")
	public @ResponseBody boolean isUseRole (HttpServletRequest request){
		String str = request.getParameter("roleId");
		if(str==null){return true;}
		String roleId = str.substring(0, str.length()-1);
		return 	roleService.isUseRole(Long.valueOf(roleId));
	}

}
