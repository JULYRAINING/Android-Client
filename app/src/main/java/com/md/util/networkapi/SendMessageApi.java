package com.md.util.networkapi;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.md.entity.MessageBO;
import com.md.util.httpupload.UploadBodyApi;
import com.md.util.net.UrlString;

import org.xutils.common.Callback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;


/**
 * 上传信息
 * 调用对象：SendMessageService
 * Created by SECONDHEAVEN on 2016/1/3.
 */
public class SendMessageApi {

    public static void send(MessageBO messageBO, Callback.CommonCallback commonCallback) {
        String url = UrlString.messageAddDataUrl;
        Gson gson = new Gson();
        String dataStr = gson.toJson(messageBO);
        Log.e("SendMessageApi", dataStr);


        Type type = new TypeToken<List<HashMap>>() {
        }.getType();
        List<HashMap<String, String>> imageList = gson.fromJson(messageBO.getImagePathListStr(), type);


        UploadBodyApi.send(url, dataStr, imageList, commonCallback);


    }
}
