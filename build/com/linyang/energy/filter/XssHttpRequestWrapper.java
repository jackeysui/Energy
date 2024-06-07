package com.linyang.energy.filter;

import com.alibaba.fastjson.JSON;
import com.linyang.energy.utils.XssStrUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class XssHttpRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(!StringUtils.isEmpty(value)){
            value = XssStrUtils.replaceHtmlCode(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values!=null){
            for(int i=0;i<values.length;i++){
                String value = values[i];
                if(!StringUtils.isEmpty(value)){
                    value = XssStrUtils.replaceHtmlCode(value);
                }
                values[i]=value;
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameters = super.getParameterMap();
        Map<String, String[]> map = new LinkedHashMap<>();
        if(parameters !=null){
            for(String key : parameters.keySet()){
                String[] values = parameters.get(key);
                for(int i=0;i<values.length;i++){
                    String value = values[i];
                    if(!StringUtils.isEmpty(value)){
                        value = XssStrUtils.replaceHtmlCode(value);
                    }
                    values[i]=value;
                }
                map.put(key,values);
            }
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if(!StringUtils.isEmpty(value)){
            value = XssStrUtils.replaceHtmlCode(value);
        }
        return value;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        InputStream in = super.getInputStream();
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader buffer = new BufferedReader(reader);
        StringBuilder body = new StringBuilder();
        String line = buffer.readLine();
        while (line !=null){
            body.append(line);
            line = buffer.readLine();
        }
        buffer.close();
        reader.close();
        in.close();
        Map<String,Object> map = JSON.parseObject(body.toString());;
        Map<String,Object> result = new LinkedHashMap<>();
        for(String key : map.keySet()){
            Object val = map.get(key);
            if(val instanceof String){
                if(!StringUtils.isEmpty(val.toString())){
                    result.put(key,XssStrUtils.replaceHtmlCode(val.toString()));
                }
            }else {
                result.put(key,val);
            }
        }

        String json = JSON.toJSONString(result);
        final ByteArrayInputStream bain = new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bain.read();
            }
        };
    }
}



