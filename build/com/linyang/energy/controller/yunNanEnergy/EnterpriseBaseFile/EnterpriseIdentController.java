package com.linyang.energy.controller.yunNanEnergy.EnterpriseBaseFile;

import com.linyang.common.web.common.Log;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.service.EntBaseFileService;
import com.linyang.energy.service.YunNanService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 20-5-12.
 */
@Controller
@RequestMapping("/enterpriseIdentController")
public class EnterpriseIdentController extends BaseController {

	 @Autowired
	 private EntBaseFileService entBaseFileService;

    /**
             * 进入“企业认证信息”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/enterpriseIdent")
    public ModelAndView enterpriseIdent(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defaultDate",DateUtil.getCurrentDateStr(DateUtil.SHORT_PATTERN));
        return new ModelAndView("energy/yunNan/EntBaseFileManage/entIdentInfo", params);
    }

    /**
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getEntIdentInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEntIdentInfo(HttpServletRequest request,String entId){
		return entBaseFileService.getEntIdentInfo(Long.parseLong(entId));
	}
	
	/**
	 * 合并分户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/mergeEntIdentInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> mergeEntIdentInfo(HttpServletRequest request){
		Map<String, String> resultmap = new HashMap<String, String>();
		int flag=0;
		Map<String, Object> procMap=null;
		try {
			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(procMap!=null) {
			flag=entBaseFileService.saveEntIdentInfo(procMap);
		}
		
		resultmap.put("flag", flag+"");
		return resultmap;
	}
	
	/**
	 * 上传工序信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadEntIdent", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> uploadEntIdent(HttpServletRequest request,String entId,String typeId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();		
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		result=entBaseFileService.uploadEntIdent(entId, typeId);
		if(result.get("flag").toString().equals("true")){
			rst =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		
        sb.append("upload a em:").append(entId).append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE, CommonOperaDefine.MODULE_NAME_LEDGER_ID, CommonOperaDefine.MODULE_NAME_LEDGER, CommonOperaDefine.OPRATOR_OBJECT_TYPE_LEDGER, rst, sb.toString()), request);
		return result;
	}
}
