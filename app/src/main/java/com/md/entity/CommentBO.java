package com.md.entity;

public class CommentBO {

    private int commentId;
    private int userId;
    private String userName;
    private int messageId;
    private int refCommentId;
    private String refCommentUserName;

    private String content;
    private String time;

    public String getRefCommentUserName() {
        return refCommentUserName;
    }

    public void setRefCommentUserName(String refCommentUserName) {
        this.refCommentUserName = refCommentUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public int getRefCommentId() {
        return refCommentId;
    }

    public void setRefCommentId(int refCommentId) {
        this.refCommentId = refCommentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
