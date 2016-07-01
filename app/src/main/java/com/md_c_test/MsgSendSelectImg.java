package com.md_c_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.md.adapter.MsgAddImgAdapter;
import com.md.parcelable.IntentDataParcelable;
import com.recyclerview.listener.ClickListener;
import com.recyclerview.listener.LongClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class MsgSendSelectImg extends AppCompatActivity implements ClickListener, LongClickListener {


    private static RecyclerView mAddImgRecyclerView;

    private MsgSendSelectImg context = MsgSendSelectImg.this;

    private Toolbar toolbar;
    private ArrayList<HashMap<String, String>> list, resultList;
    private MsgAddImgAdapter adapter;
    private HashMap<Integer, Boolean> isSelected;
    private Thread mThread;
    private File[] f1;
    private Handler mHandler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initData();
                    adapter = new MsgAddImgAdapter(MsgSendSelectImg.this,
                            list,
                            isSelected);
                    adapter.setClickListener(context);
                    adapter.setLongClickListener(context);
                    mAddImgRecyclerView.setAdapter(adapter);
                    Snackbar.make(mAddImgRecyclerView, "选择完成后返回即可", Snackbar.LENGTH_LONG).show();
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
        isSelected = new HashMap<>();

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

    @Override
    protected void onStart() {
        super.onStart();

        getIntentData();

        showToolbarHint(resultList.size());


    }

    private void getIntentData() {
        Intent intent = getIntent();
        IntentDataParcelable idp = new IntentDataParcelable();
        idp = intent.getParcelableExtra(getString(R.string.FlagImageListSendImage));
        resultList = idp.mList;

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

    private void initData() {
        for (int i = 0; i < list.size(); i++) {
            if (resultList.contains(list.get(i))) {
                isSelected.put(i, true);
            } else {
                isSelected.put(i, false);
            }
        }


    }


    private void resultImg() {
        Intent intentParcelable = new Intent();
        IntentDataParcelable idp = new IntentDataParcelable();
        idp.mInt = 1;
        idp.mStr = "julyraining";
        idp.mList = resultList;
        intentParcelable.putExtra("Select_Img_Result", idp);
        setResult(RESULT_OK, intentParcelable);
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
        CheckBox m = (CheckBox) v.findViewById(R.id.id_add_item_checkbox);
        if (resultList.size() < 9) {
            if (resultList.contains(list.get(position))) {
                isSelected.put(position, false);
                m.setChecked(isSelected.get(position));
                resultList.remove(list.get(position));
            } else {

                isSelected.put(position, true);
                m.setChecked(isSelected.get(position));
                resultList.add(list.get(position));
            }
            showToolbarHint(resultList.size());
            System.out.println("" + 1 + isSelected.get(position));
        } else if (resultList.size() == 9) {
            if (resultList.contains(list.get(position))) {
                isSelected.put(position, false);
                m.setChecked(isSelected.get(position));
                resultList.remove(list.get(position));

            } else {
                Toast.makeText(MsgSendSelectImg.this,
                        getResources().getString(R.string.alert_AtySelectImg_max_nine),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MsgSendSelectImg.this,
                    getResources().getString(R.string.alert_AtySelectImg_max_nine),
                    Toast.LENGTH_SHORT).show();
        }
        showToolbarHint(resultList.size());

    }

    private void showToolbarHint(int count) {

        toolbar.setTitle("选择图片" + count + "/9");
    }

    @Override
    public void longClick(View v, int position) {
        Toast.makeText(context, "你点击了第" + position + "张图片", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                resultImg();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                resultImg();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}