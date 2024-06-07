package com.linyang.energy.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;


import com.linyang.common.web.common.Log;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class WordGenerator {
	private static Configuration configuration = null;
	private static Map<String, Template> allTemplates = null;
	
	static {
		configuration = new Configuration();
		configuration.setDefaultEncoding( "utf-8" );
		configuration.setClassForTemplateLoading( WordGenerator.class, "/com/linyang/energy/ftl" );
		allTemplates = new HashMap<String, Template>();
		try {
			allTemplates.put( "report", configuration.getTemplate( "report.ftl", "utf-8" ) );
			allTemplates.put( "report2", configuration.getTemplate( "report2.ftl", "utf-8" ) );
			allTemplates.put( "report3", configuration.getTemplate( "report3.ftl", "utf-8" ) );
			allTemplates.put( "report4", configuration.getTemplate( "report4.ftl", "utf-8" ) );
		} catch (IOException e) {
			Log.error( "WordGenerator --  getTemplate error!" );
			throw new RuntimeException( e );
		}
	}
	
	private WordGenerator() {
		throw new AssertionError();
	}
	
	public static File createDoc(Map<?, ?> dataMap, String type) {
		String name = "temp";
		try {
			SecureRandom random1 = SecureRandom.getInstance( "SHA1PRNG" );
			name = name + String.valueOf( random1.nextInt() * 100000 );
		} catch (NoSuchAlgorithmException e) {
			Log.error( "WordGenerator createDoc No Such Algorithm!" );
		}
		
		name = name + ".doc";
		File f = new File( name );
		Template t = allTemplates.get( type );
		Writer w = null;
		try {
			// 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
			w = new OutputStreamWriter( new FileOutputStream( f ), "utf-8" );
			t.process( dataMap, w );
		} catch (Exception ex) {
			Log.error( "WordGenerator -- create doc error!" );
			throw new RuntimeException( ex );
		} finally {
			if (w != null) {
				try {
					w.close();
					w = null;
				} catch (IOException e) {
					Log.error( "WordGenerator --  ios error!" );
				}
			}
		}
		return f;
	}
}
