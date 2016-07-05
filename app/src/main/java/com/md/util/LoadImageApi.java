package com.md.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import com.md_c_test.MyApplication;
import com.md_c_test.R;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;


/**
 * Created by SECONDHEAVEN on 2015/12/25.
 */
public class LoadImageApi {
    public static void display(ImageView container, String resourcePath) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120)).setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();

        x.image().bind(container, resourcePath);

        //x.image().loadDrawable("",null,null);
    }

    public static void displayServer(ImageView container, String resourcePath) {
        String token;
        int userId;
        String tokenInfo;
        //为了拼接验证token字符串，总感觉效率太低，暂且这样写着
        //读取token信息
        SharedPreferences sharedPreferences = MyApplication.getContext()
                .getSharedPreferences(MyApplication.getContext()
                        .getString(R.string.sharedPreferences_tokenInfo), Context.MODE_PRIVATE);

        userId = sharedPreferences.getInt(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_userId), -1);
        token = sharedPreferences.getString(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_token), "");
        tokenInfo = "&token={\"token\":\"" + token + "\",\"userId\":" + userId + "}";

        String url = resourcePath + tokenInfo;

        ImageOptions imageOptions = new ImageOptions.Builder()
                 .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();

        x.image().bind(container, url, imageOptions);

        //x.image().loadDrawable("",null,null);
    }

}
