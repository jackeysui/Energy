package com.linyang.energy.controller.recordmanager;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.DateUtil;
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
//import com.linyang.energy.job.CreateCalculatedPointJob;
import com.linyang.energy.model.MeterBean;
import com.linyang.energy.model.OptLogBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.MeterManagerService;
import com.linyang.energy.staticdata.DictionaryDataFactory;
import com.linyang.energy.utils.CommonOperaDefine;
import com.linyang.energy.utils.SequenceUtils;
import com.linyang.util.CommonMethod;

/**
 * 
 * @类功能说明：采集点管理Controller
 * @公司名称：江苏林洋电子有限公司
 * @作者：zhanmingming
 * @创建时间：2013-12-12 下午01:46:54  
 * @版本：V1.0
 */
@Controller
@RequestMapping("/meterManager")
public class MeterManagerController extends BaseController{
	/**
	 * 注入采集点service
	 */
	@Autowired
	private MeterManagerService meterManagerService;
    @Autowired
    private LedgerManagerService ledgerManagerService;
	@Autowired
    private UserAnalysisService userAnalysisService;

	
	/**
	 * 进入采集点管理页面
	 * @return
	 */
	@RequestMapping(value = "/gotoMeterManagerMain")
	public ModelAndView gotoMeterManagerMain(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		long[] volumes = { 5, 10, 20, 30, 50, 63, 80, 100, 125, 160, 200, 250, 315, 400, 500, 630, 800, 1000,
				1250, 1600, 2000, 2500, 3150, 4000, 5000, 6300, 8000, 10000, 12500, 16000, 20000, 25000, 31500,
				40000, 50000, 63000, 90000, 120000, 150000, 180000, 260000, 360000, 400000};
		map.put("volumes", volumes);// 变压器容量
 		return new ModelAndView(URLConstant.URL_METER_LIST,map);
	}

    /**
     * 进入“虚拟采集点管理”页面
     * @return
     */
    @RequestMapping(value = "/gotoVirtualMeter")
    public ModelAndView gotoVirtualMeter(HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();

        return new ModelAndView(URLConstant.URL_VIRTUAL_METER_LIST,map);
    }
	
	/**
	 * 
	 * 函数功能说明  :根据采集点的ID得到采集点的信息
	 * @param request
	 * @param meterId
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getMeterData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMeterData(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object> ();
		String meterId = request.getParameter("meterId");
		if(CommonMethod.isNotEmpty(meterId)){
			map.put("meter", meterManagerService.getMeterDataById(Long.valueOf(meterId)));
		}
		return map;
	}
	
	/**
	 * 查询采集点信息列表
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryMeterList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> queryMeterList(HttpServletRequest request) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> mapQuery = new HashMap<String, Object>();
		UserBean userBean = this.getSessionUserInfo(request);
        mapQuery.put("ledgerId", userBean.getLedgerId());
		mapQuery.put("accountId", userBean.getAccountId());
		Page page = null;
		// 得到当前分页
		Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
		if(CommonMethod.isNotEmpty(queryMap.get("pageIndex")) && CommonMethod.isNotEmpty(queryMap.get("pageSize")))
			page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
		else
			page = new Page();
		if(queryMap.containsKey("queryMap"))
			mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
		// 分页查询采集点信息
		List<Map<String,Object>> dataList = meterManagerService.getMeterPageList(page, mapQuery);
		Map<String, String> map = DictionaryDataFactory.getDictionaryData(request).get("meter_type");
		for (Map<String,Object> meterBean : dataList) {
			meterBean.put("METERTYPENAME", map.get(meterBean.get("METER_TYPE").toString()));
		}
		result.put("page", page);
		result.put("list",dataList);
		result.put("queryMap",mapQuery);
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_145, 8l, 1);
		return result;
	}

    /**
     * 查询虚拟采集点列表
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/queryVirtualMeterList", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> queryVirtualMeterList(HttpServletRequest request) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> mapQuery = new HashMap<String, Object>();
        UserBean userBean = this.getSessionUserInfo(request);
        if(userBean.getAccountId() == 1){
            mapQuery.put("ledgerId", -100L);
        } else {
            mapQuery.put("ledgerId", userBean.getLedgerId());
        }
        mapQuery.put("accountId", userBean.getAccountId());
        Page page = null;
        // 得到当前分页
        Map<String, Object> queryMap = jacksonUtils.readJSON2Map(request.getParameter("pageInfo"));
        if(CommonMethod.isNotEmpty(queryMap.get("pageIndex")) && CommonMethod.isNotEmpty(queryMap.get("pageSize")))
            page = new Page(Integer.valueOf(queryMap.get("pageIndex").toString()),Integer.valueOf(queryMap.get("pageSize").toString()));
        else
            page = new Page();
        if(queryMap.containsKey("queryMap"))
            mapQuery.putAll((Map<String,Object>)queryMap.get("queryMap"));
        // 分页查询采集点信息
        List<Map<String,Object>> dataList = meterManagerService.getVirtualMeterPageList(page, mapQuery);
        Map<String, String> map = DictionaryDataFactory.getDictionaryData(request).get("meter_type");
        for (Map<String,Object> meterBean : dataList) {
            meterBean.put("METERTYPENAME", map.get(meterBean.get("METER_TYPE").toString()));
            meterBean.put("CONTAINS", this.meterManagerService.getVirtualContains(Long.valueOf(meterBean.get("METER_ID").toString()), 0));
        }
        result.put("page", page);
        result.put("list",dataList);
        result.put("queryMap",mapQuery);
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_147, 142l, 1);
        return result;
    }

	/**
	 * 
	 * 函数功能说明  : 删除采集点信息
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/deleteMeterData", method = RequestMethod.POST)
	public @ResponseBody boolean deleteMeterData(HttpServletRequest request,Long[] meterIds) {
		boolean flag=false;
		StringBuffer desc = new StringBuffer();
		desc.append("delete a meter:" + this.meterManagerService.getMeterDataById(meterIds[0]).getMeterName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		int result = CommonOperaDefine.OPRATOR_FAIL;
		flag=meterManagerService.deleteMeterData(Arrays.asList(meterIds));				//调用
		if(flag) {
			result = CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_METER_ID,CommonOperaDefine.MODULE_NAME_METER,CommonOperaDefine.OPRATOR_OBJECT_TYPE_POINT,result,desc.toString()),request);
		return flag;
	}

    @RequestMapping(value = "/deleteVirtualMeterData", method = RequestMethod.POST)
    public @ResponseBody boolean deleteVirtualMeterData(HttpServletRequest request,Long[] meterIds) {
        boolean flag=false;
        StringBuffer desc = new StringBuffer();
        desc.append("delete a meter:" + this.meterManagerService.getMeterDataById(meterIds[0]).getMeterName())
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

        int result = CommonOperaDefine.OPRATOR_FAIL;
        flag=meterManagerService.deleteVirtualMeterData(Arrays.asList(meterIds));;
        if(flag) {
            result = CommonOperaDefine.OPRATOR_SUCCESS;
        }
        super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_DELETE,CommonOperaDefine.MODULE_NAME_METER_ID,CommonOperaDefine.MODULE_NAME_METER,CommonOperaDefine.OPRATOR_OBJECT_TYPE_POINT,result,desc.toString()),request);
        return flag;
    }

	/**
	 * 
	 * 函数功能说明  :采集点配置页面中能源类型，累加属性的初始化数据
	 * @param request
	 * @return      
	 * @return  Map<String,String>     
	 * @throws
	 */
	@RequestMapping(value = "/setSelectData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> setSelectData(HttpServletRequest request) {
		//从数据字典类中获取所需要的信息
		Map<String, Map<String, String>> map = DictionaryDataFactory.getDictionaryData(request);
		Map<String, Object> quaryMap = new HashMap<String,Object> (); 
		quaryMap.put("meterType", map.get("meter_type"));
		quaryMap.put("attrTypeElc", map.get("attr_type_elc"));
		quaryMap.put("attrTypeWater", map.get("attr_type_water"));
		return quaryMap;
	}
	
	/**
	 * 
	 * 函数功能说明  :得到所有的终端信息
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getTerminalData", method = RequestMethod.POST)
	public @ResponseBody String getTerminalData(HttpServletRequest request) {
		String str = "";
		StringBuffer sb = new StringBuffer("[");
		List<Map<String,Object>> dataList = meterManagerService.getTerminalData();
		//按照插件所需要的格式拼接字符串
		if(dataList.size()>0) {
			for(Map<String,Object> data : dataList) {
				sb.append("{id:'").append(data.get("TERMINAL_ID"))
					.append("',label:'").append(data.get("TERMINAL_NAME"))
					.append("',value:'").append(data.get("TERMINAL_ID"))
					.append("',num:'").append(data.get("TERMINAL_ID"))
					.append("',count:'0'},");
			}	
			str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
		} else
			str = "[]";
		return str;
	}

    /**
     *
     * 函数功能说明  :得到和达水表信息
     * @param request
     * @return
     * @return  Map<String,Object>
     * @throws
     */
    @RequestMapping(value = "/getHdWaterMeterData", method = RequestMethod.POST)
    public @ResponseBody String getHdWaterMeterData(HttpServletRequest request) {
        String str = "";
        StringBuffer sb = new StringBuffer("[");
        List<Map<String,Object>> dataList = meterManagerService.getHdWaterMeterData();
        //按照插件所需要的格式拼接字符串
        if(dataList.size()>0) {
            for(Map<String,Object> data : dataList) {
                sb.append("{id:'").append(data.get("ID"))
                        .append("',label:'").append(data.get("NAME"))
                        .append("',value:'").append(data.get("NAME"))
                        .append("',num:'").append(data.get("ID"))
                        .append("',count:'0'},");
            }
            str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
        } else
            str = "[]";
        return str;
    }
	
	/**
	 * 
	 * 函数功能说明  :检查当前分户下是否有该名称的采集点
	 * @param request
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping(value = "/checkMeterName", method = RequestMethod.POST)
	public @ResponseBody boolean checkMeterName(HttpServletRequest request) {
		boolean flag = true;
		Map<String, Object> map = new HashMap<String, Object> ();
		String meterName = request.getParameter("meterName");//采集点名称
		String changeName = request.getParameter("changeName");
		Object ledgerId = request.getParameter("ledgerId");//分户ID
		//获取session中的user信息
		UserBean user = this.getSessionUserInfo(request);
		//判断页面上传过来的是否是空
		if(null!=ledgerId&&CommonMethod.isNotEmpty(ledgerId)) {
			map.put("ledgerId", Long.parseLong(ledgerId.toString()));
		} else {
			map.put("ledgerId", user.getLedgerId());
		}
		map.put("meterName", meterName==null?"":meterName);
		if(meterManagerService.checkMeterName(map)>0) {
			if(meterName != null && meterName.equals(changeName)) {
				flag = true;
			} else {
				flag = false;
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * 函数功能说明  : 查询终端列表信息
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getTerminalDataList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getTerminalDataList(HttpServletRequest request) {
		List<Map<String,Object>> dataList = meterManagerService.getTerminalData();
		Map<String, Object> data = new HashMap<String, Object> ();
		data.put("list", dataList);
		return data;
	}
	
	/**
	 * 
	 * 函数功能说明  :查询某终端下的所有测量点
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getMpedDataByTerId", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMpedDataByTerId(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object> ();
		map.put("terminalId", Long.valueOf(request.getParameter("terminalId")));
		map.put("powerType", Integer.valueOf(request.getParameter("powerType")));
		List<Map<String,Object>> ledgerData = meterManagerService.getMpedDataByTerId(map);
		Map<String, Object> data = new HashMap<String, Object> ();
		data.put("list", ledgerData);
		return data;
	}
	
	
	
	/**
	 * 
	 * 函数功能说明  :查询设备集信息
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getDeviceData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getDeviceData(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object> ();
		List<Map<String,Object>> deviceData = meterManagerService.getDeviceData();
		map.put("list", deviceData);
		return map;
	}

    /**
     *  全局刷新
     * */
	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public @ResponseBody boolean refresh(HttpServletRequest request) {
        this.ledgerManagerService.updateLedgerMeter();
		return true;
	}

    /**
     *  保存配置的时候,局部刷新
     * */
    @RequestMapping(value = "/doRefresh", method = RequestMethod.POST)
    public @ResponseBody boolean doRefresh(HttpServletRequest request) {
        Long ledgerId = getLongParam(request, "ledgerId", -1L);
        if(ledgerId > 0){
            ledgerManagerService.updateOneLedger(ledgerId);
        }
        return true;
    }

    /**
     * 旧计算模型--新计算模型，初始化t_ledger_relation表数据的页面
     * */
    @RequestMapping("/copyOldModelPage")
    public ModelAndView copyOldModelPage(HttpServletRequest request) {
        return new ModelAndView("energy/recordmanager/copyOldModelPage");
    }

    @RequestMapping(value = "/copyOldModel", method = RequestMethod.POST)
    public @ResponseBody String copyOldModel(HttpServletRequest request) {
        String message = "初始化t_ledger_relation成功！";
        try {
            this.ledgerManagerService.copyOldModel();
        }
        catch (RuntimeException e) {
            message = "初始化t_ledger_relation失败！";
        }
        return message;
    }
	
	/**
	 * 
	 * 函数功能说明  :新增采集点
	 * @param request
	 * @param meter
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/addMeterInfo")
	public @ResponseBody boolean addMeterInfo (HttpServletRequest request,MeterBean meter){
		//默认的采集点序列
		meter.setMeterId(SequenceUtils.getDBSequence());
		meter.setMeterStatus(1);
		boolean isSuccess = false;
		StringBuffer desc = new StringBuffer();
		desc.append("add a meter:" + meter.getMeterName())
        .append(" by ").
        append(super.getSessionUserInfo(request).getLoginName()).
        append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));

		isSuccess = meterManagerService.insertMeterInfo(meter);
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_INSERT,CommonOperaDefine.MODULE_NAME_METER_ID,CommonOperaDefine.MODULE_NAME_METER,CommonOperaDefine.OPRATOR_OBJECT_TYPE_POINT,result,desc.toString()),request);
		return isSuccess;		
	}

    /**
     *
     * 函数功能说明  :新增虚拟采集点
     */
    @RequestMapping("/addVirtualMeter")
    public @ResponseBody Map<String, Object> addVirtualMeter (HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        boolean isSuccess = true;
        String message = "保存成功";
        
            Long meterId = super.getLongParam(request, "meterId", -1);
            String meterName = super.getStrParam(request, "meterName", "");
            Long ledgerId = super.getLongParam(request, "ledgerId", -1);
            Integer meterType = super.getIntParams(request, "meterType", -1);
            String relations = super.getStrParam(request, "relations", "");
            Map<String, Object> map = this.meterManagerService.insertUpdateVirtualMeter(meterId, meterName, ledgerId, meterType, relations);
            String resultMess = map.get("message").toString();
            if(resultMess.length()>0){  //保存失败
                isSuccess = false;
                message = resultMess;
            }
            else{
	            
	            isSuccess = true;
	            message = "保存成功";
            }

        resultMap.put("isSuccess", isSuccess);
        resultMap.put("message", message);
        return resultMap;
    }

    /**
     * 得到虚拟采集点信息
     */
    @RequestMapping("/getVirtualMeterData")
    public @ResponseBody Map<String,Object> getVirtualMeterData(HttpServletRequest request){
        Map<String,Object> resultMap = new HashMap<String, Object>();

        Long meterId = super.getLongParam(request, "meterId", -1);
        if(meterId > 0){
//            try {
                resultMap.putAll(this.meterManagerService.getVirtualMeterData(meterId));
//            } catch (Exception e) {
//                Log.error(this.getClass()+".getVirtualMeterData()", e);
//            }

        }
        return resultMap;
    }

	
	/**
	 * 
	 * 函数功能说明  :修改采集点
	 * @param request
	 * @param meter
	 * @return      
	 * @return  boolean     
	 * @throws
	 */
	@RequestMapping("/updateMeterInfo")
	public @ResponseBody boolean updateMeterInfo (HttpServletRequest request,MeterBean meter){
		boolean isSuccess = false;
		isSuccess = meterManagerService.updateMeterInfo(meter);

        //记录日志
        StringBuffer desc = new StringBuffer();
        desc.append("update a meter:" + meter.getMeterName())
                .append(" by ").
                append(super.getSessionUserInfo(request).getLoginName()).
                append("  ").append(DateUtil.convertDateToStr(new Date(), DateUtil.MOUDLE_PATTERN));
		int result = CommonOperaDefine.OPRATOR_FAIL;
		if(isSuccess){
			result =  CommonOperaDefine.OPRATOR_SUCCESS;
		}
		super.writeLog(new OptLogBean(CommonOperaDefine.LOG_TYPE_UPDATE,CommonOperaDefine.MODULE_NAME_METER_ID,CommonOperaDefine.MODULE_NAME_METER,CommonOperaDefine.OPRATOR_OBJECT_TYPE_POINT,result,desc.toString()),request);

		return isSuccess;		
	}
	
	/**
	 * 
	 * 函数功能说明  ：根据条件得到计量点列表
	 * @author guosen
	 * @data 2014-8-13
	 * @param request
	 * @return      
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getAjaxMeterList")
	public @ResponseBody Map<String, Object> getAjaxMeterList(HttpServletRequest request) throws IOException{
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
		
		List<MeterBean> meters = this.meterManagerService.queryMeterList(param);
		result.put("list", meters);
		return result;
	}
	
	/**
	 *@函数功能说明 : 根据id获取测量点信息
	 *
	 *@author chengq
	 *@date 2014-8-13
	 *@param
	 *@return
	 */
	@RequestMapping("/getMpedInfoById")
	public @ResponseBody Map<String,Object> getMpedInfoById(HttpServletRequest request,Long mpedId){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		if(mpedId != null){
			
				resultMap.put("mpedInfo", meterManagerService.getMpedInfoById(mpedId));
		}
		return resultMap;
	}
	
	
//	/**
//	 * 
//	 * 函数功能说明  :得到该终端下的测量点
//	 * @param request
//	 * @return      
//	 * @return  Map<String,Object>     
//	 * @throws
//	 */
//	@RequestMapping(value = "/getMeterList", method = RequestMethod.POST)
//	public @ResponseBody String getMeterList(HttpServletRequest request) {
//		String ledgerId = request.getParameter("ledgerId");
//		String meterId = request.getParameter("meterId");
//		
//		String str = "";
//		StringBuffer sb = new StringBuffer("[");
//		sb.append("{id:-1,label:'不参加线损计算',value:'不参加线损计算',num:0,count:0},");
//		sb.append("{id:0,label:'进线总表',value:'进线总表',num:0,count:0},");
//		List<Map<String,Object>> dataList = meterManagerService.getLineLossMeterList(Long.parseLong(ledgerId));
//		//按照插件所需要的格式拼接字符串
//		if(dataList.size()>0) {
//			for(Map<String, Object> meter : dataList) {
//				if(!meterId.equals(meter.get("METERID").toString())){
//					sb.append("{id:'").append(meter.get("METERID"))
//					.append("',label:'").append(meter.get("METERNAME"))
//					.append("',value:'").append(meter.get("METERNAME")).append("',num:'")
//					.append(meter.get("METERID")).append("',count:'0'},");
//				}
//			}	
//		} 
//		str = sb.deleteCharAt(sb.lastIndexOf(",")).append("]").toString();
//		return str;
//	}
	
	/**
	 * 
	 * 函数功能说明  :得到该终端下的测量点
	 * @param request
	 * @return      
	 * @return  Map<String,Object>     
	 * @throws
	 */
	@RequestMapping(value = "/getMeterList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getMeterList(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String ledgerId = request.getParameter("ledgerId");
		String meterIdstr = request.getParameter("meterId");
		Long meterId = null;
		if(meterIdstr != null){
			meterId = Long.parseLong(meterIdstr);
		}
		List<Map<String,Object>> dataList = meterManagerService.getLineLossMeterList(Long.parseLong(ledgerId),meterId);
		map.put("list", dataList);
		return map;
	}
	
	 /**
	 * 获取采集点id得到父级采集点
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMeterParentByMeterId")
	public @ResponseBody Long getMeterParentByMeterId(HttpServletRequest request){
		long meterId = getLongParam(request, "meterId", 0);
		Long meterParentId = meterManagerService.getMeterParentByMeterId(meterId);
		if (meterParentId != null) {
			return meterParentId;
		}
		return 0l;
	}
	
	/**
	 * 从管理者切换到工程师，获得左侧结构树焦点meterId
	 * @param request
	 * @return
	 */
	@RequestMapping("/getMeterIdByLegerId")
	public @ResponseBody Map<String, Object> getMeterIdByLegerId(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		long legerId = getLongParam(request, "legerId", 0);
		int selectType = getIntParams(request, "selectType", 0);
		map = meterManagerService.getMeterInfoByLedgerId(legerId,selectType);
		return map;
	}
	
	/**
	 * 工程师页面加载第一个测量点
	 * @param request
	 * @return
	 */
	@RequestMapping("/elePageLoadFirstMeterByLegerId")
	public  @ResponseBody Map<String, Object> elePageLoadFirstMeterByLegerId(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		long ledgerId = getLongParam(request, "ledgerId", 0);
		map = meterManagerService.elePageLoadFirstMeter(ledgerId);
		return map;
	}
}
