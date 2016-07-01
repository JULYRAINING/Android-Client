package com.md.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.com.fragment.FragmentMsg;
import com.md.entity.MessageBO;
import com.md_c_test.MsgContent;
import com.md_c_test.R;

import java.util.List;

public class MsgListAdapter extends BaseAdapter implements OnClickListener {
    private List<MessageBO> list;
    private FragmentMsg f;

    public MsgListAdapter(FragmentMsg f, List<MessageBO> list) {
        this.f = f;
        this.list = list;// TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressWarnings("null")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(f.getActivity());
        ViewHolder viewholder = null;
        viewholder = new ViewHolder();

        // LayoutInflater inflater = Context.LAYOUT_INFLATER_SERVICE;
        convertView = inflater.inflate(R.layout.tab_message_list, null);

        initView(convertView, viewholder, position);

        initEvent(viewholder);
        showData(viewholder, position);
        return convertView;
    }

    private void showData(ViewHolder viewholder, int position) {
        viewholder.msg_content_user_name.setText(list.get(position).getMsgSender());
        //viewholder.msg_content_user_img.setImageResource(list.get(position).getMsg_sender_img());
        viewholder.msg_content.setText(list.get(position).getMsgContent());
        viewholder.msg_content_publish_time.setText(list.get(position).getMsgSendTime());

    }

    private void initView(View convertView, ViewHolder viewholder, int position) {
        viewholder.msg_content_user_img = (ImageView) convertView
                .findViewById(R.id.id_msg_content_user_img);
        viewholder.msg_content_publish_time = (TextView) convertView
                .findViewById(R.id.id_msg_content_publish_time);
        viewholder.msg_content_user_name = (TextView) convertView
                .findViewById(R.id.id_msg_content_user_name);
        viewholder.msg_content = (TextView) convertView
                .findViewById(R.id.id_msg_content);
        viewholder.msg_content_layout = (LinearLayout) convertView
                .findViewById(R.id.id_msg_content_layout);

        viewholder.msg_content_layout.setTag(position);
    }

    private void initEvent(ViewHolder viewholder) {
        viewholder.msg_content_layout.setOnClickListener(this);
        viewholder.msg_content_user_img.setOnClickListener(this);
        viewholder.msg_content_publish_time.setOnClickListener(this);
        viewholder.msg_content_user_name.setOnClickListener(this);
    }

    class ViewHolder {
        private ImageView msg_content_user_img;
        private TextView msg_content_user_name;
        private TextView msg_content_publish_time;
        private TextView msg_content;
        private LinearLayout msg_content_layout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_msg_content_user_img:

                break;
            case R.id.id_msg_content_publish_time:
                break;
            case R.id.id_msg_content_user_name:
                break;
            case R.id.id_msg_content_layout:
                int pos = (int) v.getTag();
                Intent intent2 = new Intent(f.getActivity(), MsgContent.class);
                MessageBO msg = list.get(pos);
                intent2.putExtra("messageContent", msg);
                f.getActivity().startActivity(intent2);
                break;
        }

    }
}
