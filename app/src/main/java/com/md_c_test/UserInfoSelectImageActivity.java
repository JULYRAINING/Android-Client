package com.md_c_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.md.adapter.UserSelectImageAdapter;
import com.recyclerview.listener.ClickListener;
import com.recyclerview.listener.LongClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class UserInfoSelectImageActivity extends AppCompatActivity implements ClickListener, LongClickListener {


    private static RecyclerView mAddImgRecyclerView;

    private UserInfoSelectImageActivity context = UserInfoSelectImageActivity.this;

    private Toolbar toolbar;
    private ArrayList<HashMap<String, String>> list, resultList;
    private UserSelectImageAdapter adapter;
    private String imagePath;
    private Thread mThread;
    private File[] f1;
    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new UserSelectImageAdapter(UserInfoSelectImageActivity.this,
                            list);
                    adapter.setClickListener(context);
                    adapter.setLongClickListener(context);
                    mAddImgRecyclerView.setAdapter(adapter);
                    break;
            }

        }

    };
    private Runnable work = new Runnable() {


        @Override
        public void run() {
            loadImageFromSdCard();
            sortImageListByLastModify();
            mHandler.obtainMessage(1).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.message_send_add_img);
        initView();


        list = new ArrayList<>();
        resultList = new ArrayList<>();


        mThread = new Thread(work);
        mThread.start();


    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("选择图片");
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


        mAddImgRecyclerView = (RecyclerView) findViewById(R.id.id_add_img);
        mAddImgRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        mAddImgRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    private void loadImageFromSdCard() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this,
                    getResources().getString(R.string.alert_AtySelectImg_noSDCard),
                    Toast.LENGTH_LONG).show();
            ;
            finish();
            return;
        }


        File file = new File(Environment.getExternalStorageDirectory(),
                getResources().getString(R.string.AtySelectImg_childFolder));
        File[] filelist = file.listFiles();
        if (filelist != null) {
            selectImg(filelist);

        }
    }

    private void selectImg(File[] ff) {
        for (int i = 0; i < ff.length; i++) {

            if ((!ff[i].isDirectory())
                    && (!ff[i].isHidden())
                    && (ff[i].length() > 100000)
                    && (ff[i].getName().endsWith(".jpg")
                    || ff[i].getName().endsWith(".png")
                    || ff[i].getName().endsWith(".gif")
                    || ff[i].getName().endsWith(".bmp")
                    || ff[i].getName().endsWith(".tif"))) {

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Image", ff[i].getAbsolutePath());
                list.add(map);

            } else if (ff[i].isDirectory() && !ff[i].isHidden()) {
                f1 = ff[i].listFiles();
                selectImg(f1);

            }
        }
    }


    private void resultImg(String result) {

        Intent intent = new Intent();
        intent.putExtra("imagePath", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void sortImageListByLastModify() {
        Collections.sort(list, new Comparator<HashMap<String, String>>() {

            @Override
            public int compare(HashMap<String, String> lhs,
                               HashMap<String, String> rhs) {

                return -Long.valueOf(
                        new File(lhs.get("Image")
                        ).lastModified()
                )
                        .compareTo(
                                new File(rhs.get("Image")
                                ).lastModified()
                        );

            }
        });
    }

    @Override
    public void click(View v, int position) {
        imagePath = list.get(position).get("Image");
        resultImg(imagePath);

    }


    @Override
    public void longClick(View v, int position) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                System.gc();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddImgRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                UserSelectImageAdapter.ViewHolder viewHolder = (UserSelectImageAdapter.ViewHolder) holder;
                viewHolder.mImgItem.setImageBitmap(null);
            }
        });
    }
}