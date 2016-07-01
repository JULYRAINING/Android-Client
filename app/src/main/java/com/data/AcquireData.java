package com.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SECONDHEAVEN on 2016/3/15.
 */
public class AcquireData {

    public static String get(String urlstr, String encode) throws IOException {
        InputStream inputStream;

        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        URL url = new URL(urlstr);


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setDoOutput(true);

        conn.setReadTimeout(5000);
        conn.setDoInput(true);
        conn.setUseCaches(false);

        inputStream = conn.getInputStream();


        inputStreamReader = new InputStreamReader(inputStream, encode);
        bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {

            stringBuilder.append(line);
        }


        String data = stringBuilder.toString();

        return data;

    }
}
