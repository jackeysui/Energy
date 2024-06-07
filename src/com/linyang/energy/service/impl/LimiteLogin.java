package com.linyang.energy.service.impl;

import com.linyang.energy.model.UserBean;
import com.linyang.energy.service.UserService;
import com.linyang.energy.utils.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class LimiteLogin {

    private static Logger log = Logger.getLogger(LimiteLogin.class);

    private static Map<String, String> loginUserMap = new HashMap<>();//存储在线用户
    @Autowired
    private UserService userService;

    public String loginLimite(HttpServletRequest request, String userName) {
        UserBean user = userService.getUserByUserName(userName);
        String sessionId = request.getSession().getId();
        for (String key : loginUserMap.keySet()) {
            //用户已在另一处登录
            if (key.equals(user.getLoginName()) && !loginUserMap.containsValue(sessionId)) {
                log.info("用户：" + user.getLoginName() + "，于" + DateUtil.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss") + "被剔除！");
                loginUserMap.remove(user.getLoginName());
                break;
            }
        }

        loginUserMap.put(user.getLoginName(), sessionId);
        request.getSession().getServletContext().setAttribute("loginUserMap", loginUserMap);
        return "success";
    }


}
