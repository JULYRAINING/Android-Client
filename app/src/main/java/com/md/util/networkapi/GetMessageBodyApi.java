package com.md.util.networkapi;

import android.util.Log;

import com.md.entity.TokenValidate;
import com.md.util.net.JsonConverter;
import com.md.util.net.UrlString;
import com.md.util.request.ResponseListener;
import com.md.util.request.StringPostRequest;
import com.md.util.request.VolleyQueueApi;
import com.md_c_test.MyApplication;
import com.md_c_test.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SECONDHEAVEN on 2016/1/21.
 */
public class GetMessageBodyApi {
    /**
     * @param flag             若为1，则表示请求最新数据 若为0，则表示更早数据
     * @param startId
     * @param endId
     * @param tokenValidate
     * @param responseListener
     */
    public static void getMessagesFromServer(int flag, int startId, int endId, TokenValidate tokenValidate, ResponseListener responseListener) {

        String url = null;


        url = UrlString.messageGetBodyUrl;


        String tokenStr = JsonConverter.toJson(tokenValidate);
        Map<String, String> params = new HashMap<>();
        Log.e("getMessageService启动请求开始", "456");

        params.put(MyApplication.getContext()
                .getString(R.string.getMessageFlag), String.valueOf(flag));
        params.put(MyApplication.getContext()
                .getString(R.string.getMessageStartId), String.valueOf(startId));
        params.put(MyApplication.getContext()
                .getString(R.string.getMessageEndId), String.valueOf(endId));
        params.put(MyApplication.getContext()
                .getString(R.string.RequestParam_UserValidate_token), tokenStr);
        StringPostRequest stringPostRequest = new StringPostRequest(
                url,
                params,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);

    }
}
