package com.linyang.energy.controller.yunNanEnergy.EnterpriseBaseFile;

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
@RequestMapping("/waterPowerGasController")
public class WaterPowerGasController extends BaseController {

	 @Autowired
	 private EntBaseFileService entBaseFileService;

    /**
             * 进入“水电气户号”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/waterPowerGas")
    public ModelAndView waterPowerGas(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();

        return new ModelAndView("energy/yunNan/EntBaseFileManage/waterPowerGasUserId", params);
    }
    
    /**
   	 * @param request
   	 * @return
   	 */
   	@RequestMapping(value = "/getWaterPowerGas", method = RequestMethod.POST)
   	public @ResponseBody Map<String, Object> getWaterPowerGas(HttpServletRequest request,String entId,String accType){
   		return entBaseFileService.getWaterPowerGasInfo(Long.parseLong(entId),Integer.parseInt(accType));
   	}
   	
   	/**
   	 * 合并分户信息
   	 * @param request
   	 * @return
   	 */
   	@RequestMapping(value = "/mergeWaterPowerGas", method = RequestMethod.POST)
   	public @ResponseBody Map<String, String> mergeWaterPowerGas(HttpServletRequest request){
   		Map<String, String> resultmap = new HashMap<String, String>();
   		int flag=0;
   		Map<String, Object> procMap=null;
   		try {
   			procMap = jacksonUtils.readJSON2Map(request.getParameter("procInfo"));
   		} catch (IOException e) {
   			e.printStackTrace();
   		}
   		if(procMap!=null) {
   			flag=entBaseFileService.saveWaterPowerGasInfo(procMap);
   		}
   		
   		resultmap.put("flag", flag+"");
   		return resultmap;
   	}
   	
   	/**
	 * 上传工序信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadWPGInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> uploadWPGInfo(HttpServletRequest request,String entId,String accType,String typeId){
		Map<String, String> result = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();		
		int rst = CommonOperaDefine.OPRATOR_FAIL;
		result=entBaseFileService.uploadWaterPowerGas(entId,accType, typeId);
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
