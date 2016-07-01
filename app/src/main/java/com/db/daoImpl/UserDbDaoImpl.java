package com.db.daoImpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.db.dao.UserDbDao;
import com.db.helper.DBHelper;
import com.md.entity.UserBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/1/21.
 */
public class UserDbDaoImpl implements UserDbDao {

    private DBHelper dbHelper;

    public UserDbDaoImpl(Context context) {
        dbHelper = new DBHelper(context,
                null,
                null
        );
    }

    /**
     * 向数据库user表中插入一条数据
     *
     * @param userBO 待保存的user信息
     */
    @Override
    public void addUser(UserBO userBO) {
        //获取可读的数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into user(_id," +
                        "name," +
                        "gender," +
                        "grade," +
                        "major," +
                        "minor," +
                        "signature," +
                        "userImage) values(?,?,?,?,?,?,?,?)",
                new Object[]{
                        userBO.getUserId(),
                        userBO.getName(),
                        userBO.getGender(),
                        userBO.getGrade(),
                        userBO.getMajor(),
                        userBO.getMinor(),
                        userBO.getSignature(),
                        userBO.getUserImage()
                });
        db.close();
    }

    /**
     * 删除一条user数据
     *
     * @param id userId
     */
    @Override
    public void deleteUserById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from user where _id = ?", new Object[]{id});
        db.close();
    }

    /**
     * 获取所有用户信息
     *
     * @return user集合
     */


    @Override
    public List<UserBO> getUser() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user", new String[]{});

        return convertToMessages(db, cursor);
    }

    @Override
    public UserBO getUserById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user where _id = ?", new String[]{String.valueOf(id)});

        UserBO userBO = null;
        List resultList = convertToMessages(db, cursor);

        if (resultList.size() != 0) {
            userBO = (UserBO) resultList.get(0);

        }
        return userBO;
    }

    private List<UserBO> convertToMessages(SQLiteDatabase db, Cursor cursor) {
        List<UserBO> userBOs = new ArrayList<>();
        while (cursor.moveToNext()) {
            UserBO userBO = new UserBO();
            userBO.setUserId(cursor.getInt(cursor.getColumnIndex("_id")));
            userBO.setName(cursor.getString(cursor.getColumnIndex("name")));
            userBO.setMajor(cursor.getString(cursor.getColumnIndex("major")));
            userBO.setMinor(cursor.getString(cursor.getColumnIndex("minor")));
            userBO.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
            userBO.setGrade(cursor.getInt(cursor.getColumnIndex("grade")));
            userBO.setSignature(cursor.getString(cursor.getColumnIndex("signature")));
            userBO.setUserImage(cursor.getString(cursor.getColumnIndex("userImage")));
            userBOs.add(userBO);

        }
        //最后记得要关闭数据库
        db.close();
        return userBOs;
    }
}
