package com.md.util.request;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by SECONDHEAVEN on 2016/1/2.
 */
public class VolleyQueueApi {

    private static RequestQueue queue;

    public static void initVolleyQueue(Context context) {
        queue = Volley.newRequestQueue(context);

    }

    public static RequestQueue getVolleyQueue() {
        if (queue != null) {
            return queue;
        } else {
            throw new RuntimeException("RequestQueue未初始化");
        }
    }
}
