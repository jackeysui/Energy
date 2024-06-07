package com.linyang.energy.controller.recordmanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linyang.energy.controller.BaseController;
import com.linyang.energy.model.DeviceTypeBean;
import com.linyang.energy.service.DeviceTypeService;

/**
 * @Description 设备集Controller
 * @author Leegern
 * @date Jan 8, 2014 4:34:06 PM
 */
@Controller
@RequestMapping("/devicetype")
public class DeviceTypeController extends BaseController {
	@Autowired
	private DeviceTypeService deviceTypeService;
	
	
	/**
	 * 查询设备集信息
	 * @return
	 */
	@RequestMapping(value = "/queryDeviceTypeInfo", method = RequestMethod.POST)
	public @ResponseBody List<DeviceTypeBean> queryDeviceTypeInfo(){
		DeviceTypeBean bean = new DeviceTypeBean();
		bean.setParentTypeId(1);
		return deviceTypeService.queryDeviceTypeInfo(bean);
	}
	
}
