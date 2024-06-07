package com.linyang.energy.service.impl;

import com.linyang.common.web.page.Page;
import com.linyang.common.web.page.dialect.Dialect;
import com.linyang.energy.dto.HolidayBean;
import com.linyang.energy.mapping.classAnalysis.ClassMapper;
import com.linyang.energy.mapping.energydataquery.EventQueryMapper;
import com.linyang.energy.model.ClassConfigBean;
import com.linyang.energy.model.WorkingHourBean;
import com.linyang.energy.service.ClassService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OfficialHolidayUtils;
import com.linyang.energy.utils.SequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by Administrator on 16-8-1.
 */
@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private EventQueryMapper eventQueryMapper;

    @Override
    public List<Map<String, Object>> getCompLedgerList(Long ledgerId){
        List<Map<String, Object>> list = this.classMapper.getCompLedgerList(ledgerId);
        return list;
    }

    @Override
    public Map<String,Object> getLedgerClassMessage(Long ledgerId){
        Map<String,Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> classes = this.classMapper.getLedgerClasses(ledgerId);
        if(classes != null && classes.size() > 0){
            result.put("classes", classes);
        }

        return result;
    }

    @Override
    public Map<String,Object> getTeamsByClassId(Long classId){
        Map<String,Object> result = new HashMap<String, Object>();
        ClassConfigBean classConfig = this.classMapper.getClassConfigById(classId);
        result.put("classConfig", classConfig);
        if(classConfig.getCycle() == 3){
            List<Map<String, Object>> teams = this.classMapper.getTeamListByClassId(classId);
            result.put("teams", teams);
            List<Map<String, Object>> baseTimes = this.classMapper.getBasetimeListByClassId(classId);
            result.put("baseTimes", baseTimes);
        }
        else{
            //根据班制ID获取下面的班组列表
            List<Map<String, Object>> teamList = this.classMapper.getTeamsByClassId(classId);
            result.put("teamList", teamList);
        }
        return result;
    }

    @Override
    public Map<String, Object> insertOrUpdateClass(Map<String, Object> classInfo){
        Map<String, Object> result = new HashMap<String, Object>();

        Long ledgerId = Long.valueOf(classInfo.get("ledgerId").toString());
        Long classId = Long.valueOf(classInfo.get("classId").toString());
        String className = classInfo.get("className").toString();
        Integer classType = Integer.valueOf(classInfo.get("classType").toString());
        //先检查班制名是否重复
        int num = this.classMapper.getClassNumByLedgerAndName(ledgerId, classId, className);
        if(num > 0){
            result.put("message", "班制名在该企业中重复");
            return result;
        }
        if(classId == 0){         ///表示新增
            //插入t_class_config
            classId = SequenceUtils.getDBSequence();
            Map<String, Object> classConfig = new HashMap<String, Object>();
            classConfig.put("classId", classId);
            classConfig.put("ledgerId", ledgerId);
            classConfig.put("className", className);
            classConfig.put("classType", classType);
            this.classMapper.insertClassConfig(classConfig);

            if(classType != 3){
                List<Map<String, Object>> teamList = (List<Map<String, Object>>) classInfo.get("teamList");
                insertClassRelationTb(classId, teamList);
            }
            else{
                List<Map<String, Object>> teams = (List<Map<String, Object>>) classInfo.get("teams");
                List<Map<String, Object>> baseTimes = (List<Map<String, Object>>) classInfo.get("baseTimes");
                insertDefineClassRelation(classId, teams, baseTimes);
            }
        }
        else{                                       ///表示修改
            //修改t_class_config
            Map<String, Object> classConfig = new HashMap<String, Object>();
            classConfig.put("classId", classId);
            classConfig.put("className", className);
            classConfig.put("classType", classType);
            this.classMapper.updateClassConfig(classConfig);

            if(classType != 3){
                List<Map<String, Object>> teamList = (List<Map<String, Object>>) classInfo.get("teamList");
                this.classMapper.deleteWorkingHourRelation(classId); //删除T_WORKINGHOUR_RELATION
                this.classMapper.deleteTeamConfig(classId); //删除T_TEAM_CONFIG
                insertClassRelationTb(classId, teamList);
                //this.classMapper.deleteProductOutput(classId); //删除t_product_output
            }
            else{
                List<Map<String, Object>> teams = (List<Map<String, Object>>) classInfo.get("teams");
                List<Map<String, Object>> baseTimes = (List<Map<String, Object>>) classInfo.get("baseTimes");
                insertDefineClassRelation(classId, teams, baseTimes);
            }
        }

        result.put("message", "");
        result.put("classId", classId);
        return result;
    }

    private void insertClassRelationTb(Long classId, List<Map<String, Object>> teamList){
        //插入T_TEAM_CONFIG 和 T_WORKINGHOUR_RELATION
        if(teamList != null && teamList.size() > 0){
            for(int i = 0; i < teamList.size(); i++){
                Map<String, Object> temp = teamList.get(i);
                String teamName = temp.get("teamName").toString();
                Integer onDutyWeek = Integer.valueOf(temp.get("onDutyWeek").toString());
                Date onDuty = DateUtil.convertStrToDate(temp.get("onDuty").toString(), DateUtil.HOUR_MINUTE_PATTERN);
                Integer offDutyWeek = Integer.valueOf(temp.get("offDutyWeek").toString());
                Date offDuty = DateUtil.convertStrToDate(temp.get("offDuty").toString(), DateUtil.HOUR_MINUTE_PATTERN);

                // T_TEAM_CONFIG
                Long teamId = Long.valueOf(temp.get("teamId").toString());
                List<Long> teamIds = this.classMapper.getTeamIdByClassId(classId, teamName);
                if(teamId == 0 && teamIds != null && teamIds.size() > 0){
                    teamId = teamIds.get(0);
                }
                if(teamId == 0){
                    teamId = SequenceUtils.getDBSequence();
                }
                if(teamIds == null || teamIds.size() == 0){
                    this.classMapper.insertTeamConfig(teamId, classId, teamName);
                }
                // T_WORKINGHOUR_RELATION
                Long workHourId = Long.valueOf(temp.get("workHourId").toString());
                if(workHourId == 0){
                    workHourId = SequenceUtils.getDBSequence();
                }
                this.classMapper.insertWorkHourRelation(workHourId, teamId, onDutyWeek, offDutyWeek, onDuty, offDuty);
            }
        }
    }

    private void insertDefineClassRelation(Long classId, List<Map<String, Object>> teams, List<Map<String, Object>> baseTimes){
        if(teams != null && teams.size() > 0 && baseTimes != null && baseTimes.size() > 0){
            this.classMapper.deleteTeamConfig(classId);     //删除T_TEAM_CONFIG
            this.classMapper.deleteClassBaseTime(classId);  //删除T_CLASS_BASE_TIME
            for(int i = 0; i < teams.size(); i++){
                Map<String, Object> temp = teams.get(i);
                Long teamId = Long.valueOf(temp.get("teamId").toString());
                String teamName = temp.get("teamName").toString();
                // T_TEAM_CONFIG
                List<Long> teamIds = this.classMapper.getTeamIdByClassId(classId, teamName);
                if(teamId == 0 && teamIds != null && teamIds.size() > 0){
                    teamId = teamIds.get(0);
                }
                if(teamId == 0){
                    teamId = SequenceUtils.getDBSequence();
                }
                if(teamIds == null || teamIds.size() == 0){
                    this.classMapper.insertTeamConfig(teamId, classId, teamName);
                }
            }
            for(int i = 0; i < baseTimes.size(); i++){
                Map<String, Object> temp = baseTimes.get(i);
                Long timeId = Long.valueOf(temp.get("timeId").toString());
                Integer beginDay = Integer.valueOf(temp.get("beginDay").toString());
                Date beginTime = DateUtil.convertStrToDate(temp.get("beginTime").toString(), DateUtil.HOUR_MINUTE_PATTERN);
                Integer endDay = Integer.valueOf(temp.get("endDay").toString());
                Date endTime = DateUtil.convertStrToDate(temp.get("endTime").toString(), DateUtil.HOUR_MINUTE_PATTERN);
                if(timeId == 0){
                    timeId = SequenceUtils.getDBSequence();
                }
                this.classMapper.insertBaseTimeConfig(timeId, classId, beginDay, beginTime, endDay, endTime);
            }
        }
    }

    @Override
    public void deleteClass(Long classId){
        //删除T_WORKINGHOUR_RELATION
        this.classMapper.deleteWorkingHourRelation(classId);
        //删除t_product_output
        this.classMapper.deleteProductOutput(classId);
        //删除T_TEAM_CONFIG
        this.classMapper.deleteTeamConfig(classId);

        //删除T_WORKSHOP_METER
        this.classMapper.deleteWorkShopMeter(classId);
        //删除T_WORKSHOP_RELATION
        this.classMapper.deleteWorkShopRelation(classId);
        //删除T_WORKSHOP_CONFIG
        this.classMapper.deleteWorkshopConfig(classId);

        //删除班制基本时段表("自定义"用的)、删除班制休息日配置
        this.classMapper.deleteClassBaseTime(classId);
        this.classMapper.deleteClassRestDay(classId);
        this.classMapper.deleteClassCustomHoliday(classId);
        this.classMapper.deleteClassWorkdayRest(classId);

        //删除t_class_config
        this.classMapper.deleteClassConfig(classId);
    }

    @Override
    public List<Map<String, Object>> getWorkShopConfigs(Page page, Long ledgerId){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put(Dialect.pageNameField, page);
        map.put("ledgerId", ledgerId);
        List<Map<String, Object>> list = this.classMapper.getWorkshopPageConfig(map);
        return list == null?new ArrayList<Map<String,Object>>():list;
    }

    @Override
    public Map<String,Object> getWorkShopDetail(Long ledgerId, Long workShopId){
        Map<String,Object> result = new HashMap<String, Object>();

        List<Map<String, Object>> classes = this.classMapper.getLedgerClasses(ledgerId);
        result.put("classes", classes);
        Map<String, Object> workShop = this.classMapper.getWorkShopById(workShopId);
        if(workShop != null){
            result.putAll(workShop);
        }
        List<Map<String, Object>> relations = this.classMapper.getRelationsByWorkShop(workShopId);
        result.put("relations", relations);

        return result;
    }

    @Override
    public Map<String, Object> insertUpdateWorkshop(Map<String, Object> workInfo){
        Map<String, Object> result = new HashMap<String, Object>();

        Long ledgerId = Long.valueOf(workInfo.get("ledgerId").toString());
        Long workShopId = Long.valueOf(workInfo.get("workShopId").toString());
        String workShopName = workInfo.get("workShopName").toString();
        Long classId = Long.valueOf(workInfo.get("classId").toString());
        List<Map<String, Object>> relationList = (List<Map<String, Object>>) workInfo.get("relationList");

        //先检查名称是否重复
        int num = this.classMapper.getWorkshopNumBy(workShopId, ledgerId, workShopName);
        if(num > 0){
            result.put("message", "车间/单位名重复");
            return result;
        }
        if(workShopId == 0){ //新增

            //T_WORKSHOP_CONFIG
            workShopId = SequenceUtils.getDBSequence();
            this.classMapper.insertWorkshopConfig(workShopId,workShopName,ledgerId,classId);
            //T_WORKSHOP_RELATION、T_WORKSHOP_METER
            if(relationList != null && relationList.size() > 0){
                for(int i = 0; i < relationList.size(); i++){
                    Map<String, Object> temp = relationList.get(i);
                    Integer type = Integer.valueOf(temp.get("type").toString());
                    Long id = Long.valueOf(temp.get("id").toString());
                    this.classMapper.insertWorkshopRelation(workShopId,type,id);

                    processWorkshopEmoDcp(workShopId, type, id);
                }
            }
        }
        else{ //修改

            //T_WORKSHOP_CONFIG
            this.classMapper.updateWorkshopConfig(workShopId,workShopName,ledgerId,classId);
            //T_WORKSHOP_RELATION、T_WORKSHOP_METER
            this.classMapper.deleteWorkShopMeterByShopId(workShopId);
            this.classMapper.deleteWorkShopRelationByShopId(workShopId);
            if(relationList != null && relationList.size() > 0){
                for(int i = 0; i < relationList.size(); i++){
                    Map<String, Object> temp = relationList.get(i);
                    Integer type = Integer.valueOf(temp.get("type").toString());
                    Long id = Long.valueOf(temp.get("id").toString());
                    this.classMapper.insertWorkshopRelation(workShopId,type,id);

                    processWorkshopEmoDcp(workShopId, type, id);
                }
            }
        }
        result.put("message", "");
        return result;
    }

    @Override
    public void deleteWorkshopConfig(Long workShopId){
        //删除T_WORKSHOP_METER
        this.classMapper.deleteWorkShopMeterByShopId(workShopId);
        //删除T_WORKSHOP_RELATION
        this.classMapper.deleteWorkShopRelationByShopId(workShopId);
        //删除t_product_output
        this.classMapper.deleteProductOutputByShopId(workShopId);
        //删除T_WORKSHOP_CONFIG
        this.classMapper.deleteWorkshopByShopId(workShopId);
    }

    private void processWorkshopEmoDcp(Long workShopId, Integer type, Long id){
        if(type == 1){  //Emo
             this.classMapper.insertWorkshopMeterEmo(workShopId, id);
        }
        else if(type == 2) {  //Dcp
            int count = this.classMapper.getWorkshopMeterNumByMeterId(workShopId, id);
            if(count == 0){
                this.classMapper.insertWorkshopMeterDcp(workShopId, id);
            }
        }
    }

    @Override
    public void refreshWorkshopMeterAll(Long ledgerId){
        List<Long> workShopIds = this.classMapper.getWorkshopsByLedgerId(ledgerId);
        if(workShopIds != null && workShopIds.size() > 0){
            for(int i = 0; i < workShopIds.size(); i++){
                Long workShopId = workShopIds.get(i);
                processWorkshopMeter(workShopId);
            }
        }
    }

    private void processWorkshopMeter(Long workShopId){
        //删除 T_WORKSHOP_METER
        this.classMapper.deleteWorkShopMeterByShopId(workShopId);
        //开始插入 T_WORKSHOP_METER
        List<Map<String, Object>> list = this.classMapper.getRelationsByWorkShop(workShopId);
        if(list != null && list.size() > 0){
            for(int i = 0; i < list.size(); i++){
                Map<String, Object> temp = list.get(i);
                Integer type = Integer.valueOf(temp.get("TYPE").toString());
                Long id = Long.valueOf(temp.get("ID").toString());
                processWorkshopEmoDcp(workShopId, type, id);
            }
        }
    }

    @Override
    public Map<String,Object> getLedgerCanChoose(Long ledgerId){
        Map<String,Object> result = new HashMap<String, Object>();
        String ledgersStr = "";
        List<String> ledgers = this.classMapper.getEmoStrByLedger(ledgerId);
        if(ledgers != null && ledgers.size() > 0) {
            ledgersStr = StringUtils.join(ledgers.toArray(), ";");
            ledgersStr = ledgersStr + ";";
        }
        result.put("ledgersStr", ledgersStr);
        String metersStr = "";
        List<String> meters = this.classMapper.getDcpStrByLedger(ledgerId);
        if(meters != null && meters.size() > 0) {
            metersStr = StringUtils.join(meters.toArray(), ";");
            metersStr = metersStr + ";";
        }
        result.put("metersStr", metersStr);
        return result;
    }

    @Override
    public List<WorkingHourBean> getRecentClassWorkHours(Long classId){
        List<WorkingHourBean> workHours = this.classMapper.getTeamClassLastInterval(classId, null);
        if(CollectionUtils.isEmpty(workHours)){
            return null;
        }
        while(workHours.get(workHours.size()-1).getOffDuty().before(new Date())){
            //先找到第一天
            Date firstDay = DateUtil.clearDate(workHours.get(workHours.size() - 1).getOffDuty());
            firstDay = DateUtil.getNextDayDate(firstDay);
            //开始循环
            List<WorkingHourBean> list = new ArrayList<WorkingHourBean>();
            List<WorkingHourBean> roundList = this.classMapper.getTeamClassLastInterval(classId, null);
            for(int i = 0; i < roundList.size(); i++){
                while(ifSkipClass(firstDay, classId)){
                    firstDay = DateUtil.getNextDayDate(firstDay);
                }

                WorkingHourBean hourBean = roundList.get(i);
                Date this_begin = hourBean.getOnDuty();      //时段开始
                Date this_end = hourBean.getOffDuty();       //时段结束
                int this_num = DateUtil.daysBetweenDates(this_end, this_begin);  //时段结束、开始 相差天数
                String str_1 = DateUtil.convertDateToStr(firstDay, DateUtil.SHORT_PATTERN) + " "
                        + DateUtil.convertDateToStr(this_begin, DateUtil.HOUR_MINUTE_PATTERN);
                String str_2 = DateUtil.convertDateToStr(firstDay, DateUtil.SHORT_PATTERN) + " "
                        + DateUtil.convertDateToStr(this_end, DateUtil.HOUR_MINUTE_PATTERN);
                Date date_1 = DateUtil.convertStrToDate(str_1, DateUtil.MOUDLE_PATTERN);   //找到的时段开始点
                Date date_2 = DateUtil.convertStrToDate(str_2, DateUtil.MOUDLE_PATTERN);
                date_2 = DateUtil.getSomeNextDayDate(date_2, this_num);                    //找到的时段结束点
                firstDay = DateUtil.getSomeNextDayDate(firstDay, this_num);                //修改“firstDay”
                //计算与下一时段相差天数
                if(i < roundList.size()-1){
                    Date next_begin = roundList.get(i+1).getOnDuty();
                    int this_next = DateUtil.daysBetweenDates(next_begin, this_end);
                    //直到找到“this_next”个非休息日
                    if(this_next > 0){
                        int interval = 0;
                        while (interval < this_next){
                            firstDay = DateUtil.getNextDayDate(firstDay);                  //修改“firstDay”
                            if(!ifSkipClass(firstDay, classId)){
                                interval++;
                            }
                        }
                    }
                }

                WorkingHourBean temp = new WorkingHourBean();
                temp.setTeamId(hourBean.getTeamId());
                temp.setOnDuty(date_1);
                temp.setOffDuty(date_2);
                list.add(temp);
            }
            if(list.get(list.size()-1).getOffDuty().after(new Date())){
                break;
            }
            workHours = list;
        }
        return workHours;
    }

    @Override
    public Map<String, Object> getProductSplitTime(Long teamId, Date beginTime, Date endTime){
        List<Map<String, Date>> result = new ArrayList<Map<String, Date>>();
        Integer cycle = this.classMapper.getCycleByTeamId(teamId);
        if(cycle == null){
              return processResult(result);
        }
        List<Map<String, Object>> teamTimes = this.classMapper.getTeamTimeByTeamId(teamId);
        if(teamTimes == null || teamTimes.size() == 0){
            return processResult(result);
        }
        int length = teamTimes.size();

        if(cycle == 1){  //"天"
            //先找第一个时段
            Date first_begin = null;
            Date first_end = null;
            int first_order = -1;
            for(int i = 0; i < length; i++){
                Map<String, Object> temp_1 = teamTimes.get(i);
                String start_1 = temp_1.get("BEGIN_TIME").toString();
                String end_1 = temp_1.get("END_TIME").toString();
                Map<String, Object> temp_2 = teamTimes.get( (i + 1) % length );
                String start_2 = temp_2.get("BEGIN_TIME").toString();
                String end_2 = temp_2.get("END_TIME").toString();
                if( dayTimeCheck(beginTime, start_1, end_1) ){ //在时段内
                    first_begin = beginTime;
                    String end_str = DateUtil.convertDateToStr(beginTime, DateUtil.SHORT_PATTERN) + " " + end_1;
                    first_end = DateUtil.convertStrToDate(end_str, DateUtil.MOUDLE_PATTERN);
                    if(first_begin.after(first_end)){
                        first_end = DateUtil.addDateDay(first_end, 1);
                    }

                    first_order = i;
                    break;
                }
                else if( dayTimeCheck(beginTime, end_1, start_2) ){
                    String first_str = DateUtil.convertDateToStr(beginTime, DateUtil.SHORT_PATTERN) + " " + start_2;
                    first_begin = DateUtil.convertStrToDate(first_str, DateUtil.MOUDLE_PATTERN);
                    String end_str = DateUtil.convertDateToStr(beginTime, DateUtil.SHORT_PATTERN) + " " + end_2;
                    first_end = DateUtil.convertStrToDate(end_str, DateUtil.MOUDLE_PATTERN);
                    if(first_begin.after(first_end)){
                        first_end = DateUtil.addDateDay(first_end, 1);
                    }

                    first_order = (i + 1)%length;
                    break;
                }
            }
            if(first_begin == null || first_end == null || first_order == -1){
                return processResult(result);
            }
            //判断找到的第一个时段
            boolean isOver = dayCompareToEndTime(endTime, first_begin, first_end, result);
            if(isOver){
                return processResult(result);
            }
            //循环依次找下一个时段
            int addDay = 0;
            if(first_order == length-1){
                Map<String, Object> first_t = teamTimes.get(first_order);
                String first_b = first_t.get("BEGIN_TIME").toString();
                String first_e = first_t.get("END_TIME").toString();
                if(first_b.compareTo(first_e) < 0){
                    addDay = 1;
                }
            }
            while(true){
                Map<String, Object> temp = teamTimes.get( (first_order+1)%length );
                Date lastEnd = result.get(result.size() - 1).get("end");
                String nextBegin_str = DateUtil.convertDateToStr(lastEnd, DateUtil.SHORT_PATTERN) + " " + temp.get("BEGIN_TIME").toString();
                String nextEnd_str = DateUtil.convertDateToStr(lastEnd, DateUtil.SHORT_PATTERN) + " " + temp.get("END_TIME").toString();
                Date nextBegin = DateUtil.convertStrToDate(nextBegin_str, DateUtil.MOUDLE_PATTERN);
                Date nextEnd = DateUtil.convertStrToDate(nextEnd_str, DateUtil.MOUDLE_PATTERN);
                if(addDay == 1){
                    nextBegin = DateUtil.addDateDay(nextBegin, 1);
                    nextEnd = DateUtil.addDateDay(nextEnd, 1);
                    addDay = 0;
                }
                //若是班组时段的最后一条
                if( (first_order+1)%length == length-1){
                    if(nextBegin.after(nextEnd)){ //若跨天 (这次循环，结束时间加1天)
                        nextEnd = DateUtil.addDateDay(nextEnd, 1);
                    }
                    else{ //若没跨天 (下次循环,开始、结束时间都要加1天)
                        addDay = 1;
                    }
                }

                isOver = dayCompareToEndTime(endTime, nextBegin, nextEnd, result);
                if(isOver){
                    break;
                }

                first_order++;
            }
        }
        else if(cycle == 2){  //"周"
            //先找第一个时段
            Date first_begin = null;
            Date first_end = null;
            int first_order = -1;
            for(int i = 0; i < teamTimes.size(); i++){
                String monday = DateUtil.getMonday(DateUtil.convertDateToStr(beginTime, DateUtil.SHORT_PATTERN));
                Map<String, Object> temp = teamTimes.get(i);
                int begin_week = Integer.valueOf(temp.get("BEGIN_WEEK").toString());
                String begin_time = temp.get("BEGIN_TIME").toString();
                int end_week = Integer.valueOf(temp.get("END_WEEK").toString());
                String end_time = temp.get("END_TIME").toString();
                Date i_begin = DateUtil.convertStrToDate(monday + " " + begin_time, DateUtil.MOUDLE_PATTERN);
                i_begin = DateUtil.addDateDay(i_begin, begin_week - 1);
                Date i_end = DateUtil.convertStrToDate(monday + " " + end_time, DateUtil.MOUDLE_PATTERN);
                i_end = DateUtil.addDateDay(i_end, end_week - 1);

                if(beginTime.before(i_begin)){
                    first_begin = i_begin;
                    first_end = i_end;
                    first_order = i;
                    break;
                }
                else if(!beginTime.before(i_begin) && !beginTime.after(i_end)){
                    first_begin = beginTime;
                    first_end = i_end;
                    first_order = i;
                    break;
                }
            }
            if(first_begin == null || first_end == null || first_order == -1){
                String monday = DateUtil.getMonday(DateUtil.convertDateToStr(beginTime, DateUtil.SHORT_PATTERN));
                Map<String, Object> temp = teamTimes.get(0);
                int begin_week = Integer.valueOf(temp.get("BEGIN_WEEK").toString());
                String begin_time = temp.get("BEGIN_TIME").toString();
                int end_week = Integer.valueOf(temp.get("END_WEEK").toString());
                String end_time = temp.get("END_TIME").toString();
                first_begin = DateUtil.convertStrToDate(monday + " " + begin_time, DateUtil.MOUDLE_PATTERN);
                first_begin = DateUtil.addDateDay(first_begin, begin_week - 1 + 7);
                first_end = DateUtil.convertStrToDate(monday + " " + end_time, DateUtil.MOUDLE_PATTERN);
                first_end = DateUtil.addDateDay(first_end, end_week - 1 + 7);

                first_order = 0;
            }
            //判断找到的第一个时段
            boolean isOver = dayCompareToEndTime(endTime, first_begin, first_end, result);
            if(isOver){
                return processResult(result);
            }
            //循环依次找下一个时段
            while (true){
                Map<String, Object> temp = teamTimes.get( (first_order+1)%length );
                int begin_week = Integer.valueOf(temp.get("BEGIN_WEEK").toString());
                String begin_time = temp.get("BEGIN_TIME").toString();
                int end_week = Integer.valueOf(temp.get("END_WEEK").toString());
                String end_time = temp.get("END_TIME").toString();
                Date lastEnd = result.get(result.size() - 1).get("end");
                String monday = DateUtil.getMonday(DateUtil.convertDateToStr(lastEnd, DateUtil.SHORT_PATTERN));
                Date begin = DateUtil.convertStrToDate(monday + " " + begin_time, DateUtil.MOUDLE_PATTERN);
                begin = DateUtil.addDateDay(begin, begin_week - 1);
                if( (first_order+1)%length == 0){
                    begin = DateUtil.addDateDay(begin, 7);
                }
                Date end = DateUtil.convertStrToDate(monday + " " + end_time, DateUtil.MOUDLE_PATTERN);
                end = DateUtil.addDateDay(end, end_week - 1);
                if( (first_order+1)%length == 0){
                    end = DateUtil.addDateDay(end, 7);
                }
                isOver = dayCompareToEndTime(endTime, begin, end, result);
                if(isOver){
                    break;
                }

                first_order++;
            }
        }
        else if(cycle == 3){  //"自定义"
            List<Map<String, Object>> workHours = this.classMapper.getWorkHoursByTeam(teamId);   //该对象常用
            if(CollectionUtils.isEmpty(workHours)){
                return processResult(result);
            }
            Date workBegin = (Date) workHours.get(0).get("BEGIN_TIME");                          //该对象常用
            Date workEnd = (Date) workHours.get(workHours.size() - 1).get("END_TIME");           //该对象常用
            if(DateUtil.getMinBetweenTime(workBegin, endTime) <= 0){
                //基准时段列表(workHours)之前的产量录入，不予考虑
                return processResult(result);
            }
            //找到第一个和 beginTime、endTime有交集 的基准时段列表(workHours)
            while(DateUtil.getMinBetweenTime(workEnd, beginTime) >= 0){
                workHours = getNextDefWorkHours(teamId, workHours);
                workBegin = (Date) workHours.get(0).get("BEGIN_TIME");
                workEnd = (Date) workHours.get(workHours.size() - 1).get("END_TIME");

                //找不到的话,直接返回
                if(DateUtil.getMinBetweenTime(workBegin, endTime) <= 0){
                    return processResult(result);
                }
            }
            //依次寻找下一个迭代时段，并处理，直到越界为止
            while(DateUtil.getMinBetweenTime(workBegin, endTime) > 0){
                for(int i = 0; i < workHours.size(); i++){
                    Date i_begin = (Date) workHours.get(i).get("BEGIN_TIME");
                    Date i_end = (Date) workHours.get(i).get("END_TIME");
                    if(DateUtil.getMinBetweenTime(beginTime, i_begin) <= 0){
                        if(DateUtil.getMinBetweenTime(beginTime, i_end) > 0 && DateUtil.getMinBetweenTime(endTime, i_end) <= 0){

                            if(!ifExclude(beginTime, teamId)){
                                Map<String, Date> map = new HashMap<String, Date>();
                                map.put("begin", beginTime);
                                map.put("end", i_end);
                                result.add(map);
                            }
                        }
                        else if(DateUtil.getMinBetweenTime(beginTime, i_end) > 0 && DateUtil.getMinBetweenTime(endTime, i_end) > 0){

                            if(!ifExclude(beginTime, teamId)){
                                Map<String, Date> map = new HashMap<String, Date>();
                                map.put("begin", beginTime);
                                map.put("end", endTime);
                                result.add(map);
                            }
                            break;
                        }
                    }
                    else if(DateUtil.getMinBetweenTime(beginTime, i_begin) > 0 && DateUtil.getMinBetweenTime(endTime, i_begin) < 0){
                        if(DateUtil.getMinBetweenTime(endTime, i_end) <= 0){

                            if(!ifExclude(i_begin, teamId)){
                                Map<String, Date> map = new HashMap<String, Date>();
                                map.put("begin", i_begin);
                                map.put("end", i_end);
                                result.add(map);
                            }
                        }
                        else if(DateUtil.getMinBetweenTime(endTime, i_end) > 0){

                            if(!ifExclude(i_begin, teamId)){
                                Map<String, Date> map = new HashMap<String, Date>();
                                map.put("begin", i_begin);
                                map.put("end", endTime);
                                result.add(map);
                            }
                            break;
                        }
                    }
                }

                workHours = getNextDefWorkHours(teamId, workHours);
                workBegin = (Date) workHours.get(0).get("BEGIN_TIME");
                workEnd = (Date) workHours.get(workHours.size() - 1).get("END_TIME");
            }
        }
        return processResult(result);
    }

    //"自定义"模式下，寻找下一个基准时段列表(workHours)
    private List<Map<String, Object>> getNextDefWorkHours(Long teamId, List<Map<String, Object>> workHours){
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        if(CollectionUtils.isEmpty(workHours)){
            return mapList;
        }
        //先找到下个“完整班制周期”的第一天
        Date firstDay = DateUtil.clearDate((Date) workHours.get(workHours.size() - 1).get("END_TIME"));
        ClassConfigBean classConfigBean = this.classMapper.getClassConfigByTeamId(teamId);
        List<WorkingHourBean> hourList = this.classMapper.getWorkingHoursByTeamId(teamId);
        if(CollectionUtils.isEmpty(hourList)){
            return mapList;
        }
        Date teamLast = hourList.get(hourList.size() - 1).getOffDuty();   //该班组的最后一个时间点
        List<WorkingHourBean> intervals = this.classMapper.getTeamClassLastInterval(classConfigBean.getClassId(), teamLast);
        if(intervals != null && intervals.size() > 1){
            int order = 1;
            while (order < intervals.size()){
                Date begin = intervals.get(order).getOnDuty();
                Date end = intervals.get(order).getOffDuty();
                int num1 = DateUtil.daysBetweenDates(begin, intervals.get(order - 1).getOffDuty());
                int num2 = DateUtil.daysBetweenDates(end, begin);
                if(num1 == 0){
                    //判断firstDay是否需要跳过
                    while(ifSkip(firstDay, teamId)){
                        firstDay = DateUtil.getNextDayDate(firstDay);
                    }
                }
                else if(num1 > 0){
                    int getNum = 0;
                    while (getNum < num1){
                        firstDay = DateUtil.getNextDayDate(firstDay);
                        if(!ifSkip(firstDay, teamId)){
                            getNum++;
                        }
                    }
                }
                firstDay = DateUtil.addDateDay(firstDay, num2);
                order++;
            }
        }
        firstDay = DateUtil.getNextDayDate(firstDay);

        //至此已找到下一个第一天，开始轮巡
        List<WorkingHourBean> roundList = this.classMapper.getTeamClassBeforeInterval(classConfigBean.getClassId(), teamLast);
        for(int i = 0; i < roundList.size(); i++){
            //判断firstDay是否需要跳过
            while(ifSkip(firstDay, teamId)){
                firstDay = DateUtil.getNextDayDate(firstDay);
            }

            WorkingHourBean hourBean = roundList.get(i);
            Long t_id = hourBean.getTeamId();
            Date this_begin = hourBean.getOnDuty();      //时段开始
            Date this_end = hourBean.getOffDuty();       //时段结束
            int this_num = DateUtil.daysBetweenDates(this_end, this_begin);  //时段结束、开始 相差天数
            String str_1 = DateUtil.convertDateToStr(firstDay, DateUtil.SHORT_PATTERN) + " "
                              + DateUtil.convertDateToStr(this_begin, DateUtil.HOUR_MINUTE_PATTERN);
            String str_2 = DateUtil.convertDateToStr(firstDay, DateUtil.SHORT_PATTERN) + " "
                              + DateUtil.convertDateToStr(this_end, DateUtil.HOUR_MINUTE_PATTERN);
            Date date_1 = DateUtil.convertStrToDate(str_1, DateUtil.MOUDLE_PATTERN);   //找到的时段开始点
            Date date_2 = DateUtil.convertStrToDate(str_2, DateUtil.MOUDLE_PATTERN);
            date_2 = DateUtil.getSomeNextDayDate(date_2, this_num);                    //找到的时段结束点
            firstDay = DateUtil.getSomeNextDayDate(firstDay, this_num);                //修改“firstDay”
            //计算与下一时段相差天数
            if(i < roundList.size()-1){
                Date next_begin = roundList.get(i+1).getOnDuty();
                int this_next = DateUtil.daysBetweenDates(next_begin, this_end);
                //直到找到“this_next”个非休息日
                if(this_next > 0){
                    int interval = 0;
                    while (interval < this_next){
                        firstDay = DateUtil.getNextDayDate(firstDay);                  //修改“firstDay”
                        if(!ifSkip(firstDay, teamId)){
                            interval++;
                        }
                    }
                }
            }
            if(t_id.equals(teamId)){
                Map<String, Object> temp_map = new HashMap<String, Object>();
                temp_map.put("BEGIN_TIME", date_1);
                temp_map.put("END_TIME", date_2);
                mapList.add(temp_map);
            }
        }
        return mapList;
    }


    //"自定义"模式下，轮寻时是否"跳过"
    private boolean ifSkip(Date baseTime, Long teamId){
        ClassConfigBean classBean = this.classMapper.getClassConfigByTeamId(teamId);
        if(classBean == null || classBean.getCircleType() == 1){
            //轮空
            return false;
        }
        else {
            //轮流
            return isClassRestDay(classBean, baseTime);
        }
    }
    private boolean ifSkipClass(Date baseTime, Long classId){
        ClassConfigBean classBean = this.classMapper.getClassConfigById(classId);
        if(classBean == null || classBean.getCircleType() == 1){
            //轮空
            return false;
        }
        else {
            //轮流
            return isClassRestDay(classBean, baseTime);
        }
    }


    //"自定义"模式下，是否"排除"
    private boolean ifExclude(Date baseTime, Long teamId){
        ClassConfigBean classBean = this.classMapper.getClassConfigByTeamId(teamId);
        if(classBean == null || classBean.getCircleType() == 1){
            //轮空
            return isClassRestDay(classBean, baseTime);
        }
        else{
            //轮流
            return false;
        }
    }

    //判断某天是否班制的休息日
    private boolean isClassRestDay(ClassConfigBean classBean, Date baseTime){
        if(classBean.getRestType() == 1){ //全年无休
            return false;
        }
        else if(classBean.getRestType() == 3){ //只法定节日
            List<HolidayBean> holidayList = OfficialHolidayUtils.readOfficialHolidayList();
            if(holidayList != null && holidayList.size() > 0){
                for(int i = 0; i < holidayList.size(); i++){
                    HolidayBean holidayBean = holidayList.get(i);
                    int minusFromDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseTime), holidayBean.getFromDate());
                    int minusEndDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseTime), holidayBean.getEndDate());
                    if(minusFromDate >= 0 && minusEndDate <= 0){
                        return true;
                    }
                }
            }
            return false;
        }
        else if(classBean.getRestType() == 2){ //厂休日
            Long ledgerId = classBean.getLedgerId();
            //判断是否法定节假日
            int defaultHoliday = this.eventQueryMapper.includeDefaultHoliday(ledgerId);
            if(defaultHoliday > 0){
                List<HolidayBean> holidayList = OfficialHolidayUtils.readOfficialHolidayList();
                if(holidayList != null && holidayList.size() > 0){
                    for(int i = 0; i < holidayList.size(); i++){
                        HolidayBean holidayBean = holidayList.get(i);
                        int minusFromDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseTime), holidayBean.getFromDate());
                        int minusEndDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseTime), holidayBean.getEndDate());
                        if(minusFromDate >= 0 && minusEndDate <= 0){
                            return true;
                        }
                    }
                }
            }
            //判断是否是自定义节假日
            int ifHoliday = this.eventQueryMapper.judgeHoliday(ledgerId, DateUtil.clearDate(baseTime));
            if(ifHoliday > 0){
                return true;
            }
            //判断是否休息日
            Integer ifRestDay = this.eventQueryMapper.judgeRestDay(ledgerId, DateUtil.getDateWeekNum(baseTime));
            if(ifRestDay != null && ifRestDay == 1){
                return true;
            }
            return false;
        }
        else if(classBean.getRestType() == 4){ // 自定义休息日
            Long classId = classBean.getClassId();
            //判断是否法定节假日
            int defaultHoliday = this.classMapper.includeDefaultHoliday(classId);
            if(defaultHoliday > 0){
                List<HolidayBean> holidayList = OfficialHolidayUtils.readOfficialHolidayList();
                if(holidayList != null && holidayList.size() > 0){
                    for(int i = 0; i < holidayList.size(); i++){
                        HolidayBean holidayBean = holidayList.get(i);
                        int minusFromDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseTime), holidayBean.getFromDate());
                        int minusEndDate = DateUtil.daysBetweenDates(DateUtil.clearDate(baseTime), holidayBean.getEndDate());
                        if(minusFromDate >= 0 && minusEndDate <= 0){
                            return true;
                        }
                    }
                }
            }
            //判断是否是自定义节假日
            int ifHoliday = this.classMapper.judgeHoliday(classId, DateUtil.clearDate(baseTime));
            if(ifHoliday > 0){
                return true;
            }
            //判断是否休息日
            Integer ifRestDay = this.classMapper.judgeRestDay(classId, DateUtil.getDateWeekNum(baseTime));
            if(ifRestDay != null && ifRestDay == 1){
                return true;
            }
            return false;
        }
        return false;
    }

    //处理结果集
    private Map<String, Object> processResult(List<Map<String, Date>> result){
        Map<String, Object> reMap = new HashMap<String, Object>();
        List<Map<String, Date>> list = new ArrayList<Map<String, Date>>();
        Long minTotal = 0L;
        if(result != null && result.size() > 0){
            for(int i = 0; i < result.size(); i++){
                Map<String, Date> map = result.get(i);
                Date begin = map.get("begin");
                Date end = map.get("end");
                if(DateUtil.getMinBetweenTime(begin, end) != 0){
                    list.add(map);
                    minTotal = minTotal + DateUtil.getMinBetweenTime(begin, end);
                }
            }
        }

        reMap.put("list", list);
        reMap.put("minTotal", minTotal);
        return reMap;
    }

    //找到的时间段和结束时间比
    private boolean dayCompareToEndTime(Date endTime, Date begin, Date end, List<Map<String, Date>> result){
        if(begin.after(endTime)){
            return true;
        }
        else if(end.after(endTime)){
            Map<String, Date> one = new HashMap<String, Date>();
            one.put("begin", begin);
            one.put("end", endTime);
            result.add(one);
            return true;
        }
        else {
            Map<String, Date> one = new HashMap<String, Date>();
            one.put("begin", begin);
            one.put("end", end);
            result.add(one);
            return false;
        }
    }

    //"天"模式下，判断beginTime是否落在某个时间段
    private boolean dayTimeCheck(Date beginTime, String start, String end){
        String begin = DateUtil.convertDateToStr(beginTime, DateUtil.HOUR_MINUTE_PATTERN);
        if(end.compareTo(start) > 0){
            //时间段"未跨天"
            if(begin.compareTo(start) >= 0 && begin.compareTo(end) <= 0){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            //时间段"跨天"
            if(begin.compareTo(end) <= 0 || begin.compareTo(start) >= 0){
                return true;
            }
            else {
                return false;
            }
        }
    }

    @Override
    public Map<String,Object> getDefineClassDetail(Long classId){
        Map<String,Object> result = new HashMap<String, Object>();
        ClassConfigBean classConfig = this.classMapper.getClassConfigById(classId);
        result.put("classConfig", classConfig);
        List<Map<String, Object>> teamList = this.classMapper.getTeamsByClassIdNew(classId);
        result.put("teamList", teamList);
        //基础时段列表
        List<Map<String, Object>> baseTimes = this.classMapper.getBasetimeListByClassId(classId);
        result.put("baseTimes", baseTimes);
        //班组列表
        List<Map<String, Object>> teams = this.classMapper.getTeamListByClassId(classId);
        result.put("teams", teams);
        return result;
    }

    @Override
    public Map<String, Object> insertUpdateDefineClass(Map<String, Object> workingInfo){
        Map<String, Object> result = new HashMap<String, Object>();
        Long classId = Long.valueOf(workingInfo.get("classId").toString());
        Integer restType = Integer.valueOf(workingInfo.get("restType").toString());
        Integer circleType = Integer.valueOf(workingInfo.get("circleType").toString());
        List<Map<String, Object>> timeList = (List<Map<String, Object>>) workingInfo.get("timeList");

        Map<String, Object> classConfig = new HashMap<String, Object>();
        classConfig.put("classId", classId);
        classConfig.put("restType", restType);
        classConfig.put("circleType", circleType);
        this.classMapper.updateClassConfig(classConfig);

        if(timeList != null && timeList.size() > 0){
            this.classMapper.deleteWorkingHourRelation(classId); //删除T_WORKINGHOUR_RELATION
            for(int i = 0; i < timeList.size(); i++){
                Map<String, Object> temp = timeList.get(i);

                Long workHourId = Long.valueOf(temp.get("workHourId").toString());
                Long teamId = Long.valueOf(temp.get("teamId").toString());
                Date onDuty = DateUtil.convertStrToDate(temp.get("onDuty").toString(), DateUtil.MOUDLE_PATTERN);
                Date offDuty = DateUtil.convertStrToDate(temp.get("offDuty").toString(), DateUtil.MOUDLE_PATTERN);
                if(workHourId == 0){
                    workHourId = SequenceUtils.getDBSequence();
                }
                this.classMapper.insertWorkHourRelation(workHourId, teamId, 0, 0, onDuty, offDuty);
            }
        }

        result.put("message", "");
        result.put("classId", classId);
        return result;
    }

}
