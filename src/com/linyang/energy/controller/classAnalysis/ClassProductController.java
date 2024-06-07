package com.linyang.energy.controller.classAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.common.web.common.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.common.URLConstant;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.ProductConfigBean;
import com.linyang.energy.model.ProductOutputBean;
import com.linyang.energy.service.ClassProductService;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.WebConstant;

/**
 * 产品控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/classProduct")
public class ClassProductController extends BaseController{
	
	@Autowired
    private ClassProductService classProductService;
	
	@Autowired
    private LedgerManagerService ledgerManagerService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;
	
	/**
     * 进入产品配置页面   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoProductConfig")
    public ModelAndView gotoProductConfig(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super.getSessionUserInfo(request).getLedgerId());
        params.put("loginLedger", bean);

        return new ModelAndView(URLConstant.URL_PRODUCT_CONFIG, params);
    }
	
	/**
     * 根据ledgerId得到产品配置列表
     * @param request
     * @return
     */
	@RequestMapping(value = "/queryProductConfigList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryProductConfigList(HttpServletRequest request){
		Map<String, Object>  paramInfo = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ProductConfigBean> list = null;
		Long ledgerId = super.getLongParam(request, "ledgerId", -1);
		Integer pageIndex = super.getIntParams(request, "pageIndex", 1);
        Integer pageSize = super.getIntParams(request, "pageSize", 10);
        Page page = new Page(pageIndex, pageSize);
		if (ledgerId > 0) {
			paramInfo.put("ledgerId", ledgerId);
			paramInfo.put("page", page);
			list = this.classProductService.queryProductConfigPageList(paramInfo);
		} 
		
		resultMap.put("page", page);
		if (list != null && list.size() > 0) {
			resultMap.put("list", list);
		}
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_133, 126l, 1);
		return resultMap;
	}
	
	/**
	 * 根据productId获得产品配置信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gotoProductConfigDetail")
	public ModelAndView gotoProductConfigDetail(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> unitList = new ArrayList<String>();
		Map<String, Object> productConfig = null;
		long ledgerId = super.getLongParam(request, "ledgerId", -1); 
		long productId = super.getLongParam(request, "productId", -1);
		if (productId > 0) {
			productConfig = this.classProductService.getProductConfigById(productId);
		}
		if(productConfig != null){
			resultMap.putAll(productConfig);
		}
		
		//获取静态数据库中的单位
		Map<String, String> map = DictionaryDataFactory.getDictionaryData(request).get("unit_type");
		if(map!=null){
		for (String value : map.values()) {  
			unitList.add(value);
		}
		}
		resultMap.put("unitList", unitList);
		resultMap.put("LEDGER_ID", ledgerId);
		return new ModelAndView("energy/classmanager/add_update_product_config", resultMap);
	}
	
	 /**
     * 新增 产品配置
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertProductConfig")
    public @ResponseBody Map<String,Object> insertProductConfig(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> productConfigInfo = jacksonUtils.readJSON2Map(request.getParameter("productConfigInfo"));
            Map<String, Object> result = this.classProductService.insertProductConfig(productConfigInfo);
            String resultMessage = result.get("message").toString();
            if(resultMessage.length()>0){  //保存失败
                isSuccess = false;
                message = resultMessage;
            }
        } catch (IOException e) {
        	Log.info("insertProductConfig error IOException");
            isSuccess = false;
            message = "保存失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 修改 产品配置
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateProductConfig")
    public @ResponseBody Map<String,Object> updateProductConfig(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> productConfigInfo = jacksonUtils.readJSON2Map(request.getParameter("productConfigInfo"));
            this.classProductService.updateProductConfig(productConfigInfo);
        } catch (IOException e) {
        	Log.info("updateProductConfig error IOException");
            isSuccess = false;
            message = "保存失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }
    
    /**
     * 删除产品配置
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteProductConfig")
    public @ResponseBody Map<String,Object> deleteProductConfig(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        String message = "删除成功";
        boolean isSuccess = true;
        
        try {
            String productStr = super.getStrParam(request, "productStr", "");
            if(productStr!=null){
            String[] items = productStr.split(",");
            for(int i = 0; i < items.length; i++){
                String item = items[i];
                Long productId = Long.valueOf(item);
                resultMap = this.classProductService.deleteProductConfig(productId);
            }
            String resultMessage = resultMap.get("message").toString();
            if(resultMessage.length()>0){  //删除失败
                isSuccess = false;
                message = resultMessage;
            }}
        } catch (NumberFormatException e) {
        	Log.info("deleteProductConfigriptionInfo error NumberFormatExceptionception");
            isSuccess = false;
            message = "删除失败";
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }
    
    /**
     * 进入产量页面   ---------------------------------------------------
     * @return
     */
    @RequestMapping(value = "/gotoProductOutput")
    public ModelAndView gotoProductOutput(HttpServletRequest request){
		Date lastMonthDate = DateUtil.getLastMonthDate(WebConstant
				.getChartBaseDate());
		Map<String, Object> params = new HashMap<String, Object>();
		LedgerBean bean = this.ledgerManagerService.getLedgerDataById(super
				.getSessionUserInfo(request).getLedgerId());
		params.put("startTime", DateUtil.convertDateToStr(lastMonthDate,
				DateUtil.MOUDLE_PATTERN));
		params.put("endTime", DateUtil.convertDateToStr(WebConstant
				.getChartBaseDate(), DateUtil.MOUDLE_PATTERN));
		params.put("loginLedger", bean);

        return new ModelAndView(URLConstant.URL_PRODUCT_OUTPUT, params);
    }
    
    /**
     * 根据条件获取产量列表
     * @param request
     * @return
     */
	@RequestMapping(value = "/queryProductOutputPageList")
	public @ResponseBody Map<String, Object> queryProductOutputPageList(HttpServletRequest request){
		Map<String, Object>  paramInfo = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ProductOutputBean> list = null;
		Long ledgerId = super.getLongParam(request, "ledgerId", -1);
		Long productId = super.getLongParam(request, "productId", -1);
		Long workshopId = super.getLongParam(request, "workshopId", -1);
		Long teamId = super.getLongParam(request, "teamId", -1);
		Integer pageIndex = super.getIntParams(request, "pageIndex", 1);
        Integer pageSize = super.getIntParams(request, "pageSize", 10);
        Page page = new Page(pageIndex, pageSize);
		if (ledgerId > 0) {
			paramInfo.put("ledgerId", ledgerId);
			paramInfo.put("productId", productId);
			paramInfo.put("workshopId", workshopId);
			paramInfo.put("teamId", teamId);
			paramInfo.put("startTime", DateUtil.convertStrToDate(super.getStrParam(request, "startTime", null),DateUtil.MOUDLE_PATTERN));
			paramInfo.put("endTime", DateUtil.convertStrToDate(super.getStrParam(request, "endTime", null),DateUtil.MOUDLE_PATTERN));
			
			paramInfo.put("page", page);
			list = this.classProductService.queryProductOutputPageList(paramInfo);
		} 
		
		resultMap.put("page", page);
		if (list != null && list.size() > 0) {
			resultMap.put("list", list);
		}
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_134, 127l, 1);
		return resultMap;
	}
	
	/**
     * 根据ledgerId获取产品列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getProductListByLedger")
    public @ResponseBody Map<String,Object> getProductListByLedger(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        Long classId = super.getLongParam(request, "classId", -1);
        String workshopIds = super.getStrParam(request, "workshopIds", "");
        List<Map<String,Object>> list = null;
        if(ledgerId > 0){
            list = this.classProductService.getProductListByLedger(ledgerId, classId, workshopIds);
        }
        
       result.put("list", list);
       return result;
    }
    
    /**
     * 根据productId获取车间列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getWorkshopListByProductId")
    public @ResponseBody Map<String,Object> getWorkshopListByProductId(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long productId = super.getLongParam(request, "productId", -1);
        List<Map<String,Object>> list = null;
        if(productId > 0){
            list = this.classProductService.getWorkshopListByProductId(productId);
        }
        
       result.put("list", list);
       return result;
    }
    
    /**
     * 根据productId,workshopId获取班组列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTeamListByProductId")
    public @ResponseBody Map<String,Object> getTeamListByProductId(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long productId = super.getLongParam(request, "productId", -1);
        Long workshopId = super.getLongParam(request, "workshopId", -1);
        List<Map<String,Object>> list = null;
        if(productId > 0 && workshopId > 0){
            list = this.classProductService.getTeamListByProductId(productId, workshopId);
        }
        
       result.put("list", list);
       return result;
    }
    
    /**
	 * 根据ledgerId获得车间，产品信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gotoProductOutputDetail")
	public ModelAndView gotoProductOutputDetail(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String,Object>> productlist = null;
		List<Map<String,Object>> workshoplist = null;
		Map<String, Object> productOutput = null;
		String productUnit = "";
		long ledgerId = getLongParam(request, "ledgerId", -1);
		long outputId = getLongParam(request, "outputId", -1);
		//初始化获取企业下的产品列表和车间列表
		if (ledgerId > 0) {
			productlist = this.classProductService.getProductListByLedgerAll(ledgerId);
			workshoplist = this.classProductService.getWorkshopListByLedgerId(ledgerId);
		}
		//获取产量信息与产品单位
		if (outputId > 0) {
			productOutput = this.classProductService.getProductOutputById(outputId);
			Long productId = Long.valueOf(productOutput.get("PRODUCT_ID").toString());
			productUnit = this.classProductService.getProductUnitByProductId(productId);
			resultMap.putAll(productOutput);
			resultMap.put("PRODUCT_UNIT", productUnit);
		}
		resultMap.put("LEDGER_ID", ledgerId);
		resultMap.put("productlist", productlist);
		resultMap.put("workshoplist", workshoplist);
		
		return new ModelAndView("energy/classmanager/add_update_output", resultMap);
	}
    
    /**
     * 根据ledgerId获取车间列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getWorkshopListByLedgerId")
    public @ResponseBody Map<String,Object> getWorkshopListByLedgerId(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long ledgerId = super.getLongParam(request, "ledgerId", -1);
        List<Map<String,Object>> list = null;
        if(ledgerId > 0){
            list = this.classProductService.getWorkshopListByLedgerId(ledgerId);
        }
        
       result.put("list", list);
       return result;
    }
    
    /**
     * 根据workshopId获取班组列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/getTeamListByWorkshopId")
    public @ResponseBody Map<String,Object> getTeamListByWorkshopId(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long workshopId = super.getLongParam(request, "workshopId", -1);
        List<Map<String,Object>> list = null;
        if(workshopId > 0){
            list = this.classProductService.getTeamListByWorkshopId(workshopId);
        }
        
       result.put("list", list);
       return result;
    }
    
    /**
     * 根据productId获取单位
     * @param request
     * @return
     */
    @RequestMapping(value = "/getProductUnitByProductId")
    public @ResponseBody Map<String,Object> getProductUnitByProductId(HttpServletRequest request){
        Map<String,Object> result = new HashMap<String, Object>();
        Long productId = super.getLongParam(request, "productId", -1);
        String unit = "";
        if(productId > 0){
            unit = this.classProductService.getProductUnitByProductId(productId);
        }
        
       result.put("unit", unit);
       return result;
    }
    
    /**
     * 新增 产量
     * @param request
     * @return
     */
    @RequestMapping(value = "/insertOrUpdateProductOutput")
    public @ResponseBody Map<String,Object> insertOrUpdateProductOutput(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        try {
            Map<String, Object> productOutputInfo = jacksonUtils.readJSON2Map(request.getParameter("productOutputInfo"));
            Map<String, Object> result = this.classProductService.insertOrUpdateProductOutput(productOutputInfo);
            String resultMessage = result.get("message").toString();
            if(resultMessage.length()>0){  //保存失败
                isSuccess = false;
                message = resultMessage;
            }
        } catch (IOException e) {
        	Log.info("insertOrUpdateProductOutput error IOException");
            message = "保存失败";
            isSuccess = false;
        }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }
    
    /**
     * 删除产量
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteProductOutputById")
    public @ResponseBody Map<String,Object> deleteProductOutputById(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = false;
        try {
        	String outputStr = super.getStrParam(request, "outputStr", "");
        	if(outputStr!=null){
	        	String[] items = outputStr.split(",");
	        	for (int i = 0; i < items.length; i++) {
	        		String item = items[i];
	                Long outputId = Long.valueOf(item);
	                this.classProductService.deleteProductOutputById(outputId);
				}
	        	
	        	isSuccess = true;
        	}  
        } catch (RuntimeException e) {
            Log.info(this.getClass() + ".deleteProductConfig()", e);
            isSuccess = false;
        }

        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }
}
