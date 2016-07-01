package com.md.util;

import android.support.v4.app.FragmentActivity;

import com.md.entity.MessageBO;
import com.md_c_test.MainActivity;
import com.md_c_test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SECONDHEAVEN on 2015/9/11.
 */
public class GetMsgListUtils {
    public List<MessageBO> list;
    private MainActivity context;

    public GetMsgListUtils() {
    }

    public GetMsgListUtils(FragmentActivity context) {
        this.context = (MainActivity) context;
    }

    public List<MessageBO> getMessage() {
        list = new ArrayList<MessageBO>();

        for (int i = 0; i <= 10; i++) {
            MessageBO msg = new MessageBO();
            msg.setUserId(711);
            msg.setMsgSender(context.getResources().getString(R.string.default_message_username));
            msg.setMsgSenderImg("assets://msg_content_user_img.png");
            msg.setMsgContent(context.getResources().getString(R.string.default_message_content));
            msg.setMsgSendTime(context.getResources().getString(R.string.default_time));
            list.add(msg);
        }

        return list;
    }
}
