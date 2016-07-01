package com.db.dao;

import com.md.entity.MessageBO;

import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/1/21.
 */
public interface MessageDbDao {

    public void addMessage(MessageBO messageBO);

    public void deleteMessageById(int id);

    public List<MessageBO> getMessage();

    public List<MessageBO> getMessageInTheRangeOf(int startId, int endId);

    public List<MessageBO> getMessageToUpStartAtId(int id);

    public int getMaxMessageId();

    public int getMinMessageId();

}
