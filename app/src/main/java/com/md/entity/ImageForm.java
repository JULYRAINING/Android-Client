package com.md.entity;

import android.util.Log;

import com.md_c_test.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by SECONDHEAVEN on 2015/12/10.
 */
public class ImageForm {

    private String path;


    public ImageForm(String path) {
        this.path = path;
        Log.e("~~~~~~~~~", path);
    }

    public byte[] getValue() throws Exception {
        InputStream inputStream;
        byte[] buffer = null;
        ByteArrayOutputStream bos;

        if (path.startsWith("/assets")) {
            path = path.substring(10, path.length());
            Log.e("~~~~~~~~~", path);
            inputStream = MyApplication.getContext().getAssets().open(path);
        } else {
            File imageFile = new File(path);
            inputStream = new FileInputStream(imageFile);
        }


        bos = new ByteArrayOutputStream();

        byte[] b = new byte[1024];
        int n;
        while ((n = inputStream.read(b)) != -1) {
            bos.write(b, 0, n);
            Log.e("x写入", "" + n);
        }
        inputStream.close();
        bos.close();
        buffer = bos.toByteArray();

        return buffer;


    }


    public String getName() {
        return "uploadImage";
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getMimeType() {
        return "image/png";
    }
}
