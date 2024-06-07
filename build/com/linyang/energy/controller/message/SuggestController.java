package com.linyang.energy.controller.message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linyang.energy.service.UserAnalysisService;
import com.linyang.energy.utils.OperItemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esotericsoftware.minlog.Log;
import com.linyang.common.web.page.Page;
import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.LedgerBean;
import com.linyang.energy.model.RecordBean;
import com.linyang.energy.model.ReplyBean;
import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.SuggestService;
import com.linyang.energy.service.UserService;
import com.linyang.energy.utils.DateUtil;

import net.sf.json.JSONObject;

/**
 * 不支持删除和修改功能
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/suggest")
public class SuggestController extends BaseController {

	@Autowired
	private SuggestService suggestService;

	@Autowired
	private UserService userService;

	@Autowired
	private LedgerManagerService ledgerService;
	
	@Autowired
	private UserAnalysisService userAnalysisService;



	/**
	 * 获取用户反馈列表 105-平台运营商,按照平台运营商查询
	 */
	@RequestMapping(value = "/getSuggestList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> querySuggertList(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(getStrParam(request, "paramInfo", ""));
		long userId = Long.parseLong(json.getString("userId"));
		UserBean userBean = userService.getUserByAccountId(userId);
		LedgerBean ledgerBean = ledgerService.getLedgerDataById(userBean.getLedgerId());
		// 分页信息
		Page page = super.getCurrentPage(json.getString("pageNo"), json.getString("pageSize"));
		List<RecordBean> result = null;
		param.put("page", page);
		param.put("ledgerId", ledgerBean.getLedgerId());
		result = suggestService.getSuggestPageList(param);
		resultMap.put("page", page);
		if (result != null && result.size() > 0)
			resultMap.put("dataInfo", result);
		//记录用户足迹
		this.userAnalysisService.addAccountTrace(super.getSessionUserInfo(request).getAccountId(), OperItemConstant.OPER_ITEM_143, 119l, 1);
		return resultMap;
	}

	/**
	 * 获取用户未处理信息数量 管理员和用户分别获取不同信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getSuggestNums", method = RequestMethod.POST)
	public @ResponseBody Map<String, Long> querySuggestNums(HttpServletRequest request) {
		Map<String, Long> resultMap = new HashMap<String, Long>();
		long accountId = getLongParam(request, "userId", 0l);
		UserBean userBean = userService.getUserByAccountId(accountId);
        Long ledgerId = 0L;
        if (userBean.getLedgerId() == 0L) {
            // 权限为群组分配ledgerId
            ledgerId = ledgerService.getLedgerIfNull(accountId);
        }
        else {
            ledgerId = userBean.getLedgerId();
        }

		LedgerBean ledgerBean = ledgerService.getLedgerDataById(ledgerId);
		if (ledgerBean.getAnalyType() == 105) {
			Long NumsForAdmin = suggestService.getSuggestNumsForAdmin(accountId);
			resultMap.put("uncompleted", NumsForAdmin);
		}
        else {
			Long NumsForUsers = suggestService.getSuggestNumsForUsers(accountId);
			resultMap.put("uncompleted", NumsForUsers);
		}
		return resultMap;
	}

	/**
	 * 获取对话记录(聊天窗口) 
	 * 小程序只有accountId以及pageNo
	 */
	@RequestMapping(value = "/queryChatRecord")
	public @ResponseBody Map<String, Object> queryChatRecord(HttpServletRequest request) {
		String openId = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ReplyBean> result = new ArrayList<ReplyBean>();
		JSONObject json = JSONObject.fromObject(getStrParam(request, "paramInfo", ""));
		long accountId = Long.parseLong(json.getString("accountId"));
		UserBean userByAccountId = userService.getUserByAccountId(accountId);
		int pageNo = Integer.parseInt(json.getString("pageNo"));
		Long sugId = suggestService.toViewUserSugIdIsExist(accountId);
		result = suggestService.getChatRecord(accountId,openId,sugId, pageNo);
		resultMap.put("dataInfo", "暂无数据");
		if (result != null && result.size() > 0)
			resultMap.put("dataInfo", result);
		if (userByAccountId != null && userByAccountId.toString().length() > 0)
			resultMap.put("user", userByAccountId);
		// 修改record和reply表的status
		return resultMap;
	}

	/**
	 *
	 */
	@RequestMapping(value = "/queryChatRecordForUser")
	public @ResponseBody Map<String, Object> queryChatRecordForUser(HttpServletRequest request,
			HttpServletResponse response) {
		String openId = "";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<ReplyBean> result = new ArrayList<ReplyBean>();
		boolean errorMsg = true;
		JSONObject json = JSONObject.fromObject(getStrParam(request, "paramInfo", ""));
		Integer pageNo = Integer.parseInt(json.getString("pageNo"));
		long userId = Long.parseLong(json.getString("userId"));
		Long sugId = suggestService.toViewUserSugIdIsExist(userId);

		if (sugId != null && sugId != 0l) {
			result = suggestService.getChatRecord(userId,openId,sugId, pageNo);
			// 给推送状态清0
			Date today = new Date();

			Map<String, Object> lastDate = suggestService.lastDate(userId);
			
			Object submitDate = null;
			if(lastDate != null)
				submitDate = lastDate.get("SUBMIT_DATE");

			if (null != submitDate
					&& !DateUtil.clearDate(DateUtil.convertStrToDate(submitDate.toString(), DateUtil.DEFAULT_PATTERN))
							.equals(DateUtil.clearDate(today))) {
				suggestService.updateIsPush(0, userId);
			}

			Map<String, Object> push = suggestService.isPush(userId);
			if (push != null && push.toString().length() > 0)
				resultMap.put("isPush", push.get("IS_PUSH"));
			else
				resultMap.put("isPush", 0);

			if (result != null && result.size() > 0)
				resultMap.put("dataInfo", result);
		} else {
			errorMsg = false;
			resultMap.put("dataInfo", errorMsg);
		}
		suggestService.updateIsPush(1, userId);
		return resultMap;
	}

	/**
	 * WEB端插入
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/interpositionRecordForWeb", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> interpositionRecordForWeb(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(getStrParam(request, "paramInfo", ""));
		long accountId = Long.parseLong(json.getString("accountId"));
		String MSG = json.getString("MSG");
		long sugId = Long.parseLong(json.getString("sugId"));
		String contactWay = json.getString("phoneNum");
		Object interRecord = null;
		interRecord = suggestService.interpositionRecordForWeb(accountId, MSG, sugId, contactWay);
		if (interRecord != null) {
			resultMap.put("result", interRecord);
			resultMap.put("status", true);
			suggestService.updateStatus(sugId, accountId);
		} else {
			resultMap.put("errorMessage", "插入失败");
			resultMap.put("status", false);
		}
		return resultMap;
	}

	/**
	 * 此方法暂不使用,不要删除,可能还会使用
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getExcel")
	public @ResponseBody void getSugExcel(HttpServletRequest request, HttpServletResponse response) {
		String filename = "用户反馈";
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1") + ".xls");
		} catch (UnsupportedEncodingException e) {
			Log.info("getSugExcel error UnsupportedEncodingException");
		} // 指定下载的文件名
		response.setContentType("application/vnd.ms-excel");
		// 得到数据
		List<ReplyBean> excelList = suggestService.getExcelList();
		// 得到页面请求信息pageInfo，作为参数传进getExcel方法
		try {
			suggestService.getExcel("Cache.xls", response.getOutputStream(), excelList);
		} catch (IOException e) {
			Log.info("getSugExcel error IOException");
		}

	}

	@RequestMapping(value = "/updatePhoneNum")
	public @ResponseBody Map<String, Object> updatePhoneNum(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(getStrParam(request, "paramInfo", ""));
		long accountId = Long.parseLong(json.getString("accountId"));
		String phoneNum = json.getString("phoneNum");
		Integer updatePhoneNum = userService.updatePhoneNum(accountId, phoneNum);
		if (updatePhoneNum != null && updatePhoneNum != 0) {
			resultMap.put("result", updatePhoneNum);
			resultMap.put("status", true);
		} else {
			resultMap.put("errorMessage", "修改失败");
			resultMap.put("status", false);
		}
		return resultMap;
	}

	/**
	 * 用户用来插入反馈表数据用,参数不同
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/interpositionRecordForUser", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> interpositionRecordForUser(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		JSONObject json = JSONObject.fromObject(getStrParam(request, "paramInfo", ""));
		long accountId = Long.parseLong(json.getString("accountId"));
		String MSG = json.getString("MSG");
		Long sugId = 0l;
		if (suggestService.toViewUserSugIdIsExist(accountId) != null
				&& suggestService.toViewUserSugIdIsExist(accountId) != 0l)
			sugId = suggestService.toViewUserSugIdIsExist(accountId);
		String contactWay = json.getString("phoneNum");

		Object interpositionRecord = suggestService.interpositionRecordForWeb(accountId, MSG, sugId, contactWay);

		if (interpositionRecord != null) {
			resultMap.put("result", interpositionRecord);
			resultMap.put("status", true);
			suggestService.updateStatus(sugId, accountId);
		} else {
			resultMap.put("errorMessage", "插入失败");
			resultMap.put("status", false);
		}
		return resultMap;
	}

	@RequestMapping(value = "/isOperator", method = RequestMethod.POST)
	public @ResponseBody boolean isOperator(HttpServletRequest request) {
		long accountId = getLongParam(request, "userId", 0l);
		UserBean userBean = userService.getUserByAccountId(accountId);
        Long ledgerId = 0L;
        if (userBean.getLedgerId() == 0L) {
            // 权限为群组分配ledgerId
            ledgerId = ledgerService.getLedgerIfNull(accountId);
        }
        else {
            ledgerId = userBean.getLedgerId();
        }
		LedgerBean ledgerBean = ledgerService.getLedgerDataById(ledgerId);
		if (ledgerBean.getAnalyType() == 105) {
			return true;
		}
		return false;
	}

}
