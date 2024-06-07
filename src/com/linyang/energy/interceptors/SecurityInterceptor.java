package com.linyang.energy.interceptors;

import java.io.File;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.linyang.energy.dto.BaseUserBean;
import com.linyang.energy.utils.WebConstant;
import com.linyang.util.CommonMethod;


/**
 * 权限拦截器
  @description:
  @version:0.1
  @author:Cherry
  @date:Nov 29, 2013
 */
public class SecurityInterceptor implements HandlerInterceptor {

	private Set<String> excludeUrls;// 不需要拦截的资源
	public Set<String> getExcludeUrls() {
		return excludeUrls;
	}
	public void setExcludeUrls(Set<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	/**
	 * 完成页面的render后调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {

	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
		request.setAttribute("SYSTEM_STARUP_TIME", WebConstant.SYSTEM_STARUP_TIME);
	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		
		if (getExcludeUrls().contains(url) || !url.contains(".htm")) {// 如果要访问的资源是不需要验证的
			return true;
		}
		
		//Referer拦截
		String referer = request.getHeader("Referer");
		StringBuilder buffer = new StringBuilder();
		buffer.append(request.getScheme()).append("://").append(request.getServerName());
		if (referer == null || referer.length()==0 || referer.lastIndexOf(buffer.toString()) != 0)
			return false;
		
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("sessionInfo") == null || CommonMethod.isEmpty(((BaseUserBean)session.getAttribute("sessionInfo")).getUserId())){
			String requestedWith = request.getHeader("x-requested-with");
			String type = request.getContentType();
			// 如果是ajax请求，返回js代码
			if (requestedWith != null&& ("XMLHttpRequest".equals(requestedWith) || (null != type && type.indexOf("application/x-www-form-urlencoded") != -1))) {
				response.getWriter().print("sessionTimeout");
				return false;
			} else {
				// 如果session为空则重定向到login.jsp页面
				// 根据cookie内存储路径返回不同页面
				String loginPath = "login";
                if(request.getCookies() != null && request.getCookies().length > 0){
                    for (Cookie cookie : request.getCookies()) {
                        if("loginPath".equals(cookie.getName()))
                            loginPath = cookie.getValue();
                    }
                }

                String addUrl = "";
                if("neuter_login".equals(loginPath)){
                    addUrl = File.separator + "neuter";
                }
                else if("heat_login".equals(loginPath)){
                    addUrl = File.separator + "Heating";
                }
                else if("dlfl_login".equals(loginPath)){
                    addUrl = File.separator + "dlfl";
                }
                else if("zdny_login".equals(loginPath)){
                    addUrl = File.separator + "zdny";
                }
				response.sendRedirect(request.getContextPath() + addUrl);
				return false;
			}
		}
		/*BaseUserBean sessionInfo = (BaseUserBean) session.getAttribute("sessionInfo");
		if (sessionInfo == null || sessionInfo.getUserId().equalsIgnoreCase("")) {// 如果没有登录或登录超时
			request.setAttribute("msg", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
			request.getRequestDispatcher("/error/noSession.jsp").forward(request, response);
			return false;
		}*/
		return true;
	}
}
