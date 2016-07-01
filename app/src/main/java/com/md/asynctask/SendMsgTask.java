package com.md.asynctask;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SECONDHEAVEN on 2015/9/26.
 */
public class SendMsgTask extends AsyncTask<String, Integer, String> {

    private String Uri = "http://10.10.4.44:8080/Test_Servlet/AddMessageServlet";

    @Override
    protected String doInBackground(String... params) {
        String gsonString = params[0];
        HttpPost httpPost = new HttpPost(Uri);
        List<NameValuePair> msg_params = new ArrayList<NameValuePair>();
        msg_params.add(new BasicNameValuePair("message", gsonString));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(msg_params));
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpPost);
            System.out.println("已发送");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
