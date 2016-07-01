package com.md.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.md.util.LoadImageApi;
import com.md.util.net.UrlString;
import com.md_c_test.R;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/1/1.
 */
public class MsgContentNestedAdapter extends RecyclerView.Adapter<MsgContentNestedAdapter.ViewHolder> {
    private String getMessageImageUrl = UrlString.messageGetImageUrl;
    private Context context;
    private List<HashMap<String, String>> imgList;

    public MsgContentNestedAdapter(Context context, String imgListStr) {
        this.context = context;

        Log.e("加载", "内层adapter");
        Log.e("imageStr", imgListStr);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<HashMap>>() {
        }.getType();
        imgList = gson.fromJson(imgListStr, listType);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.nine_image_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        Log.e("加载", "内层adapter视图已创建");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String fileName = imgList.get(position).get("Image");
        StringBuilder sb = new StringBuilder();
        sb.append(getMessageImageUrl).append("?").append("Image").append("=").append(fileName);

        LoadImageApi.displayServer(holder.imageView, sb.toString());
        Log.e("加载", "内容已加载");
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.id_nine_item_image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(itemView, "" + getAdapterPosition(), Snackbar.LENGTH_SHORT).show();

                }
            });
        }
    }
}
