package com.md.util.networkapi;

import com.md.entity.UserValidate;
import com.md.util.net.JsonConverter;
import com.md.util.net.UrlString;
import com.md.util.request.ResponseListener;
import com.md.util.request.StringPostRequest;
import com.md.util.request.VolleyQueueApi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SECONDHEAVEN on 2016/1/19.
 */
public class SimpleRegisterApi {
    public static void regist(UserValidate userValidate,
                              ResponseListener responseListener) {

        String userSimpleInfo = JsonConverter.toJson(userValidate);

        Map<String, String> params = new HashMap<>();
        params.put(UrlString.SimpleRegisteUserRequestParam, userSimpleInfo);
        StringPostRequest stringPostRequest = new StringPostRequest(
                UrlString.userSimpleRegisterUrl,
                params,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);

    }

}
