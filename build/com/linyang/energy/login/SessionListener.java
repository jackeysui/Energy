package com.linyang.energy.login;

import com.linyang.energy.model.UserBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

public class SessionListener implements HttpSessionListener {

    private static Logger log = Logger.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent event) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        String sessionId = session.getId();
        //在session销毁的时候,把loginUserMap中保存的键值对清除
        UserBean user = (UserBean) session.getAttribute("now_user");
        if (user != null) {
            Map<String, String> loginUserMap = (Map<String, String>) event.getSession().getServletContext().getAttribute("loginUserMap");
            if(loginUserMap.get(user.getLoginName()).equals(sessionId)){
                log.info("clean user from application : " + user.getLoginName());
                loginUserMap.remove(user.getLoginName());
                event.getSession().getServletContext().setAttribute("loginUserMap", loginUserMap);
            }
        }

    }

}