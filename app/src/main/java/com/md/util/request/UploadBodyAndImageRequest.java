package com.md.util.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.md.entity.ImageForm;
import com.md.entity.TokenValidate;
import com.md.util.net.JsonConverter;
import com.md.util.net.UrlString;
import com.md_c_test.MyApplication;
import com.md_c_test.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by SECONDHEAVEN on 2015/12/10.
 */
public class UploadBodyAndImageRequest extends PostRequest {
    private static final String BOUNDARY = "---------------------------7d931c5d043e";
    private static final String ENTRY_BOUNDARY = "--" + BOUNDARY;
    private static final String END_BOUNDARY = ENTRY_BOUNDARY + "--\r\n";

    private List<ImageForm> imageList;
    private String dataBeanStr;

    public UploadBodyAndImageRequest(String url, String dataBeanStr, List<ImageForm> imageList, ResponseListener listener) {
        super(url, null, listener);
        this.imageList = imageList;
        this.dataBeanStr = dataBeanStr;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
//        if (imageList == null || imageList.size() == 0) {
//            return null;
//        }
        int N = imageList.size();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageForm imageForm;
        try {

            StringBuffer sb = new StringBuffer();
            //表单格式
            //ENTRY_BOUNDARY
            //\r\n
            //Content-Disposition:form-data;name="upload"
            //\r\n
            //\r\n
            //content
            //\r\n

            //传输message内容
            sb.append(ENTRY_BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition:form-data;name=\"");
            sb.append(UrlString.uploadDataTag);
            sb.append("\"");
            sb.append("\r\n");
            sb.append("\r\n");
            bos.write(sb.toString().getBytes(getParamsEncoding()));
            bos.write(dataBeanStr.getBytes(getParamsEncoding()));
            bos.write("\r\n".getBytes(getParamsEncoding()));


//读取token信息
            SharedPreferences sharedPreferences = MyApplication.getContext()
                    .getSharedPreferences(MyApplication.getContext()
                            .getString(R.string.sharedPreferences_tokenInfo), Context.MODE_PRIVATE);

            int userId = sharedPreferences.getInt(MyApplication.getContext()
                    .getString(R.string.sharedPreference_tokenInfo_userId), -1);
            String token = sharedPreferences.getString(MyApplication.getContext()
                    .getString(R.string.sharedPreference_tokenInfo_token), "");

            TokenValidate tokenValidate = new TokenValidate(userId, token);
            String tokenInfo = JsonConverter.toJson(tokenValidate);


            //传输token验证信息
            StringBuffer sbToken = new StringBuffer();
            sbToken.append(ENTRY_BOUNDARY);
            sbToken.append("\r\n");
            sbToken.append("Content-Disposition:form-data;name=\"");
            sbToken.append(MyApplication.getContext()
                    .getString(R.string.RequestParam_UserValidate_token));
            sbToken.append("\"");
            sbToken.append("\r\n");
            sbToken.append("\r\n");
            bos.write(sbToken.toString().getBytes(getParamsEncoding()));
            bos.write(tokenInfo.getBytes(getParamsEncoding()));
            bos.write("\r\n".getBytes(getParamsEncoding()));


            //传输message图片文件
            for (int i = 0; i < N; i++) {
                imageForm = imageList.get(i);
                StringBuffer sb1 = new StringBuffer();
                sb1.append(ENTRY_BOUNDARY);
                sb1.append("\r\n");
                sb1.append("Content-Disposition:form-data;name=\"");
                sb1.append(imageForm.getName());
                sb1.append("\";filename=\"" + imageForm.getPath());
                sb1.append("\"\r\nContent-Type:");
                sb1.append(imageForm.getMimeType());
                sb1.append("\r\n\r\n");
                bos.write(sb1.toString().getBytes(getParamsEncoding()));
                bos.write(imageForm.getValue());
                bos.write("\r\n".getBytes(getParamsEncoding()));

            }

            bos.write(END_BOUNDARY.getBytes(getParamsEncoding()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }
}
