package com.com.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.db.dao.MessageDbDao;
import com.db.daoImpl.MessageDbDaoImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.md.adapter.MsgAdapter;
import com.md.asynctask.SendMsgTask;
import com.md.entity.MessageBO;
import com.md.parcelable.IntentDataParcelable;
import com.md_c_test.MsgContent;
import com.md_c_test.MsgSend;
import com.md_c_test.MyApplication;
import com.md_c_test.R;
import com.md_c_test.UserDetailActivity;
import com.recyclerview.listener.ClickListener;
import com.recyclerview.listener.LongClickListener;
import com.service.GetMessageService;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FragmentMsg extends Fragment implements ClickListener, LongClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    //代表MsgSend这个Activity，
    //在调用startActivityForResult（Intent,Flag）时作为Flag
    // 用来在onActivityResult中判断获取到的数据从何处来
    public static final int SEND_MESSAGE_AVTIVITY = 1;
    //自此id向上请求message（向最新）
    private static final int GET_NEW_MESSAGE = 1;
    //自此id向下请求message（向之前）
    private static final int GET_OLDER_MESSAGE = 0;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    //FragmentMsg中UI显示的数据源
    private List<MessageBO> messageList;
    //ReciclerView的适配器
    private MsgAdapter adapter;


    //SharedPreference
    //在本Fragment中用于读取messgae信息
    private SharedPreferences sharedPreferences;
    //接收从MsgSend传递过来的图片信息
    private List<HashMap<String, String>> resultImgList;
    //解析图片路径
    private String imagePathStr;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private GetMsgBroadcastReceiver getMsgBroadcastReceiver;
    private IntentFilter getMsgIntentFilter;
    private MessageDbDao messageDbDao;
    //sqlite中messageId的最大值
    private int maxMessageIdInDb = 0;
    private int SCORLL_DIRECT = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        messageDbDao = new MessageDbDaoImpl(MyApplication.getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = initData(inflater, container);

        //初始化sharedPreferences
        sharedPreferences = getActivity().
                getSharedPreferences(getString(R.string.sharedPreference_userInfo), getActivity().MODE_PRIVATE);

        //MsgBroadcastReceiver为内部类,负责刷新视图recyclerView

        // 动态注册BroadcastReceiver，action为GetMessageService.UNIQUE_STRING
        getMsgBroadcastReceiver = new GetMsgBroadcastReceiver();
        getMsgIntentFilter = new IntentFilter();
        getMsgIntentFilter.addAction(GetMessageService.UNIQUE_STRING);


        bindToAdapter();


        //启动Service,请求网络数据message
        //
        loadMessages();


        return rootView;

    }

    private void bindToAdapter() {
        adapter = new MsgAdapter(FragmentMsg.this, messageList);
        adapter.setClickListener(FragmentMsg.this);
        adapter.setLongClickListener(FragmentMsg.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    private void loadMessages() {


        if (messageList.size() != 0) {
            bindToAdapter();
            Log.e("进入", "1");
            swipeRefreshLayout.setRefreshing(false);
        } else {

            Log.e("进入", "2");
            maxMessageIdInDb = messageDbDao.getMaxMessageId();
            messageList = messageDbDao.getMessageInTheRangeOf(maxMessageIdInDb, maxMessageIdInDb - 20);
            Log.e("MaxId", maxMessageIdInDb + "");

            if (messageList.size() != 0) {
                bindToAdapter();

                Log.e("进入", "3");
                swipeRefreshLayout.setRefreshing(false);

            } else {
                Log.e("进入", "4");
                maxMessageIdInDb = messageDbDao.getMaxMessageId();
                getMessagesFromAnyWhere(GET_NEW_MESSAGE, 0, maxMessageIdInDb);

            }
        }


    }

    private void getMessagesFromAnyWhere(int flag, int startId, int endId) {
        Intent getMessageIntent = new Intent(getActivity(), GetMessageService.class);
        getMessageIntent.setAction(GetMessageService.UNIQUE_STRING);
        getMessageIntent.putExtra(
                getString(R.string.getMessageStartId), startId);
        getMessageIntent.putExtra(
                getString(R.string.getMessageFlag), flag);
        getMessageIntent.putExtra(
                getString(R.string.getMessageEndId), endId);

        getActivity().startService(getMessageIntent);
    }


    @NonNull
    private View initData(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.tab_message, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.id_msg_recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.id_msg_swipe);

        linearLayoutManager = new LinearLayoutManager(getContext());
        //Fragment创建时显示进度条
        //设置偏移量
        swipeRefreshLayout.setProgressViewOffset(false, 0, 100);
        swipeRefreshLayout.setRefreshing(true);

        //设置进度条颜色，最多四种颜色，交替出现
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //监听刷新事件
        swipeRefreshLayout.setOnRefreshListener(this);


        //下面这个方法几乎没什么用
        /*recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRefreshing) {
                    //true表示动作已完成，不再发到下一层处理
                    return true;
                } else {
                    return false;
                }
            }
        });*/
        //当recyclerView为空时，不能去touch，否则会出现异常crash
        messageList = new ArrayList<>();
        adapter = new MsgAdapter(FragmentMsg.this, messageList);
        adapter.setClickListener(FragmentMsg.this);
        adapter.setLongClickListener(FragmentMsg.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        //必须在初始化recyclerView后才可以响应此方法，否则touch的时候会出现NullPointerException
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //判断最后一条item是否已经显示，若是，则刷新,加载之前的message
                //
                if (newState == RecyclerView.SCROLL_STATE_SETTLING
                        && lastVisibleItem + 1 == adapter.getItemCount()
                        && !swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                    //若此时界面有message存在，则请求当前显示的message之前的20条数据
                    if (lastVisibleItem != 0 && messageList.size() != 0) {
                        //从sqlite或server获取较早的message
                        getMessagesFromAnyWhere(
                                GET_OLDER_MESSAGE,
                                messageList.get(lastVisibleItem - 1).getMsgId() - 1,
                                messageList.get(lastVisibleItem - 1).getMsgId() - 20);

                    }
                    //否则加载本地数据库中最新的20条数据
                    else {
                        getMessagesFromAnyWhere(
                                GET_OLDER_MESSAGE,
                                maxMessageIdInDb,
                                maxMessageIdInDb - 20);

                    }


                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    //手指向上滑动，有滑到底部的趋势
                    SCORLL_DIRECT = 1;
                } else if (dy < 0) {
                    //手指向下滑动，有滑到顶部的趋势
                    SCORLL_DIRECT = -1;
                }
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.e("滑动", dx + " " + dy);

            }
        });


        return rootView;
    }

    /**
     * 当从子Ativity返回时，调用此方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SEND_MESSAGE_AVTIVITY:
                //弃用 不接收MsgSend的数据，改为直接向服务器请求
                //~~~~~~~~~~~~~~~~``
                //若成功返回，显示新message
                if (resultCode == MsgSend.RESULT_OK) {
                    //showNewMessage(data);

                }
                break;
        }

    }

    private void showNewMessage(Intent data) {


        IntentDataParcelable intentDataParcelable = data.getParcelableExtra(getString(R.string.IntentExtraFlag_msgBody));
        String msg_contentText = intentDataParcelable.mStr;
        resultImgList = intentDataParcelable.mList;
        Gson gson_imgToPath = new Gson();
        imagePathStr = gson_imgToPath.toJson(resultImgList);

        /*Log.e("输出",imagePathStr);

        Type listType = new TypeToken<List<HashMap>>(){}.getType();
        resultImgList = gson_imgToPath.fromJson(imagePathStr,listType);
        Log.e("输出",imgList.toString());*/

        String user_name = sharedPreferences.getString("userName", getResources().getString(R.string.default_username));
        String user_img = sharedPreferences.getString("userImage", "assets://msg_content_user_img.png");

        MessageBO msg = new MessageBO();
        Time time = new Time();
        time.setToNow();

        int year = time.year;
        //Android count month from 0 but 1;
        int month = time.month + 1;
        int day = time.monthDay;
        int hour = time.hour;
        int minute = time.minute;
        int second = time.second;

        Toast.makeText(getActivity(), "主界面Fragment已收到信息", Toast.LENGTH_SHORT).show();
        msg.setMsgSendTime(getResources().getString(R.string.default_time));
        msg.setMsgContent(msg_contentText);
        msg.setMsgSender(user_name);
        msg.setMsgSenderImg(user_img);
        msg.setImagePathListStr(imagePathStr);

        StringBuilder str_time = new StringBuilder();
        str_time.append(year).append("-")
                .append(month).append("-")
                .append(day).append(" ")
                .append(hour).append(":")
                .append(minute).append(":")
                .append(second).append(":");
        msg.setMsgSendTime(str_time.toString());

        Collections.reverse(messageList);
        messageList.add(msg);
        Collections.reverse(messageList);
        adapter.notifyDataSetChanged();


        Gson gson = new Gson();
        String gsonString = gson.toJson(msg);


        SendMsgTask sendMsgTask = new SendMsgTask();
        sendMsgTask.execute(gsonString);


    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        maxMessageIdInDb = messageDbDao.getMaxMessageId();
        getMessagesFromAnyWhere(GET_NEW_MESSAGE, 0, maxMessageIdInDb);
    }


    /**
     * 点击事件，跳转
     *
     * @param v        点击的视图
     * @param position 点击的位置
     */
    @Override
    public void click(View v, int position) {
        int id = v.getId();
        switch (id) {
            case R.id.id_msg_content_user_img:
                Log.e("点击", "id_msg_content_user_img");
            case R.id.id_msg_content_publish_time:
                Log.e("点击", "id_msg_content_publish_time");
            case R.id.id_msg_content_user_name:
                Log.e("点击", "id_msg_content_user_name");
                Intent intent = new Intent(getContext(), UserDetailActivity.class);
                intent.putExtra("userId", messageList.get(position).getUserId());
                //输入标记，表示查看用户信息
                intent.putExtra(getString(R.string.UserDetailStartFlag),
                        UserDetailActivity.CHECK_INFO);
                startActivity(intent);
                break;
            case R.id.id_msg_content:
                Log.e("点击", "id_msg_content");
            case R.id.id_message_layout:
                Log.e("点击", "id_message_layout");
                //查看文章内容
                Intent intentToMsgSend = new Intent(getContext(), MsgContent.class);
                MessageBO msg = messageList.get(position);
                intentToMsgSend.putExtra(getString(R.string.Flag_msgContent), msg);
                getActivity().startActivity(intentToMsgSend);
                break;
        }

    }

    @Override
    public void longClick(View v, int position) {
        Snackbar.make(v, v.getId() + "长按" + position, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(getMsgBroadcastReceiver, getMsgIntentFilter);


    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(getMsgBroadcastReceiver);
    }

    public class GetMsgBroadcastReceiver extends BroadcastReceiver {
        public GetMsgBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(GetMessageService.UNIQUE_STRING)) {

                String messagesJson = intent.getStringExtra("messages");

                Type type = new TypeToken<List<MessageBO>>() {
                }.getType();
                Gson gson = new Gson();
                List<MessageBO> newData = gson.fromJson(messagesJson, type);

                //如果获取到的为空，则表示无新内容 或查询失败
                if (newData != null) {

                    if (newData.size() != 0) {

                        //若获得的message中，最大id大于本地sqlite的最大id， 则表示获取到了新内容
                        maxMessageIdInDb = messageDbDao.getMaxMessageId();
                        if (newData.get(0).getMsgId() > maxMessageIdInDb) {
                            messageList.clear();
                            messageList.addAll(newData);
                            bindToAdapter();
                            Snackbar.make(recyclerView, newData.size() + "条新消息", Snackbar.LENGTH_SHORT).show();
                        } else {
                            int listSize = messageList.size();

                            messageList.addAll(newData);
                            adapter.notifyItemInserted(listSize);
                        }


                        swipeRefreshLayout.setRefreshing(false);

                        //将获取的message数据存储到数据库

                        for (MessageBO messageBO : messageList) {
                            messageDbDao.addMessage(messageBO);
                        }
                    } else {
                        Snackbar.make(recyclerView, "无新信息", Snackbar.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } else {
                    Snackbar.make(recyclerView, "无新信息", Snackbar.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }


            }
        }
    }

}
