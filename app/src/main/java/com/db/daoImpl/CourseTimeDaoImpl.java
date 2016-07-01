package com.db.daoImpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.db.dao.CourseTimeDao;
import com.db.helper.DBHelper;
import com.md.entity.SchoolTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/2/13.
 */
public class CourseTimeDaoImpl implements CourseTimeDao {

    private DBHelper courseTimeDBHelper;

    public CourseTimeDaoImpl(Context context) {

        courseTimeDBHelper = new DBHelper(context,
                null,
                null);
        Log.e("1111", "coursetime已创建");
    }

    @Override
    public void addCourseTime(SchoolTime schoolTime) {
        SQLiteDatabase db = courseTimeDBHelper.getWritableDatabase();
        db.execSQL("insert into coursetime(" +
                        "weekly," +
                        "week," +
                        "part," +
                        "partLength," +
                        "campus," +
                        "building," +
                        "classRoom," +
                        "courseNumber) values(?,?,?,?,?,?,?,?)",
                new Object[]{
                        schoolTime.getWeek_of_semester(),
                        schoolTime.getDay_of_week(),
                        schoolTime.getPart(),
                        schoolTime.getPartLength(),
                        schoolTime.getCampus(),
                        schoolTime.getBuilding(),
                        schoolTime.getClassRoom(),
                        schoolTime.getCourseNumber()
                });
        db.close();
    }

    @Override
    public List<SchoolTime> getCourseTime() {
        SQLiteDatabase db = courseTimeDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from coursetime", new String[]{});

        return convertToCourse(db, cursor);
    }

    @Override
    public List<SchoolTime> getCourseTimeByNumber(String courseNumber) {
        SQLiteDatabase db = courseTimeDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from coursetime where courseNumber = ?", new String[]{courseNumber});

        return convertToCourse(db, cursor);
    }

    @Override
    public List<SchoolTime> getCourseTimeByWeek(int week, int part) {
        SQLiteDatabase db = courseTimeDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from coursetime where week = ? and part = ?",
                new String[]{String.valueOf(week), String.valueOf(part)});

        return convertToCourse(db, cursor);
    }


    private List<SchoolTime> convertToCourse(SQLiteDatabase db, Cursor cursor) {
        List<SchoolTime> courseTimes = new ArrayList<>();
        while (cursor.moveToNext()) {
            SchoolTime schoolTime = new SchoolTime();
            schoolTime.setWeek_of_semester(cursor.getString(cursor.getColumnIndex("weekly")));
            schoolTime.setDay_of_week(cursor.getString(cursor.getColumnIndex("week")));
            schoolTime.setPart(cursor.getString(cursor.getColumnIndex("part")));
            schoolTime.setPartLength(cursor.getString(cursor.getColumnIndex("partLength")));
            schoolTime.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
            schoolTime.setBuilding(cursor.getString(cursor.getColumnIndex("building")));
            schoolTime.setClassRoom(cursor.getString(cursor.getColumnIndex("classRoom")));
            schoolTime.setCourseNumber(cursor.getString(cursor.getColumnIndex("courseNumber")));
            courseTimes.add(schoolTime);

        }
        cursor.close();
        db.close();
        return courseTimes;
    }
}
