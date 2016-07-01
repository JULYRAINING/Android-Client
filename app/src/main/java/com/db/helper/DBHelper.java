package com.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SECONDHEAVEN on 2016/1/21.
 * SQLite帮助类
 * 用来实例化数据库
 */
public class DBHelper extends SQLiteOpenHelper {

    //建立数据库表项的语句
    private String dbCreate;
    //数据库名称
    private static final String dbName = "cupDB";
    //删除数据库表项的语句
    private String dbDrop;
    //数据库版本
    private static final int dbVersion = 1;

    //删除数据库表项message的语句
    private static final String DB_MESSAGE_DROP = "drop table if exists message";

    private static final String DB_DROP_COURSE = "drop table if exists course";
    private static final String DB_DROP_COURSETIME = "drop table if exists coursetime";
    //建立数据库表项message的语句
    private static final String DB_MESSAGE_CREATE =
            "create table message(_id integer primary key autoincrement," +
                    "userId integer," +
                    "userName text," +
                    "content text," +
                    "time text," +
                    "isDelete," +
                    "title text," +
                    "image text)";
    //删除数据库表项USER的语句
    private static final String DB_USER_DROP = "drop table if exists user";
    //建立数据库表项USER的语句
    private static final String DB_USER_CREATE =
            "create table user(_id integer primary key autoincrement," +
                    "name text," +
                    "gender integer," +
                    "grade text," +
                    "major text," +
                    "minor text," +
                    "signature text," +
                    "userImage text)";
    private static final String CREATE_COURSE = "create table course(_id integer primary key autoincrement," +
            "trainingProgram text," +
            "courseNumber text," +
            "courseName text," +
            "courseSerialNumber text," +
            "courseCredit text," +
            "courseAttribute text," +
            "testType text," +
            "teacherName text," +
            "readMethod text," +
            "electiveState text)";
    private static final String CREATE_COURSETIME = "create table coursetime(_id integer primary key autoincrement," +
            "weekly text," +
            "week text," +
            "part text," +
            "partLength text," +
            "campus text," +
            "building text," +
            "classRoom text," +
            "courseNumber text)";

    public DBHelper(Context context, String create, String drop) {
        super(context, dbName, null, dbVersion);


    }


    /**
     * 一般只在建立数据库时调用一次
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_MESSAGE_CREATE);
        db.execSQL(DB_USER_CREATE);
        db.execSQL(CREATE_COURSE);
        db.execSQL(CREATE_COURSETIME);
    }

    /**
     * 当数据库版本发生变化时被调用
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB_MESSAGE_DROP);
        db.execSQL(DB_USER_DROP);
        db.execSQL(DB_DROP_COURSE);
        db.execSQL(DB_DROP_COURSETIME);
    }
}
