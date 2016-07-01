package com.db.daoImpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.db.dao.MessageDbDao;
import com.db.helper.DBHelper;
import com.md.entity.MessageBO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/1/21.
 * 操作message表的类
 */
public class MessageDbDaoImpl implements MessageDbDao {


    private DBHelper messageDBHelper;

    public MessageDbDaoImpl(Context context) {
        messageDBHelper = new DBHelper(
                context,
                null,
                null
        );
    }

    /**
     * 向数据库的message表中插入一条记录
     *
     * @param messageBO 待保存的message数据
     */
    @Override
    public void addMessage(MessageBO messageBO) {
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();
        try {
            db.execSQL("insert into message(_id," +
                            "userId," +
                            "userName," +
                            "content," +
                            "time," +
                            "isDelete," +
                            "title," +
                            "image) values(?,?,?,?,?,?,?,?)",
                    new Object[]{
                            messageBO.getMsgId(),
                            messageBO.getUserId(),
                            messageBO.getMsgSender(),
                            messageBO.getMsgContent(),
                            messageBO.getMsgSendTime(),
                            messageBO.getIsDelete(),
                            messageBO.getTitle(),
                            messageBO.getImagePathListStr()
                    });
        } catch (SQLiteConstraintException e) {
            Log.e("SQLiteConstraintExcepti", "主键冲突");
        }

        db.close();
    }

    /**
     * 删除数据库中一条message记录
     *
     * @param id messageId
     */
    @Override
    public void deleteMessageById(int id) {
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();
        db.execSQL("delete from message where _id = ?", new Object[]{id});
        db.close();
    }

    /**
     * 获取所有message信息
     *
     * @return 返回message集合
     */
    @Override
    public List<MessageBO> getMessage() {
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from message", new String[]{});

        return convertToMessages(db, cursor);
    }

    @Override
    public List<MessageBO> getMessageInTheRangeOf(int startId, int endId) {
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();
        Log.e("messagetodown", "开始查询");
        Log.e("messagetodown", "startId:" + startId + "endId:" + endId);

        Cursor cursor = db.rawQuery("select * from message where _id between ? and ? order by _id",
                new String[]{String.valueOf(endId), String.valueOf(startId)});

        return convertToMessages(db, cursor);
    }

    @Override
    public List<MessageBO> getMessageToUpStartAtId(int id) {
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from message limit ? , ?",
                new String[]{String.valueOf(id), String.valueOf(20)});

        return convertToMessages(db, cursor);
    }

    @Override
    public int getMaxMessageId() {
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select _id from message",
                new String[]{});
        int maxId = 0;
        while (cursor.moveToNext()) {
            int messageId = cursor.getInt(0);
            if (messageId > maxId) {
                maxId = messageId;
            }

        }
        return maxId;
    }

    @Override
    public int getMinMessageId() {
        SQLiteDatabase db = messageDBHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select _id from message",
                new String[]{});
        int minId = 10000;
        while (cursor.moveToNext()) {
            int messageId = cursor.getInt(0);
            if (messageId < minId) {
                minId = messageId;
            }

        }
        return minId;
    }

    /**
     * 移动游标，逐条读出message数据
     *
     * @param db
     * @param cursor
     * @return
     */
    private List<MessageBO> convertToMessages(SQLiteDatabase db, Cursor cursor) {
        List<MessageBO> messageBOs = new ArrayList<>();
        Log.e("messagetodown", "convert");

        while (cursor.moveToNext()) {
            Log.e("messagetodown", "count");

            MessageBO messageBO = new MessageBO();
            messageBO.setMsgId(cursor.getInt(cursor.getColumnIndex("_id")));
            messageBO.setUserId(cursor.getInt(cursor.getColumnIndex("userId")));
            messageBO.setMsgSender(cursor.getString(cursor.getColumnIndex("userName")));
            messageBO.setIsDelete(cursor.getInt(cursor.getColumnIndex("isDelete")));
            messageBO.setImagePathListStr(cursor.getString(cursor.getColumnIndex("image")));
            messageBO.setMsgSendTime(cursor.getString(cursor.getColumnIndex("time")));
            messageBO.setMsgContent(cursor.getString(cursor.getColumnIndex("content")));

            messageBO.setTitle(cursor.getString(cursor.getColumnIndex("title")));

            messageBOs.add(messageBO);
        }
        db.close();
        Collections.reverse(messageBOs);
        return messageBOs;

    }
}
