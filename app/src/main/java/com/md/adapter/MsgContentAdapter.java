package com.md.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.md.entity.CommentBO;
import com.md.entity.MessageBO;
import com.md.util.LoadImageApi;
import com.md.util.net.UrlString;
import com.md.view.MyGridLayoutManager;
import com.md_c_test.R;
import com.recyclerview.listener.ClickListener;
import com.recyclerview.listener.LongClickListener;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SECONDHEAVEN on 2015/12/26.
 */
public class MsgContentAdapter extends RecyclerView.Adapter {
    private String getMessageImageUrl = UrlString.messageGetImageUrl;
    String imageUrl = UrlString.userGetImageUrl;
    private Context context;
    private List<CommentBO> list;
    private MessageBO msgContent;
    private ClickListener clickListener;
    private LongClickListener longClickListener;


    public MsgContentAdapter(Context context, List<CommentBO> list, MessageBO msgContent) {
        this.context = context;
        this.list = list;
        this.msgContent = msgContent;

        Log.e("加载", "外层adapter");

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if (viewType == 0) {

            View view = layoutInflater.inflate(R.layout.message_content, null);
            MsgViewHolder msgViewHolder = new MsgViewHolder(view);
            return msgViewHolder;
        } else if (viewType == 1) {
            View view = layoutInflater.inflate(R.layout.msg_comment_comment, null);
            CommentTitleVeiwHolder commentTitleVeiwHolder = new CommentTitleVeiwHolder(view);
            return commentTitleVeiwHolder;
        } else {
            View view = layoutInflater.inflate(R.layout.comment_list, null);
            CommentViewHolder commentViewHolder = new CommentViewHolder(view);
            return commentViewHolder;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            MsgViewHolder msgViewHolder = (MsgViewHolder) holder;


            String params = "?userId=" + msgContent.getUserId();


            String url = imageUrl + params;
            LoadImageApi.displayServer(msgViewHolder.msg_content_user_img,
                    url);
            //2013-02-26 20:13:57.0
            String date = msgContent.getMsgSendTime();
            msgViewHolder.msg_content_publish_time.setText(date.substring(5, 16));
            msgViewHolder.msg_content_user_name.setText(msgContent.getMsgSender());
            msgViewHolder.msg_content.setText(msgContent.getMsgContent());

            String imageListStr = msgContent.getImagePathListStr();
            List<HashMap<String, String>> imgList;
            Gson gson = new Gson();
            Type listType = new TypeToken<List<HashMap>>() {
            }.getType();
            imgList = gson.fromJson(imageListStr, listType);
            if (imageListStr == null) {

            } else {
                for(int i = 0; i<imgList.size(); i++){
                    ImageView imageView = new ImageView(context);
                    String fileName = imgList.get(position).get("Image");
                    StringBuilder sb = new StringBuilder();
                    sb.append(getMessageImageUrl).append("?").append("Image").append("=").append(fileName);

                    LoadImageApi.displayServer(imageView, sb.toString());
                    msgViewHolder.images_layout.addView(imageView);
                }
            }


        } else if (position == 1) {

        } else {
            CommentViewHolder commentViewHolder = (CommentViewHolder) holder;
            commentViewHolder.commentName.setText(String.valueOf(list.get(position).getUserName()));
            commentViewHolder.commentContent.setText("回复" + list.get(position).getRefCommentUserName() + ":" + list.get(position).getContent());


            //2013-02-26 20:13:57.0

            String date = list.get(position).getTime();
            //02-26 20:13
            commentViewHolder.commentTime.setText(date.substring(5, 16));


            String params = "?userId=" + list.get(position).getUserId();

            String url = imageUrl + params;
            LoadImageApi.displayServer(commentViewHolder.commentImg,
                    url);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView msg_content_user_img;
        private TextView msg_content_user_name;
        private TextView msg_content_publish_time;
        private TextView msg_content;
        private LinearLayout images_layout;

        public MsgViewHolder(View itemView) {
            super(itemView);
            msg_content_user_img = (ImageView) itemView
                    .findViewById(R.id.id_msg_content_user_img);
            msg_content_publish_time = (TextView) itemView
                    .findViewById(R.id.id_msg_content_publish_time);
            msg_content_user_name = (TextView) itemView
                    .findViewById(R.id.id_msg_content_user_name);
            msg_content = (TextView) itemView
                    .findViewById(R.id.id_msg_content);
            images_layout = (LinearLayout) itemView
                    .findViewById(R.id.images_layout);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView commentImg;
        private TextView commentName;
        private TextView commentTime;
        private TextView commentContent;
        private RelativeLayout commentLayout;

        public CommentViewHolder(View itemView) {
            super(itemView);
            commentName = (TextView) itemView.findViewById(R.id.id_comment_name);
            commentImg = (ImageView) itemView.findViewById(R.id.id_comment_img);
            commentTime = (TextView) itemView.findViewById(R.id.id_comment_time);
            commentContent = (TextView) itemView.findViewById(R.id.id_comment_content);
            commentLayout = (RelativeLayout) itemView.findViewById(R.id.id_comment_layout);

            LoadImageApi.display(commentImg, "assets://msg_content_user_img.png");
            commentName.setOnClickListener(this);
            commentImg.setOnClickListener(this);
            commentTime.setOnClickListener(this);
            //commentContent.setOnClickListener(this);
            commentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.click(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    class CommentTitleVeiwHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private LinearLayout linearLayout;
        private TextView approve_tv;
        private TextView commend_tv;
        private TextView tip_tv;

        public CommentTitleVeiwHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}

