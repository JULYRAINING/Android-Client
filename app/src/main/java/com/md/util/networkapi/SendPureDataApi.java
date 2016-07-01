package com.md.util.networkapi;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.md.util.net.UrlString;
import com.md.util.request.ResponseListener;
import com.md.util.request.StringPostRequest;
import com.md.util.request.VolleyQueueApi;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by SECONDHEAVEN on 2016/1/3.
 */
public class SendPureDataApi {
    public static void uploadData(final Context context, String dataBeanStr) {
        Map<String, String> params = new HashMap<>();
        params.put(UrlString.SimpleRegisteUserRequestParam, dataBeanStr);
        StringPostRequest stringPostRequest = new StringPostRequest(
                UrlString.userSimpleRegisterUrl,
                params,
                new ResponseListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "simple注册失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object o) {
                        Toast.makeText(context, "simple注册成功", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);
    }
}
