package com.md.util.networkapi;

import com.md.util.net.UrlString;
import com.md.util.request.ResponseListener;
import com.md.util.request.StringPostRequest;
import com.md.util.request.VolleyQueueApi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SECONDHEAVEN on 2016/1/20.
 */
public class GetUserInfoApi {
    public static void getUserById(int userId, ResponseListener responseListener) {


        Map<String, String> params = new HashMap<>();

        params.put(UrlString.GetUserDetailByIdRequestParam, String.valueOf(userId));


        StringPostRequest stringPostRequest = new StringPostRequest(
                UrlString.userGetDetailUrl,
                params,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(stringPostRequest);
    }
}
