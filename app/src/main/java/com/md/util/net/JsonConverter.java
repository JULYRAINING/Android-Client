package com.md.util.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class JsonConverter {

    public static <T> T toBean(String jsonString, Type classType) {
        Gson gson = new Gson();

        StringReader reader = new StringReader(jsonString);
        T target = (T) gson.fromJson(reader, classType);
        return target;


    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        return json;

    }

    public static List<HashMap<String, String>> toImageList(String imageStr) {
        Type type = new TypeToken<List<HashMap>>() {
        }.getType();
        Gson gson = new Gson();
        List<HashMap<String, String>> imageList = gson.fromJson(imageStr, type);
        return imageList;
    }

}
