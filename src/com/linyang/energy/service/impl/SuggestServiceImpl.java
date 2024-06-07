package com.linyang.energy.service.impl;

import com.linyang.common.web.common.Log;
import com.linyang.energy.mapping.message.SuggestMapper;
import com.linyang.energy.model.*;
import com.linyang.energy.service.LedgerManagerService;
import com.linyang.energy.service.PhoneService;
import com.linyang.energy.service.SuggestService;
import com.linyang.energy.service.UserService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class SuggestServiceImpl implements SuggestService {

	@Autowired
	private SuggestMapper suggestMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private LedgerManagerService ledgerManagerService;

	@Autowired
	private PhoneService phoneService;

    @Autowired
	private LedgerManagerService ledgerService;

	@Override
	public List<RecordBean> getSuggestPageList(Map<String, Object> param) {
		List<RecordBean> recodeBean = suggestMapper.getSuggestPageList(param);
		return recodeBean;
	}

	@Override
	public Long getSuggestNumsForAdmin(Long accountId) {
		Long NumsForAdmin = 0l;
		Long suggestNums = 0l;

		UserBean userBean = userService.getUserByAccountId(accountId);

		if (suggestMapper.getReplyNumsForAdmin(userBean.getLedgerId()) != null)
			suggestNums = suggestMapper.getReplyNumsForAdmin(userBean.getLedgerId());
		if (suggestMapper.getRocordNumsForAdmin(userBean.getLedgerId()) != null)
			NumsForAdmin = suggestMapper.getRocordNumsForAdmin(userBean.getLedgerId());

		Long sugNums = (suggestNums + NumsForAdmin);
		return sugNums;
	}

	@Override
	public Long getSuggestNumsForUsers(Long accountId) {
		Long suggestNumsForUsers = 0l;
		suggestNumsForUsers = suggestMapper.getSuggestNumsForUsers(accountId);
		return suggestNumsForUsers;
	}

	@Override
	public Long toViewUserSugIdIsExist(Long accountId) {
		Long sugId = 0l;
		if (suggestMapper.toViewUserSugIdIsExist(accountId) != null
				&& suggestMapper.toViewUserSugIdIsExist(accountId) != 0l)
			sugId = suggestMapper.toViewUserSugIdIsExist(accountId);
		return sugId;
	}

	@Override
	public List<ReplyBean> getChatRecord(Long accountId, String openId, Long sugId, Integer pageNo) {
		List<ReplyBean> chatRecord = suggestMapper.getChatRecordByAccountId(accountId, openId, sugId, pageNo);
		if (chatRecord != null & chatRecord.toString().length() != 0) {
			for (int i = 0; i < chatRecord.size(); i++) {
				Date submitDate = chatRecord.get(i).getSubmitDate();
				String submitDateStr = DateUtil.convertDateToStr(submitDate, DateUtil.DEFAULT_PATTERN);
				chatRecord.get(i).setSubmitDateStr(submitDateStr);
			}
			return chatRecord;
		}
		return null;
	}

	@Override
	public Object interpositionRecord(Long accountId, String MSG, String openId) {

		HashMap<String, Object> map = new HashMap<String, Object>();// 封装参数

		Object obj = null;

		if ((accountId != null && accountId != 0l) || (openId != null && !openId.equals("")))
			if (openId != null && !openId.equals("")) {// 根据openId查询用户信息
				Map<String, Object> appUserForOpenId = suggestMapper.getAppUserForOpenId(openId);

				LedgerBean ledgerDataById = null;
				if (appUserForOpenId != null && appUserForOpenId.get("LEDGERID") != null)
					ledgerDataById = ledgerManagerService.getLedgerDataById(Long.valueOf((appUserForOpenId.get("LEDGERID")).toString()));

					map.put("phoneNum", null);

					map.put("ledgerName", null);

					map.put("loginName", null);

					map.put("ledgerId", null);

					if (appUserForOpenId != null) {
						if (appUserForOpenId.get("PHONE") != null)
							map.put("phoneNum", appUserForOpenId.get("PHONE"));

						if (ledgerDataById != null && !ledgerDataById.getLedgerName().equals(""))
							map.put("ledgerName", ledgerDataById.getLedgerName());

						if (appUserForOpenId.get("LOGINNAME") != null)
							map.put("loginName", appUserForOpenId.get("LOGINNAME"));

						if (appUserForOpenId.get("LEDGERID") != null)
							map.put("ledgerId", appUserForOpenId.get("LEDGERID"));
					}

					Long sugId = suggestMapper.toViewSugIdIsExist(openId);

					map.put("accountId", 0);
					if (accountId != null)
						map.put("accountId", accountId);

					map.put("openId", openId);

					map.put("MSG", MSG);

					map.put("sugId", null);
					if (sugId != null && sugId != 0l)
						map.put("sugId", sugId);

					map.put("analyType", 102);

					obj = interObject(map);

				} else if (accountId != null && accountId != 0l) {

					Long sugId = suggestMapper.toViewUserSugIdIsExist(accountId);

					UserBean userByAccountId = userService.getUserByAccountId(accountId);

					LedgerBean ledgerDataById = ledgerManagerService.getLedgerDataById(userByAccountId.getLedgerId()); // 根据ledgerid获取分户信息

					map.put("openId", openId);

					map.put("accountId", userByAccountId.getAccountId());

					map.put("phoneNum", userByAccountId.getPhone());

					map.put("ledgerId", userByAccountId.getLedgerId());

					map.put("MSG", MSG);

					map.put("sugId", sugId);

					map.put("ledgerName", ledgerDataById.getLedgerName());

					map.put("analyType", ledgerDataById.getAnalyType());

					map.put("loginName", userByAccountId.getLoginName());

					obj = interObject(map);

				}

				return obj;
			}


		public Object interObject (Map < String, Object > map){

			ReplyBean replyBean = new ReplyBean();
			RecordBean recordBean = new RecordBean();

			if (map.get("sugId") != null && Long.parseLong(map.get("sugId").toString()) != 0) { // 小程序字段accountId,MSG
				replyBean.setAccountId(Long.parseLong(map.get("accountId").toString()));
				replyBean.setContactWay("0");
				if (map.get("phoneNum") != null)
					replyBean.setContactWay(map.get("phoneNum").toString());
				replyBean.setLedgerId(0l);
				if (map.get("ledgerId") != null)
					replyBean.setLedgerId(Long.parseLong(map.get("ledgerId").toString()));
				replyBean.setReplyMsg("0");
				if (map.get("MSG") != null)
					replyBean.setReplyMsg(map.get("MSG").toString());
				replyBean.setReplyId(SequenceUtils.getDBSequence());
				replyBean.setSubmitDate(new Date());
				replyBean.setSugId(0l);
				if (map.get("sugId") != null)
					replyBean.setSugId(Long.parseLong(map.get("sugId").toString()));
				replyBean.setSubmitLedger("0");
				if (map.get("ledgerName") != null)
					replyBean.setSubmitLedger(map.get("ledgerName").toString());
				replyBean.setSubmitUser("0");
				if (map.get("loginName") != null)
					replyBean.setSubmitUser(map.get("loginName").toString());
				replyBean.setSubmitDateStr(DateUtil.convertDateToStr(new Date(), DateUtil.DEFAULT_PATTERN));
				replyBean.setOpenId("0");
				if (map.get("openId") != null)
					replyBean.setOpenId(map.get("openId").toString());
				suggestMapper.interpositionReply(replyBean);
				replyBean.setTag(1);
				if (map.get("analyType") != null && Integer.parseInt(map.get("analyType").toString()) == 105)
					replyBean.setTag(0);
				return replyBean;
			} else if (map.get("analyType") != null && Integer.parseInt(map.get("analyType").toString()) != 105) {
				recordBean.setAccountId(Long.parseLong(map.get("accountId").toString()));
				recordBean.setSubmitDate(new Date());
				recordBean.setContactWay("0");
				if (map.get("phoneNum") != null)
					recordBean.setContactWay(map.get("phoneNum").toString());
				recordBean.setLedgerId(0l);
				if (map.get("ledgerId") != null)
					recordBean.setLedgerId(Long.parseLong(map.get("ledgerId").toString()));
				recordBean.setSubmitLedger("0");
				if (map.get("ledgerName") != null)
					recordBean.setSubmitLedger(map.get("ledgerName").toString());
				recordBean.setSubmitUser("0");
				if (map.get("loginName") != null)
					recordBean.setSubmitUser(map.get("loginName").toString());
				recordBean.setSugId(SequenceUtils.getDBSequence());
				recordBean.setSugMsg(map.get("MSG").toString());
				recordBean.setReplyMsg(map.get("MSG").toString());
				recordBean.setSubmitDateStr(DateUtil.convertDateToStr(new Date(), DateUtil.DEFAULT_PATTERN));
				recordBean.setTag(1);
				if (map.get("openId") == null)
					return null;
				recordBean.setOpenId(map.get("openId").toString());
				suggestMapper.interpositionRecord(recordBean);
				return recordBean;
			} else {
				return null;
			}
		}

		/**
		 * 小程序没有sugId
		 */
		@Override
		public Integer updateStatus (@Param("sugId") Long sugId, @Param("accountId") Long accountId){
			Integer result = 0;
			result += suggestMapper.updateRecordStatus(sugId, accountId);
			result += suggestMapper.updateReplyStatus(sugId, accountId);
			return result;
		}

		/**
		 * 得到用户反馈的Excel
		 *
		 * @param
		 * @throws Exception
		 */
		@Override
		public void getExcel (String sheetName, OutputStream output, List < ReplyBean > sugList){
			// excel表格内容填充
			int f = 2;
			Set<String> sheetNames = new HashSet<String>();
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = null;

			// 声明一个工作簿

			sheet = wb.createSheet();
			wb.setSheetName(0, sugList.get(0).getSubmitUser());
			sheetNames.add(sugList.get(0).getSubmitUser());
			// 设置默认宽度为25字节
			sheet.setDefaultColumnWidth(30);

			HSSFCellStyle titlestyle = wb.createCellStyle();
			titlestyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			titlestyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			titlestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			titlestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			titlestyle.setRightBorderColor(HSSFColor.BLACK.index);
			titlestyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			titlestyle.setLeftBorderColor(HSSFColor.BLACK.index);
			titlestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			titlestyle.setTopBorderColor(HSSFColor.BLACK.index);
			titlestyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			titlestyle.setBottomBorderColor(HSSFColor.BLACK.index);
			titlestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			HSSFFont font = wb.createFont();
			font.setColor(HSSFColor.WHITE.index);
			titlestyle.setFont(font);

			// 生成并设置另一个表格内容样式
			HSSFCellStyle styleAno = wb.createCellStyle();
			styleAno.setRightBorderColor(HSSFColor.BLACK.index);
			styleAno.setBorderRight(HSSFCellStyle.BORDER_THIN);
			styleAno.setLeftBorderColor(HSSFColor.BLACK.index);
			styleAno.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			styleAno.setTopBorderColor(HSSFColor.BLACK.index);
			styleAno.setBorderTop(HSSFCellStyle.BORDER_THIN);
			styleAno.setBottomBorderColor(HSSFColor.BLACK.index);
			styleAno.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			styleAno.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 生成另一个字体
			HSSFFont fontAno = wb.createFont();
			fontAno.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			styleAno.setFont(fontAno);
			// 生成表头，也就是第一行
			HSSFRow row = sheet.createRow(1);
			HSSFCell cellA = row.createCell(0);
			HSSFCell cellB = row.createCell(1);
			HSSFCell cellC = row.createCell(2);
			HSSFCell cellD = row.createCell(3);
			HSSFCell cellE = row.createCell(4);

			cellA.setCellStyle(titlestyle);
			cellB.setCellStyle(titlestyle);
			cellC.setCellStyle(titlestyle);
			cellD.setCellStyle(titlestyle);
			cellE.setCellStyle(titlestyle);

			cellA.setCellValue("用户");
			cellB.setCellValue("消息");
			cellC.setCellValue("时间");
			cellD.setCellValue("分户名称");
			cellE.setCellValue("联系方式");

			for (int i = 0; i < sugList.size(); i++) {
				String tempName = sugList.get(i).getSubmitUser();
				if (!sheetNames.contains(tempName)) {
					sheet = wb.createSheet();
					wb.setSheetName(sheetNames.size(), tempName);
					// 设置默认宽度为25字节
					sheet.setDefaultColumnWidth(30);
					sheetNames.add(tempName);
					f = 2;

					// 生成表头，也就是第一行
					row = sheet.createRow(1);
					cellA = row.createCell(0);
					cellB = row.createCell(1);
					cellC = row.createCell(2);
					cellD = row.createCell(3);
					cellE = row.createCell(4);

					cellA.setCellStyle(titlestyle);
					cellB.setCellStyle(titlestyle);
					cellC.setCellStyle(titlestyle);
					cellD.setCellStyle(titlestyle);
					cellE.setCellStyle(titlestyle);

					cellA.setCellValue("用户");
					cellB.setCellValue("消息");
					cellC.setCellValue("时间");
					cellD.setCellValue("分户名称");
					cellE.setCellValue("联系方式");
				}

				HSSFRow row1 = sheet.createRow(f);
				HSSFCell cell1A = row1.createCell(0);
				HSSFCell cell1B = row1.createCell(1);
				HSSFCell cell1C = row1.createCell(2);
				HSSFCell cell1D = row1.createCell(3);
				HSSFCell cell1E = row1.createCell(4);

				cell1A.setCellStyle(styleAno);
				cell1B.setCellStyle(styleAno);
				cell1C.setCellStyle(styleAno);
				cell1D.setCellStyle(styleAno);
				cell1E.setCellStyle(styleAno);
				cell1A.setCellValue(sugList.get(i).getSubmitUser());
				cell1B.setCellValue(sugList.get(i).getReplyMsg());
				cell1C.setCellValue(DateUtil.convertDateToStr(sugList.get(i).getSubmitDate(), DateUtil.DEFAULT_PATTERN));
				cell1D.setCellValue(sugList.get(i).getSubmitLedger());
				cell1E.setCellValue(sugList.get(i).getContactWay() == null ? "无" : sugList.get(i).getContactWay());
				f++;
			}
			try {
				output.flush();
				wb.write(output);
				output.close();
			} catch (IOException e) {
				Log.info("getSugExcel error IOException");
			}

		}

		@Override
		public List<ReplyBean> getExcelList () {
			return suggestMapper.getExcelList();
		}

		@Override
		public Object interpositionRecordForWeb (Long accountId, String MSG, Long sugId, String contactWay){
			Long viewUserSugIdIsExist = suggestMapper.toViewUserSugIdIsExist(accountId);

			UserBean userByAccountId = userService.getUserByAccountId(accountId); // 根据用户id获取用户信息
            Long ledgerId = 0L;
            if (userByAccountId.getLedgerId() == 0L) {
                // 权限为群组分配ledgerId
                ledgerId = ledgerService.getLedgerIfNull(accountId);
            }
            else {
                ledgerId = userByAccountId.getLedgerId();
            }

			LedgerBean ledgerDataById = ledgerManagerService.getLedgerDataById(ledgerId); // 根据ledgerid获取分户信息

			if ((viewUserSugIdIsExist != null && viewUserSugIdIsExist != 0) || (sugId != null && sugId != 0l)) { // 小程序字段accountId,MSG
				ReplyBean replyBean = new ReplyBean();
				replyBean.setAccountId(accountId);
				replyBean.setOpenId("0");
				replyBean.setContactWay("");
				if (userByAccountId.getPhone() != null && userByAccountId.getPhone().toString().length() > 0)
					replyBean.setContactWay(userByAccountId.getPhone());
				if (contactWay != null && !contactWay.equals(""))
					replyBean.setContactWay(contactWay);
				replyBean.setLedgerId(userByAccountId.getLedgerId());
				replyBean.setReplyMsg(MSG);
				replyBean.setReplyId(SequenceUtils.getDBSequence());
				replyBean.setSubmitDate(new Date());
				replyBean.setSugId(viewUserSugIdIsExist);
				if (sugId != null && sugId != 0l)
					replyBean.setSugId(sugId);
				replyBean.setSubmitLedger(ledgerDataById.getLedgerName());
				replyBean.setSubmitUser(userByAccountId.getLoginName());
				suggestMapper.interpositionReply(replyBean);
				replyBean.setSubmitDateStr(DateUtil.convertDateToStr(new Date(), DateUtil.DEFAULT_PATTERN));
				replyBean.setTag(1);
				if (ledgerDataById.getAnalyType() == 105)
					replyBean.setTag(0);
				return replyBean;
			}
            else if (ledgerDataById.getAnalyType() != 105) {
				RecordBean recordBean = new RecordBean();
				recordBean.setAccountId(accountId);
				recordBean.setSubmitDate(new Date());
				recordBean.setOpenId("0");
				recordBean.setContactWay("");
				if (userByAccountId.getPhone() != null && userByAccountId.getPhone().toString().length() > 0)
					recordBean.setContactWay(userByAccountId.getPhone());
				if (contactWay != null && contactWay.equals(""))
					recordBean.setContactWay(contactWay);
				recordBean.setLedgerId(userByAccountId.getLedgerId());
				recordBean.setSubmitLedger(ledgerDataById.getLedgerName());
				recordBean.setSubmitUser(userByAccountId.getLoginName());
				recordBean.setSugId(SequenceUtils.getDBSequence());
				recordBean.setSugMsg(MSG);
				suggestMapper.interpositionRecord(recordBean);
				recordBean.setSubmitDateStr(DateUtil.convertDateToStr(new Date(), DateUtil.DEFAULT_PATTERN));
				recordBean.setTag(1);
				if (ledgerDataById.getAnalyType() == 105)
					recordBean.setTag(0);
				return recordBean;
			}
			return null;
		}

		@Override
		public Map<String, Object> isPush (Long accountId){
			return suggestMapper.isPush(accountId);
		}

		@Override
		public Integer updateIsPush (Integer push, Long accountId){
			return suggestMapper.updateIsPush(push, accountId);
		}

		@Override
		public Map<String, Object> lastDate (Long accountId){
			return suggestMapper.lastDate(accountId);
		}

		@Override
		public Long toViewUserSugIdIsExist (String openId){
			return suggestMapper.toViewSugIdIsExist(openId);
		}

		@Override
		public Map<String, Object> getAppUserForOpenId (String openId){
			return suggestMapper.getAppUserForOpenId(openId);
		}

	}
