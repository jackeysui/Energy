/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.linyang.energy.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.ClassHolidayBean;
import com.linyang.energy.model.ClassRestDayBean;
import com.linyang.energy.model.ClassTimePeriodBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.dto.HolidayBean;
import com.linyang.energy.dto.TimePeriodBean;
import com.linyang.energy.mapping.demanddeclare.DemandDeclareMapper;
import com.linyang.energy.mapping.timeManager.RestTimeMapper;
import com.linyang.energy.model.LedgerRestDayBean;
import com.linyang.energy.service.TimeService;
import com.linyang.energy.utils.DateUtil;
import com.linyang.energy.utils.OfficialHolidayUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;


/**
 * 
 * @author xs
 */
@Service
public class TimeServiceImpl implements TimeService {

	@Autowired
	private RestTimeMapper restTimeMapper;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Override
	public boolean saveRestTimeSetting(Long ledgerId,Map<String, Object> settingInfo) {
		SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		LedgerRestDayBean restDayBean = new LedgerRestDayBean();
		restDayBean.setLedgerId(ledgerId);
		String restDayStr = (String) settingInfo.get("restDay");
		if (restDayStr != null && !restDayStr.equalsIgnoreCase("")) {
			String[] restDay = restDayStr.split(",");
			for (String thisDay : restDay) {
				if ("1".equals(thisDay)) {
					restDayBean.setMonday(1);
				} else if ("2".equals(thisDay)) {
					restDayBean.setTuesday(1);
				} else if ("3".equals(thisDay)) {
					restDayBean.setWednesday(1);
				} else if ("4".equals(thisDay)) {
					restDayBean.setThursday(1);
				} else if ("5".equals(thisDay)) {
					restDayBean.setFriday(1);
				} else if ("6".equals(thisDay)) {
					restDayBean.setSaturday(1);
				} else if ("7".equals(thisDay)) {
					restDayBean.setSunday(1);
				} else if ("8".equals(thisDay)) {
					restDayBean.setIncludeDefaultHoliday(1);
				} else if ("9".equals(thisDay)) {
					restDayBean.setIncludeCustomHoliday(1);
				}
			}
		}
		boolean isSuccess = true;
		try {
			RestTimeMapper restTimeMapper = session.getMapper(RestTimeMapper.class);
			restTimeMapper.deleteLedgerRestDay(restDayBean.getLedgerId());
			restTimeMapper.insertLedgerRestDay(restDayBean);

			restTimeMapper.deleteLedgerHolidaySetting(restDayBean.getLedgerId());
			if (restDayBean.getIncludeCustomHoliday() == 1) {
				List<Map<String, Object>> holidayMaps = (List<Map<String, Object>>) settingInfo
						.get("holidayInfo");
				if(holidayMaps != null){
					for (Map<String, Object> map : holidayMaps) {
						HolidayBean hBean = new HolidayBean();
						hBean.setLedgerId(restDayBean.getLedgerId());
						hBean.setName(map.get("name").toString());
						int year = DateUtil.getYear(new Date());
						hBean.setFromDate(DateUtil.convertStrToDate(year + "-"
								+ map.get("fromDate").toString(),
								DateUtil.SHORT_PATTERN));
						hBean.setEndDate(DateUtil.convertStrToDate(year + "-"
								+ map.get("endDate").toString(),
								DateUtil.SHORT_PATTERN));
						restTimeMapper.insertLedgerHolidaySetting(hBean);
					}
				}
			}
			restTimeMapper.deleteLedgerWorkdayRestTimeSetting(restDayBean
					.getLedgerId());
			List<Map<String, Object>> timeMaps = (List<Map<String, Object>>) settingInfo
					.get("timeInfo");
			if (timeMaps != null) {
				for (Map<String, Object> map : timeMaps) {
					TimePeriodBean tBean = new TimePeriodBean();
					tBean.setLedgerId(restDayBean.getLedgerId());

					tBean.setBeginTime(DateUtil.convertStrToDate(map.get(
					"beginTime").toString(), "HH:mm"));
					tBean.setEndTime(DateUtil.convertStrToDate(map.get("endTime")
							.toString(), "HH:mm"));
					restTimeMapper.insertLedgerWorkdayRestTimeSetting(tBean);
				}
			}
		} catch (ClassCastException e) {
			session.rollback();
			isSuccess = false;
		}finally{
			session.commit();
			session.close();
		}
		return isSuccess;
	}

    @Override
    public boolean saveClassRestSetting(Long classId, Map<String, Object> settingInfo){
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
        ClassRestDayBean restDayBean = new ClassRestDayBean();
        restDayBean.setClassId(classId);
        String restDayStr = (String) settingInfo.get("restDay");
        if (restDayStr != null && !restDayStr.equalsIgnoreCase("")) {
            String[] restDay = restDayStr.split(",");
            for (String thisDay : restDay) {
                if ("1".equals(thisDay)) {
                    restDayBean.setMonday(1);
                } else if ("2".equals(thisDay)) {
                    restDayBean.setTuesday(1);
                } else if ("3".equals(thisDay)) {
                    restDayBean.setWednesday(1);
                } else if ("4".equals(thisDay)) {
                    restDayBean.setThursday(1);
                } else if ("5".equals(thisDay)) {
                    restDayBean.setFriday(1);
                } else if ("6".equals(thisDay)) {
                    restDayBean.setSaturday(1);
                } else if ("7".equals(thisDay)) {
                    restDayBean.setSunday(1);
                } else if ("8".equals(thisDay)) {
                    restDayBean.setIncludeDefaultHoliday(1);
                } else if ("9".equals(thisDay)) {
                    restDayBean.setIncludeCustomHoliday(1);
                }
            }
        }
        boolean isSuccess = true;
        try {
            RestTimeMapper restTimeMapper = session.getMapper(RestTimeMapper.class);
            restTimeMapper.deleteClassRestDay(restDayBean.getClassId());
            restTimeMapper.insertClassRestDay(restDayBean);

            restTimeMapper.deleteClassHolidaySetting(restDayBean.getClassId());
            if (restDayBean.getIncludeCustomHoliday() == 1) {
                List<Map<String, Object>> holidayMaps = (List<Map<String, Object>>) settingInfo
                        .get("holidayInfo");
                if(holidayMaps != null){
                    for (Map<String, Object> map : holidayMaps) {
                        ClassHolidayBean hBean = new ClassHolidayBean();
                        hBean.setClassId(restDayBean.getClassId());
                        hBean.setName(map.get("name").toString());
                        int year = DateUtil.getYear(new Date());
                        hBean.setFromDate(DateUtil.convertStrToDate(year + "-"
                                        + map.get("fromDate").toString(),
                                DateUtil.SHORT_PATTERN));
                        hBean.setEndDate(DateUtil.convertStrToDate(year + "-"
                                        + map.get("endDate").toString(),
                                DateUtil.SHORT_PATTERN));
                        restTimeMapper.insertClassHolidaySetting(hBean);
                    }
                }
            }
            restTimeMapper.deleteClassWorkdayRestTimeSetting(restDayBean.getClassId());
            List<Map<String, Object>> timeMaps = (List<Map<String, Object>>) settingInfo.get("timeInfo");
            if (timeMaps != null) {
                for (Map<String, Object> map : timeMaps) {
                    ClassTimePeriodBean tBean = new ClassTimePeriodBean();
                    tBean.setClassId(restDayBean.getClassId());

                    tBean.setBeginTime(DateUtil.convertStrToDate(map.get(
                            "beginTime").toString(), "HH:mm"));
                    tBean.setEndTime(DateUtil.convertStrToDate(map.get("endTime")
                            .toString(), "HH:mm"));
                    restTimeMapper.insertClassWorkdayRestTimeSetting(tBean);
                }
            }
        } catch (ClassCastException e) {
            session.rollback();
            isSuccess = false;
        }finally{
            session.commit();
            session.close();
        }
        return isSuccess;
    }

	@Override
	public Map<String, Object> getRestTimeSetting(Long ledgerId) {
        Map<String, Object> settingInfo = new HashMap<String, Object>();
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        settingInfo.put("year", year);
		LedgerRestDayBean restDayBean = restTimeMapper.getLedgerRestDayByLedgerId(ledgerId);
		if (restDayBean != null) {
            List<Integer> restDayList = new ArrayList<Integer>();
            restDayList.add(restDayBean.getMonday());
            restDayList.add(restDayBean.getTuesday());
            restDayList.add(restDayBean.getWednesday());
            restDayList.add(restDayBean.getThursday());
            restDayList.add(restDayBean.getFriday());
            restDayList.add(restDayBean.getSaturday());
            restDayList.add(restDayBean.getSunday());
            restDayList.add(restDayBean.getIncludeDefaultHoliday());
            restDayList.add(restDayBean.getIncludeCustomHoliday());
            String restDayStr = StringUtils.join(restDayList, ",");
            settingInfo.put("restDay", restDayStr);
		}
        
        List<HolidayBean> hBeanList = restTimeMapper.getLedgerHolidaySettingByLedgerId(ledgerId);
        if (hBeanList != null && !hBeanList.isEmpty()) {
            List<Map<String, Object>> holidayMaps = new ArrayList<Map<String, Object>>();
            for (HolidayBean hBean : hBeanList) {
                Map<String, Object> holidayMap = new HashMap<String, Object>();
                holidayMap.put("name", hBean.getName());
                holidayMap.put("fromDate", DateUtil.convertDateToStr(hBean.getFromDate(), DateUtil.SHORT_PATTERN));
                holidayMap.put("endDate", DateUtil.convertDateToStr(hBean.getEndDate(),DateUtil.SHORT_PATTERN));
                holidayMaps.add(holidayMap);
            }
            settingInfo.put("holidayInfo", holidayMaps);
        }
        
        List<TimePeriodBean> tpBeanList = restTimeMapper.getLedgerWorkdayRestTimeByLedgerId(ledgerId);
        if (tpBeanList != null && !tpBeanList.isEmpty()) {
            List<Map<String, Object>> timeMaps = new ArrayList<Map<String, Object>>();
            for (TimePeriodBean tpBean : tpBeanList) {
                Map<String, Object> timeMap = new HashMap<String, Object>();
                timeMap.put("beginTime", DateUtil.convertDateToStr(tpBean.getBeginTime(), "HH:mm"));
                timeMap.put("endTime", DateUtil.convertDateToStr(tpBean.getEndTime(),"HH:mm"));
                timeMaps.add(timeMap);
            }
            settingInfo.put("timeInfo", timeMaps);
        }
        return settingInfo;
	}

    @Override
    public Map<String, Object> getClassRestSetting(Long classId){
        Map<String, Object> settingInfo = new HashMap<String, Object>();
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        settingInfo.put("year", year);
        ClassRestDayBean restDayBean = restTimeMapper.getClassRestDayById(classId);
        if (restDayBean != null) {
            List<Integer> restDayList = new ArrayList<Integer>();
            restDayList.add(restDayBean.getMonday());
            restDayList.add(restDayBean.getTuesday());
            restDayList.add(restDayBean.getWednesday());
            restDayList.add(restDayBean.getThursday());
            restDayList.add(restDayBean.getFriday());
            restDayList.add(restDayBean.getSaturday());
            restDayList.add(restDayBean.getSunday());
            restDayList.add(restDayBean.getIncludeDefaultHoliday());
            restDayList.add(restDayBean.getIncludeCustomHoliday());
            String restDayStr = StringUtils.join(restDayList, ",");
            settingInfo.put("restDay", restDayStr);
        }

        List<ClassHolidayBean> hBeanList = restTimeMapper.getClassHolidaySettingById(classId);
        if (hBeanList != null && !hBeanList.isEmpty()) {
            List<Map<String, Object>> holidayMaps = new ArrayList<Map<String, Object>>();
            for (ClassHolidayBean hBean : hBeanList) {
                Map<String, Object> holidayMap = new HashMap<String, Object>();
                holidayMap.put("name", hBean.getName());
                holidayMap.put("fromDate", DateUtil.convertDateToStr(hBean.getFromDate(), DateUtil.SHORT_PATTERN));
                holidayMap.put("endDate", DateUtil.convertDateToStr(hBean.getEndDate(),DateUtil.SHORT_PATTERN));
                holidayMaps.add(holidayMap);
            }
            settingInfo.put("holidayInfo", holidayMaps);
        }

        List<ClassTimePeriodBean> tpBeanList = restTimeMapper.getClassWorkdayRestTimeById(classId);
        if (tpBeanList != null && !tpBeanList.isEmpty()) {
            List<Map<String, Object>> timeMaps = new ArrayList<Map<String, Object>>();
            for (ClassTimePeriodBean tpBean : tpBeanList) {
                Map<String, Object> timeMap = new HashMap<String, Object>();
                timeMap.put("beginTime", DateUtil.convertDateToStr(tpBean.getBeginTime(), "HH:mm"));
                timeMap.put("endTime", DateUtil.convertDateToStr(tpBean.getEndTime(),"HH:mm"));
                timeMaps.add(timeMap);
            }
            settingInfo.put("timeInfo", timeMaps);
        }
        return settingInfo;
    }
    
    @Override
    public boolean updateHolidaysByOfficalSetting(){
        List<HolidayBean> holidayList = OfficialHolidayUtils.readOfficialHolidayList();
        for(HolidayBean hBean : holidayList){
            
                restTimeMapper.updateLedgerHolidaySetting(hBean);
                restTimeMapper.updateClassHolidaySetting(hBean);
            
        }
        return true;
    }
}
