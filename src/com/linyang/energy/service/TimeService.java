package com.linyang.energy.service;

import java.util.Map;

/**
 *
 * @author xs
 */
public interface TimeService {
        
        /**
	 * 
	 * 函数功能说明  :保存分户休息时间设置
	 * @return         
	 * @throws
	 */
		public boolean saveRestTimeSetting(Long ledgerId, Map<String, Object> settingInfo);
        
           /**
	 * 
	 * 函数功能说明  :获取分户休息时间设置
	 * @return         
	 * @throws
	 */
		public Map<String, Object> getRestTimeSetting(Long ledgerId);
        
    /**
	 * 
	 * 函数功能说明  :job使用，及时更新法定节假日
	 * @return         
	 * @throws
	 */
		public boolean updateHolidaysByOfficalSetting();


    /**
     *  获取班制休息时间设置
     */
    public Map<String, Object> getClassRestSetting(Long classId);

    /**
     *  保存班制休息时间设置
     */
    public boolean saveClassRestSetting(Long classId, Map<String, Object> settingInfo);

}
