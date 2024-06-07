package com.linyang.energy.mapping.heatpump;

import com.linyang.energy.model.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16-8-1.
 */
public interface HeatPumpDataMapper {

    /**
     * 得到下拉de企业列表
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> getElecPowerData(Map<String, Object> params);
    
    
    /**
     *获取5分钟热力数据
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> get5MinHeatPumpData(Map<String, Object> params);

    /**
     * 获取1分钟热力数据
     * @param ledgerId
     * @return
     */
    List<Map<String, Object>> get1MinHeatPumpData(Map<String, Object> params);
}
