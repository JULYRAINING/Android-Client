package com.md.util.httpupload;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.md.entity.TokenValidate;
import com.md.util.net.JsonConverter;
import com.md.util.net.UrlString;
import com.md_c_test.MyApplication;
import com.md_c_test.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * 上传字符串与图片
 * 调用对象：SendMessageApi
 * registDetailApi
 * <p/>
 * Created by SECONDHEAVEN on 2016/2/11.
 */
public class UploadBodyApi {
    public static void send(String url, String data, List<HashMap<String, String>> imageList, Callback.CommonCallback<ResponseEntity> commonCallback){
        Log.e("data", data);
        SharedPreferences sharedPreferences = MyApplication.getContext()
                .getSharedPreferences(MyApplication.getContext()
                        .getString(R.string.sharedPreferences_tokenInfo), Context.MODE_PRIVATE);

        int userId = sharedPreferences.getInt(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_userId), -1);
        String token = sharedPreferences.getString(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_token), "");

        TokenValidate tokenValidate = new TokenValidate(userId, token);
        String tokenInfo = JsonConverter.toJson(tokenValidate);


        //构造请求url及参数
        RequestParams params = new RequestParams(url);


        //参数 data
        params.addParameter(UrlString.uploadDataTag, data);
        //参数 token
        params.addParameter(UrlString.tokenTag, tokenInfo);
        //参数Image
        for (HashMap<String, String> imageStr : imageList) {

            String filename = imageStr.get("Image");
            if(filename.equals("user_empty")){
                filename = Environment.getExternalStorageDirectory() + File.separator + "user_image.png";
                InputStream is = null;
                try {
                    is = MyApplication.getContext().getAssets().open("user_img_png.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(new File(filename));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                try {
                    while((byteCount=is.read(buffer))!=-1){
                        fos.write(buffer, 0, byteCount);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.flush();
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }else if(filename.equals("message_empty")){
                filename = Environment.getExternalStorageDirectory() + File.separator + "message_image.png";
                InputStream is = null;
                try {
                    is = MyApplication.getContext().getAssets().open("default_upload_img.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(new File(filename));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                try {
                    while((byteCount=is.read(buffer))!=-1){
                        fos.write(buffer, 0, byteCount);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.flush();
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }

            params.addBodyParameter(UrlString.uploadImageTag, new File(filename));
            Log.e("filename", imageStr.get("Image"));
            Log.e("filename", filename);
        }

        //执行回调，发送http请求。
        Callback.Cancelable cancelable
                = x.http().post(params, commonCallback);
    }
}
