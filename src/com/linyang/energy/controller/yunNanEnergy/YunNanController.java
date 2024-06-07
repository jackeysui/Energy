package com.linyang.energy.controller.yunNanEnergy;

import com.leegern.util.DateUtil;
import com.linyang.common.web.common.Log;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.controller.yunNanEnergy.CollectConfigManage.CollConfigStaticData;
import com.linyang.energy.model.IndustryBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.service.YunNanService;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.SequenceUtils;
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
 * Created by Administrator on 20-5-12.
 */
@Controller
@RequestMapping("/yunNanController")
public class YunNanController extends BaseController {

    @Autowired
    private YunNanService yunNanService;


    /**
     * 进入“平台接入管理”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/joinManage")
    public ModelAndView joinManage(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        return new ModelAndView("energy/yunNan/joinManage", params);
    }

    /**
     * “平台接入管理”页面 初始化页面数据
     */
    @RequestMapping(value="/joinManageData")
    public @ResponseBody Map<String,Object> joinManageData(HttpServletRequest request) {
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        return this.yunNanService.joinManageData(ledgerId);
    }

    /**
     * 获取“单位类型”下拉列表
     */
    @RequestMapping(value="/getBaseDataEntType")
    public @ResponseBody Map<String,Object> getBaseDataEntType(HttpServletRequest request) {
        String regionId = super.getStrParam(request, "regionId", "530000");
        return this.yunNanService.getBaseDataEntType(regionId);
    }

    /**
     * 查询区域树信息
     */
    @RequestMapping(value = "/queryIndustryInfo", method = RequestMethod.POST)
    public @ResponseBody List<IndustryBean> queryIndustryInfo(){
        return this.yunNanService.queryIndustryInfo(null);
    }

    /**
     *  "营业执照"图片上传
     */
    @RequestMapping(value="/uploadCompanyImg")
    public @ResponseBody Map<String,Object> uploadCompanyImg(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();
        boolean isSuccess = false;
        String msg = "上传失败,发生异常";

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
        if(multipartRequest == null){
            resultMap.put("isSuccess", isSuccess);
            resultMap.put("msg", msg);
            return resultMap;
        }
        MultipartFile multipartFile = multipartRequest.getFile("filePath");
        if (multipartFile == null){
            resultMap.put("isSuccess", isSuccess);
            resultMap.put("msg", msg);
            return resultMap;
        }

        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            String filePath = request.getContextPath() + File.separator + "upload" + File.separator + "yunNan" + File.separator; // 文件存储文件夹路径
            String fileName = multipartFile.getOriginalFilename();
            String saveName = SequenceUtils.getDBSequence() + fileName.substring(fileName.lastIndexOf("."));	// 图片存储名
            File file = null;
            if(!DataUtil.checkFilePath(filePath) && !DataUtil.checkFilePath(saveName) ){
                file = new File(filePath + saveName);
            }
            File parentFile = null;
            if(file != null){
                parentFile= file.getParentFile();
            }

            if(parentFile == null || !parentFile.exists()){
                if(!parentFile.mkdirs()){
                    msg = "文件夹创建失败";
                    resultMap.put("isSuccess", isSuccess);
                    resultMap.put("msg", msg);
                    return resultMap;
                }
            }

            if(!file.exists() && file != null){
                multipartFile.transferTo(file);
            }

            isSuccess = true;
            msg = "上传成功";
            resultMap.put("fileName", saveName);
        }
        catch (IOException e) {
            Log.info("uploadCompanyImg error IOException");
            isSuccess = false;
            msg = "上传失败,发生异常";
        }
        finally {if(null != inputStream){try {inputStream.close();} catch (IOException e) {Log.error(this.getClass() + ".uploadCompanyImg() when stream close!");}}}
        resultMap.put("isSuccess", isSuccess);
        resultMap.put("msg", msg);
        return resultMap;
    }

    /**
     * "营业执照"图片读取
     */
    @RequestMapping(value="/readCompanyImg")
    public void readCompanyImg(HttpServletRequest request,HttpServletResponse response){
        OutputStream ous = null; InputStream ios = null;
        try {String filePath = request.getContextPath() + File.separator + "upload" + File.separator + "yunNan" + File.separator; // 文件存储文件夹路径
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
            Log.error(this.getClass() + ".readCompanyImg()--无法读取文件");
        } finally {
            if(ous != null){try {ous.flush(); ous.close();} catch (IOException e) {Log.error(this.getClass() + ".readCompanyImg()--ous出错");}}
            if(ios != null){try {ios.close();} catch (IOException e) {Log.error(this.getClass() + ".readCompanyImg()--IOS出错");}}
        }
    }

    /**
     * 1.企业信息注册--保存
     * 2.保存 企业"统一社会信用代码"和"省级平台访问密码"
     */
    @RequestMapping(value = "/insertUpdateCompany")
    public @ResponseBody Map<String,Object> insertUpdateCompany(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        try {
            Map<String, Object> companyInfo = jacksonUtils.readJSON2Map(request.getParameter("companyInfo"));
            this.yunNanService.insertUpdateCompany(companyInfo);
        }
        catch (IOException e){
            isSuccess = false;
        }

        resultMap.put("isSuccess", isSuccess);
        return resultMap;
    }

    /**
     * 申请工作密钥
     */
    @RequestMapping("/applySk")
    public @ResponseBody Map<String,Object> applySk(HttpServletRequest request){
        Map<String, Object> resultMap = null;

        Map<String, Object> companyInfo = null;
        try {
            companyInfo = jacksonUtils.readJSON2Map(request.getParameter("companyInfo"));
            resultMap = this.yunNanService.applySk(companyInfo);
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".applySk()");
        }

        return resultMap;
    }

    /**
     * 企业信息注册--申请
     */
    @RequestMapping("/applyCompany")
    public @ResponseBody Map<String,Object> applyCompany(HttpServletRequest request){
        Map<String, Object> resultMap = null;

        Map<String, Object> companyInfo = null;
        try {
            companyInfo = jacksonUtils.readJSON2Map(request.getParameter("companyInfo"));
            String filePath = request.getContextPath() + File.separator + "upload" + File.separator + "yunNan" + File.separator;
            resultMap = this.yunNanService.applyCompany(companyInfo, filePath);
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".applyCompany()");
        }

        return resultMap;
    }

    /**
     * 重置
     */
    @RequestMapping("/resetRegist")
    public @ResponseBody Map<String,Object> resetRegist(HttpServletRequest request){
        Long entId = getLongParam(request, "entId", -1);
        Map<String, Object> resultMap = this.yunNanService.resetRegist(entId);
        return resultMap;
    }

    /**
     * 获取AK--申请
     */
    @RequestMapping("/applyAK")
    public @ResponseBody Map<String,Object> applyAK(HttpServletRequest request){
        Long entId = super.getLongParam(request, "entId", -1);
        return this.yunNanService.applyAK(entId);
    }

    /**
     * 平台版本校验
     */
    @RequestMapping("/applyCheckVersion")
    public @ResponseBody Map<String,Object> applyCheckVersion(HttpServletRequest request){
        Long entId = super.getLongParam(request, "entId", -1);
        return this.yunNanService.applyCheckVersion(entId);
    }

    /**
     * 平台基础配置地址查询
     */
    @RequestMapping("/applyPlatUrls")
    public @ResponseBody Map<String,Object> applyPlatUrls(HttpServletRequest request){
        Long entId = super.getLongParam(request, "entId", -1);
        return this.yunNanService.applyPlatUrls(entId);
    }

    /**
     * 平台基础数据下载
     */
    @RequestMapping("/downloadBaseData")
    public @ResponseBody Map<String,Object> downloadBaseData(HttpServletRequest request){
        Long entId = super.getLongParam(request, "entId", -1);
        Integer itemIndex = super.getIntParams(request, "itemIndex", 1);
        return this.yunNanService.downloadBaseData(entId, itemIndex);
    }


    /**
     * 进入“机构能源消费”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/institutionalEnergy")
    public ModelAndView institutionalEnergy(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        return new ModelAndView("energy/yunNan/instituteEnergy", params);
    }

    /**
     * “机构能源消费”页面--初始化页面数据
     */
    @RequestMapping(value="/instituteEnergyData")
    public @ResponseBody Map<String,Object> instituteEnergyData(HttpServletRequest request) {
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        return this.yunNanService.instituteEnergyData(ledgerId);
    }

    /**
     * “机构能源消费”页面--载入历史数据
     */
    @RequestMapping(value="/instituteEnergyCopy")
    public @ResponseBody Map<String,Object> instituteEnergyCopy(HttpServletRequest request) {
        Long entId = getLongParam(request, "entId", -1);
        String date = getStrParam(request, "date", "");
        return this.yunNanService.instituteEnergyCopy(entId, date);
    }

    /**
     * “机构能源消费”页面--手工上传
     */
    @RequestMapping("/reportInstituteEnergy")
    public @ResponseBody Map<String,Object> reportInstituteEnergy(HttpServletRequest request){
        Map<String, Object> resultMap = null;

        Map<String, Object> resrContInfo = null;
        try {
            resrContInfo = jacksonUtils.readJSON2Map(request.getParameter("resrContInfo"));
            resultMap = this.yunNanService.reportInstituteEnergy(resrContInfo);
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".reportInstituteEnergy()");
        }

        return resultMap;
    }

    /**
     * 进入“机构机房消费”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/mechanismRoom")
    public ModelAndView mechanismRoom(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        return new ModelAndView("energy/yunNan/mechanismRoom", params);
    }

    /**
     * “机构机房消费”页面--初始化页面数据
     */
    @RequestMapping(value="/mechanismRoomData")
    public @ResponseBody Map<String,Object> mechanismRoomData(HttpServletRequest request) {
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        return this.yunNanService.mechanismRoomData(ledgerId);
    }

    /**
     * “机构机房消费”页面--载入历史数据
     */
    @RequestMapping(value="/mechanismRoomCopy")
    public @ResponseBody Map<String,Object> mechanismRoomCopy(HttpServletRequest request) {
        Long entId = getLongParam(request, "entId", -1);
        String date = getStrParam(request, "date", "");
        return this.yunNanService.mechanismRoomCopy(entId, date);
    }

    /**
     * “机构机房消费”页面--手工上传
     */
    @RequestMapping("/reportMechanismRoom")
    public @ResponseBody Map<String,Object> reportMechanismRoom(HttpServletRequest request){
        Map<String, Object> resultMap = null;

        Map<String, Object> contInfo = null;
        try {
            contInfo = jacksonUtils.readJSON2Map(request.getParameter("contInfo"));
            resultMap = this.yunNanService.reportMechanismRoom(contInfo);
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".reportMechanismRoom()");
        }

        return resultMap;
    }


    /**
     * 进入“机构采暖消费”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/mechanismHeating")
    public ModelAndView mechanismHeating(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        return new ModelAndView("energy/yunNan/mechanismHeating", params);
    }

    /**
     * “机构采暖消费”页面--初始化页面数据
     */
    @RequestMapping(value="/mechanismHeatingData")
    public @ResponseBody Map<String,Object> mechanismHeatingData(HttpServletRequest request) {
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        return this.yunNanService.mechanismHeatingData(ledgerId);
    }

    /**
     * “机构采暖消费”页面--载入历史数据
     */
    @RequestMapping(value="/mechanismHeatingCopy")
    public @ResponseBody Map<String,Object> mechanismHeatingCopy(HttpServletRequest request) {
        Long entId = getLongParam(request, "entId", -1);
        String date = getStrParam(request, "date", "");
        return this.yunNanService.mechanismHeatingCopy(entId, date);
    }

    /**
     * “机构采暖消费”页面--手工上传
     */
    @RequestMapping("/reportMechanismHeating")
    public @ResponseBody Map<String,Object> reportMechanismHeating(HttpServletRequest request){
        Map<String, Object> resultMap = null;

        Map<String, Object> contInfo = null;
        try {
            contInfo = jacksonUtils.readJSON2Map(request.getParameter("contInfo"));
            resultMap = this.yunNanService.reportMechanismHeating(contInfo);
        }
        catch (IOException e) {
            Log.error(this.getClass() + ".reportMechanismHeating()");
        }

        return resultMap;
    }


    /**
     * 进入“采集数据重传”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/dataReUpload")
    public ModelAndView dataReUpload(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defaultDate", DateUtil.getCurrentDateStr(DateUtil.DEFAULT_SHORT_PATTERN));
        params.put("frequces",CollConfigStaticData.getStaticFrequce());
        return new ModelAndView("energy/yunNan/dataReUpload", params);
    }
    
    /**
	 * 重新上传数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/reUploadData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> reUploadData(HttpServletRequest request){
		 Map<String,Object> resultMap = new HashMap<String, Object>();

	      try {
	            Map<String, Object> contInfo = jacksonUtils.readJSON2Map(request.getParameter("cond"));
	            resultMap = this.yunNanService.reUploadData(contInfo);
	      }
	      catch (Exception e){
	    	    e.printStackTrace();
	            resultMap.put("isSuccess", false);
	            resultMap.put("message", "上报失败");
	      }

	      return resultMap;
	}


    /**
     * 进入“数据填报上传”页面   ---------------------------------------------------
     */
    @RequestMapping(value = "/dataFillUpload")
    public ModelAndView dataFillUpload(HttpServletRequest request){
        Map<String, Object> params = new HashMap<String, Object>();
        return new ModelAndView("energy/yunNan/dataFillUpload", params);
    }

    /**
     * “数据填报上传”页面--初始化页面数据
     */
    @RequestMapping(value="/getCompanyCollectionConfig")
    public @ResponseBody Map<String,Object> getCompanyCollectionConfig(HttpServletRequest request) {
        Long ledgerId = getLongParam(request, "ledgerId", -1);
        return this.yunNanService.getCompanyCollectionConfig(ledgerId);
    }

    @RequestMapping(value="/getOneCompanyCollectionConfig")
    public @ResponseBody Map<String,Object> getOneCompanyCollectionConfig(HttpServletRequest request) {
        Long entId = getLongParam(request, "entId", -1);
        Long dataId = getLongParam(request, "dataId", -1);
        return this.yunNanService.getOneCompanyCollectionConfig(entId, dataId);
    }

    /**
     *  数据填报上传
     */
    @RequestMapping(value = "/reportDataFillUpload")
    public @ResponseBody Map<String,Object> reportDataFillUpload(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        try {
            Map<String, Object> contInfo = jacksonUtils.readJSON2Map(request.getParameter("contInfo"));
            resultMap = this.yunNanService.reportDataFillUpload(contInfo);
        }
        catch (Exception e){
            resultMap.put("isSuccess", false);
            resultMap.put("message", "上报失败");
        }

        return resultMap;
    }

}
