package com.linyang.energy.utils;

import net.sf.json.JSONObject;

/**
 * 百度API工具类
 *
 */
public class BaiDuUtil {


    //根据地址查经纬度
    public static JSONObject getPositionByAddress(String regionName) {
        String url = "http://api.map.baidu.com/geocoder?address=" + regionName + "&output=json&key=CC99a02db47f0ad1a876db0c090285c9";
        JSONObject obj = JSONObject.fromObject(HttpUtil.getRequest(url));
        return obj;
    }
}