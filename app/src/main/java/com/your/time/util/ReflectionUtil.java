package com.your.time.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.your.time.bean.Rest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class ReflectionUtil {

    public static String getFieldName(String columnName){
        String fieldName = columnName;
        int _index = fieldName.indexOf('_') != -1?0:-1;
        while (_index != -1) {
            _index = fieldName.indexOf('_', _index);
            fieldName = fieldName.replaceFirst("_", "");
            String ch = ("" + fieldName.charAt(_index)).toUpperCase();
            fieldName = fieldName.substring(0, _index) + ch + fieldName.substring(_index + 1);
            _index = fieldName.indexOf('_', _index);
        }
        return fieldName;
    }

    public static String getSetter(String columnName){
        String fieldName = getFieldName(columnName);
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static <T> T getValue(Object object, String fieldName){
        Object value = null;
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            value = field.get(object);
            field.setAccessible(isAccessible);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return (T)value;
    }

    public static <T> Rest mapJson2Bean(JSONObject jsonObject, Class<T> t){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (Rest) objectMapper.readValue(jsonObject.toString(),t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> mapJson2Bean(JSONArray jsonArray, Class<T> t){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonArray.toString(), TypeFactory.defaultInstance().constructCollectionType(List.class, t));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}