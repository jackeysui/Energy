/*
 * Copyright Notice:
 *      Copyright  1998-2008, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */

package com.linyang.energy.utils.yunNanUtil;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonUtil {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();

        // 设置FAIL_ON_EMPTY_BEANS属性，当序列化空对象不要抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 设置FAIL_ON_UNKNOWN_PROPERTIES属性，当JSON字符串中存在Java对象没有的属性，忽略
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Convert Object to JsonString
     */
    public static String jsonObj2Sting(Object jsonObj) {
        String jsonString = null;

        try {
            jsonString = objectMapper.writeValueAsString(jsonObj);
        }
        catch (IOException e) {
            System.out.printf("pasre json Object[{}] to string failed.",jsonString);
        }

        return jsonString;
    }

    /**
     * Convert JsonString to Simple Object
     */
    public static <T> T jsonString2SimpleObj(String jsonString, Class<T> cls) {
        T jsonObj = null;

        try {
            jsonObj = objectMapper.readValue(jsonString, cls);
        }
        catch (IOException e) {
            System.out.printf("pasre json Object[{}] to string failed.",jsonString);
        }

        return jsonObj;
    }

    /**
     * Method that will convert object to the ObjectNode.
     */
    public static <T> ObjectNode convertObject2ObjectNode(T object){
        if (null == object) {
            return null;
        }

        ObjectNode objectNode = null;
        if (object instanceof String) {
            objectNode = convertJsonStringToObject((String) object, ObjectNode.class);
        }
        else {
            objectNode = convertValue(object, ObjectNode.class);
        }

        return objectNode;
    }

    /**
     * Method that will convert the json string to destination by the type(cls).
     */
    public static <T> T convertJsonStringToObject(String jsonString, Class<T> cls) {
        if (null == jsonString || jsonString.trim().length() < 1) {
            return null;
        }

        try {
            T object = objectMapper.readValue(jsonString, cls);
            return object;
        }
        catch (Exception e) {
            System.out.printf("pasre json string to object failed.",jsonString);
            return null;
        }
    }

    /**
     * Method that will convert from given value into instance of given value
     */
    private static <T> T convertValue(Object fromValue, Class<T> toValueType){
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
    }

}