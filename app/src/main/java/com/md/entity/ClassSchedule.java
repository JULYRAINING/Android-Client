package com.md.entity;

import java.util.List;


public class ClassSchedule {

    @Override
    public String toString() {
        return "ClassSchedule [trainingProgram=" + trainingProgram
                + ", courseNumber=" + courseNumber + ", courseName="
                + courseName + ", courseSerialNumber=" + courseSerialNumber
                + ", courseCredit=" + courseCredit + ", courseAttribute="
                + courseAttribute + ", testType=" + testType + ", teacherName="
                + teacherName + ", readMethod=" + readMethod
                + ", electiveState=" + electiveState + ", schoolTimes="
                + schoolTimes + "]";
    }

    // 培养方案
    private String trainingProgram;
    // 课程号
    private String courseNumber;
    // 课程名
    private String courseName;
    // 课序号
    private String courseSerialNumber;
    // 学分
    private String courseCredit;
    // 课程属性
    private String courseAttribute;
    // 考试类型
    private String testType;
    // 教师姓名
    private String teacherName;
    // 修读方式
    private String readMethod;
    // 选课状态
    private String electiveState;

    private List<SchoolTime> schoolTimes;

    public String getTrainingProgram() {
        return trainingProgram;
    }

    public void setTrainingProgram(String trainingProgram) {
        this.trainingProgram = trainingProgram;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseSerialNumber() {
        return courseSerialNumber;
    }

    public void setCourseSerialNumber(String courseSerialNumber) {
        this.courseSerialNumber = courseSerialNumber;
    }

    public String getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(String courseCredit) {
        this.courseCredit = courseCredit;
    }

    public String getCourseAttribute() {
        return courseAttribute;
    }

    public void setCourseAttribute(String courseAttribute) {
        this.courseAttribute = courseAttribute;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getReadMethod() {
        return readMethod;
    }

    public void setReadMethod(String readMethod) {
        this.readMethod = readMethod;
    }

    public String getElectiveState() {
        return electiveState;
    }

    public void setElectiveState(String electiveState) {
        this.electiveState = electiveState;
    }

    public List<SchoolTime> getSchoolTimes() {
        return schoolTimes;
    }

    public void setSchoolTimes(List<SchoolTime> schoolTimes) {
        this.schoolTimes = schoolTimes;
    }


}
