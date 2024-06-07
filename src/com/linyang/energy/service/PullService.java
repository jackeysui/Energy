package com.linyang.energy.service;

import com.linyang.energy.model.PullResultBean;
import com.linyang.ws.pullwebService.ResultBean;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ Author     ：catkins.
 * @ Date       ：Created in 14:27 2019/11/27
 * @ Description：拉合闸
 * @ Modified By：:catkins.
 * @Version: $version$
 */
public interface PullService {
	
	/**
	 * 拉合闸发送
	 * @author catkins.
	 * @param param
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @exception
	 * @date 2019/12/6 14:17
	 */
	public Map<String,Object> pullTheSwicth(Map<String,Object> param,HttpServletRequest request);


}
