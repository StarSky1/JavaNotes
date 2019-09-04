package com.yj.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

/**
 * 
 */
public class GsonUtil {

    private static Gson gson;

    private static JsonParser jsonParser;

    public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
        return getGson().fromJson(jsonStr, clazz);
    }

    /**
     * 新添加根据具体类型转换对象方法
     **/
    public static <T> T jsonToObject(String jsonStr, Type listType) {
        return getGson().fromJson(jsonStr, listType);
    }

    public static String objectToJson(Object object) {
        return getGson().toJson(object);
    }

    public static JsonObject getJsonObject(String jsonStr){
        return getJsonParser().parse(jsonStr).getAsJsonObject();
    }

    public static JsonParser getJsonParser(){
        if(jsonParser==null){
            return new JsonParser();
        }
        return jsonParser;
    }

    private static Gson getGson() {
        if(gson==null){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
            gsonBuilder.disableHtmlEscaping();
            gson = gsonBuilder.create();
        }
        return gson;
    }
}
