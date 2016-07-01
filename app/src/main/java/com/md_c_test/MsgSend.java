package com.md_c_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.md.adapter.MsgSendAdapter;
import com.md.parcelable.IntentDataParcelable;
import com.recyclerview.listener.ClickListener;
import com.service.SendMessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class MsgSend extends AppCompatActivity implements ClickListener {


    private static final int SUBACTIVITY_SELECT_IMAGE = 1;
    private static final int SUBACTIVITY_CHECK_IMAGE = 2;
    private static final int SUCCESS = 711;

    private EditText mSendEditText;
    private RecyclerView mSendImageRecyclerView;
    private ArrayList<HashMap<String, String>> sendImageList;
    private Toolbar toolbar;
    private TextView mAddImageButton;
    private int categoryId = 0;
    private List<String> categoryList;
    private AppCompatSpinner appCompatSpinner;
    private MsgSendAdapter sendImageAdapter;
    private MsgSend context;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    sendImageAdapter.notifyDataSetChanged();
            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.send_message);
        context = this;
        initView();

        sendImageList = new ArrayList<HashMap<String, String>>();
        sendImageAdapter = new MsgSendAdapter(this, sendImageList);
        sendImageAdapter.setClickListener(this);


    }

    private void initView() {
        //toolbar替换actionbar
        toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("发表新文章");
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //设置custonView
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.send_message_toolbar_custom_spinner);
        appCompatSpinner = (AppCompatSpinner) findViewById(R.id.id_send_msg_toolbar_spinner);

        categoryList = new ArrayList<String>();
        categoryList.add("校内");
        categoryList.add("公寓");
        categoryList.add("学习");
        categoryList.add("其它");

        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, categoryList);
        appCompatSpinner.setAdapter(arrayAdapter);
        appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mSendEditText = (EditText) findViewById(R.id.id_send_msg_title);

        mAddImageButton = (TextView) findViewById(R.id.id_send_msg_add_img);

        mSendImageRecyclerView = (RecyclerView) findViewById(R.id.id_send_img_view);
        mSendImageRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mSendImageRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAddImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSelectImageAty = new Intent(MsgSend.this,
                        MsgSendSelectImg.class);
                IntentDataParcelable idp = new IntentDataParcelable();
                idp.mInt = sendImageList.size();
                idp.mList = sendImageList;
                intentSelectImageAty.putExtra(getString(R.string.FlagImageListSendImage), idp);
                startActivityForResult(intentSelectImageAty, SUBACTIVITY_SELECT_IMAGE);
            }
        });
        mSendImageRecyclerView.setAdapter(sendImageAdapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSendEditText = null;
        mSendImageRecyclerView = null;
        sendImageList = null;
        mAddImageButton = null;

        sendImageAdapter = null;
        setContentView(R.layout.view_null);
    }


    private void returnNewMessage(Intent inent) {

        setResult(RESULT_OK, inent);
        Snackbar.make(mSendImageRecyclerView, "正在发送", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SUBACTIVITY_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    IntentDataParcelable idp = data.getParcelableExtra("Select_Img_Result");
                    ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
                    resultList = idp.mList;
                    sendImageList.clear();
                    sendImageList.addAll(resultList);
                    mHandler.obtainMessage(SUCCESS).sendToTarget();


                }
                if (resultCode == Activity.RESULT_CANCELED) {

                }
                break;
            case SUBACTIVITY_CHECK_IMAGE:
                if (resultCode == RESULT_OK) {
                    IntentDataParcelable idp = data
                            .getParcelableExtra("Select_Img_Result");
                    ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
                    resultList = idp.mList;
                    sendImageList.clear();
                    sendImageList.addAll(resultList);
                    mHandler.obtainMessage(SUCCESS).sendToTarget();


                }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showMyDialog();
                break;
        }
        return true;
    }

    private void showMyDialog() {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setMessage("要退出编辑吗?");
        materialDialog.setPositiveButton("继续编辑", new OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.setNegativeButton("退出", new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        materialDialog.show();
    }

    @Override
    public void click(View v, int position) {
        Intent intentParcelable = new Intent(MsgSend.this,
                ImageFullScreenActivity.class);
        IntentDataParcelable idp = new IntentDataParcelable();
        String filePath = (sendImageList.get(position).get("Image"));
        idp.mInt = position;
        idp.mStr = filePath;
        idp.mList = sendImageList;
        intentParcelable.putExtra("Image_Path", idp);
        startActivityForResult(intentParcelable, SUBACTIVITY_CHECK_IMAGE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.msg_send_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.send_message:

                Intent intentResult = new Intent();
                Intent intentSendMessageService = new Intent(context, SendMessageService.class);

                //获取用户输入的msgContent
                String msg_content = mSendEditText.getText().toString();
                //构造传输介质 包括文章内容和图片路径
                IntentDataParcelable intentDataParcelable = new IntentDataParcelable();
                intentDataParcelable.mInt = categoryId;
                intentDataParcelable.mStr = msg_content;
                if (sendImageList.size() != 0) {
                    intentDataParcelable.mList = sendImageList;

                } else {
                    intentDataParcelable.mList = null;
                }


                //将文章内容放入两个intent
                intentResult
                        .putExtra(getString(R.string.IntentExtraFlag_msgBody), intentDataParcelable);
                intentSendMessageService
                        .putExtra(getString(R.string.IntentExtraFlag_msgBody), intentDataParcelable);
                //向服务器发送消息
                startService(intentSendMessageService);
                //向MainActivity发送消息
                returnNewMessage(intentResult);
                finish();
                break;
            case android.R.id.home:
                showMyDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
