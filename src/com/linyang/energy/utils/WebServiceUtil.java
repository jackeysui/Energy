package com.linyang.energy.utils;

import com.linyang.ws.service.DataCollectionServiceService;
import com.linyang.ws.service.DataCollectionWebService;

public class WebServiceUtil {
	
	private static DataCollectionServiceService service;
	
	private static DataCollectionWebService webServie;
	
	private static WebServiceUtil webServiceUtil;
	
	private WebServiceUtil(){
		initWebService();
	}

	private void initWebService() {
		
			service = new DataCollectionServiceService();
			webServie = service.getDataCollectionServicePort();
		
	} 
	
	public static WebServiceUtil getInstance() {
		if (webServiceUtil == null){
			webServiceUtil = new WebServiceUtil();
		}
		return webServiceUtil;
	}
	
	public DataCollectionWebService getWebService(){
		if(webServie == null){
			initWebService();
		}
		return webServie;
	}

}
