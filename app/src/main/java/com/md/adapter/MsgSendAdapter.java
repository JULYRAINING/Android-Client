package com.md.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.md.util.LoadImageApi;
import com.md_c_test.R;
import com.recyclerview.listener.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MsgSendAdapter extends RecyclerView.Adapter {

    private ArrayList<HashMap<String, String>> list;
    private Context context;
    private ClickListener clickListener;

    public MsgSendAdapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.context = context;


    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.nine_image_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        LoadImageApi.display(viewHolder.mSendImg, list.get(position).get("Image"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mSendImg;

        public ViewHolder(View itemView) {
            super(itemView);
            mSendImg = (ImageView) itemView.findViewById(R.id.id_nine_item_image);
            mSendImg.setOnClickListener(this);
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
}
