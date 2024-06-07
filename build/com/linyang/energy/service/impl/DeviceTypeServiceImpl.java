package com.linyang.energy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.mapping.recordmanager.DeviceTypeMapper;
import com.linyang.energy.model.DeviceTypeBean;
import com.linyang.energy.service.DeviceTypeService;

/**
 * @Description 设备集Service实现类
 * @author Leegern
 * @date Jan 8, 2014 4:36:27 PM
 */
@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {
	@Autowired
	private DeviceTypeMapper deviceTypeMapper;
	
	/*
	 * (non-Javadoc)
	 * @see com.linyang.energy.service.DeviceTypeService#queryDeviceTypeInfo(com.linyang.energy.model.DeviceTypeBean)
	 */
	@Override
	public List<DeviceTypeBean> queryDeviceTypeInfo(DeviceTypeBean bean) {
		return deviceTypeMapper.queryDeviceTypeInfo(bean);
	}
	
	
}
