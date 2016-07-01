package com.md.util.networkapi;

import android.util.Log;

import com.md.entity.ImageForm;
import com.md.util.net.UrlString;
import com.md.util.request.ResponseListener;
import com.md.util.request.UploadBodyAndImageRequest;
import com.md.util.request.VolleyQueueApi;

import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/1/2.
 */
public class SendBodyAndImageApi {

    public static void upload(String dataBeanStr, List<ImageForm> imageList,
                              ResponseListener responseListener) {
        Log.e("SendBodyAndImageApi", dataBeanStr);


        UploadBodyAndImageRequest uploadFileRequest = new UploadBodyAndImageRequest(
                UrlString.messageAddDataUrl,
                dataBeanStr,
                imageList,
                responseListener);
        VolleyQueueApi.getVolleyQueue().add(uploadFileRequest);

    }
}
