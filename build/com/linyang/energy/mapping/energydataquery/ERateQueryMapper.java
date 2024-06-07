package com.linyang.energy.mapping.energydataquery;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.linyang.energy.model.MeterBean;
import org.apache.ibatis.annotations.Param;

/**
 * @author guosen
 * @date 2014-11-24
 */
public interface ERateQueryMapper {

    /**
     * 查询正向有功分费率电能示值(用于显示示值)
     * @param meterId
     * @param date
     * @param rate
     * @return
     */
    Double queryFaeRate(@Param("meterId")Long meterId,  @Param("date")Date date, @Param("rate")int rate);

    /**
     * 查询正向有功电能示值(用于计算电量)
     * @param meterId
     * @param date
     * @param rate
     * @return
     */
    Double getViewDayERate(@Param("meterId")Long meterId,  @Param("date")Date date, @Param("rate")int rate);


    /**
     * 查询正向有功总电能示值(用于显示示值)
     * @param meterId
     * @param date
     * @return
     */
    Double queryTotalFaeRate(@Param("meterId")Long meterId,  @Param("date")Date date);

    /**
     * 查询正向有功总电能示值(用于计算电量)
     * @param meterId
     * @param date
     * @return
     */
    Double getViewDayETotal(@Param("meterId")Long meterId,  @Param("date")Date date);


    /**
     * 查询分户下需要计算的DCP列表
     * */
    public List<MeterBean> getComputeMeters(@Param("ledgerId")Long ledgerId);
    
    /**
     * 查询分户下需要计算的DCP列表
     * */
    public List<MeterBean> getComputeMetersWithMeterType(@Param("ledgerId")Long ledgerId,@Param("meterType")Short meterType);

    /**
     * 查询水能示值
     * @param meterId
     * @param date
     * @return
     */
    Double queryEWater(@Param("meterId")Long meterId,  @Param("date")Date date);
    
    /**
     * 查询气能示值
     * @param meterId
     * @param date
     * @return
     */
    Double queryEGas(@Param("meterId")Long meterId,  @Param("date")Date date);
    
    /**
     * 查询热能示值
     * @param meterId
     * @param date
     * @return
     */
    Double queryEHeat(@Param("meterId")Long meterId,  @Param("date")Date date);

    /**
     * 获取t_cur_e_total表数据，用于显示示值
     */
    Double getCurETotal(@Param("meterId")Long meterId,  @Param("date")Date date);

    /**
     * 获取view_cur_e_total表数据，用于计算电量的
     */
    Double getViewCurETotal(@Param("meterId")Long meterId,  @Param("date")Date date);

}
