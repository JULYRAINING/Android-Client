package com.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.md.entity.MessageBO;
import com.md.parcelable.IntentDataParcelable;
import com.md.util.httpupload.ResponseEntity;
import com.md.util.net.JsonConverter;
import com.md.util.networkapi.SendMessageApi;
import com.md_c_test.R;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.List;

public class SendMessageService extends Service {
    private SendMessageService context;
    public static final String UNIQUE_STRING = "GBJGKLJSDFKSJDLSKDJF";
    public static final int UPLOAD_START = 1;
    public static final int UPLOAD_FINISH = 0;
    public static final int UPLOAD_FAILED = 2;

    private Intent intentToMain;

    public SendMessageService() {
        this.context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intentToMain = new Intent(UNIQUE_STRING);
        //向MainActivity发送广播消息，提示上传开始
        intentToMain.putExtra(getString(R.string.SendMessageService_SendBroadcast_Intent_flag), UPLOAD_START);
        sendBroadcast(intentToMain);
        Log.e("uploadState","start");
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.sharedPreference_userInfo), Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(getString(R.string.SharedPreference_userInfo_userId), -1);
        String userName = sharedPreferences.getString(
                getString(R.string.SharedPreference_userInfo_userName), "userName"
        );
        Log.e("用户Id", String.valueOf(userId));
        if (userId != -1) {
            IntentDataParcelable intentDataParcelable = intent.getParcelableExtra(getString(R.string.IntentExtraFlag_msgBody));

            //message内容
            String msgContent = intentDataParcelable.mStr;
            //message图片文件
            List<HashMap<String, String>> msgImageList = intentDataParcelable.mList;
            //message分类id
            int categoryId = intentDataParcelable.mInt;

            MessageBO messageBO = new MessageBO();
            messageBO.setMsgContent(msgContent);
            messageBO.setCategoryId(categoryId);
            messageBO.setUserId(userId);
            messageBO.setMsgSender(userName);

            if (msgImageList != null) {
                String msgImageListStr = JsonConverter.toJson(msgImageList);
                messageBO.setImagePathListStr(msgImageListStr);
            } else {
                messageBO.setImagePathListStr("[{\"Image\":\"message_empty\"}]");
                //messageBO.setImagePathListStr("[{\"Image\":\"/storage/emulated/0/image2.jpg\"}]");
            }




            Log.e("userName", userName);
            SendMessageApi.send(messageBO, new Callback.CommonCallback<ResponseEntity>() {

                @Override
                public void onSuccess(ResponseEntity result) {
                    Log.e("uploadState","success");

                    intentToMain.putExtra(getString(R.string.SendMessageService_SendBroadcast_Intent_flag), UPLOAD_FINISH);
                    //to MainActivity
                    sendBroadcast(intentToMain);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("uploadState","failed");


                    intentToMain.putExtra(getString(R.string.SendMessageService_SendBroadcast_Intent_flag), UPLOAD_FAILED);
                    //to MainActivity

                    sendBroadcast(intentToMain);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

            /*SendMessageApi.send(messageBO, new ResponseListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();

                    intentToMain.putExtra(getString(R.string.SendMessageService_SendBroadcast_Intent_flag), UPLOAD_FAILED);
                    sendBroadcast(intentToMain);
                }

                @Override
                public void onResponse(Object o) {
                    Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
                    intentToMain.putExtra(getString(R.string.SendMessageService_SendBroadcast_Intent_flag), UPLOAD_FINISH);
                    sendBroadcast(intentToMain);
                }
            });*/
        }

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
