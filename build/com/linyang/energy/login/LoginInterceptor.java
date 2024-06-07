package com.linyang.energy.login;

import com.linyang.energy.model.UserBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        UserBean user = (UserBean) session.getAttribute("now_user");
        if (session.getAttribute("now_user") == null) {
            request.getSession().setAttribute("loginPath", getStrParam(request, "loginPath", "login"));
        }

        //多用户登录限制判断,并给出提示信息
        boolean isLogin = false;
        if (user != null) {
            Map<String, String> loginUserMap = (Map<String, String>) session.getServletContext().getAttribute("loginUserMap");
            String sessionId = session.getId();
            for (String key : loginUserMap.keySet()) {
                //用户已在另一处登录
                if (key.equals(user.getLoginName()) && !loginUserMap.containsValue(sessionId)) {
                    isLogin = true;
                    break;
                }
            }
        }
        if (isLogin) {
            // session注销,重定向到登录页
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/index/showIndexPage.htm");
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    /**
     * 从request中获得类型为字符串的参数值
     * @param param_name 参数名
     * @param default_value 参数值为NULL时的默认值
     * @return 用法如: String endTime = getStrParam("endTime","");
     */
    public String getStrParam(HttpServletRequest request,String param_name,String default_value){
        String paramName = request.getParameter(param_name); if (paramName != null && paramName.trim().length()>0) return paramName; else return default_value;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}