package com.md.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.md.util.LoadImageApi;
import com.md.view.MyGridView;
import com.md_c_test.R;
import com.recyclerview.listener.ClickListener;
import com.recyclerview.listener.LongClickListener;

import java.util.HashMap;
import java.util.List;

public class UserSelectImageAdapter extends RecyclerView.Adapter {

    private List<HashMap<String, String>> list;
    private Context context;

    private ClickListener clickListener;
    private LongClickListener longClickListener;

    public UserSelectImageAdapter(Context context, List<HashMap<String, String>> list) {
        this.context = context;
        this.list = list;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_select_image_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        LoadImageApi.display(viewHolder.mImgItem, list.get(position).get("Image"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView mImgItem;

        private MyGridView myGridView;

        public ViewHolder(View itemView) {
            super(itemView);
            myGridView = (MyGridView) itemView.findViewById(R.id.id_user_select_image_item_view);
            mImgItem = (ImageView) itemView.findViewById(R.id.id_user_select_image_item_image);

            myGridView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            clickListener.
                    click(v, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.longClick(v, getAdapterPosition());
            return true;
        }

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
