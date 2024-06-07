package com.linyang.energy.service.impl;

import com.leegern.util.CollectionUtil;
import com.leegern.util.NumberUtil;
import com.leegern.util.StringUtil;
import com.linyang.common.web.common.SpringContextHolder;
import com.linyang.energy.common.CommonResource;
import com.linyang.energy.mapping.enviManage.EnviManageMapper;
import com.linyang.energy.mapping.recordmanager.LedgerManagerMapper;
import com.linyang.energy.model.EnviManageBean;
import com.linyang.energy.model.IndustryBean;
import com.linyang.energy.service.EnviManageService;
import com.linyang.energy.utils.DataUtil;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.SequenceUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import com.linyang.common.web.common.Log;

/**
 * Created by Administrator on 18-12-11.
 */
@Service
public class EnviManageServiceImpl implements EnviManageService {

    @Autowired
    private EnviManageMapper enviManageMapper;

    @Autowired
    private LedgerManagerMapper ledgerManagerMapper;

    public List<IndustryBean> getPolluteControlTree(Integer type, String parentId){
        List<IndustryBean> list = new ArrayList<IndustryBean>();
        if(type == 1){
            list = enviManageMapper.queryPolluteInfo(null);
        }
        else if(type == 2){
            list = enviManageMapper.queryControlInfo(null);
        }
        return list;
    }

    public List<EnviManageBean> ajaxEnviManagePageList(Map<String, Object> paramInfo){
        return this.enviManageMapper.ajaxEnviManagePageList(paramInfo);
    }

    public void deletePolluteControlRelation(Long relateId){
        this.enviManageMapper.deletePolluteControlRelation(relateId);
    }

    public int getEventSetNumBy(Long relateId){
        return this.enviManageMapper.getEventSetNumBy(relateId);
    }

    public Map<String, Object> getPolluteControlMessage(Long ledgerId, Long id, Integer type){
        Map<String, Object> result = new HashMap<String, Object>();

        //查询设备类型、额定功率
        List<Map<String, Object>> list = this.enviManageMapper.getPolluteControlMessage1(id, type);
        if(CollectionUtils.isNotEmpty(list)){
            result.putAll(list.get(0));
        }
        //查询启动阀值（如果已配置的话）
        Double startVal = this.enviManageMapper.getPolluteControlMessage2(ledgerId, id, type);
        if(null != startVal){
            result.put("startVal", startVal);
        }

        return result;
    }

    public EnviManageBean getEnviManageByRelateId(Long relateId){
        EnviManageBean enviManageBean = null;
        if(relateId > 0){
            enviManageBean = this.enviManageMapper.getEnviManageByRelateId(relateId);
        }
        return enviManageBean;
    }

    public Map<String, Object> getLedgerNotRelated(Long ledgerId, Long relateId){
        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> polluteList = new ArrayList<>();
        List<Map<String, Object>> controlList = new ArrayList<>();
        if(null != relateId && relateId > 0){
            //根据relateId
            EnviManageBean temp = this.enviManageMapper.getEnviManageByRelateId(relateId);
            if(null != temp){
                Map<String, Object> map1 = new HashMap<>();
                map1.put("LEDGER_ID", temp.getPolluteId());
                map1.put("LEDGER_NAME", temp.getPolluteName());
                polluteList.add(map1);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("LEDGER_ID", temp.getControlId());
                map2.put("LEDGER_NAME", temp.getControlName());
                controlList.add(map2);

                result.put("polluteId", temp.getPolluteId());
                result.put("controlId", temp.getControlId());
            }
        }
        //根据ledgerId获取未关联的“产污”
        List<Map<String, Object>> list1 = this.enviManageMapper.getNotRelateEnviManage(ledgerId, 1);
        if(CollectionUtils.isNotEmpty(list1)){
            polluteList.addAll(list1);
        }
        //根据ledgerId获取未关联的“治污”
        List<Map<String, Object>> list2 = this.enviManageMapper.getNotRelateEnviManage(ledgerId, 2);
        if(CollectionUtils.isNotEmpty(list2)){
            controlList.addAll(list2);
        }
        result.put("polluteList", polluteList);
        result.put("controlList", controlList);

        return result;
    }

    public void insertUpdateEnviManage(EnviManageBean enviManageBean){
        if(enviManageBean.getRelateId() > 0){
            //修改
            this.enviManageMapper.updateEnviManage(enviManageBean);
        }
        else{
            //新增
            Long relateId = SequenceUtils.getDBSequence();
            enviManageBean.setRelateId(relateId);
            this.enviManageMapper.insertEnviManage(enviManageBean);
        }
    }

    public List<Map<String, Object>> getChildLedgerList(Long ledgerId){
        List<Map<String, Object>> list = this.enviManageMapper.getChildLedgerList(ledgerId);
        return list;
    }

    public Map<String, Object> ajaxEnviContolResult(Long partId, String ledgerName, String baseTime, String beginTime, String endTime, Long accountId, int isGroup, Long userLedgerId){
        Map<String, Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> resultList = getResultListBy(partId, ledgerName, accountId, isGroup, userLedgerId);
        //用resultList去查每个企业的产污列表
        Double baseTotal = new Double(0);
        Double actualTotal = new Double(0);
        Double percentTotal = new Double(0);
        int interval = 1 + DateUtil.daysBetweenDates(DateUtil.convertStrToDate(endTime, DateUtil.SHORT_PATTERN), DateUtil.convertStrToDate(beginTime, DateUtil.SHORT_PATTERN));
        if(CollectionUtil.isNotEmpty(resultList)){
            for(int i = 0; i < resultList.size(); i++){
                Map<String, Object> map = resultList.get(i);
                Long ledId = Long.valueOf(map.get("LEDGER_ID").toString());
                //查该企业下的产污列表
                List<Map<String, Object>> list = this.enviManageMapper.getPolluteDetailByLedgerId(ledId, DateUtil.convertStrToDate(baseTime, DateUtil.SHORT_PATTERN),
                                                                DateUtil.convertStrToDate(beginTime, DateUtil.SHORT_PATTERN), DateUtil.convertStrToDate(endTime, DateUtil.SHORT_PATTERN));
                if(CollectionUtil.isNotEmpty(list)){
                    for(int j = 0; j < list.size(); j++){
                        Map<String, Object> item2 = list.get(j);
                        Double baseData = Double.valueOf(item2.get("baseData").toString());
                        baseData = interval * baseData;
                        Double actualData = Double.valueOf(item2.get("actualData").toString());
                        Double percent = new Double(0);
                        if(baseData != 0){
                            percent = 100 * (actualData - baseData)/baseData;
                        }
                        else if(actualData > 0){
                            percent = 100D;
                        }
                        else if(actualData < 0){
                            percent = -100D;
                        }
                        item2.put("percent", NumberUtil.formatDouble(percent, "0.#"));           //塞到list里去
                        item2.put("baseData", NumberUtil.formatDouble(baseData, "0.#"));
                        item2.put("actualData", NumberUtil.formatDouble(actualData, "0.#"));

                        //算合计
                        baseTotal = baseTotal + baseData;
                        actualTotal = actualTotal + actualData;
                    }
                }
                map.put("list", list);                     //塞到resultList里去
            }
        }
        //算合计
        if(baseTotal != 0){
            percentTotal = 100 * (actualTotal - baseTotal)/baseTotal;
        }
        else if(actualTotal > 0){
            percentTotal = 100D;
        }
        else if(actualTotal < 0){
            percentTotal = -100D;
        }

        result.put("resultList", resultList);
        result.put("percentTotal", NumberUtil.formatDouble(percentTotal, "0.#"));
        result.put("baseTotal", NumberUtil.formatDouble(baseTotal, "0.#"));
        result.put("actualTotal", NumberUtil.formatDouble(actualTotal, "0.#"));
        return result;
    }

    public Map<String, Object> ajaxEnviPolluteOpen(Long partId, String ledgerName, Integer hasControl, String beginTime, String endTime, Long accountId, int isGroup, Long userLedgerId){
        Map<String, Object> result = new HashMap<String, Object>();
        Date beginDate = DateUtil.clearDate(DateUtil.convertStrToDate(beginTime, DateUtil.SHORT_PATTERN));
        Date endDate = DateUtil.getDateLastMoment(DateUtil.convertStrToDate(endTime, DateUtil.SHORT_PATTERN));

        List<Map<String, Object>> resultList = getResultListBy(partId, ledgerName, accountId, isGroup, userLedgerId);
        //用resultList去查每个企业的产污列表
        Double pollutePTotal = new Double(0);
        Double polluteOpenTotal = new Double(0);
        Double polluteQTotal = new Double(0);
        Double controlPTotal = new Double(0);
        Double controlOpenTotal = new Double(0);
        Double controlQTotal = new Double(0);
        Double errorOpenTotal = new Double(0);
        if(CollectionUtil.isNotEmpty(resultList)){
            for(int i = 0; i < resultList.size(); i++){
                Map<String, Object> map = resultList.get(i);
                Long ledId = Long.valueOf(map.get("LEDGER_ID").toString());
                //查该企业下的产污列表
                List<Map<String, Object>> list = this.enviManageMapper.getPolluteOpenDetailBy(ledId, hasControl); //polluteId,polluteName,controlId,controlName,polluteVal,controlVal
                if(CollectionUtil.isNotEmpty(list)){
                    for(int j = 0; j < list.size(); j++){
                        Map<String, Object> item1 = list.get(j);
                        Long polluteId = Long.valueOf(item1.get("polluteId").toString());
                        Double polluteEpower = this.enviManageMapper.getLedgerEpower(polluteId);             //产污 额定功率
                        if(null != polluteEpower){
                            polluteEpower = NumberUtil.formatDouble((double)polluteEpower, "0.#");
                            pollutePTotal = pollutePTotal + polluteEpower;
                        }
                        item1.put("polluteEpower", polluteEpower);

                        Double polluteQ = this.enviManageMapper.getLedgerQby(polluteId, beginDate, endDate);     //产污 电量
                        if(null != polluteQ){
                            polluteQ = NumberUtil.formatDouble((double)polluteQ, "0.#");
                            polluteQTotal = polluteQTotal + polluteQ;
                        }
                        item1.put("polluteQ", polluteQ);

                        if(item1.keySet().contains("controlId") && StringUtil.isNotEmpty(item1.get("controlId").toString())){
                            //有治理源
                            Long controlId = Long.valueOf(item1.get("controlId").toString());
                            Double controlEpower = this.enviManageMapper.getLedgerEpower(controlId);             //治污 额定功率
                            if(null != controlEpower){
                                controlEpower = NumberUtil.formatDouble((double)controlEpower, "0.#");
                                controlPTotal = controlPTotal + controlEpower;
                            }
                            item1.put("controlEpower", controlEpower);

                            Double controlQ = this.enviManageMapper.getLedgerQby(controlId, beginDate, endDate);     //治污 电量
                            if(null != controlQ){
                                controlQ = NumberUtil.formatDouble((double)controlQ, "0.#");
                                controlQTotal = controlQTotal + controlQ;
                            }
                            item1.put("controlQ", controlQ);

                            //产污开机时间列表
                            Double polluteVal = Double.valueOf(item1.get("polluteVal").toString());
                            List<String> polluteTimes = this.enviManageMapper.getPolluteControlOpenTimes(polluteId, beginDate, endDate, polluteVal);
                            double polluteOpenTime = 0;
                            if(CollectionUtil.isNotEmpty(polluteTimes)){
                                polluteOpenTime = polluteTimes.size();
                            }
                            item1.put("polluteOpenTime", NumberUtil.formatDouble(polluteOpenTime/4, "0.##"));         //产污 开机时长
                            polluteOpenTotal = polluteOpenTotal + polluteOpenTime/4;

                            //治污开机时间列表
                            Double controlVal = Double.valueOf(item1.get("controlVal").toString());
                            List<String> controlTimes = this.enviManageMapper.getPolluteControlOpenTimes(controlId, beginDate, endDate, controlVal);
                            double controlOpenTime = 0;
                            if(CollectionUtil.isNotEmpty(controlTimes)){
                                controlOpenTime = controlTimes.size();
                            }
                            item1.put("controlOpenTime", NumberUtil.formatDouble(controlOpenTime/4, "0.##"));         //治污 开机时长
                            controlOpenTotal = controlOpenTotal + controlOpenTime/4;

                            //计算异常时长
                            double count = 0;
                            for(String contolTime:controlTimes){
                                if(polluteTimes.contains(contolTime)){
                                    count = count + 1;
                                }
                            }
                            double errorOpenTime = polluteOpenTime - count;
                            item1.put("errorOpenTime", NumberUtil.formatDouble(errorOpenTime/4, "0.##"));         //异常时长
                            errorOpenTotal = errorOpenTotal + errorOpenTime/4;
                        }
                        else{
                            //无治理源
                            item1.put("controlName", null);
                            item1.put("controlEpower", null);
                            item1.put("controlOpenTime", null);
                            item1.put("controlQ", null);

                            List<String> polluteTimes = this.enviManageMapper.getPolluteControlOpenTimes(polluteId, beginDate, endDate, 0D);
                            double polluteOpenTime = 0;
                            if(CollectionUtil.isNotEmpty(polluteTimes)){
                                polluteOpenTime = polluteTimes.size();
                            }
                            item1.put("polluteOpenTime", NumberUtil.formatDouble(polluteOpenTime/4, "0.##"));           //产污 开机时长
                            polluteOpenTotal = polluteOpenTotal + polluteOpenTime/4;

                            item1.put("errorOpenTime", NumberUtil.formatDouble(polluteOpenTime/4, "0.##"));            //异常时长
                            errorOpenTotal = errorOpenTotal + polluteOpenTime/4;
                        }
                    }
                }
                map.put("list", list);                     //塞到resultList里去
            }
        }

        result.put("resultList", resultList);
        result.put("pollutePTotal", NumberUtil.formatDouble((double)pollutePTotal, "0.#"));
        result.put("polluteOpenTotal", polluteOpenTotal);
        result.put("polluteQTotal", NumberUtil.formatDouble((double)polluteQTotal, "0.#"));
        result.put("controlPTotal", NumberUtil.formatDouble((double)controlPTotal, "0.#"));
        result.put("controlOpenTotal", controlOpenTotal);
        result.put("controlQTotal", NumberUtil.formatDouble((double)controlQTotal, "0.#"));
        result.put("errorOpenTotal", errorOpenTotal);
        return result;
    }

    private List<Map<String, Object>> getResultListBy(Long partId, String ledgerName, Long accountId, int isGroup, Long userLedgerId){
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if(partId > 0){   //选择了区域
            String partName = this.ledgerManagerMapper.selectByLedgerId(partId).getLedgerName();
            resultList = this.enviManageMapper.getLedgersOfPart(partId, partName, ledgerName);//用区域去查下面的企业(PART_NAME, LEDGER_ID, LEDGER_NAME)
        }
        else{      //未选择区域

            if(isGroup == 0){   //非群组
                List<Map<String, Object>> pIds = this.enviManageMapper.getPartsOfNotGroup(userLedgerId);
                if(CollectionUtil.isNotEmpty(pIds)){
                    for(int i = 0; i < pIds.size(); i++){
                        Map<String, Object> item = pIds.get(i);
                        Long pId = Long.valueOf(item.get("LEDGER_ID").toString());
                        String pName = item.get("LEDGER_NAME").toString();
                        List<Map<String, Object>> tempList = this.enviManageMapper.getLedgersOfPart(pId, pName, ledgerName);//用区域去查下面的企业(PART_NAME, LEDGER_ID, LEDGER_NAME)
                        if(CollectionUtil.isNotEmpty(tempList)){
                            resultList.addAll(tempList);
                        }
                    }
                }
            }
            else{     //群组
                List<Map<String, Object>> tempList = this.enviManageMapper.getLedgersOfGroup(accountId, ledgerName);
                if(CollectionUtil.isNotEmpty(tempList)){
                    resultList.addAll(tempList);
                }
            }
        }
        return resultList;
    }


    @SuppressWarnings("unchecked")
    public void getEleExcel(String sheetName, OutputStream output, Map<String, Object> queryMap) throws UnsupportedEncodingException {
        // 声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        // 生成一个表格"治理效果分析"。
        HSSFSheet sheet = null;
        if (sheetName != null) {
            sheet = wb.createSheet(sheetName);
        } else {
            sheet = wb.createSheet("治理效果分析");
        }
        // 设置默认宽度为20字节
        sheet.setDefaultColumnWidth(20);

        //表头样式
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
        //表格内容样式1
        HSSFCellStyle style_ = wb.createCellStyle();
        style_.setRightBorderColor(HSSFColor.BLACK.index);
        style_.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style_.setLeftBorderColor(HSSFColor.BLACK.index);
        style_.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style_.setTopBorderColor(HSSFColor.BLACK.index);
        style_.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style_.setBottomBorderColor(HSSFColor.BLACK.index);
        style_.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style_.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font_ = wb.createFont();               //字体
        font_.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style_.setFont(font_);
        //表格内容样式2
        HSSFCellStyle style_2 = wb.createCellStyle();
        style_2.setRightBorderColor(HSSFColor.BLACK.index);
        style_2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style_2.setLeftBorderColor(HSSFColor.BLACK.index);
        style_2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style_2.setTopBorderColor(HSSFColor.BLACK.index);
        style_2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style_2.setBottomBorderColor(HSSFColor.BLACK.index);
        style_2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style_2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font_2 = wb.createFont();               //字体
        font_2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font_2.setColor(HSSFColor.RED.index);
        style_2.setFont(font_2);
        //表格内容样式3
        HSSFCellStyle style_3 = wb.createCellStyle();
        style_3.setRightBorderColor(HSSFColor.BLACK.index);
        style_3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style_3.setLeftBorderColor(HSSFColor.BLACK.index);
        style_3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style_3.setTopBorderColor(HSSFColor.BLACK.index);
        style_3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style_3.setBottomBorderColor(HSSFColor.BLACK.index);
        style_3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style_3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font_3 = wb.createFont();               //字体
        font_3.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font_3.setColor(HSSFColor.GREEN.index);
        style_3.setFont(font_3);


        // 生成表头，也就是第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cellA = row.createCell(0);
        HSSFCell cellB = row.createCell(1);
        HSSFCell cellC = row.createCell(2);
        HSSFCell cellD = row.createCell(3);
        HSSFCell cellE = row.createCell(4);
        HSSFCell cellF = row.createCell(5);
        HSSFCell cellG = row.createCell(6);
        cellA.setCellStyle(titlestyle);
        cellB.setCellStyle(titlestyle);
        cellC.setCellStyle(titlestyle);
        cellD.setCellStyle(titlestyle);
        cellE.setCellStyle(titlestyle);
        cellF.setCellStyle(titlestyle);
        cellG.setCellStyle(titlestyle);
        cellA.setCellValue("序号");
        cellB.setCellValue("所属机构");
        cellC.setCellValue("所属企业");
        cellD.setCellValue("产污设施");
        cellE.setCellValue("基准电量(kWh)");
        cellF.setCellValue("实际电量(kWh)");
        cellG.setCellValue("增幅(%)");

        // excel表格内容填充
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) queryMap.get("resultList");
        String percentTotal = queryMap.get("percentTotal").toString();
        String baseTotal = queryMap.get("baseTotal").toString();
        String actualTotal = queryMap.get("actualTotal").toString();

        int index = 1;
        if(CollectionUtil.isNotEmpty(resultList)){
            for(int j = 0; j < resultList.size(); j++){
                Map<String, Object> item1 = resultList.get(j);
                String partName = item1.get("PART_NAME").toString();
                String ledgerName = item1.get("LEDGER_NAME").toString();
                List<Map<String, Object>> list = (List<Map<String, Object>>) item1.get("list");
                if(CollectionUtil.isNotEmpty(list)){
                    for(int k = 0; k < list.size(); k++){
                        Map<String, Object> item2 = list.get(k);
                        String baseData = item2.get("baseData").toString();
                        String actualData = item2.get("actualData").toString();
                        String percent = item2.get("percent").toString();
                        String polluteName = item2.get("ledgerName").toString();

                        HSSFRow row1 = sheet.createRow(index);
                        HSSFCell cell1A = row1.createCell(0);
                        HSSFCell cell1B = row1.createCell(1);
                        HSSFCell cell1C = row1.createCell(2);
                        HSSFCell cell1D = row1.createCell(3);
                        HSSFCell cell1E = row1.createCell(4);
                        HSSFCell cell1F = row1.createCell(5);
                        HSSFCell cell1G = row1.createCell(6);
                        cell1A.setCellStyle(style_);
                        cell1B.setCellStyle(style_);
                        cell1C.setCellStyle(style_);
                        cell1D.setCellStyle(style_);
                        cell1E.setCellStyle(style_);
                        cell1F.setCellStyle(style_);
                        if(Double.valueOf(percent) > 0){
                            cell1G.setCellStyle(style_2);
                        }
                        else{
                            cell1G.setCellStyle(style_3);
                        }
                        cell1A.setCellValue(index);
                        cell1B.setCellValue(partName);
                        cell1C.setCellValue(ledgerName);
                        cell1D.setCellValue(polluteName);
                        cell1E.setCellValue(baseData);
                        cell1F.setCellValue(actualData);
                        cell1G.setCellValue(percent);
                        index++;
                    }
                }
            }

            HSSFRow row1 = sheet.createRow(index);
            HSSFCell cell1A = row1.createCell(0);
            HSSFCell cell1B = row1.createCell(1);
            HSSFCell cell1C = row1.createCell(2);
            HSSFCell cell1D = row1.createCell(3);
            HSSFCell cell1E = row1.createCell(4);
            HSSFCell cell1F = row1.createCell(5);
            HSSFCell cell1G = row1.createCell(6);
            cell1A.setCellStyle(style_);
            cell1B.setCellStyle(style_);
            cell1C.setCellStyle(style_);
            cell1D.setCellStyle(style_);
            cell1E.setCellStyle(style_);
            cell1F.setCellStyle(style_);
            if(Double.valueOf(percentTotal) > 0){
                cell1G.setCellStyle(style_2);
            }
            else{
                cell1G.setCellStyle(style_3);
            }
            cell1A.setCellValue("合计");
            cell1B.setCellValue("");
            cell1C.setCellValue("");
            cell1D.setCellValue("");
            cell1E.setCellValue(baseTotal);
            cell1F.setCellValue(actualTotal);
            cell1G.setCellValue(percentTotal);
        }
        try {
            output.flush();
            wb.write(output);
            output.close();
        } catch (IOException e) {
            Log.info("getEleExcel error IOException");
        }
    }

    @SuppressWarnings("unchecked")
    public void getOpenExcel(String sheetName, OutputStream output, Map<String, Object> queryMap) throws UnsupportedEncodingException {
        // 声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        // 生成一个表格"设施开机分析"
        HSSFSheet sheet = null;
        if (sheetName != null) {
            sheet = wb.createSheet(sheetName);
        } else {
            sheet = wb.createSheet("设施开机分析");
        }
        // 设置默认宽度为20字节
        sheet.setDefaultColumnWidth(20);

        //表头样式
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
        //表格内容样式1
        HSSFCellStyle style_ = wb.createCellStyle();
        style_.setRightBorderColor(HSSFColor.BLACK.index);
        style_.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style_.setLeftBorderColor(HSSFColor.BLACK.index);
        style_.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style_.setTopBorderColor(HSSFColor.BLACK.index);
        style_.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style_.setBottomBorderColor(HSSFColor.BLACK.index);
        style_.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style_.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font_ = wb.createFont();               //字体
        font_.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style_.setFont(font_);

        // 生成表头，也就是第一行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cellA = row.createCell(0);
        HSSFCell cellB = row.createCell(1);
        HSSFCell cellC = row.createCell(2);
        HSSFCell cellD = row.createCell(3);
        HSSFCell cellE = row.createCell(4);
        HSSFCell cellF = row.createCell(5);
        HSSFCell cellG = row.createCell(6);
        HSSFCell cellH = row.createCell(7);
        HSSFCell cellI = row.createCell(8);
        HSSFCell cellJ = row.createCell(9);
        HSSFCell cellK = row.createCell(10);
        HSSFCell cellL = row.createCell(11);
        cellA.setCellStyle(titlestyle);
        cellB.setCellStyle(titlestyle);
        cellC.setCellStyle(titlestyle);
        cellD.setCellStyle(titlestyle);
        cellE.setCellStyle(titlestyle);
        cellF.setCellStyle(titlestyle);
        cellG.setCellStyle(titlestyle);
        cellH.setCellStyle(titlestyle);
        cellI.setCellStyle(titlestyle);
        cellJ.setCellStyle(titlestyle);
        cellK.setCellStyle(titlestyle);
        cellL.setCellStyle(titlestyle);
        cellA.setCellValue("序号");
        cellB.setCellValue("所属机构");
        cellC.setCellValue("所属企业");
        cellD.setCellValue("产污设施");
        cellE.setCellValue("额定功率(kW)");
        cellF.setCellValue("开机时长(h)");
        cellG.setCellValue("电量(kWh)");
        cellH.setCellValue("治污设施");
        cellI.setCellValue("额定功率(kW)");
        cellJ.setCellValue("开机时长(h)");
        cellK.setCellValue("电量(kWh)");
        cellL.setCellValue("异常时长(h)");

        // excel表格内容填充
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) queryMap.get("resultList");
        String pollutePTotal = queryMap.get("pollutePTotal").toString();
        String polluteOpenTotal = queryMap.get("polluteOpenTotal").toString();
        String polluteQTotal = queryMap.get("polluteQTotal").toString();
        String controlPTotal = queryMap.get("controlPTotal").toString();
        String controlOpenTotal = queryMap.get("controlOpenTotal").toString();
        String controlQTotal = queryMap.get("controlQTotal").toString();
        String errorOpenTotal = queryMap.get("errorOpenTotal").toString();

        int index = 1;
        if(CollectionUtil.isNotEmpty(resultList)){
            for(int j = 0; j < resultList.size(); j++){
                Map<String, Object> item1 = resultList.get(j);
                String partName = item1.get("PART_NAME").toString();
                String ledgerName = item1.get("LEDGER_NAME").toString();
                List<Map<String, Object>> list = (List<Map<String, Object>>) item1.get("list");
                if(CollectionUtil.isNotEmpty(list)){
                    for(int k = 0; k < list.size(); k++){
                        Map<String, Object> item2 = list.get(k);
                        String polluteName = "-";
                        if(item2.keySet().contains("polluteName") && item2.get("polluteName") != null){
                            polluteName = item2.get("polluteName").toString();
                        }
                        String polluteEpower = "-";
                        if(item2.keySet().contains("polluteEpower") && item2.get("polluteEpower") != null){
                            polluteEpower = item2.get("polluteEpower").toString();
                        }
                        String polluteOpenTime = "-";
                        if(item2.keySet().contains("polluteOpenTime") && item2.get("polluteOpenTime") != null){
                            polluteOpenTime = item2.get("polluteOpenTime").toString();
                        }
                        String polluteQ = "-";
                        if(item2.keySet().contains("polluteQ") && item2.get("polluteQ") != null){
                            polluteQ = item2.get("polluteQ").toString();
                        }
                        String controlName = "-";
                        if(item2.keySet().contains("controlName") && item2.get("controlName") != null){
                            controlName = item2.get("controlName").toString();
                        }
                        String controlEpower = "-";
                        if(item2.keySet().contains("controlEpower") && item2.get("controlEpower") != null){
                            controlEpower = item2.get("controlEpower").toString();
                        }
                        String controlOpenTime = "-";
                        if(item2.keySet().contains("controlOpenTime") && item2.get("controlOpenTime") != null){
                            controlOpenTime = item2.get("controlOpenTime").toString();
                        }
                        String controlQ = "-";
                        if(item2.keySet().contains("controlQ") && item2.get("controlQ") != null){
                            controlQ = item2.get("controlQ").toString();
                        }
                        String errorOpenTime = "-";
                        if(item2.keySet().contains("errorOpenTime") && item2.get("errorOpenTime") != null){
                            errorOpenTime = item2.get("errorOpenTime").toString();
                        }

                        HSSFRow row1 = sheet.createRow(index);
                        HSSFCell cell1A = row1.createCell(0);
                        HSSFCell cell1B = row1.createCell(1);
                        HSSFCell cell1C = row1.createCell(2);
                        HSSFCell cell1D = row1.createCell(3);
                        HSSFCell cell1E = row1.createCell(4);
                        HSSFCell cell1F = row1.createCell(5);
                        HSSFCell cell1G = row1.createCell(6);
                        HSSFCell cell1H = row1.createCell(7);
                        HSSFCell cell1I = row1.createCell(8);
                        HSSFCell cell1J = row1.createCell(9);
                        HSSFCell cell1K = row1.createCell(10);
                        HSSFCell cell1L = row1.createCell(11);
                        cell1A.setCellStyle(style_);
                        cell1B.setCellStyle(style_);
                        cell1C.setCellStyle(style_);
                        cell1D.setCellStyle(style_);
                        cell1E.setCellStyle(style_);
                        cell1F.setCellStyle(style_);
                        cell1G.setCellStyle(style_);
                        cell1H.setCellStyle(style_);
                        cell1I.setCellStyle(style_);
                        cell1J.setCellStyle(style_);
                        cell1K.setCellStyle(style_);
                        cell1L.setCellStyle(style_);
                        cell1A.setCellValue(index);
                        cell1B.setCellValue(partName);
                        cell1C.setCellValue(ledgerName);
                        cell1D.setCellValue(polluteName);
                        cell1E.setCellValue(polluteEpower);
                        cell1F.setCellValue(polluteOpenTime);
                        cell1G.setCellValue(polluteQ);
                        cell1H.setCellValue(controlName);
                        cell1I.setCellValue(controlEpower);
                        cell1J.setCellValue(controlOpenTime);
                        cell1K.setCellValue(controlQ);
                        cell1L.setCellValue(errorOpenTime);
                        index++;
                    }
                }
            }

            HSSFRow row1 = sheet.createRow(index);
            HSSFCell cell1A = row1.createCell(0);
            HSSFCell cell1B = row1.createCell(1);
            HSSFCell cell1C = row1.createCell(2);
            HSSFCell cell1D = row1.createCell(3);
            HSSFCell cell1E = row1.createCell(4);
            HSSFCell cell1F = row1.createCell(5);
            HSSFCell cell1G = row1.createCell(6);
            HSSFCell cell1H = row1.createCell(7);
            HSSFCell cell1I = row1.createCell(8);
            HSSFCell cell1J = row1.createCell(9);
            HSSFCell cell1K = row1.createCell(10);
            HSSFCell cell1L = row1.createCell(11);
            cell1A.setCellStyle(style_);
            cell1B.setCellStyle(style_);
            cell1C.setCellStyle(style_);
            cell1D.setCellStyle(style_);
            cell1E.setCellStyle(style_);
            cell1F.setCellStyle(style_);
            cell1G.setCellStyle(style_);
            cell1H.setCellStyle(style_);
            cell1I.setCellStyle(style_);
            cell1J.setCellStyle(style_);
            cell1K.setCellStyle(style_);
            cell1L.setCellStyle(style_);
            cell1A.setCellValue("合计");
            cell1B.setCellValue("");
            cell1C.setCellValue("");
            cell1D.setCellValue("");
            cell1E.setCellValue(pollutePTotal);
            cell1F.setCellValue(polluteOpenTotal);
            cell1G.setCellValue(polluteQTotal);
            cell1H.setCellValue("");
            cell1I.setCellValue(controlPTotal);
            cell1J.setCellValue(controlOpenTotal);
            cell1K.setCellValue(controlQTotal);
            cell1L.setCellValue(errorOpenTotal);
        }
        try {
            output.flush();
            wb.write(output);
            output.close();
        } catch (IOException e) {
            Log.info("getOpenExcel error IOException");
        }
    }

    @Override
    public List<Map<String, Object>> queryReducePlanList(Map<String,Object> queryMap){
        List<Map<String, Object>> list = this.enviManageMapper.queryReducePlanPageList(queryMap);
        return list;
    }

    @Override
    public List<Map<String, Object>> queryReduceList(Map<String,Object> queryMap){
        List<Map<String, Object>> resultList = this.enviManageMapper.getReduceLedgerPageList(queryMap);

        for(Map<String, Object> temp : resultList){
            Long ledgerId = Long.valueOf(temp.get("ledgerId").toString());
            String beginTime = temp.get("begin").toString();
            String endTime = temp.get("end").toString();
            Integer planType = Integer.valueOf(queryMap.get("planType").toString());
            Integer limitResult = Integer.valueOf(queryMap.get("limitResult").toString());
            List<Map<String, Object>> polluteList = this.enviManageMapper.getReducePolluteList(ledgerId, beginTime, endTime, planType, limitResult);
            Double planQ = 0D;
            String limit_result = "";
            for(Map<String, Object> pollute : polluteList){
                int percent = Integer.valueOf(pollute.get("percent").toString());
                if(limit_result.isEmpty()){
                    limit_result = pollute.get("LIMIT_RESULT").toString();
                }
                else {
                    limit_result = limit_result + "," + pollute.get("LIMIT_RESULT").toString();
                }

                Long polluteId = Long.valueOf(pollute.get("polluteId").toString());
                String start = pollute.get("start").toString();
                String over = pollute.get("over").toString();
                Double polluteQ = this.enviManageMapper.getPolluteLedgerQBy(polluteId, start, over);
                polluteQ = NumberUtil.formatDouble(polluteQ * percent/100, NumberUtil.PATTERN_INTEGER);
                planQ = planQ + polluteQ;
            }
            temp.put("planQ", planQ);
            temp.put("limit_result", limit_result);

            //得到正常生产日平均电量
            Double average = this.enviManageMapper.getLedgerAverageQ(ledgerId);
            temp.put("average", average);
            if(null != average && average != 0){
                int interval = DateUtil.daysBetweenDates(DateUtil.convertStrToDate(endTime, DateUtil.SHORT_PATTERN), DateUtil.convertStrToDate(beginTime, DateUtil.SHORT_PATTERN)) + 1;
                Double limitRate = NumberUtil.formatDouble((100*planQ)/(interval * average), NumberUtil.PATTERN_FLOAT);
                temp.put("limitRate", limitRate);
            }
            else {
                temp.put("limitRate", "-");
            }
        }

        return resultList;
    }

    @Override
    public List<Map<String, Object>> queryReduceRanking(Map<String,Object> queryMap){
        List<Map<String, Object>> resultList = this.enviManageMapper.getReduceLedgerRanking(queryMap);

        for(Map<String, Object> temp : resultList){
            Long ledgerId = Long.valueOf(temp.get("ledgerId").toString());
            String beginTime = temp.get("begin").toString();
            String endTime = temp.get("end").toString();
            Integer planType = Integer.valueOf(queryMap.get("planType").toString());

            List<Map<String, Object>> polluteList = this.enviManageMapper.getReducePolluteList(ledgerId, beginTime, endTime, planType, 0);
            Double planQ = 0D;

            for(Map<String, Object> pollute : polluteList){
                int percent = Integer.valueOf(pollute.get("percent").toString());

                Long polluteId = Long.valueOf(pollute.get("polluteId").toString());
                String start = pollute.get("start").toString();
                String over = pollute.get("over").toString();
                Double polluteQ = this.enviManageMapper.getPolluteLedgerQBy(polluteId, start, over);
                polluteQ = NumberUtil.formatDouble(polluteQ * percent/100, NumberUtil.PATTERN_FLOAT);
                planQ = planQ + polluteQ;
            }
            temp.put("planQ", planQ);

            //得到基准电量
            Double baseQ = this.enviManageMapper.getLedgerAverageQ(ledgerId);
            if(null != baseQ && baseQ != 0){
                int interval = DateUtil.daysBetweenDates(DateUtil.convertStrToDate(endTime, DateUtil.SHORT_PATTERN), DateUtil.convertStrToDate(beginTime, DateUtil.SHORT_PATTERN)) + 1;
                baseQ = NumberUtil.formatDouble(interval * baseQ, NumberUtil.PATTERN_FLOAT);
                temp.put("baseQ", baseQ);

                //降低电量
                Double reduceQ = baseQ - planQ;
                temp.put("reduceQ", reduceQ);
                //降低电量(标煤)
                CommonResource resource = SpringContextHolder.getBean(CommonResource.class);
                Double reduceCo = DataUtil.doubleDivide(DataUtil.doubleMultiply(reduceQ, resource.getElecUnit()), 1000, 2); //换算成标准煤,单位:吨
                temp.put("reduceCo", reduceCo);
                //降低率
                Double rate = NumberUtil.formatDouble(100* reduceQ/baseQ, NumberUtil.PATTERN_FLOAT);
                temp.put("rate", rate);
            }
            else {
                temp.put("baseQ", "-");
                temp.put("reduceQ", "-");
                temp.put("reduceCo", "-");
                temp.put("rate", "-");
            }
        }

        return resultList;
    }

    @Override
    public void updateLimitResult(Map<String,Object> queryMap){
        this.enviManageMapper.updateLimitResult(queryMap);
    }
}
