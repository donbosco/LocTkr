package com.your.time.util;

import com.your.time.bean.Status;

import org.json.JSONObject;

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
            value = object.getClass().getDeclaredField(fieldName).get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return (T)value;
    }

    public static <T> Status<T> mapJson2Bean(JSONObject jsonObject,T t){
        return null;
    }

}