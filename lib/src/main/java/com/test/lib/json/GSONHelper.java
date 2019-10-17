package com.test.lib.json;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.test.lib.log.HLog;

public class GSONHelper {

    private static Gson gson = new Gson();

    public static <T> T getObject(String content, Class<T> tClass) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }

        try {
            return gson.fromJson(content, tClass);
        } catch (JsonSyntaxException e) {
            HLog.d("test", "GSONHelper.getObject: JsonSyntaxException=" + e.getMessage());
            return null;
        }
    }

    public static String toString(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            HLog.d("test", "GSONHelper.toString: Exception=" + e.getMessage());
            return null;
        }
    }

}
