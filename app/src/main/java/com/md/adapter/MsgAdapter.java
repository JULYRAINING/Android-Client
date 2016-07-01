package com.md.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.entity.MessageBO;
import com.md.util.LoadImageApi;
import com.md.util.net.UrlString;
import com.md_c_test.MyApplication;
import com.md_c_test.R;
import com.recyclerview.listener.ClickListener;
import com.recyclerview.listener.LongClickListener;

import java.util.List;

/**
 * Created by SECONDHEAVEN on 2015/12/13.
 */
public class MsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Fragment f;
    private List<MessageBO> list;
    private ClickListener clickListener;
    private LongClickListener longClickListener;
    private static final int OtherItemType = 1;
    private static final int FinalItemType = 0;
    private String token;
    private int userId;

    public MsgAdapter(Fragment f, List<MessageBO> list) {
        this.f = f;
        this.list = list;


        //为了拼接验证token字符串，总感觉效率太低，暂且这样写着
        //读取token信息
        SharedPreferences sharedPreferences = MyApplication.getContext()
                .getSharedPreferences(MyApplication.getContext()
                        .getString(R.string.sharedPreferences_tokenInfo), Context.MODE_PRIVATE);

        userId = sharedPreferences.getInt(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_userId), -1);
        token = sharedPreferences.getString(MyApplication.getContext()
                .getString(R.string.sharedPreference_tokenInfo_token), "");
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == OtherItemType) {
            LayoutInflater layoutInflater = LayoutInflater.from(f.getContext());
            View view = layoutInflater.inflate(R.layout.tab_message_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(f.getContext());
            View view = layoutInflater.inflate(R.layout.recyclerview_final_item, null);
            FinalView finalView = new FinalView(view);
            return finalView;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.msg_content_user_name.setText(list.get(position).getMsgSender());
            //LoadImageApi.display(viewHolder.msg_content_user_img, "assets://msg_content_user_img.png");


            String imageUrl = UrlString.userGetImageUrl;
            String params = "?userId=" + list.get(position).getUserId();

            String tokenInfo = "&token={\"token\":\"" + token + "\",\"userId\":" + userId + "}";

            String url = imageUrl + params + tokenInfo;
            LoadImageApi.display(viewHolder.msg_content_user_img,
                    url);
            viewHolder.msg_content.setText(list.get(position).getMsgContent());
            //2013-02-26 20:13:57.0
            String date = list.get(position).getMsgSendTime();
            viewHolder.msg_content_publish_time.setText(date.substring(5, 16));
            viewHolder.msg_content_layout.setTag(position);
        }

    }

    @Override
    public int getItemCount() {
        //添加一个尾部提示语
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FinalItemType;
        } else {
            return OtherItemType;
        }

    }

    class FinalView extends RecyclerView.ViewHolder {

        public FinalView(View itemView) {
            super(itemView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView msg_content_user_img;
        private TextView msg_content_user_name;
        private TextView msg_content_publish_time;
        private TextView msg_content;
        private LinearLayout msg_content_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            msg_content_user_img = (ImageView) itemView
                    .findViewById(R.id.id_msg_content_user_img);

            msg_content_publish_time = (TextView) itemView
                    .findViewById(R.id.id_msg_content_publish_time);
            msg_content_user_name = (TextView) itemView
                    .findViewById(R.id.id_msg_content_user_name);
            msg_content = (TextView) itemView
                    .findViewById(R.id.id_msg_content);
            msg_content_layout = (LinearLayout) itemView
                    .findViewById(R.id.id_message_layout);


            msg_content_user_img.setOnClickListener(this);
            msg_content_publish_time.setOnClickListener(this);
            msg_content_user_name.setOnClickListener(this);
            //msg_content.setOnClickListener(this);
            msg_content_layout.setOnClickListener(this);
            msg_content_layout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {


            clickListener.click(v, getAdapterPosition());


        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.longClick(v, getAdapterPosition());
            return true;
        }
    }
}
