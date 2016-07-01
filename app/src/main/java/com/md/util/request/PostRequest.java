package com.md.util.request;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by SECONDHEAVEN on 2015/12/10.
 */
public class PostRequest extends Request<String> {
    private Map<String, String> params;
    private ResponseListener listener;

    public PostRequest(String url, Map<String, String> params, ResponseListener listener) {
        super(Method.POST, url, listener);
        this.params = params;
        this.listener = listener;
        setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    protected void deliverResponse(String s) {
        listener.onResponse(s);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        String result;
        try {
            //若服务器未指定header字符编码方式，则默认返回"iso-8859-1"
            // result = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            result = new String(networkResponse.data, "utf-8");
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new VolleyError());
        }


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
