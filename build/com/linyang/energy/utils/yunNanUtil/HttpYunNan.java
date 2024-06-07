package com.linyang.energy.utils.yunNanUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.linyang.energy.model.InnerBean;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Date;
import java.util.Map;

import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import sun.misc.BASE64Encoder;

/**
 * Created by Administrator on 20-5-9.
 */
public class HttpYunNan {

    /**
     * requestUrl:请求接口地址;
     * params:请求参数 （可用LinkedHashMap）;
     * ak:企业从云南省平台那获取到的ak (若为空则表示不需要);
     */
    public static ObjectNode sendHttpToYunNan(String requestUrl, Map<String, Object> params, String ak){
        String responseStr = "";

        // 客户端实例化
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(requestUrl);

        try {
            //设置请求头Authorization
            if(null != ak){
                postMethod.setRequestHeader("Authorization", "Bearer " + ak);
            }

            // 设置请求头  Content-Type
            postMethod.setRequestHeader("Content-Type", "application/json");

            byte[] requestBytes = JsonUtil.jsonObj2Sting(params).getBytes("utf-8"); // 将参数转为二进制流
            InputStream inputStream = new ByteArrayInputStream(requestBytes, 0, requestBytes.length);
            RequestEntity requestEntity = new InputStreamRequestEntity(inputStream, requestBytes.length, "application/json; charset=utf-8"); // 请求体
            postMethod.setRequestEntity(requestEntity);

            // 执行请求
            httpClient.executeMethod(postMethod);

            InputStream soapResponseStream = postMethod.getResponseBodyAsStream();// 获取返回的流
            byte[] datas = readInputStream(soapResponseStream);// 从输入流中读取数据

            // 将二进制流转为String
            responseStr = new String(datas, "UTF-8");
        }
        catch (Exception e) {}
        finally {
            // 关闭连接，释放资源
            postMethod.releaseConnection();
            ((SimpleHttpConnectionManager) httpClient.getHttpConnectionManager()).shutdown();
        }

        //返回报文解析
        ObjectNode responseObj = null;
        if(StringUtils.isNotEmpty(responseStr)){
            try {
                responseObj = JsonUtil.convertObject2ObjectNode(responseStr);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return responseObj;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    /**
     * 云南“营业执照”图片转换成Base64字符串（并在开头加入"data:image/png;base64,"）
     */
    public static String convertFileToBase64(String imgPath){
        String imgType = imgPath.substring(imgPath.lastIndexOf(".") + 1);

        byte[] data = null;
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Str = null;

        try {
            // 读取图片字节数组
            InputStream in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            // 对字节数组进行Base64编码，得到Base64编码的字符串
            base64Str = encoder.encode(data);
            base64Str = base64Str.replaceAll("\r\n","");

            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "data:image/" + imgType + ";base64," + base64Str;
    }
    
    /**
     * requestUrl:请求接口地址;
     * params:请求参数 （可用LinkedHashMap）;
     */
    public static String sendHttpToInnerYunNan(String requestUrl, InnerBean params){
        String responseStr = "";

        // 客户端实例化
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(requestUrl);

        try {
        	
            // 设置请求头  Content-Type
            postMethod.setRequestHeader("Content-Type", "application/json");

            byte[] requestBytes = JsonUtil.jsonObj2Sting(params).getBytes("utf-8"); // 将参数转为二进制流
            InputStream inputStream = new ByteArrayInputStream(requestBytes, 0, requestBytes.length);
            RequestEntity requestEntity = new InputStreamRequestEntity(inputStream, requestBytes.length, "application/json; charset=utf-8"); // 请求体
            postMethod.setRequestEntity(requestEntity);

            // 执行请求
            httpClient.executeMethod(postMethod);

            InputStream soapResponseStream = postMethod.getResponseBodyAsStream();// 获取返回的流
            byte[] datas = readInputStream(soapResponseStream);// 从输入流中读取数据

            // 将二进制流转为String
            responseStr = new String(datas, "UTF-8");
        }
        catch (Exception e) {
        	 e.printStackTrace();
        }
        finally {
            // 关闭连接，释放资源
            postMethod.releaseConnection();
            ((SimpleHttpConnectionManager) httpClient.getHttpConnectionManager()).shutdown();
        }
        
        return responseStr;
    }


}
