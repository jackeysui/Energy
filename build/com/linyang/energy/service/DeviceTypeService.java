package com.linyang.energy.service;

import java.util.List;

import com.linyang.energy.model.DeviceTypeBean;

/**
 * @Description 设备集Service
 * @author Leegern
 * @date Jan 8, 2014 4:35:40 PM
 */
public interface DeviceTypeService {
	
	/**
	 * 查询设备集信息
	 * @param bean 查询参数
	 * @return
	 */
	List<DeviceTypeBean> queryDeviceTypeInfo(DeviceTypeBean bean);
	
}
