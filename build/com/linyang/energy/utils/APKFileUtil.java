package com.linyang.energy.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

/**
 * 取webroot目录下APK文件的名称工具类
 * 
 * @author gaofeng
 * 
 */
public class APKFileUtil {

	/**
	 * 取webroot目录下APK文件的名称
	 * @param type 默认林洋APP：1林洋标识;2中性APP
	 * @param key 默认安卓App:1安卓App 2 IOS App
	 * @return
	 */
	public static String getNewApkName(int ...type) {
		String path = XMLMethods.loadPath("");// 取当前路径
		int index = path.lastIndexOf("WEB-INF");
		if (index != -1) {
			path = path.substring(0, index);
		}
		File file = new File(path);
		File[] list = file.listFiles();// 取webroot下面所有文件
		File lastFile = null;
		String fileName = "";
		String pathStart = "e";
		if (type.length > 0 && type[0] == 2)
			pathStart = "n";
		for (File f : list) {
			if (f.isDirectory())
				continue;
			String suffix;
			if(type.length == 2 && type[1] == 2 ){ // IOS 版本
				suffix = "ipa";
			} else { // 安卓版本
				suffix = "apk";
			}
			if (f.getName().endsWith(suffix) && f.getName().startsWith(pathStart)) {
				if (lastFile == null) {
					lastFile = f;
					fileName = f.getName();
				} else {
					if (f.lastModified() > lastFile.lastModified()) {// 如果有多个取最新的
						lastFile = f;
						fileName = f.getName();
					}
				}
			}
			
		}
		return fileName;
	}
	
	/**
	 * 获取当前版本号
	 * @param type
	 * @return
	 */
	public static String getCurrentVersion(int ...type){
		String apkName = APKFileUtil.getNewApkName(type);
		String newVersion = apkName.substring(apkName.lastIndexOf("_")+1, apkName.lastIndexOf("."));
		return newVersion;
	}
	
	/**
	 * 获取安卓手机增量包名称
	 * @param type 默认林洋App：1林洋;2、中性
	 * @return
	 */
	public static String getNewPatchName(int type){
		String path = XMLMethods.loadPath("");// 取当前路径
		int index = path.lastIndexOf("WEB-INF");
		if (index != -1)
			path = path.substring(0, index);
		File file = new File(path);
		File[] list = file.listFiles();// 取webroot下面所有文件
		File lastFile = null;
		String fileName = "";
		String pathStart = "p";
		if (type == 2)
			pathStart = "n";
		for (File f : list) {
			if (f.isDirectory())
				continue;
			String suffix= "jar";
			if (f.getName().endsWith(suffix) && f.getName().startsWith(pathStart)) {
				if (lastFile == null) {
					lastFile = f;
					fileName = f.getName();
				} else {
					if (lastFile.lastModified() > f.lastModified()) {// 如果有多个取最新的
						lastFile = f;
						fileName = f.getName();
					}
				}
			}
		}
		return fileName;
	}
	
	/**
	 * 获取安卓手机增量包版本(patch3.0_1.jar,neuter_patch3.0_1.jar)
	 * @param type 默认林洋App：1林洋;2、中性
	 * @return
	 */
	public static String getCurrentPatchVersion(int type){
		String name = getNewPatchName(type);
		String version = "";
		if (name.length() > 5) {
			if (type == 1) {
				version = name.substring(5, name.lastIndexOf("."));
			} else {
				version = name.substring(12, name.lastIndexOf("."));
			}
		}
		return version;
	}
}
