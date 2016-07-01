package com.md.entity;

public class CollectionBO {

    private int userId;
    private int messageId;

    public CollectionBO(int userId, int messageId) {
        this.userId = userId;
        this.messageId = messageId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


}
