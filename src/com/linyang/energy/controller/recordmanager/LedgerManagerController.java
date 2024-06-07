package com.linyang.energy.controller.recordmanager;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.dto.LedgerTreeBean;
import com.linyang.energy.mapping.recordmanager.MeterManagerMapper;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 分户管理Controller
 * @author Leegern
 * @date Dec 9, 2013 1:37:16 PM
 */
@Controller
@RequestMapping("/ledgermanager")
public class LedgerManagerController extends BaseController {

	@Autowired
	private LedgerManagerService ledgerManagerService;

    @Autowired
	private UserAnalysisService userAnalysisService;
    
    @Autowired
    private MeterManagerMapper meterManagerMapper;

	/**
	 * 进入分户管理页面
	 * @return
	 */
	@RequestMapping(value = "/gotoLedgerManagerMain")
	public ModelAndView gotoLedgerManagerMain(){
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("rates", ledgerManagerService.queryRateInfo(new Date())); // 电 费率信息
        map.put("rates_1", ledgerManagerService.queryOtherRateInfo(2));   // 水 费率信息
        map.put("rates_2", ledgerManagerService.queryOtherRateInfo(3));   // 气 费率信息
        map.put("rates_3", ledgerManagerService.queryOtherRateInfo(4));   // 热 费率信息

		map.put("analyTypes", ledgerManagerService.queryAnalyTypes());    // 查询分析类型
		
		
		return new ModelAndView(URLConstant.URL_LEDGER_LIST, "params", map);

	}

	
	/**
	 * 查询分户信息列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryLedgerList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryLedgerList(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		List<LedgerBean> dataList = null;
		// 得到当前分页
		Page page = super.getCurrentPage(request.getParameter("pageNo"), request.getParameter("pageSize"));
        String parentName = request.getParameter("parentName");
		try {
			LedgerBean ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
			UserBean userBean = this.getSessionUserInfo(request);
            ledger.setLedgerId(this.getSessionUserInfo(request).getLedgerId());
			ledger.setUserNo(userBean.getAccountId().toString());
			// 分页查询分户信息
			dataList = ledgerManagerService.getLedgerList(page, ledger, parentName);
			//记录用户足迹
			this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_146, 35l, 1);
		} catch (IOException e) {
			Log.error(this.getClass() + ".queryLedgerList()--无法查询分户信息列表");
		}
		result.put("page", page);
		result.put("dataList", dataList);
		return result;
	}
	
	/**
	 * 合并分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mergeLedgerInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> mergeLedgerInfo(HttpServletRequest request){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		LedgerBean ledger = null;
		String flag = "true";
		int modifyType = 0;
		try {
			ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
			// 表示修改操作
			if (ledger.getLedgerId() != null && ledger.getLedgerId() != 0) {
				// 更新分户信息(只更新不为空的字段) 
				ledgerManagerService.updateBySelective(ledger);
				sb.append("update a ledger:");
				modifyType = CommonOperaDefine.LOG_TYPE_UPDATE;
			}
			// 表示添加操作
			else {
				// 添加分户信息(只插入不为空的字段) 
				ledgerManagerService.insertBySelective(ledger);
				sb.append("add a ledger:");
				modifyType = CommonOperaDefine.LOG_TYPE_INSERT;
			}
		} catch (IOException e) {
			Log.error(this.getClass() + ".mergeLedgerInfo()--无法合并分户信息");
			flag = "false";
		}
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		if(flag.equals("true")){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		String ledgerName = "";
		if(ledger!=null && ledger.getLedgerName()!=null){
			ledgerName = ledger.getLedgerName();
		}
        sb.append(ledgerName).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(modifyType, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		result.put("flag", flag);
		return result;
	}
	
	/**
	 * 修改分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/deleteLedgerInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> deleteLedgerInfo(HttpServletRequest request){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		LedgerBean ledger = null;
		String flag = "true";
		try {
			ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
			// 根据分户Id删除分户信息 
			ledgerManagerService.deleteByLedgerId(ledger.getLedgerId());
		} catch (IOException e) {
			Log.error(this.getClass() + ".deleteLedgerInfo()--无法修改分户信息");
			flag = "false";
		}
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		if(flag.equals("true")){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		String ledgerName = "";  if(ledger != null && ledger.getLedgerName() != null){ledgerName = ledger.getLedgerName();}
        sb.append("delete a ledger:").append(ledgerName).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		result.put("flag", flag);
		return result;
	}
	
	/**
	 * 查询分户关联数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLedgerRelated", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> getLedgerRelated(HttpServletRequest request){
		Map<String, String> result = new HashMap<String, String>();
		String flag = "";
		int count = 0;
		try {
			LedgerBean ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
			// 查询是否存在子分户
			//List<LedgerBean> subLedgers = ledgerManagerService.getLedgerList(new Page(), ledger);
			List<LedgerTreeBean> subLedgers = ledgerManagerService.getSubLedgerTree(ledger.getLedgerId());
			if (subLedgers.size()==0) {
				// 根据分户Id查询分户关联数
				count = ledgerManagerService.getLedgerRelatedByLedgerId(ledger.getLedgerId());
				flag = count > 0 ? "related" : "none";
			} else {
				flag = "children";  // 存在子节点
			}
		} catch (IOException e) {
			Log.error(this.getClass() + ".deleteLedgerInfo()--无法查询分户关联数据");
		}
		result.put("flag", flag);
		return result;
	}
	
	/**
	 * 根据分户Id查询分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findOneLedger", method = RequestMethod.POST)
	public @ResponseBody LedgerBean findOneLedger(HttpServletRequest request){
		LedgerBean ledgerBean = null;
		try {
			LedgerBean ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
			ledgerBean = ledgerManagerService.selectByLedgerId(ledger.getLedgerId());
		} catch (IOException e) {
			Log.error(this.getClass() + ".findOneLedger()--无法根据分户Id查询分户信息");
		}
		return ledgerBean;
	}

    /**
     * 根据分户Id查询该分户是否为企业（或者在企业下）
     * @param request
     * @return
     */
    @RequestMapping(value="/getIsLedgerFlag")
    public @ResponseBody Map<String,Object> getIsLedgerFlag(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        int ledgerFlag = 0;
        if(ledgerId > 0){
            ledgerFlag = ledgerManagerService.getIsLedgerFlag(ledgerId);
        }
        result.put("ledgerFlag", ledgerFlag);
        return result;
    }
	
	/**
	 * 根据分户Id获取分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findLedgerByLedgerId", method = RequestMethod.POST)
	public @ResponseBody LedgerBean findLedgerByLedgerId(HttpServletRequest request){
		LedgerBean bean = null;
		MeterBean meterBean = null;
		boolean flag = true;
		try {
			LedgerBean ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
			// 根据分户Id查询分户信息
			bean = ledgerManagerService.selectByLedgerId(ledger.getLedgerId());
			if (null != bean && null != bean.getParentLedgerId() && 0 != bean.getParentLedgerId()) {
				// 查询父分户名称
				LedgerBean tmp = ledgerManagerService.selectByLedgerId(bean.getParentLedgerId());
				if (null != tmp && null != tmp.getLedgerName()) {
					bean.setParentLedgerName(tmp.getLedgerName());
				}
			}
			
			if(bean == null) {
				meterBean = meterManagerMapper.getMeterDataByPrimaryKey(ledger.getLedgerId());
				bean = ledgerManagerService.selectByLedgerId(meterBean.getLedgerId());
			}


			if (bean.getParentLedgerId() != null) {
				LedgerBean parentLedger = ledgerManagerService.selectByLedgerId(bean.getParentLedgerId());
				if(parentLedger != null && parentLedger.getAnalyType() != null)
				bean.setParentAnalyType(parentLedger.getAnalyType());
			}


			/*
			 * 下面循环注释掉    bean会为null
			 */
				do{
				if((int) bean.getAnalyType() != 102 && (int) bean.getAnalyType() != 105)
					bean = ledgerManagerService.getLedgerDataById(bean.getParentLedgerId());
				if((int) bean.getAnalyType() == 102 || (int) bean.getAnalyType() == 105 )
					flag=false;
				}while(flag);

		} catch (IOException e) {
			Log.error(this.getClass() + ".findOneLedger()--无法根据分户Id获取分户信息");
		}
		return bean;
	}
	
	
	/**
	 * 根据分户Id获取分户信息(角色为管理者时,能管对象管理页面调用)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findLedgerByLedgerId2", method = RequestMethod.POST)
	public @ResponseBody LedgerBean findLedgerByLedgerId2(HttpServletRequest request){
		LedgerBean bean = null;
		MeterBean meterBean = null;
		boolean flag = true;
		try {
			LedgerBean ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
			// 根据分户Id查询分户信息
			bean = ledgerManagerService.selectByLedgerId(ledger.getLedgerId());
			if (null != bean && null != bean.getParentLedgerId() && 0 != bean.getParentLedgerId()) {
				// 查询父分户名称
				LedgerBean tmp = ledgerManagerService.selectByLedgerId(bean.getParentLedgerId());
				if (null != tmp && null != tmp.getLedgerName()) {
					bean.setParentLedgerName(tmp.getLedgerName());
				}
			}
			
			if(bean == null) {
				meterBean = meterManagerMapper.getMeterDataByPrimaryKey(ledger.getLedgerId());
				bean = ledgerManagerService.selectByLedgerId(meterBean.getLedgerId());
			}


			if (bean.getParentLedgerId() != null) {
				LedgerBean parentLedger = ledgerManagerService.selectByLedgerId(bean.getParentLedgerId());
				if(parentLedger != null && parentLedger.getAnalyType() != null)
				bean.setParentAnalyType(parentLedger.getAnalyType());
			}
			
			///这里准备开始查询所选节点是否是101/106的,如果是的话查询一下上级节点是否有已经选择了产污/治污的,如果有的话,该节点则不允许设置
			if(bean.getLedgerId() != null && (bean.getAnalyType() == 101 || bean.getAnalyType() == 106 || bean.getAnalyType() == 0)){
				List<Map<String, Object>> maps = ledgerManagerService.selectParent( bean.getLedgerId() );
				if(maps.size() < 1)
					bean.setCanSet( true );
				for (Map<String,Object> map:maps) {
					if(!map.containsKey( "POLLUTCTL_ID" ) && !map.containsKey( "POLLUT_ID" )){
						bean.setCanSet( true );
					}
					if(map.containsKey( "POLLUTCTL_ID" ) || map.containsKey( "POLLUT_ID" )){
						bean.setCanSet( false );
						break;
					}
				}
				List<Map<String, Object>> maps1 = null;
				///这里查询一次是否有关联关系
				maps1 = ledgerManagerService.queryPollut( bean.getLedgerId() );
				
				if (null != maps1 && maps1.size() > 0) {
					bean.setDelete( false );
				} else {
					maps1 = ledgerManagerService.queryPollutctl(bean.getLedgerId());
				}
				
				if (null != maps1 && maps1.size() > 0) {
					bean.setDelete( false );
				} else {
					bean.setDelete( true );
				}
			}

		} catch (IOException e) {
			Log.error(this.getClass() + ".findLedgerByLedgerId2()--无法根据分户Id获取分户信息");
		}
		return bean;
	}
	
	
	
	/**
	 * 验证名称是否重复
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/validItem", method = RequestMethod.POST)
	public @ResponseBody LedgerBean validItem(HttpServletRequest request){
		LedgerBean ledgerBean = null;
		try {
			LedgerBean ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
			ledgerBean = ledgerManagerService.findOneLedger(ledger);
		} catch (IOException e) {
			Log.error(this.getClass() + ".validItem()--无法验证名称是否重复");
		}
		return ledgerBean;
	}
	
	/**
	 * 验证同级分户名称是否重复
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLedgerName", method = RequestMethod.POST)
	public @ResponseBody boolean checkLedgerName(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put("ledgerId", request.getParameter("ledgerId"));
		map.put("ledgerName", request.getParameter("ledgerName"));
		map.put("parentId", request.getParameter("parentId"));
		return ledgerManagerService.checkLedgerName(map);
	}
	
	/**
	 * 得到分户信息列表
	 * @author guosen
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getAjaxLedgerList")
	public @ResponseBody Map<String, Object> getAjaxLedgerList(HttpServletRequest request) throws IOException{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		
		Page page = null;
		if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null) {
			int pageIndex = Integer.valueOf(queryMap.get("pageIndex").toString());
			int pageSize = Integer.valueOf(queryMap.get("pageSize").toString());
			page = new Page(pageIndex , pageSize);
		}
		else {
			page = new Page();
		}
		param.put("page", page);
		result.put("page", page);
		
		if(queryMap.containsKey("queryMap")) {
			param.putAll((Map<String,Object>)queryMap.get("queryMap"));
			result.put("queryMap", queryMap.get("queryMap"));
		}
		
		UserBean userBean = super.getSessionUserInfo(request);
		if (userBean != null) {
			if (userBean.getLedgerId() != null && userBean.getLedgerId() != 0) {
				Long ledgerId = userBean.getLedgerId();
				if (userBean.getAccountId() == 1){
					ledgerId = -100L;// 超级管理员特殊处理
				} 
				param.put("ledgerId", ledgerId);
			} else {
				Long accountId = super.getSessionUserInfo(request).getAccountId();
				param.put("accountId", accountId);
			}
		} 
		
		List<LedgerBean> ledgerList = this.ledgerManagerService.getLedgerListByParam(param);
		result.put("list", ledgerList);
		
		return result;
	}
	
	/**
	 * 查看是否可以配置分户的分期类型为企业
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkLineloss", method = RequestMethod.POST)
	public @ResponseBody boolean checkLineloss(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put("ledgerId", getLongParam(request, "ledgerId", 0));
		map.put("parentLedgerId", getLongParam(request, "parentLedgerId", 0));
		return ledgerManagerService.checkLineloss(map);
	}
	
	/**
	 * 查看是否可以配置企业属性的才可以配置电力拓扑树
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/hasLineloss", method = RequestMethod.POST)
	public @ResponseBody boolean hasLineloss(HttpServletRequest request){
		boolean isOK = false;
		long ledgerId = getLongParam(request, "ledgerId", 0);
		LedgerBean bean =  ledgerManagerService.selectByLedgerId(ledgerId);
		if(bean != null && bean.getAnalyType() == 102){//企业属性的才可以配置电力拓扑树
			isOK = true;
		}
		return isOK;
	}
	
	/**
	 * 进入第三方设置,加载配置信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gotoThirdCompanySet")
	public ModelAndView gotoThirdCompanySet(HttpServletRequest request){
		UserBean userBean = super.getSessionUserInfo(request);
		String loginPath = "login";
		if(request.getSession().getAttribute("loginPath")!=null)
			loginPath = request.getSession().getAttribute("loginPath").toString();
		return new ModelAndView("/energy/system/third_company_set",ledgerManagerService.getLogoConfig(userBean.getLedgerId(),loginPath,2));
	}
	
	/**
	 * 第三方图片上传
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/uploadThirdCompanyImg")
	public @ResponseBody Map<String,Object> uploadThirdCompanyImg(HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		boolean isSuccess = false; String msg = "上传失败,发生未知异常";
		resultMap.put("isSuccess", isSuccess);resultMap.put("msg", msg);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request; if(multipartRequest == null) return resultMap;
        MultipartFile multipartFile = multipartRequest.getFile("filePath"); if (multipartFile == null) return resultMap;
        InputStream inputStream = null;
		try {
            inputStream = multipartFile.getInputStream(); BufferedImage image = ImageIO.read(inputStream);
			if(multipartFile.getSize() > 2*1024*1024){
				isSuccess = false;
				msg = "上传失败,图片大小超出范围";
			} else if(image.getWidth() > 500 || image.getHeight() > 80){
				isSuccess = false;
				msg = "上传失败,图片尺寸超出范围";
			} else {
				String filePath = request.getContextPath() + File.separator + "upload" + File.separator + "logo" + File.separator; // 文件存储文件夹路径
				String fileName = multipartFile.getOriginalFilename();
				String saveName = SequenceUtils.getDBSequence() + fileName.substring(fileName.lastIndexOf("."));	// 图片存储名
				File file = null;
                if(!DataUtil.checkFilePath(filePath) && !DataUtil.checkFilePath(saveName) ){file = new File(filePath + saveName);}   File parentFile = null; if(file!=null) { parentFile= file.getParentFile();}
				if(file == null || parentFile==null  || !file.exists())		
					if(!parentFile.mkdirs()){
						msg = "文件创建失败";
					}
				if(!file.exists() && file != null)
					multipartFile.transferTo(file);
				isSuccess = true;
				msg = "上传成功";
				resultMap.put("filePath", filePath);
				resultMap.put("fileName", saveName);
			}
		} catch (IOException e) {
			Log.info("uploadThirdCompanyImg error IOException");
			isSuccess = false;
			msg = "上传失败,发生未知异常";
		}
        finally {if(null != inputStream){try {inputStream.close();} catch (IOException e) {Log.error(this.getClass() + ".uploadThirdCompanyImg() when stream close!");}}}
        resultMap.put("isSuccess", isSuccess);
		resultMap.put("msg", msg);
		return resultMap;
	}
	
	/**
	 * 读取文件
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/readThirdCompanyImg")
	public void readThirdCompanyImg(HttpServletRequest request,HttpServletResponse response) {
        OutputStream ous = null; InputStream ios = null;
        try {String filePath = request.getContextPath() + File.separator + "upload" + File.separator + "logo" + File.separator; // 文件存储文件夹路径
            File file = null;
            String fileName="";String tempFileName=super.getStrParam(request, "fileName", "");
            if(null!=tempFileName){fileName=tempFileName;}
            if(!DataUtil.checkFilePath(filePath) && !DataUtil.checkFilePath(fileName)){ file = new File(filePath + fileName); }
            if (file != null && file.exists()) {
                ous = response.getOutputStream();
                response.setContentType("image/jpeg; charset=GBK");
                ios = new FileInputStream(file);
                byte buffer[] = new byte[1024];
                int index;
                while ((index = ios.read(buffer)) != -1) {
                    ous.write(buffer, 0, index);
                }
            }
        } catch (IOException e) {
			Log.error(this.getClass() + ".readUploadThirdComImg()--无法读取文件");
        } finally {
            if(ous != null){try {ous.flush(); ous.close();} catch (IOException e) {Log.error(this.getClass() + ".readUploadThirdComImg()--ous出错");}}
            if(ios != null){try {ios.close();} catch (IOException e) {Log.error(this.getClass() + ".readUploadThirdComImg()--IOS出错");}}
        }
	}
	
	/**
	 * 保存第三方设置
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/saveThirdCompanySet")
	public @ResponseBody Map<String,Object> saveThirdCompanySet(HttpServletRequest request) {
		Map<String,Object> resultMap = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		boolean isSuccess = false;
		LedgerBean bean;
		
			bean = ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
			String logoColor = getStrParam(request, "logoColor", null);
			String logoIcon = getStrParam(request, "logoIcon", null);
			if(StringUtils.isNotBlank(logoColor))
				bean.setLogoColor(logoColor);
			if(StringUtils.isNotBlank(logoIcon))
				bean.setLogoIcon(logoIcon);
			//try{
			isSuccess = ledgerManagerService.saveThirdCompanySet(bean);
//			isSuccess = true;
			//}catch(Exception e){Log.info(this.getClass()+".saveThirdCompanySet--save third company set fail!");}
			sb.append("update a ledger:").append(bean.getLedgerName());
		
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess)
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		sb.append(" by ").append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		resultMap.put("isSuccess", isSuccess);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_152, 123l, 1);
		return resultMap;
	}

    /**
     * 保存分户坐标
     */
    @RequestMapping(value="/saveLedgerPosition")
    public @ResponseBody Map<String,Object> saveLedgerPosition(HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        boolean isSuccess = false;
        resultMap.put("isSuccess", isSuccess);
        
        //校验CSRF Token	
        String tokenIn = request.getHeader("RequestVerificationToken");
        String tokenSession = CSRFTokenManager.getTokenFromSession(request.getSession());
        if (!validateData(tokenIn) || !validateData(tokenSession) || !tokenIn.equals(tokenSession)) {
        	return resultMap;
        }
        
        LedgerBean ledger;
        try {
            Long ledgerId = getLongParam(request, "ledgerId", -1);
            ledger = ledgerManagerService.getLedgerDataById(ledgerId);
            String xstr=getStrParam(request, "x", ""),ystr=getStrParam(request, "y", "");
            Double x=0d,y=0d;
            if(null!=xstr&&xstr.length()<=20&&xstr.length()>0) x = Double.valueOf(xstr);
            if(null!=ystr&&ystr.length()<=20&&ystr.length()>0) y = Double.valueOf(ystr);
            ledger.setX(x);
            ledger.setY(y);
            ledgerManagerService.saveLedgerPosition(ledger);
            isSuccess = true;
            sb.append("update a ledger:").append(ledger.getLedgerName());
        } catch (NumberFormatException e) {
            isSuccess = false;
            Log.info("saveLedgerPosition error NumberFormatException");
        }
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        sb.append(" by ").append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }
    /**
     * 删除分户坐标
     */
    @RequestMapping(value="/deleteLedgerPosition")
    public @ResponseBody Map<String,Object> deleteLedgerPosition(HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        boolean isSuccess = false;
        LedgerBean ledger;
       
            Long ledgerId = getLongParam(request, "ledgerId", -1);
            ledger = ledgerManagerService.getLedgerDataById(ledgerId);
            try{ledgerManagerService.deleteLedgerPosition(ledgerId);
            isSuccess = true;}catch(RuntimeException e){Log.info(this.getClass()+"deleteLedgerPosition()--delete ledger position faile");}
            sb.append("update a ledger:").append(ledger.getLedgerName());
        
        int rst = CommonOperaDefine.OPRATOR_FAIL;
        if(isSuccess){
            rst =  CommonOperaDefine.OPRATOR_SUCCESS;
        }
        sb.append(" by ").append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }

    /**
     *  企业地图展示图片上传
     */
    @RequestMapping(value="/uploadShowCompanyImg")
    public @ResponseBody Map<String,Object> uploadShowCompanyImg(HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String, Object>();
        boolean isSuccess = false;String msg = "获取文件失败";
        resultMap.put("isSuccess", isSuccess);resultMap.put("msg", msg);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        MultipartFile multipartFile = multipartRequest.getFile("filePath");if(!validateData(multipartFile)) return resultMap;
        InputStream inputStream = null;
        try { inputStream = multipartFile.getInputStream();if(!validateData(inputStream)) return resultMap;
            BufferedImage image = ImageIO.read(inputStream);
            if(multipartFile.getSize() > 2*1024*1024){
                isSuccess = false;
                msg = "上传失败,图片大小超出范围";
            } else if(image.getWidth() > 140 || image.getHeight() > 100){
                isSuccess = false;
                msg = "上传失败,图片尺寸超出范围";
            } else {
                String filePath = request.getContextPath() + File.separator + "upload" + File.separator + "logo" + File.separator; // 文件存储文件夹路径
                String fileName = "";if(multipartFile.getOriginalFilename().length()>0&&multipartFile.getOriginalFilename()!=null){fileName = multipartFile.getOriginalFilename(); }// 图片原名
                String saveName = SequenceUtils.getDBSequence() + fileName.substring(fileName.lastIndexOf(".")); // 图片存储名
                File file = null;
                if(!DataUtil.checkFilePath(filePath) && !DataUtil.checkFilePath(saveName)){file = new File(filePath + saveName);}	 File parentFile = null; if(file!=null) { parentFile= file.getParentFile();}
                if(file == null || parentFile == null || !file.exists())
                	if(!parentFile.exists()&&!parentFile.mkdirs()){
                		throw new IOException("创建目录失败");
                	}
                if(!file.exists() && file !=null)
                    multipartFile.transferTo(file);
                isSuccess = true;
                msg = "上传成功";
                resultMap.put("filePath", filePath);
                resultMap.put("fileName", saveName);
            }
        } catch (IOException e) {
        	Log.info("uploadShowCompanyImg error IOException");
            isSuccess = false;
            msg = "上传失败,发生未知异常";
        }finally {if(inputStream != null){try {inputStream.close();} catch (IOException e) {Log.error(this.getClass()+" uploadShowCompanyImg inputStream close error!");}}}
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * 获得地图需要显示的企业 相关数据
     */
    @RequestMapping(value="/getMapShowLedgerList")
    public @ResponseBody Map<String,Object> getMapShowLedgerList(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
        Integer searchModel = getIntParams(request, "searchModel", 1);
        String selectIdStr = getStrParam(request, "selectIdStr", "");
        String keyWord = getStrParam(request, "keyWord", "");
        List<LedgerBean> list = this.ledgerManagerService.getMapShowLedgerList(ledgerId, searchModel, selectIdStr, keyWord);
        result.put("list", list);

        //记录用户足迹
        Integer useRecord = getIntParams(request, "useRecord", 0);
        if(useRecord == 1){
            this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_60, null, 1);
        }
        return result;
    }

    /**
     * 获得某个EMO的电、水、气、热的用量数据
     */
    @RequestMapping(value="/getLedgerUseData")
    public @ResponseBody Map<String,Object> getLedgerUseData(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        if(ledgerId < 0){
            ledgerId = super.getSessionUserInfo(request).getLedgerId();
        }
        LedgerBean ledger = ledgerManagerService.getLedgerDataById(ledgerId);
        result.put("ledger", ledger);
        Map<String, Object> energyUse = this.ledgerManagerService.getLedgerUseData(ledger);
        result.putAll(energyUse);
        return result;
    }
    /**
     * 获得某个EMO的企业户数、监测点个数、运营商个数
     */
    @RequestMapping(value="/getLedgerMessageData")
    public @ResponseBody Map<String,Object> getLedgerMessageData(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        if(ledgerId < 0){
            ledgerId = super.getSessionUserInfo(request).getLedgerId();
        }
        Map<String, Object> energy = this.ledgerManagerService.getLedgerMessageData(ledgerId);
        result.putAll(energy);
        return result;
    }
    /**
     * 获得某个EMO的实时功率
     */
    @RequestMapping(value="/getLedgerPowerData")
    public @ResponseBody Map<String,Object> getLedgerPowerData(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        if(ledgerId < 0){
            ledgerId = super.getSessionUserInfo(request).getLedgerId();
        }
        Map<String, Object> energy = this.ledgerManagerService.getLedgerPowerData(ledgerId);
        result.putAll(energy);
        return result;
    }

    
    /**
	 * 得到emo计算模型已配置的DCP所有类型
	 * @param request
	 * @return
	 */
	@RequestMapping("/getLedgerCalcDCPType")
	public @ResponseBody List<Short> getLedgerCalcDCPType(HttpServletRequest request){
		long ledgerId = getLongParam(request, "ledgerId", 0);
		return ledgerManagerService.getLedgerCalcDCPType(ledgerId);
	}

    /**
	 * 获取能管对象id得到父级能管对象
	 * @param request
	 * @return
	 */
	@RequestMapping("/getParentLedgerByLedgerId")
	public @ResponseBody LedgerBean getParentLedgerByLedgerId(HttpServletRequest request){
		long ledgerId = getLongParam(request, "ledgerId", 0);
        LedgerBean ledgerBean = ledgerManagerService.selectByLedgerId(ledgerId);
        if(ledgerBean != null && ledgerBean.getParentLedgerId() != null && ledgerBean.getParentLedgerId() != 0){
            return ledgerManagerService.selectByLedgerId(ledgerBean.getParentLedgerId());
        }else{
            return null;
        }
	}
	
	 /**
	 * 获取采集点id得到父级能管对象
	 * @param request
	 * @return
	 */
	@RequestMapping("/getParentLedgerByMeterId")
	public @ResponseBody Long getParentLedgerByMeterId(HttpServletRequest request){
		long meterId = getLongParam(request, "meterId", 0);
		Long ledgerId = ledgerManagerService.getLedgerByMeterId(meterId);
		if (ledgerId != null) {
			return ledgerId;
		}
		return 0l;
	}

	/**
	 * 进入第三方网页链接
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/gotoThirdLinkSet")
	public ModelAndView gotoThirdLinkSet(HttpServletRequest request){
		return new ModelAndView("/energy/system/third_link_set");
	}
	
	/**
	 * 保存第三方链接
	 * @return
	 */
	@RequestMapping("/saveCompanyLink")
	public @ResponseBody Map<String, Object> saveCompanyLink(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		Long ledgerId = getLongParam(request, "ledgerId", 0);
		String urlStr = getStrParam(request, "url", "");
		boolean isOk = ledgerManagerService.saveCompanyLink(ledgerId, urlStr);
		result.put("isOk", isOk);
		
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_153, 222l, 1);
		return result;
	} 
	
	/**
	 * 保存第三方链接
	 * @return
	 */
	@RequestMapping("/getCompanyLink")
	public @ResponseBody Map<String, Object> getCompanyLink(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		Long ledgerId = getLongParam(request, "ledgerId", 0);
		LedgerBean ledger = ledgerManagerService.selectByLedgerId(ledgerId);
		result.put("url", ledger.getUrl());
		return result;
	} 
    
	/**
	 * 从工程师切换到管理者，获得左侧结构树焦点ledger
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLedgerInfoByMeterId", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getLedgerInfoByMeterId(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		long meterId = getLongParam(request, "meterId", 0);
		long ledgerId = getLongParam(request, "ledgerId", 0);
		map = ledgerManagerService.getLedgerInfoByMeterId(meterId, ledgerId);
		return map;
	}

    /**
     * 进入 能源地图
     * @param request
     * @return
     */
    @RequestMapping("/gotoEnergyMap")
    public ModelAndView gotoEnergyMap(HttpServletRequest request){
        return new ModelAndView("/energy/system/energy_map");
    }

    /**
     * 能源地图--数据查询
     */
    @RequestMapping(value = "/getEnergyMapData", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> getEnergyMapData(HttpServletRequest request){
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_127, 39l, 1);//记录用户足迹
        return this.ledgerManagerService.getEnergyMapData(ledgerId);
    }

    /**
     * 能源地图--图片上传
     */
    @RequestMapping(value="/uploadEnergyMapImg")
    public @ResponseBody Map<String,Object> uploadEnergyMapImg(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        boolean isSuccess = false;
        String msg = "上传失败,发生异常";
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("msg", msg);
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        if(multipartRequest == null){
            return resultMap;
        }
        MultipartFile multipartFile = multipartRequest.getFile("filePath");
        if (multipartFile == null){
            return resultMap;
        }

        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            if(multipartFile.getSize() > 2*1024*1024){
                isSuccess = false;
                msg = "上传失败,图片大小超出范围";
            }
            else if(image.getWidth() > 1800 || image.getHeight() > 1200){
                isSuccess = false;
                msg = "上传失败,图片尺寸超出范围";
            }
            else {
                String filePath = request.getContextPath() + File.separator + "upload" + File.separator + "energyMap" + File.separator; // 文件存储文件夹路径
                String fileName = multipartFile.getOriginalFilename();
                String saveName = SequenceUtils.getDBSequence() + fileName.substring(fileName.lastIndexOf("."));	// 图片存储名
                File file = null;
                if(!DataUtil.checkFilePath(filePath) && !DataUtil.checkFilePath(saveName) ){
                    file = new File(filePath + saveName);
                }
                File parentFile = null; if(file!=null) { parentFile= file.getParentFile();}
                if(file == null || parentFile == null  || !file.exists())
                    if(!parentFile.mkdirs()){
                        msg = "文件创建失败";
                    }
                if(!file.exists() && file != null){
                    multipartFile.transferTo(file);
                }
                resultMap.put("fileName", saveName);

                isSuccess = true;
                msg = "上传成功";
                //图片名入库
                long ledgerId = getLongParam(request, "ledgerId", 0);
                if(ledgerId > 0){
                    ledgerManagerService.saveLedgerBackImg(ledgerId, saveName);
                }
            }
        }
        catch (IOException e) {
            Log.info("uploadEnergyMapImg error IOException");
            isSuccess = false;
            msg = "上传失败,发生异常";
        }
        finally {if(null != inputStream){try {inputStream.close();} catch (IOException e) {Log.error(this.getClass() + ".uploadEnergyMapImg() when stream close!");}}}
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * 能源地图--图片读取
     */
    @RequestMapping(value="/readEnergyMapImg")
    public void readEnergyMapImg(HttpServletRequest request,HttpServletResponse response){
        OutputStream ous = null; InputStream ios = null;
        try {String filePath = request.getContextPath() + File.separator + "upload" + File.separator + "energyMap" + File.separator; // 文件存储文件夹路径
            File file = null;
            String fileName="";String tempFileName=super.getStrParam(request, "fileName", "");
            if(null!=tempFileName){fileName=tempFileName;}
            if(!DataUtil.checkFilePath(filePath) && !DataUtil.checkFilePath(fileName)){ file = new File(filePath + fileName); }
            if (file != null && file.exists()) {
                ous = response.getOutputStream();
                response.setContentType("image/jpeg; charset=GBK");
                ios = new FileInputStream(file);
                byte buffer[] = new byte[1024];
                int index;
                while ((index = ios.read(buffer)) != -1) {
                    ous.write(buffer, 0, index);
                }
            }
        } catch (IOException e) {
            Log.error(this.getClass() + ".readEnergyMapImg()--无法读取文件");
        } finally {
            if(ous != null){try {ous.flush(); ous.close();} catch (IOException e) {Log.error(this.getClass() + ".readEnergyMapImg()--ous出错");}}
            if(ios != null){try {ios.close();} catch (IOException e) {Log.error(this.getClass() + ".readEnergyMapImg()--IOS出错");}}
        }
    }

    /**
     * 能源地图--添加、修改能管对象展示样式
     */
    @RequestMapping(value = "/insertOrUpdateLedgerStyle")
    public @ResponseBody Map<String,Object> insertOrUpdateLedgerStyle(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        try {
            Long ledgerId = super.getLongParam(request, "ledgerId", 0);
            Integer displayForm = super.getIntParams(request, "displayForm", 1);
            Integer fontSize = super.getIntParams(request, "fontSize", 1);
            String fontWeight = super.getStrParam(request, "fontWeight", "normal");
            String fontColor = super.getStrParam(request, "fontColor", "#455058");
            Double bubble = Double.valueOf(super.getStrParam(request, "bubble", "1"));
            String backColor = super.getStrParam(request, "backColor", "");
            String dataColor = super.getStrParam(request, "dataColor", "");
            if(ledgerId > 0){
                ledgerManagerService.saveLedgerStyle(ledgerId, displayForm, fontSize, fontWeight, fontColor, bubble, backColor, dataColor);
            }
        }
        catch (Exception e){
            Log.info("insertOrUpdateLedgerStyle error!");
            isSuccess = false;
        }

        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }

    /**
     * 能源地图--添加、修改 关联的坐标点（能管对象或者采集点）
     */
    @RequestMapping(value = "/insertOrUpdateLedgerRelate")
    public @ResponseBody Map<String,Object> insertOrUpdateLedgerRelate(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Long ledgerId = super.getLongParam(request, "ledgerId", 0);

            Long oldObjectId = super.getLongParam(request, "oldObjectId", -1);
            Integer oldObjectType = super.getIntParams(request, "oldObjectType", -1);
            Long newObjectId = super.getLongParam(request, "newObjectId", -1);
            Integer newObjectType = super.getIntParams(request, "newObjectType", -1);

            String dataType = super.getStrParam(request, "dataType", "");
            Double x = Double.valueOf(super.getStrParam(request, "xData", "0"));
            Double y = Double.valueOf(super.getStrParam(request, "yData", "0"));
            Integer newPosition = super.getIntParams(request, "newPosition", 1);

            String str = ledgerManagerService.saveLedgerRelate(ledgerId, oldObjectId, oldObjectType, newObjectId, newObjectType, dataType, x, y, newPosition);
            if(StringUtils.isNotEmpty(str)){
                isSuccess = false;
                message = str;
            }
        }
        catch (Exception e){
            Log.info("insertOrUpdateLedgerRelate error!");
            isSuccess = false;
            message = "保存失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 能源地图-- 删除 关联的坐标点（能管对象或者采集点）
     */
    @RequestMapping(value = "/removeLedgerRelate")
    public @ResponseBody Map<String,Object> removeLedgerRelate(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        try {
            Long ledgerId = super.getLongParam(request, "ledgerId", 0);
            Long oldObjectId = super.getLongParam(request, "oldObjectId", -1);
            Integer oldObjectType = super.getIntParams(request, "oldObjectType", -1);


            ledgerManagerService.removeLedgerRelate(ledgerId, oldObjectId, oldObjectType);
        }
        catch (Exception e){
            Log.info("removeLedgerRelate error!");
            isSuccess = false;
        }

        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }



    /**
     * 能源地图--查询每个坐标点的数据
     */
    @RequestMapping(value = "/getOnePointDataInImg")
    public @ResponseBody Map<String,Object> getOnePointDataInImg(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        Long objectId = super.getLongParam(request, "objectId", 0);
        Integer objectType = super.getIntParams(request, "objectType", 0);

        if(ledgerId > 0 && objectId > 0 && objectType > 0){
            resultMap.putAll(ledgerManagerService.getOnePointDataInImg(objectId, objectType, ledgerId));
        }

        return resultMap;
    }

    /**
     * 能源地图--弹出图上坐标点详细页面
     */
    @RequestMapping("/gotoOnePointDetail")
    public ModelAndView gotoOnePointDetail(HttpServletRequest request){
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        Long objectId = super.getLongParam(request, "objectId", -1);
        Integer objectType = super.getIntParams(request, "objectType", -1);

        Double xData = Double.valueOf(super.getStrParam(request, "x", "0"));
        Double yData = Double.valueOf(super.getStrParam(request, "y", "0"));

        Map<String, Object> map = this.ledgerManagerService.getObjectNeedData(ledgerId, objectId, objectType);
        map.put("objectId", objectId);
        map.put("objectType", objectType);
        map.put("xData", xData);
        map.put("yData", yData);
        return new ModelAndView("/energy/system/energy_map_onePoint",map);
    }

    /**
     * 能源地图--自动完成结果
     */
    @RequestMapping("/getRelateLedgerMeterList")
    public @ResponseBody String getRelateLedgerMeterList(HttpServletRequest request){
        Integer searchModel = super.getIntParams(request, "searchModel", 0);
        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        Long objectId = super.getLongParam(request, "objectId", -1);
        Integer objectType = super.getIntParams(request, "objectType", -1);
        String objectName = super.getStrParam(request, "objectName", "");

        List<Map<String, Object>> dataList = ledgerManagerService.getRelateLedgerMeterList(ledgerId, searchModel, objectId, objectType, objectName);
        StringBuffer sb = new StringBuffer("[");
        String str = "";
        //按照插件所需要的格式拼接字符串
        if(dataList != null && dataList.size()>0) {
            for(Map<String,Object> data : dataList) {
                sb.append("{id:'").append(data.get("ID"))
                        .append("',label:'").append(data.get("NAME"))
                        .append("',value:'").append(data.get("NAME"))
                        .append("',num:'").append(data.get("ID"))
                        .append("',count:'0'},");
            }
            str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
        }
        else {
            str = "[]";
        }
        return str;
    }

    /**
     * 能源地图--判断是否能修改为液晶模式
     */
    @RequestMapping(value = "/getIfCanChangeYj")
    public @ResponseBody Map<String,Object> getIfCanChangeYj(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        Long ledgerId = super.getLongParam(request, "ledgerId", 0);
        resultMap.put("flag", this.ledgerManagerService.getIfCanChangeYj(ledgerId));

        return resultMap;
    }

    /**
     * 进入“重点数据维护”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/gotoDataMaintain")
    public ModelAndView gotoDataMaintain(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        String maxDate = DateUtil.convertDateToStr(DateUtil.getMonthDate(new Date(), 12), DateUtil.MONTH_PATTERN);
        params.put("maxDate", maxDate);

        return new ModelAndView("/energy/system/dataMaintain", params);
    }

    /**
     *
     * 重点数据维护 -- 列表
     */
    @RequestMapping(value = "/dataMaintainList", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> dataMaintainList(HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));

            Map<String, Object> mapQuery = new HashMap<String, Object>();

            Page page = null;
            if(queryMap.get("pageIndex")!= null && queryMap.get("pageSize") != null){
                page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
            }
            else {
                page = new Page(1,10);
            }
            mapQuery.put("page", page);

            Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
            mapQuery.put("ledgerId", ledgerId);

            if(queryMap.get("searchName")!= null){
                mapQuery.put("searchName", queryMap.get("searchName").toString());
            }
            // 分页查询
            List<Map<String,Object>> dataList = ledgerManagerService.getDataMaintainPage(mapQuery);
            result.put("page", page);
            result.put("list",dataList);
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".dataMaintainList()");
        }

        return result;
    }

    /**
     * 重点数据维护 -- 得到下拉de企业列表
     * @return
     */
    @RequestMapping("/getCompLedgerList")
    public @ResponseBody String getCompLedgerList(HttpServletRequest request){
        Long ledgerId = super.getSessionUserInfo(request).getLedgerId();
        List<Map<String, Object>> dataList = this.ledgerManagerService.getCompLedgerList(ledgerId);

        //按照插件所需要的格式拼接字符串
        String str = "";
        if(dataList != null && dataList.size()>0) {
            StringBuffer sb = new StringBuffer("[");
            for(Map<String,Object> data : dataList) {
                sb.append("{id:'").append(data.get("LEDGER_ID"))
                        .append("',label:'").append(data.get("LEDGER_NAME"))
                        .append("',value:'").append(data.get("LEDGER_NAME"))
                        .append("',num:'").append(data.get("LEDGER_ID"))
                        .append("',count:'0'},");
            }
            str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
        }
        else {
            str = "[]";
        }
        return str;
    }

    /**
     * 重点数据维护 -- 删除
     */
    @RequestMapping(value = "/deleteMaintain")
    public @ResponseBody Map<String,Object> deleteMaintain(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        this.ledgerManagerService.deleteMaintain(ledgerId);

        return resultMap;
    }

    /**
     * 重点数据维护 -- 详情弹出页
     */
    @RequestMapping(value = "/dataMaintainDetail")
    public ModelAndView dataMaintainDetail(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        if(ledgerId > 0){
            params.putAll(this.ledgerManagerService.getDataMaintain(ledgerId));
        }
        params.put("ledgerId", ledgerId);

        return new ModelAndView("energy/system/dataMaintainDetail", params);
    }

    /**
     * 重点数据维护 -- 保存
     */
    @RequestMapping(value = "/saveDataMaintain")
    public @ResponseBody Map<String,Object> saveDataMaintain(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> info = jacksonUtils.readJSON2Map(request.getParameter("info"));
            this.ledgerManagerService.saveDataMaintain(info);
        }
        catch (IOException e){
            isSuccess = false;
            message = "保存错误";
        }
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }
	
	/**
	 * 对设备编号进行验证,同一企业下同一产污 or 治污类型下的设备编号不能相同
	 * @param request
	 * @return
	 */
//	@RequestMapping(value = "/checkdeviceCode", method = RequestMethod.POST)
//	public @ResponseBody boolean checkdeviceCode(HttpServletRequest request){
//		LedgerBean ledger = null;
//
//		Map<String,Object> param = new HashMap<>( 0 );
//
//		boolean flag = false;
//
//		try {
//			ledger = super.jacksonUtils.readJSON2Bean(LedgerBean.class, request.getParameter("paramInfo"));
//
//			Long parentId = ledgerManagerMapper.getCompanyId(ledger.getLedgerId());//得到上级企业id
//
//			param.put( "parentId" ,  parentId );
//
//			param.put( "ledgerId" ,  ledger.getLedgerId() );	//企业id
//
//			param.put( "deviceCode" ,  ledger.getDeviceCode() );	//设备编号
//
//			// 0 治污   1 产污
//			if(ledger.getPollutType() == 0)	//产污或者治污id
//			{
//				param.put( "pol" ,  ledger.getPollutId() );
//			}
//			if(ledger.getPollutType() == 1)
//			{
//				param.put( "pol" ,  ledger.getPollutctlId() );
//			}
//			param.put( "pollutType" , ledger.getPollutType());
//
//			flag = ledgerManagerService.checkdeviceCode(param);
//
//		} catch (IOException e) { }
//
//		return flag;
//	}

}