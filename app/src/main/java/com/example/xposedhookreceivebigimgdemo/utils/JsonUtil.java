package com.example.xposedhookreceivebigimgdemo.utils;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

//* 封装的是使用Gson解析json的方法
// * @author Administrator
// *
// */
public class JsonUtil {
    private static final String TAG = "JsonUtil";
    /**
     * //	 * 把一个map变成json字符串
     * //	 * @param map
     * //	 * @return
     * ////
     */
    public static String parseMapToJson(Map<?, ?> map) {
        try {
            Gson gson = new Gson();
            return gson.toJson(map);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * ////	 * 把一个json字符串变成对象
     * ////	 * @param json
     * ////	 * @param cls
     * ////	 * @return
     * ////
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
        }
        return t;
    }

    ////	/**
////	 * 把json字符串变成map
////	 * @param json
////	 * @return
////	 */
    public static Map<String, Object> parseJsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        Map<String, Object> map = null;
        try {
            map = gson.fromJson(json, type);
        } catch (Exception e) {
            Log.i(TAG,"json转MAP 失败 "+e);

        }
        return map;
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type new TypeToken<List<yourbean>>(){}.getType()
     * @return
     */
    public static List<?> parseJsonToList(String json, Type type) {
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    /**
     * 获取json串中某个字段的值，注意，只能获取同一层级的value
     *
     * @param json
     * @param key
     * @return
     */
    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json))
            return null;
        if (!json.contains(key))
            return "";
        JSONObject jsonObject = null;
        String value = null;
        try {
            jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getjson(Object object) {
        Gson gson = new Gson();
        String s = gson.toJson(object.toString()).toString();
        return s;
    }

    public static String tojson(Object object) {
        Gson gson = new Gson();
        String s = gson.toJson(object);
        return s;
    }

    /**
     * Google Gson
     * @param jsonInString
     * @return
     */
    public final static boolean isJSONValid3(String jsonInString) {
        try {
            Gson  gson=new Gson();
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch(JsonSyntaxException ex) {
            return false;
        }
    }
}
