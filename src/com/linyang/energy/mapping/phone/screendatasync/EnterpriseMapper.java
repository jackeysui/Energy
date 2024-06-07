package com.linyang.energy.mapping.phone.screendatasync;

import com.linyang.energy.model.modelscreen.ConsDayEnergy;
import com.linyang.energy.model.modelscreen.ConsMonthAmt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnterpriseMapper {


    /**
     * 查询用户月电量数据
     * @param consNo
     * @param dataDate
     * @return
     */
    ConsMonthAmt consMonthAmt(@Param("consNo") String consNo, @Param("dataDate") String dataDate);

    /**
     * 查询用户日用电趋势
     *
     * @param consNo
     * @param begDate
     * @param endDate
     * @return
     */
    List<ConsDayEnergy> consDayEnergy3(@Param("consNo") String consNo, @Param("begDate") String begDate, @Param("endDate") String endDate);

    // 查询用户月用电
    List<ConsDayEnergy> consMonEnergy(@Param("consNo") String consNo, @Param("monDate") String monDate);

}
