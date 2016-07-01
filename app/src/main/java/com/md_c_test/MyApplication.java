package com.md_c_test;

import android.app.Application;
import android.content.Context;

import com.md.util.request.VolleyQueueApi;

import org.xutils.x;

/**
 * Created by SECONDHEAVEN on 2015/12/25.
 */
public class MyApplication extends Application {
    //http://developer.android.com/reference

    private static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
        x.Ext.setDebug(true);
        VolleyQueueApi.initVolleyQueue(this);
        context = this;

    }

    public static Context getContext() {
        if (context == null) {
            context = new MyApplication();
        }
        return context;
    }
}
