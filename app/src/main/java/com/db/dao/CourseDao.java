package com.db.dao;


import com.md.entity.ClassSchedule;

import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/2/13.
 */
public interface CourseDao {
    public void addCourse(ClassSchedule classSchedule);

    List<ClassSchedule> getCourse();

    List<ClassSchedule> getCourseByCourseNumber(String courseNumber);
}
