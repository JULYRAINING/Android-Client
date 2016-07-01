package com.md.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageBO implements Parcelable {
    public MessageBO() {
    }

    //自增序列
    private int msgId;
    //发布者id
    private int userId;
    //是否删除
    private int isDelete;
    //发布者
    private String msgSender;
    //发布时间
    private String msgSendTime;
    //发布者头像
    private String msgSenderImg;
    //发布内容
    private String msgContent;
    private int msgApprove;
    private int msgOppose;
    private String title;
    private String imagePathListStr;
    private int categoryId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getImagePathListStr() {
        return imagePathListStr;
    }

    public void setImagePathListStr(String imagePathListStr) {
        this.imagePathListStr = imagePathListStr;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getMsgApprove() {
        return msgApprove;
    }

    public void setMsgApprove(int msgApprove) {
        this.msgApprove = msgApprove;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgOppose() {
        return msgOppose;
    }

    public void setMsgOppose(int msgOppose) {
        this.msgOppose = msgOppose;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    public String getMsgSenderImg() {
        return msgSenderImg;
    }

    public void setMsgSenderImg(String msgSenderImg) {
        this.msgSenderImg = msgSenderImg;
    }

    public String getMsgSendTime() {
        return msgSendTime;
    }

    public void setMsgSendTime(String msgSendTime) {
        this.msgSendTime = msgSendTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.msgId);
        dest.writeInt(this.userId);
        dest.writeInt(this.isDelete);
        dest.writeString(this.msgSender);
        dest.writeString(this.msgSendTime);
        dest.writeString(this.msgSenderImg);
        dest.writeString(this.msgContent);
        dest.writeInt(this.msgApprove);
        dest.writeInt(this.msgOppose);
        dest.writeString(this.title);
        dest.writeString(this.imagePathListStr);
        dest.writeInt(this.categoryId);
    }

    protected MessageBO(Parcel in) {
        this.msgId = in.readInt();
        this.userId = in.readInt();
        this.isDelete = in.readInt();
        this.msgSender = in.readString();
        this.msgSendTime = in.readString();
        this.msgSenderImg = in.readString();
        this.msgContent = in.readString();
        this.msgApprove = in.readInt();
        this.msgOppose = in.readInt();
        this.title = in.readString();
        this.imagePathListStr = in.readString();
        this.categoryId = in.readInt();
    }

    public static final Creator<MessageBO> CREATOR = new Creator<MessageBO>() {
        public MessageBO createFromParcel(Parcel source) {
            return new MessageBO(source);
        }

        public MessageBO[] newArray(int size) {
            return new MessageBO[size];
        }
    };
}
