package com.db.daoImpl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.db.dao.CourseDao;
import com.db.helper.DBHelper;
import com.md.entity.ClassSchedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/2/13.
 */
public class CourseDaoImpl implements CourseDao {

    private DBHelper courseDBHelper;

    public CourseDaoImpl(Context context) {
        courseDBHelper = new DBHelper(context,
                null,
                null);
    }

    @Override
    public void addCourse(ClassSchedule classSchedule) {

        SQLiteDatabase db = courseDBHelper.getWritableDatabase();
        db.execSQL("insert into course(" +
                        "trainingProgram," +
                        "courseNumber," +
                        "courseName," +
                        "courseSerialNumber," +
                        "courseCredit," +
                        "courseAttribute," +
                        "testType," +
                        "teacherName," +
                        "readMethod," +
                        "electiveState) values(?,?,?,?,?,?,?,?,?,?)",
                new Object[]{
                        classSchedule.getTrainingProgram(),
                        classSchedule.getCourseNumber(),
                        classSchedule.getCourseName(),
                        classSchedule.getCourseSerialNumber(),
                        classSchedule.getCourseCredit(),
                        classSchedule.getCourseAttribute(),
                        classSchedule.getTestType(),
                        classSchedule.getTeacherName(),
                        classSchedule.getReadMethod(),
                        classSchedule.getElectiveState()
                });
        db.close();
    }

    @Override
    public List<ClassSchedule> getCourse() {
        SQLiteDatabase db = courseDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from course", new String[]{});

        return convertToCourse(db, cursor);
    }

    @Override
    public List<ClassSchedule> getCourseByCourseNumber(String courseNumber) {
        SQLiteDatabase db = courseDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from course where courseNumber = ?", new String[]{courseNumber});

        return convertToCourse(db, cursor);
    }

    private List<ClassSchedule> convertToCourse(SQLiteDatabase db, Cursor cursor) {
        List<ClassSchedule> courses = new ArrayList<>();
        while (cursor.moveToNext()) {
            ClassSchedule classSchedule = new ClassSchedule();
            classSchedule.setTrainingProgram(cursor.getString(cursor.getColumnIndex("trainingProgram")));
            classSchedule.setCourseNumber(cursor.getString(cursor.getColumnIndex("courseNumber")));
            classSchedule.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
            classSchedule.setCourseSerialNumber(cursor.getString(cursor.getColumnIndex("courseSerialNumber")));
            classSchedule.setCourseCredit(cursor.getString(cursor.getColumnIndex("courseCredit")));
            classSchedule.setCourseAttribute(cursor.getString(cursor.getColumnIndex("courseAttribute")));
            classSchedule.setTestType(cursor.getString(cursor.getColumnIndex("testType")));
            classSchedule.setTeacherName(cursor.getString(cursor.getColumnIndex("teacherName")));
            classSchedule.setReadMethod(cursor.getString(cursor.getColumnIndex("readMethod")));
            classSchedule.setElectiveState(cursor.getString(cursor.getColumnIndex("electiveState")));
            courses.add(classSchedule);
        }
        cursor.close();
        db.close();
        return courses;
    }
}
