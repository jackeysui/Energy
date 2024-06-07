package com.linyang.energy.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.codehaus.jackson.type.TypeReference;

import com.linyang.common.web.common.Log;
import com.linyang.energy.dto.ChartNameBean;
import com.linyang.util.CommonMethod;
import com.linyang.util.JacksonUtils;

public class ChartNameUtils {
	private static ChartNameBean bean;
	static{

        String filePath = CommonMethod.getAbsoluteFilePath(ChartNameUtils.class.getResource("/")+ "com/linyang/energy/xml/ChartName.json"); 
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(filePath)); 
            bean = JacksonUtils.getInstance().readFromFile(fileInputStream, new TypeReference<ChartNameBean>(){});
        } catch (FileNotFoundException e) {Log.info("ChartNameBean error FileNotFoundException");
        } catch (IOException e) {Log.info("ChartNameBean error IOException");
        } finally {if(fileInputStream != null){try {fileInputStream.close();} catch (IOException e) {Log.error("ChartNameUtils FileInputStream close error!");}}}

	}
	private ChartNameUtils(){}
	
	public static ChartNameBean getChartNameBean() {
		return bean;
	};
	
}
