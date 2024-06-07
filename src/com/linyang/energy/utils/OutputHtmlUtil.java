package com.linyang.energy.utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;import java.io.IOException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import com.linyang.common.web.common.Log;

/**
 * 生成HTML文件
 * 
 * @author gaofeng
 * 
 */
public class OutputHtmlUtil {

	public static void createHtmlFile(String filePath, String title, String content) {
        BufferedWriter bw = null;   FileOutputStream cw = null;  OutputStreamWriter dw = null;
		try {
			cw = new FileOutputStream(filePath); dw = new OutputStreamWriter(cw, "UTF-8");  bw = new BufferedWriter(dw);
			StringBuffer strHtml = new StringBuffer("");
			strHtml.append("<!DOCTYPE>\r\n");
			strHtml.append("<html>\r\n");
			strHtml.append("<head>\r\n");
			strHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
			strHtml.append("<meta name=\"viewport\" content=\"height=device-height,width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />\r\n");
			strHtml.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />\r\n");
			strHtml.append("<title></title>\r\n");
			strHtml.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
			strHtml.append("<body><p class=\"list_title\"><font><b style=\"font-size:24px;\">\r\n");
			strHtml.append(title).append("</b></font><br/><b style=\"font-size:14px; font-weight:100\">").append(DateUtil.convertDateToStr(new Date(), DateUtil.DEFAULT_PATTERN));
			strHtml.append("</b></p><br/><br/>\r\n");
			strHtml.append("<p class=\"list_page\">\r\n");
			strHtml.append(content);
			strHtml.append("</p></body></html>\r\n");

			bw.write(strHtml.toString());
		} catch (IOException e) {
			Log.error("OutputHtmlUitl -- create html file error!"); 
		}finally {
            if(bw != null){try {bw.close();} catch (IOException e) {Log.error("OutputHtmlUtil createHtmlFile IO error!");}}
            if(cw != null){try {cw.close();} catch (IOException e) {Log.error("OutputHtmlUtil createHtmlFile IO error!");}}
            if(dw != null){try {dw.close();} catch (IOException e) {Log.error("OutputHtmlUtil createHtmlFile IO error!");}}
        }
    }

}
