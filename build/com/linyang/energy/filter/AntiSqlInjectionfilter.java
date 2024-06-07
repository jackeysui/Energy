package com.linyang.energy.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Created by Administrator on 18-3-22.
 */
public class AntiSqlInjectionfilter implements Filter {
    public void destroy() {
        // TODO Auto-generated method stub
    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }

    public void doFilter(ServletRequest args0, ServletResponse args1, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)args0;
        String requestURI = req.getRequestURI();
        if(!requestURI.contains("anShanMessage/toWord.htm") && !requestURI.contains("message/toWord.htm") && !requestURI.contains("logController/getLogPageData.htm")
                && !requestURI.contains("anShanMessage/compressionImg.htm")){   //这些请求业务需要，不能过滤
            //获得所有请求参数名
            Enumeration params = req.getParameterNames();
            while (params.hasMoreElements()) {
                //得到参数名
                String name = params.nextElement().toString();
                //得到参数对应值
                String[] value = req.getParameterValues(name);
                if(null!=value){
	                for (int i = 0; i < value.length; i++) {
	                    if(sqlValidate(value[i])){        //有sql关键字
	                        throw new IOException("您发送请求中的参数中含有非法字符");
	                    }
	                }
                }
            }
        }

        // 加密会话（SSL）Cookie 中缺少 Secure 属性
        HttpServletResponse response = (HttpServletResponse) args1;
        String scheme = req.getScheme();
//        LogFactory.info("====> schema is: " + scheme);
        if(!StringUtils.isEmpty(scheme) && "HTTPS".equalsIgnoreCase(scheme)) {
            response.setHeader("Set-Cookie", "JSESSIONID=" + req.getSession().getId() + "; Path=/;Secure=true;");
        }

        // 链接注入和脚本注入的校验
        XssHttpRequestWrapper requestWrapper = new XssHttpRequestWrapper(req);

        //登录接口参数XSS过滤
        if(requestURI.contains("frameController/showLoginPage.htm")){
            //获得所有请求参数名
            Enumeration params = req.getParameterNames();
            while (params.hasMoreElements()) {
                //得到参数名
                String name = params.nextElement().toString();
                //得到参数对应值
                String value = req.getParameterValues(name)[0];
                if("loginPath".equals(name) && !Arrays.asList("login","neuter_login","heat_login","dlfl_login","zdny_login").contains(value)){
                    throw new IOException("您发送请求中的参数中含有非法字符");
                }
            }
        }

        chain.doFilter(requestWrapper,args1);
    }

    //效验
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "exec |execute |insert |select |delete |update |count(|drop |table |group_concat|column_name|--|+|;";  //过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }
}
