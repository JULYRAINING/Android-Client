package com.md.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.entity.CupNotice;
import com.md_c_test.R;
import com.recyclerview.listener.ClickListener;

import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/3/15.
 */
public class NoticeAdapter extends RecyclerView.Adapter {
    private List<CupNotice> list;
    private Context context;
    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    public NoticeAdapter(Context context, List<CupNotice> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.fragment_notice_recyc_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.notice_title.setText(list.get(position).getTitle().substring(2));
        viewHolder.notice_category.setText("");
        viewHolder.notice_date.setText(list.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView notice_title;
        private TextView notice_category;
        private TextView notice_date;
        private LinearLayout notice_item;

        public ViewHolder(View itemView) {
            super(itemView);
            notice_item = (LinearLayout) itemView.findViewById(R.id.id_fragment_notice_item);
            notice_title = (TextView) itemView.findViewById(R.id.fragment_notice_title);
            notice_category = (TextView) itemView.findViewById(R.id.fragment_notice_category);
            notice_date = (TextView) itemView.findViewById(R.id.fragment_notice_date);
            notice_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.click(v, getAdapterPosition());
        }
    }
}
