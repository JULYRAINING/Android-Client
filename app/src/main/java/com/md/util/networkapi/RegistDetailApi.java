package com.md.util.networkapi;

import com.md.entity.UserBO;
import com.md.util.httpupload.ResponseEntity;
import com.md.util.httpupload.UploadBodyApi;
import com.md.util.net.JsonConverter;
import com.md.util.net.UrlString;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 注册新用户
 * 调用对象：UserDetailActivity
 * Created by SECONDHEAVEN on 2016/1/16.
 */
public class RegistDetailApi {
    public static void improve(UserBO userBo,
                               Callback.CommonCallback<ResponseEntity> commonCallback) {
        String url = UrlString.userRegisterUrl;
        String data = JsonConverter.toJson(userBo);

        List<HashMap<String, String>> imageList = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(UrlString.imageMapTag, userBo.getUserImage());
        imageList.add(hashMap);

        UploadBodyApi.send(url, data, imageList, commonCallback);


    }
}
