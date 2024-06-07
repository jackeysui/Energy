package com.linyang.energy.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * @ Author     ：dingyang.
 * @ Date       ：Created in 10:39 2018/8/9
 * @ Description：获取properties内容的工具类* @ Modified By：:dingyang
 * @Version: $version$
 */
public class PropertyUtil {
    private static Logger logger = Logger.getLogger(PropertyUtil.class);

    private static Properties props;

    static {
        loadProps();
    }

    /**
     * 输入流(获取文件,并且取值)
     */
    synchronized static private void loadProps() {
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getResourceAsStream("/declareAudit.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("declareAudit.properties文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("declareAudit.properties文件流关闭出现异常");
            }
        }
    }


    /**
     * 修改指定properties文件的内容
     *
     * @param path
     * @param key
     * @param value
     * @return void
     * @throws
     * @author dingy
     * @date 2018/8/21 14:13
     */
    public synchronized static void outputFile(String path, String key, String value) {
        ///保存属性到b.properties文件
        Properties props = new Properties();
        try {
            FileOutputStream oFile = new FileOutputStream(path, true);//true表示追加打开
            props.setProperty(key, value);
            //store(OutputStream,comments):store(输出流，注释)  注释可以通过“\n”来换行
            props.store(oFile, "The New properties file Annotations" + "\n" + "Test For Save!");
            oFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定properties文件的内容
     *
     * @param path
     * @param key
     * @return void
     * @throws
     * @author dingy
     * @date 2018/8/21 14:13
     */
    public synchronized static String inputFile(String path, String key) {
        ///保存属性到b.properties文件
        try {
            FileInputStream fis = new FileInputStream(path);
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props.getProperty(key);
    }


    synchronized static private void setProps() {
        props = new Properties();
        FileOutputStream oFile = null;
        try {
            oFile = new FileOutputStream("/declareAudit.properties", true);
//            props.store(oFile, "Comment");
            oFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据properties文件的key获取value
     *
     * @param key
     * @return java.lang.String
     * @throws
     * @author dingy
     * @date 2018/8/9 11:03
     */
    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    /**
     * 根据properties文件的key获取value(可填写默认值:建议使用这种)
     *
     * @param key
     * @return java.lang.String
     * @throws
     * @author dingy
     * @date 2018/8/9 11:03
     */
    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

    /**
     * 获取所有key
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author dingy
     * @date 2018/8/9 11:05
     */
    public static Set<Object> getProperty() {
        if (null == props) {
            loadProps();
        }
        return props.keySet();
    }


//    public static void main(String[] args) {
//        String timeOut = getProperty("timeOut","3");
//        System.out.println(timeOut);
//    }



}
