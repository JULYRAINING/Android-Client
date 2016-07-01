package com.md.util.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.md.entity.TokenValidate;
import com.md.util.net.JsonConverter;
import com.md_c_test.MyApplication;
import com.md_c_test.R;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by SECONDHEAVEN on 2016/1/3.
 */
public class StringPostRequest extends Request<String> {

    private Map<String, String> params;
    private ResponseListener listener;

    public StringPostRequest(String url, Map<String, String> params, ResponseListener listener) {
        super(Method.POST, url, listener);

        //读取token信息
        SharedPreferences sharedPreferences = MyApplication.getContext()
                .getSharedPreferences(MyApplication.getContext()
                        .getString(R.string.sharedPreferences_tokenInfo), Context.MODE_PRIVATE);

        int userId = sharedPreferences.getInt(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_userId), -1);
        String token = sharedPreferences.getString(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_token), "");

        TokenValidate tokenValidate = new TokenValidate(userId, token);
        params.put(MyApplication.getContext()
                        .getString(R.string.RequestParam_UserValidate_token),
                JsonConverter.toJson(tokenValidate));


        this.params = params;
        this.listener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        String result = null;
        try {
            //若服务器未指定header字符编码方式，则默认返回"iso-8859-1"
            // result = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));

            result = new String(networkResponse.data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            result = new String(networkResponse.data);
        }
        return Response.success(result, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected void deliverResponse(String o) {
        this.listener.onResponse(o);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params;
    }
}
