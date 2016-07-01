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
public class LoginApi {
    public static void login(UserValidate userValidate, ResponseListener responseListener) {

        String str = JsonConverter.toJson(userValidate);

        Map<String, String> params = new HashMap<>();
        params.put(UrlString.LoginRequestParam, str);
        StringPostRequest stringPostRequest = new StringPostRequest(
                UrlString.userLoginUrl,
                params,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);

    }
}
