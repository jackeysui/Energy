/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.linyang.energy.mapping.timeManager;

import java.util.List;

import com.linyang.energy.dto.HolidayBean;
import com.linyang.energy.dto.TimePeriodBean;
import com.linyang.energy.model.ClassHolidayBean;
import com.linyang.energy.model.ClassRestDayBean;
import com.linyang.energy.model.ClassTimePeriodBean;
import com.linyang.energy.model.LedgerRestDayBean;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author xs
 */
public interface RestTimeMapper {
	/**
	 * 删除用户休息日设置
	 * 
	 * @param ledgerId
	 */
	int deleteLedgerRestDay(@Param("ledgerId") long ledgerId);

	/**
	 * 保存用户休息日设置
	 * 
	 * @param LedgerRestDayBean
	 */
	int insertLedgerRestDay(LedgerRestDayBean restDayBean);

	/**
	 * 获取用户休息日设置
	 * 
	 * @param ledgerId
	 */
	LedgerRestDayBean getLedgerRestDayByLedgerId(@Param("ledgerId") long ledgerId);

	/**
	 * 删除用户节假日设置
	 * 
	 * @param ledgerId
	 */
	int deleteLedgerHolidaySetting(@Param("ledgerId") long ledgerId);

	/**
	 * 保存用户节假日设置
	 * 
	 * @param HolidayBean
	 */
	int insertLedgerHolidaySetting(HolidayBean hBean);
    
    /**
	 * 更新用户节假日设置
	 * 
	 * @param
	 */
	int updateLedgerHolidaySetting(HolidayBean hBean);

	int updateClassHolidaySetting(HolidayBean hBean);

    	/**
	 * 获取用户节假日设置
	 * 
	 * @param ledgerId
	 */
	List<HolidayBean> getLedgerHolidaySettingByLedgerId(@Param("ledgerId") long ledgerId);

	/**
	 * 删除用户工作日休息时段设置
	 * 
	 * @param ledgerId
	 */
	int deleteLedgerWorkdayRestTimeSetting(long ledgerId);
    
    /**
	 * 保存用户工作日休息时段设置
	 * 
	 * @param TimePeriodBean
	 */
	int insertLedgerWorkdayRestTimeSetting(TimePeriodBean tBean);
	
	/**
	 * 获取用户工作日休息时段设置
	 * 
	 * @param ledgerId
	 */
	List<TimePeriodBean> getLedgerWorkdayRestTimeByLedgerId(@Param("ledgerId") long ledgerId);

    /**
     * 获取班制休息日设置
     */
    ClassRestDayBean getClassRestDayById(@Param("classId") long classId);

    /**
     * 获取班制节假日设置
     */
    List<ClassHolidayBean> getClassHolidaySettingById(@Param("classId") long classId);

    /**
     * 获取班制工作日休息时段设置
     */
    List<ClassTimePeriodBean> getClassWorkdayRestTimeById(@Param("classId") long classId);

    /**
     * 删除班制休息日设置
     */
    int deleteClassRestDay(@Param("classId") long classId);

    /**
     * 保存班制休息日设置
     */
    int insertClassRestDay(ClassRestDayBean restDayBean);

    /**
     * 删除班制节假日设置
     */
    int deleteClassHolidaySetting(@Param("classId") long classId);

    /**
     * 保存班制节假日设置
     */
    int insertClassHolidaySetting(ClassHolidayBean hBean);

    /**
     * 删除班制工作日休息时段设置
     */
    int deleteClassWorkdayRestTimeSetting(long classId);

    /**
     * 保存班制工作日休息时段设置
     */
    int insertClassWorkdayRestTimeSetting(ClassTimePeriodBean tBean);

}
