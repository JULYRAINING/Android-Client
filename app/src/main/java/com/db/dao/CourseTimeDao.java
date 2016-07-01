package com.db.dao;


import com.md.entity.SchoolTime;

import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/2/13.
 */
public interface CourseTimeDao {
    void addCourseTime(SchoolTime schoolTime);

    List<SchoolTime> getCourseTime();

    List<SchoolTime> getCourseTimeByNumber(String courseNumber);

    List<SchoolTime> getCourseTimeByWeek(int week, int part);
}
