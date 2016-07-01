package com.md_c_test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.md.adapter.MsgContentAdapter;
import com.md.entity.CollectionBO;
import com.md.entity.CommentBO;
import com.md.entity.MessageBO;
import com.md.util.networkapi.CollectionApi;
import com.md.util.networkapi.CommentApi;
import com.md.util.request.ResponseListener;
import com.recyclerview.listener.ClickListener;
import com.recyclerview.listener.LongClickListener;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class MsgContent extends AppCompatActivity implements ClickListener,
        LongClickListener, View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final int NON_REFERENCE = -1;

    private ImageView msg_content_user_img;
    private TextView msg_content_user_name;
    private TextView msg_content_publish_time;
    private TextView msg_content;
    private ImageView appBarImage;
    private RecyclerView msg_comment_list_RecyclerView;
    private List<CommentBO> contentAndCommentList;
    private MsgContentAdapter adapter;
    private MessageBO messageBO;
    private TextView mTopRight;
    private AppBarLayout appBarLayout;
    private FloatingActionButton fab;
    private int isCollection = 0;
    private TextView commentBtnTv;
    private int userId;
    private String userName;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isRefreshing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.message_content_real);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initView();
        messageBO = getIntentData();
        initEvent();


        SharedPreferences sharedPreferences = MyApplication.getContext()
                .getSharedPreferences(MyApplication.getContext()
                        .getString(R.string.sharedPreference_userInfo), Context.MODE_PRIVATE);

        userId = sharedPreferences.getInt(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_userId), -1);
        userName = sharedPreferences.getString(getString(R.string.SharedPreference_userInfo_userName), "userName");


        contentAndCommentList = new ArrayList<>();
        contentAndCommentList.add(null);
        contentAndCommentList.add(null);

        adapter = new MsgContentAdapter(this, contentAndCommentList, messageBO);
        adapter.setClickListener(this);
        adapter.setLongClickListener(this);

        msg_comment_list_RecyclerView.setItemAnimator(new DefaultItemAnimator());
        msg_comment_list_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        msg_comment_list_RecyclerView.setAdapter(adapter);
        msg_comment_list_RecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRefreshing) {
                    //true表示动作已完成，不再发到下一层处理
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void initEvent() {

        commentBtnTv.setOnClickListener(this);

    }

    private void initView() {
        mTopRight = (TextView) findViewById(R.id.id_top_right);
        msg_comment_list_RecyclerView = (RecyclerView) findViewById(R.id.id_msg_content_list);
        appBarLayout = (AppBarLayout) findViewById(R.id.id_msg_content_app_bar);
        fab = (FloatingActionButton) findViewById(R.id.id_msg_content_fab);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_msg_content_swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(this);

        commentBtnTv = (TextView) findViewById(R.id.id_msg_comment_title_comment);
        mTopRight.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        toolbar.setTitle("详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CollectionBO collectionBO = new CollectionBO(userId, messageBO.getMsgId());
                if (isCollection == 0) {


                    CollectionApi.collect(collectionBO, new ResponseListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(commentBtnTv, "收藏失败", Snackbar.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onResponse(Object o) {
                            isCollection = 1;
                            fab.setImageResource(R.mipmap.love_3);
                            Snackbar.make(commentBtnTv, "已收藏", Snackbar.LENGTH_SHORT).show();
                        }
                    });

                } else {


                    CollectionApi.unCollect(collectionBO, new ResponseListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(commentBtnTv, "取消收藏失败", Snackbar.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onResponse(Object o) {
                            isCollection = 0;
                            fab.setImageResource(R.mipmap.love_1);
                            Snackbar.make(commentBtnTv, "已取消收藏", Snackbar.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                /**
                 * verticalOffset changes in diapason
                 * from 0 - appBar is fully unwrapped
                 * to -appBarLayout's height - appBar is totally collapsed
                 * so in example we hide FAB when user folds half of the appBarLayout
                 */
                if (appBarLayout.getHeight() / 2 < -verticalOffset) {
                    fab.setVisibility(View.GONE);
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_msg_comment_title_comment:

                //直接对message进行评论，传递非法参数进行判断

                sendMessageDialog(NON_REFERENCE, "");
                break;
        }

    }

    //服务器根据refCommentId 查找user 若找到则设置为refId
    //否则设置为null 表示是对原消息的直接评论
    private void sendMessageDialog(final int refCommentId, final String refUserName) {
        final EditText editText = new EditText(MsgContent.this);
        TextInputLayout textInputLayout = new TextInputLayout(MsgContent.this);
        textInputLayout.addView(editText);

        textInputLayout.setHint("回复" + refUserName + ":");
        final MaterialDialog materialDialog = new MaterialDialog(MsgContent.this);
        materialDialog.setTitle("评论").setContentView(textInputLayout)
                .setPositiveButton("发送", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommentBO commentBO = new CommentBO();
                        commentBO.setMessageId(messageBO.getMsgId());
                        commentBO.setUserId(userId);
                        commentBO.setUserName(userName);
                        commentBO.setRefCommentId(refCommentId);
                        commentBO.setRefCommentUserName(refUserName);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                        String date = format.format(new Date());

                        commentBO.setTime(date);
                        commentBO.setContent(editText.getText().toString());
                        //插入到第三个位置，即直接显示为第一条评论
                        contentAndCommentList.add(2, commentBO);
                        adapter.notifyItemInserted(2);
                        materialDialog.dismiss();
                        CommentApi.comment(commentBO, new ResponseListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Snackbar.make(commentBtnTv, "评论未完成", Snackbar.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onResponse(Object o) {

                                Snackbar.make(commentBtnTv, "评论已完成", Snackbar.LENGTH_SHORT).show();

                            }
                        });

                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();


    }

    public MessageBO getIntentData() {
        Intent dataIntent = getIntent();
        MessageBO msg = new MessageBO();
        msg = dataIntent.getParcelableExtra(getString(R.string.Flag_msgContent));
        return msg;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void click(View v, int position) {
        int id = v.getId();
        switch (id) {
            case R.id.id_comment_time:
                Snackbar.make(v, "time", Snackbar.LENGTH_SHORT).show();


            case R.id.id_comment_img:
                Snackbar.make(v, "img", Snackbar.LENGTH_SHORT).show();


            case R.id.id_comment_name:
                Snackbar.make(v, "name", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(MsgContent.this, UserDetailActivity.class);
                intent.putExtra("userId", contentAndCommentList.get(position).getUserId());
                //输入标记，表示查看用户信息
                intent.putExtra(getString(R.string.UserDetailStartFlag),
                        UserDetailActivity.CHECK_INFO);

                startActivity(intent);
                break;


            case R.id.id_comment_layout:
                int refCommentId = contentAndCommentList.get(position).getCommentId();
                int commentUserId = contentAndCommentList.get(position).getUserId();
                String refUserName = contentAndCommentList.get(position).getRefCommentUserName();
                if (commentUserId == userId) {
                    Snackbar.make(v, "请不要对自己的评论进行回复", Snackbar.LENGTH_SHORT).show();
                } else {
                    sendMessageDialog(refCommentId, refUserName);
                    Snackbar.make(v, "layout", Snackbar.LENGTH_SHORT).show();
                }


                break;

        }
    }

    @Override
    public void longClick(View v, int position) {

    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        swipeRefreshLayout.setRefreshing(true);

        CommentApi.getAllComment(messageBO.getMsgId(), new ResponseListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(commentBtnTv, "获取评论失败", Snackbar.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                isRefreshing = false;
            }

            @Override
            public void onResponse(Object o) {
                Snackbar.make(commentBtnTv, "获取评论成功", Snackbar.LENGTH_SHORT).show();
                String result = (String) o;
                Type type = new TypeToken<List<CommentBO>>() {
                }.getType();
                Gson gsonToMessageBOList = new Gson();
                List<CommentBO> commentBOs = gsonToMessageBOList.fromJson(result, type);
                //contentAndCommentList.clear();
                int listSize = contentAndCommentList.size();

                contentAndCommentList.addAll(commentBOs);
                //从第一条评论开始移除所有评论
                //这里好像没有动态删除的问题 可能有bug
                for (int i = 2; i < listSize; i++) {
                    contentAndCommentList.remove(i);
                }
                /*for(CommentBO commentBO :commentBOs){
                    Log.e("评论", commentBO.getContent());
                    contentAndCommentList.add(commentBO);
                }*/

                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                isRefreshing = false;

            }
        });
    }
}
