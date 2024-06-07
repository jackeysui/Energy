package com.linyang.energy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;



import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.linyang.common.web.common.Log;
/**
 * IP和城市相关类
  @description:
  @version:0.1
  @author:James.Wei
  @date:Dec 9, 2014
 */
public class IpAddrUtils {
	private static String getSoapRequest(String ip) {
		StringBuilder sb = new StringBuilder();
		sb
				.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
						+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
						+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
						+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soap:Body>    " 
						+ "<getCountryCityByIp xmlns=\"http://WebXml.com.cn/\">"
						+ "<theIpAddress>" + ip	+ "</theIpAddress>" 
						+ "</getCountryCityByIp>"
						+ "</soap:Body></soap:Envelope>");
		return sb.toString();
	}
	
	/**
	 * 用户把SOAP请求发送给服务器端，并返回服务器点返回的输入流
	 *            用户输入的城市名称
	 * @return 服务器端返回的输入流，供客户端读取
	 * @throws Exception
	 */
	private static InputStream getSoapInputStream(String ip) throws IOException{
		String soap = getSoapRequest(ip);
		if(!checkIp(ip)){return null;}
        if (soap == null) {
            return null;
        }
        URL url = new URL("http://webservice.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx");
        URLConnection conn = url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("SOAPAction","http://WebXml.com.cn/getCountryCityByIp");


        OutputStream os = conn.getOutputStream();
        OutputStreamWriter osw = null;
        InputStream is = null;
		try {osw = new OutputStreamWriter(os, "utf-8");
			osw.write(soap);
			is = conn.getInputStream();
			return is;
		} catch (IOException e) {
			Log.error("IpAddrUtil getSoapInputStream OutputStreamWriter write error!");
			return null;
		}finally {
			CloseHander(osw,is,os);
        }
    }
	
	private static void CloseHander(OutputStreamWriter osw,InputStream is,OutputStream os)
	{
		try
		{
		if(osw != null){osw.close(); osw = null;}   if(is != null){is.close();}   if(os != null){os.close();}
		}catch(IOException ex){
			Log.error("CloseHander error"+ex.getMessage());
		}
	}
	

	/*
	 * 白名单
	 */
	private static boolean checkIp(String ip){
		if(ip!=null&&ip.length()>0){return true;}
		return false;
	}

	/**
	 * 对服务器端返回的XML进行解析
	 * 
	 * @return 字符串 用,分割
	 */
	public static String getIp(String ip) {
        InputStream is = null;
		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
            dbf.setValidating(true);
            //XML解析器配置：防止XML实体扩展注入
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);       // 这是优先选择. 如果不允许DTDs (doctypes) ,几乎可以阻止所有的XML实体攻击
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);     // 是否包含外部生成的实体。当正在解析文档时为只读属性，未解析文档的状态下为读写
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);   // 是否包含外部的参数，包括外部DTD子集。当正在解析文档时为只读属性，未解析文档的状态下为读写
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);

            DocumentBuilder db = dbf.newDocumentBuilder();
            is = getSoapInputStream(ip);
			doc = db.parse(is);
			NodeList nl = doc.getElementsByTagName("string");
			StringBuffer sb = new StringBuffer();
			for (int count = 0; count < nl.getLength(); count++) {
				Node n = nl.item(count);
				if(n.getFirstChild().getNodeValue().equals("查询结果为空！")) {
					sb = new StringBuffer("#") ;
					break ;
				}
				sb.append(n.getFirstChild().getNodeValue() + "#\n");
			}
			return sb.toString();
		} catch (IOException e) {
			Log.info("getIp error IOException");
			return null;
		}catch (ParserConfigurationException e) {
			Log.info("getIp error ParserConfigurationException");
			return null;
		}
		catch (SAXException e) {
			Log.info("getIp error SAXException");
			return null;
		}
		//catch (Exception e) {
		//	Log.info("getIp error Exception");
		//	return null;
		//}
        finally {if(is != null){try {is.close();} catch (IOException e) {Log.error("IPAddreUtil getIp InputStream close error!");}}}
    }
	
	public static String GetCityNameByIP(String ip)
	{
		String addr=getIp(ip);
		String name="";
	    int sindex=	addr.indexOf("省");
	    int eindex=addr.indexOf("市");
	    if(eindex == -1){
	    	return "";
	    }
	    name=addr.substring(sindex+1,eindex);
	   // System.out.println(addr.substring(sindex+1,eindex+1));
	    return name;
	}
	
	/*
	 * 获取客户端IP
	 */
	public static String getIpAddr(HttpServletRequest request) {
		  String ip = request.getHeader("x-forwarded-for");
		  if (ip ==null || "unknown".equalsIgnoreCase(ip)) {
		   ip = request.getHeader("Proxy-Client-IP");
		  }
		  if (ip ==null || "unknown".equalsIgnoreCase(ip)) {
		   ip = request.getHeader("WL-Proxy-Client-IP");
		  }
		  if (ip ==null || "unknown".equalsIgnoreCase(ip)) {
		   ip = request.getRemoteAddr();
		  }
		  return ip;
	}
	
	public static boolean isInnerIP(String ipAddress){    
	       boolean isInnerIp = false;    
	       long ipNum = getIpNum(ipAddress);    
	       /**   
	       私有IP：A类  10.0.0.0-10.255.255.255   
	              B类  172.16.0.0-172.31.255.255   
	              C类  192.168.0.0-192.168.255.255   
	       当然，还有127这个网段是环回地址   
	       **/   
	        long aBegin = getIpNum(WebConstant.IP_A_BEGIN);    
	        long aEnd = getIpNum(WebConstant.IP_A_END);    
	        long bBegin = getIpNum(WebConstant.IP_B_BEGIN);    
	        long bEnd = getIpNum(WebConstant.IP_B_END);    
	        long cBegin = getIpNum(WebConstant.IP_C_BEGIN);    
	        long cEnd = getIpNum(WebConstant.IP_C_END);    
	        isInnerIp = isInner(ipNum,aBegin,aEnd) || isInner(ipNum,bBegin,bEnd) || isInner(ipNum,cBegin,cEnd) || ipAddress.equals(WebConstant.IP_LOCALHOST);    
	        return isInnerIp;               
	 }   
	
	private static long getIpNum(String ipAddress) {    
		   String [] ip = ipAddress.split("\\.");    
		   long a = Integer.parseInt(ip[0]);    
		   long b = Integer.parseInt(ip[1]);    
		   long c = Integer.parseInt(ip[2]);    
		   long d = Integer.parseInt(ip[3]);    
		  
		   long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;    
		   return ipNum;    
		 }  
	
	private static boolean isInner(long userIp,long begin,long end){    
	     return (userIp>=begin) && (userIp<=end);    
	 }
	
	/*
	 * 获取本机外网IP
	 */
	public static String GetPublicIP() throws IOException
	{
        String ip="";
        URL url = new URL("http://www.ip138.com/ip2city.asp");
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.15) Gecko/20110303 Firefox/3.6.15");
        conn.setRequestProperty("Content-Type", "text/html");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        InputStream is = conn.getInputStream();
        BufferedReader br = null;
        //判断流长度
        if(is.available()>2000000) {
        	throw new IOException();
        }
        String line = null;
        try{br = new BufferedReader(new InputStreamReader(is, "GB2312"));
            while ((line = br.readLine()) != null) {
                if (line.contains("您的IP地址是")) {
                    int start = line.indexOf('[') + 1;  int end = line.indexOf(']');  ip=line.substring(start, end);
                }
            }
            return ip;
        }catch (IOException e){
            return ip;
        }
        finally {
            if(br != null){
                try {br.close();  br = null;
                }
                catch (IOException e) {
                    Log.error("GetPublicIP --  ios error!");
                }
            }
            if(is != null){
                try {is.close();
                }
                catch (IOException e) {
                    Log.error("GetPublicIP --  ios error!");
                }
            }
        }
    }
}
