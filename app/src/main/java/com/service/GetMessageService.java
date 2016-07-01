package com.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.VolleyError;
import com.db.dao.MessageDbDao;
import com.db.daoImpl.MessageDbDaoImpl;
import com.md.entity.MessageBO;
import com.md.entity.TokenValidate;
import com.md.util.net.JsonConverter;
import com.md.util.networkapi.GetMessageBodyApi;
import com.md.util.request.ResponseListener;
import com.md_c_test.R;

import java.util.List;

/**
 * GetMessageService
 * 用来获取服务器中的文章数据
 * 由FragmentMsg启动
 * Service请求网络数据完成后，发送广播消息（带有数据Intent）通知由FragmentMsg更新UI界面
 */
public class GetMessageService extends Service {

    public static final String UNIQUE_STRING = "com.fragment.msgBroadcastReceiver";
    public static final String FAILURE_STRING = "ENKELFLDFUHASLDKJF";
    public static final String SUCCESS_STRING = "LHEAUIFHALKDHFLAKJDF";


    public GetMessageService() {
    }

    /**
     * 每次外部调用startService后，启动的第一个方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        /**
         * SharedPreferences
         * 名称 tokenInfo
         * 数据 userId token
         * 用来存储服务器发送的token
         * 请求数据时附加在表单中
         */

        //进程被kill之后，Service可能被系统重启，但由于Service是由startSerivce启动的，所以重启之后intent为null，于是这里要加判断
        if (intent != null) {
            int getMessageStartId = intent.getIntExtra(getString(R.string.getMessageStartId), 0);
            int getMessageFlag = intent.getIntExtra(getString(R.string.getMessageFlag), 0);
            int getMessageEndId = intent.getIntExtra(getString(R.string.getMessageEndId), 0);

            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.sharedPreferences_tokenInfo), Context.MODE_PRIVATE);
            int userId = sharedPreferences.getInt(
                    getString(R.string.sharedPreference_tokenInfo_userId), -1);
            String token = sharedPreferences.getString(
                    getString(R.string.sharedPreference_tokenInfo_token), "");


            TokenValidate tokenValidate = new TokenValidate(userId, token);

            //若请求较早的数据
            if (getMessageFlag == 0) {
                //查询sqlite中是否有请求的数据
                MessageDbDao messageDbDao = new MessageDbDaoImpl(this);
                List<MessageBO> list = messageDbDao.getMessageInTheRangeOf(getMessageStartId, getMessageEndId);
                //若存在 则发送广播
                Log.e("sqlite message size", list.size() + "");
                if (list.size() != 0) {
                    Intent intentSuccess = new Intent(UNIQUE_STRING);
                    intentSuccess.putExtra("messages", JsonConverter.toJson(list));
                    sendBroadcast(intentSuccess);
                } else {
                    //若不存在 则向server发送请求
                    requestServer(getMessageFlag, getMessageStartId, getMessageEndId, tokenValidate);
                }

            } else {
                //若请求最新数据 则向server发送请求
                requestServer(getMessageFlag, getMessageStartId, getMessageEndId, tokenValidate);
            }
        }


        stopSelf();


        return super.onStartCommand(intent, flags, startId);
    }

    private void requestServer(int getMessageFlag, int getMessageStartId, int getMessageEndId, TokenValidate tokenValidate) {
        GetMessageBodyApi.getMessagesFromServer(getMessageFlag,
                getMessageStartId,
                getMessageEndId,
                tokenValidate, new ResponseListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("service获得返回值", "失败");
                        Intent intentFailure = new Intent(UNIQUE_STRING);
                        sendBroadcast(intentFailure);
                    }

                    @Override
                    public void onResponse(Object o) {
                        String messageList = (String) o;

                        Intent intentSuccess = new Intent(UNIQUE_STRING);
                        intentSuccess.putExtra("messages", messageList);
                        //发送广播消息
                        sendBroadcast(intentSuccess);


                    }
                });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
