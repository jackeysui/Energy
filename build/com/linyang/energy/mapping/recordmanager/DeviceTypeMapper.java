package com.linyang.energy.mapping.recordmanager;

import java.util.List;

import com.linyang.energy.model.DeviceTypeBean;

/**
 * @Description 设备集Mapper
 * @author Leegern
 * @date Jan 8, 2014 4:34:52 PM
 */
public interface DeviceTypeMapper {
	
	/**
	 * 查询设备集信息
	 * @param bean 查询参数
	 * @return
	 */
	List<DeviceTypeBean> queryDeviceTypeInfo(DeviceTypeBean bean);
	
}
